package gregtechmod.api.util;

import gregtechmod.api.GregTech_API;
import gregtechmod.api.enums.GT_Items;
import gregtechmod.api.enums.Materials;

import java.util.*;
import java.util.Map.Entry;

import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;

/**
 * NEVER INCLUDE THIS FILE IN YOUR MOD!!!
 * 
 * This File contains the functions used for Recipes. Please do not include this File AT ALL in your Moddownload as it ruins compatibility
 * This is just the Core of my Recipe System, if you just want to GET the Recipes I add, then you can access this File.
 * Do NOT add Recipes using the Constructors inside this Class, The GregTech_API File calls the correct Functions for these Constructors.
 * 
 * I know this File causes some Errors, because of missing Main Functions, but if you just need to compile Stuff, then remove said erroreous Functions.
 */
public class GT_Recipe {
	public static volatile int VERSION = 407;
	
	/**
	 * If you want to remove Recipes, then set the Index to null, instead of removing the complete Entry!
	 * That's because I have a mapping for quick access, so you should also remove the Mapping of the Recipe.
	 * 
	 * However, every single one of these Recipes has a Config, so you could just disable the Config Setting.
	 */
	public static Map<Long, GT_Recipe> pFusionRecipes			= new HashMap<Long, GT_Recipe>();
	public static Map<Long, GT_Recipe> pCentrifugeRecipes		= new HashMap<Long, GT_Recipe>();
	public static Map<Long, GT_Recipe> pElectrolyzerRecipes		= new HashMap<Long, GT_Recipe>();
	public static Map<Long, GT_Recipe> pGrinderRecipes			= new HashMap<Long, GT_Recipe>();
	public static Map<Long, GT_Recipe> pBlastRecipes			= new HashMap<Long, GT_Recipe>();
	public static Map<Long, GT_Recipe> pImplosionRecipes		= new HashMap<Long, GT_Recipe>();
	public static Map<Long, GT_Recipe> pSawmillRecipes			= new HashMap<Long, GT_Recipe>();
	public static Map<Long, GT_Recipe> pVacuumRecipes			= new HashMap<Long, GT_Recipe>();
	public static Map<Long, GT_Recipe> pChemicalRecipes			= new HashMap<Long, GT_Recipe>();
	public static Map<Long, GT_Recipe> pDistillationRecipes		= new HashMap<Long, GT_Recipe>();
	public static Map<Long, GT_Recipe> pWiremillRecipes			= new HashMap<Long, GT_Recipe>();
	public static Map<Long, GT_Recipe> pBenderRecipes			= new HashMap<Long, GT_Recipe>();
	public static Map<Long, GT_Recipe> pAlloySmelterRecipes		= new HashMap<Long, GT_Recipe>();
	public static Map<Long, GT_Recipe> pAssemblerRecipes		= new HashMap<Long, GT_Recipe>();
	public static Map<Long, GT_Recipe> pCannerRecipes			= new HashMap<Long, GT_Recipe>();
	public static Map<Long, GT_Recipe> pCNCRecipes				= new HashMap<Long, GT_Recipe>();
	public static Map<Long, GT_Recipe> pLatheRecipes			= new HashMap<Long, GT_Recipe>();
	public static Map<Long, GT_Recipe> pCutterRecipes			= new HashMap<Long, GT_Recipe>();
	public static Map<Long, GT_Recipe> pExtruderRecipes			= new HashMap<Long, GT_Recipe>();
	
	public static Map<Long, GT_Recipe> pDieselFuels				= new HashMap<Long, GT_Recipe>();
	public static Map<Long, GT_Recipe> pTurbineFuels			= new HashMap<Long, GT_Recipe>();
	public static Map<Long, GT_Recipe> pHotFuels				= new HashMap<Long, GT_Recipe>();
	public static Map<Long, GT_Recipe> pDenseLiquidFuels		= new HashMap<Long, GT_Recipe>();
	public static Map<Long, GT_Recipe> pPlasmaFuels				= new HashMap<Long, GT_Recipe>();
	public static Map<Long, GT_Recipe> pMagicFuels				= new HashMap<Long, GT_Recipe>();
	
