#include "list_prototype.h"

void main()
{
	SLList* sllist = create_list();
	insert_tail(sllist, 100);
	insert_tail(sllist, 20);
	insert_tail(sllist, 70);
	insert_tail(sllist, 55);
	insert_tail(sllist, 40);
	
	/*
	describe_list(sllist);

	insert_at(sllist, 0, 0);
	describe_list(sllist);

	insert_at(sllist, 7777, 1);
	describe_list(sllist);

	insert_at(sllist, 9999, sllist->cnt-1);
	describe_list(sllist);

	insert_at(sllist, 9999, 9999);
	describe_list(sllist);
	
	delete_tail(sllist);
	describe_list(sllist);

	delete_tail(sllist);
	describe_list(sllist);
	*/

	delete_at(sllist, 0);
	describe_list(sllist);

	delete_at(sllist, sllist->cnt - 1);
	describe_list(sllist);
}