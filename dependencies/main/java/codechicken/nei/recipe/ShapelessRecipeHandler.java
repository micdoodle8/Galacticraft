package codechicken.nei.recipe;

import codechicken.nei.NEIClientUtils;
import codechicken.nei.NEIServerUtils;
import codechicken.nei.PositionedStack;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.ShapelessRecipes;
import net.minecraftforge.oredict.ShapelessOreRecipe;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ShapelessRecipeHandler extends ShapedRecipeHandler
{
    public int[][] stackorder = new int[][]{
            {0, 0},
            {1, 0},
            {0, 1},
            {1, 1},
            {0, 2},
            {1, 2},
            {2, 0},
            {2, 1},
            {2, 2}};

    public class CachedShapelessRecipe extends CachedRecipe
    {
        public CachedShapelessRecipe() {
            ingredients = new ArrayList<PositionedStack>();
        }

        public CachedShapelessRecipe(ItemStack output) {
            this();
            setResult(output);
        }

        public CachedShapelessRecipe(Object[] input, ItemStack output) {
            this(Arrays.asList(input), output);
        }

        public CachedShapelessRecipe(List<?> input, ItemStack output) {
            this(output);
            setIngredients(input);
        }

        public void setIngredients(List<?> items) {
            ingredients.clear();
            for (int ingred = 0; ingred < items.size(); ingred++) {
                PositionedStack stack = new PositionedStack(items.get(ingred), 25 + stackorder[ingred][0] * 18, 6 + stackorder[ingred][1] * 18);
                stack.setMaxSize(1);
                ingredients.add(stack);
            }
        }

        public void setResult(ItemStack output) {
            result = new PositionedStack(output, 119, 24);
        }

        @Override
        public List<PositionedStack> getIngredients() {
            return getCycledIngredients(cycleticks / 20, ingredients);
        }

        @Override
        public PositionedStack getResult() {
            return result;
        }

        public ArrayList<PositionedStack> ingredients;
        public PositionedStack result;
    }

    public String getRecipeName() {
        return NEIClientUtils.translate("recipe.shapeless");
    }

    @Override
    public void loadCraftingRecipes(String outputId, Object... results) {
        if (outputId.equals("crafting") && getClass() == ShapelessRecipeHandler.class) {
            List<IRecipe> allrecipes = CraftingManager.getInstance().getRecipeList();
            for (IRecipe irecipe : allrecipes) {
                CachedShapelessRecipe recipe = null;
                if (irecipe instanceof ShapelessRecipes)
                    recipe = shapelessRecipe((ShapelessRecipes) irecipe);
                else if (irecipe instanceof ShapelessOreRecipe)
                    recipe = forgeShapelessRecipe((ShapelessOreRecipe) irecipe);

                if (recipe == null)
                    continue;

                arecipes.add(recipe);
            }
        } else {
            super.loadCraftingRecipes(outputId, results);
        }
    }

    @Override
    public void loadCraftingRecipes(ItemStack result) {
        List<IRecipe> allrecipes = CraftingManager.getInstance().getRecipeList();
        for (IRecipe irecipe : allrecipes) {
            if (NEIServerUtils.areStacksSameTypeCrafting(irecipe.getRecipeOutput(), result)) {
                CachedShapelessRecipe recipe = null;
                if (irecipe instanceof ShapelessRecipes)
                    recipe = shapelessRecipe((ShapelessRecipes) irecipe);
                else if (irecipe instanceof ShapelessOreRecipe)
                    recipe = forgeShapelessRecipe((ShapelessOreRecipe) irecipe);

                if (recipe == null)
                    continue;

                arecipes.add(recipe);
            }
        }
    }

    @Override
    public void loadUsageRecipes(ItemStack ingredient) {
        List<IRecipe> allrecipes = CraftingManager.getInstance().getRecipeList();
        for (IRecipe irecipe : allrecipes) {
            CachedShapelessRecipe recipe = null;
            if (irecipe instanceof ShapelessRecipes)
                recipe = shapelessRecipe((ShapelessRecipes) irecipe);
            else if (irecipe instanceof ShapelessOreRecipe)
                recipe = forgeShapelessRecipe((ShapelessOreRecipe) irecipe);

            if (recipe == null)
                continue;

            if (recipe.contains(recipe.ingredients, ingredient)) {
                recipe.setIngredientPermutation(recipe.ingredients, ingredient);
                arecipes.add(recipe);
            }
        }
    }

    private CachedShapelessRecipe shapelessRecipe(ShapelessRecipes recipe) {
        if(recipe.recipeItems == null) //because some mod subclasses actually do this
            return null;

        return new CachedShapelessRecipe(recipe.recipeItems, recipe.getRecipeOutput());
    }

    public CachedShapelessRecipe forgeShapelessRecipe(ShapelessOreRecipe recipe) {
        ArrayList<Object> items = recipe.getInput();

        for (Object item : items)
            if (item instanceof List && ((List<?>) item).isEmpty())//ore handler, no ores
                return null;

        return new CachedShapelessRecipe(items, recipe.getRecipeOutput());
    }

    @Override
    public boolean isRecipe2x2(int recipe) {
        return getIngredientStacks(recipe).size() <= 4;
    }
}
