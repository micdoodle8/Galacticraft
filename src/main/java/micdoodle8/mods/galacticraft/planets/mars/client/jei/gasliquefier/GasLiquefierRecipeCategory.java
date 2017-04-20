package micdoodle8.mods.galacticraft.planets.mars.client.jei.gasliquefier;

import mezz.jei.api.IGuiHelper;
import mezz.jei.api.gui.*;
import mezz.jei.api.recipe.BlankRecipeCategory;
import mezz.jei.api.recipe.IRecipeWrapper;
import micdoodle8.mods.galacticraft.core.GCItems;
import micdoodle8.mods.galacticraft.core.client.jei.RecipeCategories;
import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
import micdoodle8.mods.galacticraft.planets.GalacticraftPlanets;
import micdoodle8.mods.galacticraft.planets.asteroids.items.AsteroidsItems;
import net.minecraft.client.Minecraft;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnull;

public class GasLiquefierRecipeCategory extends BlankRecipeCategory
{
    private static final ResourceLocation refineryGuiTex = new ResourceLocation(GalacticraftPlanets.ASSET_PREFIX, "textures/gui/gas_liquefier_recipe.png");
    private static final ResourceLocation gasesTex = new ResourceLocation(GalacticraftPlanets.ASSET_PREFIX, "textures/gui/gases_methane_oxygen_nitrogen.png");

    @Nonnull
    private final IDrawable background;
    @Nonnull
    private final String localizedName;
    @Nonnull
    private final IDrawableAnimated methaneBarInput;
    @Nonnull
    private final IDrawableAnimated oxygenBarInput;
    @Nonnull
    private final IDrawableAnimated nitrogenBarInput;
    @Nonnull
    private final IDrawableAnimated fuelBarOutput;
    @Nonnull
    private final IDrawableAnimated oxygenBarOutput;
    @Nonnull
    private final IDrawableAnimated nitrogenBarOutput;

    private int inputGas = 2;
    private int outputGas = 2;

    public GasLiquefierRecipeCategory(IGuiHelper guiHelper)
    {
        this.background = guiHelper.createDrawable(refineryGuiTex, 3, 4, 168, 64);
        this.localizedName = GCCoreUtil.translate("tile.mars_machine.4.name");

        IDrawableStatic methaneBar = guiHelper.createDrawable(gasesTex, 1, 0, 16, 38);
        this.methaneBarInput = guiHelper.createAnimatedDrawable(methaneBar, 70, IDrawableAnimated.StartDirection.TOP, true);
        IDrawableStatic oxygenBar = guiHelper.createDrawable(gasesTex, 18, 0, 16, 38);
        this.oxygenBarInput = guiHelper.createAnimatedDrawable(oxygenBar, 70, IDrawableAnimated.StartDirection.TOP, true);
        IDrawableStatic atmosphereBar = guiHelper.createDrawable(gasesTex, 34, 0, 16, 38);
        this.nitrogenBarInput = guiHelper.createAnimatedDrawable(atmosphereBar, 70, IDrawableAnimated.StartDirection.TOP, true);
        IDrawableStatic fuelBar = guiHelper.createDrawable(refineryGuiTex, 176, 0, 16, 38);
        this.fuelBarOutput = guiHelper.createAnimatedDrawable(fuelBar, 70, IDrawableAnimated.StartDirection.BOTTOM, false);
        this.oxygenBarOutput = guiHelper.createAnimatedDrawable(oxygenBar, 70, IDrawableAnimated.StartDirection.BOTTOM, false);
        IDrawableStatic nitrogenBar = guiHelper.createDrawable(gasesTex, 68, 0, 16, 38);
        this.nitrogenBarOutput = guiHelper.createAnimatedDrawable(nitrogenBar, 70, IDrawableAnimated.StartDirection.BOTTOM, false);
    }

    @Nonnull
    @Override
    public String getUid()
    {
        return RecipeCategories.GAS_LIQUEFIER_ID;
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
        IDrawableAnimated input;
        IDrawableAnimated output;

        switch (this.inputGas)
        {
        case 0:
            input = this.methaneBarInput;
            break;
        case 1:
            input = this.oxygenBarInput;
            break;
        default:
            input = this.nitrogenBarInput;
            break;
        }

        switch (this.outputGas)
        {
        case 0:
            output = this.fuelBarOutput;
            break;
        case 3:
            output = this.oxygenBarOutput;
            break;
        default:
            output = this.nitrogenBarOutput;
            break;
        }

        input.draw(minecraft, 40, 24);
        output.draw(minecraft, this.outputGas == 0 || this.outputGas == 3 ? 124 : 105, 24);
    }

    @Override
    public void setRecipe(@Nonnull IRecipeLayout recipeLayout, @Nonnull IRecipeWrapper recipeWrapper)
    {
        IGuiItemStackGroup itemstacks = recipeLayout.getItemStacks();

        itemstacks.init(0, true, 39, 2);
        itemstacks.init(1, false, 104, 2);
        itemstacks.init(2, false, 123, 2);

        if (recipeWrapper instanceof GasLiquefierRecipeWrapper)
        {
            GasLiquefierRecipeWrapper gasLiquefierRecipeWrapper = (GasLiquefierRecipeWrapper) recipeWrapper;
            ItemStack input = ((ItemStack) gasLiquefierRecipeWrapper.getInputs().get(0));
            itemstacks.setFromRecipe(0, input);

            Item inputItem = input.getItem();
            if (inputItem == AsteroidsItems.methaneCanister)
            {
                this.inputGas = 0;
            }
            else if (inputItem == AsteroidsItems.canisterLOX)
            {
                this.inputGas = 1;
            }

            Item outputItem = gasLiquefierRecipeWrapper.getOutputs().get(0).getItem();
            if (outputItem == GCItems.fuelCanister)
            {
                this.outputGas = 0;
            }
            else if (outputItem == AsteroidsItems.canisterLOX)
            {
                this.outputGas = 3;
            }
            else
            {
                this.outputGas = 4;
            }

            itemstacks.setFromRecipe(this.outputGas == 0 || this.outputGas == 3 ? 2 : 1, gasLiquefierRecipeWrapper.getOutputs());
        }
    }
}