	@Deprecated public static ArrayList<GT_Recipe> sDieselFuels			= new ArrayList<GT_Recipe>();
	@Deprecated public static ArrayList<GT_Recipe> sTurbineFuels		= new ArrayList<GT_Recipe>();
	@Deprecated public static ArrayList<GT_Recipe> sHotFuels			= new ArrayList<GT_Recipe>();
	@Deprecated public static ArrayList<GT_Recipe> sDenseLiquidFuels	= new ArrayList<GT_Recipe>();
	@Deprecated public static ArrayList<GT_Recipe> sPlasmaFuels			= new ArrayList<GT_Recipe>();
	@Deprecated public static ArrayList<GT_Recipe> sMagicFuels			= new ArrayList<GT_Recipe>();
	
	public static List<Map<Long, GT_Recipe>> mRecipeMaps = new ArrayList<Map<Long, GT_Recipe>>();
	
	static {
    	mRecipeMaps.add(pFusionRecipes);
    	mRecipeMaps.add(pCentrifugeRecipes);
    	mRecipeMaps.add(pElectrolyzerRecipes);
    	mRecipeMaps.add(pGrinderRecipes);
    	mRecipeMaps.add(pBlastRecipes);
    	mRecipeMaps.add(pImplosionRecipes);
    	mRecipeMaps.add(pSawmillRecipes);
    	mRecipeMaps.add(pVacuumRecipes);
    	mRecipeMaps.add(pChemicalRecipes);
    	mRecipeMaps.add(pDistillationRecipes);
    	mRecipeMaps.add(pWiremillRecipes);
    	mRecipeMaps.add(pBenderRecipes);
    	mRecipeMaps.add(pAlloySmelterRecipes);
    	mRecipeMaps.add(pAssemblerRecipes);
    	mRecipeMaps.add(pCannerRecipes);
    	mRecipeMaps.add(pCNCRecipes);
    	mRecipeMaps.add(pLatheRecipes);
    	mRecipeMaps.add(pCutterRecipes);
    	mRecipeMaps.add(pExtruderRecipes);
    	
    	mRecipeMaps.add(pDieselFuels);
    	mRecipeMaps.add(pTurbineFuels);
    	mRecipeMaps.add(pHotFuels);
    	mRecipeMaps.add(pDenseLiquidFuels);
    	mRecipeMaps.add(pPlasmaFuels);
    	mRecipeMaps.add(pMagicFuels);
	}
	
	public static void reinit() {
        GT_Log.out.println("GT_Mod: Re-Initializing Item Hashcodes for quick Recipe access.");
        for (Map<Long, GT_Recipe> tMap : mRecipeMaps) {
        	Map<Long, GT_Recipe> tNewMap = new HashMap<Long, GT_Recipe>();
        	for (Entry<Long, GT_Recipe> tEntry : tMap.entrySet()) {
        		GT_OreDictUnificator.setStack(tEntry.getValue().mInput1);
        		GT_OreDictUnificator.setStack(tEntry.getValue().mInput2);
        		GT_OreDictUnificator.setStack(tEntry.getValue().mOutput1);
        		GT_OreDictUnificator.setStack(tEntry.getValue().mOutput2);
        		GT_OreDictUnificator.setStack(tEntry.getValue().mOutput3);
        		GT_OreDictUnificator.setStack(tEntry.getValue().mOutput4);
        		tNewMap.put(GT_Utility.stacksToLong(tEntry.getValue().mInput1, tEntry.getValue().mInput2), tEntry.getValue());
        	}
        	tMap.clear();
        	tMap.putAll(tNewMap);
        }
	}
	
	public final ItemStack mInput1, mInput2, mOutput1, mOutput2, mOutput3, mOutput4;
	public final int mDuration, mEUt, mStartEU;
	
	public ItemStack getRepresentativeInput1() {
		return mInput1;
	}
	
	public ItemStack getRepresentativeInput2() {
		return mInput2;
	}
	
