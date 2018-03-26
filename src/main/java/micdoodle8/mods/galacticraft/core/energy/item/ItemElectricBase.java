package micdoodle8.mods.galacticraft.core.energy.item;

import ic2.api.item.IElectricItemManager;
import micdoodle8.mods.galacticraft.api.item.ElectricItemHelper;
import micdoodle8.mods.galacticraft.api.item.IItemElectricBase;
import micdoodle8.mods.galacticraft.core.energy.EnergyConfigHandler;
import micdoodle8.mods.galacticraft.core.energy.EnergyDisplayHelper;
import micdoodle8.mods.galacticraft.core.items.ItemBatteryInfinite;
import micdoodle8.mods.miccore.Annotations.RuntimeInterface;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagDouble;
import net.minecraft.nbt.NBTTagFloat;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.versioning.DefaultArtifactVersion;
import net.minecraftforge.fml.relauncher.FMLInjectionData;

import java.util.List;

public abstract class ItemElectricBase extends Item implements IItemElectricBase
{
    private static Object itemManagerIC2;
    public float transferMax;
    private DefaultArtifactVersion mcVersion = null;
    private static final int DAMAGE_RANGE = 100;

    public ItemElectricBase()
    {
        super();
        this.setMaxStackSize(1);
        this.setMaxDamage(DAMAGE_RANGE);
        this.setNoRepair();
        this.setMaxTransfer();

        this.mcVersion = new DefaultArtifactVersion((String) FMLInjectionData.data()[4]);

        if (EnergyConfigHandler.isIndustrialCraft2Loaded())
        {
            itemManagerIC2 = new ElectricItemManagerIC2();
        }
    }

    @Override
    public boolean isItemTool(ItemStack stack)
    {
        return false;
    }

    protected void setMaxTransfer()
    {
        this.transferMax = 200;
    }

    @Override
    public float getMaxTransferGC(ItemStack itemStack)
    {
        return this.transferMax;
    }
    
    @Override
    public void addInformation(ItemStack itemStack, EntityPlayer entityPlayer, List<String> tooltip, boolean par4)
    {
        String color = "";
        float joules = this.getElectricityStored(itemStack);

        if (joules <= this.getMaxElectricityStored(itemStack) / 3)
        {
            color = "\u00a74";
        }
        else if (joules > this.getMaxElectricityStored(itemStack) * 2 / 3)
        {
            color = "\u00a72";
        }
        else
        {
            color = "\u00a76";
        }

        tooltip.add(color + EnergyDisplayHelper.getEnergyDisplayS(joules) + "/" + EnergyDisplayHelper.getEnergyDisplayS(this.getMaxElectricityStored(itemStack)));
    }

    /**
     * Makes sure the item is uncharged when it is crafted and not charged.
     * Change this if you do not want this to happen!
     */
    @Override
    public void onCreated(ItemStack itemStack, World par2World, EntityPlayer par3EntityPlayer)
    {
        this.setElectricity(itemStack, 0);
    }

    @Override
    public float recharge(ItemStack itemStack, float energy, boolean doReceive)
    {
        float rejectedElectricity = Math.max(this.getElectricityStored(itemStack) + energy - this.getMaxElectricityStored(itemStack), 0);
        float energyToReceive = energy - rejectedElectricity;
        if (energyToReceive > this.transferMax)
        {
            rejectedElectricity += energyToReceive - this.transferMax;
            energyToReceive = this.transferMax;
        }

        if (doReceive)
        {
            this.setElectricity(itemStack, this.getElectricityStored(itemStack) + energyToReceive);
        }

        return energyToReceive;
    }

    @Override
    public float discharge(ItemStack itemStack, float energy, boolean doTransfer)
    {
        float thisEnergy = this.getElectricityStored(itemStack);
        float energyToTransfer = Math.min(Math.min(thisEnergy, energy), this.transferMax);

        if (doTransfer)
        {
            this.setElectricity(itemStack, thisEnergy - energyToTransfer);
        }

        return energyToTransfer;
    }

    @Override
    public int getTierGC(ItemStack itemStack)
    {
        return 1;
    }

