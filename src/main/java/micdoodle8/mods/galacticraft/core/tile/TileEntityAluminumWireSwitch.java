package micdoodle8.mods.galacticraft.core.tile;

import ic2.api.energy.tile.IEnergyAcceptor;
import ic2.api.energy.tile.IEnergyEmitter;
import micdoodle8.mods.galacticraft.api.transmission.NetworkType;
import micdoodle8.mods.galacticraft.api.transmission.grid.IElectricityNetwork;
import micdoodle8.mods.galacticraft.api.transmission.grid.IGridNetwork;
import micdoodle8.mods.galacticraft.api.transmission.tile.IConnector;
import micdoodle8.mods.galacticraft.api.transmission.tile.INetworkProvider;
import micdoodle8.mods.galacticraft.api.vector.BlockVec3;
import micdoodle8.mods.galacticraft.core.energy.grid.EnergyNetwork;
import micdoodle8.mods.galacticraft.core.energy.tile.TileBaseConductor;
import micdoodle8.mods.galacticraft.core.energy.tile.TileBaseUniversalConductor;
import micdoodle8.mods.galacticraft.core.util.RedstoneUtil;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;

public class TileEntityAluminumWireSwitch extends TileBaseUniversalConductor
{
    public int tier;
    private boolean disableConnections;

    public TileEntityAluminumWireSwitch()
    {
        this(1);
    }

    public TileEntityAluminumWireSwitch(int theTier)
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
        this.disableConnections = this.disableConnections();
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
    
    @Override
    public void refresh()
    {
    	boolean newDisableConnections = this.disableConnections();
    	if (newDisableConnections && !this.disableConnections)
    	{
    		this.disableConnections = newDisableConnections;
        	if (!this.world.isRemote)
            {
        		this.disConnect();
            }
    	}
    	else if (!newDisableConnections && this.disableConnections)
    	{
    		this.disableConnections = newDisableConnections;
        	if (!this.world.isRemote)
            {
        		this.setNetwork(null);  //Force a full network refresh of this and conductors either side
            }
    	}

    	if (!this.world.isRemote)
        {
            this.adjacentConnections = null;
            if (!this.disableConnections)
            {
            	this.getNetwork().refresh();

            	BlockVec3 thisVec = new BlockVec3(this);
            	for (EnumFacing side : EnumFacing.VALUES)
            	{
            		if (this.canConnect(side, NetworkType.POWER))
            		{
            			TileEntity tileEntity = thisVec.getTileEntityOnSide(this.world, side);

            			if (tileEntity instanceof TileBaseConductor && ((TileBaseConductor)tileEntity).canConnect(side.getOpposite(), NetworkType.POWER))
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
    public boolean canConnect(EnumFacing direction, NetworkType type)
    {
        return type == NetworkType.POWER && !this.disableConnections();
    }
    
    @Override
    public IElectricityNetwork getNetwork()
    {
        if (this.network == null)
        {
            EnergyNetwork network = new EnergyNetwork();
            if (!this.disableConnections) network.getTransmitters().add(this);
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
            		EnumFacing side = EnumFacing.getFront(i);
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

    //IC2
    @Override
    public boolean acceptsEnergyFrom(IEnergyEmitter emitter, EnumFacing side)
    {
    	return this.disableConnections() ? false : super.acceptsEnergyFrom(emitter, side);   	
    }

    //IC2
    @Override
    public double injectEnergy(EnumFacing directionFrom, double amount, double voltage)
    {
    	return this.disableConnections ? amount : super.injectEnergy(directionFrom, amount, voltage);   	   	
    }
    
    //IC2
    @Override
    public boolean emitsEnergyTo(IEnergyAcceptor receiver, EnumFacing side)
    {
    	return this.disableConnections() ? false : super.emitsEnergyTo(receiver, side);
    }

    //RF
    @Override
    public int receiveEnergy(EnumFacing from, int maxReceive, boolean simulate)
    {
    	return this.disableConnections ? 0 : super.receiveEnergy(from, maxReceive, simulate);
    }

    //RF
    @Override
    public boolean canConnectEnergy(EnumFacing from)
    {
    	return this.disableConnections() ? false : super.canConnectEnergy(from);
    }

    //Mekanism
    @Override
    public boolean canReceiveEnergy(EnumFacing side)
    {
    	return this.disableConnections() ? false : super.canReceiveEnergy(side);
    }
}
