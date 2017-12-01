package micdoodle8.mods.galacticraft.core.client.jei.ingotcompressor;

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
import java.util.List;

public class IngotCompressorRecipeCategory extends BlankRecipeCategory
{
    private static final ResourceLocation compressorTex = new ResourceLocation(Constants.ASSET_PREFIX, "textures/gui/ingot_compressor.png");

    @Nonnull
    private final IDrawable background;
    @Nonnull
    private final String localizedName;
    @Nonnull
    private final IDrawableAnimated progressBar;

    public IngotCompressorRecipeCategory(IGuiHelper guiHelper)
    {
        this.background = guiHelper.createDrawable(compressorTex, 18, 17, 137, 78);
        this.localizedName = GCCoreUtil.translate("tile.machine.3.name");

        IDrawableStatic progressBarDrawable = guiHelper.createDrawable(compressorTex, 176, 13, 52, 17);
        this.progressBar = guiHelper.createAnimatedDrawable(progressBarDrawable, 70, IDrawableAnimated.StartDirection.LEFT, false);
    }

    @Nonnull
    @Override
    public String getUid()
    {
        return RecipeCategories.INGOT_COMPRESSOR_ID;
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
        this.progressBar.draw(minecraft, 59, 20);
    }

    @Override
    public void setRecipe(@Nonnull IRecipeLayout recipeLayout, @Nonnull IRecipeWrapper recipeWrapper)
    {
        IGuiItemStackGroup itemstacks = recipeLayout.getItemStacks();

        for (int j = 0; j < 9; j++)
        {
            itemstacks.init(j, true, j % 3 * 18, j / 3 * 18);
        }

        itemstacks.init(9, false, 119, 20);

        if (recipeWrapper instanceof IngotCompressorShapedRecipeWrapper)
        {
            IngotCompressorShapedRecipeWrapper ingotCompressorRecipeWrapper = (IngotCompressorShapedRecipeWrapper) recipeWrapper;
            List inputs = ingotCompressorRecipeWrapper.getInputs();

            for (int i = 0; i < inputs.size(); ++i)
            {
                Object o = inputs.get(i);
                if (o != null)
                {
                    itemstacks.setFromRecipe(i, o);
                }
            }
            itemstacks.setFromRecipe(9, ingotCompressorRecipeWrapper.getOutputs());
        }
        else if (recipeWrapper instanceof IngotCompressorShapelessRecipeWrapper)
        {
            IngotCompressorShapelessRecipeWrapper ingotCompressorRecipeWrapper = (IngotCompressorShapelessRecipeWrapper) recipeWrapper;
            List inputs = ingotCompressorRecipeWrapper.getInputs();

            for (int i = 0; i < inputs.size(); ++i)
            {
                Object o = inputs.get(i);
                if (o != null)
                {
                    itemstacks.setFromRecipe(i, o);
                }
            }
            itemstacks.setFromRecipe(9, ingotCompressorRecipeWrapper.getOutputs());
        }
    }
}
