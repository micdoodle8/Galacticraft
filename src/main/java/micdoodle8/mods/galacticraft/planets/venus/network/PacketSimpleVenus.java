package micdoodle8.mods.galacticraft.planets.venus.network;

import io.netty.buffer.ByteBuf;
import micdoodle8.mods.galacticraft.core.network.NetworkUtil;
import micdoodle8.mods.galacticraft.core.network.PacketBase;
import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
import micdoodle8.mods.galacticraft.core.util.GCLog;
import micdoodle8.mods.galacticraft.core.util.PlayerUtil;
import micdoodle8.mods.galacticraft.planets.asteroids.network.PacketSimpleAsteroids;
import micdoodle8.mods.galacticraft.planets.venus.tile.TileEntityLaserTurret;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.dimension.DimensionType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.network.NetworkEvent;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;

public class PacketSimpleVenus extends PacketBase
{
    public enum EnumSimplePacketVenus
    {
        // SERVER
        S_UPDATE_ADVANCED_GUI(LogicalSide.SERVER, Integer.class, BlockPos.class, Integer.class),
        S_OPEN_LASER_TURRET_GUI(LogicalSide.SERVER, BlockPos.class),
        S_MODIFY_LASER_TARGET(LogicalSide.SERVER, Integer.class, BlockPos.class, String.class);
        // CLIENT

        private final LogicalSide targetSide;
        private final Class<?>[] decodeAs;

        EnumSimplePacketVenus(LogicalSide targetSide, Class<?>... decodeAs)
        {
            this.targetSide = targetSide;
            this.decodeAs = decodeAs;
        }

        public LogicalSide getTargetSide()
        {
            return this.targetSide;
        }

        public Class<?>[] getDecodeClasses()
        {
            return this.decodeAs;
        }
    }

    private EnumSimplePacketVenus type;
    private List<Object> data;

    public PacketSimpleVenus()
    {
        super();
    }

    public PacketSimpleVenus(EnumSimplePacketVenus packetType, DimensionType dimID, Object[] data)
    {
        this(packetType, dimID, Arrays.asList(data));
    }

    public PacketSimpleVenus(EnumSimplePacketVenus packetType, DimensionType dimID, List<Object> data)
    {
        super(dimID);

        if (packetType.getDecodeClasses().length != data.size())
        {
            GCLog.info("Venus Simple Packet found data length different than packet type: " + packetType.name());
        }

        this.type = packetType;
        this.data = data;
    }

