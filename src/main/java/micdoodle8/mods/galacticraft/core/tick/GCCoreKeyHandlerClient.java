package micdoodle8.mods.galacticraft.core.tick;

import micdoodle8.mods.galacticraft.api.prefab.entity.EntityAutoRocket;
import micdoodle8.mods.galacticraft.api.prefab.entity.EntitySpaceshipBase;
import micdoodle8.mods.galacticraft.core.GCCoreConfigManager;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.client.KeyHandler;
import micdoodle8.mods.galacticraft.core.entities.GCCoreEntityBuggy;
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
    private static Minecraft mc;

	public GCCoreKeyHandlerClient()
	{
		super(new KeyBinding[] { GCCoreKeyHandlerClient.galaxyMap, GCCoreKeyHandlerClient.openFuelGui, GCCoreKeyHandlerClient.toggleAdvGoggles }, new boolean[] { false, false, false }, getVanillaKeyBindings(), new boolean[] { false, true, true, true, true, true, true });
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
			else if (kb.getKeyCode() == GCCoreKeyHandlerClient.openFuelGui.getKeyCode())
			{
				if (playerBase.ridingEntity instanceof EntitySpaceshipBase || playerBase.ridingEntity instanceof GCCoreEntityBuggy)
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

		Entity entityTest = this.mc.thePlayer.ridingEntity;
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

		if (entityTest != null && entityTest instanceof IControllableEntity && keyNum != -1)
		{
			IControllableEntity entity = (IControllableEntity) entityTest;

			if (kb.getKeyCode() == mc.gameSettings.keyBindInventory.getKeyCode())
			{
				KeyBinding.setKeyBindState(mc.gameSettings.keyBindInventory.getKeyCode(), false);
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

	@Override
	public void keyUp(Type types, KeyBinding kb, boolean tickEnd)
	{
	}
}
