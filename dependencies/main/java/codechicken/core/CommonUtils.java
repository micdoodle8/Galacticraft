package codechicken.core;

import java.io.File;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.relauncher.FMLInjectionData;
import net.minecraft.world.World;
import net.minecraftforge.common.DimensionManager;

public class CommonUtils
{
    public static boolean isClient() {
        return FMLCommonHandler.instance().getSide().isClient();
    }

    public static File getSaveLocation(int dim) {
        File basesave = DimensionManager.getCurrentSaveRootDirectory();
        if (dim != 0)
            return new File(basesave, DimensionManager.getWorld(dim).provider.getSaveFolder());

        return basesave;
    }

    public static String getWorldName(World world) {
        return world.getWorldInfo().getWorldName();
    }

    public static int getDimension(World world) {
        return world.provider.dimensionId;
    }

    public static File getMinecraftDir() {
        return (File) FMLInjectionData.data()[6];
    }

    public static String getRelativePath(File parent, File child) {
        if (parent.isFile() || !child.getPath().startsWith(parent.getPath()))
            return null;

        return child.getPath().substring(parent.getPath().length() + 1);
    }
}
