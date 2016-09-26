#include <stdlib.h>
#include <stdio.h>
#include <stdint.h>
#include <string.h>
#include <stdbool.h>
#include "elf_read.h"
#include "elf_print.h"

bool gIsFirst = true; // Variable Globale : Permet un affichage propre de 'Relocation Table'

void elf_print(Elf_s elf) {
    elf_print_header(elf);
    elf_print_section_header(elf);
    elf_print_symbols(elf);

    elf_print_relocation_header();
    Elf32_Shdr* section_header = elf->sections_headers;
    for (int i = 0; i < elf->header->e_shnum; i++, section_header++) {
        if (elf_section_is_relocations(section_header)) {
            elf_print_relocations(elf, section_header);
        }
    }
	elf_print_relocation_footer();
}

void elf_print_header(Elf_s elf) {
    Elf32_Ehdr header = *elf->header;

    printf("╔═════════════════════════════════════════════════════════════╗\n");
    printf("║                         \033[32;1mELF  HEADER\033[0m                         ║\n");
    printf("╠═══════════╤═════════════════════════════════════════════════╣\n");
// MAGIC KEY
    printf("║ \033[1mMagic Key\033[0m │ ");
    for (int i=0; i < 16; i++) {
        printf("%.2X ", header.e_ident[i]);
    }
    printf("║\n╠═══════════╧══════════╤══════════════════════════════════════╣\n");
// CLASS
    if (header.e_ident[EI_CLASS] == ELFCLASS32) {
        printf("║ Class                │ ELF32                                ║\n");
    } else if (header.e_ident[EI_CLASS] == ELFCLASS64) {
        printf("║ Class                │ ELF64                                ║\n");
    } else {
        printf("║ Class                │ Invalid class                        ║\n");
    }
// DATA
    if (header.e_ident[EI_DATA] == ELFDATA2LSB) {
        printf("║ Data                 │ Little Endian                        ║\n");
    } else if (header.e_ident[EI_DATA] == ELFDATA2MSB) {
        printf("║ Data                 │ Big Endian                           ║\n");
    } else {
        printf("║ Data                 │ Invalid Data encoding                ║\n");
    }
// VERSION
    if (header.e_ident[EI_VERSION] == EV_CURRENT) {
        printf("║ MK-Version           │ %-*d (Current)                         ║\n", 2, header.e_ident[EI_VERSION]);
    } else if (header.e_ident[EI_VERSION] == EV_NONE) {
        printf("║ MK-Version           │ Invalid Version                      ║\n");
    } else {
        printf("║ MK-Version           │ %-*d (Old)                             ║\n", 2, header.e_ident[EI_VERSION]);
    }
// OS/ABI
    if (header.e_ident[EI_OSABI] == ELFOSABI_NONE
            || header.e_ident[EI_OSABI] == ELFOSABI_SYSV) {
        printf("║ OS/ABI               │ UNIX - System V                      ║\n");
    } else {
        printf("║ OS/ABI               │ %-*d                                   ║\n", 2, header.e_ident[EI_OSABI]);
    }
// ABI VERSION
    printf("║ ABI Version          │ %-*d                                   ║\n", 2, header.e_ident[EI_ABIVERSION]);
// TYPE
    switch (header.e_type) {
        case ET_NONE:
            printf("║ Type                 │ No File Type                         ║\n");
            break;
        case ET_REL:
            printf("║ Type                 │ REL (Relocatable File)               ║\n");
            break;
        case ET_EXEC:
            printf("║ Type                 │ EXEC (Executable File)               ║\n");
            break;
        case ET_DYN:
            printf("║ Type                 │ DYN (Shared Object File)             ║\n");
            break;
        case ET_CORE:
            printf("║ Type                 │ CORE (Core File)                     ║\n");
            break;
        default:
            if (header.e_type >= 0xff00) {
                printf("║ Type                 │ PROC (Processor Specific)            ║\n");
            }
    }
// MACHINE
    if (header.e_machine == EM_ARM) {
        printf("║ Machine              │ ARM                                  ║\n");
    }
    else {
        printf("║ Machine              │ \033[33m%.4X\033[0m                                 ║\n", header.e_machine);
    }
// VERSION
    if (header.e_version == EV_CURRENT) {
        printf("║ Version              │ %-*d (Current)                         ║\n", 2,header.e_version);
    }
    else if (header.e_version == EV_NONE) {
        printf("║ Version              │ Invalid Version                      ║\n");
    }
    else {
        printf("║ Version              │ %-*d (Old)                             ║\n", 2,header.e_version);
    }
// EPA
    printf("║ Entry Point Adress   │ 0x%.8X                           ║\n", header.e_entry);
// SPH
    printf("║ Start Program Header │ 0x%.8X                           ║\n", header.e_phoff);
// SSH
    printf("║ Start Section Header │ 0x%.8X                           ║\n", header.e_shoff);
// FLAGS
    printf("║ Flags                │ 0x%.8X                           ║\n", header.e_flags);
// HS
    printf("║ Header Size          │ %-*d                                 ║\n", 4,header.e_ehsize);
// PHS
    printf("║ Program Header Size  │ %-*d                                 ║\n", 4,header.e_phentsize);
// PHC
    printf("║ Program Header Count │ %-*d                                 ║\n", 4,header.e_phnum);
// SHS
	printf("║ Section Header Size  │ %-*d                                 ║\n", 4,header.e_shentsize);
// SHC
    printf("║ Section Header Count │ %-*d                                 ║\n", 4,header.e_shnum);
// SHSTI
    printf("║ Section Header STI   │ %-*d                                 ║\n", 4,header.e_shstrndx);

    printf("╚══════════════════════╧══════════════════════════════════════╝\n");
}

