package micdoodle8.mods.galacticraft.planets.asteroids.recipe;

import micdoodle8.mods.galacticraft.core.GCItems;
import micdoodle8.mods.galacticraft.core.items.ItemOxygenTank;
import micdoodle8.mods.galacticraft.planets.asteroids.items.ItemCanisterLiquidOxygen;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.item.crafting.ShapelessRecipes;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;

public class CanisterRecipes extends ShapelessRecipes
{
    public CanisterRecipes(ItemStack stack, NonNullList<Ingredient> list)
    {
        super("canisters", stack, list);
    }

    /**
     * Used to check if a recipe matches current crafting inventory
     */
    @Override
    public boolean matches(InventoryCrafting p_77569_1_, World p_77569_2_)
    {
        ItemStack itemCanister = ItemStack.EMPTY;
        ItemStack itemTank = ItemStack.EMPTY;

        for (int i = 0; i < p_77569_1_.getSizeInventory(); ++i)
        {
            ItemStack itemstack1 = p_77569_1_.getStackInSlot(i);

            if (!itemstack1.isEmpty())
            {
                Item testItem = itemstack1.getItem();
                if (testItem instanceof ItemCanisterLiquidOxygen || testItem == GCItems.oxygenCanisterInfinite)
                {
                    if (!itemCanister.isEmpty())
                    {
                        //Two canisters
                        return false;
                    }

                    itemCanister = itemstack1;
                }
                else
                {
                    if (!(testItem instanceof ItemOxygenTank) || !itemTank.isEmpty())
                    {
                        //Something other than an oxygen tank
                        return false;
                    }

                    itemTank = itemstack1;
                }
            }
        }

        //Need one canister + one tank
        if (itemCanister.isEmpty() || itemTank.isEmpty())
        {
            return false;
        }

        //Empty canister
        if (itemCanister.getItemDamage() >= itemCanister.getMaxDamage())
        {
            return false;
        }

        //Full tank
        if (itemTank.getItemDamage() <= 0)
        {
            return false;
        }

        return true;
    }

    /**
     * Returns an Item that is the result of this recipe
     */
    @Override
    public ItemStack getCraftingResult(InventoryCrafting inv)
    {
        ItemStack itemTank = null;  //Intentionally null - internal use only
        ItemStack itemCanister = null;  //Intentionally null - internal use only

        for (int i = 0; i < inv.getSizeInventory(); ++i)
        {
            ItemStack itemstack1 = inv.getStackInSlot(i);

            if (!itemstack1.isEmpty())
            {
                Item testItem = itemstack1.getItem();
                if (testItem instanceof ItemCanisterLiquidOxygen || testItem == GCItems.oxygenCanisterInfinite)
                {
                    //Intentional item null check
                    if (itemCanister != null)
                    {
                        //Two canisters
                        return ItemStack.EMPTY;
                    }

                    itemCanister = itemstack1;
                }
                else
                {
                    //Intentional item null check
                    if (!(testItem instanceof ItemOxygenTank) || itemTank != null)
                    {
                        //Something other than an oxygen tank
                        return ItemStack.EMPTY;
                    }

                    itemTank = itemstack1;
                }
            }
        }

        //Need one canister + one tank (intentional item null check)
        if (itemCanister == null || itemTank == null)
        {
            return ItemStack.EMPTY;
        }

        //Empty canister
        if (itemCanister.getItemDamage() >= itemCanister.getMaxDamage())
        {
            return ItemStack.EMPTY;
        }

        //Full tank
        if (itemTank.getItemDamage() <= 0)
        {
            return ItemStack.EMPTY;
        }

        int oxygenAvail = itemCanister.getMaxDamage() - itemCanister.getItemDamage();
        int oxygenToFill = itemTank.getItemDamage() * 5 / 54;

        if (oxygenAvail >= oxygenToFill)
        {
            ItemStack result = itemTank.copy();
            result.setItemDamage(0);
            if (itemCanister.getItem() instanceof ItemCanisterLiquidOxygen)
            {
                ItemCanisterLiquidOxygen.saveDamage(itemCanister, itemCanister.getItemDamage() + oxygenToFill);
            }
            return result;
        }

        int tankDamageNew = (oxygenToFill - oxygenAvail) * 54 / 5;
        ItemStack result = itemTank.copy();
        result.setItemDamage(tankDamageNew);
        if (itemCanister.getItem() instanceof ItemCanisterLiquidOxygen)
        {
            ItemCanisterLiquidOxygen.saveDamage(itemCanister, itemCanister.getMaxDamage());
        }
        return result;
    }

    @Override
    public boolean canFit(int width, int height)
    {
        return width * height >= 2;
    }
}
