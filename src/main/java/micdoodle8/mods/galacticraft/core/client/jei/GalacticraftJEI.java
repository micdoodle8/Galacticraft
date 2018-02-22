package micdoodle8.mods.galacticraft.core.client.jei;

import mezz.jei.api.*;
import micdoodle8.mods.galacticraft.api.recipe.CompressorRecipes;
import micdoodle8.mods.galacticraft.core.GCItems;
import micdoodle8.mods.galacticraft.core.client.jei.buggy.BuggyRecipeCategory;
import micdoodle8.mods.galacticraft.core.client.jei.buggy.BuggyRecipeHandler;
import micdoodle8.mods.galacticraft.core.client.jei.buggy.BuggyRecipeMaker;
import micdoodle8.mods.galacticraft.core.client.jei.circuitfabricator.CircuitFabricatorRecipeCategory;
import micdoodle8.mods.galacticraft.core.client.jei.circuitfabricator.CircuitFabricatorRecipeHandler;
import micdoodle8.mods.galacticraft.core.client.jei.circuitfabricator.CircuitFabricatorRecipeMaker;
import micdoodle8.mods.galacticraft.core.client.jei.ingotcompressor.IngotCompressorRecipeCategory;
import micdoodle8.mods.galacticraft.core.client.jei.ingotcompressor.IngotCompressorShapedRecipeHandler;
import micdoodle8.mods.galacticraft.core.client.jei.ingotcompressor.IngotCompressorShapelessRecipeHandler;
import micdoodle8.mods.galacticraft.core.client.jei.refinery.RefineryRecipeCategory;
import micdoodle8.mods.galacticraft.core.client.jei.refinery.RefineryRecipeHandler;
import micdoodle8.mods.galacticraft.core.client.jei.refinery.RefineryRecipeMaker;
import micdoodle8.mods.galacticraft.core.client.jei.tier1rocket.Tier1RocketRecipeCategory;
import micdoodle8.mods.galacticraft.core.client.jei.tier1rocket.Tier1RocketRecipeHandler;
import micdoodle8.mods.galacticraft.core.client.jei.tier1rocket.Tier1RocketRecipeMaker;
import javax.annotation.Nonnull;

@JEIPlugin
public class GalacticraftJEI extends BlankModPlugin
{
    private static IModRegistry registryCached = null;

    @Override
    public void register(@Nonnull IModRegistry registry)
    {
        registryCached = registry;
        IGuiHelper guiHelper = registry.getJeiHelpers().getGuiHelper();
        registry.addRecipeCategories(new Tier1RocketRecipeCategory(guiHelper),
                new BuggyRecipeCategory(guiHelper),
                new CircuitFabricatorRecipeCategory(guiHelper),
                new IngotCompressorRecipeCategory(guiHelper),
                new RefineryRecipeCategory(guiHelper));
        registry.addRecipeHandlers(new Tier1RocketRecipeHandler(),
                new BuggyRecipeHandler(),
                new CircuitFabricatorRecipeHandler(),
                new IngotCompressorShapedRecipeHandler(),
                new IngotCompressorShapelessRecipeHandler(),
                new RefineryRecipeHandler());
        registry.addRecipes(Tier1RocketRecipeMaker.getRecipesList());
        registry.addRecipes(BuggyRecipeMaker.getRecipesList());
        registry.addRecipes(CircuitFabricatorRecipeMaker.getRecipesList());
        registry.addRecipes(CompressorRecipes.getRecipeListAll());
        registry.addRecipes(RefineryRecipeMaker.getRecipesList());
        
        GCItems.hideItemsJEI(registry.getJeiHelpers().getItemBlacklist());
    }

    public static void updateHiddenSteel(boolean hide)
    {
        // Not possible in Minecraft 1.8.9
    }

    public static void updateHiddenAdventure(boolean hide)
    {
        // Not possible in Minecraft 1.8.9
    }
}
