#include <stdio.h>
#include "cll_imp.h"

void main()
{
	Circular_linked_list* list = create_list();

	append(list, 100, 0);
	append(list, 200, 0);
	append(list, 300, 0);
	describe(list);

	append(list, 111, list->length-1);
	append(list, 222, list->length - 1);
	append(list, 333, list->length - 1);
	describe(list);

	append(list, 1, list->length / 2);
	append(list, 1, list->length / 2);
	append(list, 1, list->length / 2);
	describe(list);

	remove_by_loc(list, 0);
	remove_by_loc(list, list->length - 1);
	describe(list);

	remove_by_data(list, 200);
	remove_by_data(list, 1);
	remove_by_data(list, 333);
	describe(list);

	update_by_loc(list, 0, 123);
	update_by_loc(list, list->length - 1, 321);
	describe(list);

	update_by_data(list, 123, 987);
	update_by_data(list, 321, 789);
	update_by_data(list, 1, 135);
	describe(list);
}