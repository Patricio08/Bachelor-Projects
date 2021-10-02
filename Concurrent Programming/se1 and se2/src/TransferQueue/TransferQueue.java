package TransferQueue;

import utils.NodeList;
import utils.TimeoutHolder;

import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class TransferQueue<E> {

    public Lock monitor = new ReentrantLock();

    // Fifo
    private NodeList<Requests<E>> requestsQueue = new NodeList<>();
    private NodeList<Result> takerQueue = new NodeList<>();

    public void put(E message) {
        monitor.lock();

        if(takerQueue.empty()) {
            Requests req = new Requests<>(message, monitor.newCondition());
            req.needsToBeNotified = false;
            requestsQueue.addLast(req);
        } else {
            Result req = takerQueue.removeFirst();
            req.message = message;
            req.done = true;
            req.takeReady.signal();
        }

        monitor.unlock();

    }
    public boolean transfer(E message, long timeout) throws InterruptedException {

        monitor.lock();

        try {

            // non blockable
            // Se já houver um pedido de um take
            if(!takerQueue.empty()) {
                Result req = takerQueue.removeFirst();
                req.message = message;
                req.done = true;
                req.takeReady.signal();
                return true;
            }

            TimeoutHolder th = new TimeoutHolder(timeout);
            Requests req = new Requests<>(message, monitor.newCondition());

            do {
                try {
                    NodeList.Node request = requestsQueue.addLast(req);

                    req.hasConsumer.await(th.remaining(), TimeUnit.MILLISECONDS);

                    if (req.done) return true;

                    if (th.timeout()) {
                        requestsQueue.remove(request);
                        return false;
                    }

                } catch (InterruptedException e) {
                    if (req.done) {
                        Thread.currentThread().interrupt();
                        break;
                    }

                    throw e;
                }
            } while (!req.done);
        } finally {
            monitor.unlock();
        }
        return false;
    }

    public Result take() {

        monitor.lock();
        try {

        Result result = new Result(null, monitor.newCondition());
        if(!requestsQueue.empty()) {
            Requests<E> res = requestsQueue.removeFirst();
            result.message = res.message;
            result.done = true;
            if(res.needsToBeNotified)
                res.hasConsumer.signal();
            return result;
        }

        result.node = takerQueue.addLast(result);

        return result;

        } finally {
            monitor.unlock();
        }
    }

    public class Result {

        E message;
        boolean done;
        Condition takeReady;
        NodeList.Node<Result> node;

        public Result(E message, Condition takeReady) {
            this.message = message;
            done = false;
            this.takeReady = takeReady;
        }

        public boolean isComplete() {
            return done;
        }

        public boolean tryCancel() {
            monitor.lock();
            try {
                if (!isComplete()) {
                    takerQueue.remove(node);
                    takeReady.signal();
                    return true;
                }
                return false;
            } finally {
                monitor.unlock();
            }

        }

        public Optional get(long timeout) throws InterruptedException {
            monitor.lock();
            try {
                if(isComplete()) return Optional.of(message);

                TimeoutHolder th = new TimeoutHolder(timeout);

                do {
                    try {
                        takeReady.await(th.remaining(), TimeUnit.MILLISECONDS);

                        if(isComplete()) return Optional.of(message);

                        if (th.timeout()) {
                            tryCancel();
                            return Optional.empty();
                        }
                    } catch (InterruptedException e) {
                        if (isComplete()) {
                            Thread.currentThread().interrupt();
                            return Optional.of(message);
                        }
                        tryCancel();
                    }


                } while (isComplete());

            } finally {
                monitor.unlock();
            }
            return Optional.of(message);
        }
    }

    private static class Requests<E> {

        private E message;
        private Condition hasConsumer;
        private boolean needsToBeNotified;
        private boolean done;

        Requests(E message, Condition hasConsumer) {
            this.message = message;
            this.hasConsumer = hasConsumer;
            needsToBeNotified = true;
            this.done = false;
        }
    }

}
