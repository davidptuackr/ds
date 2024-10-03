#pragma once
#include <stdlib.h>
#include <stdio.h>
#include <string.h>

typedef struct _Heap_Node
{
	int key;
	struct _Heap_Node* L;
	struct _Heap_Node* R;
} Heap_Node;

typedef struct _Heap
{
	Heap_Node* root;
	int cap;
	int cnt;
} Heap;

Heap* create_heap(int cap);

//Heap_Node* create_heap_node(int key);
int heap_insert(Heap* heap, int key);
void reshape_after_insert(Heap* heap, int loc);

int heap_remove(Heap* heap, int key);
void reshape_after_remove(Heap* heap, int loc);

void heap_extend(Heap* heap);
void heap_shrink(Heap* heap);