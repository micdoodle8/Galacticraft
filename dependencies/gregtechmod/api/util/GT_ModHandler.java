package gregtechmod.api.util;

import gregtechmod.api.GregTech_API;
import gregtechmod.api.interfaces.IBasicEnergyContainer;
import gregtechmod.api.interfaces.ICapsuleCellContainer;
import gregtechmod.api.interfaces.IHasWorldObjectAndCoords;
import ic2.api.item.IBoxable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import net.minecraft.block.Block;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.oredict.ShapelessOreRecipe;
import cpw.mods.fml.common.registry.GameRegistry;

/**
 * NEVER INCLUDE THIS FILE IN YOUR MOD!!!
 * 
 * This is the Interface I use for interacting with other Mods.
 * 
 * Due to the many imports, this File can cause compile Problems if not all the APIs are installed
 */
public class GT_ModHandler {
	public static volatile int VERSION = 402;
	
	/**
	 * These are getting assigned somewhen in the Load Phase
	 */
	public static ItemStack
			mTCFluxEssence = null,
			mTCWispEssence = null,
			mTCResource = null,
			mTCCrystal = null,
			mNuggetIron	= null,
			mNuggetCopper = null,
			mNuggetTin = null,
			mNuggetSilver = null,
			mNuggetLead	= null,
			mForcicium = null,
			mIronNugget = null,
			mSilverNugget = null,
			mTinNugget = null,
			mCopperNugget = null,
			mRuby = null,
			mGreenSapphire = null,
			mSapphire = null,
			mSilver = null,
			mTin = null,
			mCopper = null,
			mNikolite = null,
			mRedAlloy = null,
			mBlueAlloy = null,
			mBrass = null,
			mSiliconBoule = null,
			mSiliconWafer = null,
			mBlueWafer = null,
			mRedWafer = null,
			mRPTinPlate = null,
			mFineCopper = null,
			mFineIron = null,
			mCopperCoil = null,
			mBlutricMotor = null,
			mCanvas = null,
			mDiamondDrawplate = null,
			mBCFuelBucket = null,
			mBCWoodGear = null,
			mBCStoneGear = null,
			mBCIronGear = null,
			mBCGoldGear = null,
			mBCDiamondGear = null,
			mIC_Cell = null,
			mIC_AirCell = null,
			mIC_WaterCell = null,
			mIC_LavaCell = null,
			mIC_Fuelcan = null;
	
	public static boolean isIC2loaded() {
		return getIC2Item("resin", 1) != null;
	}
	
	public static boolean isRCloaded() {
		return getRCItem("machine.alpha.rolling.machine", 1) != null;
	}
	
