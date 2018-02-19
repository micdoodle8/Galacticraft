package micdoodle8.mods.galacticraft.core.client.jei.ingotcompressor;

import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.BlankRecipeWrapper;
import mezz.jei.api.recipe.IRecipeWrapper;
import micdoodle8.mods.galacticraft.api.recipe.ShapedRecipesGC;
import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;

import javax.annotation.Nonnull;

import java.awt.Color;
import java.util.Arrays;

public class IngotCompressorShapedRecipeWrapper extends BlankRecipeWrapper implements IRecipeWrapper
{
    @Nonnull
    private final ShapedRecipesGC recipe;

    public IngotCompressorShapedRecipeWrapper(@Nonnull ShapedRecipesGC recipe)
    {
        this.recipe = recipe;
    }

    @Override
    public void getIngredients(IIngredients ingredients)
    {
        ingredients.setInputs(ItemStack.class, Arrays.asList(recipe.recipeItems));
        ingredients.setOutput(ItemStack.class, recipe.getRecipeOutput());
    }

    @Override
    public void drawInfo(Minecraft mc, int recipeWidth, int recipeHeight, int mouseX, int mouseY)
    {
        FurnaceRecipes furnaceRecipes = FurnaceRecipes.instance();
        float experience = 0;

        try
        {
            experience = furnaceRecipes.getSmeltingExperience(this.recipe.getRecipeOutput());
        }
        catch (Exception e) {}

        if (experience > 0)
        {
            String experienceString = GCCoreUtil.translateWithFormat("gui.jei.category.smelting.experience", experience);
            FontRenderer fontRendererObj = mc.fontRendererObj;
            int stringWidth = fontRendererObj.getStringWidth(experienceString);
            fontRendererObj.drawString(experienceString, recipeWidth + 6 - stringWidth, 8, Color.gray.getRGB());
        }
    }
    
    public boolean equals(Object o)
    {
        if (o instanceof IngotCompressorShapedRecipeWrapper)
        {
            ShapedRecipesGC match = ((IngotCompressorShapedRecipeWrapper)o).recipe;
            if (!ItemStack.areItemStacksEqual(match.getRecipeOutput(), this.recipe.getRecipeOutput()))
                return false;
            for (int i = 0; i < this.recipe.recipeItems.length; i++)
            {
                ItemStack a = this.recipe.recipeItems[i];
                ItemStack b = match.recipeItems[i];
                if (!a.isEmpty() && !b.isEmpty() && !ItemStack.areItemStacksEqual(a, b))
                    return false;
            }
            return true;
        }
        return false;
    }
}
