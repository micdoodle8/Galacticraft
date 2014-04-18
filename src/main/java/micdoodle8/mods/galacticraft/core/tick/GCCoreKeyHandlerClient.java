package micdoodle8.mods.galacticraft.core.tick;

import micdoodle8.mods.galacticraft.api.prefab.entity.EntityAutoRocket;
import micdoodle8.mods.galacticraft.api.prefab.entity.EntitySpaceshipBase;
import micdoodle8.mods.galacticraft.core.GCCoreConfigManager;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.client.KeyHandler;
import micdoodle8.mods.galacticraft.core.entities.EntityBuggy;
import micdoodle8.mods.galacticraft.core.entities.IControllableEntity;
import micdoodle8.mods.galacticraft.core.entities.player.GCCorePlayerSP;
import micdoodle8.mods.galacticraft.core.network.PacketSimple;
import micdoodle8.mods.galacticraft.core.network.PacketSimple.EnumSimplePacket;
import micdoodle8.mods.galacticraft.core.util.PlayerUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.Entity;
import net.minecraft.util.StatCollector;

import org.lwjgl.input.Keyboard;

import cpw.mods.fml.common.gameevent.TickEvent.Type;

/**
 * GCCoreKeyHandlerClient.java
 * 
 * This file is part of the Galacticraft project
 * 
 * @author micdoodle8
 * @license Lesser GNU Public License v3 (http://www.gnu.org/licenses/lgpl.html)
 * 
 */
public class GCCoreKeyHandlerClient extends KeyHandler
{
	public static KeyBinding galaxyMap = new KeyBinding(StatCollector.translateToLocal("keybind.map.name"), Keyboard.KEY_M, "");
	public static KeyBinding openFuelGui = new KeyBinding(StatCollector.translateToLocal("keybind.spaceshipinv.name"), Keyboard.KEY_F, "");
	public static KeyBinding toggleAdvGoggles = new KeyBinding(StatCollector.translateToLocal("keybind.sensortoggle.name"), Keyboard.KEY_K, "");
	public static KeyBinding accelerateKey;
	public static KeyBinding decelerateKey;
	public static KeyBinding leftKey;
	public static KeyBinding rightKey;
	public static KeyBinding spaceKey;
	public static KeyBinding leftShiftKey;
	private static KeyBinding invKey;
	private static Minecraft mc = Minecraft.getMinecraft();

	public GCCoreKeyHandlerClient()
	{
		super(new KeyBinding[] { GCCoreKeyHandlerClient.galaxyMap, GCCoreKeyHandlerClient.openFuelGui, GCCoreKeyHandlerClient.toggleAdvGoggles }, new boolean[] { false, false, false }, GCCoreKeyHandlerClient.getVanillaKeyBindings(), new boolean[] { false, true, true, true, true, true, true });
	}

	private static KeyBinding[] getVanillaKeyBindings()
	{
		GCCoreKeyHandlerClient.invKey = GCCoreKeyHandlerClient.mc.gameSettings.keyBindInventory;
		GCCoreKeyHandlerClient.accelerateKey = GCCoreKeyHandlerClient.mc.gameSettings.keyBindForward;
		GCCoreKeyHandlerClient.decelerateKey = GCCoreKeyHandlerClient.mc.gameSettings.keyBindBack;
		GCCoreKeyHandlerClient.leftKey = GCCoreKeyHandlerClient.mc.gameSettings.keyBindLeft;
		GCCoreKeyHandlerClient.rightKey = GCCoreKeyHandlerClient.mc.gameSettings.keyBindRight;
		GCCoreKeyHandlerClient.spaceKey = GCCoreKeyHandlerClient.mc.gameSettings.keyBindJump;
		GCCoreKeyHandlerClient.leftShiftKey = GCCoreKeyHandlerClient.mc.gameSettings.keyBindSneak;
		return new KeyBinding[] { GCCoreKeyHandlerClient.invKey, GCCoreKeyHandlerClient.accelerateKey, GCCoreKeyHandlerClient.decelerateKey, GCCoreKeyHandlerClient.leftKey, GCCoreKeyHandlerClient.rightKey, GCCoreKeyHandlerClient.spaceKey, GCCoreKeyHandlerClient.leftShiftKey };
	}

