package micdoodle8.mods.galacticraft.core.util;

import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import micdoodle8.mods.galacticraft.core.client.gui.screen.GuiMissingCore;

public class ThreadRequirementMissing extends Thread
{
    private static Side threadSide;
    public static ThreadRequirementMissing INSTANCE;

    public ThreadRequirementMissing(Side threadSide)
    {
        super("Galacticraft Requirement Check Thread");
        this.setDaemon(true);
        ThreadRequirementMissing.threadSide = threadSide;
    }

    public static void beginCheck(Side threadSide)
    {
        INSTANCE = new ThreadRequirementMissing(threadSide);
        INSTANCE.start();
    }

    @Override
    public void run()
    {
        if (!Loader.isModLoaded("Micdoodlecore"))
        {
            if (ThreadRequirementMissing.threadSide.isServer())
            {
                FMLCommonHandler.instance().getMinecraftServerInstance().logSevere("===================================================================");
                FMLCommonHandler.instance().getMinecraftServerInstance().logSevere("MicdoodleCore not found in mods folder. Galacticraft will not load.");
                FMLCommonHandler.instance().getMinecraftServerInstance().logSevere("===================================================================");
            }
            else
            {
                ThreadRequirementMissing.openGuiClient();
            }
        }
    }

    @SideOnly(Side.CLIENT)
    private static void openGuiClient()
    {
        FMLClientHandler.instance().getClient().displayGuiScreen(new GuiMissingCore());
    }
}