	@SuppressWarnings("null") // And again too stupid to get that it cannot be null at that point.
	private GT_Recipe(ItemStack aInput1, ItemStack aInput2, ItemStack aOutput1, ItemStack aOutput2, ItemStack aOutput3, ItemStack aOutput4, int aDuration, int aEUt, int aStartEU) {
		aInput1  = GT_OreDictUnificator.get(true, aInput1);
		aInput2  = GT_OreDictUnificator.get(true, aInput2);
		aOutput1 = GT_OreDictUnificator.get(true, aOutput1);
		aOutput2 = GT_OreDictUnificator.get(true, aOutput2);
		aOutput3 = GT_OreDictUnificator.get(true, aOutput3);
		aOutput4 = GT_OreDictUnificator.get(true, aOutput4);
		
		if (aInput1 != null) {
			if (GT_Utility.areStacksEqual(aInput1, aOutput1)) {
				if (aInput1.stackSize >= aOutput1.stackSize) {
					aInput1.stackSize -= aOutput1.stackSize;
					aOutput1 = null;
				} else {
					aOutput1.stackSize -= aInput1.stackSize;
				}
			}
			if (GT_Utility.areStacksEqual(aInput1, aOutput2)) {
				if (aInput1.stackSize >= aOutput2.stackSize) {
					aInput1.stackSize -= aOutput2.stackSize;
					aOutput2 = null;
				} else {
					aOutput2.stackSize -= aInput1.stackSize;
				}
			}
			if (GT_Utility.areStacksEqual(aInput1, aOutput3)) {
				if (aInput1.stackSize >= aOutput3.stackSize) {
					aInput1.stackSize -= aOutput3.stackSize;
					aOutput3 = null;
				} else {
					aOutput3.stackSize -= aInput1.stackSize;
				}
			}
			if (GT_Utility.areStacksEqual(aInput1, aOutput4)) {
				if (aInput1.stackSize >= aOutput4.stackSize) {
					aInput1.stackSize -= aOutput4.stackSize;
					aOutput4 = null;
				} else {
					aOutput4.stackSize -= aInput1.stackSize;
				}
			}
		}
		
		if (aInput2 != null) {
			if (GT_Utility.areStacksEqual(aInput2, aOutput1)) {
				if (aInput2.stackSize >= aOutput1.stackSize) {
					aInput2.stackSize -= aOutput1.stackSize;
					aOutput1 = null;
				} else {
					aOutput1.stackSize -= aInput2.stackSize;
				}
			}
			if (GT_Utility.areStacksEqual(aInput2, aOutput2)) {
				if (aInput2.stackSize >= aOutput2.stackSize) {
					aInput2.stackSize -= aOutput2.stackSize;
					aOutput2 = null;
				} else {
					aOutput2.stackSize -= aInput2.stackSize;
				}
			}
			if (GT_Utility.areStacksEqual(aInput2, aOutput3)) {
				if (aInput2.stackSize >= aOutput3.stackSize) {
					aInput2.stackSize -= aOutput3.stackSize;
					aOutput3 = null;
				} else {
					aOutput3.stackSize -= aInput2.stackSize;
				}
			}
			if (GT_Utility.areStacksEqual(aInput2, aOutput4)) {
				if (aInput2.stackSize >= aOutput4.stackSize) {
					aInput2.stackSize -= aOutput4.stackSize;
					aOutput4 = null;
				} else {
					aOutput4.stackSize -= aInput2.stackSize;
				}
			}
		}
		
		for (byte i = 64; i > 1; i--) if (aDuration / i > 0) {
			if (aInput1  == null || aInput1 .stackSize % i == 0)
			if (aInput2  == null || aInput2 .stackSize % i == 0)
			if (aOutput1 == null || aOutput1.stackSize % i == 0)
			if (aOutput2 == null || aOutput2.stackSize % i == 0)
			if (aOutput3 == null || aOutput3.stackSize % i == 0)
			if (aOutput4 == null || aOutput4.stackSize % i == 0) {
				if (aInput1  != null) aInput1 .stackSize /= i;
				if (aInput2  != null) aInput2 .stackSize /= i;
				if (aOutput1 != null) aOutput1.stackSize /= i;
				if (aOutput2 != null) aOutput2.stackSize /= i;
				if (aOutput3 != null) aOutput3.stackSize /= i;
				if (aOutput4 != null) aOutput4.stackSize /= i;
				aDuration /= i;
			}
		}
		
		mInput1 = aInput1;
		mInput2 = aInput2;
		mOutput1 = aOutput1;
		mOutput2 = aOutput2;
		mOutput3 = aOutput3;
		mOutput4 = aOutput4;
		mDuration = aDuration;
		mStartEU = aStartEU;
		mEUt = aEUt;
	}
	
	public GT_Recipe(ItemStack aInput1, ItemStack aOutput1, int aStartEU, int aType) {
		this(aInput1, aOutput1, null, null, null, aStartEU, aType);
	}
	
