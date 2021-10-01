#include <stdio.h>
#include "../includes/count_latch_t.h"


// Inicia o count latch com o nível máximo de paralelismo especificado em par_level. Caso o valor de par_level seja 0, considera-se não haver limite ao nível de paralelismo.
void cl_init(count_latch_t *latch, int par_level){
	pthread_mutex_init(&(latch->lock), NULL);
	pthread_cond_init(&(latch->waiters_all), NULL);
	pthread_cond_init(&(latch->waiters_up), NULL);
	latch->count = 0;
	latch->par_level = par_level;
}

// Espera que a contagem das unidades de trabalho seja 0.
void cl_wait_all(count_latch_t *latch){
	pthread_mutex_lock(&(latch->lock));
	{
		while (latch->count > 0) {
			pthread_cond_wait(&(latch->waiters_all), &(latch->lock));
			
		}
	}
	pthread_mutex_unlock(&(latch->lock));
}


// Contabiliza mais uma unidade de trabalho, Bloqueia a thread invocante caso o nível máximo de paralelismo tenha sido atingido.
void cl_up(count_latch_t *latch){
	pthread_mutex_lock(&(latch->lock));
	{
		if (latch->count == latch->par_level && latch->par_level != 0){
			printf("Blocked\n");
			pthread_cond_wait(&(latch->waiters_up), &(latch->lock));
			printf("Unblocked - %d\n", latch->count);

		}
		latch->count += 1;
		printf("Count - %d\n", latch->count);
		
		if(latch->par_level == 0){
			printf("::: NO BLOCKING REQUIRED ! - PAR_LEVEL IS 0 :::\n");
		}
	}
	pthread_mutex_unlock(&(latch->lock));
}

// Contabiliza menos uma unidade de trabalho. Caso se tenha descido abaixo do nível máximo de paralelismo liberta umas das threads bloqueadas na operação cl_up. Caso o número de unidades de trabalho chegue a 0, desbloqueia todas as threads bloqueadas na operação cl_wait_all.
void cl_down(count_latch_t *latch){
	pthread_mutex_lock(&(latch->lock));
	{
		latch->count -= 1;
		if ( latch->par_level != 0 ){
			pthread_cond_signal(&(latch->waiters_up));
		}
		if (latch->count == 0) {
			pthread_cond_broadcast(&(latch->waiters_all));
		}
		
	}
	pthread_mutex_unlock(&(latch->lock));
}
