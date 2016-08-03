package codechicken.nei.recipe;

import codechicken.nei.ItemStackSet;
import codechicken.nei.NEIClientUtils;
import codechicken.nei.NEIServerUtils;
import codechicken.nei.PositionedStack;
import codechicken.nei.api.API;
import codechicken.nei.api.ItemFilter;
import com.google.common.base.Function;
import com.google.common.collect.Iterables;
import net.minecraft.client.gui.inventory.GuiBrewingStand;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemPotion;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.potion.PotionHelper;
import net.minecraftforge.common.brewing.*;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.TreeSet;

public class BrewingRecipeHandler extends TemplateRecipeHandler {

    public static class NEIBrewingRecipe {

        final PositionedStack input;
        final PositionedStack output;
        final PositionedStack ingredient;

        public NEIBrewingRecipe(AbstractBrewingRecipe<?> recipe) {
            input = new PositionedStack(recipe.input, 51, 35);
            output = new PositionedStack(recipe.output, 97, 35);
            ingredient = new PositionedStack(recipe.ingredient, 74, 6);
        }

        public NEIBrewingRecipe(ItemStack ingred, int basePotionID, int resultDamage) {
            input = new PositionedStack(new ItemStack(Items.potionitem, 1, basePotionID), 51, 35);
            output = new PositionedStack(new ItemStack(Items.potionitem, 1, resultDamage), 97, 35);
            ingredient = new PositionedStack(ingred, 74, 6);
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
        } else {
            super.loadCraftingRecipes(outputId, results);
        }
    }

    @Override
    public void loadCraftingRecipes(ItemStack result) {
        if (result.getItem() == Items.potionitem) {
            int damage = result.getItemDamage();
            for (NEIBrewingRecipe recipe : apotions) {
                if (recipe.output.item.getItemDamage() == damage) {
                    arecipes.add(new CachedBrewingRecipe(recipe));
                }
            }
        }

        for (IBrewingRecipe recipe : BrewingRecipeRegistry.getRecipes()) {
            if (recipe instanceof BrewingRecipe || recipe instanceof BrewingOreRecipe) {
                if (NEIServerUtils.areStacksSameType(((AbstractBrewingRecipe<?>) recipe).output, result)) {
                    arecipes.add(new CachedBrewingRecipe((AbstractBrewingRecipe<?>) recipe));
                }
            }
        }
    }

    @Override
    public void loadUsageRecipes(ItemStack ingredient) {
        if (ingredient.getItem() == Items.potionitem || ingredients.contains(ingredient)) {
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
                if (NEIServerUtils.areStacksSameType(arecipe.input, ingredient)) {
                    arecipes.add(new CachedBrewingRecipe(arecipe));
                } else {
                    ItemStack[] recipeIngredients = NEIServerUtils.extractRecipeItems(arecipe.ingredient);
                    for (ItemStack recipeIngredient : recipeIngredients) {
                        if (NEIServerUtils.areStacksSameType(recipeIngredient, ingredient)) {
                            arecipes.add(new CachedBrewingRecipe(arecipe));
                            continue nextRecipe;
                        }
                    }
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
        drawProgressBar(60, 1, 185, -2, 12, 30, 35, 3);
    }

    public static void searchPotions() {
        TreeSet<Integer> allPotions = new TreeSet<Integer>();
        HashSet<Integer> searchPotions = new HashSet<Integer>();
        searchPotions.add(0);
        allPotions.add(0);
        do {
            HashSet<Integer> newPotions = new HashSet<Integer>();
            for (Integer basePotion : searchPotions) {
                if (ItemPotion.isSplash(basePotion)) {
                    continue;
                }

                for (ItemStack ingred : ingredients.values()) {
                    int result = PotionHelper.applyIngredient(basePotion, ingred.getItem().getPotionEffect(ingred));

                    if (ItemPotion.isSplash(result)) {//splash potions qualify
                        addPotion(ingred, basePotion, result, allPotions, newPotions);
                        continue;
                    }

                    List<?> baseMods = Items.potionitem.getEffects(basePotion);
                    List<?> newMods = Items.potionitem.getEffects(result);//compare ID's
                    if (basePotion > 0 && baseMods == newMods || //same modifers and not water->empty
                            baseMods != null && (baseMods.equals(newMods) || newMods == null) || //modifiers different and doesn't lose modifiers
                            basePotion == result || //same potion
                            levelModifierChanged(basePotion, result))//redstone/glowstone cycle
                    {
                        continue;
                    }

                    addPotion(ingred, basePotion, result, allPotions, newPotions);
                }
            }

            searchPotions = newPotions;
        } while (!searchPotions.isEmpty());

        API.setItemListEntries(Items.potionitem, Iterables.transform(allPotions, new Function<Integer, ItemStack>()//override with only potions that can be crafted
        {
            @Override
            public ItemStack apply(Integer potionID) {
                return new ItemStack(Items.potionitem, 1, potionID);
            }
        }));
        API.addSubset("Items.Potions", new ItemStackSet().with(Items.potionitem));
        API.addSubset("Items.Potions.Splash", new ItemFilter() {
            @Override
            public boolean matches(ItemStack item) {
                return item.getItem() == Items.potionitem && (item.getItemDamage() & 0x4000) != 0;
            }
        });

        ItemStackSet positivepots = new ItemStackSet();
        ItemStackSet negativepots = new ItemStackSet();
        ItemStackSet neutralpots = new ItemStackSet();

        for (int potionID : allPotions) {
            List<PotionEffect> effectlist = Items.potionitem.getEffects(potionID);
            int type = 0;
            if (effectlist != null && !effectlist.isEmpty()) {
                for (PotionEffect potioneffect : effectlist) {
                    if (Potion.potionTypes[potioneffect.getPotionID()].isBadEffect()) {
                        type--;
                    } else {
                        type++;
                    }
                }
            }

            (type == 0 ? neutralpots : type > 0 ? positivepots : negativepots).add(new ItemStack(Items.potionitem, 1, potionID));
        }

        API.addSubset("Items.Potions.Positive", positivepots);
        API.addSubset("Items.Potions.Negative", negativepots);
        API.addSubset("Items.Potions.Neutral", neutralpots);
    }

    private static boolean levelModifierChanged(int basePotionID, int result) {
        int basemod = basePotionID & 0xE0;
        int resultmod = result & 0xE0;

        return basemod != 0 && basemod != resultmod;
    }

    private static void addPotion(ItemStack ingred, int basePotion, int result, TreeSet<Integer> allPotions, HashSet<Integer> newPotions) {
        apotions.add(new NEIBrewingRecipe(ingred, basePotion, result));
        if (allPotions.add(result))//it's new
        {
            newPotions.add(result);
        }
    }

    @Override
    public String getOverlayIdentifier() {
        return "brewing";
    }
}