	// aStartEU = EU per Liter! If there is no Liquid for this Object, then it gets multiplied with 1000!
	public GT_Recipe(ItemStack aInput1, ItemStack aOutput1, ItemStack aOutput2, ItemStack aOutput3, ItemStack aOutput4, int aStartEU, int aType) {
		this(aInput1, null, aOutput1, aOutput2, aOutput3, aOutput4, 0, 0, Math.max(1, aStartEU));
		
		if (mInput1 != null && aStartEU > 0) {
			switch (aType) {
			// Diesel Generator
			case 0:
				pDieselFuels.put(GT_Utility.stacksToLong(mInput1, mInput2), this);
				sDieselFuels.add(this);
				break;
			// Gas Turbine
			case 1:
				pTurbineFuels.put(GT_Utility.stacksToLong(mInput1, mInput2), this);
				sTurbineFuels.add(this);
				break;
			// Thermal Generator
			case 2:
				pHotFuels.put(GT_Utility.stacksToLong(mInput1, mInput2), this);
				sHotFuels.add(this);
				break;
			// Plasma Generator
			case 4:
				pPlasmaFuels.put(GT_Utility.stacksToLong(mInput1, mInput2), this);
				sPlasmaFuels.add(this);
				break;
			// Magic Generator
			case 5:
				pMagicFuels.put(GT_Utility.stacksToLong(mInput1, mInput2), this);
				sMagicFuels.add(this);
				break;
			// Fluid Generator. Usually 3. Every wrong Type ends up in the Semifluid Generator
			default:
				pDenseLiquidFuels.put(GT_Utility.stacksToLong(mInput1, mInput2), this);
				sDenseLiquidFuels.add(this);
				break;
			}
		}
	}
	
	public GT_Recipe(ItemStack aInput1, ItemStack aInput2, ItemStack aOutput1, int aDuration, int aEUt, int aStartEU) {
		this(aInput1, aInput2, aOutput1, null, null, null, Math.max(aDuration, 1), aEUt, Math.max(Math.min(aStartEU, 160000000), 0));
		if (mInput1 != null && mInput2 != null && findEqualFusionRecipe(mInput1, mInput2) == null) {
			pFusionRecipes.put(GT_Utility.stacksToLong(mInput1, mInput2), this);
		}
	}
	
	public GT_Recipe(ItemStack aInput1, ItemStack aInput2, ItemStack aOutput1, ItemStack aOutput2, ItemStack aOutput3, ItemStack aOutput4, int aDuration) {
		this(aInput1, aInput2, aOutput1, aOutput2, aOutput3, aOutput4, Math.max(aDuration, 1), 5, 0);
		checkCellBalance();
		if (mInput1 != null && mOutput1 != null && findEqualCentrifugeRecipe(mInput1, mInput2) == null) {
			pCentrifugeRecipes.put(GT_Utility.stacksToLong(mInput1, mInput2), this);
		}
	}
	
	public GT_Recipe(ItemStack aInput1, ItemStack aInput2, ItemStack aOutput1, ItemStack aOutput2, ItemStack aOutput3, ItemStack aOutput4, int aDuration, int aEUt) {
		this(aInput1, aInput2, aOutput1, aOutput2, aOutput3, aOutput4, Math.max(aDuration, 1), Math.max(aEUt, 1), 0);
		checkCellBalance();
		if (mInput1 != null && mOutput1 != null && findEqualElectrolyzerRecipe(mInput1, mInput2) == null) {
			pElectrolyzerRecipes.put(GT_Utility.stacksToLong(mInput1, mInput2), this);
		}
	}
	
	public GT_Recipe(ItemStack aInput1, ItemStack aOutput1, ItemStack aOutput2, int aDuration, int aEUt) {
		this(aInput1, null, aOutput1, aOutput2, null, null, aDuration, aEUt, 0);
		checkCellBalance();
		if (mInput1 != null && mOutput1 != null && findEqualLatheRecipe(mInput1, mInput2) == null) {
			pLatheRecipes.put(GT_Utility.stacksToLong(mInput1, mInput2), this);
		}
	}
	
