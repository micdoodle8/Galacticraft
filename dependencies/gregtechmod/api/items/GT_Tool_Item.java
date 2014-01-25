package gregtechmod.api.items;

import gregtechmod.api.GregTech_API;
import gregtechmod.api.util.GT_ModHandler;
import gregtechmod.api.util.GT_OreDictUnificator;
import gregtechmod.api.util.GT_Utility;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;

import com.google.common.collect.Multimap;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

/**
 * This is just a basic Tool, which has normal durability and could break Blocks.
 */
public class GT_Tool_Item extends GT_Generic_Item {
	public final List<String> mEffectiveAgainstList = new ArrayList<String>();
	public final List<Block> mEffectiveBlocksList = new ArrayList<Block>();
	public final List<String> mEffectiveOreDictList = new ArrayList<String>();
	public final List<Material> mEffectiveMaterialsList = new ArrayList<Material>();
	
	protected final int mEntityDamage, mChargedGTID, mDisChargedGTID, mToolQuality;
	protected final float mToolStrength;
	
	private short mDamagePerContainerCraft = 10, mDamagePerWeaponUse = 3, mDamagePerBlockBreak = 1;
	private int mTier = 1, mEUperBrokenBlock = -1, mEUperHitEntity = -1, mSilklevel = 0, mFortunelevel = 0, mLootinglevel = 0;
	private String mToolClasses[], mCraftingSound, mBreakingSound, mBlockBreakSound, mEntityHitSound;
	private float mCraftingSoundStrength = 1, mBreakingSoundStrength = 1, mBlockBreakSoundStrength = 1, mEntityHitSoundStrength = 1;
	private final boolean mSwingIfUsed;
	
	public GT_Tool_Item(int aID, String aUnlocalized, String aEnglish, String aTooltip, int aMaxDamage, int aEntityDamage, boolean aSwingIfUsed) {
		this(aID, aUnlocalized, aEnglish, aTooltip, aMaxDamage, aEntityDamage, aSwingIfUsed, -1, -1);
	}
	
	public GT_Tool_Item(int aID, String aUnlocalized, String aEnglish, String aTooltip, int aMaxDamage, int aEntityDamage, boolean aSwingIfUsed, int aChargedGTID, int aDisChargedGTID) {
		this(aID, aUnlocalized, aEnglish, aTooltip, aMaxDamage, aEntityDamage, aSwingIfUsed, aChargedGTID, aDisChargedGTID, 0, 0.0F);
	}
	
	public GT_Tool_Item(int aID, String aUnlocalized, String aEnglish, String aTooltip, int aMaxDamage, int aEntityDamage, boolean aSwingIfUsed, int aChargedGTID, int aDisChargedGTID, int aToolQuality, float aToolStrength) {
		super(aID, aUnlocalized, aEnglish, aTooltip, aTooltip != null && !aTooltip.equals("Doesn't work as intended, this is a Bug"));
		mEntityDamage = aEntityDamage;
		mDisChargedGTID = aDisChargedGTID;
		mChargedGTID = aChargedGTID;
		mToolQuality = Math.max(aToolQuality, 0);
		mToolStrength = aToolStrength;
		mSwingIfUsed = aSwingIfUsed;
		setMaxDamage(aMaxDamage);
		setMaxStackSize(1);
		setNoRepair();
		setFull3D();
		GT_ModHandler.registerBoxableItemToToolBox(this);
	}
	
	public final GT_Tool_Item addToEffectiveList(String aEntityClassName) {
		mEffectiveAgainstList.add(aEntityClassName.substring(aEntityClassName.lastIndexOf(".")+1));
		return this;
	}
	
	public final GT_Tool_Item addToOreDictList(String aOreName) {
		mEffectiveOreDictList.add(aOreName);
		return this;
	}
	
	public final GT_Tool_Item addToBlockList(ItemStack aBlock) {
		return addToBlockList(GT_Utility.getBlockFromStack(aBlock));
	}
	
	public final GT_Tool_Item addToBlockList(Block aBlock) {
		if (GT_Utility.isBlockValid(aBlock)) mEffectiveBlocksList.add(aBlock);
		return this;
	}
	
	public final GT_Tool_Item addToMaterialList(Material aMaterial) {
		if (aMaterial != null) mEffectiveMaterialsList.add(aMaterial);
		return this;
	}
	
