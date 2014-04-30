package codechicken.core;

import java.io.File;
import java.lang.reflect.Array;
import java.util.LinkedList;
import java.util.List;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.ModContainer;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ChatAllowedCharacters;
import net.minecraft.util.ChatComponentText;
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

    public static File getModsFolder() {
        return new File(getMinecraftDir(), "mods");
    }

    public static File getMinecraftDir() {
        return Minecraft.getMinecraft().mcDataDir;
    }

    public static String getRelativePath(File parent, File child) {
        if (parent.isFile() || !child.getPath().startsWith(parent.getPath()))
            return null;

        return child.getPath().substring(parent.getPath().length() + 1);
    }

    public static ModContainer findModContainer(String modID) {
        for (ModContainer mc : Loader.instance().getModList())
            if (modID.equals(mc.getModId()))
                return mc;

        return null;
    }
}
