package gregtechmod.api.util;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.StatCollector;
import net.minecraftforge.common.Configuration;
import net.minecraftforge.common.Property;
import cpw.mods.fml.common.registry.LanguageRegistry;

public class GT_LanguageManager {
	public static volatile int VERSION = 407;
	
    public static Configuration sEnglishFile;
    
	public static String addStringLocalization(String aKey, String aEnglish) {
		return addStringLocalization(aKey, aEnglish, true);
	}
	
	public static String addStringLocalization(String aKey, String aEnglish, boolean aWriteIntoLangFile) {
		if (aWriteIntoLangFile) {
			Property tProperty = sEnglishFile.get("LanguageFile", aKey.trim(), aEnglish);
			if (!tProperty.wasRead()) sEnglishFile.save();
			if (sEnglishFile.get("EnableLangFile", "UseThisFileAsLanguageFile", false).getBoolean(false)) aEnglish = tProperty.getString();
		}
		LanguageRegistry.instance().addStringLocalization(aKey.trim(), aEnglish);
		return aEnglish;
	}
	
	public static String getTranslation(String aKey) {
		String tTrimmedKey = aKey.trim(), rTranslation = LanguageRegistry.instance().getStringLocalization(tTrimmedKey);
		if (GT_Utility.isStringInvalid(rTranslation)) {
			rTranslation = StatCollector.translateToLocal(tTrimmedKey);
			if (GT_Utility.isStringInvalid(rTranslation) || tTrimmedKey.equals(rTranslation)) {
				if (aKey.endsWith(".name")) {
					rTranslation = StatCollector.translateToLocal(tTrimmedKey.substring(0, tTrimmedKey.length() - 5));
					if (GT_Utility.isStringInvalid(rTranslation) || tTrimmedKey.substring(0, tTrimmedKey.length() - 5).equals(rTranslation)) {
						return aKey;
					}
				} else {
					rTranslation = StatCollector.translateToLocal(tTrimmedKey + ".name");
					if (GT_Utility.isStringInvalid(rTranslation) || (tTrimmedKey + ".name").equals(rTranslation)) {
						return aKey;
					}
				}
			}
		}
		return rTranslation;
	}
	
	public static String getTranslation(String aKey, String aSeperator) {
		String rTranslation = "";
		for (String tString : aKey.split(aSeperator)) {
			rTranslation += getTranslation(tString);
		}
		return rTranslation;
	}
	
	public static String getTranslateableItemStackName(ItemStack aStack) {
		if (GT_Utility.isStackInvalid(aStack)) return "null";
		NBTTagCompound tNBT = aStack.getTagCompound();
		if (tNBT != null && tNBT.hasKey("display")) {
			String tName = tNBT.getCompoundTag("display").getString("Name");
			if (GT_Utility.isStringValid(tName)) {
				return tName;
			}
		}
		return aStack.getUnlocalizedName() + ".name";
	}
}