	public final GT_Tool_Item setElectricTier(int aTier) {
		mTier = Math.max(1, aTier);
		return this;
	}

	public final GT_Tool_Item setElectricConsumptionPerBrokenBlock(int aEU) {
		mEUperBrokenBlock = Math.max(0, aEU);
		return this;
	}
	
	public final GT_Tool_Item setElectricConsumptionPerHitEntity(int aEU) {
		mEUperHitEntity = Math.max(0, aEU);
		return this;
	}
	
	public final GT_Tool_Item setToolClasses(String... aClasses) {
		mToolClasses = aClasses;
		return this;
	}
	
	public final GT_Tool_Item setCraftingSound(String aSound) {
		mCraftingSound = aSound;
		return this;
	}
	
	public final GT_Tool_Item setBreakingSound(String aSound) {
		mBreakingSound = aSound;
		return this;
	}
	
	public final GT_Tool_Item setBlockBreakSound(String aSound) {
		mBlockBreakSound = aSound;
		return this;
	}
	
	public final GT_Tool_Item setEntityHitSound(String aSound) {
		mEntityHitSound = aSound;
		return this;
	}
	
	public final GT_Tool_Item setUsageAmounts(int aDamagePerContainerCraft, int aDamagePerWeaponUse, int aDamagePerBlockBreak) {
		mDamagePerContainerCraft = (short)aDamagePerContainerCraft;
		mDamagePerWeaponUse = (short)aDamagePerWeaponUse;
		mDamagePerBlockBreak = (short)aDamagePerBlockBreak;
		return this;
	}
	
	public final GT_Tool_Item setSilkyness(int aLevel) {
		mSilklevel = aLevel;
		return this;
	}

	public final GT_Tool_Item setLuckyness(int aLevel) {
		mFortunelevel = aLevel;
		return this;
	}
	
	public final GT_Tool_Item setLootyness(int aLevel) {
		mLootinglevel = aLevel;
		return this;
	}
	
	public final GT_Tool_Item registerAtOreDict(Object aName) {
		GT_OreDictUnificator.registerOre(aName, new ItemStack(this, 1, GregTech_API.ITEM_WILDCARD_DAMAGE));
		return this;
	}
	
	public void setMode(ItemStack aStack, int aMode) {
        NBTTagCompound tNBT = aStack.getTagCompound();
        if (tNBT == null) {
            tNBT = new NBTTagCompound();
            aStack.setTagCompound(tNBT);
        }
		checkEnchantmentEffects(aStack);
        tNBT.setInteger("mMode", aMode);
	}
	
	public int getMode(ItemStack aStack) {
		if (aStack == null) return 0;
        NBTTagCompound tNBT = aStack.getTagCompound();
        if (tNBT == null) {
            tNBT = new NBTTagCompound();
            aStack.setTagCompound(tNBT);
        }
		checkEnchantmentEffects(aStack);
        return tNBT.getInteger("mMode");
	}
	
	public final short getDamagePerContainerItemCraft() {
		return mDamagePerContainerCraft;
	}
	
	public final short getDamagePerWeaponUse() {
		return mDamagePerWeaponUse;
	}
	
	public final short getDamagePerBlockBreak() {
		return mDamagePerBlockBreak;
	}
	
	public void onHitEntity(Entity aEntity) {
		//
	}
	
	public void checkEnchantmentEffects(ItemStack aStack) {
		if (aStack != null) {
			if (aStack.isItemEnchanted()) aStack.stackTagCompound.getTags().remove(aStack.stackTagCompound.getTag("ench"));
			if (!GT_ModHandler.isElectricItem(aStack) || GT_ModHandler.canUseElectricItem(aStack, mEUperBrokenBlock < 0 ? getDamagePerBlockBreak() * 1000 : mEUperBrokenBlock)) {
				if (mSilklevel		> 0) aStack.addEnchantment(Enchantment.silkTouch	, mSilklevel);
				if (mFortunelevel	> 0) aStack.addEnchantment(Enchantment.fortune		, mFortunelevel);
				if (mLootinglevel	> 0) aStack.addEnchantment(Enchantment.looting		, mLootinglevel);
			}
		}
	}
	
	public ItemStack getEmptyItem(ItemStack aStack) {
		return null;
	}
	
