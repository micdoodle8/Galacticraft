package gregtechmod.api.items;

import gregtechmod.api.GregTech_API;
import gregtechmod.api.enums.Materials;
import gregtechmod.api.enums.OrePrefixes;
import gregtechmod.api.interfaces.IIconContainer;
import gregtechmod.api.util.GT_Config;
import gregtechmod.api.util.GT_LanguageManager;
import gregtechmod.api.util.GT_OreDictUnificator;
import gregtechmod.api.util.GT_Utility;

import java.util.Arrays;
import java.util.List;

import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Icon;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

/**
 * @author Gregorius Techneticies
 * 
 * This brilliant Item Class is used for automatically generating all possible variations of Material Items, like Dusts, Ingots, Gems, Plates and similar.
 * It saves me a ton of work, when adding Items, because I always have to make a new Item Subtype for each OreDict Prefix, when adding a new Material.
 * 
 *  As you can see, up to 32766 Items can be generated using this Class. And the last 766 Items can be customly defined, just to save space and MetaData.
 */
public abstract class GT_MetaGenerated_Item extends GT_Generic_Item {
	
	/* ---------- CONSTRUCTOR AND MEMBER VARIABLES ---------- */
	
	private boolean[] mEnabledItems = new boolean[766];
	private Icon[] mIconList = new Icon[mEnabledItems.length];
	private final OrePrefixes[] mGeneratedPrefixList;
	
	/**
	 * Creates the Item using these Parameters.
	 * @param aID the Item ID.
	 * @param aUnlocalized The Unlocalized Name of this Item.
	 * @param aGeneratedPrefixList The OreDict Prefixes you want to have generated.
	 */
	protected GT_MetaGenerated_Item(int aID, String aUnlocalized, OrePrefixes... aGeneratedPrefixList) {
		super(aID, aUnlocalized, "Generated Item", null, false);
		mGeneratedPrefixList = Arrays.copyOf(aGeneratedPrefixList, 32);
		setCreativeTab(GregTech_API.TAB_GREGTECH_MATERIALS);
        setHasSubtypes(true);
        setMaxDamage(0);
        
        for (int i = 0; i < 32000; i++) {
			OrePrefixes tPrefix = mGeneratedPrefixList[i / 1000];
			if (tPrefix == null) continue;
			Materials tMaterial = GregTech_API.sGeneratedMaterials[i % 1000];
			if (tMaterial == null) continue;
			if (!doesMaterialAllowGeneration(tPrefix, tMaterial)) continue;
			if (tPrefix.mIsUnificatable) GT_OreDictUnificator.add(tPrefix, tMaterial, new ItemStack(this, 1, i)); else GT_OreDictUnificator.registerOre(tPrefix, tMaterial, new ItemStack(this, 1, i));
			GT_LanguageManager.addStringLocalization(getUnlocalizedName() + "." + i, getDefaultLocalization(tPrefix, tMaterial, i));
			GT_LanguageManager.addStringLocalization(getUnlocalizedName() + "." + i + ".tooltip", tMaterial.getToolTip(tPrefix.mMaterialAmount / GregTech_API.MATERIAL_UNIT));
		}
	}
	
	/* ---------- IMPLEMENTABLE FUNCTIONS ---------- */
	
	/**
	 * @param aPrefix the OreDict Prefix
	 * @param aMaterial the Material
	 * @param aMetaData a Index from [0 - 31999]
	 * @return the Localized Name when default LangFiles are used.
	 */
	protected String getDefaultLocalization(OrePrefixes aPrefix, Materials aMaterial, int aMetaData) {
		return aPrefix.mLocalizedMaterialPre + aMaterial.mDefaultLocalName + aPrefix.mLocalizedMaterialPost;
	}
	
	/**
	 * @param aMetaData a Index from [0 - 31999]
	 * @param aMaterial the Material
	 * @return an Icon Container for the Item Display.
	 */
	protected abstract IIconContainer getIcon(int aMetaData, Materials aMaterial);
	
	/**
	 * @return if this Item should be generated and visible.
	 */
	protected abstract boolean doesMaterialAllowGeneration(OrePrefixes aPrefix, Materials aMaterial);
	