void elf_print_section_header(Elf_s elf) {
    Elf32_Shdr* section_header = elf->sections_headers;

    printf("╔═══════════════════════════════════════════════════════════════════════════════════════════════════╗\n");
    printf("║                                           \033[32;1mSECTION TABLE \033[0m                                          ║\n");
    printf("╠════╤════════════════╤════════════════╤══════════╤══════════╤══════╤════╤══════╤══════╤══════╤═════╣\n");
    printf("║ ID │      Name      │      Type      │   Addr   │  Offset  │ Size │ ES │ Flag │ Link │ Info │ AAl ║\n");
    printf("╠════╪════════════════╪════════════════╪══════════╪══════════╪══════╪════╪══════╪══════╪══════╪═════╣\n");

	for (int i =0; i < elf->header->e_shnum; i++, section_header++) {
// ID
        printf("║ %-*d │", 2, i);
// NAME
		printf("  %-12.12s  │", elf_section_name(elf, section_header));
// TYPE
        switch (section_header->sh_type) {
			case SHT_NULL:
				printf("      NULL      │");
				break;
			case SHT_PROGBITS:
				printf("    PROGBITS    │");
				break;
			case SHT_SYMTAB:
				printf("     SYMTAB     │");
				break;
			case SHT_STRTAB:
				printf("     STRTAB     │");
				break;
			case SHT_RELA:
				printf("      RELA      │");
				break;
			case SHT_HASH:
				printf("      HASH      │");
				break;
			case SHT_DYNAMIC:
				printf("     DYNAMIC    │");
				break;
			case SHT_NOTE:
				printf("      NOTE      │");
				break;
			case SHT_NOBITS:
				printf("     NOBITS     │");
				break;
			case SHT_REL:
				printf("       REL      │");
				break;
			case SHT_SHLIB:
				printf("     SHLIB      │");
				break;
			case SHT_DYNSYM:
				printf("     DYNSIM     │");
				break;
            case 0x70000003:
                printf("    ARM_ATTR    │");
                break;
            default:
                if (section_header->sh_type >= SHT_LOPROC
                    && section_header->sh_type <= SHT_HIPROC) {
                    printf("      PROC      │");
                }
                else if (section_header->sh_type >= SHT_LOUSER
                    && section_header->sh_type <= SHT_HIUSER) {
                    printf("      USER      │");
                }
                else {
                    printf("    \033[33m%.8X\033[0m    │",(section_header+i)->sh_type);
                }
				break;
		}
// ADDR
        printf(" %.8X │", section_header->sh_addr);
// OFF
        printf(" %.8X │", section_header->sh_offset);
// Size
        printf(" %-*d │", 4, section_header->sh_size);
// ES
        printf(" %-*d │", 2, section_header->sh_entsize);
// FLAGS
        switch (section_header->sh_flags) {
            case 0: printf("      │"); break;
            case 1: printf("   W  │"); break;
            case 2: printf("   A  │"); break;
            case 3: printf("  AW  │"); break;
            case 4: printf("   X  │"); break;
            case 5: printf("  XW  │"); break;
            case 6: printf("  XA  │"); break;
            case 7: printf(" XAW  │"); break;
        }
// LINK
        printf("  %-*d  │", 2, section_header->sh_link);
// INFO
        printf("  %-*d  │", 2, section_header->sh_info);
// AL
        printf(" %-*d  ║\n", 2, section_header->sh_addralign);
    }

    printf("╚════╧════════════════╧════════════════╧══════════╧══════════╧══════╧════╧══════╧══════╧══════╧═════╝\n");

}

