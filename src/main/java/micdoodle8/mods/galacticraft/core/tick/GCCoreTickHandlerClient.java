package micdoodle8.mods.galacticraft.core.tick;

import java.util.List;

import micdoodle8.mods.galacticraft.api.block.IDetectableResource;
import micdoodle8.mods.galacticraft.api.prefab.entity.EntityAutoRocket;
import micdoodle8.mods.galacticraft.api.prefab.entity.EntitySpaceshipBase;
import micdoodle8.mods.galacticraft.api.prefab.entity.EntitySpaceshipBase.EnumLaunchPhase;
import micdoodle8.mods.galacticraft.api.vector.Vector3;
import micdoodle8.mods.galacticraft.api.world.IGalacticraftWorldProvider;
import micdoodle8.mods.galacticraft.core.GCCoreConfigManager;
import micdoodle8.mods.galacticraft.core.GCCoreThreadRequirementMissing;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.client.ClientProxyCore;
import micdoodle8.mods.galacticraft.core.client.GCCoreCloudRenderer;
import micdoodle8.mods.galacticraft.core.client.GCCoreSkyProviderOrbit;
import micdoodle8.mods.galacticraft.core.client.GCCoreSkyProviderOverworld;
import micdoodle8.mods.galacticraft.core.client.gui.GCCoreGuiChoosePlanet;
import micdoodle8.mods.galacticraft.core.client.gui.GCCoreOverlayCountdown;
import micdoodle8.mods.galacticraft.core.client.gui.GCCoreOverlayDockingRocket;
import micdoodle8.mods.galacticraft.core.client.gui.GCCoreOverlayLander;
import micdoodle8.mods.galacticraft.core.client.gui.GCCoreOverlayOxygenTankIndicator;
import micdoodle8.mods.galacticraft.core.client.gui.GCCoreOverlayOxygenWarning;
import micdoodle8.mods.galacticraft.core.client.gui.GCCoreOverlaySpaceship;
import micdoodle8.mods.galacticraft.core.dimension.GCCoreWorldProviderSpaceStation;
import micdoodle8.mods.galacticraft.core.entities.EntityLander;
import micdoodle8.mods.galacticraft.core.entities.EntityTier1Rocket;
import micdoodle8.mods.galacticraft.core.entities.player.GCCorePlayerSP;
import micdoodle8.mods.galacticraft.core.items.GCCoreItemSensorGlasses;
import micdoodle8.mods.galacticraft.core.network.PacketRotateRocket;
import micdoodle8.mods.galacticraft.core.network.PacketSimple;
import micdoodle8.mods.galacticraft.core.network.PacketSimple.EnumSimplePacket;
import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
import micdoodle8.mods.galacticraft.core.util.OxygenUtil;
import micdoodle8.mods.galacticraft.core.util.PlayerUtil;
import micdoodle8.mods.galacticraft.core.wrappers.BlockMetaList;
import micdoodle8.mods.galacticraft.moon.client.GCMoonSkyProvider;
import micdoodle8.mods.galacticraft.moon.dimension.GCMoonWorldProvider;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityClientPlayerMP;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.WorldProviderSurface;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import tconstruct.client.tabs.TabRegistry;

import com.google.common.collect.Lists;

import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.ObfuscationReflectionHelper;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent.ClientTickEvent;
import cpw.mods.fml.common.gameevent.TickEvent.Phase;
import cpw.mods.fml.common.gameevent.TickEvent.RenderTickEvent;
import cpw.mods.fml.relauncher.Side;

/**
 * GCCoreTickHandlerClient.java
 * 
 * This file is part of the Galacticraft project
 * 
 * @author micdoodle8
 * @license Lesser GNU Public License v3 (http://www.gnu.org/licenses/lgpl.html)
 * 
 */
public class GCCoreTickHandlerClient
{
	public static int airRemaining;
	public static int airRemaining2;
	public static boolean checkedVersion = true;
	private static boolean lastInvKeyPressed;
	private static long tickCount;
	public static boolean addTabsNextTick = false;

	private static GCCoreThreadRequirementMissing missingRequirementThread;

	static
	{
		for (final String s : GCCoreConfigManager.detectableIDs)
		{
			final String[] split = s.split(":");
			Block blockID = Block.getBlockById(Integer.parseInt(split[0]));
			List<Integer> metaList = Lists.newArrayList();
			metaList.add(Integer.parseInt(split[1]));

			for (BlockMetaList blockMetaList : ClientProxyCore.detectableBlocks)
			{
				if (blockMetaList.getBlock() == blockID)
				{
					metaList.addAll(blockMetaList.getMetaList());
					break;
				}
			}

			if (!metaList.contains(0))
			{
				metaList.add(0);
			}

			ClientProxyCore.detectableBlocks.add(new BlockMetaList(blockID, metaList));
		}
	}

