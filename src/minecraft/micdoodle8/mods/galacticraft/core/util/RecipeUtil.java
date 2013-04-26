package micdoodle8.mods.galacticraft.core.util;

import gregtechmod.api.GregTech_API;
import ic2.api.Items;

import java.util.ArrayList;
import java.util.List;

import micdoodle8.mods.galacticraft.core.blocks.GCCoreBlocks;
import micdoodle8.mods.galacticraft.core.inventory.GCCoreInventoryRocketBench;
import micdoodle8.mods.galacticraft.core.items.GCCoreItemFlag;
import micdoodle8.mods.galacticraft.core.items.GCCoreItemParachute;
import micdoodle8.mods.galacticraft.core.items.GCCoreItems;
import micdoodle8.mods.galacticraft.moon.items.GCMoonItems;
import net.minecraft.block.Block;
import net.minecraft.block.BlockChest;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.oredict.ShapelessOreRecipe;
import thermalexpansion.api.item.ItemRegistry;
import universalelectricity.components.common.BasicComponents;

public class RecipeUtil
{
	public static void addGregTechCraftingRecipes()
	{
		addRecipe(new ItemStack(GCCoreItems.rocketEngine, 1), new Object[] {
			" V ", 
			"XWX", 
			"XZX", 
				'V', new ItemStack(GCCoreItems.canister, 1, 0),
				'W', GregTech_API.getGregTechBlock(1, 1, 31),
				'X', GCCoreItems.heavyPlating,
				'Z', GregTech_API.getGregTechBlock(1, 1, 37)
			});
		
		addRecipe(new ItemStack(GCCoreItems.rocketNoseCone, 1), new Object[] {
			" Z ",
			" X ",
			"XYX",
			'X', GCCoreItems.heavyPlating,
			'Y', GregTech_API.getGregTechBlock(1, 1, 4),
			'Z', Items.getItem("reinforcedGlass"),
		});
		
		addRecipe(new ItemStack(GCCoreBlocks.oxygenPipe, 4), new Object[] {
			"XXX",
			"   ",
			"XXX",
			'X', Block.thinGlass
		});
		
		addRecipe(new ItemStack(GCCoreItems.lightOxygenTank, 1, GCCoreItems.lightOxygenTank.getMaxDamage()), new Object[] {
			"Z",
			"X",
			"Y",
			'X', new ItemStack(GCCoreItems.canister, 1, 0),
			'Y', "ingotCopper",
			'Z', new ItemStack(Block.cloth, 1, 5)
		});
		
		addRecipe(new ItemStack(GCCoreItems.medOxygenTank, 1, GCCoreItems.medOxygenTank.getMaxDamage()), new Object[] {
			"ZZ",
			"XX",
			"YY",
			'X', new ItemStack(GCCoreItems.canister, 1, 0),
			'Y', "ingotTin",
			'Z', new ItemStack(Block.cloth, 1, 1)
		});

		addRecipe(new ItemStack(GCCoreItems.sensorGlasses, 1), new Object[] {
			"YYY",
			"Y Y",
			"XYX",
			'X', GCCoreItems.sensorLens,
			'Y', GCMoonItems.meteoricIronIngot
		});
		
		addRecipe(new ItemStack(GCCoreItems.sensorLens, 1), new Object[] {
			"ZYZ",
			"YXY",
			"ZYZ",
			'X', Block.thinGlass,
			'Y', GCMoonItems.meteoricIronIngot,
			'Z', Item.redstone
		});
		
		addRecipe(new ItemStack(GCCoreItems.canister, 1, 0), new Object[] {
			"X X",
			"X X",
			"XXX",
			'X', "ingotTin"
		});
		
		addRecipe(new ItemStack(GCCoreItems.canister, 1, 1), new Object[] {
			"X X",
			"X X",
			"XXX",
			'X', "ingotCopper"
		});

		addRecipe(new ItemStack(GCCoreItems.oxygenMask, 1), new Object[] {
			"XXX",
			"XYX",
			"XXX",
			'X', Block.thinGlass,
			'Y', Item.helmetIron
		});

		addRecipe(new ItemStack(GCCoreItems.canvas, 1), new Object[] {
			" XY",
			"XXX",
			"YX ",
			'Y', Item.stick, 'X', Item.silk
		});
		
		addRecipe(new ItemStack(GCCoreItems.parachute, 1, 0), new Object[] {
			"XXX",
			"Y Y",
			" Y ",
			'X', GCCoreItems.canvas, 'Y', Item.silk
		});
		
		addRecipe(new ItemStack(GCCoreBlocks.enclosedWire, 1, 1), new Object[] {
			"XYX",
			'X', GCCoreBlocks.oxygenPipe, 'Y', new ItemStack(GCCoreBlocks.decorationBlocks, 1, 4)
		});


		addRecipe(new ItemStack(GCCoreItems.oxygenGear), new Object[] {
			" Y ",
			"YXY",
			"Y Y",
			'X', GCCoreItems.oxygenConcentrator, 'Y', GCCoreBlocks.oxygenPipe
		});

		addRecipe(new ItemStack(GCCoreBlocks.decorationBlocks, 4, 3), new Object[] {
			"   ",
			" XY",
			"   ",
			'X', new ItemStack(Block.stone, 4, 0), 'Y', "ingotTin"
		});

		addRecipe(new ItemStack(GCCoreBlocks.decorationBlocks, 4, 4), new Object[] {
			"   ",
			" X ",
			" Y ",
			'X', new ItemStack(Block.stone, 4, 0), 'Y', "ingotTin"
		});

		addRecipe(new ItemStack(GCCoreItems.flag, 1, 16), new Object[] {
			"XYY",
			"XYY",
			"X  ",
			'X', GCCoreItems.flagPole, 'Y', GCCoreItems.canvas
		});

        for (int var2 = 0; var2 < 16; ++var2)
        {
        	CraftingManager.getInstance().addShapelessRecipe(new ItemStack(GCCoreItems.parachute, 1, GCCoreItemParachute.getParachuteDamageValueFromDye(var2)), new Object[] {new ItemStack(Item.dyePowder, 1, var2), new ItemStack(GCCoreItems.parachute, 1, 0)});
        }

        for (int var2 = 0; var2 < 16; ++var2)
        {
        	CraftingManager.getInstance().addShapelessRecipe(new ItemStack(GCCoreItems.flag, 1, GCCoreItemFlag.getFlagDamageValueFromDye(var2)), new Object[] {new ItemStack(Item.dyePowder, 1, var2), new ItemStack(GCCoreItems.flag, 1, 16)});
        }
        
		addRecipe(new ItemStack(GCCoreItems.heavyPlating, 2), new Object[] {
			"X",
			"Y",
			"Z",
			'X', GregTech_API.getGregTechItem(0, 1, 83),
			'Y', GregTech_API.getGregTechItem(0, 1, 77),
			'Z', GregTech_API.getGregTechItem(0, 1, 75),
		});
		
		addRecipe(new ItemStack(GCCoreItems.rocketFins, 1), new Object[] {
			" Y ",
			"XYX",
			"X X",
			'X', GCCoreItems.heavyPlating,
			'Y', GregTech_API.getGregTechItem(0, 1, 78)
		});
		
		addRecipe(new ItemStack(GCCoreBlocks.landingPad, 9), new Object[] {
			"YYY",
			"XXX",
			'X', Block.blockIron,
			'Y', "plateIron"
		});
		
		addRecipe(new ItemStack(GCCoreBlocks.airDistributor, 1), new Object[] {
			"WXW",
			"YZY",
			"WVW",
			'V', GregTech_API.getGregTechItem(0, 1, 83),
			'W', "ingotSteel",
			'X', GCCoreItems.airFan,
			'Y', GCCoreItems.airVent,
			'Z', Items.getItem("machine")
		});
		
		addRecipe(new ItemStack(GCCoreBlocks.sealer, 1), new Object[] {
			"WZW",
			"YTX",
			"WUW",
			'T', Items.getItem("machine"),
			'V', "copperWire",
			'W', "ingotSteel",
			'X', GCCoreItems.airFan,
			'Y', GCCoreItems.airVent,
			'Z', GregTech_API.getGregTechItem(0, 1, 78),
			'U', GregTech_API.getGregTechItem(0, 1, 83)
		});
		
		addRecipe(new ItemStack(GCCoreBlocks.blockAirCollector, 1), new Object[] {
			"WVW",
			"YXZ",
			"WUW",
			'U', GregTech_API.getGregTechItem(0, 1, 83),
			'V', GCCoreItems.oxygenConcentrator,
			'W', "ingotSteel",
			'X', Items.getItem("machine"),
			'Y', GCCoreItems.airFan,
			'Z', GCCoreItems.airVent
		});
		
		addRecipe(new ItemStack(GCCoreBlocks.rocketBench, 1), new Object[] {
			"X Y",
			"ZAB",
			"DCD",
			'X', GregTech_API.getGregTechItem(42, 1, 1),
			'Y', GregTech_API.getGregTechItem(46, 1, 1),
			'Z', Items.getItem("diamondDrill"),
			'A', GregTech_API.getGregTechBlock(1, 1, 16),
			'B', Items.getItem("electricWrench"),
			'C', GregTech_API.getGregTechBlock(1, 1, 60),
			'D', GregTech_API.getGregTechBlock(1, 1, 10),
		});
		
		addRecipe(new ItemStack(GCCoreItems.heavyOxygenTank, 1, GCCoreItems.heavyOxygenTank.getMaxDamage()), new Object[] {
			"ZZZ",
			"XXX",
			"YYY",
			'X', new ItemStack(GCCoreItems.canister, 1, 0),
			'Y', "ingotSteel",
			'Z', new ItemStack(Block.cloth, 1, 14)
		});
		
		addRecipe(new ItemStack(GCCoreItems.airFan, 1), new Object[] {
			"Z Z",
			" Y ",
			"ZXZ",
			'X', Item.redstone,
			'Y', "motor",
			'Z', "ingotSteel"
		});
		
		addRecipe(new ItemStack(GCCoreItems.oxygenConcentrator, 1), new Object[] {
			"ZWZ",
			"WYW",
			"ZXZ",
			'W', "ingotTin",
			'X', GCCoreItems.airVent,
			'Y', new ItemStack(GCCoreItems.canister, 1, 0),
			'Z', "ingotSteel"
		});
		
		addRecipe(new ItemStack(GCCoreItems.steelPickaxe, 1), new Object[] {
			"YYY",
			" X ",
			" X ",
			'Y', "ingotSteel",
			'X', Item.stick
		});
		
		addRecipe(new ItemStack(GCCoreItems.steelAxe, 1), new Object[] {
			"YY ",
			"YX ",
			" X ",
			'Y', "ingotSteel",
			'X', Item.stick
		});
		
		addRecipe(new ItemStack(GCCoreItems.steelAxe, 1), new Object[] {
			" YY",
			" XY",
			" X ",
			'Y', "ingotSteel",
			'X', Item.stick
		});
		
		addRecipe(new ItemStack(GCCoreItems.steelHoe, 1), new Object[] {
			" YY",
			" X ",
			" X ",
			'Y', "ingotSteel",
			'X', Item.stick
		});
		
		addRecipe(new ItemStack(GCCoreItems.steelHoe, 1), new Object[] {
			"YY ",
			" X ",
			" X ",
			'Y', "ingotSteel",
			'X', Item.stick
		});
		
		addRecipe(new ItemStack(GCCoreItems.steelSpade, 1), new Object[] {
			" Y ",
			" X ",
			" X ",
			'Y', "ingotSteel",
			'X', Item.stick
		});
		
		addRecipe(new ItemStack(GCCoreItems.steelSword, 1), new Object[] {
			" Y ",
			" Y ",
			" X ",
			'Y', "ingotSteel",
			'X', Item.stick
		});
		
		addRecipe(new ItemStack(GCCoreItems.steelBoots, 1), new Object[] {
			"X X",
			"X X",
			'X', "ingotSteel"
		});
		
		addRecipe(new ItemStack(GCCoreItems.steelChestplate, 1), new Object[] {
			"X X",
			"XXX",
			"XXX",
			'X', "ingotSteel"
		});
		
		addRecipe(new ItemStack(GCCoreItems.steelLeggings, 1), new Object[] {
			"XXX",
			"X X",
			"X X",
			'X', "ingotSteel"
		});
		
		addRecipe(new ItemStack(GCCoreItems.steelHelmet, 1), new Object[] {
			"XXX",
			"X X",
			'X', "ingotSteel"
		});
		
		addRecipe(new ItemStack(GCCoreBlocks.enclosedWire, 1, 0), new Object[] {
			"XYX",
			'X', "copperWire", 'Y', new ItemStack(GCCoreBlocks.decorationBlocks, 1, 4)
		});

		addRecipe(new ItemStack(GCCoreItems.flagPole, 2, 0), new Object[] {
			"X",
			"X",
			"X",
			'X', "ingotSteel"
		});

    	CraftingManager.getInstance().getRecipeList().add(new ShapelessOreRecipe(new ItemStack(GCCoreItems.airVent, 1), new Object[] {
    		"ingotTin",
    		"ingotTin",
    		"ingotTin",
    		"ingotSteel"
    	}));

		addRecipe(new ItemStack(GCCoreBlocks.airLockFrame, 2), new Object[] {
			"XXX",
			"YVY",
			"ZWZ",
			'V', GCCoreBlocks.oxygenPipe, 'W', Item.redstone, 'X', GCMoonItems.meteoricIronIngot, 'Y', "ingotSteel", 'Z', GCCoreItems.oxygenConcentrator
		});

		addRecipe(new ItemStack(GCCoreItems.oilExtractor), new Object[] {
			"X  ",
			" XY",
			"ZYY",
			'X', "ingotSteel", 'Y', "ingotBronze", 'Z', Item.redstone
		});

		addRecipe(new ItemStack(GCCoreItems.oilCanister, 1, 61), new Object[] {
			"WXW",
			"WYW",
			"WZW",
			'X', "ingotSteel", 'Y', Block.glass, 'Z', new ItemStack(GCCoreItems.canister, 1, 0), 'W', "ingotTin"
		});

		addRecipe(new ItemStack(GCCoreBlocks.refinery), new Object[] {
			" Z ",
			"WYW",
			"XVX",
			'X', "ingotSteel", 'Y', Items.getItem("machine"), 'Z', new ItemStack(GCCoreItems.canister, 1, 1), 'W', Block.stone, 'V', GregTech_API.getGregTechItem(0, 1, 83)
		});

		addRecipe(new ItemStack(GCCoreBlocks.compressor), new Object[] {
			"XZX",
			"XWX",
			"XYX",
			'X', "ingotSteel", 'Y', GregTech_API.getGregTechItem(0, 1, 83), 'Z', GCCoreItems.oxygenConcentrator, 'W', Items.getItem("machine")
		});

		addRecipe(new ItemStack(GCCoreBlocks.fuelLoader), new Object[] {
			"XZX",
			"ZWZ",
			"XYX",
			'X', "ingotSteel", 'Y', GregTech_API.getGregTechItem(0, 1, 83), 'Z', Items.getItem("reinforcedGlass"), 'W', Items.getItem("machine")
		});
	}
	
