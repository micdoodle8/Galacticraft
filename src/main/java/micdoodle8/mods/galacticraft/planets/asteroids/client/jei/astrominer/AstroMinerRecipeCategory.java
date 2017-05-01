package micdoodle8.mods.galacticraft.planets.asteroids.client.jei.astrominer;

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

public class AstroMinerRecipeCategory extends BlankRecipeCategory
{
    private static final ResourceLocation astroMinerGuiTexture = new ResourceLocation(GalacticraftPlanets.ASSET_PREFIX, "textures/gui/schematic_astro_miner.png");

    @Nonnull
    private final IDrawable background;
    @Nonnull
    private final String localizedName;

    public AstroMinerRecipeCategory(IGuiHelper guiHelper)
    {
        this.background = guiHelper.createDrawable(astroMinerGuiTexture, 3, 39, 168, 91);
        this.localizedName = GCCoreUtil.translate("tile.rocket_workbench.name");

    }

    @Nonnull
    @Override
    public String getUid()
    {
        return RecipeCategories.ASTRO_MINER_ID;
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

        itemstacks.init(0, true, 23, 21);
        itemstacks.init(1, true, 41, 21);
        itemstacks.init(2, true, 59, 21);
        itemstacks.init(3, true, 77, 21);
        itemstacks.init(4, true, 12, 39);
        itemstacks.init(5, true, 30, 39);
        itemstacks.init(6, true, 48, 39);
        itemstacks.init(7, true, 66, 39);
        itemstacks.init(8, true, 84, 39);
        itemstacks.init(9, true, 40, 57);
        itemstacks.init(10, true, 58, 57);
        itemstacks.init(11, true, 76, 57);
        itemstacks.init(12, true, 4, 63);
        itemstacks.init(13, true, 22, 63);
        itemstacks.init(14, false, 138, 58);

        if (recipeWrapper instanceof AstroMinerRecipeWrapper)
        {
            AstroMinerRecipeWrapper astroMinerRecipeWrapper = (AstroMinerRecipeWrapper) recipeWrapper;
            List inputs = astroMinerRecipeWrapper.getInputs();

            for (int i = 0; i < inputs.size(); ++i)
            {
                Object o = inputs.get(i);
                if (o != null)
                {
                    itemstacks.setFromRecipe(i, o);
                }
            }
            itemstacks.setFromRecipe(14, astroMinerRecipeWrapper.getOutputs());
        }
    }
}
