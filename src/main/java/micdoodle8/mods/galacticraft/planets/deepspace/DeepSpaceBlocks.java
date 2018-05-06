package micdoodle8.mods.galacticraft.planets.deepspace;

import micdoodle8.mods.galacticraft.core.GCBlocks;
import micdoodle8.mods.galacticraft.core.items.ItemBlockGC;
import micdoodle8.mods.galacticraft.planets.deepspace.blocks.BlockDeepStructure;
import micdoodle8.mods.galacticraft.planets.deepspace.blocks.BlockGlassProtective;
import net.minecraft.block.Block;

public class DeepSpaceBlocks
{
    public static Block deepStructure;
    public static Block deepWall;
    public static Block glassProtective;


    public static void initBlocks()
    {
        DeepSpaceBlocks.deepStructure = new BlockDeepStructure("deep", false);
        DeepSpaceBlocks.deepWall = new BlockDeepStructure("deep_wall", true);
        DeepSpaceBlocks.glassProtective = new BlockGlassProtective("protective_glass");
    }

    public static void registerBlocks()
    {
        GCBlocks.registerBlock(DeepSpaceBlocks.deepStructure, ItemBlockGC.class);
        GCBlocks.registerBlock(DeepSpaceBlocks.deepWall, ItemBlockGC.class);
        GCBlocks.registerBlock(DeepSpaceBlocks.glassProtective, ItemBlockGC.class);
    }

    public static void setHarvestLevels()
    {
        glassProtective.setHarvestLevel("axe", 2);
    }

}
