package codechicken.nei.recipe;

import codechicken.nei.ItemList;
import codechicken.nei.api.stack.PositionedStack;
import codechicken.nei.util.NEIClientUtils;
import codechicken.nei.util.NEIServerUtils;
import net.minecraft.block.Block;
import net.minecraft.block.BlockDoor;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.gui.inventory.GuiFurnace;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.tileentity.TileEntityFurnace;

import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.Map.Entry;

public class FurnaceRecipeHandler extends TemplateRecipeHandler {
    public class SmeltingPair extends CachedRecipe {
        public SmeltingPair(ItemStack ingred, ItemStack result) {
            ingred.stackSize = 1;
            this.ingred = new PositionedStack(ingred, 51, 6);
            this.result = new PositionedStack(result, 111, 24);
        }

        public List<PositionedStack> getIngredients() {
            return getCycledIngredients(cycleticks / 48, Arrays.asList(ingred));
        }

        public PositionedStack getResult() {
            return result;
        }

        public PositionedStack getOtherStack() {
            return afuels.get((cycleticks / 48) % afuels.size()).stack;
        }

        PositionedStack ingred;
        PositionedStack result;
    }

    public static class FuelPair {
        public FuelPair(ItemStack ingred, int burnTime) {
            this.stack = new PositionedStack(ingred, 51, 42, false);
            this.burnTime = burnTime;
        }

        public PositionedStack stack;
        public int burnTime;
    }

    public static ArrayList<FuelPair> afuels;
    public static HashSet<Block> efuels;

    @Override
    public void loadTransferRects() {
        transferRects.add(new RecipeTransferRect(new Rectangle(50, 23, 18, 18), "fuel"));
        transferRects.add(new RecipeTransferRect(new Rectangle(74, 23, 24, 18), "smelting"));
    }

    @Override
    public Class<? extends GuiContainer> getGuiClass() {
        return GuiFurnace.class;
    }

    @Override
    public String getRecipeName() {
        return NEIClientUtils.translate("recipe.furnace");
    }

    @Override
    public TemplateRecipeHandler newInstance() {
        if (afuels == null || afuels.isEmpty()) {
            findFuels();
        }
        return super.newInstance();
    }

    @Override
    public void loadCraftingRecipes(String outputId, Object... results) {
        if (outputId.equals("smelting") && getClass() == FurnaceRecipeHandler.class) {//don't want subclasses getting a hold of this
            Map<ItemStack, ItemStack> recipes = FurnaceRecipes.instance().getSmeltingList();
            for (Entry<ItemStack, ItemStack> recipe : recipes.entrySet()) {
                arecipes.add(new SmeltingPair(recipe.getKey(), recipe.getValue()));
            }
        } else {
            super.loadCraftingRecipes(outputId, results);
        }
    }

    @Override
    public void loadCraftingRecipes(ItemStack result) {
        Map<ItemStack, ItemStack> recipes = FurnaceRecipes.instance().getSmeltingList();
        for (Entry<ItemStack, ItemStack> recipe : recipes.entrySet()) {
            if (NEIServerUtils.areStacksSameType(recipe.getValue(), result)) {
                arecipes.add(new SmeltingPair(recipe.getKey(), recipe.getValue()));
            }
        }
    }

    @Override
    public void loadUsageRecipes(String inputId, Object... ingredients) {
        if (inputId.equals("fuel") && getClass() == FurnaceRecipeHandler.class)//don't want subclasses getting a hold of this
        {
            loadCraftingRecipes("smelting");
        } else {
            super.loadUsageRecipes(inputId, ingredients);
        }
    }

    @Override
    public void loadUsageRecipes(ItemStack ingredient) {
        Map<ItemStack, ItemStack> recipes = FurnaceRecipes.instance().getSmeltingList();
        for (Entry<ItemStack, ItemStack> recipe : recipes.entrySet()) {
            if (NEIServerUtils.areStacksSameTypeCrafting(recipe.getKey(), ingredient)) {
                SmeltingPair arecipe = new SmeltingPair(recipe.getKey(), recipe.getValue());
                arecipe.setIngredientPermutation(Arrays.asList(arecipe.ingred), ingredient);
                arecipes.add(arecipe);
            }
        }
    }

    @Override
    public String getGuiTexture() {
        return "textures/gui/container/furnace.png";
    }

    @Override
    public void drawExtras(int recipe) {
        drawProgressBar(51, 25, 176, 0, 14, 14, 48, 7);
        drawProgressBar(74, 23, 176, 14, 24, 16, 48, 0);
    }

    private static Set<Item> excludedFuels() {
        Set<Item> efuels = new HashSet<Item>();
        efuels.add(Item.getItemFromBlock(Blocks.BROWN_MUSHROOM));
        efuels.add(Item.getItemFromBlock(Blocks.RED_MUSHROOM));
        efuels.add(Item.getItemFromBlock(Blocks.STANDING_SIGN));
        efuels.add(Item.getItemFromBlock(Blocks.WALL_SIGN));
        efuels.add(Item.getItemFromBlock(Blocks.TRAPPED_CHEST));
        return efuels;
    }

    private static void findFuels() {
        afuels = new ArrayList<FuelPair>();
        Set<Item> efuels = excludedFuels();
        for (ItemStack item : ItemList.items) {
            Block block = Block.getBlockFromItem(item.getItem());
            if (block instanceof BlockDoor) {
                continue;
            }
            if (efuels.contains(item.getItem())) {
                continue;
            }

            int burnTime = TileEntityFurnace.getItemBurnTime(item);
            if (burnTime > 0) {
                afuels.add(new FuelPair(item.copy(), burnTime));
            }
        }
    }

    @Override
    public String getOverlayIdentifier() {
        return "smelting";
    }
}
