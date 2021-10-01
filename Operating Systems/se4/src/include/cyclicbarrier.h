#pragma once

#include "uthread.h"
#include "list.h"

typedef struct cyclicbarrier {
	list_entry_t waiters;
	int nwaiters;
	int parties;
} cyclicbarrier_t;


void cycle_init(cyclicbarrier_t * cycle, int parties);

void cycle_await(cyclicbarrier_t * cycle);
