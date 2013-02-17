package micdoodle8.mods.galacticraft.europa.blocks;

import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.europa.GCEuropaConfigManager;
import micdoodle8.mods.galacticraft.europa.client.ClientProxyEuropa;
import net.minecraft.block.Block;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;

/**
 * Copyright 2012-2013, micdoodle8
 * 
 *  All rights reserved.
 *
 */
public class GCEuropaBlocks
{
	public static Block brittleIce;
	
	public static void initBlocks()
	{
		GCEuropaBlocks.brittleIce = 										new GCEuropaBlockBrittleIce				(GCEuropaConfigManager.idBlockBrittleIce, 					0)						.setHardness(1.7F)																				.setCreativeTab(GalacticraftCore.galacticraftTab)																.setBlockName("brittleIce");
	}

	public static void setHarvestLevels()
	{
	}
	
	public static void registerBlocks()
	{
		GameRegistry.registerBlock(GCEuropaBlocks.brittleIce);
	}

	public static void addNames()
	{
		GCEuropaBlocks.addName(GCEuropaBlocks.brittleIce);
	}
	
	private static void addName(Block block)
	{
		LanguageRegistry.instance().addStringLocalization(block.getBlockName() + ".name", ClientProxyEuropa.lang.get(block.getBlockName() + ".name"));
	}
	
	private static void addNameWithMetadata(String string)
	{
		LanguageRegistry.instance().addStringLocalization(string, ClientProxyEuropa.lang.get(string));
	}
}
