# kkamin8
# MIPS fibonacci

.data
msg1: .asciiz "Enter n: "
msg2: .asciiz "th Fibonacci is: "

.text
    # ask user for the input
    li          $v0, 4
    la          $a0, msg1
    syscall
    li          $v0, 5
    syscall
    move        $s1, $v0

    # decrement $v0 since 1st fibonacci is f(0) = 0, 2nd is f(1) = 1 etc...
    addi        $v0, $v0, -1

    addi        $sp, $sp, -12
    sw          $v0, 0($sp)
    sw          $ra, 8($sp)
    jal         fibonacci
        
    lw          $ra, 8($sp)
    lw          $s0, 4($sp)
    addi        $sp, $sp, 12

    # print result
    move        $a0, $s1
    li          $v0, 1
    syscall

    la          $a0, msg2
    li          $v0, 4
    syscall

    move        $a0, $s0
    li          $v0, 1
    syscall

    li          $v0, 10
    syscall

fibonacci:
    lw          $t0, 0($sp)
    # if n > 46, return 0 and don't overflow 32-bit register
    bge         $t0, 46, return0

    # base case
    ble         $t0, 0, return0
    beq         $t0, 1, return1

    # fibonacci(n - 1)
    addi        $t0, $t0, -1
    addi        $sp, $sp, -12
    sw          $t0, 0($sp)
    sw          $ra, 8($sp)
    jal         fibonacci
    # reload (n - 1)
    lw          $t0, 0($sp)
    # get return value from fibonacci(n - 1)
    lw          $t1, 4($sp)
    lw          $ra, 8($sp)
    addi        $sp, $sp, 12
    # save the return value from fibonacci(n - 1) on the stack
    addi        $sp, $sp, -4
    sw          $t1, 0($sp)

    # fibonacci(n - 2)
    addi        $t0, $t0, -1
    addi        $sp, $sp, -12
    sw          $t0, 0($sp)
    sw          $ra, 8($sp)
    jal         fibonacci
    # get return value from fibonacci(n - 2)
    lw          $t2, 4($sp)
    lw          $ra, 8($sp)
    addi        $sp, $sp, 12
    lw          $t1, 0($sp)
    addi        $sp, $sp, 4

    # $t3 = fibonacci(n - 1) + fibonacci(n - 2)
    add         $t3, $t1, $t2
    sw          $t3, 4($sp)

    jr          $ra 

return0:
    sw          $0, 4($sp)
    jr          $ra  

return1:
    li          $t0, 1
    sw          $t0, 4($sp)
    jr          $ra