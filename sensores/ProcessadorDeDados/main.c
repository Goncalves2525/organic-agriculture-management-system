// ProcessadorDados

#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <time.h>
#include "func.h"
#include "extract_token.h"
#include "sort_array.h"
#include "mediana.h"
#include "enqueue_value.h"
#include "move_num_vec.h"

//USAC11: ...
int main(int argc, char **argv) {

	char *coletor = argv[1];
    char *config = argv[2];
    char *intermedio = argv[3];
    int readCounterTotal = atoi(argv[4]);

	printf("\n------------------------------------------------\n");
	printf("\nPARAMETROS USADOS:\n");
    printf("* Caminho do Coletor: %s\n", coletor);
    printf("* Ficheiro de Configuracao: %s\n", config);
    printf("* Diretorio de Saida: %s\n", intermedio);
    printf("* Numero de Leituras: %d\n\n", readCounterTotal);

	int counter = 0;

	// USAC07: ...
	int nSensors = getNumberOfLines(config); // obter o nr de linhas do ficheiro de configuração (ou seja, o nr de sensores)
	if (nSensors <= 0) { // validar se há sensores configurados para leitura
		printf("\nNão há dados no ficheiro de configuração.\n");
		return 1; // Erro: não há sensores disponíveis a usar
	}

	Sensor sensors[nSensors];

	if (extractConfigOpt(config, sensors, nSensors) != 0) { // extrair dados do ficheiro de configuração para instancias de Sensor, agrupados num array
		return 1; // Erro: o extract config falhou
	}; 

	while (1) {

		int readCounter = 0; // inicializar contador de leituras
		int hasData = 0; // variável para validar se há dados a usar, durante a fase de leitura do coletor

		printf("----- a abrir coletor\n"); // (!!!) a abertura e fecho do coletor é feito fora de função devido ao demorado tempo de abertura/fecho
		FILE *coletorFile = fopen(coletor, "r"); // abrir ficheiro
		if (coletorFile == NULL) { // testa se houve erro ao abrir o ficheiro
			perror("Error opening file"); // imprime erro
			fclose(coletorFile); // fechar ficheiro
			return 1; // Erro: erro ao abrir o ficheiro
    	}

		while (readCounter < readCounterTotal && hasData == 0) { // fazer leituras do Coletor, até atingir a qtd definida pelo parâmetro, ou não existir dados para ler
			
			// USAC08: ...
			int tempData[3] = {0,0,0}; // [0] id, [1] value, [2] timestamp
			hasData = getColectorLineData(coletorFile, tempData); // função para popular o array com dados do coletor de dados (0: sucesso, 1: insucesso)

			// (APAGAR printf's INFRA: apenas para teste)
			printf("#%03d/%03d | ", readCounter+1, readCounterTotal);
			printf("hasData: %d | ", hasData);
			printf("id: %02d | ", tempData[0]);
			printf("value: %d | ", tempData[1]);
			printf("timestamp: %d\n", tempData[2]);

			if (hasData == 0) {

				// USAC09: ...
				int idx = getSensorIndex(sensors, nSensors, tempData[0]); // obter indice do sensor com o id obtido na linha

				if (sensors[idx].lastTimestamp == -1 || idx == -1) {
					printf("-----timestamp em erro ou indice do sensor não encontrado\n\n"); // APAGAR
					// Timestamp em erro! ou indice do sensor não encontrado
				} else if (sensors[idx].lastTimestamp == 0 || (tempData[2] - sensors[idx].lastTimestamp) < sensors[idx].timeout) { // se não, testar se leitura está abaixo do timeout
					printf("----- vou adicionar ao buffer! (read: %02d/%02d & write: %02d/%02d)\n\n", sensors[idx].buffer.read, sensors[idx].buffer.length, sensors[idx].buffer.write, sensors[idx].buffer.length); // APAGAR
					enqueue_value(sensors[idx].buffer.bufferArr, sensors[idx].buffer.length, &sensors[idx].buffer.read, &sensors[idx].buffer.write, tempData[1]);
				} else {
					printf("----- vou colocar o sensor em erro!\n\n"); // APAGAR
					sensors[idx].lastTimestamp = -1; // se não, colocar o lastTimestamp em erro (-1)
				}
				readCounter++;
			}
		}

		printf("----- a fechar coletor\n");
		fclose(coletorFile); // fechar o ficheiro

		printf("\n----- a verificar existência de diretório\n"); // APAGAR: apenas para teste
		if (checkOrCreateDirectory(intermedio) != 0){
			freeAllocs(sensors, nSensors);
			return 1; // Erro: ao validar ou criar diretório
		}

		Intermedio intermedios[nSensors];
		counter++;

		for (int i = 0; i < nSensors; i++) { 

			if(moveFromBufferToMedianArr(sensors[i].buffer, sensors[i].medianArr, sensors[i].medianArrLength) != 0) {
				printf("\n(!) Não foi possível copiar dados do buffer para o medianArr!\n");
			}

			//USAC10: ...
			Intermedio intermedio;
			intermedio.intermWriteCounter = counter; // alocar o valor do counter do intermedio
			intermedio.intermSensorId = sensors[i].sensorId; // alocar o id do intermedio
			strcpy(intermedio.intermType, sensors[i].type); // alocar o type do intermedio
			strcpy(intermedio.intermUnit, sensors[i].unit); // alocar o unit do intermedio

			if (sensors[i].lastTimestamp != -1) {
				intermedio.intermMedian = mediana(sensors[i].medianArr, sensors[i].medianArrLength); // calcular a mediana e alocar valor do intermedio
			} else {
				intermedio.intermMedian = -480.00; // valor impossivel para media [minimos são 0º K, -273,15 ºC ou -459,67 ºF] e [percent 0-100]
			}

			intermedios[i] = intermedio; // adicionar intermedio ao array de Intermedio's
		}

		printf("\n----- a criar ficheiro intermedio\n"); // APAGAR: apenas para teste
		if (createIntermSensorsTxt(intermedio) != 0) { // criar o ficheiro ’AAAAMMDDHHMMSS sensors.txt’
			freeAllocs(sensors, nSensors);
			return 1; // Erro: não foi possível gerar um ficheiro
		};

		printf("\n----- a escrever dados intermedios no ficheiro\n"); // APAGAR: apenas para teste
		if (writeToIntermediosFile(intermedios, intermedio, nSensors) != 0) { // escrever no ficheiro 'fileName' no directorio 'intermedio'
			freeAllocs(sensors, nSensors);
			return 1; // Erro: não foi escrever no ficheiro intermedio
		}

		// APAGAR BLOCO INFRA : apenas para testes
		if (counter < 0 || counter >= 2) {
			printf("\nTERMINOU %d CICLOS\n\n", counter);
			freeAllocs(sensors, nSensors);
			return 0;
		}

	}

	freeAllocs(sensors, nSensors);

	return 0;
}
