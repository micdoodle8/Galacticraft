package micdoodle8.mods.galacticraft.core.tick;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.mojang.authlib.minecraft.MinecraftProfileTexture;
import com.mojang.authlib.minecraft.MinecraftProfileTexture.Type;
import micdoodle8.mods.galacticraft.api.block.IDetectableResource;
import micdoodle8.mods.galacticraft.api.entity.IEntityNoisy;
import micdoodle8.mods.galacticraft.api.entity.IIgnoreShift;
import micdoodle8.mods.galacticraft.api.item.ISensorGlassesArmor;
import micdoodle8.mods.galacticraft.api.prefab.entity.EntityAutoRocket;
import micdoodle8.mods.galacticraft.api.prefab.entity.EntitySpaceshipBase;
import micdoodle8.mods.galacticraft.api.recipe.CompressorRecipes;
import micdoodle8.mods.galacticraft.api.vector.BlockTuple;
import micdoodle8.mods.galacticraft.api.vector.BlockVec3;
import micdoodle8.mods.galacticraft.api.vector.Vector3;
import micdoodle8.mods.galacticraft.api.world.IGalacticraftWorldProvider;
import micdoodle8.mods.galacticraft.core.Constants;
import micdoodle8.mods.galacticraft.core.GCBlocks;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.client.*;
import micdoodle8.mods.galacticraft.core.client.gui.GuiIdsCore;
import micdoodle8.mods.galacticraft.core.client.gui.overlay.*;
import micdoodle8.mods.galacticraft.core.client.gui.screen.GuiCelestialSelection;
import micdoodle8.mods.galacticraft.core.client.gui.screen.GuiNewSpaceRace;
import micdoodle8.mods.galacticraft.core.client.gui.screen.GuiTeleporting;
import micdoodle8.mods.galacticraft.core.client.jei.GalacticraftJEI;
import micdoodle8.mods.galacticraft.core.dimension.WorldProviderMoon;
import micdoodle8.mods.galacticraft.core.dimension.WorldProviderSpaceStation;
import micdoodle8.mods.galacticraft.core.entities.EntityLander;
import micdoodle8.mods.galacticraft.core.entities.IBubbleProviderColored;
import micdoodle8.mods.galacticraft.core.entities.player.GCPlayerStatsClient;
import micdoodle8.mods.galacticraft.core.fluid.FluidNetwork;
import micdoodle8.mods.galacticraft.core.network.GalacticraftPacketHandler;
import micdoodle8.mods.galacticraft.core.network.PacketRotateRocket;
import micdoodle8.mods.galacticraft.core.network.PacketSimple;
import micdoodle8.mods.galacticraft.core.network.PacketSimple.EnumSimplePacket;
import micdoodle8.mods.galacticraft.core.proxy.ClientProxyCore;
import micdoodle8.mods.galacticraft.core.tile.TileEntityOxygenSealer;
import micdoodle8.mods.galacticraft.core.tile.TileEntityScreen;
import micdoodle8.mods.galacticraft.core.util.*;
import micdoodle8.mods.galacticraft.core.wrappers.BlockMetaList;
import micdoodle8.mods.galacticraft.core.wrappers.Footprint;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.ISound;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiIngameMenu;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.resources.DefaultPlayerSkin;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.WorldProviderSurface;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.Phase;
import net.minecraftforge.fml.common.gameevent.TickEvent.RenderTickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import java.util.*;

public class TickHandlerClient
{
    public static int airRemaining;
    public static int airRemaining2;
    public static boolean checkedVersion = true;
    private static boolean lastInvKeyPressed;
    private static long tickCount;
    public static boolean spaceRaceGuiScheduled = false;
    private static List<GalacticraftPacketHandler> packetHandlers = Lists.newCopyOnWriteArrayList();
    private static Set<FluidNetwork> fluidNetworks = Sets.newHashSet();
    public static GuiTeleporting teleportingGui;
    public static volatile boolean menuReset = true;
    public static volatile boolean updateJEIhiding = false;
    
