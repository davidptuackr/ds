#include "sort_def.h"
#include <stdlib.h>
#include <stdio.h>
#include <string.h>

void bubble_sort(int* arr, int size) {
    // Sort the array
    printf("Bubble Sort\n");
    int swap_flag;

    for (int i = 1; i < size; i++)
    {
        swap_flag = 0;
        // note how many loops were done
        printf("Loop %d\n", i);
        for (int j = 0; j < size-i; j++)
        {
            if (*(arr+j) > *(arr+j+1)) // Compare adjacent elements
            {
                swap(arr+j, arr+j+1);
                swap_flag = 1;
            }
        }
        if (swap_flag == 0)
        {
            // If no swaps are made in the inner loop, the array is already sorted
            // notify the user and break the outer loop
            printf("Array is already sorted\n");
            display(arr, size);
            break;
        }
    }
}

void insertion_sort(int* arr, int size)
{
    printf("Insertion Sort\n");

    for (int i = 1; i < size; i++)
    {
        if (*(arr + i - 1) > *(arr + i))
        {
            for (int j = 0; j < i; j++)
            {
                if (*(arr + j) > *(arr + i))
				{
					int temp = *(arr + i);
					memmove(arr + j + 1, arr + j, (i - j) * sizeof(int));
					*(arr + j) = temp;
					break;
				}
            }

        }
    }
}

void quick_sort(int* arr, int left, int right)
{
    if (left >= right)
    {
        return;
    }

    int pivot_idx = partition(arr, left, right);

    quick_sort(arr, left, pivot_idx-1);
    quick_sort(arr, pivot_idx+1, right);
}

int partition(int* arr, int left, int right)
{
    int pivot_idx = left;
    int left_idx = left+1;
    int right_idx = right;
    
    while (left <= right)
    {
        while (*(arr + left_idx) <= *(arr + pivot_idx) && (left_idx < right_idx))
        {
            left_idx++;
        }
        while (*(arr + right_idx) >= *(arr + pivot_idx) && (left_idx <= right_idx))
        {
            right_idx--;
        }

        if (left_idx < right_idx) swap((arr + left_idx), (arr + right_idx));
        else break;
    }
    swap((arr + pivot_idx), (arr + right_idx));

    return right_idx;
}

void swap(int* a, int* b) {
    // Swap two integers
    int temp = *a;
    *a = *b;
    *b = temp;
}

void display(int* arr, int size) {
    // Display the array
    for (int i = 0; i < size; i++) {
        printf("%d ", arr[i]);
    }
    printf("\n");
}

void display2(void* sort_func, int* arr, int size) {
	// Display the array
	void (*sort)(int*, int) = sort_func;
	sort(arr, size);
    display(arr, size);
	printf("\n");
}

int compare(const void* a, const void* b)
{
    return (*(int*)a - *(int*)b);
}

void qsort_test(int* arr, int size)
{
    qsort((void*)arr, (size_t)size, sizeof(int), compare);
}