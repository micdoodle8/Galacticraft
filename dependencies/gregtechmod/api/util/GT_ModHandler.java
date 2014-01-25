package gregtechmod.api.util;

import gregtechmod.api.GregTech_API;
import gregtechmod.api.enums.GT_ConfigCategories;
import gregtechmod.api.enums.GT_Items;
import gregtechmod.api.enums.Materials;
import gregtechmod.api.enums.OrePrefixes;
import gregtechmod.api.interfaces.IBasicEnergyContainer;
import gregtechmod.api.interfaces.IHasWorldObjectAndCoords;
import ic2.api.item.IBoxable;
import ic2.api.recipe.IRecipeInput;
import ic2.api.recipe.RecipeOutput;

import java.util.*;
import java.util.Map.Entry;

import net.minecraft.block.Block;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.*;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.oredict.ShapelessOreRecipe;
import cpw.mods.fml.common.event.FMLInterModComms;
import cpw.mods.fml.common.registry.GameRegistry;

/**
 * NEVER INCLUDE THIS FILE IN YOUR MOD!!!
 * 
 * This is the Interface I use for interacting with other Mods.
 * 
 * Due to the many imports, this File can cause compile Problems if not all the APIs are installed
 */
public class GT_ModHandler {
	public static volatile int VERSION = 407;
	
	/**
	 * Returns if that Liquid is Water
	 */
	public static boolean isWater(FluidStack aLiquid) {
		if (aLiquid == null) return false;
		return aLiquid.isFluidEqual(getWater(1));
	}
	
	/**
	 * Returns a Liquid Stack with given amount of Water.
	 */
	public static FluidStack getWater(long aAmount) {
		return FluidRegistry.getFluidStack("water", (int)aAmount);
	}
	
	/**
	 * Returns if that Liquid is Lava
	 */
	public static boolean isLava(FluidStack aLiquid) {
		if (aLiquid == null) return false;
		return aLiquid.isFluidEqual(getLava(1));
	}
	
	/**
	 * Returns a Liquid Stack with given amount of Lava.
	 */
	public static FluidStack getLava(long aAmount) {
		return FluidRegistry.getFluidStack("lava", (int)aAmount);
	}
	
	/**
	 * Returns if that Liquid is Steam
	 */
	public static boolean isSteam(FluidStack aLiquid) {
		if (aLiquid == null) return false;
		return aLiquid.isFluidEqual(getSteam(1));
	}
	
	/**
	 * Returns a Liquid Stack with given amount of Steam.
	 */
	public static FluidStack getSteam(long aAmount) {
		return FluidRegistry.getFluidStack("steam", (int)aAmount);
	}
	
	public static ItemStack getEmptyFuelCan(long aAmount) {
		return GT_Items.IC2_Fuel_Can_Empty.get(aAmount);
	}
	
	public static ItemStack getEmptyCell(long aAmount) {
		return GT_Items.Cell_Empty.get(aAmount);
	}
	
	public static ItemStack getAirCell(long aAmount) {
		return GT_Items.Cell_Air.get(aAmount);
	}
	
	public static ItemStack getWaterCell(long aAmount) {
		return GT_Items.Cell_Water.get(aAmount);
	}
	
	public static ItemStack getLavaCell(long aAmount) {
		return GT_Items.Cell_Lava.get(aAmount);
	}
	
	/**
	 * @param aValue Fuel value in EU
	 */
	public static ItemStack getFuelCan(int aValue) {
		if (aValue < 5) return GT_Items.IC2_Fuel_Can_Empty.get(1);
		ItemStack rFuelCanStack = GT_Items.IC2_Fuel_Can_Filled.get(1);
		if (rFuelCanStack == null) return null;
		NBTTagCompound tNBT = new NBTTagCompound();
        tNBT.setInteger("value", aValue/5);
        rFuelCanStack.setTagCompound(tNBT);
        return rFuelCanStack;
	}
	
	/**
	 * @param aFuelCan the Item you want to check
	 * @return the exact Value in EU the Fuel Can is worth if its even a Fuel Can.
	 */
	public static int getFuelCanValue(ItemStack aFuelCan) {
		if (GT_Utility.isStackInvalid(aFuelCan) || !GT_Items.IC2_Fuel_Can_Filled.isStackEqual(aFuelCan, false, true)) return 0;
		NBTTagCompound tNBT = aFuelCan.getTagCompound();
		return tNBT==null?0:tNBT.getInteger("value")*5;
	}
	
	private static final Map<String, ItemStack> sIC2ItemMap = new HashMap<String, ItemStack>();
	
	/**
	 * Gets an Item from IndustrialCraft, and returns a Replacement Item if not possible
	 */
	public static ItemStack getIC2Item(String aItem, long aAmount, ItemStack aReplacement) {
		if (GT_Utility.isStringInvalid(aItem) || !GregTech_API.sPreloadStarted) return null;
		//if (GregTech_API.DEBUG_MODE) GT_Log.out.println("Requested the Item '" + aItem + "' from the IC2-API");
		if (!sIC2ItemMap.containsKey(aItem)) try {sIC2ItemMap.put(aItem, ic2.api.item.Items.getItem(aItem));} catch (Throwable e) {/*Do nothing*/}
		return GT_Utility.copyAmount(aAmount, sIC2ItemMap.get(aItem), aReplacement);
	}
	
	/**
	 * Gets an Item from IndustrialCraft, but the Damage Value can be specified, and returns a Replacement Item with the same Damage if not possible
	 */
	public static ItemStack getIC2Item(String aItem, long aAmount, int aMeta, ItemStack aReplacement) {
		ItemStack rStack = getIC2Item(aItem, aAmount, aReplacement);
		if (rStack == null) return null;
		rStack.setItemDamage(aMeta);
		return rStack;
	}
	
	/**
	 * Gets an Item from IndustrialCraft, but the Damage Value can be specified
	 */
	public static ItemStack getIC2Item(String aItem, long aAmount, int aMeta) {
		return getIC2Item(aItem, aAmount, aMeta, null);
	}
	
	/**
	 * Gets an Item from IndustrialCraft
	 */
	public static ItemStack getIC2Item(String aItem, long aAmount) {
		return getIC2Item(aItem, aAmount, null);
	}
	
	/**
	 * Gets an Item from RailCraft
	 */
	public static ItemStack getRCItem(String aItem, long aAmount) {
		return getRCItem(aItem, aAmount, null);
	}
	
	/**
	 * Gets an Item from RailCraft, and returns a Replacement Item if not possible
	 */
	public static ItemStack getRCItem(String aItem, long aAmount, ItemStack aReplacement) {
		if (GT_Utility.isStringInvalid(aItem) || !GregTech_API.sPreloadStarted) return null;
		return GT_Utility.copyAmount(aAmount, GameRegistry.findItemStack("Railcraft", aItem, (int)aAmount), aReplacement);
	}
	
	/**
	 * Gets an Item from RailCraft, but the Damage Value can be specified
	 */
	public static ItemStack getRCItem(String aItem, long aAmount, int aMeta) {
		ItemStack rStack = getRCItem(aItem, aAmount);
		if (rStack == null) return null;
		rStack.setItemDamage(aMeta);
		return rStack;
	}
	
	/**
	 * Gets an Item from RailCraft, but the Damage Value can be specified, and returns a Replacement Item with the same Damage if not possible
	 */
	public static ItemStack getRCItem(String aItem, long aAmount, int aMeta, ItemStack aReplacement) {
		ItemStack rStack = getRCItem(aItem, aAmount, aReplacement);
		if (rStack == null) return null;
		rStack.setItemDamage(aMeta);
		return rStack;
	}
	
	/**
	 * Gets an Item from ThermoCraft, and returns a Replacement Item if not possible
	 */
	public static ItemStack getTEItem(String aItem, long aAmount, ItemStack aReplacement) {
		if (GT_Utility.isStringInvalid(aItem) || !GregTech_API.sPreloadStarted) return null;
		return GT_Utility.copyAmount(aAmount, GameRegistry.findItemStack("ThermalExpansion", aItem, (int)aAmount), aReplacement);
	}
	
	/**
	 * Gets an Item from ThermalExplosion, but the Damage Value can be specified
	 */
	public static ItemStack getTEItem(String aItem, long aAmount, int aMeta) {
		ItemStack rStack = getTEItem(aItem, aAmount);
		if (rStack == null) return null;
		rStack.setItemDamage(aMeta);
		return rStack;
	}
	
	/**
	 * Gets an Item from ThermalCraft
	 */
	public static ItemStack getTEItem(String aItem, long aAmount) {
		return getTEItem(aItem, aAmount, null);
	}
	
	/**
	 * Gets an Item from Thermal Expansion (Did I spell it right this time?), but the Damage Value can be specified, and returns a Replacement Item with the same Damage if not possible
	 */
	public static ItemStack getTEItem(String aItem, long aAmount, int aMeta, ItemStack aReplacement) {
		ItemStack rStack = getTEItem(aItem, aAmount, aReplacement);
		if (rStack == null) return null;
		rStack.setItemDamage(aMeta);
		return rStack;
	}
	
	/**
	 * adds an RC-Boiler Fuel
	 */
	public static void addBoilerFuel(FluidStack aLiquid, int aValue) {
		if (aLiquid == null || aValue <= 0) return;
		if (!GregTech_API.sRecipeFile.add(GT_ConfigCategories.Fuels.boilerfuels, aLiquid.getFluid().getUnlocalizedName(), true)) return;
		try {
//			mods.railcraft.api.fuel.FuelManager.addBoilerFuel(aLiquid.getFluid(), aValue);
		} catch(Throwable e) {/*Do nothing*/}
	}
    
	/**
	 * OUT OF ORDER
	 */
	public static boolean getModeKeyDown(EntityPlayer aPlayer) {
		return false;
	}
	
