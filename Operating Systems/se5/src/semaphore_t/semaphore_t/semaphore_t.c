#include <stdlib.h>
#include <stdio.h>
#include <pthread.h>
#include <stdbool.h>
#include <errno.h>
#include <sys/time.h>
#include <time.h>

#include "../includes/semaphore_t.h"


// inicia o semáforo com o número de unidades especificado em initial.
void semaphore_init (semaphore_t *sem, int initial){
	pthread_mutex_init(&(sem->lock), NULL);
	pthread_cond_init(&(sem->waiters), NULL);
	
	sem->units = initial;
}

// adquire, com timeout infinito, o número de unidades especificado em units.
void semaphore_acquire (semaphore_t *sem, int units){
	
	printf("\n::: SEMAPHORE UNITS = %d :::\n", sem->units);
	printf("Requested Units = %d\n", units);	
	pthread_mutex_lock(&(sem->lock));
	{
		
	
		while( sem->units < units ) {
			printf("-- waiting on loop --\n");	
			pthread_cond_wait(&(sem->waiters), &(sem->lock));		
		}
	
		if( sem->units >= units) {
			printf("Removed! Units = %d\n", units);
			sem->units -= units;
			printf("::: SEMAPHORE UNITS = %d :::\n", sem->units);
		}	
	}
	pthread_mutex_unlock(&(sem->lock));
	
}


// adquire, com timeout de millis milisegundos, o número de unidades especificado em units.
bool semaphore_acquire_timed (semaphore_t *sem, int units, long millis) {

	struct timespec time;

	pthread_mutex_lock(&(sem->lock));
	{
		
		clock_gettime(CLOCK_REALTIME, &time);
		time_t tt = millis / 1000;
		time.tv_sec += tt;
		time.tv_nsec += (millis%1000)*1000000;

		printf("\n::: SEMAPHORE UNITS = %d :::\n", sem->units);
		printf("Requested Units = %d\n", units);		
	
		while( sem->units < units ) {
			printf("::: WAITING IN LOOP (Requested %d):::\n",units);
			printf("::: SEMAPHORE UNITS = %d :::\n", sem->units);
			if(pthread_cond_timedwait(&(sem->waiters), &(sem->lock), &time) == ETIMEDOUT) {		
				printf("::: TIMEOUT -> Requested %d :::\n", units);
				break;

			}
		}

		if(sem->units >= units) {
			printf("Removed! Units = %d\n", units);
			sem->units -= units;
			printf("::: SEMAPHORE UNITS = %d :::\n", sem->units);
		}
	}
	pthread_mutex_unlock(&(sem->lock));

	return false;
}

//entrega o número de unidades ao semáforo especificado em units.
void semaphore_release (semaphore_t *sem, int units){
	
	pthread_mutex_lock(&(sem->lock));
	{
		sem->units += units;
		printf("sem->units = %d\n", sem->units);
	
		pthread_cond_broadcast(&(sem->waiters));
	}
	pthread_mutex_unlock(&(sem->lock));
}