	@SubscribeEvent
	public void onRenderTick(RenderTickEvent event)
	{
		final Minecraft minecraft = FMLClientHandler.instance().getClient();
		final EntityPlayerSP player = minecraft.thePlayer;
		final GCCorePlayerSP playerBaseClient = PlayerUtil.getPlayerBaseClientFromPlayer(player, false);

		if (event.phase == Phase.END)
		{
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

				final ScaledResolution scaledresolution = new ScaledResolution(minecraft.gameSettings, minecraft.displayWidth, minecraft.displayHeight);
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
				GCCoreOverlaySpaceship.renderSpaceshipOverlay(((EntitySpaceshipBase) player.ridingEntity).getSpaceshipGui());
			}

			if (minecraft.currentScreen == null && player != null && player.ridingEntity != null && player.ridingEntity instanceof EntityLander && minecraft.gameSettings.thirdPersonView != 0 && !minecraft.gameSettings.hideGUI)
			{
				GCCoreOverlayLander.renderLanderOverlay();
			}

			if (minecraft.currentScreen == null && player != null && player.ridingEntity != null && player.ridingEntity instanceof EntityAutoRocket && minecraft.gameSettings.thirdPersonView != 0 && !minecraft.gameSettings.hideGUI)
			{
				GCCoreOverlayDockingRocket.renderDockingOverlay();
			}

			if (minecraft.currentScreen == null && player != null && player.ridingEntity != null && player.ridingEntity instanceof EntitySpaceshipBase && minecraft.gameSettings.thirdPersonView != 0 && !minecraft.gameSettings.hideGUI && ((EntitySpaceshipBase) minecraft.thePlayer.ridingEntity).launchPhase != EnumLaunchPhase.LAUNCHED.getPhase())
			{
				GCCoreOverlayCountdown.renderCountdownOverlay();
			}

			if (player != null && player.worldObj.provider instanceof IGalacticraftWorldProvider && OxygenUtil.shouldDisplayTankGui(minecraft.currentScreen))
			{
				int var6 = (GCCoreTickHandlerClient.airRemaining - 90) * -1;

				if (GCCoreTickHandlerClient.airRemaining <= 0)
				{
					var6 = 90;
				}

				int var7 = (GCCoreTickHandlerClient.airRemaining2 - 90) * -1;

				if (GCCoreTickHandlerClient.airRemaining2 <= 0)
				{
					var7 = 90;
				}

				GCCoreOverlayOxygenTankIndicator.renderOxygenTankIndicator(var6, var7, !GCCoreConfigManager.oxygenIndicatorLeft, !GCCoreConfigManager.oxygenIndicatorBottom);
			}

			if (playerBaseClient != null && player.worldObj.provider instanceof IGalacticraftWorldProvider && !playerBaseClient.oxygenSetupValid && minecraft.currentScreen == null && !playerBaseClient.capabilities.isCreativeMode)
			{
				GCCoreOverlayOxygenWarning.renderOxygenWarningOverlay();
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
			if (GCCoreTickHandlerClient.tickCount >= Long.MAX_VALUE)
			{
				GCCoreTickHandlerClient.tickCount = 0;
			}

			GCCoreTickHandlerClient.tickCount++;

			if (GCCoreTickHandlerClient.tickCount % 20 == 0)
			{
				if (player != null && player.inventory.armorItemInSlot(3) != null && player.inventory.armorItemInSlot(3).getItem() instanceof GCCoreItemSensorGlasses)
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

								if (block != Blocks.air)
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

										List<Integer> metaList = Lists.newArrayList();
										metaList.add(metadata);

										for (BlockMetaList blockMetaList : ClientProxyCore.detectableBlocks)
										{
											if (blockMetaList.getBlock() == block)
											{
												metaList.addAll(blockMetaList.getMetaList());
												break;
											}
										}

										ClientProxyCore.detectableBlocks.add(new BlockMetaList(block, metaList));
									}
								}
							}
						}
					}
				}
			}

			if (GCCoreTickHandlerClient.addTabsNextTick)
			{
				if (minecraft.currentScreen.getClass().equals(GuiInventory.class))
				{
					GCCoreTickHandlerClient.addTabsToInventory((GuiContainer) minecraft.currentScreen);
				}

				GCCoreTickHandlerClient.addTabsNextTick = false;
			}

			if (minecraft.currentScreen != null && minecraft.currentScreen instanceof GuiMainMenu)
			{
				GalacticraftCore.playersServer.clear();
				GalacticraftCore.playersClient.clear();
				ClientProxyCore.playerItemData.clear();

				if (GCCoreTickHandlerClient.missingRequirementThread == null)
				{
					GCCoreTickHandlerClient.missingRequirementThread = new GCCoreThreadRequirementMissing(FMLCommonHandler.instance().getEffectiveSide());
					GCCoreTickHandlerClient.missingRequirementThread.start();
				}
			}

			if (world != null && GCCoreTickHandlerClient.checkedVersion)
			{
				GCCoreUtil.checkVersion(Side.CLIENT);
				GCCoreTickHandlerClient.checkedVersion = false;
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
						world.provider.setSkyRenderer(new GCCoreSkyProviderOverworld());
					}
					else if (world.provider.getSkyRenderer() != null && world.provider.getSkyRenderer() instanceof GCCoreSkyProviderOverworld && (player.ridingEntity == null || player.ridingEntity.posY < 200))
					{
						world.provider.setSkyRenderer(null);
					}
				}
				else if (world.provider instanceof GCCoreWorldProviderSpaceStation)
				{
					if (world.provider.getSkyRenderer() == null)
					{
						world.provider.setSkyRenderer(new GCCoreSkyProviderOrbit(new ResourceLocation(GalacticraftCore.ASSET_DOMAIN, "textures/gui/planets/overworld.png"), true, true));
					}
	
					if (world.provider.getCloudRenderer() == null)
					{
						world.provider.setCloudRenderer(new GCCoreCloudRenderer());
					}
				}
				else if (world.provider instanceof GCMoonWorldProvider)
				{
					if (world.provider.getSkyRenderer() == null)
					{
						world.provider.setSkyRenderer(new GCMoonSkyProvider());
					}
	
					if (world.provider.getCloudRenderer() == null)
					{
						world.provider.setCloudRenderer(new GCCoreCloudRenderer());
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
				for (int i = 0; i < world.loadedEntityList.size(); i++)
				{
					final Entity e = (Entity) world.loadedEntityList.get(i);

					if (e != null)
					{
						if (e instanceof EntityTier1Rocket)
						{
							final EntityTier1Rocket eship = (EntityTier1Rocket) e;

							if (eship.rocketSoundUpdater == null)
							{
								// TODO
//								eship.rocketSoundUpdater = new GCCoreSoundUpdaterSpaceship(FMLClientHandler.instance().getClient().sndManager, eship, FMLClientHandler.instance().getClient().thePlayer);
							}
						}
					}
				}
			}

			if (FMLClientHandler.instance().getClient().currentScreen instanceof GCCoreGuiChoosePlanet)
			{
				player.motionY = 0;
			}

			if (world != null && world.provider instanceof IGalacticraftWorldProvider)
			{
				world.setRainStrength(0.0F);
			}

			if (!minecraft.gameSettings.keyBindJump.getIsKeyPressed())
			{
				ClientProxyCore.lastSpacebarDown = false;
			}

			if (player != null && player.ridingEntity != null && minecraft.gameSettings.keyBindJump.getIsKeyPressed() && !ClientProxyCore.lastSpacebarDown)
			{
				GalacticraftCore.packetPipeline.sendToServer(new PacketSimple(EnumSimplePacket.S_IGNITE_ROCKET, new Object[] { }));
				ClientProxyCore.lastSpacebarDown = true;
			}
		}
		else
		{
			boolean invKeyPressed = Keyboard.isKeyDown(minecraft.gameSettings.keyBindInventory.getKeyCode());

			if (!GCCoreTickHandlerClient.lastInvKeyPressed && invKeyPressed && minecraft.currentScreen != null && minecraft.currentScreen.getClass() == GuiInventory.class)
			{
				GCCoreTickHandlerClient.addTabsToInventory((GuiContainer) minecraft.currentScreen);
			}

			GCCoreTickHandlerClient.lastInvKeyPressed = invKeyPressed;
		}		
	}

	private boolean alreadyContainsBlock(int x1, int y1, int z1)
	{
		return ClientProxyCore.valueableBlocks.contains(new Vector3(x1, y1, z1));
	}

	public static void zoom(float value)
	{
		try
		{
			ObfuscationReflectionHelper.setPrivateValue(EntityRenderer.class, FMLClientHandler.instance().getClient().entityRenderer, value, 15);
			ObfuscationReflectionHelper.setPrivateValue(EntityRenderer.class, FMLClientHandler.instance().getClient().entityRenderer, value, 16);
		}
		catch (final Exception ex)
		{
			ex.printStackTrace();
		}
	}

	public static void addTabsToInventory(GuiContainer gui)
	{
		boolean tConstructLoaded = Loader.isModLoaded("TConstruct");

		if (!tConstructLoaded)
		{
			if (!GCCoreTickHandlerClient.addTabsNextTick)
			{
				GCCoreTickHandlerClient.addTabsNextTick = true;
				return;
			}

			TabRegistry.addTabsToInventory(gui);
		}
	}
}
