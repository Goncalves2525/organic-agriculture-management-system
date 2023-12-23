#include <stdio.h>
#include "asm.h"

int main(){
    int vec[] = {3, 4, 7, 1, 9, 2};
    int* ptr = vec;

    sort_array(ptr, 6);

    for(int i = 0; i < 6; i++){
        printf("%d ", vec[i]);
    }
    
    return 0;
}