	/**
	 * OUT OF ORDER
	 */
	public static boolean getBoostKeyDown(EntityPlayer aPlayer) {
		return false;
	}
	
	/**
	 * OUT OF ORDER
	 */
	public static boolean getJumpKeyDown(EntityPlayer aPlayer) {
		return false;
	}
	
	/**
	 * Adds a Valuable Ore to the Miner
	 */
	public static boolean addValuableOre(int aID, int aMeta, int aValue) {
		if (aValue <= 0) return false;
		try {
			Class.forName("ic2.core.IC2").getMethod("addValuableOre", int.class, int.class, int.class).invoke(null, aID, aMeta, aValue);
		} catch (Throwable e) {/*Do nothing*/}
		return true;
	}
	
	/**
	 * Adds a Scrapbox Drop. Fails at April first for the "suddenly Hoes"-Feature of IC2
	 */
	public static boolean addScrapboxDrop(float aChance, ItemStack aOutput) {
		aOutput = GT_OreDictUnificator.get(true, aOutput);
		if (aOutput == null || aChance <= 0) return false;
		aOutput.stackSize = 1;
		if (GT_Config.system && !GT_Utility.areStacksEqual(aOutput, new ItemStack(Item.hoeWood, 1, 0))) return false;
		aChance = (float)GregTech_API.sRecipeFile.add(GT_ConfigCategories.Machines.scrapboxdrops, aOutput, aChance);
		if (aChance <= 0) return false;
		try {
			GT_Utility.callMethod(GT_Utility.getFieldContent("ic2.api.recipe.Recipes", "scrapboxDrops", true, true), "addDrop", true, false, true, GT_Utility.copy(aOutput), aChance);
			GT_Utility.callMethod(GT_Utility.getFieldContent("ic2.api.recipe.Recipes", "scrapboxDrops", true, true), "addRecipe", true, true, false, GT_Utility.copy(aOutput), aChance);
		} catch (Throwable e) {/*Do nothing*/}
		return true;
	}
	
	/**
	 * Adds an Item to the Recycler Blacklist
	 */
	public static boolean addToRecyclerBlackList(ItemStack aRecycledStack) {
		if (aRecycledStack == null) return false;		
		try {
			ic2.api.recipe.Recipes.recyclerBlacklist.add(GT_Utility.copy(aRecycledStack));
		} catch (Throwable e) {/*Do nothing*/}
		return true;
	}
	
	/**
	 * Just simple Furnace smelting. Unbelievable how Minecraft fails at making a simple ItemStack->ItemStack mapping...
	 */
	public static boolean addSmeltingRecipe(ItemStack aInput, ItemStack aOutput) {
		aOutput = GT_OreDictUnificator.get(true, aOutput);
		if (aInput == null || aOutput == null || GT_Utility.getContainerItem(aInput) != null) return false;
		if (!GregTech_API.sRecipeFile.add(GT_ConfigCategories.Machines.smelting, aInput, true)) return false;
		FurnaceRecipes.smelting().addSmelting(aInput.itemID, aInput.getItemDamage(), GT_Utility.copy(aOutput), 0.0F);
		return true;
	}
	
	/**
	 * Adds to Furnace AND Alloysmelter AND Induction Smelter
	 */
	public static boolean addSmeltingAndAlloySmeltingRecipe(ItemStack aInput, ItemStack aOutput) {
		if (aInput == null || aOutput == null) return false;
		boolean temp = false;
		if (aInput.stackSize == 1 && addSmeltingRecipe(aInput, aOutput)) temp = true;
		if (GregTech_API.sRecipeAdder.addAlloySmelterRecipe(aInput, null, aOutput, 130, 3)) temp = true;
		if (addInductionSmelterRecipe(aInput, null, aOutput, null, aOutput.stackSize*100, 0)) temp = true;
		return temp;
	}
	
	/**
	 * Adds a Recipe to Forestrys Squeezer
	 */
	public static boolean addSqueezerRecipe(ItemStack aInput, FluidStack aOutput, int aTime) {
		if (aInput == null || aOutput == null) return false;
		if (!GregTech_API.sRecipeFile.add(GT_ConfigCategories.Machines.squeezer, aInput, true)) return false;
		try {
//			forestry.api.recipes.RecipeManagers.squeezerManager.addRecipe(aTime>0?aTime:100, new ItemStack[] {GT_Utility.copy(aInput)}, aOutput);
		} catch(Throwable e) {
			return false;
		}
		return true;
	}
	
	/**
	 * LiquidTransposer Recipe for both directions
	 */
	public static boolean addLiquidTransposerRecipe(ItemStack aEmptyContainer, FluidStack aLiquid, ItemStack aFullContainer, int aMJ) {
		aFullContainer = GT_OreDictUnificator.get(true, aFullContainer);
		if (aEmptyContainer == null || aFullContainer == null || aLiquid == null) return false;
		if (!GregTech_API.sRecipeFile.add(GT_ConfigCategories.Machines.liquidtransposer, aFullContainer, true)) return false;
		try {
			ThermalExpansion.addTransposerFill(aMJ, aEmptyContainer, aFullContainer, aLiquid, true);
		} catch(Throwable e) {/*Do nothing*/}
		return true;
	}
	
	/**
	 * LiquidTransposer Recipe for filling Containers
	 */
	public static boolean addLiquidTransposerFillRecipe(ItemStack aEmptyContainer, FluidStack aLiquid, ItemStack aFullContainer, int aMJ) {
		aFullContainer = GT_OreDictUnificator.get(true, aFullContainer);
		if (aEmptyContainer == null || aFullContainer == null || aLiquid == null) return false;
		if (!GregTech_API.sRecipeFile.add(GT_ConfigCategories.Machines.liquidtransposerfilling, aFullContainer, true)) return false;
		try {
			ThermalExpansion.addTransposerFill(aMJ, aEmptyContainer, aFullContainer, aLiquid, false);
		} catch(Throwable e) {/*Do nothing*/}
		return true;
	}
	
	/**
	 * LiquidTransposer Recipe for emptying Containers
	 */
	public static boolean addLiquidTransposerEmptyRecipe(ItemStack aFullContainer, FluidStack aLiquid, ItemStack aEmptyContainer, int aMJ) {
		aEmptyContainer = GT_OreDictUnificator.get(true, aEmptyContainer);
		if (aFullContainer == null || aEmptyContainer == null || aLiquid == null) return false;
		if (!GregTech_API.sRecipeFile.add(GT_ConfigCategories.Machines.liquidtransposeremptying, aFullContainer, true)) return false;
		try {
			ThermalExpansion.addTransposerExtract(aMJ, aFullContainer, aEmptyContainer, aLiquid, 100, false);
		} catch(Throwable e) {/*Do nothing*/}
		return true;
	}
	
	/**
	 * IC2-Extractor Recipe. Overloads old Recipes automatically
	 */
	public static boolean addExtractionRecipe(ItemStack aInput, ItemStack aOutput) {
		aOutput = GT_OreDictUnificator.get(true, aOutput);
		if (aInput == null || aOutput == null) return false;
		GT_Utility.removeSimpleIC2MachineRecipe(aInput, getExtractorRecipeList(), null);
		if (!GregTech_API.sRecipeFile.add(GT_ConfigCategories.Machines.extractor, aInput, true)) return false;
		GT_Utility.addSimpleIC2MachineRecipe(aInput, getExtractorRecipeList(), aOutput);
		return true;
	}
	
	/**
	 * RC-BlastFurnace Recipes
	 */
	public static boolean addRCBlastFurnaceRecipe(ItemStack aInput, ItemStack aOutput, int aTime) {
		aOutput = GT_OreDictUnificator.get(true, aOutput);
		if (aInput == null || aOutput == null || aTime <= 0) return false;
		if (!GregTech_API.sRecipeFile.add(GT_ConfigCategories.Machines.rcblastfurnace, aInput, true)) return false;
		aInput = GT_Utility.copy(aInput);
		aOutput = GT_Utility.copy(aOutput);
		try {
//			mods.railcraft.api.crafting.RailcraftCraftingManager.blastFurnace.addRecipe(aInput, true, false, aTime, aOutput);
		} catch (Throwable e) {
			return false;
		}
		return true;
	}
	
	private static Map<Integer, Object> sPulverizerRecipes = new HashMap<Integer, Object>();
	
	/**
	 * @return Object that can either be cast into IPulverizerRecipe or into GT_PulverizerRecipe
	 */
	public static Object getPulverizerRecipe(ItemStack aInput) {
		if (aInput == null) return null;
		Object tObject = sPulverizerRecipes.get(GT_Utility.stackToInt(aInput));
		if (tObject != null) {
			return tObject;
		}
		
		ItemStack tInput = GT_Utility.copy(aInput);
		tInput.setItemDamage(GregTech_API.ITEM_WILDCARD_DAMAGE);
		tObject = sPulverizerRecipes.get(GT_Utility.stackToInt(tInput));
		if (tObject != null) {
			return tObject;
		}
		/*
		try {
    		for (thermalexpansion.api.crafting.IPulverizerRecipe tRecipe : thermalexpansion.api.crafting.CraftingManagers.pulverizerManager.getRecipeList()) {
    			if (GT_Utility.areStacksEqual(tRecipe.getInput(), aInput)) {
		    		return tRecipe;
    			}
    		}
		} catch(Throwable e) {}*/
		return null;
	}
	
	public static boolean addPulverisationRecipe(ItemStack aInput, ItemStack aOutput1) {
		return addPulverisationRecipe(aInput, aOutput1, null, 0, false);
	}
	
	public static boolean addPulverisationRecipe(ItemStack aInput, ItemStack aOutput1, ItemStack aOutput2) {
		return addPulverisationRecipe(aInput, aOutput1, aOutput2, 100, false);
	}
	
