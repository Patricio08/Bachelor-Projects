#include <unistd.h>
#include <stdio.h>

#include "uthread.h"
#include "cyclicbarrier.h"

#define PARTIES 3

void waiter(void * arg) {	
	cyclicbarrier_t * cycle = (cyclicbarrier_t *)arg;
	
	printf("::: Func Started ::: \n");
	printf("::: Func Await ::: \n");
	
	cycle_await(cycle);

	printf("::: Func Wakeup ::: \n");
	printf("::: Func Ended ::: \n");	
}

void cyclicbarrier_test() {
	cyclicbarrier_t cycle;
	
	printf("::: Cyclic Barrier Test - Start ::: \n");
	
	//event_init(&done);
	cycle_init(&cycle, PARTIES);
	
	int i = 0;
	
	for(; i < 5; i++) {
		if(i == 3 || i == 4){
			ut_create(waiter, &cycle, 1);
			continue;
		}
		ut_create(waiter, &cycle, 2);
	}

	printf("::: Cyclic Barrier Test - %d Threads Started ::: \n", i);
	
	ut_run();
	
	printf("::: Cyclic Barrier Test - End ::: \n");
}


int main() {
	ut_init();
	cyclicbarrier_test();
	ut_end();
    return 0;
}
