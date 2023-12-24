// SaidaDeDados

#ifndef FUNC_H
#define FUNC_H

typedef struct { // definida output struct, em múltiplos de 4
	unsigned int outputSensorId;
	unsigned int outputWriteCounter;
	char outputType[28];
	char outputUnit[12];
	char outputMedian[8];
} Output;

int checkOrCreateDirectory(char* path);
int getLatestDataPath(char* intermedio, char* latestDataPath);
int getNumberOfLines(char* latestDataPath);
int addDataToOutputs(char* latestDataPath, Output* outputs);
int writeDataToCSV(char* outputPath, Output* outputs, int nSensors);

#endif
