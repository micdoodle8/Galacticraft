package codechicken.nei.recipe;

import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.TreeSet;

import codechicken.nei.ItemList;
import codechicken.nei.NEIClientUtils;
import codechicken.nei.NEIServerUtils;
import codechicken.nei.PositionedStack;
import net.minecraft.block.Block;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.gui.inventory.GuiFurnace;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntityFurnace;

public class FurnaceRecipeHandler extends TemplateRecipeHandler
{
    public class SmeltingPair extends CachedRecipe
    {
        public SmeltingPair(ItemStack ingred, ItemStack result)
        {
            ingred.stackSize = 1;
            this.ingred = new PositionedStack(ingred, 51, 6);
            this.result = new PositionedStack(result, 111, 24);
        }
        
        public PositionedStack getIngredient()
        {
            int cycle = cycleticks / 48;
            if(ingred.item.getItemDamage() == -1)
            {
                PositionedStack stack = ingred.copy();
                int maxDamage = 0;
                do
                {
                    maxDamage++;
                    stack.item.setItemDamage(maxDamage);
                }
                while(NEIClientUtils.isValidItem(stack.item));
                
                stack.item.setItemDamage(cycle % maxDamage);
                return stack;
            }
            return ingred;
        }
        
        public PositionedStack getResult()
        {
            return result;
        }
        
        public PositionedStack getOtherStack() 
        {
            return afuels.get((cycleticks/48) % afuels.size()).stack;
        }
        
        PositionedStack ingred;
        PositionedStack result;
    }
    
    public static class FuelPair
    {
        public FuelPair(ItemStack ingred, int burnTime)
        {
            this.stack = new PositionedStack(ingred, 51, 42, false);
            this.burnTime = burnTime;
        }
        
        public PositionedStack stack;
        public int burnTime;
    }
    
    public static ArrayList<FuelPair> afuels;
    public static TreeSet<Integer> efuels;

    @Override
    public void loadTransferRects()
    {
        transferRects.add(new RecipeTransferRect(new Rectangle(50, 23, 18, 18), "fuel"));
        transferRects.add(new RecipeTransferRect(new Rectangle(74, 23, 24, 18), "smelting"));
    }
    
    @Override
    public Class<? extends GuiContainer> getGuiClass()
    {
        return GuiFurnace.class;
    }
    
    @Override
    public String getRecipeName()
    {
        return NEIClientUtils.translate("recipe.furnace");
    }
    
    @Override
    public TemplateRecipeHandler newInstance()
    {
        if(afuels == null)
            findFuels();
        return super.newInstance();
    }

    @Override
    public void loadCraftingRecipes(String outputId, Object... results)
    {
        if(outputId.equals("smelting") && getClass() == FurnaceRecipeHandler.class)//don't want subclasses getting a hold of this
        {
            HashMap<Integer, ItemStack> recipes = (HashMap<Integer, ItemStack>) FurnaceRecipes.smelting().getSmeltingList();
            HashMap<List<Integer>, ItemStack> metarecipes = (HashMap<List<Integer>, ItemStack>) FurnaceRecipes.smelting().getMetaSmeltingList();
            
            for(Entry<Integer, ItemStack> recipe : recipes.entrySet())
            {
                ItemStack item = recipe.getValue();
                arecipes.add(new SmeltingPair(new ItemStack(recipe.getKey(), 1, -1), item));
            }
            if(metarecipes == null)return;
            for(Entry<List<Integer>, ItemStack> recipe : metarecipes.entrySet())
            {
                ItemStack item = recipe.getValue();
                arecipes.add(new SmeltingPair(new ItemStack(recipe.getKey().get(0), 1, recipe.getKey().get(1)), item));
            }
        }
        else
        {
            super.loadCraftingRecipes(outputId, results);
        }
    }
    
    @Override
    public void loadCraftingRecipes(ItemStack result)
    {
        HashMap<Integer, ItemStack> recipes = (HashMap<Integer, ItemStack>) FurnaceRecipes.smelting().getSmeltingList();
        HashMap<List<Integer>, ItemStack> metarecipes = (HashMap<List<Integer>, ItemStack>) FurnaceRecipes.smelting().getMetaSmeltingList();
        
        for(Entry<Integer, ItemStack> recipe : recipes.entrySet())
        {
            ItemStack item = recipe.getValue();
            if(NEIServerUtils.areStacksSameType(item, result))
            {
                arecipes.add(new SmeltingPair(new ItemStack(recipe.getKey(), 1, -1), item));
            }
        }
        if(metarecipes == null)return;
        for(Entry<List<Integer>, ItemStack> recipe : metarecipes.entrySet())
        {
            ItemStack item = recipe.getValue();
            if(NEIServerUtils.areStacksSameType(item, result))
            {
                arecipes.add(new SmeltingPair(new ItemStack(recipe.getKey().get(0), 1, recipe.getKey().get(1)), item));
            }
        }
    }
    
    @Override
    public void loadUsageRecipes(String inputId, Object... ingredients)
    {
        if(inputId.equals("fuel") && getClass() == FurnaceRecipeHandler.class)//don't want subclasses getting a hold of this
        {
            loadCraftingRecipes("smelting");
        }
        else
        {
            super.loadUsageRecipes(inputId, ingredients);
        }
    }
    
    @Override
    public void loadUsageRecipes(ItemStack ingredient)
    {
        HashMap<Integer, ItemStack> recipes = (HashMap<Integer, ItemStack>) FurnaceRecipes.smelting().getSmeltingList();
        HashMap<List<Integer>, ItemStack> metarecipes = (HashMap<List<Integer>, ItemStack>) FurnaceRecipes.smelting().getMetaSmeltingList();
        
        for(Entry<Integer, ItemStack> recipe : recipes.entrySet())
        {
            ItemStack item = recipe.getValue();
            if(ingredient.itemID == recipe.getKey())
            {
                arecipes.add(new SmeltingPair(ingredient, item));
            }
        }
        if(metarecipes == null)return;
        for(Entry<List<Integer>, ItemStack> recipe : metarecipes.entrySet())
        {
            ItemStack item = recipe.getValue();
            if(ingredient.itemID == recipe.getKey().get(0) && ingredient.getItemDamage() == recipe.getKey().get(1))
            {
                arecipes.add(new SmeltingPair(ingredient, item));
            }
        }
    }
    
    @Override
    public String getGuiTexture()
    {
        return "textures/gui/container/furnace.png";
    }

    @Override
    public void drawExtras(int recipe)
    {
        drawProgressBar(51, 25, 176, 0, 14, 14, 48, 7);
        drawProgressBar(74, 23, 176, 14, 24, 16, 48, 0);
    }
    
    private static void removeFuels()
    {
        efuels = new TreeSet<Integer>();
        efuels.add(Block.mushroomCapBrown.blockID);
        efuels.add(Block.mushroomCapRed.blockID);
        efuels.add(Block.signPost.blockID);
        efuels.add(Block.signWall.blockID);
        efuels.add(Block.doorWood.blockID);
        efuels.add(Block.lockedChest.blockID);
    }
    
    private static void findFuels()
    {        
        afuels = new ArrayList<FuelPair>();
        for(ItemStack item : ItemList.items)
        {
            if(!efuels.contains(item.itemID))
            {
                int burnTime = TileEntityFurnace.getItemBurnTime(item);
                if(burnTime > 0)
                    afuels.add(new FuelPair(item.copy(), burnTime));
            }
        }
    }
        
    @Override
    public String getOverlayIdentifier()
    {
        return "smelting";
    }
    
    static
    {
        removeFuels();
    }
}
