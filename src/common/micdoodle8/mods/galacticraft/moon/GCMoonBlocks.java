package micdoodle8.mods.galacticraft.moon;

import micdoodle8.mods.galacticraft.mars.GCMarsBlock;
import micdoodle8.mods.galacticraft.mars.GCMarsBlockGrass;
import micdoodle8.mods.galacticraft.mars.GCMarsBlockOre;
import micdoodle8.mods.galacticraft.mars.GCMarsBlockStone;
import micdoodle8.mods.galacticraft.mars.GCMarsConfigManager;
import micdoodle8.mods.galacticraft.mars.GCMarsItemBlockOre;
import net.minecraft.src.Block;
import net.minecraft.src.CreativeTabs;
import net.minecraft.src.Item;
import net.minecraft.src.Material;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;

public class GCMoonBlocks 
{
	public static Block moonStone;
	public static Block moonDirt;
	public static Block moonGrass;
	public static Block moonCobblestone;
	public static Block blockMoonOres;

	public static void initBlocks() 
	{	
		blockMoonOres = 									new GCMoonBlockOre					(GCMoonConfigManager.idBlockOre)												.setHardness(3.0F) 																				 																											.setBlockName("moonBlockOres");
		moonCobblestone = 									new GCMoonBlock						(GCMoonConfigManager.idBlockMoonCobblestone, 			13, Material.rock)		.setHardness(2.2F)																				.setCreativeTab(CreativeTabs.tabDecorations)																.setBlockName("moonCobblestone");
		moonStone = 										new GCMoonBlockStone				(GCMoonConfigManager.idBlockMoonStone, 					1)						.setHardness(1.7F)																				.setCreativeTab(CreativeTabs.tabDecorations)																.setBlockName("moonStone");
		moonDirt = 											new GCMoonBlockDirt					(GCMoonConfigManager.idBlockMoonDirt, 					2)						.setHardness(0.6F)																				.setCreativeTab(CreativeTabs.tabDecorations)																.setBlockName("moonDirt");
		moonGrass = 										new GCMoonBlockGrass				(GCMoonConfigManager.idBlockMoonGrass,					2)						.setHardness(0.7F)																				.setCreativeTab(CreativeTabs.tabDecorations)																.setBlockName("moonGrass");
		Item.itemsList[blockMoonOres.blockID] = 			new GCMoonItemBlockOre				(blockMoonOres.blockID - 256)																																																																.setItemName("moonBlockOres");
	}

	public static void setHarvestLevels() 
	{
		// TODO
	}
	
	public static void registerBlocks() 
	{
		GameRegistry.registerBlock(moonStone);
		GameRegistry.registerBlock(moonDirt);
		GameRegistry.registerBlock(moonGrass);
		GameRegistry.registerBlock(moonCobblestone);
	}

	public static void addNames() 
	{
		
	}
	
	private static void addName(Block block)
	{
		LanguageRegistry.instance().addStringLocalization(block.getBlockName() + ".name", GalacticraftMoon.lang.get(block.getBlockName() + ".name"));
	}
	
	private static void addNameWithMetadata(String string)
	{
		LanguageRegistry.instance().addStringLocalization(string, GalacticraftMoon.lang.get(string));
	}
}
