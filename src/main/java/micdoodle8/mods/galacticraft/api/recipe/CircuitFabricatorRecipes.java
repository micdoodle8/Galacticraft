package micdoodle8.mods.galacticraft.api.recipe;

import com.google.common.collect.ImmutableMap;

import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

public class CircuitFabricatorRecipes
{
    private static HashMap<Object[], ItemStack> recipes = new HashMap<>();

    public static ArrayList<ArrayList<ItemStack>> slotValidItems = new ArrayList<>(5);

    /**
     * Input list must be array with 5 elements matching the 5 slots in the machine.  Use null if no
     * item is used in the slot.  Use a List<ItemStack> for Oredicted ingredients.
     * <p/>
     * 0: Diamond  1: Silicon  2: Silicon  3: Redstone dust  4: Recipe item
     *
     * @param output  ItemStack
     * @param inputList  Object array.  Must contain only null, ItemStack, or List<ItemStack>.
     * 
     * @return
     */
    public static void addRecipe(ItemStack output, Object[] inputList)
    {
        if (inputList.length != 5)
        {
            throw new RuntimeException("Invalid circuit fabricator recipe!  Inputs length should be 5, was " + inputList.length);
        }
        for (Object o : inputList)
        {
            if (o == null || o instanceof ItemStack)
            {
                continue;
            }
            if (o instanceof List<?> && ((List)o).size() > 0)
            {
                continue;
            }
            throw new RuntimeException("Invalid circuit fabricator recipe! " + o.toString());
        }

        CircuitFabricatorRecipes.recipes.put(inputList, output);
        validateItems(inputList);
    }

    /** Add the recipe ingredients to the valid items for each slot
     * 
     * @param inputList
     */
    private static void validateItems(Object[] inputList)
    {
        //First initialise the ArrayList if this is the first time it's used
        if (CircuitFabricatorRecipes.slotValidItems.size() == 0)
        {
            for (int i = 0; i < 5; i++)
            {
                ArrayList<ItemStack> entry = new ArrayList<>(3);
                CircuitFabricatorRecipes.slotValidItems.add(entry);
            }
        }
        //Now see if the recipe items are already valid for their slots, if not add them
        for (int i = 0; i < 5; i++)
        {
            Object input = inputList[i];
            if (input instanceof ItemStack)
            {
                validateItem(i, (ItemStack) input);
            }
            else if (input instanceof List<?>)
            {
                for (ItemStack stack : (List<ItemStack>)input)
                {
                    validateItem(i, stack);
                }
            }
        }
    }

    private static void validateItem(int i, ItemStack inputStack)
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
                    if (stack.getItem() != inputStack.getItem() || stack.getItemDamage() != inputStack.getItemDamage() || !ItemStack.areItemStackTagsEqual(stack, inputStack))
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
                        if (stack.getItem() == inputStack.getItem() && stack.getItemDamage() == inputStack.getItemDamage() && ItemStack.areItemStackTagsEqual(stack, inputStack))
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
    
    public static ImmutableMap<Object[], ItemStack> getRecipes()
    {
        return ImmutableMap.copyOf(recipes);
    }
    
    public static void replaceRecipeIngredient(ItemStack ingredient, List<ItemStack> replacement)
    {
        if (ingredient == null) return;
        CircuitFabricatorRecipes.slotValidItems.clear();

        for (Entry<Object[], ItemStack> recipe : CircuitFabricatorRecipes.recipes.entrySet())
        {
            for (int i = 0; i < 5; i++)
            {
                Object recipeStack = recipe.getKey()[i];
                if (recipeStack == null)
                {
                    continue;
                }

                if (recipeStack instanceof ItemStack)
                {
                    ItemStack stack = ((ItemStack) recipeStack); 
                    if (stack.getItem() == ingredient.getItem() && stack.getItemDamage() == ingredient.getItemDamage() && ItemStack.areItemStackTagsEqual(stack, ingredient))
                    {
                        recipe.getKey()[i] = replacement;
                    }
                }
                else if (recipeStack instanceof List<?>)
                {
                    boolean listMatchOne = false;
                    for (ItemStack stack : (List<ItemStack>)recipeStack)
                    {
                        if (stack.getItem() == ingredient.getItem() && stack.getItemDamage() == ingredient.getItemDamage() && ItemStack.areItemStackTagsEqual(stack, ingredient))
                        {
                            recipe.getKey()[i] = replacement;
                            break;
                        }
                    }
                }
            }

            CircuitFabricatorRecipes.validateItems(recipe.getKey());
        }
    }
}
