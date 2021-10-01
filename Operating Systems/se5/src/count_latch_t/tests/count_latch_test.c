#include <stdio.h>
#include <string.h>
#include <unistd.h>

#include "../includes/count_latch_t.h"
#define NRTHREADS 5

count_latch_t latch;
void test_cl(int par_level);

void *waiter(void *ptr) {
  const char *name = (const char *)ptr;
  printf("\n::: %s START :::\n", name);
  cl_up(&latch);
  printf("::: %s WAITING :::\n", name);
  cl_wait_all(&latch);
  printf("\n::: %s RELEASED :::\n", name);
  return NULL;
}

int main() {
  
  //test_cl(0);
  printf("\n\n::: STARTING NEW TEST :::\n\n");
  test_cl(3);

  return 0;
}

void test_cl(int par_level) {
  cl_init(&latch, par_level);

  printf("::: INIT :::\n");
  printf("::: Count Latch  =  %d\n", (&(latch))->count);
  printf("::: ParLevel Latch =  %d\n", (&(latch))->par_level);

  pthread_t threads[NRTHREADS];
  char names[NRTHREADS][3];

  for (int i = 0; i < NRTHREADS; ++i) {
    strcpy(names[i], "T0");
    names[i][1] += i;
    pthread_create(&threads[i], NULL, waiter, names[i]);
  }

  for (int i = 0; i < NRTHREADS; ++i) {
    sleep(1);
    printf("\n::: Count Latch Down! :::\n");
    cl_down(&latch);
  }

  for (int i = 0; i < NRTHREADS; ++i) {
    pthread_join(threads[i], NULL);
  }

  printf("::: DONE :::\n");
}
