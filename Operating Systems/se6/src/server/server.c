#include <ctype.h>
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <sys/syscall.h>
#include <sys/types.h>
#ifdef SYS_gettid
#define gettid() ((pid_t)syscall(SYS_gettid))
#else
#error SYS_gettid undefined
#endif

#include "../include/uv.h"
#include <endian.h>

#define CAPITAL_TCP_OPEN_PORT_1 54321
#define UDP_OPEN_PORT_1 54345
#define LOWER_TCP_OPEN_PORT_2 56789

#define LOWER 0
#define CAPITAL 1
#define DISPOSABLE_CHARS 2
#define UDP_RESPONSE_SIZE 15
#define UDP_RESPONSE_CHAR "A"

void bind_tcp_server(uv_tcp_t *server, int port);
uv_tcp_t *init_tcp_connection(uv_stream_t *server, int status);
void to_upper_case(char *str);
void to_lower_case(char *str);

void on_udp_read(uv_udp_t *req, ssize_t nread, const uv_buf_t *buf,
                 const struct sockaddr *addr, unsigned flags);

int get_counter_by_type(char type);

typedef struct {
  uv_write_t req;
  uv_buf_t buf;
} write_req_t;

int capital_con = 0;
int lower_con = 0;
int capital_total_chars = 0;
int lower_total_chars = 0;

void free_write_req(uv_write_t *wreq) {
  write_req_t *req = (write_req_t *)wreq;
  free(req->buf.base);
  free(req);
}

void error(int err, const char *msg) {
  fprintf(stderr, "%s: %s\n", msg, uv_strerror(err));
}

void alloc_buffer(uv_handle_t *handle, size_t suggested_size, uv_buf_t *buf) {
  buf->base = (char *)malloc(suggested_size);
  buf->len = suggested_size;
}

void on_close(uv_handle_t *handle) {
  puts("** on close **");
  free(handle);
}

void after_write(uv_write_t *req, int status) {
  if (status) {
    error(status, "write error");
  }
  free_write_req(req);
}

#pragma region TCP

void to_upper_case(char *str) {
  for (int i = 0; i < strlen(str) - DISPOSABLE_CHARS; ++i) {
    str[i] = toupper(str[i]);
  }
}

void to_lower_case(char *str) {
  for (int i = 0; i < strlen(str) - DISPOSABLE_CHARS; ++i) {
    str[i] = tolower(str[i]);
  }
}

void on_read(uv_stream_t *client, ssize_t nread, const uv_buf_t *buf,
             int capital_flag) {
  printf("\n🔧 TCP - ON_READ - Thread ID -> %d 🔧\n", gettid());

  if (nread > DISPOSABLE_CHARS) {
    buf->base[nread] = 0;
    printf("🔽 TCP - Received %d bytes: 📩 %s", (int)nread, buf->base);

    if (capital_flag) {
      to_upper_case(buf->base);
    } else {
      to_lower_case(buf->base);
    }

    write_req_t *req = (write_req_t *)malloc(sizeof(write_req_t));
    req->buf = uv_buf_init(buf->base, nread);

    uv_write((uv_write_t *)req, client, &(req->buf), 1, after_write);
    printf("🔼 TCP - Sent %d bytes: 📧 %s\n", (int)nread, req->buf);
    return;
  } else {
    if (nread == UV_EOF) {
      puts("** eof **");
    } else {
      error(nread, "read error");
    }
    uv_close((uv_handle_t *)client, on_close);
  }
}

void on_capital_read(uv_stream_t *client, ssize_t nread, const uv_buf_t *buf) {
  printf("\n⬆ TCP - CAPITAL");
  capital_total_chars += nread;
  on_read(client, nread, buf, CAPITAL);
}

void on_lower_read(uv_stream_t *client, ssize_t nread, const uv_buf_t *buf) {
  printf("\n⬇ TCP - LOWER");
  lower_total_chars += nread;
  on_read(client, nread, buf, LOWER);
}

