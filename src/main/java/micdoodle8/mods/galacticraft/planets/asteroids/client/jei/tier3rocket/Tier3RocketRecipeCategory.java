package micdoodle8.mods.galacticraft.planets.asteroids.client.jei.tier3rocket;

import mezz.jei.api.IGuiHelper;
import mezz.jei.api.gui.IDrawable;
import mezz.jei.api.gui.IGuiItemStackGroup;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.recipe.BlankRecipeCategory;
import mezz.jei.api.recipe.IRecipeWrapper;
import micdoodle8.mods.galacticraft.core.client.jei.RecipeCategories;
import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
import micdoodle8.mods.galacticraft.planets.GalacticraftPlanets;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnull;
import java.util.List;

public class Tier3RocketRecipeCategory extends BlankRecipeCategory
{
    private static final ResourceLocation rocketGuiTexture = new ResourceLocation(GalacticraftPlanets.ASSET_PREFIX, "textures/gui/schematic_rocket_t3_recipe.png");

    @Nonnull
    private final IDrawable background;
    @Nonnull
    private final String localizedName;

    public Tier3RocketRecipeCategory(IGuiHelper guiHelper)
    {
        this.background = guiHelper.createDrawable(rocketGuiTexture, 0, 0, 168, 126);
        this.localizedName = GCCoreUtil.translate("tile.rocket_workbench.name");

    }

    @Nonnull
    @Override
    public String getUid()
    {
        return RecipeCategories.ROCKET_T3_ID;
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

        itemstacks.init(0, true, 44, 0);
        itemstacks.init(1, true, 35, 18);
        itemstacks.init(2, true, 35, 36);
        itemstacks.init(3, true, 35, 54);
        itemstacks.init(4, true, 35, 72);
        itemstacks.init(5, true, 35, 90);
        itemstacks.init(6, true, 53, 18);
        itemstacks.init(7, true, 53, 36);
        itemstacks.init(8, true, 53, 54);
        itemstacks.init(9, true, 53, 72);
        itemstacks.init(10, true, 53, 90);
        itemstacks.init(11, true, 17, 72); // Booster left
        itemstacks.init(12, true, 17, 90);
        itemstacks.init(13, true, 71, 90);
        itemstacks.init(14, true, 44, 108); // Rocket
        itemstacks.init(15, true, 71, 72); // Booster right
        itemstacks.init(16, true, 17, 108);
        itemstacks.init(17, true, 71, 108);
        itemstacks.init(18, true, 89, 7);
        itemstacks.init(19, true, 115, 7);
        itemstacks.init(20, true, 141, 7);
        itemstacks.init(21, false, 138, 95);

        if (recipeWrapper instanceof Tier3RocketRecipeWrapper)
        {
            Tier3RocketRecipeWrapper rocketRecipeWrapper = (Tier3RocketRecipeWrapper) recipeWrapper;
            List inputs = rocketRecipeWrapper.getInputs();

            for (int i = 0; i < inputs.size(); ++i)
            {
                Object o = inputs.get(i);
                if (o != null)
                {
                    itemstacks.setFromRecipe(i, o);
                }
            }
            itemstacks.setFromRecipe(21, rocketRecipeWrapper.getOutputs());
        }
    }
}
