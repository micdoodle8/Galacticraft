package codechicken.lib.thread;

public class ProfileTimer {

    public double decay;
    public long startTime;
    public long nanoTime;
    public int scanCount;
    public int logScans;
    public String logName;

    public ProfileTimer() {
        this(0.98);
    }

    public ProfileTimer(double decay) {
        this.decay = decay;
    }

    public ProfileTimer log(String logName, int scans) {
        this.logScans = scans;
        this.logName = logName;
        return this;
    }

    public void start() {
        startTime = System.nanoTime();
    }

    public void end() {
        long t = System.nanoTime() - startTime;
        nanoTime = (long) (nanoTime * decay + t * (1 - decay));

        scanCount++;
        if (logScans > 0 && scanCount % logScans == 0) {
            System.out.println("Profiled " + logName + " " + nanoTime + "ns");
        }

    }
}
