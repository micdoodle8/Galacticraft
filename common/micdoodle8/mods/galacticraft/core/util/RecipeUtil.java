package micdoodle8.mods.galacticraft.core.util;

import gregtechmod.api.GregTech_API;
import ic2.api.item.Items;
import java.util.HashMap;
import micdoodle8.mods.galacticraft.api.GalacticraftRegistry;
import micdoodle8.mods.galacticraft.api.recipe.INasaWorkbenchRecipe;
import micdoodle8.mods.galacticraft.core.GCLog;
import micdoodle8.mods.galacticraft.core.inventory.GCCoreInventoryBuggyBench;
import micdoodle8.mods.galacticraft.core.inventory.GCCoreInventoryRocketBench;
import micdoodle8.mods.galacticraft.core.recipe.GCCoreNasaWorkbenchRecipe;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraftforge.oredict.ShapedOreRecipe;

public class RecipeUtil
{
    public static ItemStack findMatchingBuggy(GCCoreInventoryBuggyBench benchStacks)
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

    public static ItemStack findMatchingSpaceshipRecipe(GCCoreInventoryRocketBench inventoryRocketBench)
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

    public static void addRocketBenchRecipe(ItemStack result, HashMap<Integer, ItemStack> input)
    {
        GalacticraftRegistry.addT1RocketRecipe(new GCCoreNasaWorkbenchRecipe(result, input));
    }

    public static void addBuggyBenchRecipe(ItemStack result, HashMap<Integer, ItemStack> input)
    {
        GalacticraftRegistry.addMoonBuggyRecipe(new GCCoreNasaWorkbenchRecipe(result, input));
    }

    public static ItemStack getGregtechBlock(int index, int amount, int metadata)
    {
        ItemStack stack = GregTech_API.getGregTechBlock(index, amount, metadata);

        if (stack != null)
        {
            return stack;
        }

        GCLog.severe("Failed to load Gregtech block for recipe, ensure Gregtech has loaded properly");
        return stack;
    }

    public static ItemStack getGregtechItem(int index, int amount, int metadata)
    {
        ItemStack stack = GregTech_API.getGregTechItem(index, index, metadata);

        if (stack != null)
        {
            return stack;
        }

        GCLog.severe("Failed to load Gregtech item for recipe, ensure Gregtech has loaded properly");
        return stack;
    }

    public static ItemStack getIndustrialCraftItem(String indentifier)
    {
        ItemStack stack = Items.getItem(indentifier);

        if (stack != null)
        {
            return stack;
        }
        else
        {
            try
            {
                // Might as well, since it'll NPE when trying to add the recipe
                // anyway.
                throw new Exception("Failed to load IC2 item for recipe (" + indentifier + "), ensure it has loaded properly and isn't disabled.");
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }

        return stack;
    }
}