	public GT_Recipe(ItemStack aInput1, int aDuration, ItemStack aOutput1, int aEUt) {
		this(aInput1, null, aOutput1, null, null, null, aDuration, aEUt, 0);
		checkCellBalance();
		if (mInput1 != null && mOutput1 != null && findEqualCutterRecipe(mInput1, mInput2) == null) {
			pCutterRecipes.put(GT_Utility.stacksToLong(mInput1, mInput2), this);
		}
	}
	
	public GT_Recipe(ItemStack aInput1, ItemStack aInput2, ItemStack aOutput1, ItemStack aOutput2, ItemStack aOutput3) {
		this(aInput1, aInput2, aOutput1, aOutput2, aOutput3, null, 200*aInput1.stackSize, 30, 0);
		checkCellBalance();
		if (mInput1 != null && mOutput1 != null && findEqualSawmillRecipe(mInput1, mInput2) == null) {
			pSawmillRecipes.put(GT_Utility.stacksToLong(mInput1, mInput2), this);
		}
	}
	
	public GT_Recipe(ItemStack aInput1, ItemStack aInput2, ItemStack aOutput1, ItemStack aOutput2, ItemStack aOutput3, ItemStack aOutput4) {
		this(aInput1, aInput2, aOutput1, aOutput2, aOutput3, aOutput4, 100*aInput1.stackSize, 120, 0);
		checkCellBalance();
		if (mInput1 != null && aInput2 != null && mOutput1 != null && findEqualGrinderRecipe(mInput1, mInput2) == null) {
			pGrinderRecipes.put(GT_Utility.stacksToLong(mInput1, mInput2), this);
		}
	}
	
	public GT_Recipe(ItemStack aInput1, int aCellAmount, ItemStack aOutput1, ItemStack aOutput2, ItemStack aOutput3, ItemStack aOutput4, int aDuration, int aEUt) {
		this(aInput1, aCellAmount>0?GT_Items.Cell_Empty.get(Math.min(64, Math.max(1, aCellAmount))):null, aOutput1, aOutput2, aOutput3, aOutput4, Math.max(aDuration, 1), Math.max(aEUt, 1), 0);
		checkCellBalance();
		if (mInput1 != null && mOutput1 != null && findEqualDistillationRecipe(mInput1, mInput2) == null) {
			pDistillationRecipes.put(GT_Utility.stacksToLong(mInput1, mInput2), this);
		}
	}
	
	public GT_Recipe(ItemStack aInput1, ItemStack aInput2, ItemStack aOutput1, ItemStack aOutput2, int aDuration, int aEUt, int aLevel) {
		this(aInput1, aInput2, aOutput1, aOutput2, null, null, Math.max(aDuration, 1), Math.max(aEUt, 1), aLevel > 0 ? aLevel : 100);
		checkCellBalance();
		if (mInput1 != null && mOutput1 != null && findEqualBlastRecipe(mInput1, mInput2) == null) {
			pBlastRecipes.put(GT_Utility.stacksToLong(mInput1, mInput2), this);
		}
	}
	
	public GT_Recipe(ItemStack aInput1, int aInput2, ItemStack aOutput1, ItemStack aOutput2) {
		this(aInput1, GT_ModHandler.getIC2Item("industrialTnt", aInput2>0?aInput2<64?aInput2:64:1, new ItemStack(Block.tnt, aInput2>0?aInput2<64?aInput2:64:1)), aOutput1, aOutput2, null, null, 20, 30, 0);
		checkCellBalance();
		if (mInput1 != null && mOutput1 != null && findEqualImplosionRecipe(mInput1, mInput2) == null) {
			pImplosionRecipes.put(GT_Utility.stacksToLong(mInput1, mInput2), this);
		}
	}
	
	public GT_Recipe(ItemStack aInput1, int aEUt, int aDuration, ItemStack aOutput1) {
		this(aInput1, null, aOutput1, null, null, null, Math.max(aDuration, 1), Math.max(aEUt, 1), 0);
		checkCellBalance();
		if (mInput1 != null && mOutput1 != null && findEqualWiremillRecipe(mInput1, mInput2) == null) {
			pWiremillRecipes.put(GT_Utility.stacksToLong(mInput1, mInput2), this);
		}
	}
	
