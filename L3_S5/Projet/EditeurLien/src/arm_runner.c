#include <getopt.h>
#include <stdio.h>
#include <stdlib.h>
#include <debug.h>
#include <arm_simulator_interface.h>
#include "endianess.h"
#include "elf_common.h"
#include "elf_read.h"

void usage(char *name) {
	fprintf(stderr, "Usage:\n"
		"  %s [ --help ] [ --host hostname ] [ --service servicename ] [ --debug file ] file\n\n"
		"Loads a sample ARM code to a remote simulator. The --debug flag enables the output produced by "
		"calls to the debug function in the named source file.\n"
		, name);
}

void run(char *hostname, char *servicename, char* filename) {
	arm_simulator_data_t simulator = arm_connect(hostname, servicename);

	Elf_s elf = elf_read(filename);
	for (int i = 0; i < elf->header->e_shnum; i++) {
        Elf32_Shdr* section_header = elf_section_header_at(elf, i);
        if (!elf_section_is_alloc(section_header)) {
            continue;
        }

		uint32_t* code;

        if (elf_section_is_nobits(section_header)) {
        	code = (uint32_t*) malloc(sizeof(uint8_t) * section_header->sh_size);
        } else {
        	code = (uint32_t*) elf_read_raw_section(elf, section_header);
        }

		arm_write_memory(simulator, section_header->sh_addr, code, section_header->sh_size);
    }

    uint32_t entry = elf->header->e_entry;
    force_endianess(&entry, sizeof(entry), false);
	elf_free(elf);

	arm_write_register(simulator, 15, entry);
	arm_run(simulator);

	return;
}

int main(int argc, char *argv[]) {
	int opt;
	char *hostname, *servicename;

	struct option longopts[] = {
		{ "debug", required_argument, NULL, 'd' },
		{ "host", required_argument, NULL, 'H' },
		{ "service", required_argument, NULL, 'S' },
		{ "help", no_argument, NULL, 'h' },
		{ NULL, 0, NULL, 0 }
	};

	hostname = NULL;
	servicename = NULL;
	while ((opt = getopt_long(argc, argv, "S:H:d:h", longopts, NULL)) != -1) {
		switch(opt) {
		case 'H':
			hostname = optarg;
			break;
		case 'S':
			servicename = optarg;
			break;
		case 'h':
			usage(argv[0]);
			exit(0);
		case 'd':
			add_debug_to(optarg);
			break;
		default:
			fprintf(stderr, "Unrecognized option %c\n", opt);
			usage(argv[0]);
			exit(1);
		}
	}

	if (optind + 1 != argc) {
		fprintf(stderr, "Expected exactly one argument\n");
		usage(argv[0]);
		exit(1);
	}

	run(hostname, servicename, argv[optind]);
	return 0;
}
