package micdoodle8.mods.galacticraft.core.client.jei.ingotcompressor;

import java.util.List;

import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.BlankRecipeWrapper;
import mezz.jei.api.recipe.IRecipeWrapper;
import mezz.jei.api.recipe.IStackHelper;
import micdoodle8.mods.galacticraft.api.recipe.ShapelessOreRecipeGC;
import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;

import javax.annotation.Nonnull;

import java.awt.Color;

public class IngotCompressorShapelessRecipeWrapper extends BlankRecipeWrapper implements IRecipeWrapper
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
        if (o instanceof IngotCompressorShapelessRecipeWrapper)
        {
            ShapelessOreRecipeGC match = ((IngotCompressorShapelessRecipeWrapper)o).recipe;
            if (!ItemStack.areItemStacksEqual(match.getRecipeOutput(), this.recipe.getRecipeOutput()))
                return false;
            for (int i = 0; i < this.recipe.getInput().size(); i++)
            {
                Object a = this.recipe.getInput().get(i);
                Object b = match.getInput().get(i);
                if (a == null && b == null)
                    continue;
                
                if (a instanceof ItemStack)
                {
                    if (!(b instanceof ItemStack))
                        return false;
                    if (!ItemStack.areItemStacksEqual((ItemStack) a, (ItemStack) b))
                        return false;
                }
                else if (a instanceof List<?>)
                {
                    if (!(b instanceof List<?>))
                        return false;
                    List aa = ((List)a);
                    List bb = ((List)b);
                    if (aa.size() != bb.size())
                        return false;
                    for (int j = 0; j < aa.size(); j++)
                    {
                        ItemStack c = (ItemStack) aa.get(j);
                        ItemStack d = (ItemStack) bb.get(j);
                        if (!ItemStack.areItemStacksEqual((ItemStack) c, (ItemStack) d))
                            return false;
                    }                    
                }
            }
            return true;
        }
        return false;
    }
}
