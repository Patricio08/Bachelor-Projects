#include <string.h> 
#include <stdio.h>

#include "cyclicbarrier.h"
#include "waitblock.h"

typedef struct cyclic_waitblock {
	waitblock_t header;
} cyclic_waitblock_t;

static void init_cyclic_waitblock(cyclic_waitblock_t * cycle_wblock) {
	init_waitblock(&cycle_wblock->header);
}


void cycle_init(cyclicbarrier_t * cycle, int parties)  {
	init_list_head(&cycle->waiters);
	cycle->parties = parties;
	cycle->nwaiters = 0;
}

void cycle_await(cyclicbarrier_t * cycle) {
	
	if (cycle->nwaiters == cycle->parties - 1) {
		printf("\nFree: %d\n", cycle->nwaiters);
		
		cyclic_waitblock_t * waitblock;
	
		list_entry_t * entry;
		list_entry_t * waiters;	
		
		waiters = &cycle->waiters;
		
		
		while (cycle->nwaiters >= 0 && (entry = waiters->next) != waiters)  {
			waitblock = container_of(entry, cyclic_waitblock_t, header.entry);
			printf("\nFreeing....\n");
			cycle->nwaiters = cycle->nwaiters - 1; 
			printf("nwaiters: %d\n\n", cycle->nwaiters);
			remove_list_first(waiters);
			ut_activate(waitblock->header.thread);
		}
		
	} else if (cycle->nwaiters < cycle->parties - 1) {
		
		cyclic_waitblock_t waitblock;
		
		init_cyclic_waitblock(&waitblock);
		cycle->nwaiters = cycle->nwaiters + 1;  
		printf("\nWaiting: %d\n\n", cycle->nwaiters);
		insert_list_last(&cycle->waiters, &waitblock.header.entry);
	
		//
		// Remove the current thread from the ready list.
		//
		ut_deactivate();
	} 
}
