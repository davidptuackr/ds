#include "my_sorting.h"

void swap(int* a, int* b)
{
	int t = *a;
	*a = *b;
	*b = t;
}

void my_bubble_sort(int* data, int len)
{
	for (int i = 0; i < len - 1; i++)
	{
		for (int j = 0; j < len - i - 1; j++)
		{
			if (data[j] > data[j + 1])
			{
				swap(&data[j], &data[j + 1]);
			}
		}
	}
}

void my_insertion_sort(int* data, int len)
{
	int loc_insert = 0;
	int t = 0;

	for (int i = 1; i < len; i++)
	{
		for (int j = 0; j < i; j++)
		{
			if (data[i] < data[j])
			{
				loc_insert = j;
				t = data[i];
				memmove(&data[loc_insert + 1], &data[loc_insert], sizeof(int) * (i - j));
				data[loc_insert] = t;

				break;
			}
		}
		
	}
}

void describe(void* data, int len_data)
{
	int* p_data = data;

	for (int i = 0; i < len_data; i++)
	{
		printf("DATA[%d]: %d\n", i, *p_data);
		p_data++;
	}
	printf("\n\n");
}