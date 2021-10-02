package KeyedThreadPoolExecutor;

import utils.NodeList;
import utils.TimeoutHolder;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.RejectedExecutionException;

public class KeyedThreadPoolExecutor {

    private class PoolThread {
        public Condition cond;
        public Runnable cmd;

        public PoolThread(Runnable cmd) {
            ++nThreads;
            this.cmd = cmd;
            this.cond = monitor.newCondition();
        }

        public void start() {
            Thread thread = new Thread( () -> {
                try {
                    execThread(this);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });
        }

        public void dispatchCmd(Runnable cmd) {
            this.cmd = cmd;
            cond.signal();
        }

        public void safeExec(Runnable cmd) {
            try {
                cmd.run();
            }
            catch(Exception e) {
                // do eventual log...
            }
        }

        public void safeExec() {
            safeExec(cmd);
        }

        public boolean hasCmd() {
            return cmd != null;
        }

        public void await(long timeout) throws InterruptedException {
        }

        public void abort() {

        }
    }

    private class Request {
        public boolean accepted;
        public Condition condition;
        public final Runnable cmd;

        Request(Runnable cmd) {
            this.condition = monitor.newCondition();
            this.cmd = cmd;
            this.accepted = false;
        }

        public void accept() {
            accepted = true;
            condition.signal();
        }
    }

    private Lock monitor;
    private int maxPoolSize;
    private int keepAliveTime;
    private int nThreads;
    private NodeList<PoolThread> inactiveThreads;
    private NodeList<Request> pendingRequests;

    private boolean shutdownFlag;
    private Condition shutdownCompleted;

    public KeyedThreadPoolExecutor (int maxPoolSize, int keepAliveTime) {
        this.monitor = new ReentrantLock();
        this.maxPoolSize = maxPoolSize;
        this.keepAliveTime = keepAliveTime;
        inactiveThreads = new NodeList<>();
        pendingRequests = new NodeList<>();

        shutdownFlag = false;
        shutdownCompleted = monitor.newCondition();

    }
    public void execute(Runnable runnable, Object key) throws InterruptedException {
        monitor.lock();

        try {

            if(shutdownFlag) {
                throw new RejectedExecutionException();
            }

            if (!inactiveThreads.empty()) {
                PoolThread pt = inactiveThreads.removeFirst();
                pt.dispatchCmd(runnable);
                return;
            }

            if (nThreads < maxPoolSize) {
                PoolThread pt = new PoolThread(runnable);
                pt.start();
                return;
            }

            TimeoutHolder th = new TimeoutHolder(keepAliveTime);
            Request req = new Request(runnable);
            NodeList.Node<Request> node = pendingRequests.addLast(req);

            do {
                try {

                    req.condition.await(th.remaining(), TimeUnit.MILLISECONDS);

                    if (req.accepted) {
                        return;
                    }

                    if (th.timeout()) {
                        pendingRequests.remove(node);
                        return;
                    }


                } catch (InterruptedException e) {
                    if (req.accepted) {
                        Thread.currentThread().interrupted();
                        return;
                    }
                    pendingRequests.remove(node);
                    throw e;
                }

            } while (true);
        } finally {
            monitor.unlock();
        }
    }

    // lifecycle of a poolthread
    private void execThread(PoolThread pthread) throws InterruptedException {

        pthread.safeExec();
        monitor.lock();

        do {
            Request req;
            Runnable cmd = null;

            try {

                if (!pendingRequests.empty()) {
                    req = pendingRequests.removeFirst();
                    req.accept();
                    cmd = req.cmd;
                } else {
                   TimeoutHolder th = new TimeoutHolder(keepAliveTime);
                   // Ainda ter cache volta primeiro
                   NodeList.Node<PoolThread> node = inactiveThreads.addFirst(pthread);

                   try {
                       do {
                           pthread.cond.await(th.remaining(), TimeUnit.MILLISECONDS);
                           if (pthread.hasCmd()) {
                               cmd = pthread.cmd;
                               break;
                           }

                           if (th.timeout()) {
                               inactiveThreads.remove(node);
                               pthread.abort();
                               return;
                           }
                       } while (true);
                   } catch (InterruptedException e) {
                       //throw e;
                   }

                }
            } finally {
                monitor.unlock();
            }

            pthread.safeExec(cmd);
        } while (true);
    }

    public void shutdown() {
        monitor.lock();
        try {
            shutdownFlag = true;
        }
        finally {
            monitor.unlock();
        }

    }

    public boolean awaitTermination(int timeout) throws InterruptedException {
        monitor.lock();
        try {

            if(pendingRequests.empty()) {

            } else {
                TimeoutHolder th = new TimeoutHolder(timeout);
                while(!pendingRequests.empty()) {
                    shutdownCompleted.await();
                }
            }

            return false;
        }
        finally {
            monitor.unlock();
        }
    }

    public int numThreads() {
        monitor.lock();
        try {
            return nThreads;
        }
        finally {
            monitor.unlock();
        }
    }
}