	public static boolean addPulverisationRecipe(ItemStack aInput, ItemStack aOutput1, ItemStack aOutput2, int aChance) {
		return addPulverisationRecipe(aInput, aOutput1, aOutput2, aChance, false);
	}
	
	public static boolean addPulverisationRecipe(ItemStack aInput, ItemStack aOutput1, boolean aOverwrite) {
		return addPulverisationRecipe(aInput, aOutput1, null, 0, aOverwrite);
	}

	public static boolean addPulverisationRecipe(ItemStack aInput, ItemStack aOutput1, ItemStack aOutput2, boolean aOverwrite) {
		return addPulverisationRecipe(aInput, aOutput1, aOutput2, 100, aOverwrite);
	}
	
	/**
	 * Adds Several Pulverizer-Type Recipes.
	 */
	public static boolean addPulverisationRecipe(ItemStack aInput, ItemStack aOutput1, ItemStack aOutput2, int aChance, boolean aOverwrite) {
		aOutput1 = GT_OreDictUnificator.get(true, aOutput1);
		aOutput2 = GT_OreDictUnificator.get(true, aOutput2);
		if (aInput == null || aOutput1 == null) return false;
		GT_Utility.removeSimpleIC2MachineRecipe(aInput, getMaceratorRecipeList(), null);
		
		if (GT_Utility.getContainerItem(aInput) == null) {
			if (GregTech_API.sRecipeFile.add(GT_ConfigCategories.Machines.maceration, aInput, true)) {
				GT_Utility.addSimpleIC2MachineRecipe(aInput, getMaceratorRecipeList(), aOutput1);
			}
			
			if (GregTech_API.sRecipeFile.add(GT_ConfigCategories.Machines.pulverization, aInput, true)) {
				sPulverizerRecipes.put(GT_Utility.stackToInt(aInput), new GT_PulverizerRecipe(aInput, aOutput1, aOutput2, aChance<=0?10:aChance));
			}
			
			if (Materials.Wood.contains(aOutput1)) {
				if (GregTech_API.sRecipeFile.add(GT_ConfigCategories.Machines.pulverization, aInput, true)) {
					if (aOutput2 == null)
						ThermalExpansion.addSawmillRecipe(80, GT_Utility.copy(aInput), GT_Utility.copy(aOutput1));
					else
						ThermalExpansion.addSawmillRecipe(80, GT_Utility.copy(aInput), GT_Utility.copy(aOutput1), GT_Utility.copy(aOutput2), aChance<=0?10:aChance);
				}
			} else {
				if (GregTech_API.sRecipeFile.add(GT_ConfigCategories.Machines.rockcrushing, aInput, true)) {
					try {
						if (aInput.itemID != Block.obsidian.blockID) {
//							mods.railcraft.api.crafting.IRockCrusherRecipe tRecipe = mods.railcraft.api.crafting.RailcraftCraftingManager.rockCrusher.createNewRecipe(GT_Utility.copyAmount(1, aInput), aInput.getItemDamage() != GregTech_API.ITEM_WILDCARD_DAMAGE, false);
//							tRecipe.addOutput(GT_Utility.copy(aOutput1), 1.0F/aInput.stackSize);
//							tRecipe.addOutput(GT_Utility.copy(aOutput2), (0.01F*(aChance<=0?10:aChance))/aInput.stackSize);
						}
					} catch(Throwable e) {/*Do nothing*/}
				}
				if (GregTech_API.sRecipeFile.add(GT_ConfigCategories.Machines.pulverization, aInput, true)) {
					if (aOutput2 == null)
						ThermalExpansion.addPulverizerRecipe(80, GT_Utility.copy(aInput), GT_Utility.copy(aOutput1));
					else
						ThermalExpansion.addPulverizerRecipe(80, GT_Utility.copy(aInput), GT_Utility.copy(aOutput1), GT_Utility.copy(aOutput2), aChance<=0?10:aChance);
				}
			}
		}
		return true;
	}
	
	/**
	 * Adds a Recipe to the Sawmills of GregTech and ThermalCraft
	 */
	public static boolean addSawmillRecipe(ItemStack aInput1, ItemStack aOutput1, ItemStack aOutput2) {
		aOutput1 = GT_OreDictUnificator.get(true, aOutput1);
		aOutput2 = GT_OreDictUnificator.get(true, aOutput2);
		if (aInput1 == null || aOutput1 == null) return false;
		if (!GregTech_API.sRecipeFile.add(GT_ConfigCategories.Machines.sawmill, aInput1, true)) return false;
	    try {
	    	ThermalExpansion.addSawmillRecipe(160, aInput1, aOutput1, aOutput2, 100);
		} catch(Throwable e) {/*Do nothing*/}
	    GregTech_API.sRecipeAdder.addSawmillRecipe(aInput1, GT_Items.Cell_Water.get(1), aOutput1, aOutput2, GT_Items.Cell_Empty.get(1));
		return true;
	}
	
	/**
	 * Induction Smelter Recipes and Alloy Smelter Recipes
	 */
	public static boolean addAlloySmelterRecipe(ItemStack aInput1, ItemStack aInput2, ItemStack aOutput1, int aDuration, int aEUt, boolean aAllowSecondaryInputEmpty) {
		aOutput1 = GT_OreDictUnificator.get(true, aOutput1);
		if (aInput1 == null || (aInput2 == null && !aAllowSecondaryInputEmpty) || aOutput1 == null) return false;
		boolean temp = false;
		if (GregTech_API.sRecipeAdder.addAlloySmelterRecipe(aInput1, aInput2, aOutput1, aDuration, aEUt)) temp = true;
		if (addInductionSmelterRecipe(aInput1, aInput2, aOutput1, null, aDuration * 2, 0)) temp = true;
		return temp;
	}
	
	/**
	 * Induction Smelter Recipes for TE
	 */
	public static boolean addInductionSmelterRecipe(ItemStack aInput1, ItemStack aInput2, ItemStack aOutput1, ItemStack aOutput2, int aEnergy, int aChance) {
		aOutput1 = GT_OreDictUnificator.get(true, aOutput1);
		aOutput2 = GT_OreDictUnificator.get(true, aOutput2);
		if (aInput1 == null || aOutput1 == null || GT_Utility.getContainerItem(aInput1) != null) return false;
		if (!GregTech_API.sRecipeFile.add(GT_ConfigCategories.Machines.inductionsmelter, aInput2==null?aInput1:aOutput1, true)) return false;
	    try {
	    	ThermalExpansion.addSmelterRecipe(aEnergy, GT_Utility.copy(aInput1), aInput2==null?new ItemStack(Block.sand, 1, 0):aInput2, aOutput1, aOutput2, aChance);
		} catch(Throwable e) {/*Do nothing*/}
		return true;
	}
	
	/**
	 * Smelts dusts to Ingots
	 */
	@Deprecated
	public static boolean addDustToIngotSmeltingRecipe(ItemStack aInput, ItemStack aOutput) {
		return false;
	}
	
	/**
	 * Smelts Ores to Ingots
	 */
	public static boolean addOreToIngotSmeltingRecipe(ItemStack aInput, ItemStack aOutput) {
		aOutput = GT_OreDictUnificator.get(true, aOutput);
		if (aInput == null || aOutput == null) return false;
		FurnaceRecipes.smelting().addSmelting(aInput.itemID, aInput.getItemDamage(), GT_Utility.copy(aOutput), 0.0F);
		return true;
	}
	
	private static Map<IRecipeInput, RecipeOutput>	sExtractorRecipes	= new HashMap<IRecipeInput, RecipeOutput>();
	private static Map<IRecipeInput, RecipeOutput>	sMaceratorRecipes	= new HashMap<IRecipeInput, RecipeOutput>();
	private static Map<IRecipeInput, RecipeOutput>	sCompressorRecipes	= new HashMap<IRecipeInput, RecipeOutput>();
	private static Map<ItemStack, Integer>			sMassfabRecipes		= new HashMap<ItemStack, Integer>();
	private static Map<ItemStack, Float>			sScrapboxRecipes	= new HashMap<ItemStack, Float>();
	
	public static Map<IRecipeInput, RecipeOutput> getExtractorRecipeList() {
		try {
			return (Map<IRecipeInput, RecipeOutput>)GT_Utility.getField(ic2.api.recipe.Recipes.extractor.getClass(), "recipes").get(ic2.api.recipe.Recipes.extractor);
		} catch(Throwable e) {/*Do nothing*/}
		return sExtractorRecipes;
	}
	
	public static Map<IRecipeInput, RecipeOutput> getCompressorRecipeList() {
		try {
			return (Map<IRecipeInput, RecipeOutput>)GT_Utility.getField(ic2.api.recipe.Recipes.compressor.getClass(), "recipes").get(ic2.api.recipe.Recipes.compressor);
		} catch(Throwable e) {/*Do nothing*/}
		return sCompressorRecipes;
	}
	
	public static Map<IRecipeInput, RecipeOutput> getMaceratorRecipeList() {
		try {
			return (Map<IRecipeInput, RecipeOutput>)GT_Utility.getField(ic2.api.recipe.Recipes.macerator.getClass(), "recipes").get(ic2.api.recipe.Recipes.macerator);
		} catch(Throwable e) {/*Do nothing*/}
		return sMaceratorRecipes;
	}
	
	public static Map<ItemStack, Integer> getMassFabricatorList() {
		try {
			return (Map<ItemStack, Integer>)GT_Utility.getField(ic2.api.recipe.Recipes.matterAmplifier.getClass(), "recipes").get(ic2.api.recipe.Recipes.matterAmplifier);
		} catch(Throwable e) {/*Do nothing*/}
		return sMassfabRecipes;
	}
	