	/* ---------- FOR ADDING CUSTOM ITEMS INTO THE REMAINING 766 RANGE ---------- */
	
	/**
	 * This adds a Custom Item to the ending Range.
	 * @param aID The Id of the assigned Item [0 - 765] (The MetaData gets auto-shifted +30000)
	 * @param aEnglish The Default Localized Name of the created Item
	 * @param aToolTip The Default ToolTip of the created Item
	 * @param aOreDictNames The OreDict Names you want to give the Item.
	 * @return An ItemStack containing the generated Item.
	 */
	public final ItemStack addItem(int aID, String aEnglish, String aToolTip, Object... aOreDictNames) {
		if (aToolTip == null) aToolTip = "";
		if (aID >= 0 && aID < mEnabledItems.length) {
			mEnabledItems[aID] = true;
			GT_LanguageManager.addStringLocalization(getUnlocalizedName() + "." + (32000+aID), aEnglish);
			GT_LanguageManager.addStringLocalization(getUnlocalizedName() + "." + (32000+aID) + ".tooltip", aToolTip);
			ItemStack rStack = new ItemStack(this, 1, 32000+aID);
			for (Object tOreDictName : aOreDictNames) GT_OreDictUnificator.registerOre(tOreDictName, rStack);
			return rStack;
		}
		return null;
	}
	
	/* ---------- INTERNAL OVERRIDES ---------- */
	
	@Override
    @SideOnly(Side.CLIENT)
    public final void getSubItems(int var1, CreativeTabs aCreativeTab, List aList) {
        for (int i = 0; i < 32000; i++) {
			if (!doesMaterialAllowGeneration(mGeneratedPrefixList[i / 1000], GregTech_API.sGeneratedMaterials[i % 1000])) continue;
			aList.add(new ItemStack(this, 1, i));
        }
        for (int i = 0; i < mEnabledItems.length; i++) if (mEnabledItems[i]) aList.add(new ItemStack(this, 1, i));
    }
	
	@Override
    public final String getUnlocalizedName(ItemStack aStack) {
    	return getUnlocalizedName() + "." + aStack.getItemDamage();
    }
	
	@Override
    @SideOnly(Side.CLIENT)
    public final void registerIcons(IconRegister aIconRegister) {
    	for (int i = 0; i < mEnabledItems.length; i++) if (mEnabledItems[i]) {
    		mIconList[i] = aIconRegister.registerIcon(GregTech_API.TEXTURE_PATH_ITEM + (GT_Config.system?"troll":getUnlocalizedName() + "/" + i));
    	}
    }
	
	@Override
    public final Icon getIconFromDamage(int aMetaData) {
		if (aMetaData < 0) return null;
		if (aMetaData < 32000) {
			Materials tMaterial = GregTech_API.sGeneratedMaterials[aMetaData % 1000];
			if (tMaterial == null) return null;
			IIconContainer tIcon = getIcon(aMetaData, tMaterial);
			if (tIcon != null) return tIcon.getIcon();
			return null;
		}
		return aMetaData-32000<mIconList.length?mIconList[aMetaData-32000]:null;
    }
	
	@Override
    public final boolean hasEffect(ItemStack aStack, int aPass) {
		if (super.hasEffect(aStack, aPass)) return true;
		int aMetaData = aStack.getItemDamage();
		if (aMetaData < 0) return false;
		if (aMetaData < 32000) {
			Materials tMaterial = GregTech_API.sGeneratedMaterials[aMetaData % 1000];
			if (tMaterial == null) return false;
			return tMaterial.isRadioactive();
		}
        return false;
    }
	
	@Override
    public final void addInformation(ItemStack aStack, EntityPlayer aPlayer, List aList, boolean aF3_H) {
		String tKey = getUnlocalizedName() + "." + aStack.getItemDamage() + ".tooltip", tString = GT_LanguageManager.getTranslation(tKey);
		if (GT_Utility.isStringValid(tString) && !tKey.equals(tString)) aList.add(tString);
	}
}
