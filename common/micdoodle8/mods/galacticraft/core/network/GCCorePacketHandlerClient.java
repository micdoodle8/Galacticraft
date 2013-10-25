package micdoodle8.mods.galacticraft.core.network;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import micdoodle8.mods.galacticraft.api.recipe.ISchematicPage;
import micdoodle8.mods.galacticraft.api.recipe.SchematicRegistry;
import micdoodle8.mods.galacticraft.core.GCCoreConfigManager;
import micdoodle8.mods.galacticraft.core.GCLog;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.client.ClientProxyCore;
import micdoodle8.mods.galacticraft.core.client.ClientProxyCore.GCKeyHandler;
import micdoodle8.mods.galacticraft.core.client.GCCorePlayerSP;
import micdoodle8.mods.galacticraft.core.client.fx.GCCoreEntityWeldingSmoke;
import micdoodle8.mods.galacticraft.core.client.gui.GCCoreGuiBuggy;
import micdoodle8.mods.galacticraft.core.client.gui.GCCoreGuiChoosePlanet;
import micdoodle8.mods.galacticraft.core.client.gui.GCCoreGuiGalaxyMap;
import micdoodle8.mods.galacticraft.core.client.gui.GCCoreGuiParachest;
import micdoodle8.mods.galacticraft.core.dimension.GCCoreSpaceStationData;
import micdoodle8.mods.galacticraft.core.entities.GCCoreEntityBuggy;
import micdoodle8.mods.galacticraft.core.inventory.IInventorySettable;
import micdoodle8.mods.galacticraft.core.tick.GCCoreTickHandlerClient;
import micdoodle8.mods.galacticraft.core.util.PacketUtil;
import micdoodle8.mods.galacticraft.core.util.PlayerUtil;
import micdoodle8.mods.galacticraft.core.util.WorldUtil;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.EntityFX;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet250CustomPayload;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChatMessageComponent;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.DimensionManager;
import org.lwjgl.input.Keyboard;
import universalelectricity.prefab.tile.TileEntityConductor;
import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.common.network.IPacketHandler;
import cpw.mods.fml.common.network.Player;
import cpw.mods.fml.relauncher.Side;

public class GCCorePacketHandlerClient implements IPacketHandler
{
    Minecraft mc = FMLClientHandler.instance().getClient();

    public static enum EnumPacketClient
    {
        AIR_REMAINING(0, Integer.class, Integer.class, String.class),
        INVALID(1),
        UPDATE_DIMENSION_LIST(2, String.class, String.class),
        UNUSED_0(3),
        GEAR_PARACHUTE_ADD(4, String.class),
        GEAR_PARACHUTE_REMOVE(5, String.class),
        GEAR_PARACHUTETEX_ADD(6, String.class, String.class),
        GEAR_PARACHUTETEX_REMOVE(7, String.class, String.class),
        MOUNT_ROCKET(8, String.class),
        SPAWN_SPARK_PARTICLES(9, Integer.class, Integer.class, Integer.class),
        UPDATE_GEAR_SLOT(10, String.class, Integer.class),
        UNUSED_1(11),
        CLOSE_GUI(12),
        RESET_THIRD_PERSON(13, String.class),
        UPDATE_CONTROLLABLE_ENTITY(14),
        UNUSED_2(15),
        UPDATE_SPACESTATION_LIST(16),
        UPDATE_SPACESTATION_DATA(17),
        UPDATE_SPACESTATION_CLIENT_ID(18, Integer.class),
        UPDATE_PLANETS_LIST(19),
        ADD_NEW_SCHEMATIC(20, Integer.class),
        UPDATE_SCHEMATIC_LIST(21),
        ZOOM_CAMERA(22, Integer.class),
        PLAY_SOUND_BOSS_DEATH(23),
        PLAY_SOUND_EXPLODE(24),
        PLAY_SOUND_BOSS_LAUGH(25),
        PLAY_SOUND_BOW(26),
        UPDATE_OXYGEN_VALIDITY(27, Boolean.class),
        OPEN_PARACHEST_GUI(28, Integer.class, Integer.class, Integer.class),
        UPDATE_LANDER(29),
        UPDATE_PARACHEST(30),
        UPDATE_WIRE_BOUNDS(31, Integer.class, Integer.class, Integer.class);

        private int index;
        private Class<?>[] decodeAs;

        private EnumPacketClient(int index, Class<?>... decodeAs)
        {
            this.index = index;
            this.decodeAs = decodeAs;
        }

        public int getIndex()
        {
            return this.index;
        }

        public Class<?>[] getDecodeClasses()
        {
            return this.decodeAs;
        }
    }

