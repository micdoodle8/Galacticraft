package micdoodle8.mods.galacticraft.titan.blocks;

import net.minecraft.block.Block;
import micdoodle8.mods.galacticraft.titan.client.ClientProxyTitan;
import cpw.mods.fml.common.registry.LanguageRegistry;

/**
 * Copyright 2012, micdoodle8
 * 
 *  All rights reserved.
 *
 */
public class GCTitanBlocks 
{
	public static void initBlocks() 
	{	
	}

	public static void setHarvestLevels() 
	{
	}
	
	public static void registerBlocks() 
	{
	}

	public static void addNames() 
	{
	}
	
	private static void addName(Block block)
	{
		LanguageRegistry.instance().addStringLocalization(block.getBlockName() + ".name", ClientProxyTitan.lang.get(block.getBlockName() + ".name"));
	}
	
	private static void addNameWithMetadata(String string)
	{
		LanguageRegistry.instance().addStringLocalization(string, ClientProxyTitan.lang.get(string));
	}
}
