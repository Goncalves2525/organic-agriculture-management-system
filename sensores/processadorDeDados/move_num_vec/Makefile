# Makefile for usac03

prgm: main.o asm.o
	gcc -g -Wall main.o asm.o -z noexecstack -o prgm

main.o: main.c
	gcc -g -Wall -Wextra -fanalyzer -c main.c -o main.o

asm.o: asm.s
	gcc -g -Wall -Wextra -fanalyzer -c asm.s -o asm.o

clean:
	rm -f *.o prgm

run: prgm
	./prgm
