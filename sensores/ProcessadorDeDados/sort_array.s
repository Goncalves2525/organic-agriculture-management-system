.section .text
    
    .global sort_array
    
        sort_array:
            # %rdi = array pointer
            # %rsi = array length

            cmpq $0, %rsi # length == 0?
            je end

            movq %rsi, %r8 # i = length
            movq $0, %r9 # j = 0
        
        i_loop:
            movq $0, %r9 # make sure j = 0
            cmpq $0, %r8 # i == 0
            je end
            subq $1, %r8 # i--
            jmp j_loop

        j_loop:
            cmpq %r8, %r9 # j == i
            je i_loop
            movl (%rdi, %r9, 4), %eax # eax = array[j]
            movq %r9, %r10
            addq $1, %r10 # r10 = j + 1
            cmpl %eax, (%rdi, %r10, 4) # j+1 < j?
            jl swap
            addq $1, %r9 # j++
            jmp j_loop

        swap:
            movl (%rdi, %r10, 4), %ecx # %ecx = array[j+1]
            movl %eax, (%rdi, %r10, 4) # array[j+1] = array[j]
            movl %ecx, (%rdi, %r9, 4) # array[j] = %ecx
            addq $1, %r9 # j++
            jmp j_loop
         
        end:
            ret