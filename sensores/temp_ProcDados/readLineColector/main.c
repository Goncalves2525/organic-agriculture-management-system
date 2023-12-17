// testes

#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include "func.h"

int main(int argc, char **argv) {

	int counter = 0;

	printf("Caminho do Coletor: %s\n", argv[1]);

	// Open the file specified by argv[1]
	FILE *coletorFile = fopen(argv[1], "r");
	if (coletorFile == NULL) {
		perror("Error opening file");
		return 2; // Error code indicating file opening failure
	}

	while(counter < 10) { // Read and print the content of the file
    	char coletorLine[256]; // Adjust the coletorLine size as needed
		if (fgets(coletorLine, sizeof(coletorLine), coletorFile) != NULL) {
			if (strcmp(coletorLine,"\n") == 0) {
				// do nothing
			} else {
				counter++;
				printf("#: %d »»» ", counter);
        		printf("File Content: %s", coletorLine);
			}
		} else {
			counter = 999;
    	}
	}

	// Close the file
    fclose(coletorFile);

	return 0;
}
