package codechicken.obfuscator;

import java.io.OutputStream;

public class DummyOutputStream extends OutputStream
{
    public static DummyOutputStream instance = new DummyOutputStream();

    @Override
    public void write(int b)
    {
    }
}
