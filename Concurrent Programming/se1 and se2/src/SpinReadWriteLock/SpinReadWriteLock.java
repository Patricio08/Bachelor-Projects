package SpinReadWriteLock;

import java.util.concurrent.atomic.AtomicInteger;

public class SpinReadWriteLock {
    private AtomicInteger state = new AtomicInteger(); // 0: unlocked; -1: locked for write; > 0: number of read locks

    public SpinReadWriteLock() {
        this.state.set(0);
    }

    public void lockRead() {

        int obsState;
        do {
            obsState = state.get();

            if (obsState < 0) {
                Thread.yield();
            } else {
                int val = obsState+1;
                if(state.compareAndSet(obsState, val)) {
                    System.out.println(state.get());
                    return;
                }
            }
        } while(obsState < 0);

        /*while (state < 0)
            Thread.yield();
        state++;*/
    }

    public void unlockRead() {
        int obsState = state.get();
        int val = obsState-1;
        state.set(val);
        System.out.println(state.get());
    }

    public void lockWrite() {
        /*while (state != 0)
            Thread.yield();
        state = -1;*/

        int obsState;
        do {
            obsState = state.get();

            if (obsState != 0) {
                Thread.yield();
            } else {
                if(state.compareAndSet(obsState, -1)) {
                    System.out.println(state.get());
                    return;
                }
            }
        } while(obsState != 0);
    }

    public void unlockWrite() {
        state.set(0);
        System.out.println(state.get());
        /*int obsState;
        do {
            obsState = state.get();
            if(state.compareAndSet(obsState, 0)) {
                System.out.println(state.get());
                return;
            }

        } while(true);*/
    }
}
