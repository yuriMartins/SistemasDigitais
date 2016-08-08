# ===================================================================
# Raiz quadrada inteira de um número inteiro x passado por parâmetro. 
As variáveis locais
# devem ser armazenadas na pilha e o valor do parâmetro deve ser inserido
# por uma entrada
# ===================================================================

#====

# raiz de 25 = ?
# 1º Passo  25 −1  = 24
# 2º Passo  24 − 3 = 21
# 3º Passo  21 − 5 = 16
# 4º Passo  16 − 7 = 9
# 5º Passo  9  − 9 = 0
# 
# posição então será indice * 2 + 1
# ex: primeiro passo => 0 * 2 + 1 => 1
# 		 segundo passo => 1 * 2 + 1 => 3
#====
.module raiz quadrada
.data
	

.pseg
	
		li $v0, 5								
		la $s0, ($v0)						# guarda o valor lido
		li $s1, 0
		jal raiz						# calcula a raiz quadrada		

	print_int:
		li 		$v0, 5					
		jr 		$ra							
									
	raiz:
		mul	$t0, $s1, 2
		add $t0, $t0, 1
		sub	$s0, $s0, $t0
		add $s1, $s1, 1						# incrementa o contador, que sera o resultado da raiz
		beq $s0, $zero, success					# se chegamos a zero a raiz é perfeita
		slt	$t0, $s0, $zero					# caso seja menor que zero, deu problema
		beq $t0, 1, exit					# vai pro fim
		j  raiz							# caso não ocorra nenhum dos casos acima, itera novamente
		
	
	success:
		la $v0, 1
		la $a0, ($s1)		
		j  exit
	
	exit:
		la $v0, 10
.endseg
		