#include <stdlib.h>
#include <stdio.h>
#include <string.h>

typedef struct _Node
{
	int key;
	struct _Node* L;
	struct _Node* R;
}Node;

Node* get_node(int key)
{
	Node* N = (Node*)malloc(sizeof(Node));
	
	N->key = key;
	N->L = NULL;
	N->R = NULL;

	return N;
}

int main()
{
	Node* n1 = get_node(10);
	Node* n2 = get_node(22);
	Node* n3 = get_node(999);

	n1->L = n2; n1->R = n3;

	Node nx;
	memcpy(&nx, n1, sizeof(Node));
	printf("nx key: %d, L: %p, R: %p\n", nx.key, &(*(nx.L)), &(*(nx.R)));
	printf("n2 mem loc: %p\n", &(*n2));
	printf("n3 mem loc: %p\n", &(*n3));

	printf("%d\n", nx.L->key);
	printf("%d\n", n2->key);

	//Node* ny = NULL;
	Node* ny = (Node*)malloc(sizeof(Node));
	memcpy(ny, n1, sizeof(Node));
	printf("ny key: %d, L: %p, R: %p\n", ny->key, &(*(ny->L)), &(*(ny->R)));
	printf("n2 mem loc: %p\n", &(*n2));
	printf("n3 mem loc: %p\n", &(*n3));

	printf("%d\n", ny->L->key);
	printf("%d\n", n2->key);


	return 0;
}