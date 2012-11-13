package micdoodle8.mods.galacticraft.mars.blocks;

import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.mars.GCMarsConfigManager;
import micdoodle8.mods.galacticraft.mars.client.ClientProxyMars;
import micdoodle8.mods.galacticraft.mars.items.GCMarsItemBlockOre;
import net.minecraft.src.Block;
import net.minecraft.src.CreativeTabs;
import net.minecraft.src.Item;
import net.minecraft.src.MapColor;
import net.minecraft.src.Material;
import net.minecraft.src.MaterialLiquid;
import net.minecraftforge.common.MinecraftForge;
import cpw.mods.fml.common.Side;
import cpw.mods.fml.common.asm.SideOnly;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;

/**
 * Copyright 2012, micdoodle8
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

    public static final Material bacterialSludge = (new MaterialLiquid(MapColor.waterColor));
	
	public static void initBlocks() 
	{	
		blockOres = 										new GCMarsBlockOre					(GCMarsConfigManager.idBlockOre)												.setHardness(3.0F) 																				 																											.setBlockName("blockores");
		marsCobblestone = 									new GCMarsBlock						(GCMarsConfigManager.idBlockMarsCobblestone, 			13, Material.rock)		.setHardness(2.2F)																				.setCreativeTab(GalacticraftCore.galacticraftTab)																.setBlockName("marsCobblestone");
		marsStone = 										new GCMarsBlockStone				(GCMarsConfigManager.idBlockMarsStone, 					0)						.setHardness(1.7F)																				.setCreativeTab(GalacticraftCore.galacticraftTab)																.setBlockName("marsStone");
		marsDirt = 											new GCMarsBlock						(GCMarsConfigManager.idBlockMarsDirt, 					3, Material.ground)		.setHardness(0.6F)																				.setCreativeTab(GalacticraftCore.galacticraftTab)																.setBlockName("marsDirt");
		marsGrass = 										new GCMarsBlockGrass				(GCMarsConfigManager.idBlockMarsGrass,					2)						.setHardness(0.7F)																				.setCreativeTab(GalacticraftCore.galacticraftTab)																.setBlockName("marsGrass");
		creeperEgg = 										new GCMarsBlockCreeperEgg			(GCMarsConfigManager.idBlockCreeperEgg, 				12)						.setHardness(3.0F)		.setResistance(15.0F)	.setLightValue(0.125F)							.setCreativeTab(GalacticraftCore.galacticraftTab)		.setStepSound(Block.soundStoneFootstep)					.setBlockName("creeperEgg");
		creeperDungeonWall = 								new GCMarsBlockCreeperDungeonWall	(GCMarsConfigManager.idBlockCreeperDungeonWall,     	11, Material.rock)		.setHardness(5.0F)      .setResistance(100F)    .setLightValue(0F)								.setCreativeTab(GalacticraftCore.galacticraftTab)       	.setStepSound(Block.soundStoneFootstep)					.setBlockName("creeperDungeonWall");
		bacterialSludgeStill = 								new GCMarsBlockStationary 			(GCMarsConfigManager.idBlockBacterialSludgeStill,  		237, bacterialSludge)	.setHardness(0.0F)								.setLightValue(0.2F)	.setLightOpacity(3)		.setCreativeTab((CreativeTabs)null)																			.setBlockName("bacterialSludgeStill");
		bacterialSludgeMoving = 							new GCMarsBlockFlowing 				(GCMarsConfigManager.idBlockBacterialSludgeMoving, 		237, bacterialSludge)	.setHardness(0.0F)								.setLightValue(0.2F)	.setLightOpacity(3)		.setCreativeTab((CreativeTabs)null)																			.setBlockName("bacterialSludgeMoving");
		Item.itemsList[blockOres.blockID] = 				new GCMarsItemBlockOre				(blockOres.blockID - 256)																																																																.setItemName("blockores");
	}

	public static void setHarvestLevels() 
	{
		MinecraftForge.setBlockHarvestLevel(creeperEgg, 				"pickaxe", 	2);
		MinecraftForge.setBlockHarvestLevel(creeperDungeonWall, 		"pickaxe", 	2);
		MinecraftForge.setBlockHarvestLevel(marsCobblestone, 			"pickaxe", 	1);
		MinecraftForge.setBlockHarvestLevel(marsStone, 					"pickaxe", 	1);
	}
	
	public static void registerBlocks() 
	{
		GameRegistry.registerBlock(marsStone);
		GameRegistry.registerBlock(marsDirt);
		GameRegistry.registerBlock(marsGrass);
		GameRegistry.registerBlock(marsCobblestone);
		GameRegistry.registerBlock(creeperEgg);
		GameRegistry.registerBlock(creeperDungeonWall);
		GameRegistry.registerBlock(bacterialSludgeStill);
		GameRegistry.registerBlock(bacterialSludgeMoving);
	}

	@SideOnly(Side.CLIENT)
	public static void addNames() 
	{
		addName(marsStone);
		addName(marsDirt);
		addName(marsGrass);
		addName(marsCobblestone);
		addName(creeperEgg);
		addName(creeperDungeonWall);
		addNameWithMetadata("tile.blockores.desh.name");
		addNameWithMetadata("tile.blockores.electrum.name");
		addNameWithMetadata("tile.blockores.quandrium.name");
		addNameWithMetadata("tile.blockores.aluminummars.name");
		addNameWithMetadata("tile.blockores.coppermars.name");
		addNameWithMetadata("tile.blockores.titaniummars.name");
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