	public static Map<ItemStack, Float> getScrapboxList() {
		try {
			return (Map<ItemStack, Float>)GT_Utility.getField(ic2.api.recipe.Recipes.scrapboxDrops.getClass(), "recipes").get(ic2.api.recipe.Recipes.scrapboxDrops);
		} catch(Throwable e) {/*Do nothing*/}
		return sScrapboxRecipes;
	}
	
	/**
	 * IC2-Compressor Recipe. Overloads old Recipes automatically
	 */
	public static boolean addCompressionRecipe(ItemStack aInput, ItemStack aOutput) {
		aOutput = GT_OreDictUnificator.get(true, aOutput);
		if (aInput == null || aOutput == null) return false;
		GT_Utility.removeSimpleIC2MachineRecipe(aInput, getCompressorRecipeList(), null);
		if (!GregTech_API.sRecipeFile.add(GT_ConfigCategories.Machines.compression, aInput, true)) return false;
		GT_Utility.addSimpleIC2MachineRecipe(aInput, getCompressorRecipeList(), aOutput);
		return true;
	}
	
	/**
	 * @param aValue Scrap = 5000, Scrapbox = 45000, Diamond Dust 125000
	 */
	public static boolean addIC2MatterAmplifier(ItemStack aAmplifier, int aValue) {
		if (aAmplifier == null || aValue <= 0) return false;
		if (!GregTech_API.sRecipeFile.add(GT_ConfigCategories.Machines.massfabamplifier, aAmplifier, true)) return false;
		try {
			NBTTagCompound tNBT = new NBTTagCompound();
			tNBT.setInteger("amplification", aValue);
			GT_Utility.callMethod(ic2.api.recipe.Recipes.matterAmplifier, "addRecipe", false, false, false, aAmplifier, tNBT);
		} catch(Throwable e) {/*Do nothing*/}
		return true;
	}
	
	/**
	 * Rolling Machine Crafting Recipe
	 */
	public static boolean addRollingMachineRecipe(ItemStack aResult, Object[] aRecipe) {
		aResult = GT_OreDictUnificator.get(true, aResult);
		if (aResult == null || aRecipe == null || aResult.stackSize <= 0) return false;
		try {
//			mods.railcraft.api.crafting.RailcraftCraftingManager.rollingMachine.getRecipeList().add(new ShapedOreRecipe(GT_Utility.copy(aResult), aRecipe));
		} catch(Throwable e) {
			return addCraftingRecipe(GT_Utility.copy(aResult), false, aRecipe);
		}
		return true;
	}
	
	private static boolean sBufferCraftingRecipes = true;
	private static ArrayList<IRecipe> sCraftingRecipeList = new ArrayList<IRecipe>(), sSingleNonBlockDamagableRecipeList = new ArrayList<IRecipe>();
	
	public static void stopBufferingCraftingRecipes() {
		sBufferCraftingRecipes = false;
		for (IRecipe tRecipe : sCraftingRecipeList) GameRegistry.addRecipe(tRecipe);
		sCraftingRecipeList.clear();
	}
	
	/**
	 * Regular Crafting Recipes. Deletes conflicting Recipes too.
	 */
	public static boolean addCraftingRecipe(ItemStack aResult, Object[] aRecipe) {
		return addCraftingRecipe(aResult, isElectricItem(aResult), aRecipe);
	}
	
	/**
	 * Regular Crafting Recipes. Deletes conflicting Recipes too.
	 */
	public static boolean addCraftingRecipe(ItemStack aResult, boolean aUseIC2Handler, Object[] aRecipe) {
		return addCraftingRecipe(aResult, aUseIC2Handler, false, aRecipe);
	}
	
	/**
	 * Regular Crafting Recipes. Deletes conflicting Recipes too.
	 */
	public static boolean addCraftingRecipe(ItemStack aResult, boolean aUseIC2Handler, boolean aMirrored, Object[] aRecipe) {
		return addCraftingRecipe(aResult, aUseIC2Handler, aMirrored, true, aRecipe);
	}
	
	/**
	 * Regular Crafting Recipes. Deletes conflicting Recipes too.
	 */
	public static boolean addCraftingRecipe(ItemStack aResult, boolean aUseIC2Handler, boolean aMirrored, boolean aBuffered, Object[] aRecipe) {
		return addCraftingRecipe(aResult, aUseIC2Handler, aMirrored, aBuffered, false, aRecipe);
	}
	
	/**
	 * Regular Crafting Recipes. Deletes conflicting Recipes too.
	 */
	public static boolean addCraftingRecipe(ItemStack aResult, boolean aUseIC2Handler, boolean aMirrored, boolean aBuffered, boolean aKeepNBT, Object[] aRecipe) {
		aResult = GT_OreDictUnificator.get(true, aResult);
		if (aRecipe == null || aRecipe.length <= 0) return false;
		for (byte i = 0; i < aRecipe.length; i++) {
			if (aRecipe[i] instanceof Enum) {
				aRecipe[i] = aRecipe[i].toString();
			}
		}
		try { while (true) {
		    String shape = "";
		    int idx = 0;
		    if (aRecipe[idx] instanceof Boolean) {
		    	idx++;
		    }
	        while (aRecipe[idx] instanceof String) {
	            String s = (String)aRecipe[idx++];
	            shape += s;
	            while (s.length() < 3) s+=" ";
	            if (s.length() > 3) throw new IllegalArgumentException();
	        }
		    if (aRecipe[idx] instanceof Boolean) {
		    	idx++;
		    }
	        HashMap<Character, ItemStack> itemMap = new HashMap<Character, ItemStack>();
	        itemMap.put(' ', null);
	        for (; idx < aRecipe.length; idx += 2) {
				if (aRecipe[idx] == null || aRecipe[idx + 1] == null) {
					if (GregTech_API.DEBUG_MODE) GT_Log.err.println("WARNING: Missing Item for shaped Recipe: " + (aResult==null?"null":aResult.getDisplayName()));
					return false;
				}
	            Character chr = (Character)aRecipe[idx];
	            Object in = aRecipe[idx + 1];
	            if (in instanceof ItemStack) {
	                itemMap.put(chr, GT_Utility.copy((ItemStack)in));
	            } else if (in instanceof String) {
	            	ItemStack tStack = GT_OreDictUnificator.getFirstOre(in, 1);
	            	if (tStack == null) break;
	                itemMap.put(chr, tStack);
	            } else {
	                throw new IllegalArgumentException();
	            }
	        }
	        ItemStack[] tRecipe = new ItemStack[9];
	        int x = -1;
	        for (char chr : shape.toCharArray()) {
	        	tRecipe[++x] = itemMap.get(chr);
	            if (tRecipe[x] != null)
	            	if (tRecipe[x].getItemDamage() == GregTech_API.ITEM_WILDCARD_DAMAGE)
	            		tRecipe[x].setItemDamage(0);
		    }
	        removeRecipe(tRecipe);
	        break;
		}} catch(Throwable e) {e.printStackTrace(GT_Log.err);}
		
		if (aResult == null || aResult.stackSize <= 0) return false;
		
		if (aResult.getItemDamage() == GregTech_API.ITEM_WILDCARD_DAMAGE || aResult.getItemDamage() < 0) throw new IllegalArgumentException();
		
		if (aKeepNBT) {
			GameRegistry.addRecipe(new GT_Shaped_NBT_Keeping_Recipe(GT_Utility.copy(aResult), aRecipe).setMirrored(aMirrored));
			return true;
		}
		if (aUseIC2Handler) {
			try {
				ic2.api.recipe.Recipes.advRecipes.addRecipe(GT_Utility.copy(aResult), aRecipe);
				return true;
			} catch (Throwable e) {/*Do nothing*/}
		}
		if (sBufferCraftingRecipes && aBuffered)
			sCraftingRecipeList.add(new ShapedOreRecipe(GT_Utility.copy(aResult), aRecipe).setMirrored(aMirrored));
		else
			GameRegistry.addRecipe(new ShapedOreRecipe(GT_Utility.copy(aResult), aRecipe).setMirrored(aMirrored));
		return true;
	}

	/**
	 * Shapeless Crafting Recipes. Deletes conflicting Recipes too.
	 */
	public static boolean addShapelessCraftingRecipe(ItemStack aResult, Object[] aRecipe) {
		return addShapelessCraftingRecipe(aResult, isElectricItem(aResult), aRecipe);
	}

	/**
	 * Shapeless Crafting Recipes. Deletes conflicting Recipes too.
	 */
	public static boolean addShapelessCraftingRecipe(ItemStack aResult, boolean aUseIC2Handler, Object[] aRecipe) {
		return addShapelessCraftingRecipe(aResult, aUseIC2Handler, true, aRecipe);
	}
	
	/**
	 * Shapeless Crafting Recipes. Deletes conflicting Recipes too.
	 */
	public static boolean addShapelessCraftingRecipe(ItemStack aResult, boolean aUseIC2Handler, boolean aBuffered, Object[] aRecipe) {
		return addShapelessCraftingRecipe(aResult, aUseIC2Handler, aBuffered, false, aRecipe);
	}
	
