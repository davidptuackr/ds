#include <stdio.h>
#include <stdlib.h>
#include <math.h>
#include "ch06_rbt_def.h"

RBT* rbt_create()
{
	RBT* tree = (RBT*)malloc(sizeof(RBT));
	tree->root = NULL;
	tree->nil = NULL;
	return tree;
}

RBT_Node* create_node(int data)
{
	RBT_Node* node = (RBT_Node*)malloc(sizeof(RBT_Node));
	node->data = data;
	node->left = NULL;
	node->right = NULL;
	node->parent = NULL;
	node->color = RED;
	return node;
}

void rbt_rotate_left(RBT* tree, RBT_Node* p)
{
	RBT_Node* R = p->right;
	RBT_Node* G = p->parent;
	RBT_Node* RL = p->right->left;

	p->right = RL;
	if (RL != NULL) RL->parent = p;

	R->parent = G;
	if (G != NULL)
	{
		if (p == G->left) G->left = R;
		else G->right = R;
	}
	else
	{
		tree->root = R;
	}

	R->left = p;
	p->parent = R;
}
void rbt_rotate_right(RBT* tree, RBT_Node* p)
{
	RBT_Node* L = p->left;
	RBT_Node* G = p->parent;
	RBT_Node* LR = p->left->right;

	p->left = LR;
	if (LR != NULL) LR->parent = p;

	L->parent = G;
	if (G != NULL)
	{
		if (p == G->left) G->left = L;
		else G->right = L;
	}
	else
	{
		tree->root = L;
	}

	L->right = p;
	p->parent = L;
}
void rbt_forced_insert(RBT* tree, int data)
{
	if (tree->root == NULL)
	{
		tree->root = create_node(data);
		tree->root->color = BLACK;
		return;
	}
	RBT_Node* parent = insert_helper(tree->root, data);
	RBT_Node* node = create_node(data);
	node->parent = parent;
	if (parent->data >= data)	parent->left = node;
	else						parent->right = node;
	node->left = tree->nil;
	node->right = tree->nil;
	
	// reshape_after_insert(node);

	tree->root->color = BLACK;
}

void rbt_print(RBT* tree)
{
	RBT_Node* q[100];
	RBT_Node** p = q;
	RBT_Node** insert_loc = q;
	int i_pow = 0;

	*p = tree->root;
	insert_loc++;

	while (p != insert_loc)
	{
		if ((*p)->left != NULL)
		{
			*insert_loc = (*p)->left;
			insert_loc++;
		}
		if ((*p)->right != NULL)
		{
			*insert_loc = (*p)->right;
			insert_loc++;
		}
		p++;
	}

	p = q;

	for (size_t i = 0; p != insert_loc; i++)
	{
		printf("%d: %s", (*p)->data, (*p)->color == RED ? "R" : "B");
		if ((*p)->parent != NULL) printf(" (%d: %s)\n", (*p)->parent->data, (*p)->parent->color == RED ? "R" : "B");
		else printf(" (ROOT)\n");

		/*if (i == (int)pow(2.0, (double)i_pow))
		{
			printf("\n");
			i_pow++;
		}*/
		p++;
	}
}

RBT_Node* insert_helper(RBT_Node* tree, int data)
{
	if (tree == NULL)	return NULL;
	if (tree->data >= data)
	{
		if (tree->left == NULL)	return tree;
		else					return insert_helper(tree->left, data);
	}
	else
	{
		if (tree->right == NULL)	return tree;
		else						return insert_helper(tree->right, data);
	}
}

void rbt_insert(RBT* tree, int data)
{
	RBT_Node* X = create_node(data);

	if (tree->root == NULL)
	{
		tree->root = X;
	}
	else
	{
		RBT_Node* P = insert_helper(tree->root, data);
		
		X->parent = P;
		if (P->data >= data) P->left = X;
		else P->right = X;

		reshape_after_insert(tree, X);
	}
}



void reshape_after_insert(RBT* tree, RBT_Node* X)
{
	RBT_Node* P = X->parent;
	RBT_Node* U;
	RBT_Node* G;

	if (P != NULL && P->color == RED) // P == NULL이면 X == root
	{
		G = P->parent;

		if (G != NULL)
		{
			if (G->left == P)
			{
				U = G->right;

				if (U != NULL && U->color == RED)
				{
					P->color = BLACK;
					U->color = BLACK;
					G->color = RED;
					reshape_after_insert(tree, G);
				}
				else
				{
					if (X == P->right)
					{
						rbt_rotate_left(tree, P);
						reshape_after_insert(tree, P);
					}
					P->color = BLACK;
					G->color = RED;
					rbt_rotate_right(tree, G);
				}
			}
			else
			{
				U = G->left;

				if (U != NULL && U->color == RED)
				{
					P->color = BLACK;
					U->color = BLACK;
					G->color = RED;
					reshape_after_insert(tree, G);
				}
				else
				{
					if (X == P->left)
					{
						rbt_rotate_right(tree, P);
						reshape_after_insert(tree, P);
					}
					else
					{
						P->color = BLACK;
						G->color = RED;
						rbt_rotate_left(tree, G);
					}
				}
			}
		}
		else
		{
			tree->root = P;
		}
	}
	tree->root->color = BLACK;
}

