#include "cht_def.h"

int CHT_hash(CHT* table, int key)
{
	return key % table->size;
}

CHT* CHT_create(int size)
{
	CHT* table = (CHT*)malloc(sizeof(CHT));
	if (!table)
	{
		printf("FAILED TO MEMORY ALLOC FOR TABLE : ABORT\n");
		exit(1);
	}

	table->data = (CHT_Node**)malloc(sizeof(CHT_Node*) * size);
	if (!table->data)
	{
		printf("FAILED TO MEMORY ALLOC FOR TABLE DATA : ABORT\n");
		exit(1);
	}
	
	for (int i = 0; i < size; i++)
	{
		table->data[i] = NULL;
	}

	table->size = size;
	
	return table;
}

int CHT_set(CHT* table, int key, int value)
{
	int loc = CHT_hash(table, key);
	CHT_Node* list = table->data[loc];

	if (!list)
	{
		list = (CHT_Node*)malloc(sizeof(CHT_Node));
		if (!list)
		{
			printf("FAILED TO MALLOC NODE : ABORT\n");
			exit(1);
		}
		list->key = key;
		list->value = value;
		list->link = NULL;
		table->data[loc] = list;
	}
	else
	{
		CHT_Node* head = (CHT_Node*)malloc(sizeof(CHT_Node));
		if (!head)
		{
			printf("FAILED TO MALLOC NODE : ABORT\n");
			exit(1);
		}

		head->key = key;
		head->value = value;
		head->link = list;
		table->data[loc] = head;
	}
	return loc;
}

int CHT_get(CHT* table, int key)
{
	int loc = CHT_hash(table, key);
	CHT_Node* list = table->data[loc];
	CHT_Node* iter = list;

	while (iter != NULL)
	{
		if (iter->key == key) break;
		else iter = iter->link;
	}

	if (iter == NULL)
	{
		printf("KEY %d NOT IN TABLE\n", key);
		return -1;
	}
	
	return iter->value;
}

int CHT_remove(CHT* table, int key)
{
	int loc = CHT_hash(table, key);
	CHT_Node* list = table->data[loc];
	CHT_Node* iter = list;
	CHT_Node* iter_p = iter;

	while (iter != NULL)
	{
		if (iter->key == key) break;
		else
		{
			iter_p = iter;
			iter = iter->link;
		}
	}
	if (iter == NULL) {
		printf("KEY %d NOT IN TABLE\n", key);
		return -1;
	}
	else iter_p->link = iter->link; 

	int value = iter->value;
	free(iter);
	
	return value;
}

void CHT_describe(CHT* table)
{
	CHT_Node* iter;

	for (int i = 0; i < table->size; i++)
	{
		printf("TABLE[%d] : ", i);
		if (table->data[i])
		{
			iter = table->data[i];
			while (iter != NULL)
			{
				printf("( KEY: %d, VALUE: %d ), ", iter->key, iter->value);
				iter = iter->link;
			}
			printf("\n");
		}
		else
		{
			printf("EMPTY\n");
		}
	}
}

void CHT_test()
{
	CHT* table = CHT_create(10);

	CHT_set(table, 136, 100);
	CHT_set(table, 336, 200);
	CHT_set(table, 8136, 300);
	CHT_set(table, 231, 11);
	CHT_set(table, 331, 22);
	CHT_set(table, 8131, 33);
	CHT_describe(table);

	int n = CHT_get(table, 231);
	printf("GET VALUE: % d\n", n);
	n = CHT_get(table, 331);
	printf("GET VALUE : %d\n", n);
	n = CHT_get(table, 136);
	printf("GET VALUE : %d\n", n);
	n = CHT_get(table, 8136);
	printf("GET VALUE : %d\n", n);

	n = CHT_remove(table, 136);
	printf("REMOVE VALUE : %d\n", n);
	CHT_describe(table);
	n = CHT_remove(table, 336);
	printf("REMOVE VALUE : %d\n", n);
	CHT_describe(table);

	n = CHT_remove(table, 999);
	printf("REMOVE VALUE : %d\n", n);

}