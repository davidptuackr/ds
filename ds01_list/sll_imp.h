#pragma once

typedef struct _Node
{
	int data;
	struct _Node* next;
}Node;

typedef struct _Single_linked_list
{
	int length;
	Node* head;
	Node* tail;
}Single_linked_list;

Single_linked_list* new_list();

Node* new_node(int data);

int get_length(Single_linked_list* list);

void append(Single_linked_list* list, Node* new_node); 
/*
* list�� at �� ��ġ�� ���� (0 <= at <= length-1)
* 0  > at && at > length-1�� ��� ���� ���
* 
* �߰� �� ���� 1 ����
*/

void append_at(Single_linked_list* list, Node* new_node, int at);


void delete(Single_linked_list* list);
void delete_at(Single_linked_list* list, int at);

/*
* list�� at �� ��ġ�� ��� ���� (0 <= at <= length-1)
* 0 > at || at > length-1�� ��� ���� ���
* 
* ���� �� ���� 1 ����
*/

Node* seek(Single_linked_list* list, int at);
/*
* at �� ��ġ ��� Ž��
* 0  > at && at > length-1�� ��� ���� ���
*/

void update(Single_linked_list* list, int at, int new_data);
/*
* at �� ��ġ ����� ������ ����
* 0  > at && at > length-1�� ��� ���� ���
*/

void describe(Single_linked_list* list);

