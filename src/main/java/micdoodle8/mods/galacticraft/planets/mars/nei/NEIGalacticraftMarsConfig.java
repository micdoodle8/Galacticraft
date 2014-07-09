package micdoodle8.mods.galacticraft.planets.mars.nei;

import codechicken.nei.PositionedStack;
import codechicken.nei.api.API;
import codechicken.nei.api.IConfigureNEI;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.items.GCItems;
import micdoodle8.mods.galacticraft.planets.mars.blocks.MarsBlocks;
import micdoodle8.mods.galacticraft.planets.mars.items.MarsItems;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class NEIGalacticraftMarsConfig implements IConfigureNEI
{
    private static HashMap<ArrayList<PositionedStack>, PositionedStack> rocketBenchRecipes = new HashMap<ArrayList<PositionedStack>, PositionedStack>();
    private static HashMap<ArrayList<PositionedStack>, PositionedStack> cargoBenchRecipes = new HashMap<ArrayList<PositionedStack>, PositionedStack>();

	@Override
	public void loadConfig()
	{
        this.registerRecipes();
        API.registerRecipeHandler(new RocketT2RecipeHandler());
        API.registerUsageHandler(new RocketT2RecipeHandler());
        API.registerRecipeHandler(new CargoRocketRecipeHandler());
        API.registerUsageHandler(new CargoRocketRecipeHandler());
		API.registerHighlightIdentifier(MarsBlocks.marsBlock, new GCMarsNEIHighlightHandler());
	}

	@Override
	public String getName()
	{
		return "Galacticraft Mars NEI Plugin";
	}

	@Override
	public String getVersion()
	{
		return GalacticraftCore.LOCALMAJVERSION + "." + GalacticraftCore.LOCALMINVERSION + "." + GalacticraftCore.LOCALBUILDVERSION;
	}

    public void registerRocketBenchRecipe(ArrayList<PositionedStack> input, PositionedStack output)
    {
        NEIGalacticraftMarsConfig.rocketBenchRecipes.put(input, output);
    }

    public void registerCargoBenchRecipe(ArrayList<PositionedStack> input, PositionedStack output)
    {
        NEIGalacticraftMarsConfig.cargoBenchRecipes.put(input, output);
    }

    public static Set<Map.Entry<ArrayList<PositionedStack>, PositionedStack>> getRocketBenchRecipes()
    {
        return NEIGalacticraftMarsConfig.rocketBenchRecipes.entrySet();
    }

    public static Set<Map.Entry<ArrayList<PositionedStack>, PositionedStack>> getCargoBenchRecipes()
    {
        return NEIGalacticraftMarsConfig.cargoBenchRecipes.entrySet();
    }

    public void registerRecipes()
    {
        final int changeY = 15;

        ArrayList<PositionedStack> input1 = new ArrayList<PositionedStack>();

        input1.add(new PositionedStack(new ItemStack(GCItems.partNoseCone), 45, -8 + changeY));
        input1.add(new PositionedStack(new ItemStack(MarsItems.marsItemBasic, 1, 3), 36, -6 + 16 + changeY));
        input1.add(new PositionedStack(new ItemStack(MarsItems.marsItemBasic, 1, 3), 36, -6 + 18 + 16 + changeY));
        input1.add(new PositionedStack(new ItemStack(MarsItems.marsItemBasic, 1, 3), 36, -6 + 36 + 16 + changeY));
        input1.add(new PositionedStack(new ItemStack(MarsItems.marsItemBasic, 1, 3), 36, -6 + 54 + 16 + changeY));
        input1.add(new PositionedStack(new ItemStack(MarsItems.marsItemBasic, 1, 3), 36, -6 + 72 + 16 + changeY));
        input1.add(new PositionedStack(new ItemStack(MarsItems.marsItemBasic, 1, 3), 54, -6 + 16 + changeY));
        input1.add(new PositionedStack(new ItemStack(MarsItems.marsItemBasic, 1, 3), 54, -6 + 18 + 16 + changeY));
        input1.add(new PositionedStack(new ItemStack(MarsItems.marsItemBasic, 1, 3), 54, -6 + 36 + 16 + changeY));
        input1.add(new PositionedStack(new ItemStack(MarsItems.marsItemBasic, 1, 3), 54, -6 + 54 + 16 + changeY));
        input1.add(new PositionedStack(new ItemStack(MarsItems.marsItemBasic, 1, 3), 54, -6 + 72 + 16 + changeY));
        input1.add(new PositionedStack(new ItemStack(GCItems.rocketEngine), 45, 100 + changeY));
        input1.add(new PositionedStack(new ItemStack(GCItems.rocketEngine, 1, 1), 18, 64 + changeY));
        input1.add(new PositionedStack(new ItemStack(GCItems.rocketEngine, 1, 1), 72, 64 + changeY));
        input1.add(new PositionedStack(new ItemStack(GCItems.partFins), 18, 82 + changeY));
        input1.add(new PositionedStack(new ItemStack(GCItems.partFins), 18, 100 + changeY));
        input1.add(new PositionedStack(new ItemStack(GCItems.partFins), 72, 82 + changeY));
        input1.add(new PositionedStack(new ItemStack(GCItems.partFins), 72, 100 + changeY));
        this.registerRocketBenchRecipe(input1, new PositionedStack(new ItemStack(MarsItems.spaceship, 1, 0), 139, 87 + changeY));

        ArrayList<PositionedStack> input2 = new ArrayList<PositionedStack>(input1);
        input2.add(new PositionedStack(new ItemStack(Blocks.chest), 90, -15 + changeY));
        this.registerRocketBenchRecipe(input2, new PositionedStack(new ItemStack(MarsItems.spaceship, 1, 1), 139, 87 + changeY));

        input2 = new ArrayList<PositionedStack>(input1);
        input2.add(new PositionedStack(new ItemStack(Blocks.chest), 90 + 26, -15 + changeY));
        this.registerRocketBenchRecipe(input2, new PositionedStack(new ItemStack(MarsItems.spaceship, 1, 1), 139, 87 + changeY));

        input2 = new ArrayList<PositionedStack>(input1);
        input2.add(new PositionedStack(new ItemStack(Blocks.chest), 90 + 52, -15 + changeY));
        this.registerRocketBenchRecipe(input2, new PositionedStack(new ItemStack(MarsItems.spaceship, 1, 1), 139, 87 + changeY));

        input2 = new ArrayList<PositionedStack>(input1);
        input2.add(new PositionedStack(new ItemStack(Blocks.chest), 90, -15 + changeY));
        input2.add(new PositionedStack(new ItemStack(Blocks.chest), 90 + 26, -15 + changeY));
        this.registerRocketBenchRecipe(input2, new PositionedStack(new ItemStack(MarsItems.spaceship, 1, 2), 139, 87 + changeY));

        input2 = new ArrayList<PositionedStack>(input1);
        input2.add(new PositionedStack(new ItemStack(Blocks.chest), 90 + 26, -15 + changeY));
        input2.add(new PositionedStack(new ItemStack(Blocks.chest), 90 + 52, -15 + changeY));
        this.registerRocketBenchRecipe(input2, new PositionedStack(new ItemStack(MarsItems.spaceship, 1, 2), 139, 87 + changeY));

        input2 = new ArrayList<PositionedStack>(input1);
        input2.add(new PositionedStack(new ItemStack(Blocks.chest), 90, -15 + changeY));
        input2.add(new PositionedStack(new ItemStack(Blocks.chest), 90 + 52, -15 + changeY));
        this.registerRocketBenchRecipe(input2, new PositionedStack(new ItemStack(MarsItems.spaceship, 1, 2), 139, 87 + changeY));

        input2 = new ArrayList<PositionedStack>(input1);
        input2.add(new PositionedStack(new ItemStack(Blocks.chest), 90, -15 + changeY));
        input2.add(new PositionedStack(new ItemStack(Blocks.chest), 90 + 26, -15 + changeY));
        input2.add(new PositionedStack(new ItemStack(Blocks.chest), 90 + 52, -15 + changeY));
        this.registerRocketBenchRecipe(input2, new PositionedStack(new ItemStack(MarsItems.spaceship, 1, 3), 139, 87 + changeY));

        input1 = new ArrayList<PositionedStack>();
        input1.add(new PositionedStack(new ItemStack(GCItems.partNoseCone), 45, 14));
        input1.add(new PositionedStack(new ItemStack(GCItems.basicItem, 1, 14), 45, 32));
        input1.add(new PositionedStack(new ItemStack(MarsItems.marsItemBasic, 1, 3), 36, 50));
        input1.add(new PositionedStack(new ItemStack(MarsItems.marsItemBasic, 1, 3), 36, 68));
        input1.add(new PositionedStack(new ItemStack(MarsItems.marsItemBasic, 1, 3), 36, 86));
        input1.add(new PositionedStack(new ItemStack(MarsItems.marsItemBasic, 1, 3), 54, 50));
        input1.add(new PositionedStack(new ItemStack(MarsItems.marsItemBasic, 1, 3), 54, 68));
        input1.add(new PositionedStack(new ItemStack(MarsItems.marsItemBasic, 1, 3), 54, 86));
        input1.add(new PositionedStack(new ItemStack(GCItems.partFins), 18, 86));
        input1.add(new PositionedStack(new ItemStack(GCItems.partFins), 72, 86));
        input1.add(new PositionedStack(new ItemStack(GCItems.rocketEngine), 45, 104));
        input1.add(new PositionedStack(new ItemStack(GCItems.partFins), 18, 104));
        input1.add(new PositionedStack(new ItemStack(GCItems.partFins), 72, 104));

        input2 = new ArrayList<PositionedStack>(input1);
        input2.add(new PositionedStack(new ItemStack(Blocks.chest), 90, -7 + changeY));
        this.registerCargoBenchRecipe(input2, new PositionedStack(new ItemStack(MarsItems.spaceship, 1, 11), 139, 77 + changeY));

        input2 = new ArrayList<PositionedStack>(input1);
        input2.add(new PositionedStack(new ItemStack(Blocks.chest), 116, -7 + changeY));
        this.registerCargoBenchRecipe(input2, new PositionedStack(new ItemStack(MarsItems.spaceship, 1, 11), 139, 77 + changeY));

        input2 = new ArrayList<PositionedStack>(input1);
        input2.add(new PositionedStack(new ItemStack(Blocks.chest), 90 + 52, -7 + changeY));
        this.registerCargoBenchRecipe(input2, new PositionedStack(new ItemStack(MarsItems.spaceship, 1, 11), 139, 77 + changeY));

        input2 = new ArrayList<PositionedStack>(input1);
        input2.add(new PositionedStack(new ItemStack(Blocks.chest), 90, -7 + changeY));
        input2.add(new PositionedStack(new ItemStack(Blocks.chest), 90 + 26, -7 + changeY));
        this.registerCargoBenchRecipe(input2, new PositionedStack(new ItemStack(MarsItems.spaceship, 1, 12), 139, 77 + changeY));

        input2 = new ArrayList<PositionedStack>(input1);
        input2.add(new PositionedStack(new ItemStack(Blocks.chest), 90, -7 + changeY));
        input2.add(new PositionedStack(new ItemStack(Blocks.chest), 90 + 52, -7 + changeY));
        this.registerCargoBenchRecipe(input2, new PositionedStack(new ItemStack(MarsItems.spaceship, 1, 12), 139, 77 + changeY));

        input2 = new ArrayList<PositionedStack>(input1);
        input2.add(new PositionedStack(new ItemStack(Blocks.chest), 116, -7 + changeY));
        input2.add(new PositionedStack(new ItemStack(Blocks.chest), 90 + 52, -7 + changeY));
        this.registerCargoBenchRecipe(input2, new PositionedStack(new ItemStack(MarsItems.spaceship, 1, 12), 139, 77 + changeY));

        input2 = new ArrayList<PositionedStack>(input1);
        input2.add(new PositionedStack(new ItemStack(Blocks.chest), 90, -7 + changeY));
        input2.add(new PositionedStack(new ItemStack(Blocks.chest), 90 + 26, -7 + changeY));
        input2.add(new PositionedStack(new ItemStack(Blocks.chest), 90 + 52, -7 + changeY));
        this.registerCargoBenchRecipe(input2, new PositionedStack(new ItemStack(MarsItems.spaceship, 1, 13), 139, 77 + changeY));
    }
}
