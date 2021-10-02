package TransferQueueTest;

import BoundedCounterLatch.BoundedCounterLatchTests;
import KeydExchanger.KeyedExchanger;
import TransferQueue.TransferQueue;
import org.junit.jupiter.api.Test;

import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

import static java.lang.Thread.sleep;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class TransferQueueTest {
    private static TransferQueue transferQueue = new TransferQueue();

    @Test
    void testTimeout() throws InterruptedException {

        String message = "ToBeTransfered";

        int timeout = 1;

        AtomicReference<Optional<String>> opt1 = new AtomicReference<>();

        Thread t1 = new Thread( () -> {
            try {
                transferQueue.transfer(message, timeout);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });



        Thread t2 = new Thread( () -> {
            try {
                opt1.set(transferQueue.take().get(timeout));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        t1.start();
        sleep(1000);
        t2.start();

        t1.join();
        t2.join();

        assertEquals(opt1.get(), Optional.empty());
    }

    @Test
    void testTransfer() throws InterruptedException {

        String message = "ToBeTransfered";

        int timeout = 20000;

        AtomicReference<Optional<String>> opt1 = new AtomicReference<>();

        Thread t1 = new Thread( () -> {
            try {
                transferQueue.transfer(message, timeout);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });


        Thread t2 = new Thread( () -> {
            try {
                opt1.set(transferQueue.take().get(timeout));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        t1.start();
        t2.start();

        sleep(2000);


        assertEquals(opt1.get().get(), message);
    }

    @Test
    void testPut() throws InterruptedException {

        String message = "ToBePut";

        int timeout = 10000;

        AtomicReference<Optional<String>> opt1 = new AtomicReference<>();

        Thread t1 = new Thread( () -> {
            transferQueue.put(message);
        });

        Thread t2 = new Thread( () -> {
            try {
                opt1.set(transferQueue.take().get(timeout));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        t1.start();
        t2.start();

        t1.join();
        t2.join();

        assertEquals(opt1.get().get(), message);

    }

    @Test
    void testTryCancel() throws InterruptedException {

        String message = "ToBeTransfered";

        int timeout = 10000;

        AtomicReference<Boolean> opt1 = new AtomicReference<>();
        AtomicReference<Optional<String>> opt2 = new AtomicReference<>();
        AtomicReference<Boolean> opt3 = new AtomicReference<>();

        Thread t1 = new Thread( () -> {
            try {
                transferQueue.transfer(message, timeout);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        Thread t2 = new Thread( () -> {
            try {
                opt2.set(transferQueue.take().get(timeout));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        Thread t3 = new Thread( () -> {
            opt3.set(transferQueue.take().tryCancel());
        });

        t1.start();
        t2.start();
        t3.start();

        sleep(2000);

        assertEquals(opt3.get(), true);

    }
}
