package codechicken.lib.thread;

/**
 * Provides an abstract way to have a task run and restart if needed whilst still operating.
 * It is recommended to implement
 */
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

    /**
     * Clears the current task.
     * Not recommended to be called directly.
     */
    public void clearTasks() {
        thread = null;
        timer = null;
    }

    /**
     * Marks the Task to be restarted.
     * You may call this to start the process too.
     */
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

    /**
     * This must be implemented in your execute method, if this returns true it means you need to get your operation in to a restartable state and prepare for a restart.
     *
     * @return True if the process should end.
     */
    public boolean interrupted() {
        return restart;
    }

    public synchronized ThreadOperationTimer getTimer(int timeout) {
        if (timer == null) {
            timer = ThreadOperationTimer.start(thread, timeout);
        }
        return timer;
    }

    /**
     * Main call point for the RestartableTask, this is where you will put your logic to be called.
     * Make sure you implement some sort of bail out system if interrupted() is true.
     */
    public abstract void execute();
}
