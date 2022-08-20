addi t1, zero, 1
addi t3, zero, 10
FIBONACCI_LOOP: addi t4, t4, 1
blt t3, t4, END
add t2, t1, t0
add t0, t1, zero
add t1, t2, zero
jal FIBONACCI_LOOP
END:
