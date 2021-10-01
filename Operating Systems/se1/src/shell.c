#include <ctype.h>
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <sys/wait.h>
#include <unistd.h>
#include <fcntl.h>

#define MAX_STR_LEN 1024
#define MAX_ARGS 16
#define MAX_CMDS 16
#define EXIT_COMMAND "exit"
#define PIPE_COMMAND "|"
#define REDIRECT_COMMAND ">"

struct Command {
    char *file;
    char *cmd_args[MAX_ARGS];
};

void redirect_to(const char *file);

void close_pipes(const int *pipes, int pipe_count);

typedef struct Command Command;

int main() {
    char *save_ptr1, *save_ptr2;
    char delimiters[] = " \t\r\n\v\f";
    int res = 0;


    for (;;) {
    	puts("START");
        char str[MAX_STR_LEN];
        printf("$ ");
        if (fgets(str, MAX_STR_LEN, stdin) == NULL) {
            printf("ERROR ::: Reading Command!\n");
            exit(1);
        }

        size_t len = strlen(str);

        if (str[len - 1] == '\n') {
            str[len - 1] = 0;

            if (strcmp(EXIT_COMMAND, str) == 0) {
                exit(0);
            }

            char *token = strtok_r(str, PIPE_COMMAND, &save_ptr1);

            char *commands[MAX_CMDS];
            int number_of_commands;
            for (number_of_commands = 0; token != NULL; number_of_commands++) {
                commands[number_of_commands] = token;
                token = strtok_r(NULL, "|", &save_ptr1);
            }

            Command command_array[number_of_commands];

            int j;
            for (j = 0; j < number_of_commands; ++j) {
                char *aux = strdup(commands[j]);
                command_array[j].file = NULL;
                char *tk = strtok_r(aux, delimiters, &save_ptr2);
                int m = 0, has_redirect = 0;
                while (tk != NULL) {

                    if (strcmp(tk, REDIRECT_COMMAND) != 0) {
                        if (has_redirect == 0) {
                            command_array[j].cmd_args[m] = (char *) malloc((strlen(tk) + 1) * sizeof(char));
                            strcpy(command_array[j].cmd_args[m], tk);
                            ++m;
                        } else {
                            command_array[j].file = (char *) malloc((strlen(tk) + 1) * sizeof(char));
                            strcpy(command_array[j].file, tk);
                        }
                    } else {
                        has_redirect = 1;
                    }

                    tk = strtok_r(NULL, delimiters, &save_ptr2);
                }
                command_array[j].cmd_args[m] = NULL;
            }


            /*int pipe_fd[(number_of_commands - 1) * 2];
            if (number_of_commands > 1) {
                for (int i = 0; i < number_of_commands; i) {
                    if (i % 2 == 0) {
                        if (pipe(pipe_fd + i) == -1) {
                            printf("ERROR ::: Pipe malfunction!\n");
                        }
                    }
                }
            }*/
            int pipe_fd[4];
            pipe(pipe_fd);
            pipe(pipe_fd + 2);
			printf("Number of commands: %d\n", number_of_commands);
            for (int i = 0; i < number_of_commands; i++) {
                //int res;
                int pid = fork();
                if (pid < 0) {
                    fprintf(stderr, "ERROR ::: Command-%d - Fork malfunction!\n", i);
                    exit(1);
                }
                if (pid == 0) {
                    //close_pipes(pipe_fd, number_of_commands - 1);
                    
                    if (i == 0) {
                        if (number_of_commands > 1) {
                            if (dup2(pipe_fd[1], 1) == -1) {
                                fprintf(stderr, "ERROR ::: Command-%d - Pipe malfunction!\n", i);
                                exit(1);
                            }
                        }
                    } else if (i == number_of_commands - 1) {
                        if (dup2(pipe_fd[(number_of_commands - 2) * 2], 0) == -1) {
                            fprintf(stderr, "ERROR ::: xCommand-%d - Pipe malfunction!\n", i);
                            exit(1);
                        }
                    } else {
                        if (dup2(pipe_fd[(2 * i) - 2], 0) == -1) {
                            fprintf(stderr, "ERROR ::: yCommand-%d - Pipe malfunction!\n", i);
                            exit(1);
                        }
                        if (dup2(pipe_fd[(2 * i) + 1], 1) == -1) {
                            fprintf(stderr, "ERROR ::: zCommand-%d - Pipe malfunction!\n", i);
                            exit(1);
                        }
                    }
                    close(pipe_fd[0]);
                    close(pipe_fd[1]);
                    close(pipe_fd[2]);
                    close(pipe_fd[3]);
                    //close_pipes(pipe_fd, number_of_commands - 1);
                    redirect_to(command_array[i].file);
                    if (execvp(command_array[i].cmd_args[0], command_array[i].cmd_args) < 0) {
                        fprintf(stderr, "ERROR ::: Exec Failure!\n");
                    }             
                } /*else {
                    waitpid(pid, &res, 0);
                    if (WIFEXITED(res)) {
                        printf("INFO ::: Child returned %d\n", WEXITSTATUS(res));
                    } else {
                        printf("ERROR ::: Child terminated abnormally!\n");
                    }
                }*/

            }
            
            //close_pipes(pipe_fd, number_of_commands - 1);
    		close(pipe_fd[0]);
            close(pipe_fd[1]);
            close(pipe_fd[2]);
            close(pipe_fd[3]);

            for (int k = 0; k < number_of_commands; k++) {
            	puts("Waiting...");
                wait(&res);
                if (WIFEXITED(res)) {
                        printf("INFO ::: Child returned %d\n", WEXITSTATUS(res));
                } else {
                    printf("ERROR ::: Child terminated abnormally!\n");
                }
            }

            for (int i = 0; i < number_of_commands; ++i) {
                free(command_array[i].file);
            }

        } else {
            printf("ERROR ::: Argument wasn't received properly!\n");
        }
    }
}

void close_pipes(const int *pipes, int pipe_count) {
    for (int i = 0; i < pipe_count; ++i) {
        close(pipes[i]);
    }
}

void redirect_to(const char *file) {
    if (file != NULL) {
        int f_open = open(file, O_RDWR | O_CREAT, S_IRUSR | S_IWUSR);
        dup2(f_open, 1);
        close(f_open);
    }
}
