package micdoodle8.mods.galacticraft.planets.asteroids.recipe;

import micdoodle8.mods.galacticraft.api.recipe.CompressorRecipes;
import micdoodle8.mods.galacticraft.core.blocks.GCBlocks;
import micdoodle8.mods.galacticraft.core.items.GCItems;
import micdoodle8.mods.galacticraft.core.util.RecipeUtil;
import micdoodle8.mods.galacticraft.planets.asteroids.blocks.AsteroidBlocks;
import micdoodle8.mods.galacticraft.planets.asteroids.items.AsteroidsItems;
import micdoodle8.mods.galacticraft.planets.mars.items.MarsItems;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraftforge.oredict.OreDictionary;

public class RecipeManagerAsteroids
{
    public static void loadRecipes()
    {
        // Add compatibility stuffz here

        addUniversalRecipes();
    }

    private static void addUniversalRecipes()
    {
        FurnaceRecipes.smelting().func_151394_a(new ItemStack(AsteroidBlocks.blockBasic, 1, 3), new ItemStack(GCItems.basicItem, 1, 3), 0.0F);
        FurnaceRecipes.smelting().func_151394_a(new ItemStack(AsteroidBlocks.blockBasic, 1, 4), new ItemStack(AsteroidsItems.basicItem, 1, 5), 0.0F);
        FurnaceRecipes.smelting().func_151394_a(new ItemStack(AsteroidBlocks.blockBasic, 1, 5), new ItemStack(Items.iron_ingot), 0.0F);
        FurnaceRecipes.smelting().func_151394_a(new ItemStack(AsteroidsItems.basicItem, 1, 3), new ItemStack(Items.iron_ingot), 0.5F);
        FurnaceRecipes.smelting().func_151394_a(new ItemStack(AsteroidsItems.basicItem, 1, 4), new ItemStack(AsteroidsItems.basicItem, 1, 5), 0.5F);

        RecipeUtil.addRecipe(new ItemStack(AsteroidsItems.heavyNoseCone, 1), new Object[]{" Y ", " X ", "X X", 'X', new ItemStack(AsteroidsItems.basicItem, 1, 6), 'Y', Blocks.redstone_torch});

        RecipeUtil.addRecipe(new ItemStack(AsteroidsItems.basicItem, 1, 7), new Object[]{" X ", "XYX", " X ", 'X', Blocks.wool, 'Y', Items.redstone});

        RecipeUtil.addRecipe(new ItemStack(AsteroidsItems.thermalPadding, 1, 0), new Object[]{"XXX", "X X", 'X', new ItemStack(AsteroidsItems.basicItem, 1, 7)});
        RecipeUtil.addRecipe(new ItemStack(AsteroidsItems.thermalPadding, 1, 1), new Object[]{"X X", "XXX", "XXX", 'X', new ItemStack(AsteroidsItems.basicItem, 1, 7)});
        RecipeUtil.addRecipe(new ItemStack(AsteroidsItems.thermalPadding, 1, 2), new Object[]{"XXX", "X X", "X X", 'X', new ItemStack(AsteroidsItems.basicItem, 1, 7)});
        RecipeUtil.addRecipe(new ItemStack(AsteroidsItems.thermalPadding, 1, 3), new Object[]{"X X", "X X", 'X', new ItemStack(AsteroidsItems.basicItem, 1, 7)});

        RecipeUtil.addRecipe(new ItemStack(AsteroidsItems.titaniumHelmet, 1), new Object[]{"XXX", "X X", 'X', new ItemStack(AsteroidsItems.basicItem, 1, 6)});
        RecipeUtil.addRecipe(new ItemStack(AsteroidsItems.titaniumChestplate, 1), new Object[]{"X X", "XXX", "XXX", 'X', new ItemStack(AsteroidsItems.basicItem, 1, 6)});
        RecipeUtil.addRecipe(new ItemStack(AsteroidsItems.titaniumLeggings, 1), new Object[]{"XXX", "X X", "X X", 'X', new ItemStack(AsteroidsItems.basicItem, 1, 6)});
        RecipeUtil.addRecipe(new ItemStack(AsteroidsItems.titaniumBoots, 1), new Object[]{"X X", "X X", 'X', new ItemStack(AsteroidsItems.basicItem, 1, 6)});

        RecipeUtil.addRecipe(new ItemStack(AsteroidsItems.titaniumPickaxe, 1), new Object[] { "YYY", " X ", " X ", 'Y', new ItemStack(AsteroidsItems.basicItem, 1, 6), 'X', Items.stick });
        RecipeUtil.addRecipe(new ItemStack(AsteroidsItems.titaniumAxe, 1), new Object[] { "YY ", "YX ", " X ", 'Y', new ItemStack(AsteroidsItems.basicItem, 1, 6), 'X', Items.stick });
        RecipeUtil.addRecipe(new ItemStack(AsteroidsItems.titaniumAxe, 1), new Object[] { " YY", " XY", " X ", 'Y', new ItemStack(AsteroidsItems.basicItem, 1, 6), 'X', Items.stick });
        RecipeUtil.addRecipe(new ItemStack(AsteroidsItems.titaniumHoe, 1), new Object[] { " YY", " X ", " X ", 'Y', new ItemStack(AsteroidsItems.basicItem, 1, 6), 'X', Items.stick });
        RecipeUtil.addRecipe(new ItemStack(AsteroidsItems.titaniumHoe, 1), new Object[] { "YY ", " X ", " X ", 'Y', new ItemStack(AsteroidsItems.basicItem, 1, 6), 'X', Items.stick });
        RecipeUtil.addRecipe(new ItemStack(AsteroidsItems.titaniumSpade, 1), new Object[] { " Y ", " X ", " X ", 'Y', new ItemStack(AsteroidsItems.basicItem, 1, 6), 'X', Items.stick });
        RecipeUtil.addRecipe(new ItemStack(AsteroidsItems.titaniumSword, 1), new Object[] { " Y ", " Y ", " X ", 'Y', new ItemStack(AsteroidsItems.basicItem, 1, 6), 'X', Items.stick });

        RecipeUtil.addRecipe(new ItemStack(AsteroidsItems.basicItem, 1, 1), new Object[] { " YV", "XWX", "XZX", 'V', Blocks.stone_button, 'W', new ItemStack(GCItems.canister, 1, 0), 'X', new ItemStack(AsteroidsItems.basicItem, 1, 6), 'Y', Items.flint_and_steel, 'Z', GCItems.oxygenVent });

        RecipeUtil.addRecipe(new ItemStack(AsteroidsItems.basicItem, 1, 1), new Object[] { "VY ", "XWX", "XZX", 'V', Blocks.stone_button, 'W', new ItemStack(GCItems.canister, 1, 0), 'X', new ItemStack(AsteroidsItems.basicItem, 1, 6), 'Y', Items.flint_and_steel, 'Z', GCItems.oxygenVent });

        RecipeUtil.addRecipe(new ItemStack(AsteroidsItems.grapple, 2), new Object[] { "X", "Y", "Z", 'X', Items.iron_ingot, 'Y', Blocks.planks, 'Z', Items.string });

        RecipeUtil.addRecipe(new ItemStack(AsteroidsItems.atmosphericValve, 1, 0), new Object[] { "   ", "XYX", " X ", 'X', new ItemStack(MarsItems.marsItemBasic, 1, 2), 'Y', GCItems.oxygenVent });

        RecipeUtil.addRecipe(new ItemStack(AsteroidsItems.basicItem, 1, 2), new Object[] { " Y ", "XYX", "X X", 'X', new ItemStack(AsteroidsItems.basicItem), 'Y', "compressedTitanium" });

        RecipeUtil.addRecipe(new ItemStack(AsteroidBlocks.blockWalkway, 3), new Object[] { "XXX", " X ", 'X', new ItemStack(AsteroidsItems.basicItem, 1, 6) });
        RecipeUtil.addRecipe(new ItemStack(AsteroidBlocks.blockWalkwayWire, 3), new Object[] { "XXX", "YXY", " Y ", 'X', new ItemStack(AsteroidsItems.basicItem, 1, 6), 'Y', new ItemStack(GCBlocks.aluminumWire) });
        RecipeUtil.addRecipe(new ItemStack(AsteroidBlocks.blockWalkwayOxygenPipe, 3), new Object[] { "XXX", "YXY", " Y ", 'X', new ItemStack(AsteroidsItems.basicItem, 1, 6), 'Y', new ItemStack(GCBlocks.oxygenPipe) });

        RecipeUtil.addRecipe(new ItemStack(AsteroidBlocks.shortRangeTelepad), new Object[] { "XWX", "ZYZ", "XXX", 'W', new ItemStack(GCBlocks.aluminumWire), 'X', new ItemStack(AsteroidsItems.basicItem, 1, 6), 'Y', Items.redstone, 'Z', Items.ender_pearl });
        RecipeUtil.addRecipe(new ItemStack(AsteroidBlocks.shortRangeTelepad), new Object[] { "XWX", "ZYZ", "XXX", 'W', new ItemStack(GCBlocks.aluminumWire, 1, 1), 'X', new ItemStack(AsteroidsItems.basicItem, 1, 6), 'Y', Items.redstone, 'Z', Items.ender_pearl });

        RecipeUtil.addRecipe(new ItemStack(AsteroidsItems.basicItem, 1, 8), new Object[] { "XYX", "YZY", "XYX", 'X', Items.redstone, 'Y', "compressedIron", 'Z', Blocks.glass_pane });

        RecipeUtil.addRecipe(new ItemStack(AsteroidBlocks.beamReceiver), new Object[] { " X ", "XYX", " X ", 'X', new ItemStack(AsteroidsItems.basicItem, 1, 6), 'Y', new ItemStack(AsteroidsItems.basicItem, 1, 8) });
        RecipeUtil.addRecipe(new ItemStack(AsteroidBlocks.beamReflector), new Object[] { " ZY", " X ", "XXX", 'X', new ItemStack(AsteroidsItems.basicItem, 1, 6), 'Y', new ItemStack(AsteroidsItems.basicItem, 1, 8), 'Z', Blocks.lever });
        RecipeUtil.addRecipe(new ItemStack(AsteroidBlocks.beamReflector), new Object[] { "YZ ", " X ", "XXX", 'X', new ItemStack(AsteroidsItems.basicItem, 1, 6), 'Y', new ItemStack(AsteroidsItems.basicItem, 1, 8), 'Z', Blocks.lever });

        if (OreDictionary.getOres("ingotTitanium").size() > 0)
        {
            for (ItemStack stack : OreDictionary.getOres("ingotTitanium"))
            {
                CompressorRecipes.addShapelessRecipe(new ItemStack(AsteroidsItems.basicItem, 1, 6), stack, stack);
            }
        }

        CompressorRecipes.addShapelessRecipe(new ItemStack(AsteroidsItems.basicItem, 1, 0), new ItemStack(MarsItems.marsItemBasic, 1, 3), new ItemStack(MarsItems.marsItemBasic, 1, 5));
    }
}