	public static void addThermalExpansionCraftingRecipes()
	{
		addRecipe(new ItemStack(GCCoreItems.rocketEngine, 1), new Object[] {
			" YV", 
			"XWX", 
			"XZX", 
				'V', Block.stoneButton,
				'W', new ItemStack(GCCoreItems.canister, 1, 0),
				'X', GCCoreItems.heavyPlating,
				'Y', Item.flintAndSteel,
				'Z', GCCoreItems.airVent
			});

		addRecipe(new ItemStack(GCCoreItems.rocketEngine, 1), new Object[] {
			"VY ",
			"XWX",
			"XZX",
			'V', Block.stoneButton,
			'W', new ItemStack(GCCoreItems.canister, 1, 0),
			'X', GCCoreItems.heavyPlating,
			'Y', Item.flintAndSteel,
			'Z', GCCoreItems.airVent
		});
		
		addRecipe(new ItemStack(GCCoreItems.rocketNoseCone, 1), new Object[] {
			" Y ",
			" X ",
			"X X",
			'X', GCCoreItems.heavyPlating,
			'Y', Block.torchRedstoneActive
		});
		
		addRecipe(new ItemStack(GCCoreBlocks.oxygenPipe, 4), new Object[] {
			"XXX",
			"   ",
			"XXX",
			'X', Block.thinGlass
		});
		
		addRecipe(new ItemStack(GCCoreItems.lightOxygenTank, 1, GCCoreItems.lightOxygenTank.getMaxDamage()), new Object[] {
			"Z",
			"X",
			"Y",
			'X', new ItemStack(GCCoreItems.canister, 1, 0),
			'Y', "ingotCopper",
			'Z', new ItemStack(Block.cloth, 1, 5)
		});
		
		addRecipe(new ItemStack(GCCoreItems.medOxygenTank, 1, GCCoreItems.medOxygenTank.getMaxDamage()), new Object[] {
			"ZZ",
			"XX",
			"YY",
			'X', new ItemStack(GCCoreItems.canister, 1, 0),
			'Y', "ingotTin",
			'Z', new ItemStack(Block.cloth, 1, 1)
		});

		addRecipe(new ItemStack(GCCoreItems.sensorGlasses, 1), new Object[] {
			"YYY",
			"Y Y",
			"XYX",
			'X', GCCoreItems.sensorLens,
			'Y', GCMoonItems.meteoricIronIngot
		});
		
		addRecipe(new ItemStack(GCCoreItems.sensorLens, 1), new Object[] {
			"ZYZ",
			"YXY",
			"ZYZ",
			'X', Block.thinGlass,
			'Y', GCMoonItems.meteoricIronIngot,
			'Z', Item.redstone
		});
		
		addRecipe(new ItemStack(GCCoreItems.canister, 1, 0), new Object[] {
			"X X",
			"X X",
			"XXX",
			'X', "ingotTin"
		});
		
		addRecipe(new ItemStack(GCCoreItems.canister, 1, 1), new Object[] {
			"X X",
			"X X",
			"XXX",
			'X', "ingotCopper"
		});

		addRecipe(new ItemStack(GCCoreItems.oxygenMask, 1), new Object[] {
			"XXX",
			"XYX",
			"XXX",
			'X', Block.thinGlass,
			'Y', Item.helmetIron
		});

		addRecipe(new ItemStack(GCCoreItems.canvas, 1), new Object[] {
			" XY",
			"XXX",
			"YX ",
			'Y', Item.stick, 'X', Item.silk
		});
		
		addRecipe(new ItemStack(GCCoreItems.parachute, 1, 0), new Object[] {
			"XXX",
			"Y Y",
			" Y ",
			'X', GCCoreItems.canvas, 'Y', Item.silk
		});
		
		addRecipe(new ItemStack(GCCoreBlocks.enclosedWire, 1, 1), new Object[] {
			"XYX",
			'X', GCCoreBlocks.oxygenPipe, 'Y', new ItemStack(GCCoreBlocks.decorationBlocks, 1, 4)
		});


		addRecipe(new ItemStack(GCCoreItems.oxygenGear), new Object[] {
			" Y ",
			"YXY",
			"Y Y",
			'X', GCCoreItems.oxygenConcentrator, 'Y', GCCoreBlocks.oxygenPipe
		});

		addRecipe(new ItemStack(GCCoreBlocks.decorationBlocks, 4, 3), new Object[] {
			"   ",
			" XY",
			"   ",
			'X', new ItemStack(Block.stone, 4, 0), 'Y', "ingotTin"
		});

		addRecipe(new ItemStack(GCCoreBlocks.decorationBlocks, 4, 4), new Object[] {
			"   ",
			" X ",
			" Y ",
			'X', new ItemStack(Block.stone, 4, 0), 'Y', "ingotTin"
		});

		addRecipe(new ItemStack(GCCoreItems.flag, 1, 16), new Object[] {
			"XYY",
			"XYY",
			"X  ",
			'X', GCCoreItems.flagPole, 'Y', GCCoreItems.canvas
		});

        for (int var2 = 0; var2 < 16; ++var2)
        {
        	CraftingManager.getInstance().addShapelessRecipe(new ItemStack(GCCoreItems.parachute, 1, GCCoreItemParachute.getParachuteDamageValueFromDye(var2)), new Object[] {new ItemStack(Item.dyePowder, 1, var2), new ItemStack(GCCoreItems.parachute, 1, 0)});
        }

        for (int var2 = 0; var2 < 16; ++var2)
        {
        	CraftingManager.getInstance().addShapelessRecipe(new ItemStack(GCCoreItems.flag, 1, GCCoreItemFlag.getFlagDamageValueFromDye(var2)), new Object[] {new ItemStack(Item.dyePowder, 1, var2), new ItemStack(GCCoreItems.flag, 1, 16)});
        }
        
		addRecipe(new ItemStack(GCCoreItems.heavyPlating, 2), new Object[] {
			"X",
			"Y",
			"Z",
			'X', "plateIron",
			'Y', "ingotInvar",
			'Z', "plateBronze",
		});
		
		addRecipe(new ItemStack(GCCoreItems.rocketFins, 1), new Object[] {
			" Y ",
			"XYX",
			"X X",
			'X', GCCoreItems.heavyPlating,
			'Y', "ingotInvar"
		});
		
		addRecipe(new ItemStack(GCCoreBlocks.landingPad, 9), new Object[] {
			"YYY",
			"XXX",
			'X', Block.blockIron,
			'Y', "plateIron"
		});
		
		addRecipe(new ItemStack(GCCoreBlocks.airDistributor, 1), new Object[] {
			"WXW",
			"YZY",
			"WVW",
			'V', ItemRegistry.getItem("powerCoilGold", 1),
			'W', "ingotLead",
			'X', GCCoreItems.airFan,
			'Y', GCCoreItems.airVent,
			'Z', ItemRegistry.getItem("machineFrame", 1)
		});
		
		addRecipe(new ItemStack(GCCoreBlocks.sealer, 1), new Object[] {
			"WZW",
			"YTX",
			"WUW",
			'T', ItemRegistry.getItem("machineFrame", 1),
			'V', "copperWire",
			'W', "ingotLead",
			'X', GCCoreItems.airFan,
			'Y', GCCoreItems.airVent,
			'Z', "ingotInvar",
			'U', ItemRegistry.getItem("powerCoilGold", 1)
		});
		
		addRecipe(new ItemStack(GCCoreBlocks.blockAirCollector, 1), new Object[] {
			"WVW",
			"YXZ",
			"WUW",
			'U', ItemRegistry.getItem("powerCoilGold", 1),
			'V', GCCoreItems.oxygenConcentrator,
			'W', "ingotLead",
			'X', ItemRegistry.getItem("machineFrame", 1),
			'Y', GCCoreItems.airFan,
			'Z', GCCoreItems.airVent
		});
		
		addRecipe(new ItemStack(GCCoreBlocks.rocketBench, 1), new Object[] {
			"XXX",
			"YZY",
			"YWY",
			'X', "ingotLead",
			'Y', "ingotInvar",
			'Z', Block.workbench,
			'W', "advancedCircuit"
		});
		
		addRecipe(new ItemStack(GCCoreItems.heavyOxygenTank, 1, GCCoreItems.heavyOxygenTank.getMaxDamage()), new Object[] {
			"ZZZ",
			"XXX",
			"YYY",
			'X', new ItemStack(GCCoreItems.canister, 1, 0),
			'Y', "ingotLead",
			'Z', new ItemStack(Block.cloth, 1, 14)
		});
		
		addRecipe(new ItemStack(GCCoreItems.airFan, 1), new Object[] {
			"Z Z",
			" Y ",
			"ZXZ",
			'X', Item.redstone,
			'Y', "motor",
			'Z', "ingotLead"
		});
		
		addRecipe(new ItemStack(GCCoreItems.oxygenConcentrator, 1), new Object[] {
			"ZWZ",
			"WYW",
			"ZXZ",
			'W', "ingotTin",
			'X', GCCoreItems.airVent,
			'Y', new ItemStack(GCCoreItems.canister, 1, 0),
			'Z', "ingotLead"
		});
		
		addRecipe(new ItemStack(GCCoreItems.steelPickaxe, 1), new Object[] {
			"YYY",
			" X ",
			" X ",
			'Y', "ingotLead",
			'X', Item.stick
		});
		
		addRecipe(new ItemStack(GCCoreItems.steelAxe, 1), new Object[] {
			"YY ",
			"YX ",
			" X ",
			'Y', "ingotLead",
			'X', Item.stick
		});
		
		addRecipe(new ItemStack(GCCoreItems.steelAxe, 1), new Object[] {
			" YY",
			" XY",
			" X ",
			'Y', "ingotLead",
			'X', Item.stick
		});
		
		addRecipe(new ItemStack(GCCoreItems.steelHoe, 1), new Object[] {
			" YY",
			" X ",
			" X ",
			'Y', "ingotLead",
			'X', Item.stick
		});
		
		addRecipe(new ItemStack(GCCoreItems.steelHoe, 1), new Object[] {
			"YY ",
			" X ",
			" X ",
			'Y', "ingotLead",
			'X', Item.stick
		});
		
		addRecipe(new ItemStack(GCCoreItems.steelSpade, 1), new Object[] {
			" Y ",
			" X ",
			" X ",
			'Y', "ingotLead",
			'X', Item.stick
		});
		
		addRecipe(new ItemStack(GCCoreItems.steelSword, 1), new Object[] {
			" Y ",
			" Y ",
			" X ",
			'Y', "ingotLead",
			'X', Item.stick
		});
		
		addRecipe(new ItemStack(GCCoreItems.steelBoots, 1), new Object[] {
			"X X",
			"X X",
			'X', "ingotLead"
		});
		
		addRecipe(new ItemStack(GCCoreItems.steelChestplate, 1), new Object[] {
			"X X",
			"XXX",
			"XXX",
			'X', "ingotLead"
		});
		
		addRecipe(new ItemStack(GCCoreItems.steelLeggings, 1), new Object[] {
			"XXX",
			"X X",
			"X X",
			'X', "ingotLead"
		});
		
		addRecipe(new ItemStack(GCCoreItems.steelHelmet, 1), new Object[] {
			"XXX",
			"X X",
			'X', "ingotLead"
		});
		
		addRecipe(new ItemStack(GCCoreBlocks.enclosedWire, 1, 0), new Object[] {
			"XYX",
			'X', "copperWire", 'Y', new ItemStack(GCCoreBlocks.decorationBlocks, 1, 4)
		});

		addRecipe(new ItemStack(GCCoreItems.flagPole, 2, 0), new Object[] {
			"X",
			"X",
			"X",
			'X', "ingotLead"
		});

    	CraftingManager.getInstance().getRecipeList().add(new ShapelessOreRecipe(new ItemStack(GCCoreItems.airVent, 1), new Object[] {
    		"ingotTin",
    		"ingotTin",
    		"ingotTin",
    		"ingotLead"
    	}));

		addRecipe(new ItemStack(GCCoreBlocks.airLockFrame, 2), new Object[] {
			"XXX",
			"YVY",
			"ZWZ",
			'V', GCCoreBlocks.oxygenPipe, 'W', Item.redstone, 'X', GCMoonItems.meteoricIronIngot, 'Y', "ingotLead", 'Z', GCCoreItems.oxygenConcentrator
		});

		addRecipe(new ItemStack(GCCoreItems.oilExtractor), new Object[] {
			"X  ",
			" XY",
			"ZYY",
			'X', "ingotLead", 'Y', "ingotBronze", 'Z', Item.redstone
		});

		addRecipe(new ItemStack(GCCoreItems.oilCanister, 1, 61), new Object[] {
			"WXW",
			"WYW",
			"WZW",
			'X', "ingotLead", 'Y', Block.glass, 'Z', new ItemStack(GCCoreItems.canister, 1, 0), 'W', "ingotTin"
		});

		addRecipe(new ItemStack(GCCoreBlocks.refinery), new Object[] {
			" Z ",
			"WYW",
			"XVX",
			'X', "ingotLead", 'Y', ItemRegistry.getItem("machineFrame", 1), 'Z', new ItemStack(GCCoreItems.canister, 1, 1), 'W', Block.stone, 'V', ItemRegistry.getItem("powerCoilGold", 1)
		});

		addRecipe(new ItemStack(GCCoreBlocks.compressor), new Object[] {
			"XZX",
			"XWX",
			"XYX",
			'X', "ingotLead", 'Y', ItemRegistry.getItem("powerCoilGold", 1), 'Z', GCCoreItems.oxygenConcentrator, 'W', ItemRegistry.getItem("machineFrame", 1)
		});

		addRecipe(new ItemStack(GCCoreBlocks.fuelLoader), new Object[] {
			"XZX",
			"ZWZ",
			"XYX",
			'X', "ingotLead", 'Y', ItemRegistry.getItem("powerCoilGold", 1), 'Z', ItemRegistry.getItem("hardenedGlass", 1), 'W', ItemRegistry.getItem("machineFrame", 1)
		});
	}
	
