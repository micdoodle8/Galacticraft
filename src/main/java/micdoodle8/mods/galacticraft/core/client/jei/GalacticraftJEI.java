package micdoodle8.mods.galacticraft.core.client.jei;

import mezz.jei.api.*;
import micdoodle8.mods.galacticraft.api.recipe.CompressorRecipes;
import micdoodle8.mods.galacticraft.core.GCBlocks;
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
import micdoodle8.mods.galacticraft.core.client.jei.oxygencompressor.OxygenCompressorRecipeCategory;
import micdoodle8.mods.galacticraft.core.client.jei.oxygencompressor.OxygenCompressorRecipeHandler;
import micdoodle8.mods.galacticraft.core.client.jei.oxygencompressor.OxygenCompressorRecipeMaker;
import micdoodle8.mods.galacticraft.core.client.jei.refinery.RefineryRecipeCategory;
import micdoodle8.mods.galacticraft.core.client.jei.refinery.RefineryRecipeHandler;
import micdoodle8.mods.galacticraft.core.client.jei.refinery.RefineryRecipeMaker;
import micdoodle8.mods.galacticraft.core.client.jei.tier1rocket.Tier1RocketRecipeCategory;
import micdoodle8.mods.galacticraft.core.client.jei.tier1rocket.Tier1RocketRecipeHandler;
import micdoodle8.mods.galacticraft.core.client.jei.tier1rocket.Tier1RocketRecipeMaker;
import micdoodle8.mods.galacticraft.core.util.CompatibilityManager;
import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
import net.minecraft.item.ItemStack;

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
                new OxygenCompressorRecipeCategory(guiHelper),
                new RefineryRecipeCategory(guiHelper));
        registry.addRecipeHandlers(new Tier1RocketRecipeHandler(),
                new BuggyRecipeHandler(),
                new CircuitFabricatorRecipeHandler(),
                new IngotCompressorShapedRecipeHandler(),
                new IngotCompressorShapelessRecipeHandler(),
                new OxygenCompressorRecipeHandler(),
                new RefineryRecipeHandler());
        registry.addRecipes(Tier1RocketRecipeMaker.getRecipesList());
        registry.addRecipes(BuggyRecipeMaker.getRecipesList());
        registry.addRecipes(CircuitFabricatorRecipeMaker.getRecipesList());
        registry.addRecipes(CompressorRecipes.getRecipeListAll());
        registry.addRecipes(OxygenCompressorRecipeMaker.getRecipesList());
        registry.addRecipes(RefineryRecipeMaker.getRecipesList());
        
        this.addInformationPages(registry);
        GCItems.hideItemsJEI(registry.getJeiHelpers().getItemBlacklist());
    }

    private void addInformationPages(IModRegistry registry)
    {
        registry.addDescription(new ItemStack(GCBlocks.oxygenPipe), GCCoreUtil.translate("jei.fluid_pipe.info"));
        registry.addDescription(new ItemStack(GCBlocks.fuelLoader), GCCoreUtil.translate("jei.fuel_loader.info"));
        registry.addDescription(new ItemStack(GCBlocks.oxygenCollector), GCCoreUtil.translate("jei.oxygen_collector.info"));
        registry.addDescription(new ItemStack(GCBlocks.oxygenDistributor), GCCoreUtil.translate("jei.oxygen_distributor.info"));
        registry.addDescription(new ItemStack(GCBlocks.oxygenSealer), GCCoreUtil.translate("jei.oxygen_sealer.info"));
        if (CompatibilityManager.isAppEngLoaded())
        {
            registry.addDescription(new ItemStack(GCBlocks.machineBase2), new String [] { GCCoreUtil.translate("jei.electric_compressor.info"), GCCoreUtil.translate("jei.electric_compressor.appeng.info") });
        }
        else
        {
            registry.addDescription(new ItemStack(GCBlocks.machineBase2), GCCoreUtil.translate("jei.electric_compressor.info"));
        }
        registry.addDescription(new ItemStack(GCBlocks.crafting), GCCoreUtil.translate("jei.magnetic_crafting.info"));
        registry.addDescription(new ItemStack(GCBlocks.brightLamp), GCCoreUtil.translate("jei.arc_lamp.info"));
        registry.addDescription(new ItemStack(GCItems.wrench), GCCoreUtil.translate("jei.wrench.info"));
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