    public static void resetClient()
    {
        ClientProxyCore.playerItemData.clear();
        ClientProxyCore.overworldTextureRequestSent = false;
        ClientProxyCore.flagRequestsSent.clear();
        TickHandlerClient.clearLiquidNetworks();
        ClientProxyCore.clientSpaceStationID.clear();
        ConfigManagerCore.challengeModeUpdate();

        if (TickHandlerClient.missingRequirementThread == null)
        {
            TickHandlerClient.missingRequirementThread = new ThreadRequirementMissing(Side.CLIENT);
            TickHandlerClient.missingRequirementThread.start();
        }
        
        MapUtil.resetClient();
        GCBlocks.spaceGlassVanilla.resetColor();
        GCBlocks.spaceGlassClear.resetColor();
        GCBlocks.spaceGlassStrong.resetColor();
        GCBlocks.spaceGlassTinVanilla.resetColor();
        GCBlocks.spaceGlassTinClear.resetColor();
        GCBlocks.spaceGlassTinStrong.resetColor();
    }

    public static void addFluidNetwork(FluidNetwork network)
    {
        fluidNetworks.add(network);
    }

    public static void removeFluidNetwork(FluidNetwork network)
    {
        fluidNetworks.remove(network);
    }

    public static void clearLiquidNetworks()
    {
        fluidNetworks.clear();
    }

    public static void addPacketHandler(GalacticraftPacketHandler handler)
    {
        TickHandlerClient.packetHandlers.add(handler);
    }

    @SubscribeEvent
    public void worldUnloadEvent(WorldEvent.Unload event)
    {
        for (GalacticraftPacketHandler packetHandler : packetHandlers)
        {
            packetHandler.unload(event.getWorld());
        }
    }

    private static ThreadRequirementMissing missingRequirementThread;

    public static HashSet<TileEntityScreen> screenConnectionsUpdateList = new HashSet<TileEntityScreen>();

    static
    {
        registerDetectableBlocks(true);
    }

    public static void registerDetectableBlocks(boolean logging)
    {
        ClientProxyCore.detectableBlocks.clear();

        for (final String s : ConfigManagerCore.detectableIDs)
        {
            BlockTuple bt = ConfigManagerCore.stringToBlock(s, "External Detectable IDs", logging);
            if (bt == null)
            {
                continue;
            }

            int meta = bt.meta;
            if (meta == -1)
            {
                meta = 0;
            }

            boolean flag = false;
            for (BlockMetaList blockMetaList : ClientProxyCore.detectableBlocks)
            {
                if (blockMetaList.getBlock() == bt.block)
                {
                    if (!blockMetaList.getMetaList().contains(meta))
                    {
                        blockMetaList.getMetaList().add(meta);
                    }
                    flag = true;
                    break;
                }
            }

            if (!flag)
            {
                List<Integer> metaList = Lists.newArrayList();
                metaList.add(meta);
                ClientProxyCore.detectableBlocks.add(new BlockMetaList(bt.block, metaList));
            }
        }
    }

