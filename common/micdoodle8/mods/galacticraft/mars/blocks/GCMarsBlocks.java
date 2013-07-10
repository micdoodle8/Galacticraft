package micdoodle8.mods.galacticraft.mars.blocks;

import micdoodle8.mods.galacticraft.mars.GCMarsConfigManager;
import micdoodle8.mods.galacticraft.mars.GalacticraftMars;
import micdoodle8.mods.galacticraft.mars.items.GCMarsItemBlock;
import net.minecraft.block.Block;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialLiquid;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.common.MinecraftForge;
import cpw.mods.fml.common.registry.GameRegistry;

/**
 * Copyright 2012-2013, micdoodle8
 * 
 * All rights reserved.
 * 
 */
public class GCMarsBlocks
{
    public static Block marsBlock;
    public static Block blockSludge;
    public static Block vine;

    public static Material bacterialSludge = new MaterialLiquid(MapColor.foliageColor);

    public static void initBlocks()
    {
        GCMarsBlocks.marsBlock = new GCMarsBlock(GCMarsConfigManager.idBlockMars).setHardness(2.2F).setUnlocalizedName("mars");
        GCMarsBlocks.blockSludge = new GCMarsBlockSludge(GalacticraftMars.SLUDGE.getBlockID(), bacterialSludge).setUnlocalizedName("sludge");
        GCMarsBlocks.vine = new GCMarsBlockVine(GCMarsConfigManager.idBlockVine).setHardness(0.1F).setUnlocalizedName("vine");
    }

    public static void setHarvestLevels()
    {
        MinecraftForge.setBlockHarvestLevel(GCMarsBlocks.marsBlock, "pickaxe", 1);
    }

    public static void registerBlocks()
    {
        GameRegistry.registerBlock(GCMarsBlocks.marsBlock, GCMarsItemBlock.class, GCMarsBlocks.marsBlock.getUnlocalizedName(), GalacticraftMars.MODID);
        GameRegistry.registerBlock(GCMarsBlocks.blockSludge, ItemBlock.class, GCMarsBlocks.blockSludge.getUnlocalizedName(), GalacticraftMars.MODID);
        GameRegistry.registerBlock(GCMarsBlocks.vine, ItemBlock.class, GCMarsBlocks.vine.getUnlocalizedName(), GalacticraftMars.MODID);
    }
}
