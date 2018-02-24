package micdoodle8.mods.galacticraft.core.inventory;

import micdoodle8.mods.galacticraft.api.item.IItemElectric;
import micdoodle8.mods.galacticraft.core.energy.EnergyConfigHandler;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Creates a slot with a specific amount of items that matches the slot's
 * requirements. Allows easy shift right clicking management and slot blocking
 * in classes. In your container you can use this.getSlot(i).isItemValid to
 * justify the player's shift clicking actions to match the slot.
 *
 * @author Calclavia
 */
public class SlotSpecific extends Slot
{
    public ItemStack[] validItemStacks = new ItemStack[0];
    public Class[] validClasses = new Class[0];

    public boolean isInverted = false;
    public boolean isMetadataSensitive = false;

    public SlotSpecific(IInventory par2IInventory, int par3, int par4, int par5, ItemStack... itemStacks)
    {
        super(par2IInventory, par3, par4, par5);
        this.setItemStacks(itemStacks);
    }

    public SlotSpecific(IInventory par2IInventory, int par3, int par4, int par5, Class... validClasses)
    {
        super(par2IInventory, par3, par4, par5);
        if (validClasses != null && Arrays.asList(validClasses).contains(IItemElectric.class))
        {
            try
            {
                if (EnergyConfigHandler.isRFAPILoaded())
                {
                    ArrayList<Class> existing = new ArrayList<>(Arrays.asList(validClasses));
                    existing.add(cofh.api.energy.IEnergyContainerItem.class);
                    validClasses = existing.toArray(new Class[existing.size()]);
                }
                if (EnergyConfigHandler.isIndustrialCraft2Loaded())
                {
                    ArrayList<Class> existing = new ArrayList<>(Arrays.asList(validClasses));
                    existing.add(ic2.api.item.IElectricItem.class);
                    existing.add(ic2.api.item.ISpecialElectricItem.class);
                    validClasses = existing.toArray(new Class[existing.size()]);
                }
                if (EnergyConfigHandler.isMekanismLoaded())
                {
                    ArrayList<Class> existing = new ArrayList<>(Arrays.asList(validClasses));
                    existing.add(mekanism.api.energy.IEnergizedItem.class);
                    validClasses = existing.toArray(new Class[existing.size()]);
                }
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
        this.setClasses(validClasses);
    }

    public SlotSpecific setMetadataSensitive()
    {
        this.isMetadataSensitive = true;
        return this;
    }

    public SlotSpecific setItemStacks(ItemStack... validItemStacks)
    {
        this.validItemStacks = validItemStacks;
        return this;
    }

    public SlotSpecific setClasses(Class... validClasses)
    {
        this.validClasses = validClasses;
        return this;
    }

    public SlotSpecific toggleInverted()
    {
        this.isInverted = !this.isInverted;
        return this;
    }

    /**
     * Check if the stack is a valid item for this slot. Always true beside for
     * the armor slots.
     */
    @Override
    public boolean isItemValid(ItemStack compareStack)
    {
        boolean returnValue = false;

        for (ItemStack itemStack : this.validItemStacks)
        {
            if (compareStack.isItemEqual(itemStack) || !this.isMetadataSensitive && compareStack.getItem() == itemStack.getItem())
            {
                returnValue = true;
                break;
            }
        }

        if (!returnValue)
        {
            for (Class clazz : this.validClasses)
            {
                if (clazz.isInstance(compareStack.getItem()))
                {
                    returnValue = true;
                    break;
                }
            }
        }

        if (this.isInverted)
        {
            return !returnValue;
        }

        return returnValue;
    }
}
