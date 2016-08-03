Potencia

.data  r: .word 0.textmain:

li $v0,5   #leitura de x
syscall
add $s0,$v0,0
li $v0,5   #leitura de y
syscall
add $s1,$v0,0
add $s2,$s0,0
li $t0,1

while:  
	beq $t0,$s1,fim  
	mul $s2,$s2,$s0  
	add $t0,$t0,1  
j while

fim:
	sw $s2,r
	li $v0,1
	lw $a0,r
	syscall
	li $v0,10
	syscall