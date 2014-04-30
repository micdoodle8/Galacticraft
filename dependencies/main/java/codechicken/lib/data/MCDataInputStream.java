package codechicken.lib.data;

import java.io.InputStream;

public class MCDataInputStream extends InputStream
{
    private MCDataInput in;
    
    public MCDataInputStream(MCDataInput in)
    {
        this.in = in;
    }
    
    @Override
    public int read()
    {
        return in.readByte()&0xFF;
    }
}
