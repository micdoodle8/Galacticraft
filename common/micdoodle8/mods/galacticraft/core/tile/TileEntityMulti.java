package micdoodle8.mods.galacticraft.core.tile;

import micdoodle8.mods.galacticraft.core.blocks.BlockMulti;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.Packet250CustomPayload;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import universalelectricity.core.vector.Vector3;
import universalelectricity.prefab.network.IPacketReceiver;
import universalelectricity.prefab.network.PacketManager;
import com.google.common.io.ByteArrayDataInput;
import cpw.mods.fml.common.network.PacketDispatcher;

/**
 * This is a multiblock to be used for blocks that are bigger than one block.
 * 
 * @author Calclavia
 * 
 */
public class TileEntityMulti extends TileEntity implements IPacketReceiver
{
    // The the position of the main block
    public Vector3 mainBlockPosition;
    public String channel;
    private long ticks;

    public TileEntityMulti()
    {

    }

    public TileEntityMulti(String channel)
    {
        this.channel = channel;
    }

    @Override
    public void updateEntity()
    {
        super.updateEntity();

        if (!this.worldObj.isRemote && this.mainBlockPosition != null)
        {
            if (this.ticks >= Long.MAX_VALUE)
            {
                this.ticks = 0;
            }

            if (this.ticks % 50 == 0)
            {
                PacketDispatcher.sendPacketToAllAround(this.xCoord, this.yCoord, this.zCoord, 30, this.worldObj.provider.dimensionId, this.getDescriptionPacket());
            }

            this.ticks++;
        }
    }

    public void setMainBlock(Vector3 mainBlock)
    {
        this.mainBlockPosition = mainBlock;

        if (!this.worldObj.isRemote)
        {
            this.worldObj.markBlockForUpdate(this.xCoord, this.yCoord, this.zCoord);
        }
    }

    @Override
    public Packet getDescriptionPacket()
    {
        if (this.mainBlockPosition != null)
        {
            if (this.channel == null || this.channel == "" && this.getBlockType() instanceof BlockMulti)
            {
                this.channel = ((BlockMulti) this.getBlockType()).channel;
            }

            return PacketManager.getPacket(this.channel, this, this.mainBlockPosition.intX(), this.mainBlockPosition.intY(), this.mainBlockPosition.intZ());

        }

        return null;
    }

    public void onBlockRemoval()
    {
        if (this.mainBlockPosition != null)
        {
            TileEntity tileEntity = this.worldObj.getBlockTileEntity(this.mainBlockPosition.intX(), this.mainBlockPosition.intY(), this.mainBlockPosition.intZ());

            if (tileEntity != null && tileEntity instanceof IMultiBlock)
            {
                IMultiBlock mainBlock = (IMultiBlock) tileEntity;

                if (mainBlock != null)
                {
                    mainBlock.onDestroy(this);
                }
            }
        }
    }

    public boolean onBlockActivated(World par1World, int x, int y, int z, EntityPlayer par5EntityPlayer)
    {
        if (this.mainBlockPosition != null)
        {
            TileEntity tileEntity = this.worldObj.getBlockTileEntity(this.mainBlockPosition.intX(), this.mainBlockPosition.intY(), this.mainBlockPosition.intZ());

            if (tileEntity != null)
            {
                if (tileEntity instanceof IMultiBlock)
                {
                    return ((IMultiBlock) tileEntity).onActivated(par5EntityPlayer);
                }
            }
        }

        return false;
    }

    /**
     * Reads a tile entity from NBT.
     */
    @Override
    public void readFromNBT(NBTTagCompound nbt)
    {
        super.readFromNBT(nbt);
        this.mainBlockPosition = new Vector3(nbt.getCompoundTag("mainBlockPosition"));
    }

    /**
     * Writes a tile entity to NBT.
     */
    @Override
    public void writeToNBT(NBTTagCompound nbt)
    {
        super.writeToNBT(nbt);

        if (this.mainBlockPosition != null)
        {
            nbt.setCompoundTag("mainBlockPosition", this.mainBlockPosition.writeToNBT(new NBTTagCompound()));
        }
    }

    @Override
    public void handlePacketData(INetworkManager network, int packetType, Packet250CustomPayload packet, EntityPlayer player, ByteArrayDataInput dataStream)
    {
        try
        {
            this.mainBlockPosition = new Vector3(dataStream.readInt(), dataStream.readInt(), dataStream.readInt());
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
