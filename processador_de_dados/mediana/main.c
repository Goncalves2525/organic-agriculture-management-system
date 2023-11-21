#include <stdio.h>
#include "asm.h"

int main(){
    int vec[5] = {1, 2, 3, 4, 5};
    int* ptr = vec;
    int vec2[6] = {1, 2, 3, 4, 5, 6};
    int* ptr2 = vec2;

    printf("Mediana: %d\n", mediana(ptr, 5));

    return 0;
}