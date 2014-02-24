package micdoodle8.mods.galacticraft.mars.tile;

import micdoodle8.mods.galacticraft.mars.entities.GCMarsEntitySlimeling;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.pathfinding.PathEntity;
import net.minecraft.tileentity.TileEntity;

/**
 * GCMarsTileEntitySlimelingEgg.java
 * 
 * This file is part of the Galacticraft project
 * 
 * @author micdoodle8
 * @license Lesser GNU Public License v3 (http://www.gnu.org/licenses/lgpl.html)
 * 
 */
public class GCMarsTileEntitySlimelingEgg extends TileEntity
{
	public int timeToHatch = -1;
	public String lastTouchedPlayer = "NoPlayer";

	@Override
	public void updateEntity()
	{
		super.updateEntity();

		if (!this.worldObj.isRemote)
		{
			if (this.timeToHatch > 0)
			{
				this.timeToHatch--;
			}
			else if (this.timeToHatch == 0)
			{
				int metadata = this.worldObj.getBlockMetadata(this.xCoord, this.yCoord, this.zCoord) % 3;

				float colorRed = 0.0F;
				float colorGreen = 0.0F;
				float colorBlue = 0.0F;

				switch (metadata)
				{
				case 0:
					colorRed = 1.0F;
					break;
				case 1:
					colorBlue = 1.0F;
					break;
				case 2:
					colorRed = 1.0F;
					colorGreen = 1.0F;
					break;
				}

				GCMarsEntitySlimeling slimeling = new GCMarsEntitySlimeling(this.worldObj, colorRed, colorGreen, colorBlue);

				slimeling.setPosition(this.xCoord + 0.5, this.yCoord + 1.0, this.zCoord + 0.5);
				slimeling.setOwner(this.lastTouchedPlayer);

				if (!this.worldObj.isRemote)
				{
					this.worldObj.spawnEntityInWorld(slimeling);
				}

				slimeling.setTamed(true);
				slimeling.setPathToEntity((PathEntity) null);
				slimeling.setAttackTarget((EntityLivingBase) null);
				slimeling.setHealth(20.0F);
				slimeling.setOwner(this.lastTouchedPlayer);

				this.worldObj.setBlockToAir(this.xCoord, this.yCoord, this.zCoord);
			}
		}
	}

	@Override
	public void readFromNBT(NBTTagCompound nbt)
	{
		super.readFromNBT(nbt);
		this.timeToHatch = nbt.getInteger("TimeToHatch");
		this.lastTouchedPlayer = nbt.getString("Owner");
	}

	@Override
	public void writeToNBT(NBTTagCompound nbt)
	{
		super.writeToNBT(nbt);
		nbt.setInteger("TimeToHatch", this.timeToHatch);
		nbt.setString("Owner", this.lastTouchedPlayer);
	}
}
