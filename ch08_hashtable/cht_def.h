#pragma once

#include <stdlib.h>
#include <stdio.h>
#include <memory.h>

typedef struct _CHT_Node
{
	int key;
	int value;
	struct _CHT_Node* link;
} CHT_Node;

typedef struct _CHT
{
	CHT_Node** data;
	int size;
} CHT;

CHT* CHT_create(int size);
int CHT_set(CHT* table, int key, int value);
int CHT_get(CHT* table, int key);
int CHT_remove(CHT* table, int key);
void CHT_describe(CHT* table);
void CHT_test();