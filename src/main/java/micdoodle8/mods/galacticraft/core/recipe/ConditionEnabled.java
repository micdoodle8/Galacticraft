package micdoodle8.mods.galacticraft.core.recipe;

import com.google.gson.JsonObject;
import micdoodle8.mods.galacticraft.core.Constants;
import micdoodle8.mods.galacticraft.core.util.CompatibilityManager;
import micdoodle8.mods.galacticraft.core.util.ConfigManagerCore;
import micdoodle8.mods.galacticraft.core.util.GCLog;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.common.crafting.conditions.ICondition;
import net.minecraftforge.common.crafting.conditions.IConditionSerializer;

public class ConditionEnabled implements ICondition
{
    private static final ResourceLocation NAME = new ResourceLocation(Constants.MOD_ID_CORE, "condition");
    private final String subCondition;

    public ConditionEnabled(String subCondition)
    {
        this.subCondition = subCondition;
    }

    @Override
    public ResourceLocation getID()
    {
        return NAME;
    }

    @Override
    public boolean test()
    {
        if (subCondition.equals("can_default"))
        {
            return !ConfigManagerCore.alternateCanisterRecipe.get();
        }
        if (subCondition.equals("can_alt"))
        {
            return ConfigManagerCore.alternateCanisterRecipe.get();
        }
        if (subCondition.equals("aa_loaded"))
        {
            return !CompatibilityManager.modAALoaded;
        }
        GCLog.severe("Unrecognised condition data: " + subCondition);
        return false;
    }

    @Override
    public String toString()
    {
        return "ConditionEnabled(\"" + subCondition + "\")";
    }

    public static class Serializer implements IConditionSerializer<ConditionEnabled>
    {
        public static final ConditionEnabled.Serializer INSTANCE = new ConditionEnabled.Serializer();

        @Override
        public void write(JsonObject json, ConditionEnabled value)
        {
            json.addProperty("data", value.subCondition);
        }

        @Override
        public ConditionEnabled read(JsonObject json)
        {
            if(JSONUtils.hasField(json, "sub_cond"))
            {
                String data = JSONUtils.getString(json, "sub_cond");
                return new ConditionEnabled(data);
            }

            throw new IllegalStateException("Galacticraft recipe JSON condition error in recipe for " + CraftingHelper.getItemStack(json, false));
        }

        @Override
        public ResourceLocation getID()
        {
            return ConditionEnabled.NAME;
        }
    }
}
