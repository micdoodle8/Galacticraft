package codechicken.core;

public class ArrayUtils
{
    public static int[] byteArrayToIntArray(byte[] ab)
    {
        if(ab.length % 4 != 0)
        {
            byte[] abtemp = new byte[(ab.length/4+1)*4];
            System.arraycopy(ab, 0, abtemp, 0, ab.length);
            ab = abtemp;
        }
        int[] ai = new int[ab.length/4];

        for (int i = 0; i < ai.length; i++) 
        {
            ai[i] = ((ab[i*4]& 0xFF) << 24)
                    +((ab[i*4+1] & 0xFF) << 16)
                    +((ab[i*4+2] & 0xFF) << 8)
                    +(ab[i*4+3] & 0xFF);
        }
        
        return ai;
    }
    
    public static byte[] intArrayToByteArray(int[] ai)
    {
        byte[] ab = new byte[ai.length * 4];

        for (int i = 0; i < ai.length; i++) 
        {
            ab[i*4] = (byte)(ai[i] >> 24);
            ab[i*4+1] = (byte)(ai[i] >> 16);
            ab[i*4+2] = (byte)(ai[i] >> 8);
            ab[i*4+3] = (byte)ai[i];
        }
        
        return ab;
    }
    
    public static int hashCode(int[] ai)
    {
        int hashcode = 0;
        for(int i : ai)
        {
            hashcode = hashcode * 31 + i;
        }
        return hashcode;
    }
    
    public static int hashCode(byte[] ab)
    {
        int hashcode = 0;
        for(byte b : ab)
        {
            hashcode = hashcode * 31 + b;
        }
        return hashcode;
    }

    public static String toString(byte[] ab)
    {
        StringBuilder stringbuild = new StringBuilder().append("[");
        boolean first = true;
        for(byte b : ab)
        {
            if(first)
            {
                first = false;
            }
            else
            {
                stringbuild.append(",");
            }
            stringbuild.append(b);
        }
        return stringbuild.append("]").toString();
    }
    
    public static String toString(int[] ai)
    {
        StringBuilder stringbuild = new StringBuilder().append("[");
        boolean first = true;
        for(int i : ai)
        {
            if(first)
            {
                first = false;
            }
            else
            {
                stringbuild.append(",");
            }
            stringbuild.append(i);
        }
        return stringbuild.append("]").toString();
    }
}
