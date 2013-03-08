package micdoodle8.mods.galacticraft.io.blocks;

import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.io.GCIoConfigManager;
import micdoodle8.mods.galacticraft.io.client.ClientProxyIo;
import micdoodle8.mods.galacticraft.io.items.GCIoItemBlock;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.item.Item;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;

/**
 * Copyright 2012-2013, micdoodle8
 *
 *  All rights reserved.
 *
 */
public class GCIoBlocks
{
	public static Block block;
	public static Block blockPyroxene;

	public static void initBlocks()
	{
		GCIoBlocks.block = 										new GCIoBlock				(GCIoConfigManager.idBlock, 					0)						.setHardness(1.7F)																				.setCreativeTab(GalacticraftCore.galacticraftTab)																.setBlockName("BlockIo");
		GCIoBlocks.blockPyroxene = 								new GCIoBlockPyroxene		(GCIoConfigManager.idBlockPyroxene,				Material.rock)			.setHardness(1.7F)																				.setCreativeTab(GalacticraftCore.galacticraftTab)																.setBlockName("BlockPyroxene");
		Item.itemsList[GCIoBlocks.block.blockID] = 				new GCIoItemBlock			(GCIoBlocks.block.blockID - 256)																																																																	.setItemName("BlockIo");
	}

	public static void setHarvestLevels()
	{
	}

	public static void registerBlocks()
	{
		GameRegistry.registerBlock(GCIoBlocks.blockPyroxene);
	}

	public static void addNames()
	{
		GCIoBlocks.addName(GCIoBlocks.blockPyroxene);
		GCIoBlocks.addNameWithMetadata("tile.BlockIo.basalt.name");
		GCIoBlocks.addNameWithMetadata("tile.BlockIo.stone.name");
		GCIoBlocks.addNameWithMetadata("tile.BlockIo.sulfur.name");
		GCIoBlocks.addNameWithMetadata("tile.BlockIo.other.name");
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
