package micdoodle8.mods.galacticraft.core.tile;

import net.minecraft.nbt.NBTTagCompound;

/**
 * GCCoreTileEntityAluminumWire.java
 * 
 * This file is part of the Galacticraft project
 * 
 * @author micdoodle8
 * @license Lesser GNU Public License v3 (http://www.gnu.org/licenses/lgpl.html)
 * 
 */
public class GCCoreTileEntityAluminumWire extends GCCoreTileEntityUniversalConductor
{
	public float resistance;
	public float amperage;

	public GCCoreTileEntityAluminumWire()
	{
		this(0.05F, 200.0F);
	}

	public GCCoreTileEntityAluminumWire(float resistance, float amperage)
	{
		this.resistance = resistance;
		this.amperage = amperage;
	}

	@Override
	public float getResistance()
	{
		return this.resistance;
	}

	@Override
	public float getCurrentCapacity()
	{
		return this.amperage;
	}

	@Override
	public void readFromNBT(NBTTagCompound nbt)
	{
		super.readFromNBT(nbt);
		this.resistance = nbt.getFloat("resistance");
		this.amperage = nbt.getFloat("amperage");
	}

	@Override
	public void writeToNBT(NBTTagCompound nbt)
	{
		super.writeToNBT(nbt);
		nbt.setFloat("resistance", this.resistance);
		nbt.setFloat("amperage", this.amperage);
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
}
