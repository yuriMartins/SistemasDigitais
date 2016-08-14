.module bublesort
.data
.pseg
	move $s0,$gp			#pega o ponteiro inicial e salva no array. 
	addi $t0,1			# $t0 = 1
	add $t1,$zero,$zero		# 
	add $t2,$zero,$zero		# 
	add $t3,$zero,$zero		# 
	add $t6,$zero,$zero        
	add $t4,$zero,$zero        
	sub $t7,$zero,1			#         
	add $s1,$s0,$zero		# copia o ponteiro do array em $s1
entervalues:
	li $v0,5			# atribui um inteiro em v0 
	beq $v0,$t7,bubblesort 		# end de string caminha para bubblesort
	sb $v0,0($s1)			# colocar o valor na posição apontada por $ s1
	addi $s1,1			# mova o ponteiro $ s1 por um
	add $t5,$s1,$zero		# $T5 armazena o valor final
	j entervalues
bubblesort:
	add $t4,$s0,$zero
	addi $t6,1
	#s1-1 -> s0
	sub $s1,$s1,$t0
	beq $s1,$s0,ending  
	#s0 -> s1
	add $s2,$s0,$zero
loopinterno:
	lb $t1,0($s2)		# primeiro elemento
	lb $t2,1($s2)		# segundo element0
	slt $t3,$t2,$t1		
	beq $t3,$zero,proximo	 
	sb $t2,0($s2)		 
	sb $t1,1($s2)				
proximo:
	addi $s2,1		
	bne $s2,$s1,loopinterno 

.endseg	 
