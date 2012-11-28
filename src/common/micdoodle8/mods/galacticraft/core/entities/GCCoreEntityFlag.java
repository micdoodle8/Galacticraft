package micdoodle8.mods.galacticraft.core.entities;

import net.minecraft.src.AxisAlignedBB;
import net.minecraft.src.Direction;
import net.minecraft.src.Entity;
import net.minecraft.src.EntityLiving;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.Item;
import net.minecraft.src.ItemStack;
import net.minecraft.src.NBTTagCompound;
import net.minecraft.src.World;
import cpw.mods.fml.common.FMLLog;

public class GCCoreEntityFlag extends Entity
{
	public EntityLiving entityPlacedBy;
    public int facingDirection;
    public double xPosition;
    public double yPosition;
    public double zPosition;
    
    public GCCoreEntityFlag(World world)
    {
    	super(world);
        this.yOffset = 1.5F;
        this.setSize(0.4F, 3F);
    }
    
    public GCCoreEntityFlag(World par1World, double x, double y, double z, float dir)
    {
        this(par1World);
        this.setDirection(dir);
        this.setPosition(x, y, z);
        this.xPosition = x;
        this.yPosition = y;
        this.zPosition = z;
    }
    
    public void setDirection(float par1)
    {
        this.prevRotationYaw = this.rotationYaw = (float)(par1);
    }

    public int getWidth()
    {
    	return 25;
    }

    public int getHeight()
    {
    	return 40;
    }

	@Override
    public boolean canBeCollidedWith()
    {
        return true;
    }

    @Override
	protected boolean canTriggerWalking()
    {
        return false;
    }

    @Override
	public AxisAlignedBB getCollisionBox(Entity par1Entity)
    {
        return par1Entity.boundingBox;
    }

    @Override
	public AxisAlignedBB getBoundingBox()
    {
        return boundingBox;
    }

    @Override
	public boolean canBePushed()
    {
        return false;
    }

	@Override
    public void moveEntity(double par1, double par3, double par5)
    {
        if (!this.worldObj.isRemote && !this.isDead && par1 * par1 + par3 * par3 + par5 * par5 > 0.0D)
        {
            this.setDead();
            this.dropItemStack();
        }
    }

	@Override
    public void addVelocity(double par1, double par3, double par5)
    {
        if (!this.worldObj.isRemote && !this.isDead && par1 * par1 + par3 * par3 + par5 * par5 > 0.0D)
        {
            this.setDead();
            this.dropItemStack();
        }
    }

	@Override
	protected void entityInit() 
	{
        this.dataWatcher.addObject(16, new Integer(-1));
        this.dataWatcher.addObject(17, new String(""));
	}

	@Override
    public void readEntityFromNBT(NBTTagCompound par1NBTTagCompound)
    {
		this.setOwner(par1NBTTagCompound.getString("Owner"));
		this.setType(par1NBTTagCompound.getInteger("Type"));
		
        this.xPosition = par1NBTTagCompound.getDouble("TileX");
        this.yPosition = par1NBTTagCompound.getDouble("TileY");
        this.zPosition = par1NBTTagCompound.getDouble("TileZ");
        this.setDirection(this.facingDirection);
    }

	@Override
	protected void writeEntityToNBT(NBTTagCompound par1NBTTagCompound) 
	{
        par1NBTTagCompound.setString("Owner", String.valueOf(this.getOwner()));
        par1NBTTagCompound.setInteger("Type", Integer.valueOf(this.getType()));
        par1NBTTagCompound.setByte("Direction", (byte)this.facingDirection);
        par1NBTTagCompound.setDouble("TileX", this.xPosition);
        par1NBTTagCompound.setDouble("TileY", this.yPosition);
        par1NBTTagCompound.setDouble("TileZ", this.zPosition);
	}
	
    public void dropItemStack()
    {
        this.entityDropItem(new ItemStack(Item.painting), 0.0F); // TODO
    }

    @Override
    public boolean interact(EntityPlayer par1EntityPlayer)
    {
    	this.setDirection(this.rotationYaw + 3F);
    	
        return true;
    }
    
    public void setType(int par1)
    {
        this.dataWatcher.updateObject(16, Integer.valueOf(par1));
    }

    public int getType()
    {
        return this.dataWatcher.getWatchableObjectInt(16);
    }
    
    public void setOwner(String par1)
    {
        this.dataWatcher.updateObject(17, String.valueOf(par1));
    }
    
    public String getOwner()
    {
        return this.dataWatcher.getWatchableObjectString(17);
    }
}