    @Override
    public void onPacketData(INetworkManager manager, Packet250CustomPayload packet, Player p)
    {
        if (packet == null)
        {
            FMLLog.severe("Packet received as null!");
            return;
        }

        if (packet.data == null)
        {
            FMLLog.severe("Packet data received as null! ID " + packet.getPacketId());
            return;
        }

        final DataInputStream data = new DataInputStream(new ByteArrayInputStream(packet.data));

        final EntityPlayer player = (EntityPlayer) p;

        GCCorePlayerSP playerBaseClient = null;

        if (player != null)
        {
            playerBaseClient = PlayerUtil.getPlayerBaseClientFromPlayer(player);
        }

        EnumPacketClient packetType = EnumPacketClient.values()[PacketUtil.readPacketID(data)];

        Class<?>[] decodeAs = packetType.getDecodeClasses();
        Object[] packetReadout = PacketUtil.readPacketData(data, decodeAs);

        switch (packetType)
        {
        case AIR_REMAINING:
            if (String.valueOf(packetReadout[2]).equals(String.valueOf(FMLClientHandler.instance().getClient().thePlayer.username)))
            {
                GCCoreTickHandlerClient.airRemaining = (Integer) packetReadout[0];
                GCCoreTickHandlerClient.airRemaining2 = (Integer) packetReadout[1];
            }
            break;
        case INVALID:
            GCLog.severe("Found incorrect packet! Please report this as a bug.");
            break;
        case UPDATE_DIMENSION_LIST:
            if (String.valueOf(packetReadout[0]).equals(FMLClientHandler.instance().getClient().thePlayer.username))
            {
                final String[] destinations = ((String) packetReadout[1]).split("\\.");

                if (FMLClientHandler.instance().getClient().theWorld != null && !(FMLClientHandler.instance().getClient().currentScreen instanceof GCCoreGuiChoosePlanet || FMLClientHandler.instance().getClient().currentScreen instanceof GCCoreGuiGalaxyMap))
                {
                    FMLClientHandler.instance().getClient().displayGuiScreen(new GCCoreGuiChoosePlanet(FMLClientHandler.instance().getClient().thePlayer, destinations));
                }
                else if (FMLClientHandler.instance().getClient().currentScreen instanceof GCCoreGuiChoosePlanet)
                {
                    ((GCCoreGuiChoosePlanet) FMLClientHandler.instance().getClient().currentScreen).updateDimensionList(destinations);
                }
            }
            break;
        case UNUSED_0:
            break;
        case GEAR_PARACHUTE_ADD:
            ClientProxyCore.playersUsingParachutes.add((String) packetReadout[0]);
            break;
        case GEAR_PARACHUTE_REMOVE:
            ClientProxyCore.playersUsingParachutes.remove(packetReadout[0]);
            break;
        case GEAR_PARACHUTETEX_ADD:
            ClientProxyCore.parachuteTextures.put((String) packetReadout[0], new ResourceLocation(GalacticraftCore.ASSET_DOMAIN, "textures/model/parachute/" + (String) packetReadout[1] + ".png"));
            break;
        case GEAR_PARACHUTETEX_REMOVE:
            ClientProxyCore.parachuteTextures.remove(packetReadout[0]);
            break;
        case MOUNT_ROCKET:
            if (playerBaseClient != null)
            {
                playerBaseClient.setThirdPersonView(FMLClientHandler.instance().getClient().gameSettings.thirdPersonView);
            }

            FMLClientHandler.instance().getClient().gameSettings.thirdPersonView = 1;

            player.sendChatToPlayer(ChatMessageComponent.createFromText("SPACE - Launch"));
            player.sendChatToPlayer(ChatMessageComponent.createFromText("A / D  - Turn left-right"));
            player.sendChatToPlayer(ChatMessageComponent.createFromText("W / S  - Turn up-down"));
            player.sendChatToPlayer(ChatMessageComponent.createFromText(Keyboard.getKeyName(GCKeyHandler.openSpaceshipInv.keyCode) + "       - Inventory / Fuel"));
            break;
        case SPAWN_SPARK_PARTICLES:
            int x,
            y,
            z;
            x = (Integer) packetReadout[0];
            y = (Integer) packetReadout[1];
            z = (Integer) packetReadout[2];

            for (int i = 0; i < 4; i++)
            {
                if (this.mc != null && this.mc.renderViewEntity != null && this.mc.effectRenderer != null && this.mc.theWorld != null)
                {
                    final EntityFX fx = new GCCoreEntityWeldingSmoke(this.mc.theWorld, x - 0.15 + 0.5, y + 1.2, z + 0.15 + 0.5, this.mc.theWorld.rand.nextDouble() / 20 - this.mc.theWorld.rand.nextDouble() / 20, 0.06, this.mc.theWorld.rand.nextDouble() / 20 - this.mc.theWorld.rand.nextDouble() / 20, 1.0F);
                    if (fx != null)
                    {
                        this.mc.effectRenderer.addEffect(fx);
                    }
                }
            }
            break;
        case UPDATE_GEAR_SLOT:
            switch ((Integer) packetReadout[1])
            {
            case 0:
                ClientProxyCore.playersWithOxygenMask.add((String) packetReadout[0]);
                break;
            case 1:
                ClientProxyCore.playersWithOxygenMask.remove(packetReadout[0]);
                break;
            case 2:
                ClientProxyCore.playersWithOxygenGear.add((String) packetReadout[0]);
                break;
            case 3:
                ClientProxyCore.playersWithOxygenGear.remove(packetReadout[0]);
                break;
            case 4:
                ClientProxyCore.playersWithOxygenTankLeftRed.add((String) packetReadout[0]);
                break;
            case 5:
                ClientProxyCore.playersWithOxygenTankLeftRed.remove(packetReadout[0]);
                break;
            case 6:
                ClientProxyCore.playersWithOxygenTankLeftOrange.add((String) packetReadout[0]);
                break;
            case 7:
                ClientProxyCore.playersWithOxygenTankLeftOrange.remove(packetReadout[0]);
                break;
            case 8:
                ClientProxyCore.playersWithOxygenTankLeftGreen.add((String) packetReadout[0]);
                break;
            case 9:
                ClientProxyCore.playersWithOxygenTankLeftGreen.remove(packetReadout[0]);
                break;
            case 10:
                ClientProxyCore.playersWithOxygenTankRightRed.add((String) packetReadout[0]);
                break;
            case 11:
                ClientProxyCore.playersWithOxygenTankRightRed.remove(packetReadout[0]);
                break;
            case 12:
                ClientProxyCore.playersWithOxygenTankRightOrange.add((String) packetReadout[0]);
                break;
            case 13:
                ClientProxyCore.playersWithOxygenTankRightOrange.remove(packetReadout[0]);
                break;
            case 14:
                ClientProxyCore.playersWithOxygenTankRightGreen.add((String) packetReadout[0]);
                break;
            case 15:
                ClientProxyCore.playersWithOxygenTankRightGreen.remove(packetReadout[0]);
                break;
            case 16:
                ClientProxyCore.playersWithFrequencyModule.add((String) packetReadout[0]);
                break;
            case 17:
                ClientProxyCore.playersWithFrequencyModule.remove(packetReadout[0]);
                break;
            }
            break;
        case UNUSED_1:
            break;
        case CLOSE_GUI:
            FMLClientHandler.instance().getClient().displayGuiScreen(null);
            break;
        case RESET_THIRD_PERSON:
            if (playerBaseClient != null)
            {
                FMLClientHandler.instance().getClient().gameSettings.thirdPersonView = playerBaseClient.getThirdPersonView();
            }
            break;
        case UPDATE_CONTROLLABLE_ENTITY:
            try
            {
                new GCCorePacketEntityUpdate().handlePacket(data, new Object[] { player }, Side.SERVER);
            }
            catch (final Exception e)
            {
                e.printStackTrace();
            }
            break;
        case UNUSED_2:
            break;
        case UPDATE_SPACESTATION_LIST:
            if (WorldUtil.registeredSpaceStations == null)
            {
                WorldUtil.registeredSpaceStations = new ArrayList<Integer>();
            }

            try
            {
                final int var1 = data.readInt();

                for (int var2 = 0; var2 < var1; ++var2)
                {
                    final int var3 = data.readInt();

                    if (!WorldUtil.registeredSpaceStations.contains(Integer.valueOf(var3)))
                    {
                        WorldUtil.registeredSpaceStations.add(Integer.valueOf(var3));
                        DimensionManager.registerDimension(var3, GCCoreConfigManager.idDimensionOverworldOrbit);
                    }
                }
            }
            catch (final IOException e)
            {
                e.printStackTrace();
            }
            break;
        case UPDATE_SPACESTATION_DATA:
            try
            {
                final int var2 = data.readInt();
                NBTTagCompound var3;

                final short var1 = data.readShort();

                if (var1 < 0)
                {
                    var3 = null;
                }
                else
                {
                    final byte[] var21 = new byte[var1];
                    data.readFully(var21);
                    var3 = CompressedStreamTools.decompress(var21);
                }

                final GCCoreSpaceStationData var4 = GCCoreSpaceStationData.getMPSpaceStationData(player.worldObj, var2, player);
                var4.readFromNBT(var3);
            }
            catch (final IOException var5)
            {
                var5.printStackTrace();
            }
            break;
        case UPDATE_SPACESTATION_CLIENT_ID:
            ClientProxyCore.clientSpaceStationID = (Integer) packetReadout[0];
            break;
        case UPDATE_PLANETS_LIST:
            if (WorldUtil.registeredPlanets == null)
            {
                WorldUtil.registeredPlanets = new ArrayList<Integer>();
            }

            try
            {
                final int var1 = data.readInt();

                for (int var2 = 0; var2 < var1; ++var2)
                {
                    final int var3 = data.readInt();

                    if (!WorldUtil.registeredPlanets.contains(Integer.valueOf(var3)))
                    {
                        WorldUtil.registeredPlanets.add(Integer.valueOf(var3));
                        DimensionManager.registerDimension(var3, var3);
                    }
                }
            }
            catch (final IOException e)
            {
                e.printStackTrace();
            }
            break;
        case ADD_NEW_SCHEMATIC:
            if (playerBaseClient != null)
            {
                final ISchematicPage page = SchematicRegistry.getMatchingRecipeForID((Integer) packetReadout[0]);

                if (!playerBaseClient.unlockedSchematics.contains(page))
                {
                    playerBaseClient.unlockedSchematics.add(page);
                }
            }
            break;
        case UPDATE_SCHEMATIC_LIST:
            if (playerBaseClient != null)
            {
                try
                {
                    final int var1 = data.readInt();

                    for (int var2 = 0; var2 < var1; ++var2)
                    {
                        final int var3 = data.readInt();

                        if (var3 != -2)
                        {
                            Collections.sort(playerBaseClient.unlockedSchematics);

                            if (!playerBaseClient.unlockedSchematics.contains(SchematicRegistry.getMatchingRecipeForID(Integer.valueOf(var3))))
                            {
                                playerBaseClient.unlockedSchematics.add(SchematicRegistry.getMatchingRecipeForID(Integer.valueOf(var3)));
                            }
                        }
                    }
                }
                catch (final IOException e)
                {
                    e.printStackTrace();
                }
            }
            break;
        case ZOOM_CAMERA:
            GCCoreTickHandlerClient.zoom((Integer) packetReadout[0] == 0 ? 4.0F : 15.0F);
            break;
        case PLAY_SOUND_BOSS_DEATH:
            player.playSound(GalacticraftCore.ASSET_PREFIX + "entity.bossdeath", 10.0F, 0.8F);
            break;
        case PLAY_SOUND_EXPLODE:
            player.playSound("random.explode", 10.0F, 0.7F);
            break;
        case PLAY_SOUND_BOSS_LAUGH:
            player.playSound(GalacticraftCore.ASSET_PREFIX + "entity.bosslaugh", 10.0F, 0.2F);
            break;
        case PLAY_SOUND_BOW:
            player.playSound("random.bow", 10.0F, 0.2F);
            break;
        case UPDATE_OXYGEN_VALIDITY:
            if (playerBaseClient != null)
            {
                playerBaseClient.oxygenSetupValid = (Boolean) packetReadout[0];
            }
            break;
        case OPEN_PARACHEST_GUI:
            switch ((Integer) packetReadout[1])
            {
            case 0:
                if (player.ridingEntity instanceof GCCoreEntityBuggy)
                {
                    FMLClientHandler.instance().getClient().displayGuiScreen(new GCCoreGuiBuggy(player.inventory, (GCCoreEntityBuggy) player.ridingEntity, ((GCCoreEntityBuggy) player.ridingEntity).getType()));
                    player.openContainer.windowId = (Integer) packetReadout[0];
                }
                break;
            case 1:
                int entityID = (Integer) packetReadout[2];
                Entity entity = player.worldObj.getEntityByID(entityID);

                if (entity != null && entity instanceof IInventorySettable)
                {
                    FMLClientHandler.instance().getClient().displayGuiScreen(new GCCoreGuiParachest(player.inventory, (IInventorySettable) entity));
                }

                player.openContainer.windowId = (Integer) packetReadout[0];
                break;
            }
            break;
        case UPDATE_LANDER:
            try
            {
                new GCCorePacketLanderUpdate().handlePacket(data, new Object[] { player }, Side.CLIENT);
            }
            catch (Exception e1)
            {
                e1.printStackTrace();
            }
            break;
        case UPDATE_PARACHEST:
            try
            {
                new GCCorePacketParachestUpdate().handlePacket(data, new Object[] { player }, Side.CLIENT);
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
            break;
        case UPDATE_WIRE_BOUNDS:
            TileEntity tile = player.worldObj.getBlockTileEntity((Integer) packetReadout[0], (Integer) packetReadout[1], (Integer) packetReadout[2]);

            if (tile instanceof TileEntityConductor)
            {
                ((TileEntityConductor) tile).adjacentConnections = null;
                Block.blocksList[player.worldObj.getBlockId(tile.xCoord, tile.yCoord, tile.zCoord)].setBlockBoundsBasedOnState(player.worldObj, tile.xCoord, tile.yCoord, tile.zCoord);
            }
            break;
        }
    }
}
