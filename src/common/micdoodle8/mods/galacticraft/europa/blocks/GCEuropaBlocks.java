package micdoodle8.mods.galacticraft.europa.blocks;

import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.europa.GCEuropaConfigManager;
import micdoodle8.mods.galacticraft.europa.client.ClientProxyEuropa;
import micdoodle8.mods.galacticraft.jupiter.GalacticraftJupiter;
import net.minecraft.src.Block;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;

/**
 * Copyright 2012, micdoodle8
 * 
 *  All rights reserved.
 *
 */
public class GCEuropaBlocks 
{
	public static Block brittleIce;
	
	public static void initBlocks() 
	{	
		brittleIce = 										new GCEuropaBlockBrittleIce				(GCEuropaConfigManager.idBlockBrittleIce, 					0)						.setHardness(1.7F)																				.setCreativeTab(GalacticraftCore.galacticraftTab)																.setBlockName("brittleIce");
	}

	public static void setHarvestLevels() 
	{
	}
	
	public static void registerBlocks() 
	{
		GameRegistry.registerBlock(brittleIce);
	}

	public static void addNames() 
	{
		addName(brittleIce);
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
