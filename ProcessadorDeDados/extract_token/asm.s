.section .text
    .global extract_token
    extract_token:
        # %rdi = char* input
        # %rsi = char* token
        # %rdx = &output
        movb $35, %cl                   # %cl = '#'
        movq %rsi, %r10                 # %r10 = save char* token initial address

        input_loop:
            movq %r10, %rsi             # make sure char* token is at initial address
            cmpb $0, (%rdi)             # check if it is end of string
            je end
            cmpb %cl, (%rdi)            # check if it is '#'
            je prepare_check_token
            addq $1, %rdi               # input++
            jmp input_loop


        prepare_check_token:
            addq $1, %rdi               # input++
        check_token:
            cmpb $0, (%rsi)             # check if it is end of token string
            je prepare_correct_token
            movb (%rsi), %r8b           # %r8b = *(token)
            cmpb (%rdi), %r8b           # check if *(input) == *(token)
            jne input_loop              # continue to look for the correct token
            addq $1, %rdi               # input++
            addq $1, %rsi               # token++
            jmp check_token


        prepare_correct_token:
            addq $1, %rdi               # input++
        correct_token:
            cmpb %cl, (%rdi)            # check if it is '#'
            je prepare_convert_to_int
            cmpb $0, (%rdi)             # check if it is end of string
            je prepare_convert_to_int
            addq $1, %rdi               # input++
            jmp correct_token

        prepare_convert_to_int:
            subq $1, %rdi               # input--
            movl $1, %ecx               # n = 1
            movl $0, %r10d              # sum = 0
        convert_to_int:
            cmpb $46, (%rdi)            # check if char is "."
            je prepare_convert_to_int
            cmpb $58, (%rdi)            # check if char is ":"
            je store_output_value
            movb (%rdi), %r9b           # %r9b = *(input)
            movsbl %r9b, %r11d          # %r11d = *(input)
            subl $48, %r11d             # %r9b = *(input) - '0'
            imull %ecx, %r11d           # product = n * integer
            addl %r11d, %r10d           # sum += product
            imull $10, %ecx             # n *= 10
            subq $1, %rdi               # input--
            jmp convert_to_int

        store_output_value:
            movl %r10d, (%rdx)          # output = sum

        end:
            ret

