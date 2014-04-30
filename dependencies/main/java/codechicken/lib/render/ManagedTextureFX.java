package codechicken.lib.render;

public class ManagedTextureFX extends TextureFX
{
    public boolean changed;
    
    public ManagedTextureFX(int size, String name)
    {
        super(size, name);
        imageData = new int[size*size];
    }
    
    @Override
    public void setup()
    {
    }
    
    public void setData(int[] data)
    {
        System.arraycopy(data, 0, imageData, 0, imageData.length);
        changed = true;
    }
    
    @Override
    public boolean changed()
    {
        boolean r = changed;
        changed = false;
        return r;
    }
}
