#include "fold_hashtable_def.h"

Fold_HT* Fold_HT_create(int cnt)
{
	Fold_HT* table = (Fold_HT*)malloc(sizeof(Fold_HT));
	if (!table)
	{
		printf("테이블 생성 실패: 공간 부족, 프로그램 종료\n");
		exit(1);
	}
	
	table->data = (Fold_HT_Node*)malloc(sizeof(Fold_HT_Node) * cnt);
	if (!table->data)
	{
		printf("테이블 공간 할당 실패: 공간 부족, 프로그램 종료\n");
		exit(1);
	}
	memset(table->data, -1, sizeof(Fold_HT_Node) * cnt);

	table->cnt = cnt;
	return table;
}

void Fold_HT_set(Fold_HT* table, char* key, int value)
{
	// int loc = fold_hash(table, key);
	int loc = adv_fold_hash(table, key);

	table->data[loc].key = key;
	table->data[loc].value = value;
}

Fold_HT_Node Fold_HT_get(Fold_HT* table, char* key)
{
	// int loc = fold_hash(table, key);
	int loc = adv_fold_hash(table, key);

	Fold_HT_Node node;
	memcpy(&node, &table->data[loc], sizeof(Fold_HT_Node));

	return node;
}

Fold_HT_Node Fold_HT_remove(Fold_HT* table, char* key)
{
	// int loc = fold_hash(table, key);
	int loc = adv_fold_hash(table, key);

	Fold_HT_Node node;
	memcpy(&node, &table->data[loc], sizeof(Fold_HT_Node));
	memset(&table->data[loc], 0, sizeof(Fold_HT_Node));

	return node;
}
int fold_hash(Fold_HT* table, char* key)
{
	int loc = 0;
	size_t key_size = strlen(key);

	for (size_t i = 0; i < key_size; i++)
	{
		loc = loc + key[i];
	}
	return loc;
}

int adv_fold_hash(Fold_HT* table, char* key)
{
	int loc = 0;
	size_t key_size = strlen(key);

	for (size_t i = 0; i < key_size; i++)
	{
		loc = (loc << 1) + key[i];
	}
	return loc % table->cnt;
}

void Fold_HT_describe(Fold_HT* table)
{
	for (size_t i = 0; i < table->cnt; i++)
	{
		if (table->data[i].key != NULL && table->data[i].value != -1)
		{
			printf("TABLE[ %zd ]: ( KEY: %s, VALUE: %d )\n", i, table->data[i].key, table->data[i].value);
		}
	}
	printf("\n");
}

void fold_ht_test()
{
	Fold_HT* fold_ht = Fold_HT_create(10000);
	
	Fold_HT_set(fold_ht, "starbucks", 100);
	Fold_HT_set(fold_ht, "twosomeplace", 200);
	Fold_HT_set(fold_ht, "composecoffee", 300);
	Fold_HT_set(fold_ht, "megacoffee", 400);
	Fold_HT_set(fold_ht, "oozycoffee", 500);
	Fold_HT_describe(fold_ht);

	Fold_HT_Node node = Fold_HT_get(fold_ht, "composecoffee");
	printf(
		"TABLE[ %d ] ( KEY: %s, VALUE: %d )\n\n",
		// fold_hash(fold_ht, "composecoffee"),
		adv_fold_hash(fold_ht, "composecoffee"),
		node.key, node.value
	);

	node = Fold_HT_remove(fold_ht, "twosomeplace");
	printf(
		"REMOVE : TABLE[ %d ] -> ( KEY: %s, VALUE: %d )\n\n",
		// fold_hash(fold_ht, "twosomeplace"),
		adv_fold_hash(fold_ht, "twosomeplace"),
		node.key, node.value
	);
	Fold_HT_describe(fold_ht);
}