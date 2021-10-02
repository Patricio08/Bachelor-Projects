package SpinReadWriteLock;

import static java.lang.Thread.sleep;

public class SpinReadWriteLockTest {

    volatile static SpinReadWriteLock spin = new SpinReadWriteLock();

    public static void main(String[] args) throws InterruptedException {

        Thread t1 = new Thread( () -> {
            spin.lockWrite();
            System.out.println("Write locked");
        });

        Thread t2 = new Thread( () -> {
            try {
                sleep(1500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            spin.lockRead();
            System.out.println("Read locked");
            System.out.println();
        });

        Thread t3 = new Thread( () -> {
            try {
                sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            spin.unlockWrite();
            System.out.println("write unlocked");
        });

        Thread t4 = new Thread( () -> {
            try {
                sleep(4500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            spin.unlockRead();
            System.out.println("Read unlocked");
        });

        t1.start();
        t2.start();
        t3.start();
        t4.start();

        t1.join();
        t2.join();
        t3.join();
        t4.join();

        System.out.println("done");
    }
}