    public static void encode(final PacketSimpleVenus message, final PacketBuffer buf)
    {
        buf.writeInt(message.type.ordinal());
        NetworkUtil.writeUTF8String(buf, message.getDimensionID().getRegistryName().toString());

        try
        {
            NetworkUtil.encodeData(buf, message.data);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public static PacketSimpleVenus decode(PacketBuffer buf)
    {
        PacketSimpleVenus.EnumSimplePacketVenus type = PacketSimpleVenus.EnumSimplePacketVenus.values()[buf.readInt()];
        DimensionType dim = DimensionType.byName(new ResourceLocation(NetworkUtil.readUTF8String(buf)));
        ArrayList<Object> data = null;

        try
        {
            if (type.getDecodeClasses().length > 0)
            {
                data = NetworkUtil.decodeData(type.getDecodeClasses(), buf);
            }
            if (buf.readableBytes() > 0 && buf.writerIndex() < 0xfff00)
            {
                GCLog.severe("Galacticraft packet length problem for packet type " + type.toString());
            }
        }
        catch (Exception e)
        {
            System.err.println("[Galacticraft] Error handling simple packet type: " + type.toString() + " " + buf.toString());
            e.printStackTrace();
            throw e;
        }
        return new PacketSimpleVenus(type, dim, data);
    }

    public static void handle(final PacketSimpleVenus message, Supplier<NetworkEvent.Context> ctx)
    {
        ctx.get().enqueueWork(() ->
        {
            if (GCCoreUtil.getEffectiveSide() == LogicalSide.CLIENT)
            {
                message.handleClientSide(Minecraft.getInstance().player);
            }
            else
            {
                message.handleServerSide(ctx.get().getSender());
            }
        });
        ctx.get().setPacketHandled(true);
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
        this.type = EnumSimplePacketVenus.values()[buffer.readInt()];

        if (this.type.getDecodeClasses().length > 0)
        {
            this.data = NetworkUtil.decodeData(this.type.getDecodeClasses(), buffer);
        }
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void handleClientSide(PlayerEntity player)
    {
        ClientPlayerEntity playerBaseClient = null;

        if (player instanceof ClientPlayerEntity)
        {
            playerBaseClient = (ClientPlayerEntity) player;
        }

        switch (this.type)
        {
        default:
            break;
        }
    }

    @Override
    public void handleServerSide(PlayerEntity player)
    {
        ServerPlayerEntity playerBase = PlayerUtil.getPlayerBaseServerFromPlayer(player, false);

        switch (this.type)
        {
        case S_UPDATE_ADVANCED_GUI:
            TileEntity tile0 = player.world.getTileEntity((BlockPos) this.data.get(1));

            switch ((Integer) this.data.get(0))
            {
            case 0:
                if (tile0 instanceof TileEntityLaserTurret)
                {
                    TileEntityLaserTurret launchController = (TileEntityLaserTurret) tile0;
                    launchController.blacklistMode = ((Integer) this.data.get(2)) != 0;
                }
                break;
            case 1:
                if (tile0 instanceof TileEntityLaserTurret)
                {
                    TileEntityLaserTurret launchController = (TileEntityLaserTurret) tile0;
                    launchController.targetMeteors = ((Integer) this.data.get(2)) != 0;
                }
                break;
            case 2:
                if (tile0 instanceof TileEntityLaserTurret)
                {
                    TileEntityLaserTurret launchController = (TileEntityLaserTurret) tile0;
                    launchController.alwaysIgnoreSpaceRace = ((Integer) this.data.get(2)) != 0;
                }
                break;
            case 3:
                if (tile0 instanceof TileEntityLaserTurret)
                {
                    TileEntityLaserTurret launchController = (TileEntityLaserTurret) tile0;
                    launchController.priorityClosest = ((Integer) this.data.get(2));
                }
                break;
            case 4:
                if (tile0 instanceof TileEntityLaserTurret)
                {
                    TileEntityLaserTurret launchController = (TileEntityLaserTurret) tile0;
                    launchController.priorityLowestHealth = ((Integer) this.data.get(2));
                }
                break;
            case 5:
                if (tile0 instanceof TileEntityLaserTurret)
                {
                    TileEntityLaserTurret launchController = (TileEntityLaserTurret) tile0;
                    launchController.priorityHighestHealth = ((Integer) this.data.get(2));
                }
                break;
            default:
                break;
            }
            break;
        case S_OPEN_LASER_TURRET_GUI:
            BlockPos pos = (BlockPos) this.data.get(0);
//            player.openGui(GalacticraftPlanets.instance, GuiIdsPlanets.MACHINE_VENUS, player.world, pos.getX(), pos.getY(), pos.getZ()); TODO guis
            break;
        case S_MODIFY_LASER_TARGET:
            TileEntity tile1 = player.world.getTileEntity((BlockPos) this.data.get(1));

            switch ((Integer) this.data.get(0))
            {
            case 0:
                if (tile1 instanceof TileEntityLaserTurret)
                {
                    ((TileEntityLaserTurret) tile1).addPlayer((String) this.data.get(2));
                }
                break;
            case 1:
                if (tile1 instanceof TileEntityLaserTurret)
                {
                    ((TileEntityLaserTurret) tile1).addEntity(new ResourceLocation((String) this.data.get(2)));
                }
                break;
            case 2:
                if (tile1 instanceof TileEntityLaserTurret)
                {
                    ((TileEntityLaserTurret) tile1).removePlayer((String) this.data.get(2));
                }
                break;
            case 3:
                if (tile1 instanceof TileEntityLaserTurret)
                {
                    ((TileEntityLaserTurret) tile1).removeEntity(new ResourceLocation((String) this.data.get(2)));
                }
                break;
            }
            break;
        default:
            break;
        }
    }
}
