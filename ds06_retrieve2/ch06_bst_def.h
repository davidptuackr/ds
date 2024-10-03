#include <stdio.h>
#include <stdlib.h>

typedef struct BST_Node_
{
	int data;
	struct BST_Node_* left;
	struct BST_Node_* right;
} BST_Node;

BST_Node* bst_insert(BST_Node* root, int data);
BST_Node* bst_remove(BST_Node* sub_root, BST_Node* sub_parent, int data);
BST_Node* bst_search(BST_Node* root, int data);
void bst_describe(BST_Node* root);