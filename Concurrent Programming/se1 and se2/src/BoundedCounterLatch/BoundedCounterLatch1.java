package BoundedCounterLatch;

import utils.NodeList;
import utils.TimeoutHolder;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

class BoundedCounterLatch1 {

    private Lock monitor;
    private int maxItems;
    private int count;
    private Condition waitersAll;

    private static class Request {

        private boolean done;
        final Condition condition;

        Request(Condition condition) {
            this.condition = condition;
            this.done = false;
        }

        void complete() {
            this.done = true;
            condition.signal();
        }

        boolean isCompleted() {
            return done;
        }
    }

    private NodeList<Request> requests;

    private void notifyWaiters() {
        while (!requests.empty() && count < maxItems) {
            Request req = requests.removeFirst();
            ++count;
            req.complete();
        }
    }

    BoundedCounterLatch1(int maxCountValue) {
        monitor = new ReentrantLock();
        maxItems = maxCountValue;
        count = 0;
        requests = new NodeList<>();
        waitersAll = monitor.newCondition();
    }


    void increment(long timeout) throws InterruptedException, TimeoutException {
        monitor.lock();

        try {
            // non blocking path
            if (requests.empty() && count < maxItems) {
                ++count;
                return;
            }
            if (timeout == 0) {
                throw new TimeoutException();
            }

            TimeoutHolder th = new TimeoutHolder(timeout);
            Request req = new Request(monitor.newCondition());
            NodeList.Node<Request> node = requests.addLast(req);
            do {
                try {
                    req.condition.await(th.remaining(), TimeUnit.MILLISECONDS);

                    if (req.isCompleted()) {
                        return;
                    }

                    if (th.timeout()) {
                        requests.remove(node);
                        throw new TimeoutException();
                    }
                } catch (InterruptedException e) {
                    if( req.isCompleted()) {
                        // delay the interruption and return success
                        Thread.currentThread().interrupt();
                        return;
                    }
                    requests.remove(node);
                    throw e;
                }

            } while (true);
        }
        finally {
            monitor.unlock();
        }

    }

    void decrement() {
        monitor.lock();

        try {
            --count;
            if (count < maxItems) {
                notifyWaiters();
            }
            if(count == 0) waitersAll.signalAll();
        } finally {
            monitor.unlock();
        }

    }

    void waitAll(long timeout) throws InterruptedException, TimeoutException {

        monitor.lock();

        while (count > 0) {
            waitersAll.await(timeout, TimeUnit.MILLISECONDS);
        }

        monitor.unlock();

    }
}
