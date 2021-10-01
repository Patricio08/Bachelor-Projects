#pragma once

#include <pthread.h> 

typedef struct count_latch_t {
	pthread_mutex_t lock;
	pthread_cond_t waiters_all;
	pthread_cond_t waiters_up;
	unsigned count;
	int par_level;
} count_latch_t;


// Inicia o count latch com o nível máximo de paralelismo especificado em par_level. Caso o valor de par_level seja 0, considera-se não haver limite ao nível de paralelismo.
void cl_init(count_latch_t *latch, int par_level);

// Espera que a contagem das unidades de trabalho seja 0.
void cl_wait_all(count_latch_t *latch);

// Contabiliza mais uma unidade de trabalho, Bloqueia a thread invocante caso o nível máximo de paralelismo tenha sido atingido.
void cl_up(count_latch_t *latch);

// Contabiliza menos uma unidade de trabalho. Caso se tenha descido abaixo do nível máximo de paralelismo liberta umas das threads bloqueadas na operação cl_up. Caso o número de unidades de trabalho chegue a 0, desbloqueia todas as threads bloqueadas na operação cl_wait_all.
void cl_down(count_latch_t *latch);