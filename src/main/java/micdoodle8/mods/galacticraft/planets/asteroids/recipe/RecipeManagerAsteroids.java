package micdoodle8.mods.galacticraft.planets.asteroids.recipe;

import micdoodle8.mods.galacticraft.api.recipe.CompressorRecipes;
import micdoodle8.mods.galacticraft.core.blocks.GCBlocks;
import micdoodle8.mods.galacticraft.core.items.GCItems;
import micdoodle8.mods.galacticraft.core.util.ConfigManagerCore;
import micdoodle8.mods.galacticraft.core.util.RecipeUtil;
import micdoodle8.mods.galacticraft.planets.asteroids.blocks.AsteroidBlocks;
import micdoodle8.mods.galacticraft.planets.asteroids.items.AsteroidsItems;
import micdoodle8.mods.galacticraft.planets.mars.items.MarsItems;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraftforge.oredict.OreDictionary;

import java.util.ArrayList;
import java.util.List;

public class RecipeManagerAsteroids
{
    public static void loadRecipes()
    {
        // Add compatibility stuffz here

        addUniversalRecipes();
    }

    private static void addUniversalRecipes()
    {
        FurnaceRecipes.smelting().func_151394_a(new ItemStack(AsteroidBlocks.blockBasic, 1, 3), new ItemStack(GCItems.basicItem, 1, 5), 0.0F);
        FurnaceRecipes.smelting().func_151394_a(new ItemStack(AsteroidBlocks.blockBasic, 1, 4), new ItemStack(AsteroidsItems.basicItem, 1, 5), 0.0F);
        FurnaceRecipes.smelting().func_151394_a(new ItemStack(AsteroidBlocks.blockBasic, 1, 5), new ItemStack(Items.iron_ingot), 0.0F);
        FurnaceRecipes.smelting().func_151394_a(new ItemStack(AsteroidsItems.basicItem, 1, 3), new ItemStack(Items.iron_ingot), 0.5F);
        FurnaceRecipes.smelting().func_151394_a(new ItemStack(AsteroidsItems.basicItem, 1, 4), new ItemStack(AsteroidsItems.basicItem, 1, 5), 0.5F);

        RecipeUtil.addRecipe(new ItemStack(AsteroidsItems.heavyNoseCone, 1), new Object[] { " Y ", " X ", "X X", 'X', new ItemStack(AsteroidsItems.basicItem, 1, 0), 'Y', Blocks.redstone_torch });

        RecipeUtil.addRecipe(new ItemStack(AsteroidsItems.basicItem, 1, 7), new Object[] { " X ", "XYX", " X ", 'X', Blocks.wool, 'Y', Items.redstone });

        RecipeUtil.addRecipe(new ItemStack(AsteroidsItems.thermalPadding, 1, 0), new Object[] { "XXX", "X X", 'X', new ItemStack(AsteroidsItems.basicItem, 1, 7) });
        RecipeUtil.addRecipe(new ItemStack(AsteroidsItems.thermalPadding, 1, 1), new Object[] { "X X", "XXX", "XXX", 'X', new ItemStack(AsteroidsItems.basicItem, 1, 7) });
        RecipeUtil.addRecipe(new ItemStack(AsteroidsItems.thermalPadding, 1, 2), new Object[] { "XXX", "X X", "X X", 'X', new ItemStack(AsteroidsItems.basicItem, 1, 7) });
        RecipeUtil.addRecipe(new ItemStack(AsteroidsItems.thermalPadding, 1, 3), new Object[] { "X X", "X X", 'X', new ItemStack(AsteroidsItems.basicItem, 1, 7) });

        RecipeUtil.addRecipe(new ItemStack(AsteroidsItems.titaniumHelmet, 1), new Object[] { "XXX", "X X", 'X', new ItemStack(AsteroidsItems.basicItem, 1, 6) });
        RecipeUtil.addRecipe(new ItemStack(AsteroidsItems.titaniumChestplate, 1), new Object[] { "X X", "XXX", "XXX", 'X', new ItemStack(AsteroidsItems.basicItem, 1, 6) });
        RecipeUtil.addRecipe(new ItemStack(AsteroidsItems.titaniumLeggings, 1), new Object[] { "XXX", "X X", "X X", 'X', new ItemStack(AsteroidsItems.basicItem, 1, 6) });
        RecipeUtil.addRecipe(new ItemStack(AsteroidsItems.titaniumBoots, 1), new Object[] { "X X", "X X", 'X', new ItemStack(AsteroidsItems.basicItem, 1, 6) });

        RecipeUtil.addRecipe(new ItemStack(AsteroidsItems.titaniumPickaxe, 1), new Object[] { "YYY", " X ", " X ", 'Y', new ItemStack(AsteroidsItems.basicItem, 1, 6), 'X', Items.stick });
        RecipeUtil.addRecipe(new ItemStack(AsteroidsItems.titaniumAxe, 1), new Object[] { "YY ", "YX ", " X ", 'Y', new ItemStack(AsteroidsItems.basicItem, 1, 6), 'X', Items.stick });
        RecipeUtil.addRecipe(new ItemStack(AsteroidsItems.titaniumAxe, 1), new Object[] { " YY", " XY", " X ", 'Y', new ItemStack(AsteroidsItems.basicItem, 1, 6), 'X', Items.stick });
        RecipeUtil.addRecipe(new ItemStack(AsteroidsItems.titaniumHoe, 1), new Object[] { " YY", " X ", " X ", 'Y', new ItemStack(AsteroidsItems.basicItem, 1, 6), 'X', Items.stick });
        RecipeUtil.addRecipe(new ItemStack(AsteroidsItems.titaniumHoe, 1), new Object[] { "YY ", " X ", " X ", 'Y', new ItemStack(AsteroidsItems.basicItem, 1, 6), 'X', Items.stick });
        RecipeUtil.addRecipe(new ItemStack(AsteroidsItems.titaniumSpade, 1), new Object[] { " Y ", " X ", " X ", 'Y', new ItemStack(AsteroidsItems.basicItem, 1, 6), 'X', Items.stick });
        RecipeUtil.addRecipe(new ItemStack(AsteroidsItems.titaniumSword, 1), new Object[] { " Y ", " Y ", " X ", 'Y', new ItemStack(AsteroidsItems.basicItem, 1, 6), 'X', Items.stick });

        RecipeUtil.addRecipe(new ItemStack(AsteroidsItems.basicItem, 1, 1), new Object[] { " YV", "XWX", "XZX", 'V', Blocks.stone_button, 'W', new ItemStack(GCItems.canister, 1, 0), 'X', new ItemStack(AsteroidsItems.basicItem, 1, 0), 'Y', Items.flint_and_steel, 'Z', GCItems.oxygenVent });

        RecipeUtil.addRecipe(new ItemStack(AsteroidsItems.basicItem, 1, 1), new Object[] { "VY ", "XWX", "XZX", 'V', Blocks.stone_button, 'W', new ItemStack(GCItems.canister, 1, 0), 'X', new ItemStack(AsteroidsItems.basicItem, 1, 0), 'Y', Items.flint_and_steel, 'Z', GCItems.oxygenVent });

        RecipeUtil.addRecipe(new ItemStack(AsteroidsItems.basicItem, 1, 2), new Object[] { " Y ", "XYX", "X X", 'X', new ItemStack(AsteroidsItems.basicItem, 1, 0), 'Y', new ItemStack(MarsItems.marsItemBasic, 1, 3) });

        RecipeUtil.addRecipe(new ItemStack(AsteroidsItems.grapple, 1), new Object[] { "  Z", "XZ ", "XX ", 'X', Items.iron_ingot, 'Z', Items.string });

        RecipeUtil.addRecipe(new ItemStack(AsteroidsItems.atmosphericValve, 1, 0), new Object[] { "   ", "XYX", " X ", 'X', new ItemStack(MarsItems.marsItemBasic, 1, 2), 'Y', GCItems.oxygenVent });

        RecipeUtil.addRecipe(new ItemStack(AsteroidsItems.basicItem, 1, 2), new Object[] { " Y ", "XYX", "X X", 'X', new ItemStack(AsteroidsItems.basicItem), 'Y', "compressedTitanium" });

        RecipeUtil.addRecipe(new ItemStack(AsteroidBlocks.blockWalkway, 5), new Object[] { "XXX", " X ", 'X', new ItemStack(AsteroidsItems.basicItem, 1, 6) });
        RecipeUtil.addRecipe(new ItemStack(AsteroidBlocks.blockWalkwayWire, 5), new Object[] { "XXX", "YXY", "YYY", 'X', new ItemStack(AsteroidsItems.basicItem, 1, 6), 'Y', new ItemStack(GCBlocks.aluminumWireHeavy) });
        RecipeUtil.addRecipe(new ItemStack(AsteroidBlocks.blockWalkwayOxygenPipe, 5), new Object[] { "XXX", "YXY", "YYY", 'X', new ItemStack(AsteroidsItems.basicItem, 1, 6), 'Y', new ItemStack(GCBlocks.oxygenPipe) });
        CraftingManager.getInstance().addShapelessRecipe(new ItemStack(AsteroidBlocks.blockWalkwayWire, 1), new ItemStack(AsteroidBlocks.blockWalkway, 1), new ItemStack(GCBlocks.aluminumWireHeavy));
        CraftingManager.getInstance().addShapelessRecipe(new ItemStack(AsteroidBlocks.blockWalkwayOxygenPipe, 1), new ItemStack(AsteroidBlocks.blockWalkway, 1), new ItemStack(GCBlocks.oxygenPipe, 1));

        RecipeUtil.addRecipe(new ItemStack(AsteroidBlocks.shortRangeTelepad), new Object[] { "XWX", "ZYZ", "XXX", 'W', new ItemStack(AsteroidsItems.basicItem, 1, 8), 'X', new ItemStack(AsteroidsItems.basicItem, 1, 6), 'Y', Items.redstone, 'Z', Items.ender_pearl });

        RecipeUtil.addRecipe(new ItemStack(AsteroidsItems.basicItem, 1, 8), new Object[] { "XYX", "YZY", "XYX", 'X', Items.redstone, 'Y', "compressedIron", 'Z', Blocks.glass_pane });

        RecipeUtil.addRecipe(new ItemStack(AsteroidBlocks.beamReceiver), new Object[] { " X ", "XYX", " X ", 'X', new ItemStack(AsteroidsItems.basicItem, 1, 6), 'Y', new ItemStack(AsteroidsItems.basicItem, 1, 8) });
        RecipeUtil.addRecipe(new ItemStack(AsteroidBlocks.beamReflector), new Object[] { " Y ", "ZX ", "XXX", 'X', new ItemStack(AsteroidsItems.basicItem, 1, 6), 'Y', new ItemStack(AsteroidsItems.basicItem, 1, 8), 'Z', Blocks.lever });
        RecipeUtil.addRecipe(new ItemStack(AsteroidBlocks.beamReflector), new Object[] { " Y ", " XZ", "XXX", 'X', new ItemStack(AsteroidsItems.basicItem, 1, 6), 'Y', new ItemStack(AsteroidsItems.basicItem, 1, 8), 'Z', Blocks.lever });

        if (OreDictionary.getOres("ingotTitanium").size() > 0)
        {
            for (ItemStack stack : OreDictionary.getOres("ingotTitanium"))
            {
                CompressorRecipes.addShapelessRecipe(new ItemStack(AsteroidsItems.basicItem, ConfigManagerCore.quickMode ? 2 : 1, 6), stack, stack);
            }
        }

        CompressorRecipes.addShapelessRecipe(new ItemStack(AsteroidsItems.basicItem, 1, 0), new ItemStack(MarsItems.marsItemBasic, 1, 3), new ItemStack(MarsItems.marsItemBasic, 1, 5));

        //All this is for NEI's benefit
        List<ItemStack> list1 = new ArrayList();
        List<ItemStack> list2 = new ArrayList();
        List<ItemStack> list3 = new ArrayList();
        list1.add(new ItemStack(AsteroidsItems.canisterLOX, 1, 751));
        list2.add(new ItemStack(AsteroidsItems.canisterLOX, 1, 834));
        list3.add(new ItemStack(AsteroidsItems.canisterLOX, 1, 918));
        list1.add(new ItemStack(GCItems.oxTankHeavy, 1, 2700));
        list2.add(new ItemStack(GCItems.oxTankMedium, 1, 1800));
        list3.add(new ItemStack(GCItems.oxTankLight, 1, 900));
        CraftingManager.getInstance().getRecipeList().add(new CanisterRecipes(new ItemStack(GCItems.oxTankHeavy, 1, 0), list1));
        CraftingManager.getInstance().getRecipeList().add(new CanisterRecipes(new ItemStack(GCItems.oxTankMedium, 1, 0), list2));
        CraftingManager.getInstance().getRecipeList().add(new CanisterRecipes(new ItemStack(GCItems.oxTankLight, 1, 0), list3));
    }
}
