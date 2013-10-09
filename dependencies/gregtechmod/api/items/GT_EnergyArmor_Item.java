package gregtechmod.api.items;

import gregtechmod.api.GregTech_API;
import gregtechmod.api.interfaces.IPlayerTickingItem;
import gregtechmod.api.util.GT_ModHandler;
import gregtechmod.api.util.GT_Utility;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumArmorMaterial;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.common.ISpecialArmor;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.ForgeSubscribe;
import net.minecraftforge.event.entity.living.LivingFallEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class GT_EnergyArmor_Item extends ItemArmor implements IPlayerTickingItem, ISpecialArmor {
	public int mCharge, mTransfer, mTier, mDamageEnergyCost, mSpecials;
	public boolean mChargeProvider;
	public double mArmorAbsorbtionPercentage;
	
    public static Map jumpChargeMap = new HashMap();
    
	public GT_EnergyArmor_Item(int aID, String aName, int aCharge, int aTransfer, int aTier, int aDamageEnergyCost, int aSpecials, double aArmorAbsorbtionPercentage, boolean aChargeProvider, int aType, int aArmorIndex) {
		super(aID, EnumArmorMaterial.DIAMOND, aArmorIndex, aType);
		setMaxStackSize(1);
		setMaxDamage(100);
		setNoRepair();
		setUnlocalizedName(aName);
		mCharge = Math.max(1, aCharge);
		mTransfer = Math.max(1, aTransfer);
		mTier = Math.max(1, aTier);
		mSpecials = aSpecials;
		mChargeProvider = aChargeProvider;
		mDamageEnergyCost = Math.max(0, aDamageEnergyCost);
		mArmorAbsorbtionPercentage = aArmorAbsorbtionPercentage;
		
		setCreativeTab(GregTech_API.TAB_GREGTECH);
		
        MinecraftForge.EVENT_BUS.register(this);
	}
	
	@Override
	public ItemStack onItemRightClick(ItemStack aStack, World aWorld, EntityPlayer aPlayer) {
		ItemStack tStack = aPlayer.inventory.armorInventory[3-armorType];
		if (tStack != null) {
			for (int i = 0; i < 9; i++) {
				if (aPlayer.inventory.mainInventory[i] == aStack) {
					aPlayer.inventory.armorInventory[3-armorType] = aPlayer.inventory.mainInventory[i];
					aPlayer.inventory.mainInventory[i] = tStack;
					return tStack;
				}
			}
		}
		return super.onItemRightClick(aStack, aWorld, aPlayer);
	}
	
	@Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IconRegister aIconRegister) {
        this.itemIcon = aIconRegister.registerIcon(GregTech_API.TEXTURE_PATH_ITEM + getUnlocalizedName());
    }
    
	@Override
    public void addInformation(ItemStack aStack, EntityPlayer aPlayer, List aList, boolean aF3_H) {
		aList.add("Tier: " + mTier);
		if ((mSpecials &    1) != 0) aList.add("Rebreather");
		if ((mSpecials &    2) != 0) aList.add("Inertia Damper");
		if ((mSpecials &    4) != 0) aList.add("Food Replicator");
		if ((mSpecials &    8) != 0) aList.add("Medicine Module");
		if ((mSpecials &   16) != 0) aList.add("Lamp");
		if ((mSpecials &   32) != 0) aList.add("Solarpanel");
		if ((mSpecials &   64) != 0) aList.add("Extinguisher Module");
		if ((mSpecials &  128) != 0) aList.add("Jump Booster");
		if ((mSpecials &  256) != 0) aList.add("Speed Booster");
		if ((mSpecials &  512) != 0) aList.add("Invisibility Field");
		if ((mSpecials & 1024) != 0) aList.add("Infinite Charge");
    }
	
    private void setCharge(ItemStack aStack) {
		NBTTagCompound tNBT = aStack.getTagCompound();
		if (tNBT == null) tNBT = new NBTTagCompound();
		tNBT.setInteger("charge", 1000000000);
        aStack.setTagCompound(tNBT);
    }
    
	@Override
	public boolean onTick(EntityPlayer aPlayer, ItemStack aStack, int aTimer, boolean aIsArmor) {
		if (mSpecials == 0 || !aIsArmor) return false;
		
		if (!aPlayer.worldObj.isRemote && (mSpecials & 1) != 0) {
	        int var4 = aPlayer.getAir();
            if (GT_ModHandler.canUseElectricItem(aStack, 1000) && var4 < 50) {
            	aPlayer.setAir(var4 + 250);
            	GT_ModHandler.useElectricItem(aStack, 1000, aPlayer);
            }
		}
		
		if (!aPlayer.worldObj.isRemote && (mSpecials & 4) != 0) {
            if (GT_ModHandler.canUseElectricItem(aStack, 50000) && aPlayer.getFoodStats().needFood()) {
            	aPlayer.getFoodStats().addStats(1, 0.0F);
                GT_ModHandler.useElectricItem(aStack, 50000, aPlayer);
            }
		}
		
		if ((mSpecials & 8) != 0) {
            if (GT_ModHandler.canUseElectricItem(aStack, 10000) && aPlayer.isPotionActive(Potion.poison)) {
            	GT_Utility.removePotion(aPlayer, Potion.poison.id);
                GT_ModHandler.useElectricItem(aStack, 10000, aPlayer);
            }
            if (GT_ModHandler.canUseElectricItem(aStack, 100000) && aPlayer.isPotionActive(Potion.wither)) {
            	GT_Utility.removePotion(aPlayer, Potion.wither.id);
                GT_ModHandler.useElectricItem(aStack, 100000, aPlayer);
            }
		}

		if ((mSpecials & 64) != 0) {
            aPlayer.setFire(0);
		}
		
		if (!aPlayer.worldObj.isRemote && (mSpecials & 128) != 0) {
            float var6 = jumpChargeMap.containsKey(aPlayer) ? ((Float)jumpChargeMap.get(aPlayer)).floatValue() : 1.0F;

            if (GT_ModHandler.canUseElectricItem(aStack, 1000) && aPlayer.onGround && var6 < 1.0F) {
                var6 = 1.0F;
                GT_ModHandler.useElectricItem(aStack, 1000, aPlayer);
            }

            if (aPlayer.motionY >= 0.0D && var6 > 0.0F && !aPlayer.isInWater()) {
                if (GT_ModHandler.getJumpKeyDown(aPlayer) && GT_ModHandler.getBoostKeyDown(aPlayer)) {
                    if (var6 == 1.0F) {
                    	aPlayer.motionX *= 3.5D;
                    	aPlayer.motionZ *= 3.5D;
                    }

                    aPlayer.motionY += (double)(var6 * 0.3F);
                    var6 = (float)((double)var6 * 0.75D);
                } else if (var6 < 1.0F) {
                    var6 = 0.0F;
                }
            }

            jumpChargeMap.put(aPlayer, Float.valueOf(var6));
		}

		if ((mSpecials & 256) != 0) {
            if (GT_ModHandler.canUseElectricItem(aStack, 100) && aPlayer.isSprinting() && (aPlayer.onGround && Math.abs(aPlayer.motionX) + Math.abs(aPlayer.motionZ) > 0.10000000149011612D || aPlayer.isInWater())) {
                GT_ModHandler.useElectricItem(aStack, 100, aPlayer);
                float var7 = 0.22F;
                
                if (aPlayer.isInWater()) {
                    GT_ModHandler.useElectricItem(aStack, 100, aPlayer);
                    var7 = 0.1F;
                    
                    
                    if (aPlayer.motionY > 0) {
                    	aPlayer.motionY += 0.10000000149011612D;
                    }
                }
                
                if (var7 > 0.0F) {
                	aPlayer.moveFlying(0.0F, 1.0F, var7);
                }
            }
		}
		
		if ((mSpecials & 512) != 0) {
            if (GT_ModHandler.canUseElectricItem(aStack, 10000)) {
            	GT_ModHandler.useElectricItem(aStack, 10000, aPlayer);
                aPlayer.addPotionEffect(new PotionEffect(Potion.invisibility.getId(), 25, 1, true));
            }
		}
		
		if (!aPlayer.worldObj.isRemote && (mSpecials & (16|32)) != 0) {
			if (aTimer%20==0) {
				ItemStack tTargetChargeItem = aStack, tTargetDechargeItem = aStack;
				
				if (GT_ModHandler.chargeElectricItem(tTargetChargeItem, 1, Integer.MAX_VALUE, true, true) < 1) {
					tTargetChargeItem = aPlayer.inventory.armorInventory[2];
				}
				if (GT_ModHandler.dischargeElectricItem(tTargetDechargeItem, 10, Integer.MAX_VALUE, true, true, true) < 10) {
					tTargetDechargeItem = aPlayer.inventory.armorInventory[2];
				}
				
				if (tTargetChargeItem == null || !GT_ModHandler.isElectricItem(tTargetChargeItem)) {
					tTargetChargeItem = null;
				}
				if (tTargetDechargeItem == null || !GT_ModHandler.isElectricItem(tTargetChargeItem) || !(aStack == tTargetDechargeItem || GT_ModHandler.isChargerItem(tTargetDechargeItem))) {
					tTargetDechargeItem = null;
				}
				
				if (aPlayer.worldObj.isDaytime() && aPlayer.worldObj.canBlockSeeTheSky(MathHelper.floor_double(aPlayer.posX), MathHelper.floor_double(aPlayer.posY+1), MathHelper.floor_double(aPlayer.posZ))) {
					if ((mSpecials & 32) != 0 && tTargetChargeItem != null) {
						GT_ModHandler.chargeElectricItem(tTargetChargeItem, 20, Integer.MAX_VALUE, true, false);
					}
				} else {
					if ((mSpecials & 16) != 0 && tTargetDechargeItem != null && GT_ModHandler.canUseElectricItem(tTargetDechargeItem, 10)) {
						if (aPlayer.worldObj.getBlockId	(MathHelper.floor_double(aPlayer.posX), MathHelper.floor_double(aPlayer.posY+1), MathHelper.floor_double(aPlayer.posZ)) == 0)
							aPlayer.worldObj.setBlock	(MathHelper.floor_double(aPlayer.posX), MathHelper.floor_double(aPlayer.posY+1), MathHelper.floor_double(aPlayer.posZ), GregTech_API.getGregTechBlock(3, 1, 0).itemID);
						GT_ModHandler.useElectricItem(tTargetDechargeItem, 10, aPlayer);
					}
				}
			}
		}
		return true;
	}
	
	@Override
    public boolean getShareTag() {
        return true;
    }
	
	@Override
    @SideOnly(Side.CLIENT)
    public void getSubItems(int aStack, CreativeTabs var2, List var3) {
        ItemStack tCharged = new ItemStack(this, 1), tUncharged = new ItemStack(this, 1, getMaxDamage());
        GT_ModHandler.chargeElectricItem(tCharged, Integer.MAX_VALUE, Integer.MAX_VALUE, true, false);
        var3.add(tCharged);
        var3.add(tUncharged);
    }
    
	public boolean canProvideEnergy(ItemStack aStack) {
		if ((mSpecials & 1024) != 0) setCharge(aStack);
		return mChargeProvider;
	}
	
	public int getChargedItemId(ItemStack aStack) {
		if ((mSpecials & 1024) != 0) setCharge(aStack);
		return itemID;
	}
	
	public int getEmptyItemId(ItemStack aStack) {
		if ((mSpecials & 1024) != 0) setCharge(aStack);
		return itemID;
	}
	
	public int getMaxCharge(ItemStack aStack) {
		if ((mSpecials & 1024) != 0) setCharge(aStack);
		return mCharge;
	}
	
	public int getTier(ItemStack aStack) {
		if ((mSpecials & 1024) != 0) setCharge(aStack);
		return mTier;
	}
	
	public int getTransferLimit(ItemStack aStack) {
		if ((mSpecials & 1024) != 0) setCharge(aStack);
		return mTransfer;
	}
	
	@Override
    public int getItemEnchantability() {
        return 0;
    }
	
	@Override
    public boolean isBookEnchantable(ItemStack itemstack1, ItemStack itemstack2) {
        return false;
    }
	
	@Override
    public boolean getIsRepairable(ItemStack par1ItemStack, ItemStack par2ItemStack) {
        return false;
    }
	
    @ForgeSubscribe
    public void onEntityLivingFallEvent(LivingFallEvent var1) {
        if (!var1.entity.worldObj.isRemote && var1.entity instanceof EntityPlayer) {
            EntityPlayer var2 = (EntityPlayer)var1.entity;
            for (int i = 0; i < 4; i++) {
	            ItemStack var3 = var2.inventory.armorInventory[i];
	            if (var3 != null && var3.getItem() == this && (mSpecials & 2) != 0) {
	                int var4 = (int)var1.distance - 3;
	                int var5 = (this.mDamageEnergyCost * var4) / 4;
	                if (var5 <= GT_ModHandler.dischargeElectricItem(var3, Integer.MAX_VALUE, Integer.MAX_VALUE, true, true, true)) {
	                    GT_ModHandler.dischargeElectricItem(var3, var5, Integer.MAX_VALUE, true, false, true);
	                    var1.setCanceled(true);
	                    break;
	                }
	            }
            }
        }
    }
    
	@Override
    public ISpecialArmor.ArmorProperties getProperties(EntityLivingBase var1, ItemStack var2, DamageSource var3, double var4, int var6) {
    	return new ISpecialArmor.ArmorProperties((var3 == DamageSource.fall && (mSpecials & 2) != 0)?10:0, getBaseAbsorptionRatio() * mArmorAbsorbtionPercentage, mDamageEnergyCost > 0 ? 25 * GT_ModHandler.dischargeElectricItem(var2, Integer.MAX_VALUE, Integer.MAX_VALUE, true, true, true) / mDamageEnergyCost : 0);
    }
	
	@Override
    public int getArmorDisplay(EntityPlayer var1, ItemStack var2, int var3) {
        return (int)Math.round(20.0D * getBaseAbsorptionRatio() * mArmorAbsorbtionPercentage);
    }
	
	@Override
    public void damageArmor(EntityLivingBase var1, ItemStack var2, DamageSource var3, int var4, int var5) {
        GT_ModHandler.dischargeElectricItem(var2, var4 * mDamageEnergyCost, Integer.MAX_VALUE, true, false, true);
    }
	
    public boolean isMetalArmor(ItemStack var1, EntityPlayer var2) {
        return true;
    }
    
    private double getBaseAbsorptionRatio() {
    	if (mArmorAbsorbtionPercentage <= 0) return 0.00;
        switch (this.armorType) {
            case  0: return 0.15;
            case  1: return 0.40;
            case  2: return 0.30;
            case  3: return 0.15;
            default: return 0.00;
        }
    }
}
