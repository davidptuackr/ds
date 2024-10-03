#include <stdio.h>
#include <stdlib.h>
#include "sll_imp.h"

Single_linked_list* new_list()
{
	Single_linked_list* list = (Single_linked_list*) malloc(sizeof(Single_linked_list));
	if (list ==  NULL) return NULL;
	list->length = 0;
	list->head = NULL;
	list->tail = NULL;
	return list;
}

Node* new_node(int data)
{
	Node* node = (Node*) malloc(sizeof(Node));
	if (node == NULL) return NULL;
	node->data = data;
	node->next = NULL;
	return node;
}

int get_length(Single_linked_list* list)
{
	return list->length;
}

void append(Single_linked_list* list, Node* new_node)
{
	if (list->head == NULL)
	{
		list->head = new_node;
		list->tail = new_node;
	}
	else
	{
		list->tail->next = new_node;
		list->tail = new_node;
	}
	list->length++;
}

void append_at(Single_linked_list* list, Node* new_node, int at)
{
	if ((at < 0) || (at > list->length))
	{
		printf("INVALID LOCATION: %d\n", at);
		return;
	}

	if (list->head == NULL)
	{
		list->head = new_node;
		list->tail = new_node;
	}
	else
	{
		Node* iter = list->head;
		int count = at;

		while (count > 0)
		{
			iter = iter->next;
			count--;
		}
		new_node->next = iter->next;
		iter->next = new_node;
		if (at == (list->length-1))
		{
			list->tail = new_node;
		}
	}
	list->length++;
}

void delete(Single_linked_list* list)
{
	if (list->head == NULL)
	{
		printf("UNABLE TO DELETE: EMPTY LIST, BREAK\n");
		return;
	}

	Node* to_delete;

	if (list->head == list->tail)
	{
		to_delete = list->head;
		list->head = NULL;
		list->tail = NULL;

		free(to_delete);
		list->length--;
		return;
	}

	Node* iter = list->head;

	while (iter->next != list->tail)
	{
		iter = iter->next;
	}

	to_delete = iter->next;
	list->tail = iter;
	iter->next = NULL;

	free(to_delete);
	list->length--;
}

void delete_at(Single_linked_list* list, int at)
{
	if ((list->head == NULL))
	{
		printf("UNABLE TO DELETE: EMPTY LIST, BREAK\n");
		return;
	}

	if ((at < 0) || (at >= list->length))
	{
		printf("INVALID LOCATION %d, BREAK\n", at);
		return;
	}

	Node* iter = list->head;
	int count = 0;
	Node* to_delete;

	while (count < at - 1)
	{
		iter = iter->next;
		count++;
	}

	if (at == 0)
	{
		to_delete = list->head;
		list->head = list->head->next;
	}
	else
	{
		to_delete = iter->next;
		iter->next = iter->next->next;
		if (to_delete == list->tail)
		{
			list->tail = iter;
		}
	}
	
	free(to_delete);
	list->length--;
}

Node* seek(Single_linked_list* list, int at)
{
	if (list->head == NULL)
	{
		printf("EMPTY LIST : RETURN NULL\n");
		return NULL;
	}

	if ((at < 0) || (at >= list->length))
	{
		printf("INVALID LOCATION %d : RETURN NULL\n", at);
		return NULL;
	}

	Node* iter = list->head;
	int count = 0;

	while (count != at)
	{
		iter = iter->next;
		count++;
	}

	return iter;
}

void update(Single_linked_list* list, int at, int new_data)
{
	if (list->head == NULL)
	{
		printf("EMPTY LIST : UPDATE STOPPED\n");
		return;
	}

	if ((at < 0) || (at >= list->length))
	{
		printf("INVALID LOCATION %d : UPDATE STOPPED\n", at);
		return;
	}

	Node* iter = list->head;
	int count = 0;

	while (count < at)
	{
		iter = iter->next;
		count++;
	}

	printf("UPDATE DATA IN [%d]: %d --> %d\n", at, iter->data, new_data);
	
	iter->data = new_data;
}

void describe(Single_linked_list* list)
{
	Node* iter = list->head;
	int count = 0;

	while (iter != NULL)
	{
		printf("list[%d]: %d\n", count, iter->data);
		iter = iter->next;
		count++;
	}
	printf("\n\n");
}