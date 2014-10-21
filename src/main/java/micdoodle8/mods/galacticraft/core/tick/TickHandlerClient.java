package micdoodle8.mods.galacticraft.core.tick;

import com.google.common.collect.Lists;

import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent.ClientTickEvent;
import cpw.mods.fml.common.gameevent.TickEvent.Phase;
import cpw.mods.fml.common.gameevent.TickEvent.RenderTickEvent;
import micdoodle8.mods.galacticraft.api.block.IDetectableResource;
import micdoodle8.mods.galacticraft.api.entity.IIgnoreShift;
import micdoodle8.mods.galacticraft.api.prefab.entity.EntityAutoRocket;
import micdoodle8.mods.galacticraft.api.prefab.entity.EntitySpaceshipBase;
import micdoodle8.mods.galacticraft.api.prefab.entity.EntitySpaceshipBase.EnumLaunchPhase;
import micdoodle8.mods.galacticraft.api.vector.BlockTuple;
import micdoodle8.mods.galacticraft.api.vector.Vector3;
import micdoodle8.mods.galacticraft.api.world.IGalacticraftWorldProvider;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.client.CloudRenderer;
import micdoodle8.mods.galacticraft.core.client.SkyProviderMoon;
import micdoodle8.mods.galacticraft.core.client.SkyProviderOrbit;
import micdoodle8.mods.galacticraft.core.client.SkyProviderOverworld;
import micdoodle8.mods.galacticraft.core.client.gui.GuiIdsCore;
import micdoodle8.mods.galacticraft.core.client.gui.overlay.*;
import micdoodle8.mods.galacticraft.core.client.gui.screen.GuiCelestialSelection;
import micdoodle8.mods.galacticraft.core.client.gui.screen.GuiNewSpaceRace;
import micdoodle8.mods.galacticraft.core.client.sounds.SoundUpdaterRocket;
import micdoodle8.mods.galacticraft.core.dimension.WorldProviderMoon;
import micdoodle8.mods.galacticraft.core.dimension.WorldProviderOrbit;
import micdoodle8.mods.galacticraft.core.entities.EntityLander;
import micdoodle8.mods.galacticraft.core.entities.EntityTier1Rocket;
import micdoodle8.mods.galacticraft.core.entities.player.GCEntityClientPlayerMP;
import micdoodle8.mods.galacticraft.core.entities.player.GCPlayerStatsClient;
import micdoodle8.mods.galacticraft.core.items.ItemSensorGlasses;
import micdoodle8.mods.galacticraft.core.network.PacketRotateRocket;
import micdoodle8.mods.galacticraft.core.network.PacketSimple;
import micdoodle8.mods.galacticraft.core.network.PacketSimple.EnumSimplePacket;
import micdoodle8.mods.galacticraft.core.proxy.ClientProxyCore;
import micdoodle8.mods.galacticraft.core.util.*;
import micdoodle8.mods.galacticraft.core.wrappers.BlockMetaList;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.ISound;
import net.minecraft.client.entity.EntityClientPlayerMP;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiIngameMenu;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.WorldProviderSurface;
import net.minecraftforge.client.event.RenderGameOverlayEvent;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import java.util.List;

public class TickHandlerClient
{
    public static int airRemaining;
    public static int airRemaining2;
    public static boolean checkedVersion = true;
    private static boolean lastInvKeyPressed;
    private static long tickCount;
    public static boolean spaceRaceGuiScheduled = false;

    private static ThreadRequirementMissing missingRequirementThread;

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
        	if (bt == null) continue;

			int meta = bt.meta;
			if (meta == -1) meta = 0;
        	
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
        final EntityPlayerSP player = minecraft.thePlayer;
        final EntityClientPlayerMP playerBaseClient = PlayerUtil.getPlayerBaseClientFromPlayer(player, false);
        GCPlayerStatsClient stats = null;

        if (player != null)
        {
            stats = GCEntityClientPlayerMP.getPlayerStats(playerBaseClient);
        }

