package micdoodle8.mods.galacticraft.core.nei;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Set;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.blocks.GCCoreBlocks;
import micdoodle8.mods.galacticraft.core.items.GCCoreItems;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import codechicken.nei.PositionedStack;
import codechicken.nei.api.API;
import codechicken.nei.api.IConfigureNEI;

public class NEIGalacticraftConfig implements IConfigureNEI
{
    private static HashMap<ArrayList<PositionedStack>, PositionedStack> rocketBenchRecipes = new HashMap<ArrayList<PositionedStack>, PositionedStack>();
    private static HashMap<ArrayList<PositionedStack>, PositionedStack> buggyBenchRecipes = new HashMap<ArrayList<PositionedStack>, PositionedStack>();
    private static HashMap<PositionedStack, PositionedStack> refineryRecipes = new HashMap<PositionedStack, PositionedStack>();

    @Override
    public void loadConfig()
    {
        this.registerRecipes();
        API.hideItems(GCCoreItems.hiddenItems);
        API.hideItems(GCCoreBlocks.hiddenBlocks);
        API.registerRecipeHandler(new RocketT1RecipeHandler());
        API.registerUsageHandler(new RocketT1RecipeHandler());
        API.registerRecipeHandler(new BuggyRecipeHandler());
        API.registerUsageHandler(new BuggyRecipeHandler());
        API.registerRecipeHandler(new RefineryRecipeHandler());
        API.registerUsageHandler(new RefineryRecipeHandler());
    }

    @Override
    public String getName()
    {
        return "Galacticraft NEI Plugin";
    }

    @Override
    public String getVersion()
    {
        return GalacticraftCore.LOCALMAJVERSION + "." + GalacticraftCore.LOCALMINVERSION + "." + GalacticraftCore.LOCALBUILDVERSION;
    }

    public void registerRocketBenchRecipe(ArrayList<PositionedStack> input, PositionedStack output)
    {
        NEIGalacticraftConfig.rocketBenchRecipes.put(input, output);
    }

    public void registerBuggyBenchRecipe(ArrayList<PositionedStack> input, PositionedStack output)
    {
        NEIGalacticraftConfig.buggyBenchRecipes.put(input, output);
    }

    public void registerRefineryRecipe(PositionedStack input, PositionedStack output)
    {
        NEIGalacticraftConfig.refineryRecipes.put(input, output);
    }

    public static Set<Entry<ArrayList<PositionedStack>, PositionedStack>> getRocketBenchRecipes()
    {
        return NEIGalacticraftConfig.rocketBenchRecipes.entrySet();
    }

    public static Set<Entry<ArrayList<PositionedStack>, PositionedStack>> getBuggyBenchRecipes()
    {
        return NEIGalacticraftConfig.buggyBenchRecipes.entrySet();
    }

    public static Set<Entry<PositionedStack, PositionedStack>> getRefineryRecipes()
    {
        return NEIGalacticraftConfig.refineryRecipes.entrySet();
    }

