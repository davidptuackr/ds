#include "rbt_imp.h"
#include <time.h>

int main() {
    
    RBT* tree = create_RBT();

    srand(time(NULL));

    int to_dels[5];
    int to_in;

    for (int i = 0; i < 20; i++)
    {
        to_in = rand() % 100;
        rbt_insert(tree, to_in);
        to_dels[i % 5] = to_in;
    }

    describe(tree);

    for (int i = 0; i < 5; i ++)
    {
        rbt_delete(tree, to_dels[i]);
    }
    
    describe(tree);

    return 0;
}
