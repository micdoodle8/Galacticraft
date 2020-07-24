package micdoodle8.mods.galacticraft.core.tick;

import micdoodle8.mods.galacticraft.api.prefab.entity.EntityAutoRocket;
import micdoodle8.mods.galacticraft.api.prefab.entity.EntitySpaceshipBase;
import micdoodle8.mods.galacticraft.api.prefab.entity.EntitySpaceshipBase.EnumLaunchPhase;
import micdoodle8.mods.galacticraft.core.Constants;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.client.KeyHandler;
import micdoodle8.mods.galacticraft.core.entities.EntityBuggy;
import micdoodle8.mods.galacticraft.core.entities.IControllableEntity;
import micdoodle8.mods.galacticraft.core.entities.player.GCPlayerStatsClient;
import micdoodle8.mods.galacticraft.core.network.PacketSimple;
import micdoodle8.mods.galacticraft.core.network.PacketSimple.EnumSimplePacket;
import micdoodle8.mods.galacticraft.core.util.ConfigManagerCore;
import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
import micdoodle8.mods.galacticraft.core.util.PlayerUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.Entity;
import net.minecraftforge.event.TickEvent;
import org.lwjgl.glfw.GLFW;

public class KeyHandlerClient extends KeyHandler
{
    public static final KeyBinding galaxyMap;
    public static final KeyBinding openFuelGui;
    public static final KeyBinding toggleAdvGoggles;

    static
    {
        galaxyMap = new KeyBinding(GCCoreUtil.translate("keybind.map.name"), ConfigManagerCore.keyOverrideMapI == 0 ? GLFW.GLFW_KEY_M : ConfigManagerCore.keyOverrideMapI, Constants.MOD_NAME_SIMPLE);
        openFuelGui = new KeyBinding(GCCoreUtil.translate("keybind.spaceshipinv.name"), ConfigManagerCore.keyOverrideFuelLevelI == 0 ? GLFW.GLFW_KEY_F : ConfigManagerCore.keyOverrideFuelLevelI, Constants.MOD_NAME_SIMPLE);
        toggleAdvGoggles = new KeyBinding(GCCoreUtil.translate("keybind.sensortoggle.name"), ConfigManagerCore.keyOverrideToggleAdvGogglesI == 0 ? GLFW.GLFW_KEY_K : ConfigManagerCore.keyOverrideToggleAdvGogglesI, Constants.MOD_NAME_SIMPLE);
        // See ConfigManagerCore.class for actual defaults. These do nothing
    }

    public static KeyBinding accelerateKey;
    public static KeyBinding decelerateKey;
    public static KeyBinding leftKey;
    public static KeyBinding rightKey;
    public static KeyBinding upKey;
    public static KeyBinding downKey;
    public static KeyBinding spaceKey;
    public static KeyBinding leftShiftKey;
    private static final Minecraft mc = Minecraft.getInstance();

    public KeyHandlerClient()
    {
        super(new KeyBinding[]{KeyHandlerClient.galaxyMap, KeyHandlerClient.openFuelGui, KeyHandlerClient.toggleAdvGoggles}, new boolean[]{false, false, false}, KeyHandlerClient.getVanillaKeyBindings(), new boolean[]{false, true, true, true, true, true, true});
    }

    private static KeyBinding[] getVanillaKeyBindings()
    {
        KeyBinding invKey = KeyHandlerClient.mc.gameSettings.keyBindInventory;
        KeyHandlerClient.accelerateKey = KeyHandlerClient.mc.gameSettings.keyBindForward;
        KeyHandlerClient.decelerateKey = KeyHandlerClient.mc.gameSettings.keyBindBack;
        KeyHandlerClient.leftKey = KeyHandlerClient.mc.gameSettings.keyBindLeft;
        KeyHandlerClient.rightKey = KeyHandlerClient.mc.gameSettings.keyBindRight;
        KeyHandlerClient.upKey = KeyHandlerClient.mc.gameSettings.keyBindForward;
        KeyHandlerClient.downKey = KeyHandlerClient.mc.gameSettings.keyBindBack;
        KeyHandlerClient.spaceKey = KeyHandlerClient.mc.gameSettings.keyBindJump;
        KeyHandlerClient.leftShiftKey = KeyHandlerClient.mc.gameSettings.keyBindSneak;
        return new KeyBinding[]{invKey, KeyHandlerClient.accelerateKey, KeyHandlerClient.decelerateKey, KeyHandlerClient.leftKey, KeyHandlerClient.rightKey, KeyHandlerClient.spaceKey, KeyHandlerClient.leftShiftKey};
    }

