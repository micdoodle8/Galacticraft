package micdoodle8.mods.galacticraft.core.tick;

import micdoodle8.mods.galacticraft.api.prefab.entity.EntitySpaceshipBase;
import micdoodle8.mods.galacticraft.core.GCCoreConfigManager;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.client.KeyHandler;
import micdoodle8.mods.galacticraft.core.entities.GCCoreEntityBuggy;
import micdoodle8.mods.galacticraft.core.entities.player.GCCorePlayerSP;
import micdoodle8.mods.galacticraft.core.util.PlayerUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
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
	public static KeyBinding openSpaceshipInv = new KeyBinding(StatCollector.translateToLocal("keybind.spaceshipinv.name"), Keyboard.KEY_F, "");
	public static KeyBinding toggleAdvGoggles = new KeyBinding(StatCollector.translateToLocal("keybind.sensortoggle.name"), Keyboard.KEY_K, "");
	public static KeyBinding accelerateKey;
	public static KeyBinding decelerateKey;
	public static KeyBinding leftKey;
	public static KeyBinding rightKey;
	public static KeyBinding spaceKey;
	public static KeyBinding leftShiftKey;
    private static KeyBinding invKey;
    private static Minecraft mc;

	public GCCoreKeyHandlerClient()
	{
		super(new KeyBinding[] { GCCoreKeyHandlerClient.galaxyMap, GCCoreKeyHandlerClient.openSpaceshipInv, GCCoreKeyHandlerClient.toggleAdvGoggles }, new boolean[] { false, false, false, true, true, true, true, true, true }, getVanillaKeyBindings(), new boolean[] { false, true, true, true, true, true, true });
	}

    private static KeyBinding[] getVanillaKeyBindings ()
    {
        mc = Minecraft.getMinecraft();
        invKey = mc.gameSettings.keyBindInventory;
        accelerateKey = mc.gameSettings.keyBindForward;
        decelerateKey = mc.gameSettings.keyBindBack;
        leftKey = mc.gameSettings.keyBindLeft;
        rightKey = mc.gameSettings.keyBindRight;
        spaceKey = mc.gameSettings.keyBindJump;
        leftShiftKey = mc.gameSettings.keyBindSneak;
        return new KeyBinding[] { invKey, accelerateKey, decelerateKey, leftKey, rightKey, spaceKey, leftShiftKey };
    }

	@Override
	public void keyDown(Type types, KeyBinding kb, boolean tickEnd, boolean isRepeat) 
	{
		if (mc.thePlayer != null && tickEnd)
		{
			GCCorePlayerSP playerBase = PlayerUtil.getPlayerBaseClientFromPlayer(mc.thePlayer);
			
			if (kb.getKeyCode() == GCCoreKeyHandlerClient.galaxyMap.getKeyCode())
			{
				if (mc.currentScreen == null)
				{
					mc.thePlayer.openGui(GalacticraftCore.instance, GCCoreConfigManager.idGuiGalaxyMap, mc.theWorld, (int) mc.thePlayer.posX, (int) mc.thePlayer.posY, (int) mc.thePlayer.posZ);
				}
			}
			else if (kb.getKeyCode() == GCCoreKeyHandlerClient.openSpaceshipInv.getKeyCode())
			{
				if (playerBase.ridingEntity instanceof EntitySpaceshipBase || playerBase.ridingEntity instanceof GCCoreEntityBuggy)
				{
					final Object[] toSend = { playerBase.getGameProfile().getName() };
//					PacketDispatcher.sendPacketToServer(PacketUtil.createPacket(GalacticraftCore.CHANNEL, playerBase.ridingEntity instanceof EntitySpaceshipBase ? EnumPacketServer.OPEN_SPACESHIP_INV : playerBase.ridingEntity instanceof GCCoreEntityBuggy ? EnumPacketServer.OPEN_BUGGY_INV : null, toSend));
				}

				if (playerBase.ridingEntity instanceof EntitySpaceshipBase)
				{
					playerBase.openGui(GalacticraftCore.instance, GCCoreConfigManager.idGuiSpaceshipInventory, mc.theWorld, (int) playerBase.posX, (int) playerBase.posY, (int) playerBase.posZ);
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

		

//		if (mc.currentScreen != null || tickEnd) TODO Fix other keybinds
//		{
//			return;
//		}
//
//		int keyNum = -1;
//		boolean handled = true;
//
//		if (kb == GCCoreKeyHandlerClient.accelerateKey)
//		{
//			keyNum = 0;
//		}
//		else if (kb == GCCoreKeyHandlerClient.decelerateKey)
//		{
//			keyNum = 1;
//		}
//		else if (kb == GCCoreKeyHandlerClient.leftKey)
//		{
//			keyNum = 2;
//		}
//		else if (kb == GCCoreKeyHandlerClient.rightKey)
//		{
//			keyNum = 3;
//		}
//		else if (kb == GCCoreKeyHandlerClient.spaceKey)
//		{
//			keyNum = 4;
//		}
//		else if (kb == GCCoreKeyHandlerClient.leftShiftKey)
//		{
//			keyNum = 5;
//		}
//		else
//		{
//			handled = false;
//		}
//
//		final Entity entityTest = player.ridingEntity;
//
//		if (entityTest != null && entityTest instanceof IControllableEntity && handled == true)
//		{
//			IControllableEntity entity = (IControllableEntity) entityTest;
//
//			if (kb.getKeyCode() == mc.gameSettings.keyBindInventory.getKeyCode())
//			{
//				KeyBinding.setKeyBindState(mc.gameSettings.keyBindInventory.getKeyCode(), false);
//			}
//
//			handled = entity.pressKey(keyNum);
//		}
//		else if (entityTest != null && entityTest instanceof EntityAutoRocket && handled == true)
//		{
//			EntityAutoRocket autoRocket = (EntityAutoRocket) entityTest;
//
//			if (autoRocket.landing)
//			{
//				if (kb == GCCoreKeyHandlerClient.leftShiftKey)
//				{
//					autoRocket.motionY -= 0.02D;
//					Object[] toSend = { autoRocket.entityId, false };
//					PacketDispatcher.sendPacketToServer(PacketUtil.createPacket(GalacticraftCore.CHANNEL, EnumPacketServer.UPDATE_SHIP_MOTION_Y, toSend));
//					handled = true;
//				}
//
//				if (kb == GCCoreKeyHandlerClient.spaceKey)
//				{
//					autoRocket.motionY += 0.02D;
//					Object[] toSend = { autoRocket.entityId, true };
//					PacketDispatcher.sendPacketToServer(PacketUtil.createPacket(GalacticraftCore.CHANNEL, EnumPacketServer.UPDATE_SHIP_MOTION_Y, toSend));
//					handled = true;
//				}
//			}
//			else
//			{
//				handled = false;
//			}
//		}
//		else
//		{
//			handled = false;
//		}
//
//		if (handled == true)
//		{
//			return;
//		}
//
//		for (final KeyBinding key : mc.gameSettings.keyBindings)
//		{
//			if (kb.getKeyCode() == key.getKeyCode() && key != kb)
//			{
//				KeyBinding.setKeyBindState(key.getKeyCode(), true);
//				KeyBinding.onTick(key.getKeyCode());
//			}
//		}
	}

	@Override
	public void keyUp(Type types, KeyBinding kb, boolean tickEnd)
	{
//		if (tickEnd)
//		{
//			return;
//		}
//
//		for (final KeyBinding key : FMLClientHandler.instance().getClient().gameSettings.keyBindings)
//		{
//			if (kb.getKeyCode() == key.getKeyCode() && key != kb)
//			{
//				KeyBinding.setKeyBindState(key.getKeyCode(), false);
//			}
//		}
	}
}