	public static boolean isTEloaded() {
		return getTEItem("slag", 1) != null;
	}
	
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
	public static FluidStack getWater(int aAmount) {
		return FluidRegistry.getFluidStack("water", aAmount);
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
	public static FluidStack getLava(int aAmount) {
		return FluidRegistry.getFluidStack("lava", aAmount);
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
	public static FluidStack getSteam(int aAmount) {
		return FluidRegistry.getFluidStack("steam", aAmount);
	}
	
	/**
	 * I was just really tired of always writing the same String, without being able to just use Ctrl+Space for Auto-Completing the Function
	 */
	public static ItemStack getEmptyCell(int aAmount) {
		if (mIC_Cell == null) mIC_Cell = getIC2Item("cell", 1);
		return new ItemStack(mIC_Cell.getItem(), aAmount, 0);
	}
	
	/**
	 * I was just really tired of always writing the same String, without being able to just use Ctrl+Space for Auto-Completing the Function
	 */
	public static ItemStack getEmptyFuelCan(int aAmount) {
		if (mIC_Fuelcan == null) mIC_Fuelcan = getIC2Item("fuelCan", 1);
		return new ItemStack(mIC_Fuelcan.getItem(), aAmount, 0);
	}
	
	/**
	 * I was just really tired of always writing the same String, without being able to just use Ctrl+Space for Auto-Completing the Function
	 */
	public static ItemStack getAirCell(int aAmount) {
		if (mIC_AirCell == null) mIC_AirCell = getIC2Item("airCell", 1);
		return new ItemStack(mIC_AirCell.getItem(), aAmount, 0);
	}
	
	/**
	 * I was just really tired of always writing the same String, without being able to just use Ctrl+Space for Auto-Completing the Function
	 */
	public static ItemStack getWaterCell(int aAmount) {
		if (mIC_WaterCell == null) mIC_WaterCell = getIC2Item("waterCell", 1);
		return new ItemStack(mIC_WaterCell.getItem(), aAmount, 0);
	}
	
	/**
	 * I was just really tired of always writing the same String, without being able to just use Ctrl+Space for Auto-Completing the Function
	 */
	public static ItemStack getLavaCell(int aAmount) {
		if (mIC_LavaCell == null) mIC_LavaCell = getIC2Item("lavaCell", 1);
		return new ItemStack(mIC_LavaCell.getItem(), aAmount, 0);
	}
	
	private static final Map<String, ItemStack> sIC2ItemMap = new HashMap<String, ItemStack>();
	
	/**
	 * Gets an Item from IndustrialCraft
	 */
	public static ItemStack getIC2Item(String aItem, int aAmount) {
		if (aItem == null || aItem.equals("") || !GregTech_API.sPreloadStarted) return null;
		if (!sIC2ItemMap.containsKey(aItem)) try {sIC2ItemMap.put(aItem, ic2.api.item.Items.getItem(aItem));} catch (Throwable e) {}
		ItemStack rStack = sIC2ItemMap.get(aItem);
		if (rStack == null || rStack.getItem() == null) return null;
		rStack = rStack.copy();
		rStack.stackSize = aAmount;
		return rStack;
	}
	
	/**
	 * Gets an Item from IndustrialCraft, and returns a Replacement Item if not possible
	 */
	public static ItemStack getIC2Item(String aItem, int aAmount, ItemStack aReplacement) {
		ItemStack rStack = getIC2Item(aItem, aAmount);
		if (rStack == null) return aReplacement==null?null:aReplacement.copy().splitStack(aAmount);
		return rStack;
	}
	
	/**
	 * Gets an Item from IndustrialCraft, but the Damage Value can be specified
	 */
	public static ItemStack getIC2Item(String aItem, int aAmount, int aMeta) {
		ItemStack rStack = getIC2Item(aItem, aAmount);
		if (rStack == null) return null;
		rStack.setItemDamage(aMeta);
		return rStack;
	}
	
	/**
	 * Gets an Item from IndustrialCraft, but the Damage Value can be specified, and returns a Replacement Item with the same Damage if not possible
	 */
	public static ItemStack getIC2Item(String aItem, int aAmount, int aMeta, ItemStack aReplacement) {
		ItemStack rStack = getIC2Item(aItem, aAmount, aReplacement);
		if (rStack == null) return null;
		rStack.setItemDamage(aMeta);
		return rStack;
	}
	
	/**
	 * Gets an Item from RailCraft
	 */
	public static ItemStack getRCItem(String aItem, int aAmount) {
		if (aItem == null || aItem.equals("")) return null;
		ItemStack rStack = null;
		try {
			rStack = GameRegistry.findItemStack("Railcraft", aItem, aAmount);
		} catch (Throwable e) {}
		if (rStack == null) return null;
		if (rStack.getItem() == null) return null;
		rStack = rStack.copy();
		rStack.stackSize = aAmount;
		return rStack;
	}
	
	/**
	 * Gets an Item from RailCraft, and returns a Replacement Item if not possible
	 */
	public static ItemStack getRCItem(String aItem, int aAmount, ItemStack aReplacement) {
		ItemStack rStack = getRCItem(aItem, aAmount);
		if (rStack == null) return aReplacement==null?null:aReplacement.copy().splitStack(aAmount);
		return rStack;
	}
	
	/**
	 * Gets an Item from RailCraft, but the Damage Value can be specified
	 */
	public static ItemStack getRCItem(String aItem, int aAmount, int aMeta) {
		ItemStack rStack = getRCItem(aItem, aAmount);
		if (rStack == null) return null;
		rStack.setItemDamage(aMeta);
		return rStack;
	}
	
	/**
	 * Gets an Item from RailCraft, but the Damage Value can be specified, and returns a Replacement Item with the same Damage if not possible
	 */
	public static ItemStack getRCItem(String aItem, int aAmount, int aMeta, ItemStack aReplacement) {
		ItemStack rStack = getRCItem(aItem, aAmount, aReplacement);
		if (rStack == null) return null;
		rStack.setItemDamage(aMeta);
		return rStack;
	}
	
	/**
	 * Gets an Item from ThermoCraft
	 */
	public static ItemStack getTEItem(String aItem, int aAmount) {
		return null;
	}
	
	/**
	 * Gets an Item from ThermoCraft, but the Damage Value can be specified
	 */
	public static ItemStack getTEItem(String aItem, int aAmount, int aMeta) {
		ItemStack rStack = getTEItem(aItem, aAmount);
		if (rStack == null) return null;
		rStack.setItemDamage(aMeta);
		return rStack;
	}
	
	/**
	 * Gets an Item from ThermoCraft, and returns a Replacement Item if not possible
	 */
	public static ItemStack getTEItem(String aItem, int aAmount, ItemStack aReplacement) {
		ItemStack rStack = getTEItem(aItem, aAmount);
		if (rStack == null) return aReplacement==null?null:aReplacement.copy().splitStack(aAmount);
		return rStack;
	}
	
	/**
	 * Gets an Item from ThermoCraft, but the Damage Value can be specified, and returns a Replacement Item with the same Damage if not possible
	 */
	public static ItemStack getTEItem(String aItem, int aAmount, int aMeta, ItemStack aReplacement) {
		ItemStack rStack = getTEItem(aItem, aAmount, aReplacement);
		if (rStack == null) return null;
		rStack.setItemDamage(aMeta);
		return rStack;
	}
	
	/**
	 * Gets an Item from Forestry
	 */
	public static ItemStack getFRItem(String aItem, int aAmount) {
	    return null;
	}
	
	/**
	 * Gets an Item from Forestry, but the Damage Value can be specified
	 */
	public static ItemStack getFRItem(String aItem, int aAmount, int aMeta) {
		ItemStack rStack = getFRItem(aItem, aAmount);
		if (rStack == null) return null;
		rStack.setItemDamage(aMeta);
		return rStack;
	}
	
	/**
	 * @param aValue Fuel value in EU
	 */
	public static ItemStack getFuelCan(int aValue) {
		if (aValue <= 0) return getIC2Item("fuelCan", 1);
		ItemStack rFuelCanStack = getIC2Item("filledFuelCan", 1);
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
		if (aFuelCan == null || !aFuelCan.isItemEqual(getIC2Item("filledFuelCan", 1))) return 0;
		NBTTagCompound tNBT = aFuelCan.getTagCompound();
		if (tNBT == null) return 0;
		return tNBT.getInteger("value")*5;
	}
	
	/**
	 * adds an RC-Boiler Fuel
	 */
	public static void addBoilerFuel(FluidStack aLiquid, int aValue) {
	    
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
		} catch (Throwable e) {}
		return true;
	}
	
	/**
	 * Adds a Scrapbox Drop. Fails at April first for the "suddenly Hoes"-Feature of IC2
	 */
	public static boolean addScrapboxDrop(float aChance, ItemStack aOutput) {
		aOutput = GT_OreDictUnificator.get(aOutput);
		if (aOutput == null || aChance <= 0) return false;
		if (GregTech_API.sConfiguration.system && !aOutput.isItemEqual(new ItemStack(Item.hoeWood, 1, 0))) return false;
		if (!GregTech_API.sConfiguration.addAdvConfig("scrapboxdrops", aOutput, true)) return false;
		try {
			GT_Utility.callMethod(ic2.api.recipe.Recipes.scrapboxDrops, "addRecipe", false, true, false, aOutput.copy().splitStack(1), aChance);
			GT_Utility.callMethod(ic2.api.recipe.Recipes.scrapboxDrops, "addDrop", false, false, false, aOutput.copy().splitStack(1), aChance);
		} catch (Throwable e) {}
		return true;
	}
	
	/**
	 * Adds an Item to the Recycler Blacklist
	 */
	public static boolean addToRecyclerBlackList(ItemStack aRecycledStack) {
		if (aRecycledStack == null) return false;		
		try {
			ic2.api.recipe.Recipes.recyclerBlacklist.add(aRecycledStack.copy());
		} catch (Throwable e) {}
		return true;
	}
	
	/**
	 * Just simple Furnace smelting. Unbelievable how Minecraft fails at making a simple ItemStack->ItemStack mapping...
	 */
	public static boolean addSmeltingRecipe(ItemStack aInput, ItemStack aOutput) {
		aOutput = GT_OreDictUnificator.get(aOutput);
		if (aInput == null || aOutput == null) return false;
		if (aInput.getItem().hasContainerItem()) return false;
		if (!GregTech_API.sConfiguration.addAdvConfig("smelting", aInput, true)) return false;
		FurnaceRecipes.smelting().addSmelting(aInput.itemID, aInput.getItemDamage(), aOutput.copy(), 0.0F);
		return true;
	}
	
	/**
	 * Adds to Furnace AND Alloysmelter AND Induction Smelter
	 */
	public static boolean addSmeltingAndAlloySmeltingRecipe(ItemStack aInput, ItemStack aOutput) {
		if (aInput == null || aOutput == null) return false;
		boolean temp = false;
		if (addSmeltingRecipe(aInput, aOutput)) temp = true;
		if (GregTech_API.sRecipeAdder.addAlloySmelterRecipe(aInput, null, aOutput, 130, 3)) temp = true;
		if (addInductionSmelterRecipe(aInput, null, aOutput, null, aOutput.stackSize*100, 0)) temp = true;
		return temp;
	}
	
	/**
	 * Adds a Recipe to Forestrys Squeezer
	 */
	public static boolean addSqueezerRecipe(ItemStack aInput, FluidStack aOutput, int aTime) {
	    return false;
	}
	
	/**
	 * LiquidTransposer Recipe for both directions
	 */
	public static boolean addLiquidTransposerRecipe(ItemStack aEmptyContainer, FluidStack aLiquid, ItemStack aFullContainer, int aMJ) {
	    return false;
	}
	
	/**
	 * LiquidTransposer Recipe for filling Containers
	 */
	public static boolean addLiquidTransposerFillRecipe(ItemStack aEmptyContainer, FluidStack aLiquid, ItemStack aFullContainer, int aMJ) {
	    return false;
	}
	
	/**
	 * LiquidTransposer Recipe for emptying Containers
	 */
	public static boolean addLiquidTransposerEmptyRecipe(ItemStack aFullContainer, FluidStack aLiquid, ItemStack aEmptyContainer, int aMJ) {
	    return false;
	}
	
	/**
	 * IC2-Extractor Recipe. Overloads old Recipes automatically
	 */
	public static boolean addExtractionRecipe(ItemStack aInput, ItemStack aOutput) {
		aOutput = GT_OreDictUnificator.get(aOutput);
		if (aInput == null || aOutput == null) return false;
		GT_Utility.removeSimpleIC2MachineRecipe(aInput, getExtractorRecipeList(), null);
		if (!GregTech_API.sConfiguration.addAdvConfig("extractor", aInput, true)) return false;
		GT_Utility.addSimpleIC2MachineRecipe(aInput, getExtractorRecipeList(), aOutput);
		return true;
	}
	
	/**
	 * RC-BlastFurnace Recipes
	 */
	public static boolean addRCBlastFurnaceRecipe(ItemStack aInput, ItemStack aOutput, int aTime) {
	    return false;
	}
	
	private static Map<Integer, Object> sPulverizerRecipes = new HashMap<Integer, Object>();
	
	/**
	 * @return Object that can either be cast into IPulverizerRecipe or into GT_PulverizerRecipe
	 */
	public static Object getPulverizerRecipe(ItemStack aInput) {
		
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
	    return false;
	}
	
	/**
	 * Adds a Recipe to the Sawmills of GregTech and ThermalCraft
	 */
	public static boolean addSawmillRecipe(ItemStack aInput1, ItemStack aOutput1, ItemStack aOutput2) {
	    return false;
	}
	
	/**
	 * Induction Smelter Recipes and Alloy Smelter Recipes
	 */
	public static boolean addAlloySmelterRecipe(ItemStack aInput1, ItemStack aInput2, ItemStack aOutput1, int aDuration, int aEUt) {
		aOutput1 = GT_OreDictUnificator.get(aOutput1);
		if (aInput1 == null || aInput2 == null || aOutput1 == null) return false;
		boolean temp = false;
		if (GregTech_API.sRecipeAdder.addAlloySmelterRecipe(aInput1, aInput2, aOutput1, aDuration, aEUt)) temp = true;
		if (addInductionSmelterRecipe(aInput1, aInput2, aOutput1, null, aDuration * 2, 0)) temp = true;
		return temp;
	}
	
	/**
	 * Induction Smelter Recipes for TE
	 */
	public static boolean addInductionSmelterRecipe(ItemStack aInput1, ItemStack aInput2, ItemStack aOutput1, ItemStack aOutput2, int aEnergy, int aChance) {
	    return false;
	}
	
	/**
	 * Smelts dusts to Ingots
	 */
	public static boolean addDustToIngotSmeltingRecipe(ItemStack aInput, ItemStack aOutput) {
	    return false;
	}
	
	/**
	 * Smelts Ores to Ingots
	 */
	public static boolean addOreToIngotSmeltingRecipe(ItemStack aInput, ItemStack aOutput) {
	    return false;
	}
	
	private static Map<Object	, Object>	sExtractorRecipes	= new HashMap<Object, Object>();
	private static Map<Object	, Object>	sMaceratorRecipes	= new HashMap<Object, Object>();
	private static Map<Object	, Object>	sCompressorRecipes	= new HashMap<Object, Object>();
	private static Map<ItemStack, Integer>	sMassfabRecipes		= new HashMap<ItemStack, Integer>();
	private static Map<ItemStack, Float>	sScrapboxRecipes	= new HashMap<ItemStack, Float>();
	
	public static Map<Object, Object> getExtractorRecipeList() {
		try {
			return (Map<Object, Object>)GT_Utility.getField(ic2.api.recipe.Recipes.extractor.getClass(), "recipes").get(ic2.api.recipe.Recipes.extractor);
		} catch(Throwable e) {}
		return sExtractorRecipes;
	}
	
	public static Map<Object, Object> getCompressorRecipeList() {
		try {
			return (Map<Object, Object>)GT_Utility.getField(ic2.api.recipe.Recipes.compressor.getClass(), "recipes").get(ic2.api.recipe.Recipes.compressor);
		} catch(Throwable e) {}
		return sCompressorRecipes;
	}
	
	public static Map<Object, Object> getMaceratorRecipeList() {
		try {
			return (Map<Object, Object>)GT_Utility.getField(ic2.api.recipe.Recipes.macerator.getClass(), "recipes").get(ic2.api.recipe.Recipes.macerator);
		} catch(Throwable e) {}
		return sMaceratorRecipes;
	}
	
	public static Map<ItemStack, Integer> getMassFabricatorList() {
		try {
			return (Map<ItemStack, Integer>)GT_Utility.getField(ic2.api.recipe.Recipes.matterAmplifier.getClass(), "recipes").get(ic2.api.recipe.Recipes.matterAmplifier);
		} catch(Throwable e) {}
		return sMassfabRecipes;
	}
	
	public static Map<ItemStack, Float> getScrapboxList() {
		try {
			return (Map<ItemStack, Float>)GT_Utility.getField(ic2.api.recipe.Recipes.scrapboxDrops.getClass(), "recipes").get(ic2.api.recipe.Recipes.scrapboxDrops);
		} catch(Throwable e) {}
		return sScrapboxRecipes;
	}
	
	/**
	 * IC2-Compressor Recipe. Overloads old Recipes automatically
	 */
	public static boolean addCompressionRecipe(ItemStack aInput, ItemStack aOutput) {
		aOutput = GT_OreDictUnificator.get(aOutput);
		if (aInput == null || aOutput == null) return false;
		GT_Utility.removeSimpleIC2MachineRecipe(aInput, getCompressorRecipeList(), null);
		if (!GregTech_API.sConfiguration.addAdvConfig("compression", aInput, true)) return false;
		GT_Utility.addSimpleIC2MachineRecipe(aInput, getCompressorRecipeList(), aOutput);
		return true;
	}
	
	/**
	 * @param aValue Scrap = 5000, Scrapbox = 45000, Diamond Dust 125000
	 */
	public static boolean addIC2MatterAmplifier(ItemStack aAmplifier, int aValue) {
		if (aAmplifier == null || aValue <= 0) return false;
		if (!GregTech_API.sConfiguration.addAdvConfig("massfabamplifier", aAmplifier, true)) return false;
		try {
			GT_Utility.callMethod(ic2.api.recipe.Recipes.matterAmplifier, "addRecipe", false, true, false, aAmplifier, aValue);
			NBTTagCompound tNBT = new NBTTagCompound();
			tNBT.setInteger("amplification", aValue);
			GT_Utility.callMethod(ic2.api.recipe.Recipes.matterAmplifier, "addRecipe", false, false, false, aAmplifier, tNBT);
		} catch(Throwable e) {}
		return true;
	}
	
	/**
	 * Rolling Machine Crafting Recipe
	 */
	public static boolean addRollingMachineRecipe(ItemStack aResult, Object[] aRecipe) {
	    return false;
	}
	
	/**
	 * Special Handler for UUM Recipes
	 */
	public static boolean addUUMRecipe(ItemStack aResult, Object[] aRecipe) {
		if (aRecipe == null) return false;
		if (aResult != null && GregTech_API.sConfiguration.addAdvConfig("uumrecipe", aResult, true)) {
			return addCraftingRecipe(aResult, true, false, aRecipe);
		} else {
			return addCraftingRecipe(null, true, false, aRecipe);
		}
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
		return addCraftingRecipe(aResult, isElectricItem(aResult), false, aRecipe);
	}
	
	/**
	 * Regular Crafting Recipes. Deletes conflicting Recipes too.
	 */
	public static boolean addCraftingRecipe(ItemStack aResult, boolean aUseIC2Handler, boolean aMirrored, Object[] aRecipe) {
		aResult = GT_OreDictUnificator.get(aResult);
		if (aRecipe == null || aRecipe.length <= 0) return false;
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
	            Object val = null;
	            if (in instanceof ItemStack) {
	                itemMap.put(chr, ((ItemStack)in).copy());
	            } else if (in instanceof String) {
	            	ItemStack tStack = GT_OreDictUnificator.getFirstOre((String)in, 1);
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
		
		if (aUseIC2Handler) {
			try {
				ic2.api.recipe.Recipes.advRecipes.addRecipe(aResult.copy(), aRecipe);
				return true;
			} catch (Throwable e) {}
		}
		GameRegistry.addRecipe(new ShapedOreRecipe(aResult.copy(), aRecipe).setMirrored(aMirrored));
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
		aResult = GT_OreDictUnificator.get(aResult);
		if (aRecipe == null || aRecipe.length <= 0) return false;
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
					tRecipe[i] = GT_OreDictUnificator.getFirstOre((String)tObject, 1);
				} else if (tObject instanceof Boolean) {
					
				} else {
	                throw new IllegalArgumentException();
				}
				i++;
			}
	        removeRecipe(tRecipe);
		} catch(Throwable e) {e.printStackTrace(GT_Log.err);}
		
