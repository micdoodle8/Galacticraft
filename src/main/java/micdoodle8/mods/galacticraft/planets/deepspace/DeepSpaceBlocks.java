package micdoodle8.mods.galacticraft.planets.deepspace;

import micdoodle8.mods.galacticraft.core.GCBlocks;
import micdoodle8.mods.galacticraft.core.items.ItemBlockGC;
import micdoodle8.mods.galacticraft.planets.deepspace.blocks.BlockBasicSpace;
import micdoodle8.mods.galacticraft.planets.deepspace.blocks.BlockDeepStructure;
import micdoodle8.mods.galacticraft.planets.deepspace.blocks.BlockFlooring;
import micdoodle8.mods.galacticraft.planets.deepspace.blocks.BlockGlassProtective;
import micdoodle8.mods.galacticraft.planets.deepspace.blocks.BlockInterior;
import micdoodle8.mods.galacticraft.planets.deepspace.blocks.BlockSurface;
import micdoodle8.mods.galacticraft.planets.deepspace.item.ItemBlockGCVariants;
import net.minecraft.block.Block;

public class DeepSpaceBlocks
{
    public static Block deepStructure;
    public static Block deepWall;
    public static Block glassProtective;
    public static Block spaceBasic;
    public static Block surface;
    public static Block interior;
    public static Block flooring;

    public static void initBlocks()
    {
        DeepSpaceBlocks.deepStructure = new BlockDeepStructure("deep", false);
        DeepSpaceBlocks.deepWall = new BlockDeepStructure("deep_wall", true);
        DeepSpaceBlocks.glassProtective = new BlockGlassProtective("protective_glass");
        DeepSpaceBlocks.spaceBasic = new BlockBasicSpace("deep_space");
        DeepSpaceBlocks.surface = new BlockSurface("surface");
//        DeepSpaceBlocks.surfaceDetail = new BlockSurfaceDetail("surface_detail");
        DeepSpaceBlocks.interior = new BlockInterior("interior");
        DeepSpaceBlocks.flooring = new BlockFlooring("flooring");
    }

    public static void registerBlocks()
    {
        GCBlocks.registerBlock(DeepSpaceBlocks.deepStructure, ItemBlockGC.class);
        GCBlocks.registerBlock(DeepSpaceBlocks.deepWall, ItemBlockGC.class);
        GCBlocks.registerBlock(DeepSpaceBlocks.glassProtective, ItemBlockGC.class);
        GCBlocks.registerBlock(DeepSpaceBlocks.spaceBasic, ItemBlockGCVariants.class);
        GCBlocks.registerBlock(DeepSpaceBlocks.surface, ItemBlockGCVariants.class);
        GCBlocks.registerBlock(DeepSpaceBlocks.interior, ItemBlockGCVariants.class);
        GCBlocks.registerBlock(DeepSpaceBlocks.flooring, ItemBlockGCVariants.class);
    }

    public static void setHarvestLevels()
    {
        glassProtective.setHarvestLevel("axe", 2);
    }

}
