package micdoodle8.mods.galacticraft.enceladus.blocks;

import micdoodle8.mods.galacticraft.callisto.client.ClientProxyCallisto;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.enceladus.GCEnceladusConfigManager;
import micdoodle8.mods.galacticraft.enceladus.client.ClientProxyEnceladus;
import micdoodle8.mods.galacticraft.enceladus.items.GCEnceladusItemBlock;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import cpw.mods.fml.common.registry.LanguageRegistry;

/**
 * Copyright 2012-2013, micdoodle8
 *
 *  All rights reserved.
 *
 */
public class GCEnceladusBlocks
{
	public static Block block;

	public static void initBlocks()
	{
		GCEnceladusBlocks.block = 										new GCEnceladusBlock			(GCEnceladusConfigManager.idBlock, 					0)						.setHardness(1.7F)																				.setCreativeTab(GalacticraftCore.galacticraftTab)																.setBlockName("BlockEnceladus");
		Item.itemsList[GCEnceladusBlocks.block.blockID] = 				new GCEnceladusItemBlock		(GCEnceladusBlocks.block.blockID - 256)																																																																		.setItemName("BlockEnceladus");
	}

	public static void setHarvestLevels()
	{
	}

	public static void registerBlocks()
	{
	}

	public static void addNames()
	{
		GCEnceladusBlocks.addNameWithMetadata("tile.BlockEnceladus.grass.name");
		GCEnceladusBlocks.addNameWithMetadata("tile.BlockEnceladus.dirt.name");
		GCEnceladusBlocks.addNameWithMetadata("tile.BlockEnceladus.stone.name");
	}

	private static void addName(Block block)
	{
		LanguageRegistry.instance().addStringLocalization(block.getBlockName() + ".name", ClientProxyCallisto.lang.get(block.getBlockName() + ".name"));
	}

	private static void addNameWithMetadata(String string)
	{
		LanguageRegistry.instance().addStringLocalization(string, ClientProxyEnceladus.lang.get(string));
	}
}
