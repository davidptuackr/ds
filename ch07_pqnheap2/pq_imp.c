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
	PQ_Node* data_original = pq->data;
	pq->data = (PQ_Node*)realloc(pq->data, sizeof(PQ_Node) * pq->cap * 2);

	if (!pq->data)
	{
		printf("PQ REALLOC FAILED: RETURN ORIGINAL PQ\n");
		pq->data = data_original;
		return pq;
	}
	else if (pq->data == data_original)
	{
		printf("REALLOC ON SAME LOC : ");
	}
	else
	{
		printf("REALLOC ON DIFFERENT LOC : ");
	}
	printf("% p -> % p(SIZE: % d -> % d)\n", data_original, &pq->data, pq->cap, pq->cap * 2);
	pq->cap *= 2;
	return pq;
}

void PQ_insert(PQ* pq, int priority, void* data)
{
	if (pq->cnt >= pq->cap)
	{
		printf("PQ FULL: EXTEND\n");
		pq = PQ_extend(pq);
		if (pq->cnt == pq->cap)
		{
			printf("UNABLE TO INSERT: PQ EXTEND FAILED: ABORT\n");
			return;
		}
	}

	int loc = pq->cnt;
	PQ_Node* N = &pq->data[loc];
	PQ_Node* NP = &pq->data[(loc - 1) / 2];
	N->data = data;
	N->priority = priority;
	
	while (loc > 0 && (NP->priority > N->priority))
	{
		swap_node(NP, N);

		loc = (loc - 1) / 2;
		N = &pq->data[loc];
		NP = &pq->data[(loc - 1) / 2];
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

	//memmove(&pq->data[0], &pq->data[pq->cnt - 1], sizeof(PQ_Node));
	pq->data[0] = pq->data[pq->cnt - 1];
	memset(&pq->data[pq->cnt - 1], 0, sizeof(PQ_Node));
	pq->cnt--;

	int loc = 0;
	int c_loc = 1;

	while (c_loc < pq->cnt)
	{
		int left_loc = c_loc;
		int right_loc = c_loc + 1;

		if (left_loc < pq->cnt)
		{
			if (right_loc < pq->cnt)
			{
				if (pq->data[left_loc].priority < pq->data[right_loc].priority) c_loc = left_loc;
				else c_loc = right_loc;
			}
			else c_loc = left_loc;
		}
		
		if (pq->data[loc].priority > pq->data[c_loc].priority)
		{
			swap_node(&pq->data[loc], &pq->data[c_loc]);
			loc = c_loc;
			c_loc = (loc * 2) + 1;
		}
		else break;

	}
}

void PQ_describe(PQ* pq)
{
	printf("\nPQ AT %p\n", pq);
	printf("CAPACITY: %d, %d TASKS IN PQ\n", pq->cap, pq->cnt);

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
	printf("\n");
}

void swap_node(PQ_Node* P, PQ_Node* C)
{
	PQ_Node T;
	memset(&T, 0, sizeof(PQ_Node));

	memcpy(&T, P, sizeof(PQ_Node));
	memcpy(P, C, sizeof(PQ_Node));
	memcpy(C, &T, sizeof(PQ_Node));
}