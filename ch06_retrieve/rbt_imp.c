#include "rbt_imp.h"

RBT_Node* create_node(int data)
{
    RBT_Node* node = (RBT_Node*)malloc(sizeof(RBT_Node));

    if (node != NULL)
    {
        node->left = NULL;
        node->right = NULL;
        node->parent = NULL;
        node->color = RED;
        node->data = data;
        
        return node;
    }
    else
    {
        printf("FAIL TO CREATE NEW NODE : NOT ENOUGH MEMORY \n\n");
        return NULL;
    }
}

RBT* create_RBT()
{
    RBT* tree = (RBT*)malloc(sizeof(RBT));

    if (tree != NULL)
    {
        tree->root = NULL;
        tree->nil = NULL;
    }
    else
    {
        printf("FAIL TO CREATE NEW NODE : NOT ENOUGH MEMORY \n\n");
    }

    return tree;
}

bool empty(RBT* tree)
{
    return tree->root == NULL;
}

RBT_Node* rbt_search(RBT* tree, int data)
{
    RBT_Node* node_iter = tree->root;

    while ((node_iter != tree->nil) && (node_iter->data != data))
    {
        if (node_iter->data > data) node_iter = node_iter->left;
        else if (node_iter->data < data) node_iter = node_iter->right; //
        else break;
    }

    return node_iter;
}

RBT_Node* lookup_insert_loc(RBT* tree, int data)
{
    RBT_Node* node_iter = tree->root;
    RBT_Node* node_return = node_iter;

    while (node_iter != tree->nil)// <<<<<
    {
        if (node_iter->data > data) {
            node_return = node_iter;
            node_iter = node_iter->left;
        }
        else {
            node_return = node_iter;
            node_iter = node_iter->right;
        }
    }

    return node_return;
}

bool rbt_insert(RBT* tree, int data)
{
    RBT_Node* node = create_node(data);

    if (node == NULL) return false;
    
    if (empty(tree))
    {
        tree->root = node;
        tree->root->color = BLACK;
        tree->root->left = tree->nil;
        tree->root->right = tree->nil;

        return true;
    }

    RBT_Node* parent    = lookup_insert_loc(tree, data);    // <<<<<
    node->parent        = parent;                           // <<<<<

    if (parent->data > data) parent->left = node;
    else parent->right = node;
    
    reshape_after_insert(tree, node);

    return true;
}

int rbt_delete(RBT* tree, int data)
{
    if (empty(tree))
    {
        printf("EMPTY TREE\n\n");
        return false;
    }

    RBT_Node* to_paste = rbt_search(tree, data);
    RBT_Node* removed;
    RBT_Node* to_handle;
    RBT_Node* to_handle_parent;

    if (to_paste == NULL)
    {
        printf("%d NOT IN TREE, ABORT\n\n", data);
        return -1;
    }
    
    if (to_paste->left == tree->nil || to_paste->right == tree->nil) // case when to_paste has single node for child
    {
        removed = to_paste;
    }
    else
    {
        removed = to_paste->right;
        while (removed->left != tree->nil)
        {
            removed = removed->left;
        }
        to_paste->data = removed->data;
    }

    if (removed->left != tree->nil)
    {
        to_handle = removed->left;
    }
    else
    {
        to_handle = removed->right;
    }

    to_handle->parent = removed->right;

    if (removed->parent == NULL)
    {
        tree->root = to_handle;
    }
    else
    {
        if (removed == removed->parent->left)
        {
            removed->parent->left = to_handle;
        }
        else
        {
            removed->parent->right = to_handle;
        }
    }
    
    if (removed->color == BLACK)
    {
        reshape_after_deleete(tree, to_handle, removed->parent);
    }

    free(removed);

    return 0;
}

void rotate_left(RBT* tree, RBT_Node* pivot)
{
    RBT_Node* pivot_parent = pivot->parent;
    RBT_Node* right_child = pivot->right;
    RBT_Node* right_child_lefts;  // <<<<<
    
    if (right_child->left != tree->nil)
    {
        right_child_lefts = right_child->left;
        right_child_lefts->parent = pivot;
    }
    else
    {
        right_child_lefts = tree->nil;
    }
    pivot->right = right_child_lefts; // <<<<<
    // right_child_lefts->parent = pivot; // <<<<<<

    right_child->left = pivot; // <<<<<
    pivot->parent = right_child;

    right_child->parent = pivot_parent;
    if (pivot != tree->root)
    {
        if (pivot == pivot_parent->left)
        {
            pivot_parent->left = right_child;
        }
        else
        {
            pivot_parent->right = right_child;
        }
    }
    else
    {
        tree->root = right_child;
    }
}

void rotate_right(RBT* tree, RBT_Node* pivot)
{
    RBT_Node* pivot_parent = pivot->parent;
    RBT_Node* left_child = pivot->left;
    RBT_Node* left_child_rights;    // <<<<<

    if (left_child->right != tree->nil)
    {
        left_child_rights = left_child->right;
        left_child_rights->parent = pivot;
    }
    else
    {
        left_child_rights = tree->nil;
    }

    pivot->left = left_child_rights;
    // left_child_rights->parent = pivot;

    left_child->right = pivot;
    pivot->parent = left_child;

    left_child->parent = pivot_parent;
    if (pivot != tree->root)
    {
        if (pivot == pivot_parent->left)
        {
            pivot_parent->left = left_child;
        }
        else
        {
            pivot_parent->right = left_child;
        }
    }
    else
    {
        tree->root = left_child;
    }
}

