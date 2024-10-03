#include "pq_def.h"
#include <math.h>

PQ* PQ_create(int cap)
{
	PQ* pq = (PQ*)malloc(sizeof(PQ));
	if (!pq)
	{
		printf("PQ MEMORY ALLOC FAILED, ABORT\n");
		exit(1);
	}

	pq->data = (PQ_Node*)calloc(cap, sizeof(PQ_Node));
	if (!pq->data)
	{
		printf("PQ DATA MEMORY ALLOC FAILED, ABORT\n");
		exit(1);
	}

	pq->cap = cap;
	pq->cnt = 0;

	return pq;
}

PQ* PQ_extend(PQ* pq)
{
	PQ* pq_original = pq;
	pq = (PQ*)realloc(pq, pq->cap * 2);

	if (!pq)
	{
		printf("PQ REALLOC FAILED: RETURN ORIGINAL PQ\n");
		return pq_original;
	}
	else if (pq == pq_original)
	{
		printf("REALLOC ON SAME LOC\n");
	}
	else
	{
		printf("REALLOC ON DIFFERENT LOC\n");
		free(pq_original);
	}
	pq->cap *= 2;
	return pq;
}

void PQ_insert(PQ* pq, int priority, void* data)
{
	if (pq->cnt >= pq->cap)
	{
		printf("PQ FULL: EXTEND\n");
		PQ_extend(pq);
		if (pq->cnt == pq->cap)
		{
			printf("UNABLE TO INSERT: PQ EXTEND FAILED: ABORT\n");
			return;
		}
	}
	PQ_Node* N = &pq->data[pq->cnt];
	if (pq->cnt -1 / 2 < 0)
	{
		return;
	}
	PQ_Node* NP = &pq->data[pq->cnt - 1 / 2];
	N->data = data;
	N->priority = priority;
	
	for (int loc = pq->cnt; loc > 0 && (NP->priority > N->priority); loc = (loc - 1) / 2)
	{
		swap_node(NP, N);
	}

	pq->cnt++;
}

void PQ_remove(PQ* pq)
{
	if (pq->cnt == 0)
	{
		printf("YOU ARE TRYING TO REMOVE EMPTY TREE: ABORT\n");
		return;
	}

	memset(&pq->data[0], 0, sizeof(PQ_Node));
	memmove(&pq->data[0], &pq->data[pq->cnt - 1], sizeof(PQ_Node));
	pq->cnt--;

	int loc = 0;
	int c_loc = 1;

	while (c_loc < pq->cnt-1)
	{
		if (c_loc + 1 < pq->cnt - 1)
		{
			c_loc = pq->data[c_loc].priority > pq->data[c_loc + 1].priority ? c_loc : c_loc + 1;
		}
		
		if (&pq->data[loc].priority > pq->data[c_loc].priority) swap_node(&pq->data[loc], &pq->data[c_loc]);
		else break;

		loc = c_loc;
		c_loc = loc * 2 + 1;
	}
}

void PQ_describe(PQ* pq)
{
	int level = 1;
	for (int i = 0; i < pq->cnt; i++)
	{
		if ((int)pow(2, level) - 1 == i)
		{
			printf("\n");
			level++;
		}
		printf("%s (%d)\t", pq->data[i].data, pq->data[i].priority);
		
	}
}

void swap_node(PQ_Node* P, PQ_Node* C)
{
	PQ_Node* T = (PQ_Node*)malloc(sizeof(PQ_Node));

	if (!T)
	{
		printf("UNABLE TO SWAP: T NODE ALLOC FAILED\n");
		return;
	}

	memcpy(T, P, sizeof(PQ_Node));
	memcpy(P, C, sizeof(PQ_Node));
	memcpy(C, T, sizeof(PQ_Node));
}