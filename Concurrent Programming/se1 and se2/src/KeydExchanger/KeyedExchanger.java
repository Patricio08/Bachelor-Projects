package KeydExchanger;

import utils.TimeoutHolder;

import java.util.HashMap;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;


public class KeyedExchanger<T> {

    private Lock monitor;
    private HashMap<Integer, Pair<T>> exchangeRequests;

    private class Pair<T> {
        T data;
        boolean exchangeDone;
        Condition waitingOnPair;

        public Pair(T data, boolean exchangeDone, Condition waitingOnPair) {
            this.data = data;
            this.exchangeDone = exchangeDone;
            this.waitingOnPair = waitingOnPair;
        }
    }

    public KeyedExchanger() {
        monitor = new ReentrantLock();
        exchangeRequests = new HashMap<>();
    }

    public Optional<T> exchange(int key, T mydata, int timeout) throws InterruptedException {
        monitor.lock();
        try {

            if(exchangeRequests.containsKey(key)) {

                // Blocks 3rd thread
                exchangeRequests.get(key).exchangeDone = true;

                T auxData = exchangeRequests.get(key).data;
                exchangeRequests.get(key).data = mydata;
                exchangeRequests.get(key).waitingOnPair.signal();

                exchangeRequests.remove(key);

                return Optional.of(auxData);
            }
            if (timeout == 0) {
                return Optional.empty();
            }

            // If list doesn't contain the key

            TimeoutHolder th = new TimeoutHolder(timeout);
            Pair<T> initPair = new Pair<>(mydata, false, monitor.newCondition());
            exchangeRequests.put(key, initPair);

            do {
                try {
                    while(!initPair.exchangeDone) {
                        exchangeRequests.get(key).waitingOnPair.await(th.remaining(), TimeUnit.MILLISECONDS);
                    }

                    if (th.timeout()) {
                        exchangeRequests.remove(key, mydata);
                        return Optional.empty();
                    }

                    T auxData = initPair.data;

                    return Optional.of(auxData);
                } catch (InterruptedException e) {
                    if(exchangeRequests.get(key).exchangeDone) {
                        // delay the interruption and return success
                        Thread.currentThread().interrupt();
                    }
                    exchangeRequests.remove(key);
                    throw e;
                }

            } while (true);
        } finally {
            //exchangeRequests.remove(key);
            monitor.unlock();
        }

    }
}