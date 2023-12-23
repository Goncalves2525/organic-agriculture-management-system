#include <stdio.h>
#include "asm.h"

int main(){
 
    char * s="sensor_id:8#type:atmospheric_temperature#value:21.60#unit:celsius#time:2470030";
	char * t= "time";
	int o;
    int result;
	result = extract_token(s, t, &o);

    printf("Result: %d\n", result);
    printf("Output: %d\n", o);


    return 0;
    
}