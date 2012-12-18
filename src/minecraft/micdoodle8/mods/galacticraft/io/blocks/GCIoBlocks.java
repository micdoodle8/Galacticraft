package micdoodle8.mods.galacticraft.io.blocks;

import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.io.GCIoConfigManager;
import micdoodle8.mods.galacticraft.io.client.ClientProxyIo;
import micdoodle8.mods.galacticraft.io.items.GCIoItemBlock;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import cpw.mods.fml.common.registry.LanguageRegistry;

/**
 * Copyright 2012, micdoodle8
 * 
 *  All rights reserved.
 *
 */
public class GCIoBlocks 
{
	public static Block block;
	
	public static void initBlocks() 
	{	
		block = 										new GCIoBlock				(GCIoConfigManager.idBlock, 					0)						.setHardness(1.7F)																				.setCreativeTab(GalacticraftCore.galacticraftTab)																.setBlockName("BlockIo");
		Item.itemsList[block.blockID] = 				new GCIoItemBlock			(block.blockID - 256)																																																																	.setItemName("BlockIo");	
	}

	public static void setHarvestLevels() 
	{
	}
	
	public static void registerBlocks() 
	{
	}

	public static void addNames() 
	{
		addNameWithMetadata("tile.BlockIo.pyroxene.name");
		addNameWithMetadata("tile.BlockIo.iostone.name");
		addNameWithMetadata("tile.BlockIo.sulfur.name");
	}
	
	private static void addName(Block block)
	{
		LanguageRegistry.instance().addStringLocalization(block.getBlockName() + ".name", ClientProxyIo.lang.get(block.getBlockName() + ".name"));
	}
	
	private static void addNameWithMetadata(String string)
	{
		LanguageRegistry.instance().addStringLocalization(string, ClientProxyIo.lang.get(string));
	}
}
