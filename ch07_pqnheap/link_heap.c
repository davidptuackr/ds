#include <stdio.h>
#include <stdlib.h>

// Define the structure for a node in the binary heap
typedef struct _LH_Node {
    int data;
    struct _LH_Node* parent;
    struct _LH_Node* left;
    struct _LH_Node* right;
} LH_Node;

typedef struct _My_DQ
{
    LH_Node* front;
    LH_Node* tail;
} My_DQ;

typedef struct _My_link_heap
{
    My_DQ* q;
    LH_Node root;
} My_link_heap;

// Function to create a new node
LH_Node* LH_createNode(int data) {
    LH_Node* newNode = (LH_Node*)malloc(sizeof(LH_Node));
    if (newNode == NULL) {
        printf("Memory allocation failed!\n");
        exit(1);
    }
    newNode->data = data;
    newNode->left = NULL;
    newNode->right = NULL;
    return newNode;
}

// Function to LH_insert a new value into the binary heap
LH_Node* LH_insert(LH_Node* root, int data) {
    if (root == NULL)
        return LH_createNode(data);

    if (data <= root->data)
        root->left = LH_insert(root->left, data);
    else
        root->right = LH_insert(root->right, data);

    return root;
}

// Function to extract the minimum value from the binary heap
LH_Node* LH_delete(LH_Node* root) {
    if (root == NULL)
        return NULL;

    if (root->left == NULL)
        return root->right;
    else if (root->right == NULL)
        return root->left;

    if (root->left->data < root->right->data) {
        root->data = root->left->data;
        root->left = LH_delete(root->left);
    }
    else {
        root->data = root->right->data;
        root->right = LH_delete(root->right);
    }

    return root;
}

// Function to recursively print the binary heap in inorder traversal
void describe(LH_Node* root) {
    if (root == NULL)
        return;

    describe(root->left);
    printf("%d ", root->data);
    describe(root->right);
}

