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

void my_quick_sort(int* data, int len_data)
{
	qsort_loop(data, 0, len_data - 1);
}

void qsort_loop(int* part, int part_start, int part_end)
{
	if (part_end - part_start <= 2)
	{
		if (part[part_start] > part[part_end]) {
			swap(&part[part_start], &part[part_end]);
		}
		return;
	}

	int* pivot = part;
	int i_less = part_start + 1;
	int i_big = part_end;

	while (i_big > i_less)
	{
		while ((part[i_less] <= *pivot) && (i_big != i_less))
		{
			i_less++;
		}
		while ((part[i_big] > *pivot) && (i_big != i_less))
		{
			i_big--;
		}
		swap(&part[i_less++], &part[i_big--]);
	}
	swap(&part[i_big], pivot);

	qsort_loop(part, part_start, i_big - 1);
	qsort_loop(&part[i_less], i_less, part_end);
}