void prepare_tcp_client(uv_tcp_t *client, int capital_flag) {
  printf("\n🔧 TCP - PREPARE TCP CLIENT - Thread ID -> %d 🔧\n", gettid());

  struct sockaddr_in cli_addr;
  int cli_addr_len = sizeof(struct sockaddr_in);
  uv_tcp_getpeername(client, (struct sockaddr *)&cli_addr, &cli_addr_len);

  char cli_addr_str[INET_ADDRSTRLEN];
  uv_ip4_name(&cli_addr, cli_addr_str, INET_ADDRSTRLEN);

  printf("\n🔗 TCP - Server Established Connection With %s In Port %d 🔗\n",
         cli_addr_str, ntohs(cli_addr.sin_port));

  if (capital_flag) {
    uv_read_start((uv_stream_t *)client, alloc_buffer, on_capital_read);
  } else {
    uv_read_start((uv_stream_t *)client, alloc_buffer, on_lower_read);
  }
}

uv_tcp_t *init_tcp_connection(uv_stream_t *server, int status) {
  if (status < 0) {
    error(status, "new connection failed");
    return;
  }

  uv_tcp_t *client = (uv_tcp_t *)malloc(sizeof(uv_tcp_t));
  uv_tcp_init(server->loop, client);
  return client;
}

void on_new_capital_connection(uv_stream_t *server, int status) {
  printf("\n🔧 TCP - ON_CAPITAL CON - Thread ID -> %d 🔧\n", gettid());

  uv_tcp_t *client = init_tcp_connection(server, status);

  // PREPARE CLIENT - UPPER

  int res = uv_accept(server, (uv_stream_t *)client);
  if (res == 0) {
    capital_con++;
    prepare_tcp_client(client, CAPITAL);
  } else {
    uv_close((uv_handle_t *)client, on_close);
  }
}

void on_new_lower_connection(uv_stream_t *server, int status) {
  printf("\n🔧 TCP - ON_LOWER CON - Thread ID -> %d 🔧\n", gettid());

  uv_tcp_t *client = init_tcp_connection(server, status);

  // PREPARE CLIENT - LOWER

  int res = uv_accept(server, (uv_stream_t *)client);
  if (res == 0) {
    lower_con++;
    prepare_tcp_client(client, LOWER);
  } else {
    uv_close((uv_handle_t *)client, on_close);
  }
}

void bind_tcp_server(uv_tcp_t *server, int port) {
  printf("\n🔧 TCP - BIND SERVER - Thread ID -> %d 🔧\n", gettid());

  struct sockaddr_in srv_addr;
  uv_ip4_addr("0.0.0.0", port, &srv_addr);

  int res = uv_tcp_bind(server, (const struct sockaddr *)&srv_addr, 0);
  if (res) {
    error(res, "TCP bind failed!");
    exit(1);
  }
}

void prepare_tcp_server(uv_tcp_t *server, int port, int capital_flag) {
  printf("\n🔧 TCP - PREPARE SERVER - Thread ID -> %d 🔧\n", gettid());

  bind_tcp_server(server, port);
  int res;
  if (capital_flag) {
    res = uv_listen((uv_stream_t *)server, 5, on_new_capital_connection);
  } else {
    res = uv_listen((uv_stream_t *)server, 5, on_new_lower_connection);
  }
  if (res) {
    error(res, "listen failed");
    exit(1);
  }
}

#pragma endregion TCP

#pragma region UDP

void on_udp_send(uv_udp_send_t *req, int status) {
  printf("\n🔧 UDP - ON_SEND - Thread ID -> %d 🔧\n", gettid());
  free(req);
  if (status) {
    fprintf(stderr, "uv_udp_send_cb error: %s\n", uv_strerror(status));
  }
}

void bind_udp_server(uv_udp_t *server, int port) {
  printf("\n🔧 UDP - BIND SERVER - Thread ID -> %d 🔧\n", gettid());
  struct sockaddr_in srv_addr;
  uv_ip4_addr("0.0.0.0", port, &srv_addr);

  int res =
      uv_udp_bind(server, (const struct sockaddr *)&srv_addr, UV_UDP_REUSEADDR);
  if (res) {
    error(res, "❌ UDP bind failed!");
    exit(1);
  }
}

