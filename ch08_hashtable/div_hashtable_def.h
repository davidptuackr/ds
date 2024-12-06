#pragma once
#include <stdlib.h>
#include <stdio.h>
#include <memory.h>

typedef struct _HT_Node
{
	int key;
	int value;
} HT_Node;

typedef struct _HT
{
	int cnt;
	HT_Node* data;
} HT;

HT* HT_create(int size);
void HT_set(HT* table, int key, int value);
HT_Node HT_get(HT* table, int key);
HT_Node HT_remove(HT* table, int key);
void HT_describe(HT* table);
void div_ht_test();