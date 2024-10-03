#pragma once

typedef struct RBT_Node_
{
	int data;
	struct RBT_Node_* left;
	struct RBT_Node_* right;
	struct RBT_Node_* parent;
	enum { RED, BLACK } color;
} RBT_Node;

typedef struct RBT_
{
	RBT_Node* root;
	RBT_Node* nil;
} RBT;

RBT* rbt_create();
void rbt_rotate_left(RBT* tree, RBT_Node* p);
void rbt_rotate_right(RBT* tree, RBT_Node* p);
void rbt_forced_insert(RBT* tree, int data);
void rbt_print(RBT* tree);
RBT_Node* create_node(int data);

void rbt_insert(RBT* tree, int data);
RBT_Node* insert_helper(RBT_Node* tree, int data);
void reshape_after_insert(RBT* tree, RBT_Node* node);

int rbt_remove(RBT* tree, int data);
RBT_Node* rbt_search(RBT* tree, int data);
RBT_Node* find_rmin(RBT_Node* X);
RBT_Node* remove_helper(RBT* tree, RBT_Node* X);
void reshape_after_remove(RBT* tree, RBT_Node* X, RBT_Node* P);