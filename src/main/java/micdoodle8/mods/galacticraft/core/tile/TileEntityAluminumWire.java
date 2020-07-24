package micdoodle8.mods.galacticraft.core.tile;

import micdoodle8.mods.galacticraft.core.BlockNames;
import micdoodle8.mods.galacticraft.core.Constants;
import micdoodle8.mods.galacticraft.core.energy.tile.TileBaseUniversalConductor;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.registries.ObjectHolder;

public class TileEntityAluminumWire extends TileBaseUniversalConductor
{
    public static class TileEntityAluminumWireT1 extends TileEntityAluminumWire
    {
        @ObjectHolder(Constants.MOD_ID_CORE + ":" + BlockNames.aluminumWire)
        public static TileEntityType<TileEntityAluminumWireT1> TYPE;

        public TileEntityAluminumWireT1()
        {
            super(TYPE, 1);
        }
    }

    public static class TileEntityAluminumWireT2 extends TileEntityAluminumWire
    {
        @ObjectHolder(Constants.MOD_ID_CORE + ":" + BlockNames.aluminumWireHeavy)
        public static TileEntityType<TileEntityAluminumWireT2> TYPE;

        public TileEntityAluminumWireT2()
        {
            super(TYPE, 2);
        }
    }

    public int tier;

    public TileEntityAluminumWire(TileEntityType<?> type, int tier)
    {
        super(type);
        this.tier = tier;
    }

//    public TileEntityAluminumWire()
//    {
//        this(1);
//    }
//
//    public TileEntityAluminumWire(int theTier)
//    {
//        this.tier = theTier;
//    }

    @Override
    public void read(CompoundNBT nbt)
    {
        super.read(nbt);
        this.tier = nbt.getInt("tier");
        //For legacy worlds (e.g. converted from 1.6.4)
        if (this.tier == 0)
        {
            this.tier = 1;
        }
    }

    @Override
    public CompoundNBT write(CompoundNBT nbt)
    {
        super.write(nbt);
        nbt.putInt("tier", this.tier);
        return nbt;
    }

    @Override
    public CompoundNBT getUpdateTag()
    {
        return this.write(new CompoundNBT());
    }

    @Override
    public int getTierGC()
    {
        return this.tier;
    }
}
