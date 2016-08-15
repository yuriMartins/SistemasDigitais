.module fibonacci
.data

.pseg
    
    li          $v0, 5 //valor a ser fibonacciado
    move        $s1, $v0

    # decremento $v0
    addi        $v0, $v0, -1

    addi        $sp, $sp, -12
    sw          $v0, 0($sp)
    sw          $ra, 8($sp)
    jal         fibonacci
        
    lw          $ra, 8($sp)
    lw          $s0, 4($sp)
    addi        $sp, $sp, 12

    # result    
    li          $v0, 1
 
fibonacci:
    lw          $t0, 0($sp)
    # if n > 46, return 0 and don't overflow 32-bit register
    li $t4, 46
    sub   	$t0, $t0, $t4
    bgtz         $t0, return0

    # base case
    bltz        $t0, return0
    beq         $t0, $zero, return0
    li 		$t4, 1
    beq         $t0, $t4, return1

    # fibonacci(n - 1)
    addi        $t0, $t0, -1
    addi        $sp, $sp, -12
    sw          $t0, 0($sp)
    sw          $ra, 8($sp)
    jal         fibonacci
    lw          $t0, 0($sp)
    lw          $t1, 4($sp)
    lw          $ra, 8($sp)
    addi        $sp, $sp, 12
    addi        $sp, $sp, -4
    sw          $t1, 0($sp)

    # fibonacci(n - 2)
    addi        $t0, $t0, -1
    addi        $sp, $sp, -12
    sw          $t0, 0($sp)
    sw          $ra, 8($sp)
    jal         fibonacci
    # pega return o valor a partir de fibonacci(n - 2)
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
    sw          $t0, 4($sp)
    jr          $ra  

return1:
    li          $t0, 1
    sw          $t0, 4($sp)
    jr          $ra
.endseg