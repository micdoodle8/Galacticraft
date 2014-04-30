package codechicken.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

public class TaskProfiler
{
    public static class ProfilerResult
    {
        public final String name;
        public final long time;
        public final double fraction;
        
        public ProfilerResult(String name, long time, long totalTime)
        {
            this.name = name;
            this.time = time;
            fraction = time/(double)totalTime;
        }
    }
    
    public HashMap<String, Long> times = new HashMap<String, Long>();
    
    public String currentSection;
    private long startTime;
    private long totalTime;
    
    public void start(String section)
    {
        if(currentSection != null)
            end();
        
        currentSection = section;
        startTime = System.nanoTime();
    }
    
    public void end()
    {
        long time = System.nanoTime()-startTime;
        totalTime+=time;
        
        Long prev = times.get(currentSection);
        if(prev == null)
            prev = 0L;
        times.put(currentSection, prev+time);
        currentSection = null;
    }
    
    public List<ProfilerResult> getResults()
    {
        ArrayList<ProfilerResult> results = new ArrayList<ProfilerResult>(times.size());
        for(Entry<String, Long> e : times.entrySet())
            results.add(new ProfilerResult(e.getKey(), e.getValue(), totalTime));
        return results;
    }

    public void clear()
    {
        if(currentSection != null)
            end();
        times.clear();
        totalTime = 0;
    }
}