    @SubscribeEvent
    public void onRenderTick(RenderTickEvent event)
    {
        final Minecraft minecraft = FMLClientHandler.instance().getClient();
        final EntityPlayerSP player = minecraft.player;
        final EntityPlayerSP playerBaseClient = PlayerUtil.getPlayerBaseClientFromPlayer(player, false);
        if (player == null || playerBaseClient == null)
        {
            return;
        }

        GCPlayerStatsClient stats = GCPlayerStatsClient.get(playerBaseClient);

        if (event.phase == Phase.END)
        {
            if (minecraft.currentScreen instanceof GuiIngameMenu)
            {
                int i = Mouse.getEventX() * minecraft.currentScreen.width / minecraft.displayWidth;
                int j = minecraft.currentScreen.height - Mouse.getEventY() * minecraft.currentScreen.height / minecraft.displayHeight - 1;

                int k = Mouse.getEventButton();

                int deltaColor = 0;

                if (i > minecraft.currentScreen.width - 100 && j > minecraft.currentScreen.height - 35)
                {
                    deltaColor = 20;

                    if (k == 0)
                    {
                        if (Mouse.getEventButtonState())
                        {
                            minecraft.displayGuiScreen(new GuiNewSpaceRace(playerBaseClient));
                        }
                    }
                }

                this.drawGradientRect(minecraft.currentScreen.width - 100, minecraft.currentScreen.height - 35, minecraft.currentScreen.width, minecraft.currentScreen.height, ColorUtil.to32BitColor(150, 10 + deltaColor, 10 + deltaColor, 10 + deltaColor), ColorUtil.to32BitColor(250, 10 + deltaColor, 10 + deltaColor, 10 + deltaColor));
                minecraft.fontRenderer.drawString(GCCoreUtil.translate("gui.space_race.create.title.name.0"), minecraft.currentScreen.width - 50 - minecraft.fontRenderer.getStringWidth(GCCoreUtil.translate("gui.space_race.create.title.name.0")) / 2, minecraft.currentScreen.height - 26, ColorUtil.to32BitColor(255, 240, 240, 240));
                minecraft.fontRenderer.drawString(GCCoreUtil.translate("gui.space_race.create.title.name.1"), minecraft.currentScreen.width - 50 - minecraft.fontRenderer.getStringWidth(GCCoreUtil.translate("gui.space_race.create.title.name.1")) / 2, minecraft.currentScreen.height - 16, ColorUtil.to32BitColor(255, 240, 240, 240));
                Gui.drawRect(minecraft.currentScreen.width - 100, minecraft.currentScreen.height - 35, minecraft.currentScreen.width - 99, minecraft.currentScreen.height, ColorUtil.to32BitColor(255, 0, 0, 0));
                Gui.drawRect(minecraft.currentScreen.width - 100, minecraft.currentScreen.height - 35, minecraft.currentScreen.width, minecraft.currentScreen.height - 34, ColorUtil.to32BitColor(255, 0, 0, 0));
            }

            ClientProxyCore.playerPosX = player.prevPosX + (player.posX - player.prevPosX) * event.renderTickTime;
            ClientProxyCore.playerPosY = player.prevPosY + (player.posY - player.prevPosY) * event.renderTickTime;
            ClientProxyCore.playerPosZ = player.prevPosZ + (player.posZ - player.prevPosZ) * event.renderTickTime;
            ClientProxyCore.playerRotationYaw = player.prevRotationYaw + (player.rotationYaw - player.prevRotationYaw) * event.renderTickTime;
            ClientProxyCore.playerRotationPitch = player.prevRotationPitch + (player.rotationPitch - player.prevRotationPitch) * event.renderTickTime;

            if (minecraft.currentScreen == null && minecraft.gameSettings.thirdPersonView != 0 && !minecraft.gameSettings.hideGUI)
            {
                if (player.getRidingEntity() instanceof EntitySpaceshipBase)
                {
                    OverlayRocket.renderSpaceshipOverlay(minecraft, ((EntitySpaceshipBase) player.getRidingEntity()).getSpaceshipGui());
                }

                if (player.getRidingEntity() instanceof EntityLander)
                {
                    OverlayLander.renderLanderOverlay(minecraft, TickHandlerClient.tickCount);
                }

                if (player.getRidingEntity() instanceof EntityAutoRocket)
                {
                    OverlayDockingRocket.renderDockingOverlay(minecraft, TickHandlerClient.tickCount);
                }

                if (player.getRidingEntity() instanceof EntitySpaceshipBase && !((EntitySpaceshipBase) minecraft.player.getRidingEntity()).getLaunched())
                {
                    OverlayLaunchCountdown.renderCountdownOverlay(minecraft);
                }
            }

            if (player.world.provider instanceof IGalacticraftWorldProvider && OxygenUtil.shouldDisplayTankGui(minecraft.currentScreen) && OxygenUtil.noAtmosphericCombustion(player.world.provider) && !(playerBaseClient.isCreative() || playerBaseClient.isSpectator()) && !minecraft.gameSettings.showDebugInfo)
            {
                int var6 = (TickHandlerClient.airRemaining - 90) * -1;

                if (TickHandlerClient.airRemaining <= 0)
                {
                    var6 = 90;
                }

                int var7 = (TickHandlerClient.airRemaining2 - 90) * -1;

                if (TickHandlerClient.airRemaining2 <= 0)
                {
                    var7 = 90;
                }

                int thermalLevel = stats.getThermalLevel() + 22;
                OverlayOxygenTanks.renderOxygenTankIndicator(minecraft, thermalLevel, var6, var7, !ConfigManagerCore.oxygenIndicatorLeft, !ConfigManagerCore.oxygenIndicatorBottom, Math.abs(thermalLevel - 22) >= 10 && !stats.isThermalLevelNormalising());
            }

            if (playerBaseClient != null && player.world.provider instanceof IGalacticraftWorldProvider && !stats.isOxygenSetupValid() && OxygenUtil.noAtmosphericCombustion(player.world.provider) && minecraft.currentScreen == null && !minecraft.gameSettings.hideGUI && !(playerBaseClient.isCreative() || playerBaseClient.isSpectator()))
            {
                OverlayOxygenWarning.renderOxygenWarningOverlay(minecraft, TickHandlerClient.tickCount);
            }
        }
    }

