package micdoodle8.mods.galacticraft.core.client.jei.ingotcompressor;

import java.util.List;

import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.IRecipeWrapper;
import mezz.jei.api.recipe.IStackHelper;
import micdoodle8.mods.galacticraft.api.recipe.ShapelessOreRecipeGC;
import micdoodle8.mods.galacticraft.core.client.jei.GalacticraftJEI;
import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;

import javax.annotation.Nonnull;

import java.awt.Color;

public class IngotCompressorShapelessRecipeWrapper implements IRecipeWrapper
{
    @Nonnull
    private final ShapelessOreRecipeGC recipe;
    private final IStackHelper stackHelper;

    public IngotCompressorShapelessRecipeWrapper(IStackHelper stackhelper, @Nonnull ShapelessOreRecipeGC recipe)
    {
        this.recipe = recipe;
        this.stackHelper = stackhelper;
    }

    @Override
    public void getIngredients(IIngredients ingredients)
    {
    	List<List<ItemStack>> inputs =  this.stackHelper.expandRecipeItemStackInputs(recipe.getInput());
    	ingredients.setInputLists(ItemStack.class, inputs);
        ingredients.setOutput(ItemStack.class, this.recipe.getRecipeOutput());
    }

    @Override
    public void drawInfo(Minecraft mc, int recipeWidth, int recipeHeight, int mouseX, int mouseY)
    {
        if (GalacticraftJEI.hidden.contains(this))
        {
            FontRenderer fontRendererObj = mc.fontRenderer;
            String experienceString = "Asteroids Challenge";
            GCCoreUtil.drawStringCentered(experienceString, 69, 8, Color.gray.getRGB(), fontRendererObj);
            experienceString = "game mode only!";
            GCCoreUtil.drawStringCentered(experienceString, 69, 20, Color.gray.getRGB(), fontRendererObj);
            return;
        }
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
            FontRenderer fontRendererObj = mc.fontRenderer;
            int stringWidth = fontRendererObj.getStringWidth(experienceString);
            fontRendererObj.drawString(experienceString, recipeWidth + 6 - stringWidth, 8, Color.gray.getRGB());
        }
    }

    public boolean matches(@Nonnull ShapelessOreRecipeGC test)
    {
        return this.recipe == test;
    }
}
