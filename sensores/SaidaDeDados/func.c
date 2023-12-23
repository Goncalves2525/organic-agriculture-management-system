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
            fprintf(stderr, "\n'%s' não é um diretório.\n\n", path);
            return 1;
        }
    } else { // o directório não existe, e será criado
        if (mkdir(path, 0777) == 0) {
            printf("\nDiretório '%s' criado com sucesso.\n\n", path);
        } else {
            perror("\nErro ao criar o diretório\n\n");
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
        if (ch != '\n') { // validar se o último caracter do ficheiro é um newline
            lines++; // se não for, incrementar a última linha ao nr de linhas
        }
    }

    printf("(nº linhas @ %s: %02d)\n\n", latestDataPath, lines);

    fclose(file); // fechar ficheiro

    return lines;
}

int addDataToOutputs(char* latestDataPath, Output* outputs, int nSensors) {
    // fazer função
    return 0;
}

int writeDataToCSV(char* outputPath, Output* outputs, int nSensors) {
    // fazer função
    return 0;
}