    @SubscribeEvent
    public void onPreGuiRender(RenderGameOverlayEvent.Pre event)
    {
        final Minecraft minecraft = FMLClientHandler.instance().getClient();
        final EntityPlayerSP player = minecraft.player;

        if (event.getType() == RenderGameOverlayEvent.ElementType.ALL)
        {
            if (player != null && player.getRidingEntity() != null && player.getRidingEntity() instanceof IIgnoreShift && ((IIgnoreShift) player.getRidingEntity()).shouldIgnoreShiftExit())
            {
                // Remove "Press shift to dismount" message when shift-exiting is disabled (not ideal, but the only option)
                String str = I18n.format("mount.onboard", new Object[] { GameSettings.getKeyDisplayString(minecraft.gameSettings.keyBindSneak.getKeyCode()) });
                if (minecraft.ingameGUI.overlayMessage.equals(str))
                {
                    minecraft.ingameGUI.overlayMessage = "";
                }
            }
        }
    }

    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public void onClientTick(ClientTickEvent event)
    {
        final Minecraft minecraft = FMLClientHandler.instance().getClient();
        final WorldClient world = minecraft.world;
        final EntityPlayerSP player = minecraft.player;

        if (teleportingGui != null)
        {
            if (minecraft.currentScreen != teleportingGui)
            {
                minecraft.currentScreen = teleportingGui;
            }
        }

        if (menuReset)
        {
            TickHandlerClient.resetClient();
            menuReset = false;
        }

        if (event.phase == Phase.START && player != null)
        {
            if (ClientProxyCore.playerHead == null && player.getGameProfile() != null)
            {
                Map<Type, MinecraftProfileTexture> map = minecraft.getSkinManager().loadSkinFromCache(player.getGameProfile());

                if (map.containsKey(Type.SKIN))
                {
                    ClientProxyCore.playerHead = minecraft.getSkinManager().loadSkin((MinecraftProfileTexture)map.get(Type.SKIN), Type.SKIN);
                }
                else
                {
                    ClientProxyCore.playerHead = DefaultPlayerSkin.getDefaultSkin(EntityPlayer.getUUID(player.getGameProfile()));
                }
            }

            TickHandlerClient.tickCount++;
            
            if (!GalacticraftCore.proxy.isPaused())
            {
                Iterator<FluidNetwork> it = TickHandlerClient.fluidNetworks.iterator();
                while (it.hasNext())
                {
                    FluidNetwork network = it.next();

                    if (network.getTransmitters().size() == 0)
                    {
                        it.remove();
                    }
                    else
                    {
                        network.clientTick();
                    }
                }
            }

            if (TickHandlerClient.tickCount % 20 == 0)
            {
                BubbleRenderer.clearBubbles();

                for (TileEntity tile : player.world.tickableTileEntities)
                {
                    if (tile instanceof IBubbleProviderColored)
                    {
                        BubbleRenderer.addBubble((IBubbleProviderColored)tile);
                    }
                }

                if (updateJEIhiding)
                {
                    updateJEIhiding = false;
                    // Update JEI to hide the ingot compressor recipe for GC steel in hard mode
                    // Update JEI to hide adventure mode recipes when not in adventure mode
                    GalacticraftJEI.updateHidden(CompressorRecipes.steelIngotsPresent && ConfigManagerCore.hardMode && !ConfigManagerCore.challengeRecipes, !ConfigManagerCore.challengeRecipes);
                }
                
                for (List<Footprint> fpList : FootprintRenderer.footprints.values())
                {
                    Iterator<Footprint> fpIt = fpList.iterator();
                    while (fpIt.hasNext())
                    {
                        Footprint fp = fpIt.next();
                        fp.age += 20;
                        fp.lightmapVal = player.world.getCombinedLight(new BlockPos(fp.position.x, fp.position.y, fp.position.z), 0);

                        if (fp.age >= Footprint.MAX_AGE)
                        {
                            fpIt.remove();
                        }
                    }
                }

                if (!player.inventory.armorItemInSlot(3).isEmpty() && player.inventory.armorItemInSlot(3).getItem() instanceof ISensorGlassesArmor)
                {
                    ClientProxyCore.valueableBlocks.clear();

                    for (int i = -4; i < 5; i++)
                    {
                        int x = MathHelper.floor(player.posX + i);
                        for (int j = -4; j < 5; j++)
                        {
                            int y = MathHelper.floor(player.posY + j);
                            for (int k = -4; k < 5; k++)
                            {
                                int z = MathHelper.floor(player.posZ + k);
                                BlockPos pos = new BlockPos(x, y, z);

                                IBlockState state = player.world.getBlockState(pos);
                                final Block block = state.getBlock();

                                if (block.getMaterial(state) != Material.AIR)
                                {
                                    int metadata = block.getMetaFromState(state);
                                    boolean isDetectable = false;

                                    for (BlockMetaList blockMetaList : ClientProxyCore.detectableBlocks)
                                    {
                                        if (blockMetaList.getBlock() == block && blockMetaList.getMetaList().contains(metadata))
                                        {
                                            isDetectable = true;
                                            break;
                                        }
                                    }

                                    if (isDetectable || (block instanceof IDetectableResource && ((IDetectableResource) block).isValueable(state)))
                                    {
                                        ClientProxyCore.valueableBlocks.add(new BlockVec3(x, y, z));
                                    }
                                }
                            }
                        }
                    }
                    
                    TileEntityOxygenSealer nearestSealer = TileEntityOxygenSealer.getNearestSealer(world, MathHelper.floor(player.posX), MathHelper.floor(player.posY), MathHelper.floor(player.posZ));
                    if (nearestSealer != null && !nearestSealer.sealed)
                    {
                        ClientProxyCore.leakTrace = nearestSealer.getLeakTraceClient();
                    }
                    else
                    {
                        ClientProxyCore.leakTrace = null;
                    }
                }
                else
                {
                    ClientProxyCore.leakTrace = null;
                }

                if (world != null)
                {
                    if (MapUtil.resetClientFlag.getAndSet(false))
                    {
                        MapUtil.resetClientBody();
                    }
                }
            }

            if (ClientProxyCore.leakTrace != null) this.spawnLeakParticles();
            
            if (world != null && TickHandlerClient.spaceRaceGuiScheduled && minecraft.currentScreen == null && ConfigManagerCore.enableSpaceRaceManagerPopup)
            {
                player.openGui(GalacticraftCore.instance, GuiIdsCore.SPACE_RACE_START, player.world, (int) player.posX, (int) player.posY, (int) player.posZ);
                TickHandlerClient.spaceRaceGuiScheduled = false;
            }

            if (world != null && TickHandlerClient.checkedVersion)
            {
                ThreadVersionCheck.startCheck();
                TickHandlerClient.checkedVersion = false;
            }

            boolean inSpaceShip = false;
            if (player.getRidingEntity() instanceof EntitySpaceshipBase)
            {
                inSpaceShip = true;
                EntitySpaceshipBase rocket = (EntitySpaceshipBase) player.getRidingEntity();
                if (rocket.prevRotationPitch != rocket.rotationPitch || rocket.prevRotationYaw != rocket.rotationYaw)
                {
                    GalacticraftCore.packetPipeline.sendToServer(new PacketRotateRocket(player.getRidingEntity()));
                }
            }

            if (world != null)
            {
                if (world.provider instanceof WorldProviderSurface)
                {
                    if (world.provider.getSkyRenderer() == null && inSpaceShip &&
                            player.getRidingEntity().posY > Constants.OVERWORLD_SKYPROVIDER_STARTHEIGHT)
                    {
                        world.provider.setSkyRenderer(new SkyProviderOverworld());
                    }
                    else if (world.provider.getSkyRenderer() instanceof SkyProviderOverworld && player.posY <= Constants.OVERWORLD_SKYPROVIDER_STARTHEIGHT)
                    {
                        world.provider.setSkyRenderer(null);
                    }
                }
                else if (world.provider instanceof WorldProviderSpaceStation)
                {
                    if (world.provider.getSkyRenderer() == null)
                    {
                        ((WorldProviderSpaceStation) world.provider).createSkyProvider();
                    }
                }
                else if (world.provider instanceof WorldProviderMoon)
                {
                    if (world.provider.getSkyRenderer() == null)
                    {
                        world.provider.setSkyRenderer(new SkyProviderMoon());
                    }

                    if (world.provider.getCloudRenderer() == null)
                    {
                        world.provider.setCloudRenderer(new CloudRenderer());
                    }
                }
            }

            if (inSpaceShip)
            {
                final EntitySpaceshipBase ship = (EntitySpaceshipBase) player.getRidingEntity();
                boolean hasChanged = false;

                if (minecraft.gameSettings.keyBindLeft.isKeyDown())
                {
                    ship.turnYaw(-1.0F);
                    hasChanged = true;
                }

                if (minecraft.gameSettings.keyBindRight.isKeyDown())
                {
                    ship.turnYaw(1.0F);
                    hasChanged = true;
                }

                if (minecraft.gameSettings.keyBindForward.isKeyDown())
                {
                    if (ship.getLaunched())
                    {
                        ship.turnPitch(-0.7F);
                        hasChanged = true;
                    }
                }

                if (minecraft.gameSettings.keyBindBack.isKeyDown())
                {
                    if (ship.getLaunched())
                    {
                        ship.turnPitch(0.7F);
                        hasChanged = true;
                    }
                }

                if (hasChanged)
                {
                    GalacticraftCore.packetPipeline.sendToServer(new PacketRotateRocket(ship));
                }
            }

            if (world != null)
            {
                List entityList = world.loadedEntityList;
                for (Object e : entityList)
                {
                    if (e instanceof IEntityNoisy)
                    {
                        IEntityNoisy vehicle = (IEntityNoisy) e;
                        if (vehicle.getSoundUpdater() == null)
                        {
                            ISound noise = vehicle.setSoundUpdater(FMLClientHandler.instance().getClient().player);
                            if (noise != null)
                            {
                                FMLClientHandler.instance().getClient().getSoundHandler().playSound(noise);
                            }
                        }
                    }
                }
            }

            if (FMLClientHandler.instance().getClient().currentScreen instanceof GuiCelestialSelection)
            {
                player.motionY = 0;
            }

            if (world != null && world.provider instanceof IGalacticraftWorldProvider && OxygenUtil.noAtmosphericCombustion(world.provider) && ((IGalacticraftWorldProvider) world.provider).shouldDisablePrecipitation())
            {
                world.setRainStrength(0.0F);
            }

            boolean isPressed = KeyHandlerClient.spaceKey.isKeyDown();

            if (!isPressed)
            {
                ClientProxyCore.lastSpacebarDown = false;
            }

            if (player.getRidingEntity() != null && isPressed && !ClientProxyCore.lastSpacebarDown)
            {
                GalacticraftCore.packetPipeline.sendToServer(new PacketSimple(EnumSimplePacket.S_IGNITE_ROCKET, GCCoreUtil.getDimensionID(player.world), new Object[] {}));
                ClientProxyCore.lastSpacebarDown = true;
            }

            if (!(this.screenConnectionsUpdateList.isEmpty()))
            {
                HashSet<TileEntityScreen> updateListCopy = (HashSet<TileEntityScreen>) screenConnectionsUpdateList.clone();
                screenConnectionsUpdateList.clear();
                for (TileEntityScreen te : updateListCopy)
                {
                    if (te.getWorld().getBlockState(te.getPos()).getBlock() == GCBlocks.screen)
                    {
                        if (te.refreshOnUpdate)
                        {
                            te.refreshConnections(true);
                        }
                        te.getWorld().markBlockRangeForRenderUpdate(te.getPos(), te.getPos());
                    }
                }
            }
        }
        else if (event.phase == Phase.END)
        {
            if (world != null)
            {
                for (GalacticraftPacketHandler handler : packetHandlers)
                {
                    handler.tick(world);
                }
            }
        }
    }

