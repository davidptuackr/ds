#include <stdbool.h>

typedef struct _Seq_stack
{
	int head;
	int size;
	int* data;
}Seq_stack;

bool create_seq_stack(Seq_stack** stack, int size);
bool full(Seq_stack* stack);
bool empty(Seq_stack* stack);
bool push(Seq_stack* stack, int data);
int pop(Seq_stack* stack);
void describe(Seq_stack* stack);

typedef struct _Node
{
	int data;
	struct _Node* next;
}Node;

typedef struct _Link_stack
{
	Node* head;
}Link_stack;

bool create_link_stack(Link_stack** stack);
bool create_node(Node** node, int data);
bool ls_empty(Link_stack* stack);
bool ls_push(Link_stack* stack, int data);
int ls_pop(Link_stack* stack);
void ls_describe(Link_stack* stack);