    @Override
    public void setElectricity(ItemStack itemStack, float joules)
    {
        // Saves the frequency in the ItemStack
        if (itemStack.getTagCompound() == null)
        {
            itemStack.setTagCompound(new NBTTagCompound());
        }

        float electricityStored = Math.max(Math.min(joules, this.getMaxElectricityStored(itemStack)), 0);
        if (joules > 0F || itemStack.getTagCompound().hasKey("electricity"))
        {
            itemStack.getTagCompound().setFloat("electricity", electricityStored);
        }

        /** Sets the damage as a percentage to render the bar properly. */
        itemStack.setItemDamage(DAMAGE_RANGE - (int) (electricityStored / this.getMaxElectricityStored(itemStack) * DAMAGE_RANGE));
    }

    @Override
    public float getTransfer(ItemStack itemStack)
    {
        return Math.min(this.transferMax, this.getMaxElectricityStored(itemStack) - this.getElectricityStored(itemStack));
    }

    /**
     * Gets the energy stored in the item. Energy is stored using item NBT
     */
    @Override
    public float getElectricityStored(ItemStack itemStack)
    {
        if (itemStack.getTagCompound() == null)
        {
            itemStack.setTagCompound(new NBTTagCompound());
        }
        float energyStored = 0f;
        if (itemStack.getTagCompound().hasKey("electricity"))
        {
            NBTBase obj = itemStack.getTagCompound().getTag("electricity");
            if (obj instanceof NBTTagDouble)
            {
                energyStored = ((NBTTagDouble) obj).getFloat();
            }
            else if (obj instanceof NBTTagFloat)
            {
                energyStored = ((NBTTagFloat) obj).getFloat();
            }
        }
        else //First time check item - maybe from addInformation() in a JEI recipe display?
        {
            if (itemStack.getItemDamage() == DAMAGE_RANGE)
                return 0F;

            energyStored = this.getMaxElectricityStored(itemStack) * (DAMAGE_RANGE - itemStack.getItemDamage()) / DAMAGE_RANGE;
            itemStack.getTagCompound().setFloat("electricity", energyStored);
        }

        /** Sets the damage as a percentage to render the bar properly. */
        itemStack.setItemDamage(DAMAGE_RANGE - (int) (energyStored / this.getMaxElectricityStored(itemStack) * DAMAGE_RANGE));
        return energyStored;
    }

    @Override
    public void getSubItems(Item par1, CreativeTabs par2CreativeTabs, List<ItemStack> par3List)
    {
        par3List.add(ElectricItemHelper.getUncharged(new ItemStack(this)));
        par3List.add(ElectricItemHelper.getWithCharge(new ItemStack(this), this.getMaxElectricityStored(new ItemStack(this))));
    }

    public static boolean isElectricItem(Item item)
    {
        if (item instanceof IItemElectricBase)
        {
            return true;
        }

        if (EnergyConfigHandler.isIndustrialCraft2Loaded())
        {
            if (item instanceof ic2.api.item.ISpecialElectricItem)
            {
                return true;
            }
        }

        return false;
    }

    public static boolean isElectricItemEmpty(ItemStack itemstack)
    {
        if (itemstack == null)
        {
            return false;
        }
        Item item = itemstack.getItem();

        if (item instanceof IItemElectricBase)
        {
            return ((IItemElectricBase) item).getElectricityStored(itemstack) <= 0;
        }

        if (EnergyConfigHandler.isIndustrialCraft2Loaded())
        {
            if (item instanceof ic2.api.item.ISpecialElectricItem)
            {
                return !((ic2.api.item.ISpecialElectricItem) item).canProvideEnergy(itemstack);
            }
        }

        return false;
    }
    
    public static boolean isElectricItemCharged(ItemStack itemstack)
    {
        if (itemstack == null) return false;        
        Item item = itemstack.getItem();
        
        if (item instanceof IItemElectricBase)
        {
            return ((IItemElectricBase) item).getElectricityStored(itemstack) > 0;
        }

        if (EnergyConfigHandler.isIndustrialCraft2Loaded())
        {
            if (item instanceof ic2.api.item.ISpecialElectricItem)
            {
                return ((ic2.api.item.ISpecialElectricItem) item).canProvideEnergy(itemstack);
            }
        }

        return false;
    }
    
