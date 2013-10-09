package gregtechmod.api.util;

import gregtechmod.api.GregTech_API;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

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
	public static volatile int VERSION = 402;
	
	/**
	 * If you want to remove Recipes, then set the Index to null, instead of removing the complete Entry!
	 * That's because I have a mapping for quick access, so you should also remove the Mapping of the Recipe.
	 * 
	 * However, every single one of these Recipes has a Config, so you could just disable the Config Setting.
	 */
	public static ArrayList<GT_Recipe> sFusionRecipes		= new ArrayList<GT_Recipe>();
	public static ArrayList<GT_Recipe> sCentrifugeRecipes	= new ArrayList<GT_Recipe>();
	public static ArrayList<GT_Recipe> sElectrolyzerRecipes = new ArrayList<GT_Recipe>();
	public static ArrayList<GT_Recipe> sGrinderRecipes		= new ArrayList<GT_Recipe>();
	public static ArrayList<GT_Recipe> sBlastRecipes		= new ArrayList<GT_Recipe>();
	public static ArrayList<GT_Recipe> sImplosionRecipes	= new ArrayList<GT_Recipe>();
	public static ArrayList<GT_Recipe> sSawmillRecipes		= new ArrayList<GT_Recipe>();
	public static ArrayList<GT_Recipe> sVacuumRecipes		= new ArrayList<GT_Recipe>();
	public static ArrayList<GT_Recipe> sChemicalRecipes		= new ArrayList<GT_Recipe>();
	public static ArrayList<GT_Recipe> sDistillationRecipes	= new ArrayList<GT_Recipe>();
	public static ArrayList<GT_Recipe> sWiremillRecipes		= new ArrayList<GT_Recipe>();
	public static ArrayList<GT_Recipe> sBenderRecipes		= new ArrayList<GT_Recipe>();
	public static ArrayList<GT_Recipe> sAlloySmelterRecipes	= new ArrayList<GT_Recipe>();
	public static ArrayList<GT_Recipe> sAssemblerRecipes	= new ArrayList<GT_Recipe>();
	public static ArrayList<GT_Recipe> sCannerRecipes		= new ArrayList<GT_Recipe>();
	public static ArrayList<GT_Recipe> sCNCRecipes			= new ArrayList<GT_Recipe>();
	public static ArrayList<GT_Recipe> sLatheRecipes		= new ArrayList<GT_Recipe>();
	public static ArrayList<GT_Recipe> sCutterRecipes		= new ArrayList<GT_Recipe>();
	
	public static ArrayList<GT_Recipe> sDieselFuels			= new ArrayList<GT_Recipe>();
	public static ArrayList<GT_Recipe> sTurbineFuels		= new ArrayList<GT_Recipe>();
	public static ArrayList<GT_Recipe> sHotFuels			= new ArrayList<GT_Recipe>();
	public static ArrayList<GT_Recipe> sDenseLiquidFuels	= new ArrayList<GT_Recipe>();
	public static ArrayList<GT_Recipe> sPlasmaFuels			= new ArrayList<GT_Recipe>();
	public static ArrayList<GT_Recipe> sMagicFuels			= new ArrayList<GT_Recipe>();
	
	public static Map<Long, Integer> pFusionRecipes			= new HashMap<Long, Integer>();
	public static Map<Long, Integer> pCentrifugeRecipes		= new HashMap<Long, Integer>();
	public static Map<Long, Integer> pElectrolyzerRecipes	= new HashMap<Long, Integer>();
	public static Map<Long, Integer> pGrinderRecipes		= new HashMap<Long, Integer>();
	public static Map<Long, Integer> pBlastRecipes			= new HashMap<Long, Integer>();
	public static Map<Long, Integer> pImplosionRecipes		= new HashMap<Long, Integer>();
	public static Map<Long, Integer> pSawmillRecipes		= new HashMap<Long, Integer>();
	public static Map<Long, Integer> pVacuumRecipes			= new HashMap<Long, Integer>();
	public static Map<Long, Integer> pChemicalRecipes		= new HashMap<Long, Integer>();
	public static Map<Long, Integer> pDistillationRecipes	= new HashMap<Long, Integer>();
	public static Map<Long, Integer> pWiremillRecipes		= new HashMap<Long, Integer>();
	public static Map<Long, Integer> pBenderRecipes			= new HashMap<Long, Integer>();
	public static Map<Long, Integer> pAlloySmelterRecipes	= new HashMap<Long, Integer>();
	public static Map<Long, Integer> pAssemblerRecipes		= new HashMap<Long, Integer>();
	public static Map<Long, Integer> pCannerRecipes			= new HashMap<Long, Integer>();
	public static Map<Long, Integer> pCNCRecipes			= new HashMap<Long, Integer>();
	public static Map<Long, Integer> pLatheRecipes			= new HashMap<Long, Integer>();
	public static Map<Long, Integer> pCutterRecipes			= new HashMap<Long, Integer>();
	
	public static Map<Long, Integer> pDieselFuels			= new HashMap<Long, Integer>();
	public static Map<Long, Integer> pTurbineFuels			= new HashMap<Long, Integer>();
	public static Map<Long, Integer> pHotFuels				= new HashMap<Long, Integer>();
	public static Map<Long, Integer> pDenseLiquidFuels		= new HashMap<Long, Integer>();
	public static Map<Long, Integer> pPlasmaFuels			= new HashMap<Long, Integer>();
	public static Map<Long, Integer> pMagicFuels			= new HashMap<Long, Integer>();
	
	public final ItemStack mInput1, mInput2, mOutput1, mOutput2, mOutput3, mOutput4;
	public final int mDuration, mEUt, mStartEU;
	
	public ItemStack getRepresentativeInput1() {
		return mInput1;
	}
	
	public ItemStack getRepresentativeInput2() {
		return mInput2;
	}
	
	private GT_Recipe(ItemStack aInput1, ItemStack aInput2, ItemStack aOutput1, ItemStack aOutput2, ItemStack aOutput3, ItemStack aOutput4, int aDuration, int aEUt, int aStartEU) {
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
	
	public GT_Recipe(ItemStack aInput1, ItemStack aOutput1, ItemStack aOutput2, ItemStack aOutput3, ItemStack aOutput4, int aStartEU, int aType) {
		mInput1   = aInput1==null?null:aInput1.copy();
		mInput2   = null;
		mOutput1  = aOutput1;
		mOutput2  = aOutput2;
		mOutput3  = aOutput3;
		mOutput4  = aOutput4;
		mDuration = 0;
		mEUt      = 0;
		// That's EU per MilliBucket! If there is no Liquid for this Object, then it gets multiplied with 1000!
		mStartEU  = Math.max(1, aStartEU);
		
		if (mInput1 != null && aStartEU > 0) {
			switch (aType) {
			// Diesel Generator
			case 0:
				pDieselFuels.put(GT_Utility.stacksToLong(mInput1, mInput2), sDieselFuels.size());
				sDieselFuels.add(this);
				break;
			// Gas Turbine
			case 1:
				pTurbineFuels.put(GT_Utility.stacksToLong(mInput1, mInput2), sTurbineFuels.size());
				sTurbineFuels.add(this);
				break;
			// Thermal Generator
			case 2:
				pHotFuels.put(GT_Utility.stacksToLong(mInput1, mInput2), sHotFuels.size());
				sHotFuels.add(this);
				break;
			// Fluid Generator
			case 3:
				pDenseLiquidFuels.put(GT_Utility.stacksToLong(mInput1, mInput2), sDenseLiquidFuels.size());
				sDenseLiquidFuels.add(this);
				break;
			// Plasma Generator
			case 4:
				pPlasmaFuels.put(GT_Utility.stacksToLong(mInput1, mInput2), sPlasmaFuels.size());
				sPlasmaFuels.add(this);
				break;
			// Magic Generator
			case 5:
				pMagicFuels.put(GT_Utility.stacksToLong(mInput1, mInput2), sMagicFuels.size());
				sMagicFuels.add(this);
				break;
			}
		}
	}
	
	public GT_Recipe(ItemStack aInput1, ItemStack aInput2, ItemStack aOutput1, int aDuration, int aEUt, int aStartEU) {
		mInput1   = aInput1==null?null:aInput1.copy();
		mInput2   = aInput2==null?null:aInput2.copy();
		mOutput1  = aOutput1==null?null:aOutput1.copy();
		mOutput2  = null;
		mOutput3  = null;
		mOutput4  = null;
		mDuration = Math.max(aDuration, 1);
		mEUt      = aEUt;
		mStartEU  = Math.max(Math.min(aStartEU, 100000000), 0);
		
		if (mInput1 != null && mInput2 != null && findEqualFusionRecipeIndex(mInput1, mInput2) == -1) {
			pFusionRecipes.put(GT_Utility.stacksToLong(mInput1, mInput2), sFusionRecipes.size());
			sFusionRecipes.add(this);
		}
	}

	public GT_Recipe(ItemStack aInput1, ItemStack aInput2, ItemStack aOutput1, ItemStack aOutput2, ItemStack aOutput3, ItemStack aOutput4, int aDuration) {
		mInput1   = aInput1==null?null:aInput1.copy();
		mInput2   = aInput2==null?null:aInput2.copy();
		mOutput1  = aOutput1==null?null:aOutput1.copy();
		mOutput2  = aOutput2==null?null:aOutput2.copy();
		mOutput3  = aOutput3==null?null:aOutput3.copy();
		mOutput4  = aOutput4==null?null:aOutput4.copy();
		mDuration = Math.max(aDuration, 1);
		mEUt      = 5;
		mStartEU  = 0;

		checkCellBalance();
		
		if (mInput1 != null && mOutput1 != null && findEqualCentrifugeRecipeIndex(mInput1, mInput2) == -1) {
			pCentrifugeRecipes.put(GT_Utility.stacksToLong(mInput1, mInput2), sCentrifugeRecipes.size());
			sCentrifugeRecipes.add(this);
		}
	}

	public GT_Recipe(ItemStack aInput1, ItemStack aInput2, ItemStack aOutput1, ItemStack aOutput2, ItemStack aOutput3, ItemStack aOutput4, int aDuration, int aEUt) {
		mInput1   = aInput1==null?null:aInput1.copy();
		mInput2   = aInput2==null?null:aInput2.copy();
		mOutput1  = aOutput1==null?null:aOutput1.copy();
		mOutput2  = aOutput2==null?null:aOutput2.copy();
		mOutput3  = aOutput3==null?null:aOutput3.copy();
		mOutput4  = aOutput4==null?null:aOutput4.copy();
		mDuration = Math.max(aDuration, 1);
		mEUt      = aEUt>0?aEUt:1;
		mStartEU  = 0;

		checkCellBalance();
		
		if (mInput1 != null && mOutput1 != null && findEqualElectrolyzerRecipeIndex(mInput1, mInput2) == -1) {
			pElectrolyzerRecipes.put(GT_Utility.stacksToLong(mInput1, mInput2), sElectrolyzerRecipes.size());
			sElectrolyzerRecipes.add(this);
		}
	}

	public GT_Recipe(ItemStack aInput1, ItemStack aOutput1, ItemStack aOutput2, int aDuration, int aEUt) {
		this(aInput1, null, aOutput1, aOutput2, null, null, aDuration, aEUt, 0);
		
		if (mInput1 != null && mOutput1 != null && findEqualLatheRecipeIndex(mInput1, mInput2) == -1) {
			pLatheRecipes.put(GT_Utility.stacksToLong(mInput1, mInput2), sLatheRecipes.size());
			sLatheRecipes.add(this);
		}
	}
	
	public GT_Recipe(ItemStack aInput1, int aDuration, ItemStack aOutput1, int aEUt) {
		this(aInput1, null, aOutput1, null, null, null, aDuration, aEUt, 0);
		
		if (mInput1 != null && mOutput1 != null && findEqualCutterRecipeIndex(mInput1, mInput2) == -1) {
			pCutterRecipes.put(GT_Utility.stacksToLong(mInput1, mInput2), sCutterRecipes.size());
			sCutterRecipes.add(this);
		}
	}
	
	public GT_Recipe(ItemStack aInput1, ItemStack aInput2, ItemStack aOutput1, ItemStack aOutput2, ItemStack aOutput3) {
		mInput1   = aInput1==null?null:aInput1.copy();
		mInput2   = aInput2==null?null:aInput2.copy();
		mOutput1  = aOutput1==null?null:aOutput1.copy();
		mOutput2  = aOutput2==null?null:aOutput2.copy();
		mOutput3  = aOutput3==null?null:aOutput3.copy();
		mOutput4  = null;
		mDuration = 200*(mInput1!=null?mInput1.stackSize:1);
		mEUt      = 32;
		mStartEU  = 0;
		
		checkCellBalance();
		
		if (mInput1 != null && mOutput1 != null && findEqualSawmillRecipeIndex(mInput1, mInput2) == -1) {
			pSawmillRecipes.put(GT_Utility.stacksToLong(mInput1, mInput2), sSawmillRecipes.size());
			sSawmillRecipes.add(this);
		}
	}
	
	public GT_Recipe(ItemStack aInput1, ItemStack aInput2, ItemStack aOutput1, ItemStack aOutput2, ItemStack aOutput3, ItemStack aOutput4) {
		mInput1   = aInput1==null?null:aInput1.copy();
		mInput2   = aInput2==null?null:aInput2.copy();
		mOutput1  = aOutput1==null?null:aOutput1.copy();
		mOutput2  = aOutput2==null?null:aOutput2.copy();
		mOutput3  = aOutput3==null?null:aOutput3.copy();
		mOutput4  = aOutput4==null?null:aOutput4.copy();
		mDuration = 100*(mInput1!=null?mInput1.stackSize:1);
		mEUt      = 128;
		mStartEU  = 0;

		checkCellBalance();
		
		if (mInput1 != null && aInput2 != null && mOutput1 != null && findEqualGrinderRecipeIndex(mInput1, mInput2) == -1) {
			pGrinderRecipes.put(GT_Utility.stacksToLong(mInput1, mInput2), sGrinderRecipes.size());
			sGrinderRecipes.add(this);
		}
	}
	
	public GT_Recipe(ItemStack aInput1, int aCellAmount, ItemStack aOutput1, ItemStack aOutput2, ItemStack aOutput3, ItemStack aOutput4, int aDuration, int aEUt) {
		mInput1   = aInput1==null?null:aInput1.copy();
		mInput2   = aCellAmount>0?GT_ModHandler.getEmptyCell(Math.min(64, Math.max(1, aCellAmount))):null;
		mOutput1  = aOutput1==null?null:aOutput1.copy();
		mOutput2  = aOutput2==null?null:aOutput2.copy();
		mOutput3  = aOutput3==null?null:aOutput3.copy();
		mOutput4  = aOutput4==null?null:aOutput4.copy();
		mDuration = Math.max(aDuration, 1);
		mEUt      = Math.max(aEUt, 1);
		mStartEU  = 0;
		
		checkCellBalance();
		
		if (mInput1 != null && mOutput1 != null && findEqualDistillationRecipeIndex(mInput1, mInput2) == -1) {
			pDistillationRecipes.put(GT_Utility.stacksToLong(mInput1, mInput2), sDistillationRecipes.size());
			sDistillationRecipes.add(this);
		}
	}
	
	public GT_Recipe(ItemStack aInput1, ItemStack aInput2, ItemStack aOutput1, ItemStack aOutput2, int aDuration, int aEUt, int aLevel) {
		mInput1   = aInput1==null?null:aInput1.copy();
		mInput2   = aInput2==null?null:aInput2.copy();
		mOutput1  = aOutput1==null?null:aOutput1.copy();
		mOutput2  = aOutput2==null?null:aOutput2.copy();
		mOutput3  = null;
		mOutput4  = null;
		mDuration = Math.max(aDuration, 1);
		mEUt      = Math.max(aEUt, 1);
		mStartEU  = aLevel>0?aLevel:100;

		checkCellBalance();
		
		if (mInput1 != null && mOutput1 != null && findEqualBlastRecipeIndex(mInput1, mInput2) == -1) {
			pBlastRecipes.put(GT_Utility.stacksToLong(mInput1, mInput2), sBlastRecipes.size());
			sBlastRecipes.add(this);
		}
	}
	
	public GT_Recipe(ItemStack aInput1, int aInput2, ItemStack aOutput1, ItemStack aOutput2) {
		mInput1   = aInput1==null?null:aInput1.copy();
		ItemStack tStack = GT_ModHandler.getIC2Item("industrialTnt", 1, new ItemStack(Block.tnt, 1));
		tStack.stackSize = (aInput2>0?aInput2<64?aInput2:64:1);
		mInput2   = tStack;
		mOutput1  = aOutput1==null?null:aOutput1.copy();
		mOutput2  = aOutput2==null?null:aOutput2.copy();
		mOutput3  = null;
		mOutput4  = null;
		mDuration = 20;
		mEUt      = 32;
		mStartEU  = 0;
		
		checkCellBalance();
		
		if (mInput1 != null && mOutput1 != null && findEqualImplosionRecipeIndex(mInput1, mInput2) == -1) {
			pImplosionRecipes.put(GT_Utility.stacksToLong(mInput1, mInput2), sImplosionRecipes.size());
			sImplosionRecipes.add(this);
		}
	}
	
	public GT_Recipe(ItemStack aInput1, int aEUt, int aDuration, ItemStack aOutput1) {
		mInput1   = aInput1==null?null:aInput1.copy();
		mInput2   = null;
		mOutput1  = aOutput1==null?null:aOutput1.copy();
		mOutput2  = null;
		mOutput3  = null;
		mOutput4  = null;
		mDuration = aDuration;
		mEUt      = aEUt;
		mStartEU  = 0;
		
		checkCellBalance();
		
		if (mInput1 != null && mOutput1 != null && findEqualWiremillRecipeIndex(mInput1, mInput2) == -1) {
			pWiremillRecipes.put(GT_Utility.stacksToLong(mInput1, mInput2), sWiremillRecipes.size());
			sWiremillRecipes.add(this);
		}
	}

	public GT_Recipe(int aEUt, int aDuration, ItemStack aInput1, ItemStack aOutput1) {
		mInput1   = aInput1==null?null:aInput1.copy();
		mInput2   = null;
		mOutput1  = aOutput1==null?null:aOutput1.copy();
		mOutput2  = null;
		mOutput3  = null;
		mOutput4  = null;
		mDuration = aDuration;
		mEUt      = aEUt;
		mStartEU  = 0;
		
		checkCellBalance();
		
		if (mInput1 != null && mOutput1 != null && findEqualBenderRecipeIndex(mInput1, mInput2) == -1) {
			pBenderRecipes.put(GT_Utility.stacksToLong(mInput1, mInput2), sBenderRecipes.size());
			sBenderRecipes.add(this);
		}
	}
	
	public GT_Recipe(ItemStack aInput1, int aEUt, ItemStack aInput2, int aDuration, ItemStack aOutput1) {
		mInput1   = aInput1==null?null:aInput1.copy();
		mInput2   = aInput2==null?null:aInput2.copy();
		mOutput1  = aOutput1==null?null:aOutput1.copy();
		mOutput2  = null;
		mOutput3  = null;
		mOutput4  = null;
		mDuration = aDuration;
		mEUt      = aEUt;
		mStartEU  = 0;
		
		//checkCellBalance();
		
		if (mInput1 != null && mOutput1 != null && findEqualAssemblerRecipeIndex(mInput1, mInput2) == -1) {
			pAssemblerRecipes.put(GT_Utility.stacksToLong(mInput1, mInput2), sAssemblerRecipes.size());
			sAssemblerRecipes.add(this);
		}
	}
	
	public GT_Recipe(ItemStack aInput1, ItemStack aInput2, int aEUt, int aDuration, ItemStack aOutput1) {
		mInput1   = aInput1==null?null:aInput1.copy();
		mInput2   = aInput2==null?null:aInput2.copy();
		mOutput1  = aOutput1==null?null:aOutput1.copy();
		mOutput2  = null;
		mOutput3  = null;
		mOutput4  = null;
		mDuration = aDuration;
		mEUt      = aEUt;
		mStartEU  = 0;
		
		checkCellBalance();
		
		if (mInput1 != null && mOutput1 != null && findEqualAlloySmelterRecipeIndex(mInput1, mInput2) == -1) {
			pAlloySmelterRecipes.put(GT_Utility.stacksToLong(mInput1, mInput2), sAlloySmelterRecipes.size());
			sAlloySmelterRecipes.add(this);
		}
	}

	public GT_Recipe(ItemStack aInput1, int aEUt, ItemStack aInput2, int aDuration, ItemStack aOutput1, ItemStack aOutput2) {
		mInput1   = aInput1==null?null:aInput1.copy();
		mInput2   = aInput2==null?null:aInput2.copy();
		mOutput1  = aOutput1==null?null:aOutput1.copy();
		mOutput2  = aOutput2==null?null:aOutput2.copy();
		mOutput3  = null;
		mOutput4  = null;
		mDuration = aDuration;
		mEUt      = aEUt;
		mStartEU  = 0;
		
		checkCellBalance();
		
		if (mInput1 != null && mOutput1 != null && findEqualCannerRecipeIndex(mInput1, mInput2) == -1) {
			pCannerRecipes.put(GT_Utility.stacksToLong(mInput1, mInput2), sCannerRecipes.size());
			sCannerRecipes.add(this);
		}
	}
	
	public GT_Recipe(ItemStack aInput1, ItemStack aOutput1, int aDuration) {
		mInput1   = aInput1==null?null:aInput1.copy();
		mInput2   = null;
		mOutput1  = aOutput1==null?null:aOutput1.copy();
		mOutput2  = null;
		mOutput3  = null;
		mOutput4  = null;
		mDuration = aDuration;
		mEUt      = 128;
		mStartEU  = 0;
		
		//checkCellBalance();
		
		if (mInput1 != null && mOutput1 != null && findEqualVacuumRecipeIndex(mInput1, mInput2) == -1) {
			pVacuumRecipes.put(GT_Utility.stacksToLong(mInput1, mInput2), sVacuumRecipes.size());
			sVacuumRecipes.add(this);
		}
	}

	public GT_Recipe(ItemStack aInput1, ItemStack aInput2, ItemStack aOutput1, int aDuration) {
		mInput1   = aInput1==null?null:aInput1.copy();
		mInput2   = aInput2==null?null:aInput2.copy();
		mOutput1  = aOutput1==null?null:aOutput1.copy();
		mOutput2  = null;
		mOutput3  = null;
		mOutput4  = null;
		mDuration = aDuration;
		mEUt      = 32;
		mStartEU  = 0;
		
		checkCellBalance();
		
		if (mInput1 != null && mOutput1 != null && findEqualChemicalRecipeIndex(mInput1, mInput2) == -1) {
			pChemicalRecipes.put(GT_Utility.stacksToLong(mInput1, mInput2), sChemicalRecipes.size());
			sChemicalRecipes.add(this);
		}
	}
	
	public static int findEqualRecipeIndex(ItemStack aInput1, ItemStack aInput2, boolean aShapeless, ArrayList<GT_Recipe> aList, Map<Long, Integer> aHash) {
		int i = -1;
		if (aShapeless && (i = findEqualRecipeIndex(aInput2, aInput1, false, aList, aHash)) >= 0) {
			return i;
		}
		if (aInput1 == null) return -1;
		long k;
		if (aHash.containsKey(k = GT_Utility.stacksToLong(aInput1, aInput2)))
			i = aHash.get(k);
		else
			if (aHash.containsKey(k = GT_Utility.stacksToLong(aInput1, null)))
				i = aHash.get(k);
		
		if (i >= 0 && i < aList.size() && isRecipeInputEqual(aShapeless, false, aInput1, aInput2, aList.get(i))) return i;
		
		boolean temp = false;
		for (i = 0; i < aList.size(); i++) {
			if (isRecipeInputEqual(aShapeless, false, aInput1, aInput2, aList.get(i))) {
				temp = true;
				break;
			}
		}
		
		if (temp) {
			if (GregTech_API.DEBUG_MODE && aList.get(i).mInput1.getItemDamage() >= 0 && (aList.get(i).mInput2 == null || aList.get(i).mInput2.getItemDamage() >= 0)) GT_Log.err.println("Didn't find Recipe via Hashcode, did another Mod attempt to remove a Recipe improperly? Hash = " + k + " / " + aInput1.getDisplayName() + " / " + (aInput2==null?"NULL":aInput2.getDisplayName()));
			return i;
		}
		return -1;
	}
	
	public static int findEqualWiremillRecipeIndex(ItemStack aInput1, ItemStack aInput2) {
		return findEqualRecipeIndex(aInput1, aInput2, false, sWiremillRecipes, pWiremillRecipes);
	}
	
	public static int findEqualBenderRecipeIndex(ItemStack aInput1, ItemStack aInput2) {
		return findEqualRecipeIndex(aInput1, aInput2, false, sBenderRecipes, pBenderRecipes);
	}

	public static int findEqualAssemblerRecipeIndex(ItemStack aInput1, ItemStack aInput2) {
		return findEqualRecipeIndex(aInput1, aInput2, true, sAssemblerRecipes, pAssemblerRecipes);
	}
	
	public static int findEqualAlloySmelterRecipeIndex(ItemStack aInput1, ItemStack aInput2) {
		return findEqualRecipeIndex(aInput1, aInput2, true, sAlloySmelterRecipes, pAlloySmelterRecipes);
	}

	public static int findEqualCannerRecipeIndex(ItemStack aInput1, ItemStack aInput2) {
		return findEqualRecipeIndex(aInput1, aInput2, true, sCannerRecipes, pCannerRecipes);
	}
	
	public static int findEqualDistillationRecipeIndex(ItemStack aInput1, ItemStack aInput2) {
		return findEqualRecipeIndex(aInput1, aInput2, false, sDistillationRecipes, pDistillationRecipes);
	}
	
	public static int findEqualFusionRecipeIndex(ItemStack aInput1, ItemStack aInput2) {
		return findEqualRecipeIndex(aInput1, aInput2, true, sFusionRecipes, pFusionRecipes);
	}

	public static int findEqualCentrifugeRecipeIndex(ItemStack aInput1, ItemStack aInput2) {
		return findEqualRecipeIndex(aInput1, aInput2, false, sCentrifugeRecipes, pCentrifugeRecipes);
	}

	public static int findEqualElectrolyzerRecipeIndex(ItemStack aInput1, ItemStack aInput2) {
		return findEqualRecipeIndex(aInput1, aInput2, false, sElectrolyzerRecipes, pElectrolyzerRecipes);
	}

	public static int findEqualSawmillRecipeIndex(ItemStack aInput1, ItemStack aInput2) {
		return findEqualRecipeIndex(aInput1, aInput2, false, sSawmillRecipes, pSawmillRecipes);
	}
	
	public static int findEqualGrinderRecipeIndex(ItemStack aInput1, ItemStack aInput2) {
		return findEqualRecipeIndex(aInput1, aInput2, false, sGrinderRecipes, pGrinderRecipes);
	}
	
	public static int findEqualBlastRecipeIndex(ItemStack aInput1, ItemStack aInput2) {
		return findEqualRecipeIndex(aInput1, aInput2, true, sBlastRecipes, pBlastRecipes);
	}

	public static int findEqualImplosionRecipeIndex(ItemStack aInput1, ItemStack aInput2) {
		return findEqualRecipeIndex(aInput1, aInput2, false, sImplosionRecipes, pImplosionRecipes);
	}

	public static int findEqualVacuumRecipeIndex(ItemStack aInput1, ItemStack aInput2) {
		return findEqualRecipeIndex(aInput1, aInput2, false, sVacuumRecipes, pVacuumRecipes);
	}
	
	public static int findEqualChemicalRecipeIndex(ItemStack aInput1, ItemStack aInput2) {
		return findEqualRecipeIndex(aInput1, aInput2, true, sChemicalRecipes, pChemicalRecipes);
	}

	public static int findEqualLatheRecipeIndex(ItemStack aInput1, ItemStack aInput2) {
		return findEqualRecipeIndex(aInput1, aInput2, true, sLatheRecipes, pLatheRecipes);
	}
	
	public static int findEqualCutterRecipeIndex(ItemStack aInput1, ItemStack aInput2) {
		return findEqualRecipeIndex(aInput1, aInput2, true, sCutterRecipes, pCutterRecipes);
	}
	
	public static boolean isRecipeInputEqual(boolean aShapeless, boolean aDecreaseStacksizeBySuccess, ItemStack aInput1, ItemStack aInput2, GT_Recipe aRecipe) {
		if (aRecipe == null) return false;
		if (aShapeless) if (isRecipeInputEqual(false, aDecreaseStacksizeBySuccess, aInput2, aInput1, aRecipe)) return true;
		if (aInput1 == null)
			return false;
		else {
			if (aInput1.getItem() == aRecipe.mInput1.getItem() && (aInput1.getItemDamage() == aRecipe.mInput1.getItemDamage() || aRecipe.mInput1.getItemDamage() == GregTech_API.ITEM_WILDCARD_DAMAGE) && aInput1.stackSize >= aRecipe.mInput1.stackSize) {
				if (aRecipe.mInput2 != null && (aInput2 == null || !(aInput2.getItem() == aRecipe.mInput2.getItem() && (aInput2.getItemDamage() == aRecipe.mInput2.getItemDamage() || aRecipe.mInput2.getItemDamage() == GregTech_API.ITEM_WILDCARD_DAMAGE)) || aInput2.stackSize < aRecipe.mInput2.stackSize)) return false;
				if (aDecreaseStacksizeBySuccess) {
					aInput1.stackSize -= aRecipe.mInput1.stackSize;
					if (aRecipe.mInput2 != null) aInput2.stackSize -= aRecipe.mInput2.stackSize;
				}
				return true;
			}
		}
		return false;
	}
	
	public void checkCellBalance() {
		try {
			if (mInput1 == null) return;
			int tInputAmount = GT_ModHandler.getCapsuleCellContainerCountMultipliedWithStackSize(mInput1) + GT_ModHandler.getCapsuleCellContainerCountMultipliedWithStackSize(mInput2);
			int tOutputAmount = GT_ModHandler.getCapsuleCellContainerCountMultipliedWithStackSize(mOutput1) + GT_ModHandler.getCapsuleCellContainerCountMultipliedWithStackSize(mOutput2) + GT_ModHandler.getCapsuleCellContainerCountMultipliedWithStackSize(mOutput3) + GT_ModHandler.getCapsuleCellContainerCountMultipliedWithStackSize(mOutput4);
			
			if (tInputAmount < tOutputAmount) {
				if (!GT_OreDictUnificator.isItemStackInstanceOf(mInput1, "ingotTin", false) && !GT_OreDictUnificator.isItemStackInstanceOf(mInput2, "ingotTin", false)) GT_Log.err.println("You get more Cells, than you put in? There must be something wrong. " + mInput1.getDisplayName() + " / " + (mInput2==null?"NULL":mInput2.getDisplayName()));
			} else if (tInputAmount > tOutputAmount && !mInput1.isItemEqual(GT_ModHandler.getLavaCell(1))) {
				if (!GT_OreDictUnificator.isItemStackInstanceOf(mOutput1, "ingotTin", false) && !GT_OreDictUnificator.isItemStackInstanceOf(mOutput2, "ingotTin", false) && !GT_OreDictUnificator.isItemStackInstanceOf(mOutput3, "ingotTin", false) && !GT_OreDictUnificator.isItemStackInstanceOf(mOutput4, "ingotTin", false)) GT_Log.err.println("You get less Cells, than you put in? My Machines usually don't destroy Cells. " + mInput1.getDisplayName() + " / " + (mInput2==null?"NULL":mInput2.getDisplayName()));
			}
		} catch(Throwable e) {
			e.printStackTrace(GT_Log.err);
		}
	}
}