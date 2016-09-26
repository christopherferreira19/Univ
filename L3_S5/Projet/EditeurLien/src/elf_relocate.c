#include "msg.h"
#include "endianess.h"
#include "elf_read.h"
#include "elf_write.h"
#include "elf_relocate_arm.h"
#include "elf_relocate.h"

// Structure keeping necessary information for most of the relocation process steps
struct Relocation {
    Elf_s src;
    Elf_s dst;

    // Mapping between the sections and symbols indexes from the source file
    // to the destination file because some symbols and sections won't be kept
    // in the executable format :
    // * A section (resp. symbol) at index i in the source file will be located
    //   at index sections_mapping[i] (resp. symbols_mapping[i]) in the
    //   destination file
    // * Or sections_mapping[i] (resp. symbols_mapping[i]) == -1 if the section
    //   (resp. symbol) is discarded
    int* sections_mapping;
    int* symbols_mapping;
};

typedef struct Relocation* Relocation;

Relocation relocation_alloc(char* filename_src, char* filename_dst);
void relocation_free(Relocation rel);
bool discard_section(Elf32_Shdr* section_header);
bool copy_header(Relocation rel);
bool copy_update_sections(Relocation rel);
bool relocate_sections(Relocation rel,
        int arg_size,
        Arg_Addr* args);
bool check_overlap(Elf_s elf);
bool copy_update_symbols(Relocation rel);
bool init_entry_point(Elf_s elf, char* entry_point);
bool relocate_instructions(Relocation rel);

Elf_s elf_relocate(char* filename_src, char* filename_dst,
        int arg_size,
        Arg_Addr* args,
        char *entry_point) {
    Relocation rel = relocation_alloc(filename_src, filename_dst);
    if (rel == NULL) {
        return NULL;
    }

	for(int i = 0; i < arg_size; i++) {
		Elf32_Shdr* section_header = elf_section_find_by_name(rel->src, args[i].section);
		if (section_header == NULL) {
			warn("IGNORE: Section %s does not exist\n",args[i].section);
		} else if(discard_section(section_header)) {
			warn("IGNORE: Section %s is not kept for relocation\n",args[i].section);
		}
	}

    bool ok = true;
    ok = ok && copy_header(rel);
    ok = ok && copy_update_sections(rel);
    ok = ok && relocate_sections(rel, arg_size, args);
    ok = ok && !check_overlap(rel->dst);
    ok = ok && copy_update_symbols(rel);
    ok = ok && init_entry_point(rel->dst,entry_point);
    ok = ok && elf_write(rel->src, rel->dst, filename_dst, rel->sections_mapping);
    ok = ok && relocate_instructions(rel);

    FILE* dst_file = rel->dst->file;
    fflush(dst_file);

    Elf_s dst = rel->dst;
    relocation_free(rel);
    if (!ok) {
        return NULL;
    }
    else {
        return dst;
    }
}

// First step of the relocation :
//   * Allocating the memory necessary for the whole process
//   * Reading the src file
Relocation relocation_alloc(char* filename_src, char* filename_dst) {
    Elf_s src = elf_read(filename_src);
    if (src == NULL) {
        return NULL;
    }

    if (src->header->e_type != ET_REL) {
        elf_free(src);
        err("Le fichier ELF n'est pas un fichier REL\n");
        return NULL;
    }

    Elf_s dst = malloc(sizeof(struct Elf_s));
    if (dst == NULL) {
        elf_free(src);
        return NULL;
    }

    dst->header = NULL;
    dst->sections_headers = NULL;
    dst->sections_name = NULL;
    dst->symbols_header = NULL;
    dst->symbols = NULL;
    dst->symbols_name = NULL;

    Relocation rel = malloc(sizeof(struct Relocation));
    if (rel == NULL) {
        elf_free(src);
        elf_free(dst);
        return NULL;
    }

    rel->src = src;
    rel->dst = dst;

    return rel;
}

void relocation_free(Relocation rel) {
    free(rel->symbols_mapping);
    free(rel->sections_mapping);
    elf_free(rel->src);
    free(rel);
}

