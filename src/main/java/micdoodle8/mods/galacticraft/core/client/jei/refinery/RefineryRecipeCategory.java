package micdoodle8.mods.galacticraft.core.client.jei.refinery;

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

public class RefineryRecipeCategory implements IRecipeCategory
{
    private static final ResourceLocation refineryGuiTex = new ResourceLocation(Constants.ASSET_PREFIX, "textures/gui/refinery_recipe.png");

    @Nonnull
    private final IDrawable background;
    @Nonnull
    private final String localizedName;
    @Nonnull
    private final IDrawableAnimated oilBar;
    @Nonnull
    private final IDrawableAnimated fuelBar;

    public RefineryRecipeCategory(IGuiHelper guiHelper)
    {
        this.background = guiHelper.createDrawable(refineryGuiTex, 3, 4, 168, 64);
        this.localizedName = GCCoreUtil.translate("tile.refinery.name");

        IDrawableStatic progressBarDrawableOil = guiHelper.createDrawable(refineryGuiTex, 176, 0, 16, 38);
        this.oilBar = guiHelper.createAnimatedDrawable(progressBarDrawableOil, 70, IDrawableAnimated.StartDirection.TOP, true);
        IDrawableStatic progressBarDrawableFuel = guiHelper.createDrawable(refineryGuiTex, 192, 0, 16, 38);
        this.fuelBar = guiHelper.createAnimatedDrawable(progressBarDrawableFuel, 70, IDrawableAnimated.StartDirection.BOTTOM, false);
    }

    @Nonnull
    @Override
    public String getUid()
    {
        return RecipeCategories.REFINERY_ID;
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
    public void drawExtras(@Nonnull Minecraft minecraft)
    {
        this.oilBar.draw(minecraft, 40, 24);
        this.fuelBar.draw(minecraft, 114, 24);
    }

    @Override
    public void setRecipe(IRecipeLayout recipeLayout, IRecipeWrapper recipeWrapper, IIngredients ingredients)
    {
        IGuiItemStackGroup itemstacks = recipeLayout.getItemStacks();

        itemstacks.init(0, true, 39, 2);
        itemstacks.init(1, false, 113, 2);

        itemstacks.set(ingredients);
    }

    @Override
    public String getModName()
    {
        return GalacticraftCore.NAME;
    }
}
