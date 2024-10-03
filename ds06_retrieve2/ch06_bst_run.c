#include <stdlib.h>
#include <stdio.h>
#include "ch06_bst_def.h"
#include <time.h>

int main() {
	BST_Node* tree = NULL;

	srand(time(NULL));

	for (int i = 0; i < 10; i++)
	{
		tree = bst_insert(tree, rand() % 100);
	}

	bst_describe(tree);

	return 0;
}