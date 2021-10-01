all: libtpool.so

libtpool.so: queue.o thread_pool.o
	gcc -o libtpool.so -shared queue.o thread_pool.o
	
queue.o: queue.c
	gcc -c -g queue.c -o queue.o -fPIC -Wall 
	
thread_pool.o: thread_pool.c
	gcc -c -g thread_pool.c -o thread_pool.o -fPIC -Wall
	 
clean:
	rm -rf *.o *.so *.a