	@Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IconRegister aIconRegister) {
		super.registerIcons(aIconRegister);
		if (mChargedGTID >= 0)		((GT_Generic_Item)GregTech_API.sItemList[mChargedGTID])		.mIcon = mIcon;
		if (mDisChargedGTID >= 0)	((GT_Generic_Item)GregTech_API.sItemList[mDisChargedGTID])	.mIcon = mIcon;
    }
	
	@Override
    public ItemStack onItemRightClick(ItemStack aStack, World aWorld, EntityPlayer aPlayer) {
    	GT_ModHandler.useElectricItem(aStack, 0, aPlayer);
		checkEnchantmentEffects(aStack);
        return aStack;
    }
    
	@Override
	public boolean onItemUseFirst(ItemStack aStack, EntityPlayer aPlayer, World aWorld, int aX, int aY, int aZ, int aSide, float hitX, float hitY, float hitZ) {
		if (mSwingIfUsed) aPlayer.swingItem();
		GT_ModHandler.useElectricItem(aStack, 0, aPlayer);
		checkEnchantmentEffects(aStack);
		return false;
	}
	
	@Override
    @SideOnly(Side.CLIENT)
    public void getSubItems(int var1, CreativeTabs var2, List aList) {
		if (GT_ModHandler.isElectricItem(new ItemStack(this, 1))) {
	        ItemStack tCharged = new ItemStack(this, 1);
	        GT_ModHandler.chargeElectricItem(tCharged, Integer.MAX_VALUE, Integer.MAX_VALUE, true, false);
	        aList.add(tCharged);
	        return;
		}
		aList.add(new ItemStack(this, 1));
    }
	
	@Override
    public int getItemEnchantability() {
        return 0;
    }
	
	@Override
    public boolean isBookEnchantable(ItemStack aStack, ItemStack aBook) {
        return false;
    }
	
	@Override
    public boolean getIsRepairable(ItemStack aStack, ItemStack aMaterial) {
        return false;
    }
	
	@Override
    public final ItemStack getContainerItemStack(ItemStack aStack) {
		checkEnchantmentEffects(aStack);
		aStack = GT_Utility.copy(aStack);
		if (aStack.getItemDamage() > aStack.getMaxDamage()) {
			GT_Utility.doSoundAtClient(mBreakingSound, 1, mBreakingSoundStrength);
			ItemStack tStack = getEmptyItem(aStack);
			if (tStack != null) return tStack;
		} else {
			GT_Utility.doSoundAtClient(mCraftingSound, 1, mCraftingSoundStrength);
			GT_ModHandler.damageOrDechargeItem(aStack, getDamagePerContainerItemCraft(), getDamagePerContainerItemCraft()*1000, null);
		}
		checkEnchantmentEffects(aStack);
		return aStack;
    }
	
	@Override
    public boolean hasContainerItem() {
        return true;
    }
	
	@Override
    public boolean hasEffect(ItemStack aStack) {
		checkEnchantmentEffects(aStack);
        return false;
    }
	
	@Override
    public final boolean hitEntity(ItemStack aStack, EntityLivingBase aEntity, EntityLivingBase aPlayer) {
		GT_Utility.sendSoundToPlayers(aEntity.worldObj, mEntityHitSound, mEntityHitSoundStrength, -1, (int)aEntity.posX, (int)aEntity.posY, (int)aEntity.posZ);
		checkEnchantmentEffects(aStack);
		if (mEntityDamage > 1) {
			GT_ModHandler.damageOrDechargeItem(aStack, getDamagePerWeaponUse(), mEUperHitEntity < 0 ? getDamagePerWeaponUse() * 1000 : mEUperHitEntity, aPlayer);
			onHitEntity(aEntity);
			checkEnchantmentEffects(aStack);
			return true;
		}
        return false;
    }
	
	@Override
    public Multimap getItemAttributeModifiers() {
        Multimap multimap = super.getItemAttributeModifiers();
        multimap.put(SharedMonsterAttributes.attackDamage.getAttributeUnlocalizedName(), new AttributeModifier(field_111210_e, "Weapon modifier", mEntityDamage>0?mEntityDamage:1, 0));
        return multimap;
    }
	
	@Override
    public final float getDamageVsEntity(Entity aAttackedEntity, ItemStack aStack) {
		checkEnchantmentEffects(aStack);
		int tDamage = mEntityDamage;
		if (mEffectiveAgainstList.contains(GT_Utility.getClassName(aAttackedEntity))) tDamage *= 2;
		if (tDamage > 1) {
			if (GT_ModHandler.isElectricItem(aStack)) {
				if (GT_ModHandler.canUseElectricItem(aStack, mEUperHitEntity < 0 ? getDamagePerWeaponUse() * 1000 : mEUperHitEntity)) return tDamage;
				return 1;
			}
			return tDamage;
		}
        return 1;
    }
	
	private final boolean isEffectiveAgainst(Block aBlock, int aMeta) {
		if (mToolStrength > 1 && GT_Utility.isBlockValid(aBlock)) {
			if (mToolClasses != null) for (String tToolClass : mToolClasses) {
				int tHarvestLevel = MinecraftForge.getBlockHarvestLevel(aBlock, aMeta==-1?0:aMeta, tToolClass);
				if (tHarvestLevel >= 0 && tHarvestLevel <= mToolQuality) return true;
			}
			if (mEffectiveMaterialsList.contains(aBlock.blockMaterial)) return true;
			if (mEffectiveBlocksList.contains(aBlock)) return true;
			for (String tString : mEffectiveOreDictList) if (GT_OreDictUnificator.isItemStackInstanceOf(new ItemStack(aBlock, 1, aMeta==-1?GregTech_API.ITEM_WILDCARD_DAMAGE:aMeta), tString)) return true;
		}
		return false;
	}
	
	@Override
    public final boolean canHarvestBlock(Block aBlock, ItemStack aStack) {
		return canHarvestBlock(aBlock) && (!GT_ModHandler.isElectricItem(aStack) || GT_ModHandler.canUseElectricItem(aStack, mEUperBrokenBlock < 0 ? getDamagePerBlockBreak() * 1000 : mEUperBrokenBlock));
    }
	
	@Override
    public final boolean canHarvestBlock(Block aBlock) {
		return mToolStrength > 1 && isEffectiveAgainst(aBlock, -1);
    }
    
    @Override
    public float getStrVsBlock(ItemStack aStack, Block aBlock, int aMeta) {
		checkEnchantmentEffects(aStack);
    	if (mToolStrength <= 1 || !isEffectiveAgainst(aBlock, aMeta) || (GT_ModHandler.isElectricItem(aStack) && !GT_ModHandler.canUseElectricItem(aStack, mEUperBrokenBlock < 0 ? getDamagePerBlockBreak() * 1000 : mEUperBrokenBlock))) return 1.0F;
        return mToolStrength;
    }
    
	@Override
    public final boolean onBlockDestroyed(ItemStack aStack, World aWorld, int aID, int aX, int aY, int aZ, EntityLivingBase aPlayer) {
		GT_Utility.sendSoundToPlayers(aWorld, mBlockBreakSound, mBlockBreakSoundStrength, -1, aX, aY, aZ);
		checkEnchantmentEffects(aStack);
		if (mToolStrength > 1 && Block.blocksList[aID].getBlockHardness(aWorld, aX, aY, aZ) != 0.0) {
        	GT_ModHandler.damageOrDechargeItem(aStack, getDamagePerBlockBreak(), mEUperBrokenBlock < 0 ? getDamagePerBlockBreak() * 1000 : mEUperBrokenBlock, aPlayer);
    		checkEnchantmentEffects(aStack);
        }
        return true;
    }
    
	@Override
    public final boolean doesContainerItemLeaveCraftingGrid(ItemStack aStack) {
        return false;
    }
    
	public boolean canProvideEnergy(ItemStack aStack) {
		return false;
	}
	
	public final int getChargedItemId(ItemStack aStack) {
		return mChargedGTID<0?itemID:GregTech_API.sItemList[mChargedGTID].itemID;
	}
	
	public final int getEmptyItemId(ItemStack aStack) {
		return mDisChargedGTID<0?itemID:GregTech_API.sItemList[mDisChargedGTID].itemID;
	}
	
	public final int getMaxCharge(ItemStack aStack) {
		return getMaxDamage() * 1000;
	}
	
	@Override
	public final int getTier(ItemStack aStack) {
		return mTier;
	}
	
	public final int getTransferLimit(ItemStack aStack) {
		return (int)(Math.pow(2, mTier) * 128);
	}
}