package micdoodle8.mods.galacticraft.planets.asteroids.nei;

import codechicken.nei.PositionedStack;
import codechicken.nei.api.API;
import codechicken.nei.api.IConfigureNEI;
import micdoodle8.mods.galacticraft.core.Constants;
import micdoodle8.mods.galacticraft.core.items.GCItems;
import micdoodle8.mods.galacticraft.planets.asteroids.items.AsteroidsItems;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class NEIGalacticraftAsteroidsConfig implements IConfigureNEI
{
    private static HashMap<ArrayList<PositionedStack>, PositionedStack> rocketBenchRecipes = new HashMap<ArrayList<PositionedStack>, PositionedStack>();

    @Override
    public void loadConfig()
    {
        this.registerRecipes();
        API.registerRecipeHandler(new RocketT3RecipeHandler());
        API.registerUsageHandler(new RocketT3RecipeHandler());
    }

    @Override
    public String getName()
    {
        return "Galacticraft Asteroids NEI Plugin";
    }

    @Override
    public String getVersion()
    {
        return Constants.LOCALMAJVERSION + "." + Constants.LOCALMINVERSION + "." + Constants.LOCALBUILDVERSION;
    }

    public void registerRocketBenchRecipe(ArrayList<PositionedStack> input, PositionedStack output)
    {
        NEIGalacticraftAsteroidsConfig.rocketBenchRecipes.put(input, output);
    }

    public static Set<Map.Entry<ArrayList<PositionedStack>, PositionedStack>> getRocketBenchRecipes()
    {
        return NEIGalacticraftAsteroidsConfig.rocketBenchRecipes.entrySet();
    }

    public void registerRecipes()
    {
        final int changeY = 15;

        ArrayList<PositionedStack> input1 = new ArrayList<PositionedStack>();

        input1.add(new PositionedStack(new ItemStack(AsteroidsItems.heavyNoseCone), 45, -8 + changeY));
        input1.add(new PositionedStack(new ItemStack(AsteroidsItems.basicItem, 1, 0), 36, -6 + 16 + changeY));
        input1.add(new PositionedStack(new ItemStack(AsteroidsItems.basicItem, 1, 0), 36, -6 + 18 + 16 + changeY));
        input1.add(new PositionedStack(new ItemStack(AsteroidsItems.basicItem, 1, 0), 36, -6 + 36 + 16 + changeY));
        input1.add(new PositionedStack(new ItemStack(AsteroidsItems.basicItem, 1, 0), 36, -6 + 54 + 16 + changeY));
        input1.add(new PositionedStack(new ItemStack(AsteroidsItems.basicItem, 1, 0), 36, -6 + 72 + 16 + changeY));
        input1.add(new PositionedStack(new ItemStack(AsteroidsItems.basicItem, 1, 0), 54, -6 + 16 + changeY));
        input1.add(new PositionedStack(new ItemStack(AsteroidsItems.basicItem, 1, 0), 54, -6 + 18 + 16 + changeY));
        input1.add(new PositionedStack(new ItemStack(AsteroidsItems.basicItem, 1, 0), 54, -6 + 36 + 16 + changeY));
        input1.add(new PositionedStack(new ItemStack(AsteroidsItems.basicItem, 1, 0), 54, -6 + 54 + 16 + changeY));
        input1.add(new PositionedStack(new ItemStack(AsteroidsItems.basicItem, 1, 0), 54, -6 + 72 + 16 + changeY));
        input1.add(new PositionedStack(new ItemStack(AsteroidsItems.basicItem, 1, 1), 45, 100 + changeY));
        input1.add(new PositionedStack(new ItemStack(GCItems.rocketEngine, 1, 1), 18, 64 + changeY));
        input1.add(new PositionedStack(new ItemStack(GCItems.rocketEngine, 1, 1), 72, 64 + changeY));
        input1.add(new PositionedStack(new ItemStack(AsteroidsItems.basicItem, 1, 2), 18, 82 + changeY));
        input1.add(new PositionedStack(new ItemStack(AsteroidsItems.basicItem, 1, 2), 18, 100 + changeY));
        input1.add(new PositionedStack(new ItemStack(AsteroidsItems.basicItem, 1, 2), 72, 82 + changeY));
        input1.add(new PositionedStack(new ItemStack(AsteroidsItems.basicItem, 1, 2), 72, 100 + changeY));
        this.registerRocketBenchRecipe(input1, new PositionedStack(new ItemStack(AsteroidsItems.tier3Rocket, 1, 0), 139, 87 + changeY));

        ArrayList<PositionedStack> input2 = new ArrayList<PositionedStack>(input1);
        input2.add(new PositionedStack(new ItemStack(Blocks.chest), 90, -15 + changeY));
        this.registerRocketBenchRecipe(input2, new PositionedStack(new ItemStack(AsteroidsItems.tier3Rocket, 1, 1), 139, 87 + changeY));

        input2 = new ArrayList<PositionedStack>(input1);
        input2.add(new PositionedStack(new ItemStack(Blocks.chest), 90 + 26, -15 + changeY));
        this.registerRocketBenchRecipe(input2, new PositionedStack(new ItemStack(AsteroidsItems.tier3Rocket, 1, 1), 139, 87 + changeY));

        input2 = new ArrayList<PositionedStack>(input1);
        input2.add(new PositionedStack(new ItemStack(Blocks.chest), 90 + 52, -15 + changeY));
        this.registerRocketBenchRecipe(input2, new PositionedStack(new ItemStack(AsteroidsItems.tier3Rocket, 1, 1), 139, 87 + changeY));

        input2 = new ArrayList<PositionedStack>(input1);
        input2.add(new PositionedStack(new ItemStack(Blocks.chest), 90, -15 + changeY));
        input2.add(new PositionedStack(new ItemStack(Blocks.chest), 90 + 26, -15 + changeY));
        this.registerRocketBenchRecipe(input2, new PositionedStack(new ItemStack(AsteroidsItems.tier3Rocket, 1, 2), 139, 87 + changeY));

        input2 = new ArrayList<PositionedStack>(input1);
        input2.add(new PositionedStack(new ItemStack(Blocks.chest), 90 + 26, -15 + changeY));
        input2.add(new PositionedStack(new ItemStack(Blocks.chest), 90 + 52, -15 + changeY));
        this.registerRocketBenchRecipe(input2, new PositionedStack(new ItemStack(AsteroidsItems.tier3Rocket, 1, 2), 139, 87 + changeY));

        input2 = new ArrayList<PositionedStack>(input1);
        input2.add(new PositionedStack(new ItemStack(Blocks.chest), 90, -15 + changeY));
        input2.add(new PositionedStack(new ItemStack(Blocks.chest), 90 + 52, -15 + changeY));
        this.registerRocketBenchRecipe(input2, new PositionedStack(new ItemStack(AsteroidsItems.tier3Rocket, 1, 2), 139, 87 + changeY));

        input2 = new ArrayList<PositionedStack>(input1);
        input2.add(new PositionedStack(new ItemStack(Blocks.chest), 90, -15 + changeY));
        input2.add(new PositionedStack(new ItemStack(Blocks.chest), 90 + 26, -15 + changeY));
        input2.add(new PositionedStack(new ItemStack(Blocks.chest), 90 + 52, -15 + changeY));
        this.registerRocketBenchRecipe(input2, new PositionedStack(new ItemStack(AsteroidsItems.tier3Rocket, 1, 3), 139, 87 + changeY));
    }
}