        if (event.phase == Phase.END)
        {
            if (minecraft.currentScreen instanceof GuiIngameMenu)
            {
                int i = Mouse.getEventX() * minecraft.currentScreen.width / minecraft.displayWidth;
                int j = minecraft.currentScreen.height - Mouse.getEventY() * minecraft.currentScreen.height / minecraft.displayHeight - 1;

                int k = Mouse.getEventButton();

                if (Minecraft.isRunningOnMac && k == 0 && (Keyboard.isKeyDown(29) || Keyboard.isKeyDown(157)))
                {
                    k = 1;
                }

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

                this.drawGradientRect(minecraft.currentScreen.width - 100, minecraft.currentScreen.height - 35, minecraft.currentScreen.width, minecraft.currentScreen.height, GCCoreUtil.to32BitColor(150, 10 + deltaColor, 10 + deltaColor, 10 + deltaColor), GCCoreUtil.to32BitColor(250, 10 + deltaColor, 10 + deltaColor, 10 + deltaColor));
                minecraft.fontRenderer.drawString(GCCoreUtil.translate("gui.spaceRace.create.title.name.0"), minecraft.currentScreen.width - 50 - minecraft.fontRenderer.getStringWidth(GCCoreUtil.translate("gui.spaceRace.create.title.name.0")) / 2, minecraft.currentScreen.height - 26, GCCoreUtil.to32BitColor(255, 240, 240, 240));
                minecraft.fontRenderer.drawString(GCCoreUtil.translate("gui.spaceRace.create.title.name.1"), minecraft.currentScreen.width - 50 - minecraft.fontRenderer.getStringWidth(GCCoreUtil.translate("gui.spaceRace.create.title.name.1")) / 2, minecraft.currentScreen.height - 16, GCCoreUtil.to32BitColor(255, 240, 240, 240));
                Gui.drawRect(minecraft.currentScreen.width - 100, minecraft.currentScreen.height - 35, minecraft.currentScreen.width - 99, minecraft.currentScreen.height, GCCoreUtil.to32BitColor(255, 0, 0, 0));
                Gui.drawRect(minecraft.currentScreen.width - 100, minecraft.currentScreen.height - 35, minecraft.currentScreen.width, minecraft.currentScreen.height - 34, GCCoreUtil.to32BitColor(255, 0, 0, 0));
            }

            if (player != null)
            {
                ClientProxyCore.playerPosX = player.prevPosX + (player.posX - player.prevPosX) * event.renderTickTime;
                ClientProxyCore.playerPosY = player.prevPosY + (player.posY - player.prevPosY) * event.renderTickTime;
                ClientProxyCore.playerPosZ = player.prevPosZ + (player.posZ - player.prevPosZ) * event.renderTickTime;
                ClientProxyCore.playerRotationYaw = player.prevRotationYaw + (player.rotationYaw - player.prevRotationYaw) * event.renderTickTime;
                ClientProxyCore.playerRotationPitch = player.prevRotationPitch + (player.rotationPitch - player.prevRotationPitch) * event.renderTickTime;
            }

            if (player != null && player.ridingEntity != null && player.ridingEntity instanceof EntityTier1Rocket)
            {
                float f = (((EntityTier1Rocket) player.ridingEntity).timeSinceLaunch - 250F) / 175F;

                if (f < 0)
                {
                    f = 0F;
                }

                if (f > 1)
                {
                    f = 1F;
                }

                final ScaledResolution scaledresolution = ClientUtil.getScaledRes(minecraft, minecraft.displayWidth, minecraft.displayHeight);
                scaledresolution.getScaledWidth();
                scaledresolution.getScaledHeight();
                minecraft.entityRenderer.setupOverlayRendering();
                GL11.glEnable(GL11.GL_BLEND);
                GL11.glDisable(GL11.GL_DEPTH_TEST);
                GL11.glDepthMask(false);
                GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
                GL11.glColor4f(1.0F, 1.0F, 1.0F, f);
                GL11.glDisable(GL11.GL_ALPHA_TEST);
                GL11.glDepthMask(true);
                GL11.glEnable(GL11.GL_DEPTH_TEST);
                GL11.glEnable(GL11.GL_ALPHA_TEST);
                GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            }

            if (minecraft.currentScreen == null && player != null && player.ridingEntity != null && player.ridingEntity instanceof EntitySpaceshipBase && minecraft.gameSettings.thirdPersonView != 0 && !minecraft.gameSettings.hideGUI)
            {
                OverlayRocket.renderSpaceshipOverlay(((EntitySpaceshipBase) player.ridingEntity).getSpaceshipGui());
            }

            if (minecraft.currentScreen == null && player != null && player.ridingEntity != null && player.ridingEntity instanceof EntityLander && minecraft.gameSettings.thirdPersonView != 0 && !minecraft.gameSettings.hideGUI)
            {
                OverlayLander.renderLanderOverlay();
            }

            if (minecraft.currentScreen == null && player != null && player.ridingEntity != null && player.ridingEntity instanceof EntityAutoRocket && minecraft.gameSettings.thirdPersonView != 0 && !minecraft.gameSettings.hideGUI)
            {
                OverlayDockingRocket.renderDockingOverlay();
            }

            if (minecraft.currentScreen == null && player != null && player.ridingEntity != null && player.ridingEntity instanceof EntitySpaceshipBase && minecraft.gameSettings.thirdPersonView != 0 && !minecraft.gameSettings.hideGUI && ((EntitySpaceshipBase) minecraft.thePlayer.ridingEntity).launchPhase != EnumLaunchPhase.LAUNCHED.ordinal())
            {
                OverlayLaunchCountdown.renderCountdownOverlay();
            }

            if (player != null && player.worldObj.provider instanceof IGalacticraftWorldProvider && OxygenUtil.shouldDisplayTankGui(minecraft.currentScreen))
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

                int thermalLevel = stats.thermalLevel + 22;
                OverlayOxygenTanks.renderOxygenTankIndicator(thermalLevel, var6, var7, !ConfigManagerCore.oxygenIndicatorLeft, !ConfigManagerCore.oxygenIndicatorBottom, Math.abs(thermalLevel - 22) >= 10);
            }

