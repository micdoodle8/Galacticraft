package micdoodle8.mods.galacticraft.planets.asteroids.recipe;

import micdoodle8.mods.galacticraft.core.items.ItemOxygenTank;
import micdoodle8.mods.galacticraft.planets.asteroids.items.ItemCanisterLiquidOxygen;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.ShapelessRecipes;
import net.minecraft.world.World;

import java.util.List;

public class CanisterRecipes extends ShapelessRecipes
{
    public CanisterRecipes(ItemStack stack, List list)
    {
        super(stack, list);
    }
	
	/**
     * Used to check if a recipe matches current crafting inventory
     */
    public boolean matches(InventoryCrafting p_77569_1_, World p_77569_2_)
    {
        ItemStack itemCanister = null;
        ItemStack itemTank = null;

        for (int i = 0; i < p_77569_1_.getSizeInventory(); ++i)
        {
            ItemStack itemstack1 = p_77569_1_.getStackInSlot(i);

            if (itemstack1 != null)
            {
                Item testItem = itemstack1.getItem();
            	if (testItem instanceof ItemCanisterLiquidOxygen)
                {
                    if (itemCanister != null)
                    {
                        //Two canisters
                    	return false;
                    }

                    itemCanister = itemstack1;
                }
                else
                {
                    if (!(testItem instanceof ItemOxygenTank) || itemTank != null)
                    {
                        //Something other than an oxygen tank
                    	return false;
                    }

                    itemTank = itemstack1;
                }
            }
        }

        //Need one canister + one tank
        if (itemCanister == null || itemTank == null) return false;
        
        //Empty canister
        if (itemCanister.getItemDamage() >= itemCanister.getMaxDamage()) return false;
        
        //Full tank
        if (itemTank.getItemDamage() <= 0) return false;
        
        return true;
    }

    /**
     * Returns an Item that is the result of this recipe
     */
    public ItemStack getCraftingResult(InventoryCrafting inv)
    {
    	ItemStack itemTank = null;
        ItemStack itemCanister = null;

        for (int i = 0; i < inv.getSizeInventory(); ++i)
        {
            ItemStack itemstack1 = inv.getStackInSlot(i);

            if (itemstack1 != null)
            {
                Item testItem = itemstack1.getItem();
            	if (testItem instanceof ItemCanisterLiquidOxygen)
                {
                    if (itemCanister != null)
                    {
                        //Two canisters
                    	return null;
                    }

                    itemCanister = itemstack1;
                }
                else
                {
                    if (!(testItem instanceof ItemOxygenTank) || itemTank != null)
                    {
                        //Something other than an oxygen tank
                    	return null;
                    }

                    itemTank = itemstack1;
                }
            }
        }

        //Need one canister + one tank
        if (itemCanister == null || itemTank == null) return null;
        
        //Empty canister
        if (itemCanister.getItemDamage() >= itemCanister.getMaxDamage()) return null;
        
        //Full tank
        if (itemTank.getItemDamage() <= 0) return null;
        
        int oxygenAvail = itemCanister.getMaxDamage() - itemCanister.getItemDamage();
        int oxygenToFill = itemTank.getItemDamage() * 5 / 54;
        
        if (oxygenAvail >= oxygenToFill)
        {
        	ItemStack result = itemTank.copy();
        	result.setItemDamage(0);
        	ItemCanisterLiquidOxygen.saveDamage(itemCanister, itemCanister.getItemDamage() + oxygenToFill);
        	return result;
        }
  
        int tankDamageNew = (oxygenToFill - oxygenAvail) * 54 / 5;
    	ItemStack result = itemTank.copy();
    	result.setItemDamage(tankDamageNew);
    	ItemCanisterLiquidOxygen.saveDamage(itemCanister, itemCanister.getMaxDamage());
        return result;
    }

	@Override
	public int getRecipeSize()
	{
		return 2;
	}
}
