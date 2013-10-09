package codechicken.core.featurehack;

import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

public class EntityUpdateHook extends Entity
{
    public static interface IUpdateCallback
    {
        public void onUpdate();

        public boolean isValid();
    }
    
    public final IUpdateCallback callback;
    public EntityUpdateHook(World world, int x, int y, int z, IUpdateCallback callback)
    {
        super(world);
        setPosition(x, y, z);
        this.callback = callback;
    }

    @Override
    public void onUpdate()
    {
        if(!callback.isValid())
            setDead();
        else
            callback.onUpdate();
    }

    @Override
    protected void entityInit()
    {
    }

    @Override
    protected void readEntityFromNBT(NBTTagCompound var1)
    {
    }

    @Override
    protected void writeEntityToNBT(NBTTagCompound var1)
    {
    }
}
