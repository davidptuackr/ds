#include "heap_def.h"
#include <math.h>

Heap* create_heap(int cap)
{
	Heap* heap = (Heap*)malloc(sizeof(Heap));
	heap->root = (Heap_Node*)malloc(sizeof(Heap_Node) * cap);
	heap->cap = cap;
	heap->cnt = 0;
	return heap;
}


//Heap_Node* create_heap_node(int key)
//{
//	Heap_Node* node = (Heap_Node*)malloc(sizeof(Heap_Node));
//	node->key = key;
//	node->L = NULL;
//	node->R = NULL;
//	return node;
//}


int heap_insert(Heap* heap, int key)
{
	// Heap_Node* node = create_heap_node(key);
	if (heap->cap <= heap->cnt) heap_extend(heap);
	
	Heap_Node* iter = heap->root + heap->cnt;
	iter->key = key;
	//iter->L = NULL;
	//iter->R = NULL;

	if (heap->cnt != 0) {
		//Heap_Node* P = heap->root + (heap->cnt - 1) / 2;

		//if (heap->cnt % 2 == 1) P->L = iter;
		//else P->R = iter;

		reshape_after_insert(heap, heap->cnt);
	}

	heap->cnt++;

	return 1;
}

void reshape_after_insert(Heap* heap, int loc)
{
	Heap_Node* I = heap->root + loc;
	
	if (loc > 0)
	{
		int p_loc = (loc - 1) / 2;
		Heap_Node* P = heap->root + p_loc;
		if (P->key > I->key)
		{
			int t = P->key;
			P->key = I->key;
			I->key = t;
			reshape_after_insert(heap, p_loc);
		}
	}
}

void heap_extend(Heap* heap)
{
	heap->root = (Heap_Node*)realloc(heap->root, sizeof(Heap_Node) * heap->cap * 2);
	heap->cap *= 2;
}

int heap_remove(Heap* heap, int key)
{
	int popped = heap->root->key;
	heap->root->key = (heap->root + (heap->cnt - 1))->key;
	Heap_Node* A = (heap->root + (heap->cnt - 1));
	Heap_Node* AP = (heap->root + (heap->cnt - 1) / 2);
	
	if ((heap->cnt - 1) % 2 == 1) AP->L = NULL;
	else AP->R = NULL;
	A = NULL;

	reshape_after_remove(heap, 0);

	heap->cnt--;

	return 0;
}

void reshape_after_remove(Heap* heap, int loc)
{
	Heap_Node* A = heap->root + loc;
	
	int c_loc = (loc * 2 + 1);
	if (c_loc > heap->cap) return;

	Heap_Node* C = heap->root + c_loc;
	if (C == NULL) return;

	
	if (((C + 1) != NULL) && (C->key < (C + 1)->key))
	{
		c_loc++;
		C += 1;
	}

	if (A->key > C->key)
	{
		int t = A->key;
		A->key = C->key;
		C->key = t;
		reshape_after_remove(heap, c_loc);
	}
	
}
