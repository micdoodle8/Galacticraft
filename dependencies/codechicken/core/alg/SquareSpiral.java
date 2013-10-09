package codechicken.core.alg;

import net.minecraft.nbt.NBTTagCompound;
import codechicken.lib.vec.BlockCoord;

public class SquareSpiral
{
    public SquareSpiral()
    {
        imax = Integer.MAX_VALUE;
    }
    
    public SquareSpiral(int squareSize)
    {
        imax = squareSize*squareSize;
    }
    
    public SquareSpiral(NBTTagCompound tag)
    {
        x = tag.getInteger("x");
        z = tag.getInteger("z");
        i = tag.getInteger("i");
        side = tag.getInteger("side");
        sidelenth = tag.getInteger("sidelenth");
        iside = tag.getInteger("iside");
        imax = tag.getInteger("imax");
    }
    
    private int x = 0;
    private int z = 0;
    private int side = 0;
    private int sidelenth = 1;
    private int iside = 0;
    private int i = 0;
    private int imax;
    
    public BlockCoord next()
    {
        BlockCoord r = new BlockCoord(x, 0, z);
        i++;
        iside++;
        switch(side)
        {
            case 0: x++; break;
            case 1: z++; break;
            case 2: x--; break;
            case 3: z--; break;
        }
        if(iside == sidelenth)
        {
            side = (side+1)%4;
            if(side%2==0)
                sidelenth++;
            iside = 0;
        }
        return r;
    }
    
    public boolean hasNext()
    {
        return i < imax;
    }

    public NBTTagCompound saveTag()
    {
        NBTTagCompound tag = new NBTTagCompound();
        tag.setInteger("x", x);
        tag.setInteger("z", z);
        tag.setInteger("i", i);
        tag.setInteger("side", side);
        tag.setInteger("sidelenth", sidelenth);
        tag.setInteger("iside", iside);
        tag.setInteger("imax", imax);
        return tag;
    }
}
