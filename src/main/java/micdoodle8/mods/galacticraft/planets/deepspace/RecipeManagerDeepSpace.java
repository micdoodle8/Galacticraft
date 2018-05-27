package micdoodle8.mods.galacticraft.planets.deepspace;

import micdoodle8.mods.galacticraft.core.util.RecipeUtil;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;

public class RecipeManagerDeepSpace
{
    public static void loadRecipes()
    {
        RecipeManagerDeepSpace.addUniversalRecipes();
    }

    private static void addUniversalRecipes()
    {
        RecipeUtil.addRecipe(new ItemStack(DeepSpaceBlocks.glassProtective, 8, 0), new Object[] { "XXX", "XYX", "XXX", 'X', Blocks.GLASS, 'Y', "ingotLead" });
    }
}
