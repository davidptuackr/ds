/*
* LM 022724 0945
* 계획
*	1. 버블 정렬, 삽입 정렬 구현 + 문제분석
*	2. 퀵 정렬 구현 + 문제분석
*/

#ifndef MY_SORTING_IMP_H
#define MY_SORTING_IMP_H

#include <stdlib.h>
#include <stdio.h>
#include <string.h>

void my_bubble_sort(int* data, int len);
void my_insertion_sort(int* data, int len);
void describe(void* data, int len_data);
void my_quick_sort(int* data, int len_data);
void qsort_loop(int* part, int part_start, int part_end);
void my_qsort_p(int* data, int len_data);
void qsort_loop_p(int* part, int* part_start, int* part_end);


#endif // !MY_SORTING_IMP_H


