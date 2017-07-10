package micdoodle8.mods.galacticraft.api.recipe;

import micdoodle8.mods.galacticraft.api.GalacticraftConfigAccess;
import net.minecraft.block.Block;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.world.World;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CompressorRecipes
{
    private static List<IRecipe> recipes = new ArrayList<>();
    private static List<IRecipe> recipesAdventure = new ArrayList<>();
    private static boolean adventureOnly = false;
    private static Field adventureFlag;
    private static boolean flagNotCached = true;

    public static ShapedRecipesGC addRecipe(ItemStack output, Object... inputList)
    {
        StringBuilder s = new StringBuilder();
        int i = 0;
        int j = 0;
        int k = 0;

        if (inputList[i] instanceof String[])
        {
            String[] astring = (String[]) inputList[i++];

            for (String s1 : astring)
            {
                ++k;
                j = s1.length();
                s.append(s1);
            }
        }
        else
        {
            while (inputList[i] instanceof String)
            {
                String s2 = (String) inputList[i++];
                ++k;
                j = s2.length();
                s.append(s2);
            }
        }

        HashMap<Character, ItemStack> hashmap;

        for (hashmap = new HashMap<>(); i < inputList.length; i += 2)
        {
            Character character = (Character) inputList[i];
            ItemStack itemstack1 = null;

            if (inputList[i + 1] instanceof Item)
            {
                itemstack1 = new ItemStack((Item) inputList[i + 1]);
            }
            else if (inputList[i + 1] instanceof Block)
            {
                itemstack1 = new ItemStack((Block) inputList[i + 1], 1, 32767);
            }
            else if (inputList[i + 1] instanceof ItemStack)
            {
                itemstack1 = (ItemStack) inputList[i + 1];
            }

            hashmap.put(character, itemstack1);
        }

        ItemStack[] aitemstack = new ItemStack[j * k];

        for (int i1 = 0; i1 < j * k; ++i1)
        {
            char c0 = s.charAt(i1);

            if (hashmap.containsKey(c0))
            {
                aitemstack[i1] = hashmap.get(c0).copy();
            }
            else
            {
                aitemstack[i1] = null;
            }
        }

        ShapedRecipesGC shapedRecipes = new ShapedRecipesGC(j, k, aitemstack, output);
        if (!adventureOnly) CompressorRecipes.recipes.add(shapedRecipes);
        CompressorRecipes.recipesAdventure.add(shapedRecipes);
        return shapedRecipes;
    }

    public static void addShapelessRecipe(ItemStack par1ItemStack, Object... par2ArrayOfObj)
    {
        ArrayList<Object> arraylist = new ArrayList<>();
        int i = par2ArrayOfObj.length;

        for (Object object1 : par2ArrayOfObj)
        {
            if (object1 instanceof ItemStack)
            {
                arraylist.add(((ItemStack) object1).copy());
            }
            else if (object1 instanceof Item)
            {
                arraylist.add(new ItemStack((Item) object1));
            }
            else if (object1 instanceof String)
            {
                arraylist.add(object1);
            }
            else
            {
                if (!(object1 instanceof Block))
                {
                    throw new RuntimeException("Invalid shapeless compressor recipe!");
                }

                arraylist.add(new ItemStack((Block) object1));
            }
        }

        IRecipe toAdd = new ShapelessOreRecipeGC(par1ItemStack, arraylist.toArray());
        if (!adventureOnly) CompressorRecipes.recipes.add(toAdd);
        CompressorRecipes.recipesAdventure.add(toAdd);
    }

    public static ShapedRecipesGC addRecipeAdventure(ItemStack output, Object... inputList)
    {
    	adventureOnly = true;
        ShapedRecipesGC returnValue = CompressorRecipes.addRecipe(output, inputList);
    	adventureOnly = false;
    	return returnValue;
    }
    
    public static void addShapelessAdventure(ItemStack par1ItemStack, Object... par2ArrayOfObj)
    {
    	adventureOnly = true;
    	CompressorRecipes.addShapelessRecipe(par1ItemStack, par2ArrayOfObj);
    	adventureOnly = false;
    }
    
    public static ItemStack findMatchingRecipe(InventoryCrafting inventory, World par2World)
    {
        int i = 0;
        ItemStack itemstack = ItemStack.EMPTY;
        ItemStack itemstack1 = ItemStack.EMPTY;
        int j;

        for (j = 0; j < inventory.getSizeInventory(); ++j)
        {
            ItemStack itemstack2 = inventory.getStackInSlot(j);

            if (!itemstack2.isEmpty())
            {
                if (i == 0)
                {
                    itemstack = itemstack2;
                }

                if (i == 1)
                {
                    itemstack1 = itemstack2;
                }

                ++i;
            }
        }

        if (i == 2 && itemstack.getItem() == itemstack1.getItem() && itemstack.getCount() == 1 && itemstack1.getCount() == 1 && itemstack.getItem().isRepairable())
        {
            int k = itemstack.getItem().getMaxDamage() - itemstack.getItemDamage();
            int l = itemstack.getItem().getMaxDamage() - itemstack1.getItemDamage();
            int i1 = k + l + itemstack.getItem().getMaxDamage() * 5 / 100;
            int j1 = itemstack.getItem().getMaxDamage() - i1;

            if (j1 < 0)
            {
                j1 = 0;
            }

            return new ItemStack(itemstack.getItem(), 1, j1);
        }
        else
        {
            List<IRecipe> theRecipes = CompressorRecipes.getRecipeList();
        	
        	for (j = 0; j < theRecipes.size(); ++j)
            {
                IRecipe irecipe = theRecipes.get(j);

                if (irecipe instanceof ShapedRecipesGC && irecipe.matches(inventory, par2World))
                {
                    return irecipe.getRecipeOutput().copy();
                }
                else if (irecipe instanceof ShapelessOreRecipeGC && irecipe.matches(inventory, par2World))
                {
                    return irecipe.getRecipeOutput().copy();
                }
            }

            return ItemStack.EMPTY;
        }
    }

    public static List<IRecipe> getRecipeList()
    {
    	return (GalacticraftConfigAccess.getChallengeRecipes()) ? CompressorRecipes.recipesAdventure : CompressorRecipes.recipes;
    }
    
    public static void removeRecipe(ItemStack match)
    {
        CompressorRecipes.getRecipeList().removeIf(irecipe -> ItemStack.areItemStacksEqual(match, irecipe.getRecipeOutput()));
    }
}
