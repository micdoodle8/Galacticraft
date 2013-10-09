package codechicken.core;

public class ReentrantCheck
{
    private boolean entered;
    
    public ReentrantCheck()
    {
        entered = false;
    }
    
    public boolean entered()
    {
        return entered;
    }
    
    public void enter()
    {
        entered = true;
    }
    
    public void exit()
    {
        entered = false;
    }

    public static ThreadLocal<ReentrantCheck> threadLocal()
    {
        return new ThreadLocal<ReentrantCheck>()
        {
            @Override
            protected ReentrantCheck initialValue()
            {
                return new ReentrantCheck();
            }
        };
    }
}