	public GT_Recipe(int aEUt, int aDuration, ItemStack aInput1, ItemStack aOutput1) {
		this(aInput1, null, aOutput1, null, null, null, Math.max(aDuration, 1), Math.max(aEUt, 1), 0);
		checkCellBalance();
		if (mInput1 != null && mOutput1 != null && findEqualBenderRecipe(mInput1, mInput2) == null) {
			pBenderRecipes.put(GT_Utility.stacksToLong(mInput1, mInput2), this);
		}
	}
	
	public GT_Recipe(int aEUt, int aDuration, ItemStack aInput1, ItemStack aShape, ItemStack aOutput1) {
		this(aInput1, aShape, aOutput1, null, null, null, Math.max(aDuration, 1), Math.max(aEUt, 1), 0);
		checkCellBalance();
		if (mInput1 != null && mInput2 != null && mOutput1 != null && findEqualExtruderRecipe(mInput1, mInput2) == null) {
			pExtruderRecipes.put(GT_Utility.stacksToLong(mInput1, mInput2), this);
		}
	}
	
	public GT_Recipe(ItemStack aInput1, int aEUt, ItemStack aInput2, int aDuration, ItemStack aOutput1) {
		this(aInput1, aInput2, aOutput1, null, null, null, Math.max(aDuration, 1), Math.max(aEUt, 1), 0);
		if (mInput1 != null && mOutput1 != null && findEqualAssemblerRecipe(mInput1, mInput2) == null) {
			pAssemblerRecipes.put(GT_Utility.stacksToLong(mInput1, mInput2), this);
		}
	}
	
	public GT_Recipe(ItemStack aInput1, ItemStack aInput2, int aEUt, int aDuration, ItemStack aOutput1) {
		this(aInput1, aInput2, aOutput1, null, null, null, Math.max(aDuration, 1), Math.max(aEUt, 1), 0);
		checkCellBalance();
		if (mInput1 != null && mOutput1 != null && findEqualAlloySmelterRecipe(mInput1, mInput2) == null) {
			pAlloySmelterRecipes.put(GT_Utility.stacksToLong(mInput1, mInput2), this);
		}
	}
	
	public GT_Recipe(ItemStack aInput1, int aEUt, ItemStack aInput2, int aDuration, ItemStack aOutput1, ItemStack aOutput2) {
		this(aInput1, aInput2, aOutput1, aOutput2, null, null, Math.max(aDuration, 1), Math.max(aEUt, 1), 0);
		checkCellBalance();
		if (mInput1 != null && mOutput1 != null && findEqualCannerRecipe(mInput1, mInput2) == null) {
			pCannerRecipes.put(GT_Utility.stacksToLong(mInput1, mInput2), this);
		}
	}
	
	public GT_Recipe(ItemStack aInput1, ItemStack aOutput1, int aDuration) {
		this(aInput1, null, aOutput1, null, null, null, Math.max(aDuration, 1), 120, 0);
		checkCellBalance();
		if (mInput1 != null && mOutput1 != null && findEqualVacuumRecipe(mInput1, mInput2) == null) {
			pVacuumRecipes.put(GT_Utility.stacksToLong(mInput1, mInput2), this);
		}
	}
	
	public GT_Recipe(ItemStack aInput1, ItemStack aInput2, ItemStack aOutput1, int aDuration) {
		this(aInput1, aInput2, aOutput1, null, null, null, Math.max(aDuration, 1), 30, 0);
		checkCellBalance();
		if (mInput1 != null && mOutput1 != null && findEqualChemicalRecipe(mInput1, mInput2) == null) {
			pChemicalRecipes.put(GT_Utility.stacksToLong(mInput1, mInput2), this);
		}
	}
	
