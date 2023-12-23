# Makefile for ProcessadorDados

processadorDados: main.o func.o extract_token.o sort_array.o enqueue_value.o mediana.o move_num_vec.o
	gcc -g -Wall main.o func.o extract_token.o sort_array.o enqueue_value.o mediana.o move_num_vec.o -z noexecstack -o processadorDados

main.o: main.c
	gcc -g -Wall -Wextra -fanalyzer -c main.c -o main.o

func.o: func.c
	gcc -g -Wall -Wextra -fanalyzer -c func.c -o func.o

extract_token.o: extract_token.s
	gcc -g -Wall -Wextra -fanalyzer -c extract_token.s -o extract_token.o
	
sort_array.o: sort_array.s
	gcc -g -Wall -Wextra -fanalyzer -c sort_array.s -o sort_array.o

enqueue_value.o: enqueue_value.s
	gcc -g -Wall -Wextra -fanalyzer -c enqueue_value.s -o enqueue_value.o

mediana.o: mediana.s
	gcc -g -Wall -Wextra -fanalyzer -c mediana.s -o mediana.o

move_num_vec.o: move_num_vec.s
	gcc -g -Wall -Wextra -fanalyzer -c move_num_vec.s -o move_num_vec.o

clean:
	rm -f *.o processadorDados

valgrind:
	valgrind ./processadorDados /dev/ttyS0 config.txt intermedio 25

runEmpty: processadorDados
	./processadorDados /dev/ttyS0 configEmpty.txt intermedio 25

runOne: processadorDados
	./processadorDados /dev/ttyS0 configOne.txt intermedio 25

run: processadorDados
	./processadorDados /dev/ttyS0 config.txt intermedio 150