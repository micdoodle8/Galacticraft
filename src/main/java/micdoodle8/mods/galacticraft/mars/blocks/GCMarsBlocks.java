package micdoodle8.mods.galacticraft.mars.blocks;

import micdoodle8.mods.galacticraft.mars.GalacticraftMars;
import micdoodle8.mods.galacticraft.mars.items.GCMarsItemBlock;
import micdoodle8.mods.galacticraft.mars.items.GCMarsItemBlockMachine;
import micdoodle8.mods.galacticraft.mars.items.GCMarsItemBlockRock;
import micdoodle8.mods.galacticraft.mars.items.GCMarsItemBlockTintedGlassPane;
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
public class GCMarsBlocks
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
		GCMarsBlocks.marsBlock = new GCMarsBlock().setHardness(2.2F).setBlockName("mars");
		GCMarsBlocks.blockSludge = new GCMarsBlockSludge(GCMarsBlocks.bacterialSludge).setBlockName("sludge");
		GCMarsBlocks.vine = new GCMarsBlockVine().setHardness(0.1F).setBlockName("cavernVines");
		GCMarsBlocks.rock = new GCMarsBlockSlimelingEgg().setHardness(0.75F).setBlockName("slimelingEgg");
		GCMarsBlocks.tier2TreasureChest = new GCMarsBlockT2TreasureChest().setHardness(2.5F).setBlockName("treasureT2");
		GCMarsBlocks.machine = new GCMarsBlockMachine().setHardness(1.8F).setBlockName("marsMachine");
		GCMarsBlocks.creeperEgg = new GCMarsBlockCreeperEgg().setHardness(-1.0F).setBlockName("creeperEgg");
		GCMarsBlocks.tintedGlassPane = new GCMarsBlockTintedGlassPane().setHardness(0.6F).setBlockName("tintedGlassPane");
	}

	public static void setHarvestLevels()
	{
//		MinecraftForge.setBlockHarvestLevel(GCMarsBlocks.marsBlock, "pickaxe", 1);
//		MinecraftForge.setBlockHarvestLevel(GCMarsBlocks.rock, "pickaxe", 3); TODO Harvest levels
	}

	public static void registerBlocks()
	{
		GameRegistry.registerBlock(GCMarsBlocks.marsBlock, GCMarsItemBlock.class, GCMarsBlocks.marsBlock.getUnlocalizedName(), GalacticraftMars.MODID);
		GameRegistry.registerBlock(GCMarsBlocks.blockSludge, ItemBlock.class, GCMarsBlocks.blockSludge.getUnlocalizedName(), GalacticraftMars.MODID);
		GameRegistry.registerBlock(GCMarsBlocks.vine, ItemBlock.class, GCMarsBlocks.vine.getUnlocalizedName(), GalacticraftMars.MODID);
		GameRegistry.registerBlock(GCMarsBlocks.rock, GCMarsItemBlockRock.class, GCMarsBlocks.rock.getUnlocalizedName(), GalacticraftMars.MODID);
		GameRegistry.registerBlock(GCMarsBlocks.tier2TreasureChest, ItemBlock.class, GCMarsBlocks.tier2TreasureChest.getUnlocalizedName(), GalacticraftMars.MODID);
		GameRegistry.registerBlock(GCMarsBlocks.machine, GCMarsItemBlockMachine.class, GCMarsBlocks.machine.getUnlocalizedName(), GalacticraftMars.MODID);
		GameRegistry.registerBlock(GCMarsBlocks.creeperEgg, ItemBlock.class, GCMarsBlocks.creeperEgg.getUnlocalizedName(), GalacticraftMars.MODID);
		GameRegistry.registerBlock(GCMarsBlocks.tintedGlassPane, GCMarsItemBlockTintedGlassPane.class, GCMarsBlocks.tintedGlassPane.getUnlocalizedName(), GalacticraftMars.MODID);
	}
}
