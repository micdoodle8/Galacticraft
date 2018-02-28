package micdoodle8.mods.galacticraft.core.recipe;

import micdoodle8.mods.galacticraft.core.GCBlocks;
import micdoodle8.mods.galacticraft.core.GCItems;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.planets.mars.blocks.MarsBlocks;
import micdoodle8.mods.galacticraft.planets.mars.items.MarsItems;
import net.minecraft.item.ItemStack;
import de.ellpeck.actuallyadditions.api.ActuallyAdditionsAPI;

public class CompatModuleActuallyAdditions
{
    public static void addRecipes()
    {
        ActuallyAdditionsAPI.addCrusherRecipe("oreAluminum", "dustAluminum", 2);
        // Silicon ore :- gives 5 silicon  (normally 1-5 depending on fortune enchantment)
        ActuallyAdditionsAPI.addCrusherRecipe(new ItemStack(GCBlocks.basicBlock, 1, 8), new ItemStack(GCItems.basicItem, 5, 2), null, 0);
        // Cheese ore :- gives 2 cheese curd (normally 1, fortune enchantment gives chance of 2)
        ActuallyAdditionsAPI.addCrusherRecipe(new ItemStack(GCBlocks.blockMoon, 1, 2), new ItemStack(GCItems.cheeseCurd, 2, 0), null, 0);
        if (GalacticraftCore.isPlanetsLoaded)
        {
            // Desh ore :- gives 2 raw desh (normally 1, fortune enchantment gives chance of 2)
            ActuallyAdditionsAPI.addCrusherRecipe(new ItemStack(MarsBlocks.marsBlock, 1, 2), new ItemStack(MarsItems.marsItemBasic, 2, 0), null, 0);
        }
    }
}
