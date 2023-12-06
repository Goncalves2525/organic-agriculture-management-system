# usac03

# int* array » %rdi
# int length » %esi
# int *read » %rdx
# int *write » %rcx
# int num » %r8d
# int* vec » %r9

.section .text
	.global move_num_vec
	
move_num_vec:
	pushq %rbx							# Callee-save(1)
	pushq %r12							# Callee-save(2)

	movl $0, %eax						# Coloca retorno a 0, para uma sittuação de não haver cópia
	movl $0, %r11d						# Inicializar iterador a ser comparado com length
	movl $0, %ebx						# Inicializar iterador a ser decrementado a partir de num

buffer_empty:
	addl $1, %r11d						# Incrementa o iterador
	movl (%rdx), %r10d					# Colocar valor de read num registo
	cmpl %r10d, (%rcx)					# Comparar write com read
	jne write							# Se não forem iguais, executar leitura (fazê-lo até encontrar posição de read)
	
read:
	movslq (%rdx), %r10					# Colocar indice de read no registo
	movl (%rdi,%r10,4), %r10d			# Colocar o valor lido do buffer num registo transporte
	cmpl $0, %r10d						# Comparar valor lido do buffer com 0
	je read_cont						# Se verificada a igualdade, ignora o passo de cópia

copy:
	movl $1, %eax						# Coloca 1 no retorno, para indicar que houve cópia de valores
	movslq %ebx, %r12					# Colocar valor do iterador em registo local
	movl %r10d, (%r9,%r12,4)			# Colocar o valor lido do buffer no indice atual de vec[num-i]
	addl $1, %ebx						# Incrementa o iterador

read_cont:
	addl $1, (%rdx)						# Adiciona 1 ao indice read
	cmpl %esi, (%rdx)					# Compara read com length
	jne write							# Salta o fecho do buffer para read, se o anterior não for igualdade
	movl $0, (%rdx)						# Faz o fecho do buffer (zerar) para read, se houver igualdade
		
write:
	addl $1, (%rcx)						# Adiciona 1 ao indice write
	cmpl %esi, (%rcx)					# Compara write com length
	jne close							# Se não se verificar igualdade, salta o fecho do buffer para write
	movl $0, (%rcx)						# Faz o fecho do buffer (zerar) para read, se houver igualdade
	
close:
	cmpl %r8d, %ebx						# Comparar o iterador local com num
	je end								# Termina função se igualdade se verificar
	cmpl %esi, %r11d					# Compara o iterador local com length
	jne buffer_empty					# Retorna ao loop, caso não se verifique a igualdade
	
end:
	popq %r12							# Callee-save(2)
	popq %rbx							# Callee-saved(1)
	ret
