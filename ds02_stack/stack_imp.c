#include "stack_imp.h"
#include <stdio.h>
#include <stdlib.h>

bool create_seq_stack(Seq_stack** stack, int size)
{
    *stack = (Seq_stack*)malloc(sizeof(Seq_stack));
    
    if (*stack == NULL) return false;
    
    (*stack)->head = -1;
    (*stack)->size = size;
    (*stack)->data = (int*)malloc(sizeof(int) * size);

    if ((*stack)->data == NULL) return false; 
    
    return true;
}

bool full(Seq_stack* stack)
{
    return (stack->head + 1) == stack->size;
}

bool empty(Seq_stack* stack)
{
    return stack->head == -1;
}

bool push(Seq_stack* stack, int data)
{
    if (full(stack)) return false;

    int* iter = stack->data + (stack->head + 1);
    *iter = data;
    stack->head += 1;

    return true;
}

int pop(Seq_stack* stack)
{
    if (empty(stack)) return -1;
    
    int* iter = stack->data + stack->head;
    stack->head -= 1;

    return *iter;
}

void describe(Seq_stack* stack)
{
    if (empty(stack))
    {
        printf("EMPTY STACK\n\n");
        return;
    }

    for (int i = stack->head; i > 0; i--)
    {
        printf("STACK[%d] : %d", i, *(stack->data + i));
        if (i == stack->head) printf(" <- TOP\n");
        else printf("\n");
    }
    printf("\n");
}

bool create_link_stack(Link_stack** stack)
{
    *stack = (Link_stack*)malloc(sizeof(Link_stack));
    (*stack)->head = NULL;

    return true;
}

bool create_node(Node** node, int data)
{
    (*node) = (Node*)malloc(sizeof(Node));
    (*node)->data = data;
    (*node)->next = NULL;

    return true;
}

bool ls_empty(Link_stack* stack)
{
    return stack->head == NULL;
}

bool ls_push(Link_stack* stack, int data)
{
    Node* new_node = NULL;
    create_node(&new_node, data);

    if (ls_empty(stack))
    {
        stack->head = new_node;
    }
    else
    {
        new_node->next = stack->head;
        stack->head = new_node;
    }

    return true;
}

int ls_pop(Link_stack* stack)
{
    if (ls_empty(stack)) return -1;

    Node* popped = stack->head;
    int data = popped->data;
    stack->head = stack->head->next;
    free(popped);

    return data;
}

void ls_describe(Link_stack* stack)
{
    if (ls_empty(stack))
    {
        printf("EMPTY STACK\n\n");
        return;
    }
    Node* iter = stack->head;
    for (int i = 0; iter != NULL; i++, iter = iter->next)
    {
        printf("STACK[%d] : %d\n", i, iter->data);
    }
}