    public void registerRecipes()
    {
        final int changey = 23;

        this.registerRefineryRecipe(new PositionedStack(new ItemStack(GCCoreItems.oilCanister, 1, 1), 2, 3), new PositionedStack(new ItemStack(GCCoreItems.fuelCanister, 1, 1), 148, 3));

        ArrayList<PositionedStack> input1 = new ArrayList<PositionedStack>();
        input1.add(new PositionedStack(new ItemStack(GCCoreItems.rocketNoseCone), 45, -8 + changey));
        input1.add(new PositionedStack(new ItemStack(GCCoreItems.heavyPlating), 36, -6 + 0 * 18 + 16 + changey));
        input1.add(new PositionedStack(new ItemStack(GCCoreItems.heavyPlating), 36, -6 + 1 * 18 + 16 + changey));
        input1.add(new PositionedStack(new ItemStack(GCCoreItems.heavyPlating), 36, -6 + 2 * 18 + 16 + changey));
        input1.add(new PositionedStack(new ItemStack(GCCoreItems.heavyPlating), 36, -6 + 3 * 18 + 16 + changey));
        input1.add(new PositionedStack(new ItemStack(GCCoreItems.heavyPlating), 54, -6 + 0 * 18 + 16 + changey));
        input1.add(new PositionedStack(new ItemStack(GCCoreItems.heavyPlating), 54, -6 + 1 * 18 + 16 + changey));
        input1.add(new PositionedStack(new ItemStack(GCCoreItems.heavyPlating), 54, -6 + 2 * 18 + 16 + changey));
        input1.add(new PositionedStack(new ItemStack(GCCoreItems.heavyPlating), 54, -6 + 3 * 18 + 16 + changey));
        input1.add(new PositionedStack(new ItemStack(GCCoreItems.rocketEngine), 45, 82 + changey));
        input1.add(new PositionedStack(new ItemStack(GCCoreItems.rocketFins), 18, 64 + changey));
        input1.add(new PositionedStack(new ItemStack(GCCoreItems.rocketFins), 18, 82 + changey));
        input1.add(new PositionedStack(new ItemStack(GCCoreItems.rocketFins), 72, 64 + changey));
        input1.add(new PositionedStack(new ItemStack(GCCoreItems.rocketFins), 72, 82 + changey));
        this.registerRocketBenchRecipe(input1, new PositionedStack(new ItemStack(GCCoreItems.spaceship, 1, 0), 139, 69 + changey));

        input1 = new ArrayList<PositionedStack>();
        input1.add(new PositionedStack(new ItemStack(GCCoreItems.rocketNoseCone), 45, -8 + changey));
        input1.add(new PositionedStack(new ItemStack(GCCoreItems.heavyPlating), 36, -6 + 0 * 18 + 16 + changey));
        input1.add(new PositionedStack(new ItemStack(GCCoreItems.heavyPlating), 36, -6 + 1 * 18 + 16 + changey));
        input1.add(new PositionedStack(new ItemStack(GCCoreItems.heavyPlating), 36, -6 + 2 * 18 + 16 + changey));
        input1.add(new PositionedStack(new ItemStack(GCCoreItems.heavyPlating), 36, -6 + 3 * 18 + 16 + changey));
        input1.add(new PositionedStack(new ItemStack(GCCoreItems.heavyPlating), 54, -6 + 0 * 18 + 16 + changey));
        input1.add(new PositionedStack(new ItemStack(GCCoreItems.heavyPlating), 54, -6 + 1 * 18 + 16 + changey));
        input1.add(new PositionedStack(new ItemStack(GCCoreItems.heavyPlating), 54, -6 + 2 * 18 + 16 + changey));
        input1.add(new PositionedStack(new ItemStack(GCCoreItems.heavyPlating), 54, -6 + 3 * 18 + 16 + changey));
        input1.add(new PositionedStack(new ItemStack(GCCoreItems.rocketEngine), 45, 82 + changey));
        input1.add(new PositionedStack(new ItemStack(GCCoreItems.rocketFins), 18, 64 + changey));
        input1.add(new PositionedStack(new ItemStack(GCCoreItems.rocketFins), 18, 82 + changey));
        input1.add(new PositionedStack(new ItemStack(GCCoreItems.rocketFins), 72, 64 + changey));
        input1.add(new PositionedStack(new ItemStack(GCCoreItems.rocketFins), 72, 82 + changey));
        input1.add(new PositionedStack(new ItemStack(Block.chest), 90 + 0 * 26, -15 + changey));
        this.registerRocketBenchRecipe(input1, new PositionedStack(new ItemStack(GCCoreItems.spaceship, 1, 1), 139, 69 + changey));

        input1 = new ArrayList<PositionedStack>();
        input1.add(new PositionedStack(new ItemStack(GCCoreItems.rocketNoseCone), 45, -8 + changey));
        input1.add(new PositionedStack(new ItemStack(GCCoreItems.heavyPlating), 36, -6 + 0 * 18 + 16 + changey));
        input1.add(new PositionedStack(new ItemStack(GCCoreItems.heavyPlating), 36, -6 + 1 * 18 + 16 + changey));
        input1.add(new PositionedStack(new ItemStack(GCCoreItems.heavyPlating), 36, -6 + 2 * 18 + 16 + changey));
        input1.add(new PositionedStack(new ItemStack(GCCoreItems.heavyPlating), 36, -6 + 3 * 18 + 16 + changey));
        input1.add(new PositionedStack(new ItemStack(GCCoreItems.heavyPlating), 54, -6 + 0 * 18 + 16 + changey));
        input1.add(new PositionedStack(new ItemStack(GCCoreItems.heavyPlating), 54, -6 + 1 * 18 + 16 + changey));
        input1.add(new PositionedStack(new ItemStack(GCCoreItems.heavyPlating), 54, -6 + 2 * 18 + 16 + changey));
        input1.add(new PositionedStack(new ItemStack(GCCoreItems.heavyPlating), 54, -6 + 3 * 18 + 16 + changey));
        input1.add(new PositionedStack(new ItemStack(GCCoreItems.rocketEngine), 45, 82 + changey));
        input1.add(new PositionedStack(new ItemStack(GCCoreItems.rocketFins), 18, 64 + changey));
        input1.add(new PositionedStack(new ItemStack(GCCoreItems.rocketFins), 18, 82 + changey));
        input1.add(new PositionedStack(new ItemStack(GCCoreItems.rocketFins), 72, 64 + changey));
        input1.add(new PositionedStack(new ItemStack(GCCoreItems.rocketFins), 72, 82 + changey));
        input1.add(new PositionedStack(new ItemStack(Block.chest), 90 + 1 * 26, -15 + changey));
        this.registerRocketBenchRecipe(input1, new PositionedStack(new ItemStack(GCCoreItems.spaceship, 1, 1), 139, 69 + changey));

        input1 = new ArrayList<PositionedStack>();
        input1.add(new PositionedStack(new ItemStack(GCCoreItems.rocketNoseCone), 45, -8 + changey));
        input1.add(new PositionedStack(new ItemStack(GCCoreItems.heavyPlating), 36, -6 + 0 * 18 + 16 + changey));
        input1.add(new PositionedStack(new ItemStack(GCCoreItems.heavyPlating), 36, -6 + 1 * 18 + 16 + changey));
        input1.add(new PositionedStack(new ItemStack(GCCoreItems.heavyPlating), 36, -6 + 2 * 18 + 16 + changey));
        input1.add(new PositionedStack(new ItemStack(GCCoreItems.heavyPlating), 36, -6 + 3 * 18 + 16 + changey));
        input1.add(new PositionedStack(new ItemStack(GCCoreItems.heavyPlating), 54, -6 + 0 * 18 + 16 + changey));
        input1.add(new PositionedStack(new ItemStack(GCCoreItems.heavyPlating), 54, -6 + 1 * 18 + 16 + changey));
        input1.add(new PositionedStack(new ItemStack(GCCoreItems.heavyPlating), 54, -6 + 2 * 18 + 16 + changey));
        input1.add(new PositionedStack(new ItemStack(GCCoreItems.heavyPlating), 54, -6 + 3 * 18 + 16 + changey));
        input1.add(new PositionedStack(new ItemStack(GCCoreItems.rocketEngine), 45, 82 + changey));
        input1.add(new PositionedStack(new ItemStack(GCCoreItems.rocketFins), 18, 64 + changey));
        input1.add(new PositionedStack(new ItemStack(GCCoreItems.rocketFins), 18, 82 + changey));
        input1.add(new PositionedStack(new ItemStack(GCCoreItems.rocketFins), 72, 64 + changey));
        input1.add(new PositionedStack(new ItemStack(GCCoreItems.rocketFins), 72, 82 + changey));
        input1.add(new PositionedStack(new ItemStack(Block.chest), 90 + 2 * 26, -15 + changey));
        this.registerRocketBenchRecipe(input1, new PositionedStack(new ItemStack(GCCoreItems.spaceship, 1, 1), 139, 69 + changey));

        input1 = new ArrayList<PositionedStack>();
        input1.add(new PositionedStack(new ItemStack(GCCoreItems.rocketNoseCone), 45, -8 + changey));
        input1.add(new PositionedStack(new ItemStack(GCCoreItems.heavyPlating), 36, -6 + 0 * 18 + 16 + changey));
        input1.add(new PositionedStack(new ItemStack(GCCoreItems.heavyPlating), 36, -6 + 1 * 18 + 16 + changey));
        input1.add(new PositionedStack(new ItemStack(GCCoreItems.heavyPlating), 36, -6 + 2 * 18 + 16 + changey));
        input1.add(new PositionedStack(new ItemStack(GCCoreItems.heavyPlating), 36, -6 + 3 * 18 + 16 + changey));
        input1.add(new PositionedStack(new ItemStack(GCCoreItems.heavyPlating), 54, -6 + 0 * 18 + 16 + changey));
        input1.add(new PositionedStack(new ItemStack(GCCoreItems.heavyPlating), 54, -6 + 1 * 18 + 16 + changey));
        input1.add(new PositionedStack(new ItemStack(GCCoreItems.heavyPlating), 54, -6 + 2 * 18 + 16 + changey));
        input1.add(new PositionedStack(new ItemStack(GCCoreItems.heavyPlating), 54, -6 + 3 * 18 + 16 + changey));
        input1.add(new PositionedStack(new ItemStack(GCCoreItems.rocketEngine), 45, 82 + changey));
        input1.add(new PositionedStack(new ItemStack(GCCoreItems.rocketFins), 18, 64 + changey));
        input1.add(new PositionedStack(new ItemStack(GCCoreItems.rocketFins), 18, 82 + changey));
        input1.add(new PositionedStack(new ItemStack(GCCoreItems.rocketFins), 72, 64 + changey));
        input1.add(new PositionedStack(new ItemStack(GCCoreItems.rocketFins), 72, 82 + changey));
        input1.add(new PositionedStack(new ItemStack(Block.chest), 90 + 0 * 26, -15 + changey));
        input1.add(new PositionedStack(new ItemStack(Block.chest), 90 + 1 * 26, -15 + changey));
        this.registerRocketBenchRecipe(input1, new PositionedStack(new ItemStack(GCCoreItems.spaceship, 1, 2), 139, 69 + changey));

        input1 = new ArrayList<PositionedStack>();
        input1.add(new PositionedStack(new ItemStack(GCCoreItems.rocketNoseCone), 45, -8 + changey));
        input1.add(new PositionedStack(new ItemStack(GCCoreItems.heavyPlating), 36, -6 + 0 * 18 + 16 + changey));
        input1.add(new PositionedStack(new ItemStack(GCCoreItems.heavyPlating), 36, -6 + 1 * 18 + 16 + changey));
        input1.add(new PositionedStack(new ItemStack(GCCoreItems.heavyPlating), 36, -6 + 2 * 18 + 16 + changey));
        input1.add(new PositionedStack(new ItemStack(GCCoreItems.heavyPlating), 36, -6 + 3 * 18 + 16 + changey));
        input1.add(new PositionedStack(new ItemStack(GCCoreItems.heavyPlating), 54, -6 + 0 * 18 + 16 + changey));
        input1.add(new PositionedStack(new ItemStack(GCCoreItems.heavyPlating), 54, -6 + 1 * 18 + 16 + changey));
        input1.add(new PositionedStack(new ItemStack(GCCoreItems.heavyPlating), 54, -6 + 2 * 18 + 16 + changey));
        input1.add(new PositionedStack(new ItemStack(GCCoreItems.heavyPlating), 54, -6 + 3 * 18 + 16 + changey));
        input1.add(new PositionedStack(new ItemStack(GCCoreItems.rocketEngine), 45, 82 + changey));
        input1.add(new PositionedStack(new ItemStack(GCCoreItems.rocketFins), 18, 64 + changey));
        input1.add(new PositionedStack(new ItemStack(GCCoreItems.rocketFins), 18, 82 + changey));
        input1.add(new PositionedStack(new ItemStack(GCCoreItems.rocketFins), 72, 64 + changey));
        input1.add(new PositionedStack(new ItemStack(GCCoreItems.rocketFins), 72, 82 + changey));
        input1.add(new PositionedStack(new ItemStack(Block.chest), 90 + 1 * 26, -15 + changey));
        input1.add(new PositionedStack(new ItemStack(Block.chest), 90 + 2 * 26, -15 + changey));
        this.registerRocketBenchRecipe(input1, new PositionedStack(new ItemStack(GCCoreItems.spaceship, 1, 2), 139, 69 + changey));

        input1 = new ArrayList<PositionedStack>();
        input1.add(new PositionedStack(new ItemStack(GCCoreItems.rocketNoseCone), 45, -8 + changey));
        input1.add(new PositionedStack(new ItemStack(GCCoreItems.heavyPlating), 36, -6 + 0 * 18 + 16 + changey));
        input1.add(new PositionedStack(new ItemStack(GCCoreItems.heavyPlating), 36, -6 + 1 * 18 + 16 + changey));
        input1.add(new PositionedStack(new ItemStack(GCCoreItems.heavyPlating), 36, -6 + 2 * 18 + 16 + changey));
        input1.add(new PositionedStack(new ItemStack(GCCoreItems.heavyPlating), 36, -6 + 3 * 18 + 16 + changey));
        input1.add(new PositionedStack(new ItemStack(GCCoreItems.heavyPlating), 54, -6 + 0 * 18 + 16 + changey));
        input1.add(new PositionedStack(new ItemStack(GCCoreItems.heavyPlating), 54, -6 + 1 * 18 + 16 + changey));
        input1.add(new PositionedStack(new ItemStack(GCCoreItems.heavyPlating), 54, -6 + 2 * 18 + 16 + changey));
        input1.add(new PositionedStack(new ItemStack(GCCoreItems.heavyPlating), 54, -6 + 3 * 18 + 16 + changey));
        input1.add(new PositionedStack(new ItemStack(GCCoreItems.rocketEngine), 45, 82 + changey));
        input1.add(new PositionedStack(new ItemStack(GCCoreItems.rocketFins), 18, 64 + changey));
        input1.add(new PositionedStack(new ItemStack(GCCoreItems.rocketFins), 18, 82 + changey));
        input1.add(new PositionedStack(new ItemStack(GCCoreItems.rocketFins), 72, 64 + changey));
        input1.add(new PositionedStack(new ItemStack(GCCoreItems.rocketFins), 72, 82 + changey));
        input1.add(new PositionedStack(new ItemStack(Block.chest), 90 + 0 * 26, -15 + changey));
        input1.add(new PositionedStack(new ItemStack(Block.chest), 90 + 2 * 26, -15 + changey));
        this.registerRocketBenchRecipe(input1, new PositionedStack(new ItemStack(GCCoreItems.spaceship, 1, 2), 139, 69 + changey));

        input1 = new ArrayList<PositionedStack>();
        input1.add(new PositionedStack(new ItemStack(GCCoreItems.rocketNoseCone), 45, -8 + changey));
        input1.add(new PositionedStack(new ItemStack(GCCoreItems.heavyPlating), 36, -6 + 0 * 18 + 16 + changey));
        input1.add(new PositionedStack(new ItemStack(GCCoreItems.heavyPlating), 36, -6 + 1 * 18 + 16 + changey));
        input1.add(new PositionedStack(new ItemStack(GCCoreItems.heavyPlating), 36, -6 + 2 * 18 + 16 + changey));
        input1.add(new PositionedStack(new ItemStack(GCCoreItems.heavyPlating), 36, -6 + 3 * 18 + 16 + changey));
        input1.add(new PositionedStack(new ItemStack(GCCoreItems.heavyPlating), 54, -6 + 0 * 18 + 16 + changey));
        input1.add(new PositionedStack(new ItemStack(GCCoreItems.heavyPlating), 54, -6 + 1 * 18 + 16 + changey));
        input1.add(new PositionedStack(new ItemStack(GCCoreItems.heavyPlating), 54, -6 + 2 * 18 + 16 + changey));
        input1.add(new PositionedStack(new ItemStack(GCCoreItems.heavyPlating), 54, -6 + 3 * 18 + 16 + changey));
        input1.add(new PositionedStack(new ItemStack(GCCoreItems.rocketEngine), 45, 82 + changey));
        input1.add(new PositionedStack(new ItemStack(GCCoreItems.rocketFins), 18, 64 + changey));
        input1.add(new PositionedStack(new ItemStack(GCCoreItems.rocketFins), 18, 82 + changey));
        input1.add(new PositionedStack(new ItemStack(GCCoreItems.rocketFins), 72, 64 + changey));
        input1.add(new PositionedStack(new ItemStack(GCCoreItems.rocketFins), 72, 82 + changey));
        input1.add(new PositionedStack(new ItemStack(Block.chest), 90 + 0 * 26, -15 + changey));
        input1.add(new PositionedStack(new ItemStack(Block.chest), 90 + 1 * 26, -15 + changey));
        input1.add(new PositionedStack(new ItemStack(Block.chest), 90 + 2 * 26, -15 + changey));
        this.registerRocketBenchRecipe(input1, new PositionedStack(new ItemStack(GCCoreItems.spaceship, 1, 3), 139, 69 + changey));

        input1 = new ArrayList<PositionedStack>();
        input1.add(new PositionedStack(new ItemStack(GCCoreItems.buggyMaterial, 1, 0), 18, 37));
        input1.add(new PositionedStack(new ItemStack(GCCoreItems.buggyMaterial, 1, 0), 18, 91));
        input1.add(new PositionedStack(new ItemStack(GCCoreItems.buggyMaterial, 1, 0), 90, 37));
        input1.add(new PositionedStack(new ItemStack(GCCoreItems.buggyMaterial, 1, 0), 90, 91));
        for (int x = 0; x < 3; x++)
        {
            for (int y = 0; y < 4; y++)
            {
                if (x == 1 && y == 1)
                {
                    input1.add(new PositionedStack(new ItemStack(GCCoreItems.buggyMaterial, 1, 1), 36 + x * 18, 37 + y * 18));
                }
                else
                {
                    input1.add(new PositionedStack(new ItemStack(GCCoreItems.heavyPlating), 36 + x * 18, 37 + y * 18));
                }
            }
        }
        this.registerBuggyBenchRecipe(input1, new PositionedStack(new ItemStack(GCCoreItems.buggy, 1, 0), 139, 78 + changey));

        input1 = new ArrayList<PositionedStack>();
        input1.add(new PositionedStack(new ItemStack(GCCoreItems.buggyMaterial, 1, 0), 18, 37));
        input1.add(new PositionedStack(new ItemStack(GCCoreItems.buggyMaterial, 1, 0), 18, 91));
        input1.add(new PositionedStack(new ItemStack(GCCoreItems.buggyMaterial, 1, 0), 90, 37));
        input1.add(new PositionedStack(new ItemStack(GCCoreItems.buggyMaterial, 1, 0), 90, 91));
        for (int x = 0; x < 3; x++)
        {
            for (int y = 0; y < 4; y++)
            {
                if (x == 1 && y == 1)
                {
                    input1.add(new PositionedStack(new ItemStack(GCCoreItems.buggyMaterial, 1, 1), 36 + x * 18, 37 + y * 18));
                }
                else
                {
                    input1.add(new PositionedStack(new ItemStack(GCCoreItems.heavyPlating), 36 + x * 18, 37 + y * 18));
                }
            }
        }
        input1.add(new PositionedStack(new ItemStack(GCCoreItems.buggyMaterial, 1, 2), 90 + 0 * 26, -15 + changey));
        this.registerBuggyBenchRecipe(input1, new PositionedStack(new ItemStack(GCCoreItems.buggy, 1, 1), 139, 78 + changey));

        input1 = new ArrayList<PositionedStack>();
        input1.add(new PositionedStack(new ItemStack(GCCoreItems.buggyMaterial, 1, 0), 18, 37));
        input1.add(new PositionedStack(new ItemStack(GCCoreItems.buggyMaterial, 1, 0), 18, 91));
        input1.add(new PositionedStack(new ItemStack(GCCoreItems.buggyMaterial, 1, 0), 90, 37));
        input1.add(new PositionedStack(new ItemStack(GCCoreItems.buggyMaterial, 1, 0), 90, 91));
        for (int x = 0; x < 3; x++)
        {
            for (int y = 0; y < 4; y++)
            {
                if (x == 1 && y == 1)
                {
                    input1.add(new PositionedStack(new ItemStack(GCCoreItems.buggyMaterial, 1, 1), 36 + x * 18, 37 + y * 18));
                }
                else
                {
                    input1.add(new PositionedStack(new ItemStack(GCCoreItems.heavyPlating), 36 + x * 18, 37 + y * 18));
                }
            }
        }
        input1.add(new PositionedStack(new ItemStack(GCCoreItems.buggyMaterial, 1, 2), 90 + 1 * 26, -15 + changey));
        this.registerBuggyBenchRecipe(input1, new PositionedStack(new ItemStack(GCCoreItems.buggy, 1, 1), 139, 78 + changey));

        input1 = new ArrayList<PositionedStack>();
        input1.add(new PositionedStack(new ItemStack(GCCoreItems.buggyMaterial, 1, 0), 18, 37));
        input1.add(new PositionedStack(new ItemStack(GCCoreItems.buggyMaterial, 1, 0), 18, 91));
        input1.add(new PositionedStack(new ItemStack(GCCoreItems.buggyMaterial, 1, 0), 90, 37));
        input1.add(new PositionedStack(new ItemStack(GCCoreItems.buggyMaterial, 1, 0), 90, 91));
        for (int x = 0; x < 3; x++)
        {
            for (int y = 0; y < 4; y++)
            {
                if (x == 1 && y == 1)
                {
                    input1.add(new PositionedStack(new ItemStack(GCCoreItems.buggyMaterial, 1, 1), 36 + x * 18, 37 + y * 18));
                }
                else
                {
                    input1.add(new PositionedStack(new ItemStack(GCCoreItems.heavyPlating), 36 + x * 18, 37 + y * 18));
                }
            }
        }
        input1.add(new PositionedStack(new ItemStack(GCCoreItems.buggyMaterial, 1, 2), 90 + 2 * 26, -15 + changey));
        this.registerBuggyBenchRecipe(input1, new PositionedStack(new ItemStack(GCCoreItems.buggy, 1, 1), 139, 78 + changey));

        input1 = new ArrayList<PositionedStack>();
        input1.add(new PositionedStack(new ItemStack(GCCoreItems.buggyMaterial, 1, 0), 18, 37));
        input1.add(new PositionedStack(new ItemStack(GCCoreItems.buggyMaterial, 1, 0), 18, 91));
        input1.add(new PositionedStack(new ItemStack(GCCoreItems.buggyMaterial, 1, 0), 90, 37));
        input1.add(new PositionedStack(new ItemStack(GCCoreItems.buggyMaterial, 1, 0), 90, 91));
        for (int x = 0; x < 3; x++)
        {
            for (int y = 0; y < 4; y++)
            {
                if (x == 1 && y == 1)
                {
                    input1.add(new PositionedStack(new ItemStack(GCCoreItems.buggyMaterial, 1, 1), 36 + x * 18, 37 + y * 18));
                }
                else
                {
                    input1.add(new PositionedStack(new ItemStack(GCCoreItems.heavyPlating), 36 + x * 18, 37 + y * 18));
                }
            }
        }
        input1.add(new PositionedStack(new ItemStack(GCCoreItems.buggyMaterial, 1, 2), 90 + 0 * 26, -15 + changey));
        input1.add(new PositionedStack(new ItemStack(GCCoreItems.buggyMaterial, 1, 2), 90 + 1 * 26, -15 + changey));
        this.registerBuggyBenchRecipe(input1, new PositionedStack(new ItemStack(GCCoreItems.buggy, 1, 2), 139, 78 + changey));

        input1 = new ArrayList<PositionedStack>();
        input1.add(new PositionedStack(new ItemStack(GCCoreItems.buggyMaterial, 1, 0), 18, 37));
        input1.add(new PositionedStack(new ItemStack(GCCoreItems.buggyMaterial, 1, 0), 18, 91));
        input1.add(new PositionedStack(new ItemStack(GCCoreItems.buggyMaterial, 1, 0), 90, 37));
        input1.add(new PositionedStack(new ItemStack(GCCoreItems.buggyMaterial, 1, 0), 90, 91));
        for (int x = 0; x < 3; x++)
        {
            for (int y = 0; y < 4; y++)
            {
                if (x == 1 && y == 1)
                {
                    input1.add(new PositionedStack(new ItemStack(GCCoreItems.buggyMaterial, 1, 1), 36 + x * 18, 37 + y * 18));
                }
                else
                {
                    input1.add(new PositionedStack(new ItemStack(GCCoreItems.heavyPlating), 36 + x * 18, 37 + y * 18));
                }
            }
        }
        input1.add(new PositionedStack(new ItemStack(GCCoreItems.buggyMaterial, 1, 2), 90 + 1 * 26, -15 + changey));
        input1.add(new PositionedStack(new ItemStack(GCCoreItems.buggyMaterial, 1, 2), 90 + 2 * 26, -15 + changey));
        this.registerBuggyBenchRecipe(input1, new PositionedStack(new ItemStack(GCCoreItems.buggy, 1, 2), 139, 78 + changey));

        input1 = new ArrayList<PositionedStack>();
        input1.add(new PositionedStack(new ItemStack(GCCoreItems.buggyMaterial, 1, 0), 18, 37));
        input1.add(new PositionedStack(new ItemStack(GCCoreItems.buggyMaterial, 1, 0), 18, 91));
        input1.add(new PositionedStack(new ItemStack(GCCoreItems.buggyMaterial, 1, 0), 90, 37));
        input1.add(new PositionedStack(new ItemStack(GCCoreItems.buggyMaterial, 1, 0), 90, 91));
        for (int x = 0; x < 3; x++)
        {
            for (int y = 0; y < 4; y++)
            {
                if (x == 1 && y == 1)
                {
                    input1.add(new PositionedStack(new ItemStack(GCCoreItems.buggyMaterial, 1, 1), 36 + x * 18, 37 + y * 18));
                }
                else
                {
                    input1.add(new PositionedStack(new ItemStack(GCCoreItems.heavyPlating), 36 + x * 18, 37 + y * 18));
                }
            }
        }
        input1.add(new PositionedStack(new ItemStack(GCCoreItems.buggyMaterial, 1, 2), 90 + 0 * 26, -15 + changey));
        input1.add(new PositionedStack(new ItemStack(GCCoreItems.buggyMaterial, 1, 2), 90 + 2 * 26, -15 + changey));
        this.registerBuggyBenchRecipe(input1, new PositionedStack(new ItemStack(GCCoreItems.buggy, 1, 2), 139, 78 + changey));

        input1 = new ArrayList<PositionedStack>();
        input1.add(new PositionedStack(new ItemStack(GCCoreItems.buggyMaterial, 1, 0), 18, 37));
        input1.add(new PositionedStack(new ItemStack(GCCoreItems.buggyMaterial, 1, 0), 18, 91));
        input1.add(new PositionedStack(new ItemStack(GCCoreItems.buggyMaterial, 1, 0), 90, 37));
        input1.add(new PositionedStack(new ItemStack(GCCoreItems.buggyMaterial, 1, 0), 90, 91));
        for (int x = 0; x < 3; x++)
        {
            for (int y = 0; y < 4; y++)
            {
                if (x == 1 && y == 1)
                {
                    input1.add(new PositionedStack(new ItemStack(GCCoreItems.buggyMaterial, 1, 1), 36 + x * 18, 37 + y * 18));
                }
                else
                {
                    input1.add(new PositionedStack(new ItemStack(GCCoreItems.heavyPlating), 36 + x * 18, 37 + y * 18));
                }
            }
        }
        input1.add(new PositionedStack(new ItemStack(GCCoreItems.buggyMaterial, 1, 2), 90 + 0 * 26, -15 + changey));
        input1.add(new PositionedStack(new ItemStack(GCCoreItems.buggyMaterial, 1, 2), 90 + 1 * 26, -15 + changey));
        input1.add(new PositionedStack(new ItemStack(GCCoreItems.buggyMaterial, 1, 2), 90 + 2 * 26, -15 + changey));
        this.registerBuggyBenchRecipe(input1, new PositionedStack(new ItemStack(GCCoreItems.buggy, 1, 3), 139, 78 + changey));
    }
}
