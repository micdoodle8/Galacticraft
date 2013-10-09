package codechicken.core;

import java.net.Socket;
import codechicken.core.internal.ClientTickHandler;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.MemoryConnection;
import net.minecraft.server.MinecraftServer;
import net.minecraft.network.TcpConnection;
import net.minecraft.world.World;
import net.minecraft.client.multiplayer.WorldClient;

public class ClientUtils extends CommonUtils
{    
    private static Minecraft mc()
    {
        return Minecraft.getMinecraft();
    }
    
    public static World getWorld()
    {
        return mc().theWorld;
    }

    public static EntityPlayer getPlayer(String playername)
    {
        return playername == mc().thePlayer.username || playername == null ? mc().thePlayer : null;
    }

    public static boolean isClient(World world)
    {
        return world instanceof WorldClient;
    }
    
    public static boolean inWorld()
    {
        return mc().getNetHandler() != null;
    }
    
    public static void openSMPGui(int windowId, GuiScreen gui)
    {
        mc().displayGuiScreen(gui);
        if(windowId != 0)
            mc().thePlayer.openContainer.windowId = windowId;
    }

    public static float getRenderFrame()
    {
        return ClientTickHandler.renderFrame;
    }
    
    public static double getRenderTime()
    {
        return ClientTickHandler.renderTime + getRenderFrame();
    }    

    public static String getServerIP()
    {
        try
        {
            INetworkManager networkManager = mc().getNetHandler().getNetManager();
            if(networkManager instanceof MemoryConnection)
                return "memory";
            
            Socket socket = ((TcpConnection)networkManager).getSocket();
            if(socket == null)
                throw new NetworkClosedException();
            return socket.getInetAddress().getHostAddress()+":"+socket.getPort();
        }
        catch(Exception e)
        {
            throw new RuntimeException(e);
        }
    }

    public static boolean isLocal() 
    {
        return getServerIP().equals("memory");
    }

    @SideOnly(Side.CLIENT)
    public static String getWorldSaveName(String worldName)
    {
        if(!isLocal())
            return null;
        return MinecraftServer.getServer().getFolderName();
    }
}
