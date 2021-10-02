package BoundedCounterLatch;

import org.junit.jupiter.api.Test;

import java.util.concurrent.TimeoutException;

import static java.lang.Thread.sleep;

public class BoundedCounterLatchTests {

    private static BoundedCounterLatch1 bcl = new BoundedCounterLatch1(3);

    static void runMethod() {
        try {
            bcl.increment(100000);
            System.out.println("incremented: " + Thread.currentThread().getName());
            bcl.waitAll(100000);
            System.out.println("released..");
        } catch (InterruptedException | TimeoutException e) {
            e.printStackTrace();
        }
    }

    @Test
    void testBoundedCounterLatch(){

        System.out.println("Init\n");

        int nthreads = 6;
        Thread[] threads = new Thread[nthreads];
        for (int i = 0; i < threads.length; i++) {

            try {
                sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            threads[i] = new Thread(BoundedCounterLatchTests::runMethod, "thread - " + i);
            threads[i].start();
        }

        for (int i = 0; i < nthreads; ++i) {
            try {
                sleep(1500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("decrement");
            bcl.decrement();
        }

        for (int i = 0; i < nthreads; ++i) {
            try {
                threads[i].join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
