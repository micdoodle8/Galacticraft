package micdoodle8.mods.galacticraft.core.util;

import micdoodle8.mods.galacticraft.core.blocks.GCCoreBlocks;
import micdoodle8.mods.galacticraft.core.items.GCCoreItemFlag;
import micdoodle8.mods.galacticraft.core.items.GCCoreItemParachute;
import micdoodle8.mods.galacticraft.core.items.GCCoreItems;
import micdoodle8.mods.galacticraft.core.tile.GCCoreInventoryRocketBench;
import micdoodle8.mods.galacticraft.moon.items.GCMoonItems;
import net.minecraft.block.Block;
import net.minecraft.block.BlockChest;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraftforge.oredict.ShapedOreRecipe;

public class RecipeUtil 
{
	public static void addCraftingRecipes()
	{
		CraftingManager.getInstance().getRecipeList().add(new ShapedOreRecipe(new ItemStack(GCCoreItems.rocketEngine, 1), new Object[] {
			" YV",
			"XWX",
			"XZX",
			'V', Block.stoneButton,
			'W', new ItemStack(GCCoreItems.canister, 1, 0),
			'X', GCCoreItems.heavyPlating,
			'Y', Item.flintAndSteel,
			'Z', GCCoreItems.airVent
		}));
		CraftingManager.getInstance().getRecipeList().add(new ShapedOreRecipe(new ItemStack(GCCoreItems.rocketEngine, 1), new Object[] {
			"VY ",
			"XWX",
			"XZX",
			'V', Block.stoneButton,
			'W', new ItemStack(GCCoreItems.canister, 1, 0),
			'X', GCCoreItems.heavyPlating,
			'Y', Item.flintAndSteel,
			'Z', GCCoreItems.airVent
		}));
		CraftingManager.getInstance().getRecipeList().add(new ShapedOreRecipe(new ItemStack(GCCoreItems.heavyPlating, 2), new Object[] {
			"XYZ",
			"XYZ",
			"XYZ",
			'X', "ingotTitanium",
			'Y', "ingotCopper",
			'Z', "ingotAluminium"
		}));
		CraftingManager.getInstance().getRecipeList().add(new ShapedOreRecipe(new ItemStack(GCCoreItems.rocketNoseCone, 1), new Object[] {
			" Y ",
			" X ",
			"X X",
			'X', GCCoreItems.heavyPlating,
			'Y', Block.torchRedstoneActive
		}));
		CraftingManager.getInstance().getRecipeList().add(new ShapedOreRecipe(new ItemStack(GCCoreItems.rocketFins, 1), new Object[] {
			" Y ",
			"XYX",
			"X X",
			'X', GCCoreItems.heavyPlating,
			'Y', "ingotAluminium"
		}));
		CraftingManager.getInstance().getRecipeList().add(new ShapedOreRecipe(new ItemStack(GCCoreBlocks.landingPad, 9), new Object[] {
			"YYY",
			"XXX",
			'X', Block.blockSteel,
			'Y', "ingotAluminium"
		}));
		CraftingManager.getInstance().getRecipeList().add(new ShapedOreRecipe(new ItemStack(GCCoreBlocks.airDistributor, 1), new Object[] {
			"WXW",
			"YZY",
			"WXW",
			'W', "ingotAluminium",
			'X', GCCoreItems.airFan,
			'Y', GCCoreItems.airVent,
			'Z', "ingotTitanium"
		}));
		CraftingManager.getInstance().getRecipeList().add(new ShapedOreRecipe(new ItemStack(GCCoreBlocks.blockAirCollector, 1), new Object[] {
			"WWW",
			"YXZ",
			"WVW",
			'W', GCCoreItems.oxygenConcentrator,
			'W', "ingotTitanium",
			'X', new ItemStack(GCCoreItems.canister, 1, 0),
			'Y', GCCoreItems.airFan,
			'Z', GCCoreItems.airVent
		}));
		CraftingManager.getInstance().getRecipeList().add(new ShapedOreRecipe(new ItemStack(GCCoreBlocks.rocketBench, 1), new Object[] {
			"XXX",
			"YZY",
			"YYY",
			'X', "ingotAluminium",
			'Y', Block.planks,
			'Z', Block.workbench
		}));
		CraftingManager.getInstance().getRecipeList().add(new ShapedOreRecipe(new ItemStack(GCCoreBlocks.oxygenPipe, 1), new Object[] {
			"XXX",
			"   ",
			"XXX",
			'X', Block.thinGlass
		}));
		CraftingManager.getInstance().getRecipeList().add(new ShapedOreRecipe(new ItemStack(GCCoreItems.lightOxygenTankFull, 1), new Object[] {
			"Z",
			"X",
			"Y",
			'X', new ItemStack(GCCoreItems.canister, 1, 0),
			'Y', "ingotCopper",
			'Z', new ItemStack(Block.cloth, 1, 5)
		}));
		CraftingManager.getInstance().getRecipeList().add(new ShapedOreRecipe(new ItemStack(GCCoreItems.medOxygenTankFull, 1), new Object[] {
			"ZZ",
			"XX",
			"YY",
			'X', new ItemStack(GCCoreItems.canister, 1, 0),
			'Y', "ingotAluminium",
			'Z', new ItemStack(Block.cloth, 1, 1)
		}));
		CraftingManager.getInstance().getRecipeList().add(new ShapedOreRecipe(new ItemStack(GCCoreItems.heavyOxygenTankFull, 1), new Object[] {
			"ZZZ",
			"XXX",
			"YYY",
			'X', new ItemStack(GCCoreItems.canister, 1, 0),
			'Y', "ingotTitanium",
			'Z', new ItemStack(Block.cloth, 1, 14)
		}));
		CraftingManager.getInstance().getRecipeList().add(new ShapedOreRecipe(new ItemStack(GCCoreItems.sensorGlasses, 1), new Object[] {
			"YYY",
			"Y Y",
			"XYX",
			'X', GCCoreItems.sensorLens,
			'Y', GCMoonItems.meteoricIronIngot
		}));
		CraftingManager.getInstance().getRecipeList().add(new ShapedOreRecipe(new ItemStack(GCCoreItems.sensorLens, 1), new Object[] {
			"ZYZ",
			"YXY",
			"ZYZ",
			'X', Block.thinGlass,
			'Y', GCMoonItems.meteoricIronIngot,
			'Z', Item.redstone
		}));
		CraftingManager.getInstance().getRecipeList().add(new ShapedOreRecipe(new ItemStack(GCCoreItems.canister, 1, 0), new Object[] {
			"X X",
			"X X",
			"XXX",
			'X', "ingotAluminium"
		}));
		CraftingManager.getInstance().getRecipeList().add(new ShapedOreRecipe(new ItemStack(GCCoreItems.canister, 1, 1), new Object[] {
			"X X",
			"X X",
			"XXX",
			'X', "ingotCopper"
		}));
		CraftingManager.getInstance().getRecipeList().add(new ShapedOreRecipe(new ItemStack(GCCoreItems.airVent, 1), new Object[] {
			"XX",
			"XX",
			'X', "ingotAluminium"
		}));
		CraftingManager.getInstance().getRecipeList().add(new ShapedOreRecipe(new ItemStack(GCCoreItems.airFan, 1), new Object[] {
			"Z Z",
			" Y ",
			"ZXZ",
			'X', Item.redstone,
			'Y', "ingotTitanium",
			'Z', "ingotAluminium"
		}));
		CraftingManager.getInstance().getRecipeList().add(new ShapedOreRecipe(new ItemStack(GCCoreItems.oxygenMask, 1), new Object[] {
			"XXX",
			"XYX",
			"XXX",
			'X', Block.thinGlass,
			'Y', Item.helmetSteel
		}));
		CraftingManager.getInstance().getRecipeList().add(new ShapedOreRecipe(new ItemStack(GCCoreItems.oxygenConcentrator, 1), new Object[] {
			"ZWZ",
			"WYW",
			"ZXZ",
			'W', "ingotAluminium",
			'X', GCCoreItems.airVent,
			'Y', new ItemStack(GCCoreItems.canister, 1, 0),
			'Z', "ingotTitanium"
		}));
		CraftingManager.getInstance().getRecipeList().add(new ShapedOreRecipe(new ItemStack(GCCoreItems.titaniumPickaxe, 1), new Object[] {
			"YYY",
			" X ",
			" X ",
			'Y', "ingotTitanium",
			'X', Item.stick
		}));
		CraftingManager.getInstance().getRecipeList().add(new ShapedOreRecipe(new ItemStack(GCCoreItems.titaniumAxe, 1), new Object[] {
			"YY ",
			"YX ",
			" X ",
			'Y', "ingotTitanium",
			'X', Item.stick
		}));
		CraftingManager.getInstance().getRecipeList().add(new ShapedOreRecipe(new ItemStack(GCCoreItems.titaniumAxe, 1), new Object[] {
			" YY",
			" XY",
			" X ",
			'Y', "ingotTitanium",
			'X', Item.stick
		}));
		CraftingManager.getInstance().getRecipeList().add(new ShapedOreRecipe(new ItemStack(GCCoreItems.titaniumHoe, 1), new Object[] {
			" YY",
			" X ",
			" X ",
			'Y', "ingotTitanium",
			'X', Item.stick
		}));
		CraftingManager.getInstance().getRecipeList().add(new ShapedOreRecipe(new ItemStack(GCCoreItems.titaniumHoe, 1), new Object[] {
			"YY ",
			" X ",
			" X ",
			'Y', "ingotTitanium",
			'X', Item.stick
		}));
		CraftingManager.getInstance().getRecipeList().add(new ShapedOreRecipe(new ItemStack(GCCoreItems.titaniumSpade, 1), new Object[] {
			" Y ",
			" X ",
			" X ",
			'Y', "ingotTitanium",
			'X', Item.stick
		}));
		CraftingManager.getInstance().getRecipeList().add(new ShapedOreRecipe(new ItemStack(GCCoreItems.titaniumSword, 1), new Object[] {
			" Y ",
			" Y ",
			" X ",
			'Y', "ingotTitanium",
			'X', Item.stick
		}));
		CraftingManager.getInstance().getRecipeList().add(new ShapedOreRecipe(new ItemStack(GCCoreItems.titaniumBoots, 1), new Object[] {
			"X X",
			"X X",
			'X', "ingotTitanium"
		}));
		CraftingManager.getInstance().getRecipeList().add(new ShapedOreRecipe(new ItemStack(GCCoreItems.titaniumChestplate, 1), new Object[] {
			"X X",
			"XXX",
			"XXX",
			'X', "ingotTitanium"
		}));
		CraftingManager.getInstance().getRecipeList().add(new ShapedOreRecipe(new ItemStack(GCCoreItems.titaniumLeggings, 1), new Object[] {
			"XXX",
			"X X",
			"X X",
			'X', "ingotTitanium"
		}));
		CraftingManager.getInstance().getRecipeList().add(new ShapedOreRecipe(new ItemStack(GCCoreItems.titaniumHelmet, 1), new Object[] {
			"XXX",
			"X X",
			'X', "ingotTitanium"
		}));
		CraftingManager.getInstance().getRecipeList().add(new ShapedOreRecipe(new ItemStack(GCCoreItems.canvas, 1), new Object[] {
			" XY",
			"XXX",
			"YX ",
			'Y', Item.stick, 'X', Item.silk
		}));
		CraftingManager.getInstance().getRecipeList().add(new ShapedOreRecipe(new ItemStack(GCCoreItems.parachute, 1, 0), new Object[] {
			"XXX",
			"Y Y",
			" Y ",
			'X', GCCoreItems.canvas, 'Y', Item.silk
		}));
		
        for (int var2 = 0; var2 < 16; ++var2)
        {
        	CraftingManager.getInstance().addShapelessRecipe(new ItemStack(GCCoreItems.parachute, 1, GCCoreItemParachute.getParachuteDamageValueFromDye(var2)), new Object[] {new ItemStack(Item.dyePowder, 1, var2), new ItemStack(GCCoreItems.parachute, 1, 0)});
        }
        
		CraftingManager.getInstance().getRecipeList().add(new ShapedOreRecipe(new ItemStack(GCCoreItems.flagPole, 2, 0), new Object[] {
			"X",
			"X",
			"X",
			'X', "ingotAluminium"
		}));
		CraftingManager.getInstance().getRecipeList().add(new ShapedOreRecipe(new ItemStack(GCCoreItems.flag, 1, 16), new Object[] {
			"XYY",
			"XYY",
			"X  ",
			'X', GCCoreItems.flagPole, 'Y', GCCoreItems.canvas
		}));
		
        for (int var2 = 0; var2 < 16; ++var2)
        {
        	CraftingManager.getInstance().addShapelessRecipe(new ItemStack(GCCoreItems.flag, 1, GCCoreItemFlag.getFlagDamageValueFromDye(var2)), new Object[] {new ItemStack(Item.dyePowder, 1, var2), new ItemStack(GCCoreItems.flag, 1, 16)});
        }
        
		CraftingManager.getInstance().getRecipeList().add(new ShapedOreRecipe(new ItemStack(GCCoreItems.oxygenGear), new Object[] {
			" Y ",
			"YXY",
			"Y Y",
			'X', GCCoreItems.oxygenConcentrator, 'Y', GCCoreBlocks.oxygenPipe
		}));
		
		CraftingManager.getInstance().getRecipeList().add(new ShapedOreRecipe(new ItemStack(GCCoreBlocks.decorationBlocks, 1, 0), new Object[] {
			"XXX",
			"XXX",
			"XXX",
			'X', "ingotCopper"
		}));
		
		CraftingManager.getInstance().getRecipeList().add(new ShapedOreRecipe(new ItemStack(GCCoreBlocks.decorationBlocks, 1, 1), new Object[] {
			"XXX",
			"XXX",
			"XXX",
			'X', "ingotAluminium"
		}));
		
		CraftingManager.getInstance().getRecipeList().add(new ShapedOreRecipe(new ItemStack(GCCoreBlocks.decorationBlocks, 1, 2), new Object[] {
			"XXX",
			"XXX",
			"XXX",
			'X', "ingotTitanium"
		}));
		
		CraftingManager.getInstance().getRecipeList().add(new ShapedOreRecipe(new ItemStack(GCCoreBlocks.decorationBlocks, 4, 3), new Object[] {
			"   ",
			" XY",
			"   ",
			'X', new ItemStack(Block.stone, 4, 0), 'Y', "ingotAluminium"
		}));
		
		CraftingManager.getInstance().getRecipeList().add(new ShapedOreRecipe(new ItemStack(GCCoreBlocks.decorationBlocks, 4, 4), new Object[] {
			"   ",
			" X ",
			" Y ",
			'X', new ItemStack(Block.stone, 4, 0), 'Y', "ingotAluminium"
		}));
		
		CraftingManager.getInstance().getRecipeList().add(new ShapedOreRecipe(new ItemStack(GCCoreBlocks.airLockFrame, 2), new Object[] {
			"XXX",
			"YVY",
			"ZWZ",
			'V', GCCoreBlocks.oxygenPipe, 'W', Item.redstone, 'X', GCMoonItems.meteoricIronIngot, 'Y', "ingotTitanium", 'Z', GCCoreItems.oxygenConcentrator
		}));
		
		CraftingManager.getInstance().getRecipeList().add(new ShapedOreRecipe(new ItemStack(GCCoreItems.oilExtractor), new Object[] {
			"X  ",
			" XY",
			"ZYY",
			'X', "ingotAluminium", 'Y', "ingotCopper", 'Z', Item.redstone
		}));
		
		CraftingManager.getInstance().getRecipeList().add(new ShapedOreRecipe(new ItemStack(GCCoreItems.oilCanister, 1, 61), new Object[] {
			"WXW",
			"WYW",
			"WZW",
			'X', "ingotTitanium", 'Y', Block.glass, 'Z', new ItemStack(GCCoreItems.canister, 1, 0), 'W', GCCoreItems.ingotAluminum
		}));
		
		CraftingManager.getInstance().getRecipeList().add(new ShapedOreRecipe(new ItemStack(GCCoreBlocks.refinery), new Object[] {
			" Z ",
			"WZW",
			"XYX",
			'X', "ingotAluminium", 'Y', Block.stoneOvenIdle, 'Z', new ItemStack(GCCoreItems.canister, 1, 1), 'W', Block.stone
		}));
	}
	
