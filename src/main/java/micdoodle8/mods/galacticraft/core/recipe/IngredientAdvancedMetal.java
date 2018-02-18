package micdoodle8.mods.galacticraft.core.recipe;

import com.google.gson.JsonObject;

import micdoodle8.mods.galacticraft.core.GCItems;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.planets.asteroids.items.AsteroidsItems;
import micdoodle8.mods.galacticraft.planets.mars.items.MarsItems;
import micdoodle8.mods.galacticraft.planets.venus.VenusItems;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.JsonUtils;
import net.minecraftforge.common.crafting.IIngredientFactory;
import net.minecraftforge.common.crafting.JsonContext;
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
            return Ingredient.fromStacks(new ItemStack(GCItems.itemBasicMoon, 1, 0));// : new OreIngredient("ingotMeteoricIron");
        }
        if (metal.equals("meteoric_iron_plate"))
        {
            return Ingredient.fromStacks(new ItemStack(GCItems.itemBasicMoon, 1, 1));// : new OreIngredient("compressedMeteoricIron");
        }
        if (metal.equals("desh_ingot"))
        {
            return GalacticraftCore.isPlanetsLoaded ? Ingredient.fromStacks(new ItemStack(MarsItems.marsItemBasic, 1, 2)) : Ingredient.fromItem(GCItems.heavyPlatingTier1);
        }
        if (metal.equals("desh_plate"))
        {
            return Ingredient.fromStacks(new ItemStack(MarsItems.marsItemBasic, 1, 5));// : new OreIngredient("compressedDesh");
        }
        if (metal.equals("titanium_ingot"))
        {
            return Ingredient.fromStacks(new ItemStack(AsteroidsItems.basicItem, 1, 0));// : new OreIngredient("ingotTitanium");
        }
        if (metal.equals("titanium_plate"))
        {
            return Ingredient.fromStacks(new ItemStack(AsteroidsItems.basicItem, 1, 6));// : new OreIngredient("compressedTitanium");
        }
        if (metal.equals("lead_ingot"))
        {
            return Ingredient.fromStacks(new ItemStack(VenusItems.basicItem, 1, 1));// : new OreIngredient("ingotLead");
        }
        return Ingredient.fromItem(GCItems.infiniteBatery);
    }
}
