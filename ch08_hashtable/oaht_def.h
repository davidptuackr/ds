#pragma once
#include <stdlib.h>
#include <stdio.h>
#include <memory.h>

typedef struct _OAHT_Node
{
	char* key;
	char* cname;
	int pc;
} OAHT_Node;

typedef struct _OAHT
{
	OAHT_Node* data;
	int size;
	int cnt;
} OAHT;

OAHT* OAHT_create(int size);
void OAHT_set(OAHT* table, int chash(OAHT*, char*, int), char* key, char* cname, int pc);
OAHT_Node OAHT_get(OAHT* table, int chash(OAHT*, char*, int), char* key);
void OAHT_describe(OAHT* table);
int OAHT_hash(OAHT* table, char* key);
int OAHT_shash(OAHT* table, char* key, int mov);
int OAHT_phash(OAHT* table, char* key, int mov);
int OAHT_xhash(OAHT* table, char* key);
void OAHT_re_hash(OAHT* table);

void OAHT_test();