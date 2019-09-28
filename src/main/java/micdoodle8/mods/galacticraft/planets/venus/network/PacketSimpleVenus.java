package micdoodle8.mods.galacticraft.planets.venus.network;

import io.netty.buffer.ByteBuf;
import micdoodle8.mods.galacticraft.core.network.NetworkUtil;
import micdoodle8.mods.galacticraft.core.network.PacketBase;
import micdoodle8.mods.galacticraft.core.util.GCLog;
import micdoodle8.mods.galacticraft.core.util.PlayerUtil;
import micdoodle8.mods.galacticraft.planets.GalacticraftPlanets;
import micdoodle8.mods.galacticraft.planets.GuiIdsPlanets;
import micdoodle8.mods.galacticraft.planets.venus.tile.TileEntityLaserTurret;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class PacketSimpleVenus extends PacketBase
{
    public static enum EnumSimplePacketVenus
    {
        // SERVER
        S_UPDATE_ADVANCED_GUI(Side.SERVER, Integer.class, BlockPos.class, Integer.class),
        S_OPEN_LASER_TURRET_GUI(Side.SERVER, BlockPos.class),
        S_MODIFY_LASER_TARGET(Side.SERVER, Integer.class, BlockPos.class, String.class);
        // CLIENT

        private Side targetSide;
        private Class<?>[] decodeAs;

        private EnumSimplePacketVenus(Side targetSide, Class<?>... decodeAs)
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

    private EnumSimplePacketVenus type;
    private List<Object> data;

    public PacketSimpleVenus()
    {
        super();
    }

    public PacketSimpleVenus(EnumSimplePacketVenus packetType, int dimID, Object[] data)
    {
        this(packetType, dimID, Arrays.asList(data));
    }

    public PacketSimpleVenus(EnumSimplePacketVenus packetType, int dimID, List<Object> data)
    {
        super(dimID);

        if (packetType.getDecodeClasses().length != data.size())
        {
            GCLog.info("Venus Simple Packet found data length different than packet type: " + packetType.name());
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
        this.type = EnumSimplePacketVenus.values()[buffer.readInt()];

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

        switch (this.type)
        {
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
            player.openGui(GalacticraftPlanets.instance, GuiIdsPlanets.MACHINE_VENUS, player.world, pos.getX(), pos.getY(), pos.getZ());
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
