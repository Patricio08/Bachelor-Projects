#include <stdio.h>
#include <string.h>

#define O_WRONLY 01
#define O_CREAT 0100

#define FILE_POS 1
#define GROUP_ELEMS 3
#define MAXSIZE 50

char ch_arr[GROUP_ELEMS][MAXSIZE] = {
        "40623 - Tiago Fernandes",
        "43509 - Daniel Patricio",
        "43592 - Tiago Pereira",

};

int xgetpid();

int xopen(const char *filename, int flags);

int xwrite(int fd, const void *buf, size_t n);

int xclose(int fd);

void change_line(const int *fd);

int main(int argc, char *argv[]) {
    char *filename = argv[FILE_POS];
    int fd = xopen(filename, O_WRONLY | O_CREAT);
    char pid[15];
    sprintf(pid, "%d", xgetpid());

    for (int i = 0; i < GROUP_ELEMS + 1; ++i) {
        if (i == 0) {
            xwrite(fd, pid, strlen(pid));
            change_line(&fd);
        } else {
            char *group_member = ch_arr[i - 1];
            xwrite(fd, group_member, strlen(group_member));
            change_line(&fd);
        }
    }
    xclose(fd);
    return 0;
}

void change_line(const int *fd) { xwrite(*fd, "\n", strlen("\n")); }