            if (playerBaseClient != null && player.worldObj.provider instanceof IGalacticraftWorldProvider && !stats.oxygenSetupValid && minecraft.currentScreen == null && !playerBaseClient.capabilities.isCreativeMode)
            {
                OverlayOxygenWarning.renderOxygenWarningOverlay();
            }
        }
    }

    @SubscribeEvent
    public void onPreGuiRender(RenderGameOverlayEvent.Pre event)
    {
        final Minecraft minecraft = FMLClientHandler.instance().getClient();
        final EntityClientPlayerMP player = minecraft.thePlayer;

        if (event.type == RenderGameOverlayEvent.ElementType.ALL)
        {
            if (player != null && player.ridingEntity != null && player.ridingEntity instanceof IIgnoreShift && ((IIgnoreShift) player.ridingEntity).shouldIgnoreShiftExit())
            {
                // Remove "Press shift to dismount" message when shift-exiting is disabled (not ideal, but the only option)
                String str = I18n.format("mount.onboard", new Object[] { GameSettings.getKeyDisplayString(minecraft.gameSettings.keyBindSneak.getKeyCode()) });
                if (minecraft.ingameGUI.recordPlaying.equals(str))
                {
                    minecraft.ingameGUI.recordPlaying = "";
                }
            }
        }
    }

    @SubscribeEvent
    public void onClientTick(ClientTickEvent event)
    {
        final Minecraft minecraft = FMLClientHandler.instance().getClient();
        final WorldClient world = minecraft.theWorld;
        final EntityClientPlayerMP player = minecraft.thePlayer;

        if (event.phase == Phase.START)
        {
            if (TickHandlerClient.tickCount >= Long.MAX_VALUE)
            {
                TickHandlerClient.tickCount = 0;
            }

            TickHandlerClient.tickCount++;

            if (TickHandlerClient.tickCount % 20 == 0)
            {
                if (player != null && player.inventory.armorItemInSlot(3) != null && player.inventory.armorItemInSlot(3).getItem() instanceof ItemSensorGlasses)
                {
                    ClientProxyCore.valueableBlocks.clear();

                    for (int i = -4; i < 5; i++)
                    {
                        for (int j = -4; j < 5; j++)
                        {
                            for (int k = -4; k < 5; k++)
                            {
                                int x = MathHelper.floor_double(player.posX + i);
                                int y = MathHelper.floor_double(player.posY + j);
                                int z = MathHelper.floor_double(player.posZ + k);

                                final Block block = player.worldObj.getBlock(x, y, z);

                                if (block.getMaterial() != Material.air)
                                {
                                    int metadata = world.getBlockMetadata(x, y, z);
                                    boolean isDetectable = false;

                                    for (BlockMetaList blockMetaList : ClientProxyCore.detectableBlocks)
                                    {
                                        if (blockMetaList.getBlock() == block && blockMetaList.getMetaList().contains(metadata))
                                        {
                                            isDetectable = true;
                                            break;
                                        }
                                    }

                                    if (isDetectable)
                                    {
                                        if (!this.alreadyContainsBlock(x, y, z))
                                        {
                                            ClientProxyCore.valueableBlocks.add(new Vector3(x, y, z));
                                        }
                                    }
                                    else if (block instanceof IDetectableResource && ((IDetectableResource) block).isValueable(metadata))
                                    {
                                        if (!this.alreadyContainsBlock(x, y, z))
                                        {
                                            ClientProxyCore.valueableBlocks.add(new Vector3(x, y, z));
                                        }

                                        List<Integer> metaList = null;

                                        for (BlockMetaList blockMetaList : ClientProxyCore.detectableBlocks)
                                        {
                                            if (blockMetaList.getBlock() == block)
                                            {
                                                metaList = blockMetaList.getMetaList();
                                                if (!metaList.contains(metadata))
                                                {
                                                    metaList.add(metadata);
                                                }
                                                break;
                                            }
                                        }

                                        if (metaList == null)
                                        {
                                            metaList = Lists.newArrayList();
                                            metaList.add(metadata);
                                            ClientProxyCore.detectableBlocks.add(new BlockMetaList(block, metaList));
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }

            if (minecraft.currentScreen != null && minecraft.currentScreen instanceof GuiMainMenu)
            {
                ClientProxyCore.playerItemData.clear();

                if (TickHandlerClient.missingRequirementThread == null)
                {
                    TickHandlerClient.missingRequirementThread = new ThreadRequirementMissing(FMLCommonHandler.instance().getEffectiveSide());
                    TickHandlerClient.missingRequirementThread.start();
                }
            }

            if (world != null && TickHandlerClient.spaceRaceGuiScheduled && minecraft.currentScreen == null)
            {
                player.openGui(GalacticraftCore.instance, GuiIdsCore.SPACE_RACE_START, player.worldObj, (int) player.posX, (int) player.posY, (int) player.posZ);
                TickHandlerClient.spaceRaceGuiScheduled = false;
            }

            if (world != null && TickHandlerClient.checkedVersion)
            {
                ThreadVersionCheck.startCheck();
                TickHandlerClient.checkedVersion = false;
            }

            if (player != null && player.ridingEntity != null && player.ridingEntity instanceof EntitySpaceshipBase)
            {
                GalacticraftCore.packetPipeline.sendToServer(new PacketRotateRocket(player.ridingEntity));
            }

            if (world != null)
            {
                if (world.provider instanceof WorldProviderSurface)
                {
                    if (world.provider.getSkyRenderer() == null && player.ridingEntity != null && player.ridingEntity.posY >= 200)
                    {
                        world.provider.setSkyRenderer(new SkyProviderOverworld());
                    }
                    else if (world.provider.getSkyRenderer() != null && world.provider.getSkyRenderer() instanceof SkyProviderOverworld && (player.ridingEntity == null || player.ridingEntity.posY < 200))
                    {
                        world.provider.setSkyRenderer(null);
                    }
                }
                else if (world.provider instanceof WorldProviderOrbit)
                {
                    if (world.provider.getSkyRenderer() == null)
                    {
                        world.provider.setSkyRenderer(new SkyProviderOrbit(new ResourceLocation(GalacticraftCore.ASSET_PREFIX, "textures/gui/celestialbodies/earth.png"), true, true));
                        ((SkyProviderOrbit) world.provider.getSkyRenderer()).spinDeltaPerTick = ((WorldProviderOrbit) world.provider).getSpinRate();
                        GCEntityClientPlayerMP.getPlayerStats(player).inFreefallFirstCheck = false;
                    }

                    if (world.provider.getCloudRenderer() == null)
                    {
                        world.provider.setCloudRenderer(new CloudRenderer());
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

            if (player != null && player.ridingEntity != null && player.ridingEntity instanceof EntitySpaceshipBase)
            {
                final EntitySpaceshipBase ship = (EntitySpaceshipBase) player.ridingEntity;
                boolean hasChanged = false;

                if (minecraft.gameSettings.keyBindLeft.getIsKeyPressed())
                {
                    ship.turnYaw(-1.0F);
                    hasChanged = true;
                }

                if (minecraft.gameSettings.keyBindRight.getIsKeyPressed())
                {
                    ship.turnYaw(1.0F);
                    hasChanged = true;
                }

                if (minecraft.gameSettings.keyBindForward.getIsKeyPressed())
                {
                    if (ship.getLaunched())
                    {
                        ship.turnPitch(-0.7F);
                        hasChanged = true;
                    }
                }

                if (minecraft.gameSettings.keyBindBack.getIsKeyPressed())
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
                    if (e instanceof EntityAutoRocket)
                    {
                        if (((EntityAutoRocket) e).rocketSoundUpdater == null)
                        {
                        	EntityAutoRocket eship = (EntityAutoRocket) e;
                        	eship.rocketSoundUpdater = new SoundUpdaterRocket(FMLClientHandler.instance().getClient().thePlayer, eship);
							FMLClientHandler.instance().getClient().getSoundHandler().playSound((ISound) eship.rocketSoundUpdater);
                        }
                    }
                }
            }

            if (FMLClientHandler.instance().getClient().currentScreen instanceof GuiCelestialSelection)
            {
                player.motionY = 0;
            }

            if (world != null && world.provider instanceof IGalacticraftWorldProvider)
            {
                world.setRainStrength(0.0F);
            }

            if (!KeyHandlerClient.spaceKey.getIsKeyPressed())
            {
                ClientProxyCore.lastSpacebarDown = false;
            }

            if (player != null && player.ridingEntity != null && KeyHandlerClient.spaceKey.getIsKeyPressed() && !ClientProxyCore.lastSpacebarDown)
            {
                GalacticraftCore.packetPipeline.sendToServer(new PacketSimple(EnumSimplePacket.S_IGNITE_ROCKET, new Object[] { }));
                ClientProxyCore.lastSpacebarDown = true;
            }
        }
    }

    private boolean alreadyContainsBlock(int x1, int y1, int z1)
    {
        return ClientProxyCore.valueableBlocks.contains(new Vector3(x1, y1, z1));
    }

    public static void zoom(float value)
    {
        FMLClientHandler.instance().getClient().entityRenderer.thirdPersonDistance = value;
        FMLClientHandler.instance().getClient().entityRenderer.thirdPersonDistanceTemp = value;
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
        Tessellator tessellator = Tessellator.instance;
        tessellator.startDrawingQuads();
        tessellator.setColorRGBA_F(f1, f2, f3, f);
        tessellator.addVertex(par3, par2, 0.0D);
        tessellator.addVertex(par1, par2, 0.0D);
        tessellator.setColorRGBA_F(f5, f6, f7, f4);
        tessellator.addVertex(par1, par4, 0.0D);
        tessellator.addVertex(par3, par4, 0.0D);
        tessellator.draw();
        GL11.glShadeModel(GL11.GL_FLAT);
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glEnable(GL11.GL_ALPHA_TEST);
        GL11.glEnable(GL11.GL_TEXTURE_2D);
    }
}
