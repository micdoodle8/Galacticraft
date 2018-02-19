package micdoodle8.mods.galacticraft.core.client.jei.buggy;

import mezz.jei.api.IGuiHelper;
import mezz.jei.api.gui.IDrawable;
import mezz.jei.api.gui.IGuiItemStackGroup;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.IRecipeCategory;
import mezz.jei.api.recipe.IRecipeWrapper;
import micdoodle8.mods.galacticraft.core.Constants;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.client.jei.RecipeCategories;
import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnull;

public class BuggyRecipeCategory implements IRecipeCategory
{
    private static final ResourceLocation buggyGuiTexture = new ResourceLocation(Constants.ASSET_PREFIX, "textures/gui/buggybench.png");

    @Nonnull
    private final IDrawable background;
    @Nonnull
    private final String localizedName;

    public BuggyRecipeCategory(IGuiHelper guiHelper)
    {
        this.background = guiHelper.createDrawable(buggyGuiTexture, 3, 4, 168, 130);
        this.localizedName = GCCoreUtil.translate("tile.rocket_workbench.name");

    }

    @Nonnull
    @Override
    public String getUid()
    {
        return RecipeCategories.BUGGY_ID;
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
    public void setRecipe(IRecipeLayout recipeLayout, IRecipeWrapper recipeWrapper, IIngredients ingredients)
    {
        IGuiItemStackGroup itemstacks = recipeLayout.getItemStacks();

        itemstacks.init(0, true, 35, 36);
        itemstacks.init(1, true, 53, 36);
        itemstacks.init(2, true, 71, 36);
        itemstacks.init(3, true, 35, 54);
        itemstacks.init(4, true, 71, 54);
        itemstacks.init(5, true, 53, 54);
        itemstacks.init(6, true, 35, 72);
        itemstacks.init(7, true, 53, 72);
        itemstacks.init(8, true, 71, 72);
        itemstacks.init(9, true, 35, 90);
        itemstacks.init(10, true, 53, 90);
        itemstacks.init(11, true, 71, 90);

        itemstacks.init(12, true, 17, 36);
        itemstacks.init(13, true, 17, 90);
        itemstacks.init(14, true, 89, 36);
        itemstacks.init(15, true, 89, 90);
        itemstacks.init(16, true, 89, 7);
        itemstacks.init(17, true, 115, 7);
        itemstacks.init(18, true, 141, 7);
        itemstacks.init(19, false, 138, 102);

        itemstacks.set(ingredients);
    }

    @Override
    public String getModName()
    {
        return GalacticraftCore.NAME;
    }
}