bool copy_header(Relocation rel) {
    Elf32_Ehdr* header = malloc(sizeof(Elf32_Ehdr));
    if (header == NULL) {
        return false;
    }

    memcpy(header, rel->src->header, sizeof(Elf32_Ehdr));
    header->e_type = ET_EXEC;
    rel->dst->header = header;
    return true;
}

bool alloc_sections_headers(Relocation rel,
        int sections_number,
        int sections_names_section_index,
        int sections_names_section_size) {
    Elf_s dst = rel->dst;
    // Initialisation de la taille et allocations de la mémoire pour
    // les sections headers et leur table des chaînes de caractères
    dst->header->e_shnum = sections_number;
    dst->sections_headers = malloc(sections_number * sizeof(Elf32_Shdr));
    dst->header->e_shstrndx = sections_names_section_index;
    dst->sections_name = malloc(sections_names_section_size * sizeof(char));
    return true;
}

// Define if section is a Relocation Section
// Return TRUE if section is useless
bool discard_section(Elf32_Shdr* section_header) {
	return (section_header->sh_size == 0
			&& !elf_section_is_null(section_header))
			|| elf_section_is_relocations(section_header);
}

// Iterate on source file sections headers in order to
// count how many are kept and the mapping
// between source and destination indexes
bool sections_mappings(Relocation rel, int* shstrndx_size) {
    int shnum_src = rel->src->header->e_shnum;
    rel->sections_mapping = malloc(shnum_src * sizeof(int));

    int shnum_dst = 0;
    int shstrndx_size_dst = 0;
    Elf32_Shdr* section_header_src = rel->src->sections_headers;
    for (int i = 0; i < shnum_src; i++, section_header_src++) {
        if (discard_section(section_header_src)) {
            rel->sections_mapping[i] = -1;
        }
        else {
            rel->sections_mapping[i] = shnum_dst++;
            char* section_name = elf_section_name(rel->src, section_header_src);
            shstrndx_size_dst += strlen(section_name) + 1; // +1 NULL Character '\0'
        }
    }

    *shstrndx_size = shstrndx_size_dst;
    return alloc_sections_headers(rel,
            shnum_dst,
            rel->sections_mapping[rel->src->header->e_shstrndx],
            shstrndx_size_dst);
}

// Copy the sections headers which are kept (and their name) in the destination
// Also update any indexed cross-reference to another section header
// with the new "mapped" index
bool copy_update_sections(Relocation rel) {
    int shstrndx_size_dst;
    sections_mappings(rel, &shstrndx_size_dst);

    Elf_s src = rel->src;
    Elf_s dst = rel->dst;

    int shstrndx_dst = 0;
    Elf32_Shdr* section_header_src = src->sections_headers;
    Elf32_Shdr* section_header_dst = dst->sections_headers;
    for (int i = 0; i < src->header->e_shnum; i++, section_header_src++) {
        if (rel->sections_mapping[i] != -1) {
            memcpy(section_header_dst, section_header_src, sizeof(Elf32_Shdr));
            section_header_dst->sh_link = rel->sections_mapping[section_header_dst->sh_link];

            char* section_name = elf_section_name(src, section_header_src);
            int section_name_size = strlen(section_name) + 1; // +1 NULL Character '\0'
            memcpy(dst->sections_name + shstrndx_dst, section_name, section_name_size * sizeof(char));
            section_header_dst->sh_name = shstrndx_dst;
            shstrndx_dst += section_name_size;

            section_header_dst++;
        }
    }

    dst->sections_headers[dst->header->e_shstrndx].sh_size = shstrndx_size_dst;
    dst->symbols_header = elf_symbol_find_header(dst);

    return true;
}

// Function to find the max address passed in arguments
// also returning the name of the section with the max address to avoid collusion
int first_free_addr(Elf_s elf, Arg_Addr *args, int num_of_args) {
    Arg_Addr* arg = args;
    int first_free_addr = 0x0;
    for (int i = 0; i < num_of_args; i++, arg++) {
        Elf32_Shdr* section = elf_section_find_by_name(elf, arg->section);
        if (section == NULL) {
            continue;
        }

        int addr_after = arg->addr + section->sh_size;
        if (addr_after > first_free_addr){
            first_free_addr = addr_after;
        }
    }

    return first_free_addr;
}