	public static void addBasicComponentsCraftingRecipes()
	{
		addRecipe(new ItemStack(GCCoreItems.rocketEngine, 1), new Object[] {
			" YV", 
			"XWX", 
			"XZX", 
				'V', Block.stoneButton,
				'W', new ItemStack(GCCoreItems.canister, 1, 0),
				'X', GCCoreItems.heavyPlating,
				'Y', Item.flintAndSteel,
				'Z', GCCoreItems.airVent
			});

		addRecipe(new ItemStack(GCCoreItems.rocketEngine, 1), new Object[] {
			"VY ",
			"XWX",
			"XZX",
			'V', Block.stoneButton,
			'W', new ItemStack(GCCoreItems.canister, 1, 0),
			'X', GCCoreItems.heavyPlating,
			'Y', Item.flintAndSteel,
			'Z', GCCoreItems.airVent
		});
		
		addRecipe(new ItemStack(GCCoreItems.rocketNoseCone, 1), new Object[] {
			" Y ",
			" X ",
			"X X",
			'X', GCCoreItems.heavyPlating,
			'Y', Block.torchRedstoneActive
		});
		
		addRecipe(new ItemStack(GCCoreBlocks.oxygenPipe, 4), new Object[] {
			"XXX",
			"   ",
			"XXX",
			'X', Block.thinGlass
		});
		
		addRecipe(new ItemStack(GCCoreItems.lightOxygenTank, 1, GCCoreItems.lightOxygenTank.getMaxDamage()), new Object[] {
			"Z",
			"X",
			"Y",
			'X', new ItemStack(GCCoreItems.canister, 1, 0),
			'Y', "ingotCopper",
			'Z', new ItemStack(Block.cloth, 1, 5)
		});
		
		addRecipe(new ItemStack(GCCoreItems.medOxygenTank, 1, GCCoreItems.medOxygenTank.getMaxDamage()), new Object[] {
			"ZZ",
			"XX",
			"YY",
			'X', new ItemStack(GCCoreItems.canister, 1, 0),
			'Y', "ingotTin",
			'Z', new ItemStack(Block.cloth, 1, 1)
		});

		addRecipe(new ItemStack(GCCoreItems.sensorGlasses, 1), new Object[] {
			"YYY",
			"Y Y",
			"XYX",
			'X', GCCoreItems.sensorLens,
			'Y', GCMoonItems.meteoricIronIngot
		});
		
		addRecipe(new ItemStack(GCCoreItems.sensorLens, 1), new Object[] {
			"ZYZ",
			"YXY",
			"ZYZ",
			'X', Block.thinGlass,
			'Y', GCMoonItems.meteoricIronIngot,
			'Z', Item.redstone
		});
		
		addRecipe(new ItemStack(GCCoreItems.canister, 1, 0), new Object[] {
			"X X",
			"X X",
			"XXX",
			'X', "ingotTin"
		});
		
		addRecipe(new ItemStack(GCCoreItems.canister, 1, 1), new Object[] {
			"X X",
			"X X",
			"XXX",
			'X', "ingotCopper"
		});

		addRecipe(new ItemStack(GCCoreItems.oxygenMask, 1), new Object[] {
			"XXX",
			"XYX",
			"XXX",
			'X', Block.thinGlass,
			'Y', Item.helmetIron
		});

		addRecipe(new ItemStack(GCCoreItems.canvas, 1), new Object[] {
			" XY",
			"XXX",
			"YX ",
			'Y', Item.stick, 'X', Item.silk
		});
		
		addRecipe(new ItemStack(GCCoreItems.parachute, 1, 0), new Object[] {
			"XXX",
			"Y Y",
			" Y ",
			'X', GCCoreItems.canvas, 'Y', Item.silk
		});
		
		addRecipe(new ItemStack(GCCoreBlocks.enclosedWire, 1, 1), new Object[] {
			"XYX",
			'X', GCCoreBlocks.oxygenPipe, 'Y', new ItemStack(GCCoreBlocks.decorationBlocks, 1, 4)
		});


		addRecipe(new ItemStack(GCCoreItems.oxygenGear), new Object[] {
			" Y ",
			"YXY",
			"Y Y",
			'X', GCCoreItems.oxygenConcentrator, 'Y', GCCoreBlocks.oxygenPipe
		});

		addRecipe(new ItemStack(GCCoreBlocks.decorationBlocks, 4, 3), new Object[] {
			"   ",
			" XY",
			"   ",
			'X', new ItemStack(Block.stone, 4, 0), 'Y', "ingotTin"
		});

		addRecipe(new ItemStack(GCCoreBlocks.decorationBlocks, 4, 4), new Object[] {
			"   ",
			" X ",
			" Y ",
			'X', new ItemStack(Block.stone, 4, 0), 'Y', "ingotTin"
		});

		addRecipe(new ItemStack(GCCoreItems.flag, 1, 16), new Object[] {
			"XYY",
			"XYY",
			"X  ",
			'X', GCCoreItems.flagPole, 'Y', GCCoreItems.canvas
		});

        for (int var2 = 0; var2 < 16; ++var2)
        {
        	CraftingManager.getInstance().addShapelessRecipe(new ItemStack(GCCoreItems.parachute, 1, GCCoreItemParachute.getParachuteDamageValueFromDye(var2)), new Object[] {new ItemStack(Item.dyePowder, 1, var2), new ItemStack(GCCoreItems.parachute, 1, 0)});
        }

        for (int var2 = 0; var2 < 16; ++var2)
        {
        	CraftingManager.getInstance().addShapelessRecipe(new ItemStack(GCCoreItems.flag, 1, GCCoreItemFlag.getFlagDamageValueFromDye(var2)), new Object[] {new ItemStack(Item.dyePowder, 1, var2), new ItemStack(GCCoreItems.flag, 1, 16)});
        }
        
		addRecipe(new ItemStack(GCCoreItems.heavyPlating, 2), new Object[] {
			"X",
			"Y",
			"Z",
			'X', "plateIron",
			'Y', "plateSteel",
			'Z', "plateBronze",
		});
		
		addRecipe(new ItemStack(GCCoreItems.rocketFins, 1), new Object[] {
			" Y ",
			"XYX",
			"X X",
			'X', GCCoreItems.heavyPlating,
			'Y', "plateSteel"
		});
		
		addRecipe(new ItemStack(GCCoreBlocks.landingPad, 9), new Object[] {
			"YYY",
			"XXX",
			'X', Block.blockIron,
			'Y', "plateIron"
		});
		
		addRecipe(new ItemStack(GCCoreBlocks.airDistributor, 1), new Object[] {
			"WXW",
			"YWY",
			"WXW",
			'W', "ingotSteel",
			'X', GCCoreItems.airFan,
			'Y', GCCoreItems.airVent
		});
		
		addRecipe(new ItemStack(GCCoreBlocks.sealer, 1), new Object[] {
			"WZW",
			"YXY",
			"WZW",
			'V', "copperWire",
			'W', "ingotSteel",
			'X', GCCoreItems.airFan,
			'Y', GCCoreItems.airVent,
			'Z', "plateSteel"
		});
		
		addRecipe(new ItemStack(GCCoreBlocks.blockAirCollector, 1), new Object[] {
			"WWW",
			"YXZ",
			"WVW",
			'V', GCCoreItems.oxygenConcentrator,
			'W', "ingotSteel",
			'X', new ItemStack(GCCoreItems.canister, 1, 0),
			'Y', GCCoreItems.airFan,
			'Z', GCCoreItems.airVent
		});
		
		addRecipe(new ItemStack(GCCoreBlocks.rocketBench, 1), new Object[] {
			"XXX",
			"YZY",
			"YWY",
			'X', "ingotSteel",
			'Y', "plateSteel",
			'Z', Block.workbench,
			'W', "advancedCircuit"
		});
		
		addRecipe(new ItemStack(GCCoreItems.heavyOxygenTank, 1, GCCoreItems.heavyOxygenTank.getMaxDamage()), new Object[] {
			"ZZZ",
			"XXX",
			"YYY",
			'X', new ItemStack(GCCoreItems.canister, 1, 0),
			'Y', "ingotSteel",
			'Z', new ItemStack(Block.cloth, 1, 14)
		});
		
		addRecipe(new ItemStack(GCCoreItems.airFan, 1), new Object[] {
			"Z Z",
			" Y ",
			"ZXZ",
			'X', Item.redstone,
			'Y', "motor",
			'Z', "ingotSteel"
		});
		
		addRecipe(new ItemStack(GCCoreItems.oxygenConcentrator, 1), new Object[] {
			"ZWZ",
			"WYW",
			"ZXZ",
			'W', "ingotTin",
			'X', GCCoreItems.airVent,
			'Y', new ItemStack(GCCoreItems.canister, 1, 0),
			'Z', "ingotSteel"
		});
		
		addRecipe(new ItemStack(GCCoreItems.steelPickaxe, 1), new Object[] {
			"YYY",
			" X ",
			" X ",
			'Y', "ingotSteel",
			'X', Item.stick
		});
		
		addRecipe(new ItemStack(GCCoreItems.steelAxe, 1), new Object[] {
			"YY ",
			"YX ",
			" X ",
			'Y', "ingotSteel",
			'X', Item.stick
		});
		
		addRecipe(new ItemStack(GCCoreItems.steelAxe, 1), new Object[] {
			" YY",
			" XY",
			" X ",
			'Y', "ingotSteel",
			'X', Item.stick
		});
		
		addRecipe(new ItemStack(GCCoreItems.steelHoe, 1), new Object[] {
			" YY",
			" X ",
			" X ",
			'Y', "ingotSteel",
			'X', Item.stick
		});
		
		addRecipe(new ItemStack(GCCoreItems.steelHoe, 1), new Object[] {
			"YY ",
			" X ",
			" X ",
			'Y', "ingotSteel",
			'X', Item.stick
		});
		
		addRecipe(new ItemStack(GCCoreItems.steelSpade, 1), new Object[] {
			" Y ",
			" X ",
			" X ",
			'Y', "ingotSteel",
			'X', Item.stick
		});
		
		addRecipe(new ItemStack(GCCoreItems.steelSword, 1), new Object[] {
			" Y ",
			" Y ",
			" X ",
			'Y', "ingotSteel",
			'X', Item.stick
		});
		
		addRecipe(new ItemStack(GCCoreItems.steelBoots, 1), new Object[] {
			"X X",
			"X X",
			'X', "ingotSteel"
		});
		
		addRecipe(new ItemStack(GCCoreItems.steelChestplate, 1), new Object[] {
			"X X",
			"XXX",
			"XXX",
			'X', "ingotSteel"
		});
		
		addRecipe(new ItemStack(GCCoreItems.steelLeggings, 1), new Object[] {
			"XXX",
			"X X",
			"X X",
			'X', "ingotSteel"
		});
		
		addRecipe(new ItemStack(GCCoreItems.steelHelmet, 1), new Object[] {
			"XXX",
			"X X",
			'X', "ingotSteel"
		});
		
		addRecipe(new ItemStack(GCCoreBlocks.enclosedWire, 1, 0), new Object[] {
			"XYX",
			'X', "copperWire", 'Y', new ItemStack(GCCoreBlocks.decorationBlocks, 1, 4)
		});

		addRecipe(new ItemStack(GCCoreItems.flagPole, 2, 0), new Object[] {
			"X",
			"X",
			"X",
			'X', "ingotSteel"
		});

    	CraftingManager.getInstance().getRecipeList().add(new ShapelessOreRecipe(new ItemStack(GCCoreItems.airVent, 1), new Object[] {
    		"ingotTin",
    		"ingotTin",
    		"ingotTin",
    		"ingotSteel"
    	}));

		addRecipe(new ItemStack(GCCoreBlocks.airLockFrame, 2), new Object[] {
			"XXX",
			"YVY",
			"ZWZ",
			'V', GCCoreBlocks.oxygenPipe, 'W', Item.redstone, 'X', GCMoonItems.meteoricIronIngot, 'Y', "ingotSteel", 'Z', GCCoreItems.oxygenConcentrator
		});

		addRecipe(new ItemStack(GCCoreItems.oilExtractor), new Object[] {
			"X  ",
			" XY",
			"ZYY",
			'X', "ingotSteel", 'Y', "ingotBronze", 'Z', Item.redstone
		});

		addRecipe(new ItemStack(GCCoreItems.oilCanister, 1, 61), new Object[] {
			"WXW",
			"WYW",
			"WZW",
			'X', "ingotSteel", 'Y', Block.glass, 'Z', new ItemStack(GCCoreItems.canister, 1, 0), 'W', "ingotTin"
		});

		addRecipe(new ItemStack(GCCoreBlocks.refinery), new Object[] {
			" Z ",
			"WZW",
			"XYX",
			'X', "ingotSteel", 'Y', Block.furnaceIdle, 'Z', new ItemStack(GCCoreItems.canister, 1, 1), 'W', Block.stone
		});

		addRecipe(new ItemStack(GCCoreBlocks.compressor), new Object[] {
			"XXX",
			"XZX",
			"XYX",
			'X', "ingotSteel", 'Y', "ingotBronze", 'Z', GCCoreItems.oxygenConcentrator
		});

		addRecipe(new ItemStack(GCCoreBlocks.fuelLoader), new Object[] {
			"XXX",
			"XZX",
			"XYX",
			'X', "ingotSteel", 'Y', "basicCircuit", 'Z', "motor"
		});
	}
	
