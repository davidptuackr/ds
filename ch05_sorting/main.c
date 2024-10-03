#include "my_sorting.h"

int main()
{
	int data[] = { 2, 9, 7, 10, 4, 0, 1, 8 };
	const int LEN_DATA = sizeof(data) / sizeof(int);
	describe(data, 8);

	//my_bubble_sort(data, 8);
	//my_insertion_sort(data, 8);
	//my_quick_sort(data, 8);
	my_qsort_p(data, LEN_DATA);
	describe(data, 8);

	return 0;
}