.module Potencia

.data
.pseg

li $v0,5   ; x

add $s0,$v0,0
li $v0,5   ; y

add $s1,$v0,0
add $s2,$s0,0
li $t0,1

while:  
	beq $t0,$s1,fim  
	mul $s2,$s2,$s0  
	add $t0,$t0,1  
j while

fim:
	sw $s2, 0($gp)
.endseg	
