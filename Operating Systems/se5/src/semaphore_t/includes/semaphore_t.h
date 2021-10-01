#pragma once

#include <stdlib.h>
#include <stdio.h>
#include <stdbool.h>

typedef struct semaphore_t {
	pthread_mutex_t lock;
	pthread_cond_t waiters;
	int units;	
} semaphore_t;


// inicia o semáforo com o número de unidades especificado em initial.
void semaphore_init (semaphore_t *sem, int initial);

// adquire, com timeout infinito, o número de unidades especificado em units.
void semaphore_acquire (semaphore_t *sem, int units);

// adquire, com timeout de millis milisegundos, o número de unidades especificado em units.
bool semaphore_acquire_timed (semaphore_t *sem, int units, long millis);

//entrega o número de unidades ao semáforo especificado em units.
void semaphore_release (semaphore_t *sem, int units);
