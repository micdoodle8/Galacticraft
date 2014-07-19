package micdoodle8.mods.galacticraft.core.tile;

import net.minecraft.nbt.NBTTagCompound;

public class TileEntityAluminumWire extends TileEntityUniversalConductor
{
	public int tier;

	public TileEntityAluminumWire()
	{
		this(1);
	}

	public TileEntityAluminumWire(int theTier)
	{
		this.tier = theTier;
	}

	@Override
	public void readFromNBT(NBTTagCompound nbt)
	{
		super.readFromNBT(nbt);
		this.tier = nbt.getInteger("tier");
		//For legacy worlds (e.g. converted from 1.6.4)
		if (this.tier == 0) this.tier = 1;
	}

	@Override
	public void writeToNBT(NBTTagCompound nbt)
	{
		super.writeToNBT(nbt);
		nbt.setInteger("tier", this.tier);
	}

	@Override
	public double getPacketRange()
	{
		return 0;
	}

	@Override
	public int getPacketCooldown()
	{
		return 0;
	}

	@Override
	public boolean isNetworkedTile()
	{
		return false;
	}

	@Override
	public int getTierGC()
	{
		return this.tier;
	}
}
