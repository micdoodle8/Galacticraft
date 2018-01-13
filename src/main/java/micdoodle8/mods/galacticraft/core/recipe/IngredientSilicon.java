package micdoodle8.mods.galacticraft.core.recipe;

import com.google.gson.JsonObject;

import micdoodle8.mods.galacticraft.core.util.ConfigManagerCore;
import net.minecraft.item.crafting.Ingredient;
import net.minecraftforge.common.crafting.IIngredientFactory;
import net.minecraftforge.common.crafting.JsonContext;
import net.minecraftforge.oredict.OreIngredient;

import javax.annotation.Nonnull;

public class IngredientSilicon implements IIngredientFactory
{
    @Nonnull
    @Override
    public Ingredient parse(JsonContext context, JsonObject json)
    {
        return new OreIngredient(ConfigManagerCore.otherModsSilicon);
    }
}
