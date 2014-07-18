package codechicken.nei.recipe;

import codechicken.nei.NEIClientUtils;
import codechicken.nei.PositionedStack;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import static codechicken.nei.NEIClientUtils.translate;

public class FuelRecipeHandler extends FurnaceRecipeHandler
{
    public class CachedFuelRecipe extends CachedRecipe
    {
        public FuelPair fuel;

        public CachedFuelRecipe(FuelPair fuel) {
            this.fuel = fuel;
        }

        @Override
        public PositionedStack getIngredient() {
            return mfurnace.get(cycleticks / 48 % mfurnace.size()).ingred;
        }

        @Override
        public PositionedStack getResult() {
            return mfurnace.get(cycleticks / 48 % mfurnace.size()).result;
        }

        @Override
        public PositionedStack getOtherStack() {
            return fuel.stack;
        }
    }

    private ArrayList<SmeltingPair> mfurnace = new ArrayList<FurnaceRecipeHandler.SmeltingPair>();

    public FuelRecipeHandler() {
        super();
        loadAllSmelting();
    }

    public String getRecipeName() {
        return NEIClientUtils.translate("recipe.fuel");
    }

    private void loadAllSmelting() {
        Map<ItemStack, ItemStack> recipes = (Map<ItemStack, ItemStack>) FurnaceRecipes.smelting().getSmeltingList();

        for (Entry<ItemStack, ItemStack> recipe : recipes.entrySet())
            mfurnace.add(new SmeltingPair(recipe.getKey(), recipe.getValue()));
    }

    @Override
    public void loadCraftingRecipes(String outputId, Object... results) {
        if (outputId.equals("fuel") && getClass() == FuelRecipeHandler.class)
            for (FuelPair fuel : afuels)
                arecipes.add(new CachedFuelRecipe(fuel));
    }

    public void loadUsageRecipes(ItemStack ingredient) {
        for (FuelPair fuel : afuels)
            if (fuel.stack.contains(ingredient))
                arecipes.add(new CachedFuelRecipe(fuel));
    }

    public String getOverlayIdentifier() {
        return "fuel";
    }

    @Override
    public List<String> handleItemTooltip(GuiRecipe gui, ItemStack stack, List<String> currenttip, int recipe) {
        CachedFuelRecipe crecipe = (CachedFuelRecipe) arecipes.get(recipe);
        FuelPair fuel = crecipe.fuel;
        float burnTime = fuel.burnTime / 200F;

        if (gui.isMouseOver(fuel.stack, recipe) && burnTime < 1) {
            burnTime = 1F / burnTime;
            String s_time = Float.toString(burnTime);
            if (burnTime == Math.round(burnTime))
                s_time = Integer.toString((int) burnTime);

            currenttip.add(translate("recipe.fuel.required", s_time));
        } else if ((gui.isMouseOver(crecipe.getResult(), recipe) || gui.isMouseOver(crecipe.getIngredient(), recipe)) && burnTime > 1) {
            String s_time = Float.toString(burnTime);
            if (burnTime == Math.round(burnTime))
                s_time = Integer.toString((int) burnTime);

            currenttip.add(translate("recipe.fuel." + (gui.isMouseOver(crecipe.getResult(), recipe) ? "produced" : "processed"), s_time));
        }

        return currenttip;
    }
}
