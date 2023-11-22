#include <stdio.h>
#include "asm.h"

int main(){
 
    char * s="sensor_id:8#type:atmospheric_temperature#value:21.60#unit:celsius#time:2470030";
	char * t= "sensor_id";
	int o;
	extract_token(s, t, &o);

    printf("Output: %d\n", o);

    return 0;
    
}