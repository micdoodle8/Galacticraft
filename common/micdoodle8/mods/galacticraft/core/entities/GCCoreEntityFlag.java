package micdoodle8.mods.galacticraft.core.entities;

import micdoodle8.mods.galacticraft.api.vector.Vector3;
import micdoodle8.mods.galacticraft.core.items.GCCoreItems;
import net.minecraft.block.Block;
import net.minecraft.block.BlockFence;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;

/**
 * GCCoreEntityFlag.java
 * 
 * This file is part of the Galacticraft project
 * 
 * @author micdoodle8
 * @license Lesser GNU Public License v3 (http://www.gnu.org/licenses/lgpl.html)
 * 
 */
public class GCCoreEntityFlag extends Entity
{
	public EntityLiving entityPlacedBy;
	public int facingDirection;
	public double xPosition;
	public double yPosition;
	public double zPosition;
	public boolean indestructable = false;

	public GCCoreEntityFlag(World world)
	{
		super(world);
		this.yOffset = 1.5F;
		this.setSize(0.4F, 3F);
		this.ignoreFrustumCheck = true;
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

	@Override
	public boolean attackEntityFrom(DamageSource par1DamageSource, float par2)
	{
		if (!this.worldObj.isRemote && !this.isDead && !this.indestructable)
		{
			if (this.isEntityInvulnerable())
			{
				return false;
			}
			else
			{
				this.setBeenAttacked();
				this.setDamage(this.getDamage() + par2 * 10);

				if (par1DamageSource.getEntity() instanceof EntityPlayer && ((EntityPlayer) par1DamageSource.getEntity()).capabilities.isCreativeMode)
				{
					this.setDamage(100.0F);
				}

				if (this.getDamage() > 40)
				{
					if (this.riddenByEntity != null)
					{
						this.riddenByEntity.mountEntity(this);
					}

					this.setDead();
					this.dropItemStack();
				}

				return true;
			}
		}
		else
		{
			return true;
		}
	}

	public void setDirection(float par1)
	{
		this.prevRotationYaw = this.rotationYaw = par1;
	}

	public void setIndestructable()
	{
		this.indestructable = true;
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
		return this.boundingBox;
	}

	@Override
	public boolean canBePushed()
	{
		return false;
	}

	@Override
	protected void entityInit()
	{
		this.dataWatcher.addObject(16, new Integer(-1));
		this.dataWatcher.addObject(17, new String(""));
		this.dataWatcher.addObject(18, new Float(0.0F));
	}

	@Override
	public void readEntityFromNBT(NBTTagCompound par1NBTTagCompound)
	{
		this.setOwner(par1NBTTagCompound.getString("Owner"));
		this.setType(par1NBTTagCompound.getInteger("Type"));
		this.indestructable = par1NBTTagCompound.getBoolean("Indestructable");

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
		par1NBTTagCompound.setBoolean("Indestructable", this.indestructable);
		par1NBTTagCompound.setByte("Direction", (byte) this.facingDirection);
		par1NBTTagCompound.setDouble("TileX", this.xPosition);
		par1NBTTagCompound.setDouble("TileY", this.yPosition);
		par1NBTTagCompound.setDouble("TileZ", this.zPosition);
	}

	public void dropItemStack()
	{
		this.entityDropItem(new ItemStack(GCCoreItems.flag, 1, this.getType()), 0.0F); // TODO
	}

	@Override
	public void onUpdate()
	{
		Vector3 vec = new Vector3(this.posX, this.posY, this.posZ);
		vec = vec.translate(new Vector3(0, -1, 0));
		final Block blockAt = Block.blocksList[vec.getBlockID(this.worldObj)];

		if (blockAt != null)
		{
			if (blockAt instanceof BlockFence)
			{

			}
			else if (blockAt.isAirBlock(this.worldObj, vec.intX(), vec.intY(), vec.intZ()))
			{
				this.motionY -= 0.02F;
			}
		}

		this.moveEntity(this.motionX, this.motionY, this.motionZ);
	}

	@Override
	public boolean interactFirst(EntityPlayer par1EntityPlayer)
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

	public void setDamage(float par1)
	{
		this.dataWatcher.updateObject(18, Float.valueOf(par1));
	}

	public float getDamage()
	{
		return this.dataWatcher.getWatchableObjectFloat(18);
	}
}
