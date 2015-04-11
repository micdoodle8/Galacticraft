package micdoodle8.mods.galacticraft.core.network;

import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import cpw.mods.fml.server.FMLServerHandler;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import micdoodle8.mods.galacticraft.api.galaxies.CelestialBody;
import micdoodle8.mods.galacticraft.api.galaxies.GalaxyRegistry;
import micdoodle8.mods.galacticraft.api.galaxies.SolarSystem;
import micdoodle8.mods.galacticraft.api.prefab.entity.EntityAutoRocket;
import micdoodle8.mods.galacticraft.api.prefab.entity.EntitySpaceshipBase;
import micdoodle8.mods.galacticraft.api.prefab.entity.EntityTieredRocket;
import micdoodle8.mods.galacticraft.api.recipe.ISchematicPage;
import micdoodle8.mods.galacticraft.api.recipe.SchematicRegistry;
import micdoodle8.mods.galacticraft.api.tile.IDisableableMachine;
import micdoodle8.mods.galacticraft.api.vector.Vector3;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.client.fx.EntityFXSparks;
import micdoodle8.mods.galacticraft.core.client.gui.GuiIdsCore;
import micdoodle8.mods.galacticraft.core.client.gui.container.GuiBuggy;
import micdoodle8.mods.galacticraft.core.client.gui.container.GuiParaChest;
import micdoodle8.mods.galacticraft.core.client.gui.screen.GuiCelestialSelection;
import micdoodle8.mods.galacticraft.core.command.CommandGCEnergyUnits;
import micdoodle8.mods.galacticraft.core.dimension.SpaceRace;
import micdoodle8.mods.galacticraft.core.dimension.SpaceRaceManager;
import micdoodle8.mods.galacticraft.core.dimension.SpaceStationWorldData;
import micdoodle8.mods.galacticraft.core.dimension.WorldProviderOrbit;
import micdoodle8.mods.galacticraft.core.energy.tile.TileBaseConductor;
import micdoodle8.mods.galacticraft.core.entities.EntityBuggy;
import micdoodle8.mods.galacticraft.core.entities.IBubbleProvider;
import micdoodle8.mods.galacticraft.core.entities.player.GCPlayerHandler;
import micdoodle8.mods.galacticraft.core.entities.player.GCPlayerHandler.EnumModelPacket;
import micdoodle8.mods.galacticraft.core.entities.player.GCPlayerStats;
import micdoodle8.mods.galacticraft.core.entities.player.GCPlayerStatsClient;
import micdoodle8.mods.galacticraft.core.inventory.ContainerSchematic;
import micdoodle8.mods.galacticraft.core.inventory.IInventorySettable;
import micdoodle8.mods.galacticraft.core.items.ItemParaChute;
import micdoodle8.mods.galacticraft.core.proxy.ClientProxyCore;
import micdoodle8.mods.galacticraft.core.tick.KeyHandlerClient;
import micdoodle8.mods.galacticraft.core.tick.TickHandlerClient;
import micdoodle8.mods.galacticraft.core.tile.TileEntityAirLockController;
import micdoodle8.mods.galacticraft.core.tile.TileEntityArclamp;
import micdoodle8.mods.galacticraft.core.tile.TileEntityScreen;
import micdoodle8.mods.galacticraft.core.tile.TileEntityTelemetry;
import micdoodle8.mods.galacticraft.core.util.*;
import micdoodle8.mods.galacticraft.core.wrappers.FlagData;
import micdoodle8.mods.galacticraft.core.wrappers.Footprint;
import micdoodle8.mods.galacticraft.core.wrappers.PlayerGearData;
import net.minecraft.block.Block;
import net.minecraft.block.material.MapColor;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityClientPlayerMP;
import net.minecraft.client.particle.EntityFX;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.INetHandler;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.server.S07PacketRespawn;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatStyle;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.ChunkCoordIntPair;
import net.minecraft.world.WorldProvider;
import net.minecraft.world.WorldServer;
import net.minecraft.world.chunk.Chunk;
import org.apache.commons.io.FileUtils;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.stream.FileImageOutputStream;
import javax.imageio.stream.ImageOutputStream;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.*;

