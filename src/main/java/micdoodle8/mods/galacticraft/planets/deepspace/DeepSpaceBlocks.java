package micdoodle8.mods.galacticraft.planets.deepspace;

import micdoodle8.mods.galacticraft.core.GCBlocks;
import micdoodle8.mods.galacticraft.core.items.ItemBlockGC;
import micdoodle8.mods.galacticraft.planets.deepspace.blocks.BlockDeepStructure;
import net.minecraft.block.Block;

public class DeepSpaceBlocks
{
    public static Block deepStructure;


    public static void initBlocks()
    {
        DeepSpaceBlocks.deepStructure = new BlockDeepStructure("deep");
    }

    public static void registerBlocks()
    {
        GCBlocks.registerBlock(DeepSpaceBlocks.deepStructure, ItemBlockGC.class);
    }

    public static void setHarvestLevels()
    {
    }

}
