#pragma once

#include <stdbool.h>

typedef struct _Node
{
	struct _Node* front;
	int data;
	struct _Node* next;
}Node;

typedef struct _Circular_linked_list
{
	Node* head;
	int length;
}Circular_linked_list;

Circular_linked_list* create_list();
Node* create_node(int data);
bool empty(Circular_linked_list* list);
bool not_valid_loc(Circular_linked_list* list, int at);
bool append(Circular_linked_list* list, int data, int at);
bool remove_by_loc(Circular_linked_list* list, int at);
bool remove_by_data(Circular_linked_list* list, int data);
bool update_by_loc(Circular_linked_list* list, int at, int new_data);
bool update_by_data(Circular_linked_list* list, int data, int new_data);
Node* search_by_loc(Circular_linked_list* list, int at, bool ascending_search);
Node* search_by_data(Circular_linked_list* list, int data, bool ascending_search);
void describe(Circular_linked_list* list);
