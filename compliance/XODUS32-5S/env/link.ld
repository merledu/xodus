OUTPUT_FORMAT("elf32-littleriscv", "elf32-littleriscv", "elf32-littleriscv")

OUTPUT_ARCH(riscv)

SECTIONS {
        . = 0x00000000;
        .text : { *(.text) }
        . = 0x00008000;
        .data : { *(.data) }
        .bss  : { *(.bss) }
}