    //For RF compatibility

    @RuntimeInterface(clazz = "cofh.api.energy.IEnergyContainerItem", modID = "")
    public int receiveEnergy(ItemStack container, int maxReceive, boolean simulate)
    {
        return (int) (this.recharge(container, maxReceive * EnergyConfigHandler.RF_RATIO, !simulate) / EnergyConfigHandler.RF_RATIO);
    }

    @RuntimeInterface(clazz = "cofh.api.energy.IEnergyContainerItem", modID = "")
    public int extractEnergy(ItemStack container, int maxExtract, boolean simulate)
    {
        return (int) (this.discharge(container, maxExtract / EnergyConfigHandler.TO_RF_RATIO, !simulate) * EnergyConfigHandler.TO_RF_RATIO);
    }

    @RuntimeInterface(clazz = "cofh.api.energy.IEnergyContainerItem", modID = "")
    public int getEnergyStored(ItemStack container)
    {
        return (int) (this.getElectricityStored(container) * EnergyConfigHandler.TO_RF_RATIO);
    }

    @RuntimeInterface(clazz = "cofh.api.energy.IEnergyContainerItem", modID = "")
    public int getMaxEnergyStored(ItemStack container)
    {
        return (int) (this.getMaxElectricityStored(container) * EnergyConfigHandler.TO_RF_RATIO);
    }

    // The following seven methods are for Mekanism compatibility

    @RuntimeInterface(clazz = "mekanism.api.energy.IEnergizedItem", modID = "Mekanism")
    public double getEnergy(ItemStack itemStack)
    {
        return this.getElectricityStored(itemStack) * EnergyConfigHandler.TO_MEKANISM_RATIO;
    }

    @RuntimeInterface(clazz = "mekanism.api.energy.IEnergizedItem", modID = "Mekanism")
    public void setEnergy(ItemStack itemStack, double amount)
    {
        this.setElectricity(itemStack, (float) amount * EnergyConfigHandler.MEKANISM_RATIO);
    }

    @RuntimeInterface(clazz = "mekanism.api.energy.IEnergizedItem", modID = "Mekanism")
    public double getMaxEnergy(ItemStack itemStack)
    {
        return this.getMaxElectricityStored(itemStack) * EnergyConfigHandler.TO_MEKANISM_RATIO;
    }

    @RuntimeInterface(clazz = "mekanism.api.energy.IEnergizedItem", modID = "Mekanism")
    public double getMaxTransfer(ItemStack itemStack)
    {
        return this.transferMax * EnergyConfigHandler.TO_MEKANISM_RATIO;
    }

    @RuntimeInterface(clazz = "mekanism.api.energy.IEnergizedItem", modID = "Mekanism")
    public boolean canReceive(ItemStack itemStack)
    {
        return (itemStack != null && !(itemStack.getItem() instanceof ItemBatteryInfinite));
    }

    public boolean canSend(ItemStack itemStack)
    {
        return true;
    }

    // All the following methods are for IC2 compatibility

    @RuntimeInterface(clazz = "ic2.api.item.ISpecialElectricItem", modID = "IC2")
    public IElectricItemManager getManager(ItemStack itemstack)
    {
        return (IElectricItemManager) ItemElectricBase.itemManagerIC2;
    }

    @RuntimeInterface(clazz = "ic2.api.item.ISpecialElectricItem", modID = "IC2")
    public boolean canProvideEnergy(ItemStack itemStack)
    {
        return true;
    }

    @RuntimeInterface(clazz = "ic2.api.item.ISpecialElectricItem", modID = "IC2")
    public int getTier(ItemStack itemStack)
    {
        return 1;
    }

    @RuntimeInterface(clazz = "ic2.api.item.ISpecialElectricItem", modID = "IC2")
    public double getMaxCharge(ItemStack itemStack)
    {
        return this.getMaxElectricityStored(itemStack) / EnergyConfigHandler.IC2_RATIO;
    }

    @RuntimeInterface(clazz = "ic2.api.item.ISpecialElectricItem", modID = "IC2")
    public double getTransferLimit(ItemStack itemStack)
    {
        return this.transferMax * EnergyConfigHandler.TO_IC2_RATIO;
    }
}
