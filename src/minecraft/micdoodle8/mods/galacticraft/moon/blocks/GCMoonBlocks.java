package micdoodle8.mods.galacticraft.moon.blocks;

import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.moon.GCMoonConfigManager;
import micdoodle8.mods.galacticraft.moon.client.ClientProxyMoon;
import micdoodle8.mods.galacticraft.moon.items.GCMoonItemBlockOre;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.item.Item;
import cpw.mods.fml.relauncher.SideOnly;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;

public class GCMoonBlocks
{
	public static Block moonStone;
	public static Block moonDirt;
	public static Block moonGrass;
	public static Block blockMoonOres;
    public static Block cheeseBlock;

	public static void initBlocks() 
	{	
		blockMoonOres = 									new GCMoonBlockOre					(GCMoonConfigManager.idBlockOre)												.setHardness(3.0F) 																				 																											.setBlockName("moonBlockOres");
		moonStone = 										new GCMoonBlockStone				(GCMoonConfigManager.idBlockMoonStone, 					9)						.setHardness(1.7F)																				.setCreativeTab(GalacticraftCore.galacticraftTab)															.setBlockName("moonStone");
		moonDirt = 											new GCMoonBlockDirt					(GCMoonConfigManager.idBlockMoonDirt, 					2)						.setHardness(0.6F)																				.setCreativeTab(GalacticraftCore.galacticraftTab)															.setBlockName("moonDirt");
		moonGrass = 										new GCMoonBlockGrass				(GCMoonConfigManager.idBlockMoonGrass,					2)						.setHardness(0.7F)																				.setCreativeTab(GalacticraftCore.galacticraftTab)															.setBlockName("moonGrass");
		Item.itemsList[blockMoonOres.blockID] = 			new GCMoonItemBlockOre				(blockMoonOres.blockID - 256)																																																																.setItemName("moonBlockOres");
		cheeseBlock = 										new GCMoonBlockCheese				(GCMoonConfigManager.idBlockCheese, 					10)						.setHardness(0.5F)		.setStepSound(Block.soundClothFootstep)																																				.setBlockName("cheeseBlock");
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
		GameRegistry.registerBlock(cheeseBlock);
	}

	@SideOnly(Side.CLIENT)
	public static void addNames() 
	{
		addName(moonStone);
		addName(moonDirt);
		addName(moonGrass);
		addNameWithMetadata("tile.moonBlockOres.aluminummoon.name");
		addNameWithMetadata("tile.moonBlockOres.ironmoon.name");
		addNameWithMetadata("tile.moonBlockOres.cheesestone.name");
	}
	
	private static void addName(Block block)
	{
		LanguageRegistry.instance().addStringLocalization(block.getBlockName() + ".name", ClientProxyMoon.lang.get(block.getBlockName() + ".name"));
	}
	
	private static void addNameWithMetadata(String string)
	{
		LanguageRegistry.instance().addStringLocalization(string, ClientProxyMoon.lang.get(string));
	}
}
