package micdoodle8.mods.galacticraft.core.tick;

import java.util.EnumSet;

import micdoodle8.mods.galacticraft.api.prefab.entity.EntityAutoRocket;
import micdoodle8.mods.galacticraft.api.prefab.entity.EntitySpaceshipBase;
import micdoodle8.mods.galacticraft.core.GCCoreConfigManager;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.entities.GCCoreEntityBuggy;
import micdoodle8.mods.galacticraft.core.entities.IControllableEntity;
import micdoodle8.mods.galacticraft.core.entities.player.GCCorePlayerSP;
import micdoodle8.mods.galacticraft.core.network.GCCorePacketHandlerServer.EnumPacketServer;
import micdoodle8.mods.galacticraft.core.util.PacketUtil;
import micdoodle8.mods.galacticraft.core.util.PlayerUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.Entity;
import net.minecraft.util.StatCollector;

import org.lwjgl.input.Keyboard;

import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.client.registry.KeyBindingRegistry.KeyHandler;
import cpw.mods.fml.common.TickType;
import cpw.mods.fml.common.network.PacketDispatcher;

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
	public static KeyBinding galaxyMap = new KeyBinding(StatCollector.translateToLocal("keybind.map.name"), Keyboard.KEY_M);
	public static KeyBinding openSpaceshipInv = new KeyBinding(StatCollector.translateToLocal("keybind.spaceshipinv.name"), Keyboard.KEY_F);
	public static KeyBinding toggleAdvGoggles = new KeyBinding(StatCollector.translateToLocal("keybind.sensortoggle.name"), Keyboard.KEY_K);
	public static KeyBinding accelerateKey = new KeyBinding(StatCollector.translateToLocal("keybind.vehicleforward.name"), Keyboard.KEY_W);
	public static KeyBinding decelerateKey = new KeyBinding(StatCollector.translateToLocal("keybind.vehiclebackward.name"), Keyboard.KEY_S);
	public static KeyBinding leftKey = new KeyBinding(StatCollector.translateToLocal("keybind.vehicleleft.name"), Keyboard.KEY_A);
	public static KeyBinding rightKey = new KeyBinding(StatCollector.translateToLocal("keybind.vehicleright.name"), Keyboard.KEY_D);
	public static KeyBinding spaceKey = new KeyBinding(StatCollector.translateToLocal("keybind.vehicleup.name"), Keyboard.KEY_SPACE);
	public static KeyBinding leftShiftKey = new KeyBinding(StatCollector.translateToLocal("keybind.vehicledown.name"), Keyboard.KEY_LSHIFT);

	public GCCoreKeyHandlerClient()
	{
		super(new KeyBinding[] { GCCoreKeyHandlerClient.galaxyMap, GCCoreKeyHandlerClient.openSpaceshipInv, GCCoreKeyHandlerClient.toggleAdvGoggles, GCCoreKeyHandlerClient.accelerateKey, GCCoreKeyHandlerClient.decelerateKey, GCCoreKeyHandlerClient.leftKey, GCCoreKeyHandlerClient.rightKey, GCCoreKeyHandlerClient.spaceKey, GCCoreKeyHandlerClient.leftShiftKey }, new boolean[] { false, false, false, true, true, true, true, true, true });
	}

	@Override
	public String getLabel()
	{
		return "Galacticraft Keybinds";
	}

	@Override
	public void keyDown(EnumSet<TickType> types, KeyBinding kb, boolean tickEnd, boolean isRepeat)
	{
		final Minecraft minecraft = FMLClientHandler.instance().getClient();

		final EntityPlayerSP player = minecraft.thePlayer;

		if (player == null)
		{
			return;
		}

		final GCCorePlayerSP playerBase = PlayerUtil.getPlayerBaseClientFromPlayer(player, false);

		if (minecraft.currentScreen != null || tickEnd)
		{
			return;
		}

		if (kb.keyCode == GCCoreKeyHandlerClient.galaxyMap.keyCode)
		{
			if (minecraft.currentScreen == null)
			{
				player.openGui(GalacticraftCore.instance, GCCoreConfigManager.idGuiGalaxyMap, minecraft.theWorld, (int) player.posX, (int) player.posY, (int) player.posZ);
			}
		}
		else if (kb.keyCode == GCCoreKeyHandlerClient.openSpaceshipInv.keyCode)
		{
			if (player.ridingEntity instanceof EntitySpaceshipBase || player.ridingEntity instanceof GCCoreEntityBuggy)
			{
				final Object[] toSend = { player.username };
				PacketDispatcher.sendPacketToServer(PacketUtil.createPacket(GalacticraftCore.CHANNEL, player.ridingEntity instanceof EntitySpaceshipBase ? EnumPacketServer.OPEN_SPACESHIP_INV : player.ridingEntity instanceof GCCoreEntityBuggy ? EnumPacketServer.OPEN_BUGGY_INV : null, toSend));
			}

			if (player.ridingEntity instanceof EntitySpaceshipBase)
			{
				player.openGui(GalacticraftCore.instance, GCCoreConfigManager.idGuiSpaceshipInventory, minecraft.theWorld, (int) player.posX, (int) player.posY, (int) player.posZ);
			}
		}
		else if (kb.keyCode == GCCoreKeyHandlerClient.toggleAdvGoggles.keyCode)
		{
			if (playerBase != null)
			{
				playerBase.toggleGoggles();
			}
		}

		if (minecraft.currentScreen != null || tickEnd)
		{
			return;
		}

		int keyNum = -1;
		boolean handled = true;

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
		else
		{
			handled = false;
		}

		final Entity entityTest = player.ridingEntity;

		if (entityTest != null && entityTest instanceof IControllableEntity && handled == true)
		{
			IControllableEntity entity = (IControllableEntity) entityTest;

			if (kb.keyCode == minecraft.gameSettings.keyBindInventory.keyCode)
			{
				minecraft.gameSettings.keyBindInventory.pressed = false;
				minecraft.gameSettings.keyBindInventory.pressTime = 0;
			}

			handled = entity.pressKey(keyNum);
		}
		else if (entityTest != null && entityTest instanceof EntityAutoRocket && handled == true)
		{
			EntityAutoRocket autoRocket = (EntityAutoRocket) entityTest;

			if (autoRocket.landing)
			{
				if (kb == GCCoreKeyHandlerClient.leftShiftKey)
				{
					autoRocket.motionY -= 0.02D;
					Object[] toSend = { autoRocket.entityId, false };
					PacketDispatcher.sendPacketToServer(PacketUtil.createPacket(GalacticraftCore.CHANNEL, EnumPacketServer.UPDATE_SHIP_MOTION_Y, toSend));
					handled = true;
				}

				if (kb == GCCoreKeyHandlerClient.spaceKey)
				{
					autoRocket.motionY += 0.02D;
					Object[] toSend = { autoRocket.entityId, true };
					PacketDispatcher.sendPacketToServer(PacketUtil.createPacket(GalacticraftCore.CHANNEL, EnumPacketServer.UPDATE_SHIP_MOTION_Y, toSend));
					handled = true;
				}
			}
			else
			{
				handled = false;
			}
		}
		else
		{
			handled = false;
		}

		if (handled == true)
		{
			return;
		}

		for (final KeyBinding key : minecraft.gameSettings.keyBindings)
		{
			if (kb.keyCode == key.keyCode && key != kb)
			{
				key.pressed = true;
				key.pressTime = 1;
			}
		}
	}

	@Override
	public void keyUp(EnumSet<TickType> types, KeyBinding kb, boolean tickEnd)
	{
		if (tickEnd)
		{
			return;
		}

		for (final KeyBinding key : FMLClientHandler.instance().getClient().gameSettings.keyBindings)
		{
			if (kb.keyCode == key.keyCode && key != kb)
			{
				key.pressed = false;
			}
		}
	}

	@Override
	public EnumSet<TickType> ticks()
	{
		return EnumSet.of(TickType.CLIENT);
	}
}
