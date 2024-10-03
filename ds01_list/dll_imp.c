#include "dll_imp.h"
#include <stdlib.h>
#include <stdio.h>
#include <stdbool.h>

Double_linked_list* create() 
{
	Double_linked_list* list = malloc(sizeof(Double_linked_list));

	if (!list)
	{
		printf("FAIL TO CREATE LIST : NOT ENOUGH MEMORY\n");
		return NULL;
	}
	list->head = NULL;
	list->tail = NULL;
	list->length = 0;
	list->MAX_LENGTH = 10;

	return list;
}

Node* create_node(int data)
{
	Node* n = (Node*)malloc(sizeof(Node));

	if (n == NULL)
	{
		printf("FAIL TO CREATE NODE : NOT ENOUGH MEMORY\n");
		return NULL;
	}
	n->front = NULL;
	n->data = data;
	n->next = NULL;

	return n;
}

bool empty(Double_linked_list* list)
{
	return list->length == 0;
}

bool full(Double_linked_list* list)
{
	return list->length == list->MAX_LENGTH;
}

bool not_valid_loc(Double_linked_list* list, int at)
{
	return (at < 0) || (at >= list->length);
}

bool append(Double_linked_list* list, int data)
{
	if (full(list))
	{
		printf("FAIL TO APPEND : LIST IS FULL\n");
		return false;
	}

	Node* new_node = create_node(data);

	if (new_node == NULL)
	{
		printf("FAIL TO APPEND : NOT ENOUGH MEMORY FOR NEW NODE\n");
		return false;
	}

	if (empty(list))
	{
		list->head = new_node;
		list->tail = new_node;
	}
	else
	{
		new_node->front = list->tail;
		list->tail->next = new_node;
		list->tail = new_node;
	}
	new_node->data = data;
	list->length++;

	return true;
}

bool append_at(Double_linked_list* list, int data, int at)
{
	if (full(list))
	{
		printf("FAIL TO APPEND : LIST IS FULL\n");
		return false;
	}

	if (not_valid_loc(list, at))
	{
		printf("FAIL TO APPEND : INVALID LOCATION\n");
		return false;
	}

	Node* new_node = create_node(data);

	if (new_node == NULL)
	{
		printf("FAIL TO APPEND : NOT ENOUGH MEMORY FOR NEW NODE\n");
		return false;
	}

	if (empty(list))
	{
		list->head = new_node;
		list->tail = new_node;
	}
	else if (at == 0)
	{
		list->head->front = new_node;
		new_node->next = list->head;
		list->head = new_node;
	}
	else if (at == list->length - 1)
	{
		new_node->front = list->tail;
		list->tail->next = new_node;
		list->tail = new_node;
	}
	else
	{
		int cnt = 0;
		Node* iter = list->head;

		while (cnt < at)
		{
			iter = iter->next;
			cnt++;
		}
		new_node->front = iter->front;
		new_node->next = iter->next;
		iter->next->front = new_node;
		iter->next = new_node;
	}
	new_node->data = data;
	list->length++;

	return true;
}

bool delete(Double_linked_list* list)
{
	if (empty(list))
	{
		printf("FAIL TO DELETE : EMPTY LIST\n");
		return false;
	}

	Node* to_delete = list->tail;

	if (list->length == 1)
	{
		list->head = NULL;
		list->tail = NULL;
	}
	else
	{
		list->tail = list->tail->front;
		list->tail->next = NULL;
	}

	free(to_delete);
	list->length--;
	return true;
}

bool delete_at(Double_linked_list* list, int at)
{
	if (empty(list))
	{
		printf("FAIL TO DELETE : EMPTY LIST\n");
		return false;
	}

	if (not_valid_loc(list, at))
	{
		printf("FAIL TO DELETE : NOT VALID LOCATION %d\n", at);
		return false;
	}

	Node* to_delete = list->head;
	int cnt = 0;

	while (cnt < at)
	{
		to_delete = to_delete->next;
		cnt++;
	}

	if (list->length == 1)
	{
		list->head = NULL;
		list->tail = NULL;
	}
	else if (at == 0)
	{
		list->head = list->head->next;
		list->head->front = NULL;
	}
	else
	{
		list->tail = list->tail->front;
		list->tail->next = NULL;
	}

	free(to_delete);
	list->length--;
	return true;
}

Node* seek_by_loc(Double_linked_list* list, int at)
{
	if (empty(list))
	{
		printf("FAIL TO SEEK : EMPTY LIST\n");
		return NULL;
	}

	if (not_valid_loc(list, at))
	{
		printf("FAIL TO SEEK : NOT VALID LOCATION %d\n", at);
		return NULL;
	}

	Node* iter = list->head;
	int cnt = 0;

	while (cnt < at)
	{
		iter = iter->next;
		cnt++;
	}

	return iter;
}

Node* seek_by_data(Double_linked_list* list, int data)
{
	if (empty(list))
	{
		printf("FAIL TO SEEK : EMPTY LIST\n");
		return NULL;
	}

	Node* iter = list->head;

	while ((iter != NULL) && (iter->data != data))
	{
		iter = iter->next;
	}

	if (iter == NULL)
	{
		printf("FAIL TO SEEK : %d NOT IN LIST\n", data);
	}

	return iter;
}

bool update(Double_linked_list* list, int at, int new_data)
{
	Node* to_update = seek_by_loc(list, at);
	if (to_update == NULL)
	{
		return false;
	}
	to_update->data = new_data;
	return true;
}

void describe(Double_linked_list* list)
{
	if (empty(list))
	{
		printf("EMPTY LIST\n");
		return;
	}
	Node* iter = list->head;
	int cnt = 0;

	while (iter != NULL)
	{
		printf("list[%5d] : %5d\n", cnt, iter->data);
		iter = iter->next;
		cnt++;
	}
	printf("\n");
}