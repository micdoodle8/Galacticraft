package micdoodle8.mods.galacticraft.planets.venus.recipe;

import micdoodle8.mods.galacticraft.core.GCBlocks;
import micdoodle8.mods.galacticraft.core.GCItems;
import micdoodle8.mods.galacticraft.core.blocks.BlockMachine;
import micdoodle8.mods.galacticraft.core.util.ConfigManagerCore;
import micdoodle8.mods.galacticraft.core.util.RecipeUtil;
import micdoodle8.mods.galacticraft.planets.asteroids.items.AsteroidsItems;
import micdoodle8.mods.galacticraft.planets.mars.items.MarsItems;
import micdoodle8.mods.galacticraft.planets.venus.VenusBlocks;
import micdoodle8.mods.galacticraft.planets.venus.VenusItems;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraftforge.oredict.OreDictionary;

public class RecipeManagerVenus
{
    public static void loadRecipes()
    {
        RecipeManagerVenus.addUniversalRecipes();
    }

    private static void addUniversalRecipes()
    {
        OreDictionary.registerOre("ingotLead", new ItemStack(VenusItems.basicItem, 1, 1));
        Object deshIngot = ConfigManagerCore.recipesRequireGCAdvancedMetals ? new ItemStack(MarsItems.marsItemBasic, 1, 2) : "ingotDesh";
        Object leadIngot = ConfigManagerCore.recipesRequireGCAdvancedMetals ? new ItemStack(VenusItems.basicItem, 1, 1) : "ingotLead";
        
        // Lead block
        RecipeUtil.addBlockRecipe(new ItemStack(VenusBlocks.venusBlock, 1, 12), "ingotLead", new ItemStack(VenusItems.basicItem, 1, 1));
        
        // Smelting
        FurnaceRecipes.instance().addSmeltingRecipe(new ItemStack(VenusBlocks.venusBlock, 1, 6), new ItemStack(GCItems.basicItem, 1, 5), 0.5F);
        FurnaceRecipes.instance().addSmeltingRecipe(new ItemStack(VenusBlocks.venusBlock, 1, 7), new ItemStack(GCItems.basicItem, 1, 3), 0.5F);
        FurnaceRecipes.instance().addSmeltingRecipe(new ItemStack(VenusBlocks.venusBlock, 1, 8), new ItemStack(VenusItems.basicItem, 1, 1), 1.0F);
        FurnaceRecipes.instance().addSmeltingRecipe(new ItemStack(VenusBlocks.venusBlock, 1, 11), new ItemStack(GCItems.basicItem, 1, 4), 0.5F);
    }
}
