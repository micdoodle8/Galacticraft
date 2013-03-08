package micdoodle8.mods.galacticraft.moon.blocks;

import micdoodle8.mods.galacticraft.moon.GCMoonConfigManager;
import micdoodle8.mods.galacticraft.moon.items.GCMoonItemBlock;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class GCMoonBlocks
{
	public static Block blockMoon;
    public static Block cheeseBlock;

	public static void initBlocks()
	{
		GCMoonBlocks.blockMoon = 										new GCMoonBlock						(GCMoonConfigManager.idBlock)												.setHardness(3.0F) 																				 																											.setBlockName("moonBlock");
		Item.itemsList[GCMoonBlocks.blockMoon.blockID] = 				new GCMoonItemBlock					(GCMoonBlocks.blockMoon.blockID - 256)																																																																.setItemName("moonBlock");
		GCMoonBlocks.cheeseBlock = 										new GCMoonBlockCheese				(GCMoonConfigManager.idBlockCheese, 					10)						.setHardness(0.5F)		.setStepSound(Block.soundClothFootstep)																																				.setBlockName("cheeseBlock");
	}

	public static void setHarvestLevels()
	{
		// TODO
	}

	public static void registerBlocks()
	{
		GameRegistry.registerBlock(GCMoonBlocks.cheeseBlock);
	}

	@SideOnly(Side.CLIENT)
	public static void addNames()
	{
//		GCMoonBlocks.addNameWithMetadata("tile.moonBlock.moonstone.name");
//		GCMoonBlocks.addNameWithMetadata("tile.moonBlock.moondirt.name");
//		GCMoonBlocks.addNameWithMetadata("tile.moonBlock.moongrass.name");
//		GCMoonBlocks.addNameWithMetadata("tile.moonBlock.aluminummoon.name");
//		GCMoonBlocks.addNameWithMetadata("tile.moonBlock.ironmoon.name");
//		GCMoonBlocks.addNameWithMetadata("tile.moonBlock.cheesestone.name");
	}

//	private static void addName(Block block)
//	{
//		LanguageRegistry.instance().addStringLocalization(block.getBlockName() + ".name", ClientProxyMoon.lang.get(block.getBlockName() + ".name"));
//	}
//
//	private static void addNameWithMetadata(String string)
//	{
//		LanguageRegistry.instance().addStringLocalization(string, ClientProxyMoon.lang.get(string));
//	}
}
