package codechicken.core;

import codechicken.core.internal.CCCEventHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.network.NetworkManager;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.ModContainer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ClientUtils extends CommonUtils {
    private static Minecraft mc() {
        return Minecraft.getMinecraft();
    }

    public static World getWorld() {
        return mc().theWorld;
    }

    public static boolean inWorld()//TODO unused
    {
        return mc().getNetHandler() != null;
    }

    public static void openSMPGui(int windowId, GuiScreen gui) {
        mc().displayGuiScreen(gui);
        if (windowId != 0) {
            mc().thePlayer.openContainer.windowId = windowId;
        }
    }

    public static float getRenderFrame() {
        return CCCEventHandler.renderFrame;
    }

    public static double getRenderTime() {
        return CCCEventHandler.renderTime + getRenderFrame();
    }

    public static String getServerIP() {
        try {
            NetworkManager networkManager = mc().getNetHandler().getNetworkManager();
            String s = networkManager.getRemoteAddress().toString();
            s = s.substring(s.indexOf("/") + 1);
            return s;
        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @SideOnly(Side.CLIENT)
    public static String getWorldSaveName() {
        return mc().isSingleplayer() ? MinecraftServer.getServer().getFolderName() : null;
    }

    public static void enhanceSupportersList(Object mod) {
        ModContainer mc = FMLCommonHandler.instance().findContainerFor(mod);
        mc.getMetadata().description = mc.getMetadata().description.replace("Supporters:", EnumChatFormatting.AQUA + "Supporters:");
        GuiModListScroll.register(mod);
    }
}