public class PacketSimple extends Packet implements IPacket
{
    public static enum EnumSimplePacket
    {
        // SERVER
        S_RESPAWN_PLAYER(Side.SERVER, String.class),
        S_TELEPORT_ENTITY(Side.SERVER, String.class),
        S_IGNITE_ROCKET(Side.SERVER),
        S_OPEN_SCHEMATIC_PAGE(Side.SERVER, Integer.class),
        S_OPEN_FUEL_GUI(Side.SERVER, String.class),
        S_UPDATE_SHIP_YAW(Side.SERVER, Float.class),
        S_UPDATE_SHIP_PITCH(Side.SERVER, Float.class),
        S_SET_ENTITY_FIRE(Side.SERVER, Integer.class),
        S_BIND_SPACE_STATION_ID(Side.SERVER, Integer.class),
        S_UNLOCK_NEW_SCHEMATIC(Side.SERVER),
        S_UPDATE_DISABLEABLE_BUTTON(Side.SERVER, Integer.class, Integer.class, Integer.class, Integer.class),
        S_ON_FAILED_CHEST_UNLOCK(Side.SERVER, Integer.class),
        S_RENAME_SPACE_STATION(Side.SERVER, String.class, Integer.class),
        S_OPEN_EXTENDED_INVENTORY(Side.SERVER),
        S_ON_ADVANCED_GUI_CLICKED_INT(Side.SERVER, Integer.class, Integer.class, Integer.class, Integer.class, Integer.class),
        S_ON_ADVANCED_GUI_CLICKED_STRING(Side.SERVER, Integer.class, Integer.class, Integer.class, Integer.class, String.class),
        S_UPDATE_SHIP_MOTION_Y(Side.SERVER, Integer.class, Boolean.class),
        S_START_NEW_SPACE_RACE(Side.SERVER, Integer.class, String.class, FlagData.class, Vector3.class, String[].class),
        S_REQUEST_FLAG_DATA(Side.SERVER, String.class),
        S_INVITE_RACE_PLAYER(Side.SERVER, String.class, Integer.class),
        S_REMOVE_RACE_PLAYER(Side.SERVER, String.class, Integer.class),
        S_ADD_RACE_PLAYER(Side.SERVER, String.class, Integer.class),
        S_COMPLETE_CBODY_HANDSHAKE(Side.SERVER, String.class),
        S_REQUEST_GEAR_DATA(Side.SERVER, String.class),
        S_REQUEST_ARCLAMP_FACING(Side.CLIENT, Integer.class, Integer.class, Integer.class), 
        S_REQUEST_OVERWORLD_IMAGE(Side.SERVER),
        S_REQUEST_PLAYERSKIN(Side.SERVER, String.class),
        S_UPDATE_VIEWSCREEN_REQUEST(Side.CLIENT, Integer.class, Integer.class, Integer.class, Integer.class),
        S_BUILDFLAGS_UPDATE(Side.SERVER, Integer.class),
        // CLIENT
        C_AIR_REMAINING(Side.CLIENT, Integer.class, Integer.class, String.class),
        C_UPDATE_DIMENSION_LIST(Side.CLIENT, String.class, String.class),
        C_SPAWN_SPARK_PARTICLES(Side.CLIENT, Integer.class, Integer.class, Integer.class),
        C_UPDATE_GEAR_SLOT(Side.CLIENT, String.class, Integer.class, Integer.class),
        C_CLOSE_GUI(Side.CLIENT),
        C_RESET_THIRD_PERSON(Side.CLIENT),
        C_UPDATE_SPACESTATION_LIST(Side.CLIENT, Integer[].class),
        C_UPDATE_SPACESTATION_DATA(Side.CLIENT, Integer.class, NBTTagCompound.class),
        C_UPDATE_SPACESTATION_CLIENT_ID(Side.CLIENT, Integer.class),
        C_UPDATE_PLANETS_LIST(Side.CLIENT, Integer[].class),
        C_UPDATE_CONFIGS(Side.CLIENT, Integer.class, Double.class, Integer.class, Integer.class, Integer.class, String.class, Float.class, Float.class, Float.class, Float.class, Integer.class, String[].class),
        C_UPDATE_STATS(Side.CLIENT, Integer.class),
        C_ADD_NEW_SCHEMATIC(Side.CLIENT, Integer.class),
        C_UPDATE_SCHEMATIC_LIST(Side.CLIENT, Integer[].class),
        C_PLAY_SOUND_BOSS_DEATH(Side.CLIENT),
        C_PLAY_SOUND_EXPLODE(Side.CLIENT),
        C_PLAY_SOUND_BOSS_LAUGH(Side.CLIENT),
        C_PLAY_SOUND_BOW(Side.CLIENT),
        C_UPDATE_OXYGEN_VALIDITY(Side.CLIENT, Boolean.class),
        C_OPEN_PARACHEST_GUI(Side.CLIENT, Integer.class, Integer.class, Integer.class),
        C_UPDATE_WIRE_BOUNDS(Side.CLIENT, Integer.class, Integer.class, Integer.class),
        C_OPEN_SPACE_RACE_GUI(Side.CLIENT),
        C_UPDATE_SPACE_RACE_DATA(Side.CLIENT, Integer.class, String.class, FlagData.class, Vector3.class, String[].class),
        C_OPEN_JOIN_RACE_GUI(Side.CLIENT, Integer.class),
        C_UPDATE_FOOTPRINT_LIST(Side.CLIENT, Long.class, Footprint[].class),
        C_UPDATE_STATION_SPIN(Side.CLIENT, Float.class, Boolean.class),
        C_UPDATE_STATION_DATA(Side.CLIENT, Double.class, Double.class),
        C_UPDATE_STATION_BOX(Side.CLIENT, Integer.class, Integer.class, Integer.class, Integer.class, Integer.class, Integer.class),
        C_UPDATE_THERMAL_LEVEL(Side.CLIENT, Integer.class),
        C_DISPLAY_ROCKET_CONTROLS(Side.CLIENT),
        C_GET_CELESTIAL_BODY_LIST(Side.CLIENT),
        C_UPDATE_ENERGYUNITS(Side.CLIENT, Integer.class),
        C_RESPAWN_PLAYER(Side.CLIENT, String.class, Integer.class, String.class, Integer.class),
        C_UPDATE_ARCLAMP_FACING(Side.CLIENT, Integer.class, Integer.class, Integer.class, Integer.class),
        C_UPDATE_VIEWSCREEN(Side.CLIENT, Integer.class, Integer.class, Integer.class, Integer.class, Integer.class),
        C_UPDATE_TELEMETRY(Side.CLIENT, Integer.class, Integer.class, Integer.class, String.class, Integer.class, Integer.class, Integer.class, Integer.class, Integer.class, String.class),
        C_SEND_PLAYERSKIN(Side.CLIENT, String.class, String.class, String.class, String.class),
        C_SEND_OVERWORLD_IMAGE(Side.CLIENT, byte[].class);
        
        private Side targetSide;
        private Class<?>[] decodeAs;

        private EnumSimplePacket(Side targetSide, Class<?>... decodeAs)
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

    private EnumSimplePacket type;
    private List<Object> data;
    static private String spamCheckString;

    public PacketSimple()
    {
    }

    public PacketSimple(EnumSimplePacket packetType, Object[] data)
    {
        this(packetType, Arrays.asList(data));
    }

