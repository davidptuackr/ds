#include <stdlib.h>
#include <stdio.h>
#include <time.h>
#include "sort_def.h"

int main()
{
    int arr[10];
    int i;

    // Set the seed for random number generation
    srand(273);

    // Generate 10 random integers and store them in the array
    for (i = 0; i < 10; i++) {
        arr[i] = rand() % 100;
    }

    // Display the array before sorting
    printf("Array before sorting: ");
    display(arr, 10);

    // Sort the array
    //bubble_sort(arr, 10);
    //insertion_sort(arr, 10);
    //quick_sort(arr, 0, 9);

    // display2(bubble_sort, arr, 10);

    qsort_test(arr, 10);

    // Display the array after sorting
    printf("\nArray after sorting: ");
    display(arr, 10);

    return 0;
}