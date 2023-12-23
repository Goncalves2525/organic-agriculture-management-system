// ProcessadorDados

#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <sys/types.h>
#include <sys/stat.h>
#include <time.h>
#include "func.h"
#include "extract_token.h"
#include "enqueue_value.h"
#include "move_num_vec.h"

int getNumberOfLines(char* config_file) {

    int lines = 0;
    char ch;

    FILE *file = fopen(config_file, "r"); // abrir ficheiro
    if (file == NULL) { // testar erro ao abrir ficheiro
        perror("Error opening file");
        fclose(file);
        return -1;
    }

    if (fgetc(file) == EOF) { // testar se o ficheiro está vazio
        lines = 0;
    } else {
        rewind(file); // reposicionar no inicio do ficheiro
        while ((ch = fgetc(file)) != EOF) { // percorre todos os caracteres até o final do ficheiro
            if (ch == '\n') { // validar se o caracter é um newline
                lines++; // incrementar ao nr de linhas se for newline
            }
        }
        if (ch != '\n') { // validar se o último caracter do ficheiro é um newline
            lines++; // se não for, incrementar a última linha ao nr de linhas
        }
    }

    printf("(nº linhas config.txt: %02d)\n\n", lines);

    fclose(file); // fechar ficheiro

    return lines;
}

int extractConfigOpt(char* configPath, Sensor* sensors, int nSensors) {

    FILE* configFile = fopen(configPath, "r"); // abrir ficheiro
    if (configFile == NULL) { // testar erro ao abrir ficheiro
        perror("Error opening file");
        fclose(configFile);
        return 1;
    }

    int i = 0; // inicializar o indice do array de sensores

    char configLine[300]; // define a dimensão da string de cada linha do config
    while (fgets(configLine, sizeof(configLine), configFile) != NULL) {

		Sensor sensor;

		char * token = strtok(configLine, "#");
		sensor.sensorId = atoi(token); // alocar id

		token = strtok(NULL, "#");
		strcpy(sensor.type, token); // alocar type

		token = strtok(NULL, "#");
		strcpy(sensor.unit, token); // alocar unit

		token = strtok(NULL, "#");
        sensor.buffer.length = atoi(token); // definir valor da dimensão do buffer, de acordo com a configuração
        sensor.buffer.read = 0; // definir valor inicial do read
        sensor.buffer.write = 0; // definir valor inicial do write
		sensor.buffer.bufferArr = (int*)calloc(atoi(token), sizeof(int)); // alocar memória para o buffer
        if (sensor.buffer.bufferArr == NULL) {
            fclose(configFile); // fechar ficheiro
            freeAllocs(sensors, i); // libertar memória alocada até o momento
            return 1; // Erro: não foi possível alocar memória para buffer
        }

		token = strtok(NULL, "#");
        sensor.medianArrLength = atoi(token); // alocar dimensão do medianArr à variável
		sensor.medianArr = (int*)calloc(atoi(token), sizeof(int)); // alocar memória para o medianArr
        if (sensor.medianArr == NULL) {
            fclose(configFile); // fechar ficheiro
            freeAllocs(sensors, i); // libertar memória alocada até o momento
            free(sensor.buffer.bufferArr); // libertar memória do bufferArr gerado neste ciclo
            return 1; // Erro: não foi possível alocar memória para medianArr
        }

		token = strtok(NULL, "#");
		sensor.timeout = atoi(token); // alocar valor timeout

		sensor.lastTimestamp = 0; // definir o lastTimestamp por defeito
		sensor.write_counter = 0; // definir o write_counter por defeito

		sensors[i] = sensor; // adicionar sensor ao array
        i++; // incrementar ao iterador dos indices do array de sensores
	}

    fclose(configFile); // fechar ficheiro

	return 0;
}

