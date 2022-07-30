addi x2, x0, 5
addi x7, x0, 6
JAL:
addi x8, x0, 9
LOOP:
sw x2, 73(x6)
lw x3, 73(x6)
bne x2, x7, LOOP
auipc x4, 4
jal JAL
jalr x2
addi x28, x0, 16

