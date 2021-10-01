#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <pthread.h>
#include "../includes/semaphore_t.h"
#define NTHREADS 10


semaphore_t sem;

void * f1(void * ptr){

	//semaphore_acquire (&sem, (long)ptr);
	semaphore_acquire_timed(&sem, (long)ptr, 10000);

	return NULL;
}


int main(){
	
	pthread_t threads[NTHREADS];

	printf("Init semaphore: 25 units\n");
	semaphore_init(&sem, 25);
	

	printf(":: starting ::\n");
	for(int i = 0; i < NTHREADS; ++i){
		pthread_create(&threads[i], NULL, f1, (void*)(long)(i+1));
	}
		
	
	sleep(1);
	printf("\n:: RELEASE + 10 UNITS ::\n");
	semaphore_release(&sem, 10);
	sleep(2);							// Alterar valor para teste
	printf("\n:: RELEASE + 20 UNITS ::\n");
	semaphore_release(&sem, 20);

	
	for(int i = 0; i < NTHREADS; i++){
		pthread_join(threads[i], NULL);
	}

	return 0;	
}