	/**
	 * Shapeless Crafting Recipes. Deletes conflicting Recipes too.
	 */
	public static boolean addShapelessCraftingRecipe(ItemStack aResult, boolean aUseIC2Handler, boolean aBuffered, boolean aKeepNBT, Object[] aRecipe) {
		aResult = GT_OreDictUnificator.get(true, aResult);
		if (aRecipe == null || aRecipe.length <= 0) return false;
		for (byte i = 0; i < aRecipe.length; i++) {
			if (aRecipe[i] instanceof Enum) {
				aRecipe[i] = aRecipe[i].toString();
			}
		}
		try {
	        ItemStack[] tRecipe = new ItemStack[9];
	        int i = 0;
			for (Object tObject : aRecipe) {
				if (tObject == null) {
					if (GregTech_API.DEBUG_MODE) GT_Log.err.println("WARNING: Missing Item for shapeless Recipe: " + (aResult==null?"null":aResult.getDisplayName()));
					return false;
				}
				if (tObject instanceof ItemStack) {
					tRecipe[i] = (ItemStack)tObject;
				} else if (tObject instanceof String) {
					tRecipe[i] = GT_OreDictUnificator.getFirstOre(tObject, 1);
				} else if (tObject instanceof Boolean) {
					//
				} else {
	                throw new IllegalArgumentException();
				}
				i++;
			}
	        removeRecipe(tRecipe);
		} catch(Throwable e) {e.printStackTrace(GT_Log.err);}
		
		if (aResult == null || aResult.stackSize <= 0) return false;
		
		if (aResult.getItemDamage() == GregTech_API.ITEM_WILDCARD_DAMAGE || aResult.getItemDamage() < 0) throw new IllegalArgumentException();
		
		if (aKeepNBT) {
			GameRegistry.addRecipe(new GT_Shapeless_NBT_Keeping_Recipe(GT_Utility.copy(aResult), aRecipe));
			return true;
		}
		if (aUseIC2Handler) {
			try {
				ic2.api.recipe.Recipes.advRecipes.addShapelessRecipe(GT_Utility.copy(aResult), aRecipe);
				return true;
			} catch (Throwable e) {/*Do nothing*/}
		}
		if (sBufferCraftingRecipes && aBuffered)
			sCraftingRecipeList.add(new ShapelessOreRecipe(GT_Utility.copy(aResult), aRecipe));
		else
			GameRegistry.addRecipe(new ShapelessOreRecipe(GT_Utility.copy(aResult), aRecipe));
		return true;
	}
	
	/**
	 * Removes a Smelting Recipe
	 */
	public static boolean removeFurnaceSmelting(ItemStack aInput, ItemStack aOutput) {
		boolean temp = false;
		if (aInput != null) {
			FurnaceRecipes.smelting().getMetaSmeltingList().remove(Arrays.asList(aInput.itemID, aInput.getItemDamage()));
			FurnaceRecipes.smelting().getSmeltingList().remove(aInput.itemID);
			temp = true;
		}
		if (aOutput != null) {
			
			temp = true;
		}
		return temp;
	}
	
	/**
	 * Removes a Crafting Recipe and gives you the former output of it.
	 * @param aRecipe The content of the Crafting Grid as ItemStackArray with length 9
	 * @return the output of the old Recipe or null if there was nothing.
	 */
    public static ItemStack removeRecipe(ItemStack... aRecipe) {
    	if (aRecipe == null) return null;
    	boolean temp = false;
    	for (byte i = 0; i < aRecipe.length; i++) {
    		if (aRecipe[i] != null) {
    			temp = true;
        		break;
    		}
    	}
    	if (!temp) return null;
    	ItemStack rReturn = null;
		InventoryCrafting aCrafting = new InventoryCrafting(new Container() {@Override public boolean canInteractWith(EntityPlayer var1) {return false;}}, 3, 3);
		for (int i = 0; i < aRecipe.length && i < 9; i++) aCrafting.setInventorySlotContents(i, aRecipe[i]);
		ArrayList<IRecipe> tList = (ArrayList<IRecipe>)CraftingManager.getInstance().getRecipeList();
		for (int i = 0; i < tList.size(); i++) {
			try {
				if (tList.get(i).matches(aCrafting, GregTech_API.sDummyWorld)) {
					rReturn = tList.get(i).getCraftingResult(aCrafting);
					tList.remove(i--);
				}
			} catch(Throwable e) {e.printStackTrace(GT_Log.err);}
		}
		for (int i = 0; i < sCraftingRecipeList.size(); i++) {
			try {
				if (sCraftingRecipeList.get(i).matches(aCrafting, GregTech_API.sDummyWorld)) {
					rReturn = sCraftingRecipeList.get(i).getCraftingResult(aCrafting);
					sCraftingRecipeList.remove(i--);
				}
			} catch(Throwable e) {e.printStackTrace(GT_Log.err);}
		}
		return rReturn;
    }
    
	/**
	 * Removes a Crafting Recipe.
	 * @param aOutput The output of the Recipe.
	 * @return if it has removed at least one Recipe.
	 */
    public static boolean removeRecipeByOutput(ItemStack aOutput) {
    	if (aOutput == null) return false;
    	boolean rReturn = false;
		ArrayList<IRecipe> tList = (ArrayList<IRecipe>)CraftingManager.getInstance().getRecipeList();
		for (int i = 0; i < tList.size(); i++) {
			if (GT_Utility.areStacksEqual(tList.get(i).getRecipeOutput(), aOutput)) {
				tList.remove(i--);
				rReturn = true;
			}
		}
		return rReturn;
    }
    
    /**
     * Checks all Crafting Handlers for Recipe Output
     * Used for the Autocrafting Table
     */
    public static ItemStack getAllRecipeOutput(World aWorld, ItemStack... aRecipe) {
    	if (aRecipe == null) return null;
    	boolean temp = false;
    	for (byte i = 0; i < aRecipe.length; i++) {
    		if (aRecipe[i] != null) {
    			temp = true;
        		break;
    		}
    	}
    	if (!temp) return null;
		InventoryCrafting aCrafting = new InventoryCrafting(new Container() {@Override
		public boolean canInteractWith(EntityPlayer var1) {return false;}}, 3, 3);
		for (int i = 0; i < 9 && i < aRecipe.length; i++) aCrafting.setInventorySlotContents(i, aRecipe[i]);
		ItemStack rOutput = CraftingManager.getInstance().findMatchingRecipe(aCrafting, aWorld);
		return GT_Utility.copy(rOutput);
    }
    
    /**
     * Gives you a copy of the Output from a Crafting Recipe
     * Used for Recipe Detection.
     */
    public static ItemStack getRecipeOutput(ItemStack... aRecipe) {
    	return getRecipeOutput(false, aRecipe);
    }
    
    /**
     * Gives you a copy of the Output from a Crafting Recipe
     * Used for Recipe Detection.
     */
    public static ItemStack getRecipeOutput(boolean aUncopiedStack, ItemStack... aRecipe) {
    	if (aRecipe == null) return null;
    	boolean temp = false;
    	for (byte i = 0; i < aRecipe.length; i++) {
    		if (aRecipe[i] != null) {
    			temp = true;
        		break;
    		}
    	}
    	if (!temp) return null;
		InventoryCrafting aCrafting = new InventoryCrafting(new Container() {@Override
		public boolean canInteractWith(EntityPlayer var1) {return false;}}, 3, 3);
		for (int i = 0; i < 9 && i < aRecipe.length; i++) aCrafting.setInventorySlotContents(i, aRecipe[i]);
		ArrayList<IRecipe> tList = (ArrayList<IRecipe>)CraftingManager.getInstance().getRecipeList();
		for (int i = 0; i < tList.size(); i++) {temp = false;
			try {
				temp = tList.get(i).matches(aCrafting, GregTech_API.sDummyWorld);
			} catch(Throwable e) {e.printStackTrace(GT_Log.err);}
			if (temp) {
				ItemStack tOutput = aUncopiedStack?tList.get(i).getRecipeOutput():tList.get(i).getCraftingResult(aCrafting);
				if (tOutput == null || tOutput.stackSize <= 0) {
					// Seriously, who would ever do that shit?
					if (!GregTech_API.sPostloadFinished) throw new GT_ItsNotMyFaultException("Seems another Mod added a Crafting Recipe with null Output. Tell the Developer of said Mod to fix that.");
				} else {
					if (aUncopiedStack) return tOutput;
					return GT_Utility.copy(tOutput);
				}
			}
		}
		return null;
    }

    /**
     * Gives you a list of the Outputs from a Crafting Recipe
     * If you have multiple Mods, which add Bronze Armor for example
     * This also removes old Recipes from the List.
     */
    public static ArrayList<ItemStack> getVanillyToolRecipeOutputs(ItemStack... aRecipe) {
    	if (!GregTech_API.sPostloadStarted || GregTech_API.sPostloadFinished) sSingleNonBlockDamagableRecipeList.clear();
    	if (sSingleNonBlockDamagableRecipeList.isEmpty()) {
    		for (IRecipe tRecipe : (ArrayList<IRecipe>)CraftingManager.getInstance().getRecipeList()) {
    			ItemStack tStack = tRecipe.getRecipeOutput();
    			if (tStack != null && tStack.getItem() != null && tStack.getMaxStackSize() == 1 && tStack.getMaxDamage() > 0 && !isElectricItem(tStack) && !(tStack.getItem() instanceof ItemBlock)) {
    				if (!(tRecipe instanceof ShapelessRecipes || tRecipe instanceof ShapelessOreRecipe)) {
    					if (tRecipe instanceof ShapedOreRecipe) {
    						boolean temp = true;
    						for (Object tObject : ((ShapedOreRecipe)tRecipe).getInput()) {
    							if (tObject != null && tObject instanceof ItemStack && (((ItemStack)tObject).getItem() == null || ((ItemStack)tObject).getMaxStackSize() < 2 || ((ItemStack)tObject).getMaxDamage() > 0 || ((ItemStack)tObject).getItem() instanceof ItemBlock)) {
    								temp = false;
    								break;
    							}
    						}
    						if (temp) sSingleNonBlockDamagableRecipeList.add(tRecipe);
    					} else if (tRecipe instanceof ShapedRecipes) {
    						boolean temp = true;
    						for (ItemStack tObject : ((ShapedRecipes)tRecipe).recipeItems) {
    							if (tObject != null && (tObject.getItem() == null || tObject.getMaxStackSize() < 2 || tObject.getMaxDamage() > 0 || tObject.getItem() instanceof ItemBlock)) {
    								temp = false;
    								break;
    							}
    						}
    						if (temp) sSingleNonBlockDamagableRecipeList.add(tRecipe);
    					} else {
    	    				sSingleNonBlockDamagableRecipeList.add(tRecipe);
    					}
    				}
    			}
    		}
    		GT_Log.out.println("GT_Mod: Created a List of Tool Recipes containing " + sSingleNonBlockDamagableRecipeList.size() + " Recipes for recycling." + (sSingleNonBlockDamagableRecipeList.size()>2048?" Scanning all these Recipes is the reason for the startup Lag you receive right now.":""));
    	}
    	ArrayList<ItemStack> rList = getRecipeOutputs(sSingleNonBlockDamagableRecipeList, true, aRecipe);
    	if (!GregTech_API.sPostloadStarted || GregTech_API.sPostloadFinished) sSingleNonBlockDamagableRecipeList.clear();
    	return rList;
    }
    
