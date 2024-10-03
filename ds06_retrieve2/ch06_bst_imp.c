#include "ch06_bst_def.h"
#include <stdio.h>
#include <stdlib.h>

BST_Node* bst_insert(BST_Node* root, int data)
{
	if (root == NULL)
	{
		root = (BST_Node*)malloc(sizeof(BST_Node));
		root->data = data;
		root->left = NULL;
		root->right = NULL;
	}
	else
	{
		if (data < root->data)
		{
			root->left = bst_insert(root->left, data);
		}
		else
		{
			root->right = bst_insert(root->right, data);
		}
	}
	return root;
}

BST_Node* bst_remove(BST_Node* sub_root, BST_Node* sub_parent, int data)
{
	BST_Node* to_rm = sub_root;

	if (to_rm == NULL)
	{
		printf("Data not found\n");
		return NULL;
	}
	else
	{
		if (to_rm->data > data) 
			to_rm = bst_remove(to_rm->left, to_rm, data);
		else if (to_rm->data < data) 
			to_rm = bst_remove(to_rm->right, to_rm, data);
		else
		{
			if (to_rm->left == NULL && to_rm->right == NULL)
			{
				if (sub_parent->left == to_rm) sub_parent->left = NULL;
				else sub_parent->right = NULL;
			}
			else if (to_rm->left == NULL)
			{
				if (sub_parent->left == to_rm) 
					sub_parent->left = to_rm->right;
				else
					sub_parent->right = to_rm->right;
			}
			else if (to_rm->right == NULL)
			{
				if (sub_parent->left == to_rm)
					sub_parent->left = to_rm->left;
				else
					sub_parent->right = to_rm->left;
			}
			else
			{
				BST_Node* temp = to_rm->right;
				while (temp->left != NULL)
				{
					temp = temp->left;
				}
				to_rm->data = temp->data;
				to_rm->right = bst_remove(to_rm->right, to_rm, temp->data);
			}
		}
	}
	return to_rm;
}

BST_Node* bst_search(BST_Node* root, int data)
{
	if (root == NULL)
	{
		printf("Data not found\n");
		return NULL;
	}
	else
	{
		if (root->data > data) return bst_search(root->left, data);
		else if (root->data < data) return bst_search(root->right, data);
		else return root;
	}
}

void bst_describe(BST_Node* root)
{
	if (root == NULL) return;
	bst_describe(root->left);
	printf("%d\n", root->data);
	bst_describe(root->right);
}