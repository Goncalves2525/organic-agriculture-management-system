# ProcessadorDados

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

			movl $1, %eax						# Coloca retorno a 1, para uma situação de não haver cópia
			movl $0, %r11d						# Inicializar iterador a ser comparado com length (bufferLength)
			movl $0, %ebx						# Inicializar iterador a ser incrementado até num (medianArrLength)

		buffer_empty:
			addl $1, %r11d						# Incrementa o iterador (bufferLength)
			movl (%rdx), %r10d					# Colocar valor de read num registo
			cmpl %r10d, (%rcx)					# Comparar write com read
			je end								# Se não forem iguais, executar leitura (fazê-lo até encontrar posição de write), se forem, terminar função
	
		read:
			movl (%rdi,%r10,4), %r10d			# Colocar o valor lido do buffer num registo transporte
			cmpl $'\0', %r10d					# Comparar valor lido do buffer com o valor de fecho do array
			je end								# Se verificada a igualdade, não há valores a ler no buffer, e terminar

		copy:
			movl $0, %eax						# Coloca 0 no retorno, para indicar que houve cópia de valores
			movslq %ebx, %r12					# Colocar valor do iterador em registo 64bit para usar nesta tag
			movl %r10d, (%r9,%r12,4)			# Colocar o valor lido do buffer no indice atual de vec[i] (medianArr)
			addl $1, %ebx						# Incrementa o iterador  (medianArr)

		read_move:
			addl $1, (%rdx)						# Adiciona 1 ao indice read
			cmpl %esi, (%rdx)					# Compara read com length (bufferLength)
			jne close							# Salta o fecho do buffer para read, se o anterior não for igualdade
			movl $0, (%rdx)						# Faz o fecho do buffer (zerar) para read, se houver igualdade
	
		close:
			cmpl %r8d, %ebx						# Comparar o iterador local do vec (medianArr) com num (medianArrLength)
			je end								# Termina função se igualdade se verificar
			cmpl %esi, %r11d					# Compara o iterador local do buffer com length (bufferLength)
			jne buffer_empty					# Retorna ao loop, caso não se verifique a igualdade
	
		end:
			popq %r12							# Callee-saved(2)
			popq %rbx							# Callee-saved(1)
			ret
