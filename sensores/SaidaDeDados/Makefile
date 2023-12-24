# Makefile for SaidaDeDados

saidaDados: main.o func.o
	gcc -g -Wall main.o func.o -z noexecstack -o saidaDados

main.o: main.c
	gcc -g -Wall -Wextra -fanalyzer -c main.c -o main.o

func.o: func.c
	gcc -g -Wall -Wextra -fanalyzer -c func.c -o func.o

clean:
	rm -f *.o saidaDados

valgrind:
	valgrind ./saidaDados intermedio output 110000

run: saidaDados
	./saidaDados intermedio output 110000