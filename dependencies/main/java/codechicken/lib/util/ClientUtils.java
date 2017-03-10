package codechicken.lib.util;

import codechicken.lib.render.CCRenderEventHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.network.NetworkManager;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ClientUtils extends CommonUtils {

    private static Minecraft mc() {
        return Minecraft.getMinecraft();
    }

    public static World getWorld() {
        return mc().theWorld;
    }

    public static boolean inWorld() {
        return mc().getConnection() != null;
    }

    public static void openSMPGui(int windowId, GuiScreen gui) {
        mc().displayGuiScreen(gui);
        if (windowId != 0) {
            mc().thePlayer.openContainer.windowId = windowId;
        }
    }

    public static float getRenderFrame() {
        return CCRenderEventHandler.renderFrame;
    }

    public static double getRenderTime() {
        return CCRenderEventHandler.renderTime + getRenderFrame();
    }

    public static String getServerIP() {
        try {
            NetworkManager networkManager = mc().getConnection().getNetworkManager();
            String s = networkManager.getRemoteAddress().toString();
            s = s.substring(s.indexOf("/") + 1);
            return s;
        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @SideOnly (Side.CLIENT)
    public static String getWorldSaveName() {
        return mc().isSingleplayer() ? FMLCommonHandler.instance().getMinecraftServerInstance().getFolderName() : null;
    }
}
