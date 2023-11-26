// usac02

#include <stdio.h>
#include "asm.h"

int main(void) {
	
	// Inicia variáveis
	int array[] = {1,0,0}; // buffer circular
	int length = 3;
	int read = 0;
	int write = 0;
	int value = 5; 
	
	// Executa método asm
	enqueue_value(array, length, &read, &write, value);
	
	// Imprime "done"
	printf("done");
	
	return 0;
}