    /**
     * Gives you a list of the Outputs from a Crafting Recipe
     * If you have multiple Mods, which add Bronze Armor for example
     */
    public static ArrayList<ItemStack> getRecipeOutputs(ItemStack... aRecipe) {
    	return getRecipeOutputs(CraftingManager.getInstance().getRecipeList(), false, aRecipe);
    }
    
    /**
     * Gives you a list of the Outputs from a Crafting Recipe
     * If you have multiple Mods, which add Bronze Armor for example
     */
    public static ArrayList<ItemStack> getRecipeOutputs(List<IRecipe> aList, boolean aDeleteFromList, ItemStack... aRecipe) {
    	ArrayList<ItemStack> rList = new ArrayList<ItemStack>();
    	if (aRecipe == null) return rList;
    	boolean temp = false;
    	for (byte i = 0; i < aRecipe.length; i++) {
    		if (aRecipe[i] != null) {
    			temp = true;
        		break;
    		}
    	}
    	if (!temp) return rList;
    	InventoryCrafting aCrafting = new InventoryCrafting(new Container() {@Override
		public boolean canInteractWith(EntityPlayer var1) {return false;}}, 3, 3);
		for (int i = 0; i < 9 && i < aRecipe.length; i++) aCrafting.setInventorySlotContents(i, aRecipe[i]);
		for (int i = 0; i < aList.size(); i++) {
			temp = false;
			try {
				temp = aList.get(i).matches(aCrafting, GregTech_API.sDummyWorld);
			} catch(Throwable e) {e.printStackTrace(GT_Log.err);}
			if (temp) {
				ItemStack tOutput = aList.get(i).getCraftingResult(aCrafting);
				if (tOutput == null || tOutput.stackSize <= 0) {
					// Seriously, who would ever do that shit?
					if (!GregTech_API.sPostloadFinished) throw new GT_ItsNotMyFaultException("Seems another Mod added a Crafting Recipe with null Output. Tell the Developer of said Mod to fix that.");
				} else {
					rList.add(GT_Utility.copy(tOutput));
					if (aDeleteFromList) aList.remove(i--);
				}
			}
		}
		return rList;
    }
    
    /**
     * Used in my own Macerator. Decreases StackSize of the Input if wanted.
     */
    public static ItemStack getMaceratorOutput(ItemStack aInput, boolean aRemoveInput, ItemStack aOutputSlot) {
    	return GT_Utility.copy(getMachineOutput(aInput, getMaceratorRecipeList(), aRemoveInput, aOutputSlot)[0]);
    }
    
    /**
     * Used in my own Extractor. Decreases StackSize of the Input if wanted.
     */
    public static ItemStack getExtractorOutput(ItemStack aInput, boolean aRemoveInput, ItemStack aOutputSlot) {
    	return GT_Utility.copy(getMachineOutput(aInput, getExtractorRecipeList(), aRemoveInput, aOutputSlot)[0]);
    }
    
    /**
     * Used in my own Compressor. Decreases StackSize of the Input if wanted.
     */
    public static ItemStack getCompressorOutput(ItemStack aInput, boolean aRemoveInput, ItemStack aOutputSlot) {
    	return GT_Utility.copy(getMachineOutput(aInput, getCompressorRecipeList(), aRemoveInput, aOutputSlot)[0]);
    }
    
    /**
     * Used in my own Furnace.
     */
    public static ItemStack getSmeltingOutput(ItemStack aInput, boolean aRemoveInput, ItemStack aOutputSlot) {
    	if (aInput == null) return null;
    	ItemStack rStack = GT_Utility.copy(FurnaceRecipes.smelting().getSmeltingResult(aInput));
    	if (rStack != null && (aOutputSlot == null || (GT_Utility.areStacksEqual(rStack, aOutputSlot) && rStack.stackSize + aOutputSlot.stackSize <= aOutputSlot.getMaxStackSize()))) {
			if (aRemoveInput) aInput.stackSize--;
			return rStack;
		}
    	return null;
    }
    
    /**
     * Used in my own Machines. Decreases StackSize of the Input if wanted.
     * 
     * Checks also if there is enough Space in the Output Slots.
     */
    public static ItemStack[] getMachineOutput(ItemStack aInput, Map<IRecipeInput, RecipeOutput> aRecipeList, boolean aRemoveInput, ItemStack... aOutputSlots) {
    	if (aOutputSlots == null || aOutputSlots.length <= 0) return new ItemStack[0];
    	if (aInput == null) return new ItemStack[aOutputSlots.length];
    	try {
			for (Entry<IRecipeInput, RecipeOutput> tEntry : aRecipeList.entrySet()) {
				if (tEntry.getKey().matches(aInput)) {
					if (tEntry.getKey().getAmount() <= aInput.stackSize) {
						ItemStack[] tList = (ItemStack[])tEntry.getValue().items.toArray();
						if (tList.length == 0) break;
						ItemStack[] rList = new ItemStack[aOutputSlots.length];
						
						for (byte i = 0; i < aOutputSlots.length; i++) {
							if (tList[i] != null) {
								if (aOutputSlots[i] == null || (GT_Utility.areStacksEqual(tList[i], aOutputSlots[i]) && tList[i].stackSize + aOutputSlots[i].stackSize <= aOutputSlots[i].getMaxStackSize())) {
									rList[i] = GT_Utility.copy(tList[i]);
								} else {
							    	return new ItemStack[aOutputSlots.length];
								}
							}
						}
						
						if (aRemoveInput) aInput.stackSize-=tEntry.getKey().getAmount();
						return rList;
					}
					break;
				}
			}
    	} catch(Throwable e) {
    		if (GregTech_API.DEBUG_MODE) e.printStackTrace(GT_Log.err);
    	}
    	return new ItemStack[aOutputSlots.length];
    }
    
    /**
     * Used in my own Recycler.
     * 
     * Only produces Scrap if aScrapChance == 0. aScrapChance is usually the random Number I give to the Function
     * If you directly insert 0 as aScrapChance then you can check if its Recycler-Blacklisted or similar
     */
    public static ItemStack getRecyclerOutput(ItemStack aInput, int aScrapChance) {
    	if (aInput == null || aScrapChance != 0) return null;
		try {
			if (ic2.api.recipe.Recipes.recyclerBlacklist.contains(aInput)) return null;
		} catch (Throwable e) {/*Do nothing*/}
    	return GT_Items.IC2_Scrap.get(1);
    }
    
    /**
     * For the Scrapboxinator
     */
	public static ItemStack getRandomScrapboxDrop() {
		return ic2.api.recipe.Recipes.scrapboxDrops.getDrop(GT_Items.IC2_Scrapbox.get(1), false);
	}
	
    /**
     * Adds TileEntity to E-net
     */
	public static boolean addTileToEnet(World aWorld, TileEntity aTileEntity) {
		try {
			Object temp = GT_Utility.callPublicMethod(aTileEntity, "isAddedToEnergyNet");
			if (aTileEntity instanceof ic2.api.energy.tile.IEnergyTile && temp != null && temp instanceof Boolean && !((Boolean)temp)) {
				net.minecraftforge.event.Event tEvent = (net.minecraftforge.event.Event)Class.forName("ic2.api.energy.event.EnergyTileLoadEvent").getConstructors()[0].newInstance((ic2.api.energy.tile.IEnergyTile)aTileEntity);
				MinecraftForge.EVENT_BUS.post(tEvent);
				return true;
			}
		} catch(Throwable e) {
			GT_Log.err.println("E-net Error. Please report to GregTech ASAP!");
			e.printStackTrace(GT_Log.err);
		}
		return false;
	}
	
    /**
     * Removes TileEntity from E-net
     */
	public static boolean removeTileFromEnet(World aWorld, TileEntity aTileEntity) {
		try {
			Object temp = GT_Utility.callPublicMethod(aTileEntity, "isAddedToEnergyNet");
			if (aTileEntity instanceof ic2.api.energy.tile.IEnergyTile && temp != null && temp instanceof Boolean && (Boolean)temp) {
				net.minecraftforge.event.Event tEvent = (net.minecraftforge.event.Event)Class.forName("ic2.api.energy.event.EnergyTileUnloadEvent").getConstructors()[0].newInstance((ic2.api.energy.tile.IEnergyTile)aTileEntity);
				MinecraftForge.EVENT_BUS.post(tEvent);
				return true;
			}
		} catch(Throwable e) {
			GT_Log.err.println("E-net Error. Please report to GregTech ASAP!");
			e.printStackTrace(GT_Log.err);
		}
		return false;
	}
	
