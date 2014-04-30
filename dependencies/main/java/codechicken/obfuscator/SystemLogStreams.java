package codechicken.obfuscator;

import java.io.PrintStream;

public class SystemLogStreams implements ILogStreams
{
    public static SystemLogStreams inst = new SystemLogStreams();
    
    @Override
    public PrintStream err()
    {
        return System.err;
    }
    
    @Override
    public PrintStream out()
    {
        return System.out;
    }
}
