package codechicken.lib.data;

import java.io.OutputStream;

public class MCDataOutputStream extends OutputStream
{
    private MCDataOutput out;
    
    public MCDataOutputStream(MCDataOutput out)
    {
        this.out = out;
    }
    
    @Override
    public void write(int b)
    {
        out.writeByte(b);
    }
}
