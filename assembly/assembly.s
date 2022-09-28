addi sp sp -64
sw s0 60(sp)
addi s0 sp 64
auipc a5 0x0
addi a5 a5 200
lw a1 0(a5)
lw a2 4(a5)
lw a3 8(a5)
lw a4 12(a5)
lw a5 16(a5)
sw a1 -44(s0)
sw a2 -40(s0)
sw a3 -36(s0)
sw a4 -32(s0)
sw a5 -28(s0)
auipc a5 0x0
addi a5 a5 172
lw a1 0(a5)
lw a2 4(a5)
lw a3 8(a5)
lw a4 12(a5)
lw a5 16(a5)
sw a1 -64(s0)
sw a2 -60(s0)
sw a3 -56(s0)
sw a4 -52(s0)
sw a5 -48(s0)
sw zero -24(s0)
sw zero -20(s0)
j 800000b4
lw a5 -20(s0)
slli a5 a5 0x2
addi a5 a5 -16
add a5 a5 s0
lw a4 -28(a5)
lw a5 -20(s0)
slli a5 a5 0x2
addi a5 a5 -16
add a5 a5 s0
lw a5 -48(a5)
add a5 a4 a5
sw a5 -24(s0)
lw a5 -20(s0)
addi a5 a5 1
sw a5 -20(s0)
lw a4 -20(s0)
li a5 4
bge a5 a4 80000078
nop
nop
lw s0 60(sp)
addi sp sp 64
ret