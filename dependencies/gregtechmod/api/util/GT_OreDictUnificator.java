package gregtechmod.api.util;

import gregtechmod.api.GregTech_API;

import java.util.ArrayList;
import java.util.HashMap;

import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

/**
 * NEVER INCLUDE THIS FILE IN YOUR MOD!!!
 * 
 * This is the Core of my OreDict Unification Code
 * 
 * If you just want to use this to unificate your Items, then use the Function in the GregTech_API File
 * 
 * P.S. It is intended to be named "Unificator" and not "Unifier", because that sounds more awesome.
 */
public class GT_OreDictUnificator {
	public static volatile int VERSION = 402;
	
	private static final HashMap<String, ItemStack> sName2OreMap = new HashMap<String, ItemStack>();
	private static final HashMap<Integer, String> sItemhash2NameMap = new HashMap<Integer, String>(), sItemhash2MaterialMap = new HashMap<Integer, String>();
	private static final ArrayList<Integer> sBlackList = new ArrayList<Integer>();
	
	/**
	 * The Blacklist just prevents the Item from being Unificated into something else.
	 * Useful if you have things like the Industrial Diamond, which is better than regular Diamond, but also placeable in absolutely all Diamond Recipes.
	 */
	public static void addToBlacklist(ItemStack aStack) {
		sBlackList.add(GT_Utility.stackToInt(aStack));
	}
	
	public static void add(String aOreDictName, ItemStack aOreStack) {
		set(aOreDictName, aOreStack, false);
	}
	
	public static void set(String aOreDictName, ItemStack aOreStack) {
		set(aOreDictName, aOreStack, true);
	}
	
	public static void set(String aOreDictName, ItemStack aOreStack, boolean aOverwrite) {
		if (aOreDictName == null || aOreDictName.equals("") || aOreDictName.startsWith("itemDust") || aOreStack == null || aOreStack.getItemDamage() < 0) return;
		aOreStack = aOreStack.copy().splitStack(1);
		addAssociation(aOreDictName, aOreStack);
		if (sName2OreMap.get(aOreDictName) == null) {
			sName2OreMap.put(aOreDictName, aOreStack);
			registerOre(aOreDictName, aOreStack);
		} else {
			if (aOverwrite && GregTech_API.sConfiguration.addAdvConfig("specialunificationtargets", aOreStack, true)) {
				sName2OreMap.remove(aOreDictName);
				sName2OreMap.put(aOreDictName, aOreStack);
			}
			registerOre(aOreDictName, aOreStack);
		}
	}
	
	public static void override(String aOreDictName, ItemStack aOreStack) {
		if (aOreDictName == null || aOreDictName.equals("") || aOreDictName.startsWith("itemDust") || aOreStack == null || aOreStack.getItemDamage() < 0) return;
		if (aOreStack.getDisplayName() == null || aOreStack.getDisplayName().equals("") || GregTech_API.sConfiguration.addAdvConfig("specialunificationtargets", aOreStack, true)) set(aOreDictName, aOreStack);
	}
	
	public static ItemStack getFirstOre(String aOreDictName, int aAmount) {
		if (aOreDictName == null || aOreDictName.equals("")) return null;
		if (sName2OreMap.containsKey(aOreDictName)) return get(aOreDictName, null, aAmount);
		ItemStack rStack = null;
		ArrayList<ItemStack> tList = getOres(aOreDictName);
		if (tList.size() > 0) rStack = tList.get(0).copy();
		if (rStack != null) rStack.stackSize = aAmount;
		return rStack;
	}
	
	public static ItemStack getFirstCapsulatedOre(String aOreDictName, int aAmount) {
		if (sName2OreMap.containsKey(aOreDictName)) return get(aOreDictName, null, aAmount);
		ItemStack rStack = null;
		ArrayList<ItemStack> tList = getOres(aOreDictName);
		for (ItemStack tStack : tList) {
			if (tStack != null) {
				if (GT_ModHandler.getCapsuleCellContainerCount(tStack) == 1) {
					rStack = tStack.copy().splitStack(aAmount);
					break;
				}
			}
		}
		return rStack;
	}
	
	public static ItemStack getFirstUnCapsulatedOre(String aOreDictName, int aAmount) {
		if (sName2OreMap.containsKey(aOreDictName)) return get(aOreDictName, null, aAmount);
		ItemStack rStack = null;
		ArrayList<ItemStack> tList = getOres(aOreDictName);
		for (ItemStack tStack : tList) {
			if (tStack != null) {
				if (GT_ModHandler.getCapsuleCellContainerCount(tStack) <= 0) {
					rStack = tStack.copy().splitStack(aAmount);
					break;
				}
			}
		}
		return rStack;
	}
	
