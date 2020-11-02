//package micdoodle8.mods.galacticraft.core.recipe;
//
//import java.util.List;
//import java.util.Map;
//import java.util.Set;
//import java.util.Map.Entry;
//
//import javax.annotation.Nonnull;
//
//import com.google.common.collect.Maps;
//import com.google.common.collect.Sets;
//import com.google.gson.JsonArray;
//import com.google.gson.JsonElement;
//import com.google.gson.JsonObject;
//import com.google.gson.JsonSyntaxException;
//
//import micdoodle8.mods.galacticraft.api.recipe.IRecipeUpdatable;
//import micdoodle8.mods.galacticraft.core.util.RecipeUtil;
//import net.minecraft.block.Block;
//import net.minecraft.item.Item;
//import net.minecraft.item.ItemStack;
//import net.minecraft.item.crafting.IRecipe;
//import net.minecraft.item.crafting.IRecipeSerializer;
//import net.minecraft.item.crafting.Ingredient;
//import net.minecraft.item.crafting.ShapedRecipe;
//import net.minecraft.network.PacketBuffer;
//import net.minecraft.util.JSONUtils;
//import net.minecraft.util.NonNullList;
//import net.minecraft.util.ResourceLocation;
//import net.minecraftforge.common.crafting.CraftingHelper;
//
//public class OreRecipeUpdatable extends ShapedRecipe implements IRecipeUpdatable
//{
//    static int MAX_WIDTH = 3;
//    static int MAX_HEIGHT = 3;
////    public OreRecipeUpdatable(ResourceLocation group, Block     result, Object... recipe){ super(group, new ItemStack(result), recipe); }
////    public OreRecipeUpdatable(ResourceLocation group, Item      result, Object... recipe){ super(group, new ItemStack(result), recipe); }
////    public OreRecipeUpdatable(ResourceLocation group, @Nonnull ItemStack result, Object... recipe) { super(group, result, CraftingHelper.parseShaped(recipe)); }
////    public OreRecipeUpdatable(ResourceLocation group, @Nonnull ItemStack result, ShapedPrimer primer){ super (group, result, primer); }
//
//
//    public OreRecipeUpdatable(ResourceLocation idIn, String groupIn, int recipeWidthIn, int recipeHeightIn, NonNullList<Ingredient> recipeItemsIn, ItemStack recipeOutputIn)
//    {
//        super(idIn, groupIn, recipeWidthIn, recipeHeightIn, recipeItemsIn, recipeOutputIn);
//    }
//
//    @Override
//    public void replaceInput(ItemStack inputA, List<ItemStack> inputB)
//    {
//        if (inputB.isEmpty()) return;
//
//        for (int i = 0; i < this.getIngredients().size(); i++)
//        {
//            Ingredient test = this.getIngredients().get(i);
//            if (test.test(inputA))
//            {
//                this.getIngredients().set(i, Ingredient.fromStacks(inputB.toArray(new ItemStack[inputB.size()])));
//            }
//        }
//    }
//
//    @Override
//    public void replaceInput(ItemStack inputB)
//    {
//        for (int i = 0; i < this.getIngredients().size(); i++)
//        {
//            Ingredient test = this.getIngredients().get(i);
//            if (/*test instanceof OreIngredient && */test.test(inputB))
//            {
//                this.getIngredients().set(i, Ingredient.fromStacks(inputB));
//            }
//        }
//    }
//
//    public static boolean itemListContains(List<?> test, ItemStack stack)
//    {
//        for (Object b : test)
//        {
//            if (b instanceof ItemStack && ItemStack.areItemsEqual(stack, (ItemStack) b) && RecipeUtil.areItemStackTagsEqual(stack, (ItemStack) b))
//                return true;
//        }
//        return false;
//    }
//
////    public static OreRecipeUpdatable factory(JsonContext context, JsonObject json)
////    {
////        String group = JSONUtils.getString(json, "group", "");
////        Map<Character, Ingredient> ingMap = Maps.newHashMap();
////        for (Entry<String, JsonElement> entry : JSONUtils.getJsonObject(json, "key").entrySet())
////        {
////            if (entry.getKey().length() != 1)
////                throw new JsonSyntaxException("Invalid key entry: '" + entry.getKey() + "' is an invalid symbol (must be 1 character only).");
////            if (" ".equals(entry.getKey()))
////                throw new JsonSyntaxException("Invalid key entry: ' ' is a reserved symbol.");
////
////            ingMap.put(entry.getKey().toCharArray()[0], CraftingHelper.getIngredient(entry.getValue(), context));
////        }
////
////        ingMap.put(' ', Ingredient.EMPTY);
////
////        JsonArray patternJ = JSONUtils.getJsonArray(json, "pattern");
////
////        if (patternJ.size() == 0)
////            throw new JsonSyntaxException("Invalid pattern: empty pattern not allowed");
////
////        String[] pattern = new String[patternJ.size()];
////        for (int x = 0; x < pattern.length; ++x)
////        {
////            String line = JSONUtils.getString(patternJ.get(x), "pattern[" + x + "]");
////            if (x > 0 && pattern[0].length() != line.length())
////                throw new JsonSyntaxException("Invalid pattern: each row must  be the same width");
////            pattern[x] = line;
////        }
////
////        ShapedPrimer primer = new ShapedPrimer();
////        primer.width = pattern[0].length();
////        primer.height = pattern.length;
////        primer.mirrored = JSONUtils.getBoolean(json, "mirrored", true);
////        primer.input = NonNullList.withSize(primer.width * primer.height, Ingredient.EMPTY);
////
////        Set<Character> keys = Sets.newHashSet(ingMap.keySet());
////        keys.remove(' ');
////
////        int x = 0;
////        for (String line : pattern)
////        {
////            for (char chr : line.toCharArray())
////            {
////                Ingredient ing = ingMap.get(chr);
////                if (ing == null)
////                    throw new JsonSyntaxException("Pattern references symbol '" + chr + "' but it's not defined in the key");
////                primer.input.set(x++, ing);
////                keys.remove(chr);
////            }
////        }
////
////        if (!keys.isEmpty())
////            throw new JsonSyntaxException("Key defines symbols that aren't used in pattern: " + keys);
////
////        ItemStack result = CraftingHelper.getItemStack(JSONUtils.getJsonObject(json, "result"), context);
////        return new OreRecipeUpdatable(group.isEmpty() ? null : new ResourceLocation(group), result, primer);
////    }
////
////    public static class RecipeFactory implements IRecipeFactory
////    {
////        @Override
////        public IRecipe parse(JsonContext context, JsonObject json)
////        {
////            return OreRecipeUpdatable.factory(context, json);
////        }
////    }
//    private static Map<String, Ingredient> deserializeKey(JsonObject json) {
//        Map<String, Ingredient> map = Maps.newHashMap();
//
//        for(Entry<String, JsonElement> entry : json.entrySet()) {
//            if (entry.getKey().length() != 1) {
//                throw new JsonSyntaxException("Invalid key entry: '" + (String)entry.getKey() + "' is an invalid symbol (must be 1 character only).");
//            }
//
//            if (" ".equals(entry.getKey())) {
//                throw new JsonSyntaxException("Invalid key entry: ' ' is a reserved symbol.");
//            }
//
//            map.put(entry.getKey(), Ingredient.deserialize(entry.getValue()));
//        }
//
//        map.put(" ", Ingredient.EMPTY);
//        return map;
//    }
//
//    private static String[] patternFromJson(JsonArray jsonArr) {
//        String[] astring = new String[jsonArr.size()];
//        if (astring.length > MAX_HEIGHT) {
//            throw new JsonSyntaxException("Invalid pattern: too many rows, " + MAX_HEIGHT + " is maximum");
//        } else if (astring.length == 0) {
//            throw new JsonSyntaxException("Invalid pattern: empty pattern not allowed");
//        } else {
//            for(int i = 0; i < astring.length; ++i) {
//                String s = JSONUtils.getString(jsonArr.get(i), "pattern[" + i + "]");
//                if (s.length() > MAX_WIDTH) {
//                    throw new JsonSyntaxException("Invalid pattern: too many columns, " + MAX_WIDTH + " is maximum");
//                }
//
//                if (i > 0 && astring[0].length() != s.length()) {
//                    throw new JsonSyntaxException("Invalid pattern: each row must be the same width");
//                }
//
//                astring[i] = s;
//            }
//
//            return astring;
//        }
//    }
//
//    private static int firstNonSpace(String str) {
//        int i;
//        for(i = 0; i < str.length() && str.charAt(i) == ' '; ++i) {
//            ;
//        }
//
//        return i;
//    }
//
//    private static int lastNonSpace(String str) {
//        int i;
//        for(i = str.length() - 1; i >= 0 && str.charAt(i) == ' '; --i) {
//            ;
//        }
//
//        return i;
//    }
//
//    static String[] shrink(String... toShrink) {
//        int i = Integer.MAX_VALUE;
//        int j = 0;
//        int k = 0;
//        int l = 0;
//
//        for(int i1 = 0; i1 < toShrink.length; ++i1) {
//            String s = toShrink[i1];
//            i = Math.min(i, firstNonSpace(s));
//            int j1 = lastNonSpace(s);
//            j = Math.max(j, j1);
//            if (j1 < 0) {
//                if (k == i1) {
//                    ++k;
//                }
//
//                ++l;
//            } else {
//                l = 0;
//            }
//        }
//
//        if (toShrink.length == l) {
//            return new String[0];
//        } else {
//            String[] astring = new String[toShrink.length - l - k];
//
//            for(int k1 = 0; k1 < astring.length; ++k1) {
//                astring[k1] = toShrink[k1 + k].substring(i, j + 1);
//            }
//
//            return astring;
//        }
//    }
//
//    private static NonNullList<Ingredient> deserializeIngredients(String[] pattern, Map<String, Ingredient> keys, int patternWidth, int patternHeight) {
//        NonNullList<Ingredient> nonnulllist = NonNullList.withSize(patternWidth * patternHeight, Ingredient.EMPTY);
//        Set<String> set = Sets.newHashSet(keys.keySet());
//        set.remove(" ");
//
//        for(int i = 0; i < pattern.length; ++i) {
//            for(int j = 0; j < pattern[i].length(); ++j) {
//                String s = pattern[i].substring(j, j + 1);
//                Ingredient ingredient = keys.get(s);
//                if (ingredient == null) {
//                    throw new JsonSyntaxException("Pattern references symbol '" + s + "' but it's not defined in the key");
//                }
//
//                set.remove(s);
//                nonnulllist.set(j + patternWidth * i, ingredient);
//            }
//        }
//
//        if (!set.isEmpty()) {
//            throw new JsonSyntaxException("Key defines symbols that aren't used in pattern: " + set);
//        } else {
//            return nonnulllist;
//        }
//    }
//
//    public static class Serializer extends ShapedRecipe.Serializer  implements IRecipeSerializer<ShapedRecipe> {
//        public ShapedRecipe read(ResourceLocation recipeId, JsonObject json) {
//            String s = JSONUtils.getString(json, "group", "");
//            Map<String, Ingredient> map = OreRecipeUpdatable.deserializeKey(JSONUtils.getJsonObject(json, "key"));
//            String[] astring = OreRecipeUpdatable.shrink(OreRecipeUpdatable.patternFromJson(JSONUtils.getJsonArray(json, "pattern")));
//            int i = astring[0].length();
//            int j = astring.length;
//            NonNullList<Ingredient> nonnulllist = OreRecipeUpdatable.deserializeIngredients(astring, map, i, j);
//            ItemStack itemstack = ShapedRecipe.deserializeItem(JSONUtils.getJsonObject(json, "result"));
//            return new OreRecipeUpdatable(recipeId, s, i, j, nonnulllist, itemstack);
//        }
//
//        public ShapedRecipe read(ResourceLocation recipeId, PacketBuffer buffer) {
//            int i = buffer.readVarInt();
//            int j = buffer.readVarInt();
//            String s = buffer.readString(32767);
//            NonNullList<Ingredient> nonnulllist = NonNullList.withSize(i * j, Ingredient.EMPTY);
//
//            for(int k = 0; k < nonnulllist.size(); ++k) {
//                nonnulllist.set(k, Ingredient.read(buffer));
//            }
//
//            ItemStack itemstack = buffer.readItemStack();
//            return new OreRecipeUpdatable(recipeId, s, i, j, nonnulllist, itemstack);
//        }
//
//        public void write(PacketBuffer buffer, ShapedRecipe recipe) {
//            super.write(buffer, recipe);
//        }
//    }
//}
