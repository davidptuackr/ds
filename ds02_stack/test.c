#include <stdio.h>
#include <stdlib.h>
#include "stack_imp.h"

int main()
{
    /*Seq_stack* s = NULL;
    create_seq_stack(&s, 5);

    full(s);
    empty(s);

    for (int i = 0; i < 5; i++) push(s, i);
    describe(s);
    printf("IS STACK FULL? : %d\n", full(s));
    printf("IS STACK EMPTY? : %d\n", empty(s));

    printf("\n");

    for (int i = 0; i < 5; i++) pop(s);
    describe(s);
    printf("IS STACK FULL? : %d\n", full(s));
    printf("IS STACK EMPTY? : %d\n", empty(s));*/

    Link_stack* s = NULL;
    create_link_stack(&s);

    ls_empty(s);

    for (int i = 0; i < 5; i++) ls_push(s, i);

    ls_describe(s);
    printf("IS STACK EMPTY? : %d\n", ls_empty(s));

    printf("\n");

    for (int i = 0; i < 5; i++) ls_pop(s);

    ls_describe(s);
    printf("IS STACK EMPTY? : %d\n", ls_empty(s));

    return 0;
}