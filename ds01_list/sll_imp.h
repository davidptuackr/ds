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
* list의 at 번 위치에 삽입 (0 <= at <= length-1)
* 0  > at && at > length-1일 경우 동작 취소
* 
* 추가 후 길이 1 증가
*/

void append_at(Single_linked_list* list, Node* new_node, int at);


void delete(Single_linked_list* list);
void delete_at(Single_linked_list* list, int at);

/*
* list의 at 번 위치의 노드 제거 (0 <= at <= length-1)
* 0 > at || at > length-1일 경우 동작 취소
* 
* 제거 후 길이 1 감소
*/

Node* seek(Single_linked_list* list, int at);
/*
* at 번 위치 노드 탐색
* 0  > at && at > length-1일 경우 동작 취소
*/

void update(Single_linked_list* list, int at, int new_data);
/*
* at 번 위치 노드의 데이터 갱신
* 0  > at && at > length-1일 경우 동작 취소
*/

void describe(Single_linked_list* list);

