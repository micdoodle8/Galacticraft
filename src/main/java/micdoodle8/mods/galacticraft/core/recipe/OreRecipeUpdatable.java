package micdoodle8.mods.galacticraft.core.recipe;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import javax.annotation.Nonnull;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;

import micdoodle8.mods.galacticraft.api.recipe.IRecipeUpdatable;
import micdoodle8.mods.galacticraft.core.util.RecipeUtil;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.common.crafting.IRecipeFactory;
import net.minecraftforge.common.crafting.JsonContext;
import net.minecraftforge.common.crafting.CraftingHelper.ShapedPrimer;
import net.minecraftforge.oredict.OreIngredient;
import net.minecraftforge.oredict.ShapedOreRecipe;

public class OreRecipeUpdatable extends ShapedOreRecipe implements IRecipeUpdatable
{
    public OreRecipeUpdatable(ResourceLocation group, Block     result, Object... recipe){ super(group, new ItemStack(result), recipe); }
    public OreRecipeUpdatable(ResourceLocation group, Item      result, Object... recipe){ super(group, new ItemStack(result), recipe); }
    public OreRecipeUpdatable(ResourceLocation group, @Nonnull ItemStack result, Object... recipe) { super(group, result, CraftingHelper.parseShaped(recipe)); }
    public OreRecipeUpdatable(ResourceLocation group, @Nonnull ItemStack result, ShapedPrimer primer){ super (group, result, primer); }
    
    @Override
    public void replaceInput(ItemStack inputA, List<ItemStack> inputB)
    {
        if (inputB.isEmpty()) return;

        for (int i = 0; i < this.input.size(); i++)
        {
            Ingredient test = this.input.get(i);
            if (test.apply(inputA))
            {
                this.input.set(i, Ingredient.fromStacks(inputB.toArray(new ItemStack[inputB.size()])));
            }
        }
    }

    @Override
    public void replaceInput(ItemStack inputB)
    {
        for (int i = 0; i < this.input.size(); i++)
        {
            Ingredient test = this.input.get(i);
            if (test instanceof OreIngredient && test.apply(inputB))
            {
                this.input.set(i, Ingredient.fromStacks(inputB));
            }
        }
    }

    public static boolean itemListContains(List<?> test, ItemStack stack)
    {
        for (Object b : test)
        {
            if (b instanceof ItemStack && ItemStack.areItemsEqual(stack, (ItemStack) b) && RecipeUtil.areItemStackTagsEqual(stack, (ItemStack) b))
                return true;
        }
        return false;
    }

    public static OreRecipeUpdatable factory(JsonContext context, JsonObject json)
    {
        String group = JsonUtils.getString(json, "group", "");
        Map<Character, Ingredient> ingMap = Maps.newHashMap();
        for (Entry<String, JsonElement> entry : JsonUtils.getJsonObject(json, "key").entrySet())
        {
            if (entry.getKey().length() != 1)
                throw new JsonSyntaxException("Invalid key entry: '" + entry.getKey() + "' is an invalid symbol (must be 1 character only).");
            if (" ".equals(entry.getKey()))
                throw new JsonSyntaxException("Invalid key entry: ' ' is a reserved symbol.");

            ingMap.put(entry.getKey().toCharArray()[0], CraftingHelper.getIngredient(entry.getValue(), context));
        }

        ingMap.put(' ', Ingredient.EMPTY);

        JsonArray patternJ = JsonUtils.getJsonArray(json, "pattern");

        if (patternJ.size() == 0)
            throw new JsonSyntaxException("Invalid pattern: empty pattern not allowed");

        String[] pattern = new String[patternJ.size()];
        for (int x = 0; x < pattern.length; ++x)
        {
            String line = JsonUtils.getString(patternJ.get(x), "pattern[" + x + "]");
            if (x > 0 && pattern[0].length() != line.length())
                throw new JsonSyntaxException("Invalid pattern: each row must  be the same width");
            pattern[x] = line;
        }

        ShapedPrimer primer = new ShapedPrimer();
        primer.width = pattern[0].length();
        primer.height = pattern.length;
        primer.mirrored = JsonUtils.getBoolean(json, "mirrored", true);
        primer.input = NonNullList.withSize(primer.width * primer.height, Ingredient.EMPTY);

        Set<Character> keys = Sets.newHashSet(ingMap.keySet());
        keys.remove(' ');

        int x = 0;
        for (String line : pattern)
        {
            for (char chr : line.toCharArray())
            {
                Ingredient ing = ingMap.get(chr);
                if (ing == null)
                    throw new JsonSyntaxException("Pattern references symbol '" + chr + "' but it's not defined in the key");
                primer.input.set(x++, ing);
                keys.remove(chr);
            }
        }

        if (!keys.isEmpty())
            throw new JsonSyntaxException("Key defines symbols that aren't used in pattern: " + keys);

        ItemStack result = CraftingHelper.getItemStack(JsonUtils.getJsonObject(json, "result"), context);
        return new OreRecipeUpdatable(group.isEmpty() ? null : new ResourceLocation(group), result, primer);
    }
    
    public static class RecipeFactory implements IRecipeFactory
    {
        @Override
        public IRecipe parse(JsonContext context, JsonObject json)
        {
            return OreRecipeUpdatable.factory(context, json);
        }
    }
}
