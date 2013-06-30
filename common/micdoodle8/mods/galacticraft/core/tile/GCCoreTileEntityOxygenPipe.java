package micdoodle8.mods.galacticraft.core.tile;

import mekanism.api.EnumGas;
import mekanism.api.GasNetwork;
import mekanism.api.IPressurizedTube;
import mekanism.api.ITubeConnection;
import mekanism.api.Object3D;
import micdoodle8.mods.galacticraft.API.IColorable;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet250CustomPayload;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.ForgeDirection;
import universalelectricity.core.vector.Vector3;
import universalelectricity.prefab.network.IPacketReceiver;
import universalelectricity.prefab.network.PacketManager;
import com.google.common.io.ByteArrayDataInput;

public class GCCoreTileEntityOxygenPipe extends TileEntity implements ITubeConnection, IPressurizedTube, IColorable, IPacketReceiver
{
    private byte pipeColor = 15;
    private byte preLoadColor;
    private byte preColorCooldown;
    private boolean setColor = false;

    public GasNetwork gasNetwork;

    @Override
    public boolean canTransferGas()
    {
        return true;
    }

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

        return true;
    }

    @Override
    public boolean canUpdate()
    {
        return !this.setColor;
    }

    @Override
    public GasNetwork getNetwork()
    {
        if (this.gasNetwork == null)
        {
            this.gasNetwork = new GasNetwork(this);
        }

        return this.gasNetwork;
    }

    @Override
    public void invalidate()
    {
        if (!this.worldObj.isRemote)
        {
            this.getNetwork().split(this);
        }

        super.invalidate();
    }

    @Override
    public void setNetwork(GasNetwork network)
    {
        this.gasNetwork = network;
    }

    @Override
    public void refreshNetwork()
    {
        if (!this.worldObj.isRemote)
        {
            if (this.canTransferGas())
            {
                for (ForgeDirection side : ForgeDirection.VALID_DIRECTIONS)
                {
                    TileEntity tileEntity = Object3D.get(this).getFromSide(side).getTileEntity(this.worldObj);

                    if (tileEntity instanceof IPressurizedTube)
                    {
                        if (((IPressurizedTube) tileEntity).canTransferGasToTube(this) && this.canTransferGasToTube(tileEntity))
                        {
                            this.getNetwork().merge(((IPressurizedTube) tileEntity).getNetwork());
                        }
                        else
                        {
                            ((IPressurizedTube) tileEntity).getNetwork().split(this);
                            this.getNetwork().split((IPressurizedTube) tileEntity);
                        }
                    }
                }

                this.getNetwork().refresh();
            }
            else
            {
                this.getNetwork().split(this);
            }
        }
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
    public void onAdjacentColorChanged(Vector3 thisVec, Vector3 updatedVec)
    {
        this.worldObj.markBlockForUpdate(thisVec.intX(), thisVec.intY(), thisVec.intZ());
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
    public void onTransfer(EnumGas type)
    {
        ;
    }
}
