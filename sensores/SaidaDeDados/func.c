// SaidaDeDados

#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <sys/types.h>
#include <sys/stat.h>
#include <time.h>
#include <unistd.h>
#include "func.h"

int checkOrCreateDirectory(char* path) {
    struct stat st;
    if (stat(path, &st) == 0) { // verifica se o directório já existe.
        if (S_ISDIR(st.st_mode)) {
            //printf("\nDiretório '%s' já existe.\n\n", path);
        } else {
            fprintf(stderr, "\n\n'%s' não é um diretório.\n\n", path);
            return 1;
        }
    } else { // o directório não existe, e será criado
        if (mkdir(path, 0777) == 0) {
            printf("\n\nDiretório '%s' criado com sucesso.\n\n", path);
        } else {
            perror("\n\nErro ao criar o diretório\n\n");
            return 1;
        }
    }
    
    return 0;
}

int getLatestDataPath(char* intermedio, char* latestDataPath) {

    char latestFileNamePath[50];
    latestFileNamePath[0] = '\0';
 
    strncat(latestFileNamePath, "../ProcessadorDeDados/", 25);
    strncat(latestFileNamePath, intermedio, 25);
    strncat(latestFileNamePath, "/latestFileName.txt", 20);
 
    FILE* fileLatestFileName = fopen(latestFileNamePath, "r"); // abrir ficheiro para leitura
    if (fileLatestFileName == NULL) { // testar se foi possivel abrir o ficheiro
        printf("\nErro ao abrir o ficheiro!\n");
        fclose(fileLatestFileName);
        return 1; // Erro: não foi possível abrir o ficheiro
    }

    fgets(latestDataPath, 80, fileLatestFileName); // ler conteudo para obter o caminho para o ficheiro onde irá escrever os dados contidos em intermedios

    fclose(fileLatestFileName); // fechar ficheiro

    return 0;
}

int getNumberOfLines(char* latestDataPath) {

    int lines = 0;
    char ch;

    FILE *file = fopen(latestDataPath, "r"); // abrir ficheiro
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
        if ((ch != '\n') & (ch != EOF)) { // validar se o último caracter do ficheiro é um newline
            lines++; // se não for, incrementar a última linha ao nr de linhas
        }
    }

    //printf("(nº linhas @ %s: %02d)\n\n", latestDataPath, lines);

    fclose(file); // fechar ficheiro

    return lines;
}

int addDataToOutputs(char* latestDataPath, Output* outputs) {

    FILE *file = fopen(latestDataPath, "r"); // abrir ficheiro para leitura
    if (file == NULL) { // testar erro ao abrir ficheiro
        perror("Error opening file");
        fclose(file);
        return 1;
    }

    int i = 0;
    char fileLine[80]; // define a dimensão da string de cada linha do ficheiro
    while (fgets(fileLine, sizeof(fileLine), file) != NULL) {

        Output output;

	    char* token = strtok(fileLine, ",");
	    output.outputSensorId = atoi(token); // alocar id

        token = strtok(NULL, ",");
        output.outputWriteCounter = atoi(token); // alocar write counter

        token = strtok(NULL, ",");
        strcpy(output.outputType, token); // alocar type

        token = strtok(NULL, ",");
        strcpy(output.outputUnit, token); // alocar unit

        // outra opção para infra poderia ser de acordo com link:
        // https://www.ibm.com/docs/en/zos/2.1.0?topic=topics-using-decimal-data-types-in-c
        token = strtok(NULL, "#");
        char left[2]; // instanciar bloco dos nrs à esquerda da vírgula
        left[0] = token[0];
        left[1] = token[1];
        char right[2]; // instanciar bloco dos nrs à direita da vírgula
        right[0] = token[2];
        right[1] = token[3];

        output.outputMedian[0] = '\0'; // alocar median
        strncat(output.outputMedian, left, 2);
        strncat(output.outputMedian, ".", 2);
        strncat(output.outputMedian, right, 2);

        outputs[i] = output;
        i++;

    }

    fclose(file);

    return 0;
}

int writeDataToCSV(char* outputPath, Output* outputs, int nSensors) {
    
    char filePath[40]; // iniciar caminho para o outputFile.csv
    filePath[0] = '\0';
    strncat(filePath, outputPath, 20);
    strncat(filePath, "/outputFile.csv", 20);

    FILE *file = fopen(filePath, "w"); // abrir ficheiro para escrita
    if (file == NULL) { // testar erro ao abrir ficheiro
        perror("Error opening file");
        fclose(file);
        return 1;
    }

    fputs("SENSOR_ID,WRITE_COUNTER,TYPE,UNIT,VALUE\n", file);

    fclose(file); // fechar ficheiro

    file = fopen(filePath, "a"); // abrir ficheiro para escrita (append)
    if (file == NULL) { // testar erro ao abrir ficheiro
        perror("Error opening file");
        fclose(file);
        return 1;
    }

    int i = 0;
    while (i < nSensors) {

        char sensorId[8];
        snprintf(sensorId, 8,"%d", outputs[i].outputSensorId);

        char writeCounter[8];
        snprintf(writeCounter, 8,"%d", outputs[i].outputWriteCounter);

        char newline[100]; // iniciar nova linha que será escrita no ficheiro
        newline[0] = '\0';

        strncat(newline, sensorId, 10);
        strncat(newline, ",", 2);
        strncat(newline, writeCounter, 10);
        strncat(newline, ",", 2);
        strncat(newline, outputs[i].outputType, 30);
        strncat(newline, ",", 2);
	    strncat(newline, outputs[i].outputUnit, 14);
        strncat(newline, ",", 2);
        strncat(newline, outputs[i].outputMedian, 10);
        strncat(newline, "\n", 2);

        fputs(newline, file); // escrever uma nova linha no ficheiro

        i++;
    }

    fclose(file); // fechar ficheiro

    return 0;
}
