package micdoodle8.mods.galacticraft.mimas.blocks;

import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.mimas.GCMimasConfigManager;
import micdoodle8.mods.galacticraft.mimas.client.ClientProxyMimas;
import micdoodle8.mods.galacticraft.mimas.items.GCMimasItemBlock;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import cpw.mods.fml.common.registry.LanguageRegistry;

/**
 * Copyright 2012-2013, micdoodle8
 * 
 *  All rights reserved.
 *
 */
public class GCMimasBlocks
{
	public static Block block;
	
	public static void initBlocks()
	{
		GCMimasBlocks.block = 										new GCMimasBlock			(GCMimasConfigManager.idBlock, 					0)						.setHardness(1.7F)																				.setCreativeTab(GalacticraftCore.galacticraftTab)																.setBlockName("BlockMimas");
		Item.itemsList[GCMimasBlocks.block.blockID] = 				new GCMimasItemBlock		(GCMimasBlocks.block.blockID - 256)																																																																	.setItemName("BlockMimas");
	}

	public static void setHarvestLevels()
	{
	}
	
	public static void registerBlocks()
	{
	}

	public static void addNames()
	{
		GCMimasBlocks.addNameWithMetadata("tile.BlockMimas.grass.name");
		GCMimasBlocks.addNameWithMetadata("tile.BlockMimas.stone.name");
	}
	
	private static void addName(Block block)
	{
		LanguageRegistry.instance().addStringLocalization(block.getBlockName() + ".name", ClientProxyMimas.lang.get(block.getBlockName() + ".name"));
	}
	
	private static void addNameWithMetadata(String string)
	{
		LanguageRegistry.instance().addStringLocalization(string, ClientProxyMimas.lang.get(string));
	}
}
