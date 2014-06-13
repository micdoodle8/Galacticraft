package micdoodle8.mods.galacticraft.core.tick;

import micdoodle8.mods.galacticraft.api.prefab.entity.EntityAutoRocket;
import micdoodle8.mods.galacticraft.api.prefab.entity.EntitySpaceshipBase;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.client.KeyHandler;
import micdoodle8.mods.galacticraft.core.client.gui.GuiIdsCore;
import micdoodle8.mods.galacticraft.core.entities.EntityBuggy;
import micdoodle8.mods.galacticraft.core.entities.IControllableEntity;
import micdoodle8.mods.galacticraft.core.entities.player.GCEntityClientPlayerMP;
import micdoodle8.mods.galacticraft.core.network.PacketSimple;
import micdoodle8.mods.galacticraft.core.network.PacketSimple.EnumSimplePacket;
import micdoodle8.mods.galacticraft.core.util.ConfigManagerCore;
import micdoodle8.mods.galacticraft.core.util.PlayerUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.Entity;
import net.minecraft.util.StatCollector;

import org.lwjgl.input.Keyboard;

import cpw.mods.fml.common.gameevent.TickEvent.Type;



public class KeyHandlerClient extends KeyHandler
{
	public static KeyBinding galaxyMap = new KeyBinding(StatCollector.translateToLocal("keybind.map.name"), Keyboard.KEY_M, "Galacticraft");
	public static KeyBinding openFuelGui = new KeyBinding(StatCollector.translateToLocal("keybind.spaceshipinv.name"), Keyboard.KEY_F, "Galacticraft");
	public static KeyBinding toggleAdvGoggles = new KeyBinding(StatCollector.translateToLocal("keybind.sensortoggle.name"), Keyboard.KEY_K, "Galacticraft");
	public static KeyBinding accelerateKey;
	public static KeyBinding decelerateKey;
	public static KeyBinding leftKey;
	public static KeyBinding rightKey;
	public static KeyBinding spaceKey;
	public static KeyBinding leftShiftKey;
	private static KeyBinding invKey;
	private static Minecraft mc = Minecraft.getMinecraft();

	public KeyHandlerClient()
	{
		super(new KeyBinding[] { KeyHandlerClient.galaxyMap, KeyHandlerClient.openFuelGui, KeyHandlerClient.toggleAdvGoggles }, new boolean[] { false, false, false }, KeyHandlerClient.getVanillaKeyBindings(), new boolean[] { false, true, true, true, true, true, true });
	}

	private static KeyBinding[] getVanillaKeyBindings()
	{
		KeyHandlerClient.invKey = KeyHandlerClient.mc.gameSettings.keyBindInventory;
		KeyHandlerClient.accelerateKey = KeyHandlerClient.mc.gameSettings.keyBindForward;
		KeyHandlerClient.decelerateKey = KeyHandlerClient.mc.gameSettings.keyBindBack;
		KeyHandlerClient.leftKey = KeyHandlerClient.mc.gameSettings.keyBindLeft;
		KeyHandlerClient.rightKey = KeyHandlerClient.mc.gameSettings.keyBindRight;
		KeyHandlerClient.spaceKey = KeyHandlerClient.mc.gameSettings.keyBindJump;
		KeyHandlerClient.leftShiftKey = KeyHandlerClient.mc.gameSettings.keyBindSneak;
		return new KeyBinding[] { KeyHandlerClient.invKey, KeyHandlerClient.accelerateKey, KeyHandlerClient.decelerateKey, KeyHandlerClient.leftKey, KeyHandlerClient.rightKey, KeyHandlerClient.spaceKey, KeyHandlerClient.leftShiftKey };
	}

	@Override
	public void keyDown(Type types, KeyBinding kb, boolean tickEnd, boolean isRepeat)
	{
		if (KeyHandlerClient.mc.thePlayer != null && tickEnd)
		{
			GCEntityClientPlayerMP playerBase = PlayerUtil.getPlayerBaseClientFromPlayer(KeyHandlerClient.mc.thePlayer, false);

			if (kb.getKeyCode() == KeyHandlerClient.galaxyMap.getKeyCode())
			{
				if (KeyHandlerClient.mc.currentScreen == null)
				{
					KeyHandlerClient.mc.thePlayer.openGui(GalacticraftCore.instance, GuiIdsCore.GALAXY_MAP, KeyHandlerClient.mc.theWorld, (int) KeyHandlerClient.mc.thePlayer.posX, (int) KeyHandlerClient.mc.thePlayer.posY, (int) KeyHandlerClient.mc.thePlayer.posZ);
				}
			}
			else if (kb.getKeyCode() == KeyHandlerClient.openFuelGui.getKeyCode())
			{
				if (playerBase.ridingEntity instanceof EntitySpaceshipBase || playerBase.ridingEntity instanceof EntityBuggy)
				{
					GalacticraftCore.packetPipeline.sendToServer(new PacketSimple(EnumSimplePacket.S_OPEN_FUEL_GUI, new Object[] { playerBase.getGameProfile().getName() }));
				}
			}
			else if (kb.getKeyCode() == KeyHandlerClient.toggleAdvGoggles.getKeyCode())
			{
				if (playerBase != null)
				{
					playerBase.toggleGoggles();
				}
			}
		}

		if (KeyHandlerClient.mc.thePlayer != null)
		{
			int keyNum = -1;
	
			if (kb == KeyHandlerClient.accelerateKey)
			{
				keyNum = 0;
			}
			else if (kb == KeyHandlerClient.decelerateKey)
			{
				keyNum = 1;
			}
			else if (kb == KeyHandlerClient.leftKey)
			{
				keyNum = 2;
			}
			else if (kb == KeyHandlerClient.rightKey)
			{
				keyNum = 3;
			}
			else if (kb == KeyHandlerClient.spaceKey)
			{
				keyNum = 4;
			}
			else if (kb == KeyHandlerClient.leftShiftKey)
			{
				keyNum = 5;
			}
	
			Entity entityTest = KeyHandlerClient.mc.thePlayer.ridingEntity;
			if (entityTest != null && entityTest instanceof IControllableEntity && keyNum != -1)
			{
				IControllableEntity entity = (IControllableEntity) entityTest;
	
				if (kb.getKeyCode() == KeyHandlerClient.mc.gameSettings.keyBindInventory.getKeyCode())
				{
					KeyBinding.setKeyBindState(KeyHandlerClient.mc.gameSettings.keyBindInventory.getKeyCode(), false);
				}
	
				entity.pressKey(keyNum);
			}
			else if (entityTest != null && entityTest instanceof EntityAutoRocket)
			{
				EntityAutoRocket autoRocket = (EntityAutoRocket) entityTest;
	
				if (autoRocket.landing)
				{
					if (kb == KeyHandlerClient.leftShiftKey)
					{
						autoRocket.motionY -= 0.02D;
						GalacticraftCore.packetPipeline.sendToServer(new PacketSimple(EnumSimplePacket.S_UPDATE_SHIP_MOTION_Y, new Object[] { autoRocket.getEntityId(), false }));
					}
	
					if (kb == KeyHandlerClient.spaceKey)
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