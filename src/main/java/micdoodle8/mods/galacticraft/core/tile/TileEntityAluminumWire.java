package micdoodle8.mods.galacticraft.core.tile;

import micdoodle8.mods.galacticraft.core.energy.tile.TileBaseUniversalConductor;
import net.minecraft.nbt.NBTTagCompound;

public class TileEntityAluminumWire extends TileBaseUniversalConductor
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
        if (this.tier == 0)
        {
            this.tier = 1;
        }
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound nbt)
    {
        super.writeToNBT(nbt);
        nbt.setInteger("tier", this.tier);
        return nbt;
    }

    @Override
    public NBTTagCompound getUpdateTag()
    {
        return this.writeToNBT(new NBTTagCompound());
    }

    @Override
    public int getTierGC()
    {
        return this.tier;
    }
}