	public static ItemStack get(String aOreDictName, int aAmount) {
		return get(aOreDictName, null, aAmount);
	}
	
	public static ItemStack get(String aOreDictName, ItemStack aReplacement, int aAmount) {
		ItemStack rStack = sName2OreMap.get(aOreDictName);
		if (!sName2OreMap.containsKey(aOreDictName) && aReplacement == null) GT_Log.err.println("Unknown Key for Unification, Typo? " + aOreDictName);
		if (rStack == null)
			rStack = aReplacement==null?null:aReplacement.copy();
		else {
			rStack = rStack.copy();
			rStack.stackSize = aAmount;
		}
		return rStack;
	}
	
	public static ItemStack get(ItemStack aOreStack) {
		if (aOreStack == null || GT_Utility.isItemStackInList(aOreStack, sBlackList)) return aOreStack;
		String tName = sItemhash2NameMap.get(GT_Utility.stackToInt(aOreStack));
		ItemStack rStack = null;
		if (tName != null) rStack = sName2OreMap.get(tName);
		if (rStack == null) rStack = aOreStack.copy(); else rStack = rStack.copy();
		if (rStack != null) rStack.stackSize = aOreStack.stackSize;
		return rStack;
	}
	
	public static void addAssociation(String aOreDictName, ItemStack aOreStack) {
		if (aOreDictName == null || aOreDictName.equals("") || aOreStack == null) return;
		
		if (aOreStack.getItemDamage() == GregTech_API.ITEM_WILDCARD_DAMAGE) {
			aOreStack = aOreStack.copy();
			for (byte i = 0; i < 16; i++) {
				aOreStack.setItemDamage(i);
				sItemhash2NameMap.put(GT_Utility.stackToInt(aOreStack), aOreDictName);
			}
		}
		
		sItemhash2NameMap.put(GT_Utility.stackToInt(aOreStack), aOreDictName);
	}
	
	public static String getAssociation(ItemStack aOreStack) {
		return sItemhash2NameMap.get(GT_Utility.stackToInt(aOreStack));
	}
	
	public static void registerMaterial(String aMaterialName, ItemStack aOreStack) {
		if (aMaterialName == null || aMaterialName.equals("") || aOreStack == null) return;
		
		if (aOreStack.getItemDamage() == GregTech_API.ITEM_WILDCARD_DAMAGE) {
			aOreStack = aOreStack.copy();
			for (byte i = 0; i < 16; i++) {
				aOreStack.setItemDamage(i);
				sItemhash2MaterialMap.put(GT_Utility.stackToInt(aOreStack), aMaterialName);
			}
		}
		
		sItemhash2MaterialMap.put(GT_Utility.stackToInt(aOreStack), aMaterialName);
	}
	
	public static String getMaterial(ItemStack aOreStack) {
		return sItemhash2MaterialMap.get(GT_Utility.stackToInt(aOreStack));
	}
	
	public static boolean isItemStackInstanceOf(ItemStack aOreStack, String aOreName, boolean aPrefix) {
		if (aOreStack == null || aOreName == null || aOreName.equals("")) return false;
		String tString = sItemhash2NameMap.get(GT_Utility.stackToInt(aOreStack));
		if (tString == null) {
			ItemStack tOreStack = aOreStack.copy();
			tOreStack.setItemDamage(GregTech_API.ITEM_WILDCARD_DAMAGE);
			tString = sItemhash2NameMap.get(GT_Utility.stackToInt(tOreStack));
			if (tString == null) {
				if (!aPrefix)
					for (ItemStack tOreStacks : getOres(aOreName))
						if (GT_Utility.areStacksEqual(tOreStacks, aOreStack))
							return true;
				return false;
			}
		}
		return aPrefix?tString.startsWith(aOreName):tString.equals(aOreName);
	}
	
	public static boolean isItemStackDye(ItemStack aStack) {
		if (aStack == null) return false;
		for (String tDye : GT_Utility.sDyeToMetaMapping.keySet()) if (isItemStackInstanceOf(aStack, tDye, false)) return true;
		return false;
	}
	
    public static boolean registerOre(String aName, ItemStack aStack) {
    	if (aName == null || aName.equals("") || aStack == null || aStack.getItem() == null) return false;
    	ArrayList<ItemStack> tList = getOres(aName);
    	for (int i = 0; i < tList.size(); i++) if (GT_Utility.areStacksEqual(tList.get(i), aStack)) return false;
    	aStack = aStack.copy().splitStack(1);
    	OreDictionary.registerOre(aName, aStack);
    	return true;
    }
    
    public static ArrayList<ItemStack> getOres(String aOreName) {
    	ArrayList<ItemStack> rList = new ArrayList<ItemStack>();
    	rList.addAll(OreDictionary.getOres(aOreName));
    	return rList;
    }
}
