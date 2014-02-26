package micdoodle8.mods.galacticraft.planets.mars.blocks;

import micdoodle8.mods.galacticraft.planets.GalacticraftPlanets;
import micdoodle8.mods.galacticraft.planets.mars.items.ItemBlockMachine;
import micdoodle8.mods.galacticraft.planets.mars.items.ItemBlockMars;
import micdoodle8.mods.galacticraft.planets.mars.items.ItemBlockSlimelingEgg;
import micdoodle8.mods.galacticraft.planets.mars.items.ItemBlockTintedGlassPane;
import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import cpw.mods.fml.common.registry.GameRegistry;

/**
 * GCMarsBlocks.java
 * 
 * This file is part of the Galacticraft project
 * 
 * @author micdoodle8
 * @license Lesser GNU Public License v3 (http://www.gnu.org/licenses/lgpl.html)
 * 
 */
public class MarsBlocks
{
	public static Block marsBlock;
	public static Block blockSludge;
	public static Block vine;
	public static Block rock;
	public static Block tier2TreasureChest;
	public static Block machine;
	public static Block creeperEgg;
	public static Block tintedGlassPane;

	public static void initBlocks()
	{
		MarsBlocks.marsBlock = new BlockMars().setHardness(2.2F).setBlockName("mars");
		MarsBlocks.vine = new BlockVines().setHardness(0.1F).setBlockName("cavernVines");
		MarsBlocks.rock = new BlockSlimelingEgg().setHardness(0.75F).setBlockName("slimelingEgg");
		MarsBlocks.tier2TreasureChest = new BlockTreasureChestTier2().setHardness(2.5F).setBlockName("treasureT2");
		MarsBlocks.machine = new BlockMachineMars().setHardness(1.8F).setBlockName("marsMachine");
		MarsBlocks.creeperEgg = new BlockCreeperEgg().setHardness(-1.0F).setBlockName("creeperEgg");
		MarsBlocks.tintedGlassPane = new BlockTintedGlassPane().setHardness(0.6F).setBlockName("tintedGlassPane");
		
		MarsBlocks.registerBlocks();
	}

//	public static void setHarvestLevels()
//	{
//		MinecraftForge.setBlockHarvestLevel(GCMarsBlocks.marsBlock, "pickaxe", 1);
//		MinecraftForge.setBlockHarvestLevel(GCMarsBlocks.rock, "pickaxe", 3);
//	}

	private static void registerBlocks()
	{
		GameRegistry.registerBlock(MarsBlocks.marsBlock, ItemBlockMars.class, MarsBlocks.marsBlock.getUnlocalizedName(), GalacticraftPlanets.MOD_ID);
		GameRegistry.registerBlock(MarsBlocks.vine, ItemBlock.class, MarsBlocks.vine.getUnlocalizedName(), GalacticraftPlanets.MOD_ID);
		GameRegistry.registerBlock(MarsBlocks.rock, ItemBlockSlimelingEgg.class, MarsBlocks.rock.getUnlocalizedName(), GalacticraftPlanets.MOD_ID);
		GameRegistry.registerBlock(MarsBlocks.tier2TreasureChest, ItemBlock.class, MarsBlocks.tier2TreasureChest.getUnlocalizedName(), GalacticraftPlanets.MOD_ID);
		GameRegistry.registerBlock(MarsBlocks.machine, ItemBlockMachine.class, MarsBlocks.machine.getUnlocalizedName(), GalacticraftPlanets.MOD_ID);
		GameRegistry.registerBlock(MarsBlocks.creeperEgg, ItemBlock.class, MarsBlocks.creeperEgg.getUnlocalizedName(), GalacticraftPlanets.MOD_ID);
		GameRegistry.registerBlock(MarsBlocks.tintedGlassPane, ItemBlockTintedGlassPane.class, MarsBlocks.tintedGlassPane.getUnlocalizedName(), GalacticraftPlanets.MOD_ID);
	}
}
