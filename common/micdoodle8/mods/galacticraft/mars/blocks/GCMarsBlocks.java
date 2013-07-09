package micdoodle8.mods.galacticraft.mars.blocks;

import micdoodle8.mods.galacticraft.mars.GCMarsConfigManager;
import micdoodle8.mods.galacticraft.mars.GalacticraftMars;
import micdoodle8.mods.galacticraft.mars.items.GCMarsItemBlock;
import net.minecraft.block.Block;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialLiquid;
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

    public static final Material bacterialSludge = new MaterialLiquid(MapColor.waterColor);

    public static void initBlocks()
    {
        GCMarsBlocks.marsBlock = new GCMarsBlock(GCMarsConfigManager.idBlockMars).setHardness(2.2F).setUnlocalizedName("mars");
    }

    public static void setHarvestLevels()
    {
        MinecraftForge.setBlockHarvestLevel(GCMarsBlocks.marsBlock, "pickaxe", 1);
    }

    public static void registerBlocks()
    {
        GameRegistry.registerBlock(GCMarsBlocks.marsBlock, GCMarsItemBlock.class, GCMarsBlocks.marsBlock.getUnlocalizedName(), GalacticraftMars.MODID);
    }
}
