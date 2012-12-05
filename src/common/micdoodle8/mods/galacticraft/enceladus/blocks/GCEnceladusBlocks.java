package micdoodle8.mods.galacticraft.enceladus.blocks;

import micdoodle8.mods.galacticraft.enceladus.client.ClientProxyEnceledus;
import net.minecraft.src.Block;
import cpw.mods.fml.common.registry.LanguageRegistry;

/**
 * Copyright 2012, micdoodle8
 * 
 *  All rights reserved.
 *
 */
public class GCEnceladusBlocks 
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
		LanguageRegistry.instance().addStringLocalization(block.getBlockName() + ".name", ClientProxyEnceledus.lang.get(block.getBlockName() + ".name"));
	}
	
	private static void addNameWithMetadata(String string)
	{
		LanguageRegistry.instance().addStringLocalization(string, ClientProxyEnceledus.lang.get(string));
	}
}
