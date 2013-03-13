package micdoodle8.mods.galacticraft.moon.blocks;

import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.moon.GCMoonConfigManager;
import micdoodle8.mods.galacticraft.moon.items.GCMoonItemBlock;
import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import cpw.mods.fml.common.registry.GameRegistry;

public class GCMoonBlocks
{
	public static Block blockMoon;
    public static Block cheeseBlock;

	public static void initBlocks()
	{
		GCMoonBlocks.blockMoon = 										new GCMoonBlock						(GCMoonConfigManager.idBlock)												.setHardness(3.0F) 														.setUnlocalizedName("moonBlock");
		GCMoonBlocks.cheeseBlock = 										new GCMoonBlockCheese				(GCMoonConfigManager.idBlockCheese)											.setHardness(0.5F)		.setStepSound(Block.soundClothFootstep)			.setUnlocalizedName("cheeseBlock");
	}

	public static void setHarvestLevels()
	{
		// TODO
	}

	public static void registerBlocks()
	{
		GameRegistry.registerBlock(GCMoonBlocks.blockMoon, 		GCMoonItemBlock.class, 	GCMoonBlocks.blockMoon.getUnlocalizedName(),	 	GalacticraftCore.MODID);
		GameRegistry.registerBlock(GCMoonBlocks.cheeseBlock, 	ItemBlock.class, 		GCMoonBlocks.cheeseBlock.getUnlocalizedName(), 		GalacticraftCore.MODID);
	}
}