// This function will first assign adresses for the sections specified in the arguments
// i.e "-s .text=0x58" for example
// then it will shift the other sections with the flag "SHF_ALLOC"
// starting with the first free address in memory (calculated with the function
// "first_free_addr") taking into account the alignement of each section

// Shifting addresses is not in the DOCs, but it's an observation made with several examples
// compiled by (arm-eabi-ld)
bool relocate_sections(Relocation rel,
        int arg_size,
        Arg_Addr* args) {

    int free_addr = first_free_addr(rel->dst,args, arg_size);
    Elf32_Shdr* section_header = rel->dst->sections_headers;
    for (int i = 1; i < rel->dst->header->e_shnum; i++, section_header++) {
        if (!elf_section_is_alloc(section_header)) {
            continue;
        }

        char* section_name = elf_section_name(rel->dst, section_header);
        int arg_addr = find_section_addr(section_name, arg_size, args);
        if (arg_addr != -1) {
            // Use the address given by the user as an argument for this section
            section_header->sh_addr = arg_addr;
        }
        else {
            // Padding if necessary following the section alignment constraint
            int alignment = section_header->sh_addralign;
            int alignment_surplus = free_addr % alignment;
            if (alignment_surplus > 0) {
                free_addr -= alignment_surplus;
                free_addr += alignment;
            }

            // Use the next free adress for this section
            warn("No address specified for section %s", section_name);
            warn(", using next available address : 0x%.8X\n", free_addr)
            section_header->sh_addr = free_addr;
            free_addr += section_header->sh_size;
        }
    }

    return true;
}

// This function is going to check if there is an overlap in the sections specified in arguments
// Example of overlap : arm-eabi-ld --section-start .text=0x58 --section-start .data=60
// An overlap will occur if the size of the section ".text" is bigger than 2 bytes
bool check_overlap(Elf_s elf) {
    int num_of_sections = elf->header->e_shnum;
    for (int i = 0; i < num_of_sections - 1; i++) {
        Elf32_Shdr* section_i = elf_section_header_at(elf, i);
        if (!elf_section_is_alloc(section_i))
            continue;

        for (int j = i + 1; j < num_of_sections; j++) {
            Elf32_Shdr* section_j = elf_section_header_at(elf, j);
            if (!elf_section_is_alloc(section_j))
                continue;

            int section_i_from = section_i->sh_addr;
            int section_i_to = section_i_from + section_i->sh_size;
            int section_j_from = section_j->sh_addr;
            int section_j_to = section_j_from + section_j->sh_size;
            if ((section_i_from > section_j_to) != (section_i_to > section_j_from)) {
                err("Section %s loaded at [%.8X , %.8X] ",
                        elf_section_name(elf, section_i),
                        section_i_from,
                        section_i_to);
                err("overlaps section %s loaded at [%.8X , %.8X]\n",
                        elf_section_name(elf, section_j),
                        section_j_from,
                        section_j_to);
                return true;
            }
        }
    }

    return false;
}

// Iterate on source file symbols in order to
// count how many are kept and the mapping
// between source and destination indexes
bool symbols_mapping(Relocation rel) {
    Elf_s dst = rel->dst;
    int symbols_number_src = elf_section_entries_number(dst->symbols_header);
    rel->symbols_mapping = malloc(symbols_number_src * sizeof(int));
    Elf32_Sym* symbol_src = rel->src->symbols;
    int symbols_number_dst = 0;
    int symbols_strtab_size_dst = 0;
    int first_non_local_symbol_index = 0;

    for (int i = 0; i < symbols_number_src; i++, symbol_src++) {
        if (rel->sections_mapping[symbol_src->st_shndx] == -1) {
            // Discard the symbol because the corresponding section is also discarded
            rel->symbols_mapping[i] = -1;
        }
        else {
            rel->symbols_mapping[i] = symbols_number_dst;
            if (!first_non_local_symbol_index && ELF32_ST_BIND(symbol_src->st_info) != STB_LOCAL) {
                first_non_local_symbol_index = symbols_number_dst;
            }

            symbols_number_dst++;
            char* symbol_name = elf_symbol_name(rel->src, symbol_src);
            symbols_strtab_size_dst += strlen(symbol_name) + 1; // +1 NULL Character '\0'
        }
    }

    dst->symbols_header->sh_size = symbols_number_dst * dst->symbols_header->sh_entsize;
    dst->symbols_header->sh_info = first_non_local_symbol_index;
    dst->symbols = malloc(symbols_number_dst * sizeof(Elf32_Sym));
    dst->symbols_name = malloc(symbols_strtab_size_dst * sizeof(char));
    dst->sections_headers[dst->symbols_header->sh_link].sh_size = symbols_strtab_size_dst;

    return true;
}

