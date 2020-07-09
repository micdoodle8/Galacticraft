package micdoodle8.mods.galacticraft.core.energy.tile;

import micdoodle8.mods.galacticraft.api.power.EnergySource;
import micdoodle8.mods.galacticraft.api.power.EnergySource.EnergySourceAdjacent;
import micdoodle8.mods.galacticraft.api.power.IEnergyHandlerGC;
import micdoodle8.mods.galacticraft.api.transmission.NetworkType;
import micdoodle8.mods.galacticraft.api.transmission.tile.IElectrical;
import micdoodle8.mods.galacticraft.core.Annotations.NetworkedField;
import micdoodle8.mods.galacticraft.core.tile.ReceiverMode;
import micdoodle8.mods.galacticraft.core.tile.TileEntityAdvanced;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraftforge.fml.LogicalSide;

public abstract class EnergyStorageTile extends TileEntityAdvanced implements IEnergyHandlerGC, IElectrical
{
    public static final float STANDARD_CAPACITY = 16000F;

    @NetworkedField(targetSide = LogicalSide.CLIENT)
    public EnergyStorage storage = new EnergyStorage(STANDARD_CAPACITY, 10);
    public int tierGC = 1;
    @NetworkedField(targetSide = LogicalSide.CLIENT)
    public int poweredByTierGC = 1;

    public EnergyStorageTile(TileEntityType<?> type)
    {
        super(type);
    }

    @Override
    public void read(CompoundNBT nbt)
    {

        super.read(nbt);
        this.storage.readFromNBT(nbt);
    }

    @Override
    public CompoundNBT write(CompoundNBT nbt)
    {
        super.write(nbt);
        this.storage.writeToNBT(nbt);
        return nbt;
    }

    @Override
    public CompoundNBT getUpdateTag()
    {
        return this.write(new CompoundNBT());
    }

    public abstract ReceiverMode getModeFromDirection(Direction direction);

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
    public boolean canConnect(Direction direction, NetworkType type)
    {
        return false;
    }

    //Five methods for compatibility with basic electricity
    @Override
    public float receiveElectricity(Direction from, float receive, int tier, boolean doReceive)
    {
        this.poweredByTierGC = (tier < 6) ? tier : 6;
        return this.storage.receiveEnergyGC(receive, !doReceive);
    }

    @Override
    public float provideElectricity(Direction from, float request, boolean doProvide)
    {
        return this.storage.extractEnergyGC(request, !doProvide);
    }

    @Override
    public float getRequest(Direction direction)
    {
        return Math.min(this.storage.getCapacityGC() - this.storage.getEnergyStoredGC(), this.storage.getMaxReceive());
    }

    @Override
    public float getProvide(Direction direction)
    {
        return 0;
    }

    @Override
    public int getTierGC()
    {
        return this.tierGC;
    }

    public void setTierGC(int newTier)
    {
        this.tierGC = newTier;
    }
}