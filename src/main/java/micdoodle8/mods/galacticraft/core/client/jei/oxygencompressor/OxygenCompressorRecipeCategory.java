package micdoodle8.mods.galacticraft.core.client.jei.oxygencompressor;

import mezz.jei.api.IGuiHelper;
import mezz.jei.api.gui.*;
import mezz.jei.api.recipe.BlankRecipeCategory;
import mezz.jei.api.recipe.IRecipeWrapper;
import micdoodle8.mods.galacticraft.core.Constants;
import micdoodle8.mods.galacticraft.core.client.jei.RecipeCategories;
import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnull;

public class OxygenCompressorRecipeCategory extends BlankRecipeCategory
{
    private static final ResourceLocation compressorTexture = new ResourceLocation(Constants.ASSET_PREFIX, "textures/gui/compressor.png");

    @Nonnull
    private final IDrawable background;
    @Nonnull
    private final String localizedName;

    private IDrawableAnimated oxygenBar;
    private IDrawable oxygenLabel;

    public OxygenCompressorRecipeCategory(IGuiHelper guiHelper)
    {
        this.background = guiHelper.createDrawable(compressorTexture, 3, 4, 168, 80);
        this.localizedName = GCCoreUtil.translate("tile.oxygen_compressor.0.name");

        IDrawableStatic progressBarDrawableO2 = guiHelper.createDrawable(compressorTexture, 197, 7, 54, 7);
        this.oxygenBar = guiHelper.createAnimatedDrawable(progressBarDrawableO2, 70, IDrawableAnimated.StartDirection.RIGHT, true);
        this.oxygenLabel = guiHelper.createDrawable(compressorTexture, 187, 0, 10, 10);
    }

    @Nonnull
    @Override
    public String getUid()
    {
        return RecipeCategories.OXYGEN_COMPRESSOR_ID;
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
    public void drawAnimations(@Nonnull Minecraft minecraft)
    {
        this.oxygenLabel.draw(minecraft, 97, 15);
        this.oxygenBar.draw(minecraft, 110, 16);
    }

    @Override
    public void setRecipe(@Nonnull IRecipeLayout recipeLayout, @Nonnull IRecipeWrapper recipeWrapper)
    {
        IGuiItemStackGroup itemstacks = recipeLayout.getItemStacks();

        itemstacks.init(0, false, 129, 61);

        if (recipeWrapper instanceof OxygenCompressorRecipeWrapper)
        {
            OxygenCompressorRecipeWrapper recipeWrapperOC = (OxygenCompressorRecipeWrapper) recipeWrapper;

            itemstacks.setFromRecipe(0, recipeWrapperOC.getOutputs());
        }
    }
}
