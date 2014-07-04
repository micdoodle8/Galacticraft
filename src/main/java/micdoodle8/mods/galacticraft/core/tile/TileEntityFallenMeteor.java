package micdoodle8.mods.galacticraft.core.tile;

import cpw.mods.fml.relauncher.Side;
import micdoodle8.mods.galacticraft.core.network.IPacketReceiver;
import micdoodle8.mods.miccore.Annotations.NetworkedField;
import net.minecraft.nbt.NBTTagCompound;

public class TileEntityFallenMeteor extends TileEntityAdvanced implements IPacketReceiver
{
	public static final int MAX_HEAT_LEVEL = 5000;
	@NetworkedField(targetSide = Side.CLIENT)
	private int heatLevel = TileEntityFallenMeteor.MAX_HEAT_LEVEL;

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
			this.worldObj.func_147479_m(this.xCoord, this.yCoord, this.zCoord);
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
		return (float) this.heatLevel / TileEntityFallenMeteor.MAX_HEAT_LEVEL;
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
