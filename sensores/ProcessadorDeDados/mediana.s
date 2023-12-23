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
            jmp end

        even:
            # eax has quotient
            # the quotient is the index of the median
            movslq %eax, %rax
            movl (%rdi, %rax, 4), %eax # eax = array[quotient]
            jmp end

        zero_length:
            movl $0, %eax

        end:
            ret



    
