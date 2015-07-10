package micdoodle8.mods.galacticraft.planets.mars.network;

import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.EntityLivingBase;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import micdoodle8.mods.galacticraft.core.entities.player.GCPlayerStats;
import micdoodle8.mods.galacticraft.core.event.EventWakePlayer;
import micdoodle8.mods.galacticraft.core.network.IPacket;
import micdoodle8.mods.galacticraft.core.network.NetworkUtil;
import micdoodle8.mods.galacticraft.core.util.GCLog;
import micdoodle8.mods.galacticraft.core.util.PlayerUtil;
import micdoodle8.mods.galacticraft.planets.mars.client.gui.GuiCargoRocket;
import micdoodle8.mods.galacticraft.planets.mars.client.gui.GuiSlimelingInventory;
import micdoodle8.mods.galacticraft.planets.mars.entities.EntityCargoRocket;
import micdoodle8.mods.galacticraft.planets.mars.entities.EntitySlimeling;
import micdoodle8.mods.galacticraft.planets.mars.tile.TileEntityCryogenicChamber;
import micdoodle8.mods.galacticraft.planets.mars.tile.TileEntityLaunchController;
import micdoodle8.mods.galacticraft.planets.mars.util.MarsUtil;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraftforge.common.MinecraftForge;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class PacketSimpleMars implements IPacket
{
    public static enum EnumSimplePacketMars
    {
        // SERVER
        S_UPDATE_SLIMELING_DATA(Side.SERVER, Integer.class, Integer.class, String.class),
        S_WAKE_PLAYER(Side.SERVER),
        S_UPDATE_ADVANCED_GUI(Side.SERVER, Integer.class, BlockPos.class, Integer.class),
        S_UPDATE_CARGO_ROCKET_STATUS(Side.SERVER, Integer.class, Integer.class),
        // CLIENT
        C_OPEN_CUSTOM_GUI(Side.CLIENT, Integer.class, Integer.class, Integer.class),
        C_BEGIN_CRYOGENIC_SLEEP(Side.CLIENT, Integer.class, Integer.class, Integer.class);

        private Side targetSide;
        private Class<?>[] decodeAs;

        private EnumSimplePacketMars(Side targetSide, Class<?>... decodeAs)
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

    private EnumSimplePacketMars type;
    private List<Object> data;

    public PacketSimpleMars()
    {

    }

    public PacketSimpleMars(EnumSimplePacketMars packetType, Object[] data)
    {
        this(packetType, Arrays.asList(data));
    }

    public PacketSimpleMars(EnumSimplePacketMars packetType, List<Object> data)
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
        this.type = EnumSimplePacketMars.values()[buffer.readInt()];

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
        case C_OPEN_CUSTOM_GUI:
            int entityID = 0;
            Entity entity = null;

            switch ((Integer) this.data.get(1))
            {
            case 0:
                entityID = (Integer) this.data.get(2);
                entity = player.worldObj.getEntityByID(entityID);

                if (entity != null && entity instanceof EntitySlimeling)
                {
                    FMLClientHandler.instance().getClient().displayGuiScreen(new GuiSlimelingInventory(player, (EntitySlimeling) entity));
                }

                player.openContainer.windowId = (Integer) this.data.get(0);
                break;
            case 1:
                entityID = (Integer) this.data.get(2);
                entity = player.worldObj.getEntityByID(entityID);

                if (entity != null && entity instanceof EntityCargoRocket)
                {
                    FMLClientHandler.instance().getClient().displayGuiScreen(new GuiCargoRocket(player.inventory, (EntityCargoRocket) entity));
                }

                player.openContainer.windowId = (Integer) this.data.get(0);
                break;
            }
        case C_BEGIN_CRYOGENIC_SLEEP:
            TileEntity tile = player.worldObj.getTileEntity(new BlockPos((Integer) this.data.get(0), (Integer) this.data.get(1), (Integer) this.data.get(2)));

            if (tile instanceof TileEntityCryogenicChamber)
            {
                ((TileEntityCryogenicChamber) tile).sleepInBedAt(player, (Integer) this.data.get(0), (Integer) this.data.get(1), (Integer) this.data.get(2));
            }
        default:
            break;
        }
    }

    @Override
    public void handleServerSide(EntityPlayer player)
    {
        EntityPlayerMP playerBase = PlayerUtil.getPlayerBaseServerFromPlayer(player, false);
        GCPlayerStats stats = GCPlayerStats.get(playerBase);

        switch (this.type)
        {
        case S_UPDATE_SLIMELING_DATA:
            Entity entity = player.worldObj.getEntityByID((Integer) this.data.get(0));

            if (entity instanceof EntitySlimeling)
            {
                EntitySlimeling slimeling = (EntitySlimeling) entity;

                int subType = (Integer) this.data.get(1);

                switch (subType)
                {
                case 0:
                    if (player == slimeling.getOwner() && !slimeling.worldObj.isRemote)
                    {
                        slimeling.setSittingAI(!slimeling.isSitting());
                        slimeling.setJumping(false);
                        slimeling.getNavigator().clearPathEntity();
                        slimeling.setAttackTarget(null);
                    }
                    break;
                case 1:
                    if (player == slimeling.getOwner() && !slimeling.worldObj.isRemote)
                    {
                        slimeling.slimelingName = (String) this.data.get(2);
                        slimeling.setName(slimeling.slimelingName);
                    }
                    break;
                case 2:
                    if (player == slimeling.getOwner() && !slimeling.worldObj.isRemote)
                    {
                        slimeling.age += 5000;
                    }
                    break;
                case 3:
                    if (!slimeling.isInLove() && player == slimeling.getOwner() && !slimeling.worldObj.isRemote)
                    {
                        slimeling.setInLove(playerBase);
                    }
                    break;
                case 4:
                    if (player == slimeling.getOwner() && !slimeling.worldObj.isRemote)
                    {
                        slimeling.attackDamage = Math.min(slimeling.attackDamage + 0.1F, 1.0F);
                    }
                    break;
                case 5:
                    if (player == slimeling.getOwner() && !slimeling.worldObj.isRemote)
                    {
                        slimeling.setHealth(slimeling.getHealth() + 5.0F);
                    }
                    break;
                case 6:
                    if (player == slimeling.getOwner() && !slimeling.worldObj.isRemote)
                    {
                        MarsUtil.openSlimelingInventory(playerBase, slimeling);
                    }
                    break;
                }
            }
            break;
        case S_WAKE_PLAYER:
            BlockPos c = playerBase.playerLocation;

            if (c != null)
            {
                EventWakePlayer event = new EventWakePlayer(playerBase, c, false, true, true, true);
                MinecraftForge.EVENT_BUS.post(event);
                playerBase.wakeUpPlayer(false, true, true);
            }
            break;
        case S_UPDATE_ADVANCED_GUI:
            TileEntity tile = player.worldObj.getTileEntity((BlockPos) this.data.get(1));

            switch ((Integer) this.data.get(0))
            {
            case 0:
                if (tile instanceof TileEntityLaunchController)
                {
                    TileEntityLaunchController launchController = (TileEntityLaunchController) tile;
                    launchController.setFrequency((Integer) this.data.get(2));
                }
                break;
            case 1:
                if (tile instanceof TileEntityLaunchController)
                {
                    TileEntityLaunchController launchController = (TileEntityLaunchController) tile;
                    launchController.setLaunchDropdownSelection((Integer) this.data.get(2));
                }
                break;
            case 2:
                if (tile instanceof TileEntityLaunchController)
                {
                    TileEntityLaunchController launchController = (TileEntityLaunchController) tile;
                    launchController.setDestinationFrequency((Integer) this.data.get(2));
                }
                break;
            case 3:
                if (tile instanceof TileEntityLaunchController)
                {
                    TileEntityLaunchController launchController = (TileEntityLaunchController) tile;
                    launchController.launchPadRemovalDisabled = (Integer) this.data.get(2) == 1;
                }
                break;
            case 4:
                if (tile instanceof TileEntityLaunchController)
                {
                    TileEntityLaunchController launchController = (TileEntityLaunchController) tile;
                    launchController.setLaunchSchedulingEnabled((Integer) this.data.get(2) == 1);
                }
                break;
            case 5:
                if (tile instanceof TileEntityLaunchController)
                {
                    TileEntityLaunchController launchController = (TileEntityLaunchController) tile;
                    launchController.requiresClientUpdate = true;
                }
                break;
            default:
                break;
            }
            break;
        case S_UPDATE_CARGO_ROCKET_STATUS:
            Entity entity2 = player.worldObj.getEntityByID((Integer) this.data.get(0));

            if (entity2 instanceof EntityCargoRocket)
            {
                EntityCargoRocket rocket = (EntityCargoRocket) entity2;

                int subType = (Integer) this.data.get(1);

                switch (subType)
                {
                default:
                    rocket.statusValid = rocket.checkLaunchValidity();
                    break;
                }
            }
            break;
        default:
            break;
        }
    }
}
