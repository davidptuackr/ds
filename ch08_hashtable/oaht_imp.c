#include "oaht_def.h"
#include <string.h>

OAHT* OAHT_create(int size)
{
	OAHT* table = (OAHT*)malloc(sizeof(OAHT));
	if (!table)
	{
		printf("TABLE MALLOC FAILED\n");
		exit(1);
	}

	table->data = (OAHT_Node*)malloc(sizeof(OAHT_Node) * size);
	if (!table->data)
	{
		printf("TABLE MALLOC FAILED\n");
		exit(1);
	}
	table->size = size;
	return table;
}

void OAHT_set(OAHT* table, int chash(OAHT*, char*, int), char* key, char* cname, int pc)
{
	int loc = OAHT_hash(table, key);

	if (table->data[loc].key == 0)
	{
		table->data[loc].key = key;
		table->data[loc].cname = cname;
		table->data[loc].pc = pc;
		return;
	}

	printf("COLLISON : SEEK ANOTHER LOC\n");
	int n = 1;
	while (n <= 10)
	{
		loc = chash(table, key, n);
		if (table->data[loc].key == 0)
		{
			table->data[loc].key = key;
			table->data[loc].cname = cname;
			table->data[loc].pc = pc;
			break;
		}
		n++;
	}
	if (n > 10)
	{
		printf("COLLISION HANDLING FAILED\n");
	}
}

OAHT_Node OAHT_get(OAHT* table, int chash(OAHT*, char*, int), char* key)
{
	OAHT_Node node;
	int loc = OAHT_hash(table, key);
	if (key != table->data[loc].key)
	{
		int n = 0;
		while ((n <= 10) && (key != table->data[loc].key))
		{
			loc = chash(table, key, n);
			n++;
		}
		if (n >= 10)
		{
			printf("%s NOT IN TABLE : ABORT\n", key);
			memset(&node, 0, sizeof(OAHT_Node));
		}
	}
	memcpy(&node, &(table->data[loc]), sizeof(OAHT_Node));
	return node;
}

void OAHT_describe(OAHT* table)
{
	/*
	* NOW ON IMP : ~ 101824 1550
	*/
}

int OAHT_hash(OAHT* table, char* key)
{
	int hash = 0;
	for (int i = 0; i < strlen(key); i++)
	{
		hash = (hash << 1) + key[i];
	}
	return hash % table->size;
}

int OAHT_shash(OAHT* table, char* key, int mov)
{
	return (OAHT_hash(table, key) + mov) & table->size;
}

int OAHT_phash(OAHT* table, char* key, int mov)
{
	return (OAHT_hash(table, key) + (mov * mov)) & table->size;
}

int OAHT_xhash(OAHT* table, char* key)
{
	int moder = (table->size - 2) > 0 ? (table->size - 1) : 1;
	return (OAHT_hash(table, key) % moder - 2) % table->size;
}

void OAHT_re_hash(OAHT* table)
{
	/*
	* NOW ON IMP : ~ 101824 1550
	*/
}