#include <stdio.h>
#include <string.h>
#include <stdlib.h>

void bubble_sort(int dataset[], int length)
{
	int i = 0;
	int j = 0;
	int temp = 0;

	for (i = 0; i < length; i++)
	{
		for (j = 0; j < length-(i+1); j++)
		{
			if (dataset[j] > dataset[j + 1])
			{
				temp = dataset[j + 1];
				dataset[j + 1] = dataset[j];
				dataset[j] = temp;
			}
		}
	}
}

void insertion_sort(int dataset[], int length)
{
	int i = 0;
	int j = 0;
	int value = 0;

	for (i = 1; i < length; i++)
	{
		if (dataset[i - 1] <= dataset[i])
		{
			continue;
		}
		value = dataset[i];

		for (j = 0; j < i; j++)
		{
			if (dataset[j] > value)
			{
				memmove(&dataset[j + 1], &dataset[j], sizeof(dataset[0]) * (i-j));
				dataset[j] = value;
				break;
			}
		}
	}
}

void swap(int* a, int* b)
{
	int temp = *a;
	*a = *b;
	*b = temp;
}

int partition(int dataset[], int left, int right)
{
	int first = left;
	int pivot = dataset[first];

	++left;

	while (left <= right)
	{
		while ((dataset[left] <= pivot) && (left < right))
		{
			++left;
		}
		while ((dataset[right] >= pivot) && (left <= right))
		{
			--right;
		}

		if (left < right) swap(&dataset[left], &dataset[right]);
		else break;
	}
	swap(&dataset[first], &dataset[right]);

	return right;
}

void quick_sort(int dataset[], int left, int right)
{
	if (left < right)
	{
		int index = partition(dataset, left, right);

		quick_sort(dataset, left, index - 1);
		quick_sort(dataset, index + 1, right);
	}
}

int compare_point(const void* _elem1, const void* _elem2)
{
	int* elem1 = (int*)_elem1;
	int* elem2 = (int*)_elem2;

	if (*elem1 > *elem2)
	{
		return 1;
	}
	else if (*elem1 < *elem2)
	{
		return -1;
	}
	else
	{
		return 0;;
	}
}
