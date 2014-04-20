package micdoodle8.mods.galacticraft.planets.mars.blocks;

import micdoodle8.mods.galacticraft.planets.GalacticraftPlanets;
import micdoodle8.mods.galacticraft.planets.mars.MarsModule;
import micdoodle8.mods.galacticraft.planets.mars.items.ItemBlockMars;
import micdoodle8.mods.galacticraft.planets.mars.items.ItemBlockMachine;
import micdoodle8.mods.galacticraft.planets.mars.items.ItemBlockEgg;
import micdoodle8.mods.galacticraft.planets.mars.items.ItemBlockTintedGlassPane;
import net.minecraft.block.Block;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialLiquid;
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

	public static Material bacterialSludge = new MaterialLiquid(MapColor.foliageColor);

	public static void initBlocks()
	{
		MarsBlocks.marsBlock = new BlockBasicMars().setHardness(2.2F).setBlockName("mars");
		MarsBlocks.blockSludge = new BlockSludge(MarsBlocks.bacterialSludge).setBlockName("sludge");
		MarsBlocks.vine = new BlockCavernousVine().setHardness(0.1F).setBlockName("cavernVines");
		MarsBlocks.rock = new BlockSlimelingEgg().setHardness(0.75F).setBlockName("slimelingEgg");
		MarsBlocks.tier2TreasureChest = new BlockTier2TreasureChest().setHardness(2.5F).setBlockName("treasureT2");
		MarsBlocks.machine = new BlockMachineMars().setHardness(1.8F).setBlockName("marsMachine");
		MarsBlocks.creeperEgg = new BlockCreeperEgg().setHardness(-1.0F).setBlockName("creeperEgg");
		MarsBlocks.tintedGlassPane = new BlockTintedGlassPane().setHardness(0.6F).setBlockName("tintedGlassPane");
	}

	public static void setHarvestLevels()
	{
//		MinecraftForge.setBlockHarvestLevel(GCMarsBlocks.marsBlock, "pickaxe", 1);
//		MinecraftForge.setBlockHarvestLevel(GCMarsBlocks.rock, "pickaxe", 3); TODO Harvest levels
	}

	public static void registerBlocks()
	{
		GameRegistry.registerBlock(MarsBlocks.marsBlock, ItemBlockMars.class, MarsBlocks.marsBlock.getUnlocalizedName(), GalacticraftPlanets.MODID);
		GameRegistry.registerBlock(MarsBlocks.blockSludge, ItemBlock.class, MarsBlocks.blockSludge.getUnlocalizedName(), GalacticraftPlanets.MODID);
		GameRegistry.registerBlock(MarsBlocks.vine, ItemBlock.class, MarsBlocks.vine.getUnlocalizedName(), GalacticraftPlanets.MODID);
		GameRegistry.registerBlock(MarsBlocks.rock, ItemBlockEgg.class, MarsBlocks.rock.getUnlocalizedName(), GalacticraftPlanets.MODID);
		GameRegistry.registerBlock(MarsBlocks.tier2TreasureChest, ItemBlock.class, MarsBlocks.tier2TreasureChest.getUnlocalizedName(), GalacticraftPlanets.MODID);
		GameRegistry.registerBlock(MarsBlocks.machine, ItemBlockMachine.class, MarsBlocks.machine.getUnlocalizedName(), GalacticraftPlanets.MODID);
		GameRegistry.registerBlock(MarsBlocks.creeperEgg, ItemBlock.class, MarsBlocks.creeperEgg.getUnlocalizedName(), GalacticraftPlanets.MODID);
		GameRegistry.registerBlock(MarsBlocks.tintedGlassPane, ItemBlockTintedGlassPane.class, MarsBlocks.tintedGlassPane.getUnlocalizedName(), GalacticraftPlanets.MODID);
	}
}
