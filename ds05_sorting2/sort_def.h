#pragma once

void bubble_sort(int* arr, int size);
void insertion_sort(int* arr, int size);
void quick_sort(int* arr, int left, int right);
int partition(int* arr, int left, int right);
void swap(int* a, int* b);
void display(int* arr, int size);
void display2(void* sort_func, int* arr, int size);
void qsort_test(int* arr, int size);