	public static void addIndustrialcraftCraftingRecipes()
	{
		addRecipe(new ItemStack(GCCoreItems.rocketEngine, 1), new Object[] {
			" YV", 
			"XWX", 
			"XZX", 
				'V', Block.stoneButton,
				'W', new ItemStack(GCCoreItems.canister, 1, 0),
				'X', GCCoreItems.heavyPlating,
				'Y', Item.flintAndSteel,
				'Z', GCCoreItems.airVent
			});

		addRecipe(new ItemStack(GCCoreItems.rocketEngine, 1), new Object[] {
			"VY ",
			"XWX",
			"XZX",
			'V', Block.stoneButton,
			'W', new ItemStack(GCCoreItems.canister, 1, 0),
			'X', GCCoreItems.heavyPlating,
			'Y', Item.flintAndSteel,
			'Z', GCCoreItems.airVent
		});
		
		addRecipe(new ItemStack(GCCoreItems.rocketNoseCone, 1), new Object[] {
			" Y ",
			" X ",
			"X X",
			'X', GCCoreItems.heavyPlating,
			'Y', Block.torchRedstoneActive
		});
		
		addRecipe(new ItemStack(GCCoreBlocks.oxygenPipe, 4), new Object[] {
			"XXX",
			"   ",
			"XXX",
			'X', Block.thinGlass
		});
		
		addRecipe(new ItemStack(GCCoreItems.lightOxygenTank, 1, GCCoreItems.lightOxygenTank.getMaxDamage()), new Object[] {
			"Z",
			"X",
			"Y",
			'X', new ItemStack(GCCoreItems.canister, 1, 0),
			'Y', "ingotCopper",
			'Z', new ItemStack(Block.cloth, 1, 5)
		});
		
		addRecipe(new ItemStack(GCCoreItems.medOxygenTank, 1, GCCoreItems.medOxygenTank.getMaxDamage()), new Object[] {
			"ZZ",
			"XX",
			"YY",
			'X', new ItemStack(GCCoreItems.canister, 1, 0),
			'Y', "ingotTin",
			'Z', new ItemStack(Block.cloth, 1, 1)
		});

		addRecipe(new ItemStack(GCCoreItems.sensorGlasses, 1), new Object[] {
			"YYY",
			"Y Y",
			"XYX",
			'X', GCCoreItems.sensorLens,
			'Y', GCMoonItems.meteoricIronIngot
		});
		
		addRecipe(new ItemStack(GCCoreItems.sensorLens, 1), new Object[] {
			"ZYZ",
			"YXY",
			"ZYZ",
			'X', Block.thinGlass,
			'Y', GCMoonItems.meteoricIronIngot,
			'Z', Item.redstone
		});
		
		addRecipe(new ItemStack(GCCoreItems.canister, 1, 0), new Object[] {
			"X X",
			"X X",
			"XXX",
			'X', "ingotTin"
		});
		
		addRecipe(new ItemStack(GCCoreItems.canister, 1, 1), new Object[] {
			"X X",
			"X X",
			"XXX",
			'X', "ingotCopper"
		});

		addRecipe(new ItemStack(GCCoreItems.oxygenMask, 1), new Object[] {
			"XXX",
			"XYX",
			"XXX",
			'X', Block.thinGlass,
			'Y', Item.helmetIron
		});

		addRecipe(new ItemStack(GCCoreItems.canvas, 1), new Object[] {
			" XY",
			"XXX",
			"YX ",
			'Y', Item.stick, 'X', Item.silk
		});
		
		addRecipe(new ItemStack(GCCoreItems.parachute, 1, 0), new Object[] {
			"XXX",
			"Y Y",
			" Y ",
			'X', GCCoreItems.canvas, 'Y', Item.silk
		});
		
		addRecipe(new ItemStack(GCCoreBlocks.enclosedWire, 1, 1), new Object[] {
			"XYX",
			'X', GCCoreBlocks.oxygenPipe, 'Y', new ItemStack(GCCoreBlocks.decorationBlocks, 1, 4)
		});


		addRecipe(new ItemStack(GCCoreItems.oxygenGear), new Object[] {
			" Y ",
			"YXY",
			"Y Y",
			'X', GCCoreItems.oxygenConcentrator, 'Y', GCCoreBlocks.oxygenPipe
		});

		addRecipe(new ItemStack(GCCoreBlocks.decorationBlocks, 4, 3), new Object[] {
			"   ",
			" XY",
			"   ",
			'X', new ItemStack(Block.stone, 4, 0), 'Y', "ingotTin"
		});

		addRecipe(new ItemStack(GCCoreBlocks.decorationBlocks, 4, 4), new Object[] {
			"   ",
			" X ",
			" Y ",
			'X', new ItemStack(Block.stone, 4, 0), 'Y', "ingotTin"
		});

		addRecipe(new ItemStack(GCCoreItems.flag, 1, 16), new Object[] {
			"XYY",
			"XYY",
			"X  ",
			'X', GCCoreItems.flagPole, 'Y', GCCoreItems.canvas
		});

        for (int var2 = 0; var2 < 16; ++var2)
        {
        	CraftingManager.getInstance().addShapelessRecipe(new ItemStack(GCCoreItems.parachute, 1, GCCoreItemParachute.getParachuteDamageValueFromDye(var2)), new Object[] {new ItemStack(Item.dyePowder, 1, var2), new ItemStack(GCCoreItems.parachute, 1, 0)});
        }

        for (int var2 = 0; var2 < 16; ++var2)
        {
        	CraftingManager.getInstance().addShapelessRecipe(new ItemStack(GCCoreItems.flag, 1, GCCoreItemFlag.getFlagDamageValueFromDye(var2)), new Object[] {new ItemStack(Item.dyePowder, 1, var2), new ItemStack(GCCoreItems.flag, 1, 16)});
        }
        
		addRecipe(new ItemStack(GCCoreItems.heavyPlating, 2), new Object[] {
			"X",
			"X",
			'X', "carbonPlate"
		});

		addRecipe(new ItemStack(GCCoreItems.rocketFins, 1), new Object[] {
			" Y ",
			"XYX",
			"X X",
			'X', GCCoreItems.heavyPlating,
			'Y', "carbonPlate"
		});

		addRecipe(new ItemStack(GCCoreBlocks.landingPad, 9), new Object[] {
			"YYY",
			"XXX",
			'X', Block.blockIron,
			'Y', "carbonPlate"
		});

		addRecipe(new ItemStack(GCCoreBlocks.airDistributor, 1), new Object[] {
			"WXW",
			"YWY",
			"WXW",
			'W', "ingotRefinedIron",
			'X', GCCoreItems.airFan,
			'Y', GCCoreItems.airVent
		});

		addRecipe(new ItemStack(GCCoreBlocks.blockAirCollector, 1), new Object[] {
			"WWW",
			"YXZ",
			"WVW",
			'V', GCCoreItems.oxygenConcentrator,
			'W', "refinedIronIngot",
			'X', new ItemStack(GCCoreItems.canister, 1, 0),
			'Y', GCCoreItems.airFan,
			'Z', GCCoreItems.airVent
		});

		addRecipe(new ItemStack(GCCoreBlocks.rocketBench, 1), new Object[] {
			"XXX",
			"YZY",
			"YWY",
			'X', "refinedIronIngot",
			'Y', "refinedIronIngot",
			'Z', Block.workbench,
			'W', "advancedCircuit"
		});

		addRecipe(new ItemStack(GCCoreItems.heavyOxygenTank, 1, GCCoreItems.heavyOxygenTank.getMaxDamage()), new Object[] {
			"ZZZ",
			"XXX",
			"YYY",
			'X', new ItemStack(GCCoreItems.canister, 1, 0),
			'Y', "ingotRefinedIron",
			'Z', new ItemStack(Block.cloth, 1, 14)
		});

		addRecipe(new ItemStack(GCCoreItems.airFan, 1), new Object[] {
			"Z Z",
			" Y ",
			"ZXZ",
			'X', Item.redstone,
			'Y', "electronicCircuit",
			'Z', "ingotRefinedIron"
		});
		
		addRecipe(new ItemStack(GCCoreBlocks.fuelLoader), new Object[] {
			"XXX",
			"X X",
			"XYX",
			'X', "ingotRefinedIron", 'Y', "electronicCircuit"
		});
		
		addRecipe(new ItemStack(GCCoreBlocks.compressor), new Object[] {
			"XXX",
			"XZX",
			"XYX",
			'X', "ingotRefinedIron", 'Y', "ingotBronze", 'Z', GCCoreItems.oxygenConcentrator
		});
		
		addRecipe(new ItemStack(GCCoreBlocks.refinery), new Object[] {
			" Z ",
			"WZW",
			"XYX",
			'X', "ingotRefinedIron", 'Y', Block.furnaceIdle, 'Z', new ItemStack(GCCoreItems.canister, 1, 1), 'W', Block.stone
		});
		
		addRecipe(new ItemStack(GCCoreItems.oilCanister, 1, 61), new Object[] {
			"WXW",
			"WYW",
			"WZW",
			'X', "ingotRefinedIron", 'Y', Block.glass, 'Z', new ItemStack(GCCoreItems.canister, 1, 0), 'W', "ingotTin"
		});
		
		addRecipe(new ItemStack(GCCoreItems.oilExtractor), new Object[] {
			"X  ",
			" XY",
			"ZYY",
			'X', "ingotRefinedIron", 'Y', "ingotBronze", 'Z', Item.redstone
		});
		
		addRecipe(new ItemStack(GCCoreBlocks.airLockFrame, 2), new Object[] {
			"XXX",
			"YVY",
			"ZWZ",
			'V', GCCoreBlocks.oxygenPipe, 'W', Item.redstone, 'X', GCMoonItems.meteoricIronIngot, 'Y', "ingotRefinedIron", 'Z', GCCoreItems.oxygenConcentrator
		});
    	
    	CraftingManager.getInstance().getRecipeList().add(new ShapelessOreRecipe(new ItemStack(GCCoreItems.airVent, 1), new Object[] {
    		"ingotTin",
    		"ingotTin",
    		"ingotTin",
    		"ingotRefinedIron"
    	}));
		
		addRecipe(new ItemStack(GCCoreItems.oxygenConcentrator, 1), new Object[] {
			"ZWZ",
			"WYW",
			"ZXZ",
			'W', "ingotTin",
			'X', GCCoreItems.airVent,
			'Y', new ItemStack(GCCoreItems.canister, 1, 0),
			'Z', "ingotRefinedIron"
		});

		addRecipe(new ItemStack(GCCoreItems.steelPickaxe, 1), new Object[] {
			"YYY",
			" X ",
			" X ",
			'Y', "ingotRefinedIron",
			'X', Item.stick
		});
		
		addRecipe(new ItemStack(GCCoreItems.steelAxe, 1), new Object[] {
			"YY ",
			"YX ",
			" X ",
			'Y', "ingotRefinedIron",
			'X', Item.stick
		});
		
		addRecipe(new ItemStack(GCCoreItems.steelAxe, 1), new Object[] {
			" YY",
			" XY",
			" X ",
			'Y', "ingotRefinedIron",
			'X', Item.stick
		});
		
		addRecipe(new ItemStack(GCCoreItems.steelHoe, 1), new Object[] {
			" YY",
			" X ",
			" X ",
			'Y', "ingotRefinedIron",
			'X', Item.stick
		});
		
		addRecipe(new ItemStack(GCCoreItems.steelHoe, 1), new Object[] {
			"YY ",
			" X ",
			" X ",
			'Y', "ingotRefinedIron",
			'X', Item.stick
		});
		
		addRecipe(new ItemStack(GCCoreItems.steelSpade, 1), new Object[] {
			" Y ",
			" X ",
			" X ",
			'Y', "ingotRefinedIron",
			'X', Item.stick
		});
		
		addRecipe(new ItemStack(GCCoreItems.steelSword, 1), new Object[] {
			" Y ",
			" Y ",
			" X ",
			'Y', "ingotRefinedIron",
			'X', Item.stick
		});
		
		addRecipe(new ItemStack(GCCoreItems.steelBoots, 1), new Object[] {
			"X X",
			"X X",
			'X', "ingotRefinedIron"
		});
		
		addRecipe(new ItemStack(GCCoreItems.steelChestplate, 1), new Object[] {
			"X X",
			"XXX",
			"XXX",
			'X', "ingotRefinedIron"
		});
		
		addRecipe(new ItemStack(GCCoreItems.steelLeggings, 1), new Object[] {
			"XXX",
			"X X",
			"X X",
			'X', "ingotRefinedIron"
		});
		
		addRecipe(new ItemStack(GCCoreItems.steelHelmet, 1), new Object[] {
			"XXX",
			"X X",
			'X', "ingotRefinedIron"
		});

		addRecipe(new ItemStack(GCCoreItems.flagPole, 2, 0), new Object[] {
			"X",
			"X",
			"X",
			'X', "ingotRefinedIron"
		});

		if (Items.getItem("insulatedCopperCableBlock") != null)
		{
			addRecipe(new ItemStack(GCCoreBlocks.sealer, 1), new Object[] {
				"WZW",
				"YXY",
				"WZW",
				'V', Items.getItem("insulatedCopperCableBlock"),
				'W', "refinedIronIngot",
				'X', GCCoreItems.airFan,
				'Y', GCCoreItems.airVent,
				'Z', "carbonPlate"
			});
		}
		
		if (Items.getItem("copperCableBlock") != null)
		{
			addRecipe(new ItemStack(GCCoreBlocks.enclosedWire, 1, 0), new Object[] {
				"XYX",
				'X', Items.getItem("copperCableBlock"), 'Y', new ItemStack(GCCoreBlocks.decorationBlocks, 1, 4)
			});
		}
	}

	public static void addSmeltingRecipes()
	{
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

								for (int i = 14; i < 17; i++)
								{
									if (slots[i] != null)
									{
										final int id = slots[i].itemID;

										if (id < Block.blocksList.length)
										{
											final Block block = Block.blocksList[id];

											if (block != null && block instanceof BlockChest)
											{
												type = 1;
												break;
											}
										}
									}
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

	@Deprecated
    public static List<ItemStack> getStandardSpaceStationRequirements()
    {
    	final List<ItemStack> stacks = new ArrayList<ItemStack>();

    	stacks.add(new ItemStack(BasicComponents.itemIngot, 16, 1));
    	stacks.add(new ItemStack(BasicComponents.itemIngot, 8, 3));
    	stacks.add(new ItemStack(Item.ingotIron, 12, 0));

    	return stacks;
    }
	
	private static void addRecipe(ItemStack result, Object[] obj)
	{
		CraftingManager.getInstance().getRecipeList().add(new ShapedOreRecipe(result, obj));
	}
}
