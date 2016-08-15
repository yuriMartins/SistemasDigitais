.module fatorial

.data

.pseg
    li        $v0, 5
    move      $t0, $v0

    # function
    addi      $sp, $sp, -12  # alloca 12 bytes
    sw        $t0, 0($sp)    # arg1: numero n
    sw        $ra, 8($sp)    # PC
    jal       factorial
    lw        $ra, 8($sp)    # carrega PC
    lw        $s0, 4($sp)    # carrega o valor de retorno
    addi      $sp, $sp, 12   # desalloca 12 bytes

    # resultado
    li        $v0, 4

factorial:
    # caso base
    lw        $t0, 0($sp)
    beq       $t0, $zero, return1

    addi      $t0, $t0, -1
    # factorial recursivo
    addi      $sp, $sp, -12
    sw        $t0, 0($sp)
    sw        $ra, 8($sp)
    jal       factorial
    #carrega o valor de returno
    lw        $t1, 4($sp)
    lw        $ra, 8($sp)
    addiu     $sp, $sp, 12
    # load n
    lw        $t0, 0($sp)
    # n * (n - 1)
    mul       $t2, $t1, $t0
    sw        $t2, 4($sp)

    jr        $ra

return1:
    li        $t0, 1
    sw        $t0, 4($sp)
    jr        $ra
.endseg