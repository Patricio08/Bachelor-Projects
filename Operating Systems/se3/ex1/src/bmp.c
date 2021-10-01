#include "bmp.h"

#include <stdio.h>
#include <string.h>
#include <fcntl.h>
#include <sys/stat.h>
#include <sys/mman.h>
#include <unistd.h>
#include <stdlib.h>

#define MAX_COLOR 255
#define MIN_COLOR 0

void add_offset(RGBTRIPLE *pixel, int offset);

void add_in_bounds(BYTE *color, int offset);

int main(int argc, char *argv[]) {
    if (argc < 2 || argc > 3 || strlen(argv[1]) < 5 ||
        strcmp(argv[1] + (strlen(argv[1]) - 4), ".bmp") != 0) {
        fprintf(stderr, "use: %s {filename}.bmp [offset]\n", argv[0]);
        return 1;
    }

    struct stat sb;

    int bmp_fd = open(argv[1], O_RDWR);

    fstat(bmp_fd, &sb);

    void *ptr = mmap(NULL, sb.st_size, PROT_READ | PROT_WRITE, MAP_SHARED, bmp_fd, 0);

    BITMAPFILEHEADER *bitmapfileheader = (BITMAPFILEHEADER *) ptr;

    char bm_verifier[2];
    bm_verifier[0] = ((char *) &bitmapfileheader->bfType)[0];
    bm_verifier[1] = ((char *) &bitmapfileheader->bfType)[1];

    printf("\n::: BITMAPFILEHEADER :::\n");

    printf("bfType ::: %s\n", bm_verifier);

    printf("bfSize in bytes ::: %u\n", bitmapfileheader->bfSize);

    DWORD offset_bits = bitmapfileheader++->bfOffBits;
    printf("bfOffBits in bytes ::: %u\n", offset_bits);

    BITMAPINFOHEADER *bitmapinfoheader = (BITMAPINFOHEADER *) bitmapfileheader;

    printf("\n::: BITMAPINFOHEADER :::\n");

    LONG width = bitmapinfoheader->biWidth;
    printf("biWidth ::: %u\n", width);

    LONG height = bitmapinfoheader->biHeight;
    printf("biHeight ::: %u\n", height);

    printf("biBitCount ::: %u\n", bitmapinfoheader->biBitCount);

    DWORD biCompression = bitmapinfoheader->biCompression;
    printf("biCompression ::: %u\n", biCompression);

    if (BI_RGB == biCompression) {
        printf("\n::: NO COMPRESSION DETECTED! :::\n");

        if (argv[2] == NULL) {
            printf("\nNO OFFSET DETECTED! ::: Exiting...\n");
            return 0;
        }

        int color_offset = atoi(argv[2]);
        printf("\nOFFSET %d DETECTED! ::: Applying...\n", color_offset);
        RGBTRIPLE *pixel = (ptr + offset_bits);
        for (int i = 0; i < (width * height); ++i) {
            add_offset(pixel, color_offset);
            pixel++;
        }
    }

    close(bmp_fd);

    return 0;
}

void add_offset(RGBTRIPLE *pixel, int offset) {
    add_in_bounds(&(pixel->rgbtRed), offset);
    add_in_bounds(&(pixel->rgbtBlue), offset);
    add_in_bounds(&(pixel->rgbtGreen), offset);
}

void add_in_bounds(BYTE *color, int offset) {
    int added_offset = *color + offset;
    if (added_offset < MAX_COLOR && added_offset > MIN_COLOR) {
        *color = (BYTE) added_offset;
    }
}
