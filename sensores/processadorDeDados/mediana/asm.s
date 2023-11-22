.section .text
    .global mediana
    mediana:
        # %rdi = array pointer
        # %rsi = array length

        push %rdi
        push %rsi
        call sort_array # sort the array
        pop %rsi
        pop %rdi
        

        movq $0, %rcx # i = 0
        cmpq $0, %rsi # length == 0?
        je zero_length # return 0
                                            #  _
        movl %esi, %eax # eax = dividend        |
        movl $2, %r8d # r8d = divisor           |
        cltd # sign extend eax to edx:eax       check if length is odd or even
        idivl %r8d # remainder in edx           |
        cmpq $0, %rdx # remainder == 0?         |
        je even                              # _|
                                                 
        # odd:
        movslq %eax, %rax
        movl (%rdi, %rax, 4), %eax # eax = array[quotient]
        jmp end1

        even:
            # eax has quotient
            # the quotient is the index of the median
            movslq %eax, %rax
            movl (%rdi, %rax, 4), %eax # eax = array[quotient]
            jmp end1

        zero_length:
            movl $0, %eax

        end1:
            ret



    
    .global sort_array
    sort_array:
         # %rdi = array pointer
        # %rsi = array length

        cmpq $0, %rsi # length == 0?
        je end2

        movq %rsi, %r8 # i = length
        movq $0, %r9 # j = 0
        i_loop:
            movq $0, %r9 # make sure j = 0
            cmpq $0, %r8 # i == 0
            je end2
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
         

        end2:
            ret
