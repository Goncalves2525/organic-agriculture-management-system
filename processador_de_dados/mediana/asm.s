.section .text
    .global mediana
    mediana:
        # %rdi = array pointer
        # %rsi = array length

        movq $0, %rcx # i = 0
        cmpq $0, %rsi # length == 0?
        je end
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
        end:
            ret