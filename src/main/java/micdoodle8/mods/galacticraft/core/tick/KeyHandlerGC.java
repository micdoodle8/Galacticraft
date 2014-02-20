package micdoodle8.mods.galacticraft.core.tick;

import micdoodle8.mods.galacticraft.api.prefab.entity.EntityAutoRocket;
import micdoodle8.mods.galacticraft.api.prefab.entity.EntitySpaceshipBase;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.client.KeyHandler;
import micdoodle8.mods.galacticraft.core.entities.EntityBuggy;
import micdoodle8.mods.galacticraft.core.entities.IControllableEntity;
import micdoodle8.mods.galacticraft.core.entities.player.GCEntityClientPlayerMP;
import micdoodle8.mods.galacticraft.core.network.PacketSimple;
import micdoodle8.mods.galacticraft.core.network.PacketSimple.EnumSimplePacket;
import micdoodle8.mods.galacticraft.core.util.GCConfigManager;
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
public class KeyHandlerGC extends KeyHandler
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

	public KeyHandlerGC()
	{
		super(new KeyBinding[] { KeyHandlerGC.galaxyMap, KeyHandlerGC.openFuelGui, KeyHandlerGC.toggleAdvGoggles }, new boolean[] { false, false, false }, KeyHandlerGC.getVanillaKeyBindings(), new boolean[] { false, true, true, true, true, true, true });
	}

	private static KeyBinding[] getVanillaKeyBindings()
	{
		KeyHandlerGC.invKey = KeyHandlerGC.mc.gameSettings.keyBindInventory;
		KeyHandlerGC.accelerateKey = KeyHandlerGC.mc.gameSettings.keyBindForward;
		KeyHandlerGC.decelerateKey = KeyHandlerGC.mc.gameSettings.keyBindBack;
		KeyHandlerGC.leftKey = KeyHandlerGC.mc.gameSettings.keyBindLeft;
		KeyHandlerGC.rightKey = KeyHandlerGC.mc.gameSettings.keyBindRight;
		KeyHandlerGC.spaceKey = KeyHandlerGC.mc.gameSettings.keyBindJump;
		KeyHandlerGC.leftShiftKey = KeyHandlerGC.mc.gameSettings.keyBindSneak;
		return new KeyBinding[] { KeyHandlerGC.invKey, KeyHandlerGC.accelerateKey, KeyHandlerGC.decelerateKey, KeyHandlerGC.leftKey, KeyHandlerGC.rightKey, KeyHandlerGC.spaceKey, KeyHandlerGC.leftShiftKey };
	}

	@Override
	public void keyDown(Type types, KeyBinding kb, boolean tickEnd, boolean isRepeat)
	{
		if (KeyHandlerGC.mc.thePlayer != null && tickEnd)
		{
			GCEntityClientPlayerMP playerBase = PlayerUtil.getPlayerBaseClientFromPlayer(KeyHandlerGC.mc.thePlayer);

			if (kb.getKeyCode() == KeyHandlerGC.galaxyMap.getKeyCode())
			{
				if (KeyHandlerGC.mc.currentScreen == null)
				{
					KeyHandlerGC.mc.thePlayer.openGui(GalacticraftCore.instance, GCConfigManager.idGuiGalaxyMap, KeyHandlerGC.mc.theWorld, (int) KeyHandlerGC.mc.thePlayer.posX, (int) KeyHandlerGC.mc.thePlayer.posY, (int) KeyHandlerGC.mc.thePlayer.posZ);
				}
			}
			else if (kb.getKeyCode() == KeyHandlerGC.openFuelGui.getKeyCode())
			{
				if (playerBase.ridingEntity instanceof EntitySpaceshipBase || playerBase.ridingEntity instanceof EntityBuggy)
				{
					GalacticraftCore.packetPipeline.sendToServer(new PacketSimple(EnumSimplePacket.S_OPEN_FUEL_GUI, new Object[] { playerBase.getGameProfile().getName() }));
				}
			}
			else if (kb.getKeyCode() == KeyHandlerGC.toggleAdvGoggles.getKeyCode())
			{
				if (playerBase != null)
				{
					playerBase.toggleGoggles();
				}
			}
		}

		if (KeyHandlerGC.mc.thePlayer != null)
		{
			int keyNum = -1;
	
			if (kb == KeyHandlerGC.accelerateKey)
			{
				keyNum = 0;
			}
			else if (kb == KeyHandlerGC.decelerateKey)
			{
				keyNum = 1;
			}
			else if (kb == KeyHandlerGC.leftKey)
			{
				keyNum = 2;
			}
			else if (kb == KeyHandlerGC.rightKey)
			{
				keyNum = 3;
			}
			else if (kb == KeyHandlerGC.spaceKey)
			{
				keyNum = 4;
			}
			else if (kb == KeyHandlerGC.leftShiftKey)
			{
				keyNum = 5;
			}
	
			Entity entityTest = KeyHandlerGC.mc.thePlayer.ridingEntity;
			if (entityTest != null && entityTest instanceof IControllableEntity && keyNum != -1)
			{
				IControllableEntity entity = (IControllableEntity) entityTest;
	
				if (kb.getKeyCode() == KeyHandlerGC.mc.gameSettings.keyBindInventory.getKeyCode())
				{
					KeyBinding.setKeyBindState(KeyHandlerGC.mc.gameSettings.keyBindInventory.getKeyCode(), false);
				}
	
				entity.pressKey(keyNum);
			}
			else if (entityTest != null && entityTest instanceof EntityAutoRocket)
			{
				EntityAutoRocket autoRocket = (EntityAutoRocket) entityTest;
	
				if (autoRocket.landing)
				{
					if (kb == KeyHandlerGC.leftShiftKey)
					{
						autoRocket.motionY -= 0.02D;
						GalacticraftCore.packetPipeline.sendToServer(new PacketSimple(EnumSimplePacket.S_UPDATE_SHIP_MOTION_Y, new Object[] { autoRocket.getEntityId(), false }));
					}
	
					if (kb == KeyHandlerGC.spaceKey)
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
