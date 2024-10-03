#include "heap_imp.h"
#include "PriorityQueue.h"

int main()
{
	PriorityQueue* PQ = PQ_Create(3);
	PQNode Popped;

	PQNode Nodes[7] = 
	{
		{34, (void*)"CODING"},
		{12, (void*)"MEETING"},
		{87, (void*)"BREWING COFFEE"},
		{45, (void*)"DOC TYPING"},
		{35, (void*)"DEBUGGING"},
		{66, (void*)"BRUSH TEETH"}
	};

	for (int i = 0; i < 6; i++)
	{
		PQ_Enqueue(PQ, Nodes[i]);
	}

	printf("TASKS ON QUEUE: %d\n", PQ->UsedSize);

	while (!PQ_IsEmpty(PQ))
	{
		PQ_Dequeue(PQ, &Popped);
		printf("TASK: %s(%d)\n", (char*)Popped.Data, Popped.Priority);
	}

	return 0;
}