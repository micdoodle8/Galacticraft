package codechicken.nei.recipe;

import codechicken.nei.NEIClientUtils;
import codechicken.nei.NEIServerUtils;
import codechicken.nei.PositionedStack;
import net.minecraft.client.gui.inventory.GuiBrewingStand;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.TreeSet;

public class BrewingRecipeHandler extends TemplateRecipeHandler
{
    public class CachedBrewingRecipe extends CachedRecipe
    {
        public CachedBrewingRecipe(Item ingred, int basePotionID, int resultDamage) {
            precursorPotion = new PositionedStack(new ItemStack(Items.potionitem, 1, basePotionID), 51, 35);
            ingredient = new PositionedStack(new ItemStack(ingred, 1, 0), 74, 6);
            result = new PositionedStack(new ItemStack(Items.potionitem, 1, resultDamage), 97, 35);
            calculateHashcode();
        }

        @Override
        public PositionedStack getResult() {
            return result;
        }

        @Override
        public ArrayList<PositionedStack> getIngredients() {
            ArrayList<PositionedStack> recipestacks = new ArrayList<PositionedStack>();
            recipestacks.add(ingredient);
            recipestacks.add(precursorPotion);
            return recipestacks;
        }

        private void calculateHashcode() {
            hashcode = result.item.getItemDamage() << 16 + precursorPotion.item.getItemDamage();
            hashcode = 31 * hashcode + (Item.getIdFromItem(ingredient.item.getItem()) << 16 + ingredient.item.getItemDamage());
        }

        public boolean equals(Object obj) {
            if (!(obj instanceof CachedBrewingRecipe)) return false;
            CachedBrewingRecipe recipe2 = (CachedBrewingRecipe) obj;
            return result.item.getItemDamage() == recipe2.result.item.getItemDamage() &&
                    precursorPotion.item.getItemDamage() == recipe2.precursorPotion.item.getItemDamage() &&
                    NEIServerUtils.areStacksSameType(ingredient.item, recipe2.ingredient.item);
        }

        public int hashCode() {
            return hashcode;
        }

        int hashcode;
        PositionedStack precursorPotion;
        PositionedStack result;
        PositionedStack ingredient;
    }

    public static final HashSet<Item> ingredients = new HashSet<Item>();
    public static final HashSet<CachedBrewingRecipe> apotions = new HashSet<CachedBrewingRecipe>();

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
        if (outputId.equals("brewing") && getClass() == BrewingRecipeHandler.class)//don't want subclasses getting a hold of this
            for (CachedBrewingRecipe recipe : apotions)
                arecipes.add(recipe);
        else
            super.loadCraftingRecipes(outputId, results);
    }

    @Override
    public void loadCraftingRecipes(ItemStack result) {
        if (result.getItem() != Items.potionitem) return;
        int damage = result.getItemDamage();

        for (CachedBrewingRecipe recipe : apotions)
            if (recipe.result.item.getItemDamage() == damage)
                arecipes.add(recipe);
    }

    @Override
    public void loadUsageRecipes(ItemStack ingredient) {
        if (ingredient.getItem() != Items.potionitem && !ingredients.contains(ingredient.getItem())) return;

        for (CachedBrewingRecipe recipe : apotions)
            if (NEIServerUtils.areStacksSameType(recipe.ingredient.item, ingredient) || NEIServerUtils.areStacksSameType(recipe.precursorPotion.item, ingredient))
                arecipes.add(recipe);
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
    
    /*public void searchPotions()
    {
        TreeSet<Integer> allPotions = new TreeSet<Integer>();
        HashSet<Integer> nextLevelPotions = new HashSet<Integer>();
        nextLevelPotions.add(0);
        do
        {
            HashSet<Integer> newPotions = new HashSet<Integer>();
            for(Integer basePotionID : nextLevelPotions)
            {
                if(ItemPotion.isSplash(basePotionID))
                {
                    continue;
                }
                
                for(Item ingred : ingredients)
                {
                    int result = PotionHelper.applyIngredient(basePotionID, ingred.getPotionEffect(null));
                    
                    if(ItemPotion.isSplash(result))//splash potions qualify
                    {
                        addPotion(ingred, basePotionID, result, allPotions, newPotions);
                        continue;
                    }
                    List<?> baseMods = Items.potionitem.getEffects(basePotionID);
                    List<?> newMods = Items.potionitem.getEffects(result);//compare ID's
                    if(basePotionID > 0 && baseMods == newMods || //same modifers and not water->empty
                            baseMods != null && (baseMods.equals(newMods) || newMods == null) || //modifiers different and doesn't lose modifiers
                            basePotionID == result ||//same potion
                            levelModifierChanged(basePotionID, result))//redstone/glowstone cycle
                    {
                        continue;
                    }
                    addPotion(ingred, basePotionID, result, allPotions, newPotions);
                }
            }
            
            nextLevelPotions = newPotions;
        }
        while(nextLevelPotions.size() > 0);
        
        allPotions.add(0);
        //TODO brewing API.setItemDamageVariants(Items.potionitem, allPotions);
        API.addSetRange("Items.Potions", new MultiItemRange().add(Items.potionitem));
        API.addSetRange("Items.Potions.Splash", new MultiItemRange().add(Items.potionitem, 0x4000, 0x8000));

        MultiItemRange positivepots = new MultiItemRange();
        MultiItemRange negativepots = new MultiItemRange();
        MultiItemRange neutralpots = new MultiItemRange();
        
        for(int potionID : allPotions)
        {
            List<PotionEffect> effectlist = Items.potionitem.getEffects(potionID);
            int type = 0;
            if(effectlist != null && !effectlist.isEmpty())
            {
                for(PotionEffect potioneffect: effectlist)
                {
                    if(Potion.potionTypes[potioneffect.getPotionID()].isBadEffect())
                        type = -1;
                    else
                        type = 1;
                    break;
                }                
            }
            if(type == 0)
                neutralpots.add(Items.potionitem, potionID, potionID);
            else if(type == 1)
                positivepots.add(Items.potionitem, potionID, potionID);
            else if(type == -1)
                negativepots.add(Items.potionitem, potionID, potionID);
        }

        API.addSetRange("Items.Potions.Positive", positivepots);
        API.addSetRange("Items.Potions.Negative", negativepots);
        API.addSetRange("Items.Potions.Neutral", neutralpots);
    }*/

    private boolean levelModifierChanged(int basePotionID, int result) {
        int basemod = basePotionID & 0xE0;
        int resultmod = result & 0xE0;

        return basemod != 0 && basemod != resultmod;
    }

    private void addPotion(Item ingred, int basePotion, int result, TreeSet<Integer> allPotions, HashSet<Integer> newPotions) {
        apotions.add(new CachedBrewingRecipe(ingred, basePotion, result));
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