    @Override
    public void keyDown(TickEvent.Type types, KeyBinding kb, boolean tickEnd, boolean isRepeat)
    {
        if (KeyHandlerClient.mc.player != null && tickEnd)
        {
            ClientPlayerEntity playerBase = PlayerUtil.getPlayerBaseClientFromPlayer(KeyHandlerClient.mc.player, false);

            if (playerBase == null)
            {
                return;
            }

            GCPlayerStatsClient stats = GCPlayerStatsClient.get(playerBase);

            if (kb == KeyHandlerClient.galaxyMap)
            {
                if (KeyHandlerClient.mc.currentScreen == null)
                {
//                    KeyHandlerClient.mc.player.openGui(GalacticraftCore.instance, GuiIdsCore.GALAXY_MAP, KeyHandlerClient.mc.world, (int) KeyHandlerClient.mc.player.getPosX(), (int) KeyHandlerClient.mc.player.getPosY(), (int) KeyHandlerClient.mc.player.getPosZ());
                    // TODO Gui
                }
            }
            else if (kb == KeyHandlerClient.openFuelGui)
            {
                if (playerBase.getRidingEntity() instanceof EntitySpaceshipBase || playerBase.getRidingEntity() instanceof EntityBuggy)
                {
                    GalacticraftCore.packetPipeline.sendToServer(new PacketSimple(EnumSimplePacket.S_OPEN_FUEL_GUI, GCCoreUtil.getDimensionType(mc.world), new Object[]{PlayerUtil.getName(playerBase)}));
                }
            }
            else if (kb == KeyHandlerClient.toggleAdvGoggles)
            {
                stats.setUsingAdvancedGoggles(!stats.isUsingAdvancedGoggles());
            }
        }

        if (KeyHandlerClient.mc.player != null && KeyHandlerClient.mc.currentScreen == null)
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

            Entity entityTest = KeyHandlerClient.mc.player.getRidingEntity();
            if (entityTest instanceof IControllableEntity && keyNum != -1)
            {
                IControllableEntity entity = (IControllableEntity) entityTest;

                if (kb == KeyHandlerClient.mc.gameSettings.keyBindInventory)
                {
                    KeyBinding.setKeyBindState(KeyHandlerClient.mc.gameSettings.keyBindInventory.getKey(), false);
                }

                entity.pressKey(keyNum);
            }
            else if (entityTest instanceof EntityAutoRocket)
            {
                EntityAutoRocket autoRocket = (EntityAutoRocket) entityTest;

                if (autoRocket.launchPhase == EnumLaunchPhase.LANDING.ordinal())
                {
                    if (kb == KeyHandlerClient.leftShiftKey)
                    {
                        autoRocket.setMotion(autoRocket.getMotion().x, autoRocket.getMotion().y - 0.02, autoRocket.getMotion().z);
                        GalacticraftCore.packetPipeline.sendToServer(new PacketSimple(EnumSimplePacket.S_UPDATE_SHIP_MOTION_Y, GCCoreUtil.getDimensionType(mc.world), new Object[]{autoRocket.getEntityId(), false}));
                    }

                    if (kb == KeyHandlerClient.spaceKey)
                    {
                        autoRocket.setMotion(autoRocket.getMotion().x, autoRocket.getMotion().y + 0.02, autoRocket.getMotion().z);
                        GalacticraftCore.packetPipeline.sendToServer(new PacketSimple(EnumSimplePacket.S_UPDATE_SHIP_MOTION_Y, GCCoreUtil.getDimensionType(mc.world), new Object[]{autoRocket.getEntityId(), true}));
                    }
                }
            }
        }
    }

    @Override
    public void keyUp(TickEvent.Type types, KeyBinding kb, boolean tickEnd)
    {
    }
}