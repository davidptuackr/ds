#pragma once
#include <stdlib.h>
#include <stdio.h>
#include <string.h>

typedef struct _Fold_HT_Node
{
	char* key;
	int value;
} Fold_HT_Node;

typedef struct _Fold_HT
{
	int cnt;
	Fold_HT_Node* data;
} Fold_HT;

Fold_HT* Fold_HT_create(int cnt);
void Fold_HT_set(Fold_HT* table, char* key, int value);
Fold_HT_Node Fold_HT_get(Fold_HT* table, char* key);
Fold_HT_Node Fold_HT_remove(Fold_HT* table, char* key);
int fold_hash(Fold_HT* table, char* key);
int adv_fold_hash(Fold_HT* table, char* key);
void Fold_HT_describe(Fold_HT* table);
void fold_ht_test();