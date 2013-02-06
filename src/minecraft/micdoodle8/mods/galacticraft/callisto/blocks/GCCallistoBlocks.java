package micdoodle8.mods.galacticraft.callisto.blocks;

import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.callisto.GCCallistoConfigManager;
import micdoodle8.mods.galacticraft.callisto.client.ClientProxyCallisto;
import micdoodle8.mods.galacticraft.callisto.items.GCCallistoItemBlock;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;

/**
 * Copyright 2012-2013, micdoodle8
 * 
 *  All rights reserved.
 *
 */
public class GCCallistoBlocks 
{
	public static Block block;
	
	public static void initBlocks() 
	{	
		block = 										new GCCallistoBlock			(GCCallistoConfigManager.idBlock, 					0)						.setHardness(1.7F)																				.setCreativeTab(GalacticraftCore.galacticraftTab)																.setBlockName("BlockCallisto");
		Item.itemsList[block.blockID] = 				new GCCallistoItemBlock		(block.blockID - 256)																																																																		.setItemName("BlockCallisto");	
	}

	public static void setHarvestLevels() 
	{
	}
	
	public static void registerBlocks() 
	{
	}

	public static void addNames() 
	{
		addNameWithMetadata("tile.BlockCallisto.grass.name");
		addNameWithMetadata("tile.BlockCallisto.dirt.name");
		addNameWithMetadata("tile.BlockCallisto.stone.name");
	}
	
	private static void addName(Block block)
	{
		LanguageRegistry.instance().addStringLocalization(block.getBlockName() + ".name", ClientProxyCallisto.lang.get(block.getBlockName() + ".name"));
	}
	
	private static void addNameWithMetadata(String string)
	{
		LanguageRegistry.instance().addStringLocalization(string, ClientProxyCallisto.lang.get(string));
	}
}