	@Override
	public void keyDown(Type types, KeyBinding kb, boolean tickEnd, boolean isRepeat)
	{
		if (GCCoreKeyHandlerClient.mc.thePlayer != null && tickEnd)
		{
			GCCorePlayerSP playerBase = PlayerUtil.getPlayerBaseClientFromPlayer(GCCoreKeyHandlerClient.mc.thePlayer, false);

			if (kb.getKeyCode() == GCCoreKeyHandlerClient.galaxyMap.getKeyCode())
			{
				if (GCCoreKeyHandlerClient.mc.currentScreen == null)
				{
					GCCoreKeyHandlerClient.mc.thePlayer.openGui(GalacticraftCore.instance, GCCoreConfigManager.idGuiGalaxyMap, GCCoreKeyHandlerClient.mc.theWorld, (int) GCCoreKeyHandlerClient.mc.thePlayer.posX, (int) GCCoreKeyHandlerClient.mc.thePlayer.posY, (int) GCCoreKeyHandlerClient.mc.thePlayer.posZ);
				}
			}
			else if (kb.getKeyCode() == GCCoreKeyHandlerClient.openFuelGui.getKeyCode())
			{
				if (playerBase.ridingEntity instanceof EntitySpaceshipBase || playerBase.ridingEntity instanceof EntityBuggy)
				{
					GalacticraftCore.packetPipeline.sendToServer(new PacketSimple(EnumSimplePacket.S_OPEN_FUEL_GUI, new Object[] { playerBase.getGameProfile().getName() }));
				}
			}
			else if (kb.getKeyCode() == GCCoreKeyHandlerClient.toggleAdvGoggles.getKeyCode())
			{
				if (playerBase != null)
				{
					playerBase.toggleGoggles();
				}
			}
		}

		if (GCCoreKeyHandlerClient.mc.thePlayer != null)
		{
			int keyNum = -1;
	
			if (kb == GCCoreKeyHandlerClient.accelerateKey)
			{
				keyNum = 0;
			}
			else if (kb == GCCoreKeyHandlerClient.decelerateKey)
			{
				keyNum = 1;
			}
			else if (kb == GCCoreKeyHandlerClient.leftKey)
			{
				keyNum = 2;
			}
			else if (kb == GCCoreKeyHandlerClient.rightKey)
			{
				keyNum = 3;
			}
			else if (kb == GCCoreKeyHandlerClient.spaceKey)
			{
				keyNum = 4;
			}
			else if (kb == GCCoreKeyHandlerClient.leftShiftKey)
			{
				keyNum = 5;
			}
	
			Entity entityTest = GCCoreKeyHandlerClient.mc.thePlayer.ridingEntity;
			if (entityTest != null && entityTest instanceof IControllableEntity && keyNum != -1)
			{
				IControllableEntity entity = (IControllableEntity) entityTest;
	
				if (kb.getKeyCode() == GCCoreKeyHandlerClient.mc.gameSettings.keyBindInventory.getKeyCode())
				{
					KeyBinding.setKeyBindState(GCCoreKeyHandlerClient.mc.gameSettings.keyBindInventory.getKeyCode(), false);
				}
	
				entity.pressKey(keyNum);
			}
			else if (entityTest != null && entityTest instanceof EntityAutoRocket)
			{
				EntityAutoRocket autoRocket = (EntityAutoRocket) entityTest;
	
				if (autoRocket.landing)
				{
					if (kb == GCCoreKeyHandlerClient.leftShiftKey)
					{
						autoRocket.motionY -= 0.02D;
						GalacticraftCore.packetPipeline.sendToServer(new PacketSimple(EnumSimplePacket.S_UPDATE_SHIP_MOTION_Y, new Object[] { autoRocket.getEntityId(), false }));
					}
	
					if (kb == GCCoreKeyHandlerClient.spaceKey)
					{
						autoRocket.motionY += 0.02D;
						GalacticraftCore.packetPipeline.sendToServer(new PacketSimple(EnumSimplePacket.S_UPDATE_SHIP_MOTION_Y, new Object[] { autoRocket.getEntityId(), true }));
					}
				}
			}
		}
	}

	@Override
	public void keyUp(Type types, KeyBinding kb, boolean tickEnd)
	{
	}
}