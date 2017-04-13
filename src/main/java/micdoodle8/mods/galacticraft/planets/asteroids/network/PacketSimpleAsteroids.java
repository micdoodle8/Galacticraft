package micdoodle8.mods.galacticraft.planets.asteroids.network;

import io.netty.buffer.ByteBuf;
import micdoodle8.mods.galacticraft.api.vector.BlockVec3;
import micdoodle8.mods.galacticraft.api.vector.Vector3;
import micdoodle8.mods.galacticraft.core.network.NetworkUtil;
import micdoodle8.mods.galacticraft.core.network.PacketBase;
import micdoodle8.mods.galacticraft.core.util.GCLog;
import micdoodle8.mods.galacticraft.core.util.PlayerUtil;
import micdoodle8.mods.galacticraft.planets.asteroids.entities.EntityGrapple;
import micdoodle8.mods.galacticraft.planets.asteroids.tile.TileEntityShortRangeTelepad;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class PacketSimpleAsteroids extends PacketBase
{
    public static enum EnumSimplePacketAsteroids
    {
        // SERVER
        S_UPDATE_ADVANCED_GUI(Side.SERVER, Integer.class, BlockPos.class, Integer.class),
        // CLIENT
        C_TELEPAD_SEND(Side.CLIENT, BlockVec3.class, Integer.class),
        C_UPDATE_GRAPPLE_POS(Side.CLIENT, Integer.class, Vector3.class);

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
        super();
    }

    public PacketSimpleAsteroids(EnumSimplePacketAsteroids packetType, int dimID, Object[] data)
    {
        this(packetType, dimID, Arrays.asList(data));
    }

    public PacketSimpleAsteroids(EnumSimplePacketAsteroids packetType, int dimID, List<Object> data)
    {
        super(dimID);

        if (packetType.getDecodeClasses().length != data.size())
        {
            GCLog.info("Asteroids Simple Packet found data length different than packet type: " + packetType.name());
        }

        this.type = packetType;
        this.data = data;
    }

    @Override
    public void encodeInto(ByteBuf buffer)
    {
        super.encodeInto(buffer);
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
    public void decodeInto(ByteBuf buffer)
    {
        super.decodeInto(buffer);
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
        EntityPlayerSP playerBaseClient = null;

        if (player instanceof EntityPlayerSP)
        {
            playerBaseClient = (EntityPlayerSP) player;
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
            TileEntity tile = player.worldObj.getTileEntity((BlockPos) this.data.get(1));

            switch ((Integer) this.data.get(0))
            {
            case 0:
                if (tile instanceof TileEntityShortRangeTelepad)
                {
                    TileEntityShortRangeTelepad launchController = (TileEntityShortRangeTelepad) tile;
                    launchController.setAddress((Integer) this.data.get(2));
                }
                break;
            case 1:
                if (tile instanceof TileEntityShortRangeTelepad)
                {
                    TileEntityShortRangeTelepad launchController = (TileEntityShortRangeTelepad) tile;
                    launchController.setTargetAddress((Integer) this.data.get(2));
                }
                break;
            default:
                break;
            }
            break;
        default:
            break;
        }
    }
}