		if (aResult == null || aResult.stackSize <= 0) return false;
		
		if (aResult.getItemDamage() == GregTech_API.ITEM_WILDCARD_DAMAGE || aResult.getItemDamage() < 0) throw new IllegalArgumentException();
		
		if (aUseIC2Handler) {
			try {
				ic2.api.recipe.Recipes.advRecipes.addShapelessRecipe(aResult.copy(), aRecipe);
				return true;
			} catch (Throwable e) {}
		}
		GameRegistry.addRecipe(new ShapelessOreRecipe(aResult.copy(), aRecipe));
		return true;
	}
	
	/**
	 * Removes a Smelting Recipe
	 */
	public static boolean removeFurnaceSmelting(ItemStack aInput, ItemStack aOutput) {
		boolean temp = false;
		if (aInput != null) {
			FurnaceRecipes.smelting().getMetaSmeltingList().remove(Arrays.asList(new Integer[] { Integer.valueOf(aInput.itemID), Integer.valueOf(aInput.getItemDamage())}));
			FurnaceRecipes.smelting().getSmeltingList().remove(Integer.valueOf(aInput.itemID));
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
    public static ItemStack removeRecipe(ItemStack[] aRecipe) {
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
		InventoryCrafting aCrafting = new InventoryCrafting(new Container() {public boolean canInteractWith(EntityPlayer var1) {return false;}}, 3, 3);
		for (int i = 0; i < aRecipe.length && i < 9; i++) aCrafting.setInventorySlotContents(i, aRecipe[i]);
		ArrayList<IRecipe> tList = (ArrayList<IRecipe>)CraftingManager.getInstance().getRecipeList();
		for (int i = 0; i < tList.size(); i++) {
			try {
				if (tList.get(i).matches(aCrafting, GregTech_API.sDummyWorld)) {
					rReturn = tList.get(i).getRecipeOutput();
					tList.remove(i--);
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
    public static boolean removeRecipe(ItemStack aOutput) {
    	if (aOutput == null) return false;
    	boolean rReturn = false;
    	ItemStack tStack;
		ArrayList<IRecipe> tList = (ArrayList<IRecipe>)CraftingManager.getInstance().getRecipeList();
		for (int i = 0; i < tList.size(); i++) {
			if (GT_Utility.areStacksEqual(tStack = tList.get(i).getRecipeOutput(), aOutput)) {
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
    public static ItemStack getAllRecipeOutput(ItemStack[] aRecipe, World aWorld) {
    	if (aRecipe == null) return null;
    	boolean temp = false;
    	for (byte i = 0; i < aRecipe.length; i++) {
    		if (aRecipe[i] != null) {
    			temp = true;
        		break;
    		}
    	}
    	if (!temp) return null;
		InventoryCrafting aCrafting = new InventoryCrafting(new Container() {public boolean canInteractWith(EntityPlayer var1) {return false;}}, 3, 3);
		for (int i = 0; i < 9 && i < aRecipe.length; i++) aCrafting.setInventorySlotContents(i, aRecipe[i]);
		ItemStack rOutput = CraftingManager.getInstance().findMatchingRecipe(aCrafting, aWorld);
		return rOutput==null?null:rOutput.copy();
    }
    
    /**
     * Gives you a copy of the Output from a Crafting Recipe
     * Used for Recipe Detection.
     */
    public static ItemStack getRecipeOutput(ItemStack[] aRecipe) {
    	if (aRecipe == null) return null;
    	boolean temp = false;
    	for (byte i = 0; i < aRecipe.length; i++) {
    		if (aRecipe[i] != null) {
    			temp = true;
        		break;
    		}
    	}
    	if (!temp) return null;
		InventoryCrafting aCrafting = new InventoryCrafting(new Container() {public boolean canInteractWith(EntityPlayer var1) {return false;}}, 3, 3);
		for (int i = 0; i < 9 && i < aRecipe.length; i++) aCrafting.setInventorySlotContents(i, aRecipe[i]);
		ArrayList<IRecipe> tList = (ArrayList<IRecipe>)CraftingManager.getInstance().getRecipeList();
		for (int i = 0; i < tList.size(); i++) {temp = false;
			try {
				temp = tList.get(i).matches(aCrafting, GregTech_API.sDummyWorld);
			} catch(Throwable e) {e.printStackTrace(GT_Log.err);}
			if (temp) {
				ItemStack tOutput = tList.get(i).getRecipeOutput();
				if (tOutput == null || tOutput.stackSize <= 0) {
					// Seriously, who would ever do that shit?
					if (!GregTech_API.sPostloadFinished) throw new GT_ItsNotMyFaultException("Seems another Mod added a Crafting Recipe with null Output. Tell the Developer of said Mod to fix that.");
				} else {
					return tOutput.copy();
				}
			}
		}
		return null;
    }
    
    /**
     * Gives you a list of the Outputs from a Crafting Recipe
     * If you have multiple Mods, which add Bronze Armor for example
     */
    public static ArrayList<ItemStack> getRecipeOutputs(ItemStack[] aRecipe) {
    	if (aRecipe == null) return null;
    	boolean temp = false;
    	for (byte i = 0; i < aRecipe.length; i++) {
    		if (aRecipe[i] != null) {
    			temp = true;
        		break;
    		}
    	}
    	if (!temp) return null;
    	ArrayList<ItemStack> rList = new ArrayList<ItemStack>();
    	InventoryCrafting aCrafting = new InventoryCrafting(new Container() {public boolean canInteractWith(EntityPlayer var1) {return false;}}, 3, 3);
		for (int i = 0; i < 9 && i < aRecipe.length; i++) aCrafting.setInventorySlotContents(i, aRecipe[i]);
		ArrayList<IRecipe> tList = (ArrayList<IRecipe>)CraftingManager.getInstance().getRecipeList();
		for (int i = 0; i < tList.size(); i++) {
			temp = false;
			try {
				temp = tList.get(i).matches(aCrafting, GregTech_API.sDummyWorld);
			} catch(Throwable e) {e.printStackTrace(GT_Log.err);}
			if (temp) {
				ItemStack tOutput = tList.get(i).getRecipeOutput();
				if (tOutput == null || tOutput.stackSize <= 0) {
					// Seriously, who would ever do that shit?
					if (!GregTech_API.sPostloadFinished) throw new GT_ItsNotMyFaultException("Seems another Mod added a Crafting Recipe with null Output. Tell the Developer of said Mod to fix that.");
				} else {
					rList.add(tOutput.copy());
				}
			}
		}
		return rList;
    }
    
    /**
     * Used in my own Macerator. Decreases StackSize of the Input if wanted.
     */
    public static ItemStack getMaceratorOutput(ItemStack aInput, boolean aRemoveInput, ItemStack aOutputSlot) {
    	ItemStack rStack = getMachineOutput(aInput, getMaceratorRecipeList(), aRemoveInput, aOutputSlot)[0];
    	if (rStack != null) rStack = rStack.copy();
    	return rStack;
    }
    
    /**
     * Used in my own Extractor. Decreases StackSize of the Input if wanted.
     */
    public static ItemStack getExtractorOutput(ItemStack aInput, boolean aRemoveInput, ItemStack aOutputSlot) {
    	ItemStack rStack = getMachineOutput(aInput, getExtractorRecipeList(), aRemoveInput, aOutputSlot)[0];
    	if (rStack != null) rStack = rStack.copy();
    	return rStack;
    }
    
    /**
     * Used in my own Compressor. Decreases StackSize of the Input if wanted.
     */
    public static ItemStack getCompressorOutput(ItemStack aInput, boolean aRemoveInput, ItemStack aOutputSlot) {
    	ItemStack rStack = getMachineOutput(aInput, getCompressorRecipeList(), aRemoveInput, aOutputSlot)[0];
    	if (rStack != null) rStack = rStack.copy();
    	return rStack;
    }
    
    /**
     * Used in my own Furnace.
     */
    public static ItemStack getSmeltingOutput(ItemStack aInput, boolean aRemoveInput, ItemStack aOutputSlot) {
    	if (aInput == null) return null;
    	ItemStack rStack = FurnaceRecipes.smelting().getSmeltingResult(aInput);
    	if (aOutputSlot == null || (GT_Utility.areStacksEqual(rStack, aOutputSlot) && rStack.stackSize + aOutputSlot.stackSize <= aOutputSlot.getMaxStackSize())) {
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
    public static ItemStack[] getMachineOutput(ItemStack aInput, Map<Object, Object> aRecipeList, boolean aRemoveInput, ItemStack... aOutputSlots) {
    	if (aOutputSlots == null) return new ItemStack[0];
    	if (aInput == null) return new ItemStack[aOutputSlots.length];
    	try {
			for (Entry<Object, Object> tEntry : aRecipeList.entrySet()) {
				if (tEntry.getValue() != null) {
					if (tEntry.getKey() instanceof ItemStack) {
						if (GT_Utility.areStacksEqual((ItemStack)tEntry.getKey(), aInput) && aInput.stackSize >= ((ItemStack)tEntry.getKey()).stackSize) {
							if (aInput.stackSize >= ((ItemStack)tEntry.getKey()).stackSize) {
								if (tEntry.getValue() instanceof ItemStack) {
									if (aOutputSlots[0] == null || (GT_Utility.areStacksEqual(((ItemStack)tEntry.getKey()), aOutputSlots[0]) && ((ItemStack)tEntry.getKey()).stackSize + aOutputSlots[0].stackSize <= aOutputSlots[0].getMaxStackSize())) {
										if (aRemoveInput) aInput.stackSize-=((ItemStack)tEntry.getKey()).stackSize;
										return new ItemStack[] {((ItemStack)tEntry.getValue()).copy()};
									}
									break;
								}
								
								ItemStack[] tList = (ItemStack[])((List<ItemStack>)GT_Utility.getField(tEntry.getValue(), "items", false, false)).toArray();
								if (tList.length == 0) break;
								ItemStack[] rList = new ItemStack[aOutputSlots.length];
								
								for (byte i = 0; i < aOutputSlots.length; i++) {
									if (tList[i] != null) {
										if (aOutputSlots[i] == null || (GT_Utility.areStacksEqual(tList[i], aOutputSlots[i]) && tList[i].stackSize + aOutputSlots[i].stackSize <= aOutputSlots[i].getMaxStackSize())) {
											rList[i] = tList[i].copy();
										} else {
									    	return new ItemStack[aOutputSlots.length];
										}
									}
								}
								
								if (aRemoveInput) aInput.stackSize-=((ItemStack)tEntry.getKey()).stackSize;
								return rList;
							}
							break;
						}
					} else {
						Object temp = GT_Utility.callMethod(tEntry.getKey(), "matches", false, false, false, aInput);
						if (temp instanceof Boolean && (Boolean)temp) {
							temp = GT_Utility.callMethod(tEntry.getKey(), "getAmount", false, false, false);
							if (temp instanceof Integer && (Integer)temp <= aInput.stackSize) {
								ItemStack[] tList = (ItemStack[])((List<ItemStack>)GT_Utility.getField(tEntry.getValue(), "items", false, false)).toArray();
								if (tList.length == 0) break;
								ItemStack[] rList = new ItemStack[aOutputSlots.length];
								
								for (byte i = 0; i < aOutputSlots.length; i++) {
									if (tList[i] != null) {
										if (aOutputSlots[i] == null || (GT_Utility.areStacksEqual(tList[i], aOutputSlots[i]) && tList[i].stackSize + aOutputSlots[i].stackSize <= aOutputSlots[i].getMaxStackSize())) {
											rList[i] = tList[i].copy();
										} else {
									    	return new ItemStack[aOutputSlots.length];
										}
									}
								}
								
								if (aRemoveInput) aInput.stackSize-=(Integer)temp;
								return rList;
							}
							break;
						}
					}
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
		} catch (Throwable e) {}
    	return getIC2Item("scrap", 1);
    }
    
    /**
     * For the Scrapboxinator
     */
	public static ItemStack getRandomScrapboxDrop(World aWorld) {
		ItemStack rStack = null;
		try {
			rStack = GT_OreDictUnificator.get((ItemStack)Class.forName("ic2.core.item.ItemScrapbox").getMethod("getDrop", World.class).invoke(null, aWorld));
		} catch (Throwable e) {}
		if (rStack == null) return getRandomScrapboxDrop(aWorld);
		return rStack;
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
		} catch(Throwable e) {}
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
		} catch(Throwable e) {}
		return false;
	}
	
    /**
     * Emits Energy to E-net. Prefers the internals of GT TileEntities for emitting Energy
     * @return the remaining Energy.
     */
	public static int emitEnergyToEnet(int aAmount, World aWorld, TileEntity aTileEntity) {
		return 0;
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
		} catch (Throwable e) {}
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
		} catch (Throwable e) {}
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
		} catch (Throwable e) {}
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
		} catch (Throwable e) {}
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
		} catch (Throwable e) {}
		return false;
	}
	
	/**
	 * Uses an Item. Tries to discharge in case of Electric Items
	 */
	public static boolean damageOrDechargeItem(ItemStack aStack, int aDamage, int aDecharge, EntityLivingBase aPlayer) {
		if (aStack == null) return false;
		if (aPlayer != null && aPlayer instanceof EntityPlayer && ((EntityPlayer)aPlayer).capabilities.isCreativeMode) return true;
		if (GT_ModHandler.isElectricItem(aStack)) {
			if (canUseElectricItem(aStack, aDecharge)) {
				if (aPlayer != null && aPlayer instanceof EntityPlayer) {
					return GT_ModHandler.useElectricItem(aStack, aDecharge, (EntityPlayer)aPlayer);
				} else {
					return GT_ModHandler.dischargeElectricItem(aStack, aDecharge, Integer.MAX_VALUE, true, false, true) >= aDecharge;
				}
			}
		} else if (aStack.getItem().isDamageable()) {
			if (aPlayer == null) {
				aStack.setItemDamage(aStack.getItemDamage() + aDamage);
			} else {
				aStack.damageItem(aDamage, aPlayer);
			}
			if (aStack.getItemDamage() >= aStack.getMaxDamage()) {
				aStack.setItemDamage(aStack.getMaxDamage()+1);
				if (aStack.getItem().hasContainerItem()) {
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
		if (aPlayer instanceof EntityPlayer) {
			EntityPlayer tPlayer = (EntityPlayer)aPlayer;
			if (tPlayer.capabilities.isCreativeMode) return true;
			if (GT_Utility.isItemStackInList(aStack, GregTech_API.sSolderingToolList)) for (int i = 0; i < 36; i++) {
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
		} catch (Throwable e) {}
		return false;
	}
	
	/**
	 * Is this an electric Item?
	 */
	public static boolean isElectricItem(ItemStack aStack) {
		try {
			return aStack != null && aStack.getItem() instanceof ic2.api.item.IElectricItem;
		} catch (Throwable e) {}
		return false;
	}
	
	public static boolean acceptsGT(TileEntity aTileEntity) {
		try {
			return aTileEntity instanceof gregtechmod.api.interfaces.IBasicEnergyContainer;
		} catch (Throwable e) {}
		return false;
	}
	
	public static boolean acceptsEU(TileEntity aTileEntity) {
		try {
			return aTileEntity instanceof ic2.api.energy.tile.IEnergyAcceptor;
		} catch (Throwable e) {}
		return false;
	}
	
	public static boolean acceptsMJ(TileEntity aTileEntity) {
		try {
			return aTileEntity instanceof buildcraft.api.power.IPowerReceptor;
		} catch (Throwable e) {}
		return false;
	}
	
	public static boolean acceptsUE(TileEntity aTileEntity) {
		try {
			return aTileEntity instanceof universalelectricity.core.block.IElectricalStorage;
		} catch (Throwable e) {}
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
			} catch(Throwable e) {}
		}
	}
	
	public static int getCapsuleCellContainerCountMultipliedWithStackSize(ItemStack aStack) {
		if (aStack == null) return 0;
		return getCapsuleCellContainerCount(aStack)*aStack.stackSize;
	}
	
	public static int getCapsuleCellContainerCount(ItemStack aStack) {
		if (aStack == null) return 0;
		if (GT_Utility.areStacksEqual(aStack, getEmptyCell(1))) return 1;
		Item tItem = aStack.getItem();
		if (tItem == null) return 0;
		ItemStack tStack = null;
		if (tItem.hasContainerItem() && null != (tStack = tItem.getContainerItemStack(aStack)) && tStack.isItemEqual(getEmptyCell(1))) return tStack.stackSize;
		if (tItem instanceof ICapsuleCellContainer) return ((ICapsuleCellContainer)tItem).CapsuleCellContainerCount(aStack);
		tStack = GT_Utility.getContainerForFilledItem(aStack);
		if (tStack != null && tStack.isItemEqual(getEmptyCell(1))) return 1;
		
		if (GT_Utility.areStacksEqual(new ItemStack(tItem, 1, GregTech_API.ITEM_WILDCARD_DAMAGE), getIC2Item("cell"						, 1))) return 1;
		if (GT_Utility.areStacksEqual(new ItemStack(tItem, 1, GregTech_API.ITEM_WILDCARD_DAMAGE), getIC2Item("airCell"					, 1))) return 1;
		if (GT_Utility.areStacksEqual(new ItemStack(tItem, 1, GregTech_API.ITEM_WILDCARD_DAMAGE), getIC2Item("waterCell"				, 1))) return 1;
		if (GT_Utility.areStacksEqual(new ItemStack(tItem, 1, GregTech_API.ITEM_WILDCARD_DAMAGE), getIC2Item("lavaCell"					, 1))) return 1;
		if (GT_Utility.areStacksEqual(new ItemStack(tItem, 1, GregTech_API.ITEM_WILDCARD_DAMAGE), getIC2Item("hydratedCoalCell"			, 1))) return 1;
		if (GT_Utility.areStacksEqual(new ItemStack(tItem, 1, GregTech_API.ITEM_WILDCARD_DAMAGE), getIC2Item("coalfuelCell"				, 1))) return 1;
		if (GT_Utility.areStacksEqual(new ItemStack(tItem, 1, GregTech_API.ITEM_WILDCARD_DAMAGE), getIC2Item("bioCell"					, 1))) return 1;
		if (GT_Utility.areStacksEqual(new ItemStack(tItem, 1, GregTech_API.ITEM_WILDCARD_DAMAGE), getIC2Item("biofuelCell"				, 1))) return 1;
		if (GT_Utility.areStacksEqual(new ItemStack(tItem, 1, GregTech_API.ITEM_WILDCARD_DAMAGE), getIC2Item("electrolyzedWaterCell"	, 1))) return 1;
		if (GT_Utility.areStacksEqual(new ItemStack(tItem, 1, GregTech_API.ITEM_WILDCARD_DAMAGE), getIC2Item("nearDepletedUraniumCell"	, 1))) return 1;
		if (GT_Utility.areStacksEqual(new ItemStack(tItem, 1, GregTech_API.ITEM_WILDCARD_DAMAGE), getIC2Item("reactorIsotopeCell"		, 1))) return 1;
		if (GT_Utility.areStacksEqual(new ItemStack(tItem, 1, GregTech_API.ITEM_WILDCARD_DAMAGE), getIC2Item("reEnrichedUraniumCell"	, 1))) return 1;
		if (GT_Utility.areStacksEqual(new ItemStack(tItem, 1, GregTech_API.ITEM_WILDCARD_DAMAGE), getIC2Item("hydratingCell"			, 1))) return 1;
		if (GT_Utility.areStacksEqual(new ItemStack(tItem, 1, GregTech_API.ITEM_WILDCARD_DAMAGE), getIC2Item("reactorCoolantSimple"		, 1))) return 1;
		if (GT_Utility.areStacksEqual(new ItemStack(tItem, 1, GregTech_API.ITEM_WILDCARD_DAMAGE), getIC2Item("reactorCoolantTriple"		, 1))) return 3;
		if (GT_Utility.areStacksEqual(new ItemStack(tItem, 1, GregTech_API.ITEM_WILDCARD_DAMAGE), getIC2Item("reactorCoolantSix"		, 1))) return 6;
		if (GT_Utility.areStacksEqual(new ItemStack(tItem, 1, GregTech_API.ITEM_WILDCARD_DAMAGE), getIC2Item("reactorUraniumSimple"		, 1))) return 1;
		if (GT_Utility.areStacksEqual(new ItemStack(tItem, 1, GregTech_API.ITEM_WILDCARD_DAMAGE), getIC2Item("reactorUraniumDual"		, 1))) return 2;
		if (GT_Utility.areStacksEqual(new ItemStack(tItem, 1, GregTech_API.ITEM_WILDCARD_DAMAGE), getIC2Item("reactorUraniumQuad"		, 1))) return 4;
		return 0;
	}
}