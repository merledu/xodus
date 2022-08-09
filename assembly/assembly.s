addi x5, x0, 6
sw x5, 8(x5)
lw x6, 8(x5)
beq x5, x6, JUMP
add x2, x3, x4
addi x3, x0, 1
addi x4, x3, 1
JUMP: add x1, x1, x2
