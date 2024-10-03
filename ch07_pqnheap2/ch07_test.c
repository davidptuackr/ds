#include "heap_def.h"
#include "pq_def.h"

int main()
{
	/*Heap* h1 = create_heap(1);
	heap_insert(h1, 10);
	heap_insert(h1, 5);
	heap_insert(h1, 4);

	Heap_Node* p = h1->root;
	printf("h1 root loc: %p\n", h1->root);
	for (size_t i = 0; i < 5; i++)
	{
		printf("h1 [%d] loc: %p\n", i, p + i);
	}

	heap_insert(h1, 40);
	heap_insert(h1, 20);

	p = h1->root;
	printf("h1 root loc: %p\n", h1->root);
	for (size_t i = 0; i < 5; i++)
	{
		printf("h1 [%d] loc: %p\n", i, p + i);
	}*/

	PQ* pq = PQ_create(10);
	PQ_insert(pq, 10, "TASK 10");
	PQ_insert(pq, 5, "TASK 5");
	PQ_insert(pq, 2, "TASK 2");
	PQ_insert(pq, 20, "TASK 20");
	PQ_insert(pq, 12, "TASK 12");
	PQ_insert(pq, 23, "TASK 23");
	PQ_insert(pq, 4, "TASK 4");

	PQ_describe(pq);

	return 0;
}