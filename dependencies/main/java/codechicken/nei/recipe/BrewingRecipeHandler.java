package codechicken.nei.recipe;

import codechicken.nei.ItemStackSet;
import codechicken.nei.api.API;
import codechicken.nei.api.stack.PositionedStack;
import codechicken.nei.recipe.potion.IPotionRecipe;
import codechicken.nei.recipe.potion.PotionRecipeHelper;
import codechicken.nei.util.NEIClientUtils;
import codechicken.nei.util.NEIServerUtils;
import net.minecraft.client.gui.inventory.GuiBrewingStand;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.PotionEffect;
import net.minecraft.potion.PotionType;
import net.minecraftforge.common.brewing.*;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

//FIXME Need rewrite
public class BrewingRecipeHandler extends TemplateRecipeHandler {

    public static class NEIBrewingRecipe {

        final PositionedStack input;
        final PositionedStack output;
        final PositionedStack ingredient;

        public NEIBrewingRecipe(AbstractBrewingRecipe<?> recipe) {
            input = new PositionedStack(recipe.getInput(), 51, 40);
            output = new PositionedStack(recipe.getOutput(), 97, 40);
            ingredient = new PositionedStack(recipe.getIngredient(), 74, 6);
        }

        public NEIBrewingRecipe(IPotionRecipe recipe) {
            input = new PositionedStack(recipe.getRecipeInput(), 51, 40);
            output = new PositionedStack(recipe.getRecipeOutput(), 97, 40);
            ingredient = new PositionedStack(recipe.getRecipeIngredient(), 74, 6);
        }
    }

    public class CachedBrewingRecipe extends CachedRecipe {

        final NEIBrewingRecipe recipe;

        public CachedBrewingRecipe(NEIBrewingRecipe recipe) {
            this.recipe = recipe;
        }

        public CachedBrewingRecipe(AbstractBrewingRecipe<?> recipe) {
            this(new NEIBrewingRecipe(recipe));
        }

        public CachedBrewingRecipe(IPotionRecipe recipe) {
            this(new NEIBrewingRecipe(recipe));
        }

        @Override
        public PositionedStack getResult() {
            return recipe.output;
        }

        @Override
        public ArrayList<PositionedStack> getIngredients() {
            ArrayList<PositionedStack> recipestacks = new ArrayList<PositionedStack>();
            recipestacks.add(recipe.ingredient);
            recipestacks.add(recipe.input);
            return recipestacks;
        }
    }

    public static final ItemStackSet ingredients = new ItemStackSet();
    public static final HashSet<NEIBrewingRecipe> apotions = new HashSet<NEIBrewingRecipe>();

    @Override
    public void loadTransferRects() {
        transferRects.add(new RecipeTransferRect(new Rectangle(58, 3, 14, 30), "brewing"));
        transferRects.add(new RecipeTransferRect(new Rectangle(92, 3, 14, 30), "brewing"));
        transferRects.add(new RecipeTransferRect(new Rectangle(68, 23, 28, 18), "brewing"));
    }

    @Override
    public Class<? extends GuiContainer> getGuiClass() {
        return GuiBrewingStand.class;
    }

    @Override
    public String getRecipeName() {
        return NEIClientUtils.translate("recipe.brewing");
    }

    @Override
    public void loadCraftingRecipes(String outputId, Object... results) {
        if (outputId.equals("brewing") && getClass() == BrewingRecipeHandler.class) {// don't want subclasses getting a hold of this
            for (NEIBrewingRecipe recipe : apotions) {
                arecipes.add(new CachedBrewingRecipe(recipe));
            }
            for (IBrewingRecipe recipe : BrewingRecipeRegistry.getRecipes()) {
                if (recipe instanceof BrewingRecipe || recipe instanceof BrewingOreRecipe) {
                    arecipes.add(new CachedBrewingRecipe((AbstractBrewingRecipe<?>) recipe));
                }
            }
            for (IPotionRecipe recipe : PotionRecipeHelper.getRecipes()) {
                arecipes.add(new CachedBrewingRecipe(recipe));
            }
        } else {
            super.loadCraftingRecipes(outputId, results);
        }
    }

    @Override
    public void loadCraftingRecipes(ItemStack result) {
        if (result.getItem() == Items.POTIONITEM) {
            int damage = result.getItemDamage();
            for (NEIBrewingRecipe recipe : apotions) {
                if (recipe.output.item.getItemDamage() == damage) {
                    arecipes.add(new CachedBrewingRecipe(recipe));
                }
            }
        }

        for (IBrewingRecipe recipe : BrewingRecipeRegistry.getRecipes()) {
            if (recipe instanceof BrewingRecipe || recipe instanceof BrewingOreRecipe) {
                if (NEIServerUtils.areStacksSameType(((AbstractBrewingRecipe<?>) recipe).getOutput(), result)) {
                    arecipes.add(new CachedBrewingRecipe((AbstractBrewingRecipe<?>) recipe));
                }
            }
        }
        for (IPotionRecipe recipe : PotionRecipeHelper.getRecipes()) {
            if (NEIServerUtils.areStacksSameType(recipe.getRecipeOutput(), result)) {
                arecipes.add(new CachedBrewingRecipe(recipe));
            }
        }
    }

