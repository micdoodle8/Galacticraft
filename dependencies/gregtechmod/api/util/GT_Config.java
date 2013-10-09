package gregtechmod.api.util;

import net.minecraft.item.ItemStack;
import net.minecraftforge.common.Configuration;
import net.minecraftforge.common.Property;

public class GT_Config {
	public static volatile int VERSION = 402;
	
	public static boolean system = false;
	
	public static Configuration sConfigFileStandard, sConfigFileIDs, sConfigFileAdvRecipes;
	
	public GT_Config(Configuration aConfigFileStandard, Configuration aConfigFileIDs, Configuration aConfigFileAdvRecipes) {
		sConfigFileAdvRecipes = aConfigFileAdvRecipes;
		sConfigFileStandard = aConfigFileStandard;
		sConfigFileIDs = aConfigFileIDs;
	}
	
	public String getStackConfigName(ItemStack aStack) {
		if (aStack == null) return null;
		String rName;
		if ((rName = GT_OreDictUnificator.getAssociation(aStack)) != null && !rName.equals("")) return rName;
		try {if ((rName = aStack.getDisplayName()) != null && !rName.equals("")) return rName;} catch (Throwable e) {}
		return aStack.itemID + "." + aStack.getItemDamage();
	}
	
	public int addIDConfig(String aCategory, String aName, int aDefault) {
		if (aName == null) return aDefault;
		Property tProperty = sConfigFileIDs.get(aCategory, aName, aDefault);
		int rResult = tProperty.getInt(aDefault);
		if (!tProperty.wasRead()) sConfigFileIDs.save();
		return rResult;
	}
	
	public boolean addAdvConfig(String aCategory, ItemStack aStack, boolean aDefault) {
		return addAdvConfig(aCategory, getStackConfigName(aStack), aDefault);
	}
	
	public boolean addAdvConfig(String aCategory, String aName, boolean aDefault) {
		if (aName == null) return aDefault;
		Property tProperty = sConfigFileAdvRecipes.get(aCategory, aName+"_"+aDefault, aDefault);
		boolean rResult = tProperty.getBoolean(aDefault);
		if (!tProperty.wasRead()) sConfigFileAdvRecipes.save();
		return rResult;
	}
	
	public int addAdvConfig(String aCategory, ItemStack aStack, int aDefault) {
		return addAdvConfig(aCategory, getStackConfigName(aStack), aDefault);
	}
	
	public int addAdvConfig(String aCategory, String aName, int aDefault) {
		if (aName == null) return aDefault;
		Property tProperty = sConfigFileAdvRecipes.get(aCategory, aName+"_"+aDefault, aDefault);
		int rResult = tProperty.getInt(aDefault);
		if (!tProperty.wasRead()) sConfigFileAdvRecipes.save();
		return rResult;
	}
}
