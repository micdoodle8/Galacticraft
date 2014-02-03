package micdoodle8.mods.galacticraft.mars.entities;

import micdoodle8.mods.galacticraft.api.vector.Vector3;
import micdoodle8.mods.galacticraft.core.entities.ISizeable;
import micdoodle8.mods.galacticraft.mars.tile.GCMarsTileEntityTerraformer;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

/**
 * GCMarsEntityTerraformBubble.java
 * 
 * This file is part of the Galacticraft project
 * 
 * @author micdoodle8
 * @license Lesser GNU Public License v3 (http://www.gnu.org/licenses/lgpl.html)
 * 
 */
public class GCMarsEntityTerraformBubble extends Entity implements ISizeable
{
	protected long ticks = 0;

	public GCMarsTileEntityTerraformer terraformer;

	public GCMarsEntityTerraformBubble(World world, Vector3 mainBlockVec, GCMarsTileEntityTerraformer terraformer)
	{
		this(world);
		this.posX = mainBlockVec.x + 0.5D;
		this.posY = mainBlockVec.y + 1.0D;
		this.posZ = mainBlockVec.z + 0.5D;
		this.terraformer = terraformer;
	}

	public GCMarsEntityTerraformBubble(World world)
	{
		super(world);
		this.noClip = true;
		this.isImmuneToFire = true;
		this.ignoreFrustumCheck = true;
		this.renderDistanceWeight = 5.0D;
	}

	@Override
	protected boolean pushOutOfBlocks(double par1, double par3, double par5)
	{
		return false;
	}

	@Override
	public AxisAlignedBB getBoundingBox()
	{
		return null;
	}

	@Override
	public boolean canBePushed()
	{
		return false;
	}

	@Override
	public void moveEntity(double par1, double par3, double par5)
	{
		;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void setPositionAndRotation2(double par1, double par3, double par5, float par7, float par8, int par9)
	{
		this.setPosition(par1, par3, par5);
		this.setRotation(par7, par8);
	}

	@Override
	public void onUpdate()
	{
		if (this.ticks >= Long.MAX_VALUE)
		{
			this.ticks = 1;
		}

		this.ticks++;

		if (this.terraformer != null)
		{
			final Vector3 vec = new Vector3(this.terraformer);

			this.posX = vec.x + 0.5D;
			this.posY = vec.y + 1.0D;
			this.posZ = vec.z + 0.5D;
		}

		super.onUpdate();

		final TileEntity tileAt = this.worldObj.getBlockTileEntity(MathHelper.floor_double(this.posX), MathHelper.floor_double(this.posY - 1.0), MathHelper.floor_double(this.posZ));

		if (tileAt instanceof GCMarsTileEntityTerraformer)
		{
			this.terraformer = (GCMarsTileEntityTerraformer) tileAt;
		}

		if (this.terraformer != null && (this.terraformer.terraformBubble == null || this.terraformer.terraformBubble.equals(this)) && !this.worldObj.isRemote)
		{
			this.terraformer.terraformBubble = this;
		}
		else if (!this.worldObj.isRemote)
		{
			this.setDead();
		}

		if (tileAt == null && !this.worldObj.isRemote)
		{
			this.setDead();
		}

		if (this.terraformer != null)
		{
			final Vector3 vec = new Vector3(this.terraformer);

			this.posX = vec.x + 0.5D;
			this.posY = vec.y + 1.0D;
			this.posZ = vec.z + 0.5D;
		}
	}

	@Override
	public boolean canBeCollidedWith()
	{
		return false;
	}

	public void setSize(float bubbleSize)
	{
		if (this.terraformer == null)
		{
			return;
		}

		this.terraformer.size = bubbleSize;
	}

	@Override
	public float getSize()
	{
		if (this.terraformer == null)
		{
			return 0.0F;
		}

		return this.terraformer.size;
	}

	@Override
	protected void entityInit()
	{
	}

	@Override
	protected void readEntityFromNBT(NBTTagCompound nbttagcompound)
	{
	}

	@Override
	protected void writeEntityToNBT(NBTTagCompound nbttagcompound)
	{
	}
}
