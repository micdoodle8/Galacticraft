package micdoodle8.mods.galacticraft.api.recipe;

import com.google.common.collect.ImmutableMap;

import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class CircuitFabricatorRecipes
{
    private static HashMap<Object[], ItemStack> recipes = new HashMap<>();

    public static ArrayList<ArrayList<ItemStack>> slotValidItems = new ArrayList<>(5);

    /**
     * Input list must be ItemStack array with 5 elements, contain null if no
     * item is used in the slot.
     * <p/>
     * 0 - Crystal slot 1 - Silicon slot 2 - Silicon slot 3 - Redstone slot 4 -
     * Optional slot
     *
     * @param output
     * @param inputList ItemStack array with length 5. Fill with stacks as explained
     *                  above
     * @return
     */
    public static void addRecipe(ItemStack output, Object[] inputList)
    {
        if (inputList.length != 5)
        {
            throw new RuntimeException("Invalid circuit fabricator recipe!");
        }

        CircuitFabricatorRecipes.recipes.put(inputList, output);

        //Add the recipe ingredients to the valid items for each slot
        //First initialise the ArrayList if this is the first time it's used
        if (CircuitFabricatorRecipes.slotValidItems.size() == 0)
        {
            for (int i = 0; i < 5; i++)
            {
                ArrayList<ItemStack> entry = new ArrayList<>();
                CircuitFabricatorRecipes.slotValidItems.add(entry);
            }
        }
        //Now see if the recipe items are already valid for their slots, if not add them
        for (int i = 0; i < 5; i++)
        {
            Object input = inputList[i];
            if (input instanceof ItemStack)
            {
                checkItem(i, (ItemStack) input);
            }
            else if (input instanceof List<?>)
            {
                for (ItemStack stack : (List<ItemStack>)input)
                {
                    checkItem(i, stack);
                }
            }
        }
    }
    
    private static void checkItem(int i, ItemStack inputStack)
    {
        if (inputStack == null)
        {
            return;
        }

        ArrayList<ItemStack> validItems = CircuitFabricatorRecipes.slotValidItems.get(i);

        boolean found = false;
        for (int j = 0; j < validItems.size(); j++)
        {
            if (inputStack.isItemEqual(validItems.get(j)))
            {
                found = true;
                break;
            }
        }
        if (!found)
        {
            validItems.add(inputStack.copy());
        }
    }

    /**
     * Gets the output ItemStack for the passed input.
     *
     * @param inputList ItemStack array of input items
     * @return The result ItemStack
     */
    public static ItemStack getOutputForInput(ItemStack[] inputList)
    {
        if (inputList.length != 5)
        {
            return null;
        }

        for (Entry<Object[], ItemStack> recipe : CircuitFabricatorRecipes.recipes.entrySet())
        {
            boolean found = true;
            for (int i = 0; i < 5; i++)
            {
                Object recipeStack = recipe.getKey()[i];
                ItemStack inputStack = inputList[i];

                if (recipeStack == null || inputStack == null)
                {
                    if (recipeStack != null || inputStack != null)
                    {
                        found = false;
                        break;
                    }
                }
                else if (recipeStack instanceof ItemStack)
                {
                    ItemStack stack = ((ItemStack) recipeStack); 
                    if (stack.getItem() != inputStack.getItem() || stack.getItemDamage() != inputStack.getItemDamage())
                    {
                        found = false;
                        break;
                    }
                }
                else if (recipeStack instanceof List<?>)
                {
                    boolean listMatchOne = false;
                    for (ItemStack stack : (List<ItemStack>)recipeStack)
                    {
                        if (stack.getItem() == inputStack.getItem() && stack.getItemDamage() == inputStack.getItemDamage())
                        {
                            listMatchOne = true;
                            break;
                        }
                    }
                    if (listMatchOne == false)
                    {
                        found = false;
                        break;
                    }
                }
            }

            if (found)
            {
                return recipe.getValue();
            }
        }

        return CircuitFabricatorRecipes.recipes.get(inputList);
    }
    
    
    public static void removeRecipe(ItemStack match)
    {
    	for (Iterator<Map.Entry<Object[], ItemStack>> it = CircuitFabricatorRecipes.recipes.entrySet().iterator(); it.hasNext(); )
        {
            Entry<Object[], ItemStack> recipe = it.next();
    		if (ItemStack.areItemStacksEqual(match, recipe.getValue()))
            	it.remove();
        }
    }

    public static ImmutableMap<Object[], ItemStack> getRecipes()
    {
        return ImmutableMap.copyOf(recipes);
    }
}