// Copy the symbols which are kept (and their name) in the destination
// Also :
//   * Update any indexed reference to a section header with the new
//     "mapped" index
//   * Update its address according to its section new address
bool copy_update_symbols(Relocation rel) {
    symbols_mapping(rel);

    int symbols_number_src = elf_section_entries_number(rel->src->symbols_header);
    Elf32_Sym* symbol_src = rel->src->symbols;
    Elf32_Sym* symbol_dst = rel->dst->symbols;
    int symbol_index_dst = 0;

    for (int i = 0; i < symbols_number_src; i++, symbol_src++) {
        if (rel->sections_mapping[symbol_src->st_shndx] != -1) {
            // Copy and update the symbol
            memcpy(symbol_dst, symbol_src, sizeof(Elf32_Sym));
            symbol_dst->st_shndx = rel->sections_mapping[symbol_dst->st_shndx];
            symbol_dst->st_value += elf_section_header_at(rel->dst, symbol_dst->st_shndx)->sh_addr;

            // Take care of the name of the symbol
            char* symbol_name = elf_symbol_name(rel->src, symbol_src);
            int symbol_name_size = strlen(symbol_name) + 1; // +1 NULL Character '\0'
            memcpy(rel->dst->symbols_name + symbol_index_dst, symbol_name, symbol_name_size * sizeof(char));
            symbol_dst->st_name = symbol_index_dst;
            symbol_index_dst += symbol_name_size;

            symbol_dst++;
        }
    }

    return true;
}

// Setting the address of "entry point"
// and the header's "flags" accordingly
bool init_entry_point(Elf_s elf, char* entry_point) {
    Elf32_Sym* symbol  = elf_symbol_find_by_name(elf, entry_point);
    if (symbol == NULL) {
        err("No symbol for entry point %s", entry_point)
        return false;
    }

    elf->header->e_entry = symbol->st_value;
    elf->header->e_flags |= SHF_ENTRY_POINT;
    return true;
}

// Iterate over the relocation table and apply the
// relocation accordingly
bool relocate_instructions(Relocation rel) {
    Elf32_Shdr* section_header_src = rel->src->sections_headers;

    bool ok = true;

    for (int i = 0; i < rel->src->header->e_shnum; i++) {
        if (elf_section_is_relocations(section_header_src)) {
            int relocations_number = elf_section_entries_number(section_header_src);
            Elf32_Rel* relocations = elf_read_relocations(rel->src, section_header_src);

            // Get the section header on which this relocation apply
            int section_index = rel->sections_mapping[section_header_src->sh_info];
            Elf32_Shdr* section_header = elf_section_header_at(rel->dst, section_index);

            Elf32_Rel* relocation = relocations;
            for (int j = 0; j < relocations_number; j++, relocation++) {
                // Get the symbol on which this relocation is based
                int symbol_index = rel->symbols_mapping[ELF32_R_SYM(relocation->r_info)];
                if (symbol_index == -1) {
                    return false;
                }
                Elf32_Sym* symbol = rel->dst->symbols + symbol_index;

                ok = ok && elf_relocate_arm(rel->dst, section_header, symbol, relocation);
            }

            free(relocations);
        }

        section_header_src++;
    }

    return ok;
}
