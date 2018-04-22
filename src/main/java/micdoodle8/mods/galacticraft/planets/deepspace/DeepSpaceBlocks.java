package micdoodle8.mods.galacticraft.planets.deepspace;

import micdoodle8.mods.galacticraft.core.GCBlocks;
import micdoodle8.mods.galacticraft.core.items.ItemBlockGC;
import micdoodle8.mods.galacticraft.planets.deepspace.blocks.BlockDeepStructure;
import net.minecraft.block.Block;

public class DeepSpaceBlocks
{
    public static Block deepStructure;
    public static Block deepWall;


    public static void initBlocks()
    {
        DeepSpaceBlocks.deepStructure = new BlockDeepStructure("deep", false);
        DeepSpaceBlocks.deepWall = new BlockDeepStructure("deep_wall", true);
    }

    public static void registerBlocks()
    {
        GCBlocks.registerBlock(DeepSpaceBlocks.deepStructure, ItemBlockGC.class);
        GCBlocks.registerBlock(DeepSpaceBlocks.deepWall, ItemBlockGC.class);
    }

    public static void setHarvestLevels()
    {
    }

}