    /**
     * Emits Energy to the E-net. Uses the internals of GT TileEntities for emitting Energy if it is not connected to the IC-2-Enet.
     * @return the remaining Energy.
     */
	public static int emitEnergyToEnet(int aVoltage, World aWorld, TileEntity aTileEntity) {
		try {
			Object temp = GT_Utility.callPublicMethod(aTileEntity, "isAddedToEnergyNet");
			if (aTileEntity instanceof ic2.api.energy.tile.IEnergySource && temp != null && temp instanceof Boolean && (Boolean)temp) {
				return aVoltage;
			}
		} catch(Throwable e) {/*Do nothing*/}
		if (aTileEntity instanceof IBasicEnergyContainer && aTileEntity instanceof IHasWorldObjectAndCoords) {
			for (byte i = 0; i < 6; i++) if (((IBasicEnergyContainer)aTileEntity).outputsEnergyTo(i)) {
				TileEntity tTileEntity = ((IHasWorldObjectAndCoords)aTileEntity).getTileEntityAtSide(i);
				if (tTileEntity instanceof IBasicEnergyContainer) {
					if (((IBasicEnergyContainer)tTileEntity).inputEnergyFrom(GT_Utility.getOppositeSide(i))) {
						if (((IBasicEnergyContainer)tTileEntity).getStoredEU() < ((IBasicEnergyContainer)tTileEntity).getEUCapacity()) {
							if (((IBasicEnergyContainer)tTileEntity).injectEnergyUnits(GT_Utility.getOppositeSide(i), aVoltage, 1)) {
								return 0;
							}
						}
					}
				}
			}
		}
		return aVoltage;
	}
	
	/**
	 * Charges an Electric Item. Only if it's a valid Electric Item of course.
	 * @return the actually used Energy.
	 */
	public static int chargeElectricItem(ItemStack aStack, int aCharge, int aTier, boolean aIgnoreLimit, boolean aSimulate) {
		try {
			if (isElectricItem(aStack)) {
				return ic2.api.item.ElectricItem.manager.charge(aStack, aCharge, aTier, aIgnoreLimit, aSimulate);
			}
		} catch (Throwable e) {/*Do nothing*/}
		return 0;
	}
	
	/**
	 * Discharges an Electric Item. Only if it's a valid Electric Item for that of course.
	 * @return the Energy got from the Item.
	 */
	public static int dischargeElectricItem(ItemStack aStack, int aCharge, int aTier, boolean aIgnoreLimit, boolean aSimulate, boolean aIgnoreDischargability) {
		try {
			if (isElectricItem(aStack)) {
				if (aIgnoreDischargability || ((ic2.api.item.IElectricItem)aStack.getItem()).canProvideEnergy(aStack)) {
					return ic2.api.item.ElectricItem.manager.discharge(aStack, aCharge, aTier, aIgnoreLimit, aSimulate);
				}
			}
		} catch (Throwable e) {/*Do nothing*/}
		return 0;
	}
	
	/**
	 * Gets the max Charge Level of an Electric Item. Only if it's a valid Electric Item for that of course.
	 * @return the Energy got from the Item.
	 */
	public static int getMaxElectricCharge(ItemStack aStack) {
		try {
			if (isElectricItem(aStack)) {
				return ((ic2.api.item.IElectricItem)aStack.getItem()).getMaxCharge(aStack);
			}
		} catch (Throwable e) {/*Do nothing*/}
		return 0;
	}
	
	/**
	 * Uses an Electric Item. Only if it's a valid Electric Item for that of course.
	 * @return if the action was successful
	 */
	public static boolean canUseElectricItem(ItemStack aStack, int aCharge) {
		try {
			if (isElectricItem(aStack)) {
				return ic2.api.item.ElectricItem.manager.canUse(aStack, aCharge);
			}
		} catch (Throwable e) {/*Do nothing*/}
		return false;
	}
	
	/**
	 * Uses an Electric Item. Only if it's a valid Electric Item for that of course.
	 * @return if the action was successful
	 */
	public static boolean useElectricItem(ItemStack aStack, int aCharge, EntityPlayer aPlayer) {
		try {
			if (isElectricItem(aStack)) {
				ic2.api.item.ElectricItem.manager.use(aStack, 0, aPlayer);
				if (ic2.api.item.ElectricItem.manager.canUse(aStack, aCharge)) {
					return ic2.api.item.ElectricItem.manager.use(aStack, aCharge, aPlayer);
				}
			}
		} catch (Throwable e) {/*Do nothing*/}
		return false;
	}
	
	/**
	 * Uses an Item. Tries to discharge in case of Electric Items
	 */
	public static boolean damageOrDechargeItem(ItemStack aStack, int aDamage, int aDecharge, EntityLivingBase aPlayer) {
		if (GT_Utility.isStackInvalid(aStack) || (aStack.getMaxStackSize() <= 1 && aStack.stackSize > 1)) return false;
		if (aPlayer != null && aPlayer instanceof EntityPlayer && ((EntityPlayer)aPlayer).capabilities.isCreativeMode) return true;
		if (GT_ModHandler.isElectricItem(aStack)) {
			if (canUseElectricItem(aStack, aDecharge)) {
				if (aPlayer != null && aPlayer instanceof EntityPlayer) {
					return GT_ModHandler.useElectricItem(aStack, aDecharge, (EntityPlayer)aPlayer);
				}
				return GT_ModHandler.dischargeElectricItem(aStack, aDecharge, Integer.MAX_VALUE, true, false, true) >= aDecharge;
			}
		} else if (aStack.getItem().isDamageable()) {
			if (aPlayer == null) {
				aStack.setItemDamage(aStack.getItemDamage() + aDamage);
			} else {
				aStack.damageItem(aDamage, aPlayer);
			}
			if (aStack.getItemDamage() >= aStack.getMaxDamage()) {
				aStack.setItemDamage(aStack.getMaxDamage()+1);
				if (GT_Utility.getContainerItem(aStack) != null) {
					ItemStack tStack = aStack.getItem().getContainerItemStack(aStack);
					if (tStack != null) {
						aStack.itemID = tStack.itemID;
						aStack.setItemDamage(tStack.getItemDamage());
						aStack.stackSize = tStack.stackSize;
						aStack.setTagCompound(tStack.getTagCompound());
					}
				}
			}
			return true;
		}
		return false;
	}
	