    @Override
    public void loadUsageRecipes(ItemStack ingredient) {
        if (ingredient.getItem() == Items.POTIONITEM || ingredients.contains(ingredient)) {
            for (NEIBrewingRecipe recipe : apotions) {
                if (NEIServerUtils.areStacksSameType(recipe.ingredient.item, ingredient) || NEIServerUtils.areStacksSameType(recipe.input.item, ingredient)) {
                    arecipes.add(new CachedBrewingRecipe(recipe));
                }
            }
        }

        nextRecipe:
        for (IBrewingRecipe recipe : BrewingRecipeRegistry.getRecipes()) {
            if (recipe instanceof BrewingRecipe || recipe instanceof BrewingOreRecipe) {
                AbstractBrewingRecipe<?> arecipe = (AbstractBrewingRecipe<?>) recipe;
                if (NEIServerUtils.areStacksSameType(arecipe.getInput(), ingredient)) {
                    arecipes.add(new CachedBrewingRecipe(arecipe));
                } else {
                    ItemStack[] recipeIngredients = NEIServerUtils.extractRecipeItems(arecipe.getIngredient());
                    for (ItemStack recipeIngredient : recipeIngredients) {
                        if (NEIServerUtils.areStacksSameType(recipeIngredient, ingredient)) {
                            arecipes.add(new CachedBrewingRecipe(arecipe));
                            continue nextRecipe;
                        }
                    }
                }
            }
        }
        for (IPotionRecipe recipe : PotionRecipeHelper.getRecipes()) {
            if (NEIServerUtils.areStacksSameType(recipe.getRecipeInput(), ingredient)) {
                arecipes.add(new CachedBrewingRecipe(recipe));
            } else {
                if (NEIServerUtils.areStacksSameType(recipe.getRecipeIngredient(), ingredient)) {
                    arecipes.add(new CachedBrewingRecipe(recipe));
                }
            }
        }

    }

    @Override
    public String getGuiTexture() {
        return "textures/gui/container/brewing_stand.png";
    }

    @Override
    public void drawExtras(int recipe) {
        drawProgressBar(92, 5, 176, 0, 8, 30, 120, 1);
        drawProgressBar(58, 1, 185, -2, 12, 30, 35, 3);
    }

    public static void searchPotions() {
        ArrayList<ItemStack> allPotions = new ArrayList<ItemStack>();
        for (IPotionRecipe recipe : PotionRecipeHelper.getRecipes()) {
            allPotions.add(recipe.getRecipeOutput());
        }

        ItemStackSet positiveEffects = new ItemStackSet();
        ItemStackSet negativeEffects = new ItemStackSet();
        ItemStackSet neutralEffects = new ItemStackSet();
        for (ItemStack potionStack : allPotions) {
            PotionType potionType = getPotionTypeFromStack(potionStack);
            if (potionType == null) {
                continue;
            }
            List<PotionEffect> stackEffects = potionType.getEffects();
            if (stackEffects.isEmpty()) {
                neutralEffects.add(potionStack);
                continue;
            }
            for (PotionEffect effect : stackEffects) {
                //If for some reason a vanilla potion has positive and negative effects, make sure we don't add it to the list more than once.
                if (effect.getPotion().isBadEffect()) {
                    if (!negativeEffects.contains(potionStack)) {
                        negativeEffects.add(potionStack);
                    }
                } else {
                    if (!positiveEffects.contains(potionStack)) {
                        positiveEffects.add(potionStack);
                    }
                }
            }
        }

        API.addSubset("Items.Potions", new ItemStackSet().with(Items.POTIONITEM).with(Items.SPLASH_POTION).with(Items.LINGERING_POTION));
        API.addSubset("Items.Potions.Splash", new ItemStackSet().with(Items.SPLASH_POTION));
        API.addSubset("Items.Potions.Lingering", new ItemStackSet().with(Items.LINGERING_POTION));
        API.addSubset("Items.Potions.Positive", positiveEffects);
        API.addSubset("Items.Potions.Negative", negativeEffects);
        API.addSubset("Items.Potions.Neutral", neutralEffects);
    }

    @Override
    public String getOverlayIdentifier() {
        return "brewing";
    }

    private static PotionType getPotionTypeFromStack(ItemStack itemStack) {
        if (itemStack.hasTagCompound()) {
            NBTTagCompound tagCompound = itemStack.getTagCompound();
            if (tagCompound.hasKey("Potion")) {
                String potion = tagCompound.getString("Potion");
                PotionType type = PotionType.getPotionTypeForName(potion);
                if (type != null) {
                    return type;
                }
            }
        }
        return null;
    }

}