void elf_print_symbols(Elf_s elf) {
    Elf32_Sym* symbol = elf->symbols;
    int symbols_num = elf_section_entries_number(elf->symbols_header);

    printf("╔═════════════════════════════════════════════════════════════════════╗\n");
    printf("║                            \033[32;1mSYMBOL  TABLE\033[0m                            ║\n");
    printf("╠════╤══════════╤══════╤══════════╤════════╤═══════╤═════╤════════════╣\n");
    printf("║ ID │   Value  │ Size │   Type   │  Bind  │  Vis  │ NDX │    Name    ║\n");
    printf("╠════╪══════════╪══════╪══════════╪════════╪═══════╪═════╪════════════╣\n");

    for (int i = 0; i < symbols_num; i++, symbol++) {
// ID
        printf("║ %-*d │",2,i);
// Value
        printf(" %.8X │", symbol->st_value);
// Size
        printf(" %-*d │",4,symbol->st_size);
// Type
        switch(ELF32_ST_TYPE(symbol->st_info)) {
            case STT_NOTYPE:
                printf("  NOTYPE  │");
                break;
            case STT_OBJECT:
                printf("  OBJECT  │");
                break;
            case STT_FUNC:
                printf("   FUNC   │");
                break;
            case STT_SECTION:
                printf(" SECTION  │");
                break;
            case STT_FILE:
                printf("   FILE   │");
                break;
            default: // >= STT_LOPROC && <= STT_HIPROC
                printf("    PROC  │");
                break;
        }
// Bind
        switch(ELF32_ST_BIND(symbol->st_info)) {
            case STB_LOCAL:
                printf("  LOCAL │");
                break;
            case STB_GLOBAL:
                printf(" GLOBAL │");
                break;
            case STB_WEAK:
                printf("  WEAK  │");
                break;
            default: // >= STT_LOPROC && <= STT_HIPROC
                printf("   PROC │");
                break;
        }
// Vis
        switch(symbol->st_other) {
            case STV_DEFAULT:
                printf(" DEFLT │");
                break;
            case STV_INTERNAL:
                printf(" INTRN │");
                break;
            case STV_HIDDEN:
                printf(" HIDDN │");
                break;
            case STV_PROTECTED:
                printf(" PROTC │");
                break;
            case 4:
                printf(" EXPRT │");
                break;
            case 5:
                printf(" SINGL │");
                break;
            case 6:
                printf(" ELIMN │");
                break;
        }
// NDX
        if(symbol->st_shndx == 0) {printf(" UND │");}
        else {printf(" %-*d │", 3,symbol->st_shndx);}
// Name
        printf(" %-10.10s ║\n", ELF32_ST_TYPE(symbol->st_info) == STT_SECTION
                ? elf_section_name(elf, elf->sections_headers + symbol->st_shndx)
                : elf_symbol_name(elf, symbol));
    }
    printf("╚════╧══════════╧══════╧══════════╧════════╧═══════╧═════╧════════════╝\n");
}

void elf_print_relocation_header() {
    printf("╔═══════════════════════════════════════════════════════════════════╗\n");
    printf("║                         \033[32;1mRELOCATION  TABLE\033[0m                         ║\n");
    printf("╠═══════════════════════════════════════════════════════════════════╣\n");
}

void elf_print_relocations(Elf_s elf, Elf32_Shdr* section_header) {
    int number = elf_section_entries_number(section_header);
    Elf32_Rel* relocation = elf_read_relocations(elf, section_header);

	if (gIsFirst) {
		gIsFirst = false;
	} else {
		printf("╠═══════════════════════════════════════════════════════════════════╣\n");
	}

    printf("║                  RELOCATION (%-*d entries)                          ║\n", 2, number);
    printf("║\033[30m───────────────────────────────────┬───────────────────────────────\033[0m║\n");
    printf("║      Symbol Table Index : %-*d      \033[30m│\033[0m   Linked Section Index : %-*d   ║\n", 2, section_header->sh_link, 2, section_header->sh_info);
    printf("║\033[30m──────┬────────────────────┬───────┴────────────┬──────────────────\033[0m║\n");
    printf("║  ID  \033[30m│\033[0m     OFFSET         \033[30m│\033[0m        TYPE        \033[30m│\033[0m       INFO       ║\n");
    printf("║\033[30m──────┼────────────────────┼────────────────────┼──────────────────\033[0m║\n");
    for (int i = 0; i < number; i++, relocation++) {
        printf("║  %*d  \033[30m│\033[0m      %.8X      \033[30m│\033[0m       ", 2, i, relocation->r_offset);

        switch (ELF32_R_TYPE(relocation->r_info)) {
            case R_ARM_ABS32:
                printf("ABS 32");
                break;
            case R_ARM_ABS16:
                printf("ABS 16");
                break;
            case R_ARM_ABS8:
                printf(" ABS8 ");
                break;
            case R_ARM_CALL:
                printf(" CALL ");
                break;
            case R_ARM_JUMP24:
                printf("JUMP24");
                break;

			default:
				printf("\033[33m__%.2X__\033[0m",ELF32_R_TYPE(relocation->r_info));
				break;
		}
		printf("       \033[30m│\033[0m      %.6X      ║\n",ELF32_R_SYM(relocation->r_info));
    }
}

void elf_print_relocation_footer() {
        gIsFirst = true;
		printf("╚═══════════════════════════════════════════════════════════════════╝\n");
}
