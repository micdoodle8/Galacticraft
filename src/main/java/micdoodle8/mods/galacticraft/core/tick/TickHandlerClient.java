package micdoodle8.mods.galacticraft.core.tick;

import com.google.common.collect.Sets;
import com.mojang.authlib.minecraft.MinecraftProfileTexture;
import com.mojang.authlib.minecraft.MinecraftProfileTexture.Type;
import micdoodle8.mods.galacticraft.api.block.IDetectableResource;
import micdoodle8.mods.galacticraft.api.entity.IEntityNoisy;
import micdoodle8.mods.galacticraft.api.entity.IIgnoreShift;
import micdoodle8.mods.galacticraft.api.item.ISensorGlassesArmor;
import micdoodle8.mods.galacticraft.api.prefab.entity.EntitySpaceshipBase;
import micdoodle8.mods.galacticraft.api.vector.BlockVec3;
import micdoodle8.mods.galacticraft.api.vector.Vector3;
import micdoodle8.mods.galacticraft.api.world.IGalacticraftDimension;
import micdoodle8.mods.galacticraft.core.GCBlocks;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.client.CloudRenderer;
import micdoodle8.mods.galacticraft.core.client.FootprintRenderer;
import micdoodle8.mods.galacticraft.core.client.GCParticles;
import micdoodle8.mods.galacticraft.core.client.gui.overlay.*;
import micdoodle8.mods.galacticraft.core.client.gui.screen.GuiCelestialSelection;
import micdoodle8.mods.galacticraft.core.client.gui.screen.GuiTeleporting;
import micdoodle8.mods.galacticraft.core.dimension.DimensionMoon;
import micdoodle8.mods.galacticraft.core.entities.player.GCPlayerStatsClient;
import micdoodle8.mods.galacticraft.core.fluid.FluidNetwork;
import micdoodle8.mods.galacticraft.core.network.PacketSimple;
import micdoodle8.mods.galacticraft.core.network.PacketSimple.EnumSimplePacket;
import micdoodle8.mods.galacticraft.core.proxy.ClientProxyCore;
import micdoodle8.mods.galacticraft.core.tile.TileEntityOxygenSealer;
import micdoodle8.mods.galacticraft.core.tile.TileEntityScreen;
import micdoodle8.mods.galacticraft.core.util.*;
import micdoodle8.mods.galacticraft.core.wrappers.Footprint;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.ISound;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.client.gui.screen.IngameMenuScreen;
import net.minecraft.client.resources.DefaultPlayerSkin;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.dimension.OverworldDimension;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.LogicalSide;

import java.util.*;

public class TickHandlerClient
{
    public static int airRemaining;
    public static int airRemaining2;
    public static boolean checkedVersion = true;
    private static boolean lastInvKeyPressed;
    private static long tickCount;
    public static boolean spaceRaceGuiScheduled = false;
    //    private static List<GalacticraftPacketHandler> packetHandlers = Lists.newCopyOnWriteArrayList();
    private static final Set<FluidNetwork> fluidNetworks = Sets.newHashSet();
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
            TickHandlerClient.missingRequirementThread = new ThreadRequirementMissing(LogicalSide.CLIENT);
            TickHandlerClient.missingRequirementThread.start();
        }

        MapUtil.resetClient();
//        GCBlocks.spaceGlassVanilla.resetColor();
//        GCBlocks.spaceGlassClear.resetColor();
//        GCBlocks.spaceGlassStrong.resetColor();
//        GCBlocks.spaceGlassTinVanilla.resetColor();
//        GCBlocks.spaceGlassTinClear.resetColor();
//        GCBlocks.spaceGlassTinStrong.resetColor(); TODO Space glass
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

