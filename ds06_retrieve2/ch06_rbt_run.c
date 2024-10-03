#include "ch06_rbt_def.h"

int main()
{
	RBT* t1 = rbt_create();
	
	/*rbt_insert(t1, 10);
	rbt_insert(t1, 20);
	rbt_insert(t1, 30);
	rbt_insert(t1, 40);
	rbt_insert(t1, 50);
	rbt_insert(t1, 60);
	rbt_insert(t1, 70);
	rbt_insert(t1, 80);
	rbt_insert(t1, 90);
	rbt_insert(t1, 100);*/

	rbt_insert(t1, 100);
	rbt_insert(t1, 90);
	rbt_insert(t1, 80);
	rbt_insert(t1, 70);
	rbt_insert(t1, 60);
	rbt_insert(t1, 50);
	rbt_insert(t1, 40);
	rbt_insert(t1, 30);
	rbt_insert(t1, 20);
	rbt_insert(t1, 10);


	rbt_print(t1);

	int rm = rbt_remove(t1, 10);
	rbt_print(t1);

	rm = rbt_remove(t1, 90);
	rbt_print(t1);

	return 0;
}