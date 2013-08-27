package micdoodle8.mods.galacticraft.core.tile;

import java.util.HashSet;
import mekanism.api.Object3D;
import mekanism.api.gas.GasNetwork;
import mekanism.api.gas.GasTransmission;
import mekanism.api.gas.IGasTransmitter;
import mekanism.api.gas.ITubeConnection;
import mekanism.api.transmitters.ITransmitter;
import mekanism.api.transmitters.TransmissionType;
import mekanism.api.transmitters.TransmitterNetworkRegistry;
import micdoodle8.mods.galacticraft.api.tile.IColorable;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet250CustomPayload;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraftforge.common.ForgeDirection;
import universalelectricity.core.vector.Vector3;
import universalelectricity.prefab.network.IPacketReceiver;
import universalelectricity.prefab.network.PacketManager;
import com.google.common.io.ByteArrayDataInput;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class GCCoreTileEntityOxygenPipe extends TileEntity implements ITubeConnection, IGasTransmitter, IColorable, IPacketReceiver
{
    private byte pipeColor = 15;
    private byte preLoadColor;
    private byte preColorCooldown;
    private boolean setColor = false;

    public GasNetwork theNetwork;

    @Override
    public boolean canTransferGasToTube(TileEntity tile)
    {
        if (tile instanceof IColorable)
        {
            if (this.getColor() == ((IColorable) tile).getColor())
            {
                return true;
            }
            else
            {
                return false;
            }
        }

        return tile instanceof IGasTransmitter;
    }
    
    @Override
    public void onChunkUnload() 
    {
        invalidate();
        TransmitterNetworkRegistry.getInstance().pruneEmptyNetworks();
    }
    
    @Override
    @SideOnly(Side.CLIENT)
    public AxisAlignedBB getRenderBoundingBox()
    {
        return INFINITE_EXTENT_AABB;
    }
    
    @Override
    public boolean areTransmitterNetworksEqual(TileEntity tileEntity)
    {
        return tileEntity instanceof ITransmitter && getTransmissionType() == ((ITransmitter)tileEntity).getTransmissionType();
    }
    
    @Override
    public GasNetwork getTransmitterNetwork()
    {
        return getTransmitterNetwork(true);
    }
    
    @Override
    public void setTransmitterNetwork(GasNetwork network)
    {
        if(network != theNetwork)
        {
            removeFromTransmitterNetwork();
            theNetwork = network;
        }
    }

    @Override
    public boolean canUpdate()
    {
        return !this.setColor;
    }

    @Override
    public void updateEntity()
    {
        if (this.preColorCooldown > 0)
        {
            this.preColorCooldown--;
        }

        if (this.preColorCooldown == 0 && !this.worldObj.isRemote && this.preLoadColor != -1)
        {
            PacketManager.sendPacketToClients(PacketManager.getPacket(GalacticraftCore.CHANNELENTITIES, this, this.getColor(), this.preLoadColor));
            this.preLoadColor = -1;
            this.setColor = true;
        }

        if (this.preColorCooldown == 0 && this.worldObj.isRemote && this.preLoadColor == 0)
        {
            final Vector3 thisVec = new Vector3(this);
            this.worldObj.markBlockForRenderUpdate(thisVec.intX(), thisVec.intY(), thisVec.intZ());
            this.preLoadColor = -1;
            this.setColor = true;
        }
    }

    @Override
    public void validate()
    {
        super.validate();

        this.preColorCooldown = 40;

        if (this.worldObj != null && this.worldObj.isRemote)
        {
            final Vector3 thisVec = new Vector3(this);
            this.worldObj.markBlockForRenderUpdate(thisVec.intX(), thisVec.intY(), thisVec.intZ());
        }
    }

    @Override
    public void setColor(byte col)
    {
        this.pipeColor = col;

        if (this.worldObj != null && this.worldObj.isRemote)
        {
            final Vector3 thisVec = new Vector3(this);
            this.worldObj.markBlockForRenderUpdate(thisVec.intX(), thisVec.intY(), thisVec.intZ());
        }
    }

    @Override
    public byte getColor()
    {
        return this.pipeColor;
    }

    @Override
    public void onAdjacentColorChanged(ForgeDirection direction)
    {
        this.worldObj.markBlockForUpdate(this.xCoord, this.yCoord, this.zCoord);
    }

    @Override
    public void readFromNBT(NBTTagCompound par1NBTTagCompound)
    {
        super.readFromNBT(par1NBTTagCompound);

        final byte by = par1NBTTagCompound.getByte("pipeColor");
        this.setColor(by);
        this.preLoadColor = by;
    }

    @Override
    public void writeToNBT(NBTTagCompound par1NBTTagCompound)
    {
        super.writeToNBT(par1NBTTagCompound);

        par1NBTTagCompound.setByte("pipeColor", this.getColor());
    }

    @Override
    public boolean canTubeConnect(ForgeDirection side)
    {
        final TileEntity tile = this.worldObj.getBlockTileEntity(this.xCoord + side.offsetX, this.yCoord + side.offsetY, this.zCoord + side.offsetZ);

        if (tile != null && tile instanceof IColorable)
        {
            final byte color = ((IColorable) tile).getColor();

            if (color == this.getColor())
            {
                return true;
            }
            else
            {
                return false;
            }
        }

        return true;
    }

    @Override
    public void handlePacketData(INetworkManager network, int packetType, Packet250CustomPayload packet, EntityPlayer player, ByteArrayDataInput dataStream)
    {
        try
        {
            if (this.worldObj.isRemote)
            {
                this.setColor(dataStream.readByte());
                this.preLoadColor = dataStream.readByte();
            }
        }
        catch (final Exception e)
        {
            e.printStackTrace();
        }
    }

    
    @Override
    public TransmissionType getTransmissionType()
    {
        return TransmissionType.GAS;
    }
    
    @SuppressWarnings("unchecked")
    @Override
    public GasNetwork getTransmitterNetwork(boolean createIfNull)
    {
        if (theNetwork == null && createIfNull)
        {
            TileEntity[] adjacentTubes = GasTransmission.getConnectedTubes(this);
            HashSet<GasNetwork> connectedNets = new HashSet<GasNetwork>();
            
            for (TileEntity tube : adjacentTubes)
            {
                if (TransmissionType.checkTransmissionType(tube, TransmissionType.GAS, this) && ((ITransmitter<GasNetwork>) tube).getTransmitterNetwork(false) != null)
                {
                    connectedNets.add(((ITransmitter<GasNetwork>)tube).getTransmitterNetwork());
                }
            }
            
            if(connectedNets.size() == 0 || worldObj.isRemote)
            {
                theNetwork = new GasNetwork(this);
            }
            else if(connectedNets.size() == 1)
            {
                theNetwork = (GasNetwork)connectedNets.iterator().next();
                theNetwork.transmitters.add(this);
            }
            else 
            {
                theNetwork = new GasNetwork(connectedNets);
                theNetwork.transmitters.add(this);
            }
        }
        
        return theNetwork;
    }

    @Override
    public void fixTransmitterNetwork()
    {
        getTransmitterNetwork().fixMessedUpNetwork(this);
    }

    @Override
    public void invalidate()
    {
        if(!worldObj.isRemote)
        {
            getTransmitterNetwork().split(this);
        }
        
        super.invalidate();
    }
    
    @Override
    public void removeFromTransmitterNetwork()
    {
        if(theNetwork != null)
        {
            theNetwork.removeTransmitter(this);
        }
    }
    
    @SuppressWarnings("unchecked")
    @Override
    public void refreshTransmitterNetwork() 
    {
        if (!worldObj.isRemote)
        {
            for (ForgeDirection side : ForgeDirection.VALID_DIRECTIONS)
            {
                TileEntity tileEntity = Object3D.get(this).getFromSide(side).getTileEntity(worldObj);
                
                if (TransmissionType.checkTransmissionType(tileEntity, TransmissionType.GAS, this))
                {
                    getTransmitterNetwork().merge(((ITransmitter<GasNetwork>)tileEntity).getTransmitterNetwork());
                }
            }
            
            getTransmitterNetwork().refresh();
        }
    }
    
    @Override
    public int getTransmitterNetworkSize()
    {
        return getTransmitterNetwork().getSize();
    }

    @Override
    public int getTransmitterNetworkAcceptorSize()
    {
        return getTransmitterNetwork().getAcceptorSize();
    }

    @Override
    public String getTransmitterNetworkNeeded()
    {
        return getTransmitterNetwork().getNeeded();
    }

    @Override
    public String getTransmitterNetworkFlow()
    {
        return getTransmitterNetwork().getFlow();
    }
}