void on_udp_read(uv_udp_t *req, ssize_t nread, const uv_buf_t *buf,
                 const struct sockaddr *addr, unsigned flags) {
  printf("\n🔧 UDP - ON_READ - Thread ID -> %d 🔧\n", gettid());
  if (nread < 0) {
    fprintf(stderr, "❌ Read error %s\n", uv_err_name(nread));
    uv_close((uv_handle_t *)req, NULL);
    free(buf->base);
    return;
  }
  if (nread > 0) {
    char counter[8] = { 0, 0, 0, 0, 0, 0, 0, 0 };
    char type[1];
    char client_id[4];
    char client_seq[2];
    strncpy(type, buf->base + 1, 1);
    strncpy(client_id, buf->base + 2, 4);
    strncpy(client_seq, buf->base + 6, 2);
    sprintf(counter, "%d", get_counter_by_type(type[0]));

	for(int i=0; i<8; ++i){
		if(counter[i] == NULL){
			counter[i] = '0';
		}
	}
	
	uint8_t counter1 = 0;
    uint8_t type1;
    uint8_t client_id1;
    uint8_t client_seq1;
    memcpy(type1, buf->base + 1, 1);
    printf("Type1: %X\n", type1);
	
    char response[16];
    response[0] = 'A';
    response[1] = type[0];
    response[2] = client_id[0];
    response[3] = client_id[1];
    response[4] = client_id[2];
    response[5] = client_id[3];
    response[6] = client_seq[0];
    response[7] = client_seq[1];
    response[8] = counter[0];
    response[9] = counter[1];
    response[10] = counter[2];
    response[11] = counter[3];
    response[12] = counter[4];
    response[13] = counter[5];
    response[14] = counter[6];
    response[15] = counter[7];
	
    uv_udp_send_t *res = malloc(sizeof(uv_udp_send_t));
    printf("🔽 UDP - Received %d bytes: 📩 %s\n", (int)nread, buf->base);

    uv_buf_t buff = uv_buf_init(response, strlen(response));
    uv_udp_send(res, req, &buff, 1, addr, on_udp_send);
    printf("🔼 UDP - Sent %d bytes: 📧 %s\n", strlen(response), response);
  }
}

void prepare_udp_server(uv_udp_t *server, int port) {
  printf("\n🔧 UDP - PREPARE_SERVER - Thread ID -> %d 🔧\n", gettid());
  bind_udp_server(server, port);
  int res = uv_udp_recv_start(server, alloc_buffer, on_udp_read);
  if (res) {
    error(res, "listen failed");

    exit(1);
  }
}

int get_counter_by_type(char type) {
  int type_res = (int)(type);
  switch (type_res - 48) {
    case 1:
      return capital_con;
    case 2:
      return lower_con;
    case 3:
      return capital_total_chars;
    case 4:
      return lower_total_chars;
    default:
      return -1;
  }
}

#pragma endregion UDP

int main(int argc, char *argv[]) {
  printf("\n🔧 MAIN - Thread ID -> %d 🔧\n", gettid());

  uv_loop_t loop;
  uv_loop_init(&loop);

  uv_tcp_t capital_server;
  uv_tcp_init(&loop, &capital_server);

  uv_tcp_t lower_server;
  uv_tcp_init(&loop, &lower_server);

  prepare_tcp_server(&capital_server, CAPITAL_TCP_OPEN_PORT_1, CAPITAL);
  prepare_tcp_server(&lower_server, LOWER_TCP_OPEN_PORT_2, LOWER);

  uv_udp_t udp_server;
  uv_udp_init(&loop, &udp_server);

  prepare_udp_server(&udp_server, UDP_OPEN_PORT_1);

  puts("\n🚀 START SERVER : OPEN PORTS 👇");
  printf(":: TCP Upper 👉 %d ::\n", CAPITAL_TCP_OPEN_PORT_1);
  printf(":: TCP Lower 👉 %d ::\n", LOWER_TCP_OPEN_PORT_2);
  printf(":: UDP 👉 %d ::\n", UDP_OPEN_PORT_1);

  uv_run(&loop, UV_RUN_DEFAULT);

  uv_loop_close(&loop);

  puts(":: END ::");

  return 0;
}