int rbt_remove(RBT* tree, int data)
{
	RBT_Node* rm = rbt_search(tree, data);
	int rm_data = rm->data;

	RBT_Node* rmin = remove_helper(tree, rm);
	RBT_Node* X = rmin->right;
	// if (X == NULL) return NULL;

	if (rmin->color == BLACK) reshape_after_remove(tree, X, rmin->parent);

	return rm_data;
}

RBT_Node* rbt_search(RBT* tree, int data)
{
	RBT_Node* X = tree->root;

	while (X != NULL && X->data != data)
	{
		if (X->data > data) X = X->left;
		else X = X->right;
	}

	if (X == NULL) printf("%d not in tree\n", data);
	else printf("found %d : %s (%d : %s)",
		X->data,
		X->color == RED ? "RED" : "BLACK",
		X->parent != NULL ? X->parent->data : -1,
		X->parent != NULL ? (X->parent->color == RED ? "RED" : "BLACK") : "ROOT"
		);
	return X;
}

RBT_Node* remove_helper(RBT* tree, RBT_Node* rm)
{
	RBT_Node* rmin = (rm->right != NULL) ? find_rmin(rm->right) : rm;
	if (rmin == NULL) return NULL;
	
	rm->data = rmin->data;

	RBT_Node* X = rmin->right;
	if (X != NULL)
	{
		X->parent = rmin->parent;
		if (rmin->color == BLACK) X->color = BLACK;
	}
	
	if (rmin == rm->right) rmin->parent->right = X;
	else rmin->parent->left = X;
	
	return rmin;
}

RBT_Node* find_rmin(RBT_Node* X)
{
	if (X != NULL && X->left != NULL) return find_rmin(X->left);
	else return X;
}

void reshape_after_remove(RBT* tree, RBT_Node* X, RBT_Node* P)
{
	if (X == tree->root && X == NULL)
	{
		// 지운 것이 루트면서 트리에 원소가 루트밖에 없는 경우
		return;
	}

	RBT_Node* S;
	RBT_Node* CL;
	RBT_Node* CR;

	if ((X == NULL || X->color == BLACK) && X != tree->root)
	{
		if (X == P->left)
		{
			S = P->right;
			CL = S->left;
			CR = S->right;

			if (S != NULL && S->color == RED)
			{
				S->color = BLACK;
				P->color = RED;
				rbt_rotate_left(tree, P);
				reshape_after_remove(tree, X, P);
			}
			else
			{
				if (
					(CL == NULL && CR == NULL) || 
					((CL != NULL && CL->color == BLACK) && (CR != NULL && CR->color == BLACK))
				)
				{
					S->color = RED;
					reshape_after_remove(tree, P, P->parent);
				}
				else if (
					((CL != NULL && CL->color == RED) && (CR == NULL || CR->color == BLACK))
					)
				{
					CL->color = BLACK;
					S->color = RED;
					rbt_rotate_right(tree, S);
					reshape_after_remove(tree, X, P);
				}
				else
				{
					CR->color = BLACK;
					S->color = P->color;
					rbt_rotate_left(tree, P);
					reshape_after_remove(tree, tree->root, tree->root->parent);
				}
			}
		}
		else
		{
			S = P->left;
			CL = S->left;
			CR = S->right;

			if (S != NULL && S->color == RED)
			{
				S->color = BLACK;
				P->color = RED;
				rbt_rotate_right(tree, P);
				reshape_after_remove(tree, X, P);
			}
			else
			{
				if (
					(CL == NULL && CR == NULL) ||
					((CL != NULL && CL->color == BLACK) && (CR != NULL && CR->color == BLACK))
					)
				{
					S->color = RED;
					reshape_after_remove(tree, P, P->parent);
				}
				else if (
					((CR != NULL && CR->color == RED) && (CL == NULL || CL->color == BLACK))
					)
				{
					CR->color = BLACK;
					S->color = RED;
					rbt_rotate_left(tree, S);
					reshape_after_remove(tree, X, P);
				}
				else
				{
					CL->color = BLACK;
					S->color = P->color;
					rbt_rotate_right(tree, P);
					reshape_after_remove(tree, tree->root, tree->root->parent);
				}
			}
		}

	}
	tree->root->color = BLACK;
}