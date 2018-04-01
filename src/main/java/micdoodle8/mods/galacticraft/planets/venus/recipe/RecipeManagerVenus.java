package micdoodle8.mods.galacticraft.planets.venus.recipe;

import micdoodle8.mods.galacticraft.core.GCBlocks;
import micdoodle8.mods.galacticraft.core.GCItems;
import micdoodle8.mods.galacticraft.core.blocks.BlockMachine;
import micdoodle8.mods.galacticraft.core.util.RecipeUtil;
import micdoodle8.mods.galacticraft.planets.asteroids.items.AsteroidsItems;
import micdoodle8.mods.galacticraft.planets.mars.items.MarsItems;
import micdoodle8.mods.galacticraft.planets.venus.VenusBlocks;
import micdoodle8.mods.galacticraft.planets.venus.VenusItems;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraftforge.oredict.OreDictionary;

public class RecipeManagerVenus
{
    public static void loadRecipes()
    {
        RecipeManagerVenus.addUniversalRecipes();
    }

    private static void addUniversalRecipes()
    {
        OreDictionary.registerOre("ingotLead", new ItemStack(VenusItems.basicItem, 1, 1));
        ItemStack deshIngot = new ItemStack(MarsItems.marsItemBasic, 1, 2);
        ItemStack leadIngot = new ItemStack(VenusItems.basicItem, 1, 1);
        
        // Lead block
        RecipeUtil.addBlockRecipe(new ItemStack(VenusBlocks.venusBlock, 1, 12), "ingotLead", new ItemStack(VenusItems.basicItem, 1, 1));
        RecipeUtil.addRecipe(new ItemStack(VenusItems.basicItem, 9, 1), new Object[] {"X", 'X', new ItemStack(VenusBlocks.venusBlock, 1, 12)});
        
        // Smelting
        FurnaceRecipes.instance().addSmeltingRecipe(new ItemStack(VenusBlocks.venusBlock, 1, 6), new ItemStack(GCItems.basicItem, 1, 5), 0.5F);
        FurnaceRecipes.instance().addSmeltingRecipe(new ItemStack(VenusBlocks.venusBlock, 1, 7), new ItemStack(GCItems.basicItem, 1, 3), 0.5F);
        FurnaceRecipes.instance().addSmeltingRecipe(new ItemStack(VenusBlocks.venusBlock, 1, 8), new ItemStack(VenusItems.basicItem, 1, 1), 1.0F);
        FurnaceRecipes.instance().addSmeltingRecipe(new ItemStack(VenusBlocks.venusBlock, 1, 9), new ItemStack(Items.quartz), 0.2F);
        FurnaceRecipes.instance().addSmeltingRecipe(new ItemStack(VenusBlocks.venusBlock, 1, 11), new ItemStack(GCItems.basicItem, 1, 4), 0.5F);

        // Atomic Battery
        RecipeUtil.addRecipeUpdatable(new ItemStack(VenusItems.atomicBattery, 1, 0), new Object[] { "XXX", "XYX", "XXX", 'X', leadIngot, 'Y', new ItemStack(VenusItems.basicItem, 1, 2) });

        //Thermal Fabric T2
        RecipeUtil.addRecipeUpdatable(new ItemStack(VenusItems.basicItem, 1, 3), new Object[] { "   ", "XYX", "   ", 'X', new ItemStack(AsteroidsItems.basicItem, 1, 7), 'Y', deshIngot });

        RecipeUtil.addRecipe(new ItemStack(VenusItems.thermalPaddingTier2, 1, 0), new Object[] { "XXX", "X X", 'X', new ItemStack(VenusItems.basicItem, 1, 3) });
        RecipeUtil.addRecipe(new ItemStack(VenusItems.thermalPaddingTier2, 1, 1), new Object[] { "X X", "XXX", "XXX", 'X', new ItemStack(VenusItems.basicItem, 1, 3) });
        RecipeUtil.addRecipe(new ItemStack(VenusItems.thermalPaddingTier2, 1, 2), new Object[] { "XXX", "X X", "X X", 'X', new ItemStack(VenusItems.basicItem, 1, 3) });
        RecipeUtil.addRecipe(new ItemStack(VenusItems.thermalPaddingTier2, 1, 3), new Object[] { "X X", "X X", 'X', new ItemStack(VenusItems.basicItem, 1, 3) });

        RecipeUtil.addRecipe(new ItemStack(VenusBlocks.geothermalGenerator, 1, 0), new Object[] { "XVX", "WGW", "XYX", 'X', "compressedBronze", 'Y', new ItemStack(VenusItems.basicItem, 1, 1), 'W', new ItemStack(GCBlocks.aluminumWire), 'V', new ItemStack(AsteroidsItems.atmosphericValve), 'G', new ItemStack(GCBlocks.machineBase, 1, BlockMachine.COAL_GENERATOR_METADATA)  } );
    }
}
