# usac02

# int* array » %rdi
# int length » %esi
# int *read » %rdx
# int *write » %rcx
# int value » %r8d

.section .text
	.global enqueue_value
	
enqueue_value:
	
buffer_empty:
	movl (%rdx), %r10d
	cmpl %r10d, (%rcx)
	je write
	
write:
	movslq (%rcx), %r10
	movl %r8d, (%rdi,%r10,4)
	addl $1, (%rcx)
	cmpl %esi, (%rcx)
	jne close
	movl $0, (%rcx)
	
close:
	movl (%rdx), %r10d
	cmpl %r10d, (%rcx)
	jne end
	
read:
	addl $1, (%rdx)
	cmpl %esi, (%rdx)
	jne end
	movl $0, (%rdx)
	
end:
	ret