    private void spawnLeakParticles()
    {
        Random rand = new Random();
        for (int i = ClientProxyCore.leakTrace.size() - 1; i >= 0; i--)
        {
            if (i == 1) continue;
            BlockVec3 curr = ClientProxyCore.leakTrace.get(i);
            int nx = i - 2;
            if (i > 2 && rand.nextInt(3) == 0) nx --;
            BlockVec3 vec;
            if (i > 1) vec = ClientProxyCore.leakTrace.get(nx).clone();
            else
            {
                vec = curr.clone().translate(0, -2, 0);
            }
            Vector3 mot = new Vector3(vec.subtract(curr));
            Vector3 rnd = new Vector3(rand.nextDouble() / 2 - 0.25, rand.nextDouble() / 2 - 0.25, rand.nextDouble() / 2 - 0.25);
            GalacticraftCore.proxy.spawnParticle("oxygen", curr.midPoint().add(rnd), mot, new Object[] { new Vector3(0.7D, 0.7D, 1.0D) });
        }
    }

    private boolean alreadyContainsBlock(int x1, int y1, int z1)
    {
        return ClientProxyCore.valueableBlocks.contains(new BlockVec3(x1, y1, z1));
    }

    public static void zoom(float value)
    {
//        FMLClientHandler.instance().getClient().entityRenderer.thirdPersonDistance = value;
//        FMLClientHandler.instance().getClient().entityRenderer.thirdPersonDistancePrev = value;
    }

