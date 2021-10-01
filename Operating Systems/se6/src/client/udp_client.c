#include <arpa/inet.h>
#include <netinet/in.h>
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <sys/socket.h>
#include <sys/types.h>
#include <unistd.h>

#define PORT 54345
#define MAXLINE 1024

char *generate_message() {
  return "Q1CLT100";
}

// Driver code
int main() {
  int sockfd;
  char buffer[MAXLINE];
  struct sockaddr_in servaddr;

  // Creating socket file descriptor
  if ((sockfd = socket(AF_INET, SOCK_DGRAM, 0)) < 0) {
    perror("socket creation failed");
    exit(EXIT_FAILURE);
  } else {
    printf("\nSocket successfully created...\n");
  }
  bzero(&servaddr, sizeof(servaddr));

  // Filling server information
  servaddr.sin_family = AF_INET;
  servaddr.sin_port = htons(PORT);
  servaddr.sin_addr.s_addr = INADDR_ANY;

  // send message
  //char message [MAXLINE];
  char * message;
  //scanf("%[^\n]", message);
  message = generate_message();
  sendto(sockfd, (const char *)message, strlen(message), MSG_CONFIRM,
         (const struct sockaddr *)&servaddr, sizeof(servaddr));

  // receive message
  int len = sizeof(struct sockaddr *);
  int n = recvfrom(sockfd, (char *)buffer, MAXLINE, 0,
                   (struct sockaddr *)&servaddr, &len);
  if (n != -1) {
    printf("\n::: Message: %s", buffer);
  } else {
    printf("No connection stablished!\nRetrying...\n");
  }
  // close the socket
  close(sockfd);
}