	public static GT_Recipe findEqualRecipe(ItemStack aInput1, ItemStack aInput2, boolean aShapeless, Map<Long, GT_Recipe> aHash) {
		GT_Recipe rRecipe;
		if (aShapeless && (rRecipe = findEqualRecipe(aInput2, aInput1, false, aHash)) != null) return rRecipe;
		if (aInput1 == null) return null;
		
		aInput1 = GT_OreDictUnificator.get(false, aInput1);
		aInput2 = GT_OreDictUnificator.get(false, aInput2);
		
		rRecipe = aHash.get(GT_Utility.stacksToLong(aInput1, aInput2));
		if (isRecipeInputEqual(aShapeless, false, aInput1, aInput2, rRecipe)) return rRecipe;
		rRecipe = aHash.get(GT_Utility.stacksToLong(aInput1, null));
		if (isRecipeInputEqual(aShapeless, false, aInput1, aInput2, rRecipe)) return rRecipe;
		rRecipe = aHash.get(GT_Utility.stacksToLong(GT_Utility.copyMetaData(GregTech_API.ITEM_WILDCARD_DAMAGE, aInput1), aInput2));
		if (isRecipeInputEqual(aShapeless, false, aInput1, aInput2, rRecipe)) return rRecipe;
		rRecipe = aHash.get(GT_Utility.stacksToLong(aInput1, GT_Utility.copyMetaData(GregTech_API.ITEM_WILDCARD_DAMAGE, aInput2)));
		if (isRecipeInputEqual(aShapeless, false, aInput1, aInput2, rRecipe)) return rRecipe;
		rRecipe = aHash.get(GT_Utility.stacksToLong(GT_Utility.copyMetaData(GregTech_API.ITEM_WILDCARD_DAMAGE, aInput1), GT_Utility.copyMetaData(GregTech_API.ITEM_WILDCARD_DAMAGE, aInput2)));
		if (isRecipeInputEqual(aShapeless, false, aInput1, aInput2, rRecipe)) return rRecipe;
		
		for (GT_Recipe tHashRecipe : aHash.values()) {
			if (isRecipeInputEqual(aShapeless, false, aInput1, aInput2, tHashRecipe)) {
				GT_Log.err.println("Didn't find Recipe via Hashcode! " + tHashRecipe.mInput1.getDisplayName() + " / " + (tHashRecipe.mInput2==null?"NULL":tHashRecipe.mInput2.getDisplayName()));
				return tHashRecipe;
			}
		}
		return null;
	}
	
	public void checkCellBalance() {
		try {
			if (mInput1 == null) return;
			int tInputAmount  = GT_ModHandler.getCapsuleCellContainerCountMultipliedWithStackSize(mInput1 ) + GT_ModHandler.getCapsuleCellContainerCountMultipliedWithStackSize(mInput2 );
			int tOutputAmount = GT_ModHandler.getCapsuleCellContainerCountMultipliedWithStackSize(mOutput1) + GT_ModHandler.getCapsuleCellContainerCountMultipliedWithStackSize(mOutput2) + GT_ModHandler.getCapsuleCellContainerCountMultipliedWithStackSize(mOutput3) + GT_ModHandler.getCapsuleCellContainerCountMultipliedWithStackSize(mOutput4);
			
			if (tInputAmount < tOutputAmount) {
				if (!Materials.Tin.contains(mInput1, mInput2)) {
					GT_Log.err.println("You get more Cells, than you put in? There must be something wrong. " + mInput1.getDisplayName() + " / " + (mInput2==null?"NULL":mInput2.getDisplayName()));
					if (GregTech_API.DEBUG_MODE) new Exception().printStackTrace(GT_Log.err);
				}
			} else if (tInputAmount > tOutputAmount) {
				if (!Materials.Tin.contains(mOutput1, mOutput2, mOutput3, mOutput4)) {
					GT_Log.err.println("You get less Cells, than you put in? GT Machines usually don't destroy Cells. " + mInput1.getDisplayName() + " / " + (mInput2==null?"NULL":mInput2.getDisplayName()));
					if (GregTech_API.DEBUG_MODE) new Exception().printStackTrace(GT_Log.err);
				}
			}
		} catch(Throwable e) {
			e.printStackTrace(GT_Log.err);
		}
	}
	
	public boolean isRecipeInputEqual(boolean aShapeless, boolean aDecreaseStacksizeBySuccess, ItemStack aInput1, ItemStack aInput2) {
		if (aShapeless) if (isRecipeInputEqual(false, aDecreaseStacksizeBySuccess, aInput2, aInput1)) return true;
		if (aInput1 == null) return false;
		if (GT_Utility.areStacksEqual(GT_OreDictUnificator.get(false, aInput1), mInput1, true) && aInput1.stackSize >= mInput1.stackSize) {
			if (mInput2 != null && (aInput2 == null || !GT_Utility.areStacksEqual(GT_OreDictUnificator.get(false, aInput2), mInput2, true) || aInput2.stackSize < mInput2.stackSize)) return false;
			if (aDecreaseStacksizeBySuccess) {
				aInput1.stackSize -= mInput1.stackSize;
				if (mInput2 != null) aInput2.stackSize -= mInput2.stackSize;
			}
			return true;
		}
		return false;
	}
	