int getColectorLineData(FILE* coletorFile, int* array) {

    int counter = 0; 
    while (counter < 1) { // executa enquanto não obtiver uma leitura do ficheiro
        char coletorLine[256];
        if (fgets(coletorLine, sizeof(coletorLine), coletorFile) != NULL) { // testa se o ficheiro está vazio
            if (strcmp(coletorLine, "\n") == 0) { // testar se a linha contêm um nextLine
                // do nothing
            } else {
                counter++;
                if (extract_token(coletorLine, "sensor_id", &array[0]) == 0 || extract_token(coletorLine, "value", &array[1]) == 0 || extract_token(coletorLine, "time", &array[2]) == 0) {
                    return 1; // Erro: um dos extract_token falhou na extração de dados
                }
            }
        } else {
            return 1; // Erro: ficheiro está vazio
        }
    }

	return 0;
}

int getSensorIndex(Sensor* sensors, int nSensors, unsigned int id) {

    if (id <= 0) { // testar se tem id inválido
        return -1; // Erro: id inválido
    }

    for (int i = 0; i < nSensors; i++) { // percorre todos os sensors
        if (sensors[i].sensorId == id) { // testa se o sensor atual tem um id igual ao id do parametro
            return i; // retorna o indice do id encontrado
        }
    }

    return -1; // Erro: id não encontrado
}

void freeAllocs(Sensor* sensors, int nSensors) {
    for (int i = 0; i < nSensors; i++) {
		free(sensors[i].buffer.bufferArr); // libertar memória usada pelo bufferArr do sensor iterado
		free(sensors[i].medianArr); // libertar memória usada pelo medianArr do sensor iterado
	}
}

int checkOrCreateDirectory(char* path) {
    struct stat st;
    if (stat(path, &st) == 0) { // verifica se o directório já existe.
        if (S_ISDIR(st.st_mode)) {
            //printf("\nDiretório '%s' já existe.\n\n", path);
        } else {
            fprintf(stderr, "\n'%s' não é um diretório.\n\n", path);
            return 1;
        }
    } else { // o directório não existe, e será criado
        if (mkdir(path, 0777) == 0) {
            printf("\nDiretório '%s' criado com sucesso.\n\n", path);

            char pathToWithLastFileCreated[50]; 
            pathToWithLastFileCreated[0] = '\0';
            strncat(pathToWithLastFileCreated, "./", 3);
            strncat(pathToWithLastFileCreated, path, 25);
            strncat(pathToWithLastFileCreated, "/latestFileName.txt", 30);

            FILE* fileWithLastFileCreated = fopen(pathToWithLastFileCreated, "w"); // criar o ficheiro
            if (fileWithLastFileCreated == NULL) { // testar se o ficheiro foi criado
                printf("\nErro ao criar o ficheiro!\n");
                return 1; // Erro: não foi possível criar o ficheiro
            }

            fclose(fileWithLastFileCreated);

        } else {
            perror("\nErro ao criar o diretório\n\n");
            return 1;
        }
    }
    return 0;
}

int createIntermSensorsTxt(char* path) {

    char fileName[30]; 
    fileName[0] = '\0';

    time_t t = time(NULL);
	struct tm tm = *localtime(&t);
    char year[12];
    snprintf(year, 12,"%d", (tm.tm_year + 1900));
    char month[12];
    snprintf(month, 12,"%02d", (tm.tm_mon + 1));
	char day[12];
    snprintf(day, 12,"%02d", tm.tm_mday);
	char hour[12];
    snprintf(hour, 12,"%02d", tm.tm_hour);
	char minute[12];
    snprintf(minute, 12,"%02d", tm.tm_min);
	char second[12];
    snprintf(second, 12,"%02d", tm.tm_sec);

    strncat(fileName, year, 12);
    strncat(fileName, month, 12);
    strncat(fileName, day, 12);
    strncat(fileName, hour, 12);
    strncat(fileName, minute, 12);
    strncat(fileName, second, 12);
	strncat(fileName, "_sensors.txt", 25);

    char fileLocalPath[50]; 
    fileLocalPath[0] = '\0';
    strncat(fileLocalPath, "./", 3);
    strncat(fileLocalPath, path, 25);
    strncat(fileLocalPath, "/", 2);
    strncat(fileLocalPath, fileName, 30);
    
    FILE* file = fopen(fileLocalPath, "r");
    if (file != NULL) { // testar se o ficheiro existe
        printf("\nO ficheiro já existe!\n");
        fclose(file); // fechar ficheiro
        return 1; // Erro: ficheiro já existe
	}
    
    FILE* newFile = fopen(fileLocalPath, "w"); // criar o ficheiro
    if (newFile == NULL) { // testar se o ficheiro foi criado
        printf("\nErro ao criar o ficheiro!\n");
        return 1; // Erro: não foi possível criar o ficheiro
    }

    fclose(newFile); // fechar ficheiro

    char latestFileName[50]; 
    latestFileName[0] = '\0';

    strncat(latestFileName, "./", 3);
    strncat(latestFileName, path, 25);
    strncat(latestFileName, "/latestFileName.txt", 20);

    FILE* fileLatestFileName = fopen(latestFileName, "w"); // abrir ficheiro para escrita
    if (fileLatestFileName == NULL) { // testar se foi possivel abrir o ficheiro
        printf("\nErro ao abrir o ficheiro!\n");
        return 1; // Erro: não foi possível abrir o ficheiro
    }

    char fileNamePath[80]; 
    fileNamePath[0] = '\0';

    strncat(fileNamePath, "../ProcessadorDeDados/", 24);
    strncat(fileNamePath, path, 25);
    strncat(fileNamePath, "/", 2);
	strncat(fileNamePath, fileName, 30);
    fputs(fileNamePath, fileLatestFileName); // escrever no ficheiro, o caminho do mais recente fileName

    fclose(fileLatestFileName); // fechar ficheiro
    
    return 0;
}

