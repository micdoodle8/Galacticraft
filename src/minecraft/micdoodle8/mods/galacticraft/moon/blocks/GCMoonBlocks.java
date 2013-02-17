package micdoodle8.mods.galacticraft.moon.blocks;

import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.moon.GCMoonConfigManager;
import micdoodle8.mods.galacticraft.moon.client.ClientProxyMoon;
import micdoodle8.mods.galacticraft.moon.items.GCMoonItemBlockOre;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class GCMoonBlocks
{
	public static Block moonStone;
	public static Block moonDirt;
	public static Block moonGrass;
	public static Block blockMoonOres;
    public static Block cheeseBlock;

	public static void initBlocks()
	{
		GCMoonBlocks.blockMoonOres = 									new GCMoonBlockOre					(GCMoonConfigManager.idBlockOre)												.setHardness(3.0F) 																				 																											.setBlockName("moonBlockOres");
		GCMoonBlocks.moonStone = 										new GCMoonBlockStone				(GCMoonConfigManager.idBlockMoonStone, 					9)						.setHardness(1.7F)																				.setCreativeTab(GalacticraftCore.galacticraftTab)															.setBlockName("moonStone");
		GCMoonBlocks.moonDirt = 											new GCMoonBlockDirt					(GCMoonConfigManager.idBlockMoonDirt, 					2)						.setHardness(0.6F)																				.setCreativeTab(GalacticraftCore.galacticraftTab)															.setBlockName("moonDirt");
		GCMoonBlocks.moonGrass = 										new GCMoonBlockGrass				(GCMoonConfigManager.idBlockMoonGrass,					2)						.setHardness(0.7F)																				.setCreativeTab(GalacticraftCore.galacticraftTab)															.setBlockName("moonGrass");
		Item.itemsList[GCMoonBlocks.blockMoonOres.blockID] = 			new GCMoonItemBlockOre				(GCMoonBlocks.blockMoonOres.blockID - 256)																																																																.setItemName("moonBlockOres");
		GCMoonBlocks.cheeseBlock = 										new GCMoonBlockCheese				(GCMoonConfigManager.idBlockCheese, 					10)						.setHardness(0.5F)		.setStepSound(Block.soundClothFootstep)																																				.setBlockName("cheeseBlock");
	}

	public static void setHarvestLevels()
	{
		// TODO
	}
	
	public static void registerBlocks()
	{
		GameRegistry.registerBlock(GCMoonBlocks.moonStone);
		GameRegistry.registerBlock(GCMoonBlocks.moonDirt);
		GameRegistry.registerBlock(GCMoonBlocks.moonGrass);
		GameRegistry.registerBlock(GCMoonBlocks.cheeseBlock);
	}

	@SideOnly(Side.CLIENT)
	public static void addNames()
	{
		GCMoonBlocks.addName(GCMoonBlocks.moonStone);
		GCMoonBlocks.addName(GCMoonBlocks.moonDirt);
		GCMoonBlocks.addName(GCMoonBlocks.moonGrass);
		GCMoonBlocks.addNameWithMetadata("tile.moonBlockOres.aluminummoon.name");
		GCMoonBlocks.addNameWithMetadata("tile.moonBlockOres.ironmoon.name");
		GCMoonBlocks.addNameWithMetadata("tile.moonBlockOres.cheesestone.name");
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