	public static boolean isRecipeInputEqual(boolean aShapeless, boolean aDecreaseStacksizeBySuccess, ItemStack aInput1, ItemStack aInput2, GT_Recipe aRecipe) {
		if (aRecipe == null) return false;
		return aRecipe.isRecipeInputEqual(aShapeless, aDecreaseStacksizeBySuccess, aInput1, aInput2);
	}
	
	public static GT_Recipe findEqualWiremillRecipe(ItemStack aInput1, ItemStack aInput2) {
		return findEqualRecipe(aInput1, aInput2, false, pWiremillRecipes);
	}
	
	public static GT_Recipe findEqualBenderRecipe(ItemStack aInput1, ItemStack aInput2) {
		return findEqualRecipe(aInput1, aInput2, false, pBenderRecipes);
	}

	public static GT_Recipe findEqualAssemblerRecipe(ItemStack aInput1, ItemStack aInput2) {
		return findEqualRecipe(aInput1, aInput2, true, pAssemblerRecipes);
	}
	
	public static GT_Recipe findEqualAlloySmelterRecipe(ItemStack aInput1, ItemStack aInput2) {
		return findEqualRecipe(aInput1, aInput2, true, pAlloySmelterRecipes);
	}

	public static GT_Recipe findEqualCannerRecipe(ItemStack aInput1, ItemStack aInput2) {
		return findEqualRecipe(aInput1, aInput2, true, pCannerRecipes);
	}
	
	public static GT_Recipe findEqualDistillationRecipe(ItemStack aInput1, ItemStack aInput2) {
		return findEqualRecipe(aInput1, aInput2, false, pDistillationRecipes);
	}
	
	public static GT_Recipe findEqualFusionRecipe(ItemStack aInput1, ItemStack aInput2) {
		return findEqualRecipe(aInput1, aInput2, true, pFusionRecipes);
	}

	public static GT_Recipe findEqualCentrifugeRecipe(ItemStack aInput1, ItemStack aInput2) {
		return findEqualRecipe(aInput1, aInput2, false, pCentrifugeRecipes);
	}

	public static GT_Recipe findEqualElectrolyzerRecipe(ItemStack aInput1, ItemStack aInput2) {
		return findEqualRecipe(aInput1, aInput2, false, pElectrolyzerRecipes);
	}

	public static GT_Recipe findEqualSawmillRecipe(ItemStack aInput1, ItemStack aInput2) {
		return findEqualRecipe(aInput1, aInput2, false, pSawmillRecipes);
	}
	
	public static GT_Recipe findEqualGrinderRecipe(ItemStack aInput1, ItemStack aInput2) {
		return findEqualRecipe(aInput1, aInput2, false, pGrinderRecipes);
	}
	
	public static GT_Recipe findEqualBlastRecipe(ItemStack aInput1, ItemStack aInput2) {
		return findEqualRecipe(aInput1, aInput2, true, pBlastRecipes);
	}

	public static GT_Recipe findEqualImplosionRecipe(ItemStack aInput1, ItemStack aInput2) {
		return findEqualRecipe(aInput1, aInput2, false, pImplosionRecipes);
	}

	public static GT_Recipe findEqualVacuumRecipe(ItemStack aInput1, ItemStack aInput2) {
		return findEqualRecipe(aInput1, aInput2, false, pVacuumRecipes);
	}
	
	public static GT_Recipe findEqualChemicalRecipe(ItemStack aInput1, ItemStack aInput2) {
		return findEqualRecipe(aInput1, aInput2, true, pChemicalRecipes);
	}

	public static GT_Recipe findEqualLatheRecipe(ItemStack aInput1, ItemStack aInput2) {
		return findEqualRecipe(aInput1, aInput2, true, pLatheRecipes);
	}

	public static GT_Recipe findEqualCutterRecipe(ItemStack aInput1, ItemStack aInput2) {
		return findEqualRecipe(aInput1, aInput2, true, pCutterRecipes);
	}
	
	public static GT_Recipe findEqualExtruderRecipe(ItemStack aInput1, ItemStack aInput2) {
		return findEqualRecipe(aInput1, aInput2, false, pExtruderRecipes);
	}
}