package micdoodle8.mods.galacticraft.core.energy.tile;

import cpw.mods.fml.relauncher.Side;
import micdoodle8.mods.galacticraft.api.power.EnergySource;
import micdoodle8.mods.galacticraft.api.power.EnergySource.EnergySourceAdjacent;
import micdoodle8.mods.galacticraft.api.power.IEnergyHandlerGC;
import micdoodle8.mods.galacticraft.api.transmission.NetworkType;
import micdoodle8.mods.galacticraft.api.transmission.tile.IElectrical;
import micdoodle8.mods.galacticraft.core.tile.ReceiverMode;
import micdoodle8.mods.galacticraft.core.tile.TileEntityAdvanced;
import micdoodle8.mods.miccore.Annotations.NetworkedField;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.ForgeDirection;

public abstract class EnergyStorageTile extends TileEntityAdvanced implements IEnergyHandlerGC, IElectrical
{
    @NetworkedField(targetSide = Side.CLIENT)
    public EnergyStorage storage = new EnergyStorage(16000, 10);
    public int tierGC = 1;
    public int poweredByTierGC = 1;

    @Override
    public void readFromNBT(NBTTagCompound nbt)
    {

        super.readFromNBT(nbt);
        this.storage.readFromNBT(nbt);
    }

    @Override
    public void writeToNBT(NBTTagCompound nbt)
    {

        super.writeToNBT(nbt);
        this.storage.writeToNBT(nbt);
    }

    public abstract ReceiverMode getModeFromDirection(ForgeDirection direction);

    @Override
    public float receiveEnergyGC(EnergySource from, float amount, boolean simulate)
    {
        return this.storage.receiveEnergyGC(amount, simulate);
    }

    @Override
    public float extractEnergyGC(EnergySource from, float amount, boolean simulate)
    {
        return this.storage.extractEnergyGC(amount, simulate);
    }

    @Override
    public boolean nodeAvailable(EnergySource from)
    {
        if (!(from instanceof EnergySourceAdjacent))
        {
            return false;
        }

        return this.getModeFromDirection(((EnergySourceAdjacent) from).direction) != ReceiverMode.UNDEFINED;
    }

    @Override
    public float getEnergyStoredGC(EnergySource from)
    {
        return this.storage.getEnergyStoredGC();
    }

    public float getEnergyStoredGC()
    {
        return this.storage.getEnergyStoredGC();
    }

    @Override
    public float getMaxEnergyStoredGC(EnergySource from)
    {
        return this.storage.getCapacityGC();
    }

    public float getMaxEnergyStoredGC()
    {
        return this.storage.getCapacityGC();
    }

    @Override
    public boolean canConnect(ForgeDirection direction, NetworkType type)
    {
        return false;
    }

    //Five methods for compatibility with basic electricity
    @Override
    public float receiveElectricity(ForgeDirection from, float receive, int tier, boolean doReceive)
    {
        this.poweredByTierGC = tier;
        return this.storage.receiveEnergyGC(receive, !doReceive);
    }

    @Override
    public float provideElectricity(ForgeDirection from, float request, boolean doProvide)
    {
        return this.storage.extractEnergyGC(request, !doProvide);
    }

    @Override
    public float getRequest(ForgeDirection direction)
    {
        return Math.min(this.storage.getCapacityGC() - this.storage.getEnergyStoredGC(), this.storage.getMaxReceive());
    }

    @Override
    public float getProvide(ForgeDirection direction)
    {
        return 0;
    }

    public int getTierGC()
    {
        return this.tierGC;
    }

    public void setTierGC(int newTier)
    {
        this.tierGC = newTier;
    }
}