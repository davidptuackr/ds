#include <stdlib.h>
#include <stdio.h>
#include <memory.h>

typedef struct _PQ_Node
{
	int priority;
	void* data;
} PQ_Node;

typedef struct _PQ
{
	int cap;
	int cnt;
	PQ_Node* data;
} PQ;



PQ* PQ_create(int cap);
PQ* PQ_extend(PQ* pq);
void PQ_insert(PQ* pq, int priority, void* data);
void PQ_remove(PQ* pq);
void PQ_describe(PQ* pq);
void swap_node(PQ_Node* P, PQ_Node* C);