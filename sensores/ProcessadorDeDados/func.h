// ProcessadorDados

#ifndef FUNC_H
#define FUNC_H

typedef struct { // definida buffer struct, em múltiplos de 4
	int length;
	int read;
	int write;
	int* bufferArr;
} Buffer;

typedef struct { // definida sensors struct, em múltiplos de 4
	unsigned int sensorId;
	char type[28];
	char unit[12];
	int lastTimestamp;
	int timeout;
	unsigned int write_counter;
	Buffer buffer; 
	int* medianArr;
	int medianArrLength;
} Sensor;

typedef struct { // definida intermedio struct, em múltiplos de 4
	unsigned int intermSensorId;
	unsigned int intermWriteCounter;
	char intermType[28];
	char intermUnit[12];
	int intermMedian;
} Intermedio;

int getNumberOfLines(char *config_file);
int extractConfigOpt(char* configPath, Sensor* sensors, int nSensors);
int getColectorLineData(FILE *coletorFile, int *array);
int getSensorIndex(Sensor* sensors, int nSensors, unsigned int id);
void freeAllocs(Sensor* sensors, int nSensors);
int checkOrCreateDirectory(char *path);
int createIntermSensorsTxt(char *path);
int writeToIntermediosFile(Intermedio *intermedios, char *intermedio, int nSensors);
int moveFromBufferToMedianArr(Buffer buffer, int* medianArr, int medianArrLength);

#endif
