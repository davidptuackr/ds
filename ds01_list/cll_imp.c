#include <stdlib.h>
#include <stdio.h>
#include <stdbool.h>
#include "cll_imp.h"

Circular_linked_list* create_list()
{
	Circular_linked_list* list = (Circular_linked_list*) malloc(sizeof(Circular_linked_list));

	if (list == NULL)
	{
		return NULL;
	}

	list->head = NULL;
	list->length = 0;

	return list;
}

Node* create_node(int data)
{
	Node* node = (Node*)malloc(sizeof(Node));

	if (node == NULL)
	{
		return NULL;
	}

	node->front = NULL;
	node->data = data;
	node->next = NULL;

	return node;
}

bool empty(Circular_linked_list* list)
{
	return list->length == 0;
}
bool not_valid_loc(Circular_linked_list* list, int at)
{
	return (empty(list) && at != 0) && (at < 0 || at >= list->length);
}

bool append(Circular_linked_list* list, int data, int at)
{
	if (not_valid_loc(list, at))
	{
		return false;
	}

	Node* new_node = create_node(data);

	if (empty(list))
	{
		list->head = new_node;
		list->head->next = new_node;
		list->head->front = new_node;
	}
	else
	{
		Node* iter = search_by_loc(list, at, (at < list->length / 2));
		new_node->next = iter;
		new_node->front = iter->front;
		iter->front->next = new_node;
		iter->front = new_node;

		if (at == 0)
		{
			list->head = new_node;
		}
	}
	printf("ADD %5d AT [%3d]\n", data, at);

	list->length++;
	return true;
}

bool remove_by_loc(Circular_linked_list* list, int at)
{
	if (empty(list) || not_valid_loc(list, at))
	{
		return false;
	}

	Node* to_remove = search_by_loc(list, at, at < list->length / 2);
	
	if (at == 0)
	{
		list->head = list->head->next;
	}
	to_remove->front->next = to_remove->next;
	to_remove->next->front = to_remove->front;

	printf("REMOVE DATA AT LIST [%3d] : %5d\n", at, to_remove->data);

	free(to_remove);
	list->length--;

	return true;
}

bool remove_by_data(Circular_linked_list* list, int data)
{
	if (empty(list))
	{
		return false;
	}
	Node* to_remove = search_by_data(list, data, true);

	if (to_remove == NULL)
	{
		return false;
	}

	if (to_remove == list->head)
	{
		list->head = list->head->next;
	}
	to_remove->front->next = to_remove->next;
	to_remove->next->front = to_remove->front;

	printf("REMOVE DATA : %5d\n", data);

	free(to_remove);
	list->length--;

	return true;
}

bool update_by_loc(Circular_linked_list* list, int at, int new_data)
{
	if (empty(list) || not_valid_loc(list, at))
	{
		return false;
	}

	Node* to_update = search_by_loc(list, at, at < list->length / 2);

	printf("UPDATE DATE AT LIST [%3d] : %5d -> %5d\n", at, to_update->data, new_data);
	to_update->data = new_data;

	return true;
}

bool update_by_data(Circular_linked_list* list, int data, int new_data)
{
	if (empty(list))
	{
		return false;
	}

	Node* to_update = search_by_data(list, data, true);

	printf("UPDATE DATA : %5d -> %5d\n", data, new_data);
	to_update->data = new_data;

	return true;
}

Node* search_by_loc(Circular_linked_list* list, int at, bool ascending_search)
{
	if (not_valid_loc(list, at))
	{
		return NULL;
	}

	Node* iter = list->head;
	int cnt = 0;

	if (ascending_search)
	{
		while (cnt < at)
		{
			iter = iter->next;
			cnt++;
		}
	}
	else
	{
		while (cnt < list->length - at)
		{
			iter = iter->front;
			cnt++;
		}
	}

	return iter;
}

Node* search_by_data(Circular_linked_list* list, int data, bool ascending_search)
{
	if (empty(list))
	{
		return NULL;
	}

	Node* iter = list->head;
	int cnt = 0;
	
	if (ascending_search)
	{
		for (cnt = 0; cnt < list->length && iter->data != data; cnt++)
		{
			iter = iter->next;
		}
	}
	else
	{
		for (cnt = 0; cnt < list->length && iter->data != data; cnt++)
		{
			iter = iter->front;
		}
	}

	if (cnt == list->length)
	{
		return NULL;
	}

	return iter;
}

void describe(Circular_linked_list* list)
{
	Node* iter = list->head;
	for (int i = 0; i < list->length; i++)
	{
		printf("LIST[%3d]: %5d\n", i, iter->data);
		iter = iter->next;
	}
	printf("\n\n");
}
