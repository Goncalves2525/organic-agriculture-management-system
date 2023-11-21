#include <stdio.h>
#include "asm.h"

int main(){
    char input[] = "#sensor_id:8#type:atmospheric_temperature#value:21.60#unit:celsius#time:2470030";
    char* inputPtr = input;
    char token[] = "time";
    char* tokenPtr = token;
    int output;

    extract_token(inputPtr, tokenPtr, &output);

    printf("Output: %d\n", output);
    
    return 0;
}