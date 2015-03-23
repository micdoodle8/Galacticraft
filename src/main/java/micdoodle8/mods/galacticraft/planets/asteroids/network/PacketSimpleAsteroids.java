package micdoodle8.mods.galacticraft.planets.asteroids.network;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import micdoodle8.mods.galacticraft.api.vector.BlockVec3;
import micdoodle8.mods.galacticraft.api.vector.Vector3;
import micdoodle8.mods.galacticraft.core.network.IPacket;
import micdoodle8.mods.galacticraft.core.network.NetworkUtil;
import micdoodle8.mods.galacticraft.core.util.GCLog;
import micdoodle8.mods.galacticraft.core.util.PlayerUtil;
import micdoodle8.mods.galacticraft.planets.asteroids.entities.EntityGrapple;
import micdoodle8.mods.galacticraft.planets.asteroids.tile.TileEntityMinerBase;
import micdoodle8.mods.galacticraft.planets.asteroids.tile.TileEntityShortRangeTelepad;
import net.minecraft.client.entity.EntityClientPlayerMP;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.tileentity.TileEntity;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class PacketSimpleAsteroids implements IPacket
{
    public static enum EnumSimplePacketAsteroids
    {
        // SERVER
        S_UPDATE_ADVANCED_GUI(Side.SERVER, Integer.class, Integer.class, Integer.class, Integer.class, Integer.class),
        S_REQUEST_MINERBASE_FACING(Side.CLIENT, Integer.class, Integer.class, Integer.class),
        // CLIENT
        C_TELEPAD_SEND(Side.CLIENT, BlockVec3.class, Integer.class),
        C_UPDATE_GRAPPLE_POS(Side.CLIENT, Integer.class, Vector3.class),
        C_UPDATE_MINERBASE_FACING(Side.CLIENT, Integer.class, Integer.class, Integer.class, Integer.class, Integer.class, Integer.class, Integer.class);

        private Side targetSide;
        private Class<?>[] decodeAs;

        private EnumSimplePacketAsteroids(Side targetSide, Class<?>... decodeAs)
        {
            this.targetSide = targetSide;
            this.decodeAs = decodeAs;
        }

        public Side getTargetSide()
        {
            return this.targetSide;
        }

        public Class<?>[] getDecodeClasses()
        {
            return this.decodeAs;
        }
    }

    private EnumSimplePacketAsteroids type;
    private List<Object> data;

    public PacketSimpleAsteroids()
    {

    }

    public PacketSimpleAsteroids(EnumSimplePacketAsteroids packetType, Object[] data)
    {
        this(packetType, Arrays.asList(data));
    }

    public PacketSimpleAsteroids(EnumSimplePacketAsteroids packetType, List<Object> data)
    {
        if (packetType.getDecodeClasses().length != data.size())
        {
            GCLog.info("Simple Packet found data length different than packet type");
        }

        this.type = packetType;
        this.data = data;
    }

    @Override
    public void encodeInto(ChannelHandlerContext context, ByteBuf buffer)
    {
        buffer.writeInt(this.type.ordinal());

        try
        {
            NetworkUtil.encodeData(buffer, this.data);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public void decodeInto(ChannelHandlerContext context, ByteBuf buffer)
    {
        this.type = EnumSimplePacketAsteroids.values()[buffer.readInt()];

        if (this.type.getDecodeClasses().length > 0)
        {
            this.data = NetworkUtil.decodeData(this.type.getDecodeClasses(), buffer);
        }
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void handleClientSide(EntityPlayer player)
    {
        EntityClientPlayerMP playerBaseClient = null;

        if (player instanceof EntityClientPlayerMP)
        {
            playerBaseClient = (EntityClientPlayerMP) player;
        }

        TileEntity tile;
        switch (this.type)
        {
        case C_TELEPAD_SEND:
            Entity entity = playerBaseClient.worldObj.getEntityByID((Integer) this.data.get(1));

            if (entity != null && entity instanceof EntityLivingBase)
            {
                BlockVec3 pos = (BlockVec3) this.data.get(0);
                entity.setPosition(pos.x + 0.5, pos.y + 2.2, pos.z + 0.5);
            }
            break;
        case C_UPDATE_GRAPPLE_POS:
            entity = playerBaseClient.worldObj.getEntityByID((Integer) this.data.get(0));
            if (entity != null && entity instanceof EntityGrapple)
            {
                Vector3 vec = (Vector3) this.data.get(1);
                entity.setPosition(vec.x, vec.y, vec.z);
            }
            break;
        case C_UPDATE_MINERBASE_FACING:
        	tile = player.worldObj.getTileEntity((Integer) this.data.get(0), (Integer) this.data.get(1), (Integer) this.data.get(2));
        	int facingNew = (Integer) this.data.get(3);
        	if (tile instanceof TileEntityMinerBase)
        	{
        		((TileEntityMinerBase)tile).facing = facingNew;
        		((TileEntityMinerBase)tile).setMainBlockPos((Integer) this.data.get(4), (Integer) this.data.get(5), (Integer) this.data.get(6));
        	}     	
        	break;
        default:
            break;
        }
    }

    @Override
    public void handleServerSide(EntityPlayer player)
    {
        EntityPlayerMP playerBase = PlayerUtil.getPlayerBaseServerFromPlayer(player, false);

        switch (this.type)
        {
        case S_UPDATE_ADVANCED_GUI:
            TileEntity tile = player.worldObj.getTileEntity((Integer) this.data.get(1), (Integer) this.data.get(2), (Integer) this.data.get(3));

            switch ((Integer) this.data.get(0))
            {
            case 0:
                if (tile instanceof TileEntityShortRangeTelepad)
                {
                    TileEntityShortRangeTelepad launchController = (TileEntityShortRangeTelepad) tile;
                    launchController.setAddress((Integer) this.data.get(4));
                }
                break;
            case 1:
                if (tile instanceof TileEntityShortRangeTelepad)
                {
                    TileEntityShortRangeTelepad launchController = (TileEntityShortRangeTelepad) tile;
                    launchController.setTargetAddress((Integer) this.data.get(4));
                }
                break;
            default:
                break;
            }
            break;
        case S_REQUEST_MINERBASE_FACING:
        	tile = player.worldObj.getTileEntity((Integer) this.data.get(0), (Integer) this.data.get(1), (Integer) this.data.get(2));
        	if (tile instanceof TileEntityMinerBase)
        	{
            	((TileEntityMinerBase)tile).updateClientFlag = true; 
        	}
        	break;
        default:
            break;
        }
    }
}
