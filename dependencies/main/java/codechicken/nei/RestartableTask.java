package codechicken.nei;

public abstract class RestartableTask {
    public final String name;
    private Thread thread;
    private volatile boolean restart;
    private volatile boolean stopped;

    private ThreadOperationTimer timer;

    public RestartableTask(String name) {
        this.name = name;
    }

    private void start() {
        thread = new Thread(name) {
            @Override
            public void run() {
                do {
                    while (stopped) {
                        try {
                            Thread.sleep(1);
                        } catch (InterruptedException ignored) {
                        }
                    }
                    if (!restart) {
                        execute();
                    }
                } while (!finish());
            }
        };
        thread.start();
    }

    private synchronized boolean finish() {
        if (restart) {
            restart = false;
            return false;
        } else {
            clearTasks();
            return true;
        }
    }

    public void clearTasks() {
        thread = null;
        timer = null;
    }

    public synchronized void restart() {
        if (thread != null) {
            stopped = false;
            restart = true;
        } else {
            start();
        }
    }

    /**
     * Stops execution until restart is called. If a thread is running, the restart flag will be set, and the thread will be paused once the execute method exits.
     */
    public synchronized void stop() {
        if (thread != null) {
            stopped = true;
            restart = true;
        }
    }

    public boolean interrupted() {
        return restart;
    }

    public synchronized ThreadOperationTimer getTimer(int timeout) {
        if (timer == null) {
            timer = ThreadOperationTimer.start(thread, timeout);
        }
        return timer;
    }

    public abstract void execute();
}
