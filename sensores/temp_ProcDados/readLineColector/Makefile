# Makefile for teste

teste: main.o func.o
	gcc -g -Wall main.o func.o -z noexecstack -o teste

main.o: main.c
	gcc -g -Wall -Wextra -fanalyzer -c main.c -o main.o
	
func.o: func.c
	gcc -g -Wall -Wextra -fanalyzer -c func.c -o func.o

clean:
	rm -f *.o teste

valgrind:
	valgrind ./teste /dev/ttyS0

run: teste
	./teste /dev/ttyS0
