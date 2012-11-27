package micdoodle8.mods.galacticraft.core.entities;

import net.minecraft.src.AxisAlignedBB;
import net.minecraft.src.Direction;
import net.minecraft.src.Entity;
import net.minecraft.src.EntityLiving;
import net.minecraft.src.Item;
import net.minecraft.src.ItemStack;
import net.minecraft.src.NBTTagCompound;
import net.minecraft.src.World;

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
        this.setSize(1F, 3F);
    }
    
    public GCCoreEntityFlag(World par1World, double x, double y, double z, int dir, EntityLiving entityPlacedBy)
    {
        this(par1World);
        this.setDirection(dir);
        this.setPosition(x, y, z);
        this.xPosition = x;
        this.yPosition = y;
        this.zPosition = z;
        this.entityPlacedBy = entityPlacedBy;
    }
    
    public void setDirection(int par1)
    {
        this.facingDirection = par1;
        this.prevRotationYaw = this.rotationYaw = (float)(par1 * 90);
        float var2 = (float)this.getWidth();
        float var3 = (float)this.getHeight();
        float var4 = (float)this.getWidth();

        if (par1 != 2 && par1 != 0)
        {
            var2 = 0.5F;
        }
        else
        {
            var4 = 0.5F;
            this.rotationYaw = this.prevRotationYaw = (float)(Direction.footInvisibleFaceRemap[par1] * 90);
        }

        var2 /= 32.0F;
        var3 /= 32.0F;
        var4 /= 32.0F;
        float var5 = (float)this.xPosition + 0.5F;
        float var6 = (float)this.yPosition + 0.5F;
        float var7 = (float)this.zPosition + 0.5F;
        float var8 = 0.5625F;

        var6 += 0.5F;
        this.setPosition((double)var5, (double)var6, (double)var7);
        float var9 = -0.03125F;
        this.boundingBox.setBounds((double)(var5 - var2 - var9), (double)(var6 - var3 - var9), (double)(var7 - var4 - var9), (double)(var5 + var2 + var9), (double)(var6 + var3 + var9), (double)(var7 + var4 + var9));
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
		
	}

	@Override
    public void readEntityFromNBT(NBTTagCompound par1NBTTagCompound)
    {
        if (par1NBTTagCompound.hasKey("Direction"))
        {
            this.facingDirection = par1NBTTagCompound.getByte("Direction");
        }
        else
        {
            switch (par1NBTTagCompound.getByte("Dir"))
            {
                case 0:
                    this.facingDirection = 2;
                    break;
                case 1:
                    this.facingDirection = 1;
                    break;
                case 2:
                    this.facingDirection = 0;
                    break;
                case 3:
                    this.facingDirection = 3;
            }
        }

        this.xPosition = par1NBTTagCompound.getDouble("TileX");
        this.yPosition = par1NBTTagCompound.getDouble("TileY");
        this.zPosition = par1NBTTagCompound.getDouble("TileZ");
        this.setDirection(this.facingDirection);
    }

	@Override
	protected void writeEntityToNBT(NBTTagCompound par1NBTTagCompound) 
	{
        par1NBTTagCompound.setByte("Direction", (byte)this.facingDirection);
        par1NBTTagCompound.setDouble("TileX", this.xPosition);
        par1NBTTagCompound.setDouble("TileY", this.yPosition);
        par1NBTTagCompound.setDouble("TileZ", this.zPosition);

        switch (this.facingDirection)
        {
            case 0:
                par1NBTTagCompound.setByte("Dir", (byte)2);
                break;
            case 1:
                par1NBTTagCompound.setByte("Dir", (byte)1);
                break;
            case 2:
                par1NBTTagCompound.setByte("Dir", (byte)0);
                break;
            case 3:
                par1NBTTagCompound.setByte("Dir", (byte)3);
        }
	}
	
    public void dropItemStack()
    {
        this.entityDropItem(new ItemStack(Item.painting), 0.0F); // TODO
    }
}
