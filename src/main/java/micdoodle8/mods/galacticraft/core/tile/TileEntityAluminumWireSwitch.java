package micdoodle8.mods.galacticraft.core.tile;

import micdoodle8.mods.galacticraft.api.transmission.NetworkType;
import micdoodle8.mods.galacticraft.api.transmission.grid.IElectricityNetwork;
import micdoodle8.mods.galacticraft.api.transmission.grid.IGridNetwork;
import micdoodle8.mods.galacticraft.api.transmission.tile.IConnector;
import micdoodle8.mods.galacticraft.api.transmission.tile.INetworkProvider;
import micdoodle8.mods.galacticraft.api.vector.BlockVec3;
import micdoodle8.mods.galacticraft.core.BlockNames;
import micdoodle8.mods.galacticraft.core.Constants;
import micdoodle8.mods.galacticraft.core.energy.grid.EnergyNetwork;
import micdoodle8.mods.galacticraft.core.energy.tile.TileBaseConductor;
import micdoodle8.mods.galacticraft.core.energy.tile.TileBaseUniversalConductor;
import micdoodle8.mods.galacticraft.core.util.RedstoneUtil;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraftforge.registries.ObjectHolder;

public class TileEntityAluminumWireSwitch extends TileBaseUniversalConductor
{
    public static class TileEntityAluminumWireSwitchableT1 extends TileEntityAluminumWireSwitch
    {
        @ObjectHolder(Constants.MOD_ID_CORE + ":" + BlockNames.aluminumWireSwitchable)
        public static TileEntityType<TileEntityAluminumWireSwitchableT1> TYPE;

        public TileEntityAluminumWireSwitchableT1()
        {
            super(TYPE, 1);
        }
    }

    public static class TileEntityAluminumWireSwitchableT2 extends TileEntityAluminumWireSwitch
    {
        @ObjectHolder(Constants.MOD_ID_CORE + ":" + BlockNames.aluminumWireSwitchableHeavy)
        public static TileEntityType<TileEntityAluminumWireSwitchableT2> TYPE;

        public TileEntityAluminumWireSwitchableT2()
        {
            super(TYPE, 2);
        }
    }

    public int tier;
    private boolean disableConnections;

    public TileEntityAluminumWireSwitch(TileEntityType<?> type, int tier)
    {
        super(type);
        this.tier = tier;
    }

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
        this.disableConnections = this.disableConnections();
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

    @Override
    public void refresh()
    {
        boolean newDisableConnections = this.disableConnections();
        if (newDisableConnections && !this.disableConnections)
        {
            this.disableConnections = true;
            if (!this.world.isRemote)
            {
                this.disConnect();
            }
        }
        else if (!newDisableConnections && this.disableConnections)
        {
            this.disableConnections = false;
            if (!this.world.isRemote)
            {
                this.setNetwork(null);  //Force a full network refresh of this and conductors either LogicalSide
            }
        }

        if (!this.world.isRemote)
        {
            this.adjacentConnections = null;
            if (!this.disableConnections)
            {
                this.getNetwork().refresh();

                BlockVec3 thisVec = new BlockVec3(this);
                for (Direction side : Direction.values())
                {
                    if (this.canConnect(side, NetworkType.POWER))
                    {
                        TileEntity tileEntity = thisVec.getTileEntityOnSide(this.world, side);

                        if (tileEntity instanceof TileBaseConductor && ((TileBaseConductor) tileEntity).canConnect(side.getOpposite(), NetworkType.POWER))
                        {
                            IGridNetwork otherNet = ((INetworkProvider) tileEntity).getNetwork();
                            if (!this.getNetwork().equals(otherNet))
                            {
                                if (!otherNet.getTransmitters().isEmpty())
                                {
                                    otherNet.merge(this.getNetwork());
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private void disConnect()
    {
        EnergyNetwork net = (EnergyNetwork) this.getNetwork();
        if (net != null)
        {
            net.split(this);
        }
    }

    private boolean disableConnections()
    {
        return RedstoneUtil.isBlockReceivingRedstone(this.world, this.pos);
    }

    @Override
    public boolean canConnect(Direction direction, NetworkType type)
    {
        return type == NetworkType.POWER && !this.disableConnections();
    }

    @Override
    public IElectricityNetwork getNetwork()
    {
        if (this.network == null)
        {
            EnergyNetwork network = new EnergyNetwork();
            if (!this.disableConnections)
            {
                network.getTransmitters().add(this);
            }
            this.setNetwork(network);
        }

        return (IElectricityNetwork) this.network;
    }

    @Override
    public TileEntity[] getAdjacentConnections()
    {
        if (this.adjacentConnections == null)
        {
            this.adjacentConnections = new TileEntity[6];

            if (!this.disableConnections)
            {
                BlockVec3 thisVec = new BlockVec3(this);
                for (int i = 0; i < 6; i++)
                {
                    Direction side = Direction.byIndex(i);
                    if (this.canConnect(side, NetworkType.POWER))
                    {
                        TileEntity tileEntity = thisVec.getTileEntityOnSide(this.world, side);

                        if (tileEntity instanceof IConnector)
                        {
                            if (((IConnector) tileEntity).canConnect(side.getOpposite(), NetworkType.POWER))
                            {
                                this.adjacentConnections[i] = tileEntity;
                            }
                        }
                    }
                }
            }
        }

        return this.adjacentConnections;
    }

//    //IC2
//    @Override
//    public boolean acceptsEnergyFrom(IEnergyEmitter emitter, Direction side)
//    {
//    	return this.disableConnections() ? false : super.acceptsEnergyFrom(emitter, LogicalSide);
//    }
//
//    //IC2
//    @Override
//    public double injectEnergy(Direction directionFrom, double amount, double voltage)
//    {
//    	return this.disableConnections ? amount : super.injectEnergy(directionFrom, amount, voltage);
//    }
//
//    //IC2
//    @Override
//    public boolean emitsEnergyTo(IEnergyAcceptor receiver, Direction side)
//    {
//    	return this.disableConnections() ? false : super.emitsEnergyTo(receiver, LogicalSide);
//    }
//
//    //RF
//    @Override
//    public int receiveEnergy(Direction from, int maxReceive, boolean simulate)
//    {
//    	return this.disableConnections ? 0 : super.receiveEnergy(from, maxReceive, simulate);
//    }
//
//    //RF
//    @Override
//    public boolean canConnectEnergy(Direction from)
//    {
//    	return this.disableConnections() ? false : super.canConnectEnergy(from);
//    }
//
//    //Mekanism
//    @Override
//    public boolean canReceiveEnergy(Direction side)
//    {
//    	return this.disableConnections() ? false : super.canReceiveEnergy(LogicalSide);
//    }
}