    public PacketSimple(EnumSimplePacket packetType, List<Object> data)
    {
        if (packetType.getDecodeClasses().length != data.size())
        {
            GCLog.info("Simple Packet Core found data length different than packet type");
            new RuntimeException().printStackTrace();
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
        this.type = EnumSimplePacket.values()[buffer.readInt()];

        try
        {
            if (this.type.getDecodeClasses().length > 0)
            {
                this.data = NetworkUtil.decodeData(this.type.getDecodeClasses(), buffer);
            }
            if (buffer.readableBytes() > 0)
            {
                GCLog.severe("Galacticraft packet length problem for packet type " + this.type.toString());
            }
        }
        catch (Exception e)
        {
            System.err.println("[Galacticraft] Error handling simple packet type: " + this.type.toString() + " " + buffer.toString());
            e.printStackTrace();
        }
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void handleClientSide(EntityPlayer player)
    {
        EntityClientPlayerMP playerBaseClient = null;
        GCPlayerStatsClient stats = null;

        if (player instanceof EntityClientPlayerMP)
        {
            playerBaseClient = (EntityClientPlayerMP) player;
            stats = GCPlayerStatsClient.get(playerBaseClient);
        }
        else
        {
            if (type != EnumSimplePacket.C_UPDATE_SPACESTATION_LIST && type != EnumSimplePacket.C_UPDATE_PLANETS_LIST && type != EnumSimplePacket.C_UPDATE_CONFIGS)
            {
                return;
            }
        }

        switch (this.type)
        {
        case C_AIR_REMAINING:
            if (String.valueOf(this.data.get(2)).equals(String.valueOf(FMLClientHandler.instance().getClient().thePlayer.getGameProfile().getName())))
            {
                TickHandlerClient.airRemaining = (Integer) this.data.get(0);
                TickHandlerClient.airRemaining2 = (Integer) this.data.get(1);
            }
            break;
        case C_UPDATE_DIMENSION_LIST:
            if (String.valueOf(this.data.get(0)).equals(FMLClientHandler.instance().getClient().thePlayer.getGameProfile().getName()))
            {
                String dimensionList = (String) this.data.get(1);
                if (ConfigManagerCore.enableDebug)
                {
                    if (!dimensionList.equals(PacketSimple.spamCheckString))
                    {
                        GCLog.info("DEBUG info: " + dimensionList);
                        PacketSimple.spamCheckString = new String(dimensionList);
                    }
                }
                final String[] destinations = dimensionList.split("\\?");
                List<CelestialBody> possibleCelestialBodies = Lists.newArrayList();
                Map<String, String> spaceStationNames = Maps.newHashMap();
                Map<String, Integer> spaceStationIDs = Maps.newHashMap();

                for (String str : destinations)
                {
                    CelestialBody celestialBody = WorldUtil.getReachableCelestialBodiesForName(str);

                    if (celestialBody == null && str.contains("$"))
                    {
                        celestialBody = GalacticraftCore.satelliteSpaceStation;

                        String[] values = str.split("\\$");

                        spaceStationNames.put(values[1], values[2]);
                        spaceStationIDs.put(values[1], Integer.parseInt(values[3]));
                    }

                    if (celestialBody != null)
                    {
                        possibleCelestialBodies.add(celestialBody);
                    }
                }

                if (FMLClientHandler.instance().getClient().theWorld != null)
                {
                    if (!(FMLClientHandler.instance().getClient().currentScreen instanceof GuiCelestialSelection))
                    {
                        GuiCelestialSelection gui = new GuiCelestialSelection(false, possibleCelestialBodies);
                        gui.spaceStationNames = spaceStationNames;
                        gui.spaceStationIDs = spaceStationIDs;
                        FMLClientHandler.instance().getClient().displayGuiScreen(gui);
                    }
                    else
                    {
                        ((GuiCelestialSelection) FMLClientHandler.instance().getClient().currentScreen).possibleBodies = possibleCelestialBodies;
                        ((GuiCelestialSelection) FMLClientHandler.instance().getClient().currentScreen).spaceStationNames = spaceStationNames;
                        ((GuiCelestialSelection) FMLClientHandler.instance().getClient().currentScreen).spaceStationIDs = spaceStationIDs;
                    }
                }
            }
            break;
        case C_SPAWN_SPARK_PARTICLES:
            int x,
                    y,
                    z;
            x = (Integer) this.data.get(0);
            y = (Integer) this.data.get(1);
            z = (Integer) this.data.get(2);
            Minecraft mc = Minecraft.getMinecraft();

            for (int i = 0; i < 4; i++)
            {
                if (mc != null && mc.renderViewEntity != null && mc.effectRenderer != null && mc.theWorld != null)
                {
                    final EntityFX fx = new EntityFXSparks(mc.theWorld, x - 0.15 + 0.5, y + 1.2, z + 0.15 + 0.5, mc.theWorld.rand.nextDouble() / 20 - mc.theWorld.rand.nextDouble() / 20, mc.theWorld.rand.nextDouble() / 20 - mc.theWorld.rand.nextDouble() / 20);

                    if (fx != null)
                    {
                        mc.effectRenderer.addEffect(fx);
                    }
                }
            }
            break;
        case C_UPDATE_GEAR_SLOT:
            int subtype = (Integer) this.data.get(2);
            EntityPlayer gearDataPlayer = null;
            MinecraftServer server = MinecraftServer.getServer();

            if (server != null)
            {
                gearDataPlayer = PlayerUtil.getPlayerForUsernameVanilla(server, (String) this.data.get(0));
            }
            else
            {
                gearDataPlayer = player.worldObj.getPlayerEntityByName((String) this.data.get(0));
            }

            if (gearDataPlayer != null)
            {
                PlayerGearData gearData = ClientProxyCore.playerItemData.get(gearDataPlayer.getGameProfile().getName());

                if (gearData == null)
                {
                    gearData = new PlayerGearData(player);
                }

                EnumModelPacket type = EnumModelPacket.values()[(Integer) this.data.get(1)];

                switch (type)
                {
                case ADDMASK:
                    gearData.setMask(0);
                    break;
                case REMOVEMASK:
                    gearData.setMask(-1);
                    break;
                case ADDGEAR:
                    gearData.setGear(0);
                    break;
                case REMOVEGEAR:
                    gearData.setGear(-1);
                    break;
                case ADDLEFTGREENTANK:
                    gearData.setLeftTank(0);
                    break;
                case ADDLEFTORANGETANK:
                    gearData.setLeftTank(1);
                    break;
                case ADDLEFTREDTANK:
                    gearData.setLeftTank(2);
                    break;
                case ADDRIGHTGREENTANK:
                    gearData.setRightTank(0);
                    break;
                case ADDRIGHTORANGETANK:
                    gearData.setRightTank(1);
                    break;
                case ADDRIGHTREDTANK:
                    gearData.setRightTank(2);
                    break;
                case REMOVE_LEFT_TANK:
                    gearData.setLeftTank(-1);
                    break;
                case REMOVE_RIGHT_TANK:
                    gearData.setRightTank(-1);
                    break;
                case ADD_PARACHUTE:
                    String name = "";

                    if (subtype != -1)
                    {
                        name = ItemParaChute.names[subtype];
                        gearData.setParachute(new ResourceLocation(GalacticraftCore.ASSET_PREFIX, "textures/model/parachute/" + name + ".png"));
                    }
                    break;
                case REMOVE_PARACHUTE:
                    gearData.setParachute(null);
                    break;
                case ADD_FREQUENCY_MODULE:
                    gearData.setFrequencyModule(0);
                    break;
                case REMOVE_FREQUENCY_MODULE:
                    gearData.setFrequencyModule(-1);
                    break;
                case ADD_THERMAL_HELMET:
                    gearData.setThermalPadding(0, 0);
                    break;
                case ADD_THERMAL_CHESTPLATE:
                    gearData.setThermalPadding(1, 0);
                    break;
                case ADD_THERMAL_LEGGINGS:
                    gearData.setThermalPadding(2, 0);
                    break;
                case ADD_THERMAL_BOOTS:
                    gearData.setThermalPadding(3, 0);
                    break;
                case REMOVE_THERMAL_HELMET:
                    gearData.setThermalPadding(0, -1);
                    break;
                case REMOVE_THERMAL_CHESTPLATE:
                    gearData.setThermalPadding(1, -1);
                    break;
                case REMOVE_THERMAL_LEGGINGS:
                    gearData.setThermalPadding(2, -1);
                    break;
                case REMOVE_THERMAL_BOOTS:
                    gearData.setThermalPadding(3, -1);
                    break;
                default:
                    break;
                }

                ClientProxyCore.playerItemData.put((String) this.data.get(0), gearData);
                ClientProxyCore.gearDataRequests.remove(this.data.get(0));
            }

            break;
        case C_CLOSE_GUI:
            FMLClientHandler.instance().getClient().displayGuiScreen(null);
            break;
        case C_RESET_THIRD_PERSON:
            FMLClientHandler.instance().getClient().gameSettings.thirdPersonView = stats.thirdPersonView;
            break;
        case C_UPDATE_SPACESTATION_LIST:
        	WorldUtil.decodeSpaceStationListClient(data);
            break;
        case C_UPDATE_SPACESTATION_DATA:
            SpaceStationWorldData var4 = SpaceStationWorldData.getMPSpaceStationData(player.worldObj, (Integer) this.data.get(0), player);
            var4.readFromNBT((NBTTagCompound) this.data.get(1));
            break;
        case C_UPDATE_SPACESTATION_CLIENT_ID:
            ClientProxyCore.clientSpaceStationID = (Integer) this.data.get(0);
            break;
        case C_UPDATE_PLANETS_LIST:
        	WorldUtil.decodePlanetsListClient(data);
            break;
        case C_UPDATE_CONFIGS:
        	ConfigManagerCore.saveClientConfigOverrideable();
        	ConfigManagerCore.setConfigOverride(data);
        	break;
        case C_ADD_NEW_SCHEMATIC:
            final ISchematicPage page = SchematicRegistry.getMatchingRecipeForID((Integer) this.data.get(0));
            if (!stats.unlockedSchematics.contains(page))
            {
                stats.unlockedSchematics.add(page);
            }
            break;
        case C_UPDATE_SCHEMATIC_LIST:
            for (Object o : this.data)
            {
                Integer schematicID = (Integer) o;

                if (schematicID != -2)
                {
                    Collections.sort(stats.unlockedSchematics);

                    if (!stats.unlockedSchematics.contains(SchematicRegistry.getMatchingRecipeForID(Integer.valueOf(schematicID))))
                    {
                        stats.unlockedSchematics.add(SchematicRegistry.getMatchingRecipeForID(Integer.valueOf(schematicID)));
                    }
                }
            }
            break;
        case C_PLAY_SOUND_BOSS_DEATH:
            player.playSound(GalacticraftCore.TEXTURE_PREFIX + "entity.bossdeath", 10.0F, 0.8F);
            break;
        case C_PLAY_SOUND_EXPLODE:
            player.playSound("random.explode", 10.0F, 0.7F);
            break;
        case C_PLAY_SOUND_BOSS_LAUGH:
            player.playSound(GalacticraftCore.TEXTURE_PREFIX + "entity.bosslaugh", 10.0F, 0.2F);
            break;
        case C_PLAY_SOUND_BOW:
            player.playSound("random.bow", 10.0F, 0.2F);
            break;
        case C_UPDATE_OXYGEN_VALIDITY:
            stats.oxygenSetupValid = (Boolean) this.data.get(0);
            break;
        case C_OPEN_PARACHEST_GUI:
            switch ((Integer) this.data.get(1))
            {
            case 0:
                if (player.ridingEntity instanceof EntityBuggy)
                {
                    FMLClientHandler.instance().getClient().displayGuiScreen(new GuiBuggy(player.inventory, (EntityBuggy) player.ridingEntity, ((EntityBuggy) player.ridingEntity).getType()));
                    player.openContainer.windowId = (Integer) this.data.get(0);
                }
                break;
            case 1:
                int entityID = (Integer) this.data.get(2);
                Entity entity = player.worldObj.getEntityByID(entityID);

                if (entity != null && entity instanceof IInventorySettable)
                {
                    FMLClientHandler.instance().getClient().displayGuiScreen(new GuiParaChest(player.inventory, (IInventorySettable) entity));
                }

                player.openContainer.windowId = (Integer) this.data.get(0);
                break;
            }
            break;
        case C_UPDATE_WIRE_BOUNDS:
            TileEntity tile = player.worldObj.getTileEntity((Integer) this.data.get(0), (Integer) this.data.get(1), (Integer) this.data.get(2));

            if (tile instanceof TileBaseConductor)
            {
                ((TileBaseConductor) tile).adjacentConnections = null;
                player.worldObj.getBlock(tile.xCoord, tile.yCoord, tile.zCoord).setBlockBoundsBasedOnState(player.worldObj, tile.xCoord, tile.yCoord, tile.zCoord);
            }
            break;
        case C_OPEN_SPACE_RACE_GUI:
            if (Minecraft.getMinecraft().currentScreen == null)
            {
                TickHandlerClient.spaceRaceGuiScheduled = false;
                player.openGui(GalacticraftCore.instance, GuiIdsCore.SPACE_RACE_START, player.worldObj, (int) player.posX, (int) player.posY, (int) player.posZ);
            }
            else
            {
                TickHandlerClient.spaceRaceGuiScheduled = true;
            }
            break;
        case C_UPDATE_SPACE_RACE_DATA:
            Integer teamID = (Integer) this.data.get(0);
            String teamName = (String) this.data.get(1);
            FlagData flagData = (FlagData) this.data.get(2);
            Vector3 teamColor = (Vector3) this.data.get(3);
            List<String> playerList = new ArrayList<String>();

            for (int i = 4; i < this.data.size(); i++)
            {
                String playerName = (String) this.data.get(i);
                ClientProxyCore.flagRequestsSent.remove(playerName);
                playerList.add(playerName);
            }

            SpaceRace race = new SpaceRace(playerList, teamName, flagData, teamColor);
            race.setSpaceRaceID(teamID);
            SpaceRaceManager.addSpaceRace(race);
            break;
        case C_OPEN_JOIN_RACE_GUI:
            stats.spaceRaceInviteTeamID = (Integer) this.data.get(0);
            player.openGui(GalacticraftCore.instance, GuiIdsCore.SPACE_RACE_JOIN, player.worldObj, (int) player.posX, (int) player.posY, (int) player.posZ);
            break;
        case C_UPDATE_FOOTPRINT_LIST:
            List<Footprint> printList = new ArrayList<Footprint>();
            long chunkKey = (Long) this.data.get(0);
            for (int i = 1; i < this.data.size(); i++)
            {
                Footprint print = (Footprint) this.data.get(i);
                printList.add(print);
            }
            ClientProxyCore.footprintRenderer.setFootprints(chunkKey, printList);
            break;
        case C_UPDATE_STATION_SPIN:
            if (playerBaseClient.worldObj.provider instanceof WorldProviderOrbit)
            {
                ((WorldProviderOrbit) playerBaseClient.worldObj.provider).setSpinRate((Float) this.data.get(0), (Boolean) this.data.get(1));
            }
            break;
        case C_UPDATE_STATION_DATA:
            if (playerBaseClient.worldObj.provider instanceof WorldProviderOrbit)
            {
                ((WorldProviderOrbit) playerBaseClient.worldObj.provider).setSpinCentre((Double) this.data.get(0), (Double) this.data.get(1));
            }
            break;
        case C_UPDATE_STATION_BOX:
            if (playerBaseClient.worldObj.provider instanceof WorldProviderOrbit)
            {
                ((WorldProviderOrbit) playerBaseClient.worldObj.provider).setSpinBox((Integer) this.data.get(0), (Integer) this.data.get(1), (Integer) this.data.get(2), (Integer) this.data.get(3), (Integer) this.data.get(4), (Integer) this.data.get(5));
            }
            break;
        case C_UPDATE_THERMAL_LEVEL:
            stats.thermalLevel = (Integer) this.data.get(0);
            break;
        case C_DISPLAY_ROCKET_CONTROLS:
            player.addChatMessage(new ChatComponentText(GameSettings.getKeyDisplayString(KeyHandlerClient.spaceKey.getKeyCode()) + "  - " + GCCoreUtil.translate("gui.rocket.launch.name")));
            player.addChatMessage(new ChatComponentText(GameSettings.getKeyDisplayString(KeyHandlerClient.leftKey.getKeyCode()) + " / " + GameSettings.getKeyDisplayString(KeyHandlerClient.rightKey.getKeyCode()) + "  - " + GCCoreUtil.translate("gui.rocket.turn.name")));
            player.addChatMessage(new ChatComponentText(GameSettings.getKeyDisplayString(KeyHandlerClient.accelerateKey.getKeyCode()) + " / " + GameSettings.getKeyDisplayString(KeyHandlerClient.decelerateKey.getKeyCode()) + "  - " + GCCoreUtil.translate("gui.rocket.updown.name")));
            player.addChatMessage(new ChatComponentText(GameSettings.getKeyDisplayString(KeyHandlerClient.openFuelGui.getKeyCode()) + "       - " + GCCoreUtil.translate("gui.rocket.inv.name")));
            break;
        case C_GET_CELESTIAL_BODY_LIST:
            String str = "";

            for (CelestialBody cBody : GalaxyRegistry.getRegisteredPlanets().values())
            {
                str = str.concat(cBody.getUnlocalizedName() + ";");
            }

            for (CelestialBody cBody : GalaxyRegistry.getRegisteredMoons().values())
            {
                str = str.concat(cBody.getUnlocalizedName() + ";");
            }

            for (CelestialBody cBody : GalaxyRegistry.getRegisteredSatellites().values())
            {
                str = str.concat(cBody.getUnlocalizedName() + ";");
            }

            for (SolarSystem solarSystem : GalaxyRegistry.getRegisteredSolarSystems().values())
            {
                str = str.concat(solarSystem.getUnlocalizedName() + ";");
            }

            GalacticraftCore.packetPipeline.sendToServer(new PacketSimple(EnumSimplePacket.S_COMPLETE_CBODY_HANDSHAKE, new Object[] { str }));
            break;
        case C_UPDATE_ENERGYUNITS:
            CommandGCEnergyUnits.handleParamClientside((Integer) this.data.get(0));
            break;
        case C_RESPAWN_PLAYER:
            final WorldProvider provider = WorldUtil.getProviderForName((String) this.data.get(0));
            final int dimID = provider.dimensionId;
            if (ConfigManagerCore.enableDebug)
            {
                GCLog.info("DEBUG: Client receiving respawn packet for dim " + dimID);
            }
            int par2 = (Integer) this.data.get(1);
            String par3 = (String) this.data.get(2);
            int par4 = (Integer) this.data.get(3);
            WorldUtil.forceRespawnClient(dimID, par2, par3, par4);
            break;
        case C_UPDATE_ARCLAMP_FACING:
        	tile = player.worldObj.getTileEntity((Integer) this.data.get(0), (Integer) this.data.get(1), (Integer) this.data.get(2));
        	int facingNew = (Integer) this.data.get(3);
        	if (tile instanceof TileEntityArclamp)
        	{
        		((TileEntityArclamp)tile).facing = facingNew;
        	}
        	break;
        case C_UPDATE_STATS:
        	stats.buildFlags = (Integer) this.data.get(0);
        	break;
        case C_UPDATE_VIEWSCREEN:
        	tile = player.worldObj.getTileEntity((Integer) this.data.get(0), (Integer) this.data.get(1), (Integer) this.data.get(2));
        	if (tile instanceof TileEntityScreen)
        	{
            	TileEntityScreen screenTile = (TileEntityScreen)tile; 
        		int screenType = (Integer) this.data.get(3);
        		int flags = (Integer) this.data.get(4);
            	screenTile.imageType = screenType;
            	screenTile.connectedUp = (flags & 8) != 0;
            	screenTile.connectedDown = (flags & 4) != 0;
            	screenTile.connectedLeft = (flags & 2) != 0;
            	screenTile.connectedRight = (flags & 1) != 0;
            	screenTile.refreshNextTick(true);
        	}      	
        	break;
        case C_UPDATE_TELEMETRY:
        	tile = player.worldObj.getTileEntity((Integer) this.data.get(0), (Integer) this.data.get(1), (Integer) this.data.get(2));
        	if (tile instanceof TileEntityTelemetry)
        	{
        		String name = (String) this.data.get(3);
        		if (name.startsWith("$"))
        		{
        			//It's a player name
        			((TileEntityTelemetry)tile).clientClass = EntityPlayerMP.class;
        			String strName = name.substring(1);
        			((TileEntityTelemetry)tile).clientName = strName;
        			GameProfile profile = FMLClientHandler.instance().getClientPlayerEntity().getGameProfile();
        			if (!strName.equals(profile.getName()))
        			{
        				profile = PlayerUtil.getOtherPlayerProfile(strName);
            			if (profile == null)
            			{
            				String strUUID = (String) this.data.get(9);
            				profile = PlayerUtil.makeOtherPlayerProfile(strName, strUUID);
            			}
            			if (VersionUtil.mcVersionMatches("1.7.10") && !profile.getProperties().containsKey("textures"))
            				GalacticraftCore.packetPipeline.sendToServer(new PacketSimple(EnumSimplePacket.S_REQUEST_PLAYERSKIN, new Object[] { strName }));
        			}
        			((TileEntityTelemetry)tile).clientGameProfile = profile;
        		}
        		else
        			((TileEntityTelemetry)tile).clientClass = (Class) EntityList.stringToClassMapping.get(name);
        		((TileEntityTelemetry)tile).clientData = new int[5];
        		for (int i = 4; i < 9; i++)
                {
            		((TileEntityTelemetry)tile).clientData[i - 4] = (Integer) this.data.get(i);
            	}
        	}
        	break;
        case C_SEND_PLAYERSKIN:
        	String strName = (String) this.data.get(0);
        	String s1 = (String) this.data.get(1);
        	String s2 = (String) this.data.get(2);
        	String strUUID = (String) this.data.get(3);
        	GameProfile gp = PlayerUtil.getOtherPlayerProfile(strName);
        	if (gp == null)
        	{
            	gp = PlayerUtil.makeOtherPlayerProfile(strName, strUUID);
        	}
        	gp.getProperties().put("textures", new Property("textures", s1, s2));
        	break;
        case C_SEND_OVERWORLD_IMAGE:
            try
            {
                byte[] bytes = (byte[]) this.data.get(0);
                
                //Class c = Launch.classLoader.loadClass("org.apache.commons.codec.binary.Base64");
                //if (c != null)
                {
                    //byte[] bytes = (byte[])c.getMethod("decodeBase64", byte[].class).invoke(null, base64);
                    File folder = new File(FMLClientHandler.instance().getClient().mcDataDir, "assets/temp");

                    try
                    {
                        if (folder.exists() || folder.mkdir())
                        {
                            File file0 = new File(folder, "overworldLocal.png");

                            if (!file0.exists() || (file0.canRead() && file0.canWrite()))
                            {
                                FileUtils.writeByteArrayToFile(file0, bytes);

                                BufferedImage img = ImageIO.read(file0);

                                if (img != null)
                                {
                                    ClientProxyCore.overworldTextureLocal = new DynamicTexture(img);
                                }
                            }
                            else
                            {
                                System.err.println("Cannot read/write to file %minecraftDir%/assets/temp/overworldLocal.png");
                            }
                        }
                        else
                        {
                            System.err.println("Cannot create directory %minecraftDir%/assets/temp!");
                        }
                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                }
            }
            catch (Exception e)
            {
                ;
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

        if (playerBase == null)
        {
            return;
        }
        
        GCPlayerStats stats = GCPlayerStats.get(playerBase);

        switch (this.type)
        {
        case S_RESPAWN_PLAYER:
            playerBase.playerNetServerHandler.sendPacket(new S07PacketRespawn(player.dimension, player.worldObj.difficultySetting, player.worldObj.getWorldInfo().getTerrainType(), playerBase.theItemInWorldManager.getGameType()));
            break;
        case S_TELEPORT_ENTITY:
            try
            {
                final WorldProvider provider = WorldUtil.getProviderForName((String) this.data.get(0));
                final Integer dim = provider.dimensionId;
                GCLog.info("Found matching world (" + dim.toString() + ") for name: " + (String) this.data.get(0));

                if (playerBase.worldObj instanceof WorldServer)
                {
                    final WorldServer world = (WorldServer) playerBase.worldObj;

                    WorldUtil.transferEntityToDimension(playerBase, dim, world);
                }

                stats.teleportCooldown = 10;
                GalacticraftCore.packetPipeline.sendTo(new PacketSimple(EnumSimplePacket.C_CLOSE_GUI, new Object[] { }), playerBase);
            }
            catch (final Exception e)
            {
                GCLog.severe("Error occurred when attempting to transfer entity to dimension: " + (String) this.data.get(0));
                e.printStackTrace();
            }
            break;
        case S_IGNITE_ROCKET:
            if (!player.worldObj.isRemote && !player.isDead && player.ridingEntity != null && !player.ridingEntity.isDead && player.ridingEntity instanceof EntityTieredRocket)
            {
                final EntityTieredRocket ship = (EntityTieredRocket) player.ridingEntity;

                if (!ship.landing)
                {
                    if (ship.hasValidFuel())
                    {
                        ItemStack stack2 = stats.extendedInventory.getStackInSlot(4);

                        if (stack2 != null && stack2.getItem() instanceof ItemParaChute || stats.launchAttempts > 0)
                        {
                            ship.igniteCheckingCooldown();
                            stats.launchAttempts = 0;
                        }
                        else if (stats.chatCooldown == 0 && stats.launchAttempts == 0)
                        {
                            player.addChatMessage(new ChatComponentText(GCCoreUtil.translate("gui.rocket.warning.noparachute")));
                            stats.chatCooldown = 250;
                            stats.launchAttempts = 1;
                        }
                    }
                    else if (stats.chatCooldown == 0)
                    {
                        player.addChatMessage(new ChatComponentText(GCCoreUtil.translate("gui.rocket.warning.nofuel")));
                        stats.chatCooldown = 250;
                    }
                }
            }
            break;
        case S_OPEN_SCHEMATIC_PAGE:
            if (player != null)
            {
                final ISchematicPage page = SchematicRegistry.getMatchingRecipeForID((Integer) this.data.get(0));

                player.openGui(GalacticraftCore.instance, page.getGuiID(), player.worldObj, (int) player.posX, (int) player.posY, (int) player.posZ);
            }
            break;
        case S_OPEN_FUEL_GUI:
            if (player.ridingEntity instanceof EntityBuggy)
            {
                GCCoreUtil.openBuggyInv(playerBase, (EntityBuggy) player.ridingEntity, ((EntityBuggy) player.ridingEntity).getType());
            }
            else if (player.ridingEntity instanceof EntitySpaceshipBase)
            {
                player.openGui(GalacticraftCore.instance, GuiIdsCore.ROCKET_INVENTORY, player.worldObj, (int) player.posX, (int) player.posY, (int) player.posZ);
            }
            break;
        case S_UPDATE_SHIP_YAW:
            if (player.ridingEntity instanceof EntitySpaceshipBase)
            {
                final EntitySpaceshipBase ship = (EntitySpaceshipBase) player.ridingEntity;

                if (ship != null)
                {
                    ship.rotationYaw = (Float) this.data.get(0);
                }
            }
            break;
        case S_UPDATE_SHIP_PITCH:
            if (player.ridingEntity instanceof EntitySpaceshipBase)
            {
                final EntitySpaceshipBase ship = (EntitySpaceshipBase) player.ridingEntity;

                if (ship != null)
                {
                    ship.rotationPitch = (Float) this.data.get(0);
                }
            }
            break;
        case S_SET_ENTITY_FIRE:
            Entity entity = player.worldObj.getEntityByID((Integer) this.data.get(0));

            if (entity instanceof EntityLivingBase)
            {
                ((EntityLivingBase) entity).setFire(3);
            }
            break;
        case S_BIND_SPACE_STATION_ID:
            if ((stats.spaceStationDimensionID == -1 || stats.spaceStationDimensionID == 0) && !ConfigManagerCore.disableSpaceStationCreation)
            {
                WorldUtil.bindSpaceStationToNewDimension(playerBase.worldObj, playerBase);

                WorldUtil.getSpaceStationRecipe((Integer) this.data.get(0)).matches(playerBase, true);
            }
            break;
        case S_UNLOCK_NEW_SCHEMATIC:
            final Container container = player.openContainer;

            if (container instanceof ContainerSchematic)
            {
                final ContainerSchematic schematicContainer = (ContainerSchematic) container;

                ItemStack stack = schematicContainer.craftMatrix.getStackInSlot(0);

                if (stack != null)
                {
                    final ISchematicPage page = SchematicRegistry.getMatchingRecipeForItemStack(stack);

                    if (page != null)
                    {
                        SchematicRegistry.unlockNewPage(playerBase, stack);

                        if (--stack.stackSize <= 0)
                        {
                            stack = null;
                        }

                        schematicContainer.craftMatrix.setInventorySlotContents(0, stack);
                        schematicContainer.craftMatrix.markDirty();

                        GalacticraftCore.packetPipeline.sendTo(new PacketSimple(EnumSimplePacket.C_ADD_NEW_SCHEMATIC, new Object[] { page.getPageID() }), playerBase);
                    }
                }
            }
            break;
        case S_UPDATE_DISABLEABLE_BUTTON:
            final TileEntity tileAt = player.worldObj.getTileEntity((Integer) this.data.get(0), (Integer) this.data.get(1), (Integer) this.data.get(2));

            if (tileAt instanceof IDisableableMachine)
            {
                final IDisableableMachine machine = (IDisableableMachine) tileAt;

                machine.setDisabled((Integer) this.data.get(3), !machine.getDisabled((Integer) this.data.get(3)));
            }
            break;
        case S_ON_FAILED_CHEST_UNLOCK:
            if (stats.chatCooldown == 0)
            {
                player.addChatMessage(new ChatComponentText(GCCoreUtil.translateWithFormat("gui.chest.warning.wrongkey", this.data.get(0))));
                stats.chatCooldown = 100;
            }
            break;
        case S_RENAME_SPACE_STATION:
            final SpaceStationWorldData ssdata = SpaceStationWorldData.getStationData(playerBase.worldObj, (Integer) this.data.get(1), playerBase);

            if (ssdata != null && ssdata.getOwner().equalsIgnoreCase(player.getGameProfile().getName()))
            {
                ssdata.setSpaceStationName((String) this.data.get(0));
                ssdata.setDirty(true);
            }
            break;
        case S_OPEN_EXTENDED_INVENTORY:
            player.openGui(GalacticraftCore.instance, GuiIdsCore.EXTENDED_INVENTORY, player.worldObj, 0, 0, 0);
            break;
        case S_ON_ADVANCED_GUI_CLICKED_INT:
            TileEntity tile1 = player.worldObj.getTileEntity((Integer) this.data.get(1), (Integer) this.data.get(2), (Integer) this.data.get(3));

            switch ((Integer) this.data.get(0))
            {
            case 0:
                if (tile1 instanceof TileEntityAirLockController)
                {
                    TileEntityAirLockController airlockController = (TileEntityAirLockController) tile1;
                    airlockController.redstoneActivation = (Integer) this.data.get(4) == 1;
                }
                break;
            case 1:
                if (tile1 instanceof TileEntityAirLockController)
                {
                    TileEntityAirLockController airlockController = (TileEntityAirLockController) tile1;
                    airlockController.playerDistanceActivation = (Integer) this.data.get(4) == 1;
                }
                break;
            case 2:
                if (tile1 instanceof TileEntityAirLockController)
                {
                    TileEntityAirLockController airlockController = (TileEntityAirLockController) tile1;
                    airlockController.playerDistanceSelection = (Integer) this.data.get(4);
                }
                break;
            case 3:
                if (tile1 instanceof TileEntityAirLockController)
                {
                    TileEntityAirLockController airlockController = (TileEntityAirLockController) tile1;
                    airlockController.playerNameMatches = (Integer) this.data.get(4) == 1;
                }
                break;
            case 4:
                if (tile1 instanceof TileEntityAirLockController)
                {
                    TileEntityAirLockController airlockController = (TileEntityAirLockController) tile1;
                    airlockController.invertSelection = (Integer) this.data.get(4) == 1;
                }
                break;
            case 5:
                if (tile1 instanceof TileEntityAirLockController)
                {
                    TileEntityAirLockController airlockController = (TileEntityAirLockController) tile1;
                    airlockController.lastHorizontalModeEnabled = airlockController.horizontalModeEnabled;
                    airlockController.horizontalModeEnabled = (Integer) this.data.get(4) == 1;
                }
                break;
            case 6:
                if (tile1 instanceof IBubbleProvider)
                {
                    IBubbleProvider distributor = (IBubbleProvider) tile1;
                    distributor.setBubbleVisible((Integer) this.data.get(4) == 1);
                }
                break;
            default:
                break;
            }
            break;
        case S_ON_ADVANCED_GUI_CLICKED_STRING:
            TileEntity tile2 = player.worldObj.getTileEntity((Integer) this.data.get(1), (Integer) this.data.get(2), (Integer) this.data.get(3));

            switch ((Integer) this.data.get(0))
            {
            case 0:
                if (tile2 instanceof TileEntityAirLockController)
                {
                    TileEntityAirLockController airlockController = (TileEntityAirLockController) tile2;
                    airlockController.playerToOpenFor = (String) this.data.get(4);
                }
                break;
            default:
                break;
            }
            break;
        case S_UPDATE_SHIP_MOTION_Y:
            int entityID = (Integer) this.data.get(0);
            boolean up = (Boolean) this.data.get(1);

            Entity entity2 = player.worldObj.getEntityByID(entityID);

            if (entity2 instanceof EntityAutoRocket)
            {
                EntityAutoRocket autoRocket = (EntityAutoRocket) entity2;
                autoRocket.motionY += up ? 0.02F : -0.02F;
            }

            break;
        case S_START_NEW_SPACE_RACE:
            Integer teamID = (Integer) this.data.get(0);
            String teamName = (String) this.data.get(1);
            FlagData flagData = (FlagData) this.data.get(2);
            Vector3 teamColor = (Vector3) this.data.get(3);
            List<String> playerList = new ArrayList<String>();

            for (int i = 4; i < this.data.size(); i++)
            {
                playerList.add((String) this.data.get(i));
            }

            boolean previousData = SpaceRaceManager.getSpaceRaceFromID(teamID) != null;

            SpaceRace newRace = new SpaceRace(playerList, teamName, flagData, teamColor);

            if (teamID > 0)
            {
                newRace.setSpaceRaceID(teamID);
            }

            SpaceRaceManager.addSpaceRace(newRace);

            if (previousData)
            {
                SpaceRaceManager.sendSpaceRaceData(null, SpaceRaceManager.getSpaceRaceFromPlayer(playerBase.getGameProfile().getName()));
            }
            break;
        case S_REQUEST_FLAG_DATA:
            SpaceRaceManager.sendSpaceRaceData(playerBase, SpaceRaceManager.getSpaceRaceFromPlayer((String) this.data.get(0)));
            break;
        case S_INVITE_RACE_PLAYER:
            EntityPlayerMP playerInvited = PlayerUtil.getPlayerBaseServerFromPlayerUsername((String) this.data.get(0), true);
            if (playerInvited != null)
            {
                Integer teamInvitedTo = (Integer) this.data.get(1);
                SpaceRace race = SpaceRaceManager.getSpaceRaceFromID(teamInvitedTo);

                if (race != null)
                {
                    GCPlayerStats.get(playerInvited).spaceRaceInviteTeamID = teamInvitedTo;
                    String dA = EnumColor.DARK_AQUA.getCode();
                    String bG = EnumColor.BRIGHT_GREEN.getCode();
                    String dB = EnumColor.PURPLE.getCode();
                    String teamNameTotal = "";
                    String[] teamNameSplit = race.getTeamName().split(" ");
                    for (String teamNamePart : teamNameSplit)
                    {
                        teamNameTotal = teamNameTotal.concat(dB + teamNamePart + " ");
                    }
                    playerInvited.addChatMessage(new ChatComponentText(dA + GCCoreUtil.translateWithFormat("gui.spaceRace.chat.inviteReceived", bG + player.getGameProfile().getName() + dA) + "  " + GCCoreUtil.translateWithFormat("gui.spaceRace.chat.toJoin", teamNameTotal, EnumColor.AQUA + "/joinrace" + dA)).setChatStyle(new ChatStyle().setColor(EnumChatFormatting.DARK_AQUA)));
                }
            }
            break;
        case S_REMOVE_RACE_PLAYER:
            Integer teamInvitedTo = (Integer) this.data.get(1);
            SpaceRace race = SpaceRaceManager.getSpaceRaceFromID(teamInvitedTo);

            if (race != null)
            {
                String playerToRemove = (String) this.data.get(0);

                if (!race.getPlayerNames().remove(playerToRemove))
                {
                    player.addChatMessage(new ChatComponentText(GCCoreUtil.translateWithFormat("gui.spaceRace.chat.notFound", playerToRemove)));
                }
                else
                {
                    SpaceRaceManager.onPlayerRemoval(playerToRemove, race);
                }
            }
            break;
        case S_ADD_RACE_PLAYER:
            Integer teamToAddPlayer = (Integer) this.data.get(1);
            SpaceRace spaceRaceToAddPlayer = SpaceRaceManager.getSpaceRaceFromID(teamToAddPlayer);

            if (spaceRaceToAddPlayer != null)
            {
                String playerToAdd = (String) this.data.get(0);

                if (!spaceRaceToAddPlayer.getPlayerNames().contains(playerToAdd))
                {
                    SpaceRace oldRace = null;
                    while ((oldRace = SpaceRaceManager.getSpaceRaceFromPlayer(playerToAdd)) != null)
                    {
                        SpaceRaceManager.removeSpaceRace(oldRace);
                    }

                    spaceRaceToAddPlayer.getPlayerNames().add(playerToAdd);
                    SpaceRaceManager.sendSpaceRaceData(null, spaceRaceToAddPlayer);

                    for (String member : spaceRaceToAddPlayer.getPlayerNames())
                    {
                        EntityPlayerMP memberObj = PlayerUtil.getPlayerForUsernameVanilla(MinecraftServer.getServer(), member);

                        if (memberObj != null)
                        {
                            memberObj.addChatMessage(new ChatComponentText(EnumColor.DARK_AQUA + GCCoreUtil.translateWithFormat("gui.spaceRace.chat.addSuccess", EnumColor.BRIGHT_GREEN + playerToAdd + EnumColor.DARK_AQUA)).setChatStyle(new ChatStyle().setColor(EnumChatFormatting.DARK_AQUA)));
                        }
                    }
                }
                else
                {
                    player.addChatMessage(new ChatComponentText(GCCoreUtil.translate("gui.spaceRace.chat.alreadyPart")).setChatStyle(new ChatStyle().setColor(EnumChatFormatting.DARK_RED)));
                }
            }
            break;
        case S_COMPLETE_CBODY_HANDSHAKE:
            String completeList = (String) this.data.get(0);
            List<String> clientObjects = Arrays.asList(completeList.split(";"));
            List<String> serverObjects = Lists.newArrayList();
            String missingObjects = "";

            for (CelestialBody cBody : GalaxyRegistry.getRegisteredPlanets().values())
            {
                serverObjects.add(cBody.getUnlocalizedName());
            }

            for (CelestialBody cBody : GalaxyRegistry.getRegisteredMoons().values())
            {
                serverObjects.add(cBody.getUnlocalizedName());
            }

            for (CelestialBody cBody : GalaxyRegistry.getRegisteredSatellites().values())
            {
                serverObjects.add(cBody.getUnlocalizedName());
            }

            for (SolarSystem solarSystem : GalaxyRegistry.getRegisteredSolarSystems().values())
            {
                serverObjects.add(solarSystem.getUnlocalizedName());
            }

            for (String str : serverObjects)
            {
                if (!clientObjects.contains(str))
                {
                    missingObjects = missingObjects.concat(str + "\n");
                }
            }

            if (missingObjects.length() > 0)
            {
                playerBase.playerNetServerHandler.kickPlayerFromServer("Missing Galacticraft Celestial Objects:\n\n " + missingObjects);
            }

            break;
        case S_REQUEST_GEAR_DATA:
            String name = (String) this.data.get(0);
            EntityPlayerMP e = PlayerUtil.getPlayerBaseServerFromPlayerUsername(name, true);
            if (e != null)
            {
                GCPlayerHandler.checkGear(e, GCPlayerStats.get(e), true);
            }
            break;
        case S_REQUEST_ARCLAMP_FACING:
        	TileEntity tileAL = player.worldObj.getTileEntity((Integer) this.data.get(0), (Integer) this.data.get(1), (Integer) this.data.get(2));
        	if (tileAL instanceof TileEntityArclamp)
        	{
            	((TileEntityArclamp)tileAL).updateClientFlag = true; 
        	}
        	break;
        case S_BUILDFLAGS_UPDATE:
        	stats.buildFlags = (Integer) this.data.get(0);
        	break;
        case S_UPDATE_VIEWSCREEN_REQUEST:
        	int screenDim = (Integer) this.data.get(0);
        	TileEntity tile = player.worldObj.getTileEntity((Integer) this.data.get(1), (Integer) this.data.get(2), (Integer) this.data.get(3));
        	if (tile instanceof TileEntityScreen)
        	{
            	((TileEntityScreen)tile).updateClients(); 
        	}
        	break;
        case S_REQUEST_OVERWORLD_IMAGE:
        	if (GalacticraftCore.enableJPEG)
        	{
            ChunkCoordIntPair chunkCoordIntPair = new ChunkCoordIntPair((int)Math.floor(stats.coordsTeleportedFromX) >> 4, (int)Math.floor(stats.coordsTeleportedFromZ) >> 4);
            File baseFolder = new File(MinecraftServer.getServer().worldServerForDimension(0).getChunkSaveLocation(), "galacticraft/overworldMap");
            if (!baseFolder.exists())
            {
                if (!baseFolder.mkdirs())
                {
                	GCLog.severe("Base folder(s) could not be created: " + baseFolder.getAbsolutePath());
                }
            }
            File outputFile = new File(baseFolder, "" + chunkCoordIntPair.chunkXPos + "_" + chunkCoordIntPair.chunkZPos + ".jpg");
            boolean success = true;

            if (!outputFile.exists() || !outputFile.isFile())
            {
                success = false;
                BufferedImage image = new BufferedImage(400, 400, BufferedImage.TYPE_INT_RGB);

                for (int x0 = -12; x0 <= 12; x0++)
                {
                    for (int z0 = -12; z0 <= 12; z0++)
                    {
                        Chunk chunk = MinecraftServer.getServer().worldServerForDimension(0).getChunkFromChunkCoords(chunkCoordIntPair.chunkXPos + x0, chunkCoordIntPair.chunkZPos + z0);

                        if (chunk != null)
                        {
                            for (int z = 0; z < 16; z++)
                            {
                                for (int x = 0; x < 16; x++)
                                {
                                    int l4 = chunk.getHeightValue(x, z) + 1;
                                    Block block = Blocks.air;
                                    int i5 = 0;

                                    if (l4 > 1)
                                    {
                                        do
                                        {
                                            --l4;
                                            block = chunk.getBlock(x, l4, z);
                                            i5 = chunk.getBlockMetadata(x, l4, z);
                                        }
                                        while (block.getMapColor(i5) == MapColor.airColor && l4 > 0);
                                    }

                                    int col = block.getMapColor(i5).colorValue;
                                    image.setRGB(x + (x0 + 12) * 16, z + (z0 + 12) * 16, col);
                                }
                            }
                        }
                    }
                }

                try
                {
                    if (!outputFile.exists() || (outputFile.canWrite() && outputFile.canRead()))
                    {
                    	ImageOutputStream outputStream = new FileImageOutputStream(outputFile);  
                    	GalacticraftCore.jpgWriter.setOutput(outputStream);
                    	GalacticraftCore.jpgWriter.write(null, new IIOImage(image, null, null), GalacticraftCore.writeParam);
                    	outputStream.close();
                        success = true;
                    }
                }
                catch (IOException ex)
                {
                    ex.printStackTrace();
                }
            }

            if (success)
            {
                try
                {
                    byte[] bytes = FileUtils.readFileToByteArray(outputFile);
                    //Class c = Launch.classLoader.loadClass("org.apache.commons.codec.binary.Base64");
                    //byte[] bytes64 = (byte[])c.getMethod("encodeBase64", byte[].class).invoke(null, bytes);
                    GalacticraftCore.packetPipeline.sendTo(new PacketSimple(EnumSimplePacket.C_SEND_OVERWORLD_IMAGE, new Object[] { bytes } ), playerBase);
                }
                catch (Exception ex)
                {
                    System.err.println("Error sending overworld image to player.");
                    ex.printStackTrace();
                }
            }
            else
            {
                System.err.println("[Galacticraft] Error creating player's overworld texture, please report this as a bug!");
            }
        	}
            break;
        case S_REQUEST_PLAYERSKIN:
        	String strName = (String) this.data.get(0);
        	EntityPlayerMP playerRequested = FMLServerHandler.instance().getServer().getConfigurationManager().func_152612_a(strName);
        	
        	//Player not online
        	if (playerRequested == null) return;
        	
        	GameProfile gp = playerRequested.getGameProfile();
        	if (gp == null) return;
        	
            Property property = (Property)Iterables.getFirst(gp.getProperties().get("textures"), (Object)null);
            if (property == null)
            {
                return;
            }
            GalacticraftCore.packetPipeline.sendTo(new PacketSimple(EnumSimplePacket.C_SEND_PLAYERSKIN, new Object[] { strName, property.getValue(), property.getSignature(), playerRequested.getUniqueID().toString() }), playerBase);
        	break;
        default:
            break;
        }
    }

	/*
     *
	 * BEGIN "net.minecraft.network.Packet" IMPLEMENTATION
	 * 
	 * This is for handling server->client packets before the player has joined the world
	 * 
	 */

    @Override
    public void readPacketData(PacketBuffer var1)
    {
        this.decodeInto(null, var1);
    }

    @Override
    public void writePacketData(PacketBuffer var1)
    {
        this.encodeInto(null, var1);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void processPacket(INetHandler var1)
    {
        if (this.type != EnumSimplePacket.C_UPDATE_SPACESTATION_LIST && this.type != EnumSimplePacket.C_UPDATE_PLANETS_LIST && this.type != EnumSimplePacket.C_UPDATE_CONFIGS)
        {
            return;
        }

        if (FMLCommonHandler.instance().getEffectiveSide() == Side.CLIENT)
        {
            this.handleClientSide(FMLClientHandler.instance().getClientPlayerEntity());
        }
    }

	/*
	 * 
	 * END "net.minecraft.network.Packet" IMPLEMENTATION
	 * 
	 * This is for handling server->client packets before the player has joined the world
	 * 
	 */
}
