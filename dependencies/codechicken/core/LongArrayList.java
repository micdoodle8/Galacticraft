package codechicken.core;

public class LongArrayList
{
    private long[] array = new long[256];
    private int size = 0;
    
    public void expand(int newsize)
    {
        if(array.length < newsize)
        {
            int expanded = array.length + (array.length >> 1);//*1.1
            if(expanded < newsize)
                expanded = newsize;
            
            long[] newarray = new long[expanded];
            System.arraycopy(array, 0, newarray, 0, array.length);
            array = newarray;
        }
    }
    
    public void add(long element)
    {
        expand(size+1);
        array[size] = element;
        size++;
    }
    
    public void clear()
    {
        size = 0;
    }
    
    public long get(int index)
    {
        if(index >= size)
            throw new ArrayIndexOutOfBoundsException(index);
        return array[index];
    }

    public int size()
    {
        return size;
    }
}
