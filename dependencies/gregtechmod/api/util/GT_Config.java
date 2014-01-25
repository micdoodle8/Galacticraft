package gregtechmod.api.util;

import net.minecraft.item.ItemStack;
import net.minecraftforge.common.Configuration;
import net.minecraftforge.common.Property;

public class GT_Config {
	public static volatile int VERSION = 407;
	
	public static boolean system = false;
	
	public static Configuration sConfigFileIDs;
	
	public static int addIDConfig(Object aCategory, String aName, int aDefault) {
		if (GT_Utility.isStringInvalid(aName)) return aDefault;
		Property tProperty = sConfigFileIDs.get(aCategory.toString().replaceAll("\\|", "."), aName.replaceAll("\\|", "."), aDefault);
		int rResult = tProperty.getInt(aDefault);
		if (!tProperty.wasRead()) sConfigFileIDs.save();
		return rResult;
	}
	
	public final Configuration mConfig;
	
	public GT_Config(Configuration aConfig) {
		mConfig = aConfig;
		mConfig.load();
		mConfig.save();
	}
	
	public static String getStackConfigName(ItemStack aStack) {
		if (GT_Utility.isStackInvalid(aStack)) return "";
		String rName;
		if (GT_Utility.isStringValid(rName = GT_OreDictUnificator.getAssociation(aStack))) return rName;
		try {if (GT_Utility.isStringValid(rName = aStack.getUnlocalizedName())) return rName;} catch (Throwable e) {/*Do nothing*/}
		return aStack.getItem() + "." + aStack.getItemDamage();
	}
	
	
	public boolean add(Object aCategory, ItemStack aStack, boolean aDefault) {
		return add(aCategory, getStackConfigName(aStack), aDefault);
	}
	
	public boolean add(Object aCategory, String aName, boolean aDefault) {
		if (GT_Utility.isStringInvalid(aName)) return aDefault;
		Property tProperty = mConfig.get(aCategory.toString().replaceAll("\\|", "."), (aName+"_"+aDefault).replaceAll("\\|", "."), aDefault);
		boolean rResult = tProperty.getBoolean(aDefault);
		if (!tProperty.wasRead()) mConfig.save();
		return rResult;
	}
	
	public int add(Object aCategory, ItemStack aStack, int aDefault) {
		return add(aCategory, getStackConfigName(aStack), aDefault);
	}
	
	public int add(Object aCategory, String aName, int aDefault) {
		if (GT_Utility.isStringInvalid(aName)) return aDefault;
		Property tProperty = mConfig.get(aCategory.toString().replaceAll("\\|", "."), (aName+"_"+aDefault).replaceAll("\\|", "."), aDefault);
		int rResult = tProperty.getInt(aDefault);
		if (!tProperty.wasRead()) mConfig.save();
		return rResult;
	}
	
	public double add(Object aCategory, ItemStack aStack, double aDefault) {
		return add(aCategory, getStackConfigName(aStack), aDefault);
	}
	
	public double add(Object aCategory, String aName, double aDefault) {
		if (GT_Utility.isStringInvalid(aName)) return aDefault;
		Property tProperty = mConfig.get(aCategory.toString().replaceAll("\\|", "."), (aName+"_"+aDefault).replaceAll("\\|", "."), aDefault);
		double rResult = tProperty.getDouble(aDefault);
		if (!tProperty.wasRead()) mConfig.save();
		return rResult;
	}
}