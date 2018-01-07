package micdoodle8.mods.galacticraft.core.util;

import micdoodle8.mods.galacticraft.core.client.gui.screen.GuiMissingCore;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

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
        if (!Loader.isModLoaded("micdoodlecore"))
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
