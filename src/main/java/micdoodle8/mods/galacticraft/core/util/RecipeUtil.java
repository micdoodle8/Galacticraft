package micdoodle8.mods.galacticraft.core.util;

import ic2.api.item.IC2Items;
import micdoodle8.mods.galacticraft.api.GalacticraftRegistry;
import micdoodle8.mods.galacticraft.api.recipe.INasaWorkbenchRecipe;
import micdoodle8.mods.galacticraft.core.inventory.InventoryBuggyBench;
import micdoodle8.mods.galacticraft.core.inventory.InventoryRocketBench;
import micdoodle8.mods.galacticraft.core.recipe.NasaWorkbenchRecipe;
import micdoodle8.mods.galacticraft.core.recipe.OreRecipeUpdatable;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.oredict.ShapelessOreRecipe;

import java.util.HashMap;

public class RecipeUtil
{
    public static ItemStack findMatchingBuggy(InventoryBuggyBench benchStacks)
    {
        for (INasaWorkbenchRecipe recipe : GalacticraftRegistry.getBuggyBenchRecipes())
        {
            if (recipe.matches(benchStacks))
            {
                return recipe.getRecipeOutput();
            }
        }

        return null;
    }

    public static ItemStack findMatchingSpaceshipRecipe(InventoryRocketBench inventoryRocketBench)
    {
        for (INasaWorkbenchRecipe recipe : GalacticraftRegistry.getRocketT1Recipes())
        {
            if (recipe.matches(inventoryRocketBench))
            {
                return recipe.getRecipeOutput();
            }
        }

        return null;
    }

    public static void addRecipe(ItemStack result, Object[] obj)
    {
        CraftingManager.getInstance().getRecipeList().add(new ShapedOreRecipe(result, obj));
    }

    public static void addShapelessRecipe(ItemStack result, Object... obj)
    {
        CraftingManager.getInstance().addShapelessRecipe(result, obj);
    }

    public static void addShapelessOreRecipe(ItemStack result, Object... obj)
    {
        CraftingManager.getInstance().getRecipeList().add(new ShapelessOreRecipe(result, obj));
    }

    public static void addCustomRecipe(IRecipe rec)
    {
        CraftingManager.getInstance().getRecipeList().add(rec);
    }

    public static void addBlockRecipe(ItemStack result, String oreDictIngot, ItemStack gcIngot)
    {
        if (OreDictionary.getOres(oreDictIngot).size() > 1)
        {
            CraftingManager.getInstance().getRecipeList().add(new ShapelessOreRecipe(result, new Object[] { gcIngot, oreDictIngot, oreDictIngot, oreDictIngot, oreDictIngot, oreDictIngot, oreDictIngot, oreDictIngot, oreDictIngot }));
        }
        else
        {
            RecipeUtil.addRecipe(result, new Object[] { "XXX", "XXX", "XXX", 'X', gcIngot });
        }
    }

    public static void addRocketBenchRecipe(ItemStack result, HashMap<Integer, ItemStack> input)
    {
        GalacticraftRegistry.addT1RocketRecipe(new NasaWorkbenchRecipe(result, input));
    }

    public static void addBuggyBenchRecipe(ItemStack result, HashMap<Integer, ItemStack> input)
    {
        GalacticraftRegistry.addMoonBuggyRecipe(new NasaWorkbenchRecipe(result, input));
    }

    public static ItemStack getIndustrialCraftItem(String indentifier, String variant)
    {
        return IC2Items.getItem(indentifier, variant);
    }

    public static void addRecipeUpdatable(ItemStack result, Object[] obj)
    {
        CraftingManager.getInstance().getRecipeList().add(new OreRecipeUpdatable(result, obj));
    }
    
    /**
     * An extended version of areItemStackTagsEqual which ignores LevelUp's "NoPlacing" tag on mined blocks
     */
    public static boolean areItemStackTagsEqual(ItemStack stackA, ItemStack stackB)
    {
        if (ItemStack.areItemStackTagsEqual(stackA, stackB))
            return true;
        
        NBTTagCompound query = null;
        if (stackA.getTagCompound() == null && stackB.getTagCompound() != null)
        {
            query = stackB.getTagCompound();
        }
        else if (stackA.getTagCompound() != null && stackB.getTagCompound() == null)
        {
            query = stackA.getTagCompound();
        }
        if (query != null)
        {
            if (query.getKeySet().size() == 1 && query.hasKey("NoPlacing"))
                return true;
        }

       return false;
    }
    
    /**
     * 
     * @param itemstack - it is assumed this one is not null in calling code
     * @param itemstack1
     * @return
     */
    public static boolean stacksMatch(ItemStack itemstack, ItemStack itemstack1)
    {
        return itemstack1 != null && itemstack1.getItem() == itemstack.getItem() && (!itemstack.getHasSubtypes() || itemstack.getItemDamage() == itemstack1.getItemDamage()) && RecipeUtil.areItemStackTagsEqual(itemstack, itemstack1);
    }
}
