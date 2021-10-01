#include <stdio.h>
#include <unistd.h>
#include <string.h>
#include <sys/wait.h>
#include <stdlib.h>
#include <dlfcn.h>
#include <sys/mman.h>

#define DATA_SIZE 16*1024*1024

char info1[12*1024*1024];
char info[DATA_SIZE];
char data[DATA_SIZE] = {1};

int main() {
    printf("PID: %u\n", getpid());
    printf("#1 (press ENTER to continue)"); getchar();

    // a
	for (int i = 0; i < 16 * 1024 * 1024; i++) {
		if (info[i] != 0)  {}
	}
	printf("\n");

    printf("#2 (press ENTER to continue)"); getchar();

    // b
	for (int i = 0; i < 16 * 1024 * 1024; i++) {
		data[i] = 1;
	}
    printf("#3 (press ENTER to continue)"); getchar();

    // c
	
	char * ptr = mmap(NULL, DATA_SIZE, PROT_READ|PROT_WRITE, MAP_PRIVATE|MAP_ANONYMOUS, -1, 0);

    printf("#4 (press ENTER to continue)"); getchar();

    // d
    
	for (int i = 0; i < DATA_SIZE; i++) {
		ptr[i] = 1;
	}

    printf("#5 (press ENTER to continue)"); getchar();

    // e
    char * ptrText = mmap(NULL, DATA_SIZE, PROT_EXEC, MAP_PRIVATE|MAP_ANONYMOUS, -1, 0);
    char * ptrData = mmap(NULL, DATA_SIZE, PROT_READ|PROT_WRITE, MAP_PRIVATE|MAP_ANONYMOUS, -1, 0);
    
    printf("#6 (press ENTER to continue)"); getchar();

    // f
    for (int i = 0; i < DATA_SIZE; i++) {
		if (ptrData[i] != 0)  {}
	}

    printf("#7 (press ENTER to continue)"); getchar();

    // g
    pid_t pid = fork();
    if (pid < 0) {
        fprintf(stderr, "ERROR ::: Fork malfunction!\n");
        exit(1);
    } else if (pid == 0) {
    	
    	puts("INFO ::: Waiting in child...");
    	getchar();
    	exit(0);
    	
    } else {
    	int res;
    	waitpid(pid, &res, 0);
        if (WIFEXITED(res)) {
            printf("INFO ::: Child returned %d\n", WEXITSTATUS(res));
        } else {
            printf("ERROR ::: Child terminated abnormally!\n");
        }
    }

    printf("END (press ENTER to continue)"); getchar();

    return 0;
}
