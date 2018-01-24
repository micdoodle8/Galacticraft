package micdoodle8.mods.galacticraft.core.client.jei.circuitfabricator;

import mezz.jei.api.IGuiHelper;
import mezz.jei.api.gui.*;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.IRecipeCategory;
import mezz.jei.api.recipe.IRecipeWrapper;
import micdoodle8.mods.galacticraft.core.Constants;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.client.jei.RecipeCategories;
import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnull;

public class CircuitFabricatorRecipeCategory implements IRecipeCategory
{
    private static final ResourceLocation circuitFabTex = new ResourceLocation(Constants.ASSET_PREFIX, "textures/gui/circuit_fabricator.png");

    @Nonnull
    private final IDrawable background;
    @Nonnull
    private final String localizedName;
    @Nonnull
    private final IDrawableAnimated progressBar;

    public CircuitFabricatorRecipeCategory(IGuiHelper guiHelper)
    {
        this.background = guiHelper.createDrawable(circuitFabTex, 3, 4, 168, 101);
        this.localizedName = GCCoreUtil.translate("tile.machine2.5.name");

        IDrawableStatic progressBarDrawable = guiHelper.createDrawable(circuitFabTex, 176, 17, 51, 10);
        this.progressBar = guiHelper.createAnimatedDrawable(progressBarDrawable, 70, IDrawableAnimated.StartDirection.LEFT, false);
    }

    @Nonnull
    @Override
    public String getUid()
    {
        return RecipeCategories.CIRCUIT_FABRICATOR_ID;
    }

    @Nonnull
    @Override
    public String getTitle()
    {
        return this.localizedName;
    }

    @Nonnull
    @Override
    public IDrawable getBackground()
    {
        return this.background;
    }

    @Override
    public void drawExtras(Minecraft minecraft)
    {
        this.progressBar.draw(minecraft, 85, 16);
    }

    @Override
    public void setRecipe(IRecipeLayout recipeLayout, IRecipeWrapper recipeWrapper, IIngredients ingredients)
    {
        IGuiItemStackGroup itemstacks = recipeLayout.getItemStacks();

        itemstacks.init(0, true, 11, 12);
        itemstacks.init(1, true, 70, 41);
        itemstacks.init(2, true, 70, 59);
        itemstacks.init(3, true, 118, 41);
        itemstacks.init(4, true, 141, 15);
        itemstacks.init(5, false, 148, 81);
        itemstacks.set(ingredients);
    }

    @Override
    public String getModName()
    {
        return GalacticraftCore.NAME;
    }
}
