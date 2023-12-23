// SaidaDeDados

#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <unistd.h>
#include "func.h"

int main(int argc, char **argv) {

	// USAC12: ...

	char *intermedioPath = argv[1];
    char *outputPath = argv[2];
    int periodicidade = atoi(argv[3]);

	printf("\n------------------------------------------------\n");
	printf("\nPARAMETROS USADOS:\n");
    printf("* Diretório com os últimos dados recolhidos: %s\n", intermedioPath);
    printf("* Diretório de saída de dados: %s\n", outputPath);
    printf("* Intervalo de tempo entre recolha de ficheiros: %d\n\n", periodicidade);

	if (checkOrCreateDirectory(outputPath) != 0) {
		printf("Erro ao criar diretório");
		return 1;
	}

	int counter = 0;

	while (1) {

		counter++; // incrementar o contador

		nanoSleep(periodicidade); // aguardar x nanosegundos antes de executar uma leitura dos últimos dados

		// USAC13: ...

		char latestDataPath[100];
		if (getLatestDataPath(intermedioPath, latestDataPath) != 0) { // obter o caminho do ficheiro mais recente em intermedio
			printf("\nNão foi possível obter um caminho válido.\n");
		} else {
			
			int nSensors = getNumberOfLines(latestDataPath);
			
			if (nSensors <= 0) {
				printf("\nNão existem sensores disponíveis.\n");
			} else {

				Output outputs[nSensors];

				if(addDataToOutputs(latestDataPath, outputs, nSensors) != 0) {
					printf("\nNão foi possível recolher dados.\n");
				} else {

					if(writeDataToCSV(outputPath, outputs, nSensors) != 0) {
						printf("\nErro ao escrever para CSV.\n");
					} else {
						printf("\nDados colocados no ficheiro CSV com sucesso");
					}
				}
			}
		}
		

		// APAGAR BLOCO INFRA : apenas para testes
		if (counter < 0 || counter >= 2) {
			printf("\nTERMINOU %d CICLOS\n\n", counter);
			return 0;
		}

	}

	return 0;
}