//    public static void addPacketHandler(GalacticraftPacketHandler handler)
//    {
//        TickHandlerClient.packetHandlers.add(handler);
//    }
//
//    @SubscribeEvent
//    public void worldUnloadEvent(WorldEvent.Unload event)
//    {
//        for (GalacticraftPacketHandler packetHandler : packetHandlers)
//        {
//            packetHandler.unload(event.getWorld());
//        }
//    }

    private static ThreadRequirementMissing missingRequirementThread;

    public static final HashSet<TileEntityScreen> screenConnectionsUpdateList = new HashSet<>();

    static
    {
        registerDetectableBlocks(true);
    }

    public static void registerDetectableBlocks(boolean logging)
    {
        ClientProxyCore.detectableBlocks.clear();

        for (final String s : ConfigManagerCore.detectableIDs)
        {
            // TODO Blockstate property parsing? To replace metadata
            Block bt = ConfigManagerCore.stringToBlock(new ResourceLocation(s), "External Detectable IDs", logging);
            if (bt == null)
            {
                continue;
            }

            ClientProxyCore.detectableBlocks.add(bt);

//            int meta = bt.meta;
//            if (meta == -1)
//            {
//                meta = 0;
//            }
//
//            boolean flag = false;
//            for (BlockMetaList blockMetaList : ClientProxyCore.detectableBlocks)
//            {
//                if (blockMetaList.getBlock() == bt.block)
//                {
//                    if (!blockMetaList.getMetaList().contains(meta))
//                    {
//                        blockMetaList.getMetaList().add(meta);
//                    }
//                    flag = true;
//                    break;
//                }
//            }
//
//            if (!flag)
//            {
//                List<Integer> metaList = Lists.newArrayList();
//                metaList.add(meta);
//                ClientProxyCore.detectableBlocks.add(new BlockMetaList(bt.block, metaList));
//            }
        }
    }

    @SubscribeEvent
    public void onRenderTick(TickEvent.RenderTickEvent event)
    {
        final Minecraft minecraft = Minecraft.getInstance();
        final ClientPlayerEntity player = minecraft.player;
        final ClientPlayerEntity playerBaseClient = PlayerUtil.getPlayerBaseClientFromPlayer(player, false);
        if (player == null || playerBaseClient == null)
        {
            return;
        }

        GCPlayerStatsClient stats = GCPlayerStatsClient.get(playerBaseClient);

        if (event.phase == TickEvent.Phase.END)
        {
            if (minecraft.currentScreen instanceof IngameMenuScreen)
            {
//                int i = Mouse.getEventX() * minecraft.currentScreen.width / minecraft.displayWidth;
//                int j = minecraft.currentScreen.height - Mouse.getEventY() * minecraft.currentScreen.height / minecraft.displayHeight - 1;
//
//                int k = Mouse.getEventButton();
//
//                int deltaColor = 0;
//
//                if (i > minecraft.currentScreen.width - 100 && j > minecraft.currentScreen.height - 35)
//                {
//                    deltaColor = 20;
//
//                    if (k == 0)
//                    {
//                        if (Mouse.getEventButtonState())
//                        {
//                            minecraft.displayGuiScreen(new GuiNewSpaceRace(playerBaseClient));
//                        }
//                    }
//                } TODO Space race button

//                this.fillGradient(minecraft.currentScreen.width - 100, minecraft.currentScreen.height - 35, minecraft.currentScreen.width, minecraft.currentScreen.height, ColorUtil.to32BitColor(150, 10 + deltaColor, 10 + deltaColor, 10 + deltaColor), ColorUtil.to32BitColor(250, 10 + deltaColor, 10 + deltaColor, 10 + deltaColor));
                minecraft.fontRenderer.drawString(GCCoreUtil.translate("gui.space_race.create.title.name.0"), minecraft.currentScreen.width - 50 - minecraft.fontRenderer.getStringWidth(GCCoreUtil.translate("gui.space_race.create.title.name.0")) / 2, minecraft.currentScreen.height - 26, ColorUtil.to32BitColor(255, 240, 240, 240));
                minecraft.fontRenderer.drawString(GCCoreUtil.translate("gui.space_race.create.title.name.1"), minecraft.currentScreen.width - 50 - minecraft.fontRenderer.getStringWidth(GCCoreUtil.translate("gui.space_race.create.title.name.1")) / 2, minecraft.currentScreen.height - 16, ColorUtil.to32BitColor(255, 240, 240, 240));
                AbstractGui.fill(minecraft.currentScreen.width - 100, minecraft.currentScreen.height - 35, minecraft.currentScreen.width - 99, minecraft.currentScreen.height, ColorUtil.to32BitColor(255, 0, 0, 0));
                AbstractGui.fill(minecraft.currentScreen.width - 100, minecraft.currentScreen.height - 35, minecraft.currentScreen.width, minecraft.currentScreen.height - 34, ColorUtil.to32BitColor(255, 0, 0, 0));
            }

            ClientProxyCore.playerPosX = player.prevPosX + (player.getPosX() - player.prevPosX) * event.renderTickTime;
            ClientProxyCore.playerPosY = player.prevPosY + (player.getPosY() - player.prevPosY) * event.renderTickTime;
            ClientProxyCore.playerPosZ = player.prevPosZ + (player.getPosZ() - player.prevPosZ) * event.renderTickTime;
            ClientProxyCore.playerRotationYaw = player.prevRotationYaw + (player.rotationYaw - player.prevRotationYaw) * event.renderTickTime;
            ClientProxyCore.playerRotationPitch = player.prevRotationPitch + (player.rotationPitch - player.prevRotationPitch) * event.renderTickTime;

            if (minecraft.currentScreen == null && minecraft.gameSettings.thirdPersonView != 0 && !minecraft.gameSettings.hideGUI)
            {
                if (player.getRidingEntity() instanceof EntitySpaceshipBase)
                {
                }

                OverlayRocket.renderSpaceshipOverlay();
                OverlayLander.renderLanderOverlay(TickHandlerClient.tickCount);
                OverlayDockingRocket.renderDockingOverlay(TickHandlerClient.tickCount);
                OverlayLaunchCountdown.renderCountdownOverlay();
            }

            if (player.world.getDimension() instanceof IGalacticraftDimension && OxygenUtil.shouldDisplayTankGui(minecraft.currentScreen) && OxygenUtil.noAtmosphericCombustion(player.world.getDimension()) && !(playerBaseClient.isCreative() || playerBaseClient.isSpectator()) && !minecraft.gameSettings.showDebugInfo)
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
//                OverlayOxygenTanks.renderOxygenTankIndicator(minecraft, thermalLevel, var6, var7, !ConfigManagerCore.oxygenIndicatorLeft, !ConfigManagerCore.oxygenIndicatorBottom, Math.abs(thermalLevel - 22) >= 10 && !stats.isThermalLevelNormalising());
                // TODO Overlays
            }

            if (player.world.getDimension() instanceof IGalacticraftDimension && !stats.isOxygenSetupValid() && OxygenUtil.noAtmosphericCombustion(player.world.getDimension()) && minecraft.currentScreen == null && !minecraft.gameSettings.hideGUI && !(playerBaseClient.isCreative() || playerBaseClient.isSpectator()))
            {
                OverlayOxygenWarning.renderOxygenWarningOverlay(TickHandlerClient.tickCount);
            }
        }
    }

    @SubscribeEvent
    public void onPreGuiRender(RenderGameOverlayEvent.Pre event)
    {
        final Minecraft minecraft = Minecraft.getInstance();
        final ClientPlayerEntity player = minecraft.player;

        if (event.getType() == RenderGameOverlayEvent.ElementType.ALL)
        {
            if (player != null && player.getRidingEntity() != null && player.getRidingEntity() instanceof IIgnoreShift && ((IIgnoreShift) player.getRidingEntity()).shouldIgnoreShiftExit())
            {
                // Remove "Press shift to dismount" message when shift-exiting is disabled (not ideal, but the only option)
                String str = I18n.format("mount.onboard", minecraft.gameSettings.keyBindSneak.getLocalizedName());
                if (minecraft.ingameGUI.overlayMessage.equals(str))
                {
                    minecraft.ingameGUI.overlayMessage = "";
                }
            }
        }
    }

    @OnlyIn(Dist.CLIENT)
    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event)
    {
        final Minecraft minecraft = Minecraft.getInstance();
        final ClientWorld world = minecraft.world;
        final ClientPlayerEntity player = minecraft.player;

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

        if (event.phase == TickEvent.Phase.START && player != null)
        {
            if (ClientProxyCore.playerHead == null)
            {
                player.getGameProfile();
                Map<Type, MinecraftProfileTexture> map = minecraft.getSkinManager().loadSkinFromCache(player.getGameProfile());

                if (map.containsKey(Type.SKIN))
                {
                    ClientProxyCore.playerHead = minecraft.getSkinManager().loadSkin(map.get(Type.SKIN), Type.SKIN);
                }
                else
                {
                    ClientProxyCore.playerHead = DefaultPlayerSkin.getDefaultSkin(PlayerEntity.getUUID(player.getGameProfile()));
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
//                BubbleRenderer.clearBubbles();
//
//                for (TileEntity tile : player.world.tickableTileEntities)
//                {
//                    if (tile instanceof IBubbleProviderColored)
//                    {
//                        BubbleRenderer.addBubble((IBubbleProviderColored) tile);
//                    }
//                } TODO Bubble Rendering

                if (updateJEIhiding)
                {
                    updateJEIhiding = false;
                    // Update JEI to hide the ingot compressor recipe for GC steel in hard mode
                    // Update JEI to hide adventure mode recipes when not in adventure mode
//                    GalacticraftJEI.updateHidden(CompressorRecipes.steelIngotsPresent && ConfigManagerCore.hardMode && !ConfigManagerCore.challengeRecipes, !ConfigManagerCore.challengeRecipes); TODO JEI
                }

                for (List<Footprint> fpList : FootprintRenderer.footprints.values())
                {
                    Iterator<Footprint> fpIt = fpList.iterator();
                    while (fpIt.hasNext())
                    {
                        Footprint fp = fpIt.next();
                        fp.age += 20;
                        fp.lightmapVal = player.world.getLight(new BlockPos(fp.position.x, fp.position.y, fp.position.z));

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
                        int x = MathHelper.floor(player.getPosX() + i);
                        for (int j = -4; j < 5; j++)
                        {
                            int y = MathHelper.floor(player.getPosY() + j);
                            for (int k = -4; k < 5; k++)
                            {
                                int z = MathHelper.floor(player.getPosZ() + k);
                                BlockPos pos = new BlockPos(x, y, z);

                                BlockState state = player.world.getBlockState(pos);
                                final Block block = state.getBlock();

                                if (block.getMaterial(state) != Material.AIR)
                                {
//                                    int metadata = block.getMetaFromState(state);
                                    boolean isDetectable = false;

                                    for (Block detectableBlock : ClientProxyCore.detectableBlocks)
                                    {
//                                        if (blockMetaList.getBlock() == block && blockMetaList.getMetaList().contains(metadata))
                                        if (detectableBlock == block)
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

                    TileEntityOxygenSealer nearestSealer = TileEntityOxygenSealer.getNearestSealer(world, MathHelper.floor(player.getPosX()), MathHelper.floor(player.getPosY()), MathHelper.floor(player.getPosZ()));
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

            if (ClientProxyCore.leakTrace != null)
            {
                this.spawnLeakParticles();
            }

            if (world != null && TickHandlerClient.spaceRaceGuiScheduled && minecraft.currentScreen == null && ConfigManagerCore.enableSpaceRaceManagerPopup)
            {
//                player.openGui(GalacticraftCore.instance, GuiIdsCore.SPACE_RACE_START, player.world, (int) player.getPosX(), (int) player.getPosY(), (int) player.getPosZ()); TODO Gui
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
//                    GalacticraftCore.packetPipeline.sendToServer(new PacketRotateRocket(player.getRidingEntity()));
                    GalacticraftCore.packetPipeline.sendToServer(new PacketSimple(EnumSimplePacket.S_ROTATE_ROCKET, rocket.dimension, new Object[]{rocket.rotationPitch, rocket.rotationYaw}));
                }
            }

            if (world != null)
            {
                if (world.getDimension() instanceof OverworldDimension)
                {
//                    if (world.getDimension().getSkyRenderer() == null && inSpaceShip &&
//                            player.getRidingEntity().getPosY() > Constants.OVERWORLD_SKYPROVIDER_STARTHEIGHT)
//                    {
//                        world.getDimension().setSkyRenderer(new SkyProviderOverworld());
//                    }
//                    else if (world.getDimension().getSkyRenderer() instanceof SkyProviderOverworld && player.getPosY() <= Constants.OVERWORLD_SKYPROVIDER_STARTHEIGHT)
//                    {
//                        world.getDimension().setSkyRenderer(null);
//                    }  TODO Sky rendering
                }
//                else if (world.getDimension() instanceof DimensionSpaceStation)
//                {
//                    if (world.getDimension().getSkyRenderer() == null)
//                    {
//                        ((DimensionSpaceStation) world.getDimension()).createSkyProvider();
//                    }
//                } TODO Space stations
                else if (world.getDimension() instanceof DimensionMoon)
                {
//                    if (world.getDimension().getSkyRenderer() == null)
//                    {
//                        world.getDimension().setSkyRenderer(new SkyProviderMoon());
//                    }  TODO Sky rendering

                    if (world.getDimension().getCloudRenderer() == null)
                    {
                        world.getDimension().setCloudRenderer(new CloudRenderer());
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
//                    GalacticraftCore.packetPipeline.sendToServer(new PacketRotateRocket(ship));
                    GalacticraftCore.packetPipeline.sendToServer(new PacketSimple(EnumSimplePacket.S_ROTATE_ROCKET, ship.dimension, new Object[]{ship.rotationPitch, ship.rotationYaw}));
                }
            }

            if (world != null)
            {
                Iterable<Entity> entityList = world.getAllEntities();
                for (Object e : entityList)
                {
                    if (e instanceof IEntityNoisy)
                    {
                        IEntityNoisy vehicle = (IEntityNoisy) e;
                        if (vehicle.getSoundUpdater() == null)
                        {
                            ISound noise = vehicle.setSoundUpdater(Minecraft.getInstance().player);
                            if (noise != null)
                            {
                                Minecraft.getInstance().getSoundHandler().play(noise);
                            }
                        }
                    }
                }
            }

            if (Minecraft.getInstance().currentScreen instanceof GuiCelestialSelection)
            {
                player.setMotion(player.getMotion().x, 0.0, player.getMotion().z);
            }

            if (world != null && world.getDimension() instanceof IGalacticraftDimension && OxygenUtil.noAtmosphericCombustion(world.getDimension()) && ((IGalacticraftDimension) world.getDimension()).shouldDisablePrecipitation())
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
                GalacticraftCore.packetPipeline.sendToServer(new PacketSimple(EnumSimplePacket.S_IGNITE_ROCKET, GCCoreUtil.getDimensionType(player.world), new Object[]{}));
                ClientProxyCore.lastSpacebarDown = true;
            }

            if (!(screenConnectionsUpdateList.isEmpty()))
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
//                        te.getWorld().markBlockRangeForRenderUpdate(te.getPos(), te.getPos());
                    }
                }
            }
        }
        else if (event.phase == TickEvent.Phase.END)
        {
            if (world != null)
            {
//                for (GalacticraftPacketHandler handler : packetHandlers)
//                {
//                    handler.tick(world);
//                } TODO Packet handler ticking?
            }
        }
    }

    private void spawnLeakParticles()
    {
        Random rand = new Random();
        for (int i = ClientProxyCore.leakTrace.size() - 1; i >= 0; i--)
        {
            if (i == 1)
            {
                continue;
            }
            BlockVec3 curr = ClientProxyCore.leakTrace.get(i);
            int nx = i - 2;
            if (i > 2 && rand.nextInt(3) == 0)
            {
                nx--;
            }
            BlockVec3 vec;
            if (i > 1)
            {
                vec = ClientProxyCore.leakTrace.get(nx).clone();
            }
            else
            {
                vec = curr.clone().translate(0, -2, 0);
            }
            Vector3 mot = new Vector3(vec.subtract(curr));
            Vector3 rnd = new Vector3(rand.nextFloat() / 2.0F - 0.25F, rand.nextFloat() / 2.0F - 0.25F, rand.nextFloat() / 2.0F - 0.25F);
            Vector3 offset = curr.midPoint().add(rnd);
            Minecraft.getInstance().world.addParticle(GCParticles.OXYGEN, offset.x, offset.y, offset.z, mot.x, mot.y, mot.z);
        }
    }

    private boolean alreadyContainsBlock(int x1, int y1, int z1)
    {
        return ClientProxyCore.valueableBlocks.contains(new BlockVec3(x1, y1, z1));
    }

    public static void zoom(float value)
    {
//        Minecraft.getInstance().entityRenderer.thirdPersonDistance = value;
//        Minecraft.getInstance().entityRenderer.thirdPersonDistancePrev = value;
    }

//    private void fillGradient(int par1, int par2, int par3, int par4, int par5, int par6)
//    {
//        float f = (par5 >> 24 & 255) / 255.0F;
//        float f1 = (par5 >> 16 & 255) / 255.0F;
//        float f2 = (par5 >> 8 & 255) / 255.0F;
//        float f3 = (par5 & 255) / 255.0F;
//        float f4 = (par6 >> 24 & 255) / 255.0F;
//        float f5 = (par6 >> 16 & 255) / 255.0F;
//        float f6 = (par6 >> 8 & 255) / 255.0F;
//        float f7 = (par6 & 255) / 255.0F;
//        RenderSystem.disableTexture();
//        RenderSystem.enableBlend();
//        RenderSystem.disableAlphaTest();
//        RenderSystem.blendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA.param, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA.param, GlStateManager.SourceFactor.ONE.param, GlStateManager.DestFactor.ZERO.param);
//        RenderSystem.shadeModel(7425);
//        Tessellator tessellator = Tessellator.getInstance();
//        BufferBuilder worldRenderer = tessellator.getBuffer();
//        worldRenderer.begin(7, DefaultVertexFormats.POSITION_COLOR);
//        worldRenderer.pos(par3, par2, 0.0D).color(f1, f2, f3, f).endVertex();
//        worldRenderer.pos(par1, par2, 0.0D).color(f1, f2, f3, f).endVertex();
//        worldRenderer.pos(par1, par4, 0.0D).color(f5, f6, f7, f4).endVertex();
//        worldRenderer.pos(par3, par4, 0.0D).color(f5, f6, f7, f4).endVertex();
//        tessellator.draw();
//        RenderSystem.shadeModel(7424);
//        RenderSystem.disableBlend();
//        RenderSystem.enableAlphaTest();
//        RenderSystem.enableTexture();
//    }
}
