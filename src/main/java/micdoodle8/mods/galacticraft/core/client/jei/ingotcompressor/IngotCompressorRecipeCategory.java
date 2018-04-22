package micdoodle8.mods.galacticraft.core.client.jei.ingotcompressor;

import java.util.List;

import mezz.jei.api.IGuiHelper;
import mezz.jei.api.gui.*;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.IRecipeCategory;
import mezz.jei.api.recipe.IRecipeWrapper;
import micdoodle8.mods.galacticraft.core.Constants;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.client.jei.GalacticraftJEI;
import micdoodle8.mods.galacticraft.core.client.jei.RecipeCategories;
import micdoodle8.mods.galacticraft.core.util.ConfigManagerCore;
import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnull;

public class IngotCompressorRecipeCategory implements IRecipeCategory
{
    private static final ResourceLocation compressorTex = new ResourceLocation(Constants.ASSET_PREFIX, "textures/gui/ingot_compressor.png");
    private static final ResourceLocation compressorTexBlank = new ResourceLocation(Constants.ASSET_PREFIX, "textures/gui/ingot_compressor_blank.png");

    @Nonnull
    private final IDrawable background;
    @Nonnull
    private final IDrawable backgroundBlank;
    @Nonnull
    private final String localizedName;
    @Nonnull
    private final IDrawableAnimated progressBar;
    
    private boolean drawNothing = false; 

    public IngotCompressorRecipeCategory(IGuiHelper guiHelper)
    {
        this.background = guiHelper.createDrawable(compressorTex, 18, 17, 137, 78);
        this.backgroundBlank = guiHelper.createDrawable(compressorTexBlank, 18, 17, 137, 78);
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
        if (this.drawNothing)
        {
            return this.backgroundBlank;
        }
        return this.background;
    }

    @Override
    public void drawExtras(@Nonnull Minecraft minecraft)
    {
        if (!this.drawNothing) this.progressBar.draw(minecraft, 59, 19);
    }

    @Override
    public void setRecipe(IRecipeLayout recipeLayout, IRecipeWrapper recipeWrapper, IIngredients ingredients)
    {
        this.drawNothing = GalacticraftJEI.hidden.contains(recipeWrapper);
        if (this.drawNothing) return;

        IGuiItemStackGroup itemstacks = recipeLayout.getItemStacks();

        for (int j = 0; j < 9; j++)
        {
            itemstacks.init(j, true, j % 3 * 18, j / 3 * 18);
        }

        itemstacks.init(9, false, 119, 20);

        if (ConfigManagerCore.quickMode)
        {
            List<ItemStack> output = ingredients.getOutputs(ItemStack.class).get(0);
            ItemStack stackOutput = output.get(0);
            if (stackOutput.getItem().getUnlocalizedName(stackOutput).contains("compressed"))
            {
                ItemStack stackDoubled = stackOutput.copy();
                stackDoubled.setCount(stackOutput.getCount() * 2);
                output.set(0, stackDoubled);
            }
        }

        itemstacks.set(ingredients);
    }

    @Override
    public String getModName()
    {
        return GalacticraftCore.NAME;
    }
}
