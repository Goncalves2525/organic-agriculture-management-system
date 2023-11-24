// usac03

#include <stdio.h>
#include "asm.h"

int main(void) {
	
	// Inicia variáveis
	int array[] = {1,0,0}; // buffer circular
	int length = 3;
	int read = 0;
	int write = 1;
	int num = 1;
	int vec[1]; 
	
	// Executa método asm e apresenta resultado no terminal
	printf("%d\n", move_num_vec(array, length, &read, &write, num, vec));
	
	return 0;
}
