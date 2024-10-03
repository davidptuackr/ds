#include <stdbool.h>

typedef struct _Node {
	struct _Node* front;
	int data;
	struct _Node* next;
} Node;

typedef struct _Double_linked_list {
	int MAX_LENGTH;
	int length;
	Node* head;
	Node* tail;
} Double_linked_list;

Double_linked_list* create();

bool append(Double_linked_list* list, int data);
bool append_at(Double_linked_list* list, int data, int at);

bool delete(Double_linked_list* list);
bool delete_at(Double_linked_list* list, int at);

Node* seek_by_loc(Double_linked_list* list, int at);
Node* seek_by_data(Double_linked_list* list, int data);

bool update(Double_linked_list* list, int at, int new_data);

void describe(Double_linked_list* list);