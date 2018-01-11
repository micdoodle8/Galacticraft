package micdoodle8.mods.galacticraft.core.recipe;

import com.google.gson.JsonObject;

import micdoodle8.mods.galacticraft.core.GCItems;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.util.ConfigManagerCore;
import micdoodle8.mods.galacticraft.planets.asteroids.items.AsteroidsItems;
import micdoodle8.mods.galacticraft.planets.mars.items.MarsItems;
import micdoodle8.mods.galacticraft.planets.venus.VenusItems;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.JsonUtils;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.common.crafting.IIngredientFactory;
import net.minecraftforge.common.crafting.JsonContext;
import net.minecraftforge.oredict.OreDictionary;

import javax.annotation.Nonnull;

public class IngredientAdvancedMetal implements IIngredientFactory
{
    @Nonnull
    @Override
    public Ingredient parse(JsonContext context, JsonObject json)
    {
        String metal = JsonUtils.getString(json, "metal");
        if (metal.equals("meteoric_iron_ingot"))
        {
            return ConfigManagerCore.recipesRequireGCAdvancedMetals ? Ingredient.fromStacks(new ItemStack(GCItems.itemBasicMoon, 1, 0)) : Ingredient.fromStacks((ItemStack[]) OreDictionary.getOres("ingotMeteoricIron").toArray());
        }
        if (metal.equals("meteoric_iron_plate"))
        {
            return ConfigManagerCore.recipesRequireGCAdvancedMetals ? Ingredient.fromStacks(new ItemStack(GCItems.itemBasicMoon, 1, 1)) : Ingredient.fromStacks((ItemStack[]) OreDictionary.getOres("compressedMeteoricIron").toArray());
        }
        if (metal.equals("desh_ingot"))
        {
            return GalacticraftCore.isPlanetsLoaded ? (ConfigManagerCore.recipesRequireGCAdvancedMetals ? Ingredient.fromStacks(new ItemStack(MarsItems.marsItemBasic, 1, 2)) : Ingredient.fromStacks((ItemStack[]) OreDictionary.getOres("ingotDesh").toArray())) : Ingredient.fromItem(GCItems.heavyPlatingTier1);
        }
        if (metal.equals("desh_plate"))
        {
            return ConfigManagerCore.recipesRequireGCAdvancedMetals ? Ingredient.fromStacks(new ItemStack(MarsItems.marsItemBasic, 1, 5)) : Ingredient.fromStacks((ItemStack[]) OreDictionary.getOres("compressedDesh").toArray());
        }
        if (metal.equals("titanium_ingot"))
        {
            return ConfigManagerCore.recipesRequireGCAdvancedMetals ? Ingredient.fromStacks(new ItemStack(AsteroidsItems.basicItem, 1, 0)) : Ingredient.fromStacks((ItemStack[]) OreDictionary.getOres("ingotTitanium").toArray());
        }
        if (metal.equals("titanium_plate"))
        {
            return ConfigManagerCore.recipesRequireGCAdvancedMetals ? Ingredient.fromStacks(new ItemStack(AsteroidsItems.basicItem, 1, 6)) : Ingredient.fromStacks((ItemStack[]) OreDictionary.getOres("compressedTitanium").toArray());
        }
        if (metal.equals("lead_ingot"))
        {
            return ConfigManagerCore.recipesRequireGCAdvancedMetals ? Ingredient.fromStacks(new ItemStack(VenusItems.basicItem, 1, 1)) : Ingredient.fromStacks((ItemStack[]) OreDictionary.getOres("ingotLead").toArray());
        }
        return Ingredient.fromItem(GCItems.infiniteBatery);
    }
}
