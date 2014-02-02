package micdoodle8.mods.galacticraft.mars.nei;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Set;

import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.items.GCCoreItems;
import micdoodle8.mods.galacticraft.mars.items.GCMarsItems;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import codechicken.nei.PositionedStack;
import codechicken.nei.api.API;
import codechicken.nei.api.IConfigureNEI;

/**
 * NEIGalacticraftMarsConfig.java
 * 
 * This file is part of the Galacticraft project
 * 
 * @author micdoodle8
 * @license Lesser GNU Public License v3 (http://www.gnu.org/licenses/lgpl.html)
 * 
 */
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

	public static Set<Entry<ArrayList<PositionedStack>, PositionedStack>> getRocketBenchRecipes()
	{
		return NEIGalacticraftMarsConfig.rocketBenchRecipes.entrySet();
	}

	public static Set<Entry<ArrayList<PositionedStack>, PositionedStack>> getCargoBenchRecipes()
	{
		return NEIGalacticraftMarsConfig.cargoBenchRecipes.entrySet();
	}

	public void registerRecipes()
	{
		final int changey = 15;

		ArrayList<PositionedStack> input1 = new ArrayList<PositionedStack>();

		input1.add(new PositionedStack(new ItemStack(GCCoreItems.partNoseCone), 45, -8 + changey));
		input1.add(new PositionedStack(new ItemStack(GCMarsItems.marsItemBasic, 1, 3), 36, -6 + 0 * 18 + 16 + changey));
		input1.add(new PositionedStack(new ItemStack(GCMarsItems.marsItemBasic, 1, 3), 36, -6 + 1 * 18 + 16 + changey));
		input1.add(new PositionedStack(new ItemStack(GCMarsItems.marsItemBasic, 1, 3), 36, -6 + 2 * 18 + 16 + changey));
		input1.add(new PositionedStack(new ItemStack(GCMarsItems.marsItemBasic, 1, 3), 36, -6 + 3 * 18 + 16 + changey));
		input1.add(new PositionedStack(new ItemStack(GCMarsItems.marsItemBasic, 1, 3), 36, -6 + 4 * 18 + 16 + changey));
		input1.add(new PositionedStack(new ItemStack(GCMarsItems.marsItemBasic, 1, 3), 54, -6 + 0 * 18 + 16 + changey));
		input1.add(new PositionedStack(new ItemStack(GCMarsItems.marsItemBasic, 1, 3), 54, -6 + 1 * 18 + 16 + changey));
		input1.add(new PositionedStack(new ItemStack(GCMarsItems.marsItemBasic, 1, 3), 54, -6 + 2 * 18 + 16 + changey));
		input1.add(new PositionedStack(new ItemStack(GCMarsItems.marsItemBasic, 1, 3), 54, -6 + 3 * 18 + 16 + changey));
		input1.add(new PositionedStack(new ItemStack(GCMarsItems.marsItemBasic, 1, 3), 54, -6 + 4 * 18 + 16 + changey));
		input1.add(new PositionedStack(new ItemStack(GCCoreItems.rocketEngine), 45, 100 + changey));
		input1.add(new PositionedStack(new ItemStack(GCCoreItems.rocketEngine, 1, 1), 18, 64 + changey));
		input1.add(new PositionedStack(new ItemStack(GCCoreItems.rocketEngine, 1, 1), 72, 64 + changey));
		input1.add(new PositionedStack(new ItemStack(GCCoreItems.partFins), 18, 82 + changey));
		input1.add(new PositionedStack(new ItemStack(GCCoreItems.partFins), 18, 100 + changey));
		input1.add(new PositionedStack(new ItemStack(GCCoreItems.partFins), 72, 82 + changey));
		input1.add(new PositionedStack(new ItemStack(GCCoreItems.partFins), 72, 100 + changey));
		this.registerRocketBenchRecipe(input1, new PositionedStack(new ItemStack(GCMarsItems.spaceship, 1, 0), 139, 87 + changey));

		ArrayList<PositionedStack> input2 = new ArrayList<PositionedStack>(input1);
		input2.add(new PositionedStack(new ItemStack(Block.chest), 90 + 0 * 26, -15 + changey));
		this.registerRocketBenchRecipe(input2, new PositionedStack(new ItemStack(GCMarsItems.spaceship, 1, 1), 139, 87 + changey));

		input2 = new ArrayList<PositionedStack>(input1);
		input2.add(new PositionedStack(new ItemStack(Block.chest), 90 + 1 * 26, -15 + changey));
		this.registerRocketBenchRecipe(input2, new PositionedStack(new ItemStack(GCMarsItems.spaceship, 1, 1), 139, 87 + changey));

		input2 = new ArrayList<PositionedStack>(input1);
		input2.add(new PositionedStack(new ItemStack(Block.chest), 90 + 2 * 26, -15 + changey));
		this.registerRocketBenchRecipe(input2, new PositionedStack(new ItemStack(GCMarsItems.spaceship, 1, 1), 139, 87 + changey));

		input2 = new ArrayList<PositionedStack>(input1);
		input2.add(new PositionedStack(new ItemStack(Block.chest), 90 + 0 * 26, -15 + changey));
		input2.add(new PositionedStack(new ItemStack(Block.chest), 90 + 1 * 26, -15 + changey));
		this.registerRocketBenchRecipe(input2, new PositionedStack(new ItemStack(GCMarsItems.spaceship, 1, 2), 139, 87 + changey));

		input2 = new ArrayList<PositionedStack>(input1);
		input2.add(new PositionedStack(new ItemStack(Block.chest), 90 + 1 * 26, -15 + changey));
		input2.add(new PositionedStack(new ItemStack(Block.chest), 90 + 2 * 26, -15 + changey));
		this.registerRocketBenchRecipe(input2, new PositionedStack(new ItemStack(GCMarsItems.spaceship, 1, 2), 139, 87 + changey));

		input2 = new ArrayList<PositionedStack>(input1);
		input2.add(new PositionedStack(new ItemStack(Block.chest), 90 + 0 * 26, -15 + changey));
		input2.add(new PositionedStack(new ItemStack(Block.chest), 90 + 2 * 26, -15 + changey));
		this.registerRocketBenchRecipe(input2, new PositionedStack(new ItemStack(GCMarsItems.spaceship, 1, 2), 139, 87 + changey));

		input2 = new ArrayList<PositionedStack>(input1);
		input2.add(new PositionedStack(new ItemStack(Block.chest), 90 + 0 * 26, -15 + changey));
		input2.add(new PositionedStack(new ItemStack(Block.chest), 90 + 1 * 26, -15 + changey));
		input2.add(new PositionedStack(new ItemStack(Block.chest), 90 + 2 * 26, -15 + changey));
		this.registerRocketBenchRecipe(input2, new PositionedStack(new ItemStack(GCMarsItems.spaceship, 1, 3), 139, 87 + changey));

		input1 = new ArrayList<PositionedStack>();
		input1.add(new PositionedStack(new ItemStack(GCCoreItems.partNoseCone), 45, 14));
		input1.add(new PositionedStack(new ItemStack(GCCoreItems.basicItem, 1, 14), 45, 32));
		input1.add(new PositionedStack(new ItemStack(GCMarsItems.marsItemBasic, 1, 3), 36, 32 + 18 * 1));
		input1.add(new PositionedStack(new ItemStack(GCMarsItems.marsItemBasic, 1, 3), 36, 32 + 18 * 2));
		input1.add(new PositionedStack(new ItemStack(GCMarsItems.marsItemBasic, 1, 3), 36, 32 + 18 * 3));
		input1.add(new PositionedStack(new ItemStack(GCMarsItems.marsItemBasic, 1, 3), 54, 32 + 18 * 1));
		input1.add(new PositionedStack(new ItemStack(GCMarsItems.marsItemBasic, 1, 3), 54, 32 + 18 * 2));
		input1.add(new PositionedStack(new ItemStack(GCMarsItems.marsItemBasic, 1, 3), 54, 32 + 18 * 3));
		input1.add(new PositionedStack(new ItemStack(GCCoreItems.partFins), 18, 86));
		input1.add(new PositionedStack(new ItemStack(GCCoreItems.partFins), 72, 86));
		input1.add(new PositionedStack(new ItemStack(GCCoreItems.rocketEngine), 45, 104));
		input1.add(new PositionedStack(new ItemStack(GCCoreItems.partFins), 18, 104));
		input1.add(new PositionedStack(new ItemStack(GCCoreItems.partFins), 72, 104));

		input2 = new ArrayList<PositionedStack>(input1);
		input2.add(new PositionedStack(new ItemStack(Block.chest), 90 + 0 * 26, -7 + changey));
		this.registerCargoBenchRecipe(input2, new PositionedStack(new ItemStack(GCMarsItems.spaceship, 1, 11), 139, 77 + changey));

		input2 = new ArrayList<PositionedStack>(input1);
		input2.add(new PositionedStack(new ItemStack(Block.chest), 90 + 1 * 26, -7 + changey));
		this.registerCargoBenchRecipe(input2, new PositionedStack(new ItemStack(GCMarsItems.spaceship, 1, 11), 139, 77 + changey));

		input2 = new ArrayList<PositionedStack>(input1);
		input2.add(new PositionedStack(new ItemStack(Block.chest), 90 + 2 * 26, -7 + changey));
		this.registerCargoBenchRecipe(input2, new PositionedStack(new ItemStack(GCMarsItems.spaceship, 1, 11), 139, 77 + changey));

		input2 = new ArrayList<PositionedStack>(input1);
		input2.add(new PositionedStack(new ItemStack(Block.chest), 90 + 0 * 26, -7 + changey));
		input2.add(new PositionedStack(new ItemStack(Block.chest), 90 + 1 * 26, -7 + changey));
		this.registerCargoBenchRecipe(input2, new PositionedStack(new ItemStack(GCMarsItems.spaceship, 1, 12), 139, 77 + changey));

		input2 = new ArrayList<PositionedStack>(input1);
		input2.add(new PositionedStack(new ItemStack(Block.chest), 90 + 0 * 26, -7 + changey));
		input2.add(new PositionedStack(new ItemStack(Block.chest), 90 + 2 * 26, -7 + changey));
		this.registerCargoBenchRecipe(input2, new PositionedStack(new ItemStack(GCMarsItems.spaceship, 1, 12), 139, 77 + changey));

		input2 = new ArrayList<PositionedStack>(input1);
		input2.add(new PositionedStack(new ItemStack(Block.chest), 90 + 1 * 26, -7 + changey));
		input2.add(new PositionedStack(new ItemStack(Block.chest), 90 + 2 * 26, -7 + changey));
		this.registerCargoBenchRecipe(input2, new PositionedStack(new ItemStack(GCMarsItems.spaceship, 1, 12), 139, 77 + changey));

		input2 = new ArrayList<PositionedStack>(input1);
		input2.add(new PositionedStack(new ItemStack(Block.chest), 90 + 0 * 26, -7 + changey));
		input2.add(new PositionedStack(new ItemStack(Block.chest), 90 + 1 * 26, -7 + changey));
		input2.add(new PositionedStack(new ItemStack(Block.chest), 90 + 2 * 26, -7 + changey));
		this.registerCargoBenchRecipe(input2, new PositionedStack(new ItemStack(GCMarsItems.spaceship, 1, 13), 139, 77 + changey));
	}
}
