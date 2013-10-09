package codechicken.core.featurehack;

import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

public class EntityRenderHook extends Entity
{
    public static interface IRenderCallback
    {
        public void render(float frame, int pass);
        
        public boolean shouldRenderInPass(int pass);

        public boolean isValid();
    }
    
    public final IRenderCallback callback;
    public EntityRenderHook(World world, double x, double y, double z, IRenderCallback callback)
    {
        super(world);
        setPosition(x, y, z);
        ignoreFrustumCheck = true;
        this.callback = callback;
    }

    @Override
    public void onUpdate()
    {
        if(!callback.isValid())
            setDead();
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
    
    //TODO: @Override
    public boolean shouldRenderInPass(int pass)
    {
        return callback.shouldRenderInPass(pass);
    }
}
