package micdoodle8.mods.galacticraft.core.tile;

import micdoodle8.mods.galacticraft.core.GCCoreAnnotations.NetworkedField;
import micdoodle8.mods.galacticraft.core.network.IPacketReceiver;
import net.minecraft.nbt.NBTTagCompound;
import cpw.mods.fml.relauncher.Side;

/**
 * GCCoreTileEntityFallenMeteor.java
 * 
 * This file is part of the Galacticraft project
 * 
 * @author micdoodle8
 * @license Lesser GNU Public License v3 (http://www.gnu.org/licenses/lgpl.html)
 * 
 */
public class GCCoreTileEntityFallenMeteor extends GCCoreTileEntityAdvanced implements IPacketReceiver
{
	public static final int MAX_HEAT_LEVEL = 5000;
	@NetworkedField(targetSide = Side.CLIENT)
	private int heatLevel = GCCoreTileEntityFallenMeteor.MAX_HEAT_LEVEL;

	@Override
	public void updateEntity()
	{
		super.updateEntity();

		if (!this.worldObj.isRemote && this.heatLevel > 0)
		{
			this.heatLevel--;
		}

		if (this.heatLevel % 20 == 0 && this.heatLevel != 0)
		{
			this.worldObj.markBlockForRenderUpdate(this.xCoord, this.yCoord, this.zCoord);
		}
	}

	public int getHeatLevel()
	{
		return this.heatLevel;
	}

	public void setHeatLevel(int heatLevel)
	{
		this.heatLevel = heatLevel;
	}

	public float getScaledHeatLevel()
	{
		return (float) this.heatLevel / GCCoreTileEntityFallenMeteor.MAX_HEAT_LEVEL;
	}

	@Override
	public void readFromNBT(NBTTagCompound nbt)
	{
		super.readFromNBT(nbt);
		this.heatLevel = nbt.getInteger("MeteorHeatLevel");
	}

	@Override
	public void writeToNBT(NBTTagCompound nbt)
	{
		super.writeToNBT(nbt);
		nbt.setInteger("MeteorHeatLevel", this.heatLevel);
	}

	@Override
	public double getPacketRange()
	{
		return 50;
	}

	@Override
	public int getPacketCooldown()
	{
		return 100;
	}

	@Override
	public boolean isNetworkedTile()
	{
		return true;
	}
}
