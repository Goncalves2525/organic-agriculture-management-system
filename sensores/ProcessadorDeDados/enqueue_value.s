# int* array » %rdi
# int length » %esi
# int *read » %rdx
# int *write » %rcx
# int value » %r8d

.section .text

	.global enqueue_value
	
		enqueue_value:
			movl $1, %eax						# Colocar o ret no valor padrão de insucesso
			movl (%rdx), %r11d					# Colocar valor de read num registo

			cmpl $0, %r11d						# Verificar se o read está na posição 0
			jne readSubtract					# Se não estiver na posição 0, colocar o read no último idx do buffer
		
		readToLastIdx:
			movl %esi, %r11d					# Colocar o valor do buffer length no registo usado para o write

		readSubtract:
			subl $1, %r11d						# Subtrair 1 a write
			cmpl %r11d, (%rcx)					# Se forem iguais, o write está imediatamente atrás do read
			je end
			
		write:
			movl $0, %eax						# Colocar o ret no valor padrão de sucesso
			movslq (%rcx), %r11					# Colocar write no registo 64bits				
			movl %r8d, (%rdi,%r11,4)			# Colocar valor no buffer
			addl $1, (%rcx)						# Mover write para a próxima posição do buffer
			cmpl %esi, (%rcx)					# Validar se write atingiu a dimensão do buffer
			jne end								# Saltar para o final, caso sejam diferentes
			movl $0, (%rcx)						# Colocar write a 0, caso tenha atingido o limite do buffer
			
		end:
			ret
