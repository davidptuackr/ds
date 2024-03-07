#pragma once
#ifndef MY_RBT_IMP
#define MY_RBT_IMP

#include <stdlib.h>
#include <stdio.h>
#include <stdbool.h>
#include <signal.h>

typedef struct _RBT_Node
{
	struct _RBT_Node* parent;
	struct _RBT_Node* left;
	struct _RBT_Node* right;
	enum {RED, BLACK} color;
	int data;
} RBT_Node;

typedef struct _RBT
{
	RBT_Node* root;
	RBT_Node* nil;
} RBT;

RBT_Node* create_node(int data);
RBT* create_RBT();
bool empty(RBT* tree);

RBT_Node* rbt_search(RBT* tree, int data);
bool rbt_insert(RBT* tree, int data);
RBT_Node* lookup_insert_loc(RBT* tree, int data);
int rbt_delete(RBT* tree, int data);

void rotate_left(RBT* tree, RBT_Node* pivot);
void rotate_right(RBT* tree, RBT_Node* pivot);

void reshape_after_insert(RBT* tree, RBT_Node* node);
void reshape_after_deleete(RBT* tree, RBT_Node* node, RBT_Node* node_parent);



#endif // !MY_RBT_IMP
