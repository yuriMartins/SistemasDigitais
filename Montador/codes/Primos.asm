.module primos
	
	.pseg
	
	addi $a0, $zero, 1
	addi $s0, $a0, 10 		#lim+1
	addi $t0, $zero, 1 		#contador
	
	slt $t1, $t0, $s0  
	bqe $t1, $zero, Fim 
	
	addi $a1, $zero, 2
	slt $t1, $a1, $s0 		#se limite < 2
	bne $t1, $a0, check 
	sw $a2, 0($sp);
	addi $sp, $zero, 1
	j Fim 
	
	check: 
	slt $t1, $t0, $s0  	
	bqe $t1, $zero, Fim 
	div $s2, $s0, $t0 		# numero/count
	mfhi $a1 
	beq	$a1, $zero, calculo	 # resto = 0, primo
	j check
	
	calculo: 	
	addi $t0, $t0, 1 #count++
	sw $a2, 0($sp);
	addi $sp, $zero, 1
	j check 			#checa os numeros que faltam
	
	Fim: 	
	
			
.endseg