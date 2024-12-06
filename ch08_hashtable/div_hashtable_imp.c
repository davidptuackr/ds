#include "div_hashtable_def.h"

HT* HT_create(int size)
{
	HT* table = (HT*)malloc(sizeof(HT));
	if (!table)
	{
		printf("테이블 생성 실패: 공간 부족, 프로그램 종료\n");
		exit(1);
	}
	
	table->data = (HT_Node*)calloc(size, sizeof(HT_Node));
	if (!table->data)
	{
		printf("테이블 공간 할당 실패: 공간 부족, 프로그램 종료\n");
		exit(1);
	}

	table->cnt = size;
	return table;
}

void HT_set(HT* table, int key, int value)
{
	int loc = HT_div_hash(table, key);

	table->data[loc].key = key;
	table->data[loc].value = value;
}

HT_Node HT_get(HT* table, int key)
{
	int loc = HT_div_hash(table, key);
	
	return table->data[loc];
}

HT_Node HT_remove(HT* table, int key)
{
	int loc = HT_div_hash(table, key);
	HT_Node node;
	memcpy(&node, &table->data[loc], sizeof(HT_Node));
	memset(&table->data[loc], 0, sizeof(HT_Node));

	return node;
}

int HT_div_hash(HT* table, int key)
{
	return key % table->cnt;
}

void HT_describe(HT* table)
{
	for (int i = 0; i < table->cnt; i++)
	{
		if (table->data[i].key != 0 && table->data[i].value != 0)
		{
			printf("TABLE[ %d ]: ( KEY: %d, VALUE: %d )\n", i, table->data[i].key, table->data[i].value);
		}
	}
}

void div_ht_test()
{
	HT* table = HT_create(13);

	HT_set(table, 81, 100);
	HT_set(table, 31, 200);
	HT_set(table, 130, 300);
	HT_set(table, 46, 400);
	HT_set(table, 152, 500);
	HT_describe(table);

	HT_Node node;
	node = HT_get(table, 152);
	printf("\n%d, %d\n", node.key, node.value);

	HT_remove(table, 46);
	HT_describe(table);
}