    private void drawGradientRect(int par1, int par2, int par3, int par4, int par5, int par6)
    {
        float f = (par5 >> 24 & 255) / 255.0F;
        float f1 = (par5 >> 16 & 255) / 255.0F;
        float f2 = (par5 >> 8 & 255) / 255.0F;
        float f3 = (par5 & 255) / 255.0F;
        float f4 = (par6 >> 24 & 255) / 255.0F;
        float f5 = (par6 >> 16 & 255) / 255.0F;
        float f6 = (par6 >> 8 & 255) / 255.0F;
        float f7 = (par6 & 255) / 255.0F;
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glDisable(GL11.GL_ALPHA_TEST);
        OpenGlHelper.glBlendFunc(770, 771, 1, 0);
        GL11.glShadeModel(GL11.GL_SMOOTH);
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder worldRenderer = tessellator.getBuffer();
        worldRenderer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_COLOR);
        worldRenderer.pos(par3, par2, 0.0D).color(f1, f2, f3, f).endVertex();
        worldRenderer.pos(par1, par2, 0.0D).color(f1, f2, f3, f).endVertex();
        worldRenderer.pos(par1, par4, 0.0D).color(f5, f6, f7, f4).endVertex();
        worldRenderer.pos(par3, par4, 0.0D).color(f5, f6, f7, f4).endVertex();
        tessellator.draw();
        GL11.glShadeModel(GL11.GL_FLAT);
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glEnable(GL11.GL_ALPHA_TEST);
        GL11.glEnable(GL11.GL_TEXTURE_2D);
    }
}
