#pragma once

typedef struct _SLNode
{
	int data;
	struct _SLNode* next;
}SLNode;

typedef struct _DLNode
{
	int data;
	struct _DLNode* left;
	struct _DLNode* right;
}DLNode;

typedef struct _SLList
{
	SLNode* head;
	SLNode* tail;
	int cnt;
}SLList;

typedef struct _DLList
{
	DLNode* head;
	DLNode* tail;
	int cnt;
}DLList;

typedef struct _Circular_List
{
	DLNode* head;
	int cnt;
}Circular_List;

SLList* create_list();
SLNode* create_node(int data);
void insert_tail(SLList* list, int data);
void insert_at(SLList* list, int data, int loc);
SLNode* delete_tail(SLList* list);
SLNode* delete_at(SLList* list, int loc);
SLNode* search_node(SLList* list, int loc);
void describe_list(SLList* list);