	public static void addSmeltingRecipes()
	{
		FurnaceRecipes.smelting().addSmelting(GCCoreBlocks.blockOres.blockID, 0, new ItemStack(GCCoreItems.ingotCopper), 0.1F);
		FurnaceRecipes.smelting().addSmelting(GCCoreBlocks.blockOres.blockID, 1, new ItemStack(GCCoreItems.ingotAluminum), 0.3F);
		FurnaceRecipes.smelting().addSmelting(GCCoreBlocks.blockOres.blockID, 2, new ItemStack(GCCoreItems.ingotTitanium), 1.0F);
	}
	
	public static ItemStack findMatchingSpaceshipRecipe(GCCoreInventoryRocketBench inventoryRocketBench)
	{
		final ItemStack[] slots = new ItemStack[18];
		
		for (int i = 0; i < 18; i++)
		{
			slots[i] = inventoryRocketBench.getStackInSlot(i + 1);
		}
		
		if (slots[0] != null && slots[1] != null && slots[2] != null && slots[3] != null && slots[4] != null && slots[5] != null && slots[6] != null && slots[7] != null && slots[8] != null && slots[9] != null && slots[10] != null && slots[11] != null && slots[12] != null && slots[13] != null)
		{
			if (slots[0].getItem().itemID == GCCoreItems.rocketNoseCone.itemID)
			{
				int platesInPlace = 0;
				
				for (int i = 1; i < 9; i++)
				{
					if (slots[i].getItem().itemID == GCCoreItems.heavyPlating.itemID)
					{
						platesInPlace++;
					}
				}
				
				if (platesInPlace == 8)
				{
					if (slots[9].getItem().itemID == GCCoreItems.rocketFins.itemID && slots[10].getItem().itemID == GCCoreItems.rocketFins.itemID)
					{
						if (slots[12].getItem().itemID == GCCoreItems.rocketFins.itemID && slots[13].getItem().itemID == GCCoreItems.rocketFins.itemID)
						{
							if (slots[11].getItem().itemID == GCCoreItems.rocketEngine.itemID)
							{
								int type = 0;
								
								if ((slots[14] != null && Block.blocksList[slots[14].getItem().itemID] instanceof BlockChest) || (slots[15] != null && Block.blocksList[slots[15].getItem().itemID] instanceof BlockChest) || (slots[16] != null && Block.blocksList[slots[16].getItem().itemID] instanceof BlockChest))
								{
									type = 1;
								}
									
								return new ItemStack(GCCoreItems.spaceship, 1, type);
							}
						}
					}
				}
			}
		}
		
		return null;
	}
}