	/**
	 * Uses a Soldering Iron
	 */
	public static boolean useSolderingIron(ItemStack aStack, EntityLivingBase aPlayer) {
		if (aPlayer == null || aStack == null) return false;
		if (GT_Utility.isItemStackInList(aStack, GregTech_API.sSolderingToolList)) {
			if (aPlayer instanceof EntityPlayer) {
				EntityPlayer tPlayer = (EntityPlayer)aPlayer;
				if (tPlayer.capabilities.isCreativeMode) return true;
				for (int i = 0; i < tPlayer.inventory.mainInventory.length; i++) {
					if (GT_Utility.isItemStackInList(tPlayer.inventory.mainInventory[i], GregTech_API.sSolderingMetalList)) {
						if (damageOrDechargeItem(aStack, 1, 1000, tPlayer)) {
							if (tPlayer.inventory.mainInventory[i].getItemDamage() >= tPlayer.inventory.mainInventory[i].getMaxDamage()) tPlayer.inventory.mainInventory[i] = null;
						    if (damageOrDechargeItem(tPlayer.inventory.mainInventory[i], 1, 1000, tPlayer)) {
								if (tPlayer.inventory.mainInventory[i].getItemDamage() >= tPlayer.inventory.mainInventory[i].getMaxDamage()) tPlayer.inventory.mainInventory[i] = null;
							    if (tPlayer.inventoryContainer != null) tPlayer.inventoryContainer.detectAndSendChanges();
								return true;
							}
						}
					}
				}
			} else {
				damageOrDechargeItem(aStack, 1, 1000, aPlayer);
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Is this an electric Item, which can charge other Items?
	 */
	public static boolean isChargerItem(ItemStack aStack) {
		try {
			if (isElectricItem(aStack)) {
				return ((ic2.api.item.IElectricItem)aStack.getItem()).canProvideEnergy(aStack);
			}
		} catch (Throwable e) {/*Do nothing*/}
		return false;
	}
	
	/**
	 * Is this an electric Item?
	 */
	public static boolean isElectricItem(ItemStack aStack) {
		try {
			return aStack != null && aStack.getItem() instanceof ic2.api.item.IElectricItem;
		} catch (Throwable e) {/*Do nothing*/}
		return false;
	}
	
	public static boolean acceptsGT(TileEntity aTileEntity) {
		try {
			return aTileEntity instanceof gregtechmod.api.interfaces.IBasicEnergyContainer;
		} catch (Throwable e) {/*Do nothing*/}
		return false;
	}
	
	public static boolean acceptsEU(TileEntity aTileEntity) {
		try {
			return aTileEntity instanceof ic2.api.energy.tile.IEnergyAcceptor;
		} catch (Throwable e) {/*Do nothing*/}
		return false;
	}
	
	public static boolean acceptsMJ(TileEntity aTileEntity) {
		try {
			return aTileEntity instanceof buildcraft.api.power.IPowerReceptor;
		} catch (Throwable e) {/*Do nothing*/}
		return false;
	}
	
	public static boolean acceptsUE(TileEntity aTileEntity) {
		try {
//			return aTileEntity instanceof universalelectricity.core.block.IElectricalStorage;
		} catch (Throwable e) {/*Do nothing*/}
		return false;
	}
	
	public static Object sBoxableWrapper = GT_Utility.callConstructor("gregtechmod.api.util.GT_IBoxableWrapper", 0, null, false);
	
	public static void registerBoxableItemToToolBox(ItemStack aStack) {
		if (aStack != null) registerBoxableItemToToolBox(aStack.getItem());
	}
	
	public static void registerBoxableItemToToolBox(Item aItem) {
		if (aItem != null && sBoxableWrapper != null) {
			try {
				ic2.api.item.ItemWrapper.registerBoxable(aItem, (IBoxable)sBoxableWrapper);
			} catch(Throwable e) {/*Do nothing*/}
		}
	}
	
	public static int getCapsuleCellContainerCountMultipliedWithStackSize(ItemStack aStack) {
		if (aStack == null) return 0;
		return getCapsuleCellContainerCount(aStack)*aStack.stackSize;
	}
	
	public static int getCapsuleCellContainerCount(ItemStack aStack) {
		if (aStack == null) return 0;
		Item tItem = aStack.getItem();
		if (tItem == null) return 0;
		if (GT_Utility.areStacksEqual(GT_Utility.getContainerForFilledItem(aStack), GT_Items.Cell_Empty.get(1)) || OrePrefixes.cell.contains(aStack) || OrePrefixes.cellPlasma.contains(aStack)) return 1;
		if (GT_Utility.areStacksEqual(aStack, getIC2Item("hydratedCoalCell", 1, GregTech_API.ITEM_WILDCARD_DAMAGE))) return 1;
		return 0;
	}
	
	/**
	 * Copy of the original Helper Class of Thermal Expansion, just to make sure it works even when other Mods include TE-APIs
	 */
	public static class ThermalExpansion {
		public static void addFurnaceRecipe(int energy, ItemStack input, ItemStack output) {
		    NBTTagCompound toSend = new NBTTagCompound();
		    toSend.setInteger("energy", energy);
		    toSend.setCompoundTag("input", new NBTTagCompound());
		    toSend.setCompoundTag("output", new NBTTagCompound());
		    input.writeToNBT(toSend.getCompoundTag("input"));
		    output.writeToNBT(toSend.getCompoundTag("output"));
		    FMLInterModComms.sendMessage("ThermalExpansion", "FurnaceRecipe", toSend);
	    }
		
	    public static void addPulverizerRecipe(int energy, ItemStack input, ItemStack primaryOutput) {
	        addPulverizerRecipe(energy, input, primaryOutput, null, 0);
	    }
	    
	    public static void addPulverizerRecipe(int energy, ItemStack input, ItemStack primaryOutput, ItemStack secondaryOutput) {
	    	addPulverizerRecipe(energy, input, primaryOutput, secondaryOutput, 100);
	    }
	    
	    public static void addPulverizerRecipe(int energy, ItemStack input, ItemStack primaryOutput, ItemStack secondaryOutput, int secondaryChance) {
	        NBTTagCompound toSend = new NBTTagCompound();
	        toSend.setInteger("energy", energy);
	        toSend.setCompoundTag("input", new NBTTagCompound());
	        toSend.setCompoundTag("primaryOutput", new NBTTagCompound());
	        toSend.setCompoundTag("secondaryOutput", new NBTTagCompound());
	        input.writeToNBT(toSend.getCompoundTag("input"));
	        primaryOutput.writeToNBT(toSend.getCompoundTag("primaryOutput"));
	        if (secondaryOutput != null) secondaryOutput.writeToNBT(toSend.getCompoundTag("secondaryOutput"));
	        toSend.setInteger("secondaryChance", secondaryChance);
	        FMLInterModComms.sendMessage("ThermalExpansion", "PulverizerRecipe", toSend);
	    }
	    
	    public static void addSawmillRecipe(int energy, ItemStack input, ItemStack primaryOutput) {
	        addSawmillRecipe(energy, input, primaryOutput, null, 0);
	    }
	    
	    public static void addSawmillRecipe(int energy, ItemStack input, ItemStack primaryOutput, ItemStack secondaryOutput) {
	        addSawmillRecipe(energy, input, primaryOutput, secondaryOutput, 100);
	    }
	    
	    public static void addSawmillRecipe(int energy, ItemStack input, ItemStack primaryOutput, ItemStack secondaryOutput, int secondaryChance) {
	        NBTTagCompound toSend = new NBTTagCompound();
	        toSend.setInteger("energy", energy);
	        toSend.setCompoundTag("input", new NBTTagCompound());
	        toSend.setCompoundTag("primaryOutput", new NBTTagCompound());
	        toSend.setCompoundTag("secondaryOutput", new NBTTagCompound());
	        input.writeToNBT(toSend.getCompoundTag("input"));
	        primaryOutput.writeToNBT(toSend.getCompoundTag("primaryOutput"));
	        if (secondaryOutput != null) secondaryOutput.writeToNBT(toSend.getCompoundTag("secondaryOutput"));
	        toSend.setInteger("secondaryChance", secondaryChance);
	        FMLInterModComms.sendMessage("ThermalExpansion", "SawmillRecipe", toSend);
	    }
	    
	    public static void addSmelterRecipe(int energy, ItemStack primaryInput, ItemStack secondaryInput, ItemStack primaryOutput) {
	        addSmelterRecipe(energy, primaryInput, secondaryInput, primaryOutput, null, 0);
	    }
	    
	    public static void addSmelterRecipe(int energy, ItemStack primaryInput, ItemStack secondaryInput, ItemStack primaryOutput, ItemStack secondaryOutput) {
	        addSmelterRecipe(energy, primaryInput, secondaryInput, primaryOutput, secondaryOutput, 100);
	    }
	    
	    public static void addSmelterRecipe(int energy, ItemStack primaryInput, ItemStack secondaryInput, ItemStack primaryOutput, ItemStack secondaryOutput, int secondaryChance) {
	        NBTTagCompound toSend = new NBTTagCompound();
	        toSend.setInteger("energy", energy);
	        toSend.setCompoundTag("primaryInput", new NBTTagCompound());
	        toSend.setCompoundTag("secondaryInput", new NBTTagCompound());
	        toSend.setCompoundTag("primaryOutput", new NBTTagCompound());
	        toSend.setCompoundTag("secondaryOutput", new NBTTagCompound());
	        primaryInput.writeToNBT(toSend.getCompoundTag("primaryInput"));
	        secondaryInput.writeToNBT(toSend.getCompoundTag("secondaryInput"));
	        primaryOutput.writeToNBT(toSend.getCompoundTag("primaryOutput"));
	        if (secondaryOutput != null) secondaryOutput.writeToNBT(toSend.getCompoundTag("secondaryOutput"));
	        toSend.setInteger("secondaryChance", secondaryChance);
	        FMLInterModComms.sendMessage("ThermalExpansion", "SmelterRecipe", toSend);
	    }
	    
	    public static void addSmelterBlastOre(Materials aMaterial) {
	        NBTTagCompound toSend = new NBTTagCompound();
	        toSend.setString("oreType", aMaterial.toString());
	        FMLInterModComms.sendMessage("ThermalExpansion", "SmelterBlastOreType", toSend);
	    }
	    
	    public static void addCrucibleRecipe(int energy, ItemStack input, FluidStack output) {
	        NBTTagCompound toSend = new NBTTagCompound();
	        toSend.setInteger("energy", energy);
	        toSend.setCompoundTag("input", new NBTTagCompound());
	        toSend.setCompoundTag("output", new NBTTagCompound());
	        input.writeToNBT(toSend.getCompoundTag("input"));
	        output.writeToNBT(toSend.getCompoundTag("output"));
	        FMLInterModComms.sendMessage("ThermalExpansion", "CrucibleRecipe", toSend);
	    }
	    
	    public static void addTransposerFill(int energy, ItemStack input, ItemStack output, FluidStack fluid, boolean reversible) {
	        NBTTagCompound toSend = new NBTTagCompound();
	        toSend.setInteger("energy", energy);
	        toSend.setCompoundTag("input", new NBTTagCompound());
	        toSend.setCompoundTag("output", new NBTTagCompound());
	        toSend.setCompoundTag("fluid", new NBTTagCompound());
	        input.writeToNBT(toSend.getCompoundTag("input"));
	        output.writeToNBT(toSend.getCompoundTag("output"));
	        toSend.setBoolean("reversible", reversible);
	        fluid.writeToNBT(toSend.getCompoundTag("fluid"));
	        FMLInterModComms.sendMessage("ThermalExpansion", "TransposerFillRecipe", toSend);
	    }
	    
	    public static void addTransposerExtract(int energy, ItemStack input, ItemStack output, FluidStack fluid, int chance, boolean reversible) {
	        NBTTagCompound toSend = new NBTTagCompound();
	        toSend.setInteger("energy", energy);
	        toSend.setCompoundTag("input", new NBTTagCompound());
	        toSend.setCompoundTag("output", new NBTTagCompound());
	        toSend.setCompoundTag("fluid", new NBTTagCompound());
	        input.writeToNBT(toSend.getCompoundTag("input"));
	        output.writeToNBT(toSend.getCompoundTag("output"));
	        toSend.setBoolean("reversible", reversible);
	        toSend.setInteger("chance", chance);
	        fluid.writeToNBT(toSend.getCompoundTag("fluid"));
	        FMLInterModComms.sendMessage("ThermalExpansion", "TransposerExtractRecipe", toSend);
	    }
	    
	    public static void addMagmaticFuel(String fluidName, int energy) {
	        NBTTagCompound toSend = new NBTTagCompound();
	        toSend.setString("fluidName", fluidName);
	        toSend.setInteger("energy", energy);
	        FMLInterModComms.sendMessage("ThermalExpansion", "MagmaticFuel", toSend);
	    }
	    
	    public static void addCompressionFuel(String fluidName, int energy) {
	        NBTTagCompound toSend = new NBTTagCompound();
	        toSend.setString("fluidName", fluidName);
	        toSend.setInteger("energy", energy);
	        FMLInterModComms.sendMessage("ThermalExpansion", "CompressionFuel", toSend);
	    }
	    
	    public static void addCoolant(String fluidName, int energy) {
	        NBTTagCompound toSend = new NBTTagCompound();
	        toSend.setString("fluidName", fluidName);
	        toSend.setInteger("energy", energy);
	        FMLInterModComms.sendMessage("ThermalExpansion", "Coolant", toSend);
	    }
	}
}