int writeToIntermediosFile(Intermedio* intermedios, char* intermedio, int nSensors) {
    
    char latestFileNamePath[50]; 
    latestFileNamePath[0] = '\0';

    strncat(latestFileNamePath, "./", 3);
    strncat(latestFileNamePath, intermedio, 25);
    strncat(latestFileNamePath, "/latestFileName.txt", 20);

    FILE* fileLatestFileName = fopen(latestFileNamePath, "r"); // abrir ficheiro para leitura
    if (fileLatestFileName == NULL) { // testar se foi possivel abrir o ficheiro
        printf("\nErro ao abrir o ficheiro!\n");
        fclose(fileLatestFileName);
        return 1; // Erro: não foi possível abrir o ficheiro
    }

    char latestFileName[80];
    fgets(latestFileName, 80, fileLatestFileName); // ler conteudo para obter o caminho para o ficheiro onde irá escrever os dados contidos em intermedios

    fclose(fileLatestFileName);

    FILE* writeFile = fopen(latestFileName, "a"); // abrir ficheiro para escrita
    if (writeFile == NULL) { // testar se foi possivel abrir o ficheiro
        printf("\nErro ao abrir o ficheiro!\n");
        fclose(writeFile);
        return 1; // Erro: não foi possível abrir o ficheiro
    }

    for (int i = 0; i < nSensors; i++) {

        char sensorId[8];
        snprintf(sensorId, 8,"%d", intermedios[i].intermSensorId);

        char writeCounter[8];
        snprintf(writeCounter, 8,"%d", intermedios[i].intermWriteCounter);

        char mediana[24];
        if (intermedios[i].intermMedian == -48000) {
            strcpy(mediana, "error");
        } else {
            snprintf(mediana, 24,"%d", intermedios[i].intermMedian);
        }

        char toWrite[100]; 
        toWrite[0] = '\0';
        
        strncat(toWrite, sensorId, 8);
        strncat(toWrite, ",", 2);
        strncat(toWrite, writeCounter, 8);
        strncat(toWrite, ",", 2);
        strncat(toWrite, intermedios[i].intermType, 28);
        strncat(toWrite, ",", 2);
        strncat(toWrite, intermedios[i].intermUnit, 12);
        strncat(toWrite, ",", 2);
        strncat(toWrite, mediana, 16);
        strncat(toWrite, "#\n", 3);
        
        fputs(toWrite, writeFile); // escrever no ficheiro, o registo dos valores do intermedio no indice atual

    }

    fclose(writeFile);

    return 0;
}

int moveFromBufferToMedianArr(Buffer buffer, int* medianArr, int medianArrLength) {
    if(move_num_vec(buffer.bufferArr, buffer.length, &buffer.read, &buffer.write, medianArrLength, medianArr) != 0) {
		return 1; // Erro: ao copiar dados do buffer para o medianArr
	}
    return 0;
}

