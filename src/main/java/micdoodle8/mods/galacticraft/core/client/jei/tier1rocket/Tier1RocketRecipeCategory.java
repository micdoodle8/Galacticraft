package micdoodle8.mods.galacticraft.core.client.jei.tier1rocket;

import mezz.jei.api.IGuiHelper;
import mezz.jei.api.gui.IDrawable;
import mezz.jei.api.gui.IGuiItemStackGroup;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.recipe.BlankRecipeCategory;
import mezz.jei.api.recipe.IRecipeWrapper;
import micdoodle8.mods.galacticraft.core.Constants;
import micdoodle8.mods.galacticraft.core.client.jei.RecipeCategories;
import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnull;
import java.util.List;

public class Tier1RocketRecipeCategory extends BlankRecipeCategory
{
    private static final ResourceLocation rocketGuiTexture = new ResourceLocation(Constants.ASSET_PREFIX, "textures/gui/rocketbench.png");

    @Nonnull
    private final IDrawable background;
    @Nonnull
    private final String localizedName;

    public Tier1RocketRecipeCategory(IGuiHelper guiHelper)
    {
        this.background = guiHelper.createDrawable(rocketGuiTexture, 3, 4, 168, 130);
        this.localizedName = GCCoreUtil.translate("tile.rocket_workbench.name");

    }

    @Nonnull
    @Override
    public String getUid()
    {
        return RecipeCategories.ROCKET_T1_ID;
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
    public void setRecipe(@Nonnull IRecipeLayout recipeLayout, @Nonnull IRecipeWrapper recipeWrapper)
    {
        IGuiItemStackGroup itemstacks = recipeLayout.getItemStacks();

        itemstacks.init(0, true, 44, 14);
        itemstacks.init(1, true, 35, 32);
        itemstacks.init(2, true, 35, 50);
        itemstacks.init(3, true, 35, 68);
        itemstacks.init(4, true, 35, 86);
        itemstacks.init(5, true, 53, 32);
        itemstacks.init(6, true, 53, 50);
        itemstacks.init(7, true, 53, 68);
        itemstacks.init(8, true, 53, 86);
        itemstacks.init(9, true, 17, 104);
        itemstacks.init(10, true, 17, 86);
        itemstacks.init(11, true, 44, 104);
        itemstacks.init(12, true, 71, 86);
        itemstacks.init(13, true, 71, 104);
        itemstacks.init(14, true, 89, 7);
        itemstacks.init(15, true, 115, 7);
        itemstacks.init(16, true, 141, 7);
        itemstacks.init(17, false, 138, 91);

        if (recipeWrapper instanceof Tier1RocketRecipeWrapper)
        {
            Tier1RocketRecipeWrapper rocketRecipeWrapper = (Tier1RocketRecipeWrapper) recipeWrapper;
            List inputs = rocketRecipeWrapper.getInputs();

            for (int i = 0; i < inputs.size(); ++i)
            {
                Object o = inputs.get(i);
                if (o != null)
                {
                    itemstacks.setFromRecipe(i, o);
                }
            }
            itemstacks.setFromRecipe(17, rocketRecipeWrapper.getOutputs());
        }
    }
}
