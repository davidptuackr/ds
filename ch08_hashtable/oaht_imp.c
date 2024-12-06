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

	table->data = (OAHT_Node*)calloc(size, sizeof(OAHT_Node));
	if (!table->data)
	{
		printf("TABLE MALLOC FAILED\n");
		exit(1);
	}
	table->size = size;
	table->cnt = 0;
	return table;
}

void OAHT_set(OAHT* table, int chash(OAHT*, char*, int), char* key, char* cname, int pc)
{
	int loc = OAHT_hash(table, key);

	if (table->size / 2 < table->cnt)
	{
		printf("OAHT RE_HASH\n");
		OAHT_re_hash(table);
	}

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
	else
	{
		printf("SET OAHT[%d] : (%s, %s, %d)\n", loc, key, cname, pc);
		table->cnt++;
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
	* RE START : 103124 1630 ~
	*/
	OAHT_Node* p = table->data;

	for (int i = 0; i < table->size; i++)
	{
		if (p->key)
		{
			printf("OAHT[%d] : (KEY : %s, NAME : %s, INT : %d)\n", i, p->key, p->cname, p->pc);
		}
		p++;
	}
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
	return (OAHT_hash(table, key) + mov) % table->size;
}

int OAHT_phash(OAHT* table, char* key, int mov)
{
	return (OAHT_hash(table, key) + (mov * mov)) % table->size;
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
	* RE START : 103124 1630
	*/
	OAHT_Node* original = table->data;

	table->data = (OAHT_Node*)realloc(table->data, sizeof(OAHT_Node) * table->size * 2);
	if (!table->data)
	{
		printf("MEMORY REALLOC FAILED\n");
		table->data = original;
	}
	else
	{
		memset(&(table->data[table->size]), 0, sizeof(OAHT_Node) * table->size);
		printf("OAHT SIZE : %d -> %d\n", table->size, table->size * 2);
		table->size *= 2;
	}
}

void OAHT_test()
{
	OAHT* t = OAHT_create(5);
	OAHT_set(t, OAHT_shash, "A", "ABC", 10);
	OAHT_set(t, OAHT_shash, "i", "EEE", 99);
	OAHT_set(t, OAHT_shash, "ae", "XYZ", 777);
	OAHT_set(t, OAHT_phash, "aQ", "uwu", 111);
	OAHT_set(t, OAHT_phash, "U", "UUU", 222);
	OAHT_set(t, OAHT_xhash, "aaq", "AAQ", 4444);
	OAHT_set(t, OAHT_xhash, "aag", "AAG", 5555);
	OAHT_set(t, OAHT_xhash, "ae", "XYZ", 7775);
	

	OAHT_describe(t);
}