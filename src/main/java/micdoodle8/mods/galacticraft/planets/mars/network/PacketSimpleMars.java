package micdoodle8.mods.galacticraft.planets.mars.network;

import io.netty.buffer.ByteBuf;
import micdoodle8.mods.galacticraft.core.event.EventWakePlayer;
import micdoodle8.mods.galacticraft.core.network.NetworkUtil;
import micdoodle8.mods.galacticraft.core.network.PacketBase;
import micdoodle8.mods.galacticraft.core.util.GCLog;
import micdoodle8.mods.galacticraft.core.util.PlayerUtil;
import micdoodle8.mods.galacticraft.planets.GalacticraftPlanets;
import micdoodle8.mods.galacticraft.planets.GuiIdsPlanets;
import micdoodle8.mods.galacticraft.planets.mars.client.gui.GuiCargoRocket;
import micdoodle8.mods.galacticraft.planets.mars.client.gui.GuiLaunchControllerAdvanced;
import micdoodle8.mods.galacticraft.planets.mars.client.gui.GuiSlimelingInventory;
import micdoodle8.mods.galacticraft.planets.mars.entities.EntityCargoRocket;
import micdoodle8.mods.galacticraft.planets.mars.entities.EntitySlimeling;
import micdoodle8.mods.galacticraft.planets.mars.tile.TileEntityCryogenicChamber;
import micdoodle8.mods.galacticraft.planets.mars.tile.TileEntityLaunchController;
import micdoodle8.mods.galacticraft.planets.mars.util.MarsUtil;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class PacketSimpleMars extends PacketBase
{
    public static enum EnumSimplePacketMars
    {
        // SERVER
        S_UPDATE_SLIMELING_DATA(Side.SERVER, Integer.class, Integer.class, String.class),
        S_WAKE_PLAYER(Side.SERVER),
        S_UPDATE_ADVANCED_GUI(Side.SERVER, Integer.class, BlockPos.class, Integer.class),
        S_UPDATE_CARGO_ROCKET_STATUS(Side.SERVER, Integer.class, Integer.class),
        S_SWITCH_LAUNCH_CONTROLLER_GUI(Side.SERVER, BlockPos.class, Integer.class),
        // CLIENT
        C_OPEN_CUSTOM_GUI(Side.CLIENT, Integer.class, Integer.class, Integer.class),
        C_OPEN_CUSTOM_GUI_TILE(Side.CLIENT, Integer.class, Integer.class, BlockPos.class),
        C_BEGIN_CRYOGENIC_SLEEP(Side.CLIENT, BlockPos.class);

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
        super();
    }

    public PacketSimpleMars(EnumSimplePacketMars packetType, int dimID, Object[] data)
    {
        this(packetType, dimID, Arrays.asList(data));
    }

    public PacketSimpleMars(EnumSimplePacketMars packetType, int dimID, List<Object> data)
    {
        super(dimID);

        if (packetType.getDecodeClasses().length != data.size())
        {
            GCLog.info("Mars Simple Packet found data length different than packet type: " + packetType.name());
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
                entity = player.world.getEntityByID(entityID);

                if (entity != null && entity instanceof EntitySlimeling)
                {
                    FMLClientHandler.instance().getClient().displayGuiScreen(new GuiSlimelingInventory(player, (EntitySlimeling) entity));
                }

                player.openContainer.windowId = (Integer) this.data.get(0);
                break;
            case 1:
                entityID = (Integer) this.data.get(2);
                entity = player.world.getEntityByID(entityID);

                if (entity != null && entity instanceof EntityCargoRocket)
                {
                    FMLClientHandler.instance().getClient().displayGuiScreen(new GuiCargoRocket(player.inventory, (EntityCargoRocket) entity));
                }

                player.openContainer.windowId = (Integer) this.data.get(0);
                break;
            }
            break;
        case C_OPEN_CUSTOM_GUI_TILE:
            BlockPos pos;
            TileEntity tile;

            switch ((Integer) this.data.get(1))
            {
            case 0:
                pos = (BlockPos) this.data.get(2);
                tile = player.world.getTileEntity(pos);

                if (tile != null && tile instanceof TileEntityLaunchController)
                {
                    FMLClientHandler.instance().getClient().displayGuiScreen(new GuiLaunchControllerAdvanced(player.inventory, (TileEntityLaunchController) tile));
                }

                player.openContainer.windowId = (Integer) this.data.get(0);
                break;
            }
            break;
        case C_BEGIN_CRYOGENIC_SLEEP:
            pos = (BlockPos) this.data.get(0);
            tile = player.world.getTileEntity(pos);

            if (tile instanceof TileEntityCryogenicChamber)
            {
                ((TileEntityCryogenicChamber) tile).sleepInBedAt(player, pos.getX(), pos.getY(), pos.getZ());
            }
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
        case S_UPDATE_SLIMELING_DATA:
            Entity entity = player.world.getEntityByID((Integer) this.data.get(0));

            if (entity instanceof EntitySlimeling)
            {
                EntitySlimeling slimeling = (EntitySlimeling) entity;

                int subType = (Integer) this.data.get(1);

                switch (subType)
                {
                case 0:
                    if (player == slimeling.getOwner() && !slimeling.world.isRemote)
                    {
                        slimeling.setSittingAI(!slimeling.isSitting());
                        slimeling.setJumping(false);
                        slimeling.getNavigator().clearPath();
                        slimeling.setAttackTarget(null);
                    }
                    break;
                case 1:
                    if (player == slimeling.getOwner() && !slimeling.world.isRemote)
                    {
                        slimeling.slimelingName = (String) this.data.get(2);
                        slimeling.setName(slimeling.slimelingName);
                    }
                    break;
                case 2:
                    if (player == slimeling.getOwner() && !slimeling.world.isRemote)
                    {
                        slimeling.age += 5000;
                    }
                    break;
                case 3:
                    if (!slimeling.isInLove() && player == slimeling.getOwner() && !slimeling.world.isRemote)
                    {
                        slimeling.setInLove(playerBase);
                    }
                    break;
                case 4:
                    if (player == slimeling.getOwner() && !slimeling.world.isRemote)
                    {
                        slimeling.attackDamage = Math.min(slimeling.attackDamage + 0.1F, 1.0F);
                    }
                    break;
                case 5:
                    if (player == slimeling.getOwner() && !slimeling.world.isRemote)
                    {
                        slimeling.setHealth(slimeling.getHealth() + 5.0F);
                    }
                    break;
                case 6:
                    if (player == slimeling.getOwner() && !slimeling.world.isRemote)
                    {
                        MarsUtil.openSlimelingInventory(playerBase, slimeling);
                    }
                    break;
                }
            }
            break;
        case S_WAKE_PLAYER:
            BlockPos c = playerBase.bedLocation;

            if (c != null)
            {
                EventWakePlayer event = new EventWakePlayer(playerBase, c, true, true, false, true);
                MinecraftForge.EVENT_BUS.post(event);
                playerBase.wakeUpPlayer(true, true, false);
            }
            break;
        case S_UPDATE_ADVANCED_GUI:
            TileEntity tile = player.world.getTileEntity((BlockPos) this.data.get(1));

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
            Entity entity2 = player.world.getEntityByID((Integer) this.data.get(0));

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
        case S_SWITCH_LAUNCH_CONTROLLER_GUI:
            BlockPos pos = (BlockPos) this.data.get(0);
            TileEntity tile1 = player.world.getTileEntity(pos);
            if (tile1 instanceof TileEntityLaunchController)
            {
                TileEntityLaunchController launchController = (TileEntityLaunchController) tile1;
                switch ((Integer) this.data.get(1))
                {
                case 0:
                    MarsUtil.openAdvancedLaunchController(playerBase, launchController);
                    break;
                case 1:
                    player.openGui(GalacticraftPlanets.instance, GuiIdsPlanets.MACHINE_MARS, player.world, pos.getX(), pos.getY(), pos.getZ());
                    break;
                }
            }
            break;
        default:
            break;
        }
    }
}
