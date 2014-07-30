package micdoodle8.mods.galacticraft.planets.asteroids.dimension;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.WorldSavedData;

public class AsteroidSaveData extends WorldSavedData
{
	public static final String saveDataID = "GCAsteroidData";
	public NBTTagCompound datacompound;
	private NBTTagCompound alldata;

	public AsteroidSaveData(String s)
	{
		super(AsteroidSaveData.saveDataID);
		this.datacompound = new NBTTagCompound();
	}

	@Override
	public void readFromNBT(NBTTagCompound nbt)
	{
		this.datacompound = nbt.getCompoundTag("asteroids");
	}

	@Override
	public void writeToNBT(NBTTagCompound nbt)
	{
		nbt.setTag("asteroids", this.datacompound);
	}
}
