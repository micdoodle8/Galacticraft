package micdoodle8.mods.galacticraft.mars.blocks;

import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.mars.GCMarsConfigManager;
import micdoodle8.mods.galacticraft.mars.client.ClientProxyMars;
import micdoodle8.mods.galacticraft.mars.items.GCMarsItemBlockOre;
import net.minecraft.block.Block;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialLiquid;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraftforge.common.MinecraftForge;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

/**
 * Copyright 2012-2013, micdoodle8
 *
 *  All rights reserved.
 *
 */
public class GCMarsBlocks
{
	public static Block marsStone;
	public static Block marsDirt;
	public static Block marsGrass;
	public static Block marsCobblestone;
	public static Block creeperEgg;
	public static Block creeperDungeonWall;
	public static Block bacterialSludgeStill;
	public static Block bacterialSludgeMoving;
	public static Block blockOres;

    public static final Material bacterialSludge = new MaterialLiquid(MapColor.waterColor);

	public static void initBlocks()
	{
		GCMarsBlocks.blockOres = 										new GCMarsBlockOre					(GCMarsConfigManager.idBlockOre)												.setHardness(3.0F) 																				 																											.setBlockName("blockores");
		GCMarsBlocks.marsCobblestone = 									new GCMarsBlock						(GCMarsConfigManager.idBlockMarsCobblestone, 			13, Material.rock)		.setHardness(2.2F)																				.setCreativeTab(GalacticraftCore.galacticraftTab)																.setBlockName("marsCobblestone");
		GCMarsBlocks.marsStone = 										new GCMarsBlockStone				(GCMarsConfigManager.idBlockMarsStone, 					0)						.setHardness(1.7F)																				.setCreativeTab(GalacticraftCore.galacticraftTab)																.setBlockName("marsStone");
		GCMarsBlocks.marsDirt = 											new GCMarsBlock						(GCMarsConfigManager.idBlockMarsDirt, 					3, Material.ground)		.setHardness(0.6F)																				.setCreativeTab(GalacticraftCore.galacticraftTab)																.setBlockName("marsDirt");
		GCMarsBlocks.marsGrass = 										new GCMarsBlockGrass				(GCMarsConfigManager.idBlockMarsGrass,					2)						.setHardness(0.7F)																				.setCreativeTab(GalacticraftCore.galacticraftTab)																.setBlockName("marsGrass");
		GCMarsBlocks.creeperEgg = 										new GCMarsBlockCreeperEgg			(GCMarsConfigManager.idBlockCreeperEgg, 				12)						.setHardness(3.0F)		.setResistance(15.0F)	.setLightValue(0.125F)							.setCreativeTab(GalacticraftCore.galacticraftTab)		.setStepSound(Block.soundStoneFootstep)					.setBlockName("creeperEgg");
		GCMarsBlocks.creeperDungeonWall = 								new GCMarsBlockCreeperDungeonWall	(GCMarsConfigManager.idBlockCreeperDungeonWall,     	11, Material.rock)		.setHardness(5.0F)      .setResistance(100F)    .setLightValue(0F)								.setCreativeTab(GalacticraftCore.galacticraftTab)       	.setStepSound(Block.soundStoneFootstep)					.setBlockName("creeperDungeonWall");
		GCMarsBlocks.bacterialSludgeStill = 								new GCMarsBlockStationary 			(GCMarsConfigManager.idBlockBacterialSludgeStill,  		237, GCMarsBlocks.bacterialSludge)	.setHardness(0.0F)								.setLightValue(0.2F)	.setLightOpacity(3)		.setCreativeTab((CreativeTabs)null)																			.setBlockName("bacterialSludgeStill");
		GCMarsBlocks.bacterialSludgeMoving = 							new GCMarsBlockFlowing 				(GCMarsConfigManager.idBlockBacterialSludgeMoving, 		237, GCMarsBlocks.bacterialSludge)	.setHardness(0.0F)								.setLightValue(0.2F)	.setLightOpacity(3)		.setCreativeTab((CreativeTabs)null)																			.setBlockName("bacterialSludgeMoving");
		Item.itemsList[GCMarsBlocks.blockOres.blockID] = 				new GCMarsItemBlockOre				(GCMarsBlocks.blockOres.blockID - 256)																																																																.setItemName("blockores");
	}

	public static void setHarvestLevels()
	{
		MinecraftForge.setBlockHarvestLevel(GCMarsBlocks.creeperEgg, 				"pickaxe", 	2);
		MinecraftForge.setBlockHarvestLevel(GCMarsBlocks.creeperDungeonWall, 		"pickaxe", 	2);
		MinecraftForge.setBlockHarvestLevel(GCMarsBlocks.marsCobblestone, 			"pickaxe", 	1);
		MinecraftForge.setBlockHarvestLevel(GCMarsBlocks.marsStone, 					"pickaxe", 	1);
	}

	public static void registerBlocks()
	{
		GameRegistry.registerBlock(GCMarsBlocks.marsStone);
		GameRegistry.registerBlock(GCMarsBlocks.marsDirt);
		GameRegistry.registerBlock(GCMarsBlocks.marsGrass);
		GameRegistry.registerBlock(GCMarsBlocks.marsCobblestone);
		GameRegistry.registerBlock(GCMarsBlocks.creeperEgg);
		GameRegistry.registerBlock(GCMarsBlocks.creeperDungeonWall);
		GameRegistry.registerBlock(GCMarsBlocks.bacterialSludgeStill);
		GameRegistry.registerBlock(GCMarsBlocks.bacterialSludgeMoving);
	}

	@SideOnly(Side.CLIENT)
	public static void addNames()
	{
		GCMarsBlocks.addName(GCMarsBlocks.marsStone);
		GCMarsBlocks.addName(GCMarsBlocks.marsDirt);
		GCMarsBlocks.addName(GCMarsBlocks.marsGrass);
		GCMarsBlocks.addName(GCMarsBlocks.marsCobblestone);
		GCMarsBlocks.addName(GCMarsBlocks.creeperEgg);
		GCMarsBlocks.addName(GCMarsBlocks.creeperDungeonWall);
		GCMarsBlocks.addNameWithMetadata("tile.blockores.desh.name");
		GCMarsBlocks.addNameWithMetadata("tile.blockores.electrum.name");
		GCMarsBlocks.addNameWithMetadata("tile.blockores.quandrium.name");
		GCMarsBlocks.addNameWithMetadata("tile.blockores.aluminummars.name");
		GCMarsBlocks.addNameWithMetadata("tile.blockores.coppermars.name");
		GCMarsBlocks.addNameWithMetadata("tile.blockores.titaniummars.name");
	}

	private static void addName(Block block)
	{
		LanguageRegistry.instance().addStringLocalization(block.getBlockName() + ".name", ClientProxyMars.lang.get(block.getBlockName() + ".name"));
	}

	private static void addNameWithMetadata(String string)
	{
		LanguageRegistry.instance().addStringLocalization(string, ClientProxyMars.lang.get(string));
	}
}
