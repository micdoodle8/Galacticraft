package codechicken.lib.thread;

import codechicken.lib.asm.ObfMapping;
import com.google.common.base.Objects;

/**
 * Used to control how long a thread can run for before a TimeoutException is thrown.
 */
public class ThreadOperationTimer extends Thread {

    @SuppressWarnings ("serial")
    public static class TimeoutException extends RuntimeException {

        public final Object operation;

        public TimeoutException(String msg, Object op) {
            super(msg);
            operation = op;
        }
    }

    private Thread thread;
    private Object operation;
    private long opTime;
    private long limit;

    private ThreadOperationTimer(Thread thread, int limit) {
        super(thread.getName() + " Operation Timer");
        this.thread = thread;
        this.limit = limit;
    }

    public synchronized void setLimit(int limit) {
        this.limit = limit;
    }

    public void reset() {
        reset(null);
    }

    public synchronized void reset(Object op) {
        operation = op;
        opTime = System.currentTimeMillis();
    }

    public synchronized void update(Object op) {
        if (!Objects.equal(operation, op)) {
            operation = op;
            opTime = System.currentTimeMillis();
        }
    }

    @SuppressWarnings ("deprecation")
    @Override
    public void run() {
        if (ObfMapping.obfuscated) {
            return;//don't run this thread in a source environment
        }

        while (thread.isAlive()) {
            synchronized (this) {
                if (operation != null && System.currentTimeMillis() - opTime > limit) {
                    thread.stop(new TimeoutException("Operation took too long", operation));
                }
            }
            try {
                Thread.sleep(50);
            } catch (InterruptedException ie) {
            }
        }
    }

    public static ThreadOperationTimer start(Thread thread, int limit) {
        ThreadOperationTimer t = new ThreadOperationTimer(thread, limit);
        t.start();
        return t;
    }
}
