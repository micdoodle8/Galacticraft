package micdoodle8.mods.galacticraft.moon.blocks;

import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.moon.GCMoonConfigManager;
import micdoodle8.mods.galacticraft.moon.items.GCMoonItemBlock;
import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.common.MinecraftForge;
import cpw.mods.fml.common.registry.GameRegistry;

public class GCMoonBlocks
{
    public static Block blockMoon;
    public static Block cheeseBlock;

    public static void initBlocks()
    {
        GCMoonBlocks.blockMoon = new GCMoonBlock(GCMoonConfigManager.idBlock).setHardness(3.0F).setUnlocalizedName("moonBlock");
        GCMoonBlocks.cheeseBlock = new GCMoonBlockCheese(GCMoonConfigManager.idBlockCheese).setHardness(0.5F).setStepSound(Block.soundClothFootstep).setUnlocalizedName("cheeseBlock");
    }

    public static void setHarvestLevels()
    {
        MinecraftForge.setBlockHarvestLevel(GCMoonBlocks.blockMoon, 0, "pickaxe", 3);
        MinecraftForge.setBlockHarvestLevel(GCMoonBlocks.blockMoon, 1, "pickaxe", 3);
        MinecraftForge.setBlockHarvestLevel(GCMoonBlocks.blockMoon, 2, "pickaxe", 3);
        MinecraftForge.setBlockHarvestLevel(GCMoonBlocks.blockMoon, 3, "shovel", 0);
        MinecraftForge.setBlockHarvestLevel(GCMoonBlocks.blockMoon, 4, "pickaxe", 2);

        for (int num = 5; num < 14; num++)
        {
            MinecraftForge.setBlockHarvestLevel(GCMoonBlocks.blockMoon, num, "shovel", 0);
        }

        MinecraftForge.setBlockHarvestLevel(GCMoonBlocks.blockMoon, 14, "pickaxe", 2);
    }

    public static void registerBlocks()
    {
        GameRegistry.registerBlock(GCMoonBlocks.blockMoon, GCMoonItemBlock.class, GCMoonBlocks.blockMoon.getUnlocalizedName(), GalacticraftCore.MODID);
        GameRegistry.registerBlock(GCMoonBlocks.cheeseBlock, ItemBlock.class, GCMoonBlocks.cheeseBlock.getUnlocalizedName(), GalacticraftCore.MODID);
    }
}