void reshape_after_insert(RBT* tree, RBT_Node* node)
{
    RBT_Node* to_handle = node;
    RBT_Node* parent;
    RBT_Node* grand;
    RBT_Node* uncle;

    while (to_handle->parent != NULL && to_handle->parent->color == RED)
    {
        parent = to_handle->parent;
        grand = parent->parent;

        if (parent == grand->left)
        {
            uncle = grand->right;

            if ((uncle != tree->nil) && (uncle->color == RED))
            {
                parent->color = BLACK;
                uncle->color = BLACK;
                grand->color = RED;

                to_handle = grand;
            }
            
            else
            {
                if (to_handle == parent->right)
                {
                    rotate_left(tree, parent);
                    parent = to_handle;
                    to_handle = parent->left;
                }
                parent->color = BLACK;
                grand->color = RED;
                rotate_right(tree, grand);
            }
        }
        else
        {
            uncle = grand->left; // <<<<<<<

            if ((uncle != tree->nil) && (uncle->color == RED))
            {
                parent->color = BLACK;
                uncle->color = BLACK;
                grand->color = RED;

                to_handle = grand;
            }
            else
            {
                if (to_handle == parent->right) // <<<<<<<<<<<<
                {
                    rotate_left(tree, parent);
                    parent = to_handle;
                    to_handle = parent->left;
                }
                parent->color = BLACK;
                grand->color = RED;
                rotate_left(tree, grand);
            }
        }
    }
    tree->root->color = BLACK;
}

void reshape_after_deleete(RBT* tree, RBT_Node* node, RBT_Node* node_parent)
{
    RBT_Node* to_handle = node;
    RBT_Node* parent = node_parent;
    RBT_Node* sib;
    RBT_Node* left_cousin;
    RBT_Node* right_cousin;

    while ((to_handle == tree->nil) || (to_handle->color == BLACK && to_handle->parent != NULL)) // <<<<<<
    {
        if (to_handle == tree->nil)
        {
            parent = node_parent;
        }
        else
        {
            parent = to_handle->parent;
        }

        if (to_handle == parent->left)
        {
            sib = parent->right;
            if (sib != tree->nil)
            {
                left_cousin = sib->left;
                right_cousin = sib->right;
            }
            else
            {
                left_cousin = tree->nil;
                right_cousin = tree->nil;
            }
            
            if (sib != tree->nil && sib->color == RED)
            {
                sib->color = BLACK;
                to_handle = parent;
            }
            else
            {
                if (
                    (left_cousin == tree->nil || left_cousin->color == BLACK) &&
                    (right_cousin == tree->nil || right_cousin->color == BLACK) // <<<<<
                    )
                {
                    sib->color = RED;
                    parent->color = BLACK;
                    to_handle = parent;
                }
                else
                {
                    if (
                        (left_cousin != tree->nil && left_cousin->color == RED) &&
                        (right_cousin == tree->nil || right_cousin->color == BLACK)
                        )
                    {
                        sib->color = RED;
                        left_cousin->color = BLACK;
                        rotate_right(tree, sib);
                        
                        sib = parent->right;
                        left_cousin = sib->left;
                        right_cousin = sib->right;
                    }
                    right_cousin->color = BLACK;
                    sib->color = parent->color;
                    parent->color = BLACK;
                    rotate_left(tree, parent);

                    to_handle = tree->root;
                }
            }
            
        }
        else
        {
            sib = parent->left;
            if (sib != tree->nil)
            {
                left_cousin = sib->left;
                right_cousin = sib->right;
            }
            else
            {
                left_cousin = tree->nil;
                right_cousin = tree->nil;
            }

            if (sib != tree->nil && sib->color == RED)
            {
                sib->color = BLACK;
                to_handle = parent;
            }
            else
            {
                if (
                    (left_cousin == tree->nil || left_cousin->color == BLACK) &&
                    (right_cousin == tree->nil || right_cousin->color == BLACK)
                    ) // <<<<<
                {
                    sib->color = RED;
                    parent->color = BLACK;
                    to_handle = parent;
                }
                else
                {
                    if (
                        (left_cousin == tree->nil || left_cousin->color == BLACK) && 
                        (right_cousin != tree->nil || right_cousin->color == RED) // <<<<<
                        )
                    {
                        sib->color = RED;
                        right_cousin->color = BLACK;
                        rotate_left(tree, sib);

                        sib = parent->left;
                        left_cousin = sib->left;
                        right_cousin = sib->right;
                    }
                    left_cousin->color = BLACK;
                    sib->color = parent->color;
                    parent->color = BLACK;
                    rotate_right(tree, parent);

                    to_handle = tree->root;
                }
            }

        }
    }

    tree->root->color = BLACK;
}

void describe(RBT* tree)
{
    RBT_Node* q[100];
    RBT_Node* node_iter;
    q[0] = tree->root;
    int j = 1;
    int lf = 1;

    printf("\n\n");

    for (int i = 0; i < 20; )
    {
        if (q[j] == NULL)
        {
            printf("END\n");
            return;
        }

        node_iter = q[i];
        if (node_iter->left != tree->nil)
        {
            q[j] = node_iter->left; 
            j++;
        }
        if (node_iter->right != tree->nil)
        {
            q[j] = node_iter->right;
            j++;
        }

        printf(" %d(%d) ", node_iter->data, node_iter->color);
        
        i++;
        if (i == lf)
        {
            printf("\n");
            lf *= 2;
        }
    }
}