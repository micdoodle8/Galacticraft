package micdoodle8.mods.galacticraft.mars.blocks;

import micdoodle8.mods.galacticraft.mars.GCMarsConfigManager;
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
import net.minecraftforge.common.MinecraftForge;
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
		GCMarsBlocks.marsBlock = new GCMarsBlock(GCMarsConfigManager.idBlockMars).setHardness(2.2F).setUnlocalizedName("mars");
		GCMarsBlocks.blockSludge = new GCMarsBlockSludge(GalacticraftMars.SLUDGE.getBlockID(), GCMarsBlocks.bacterialSludge).setUnlocalizedName("sludge");
		GCMarsBlocks.vine = new GCMarsBlockVine(GCMarsConfigManager.idBlockVine).setHardness(0.1F).setUnlocalizedName("cavernVines");
		GCMarsBlocks.rock = new GCMarsBlockSlimelingEgg(GCMarsConfigManager.idBlockRock).setHardness(0.75F).setUnlocalizedName("slimelingEgg");
		GCMarsBlocks.tier2TreasureChest = new GCMarsBlockT2TreasureChest(GCMarsConfigManager.idBlockTreasureChestT2).setHardness(2.5F).setUnlocalizedName("treasureT2");
		GCMarsBlocks.machine = new GCMarsBlockMachine(GCMarsConfigManager.idBlockMachine).setHardness(1.8F).setUnlocalizedName("marsMachine");
		GCMarsBlocks.creeperEgg = new GCMarsBlockCreeperEgg(GCMarsConfigManager.idBlockCreeperEgg).setHardness(-1.0F).setUnlocalizedName("creeperEgg");
		GCMarsBlocks.tintedGlassPane = new GCMarsBlockTintedGlassPane(GCMarsConfigManager.idBlockTintedGlassPane).setHardness(0.6F).setUnlocalizedName("tintedGlassPane");
	}

	public static void setHarvestLevels()
	{
		MinecraftForge.setBlockHarvestLevel(GCMarsBlocks.marsBlock, "pickaxe", 1);
		MinecraftForge.setBlockHarvestLevel(GCMarsBlocks.rock, "pickaxe", 3);
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
