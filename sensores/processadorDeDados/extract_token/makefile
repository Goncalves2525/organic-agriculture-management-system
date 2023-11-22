main: main.o asm.o
	gcc main.o asm.o -z noexecstack -o main

main.o: main.c
	gcc -g -Wall -Wextra -fanalyzer -c main.c -o main.o

asm.o: asm.s
	gcc -g -Wall -Wextra -fanalyzer -c asm.s -o asm.o

clean:
	rm -f *.o main

run: main
	./main