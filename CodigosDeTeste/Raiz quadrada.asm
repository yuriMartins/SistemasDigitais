# ===================================================================
# fonte: http://www.guj.com.br/t/ajuda-em-assembly/283268
# Raiz quadrada inteira de um número inteiro x passado por parâmetro. 
# As variáveis locais
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
# ex:  	primeiro passo => 0 * 2 + 1 => 1
#	segundo passo => 1 * 2 + 1 => 3
#====
.module raiz_quadrada
.data	
.pseg
	li $s0, 16                  #raiz
	li $s1, 1          	    #so para comparacao

	
	Loop: slti $t1, $s0, 1 	    #verifica se o resultado parcial Ã© menor do que 1
	beq $t1, $s1, Endfor  	    #caso seja menor do que 1 vai pro final do loop
	li $s3, 2
	mul $t0, $s2, $s3 	    #indice * 2 + 1
	addi $t0, $t0, 1
	sub $s0, $s0, $t0 	    #raiz - indice
	addi $s2, $s2, 1 	    #adiciona 1 ao contador
	j Loop
	Endfor: addi $sp, $sp, 1
	sw $s2, 0($sp)

.endseg
