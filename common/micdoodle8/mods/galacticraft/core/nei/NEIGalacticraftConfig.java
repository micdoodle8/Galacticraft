package micdoodle8.mods.galacticraft.core.nei;

import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Set;

import micdoodle8.mods.galacticraft.api.recipe.CompressorRecipes;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.blocks.GCCoreBlocks;
import micdoodle8.mods.galacticraft.core.items.GCCoreItems;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.ShapedRecipes;
import net.minecraft.item.crafting.ShapelessRecipes;
import codechicken.nei.PositionedStack;
import codechicken.nei.api.API;
import codechicken.nei.api.IConfigureNEI;

/**
 * NEIGalacticraftConfig.java
 * 
 * This file is part of the Galacticraft project
 * 
 * @author micdoodle8
 * @license Lesser GNU Public License v3 (http://www.gnu.org/licenses/lgpl.html)
 * 
 */
public class NEIGalacticraftConfig implements IConfigureNEI
{
	private static HashMap<HashMap<Integer, PositionedStack>, PositionedStack> rocketBenchRecipes = new HashMap<HashMap<Integer, PositionedStack>, PositionedStack>();
	private static HashMap<HashMap<Integer, PositionedStack>, PositionedStack> buggyBenchRecipes = new HashMap<HashMap<Integer, PositionedStack>, PositionedStack>();
	private static HashMap<PositionedStack, PositionedStack> refineryRecipes = new HashMap<PositionedStack, PositionedStack>();
	private static HashMap<HashMap<Integer, PositionedStack>, PositionedStack> circuitFabricatorRecipes = new HashMap<HashMap<Integer, PositionedStack>, PositionedStack>();
	private static HashMap<HashMap<Integer, PositionedStack>, PositionedStack> ingotCompressorRecipes = new HashMap<HashMap<Integer, PositionedStack>, PositionedStack>();

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
		API.registerRecipeHandler(new CircuitFabricatorRecipeHandler());
		API.registerUsageHandler(new CircuitFabricatorRecipeHandler());
		API.registerRecipeHandler(new IngotCompressorRecipeHandler());
		API.registerUsageHandler(new IngotCompressorRecipeHandler());
		API.registerRecipeHandler(new ElectricIngotCompressorRecipeHandler());
		API.registerUsageHandler(new ElectricIngotCompressorRecipeHandler());
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

	public void registerIngotCompressorRecipe(HashMap<Integer, PositionedStack> input, PositionedStack output)
	{
		NEIGalacticraftConfig.ingotCompressorRecipes.put(input, output);
	}

	public void registerCircuitFabricatorRecipe(HashMap<Integer, PositionedStack> input, PositionedStack output)
	{
		NEIGalacticraftConfig.circuitFabricatorRecipes.put(input, output);
	}

	public void registerRocketBenchRecipe(HashMap<Integer, PositionedStack> input, PositionedStack output)
	{
		NEIGalacticraftConfig.rocketBenchRecipes.put(input, output);
	}

	public void registerBuggyBenchRecipe(HashMap<Integer, PositionedStack> input, PositionedStack output)
	{
		NEIGalacticraftConfig.buggyBenchRecipes.put(input, output);
	}

	public void registerRefineryRecipe(PositionedStack input, PositionedStack output)
	{
		NEIGalacticraftConfig.refineryRecipes.put(input, output);
	}

	public static Set<Entry<HashMap<Integer, PositionedStack>, PositionedStack>> getIngotCompressorRecipes()
	{
		return NEIGalacticraftConfig.ingotCompressorRecipes.entrySet();
	}

	public static Set<Entry<HashMap<Integer, PositionedStack>, PositionedStack>> getCircuitFabricatorRecipes()
	{
		return NEIGalacticraftConfig.circuitFabricatorRecipes.entrySet();
	}

	public static Set<Entry<HashMap<Integer, PositionedStack>, PositionedStack>> getRocketBenchRecipes()
	{
		return NEIGalacticraftConfig.rocketBenchRecipes.entrySet();
	}

	public static Set<Entry<HashMap<Integer, PositionedStack>, PositionedStack>> getBuggyBenchRecipes()
	{
		return NEIGalacticraftConfig.buggyBenchRecipes.entrySet();
	}

	public static Set<Entry<PositionedStack, PositionedStack>> getRefineryRecipes()
	{
		return NEIGalacticraftConfig.refineryRecipes.entrySet();
	}

	public void registerRecipes()
	{
		this.registerRefineryRecipe(new PositionedStack(new ItemStack(GCCoreItems.oilCanister, 1, 1), 2, 3), new PositionedStack(new ItemStack(GCCoreItems.fuelCanister, 1, 1), 148, 3));

		this.addRocketRecipes();
		this.addBuggyRecipes();
		this.addCircuitFabricatorRecipes();
		this.addIngotCompressorRecipes();
	}

	private void addBuggyRecipes()
	{
		HashMap<Integer, PositionedStack> input1 = new HashMap<Integer, PositionedStack>();

		input1 = new HashMap<Integer, PositionedStack>();
		input1.put(0, new PositionedStack(new ItemStack(GCCoreItems.partBuggy, 1, 0), 18, 37));
		input1.put(1, new PositionedStack(new ItemStack(GCCoreItems.partBuggy, 1, 0), 18, 91));
		input1.put(2, new PositionedStack(new ItemStack(GCCoreItems.partBuggy, 1, 0), 90, 37));
		input1.put(3, new PositionedStack(new ItemStack(GCCoreItems.partBuggy, 1, 0), 90, 91));
		for (int x = 0; x < 3; x++)
		{
			for (int y = 0; y < 4; y++)
			{
				if (x == 1 && y == 1)
				{
					input1.put(y * 3 + x + 4, new PositionedStack(new ItemStack(GCCoreItems.partBuggy, 1, 1), 36 + x * 18, 37 + y * 18));
				}
				else
				{
					input1.put(y * 3 + x + 4, new PositionedStack(new ItemStack(GCCoreItems.heavyPlatingTier1), 36 + x * 18, 37 + y * 18));
				}
			}
		}
		this.registerBuggyBenchRecipe(input1, new PositionedStack(new ItemStack(GCCoreItems.buggy, 1, 0), 139, 101));

		HashMap<Integer, PositionedStack> input2 = new HashMap<Integer, PositionedStack>(input1);
		input2.put(16, new PositionedStack(new ItemStack(GCCoreItems.partBuggy, 1, 2), 90, 8));
		this.registerBuggyBenchRecipe(input2, new PositionedStack(new ItemStack(GCCoreItems.buggy, 1, 1), 139, 101));

		input2 = new HashMap<Integer, PositionedStack>(input1);
		input2.put(17, new PositionedStack(new ItemStack(GCCoreItems.partBuggy, 1, 2), 116, 8));
		this.registerBuggyBenchRecipe(input2, new PositionedStack(new ItemStack(GCCoreItems.buggy, 1, 1), 139, 101));

		input2 = new HashMap<Integer, PositionedStack>(input1);
		input2.put(18, new PositionedStack(new ItemStack(GCCoreItems.partBuggy, 1, 2), 142, 8));
		this.registerBuggyBenchRecipe(input2, new PositionedStack(new ItemStack(GCCoreItems.buggy, 1, 1), 139, 101));

		input2 = new HashMap<Integer, PositionedStack>(input1);
		input2.put(16, new PositionedStack(new ItemStack(GCCoreItems.partBuggy, 1, 2), 90, 8));
		input2.put(17, new PositionedStack(new ItemStack(GCCoreItems.partBuggy, 1, 2), 116, 8));
		this.registerBuggyBenchRecipe(input2, new PositionedStack(new ItemStack(GCCoreItems.buggy, 1, 2), 139, 101));

		input2 = new HashMap<Integer, PositionedStack>(input1);
		input2.put(17, new PositionedStack(new ItemStack(GCCoreItems.partBuggy, 1, 2), 116, 8));
		input2.put(18, new PositionedStack(new ItemStack(GCCoreItems.partBuggy, 1, 2), 142, 8));
		this.registerBuggyBenchRecipe(input2, new PositionedStack(new ItemStack(GCCoreItems.buggy, 1, 2), 139, 101));

		input2 = new HashMap<Integer, PositionedStack>(input1);
		input2.put(16, new PositionedStack(new ItemStack(GCCoreItems.partBuggy, 1, 2), 90, 8));
		input2.put(18, new PositionedStack(new ItemStack(GCCoreItems.partBuggy, 1, 2), 142, 8));
		this.registerBuggyBenchRecipe(input2, new PositionedStack(new ItemStack(GCCoreItems.buggy, 1, 2), 139, 101));

		input2 = new HashMap<Integer, PositionedStack>(input1);
		input2.put(16, new PositionedStack(new ItemStack(GCCoreItems.partBuggy, 1, 2), 90, 8));
		input2.put(17, new PositionedStack(new ItemStack(GCCoreItems.partBuggy, 1, 2), 116, 8));
		input2.put(18, new PositionedStack(new ItemStack(GCCoreItems.partBuggy, 1, 2), 142, 8));
		this.registerBuggyBenchRecipe(input2, new PositionedStack(new ItemStack(GCCoreItems.buggy, 1, 3), 139, 101));
	}

	private void addRocketRecipes()
	{
		HashMap<Integer, PositionedStack> input1 = new HashMap<Integer, PositionedStack>();
		input1.put(0, new PositionedStack(new ItemStack(GCCoreItems.partNoseCone), 45, 15));
		input1.put(1, new PositionedStack(new ItemStack(GCCoreItems.heavyPlatingTier1), 36, 33));
		input1.put(2, new PositionedStack(new ItemStack(GCCoreItems.heavyPlatingTier1), 36, 51));
		input1.put(3, new PositionedStack(new ItemStack(GCCoreItems.heavyPlatingTier1), 36, 69));
		input1.put(4, new PositionedStack(new ItemStack(GCCoreItems.heavyPlatingTier1), 36, 87));
		input1.put(5, new PositionedStack(new ItemStack(GCCoreItems.heavyPlatingTier1), 54, 33));
		input1.put(6, new PositionedStack(new ItemStack(GCCoreItems.heavyPlatingTier1), 54, 51));
		input1.put(7, new PositionedStack(new ItemStack(GCCoreItems.heavyPlatingTier1), 54, 69));
		input1.put(8, new PositionedStack(new ItemStack(GCCoreItems.heavyPlatingTier1), 54, 87));
		input1.put(9, new PositionedStack(new ItemStack(GCCoreItems.rocketEngine), 45, 105));
		input1.put(10, new PositionedStack(new ItemStack(GCCoreItems.partFins), 18, 87));
		input1.put(11, new PositionedStack(new ItemStack(GCCoreItems.partFins), 18, 105));
		input1.put(12, new PositionedStack(new ItemStack(GCCoreItems.partFins), 72, 87));
		input1.put(13, new PositionedStack(new ItemStack(GCCoreItems.partFins), 72, 105));
		this.registerRocketBenchRecipe(input1, new PositionedStack(new ItemStack(GCCoreItems.rocketTier1, 1, 0), 139, 92));

		HashMap<Integer, PositionedStack> input2 = new HashMap<Integer, PositionedStack>(input1);
		input2.put(14, new PositionedStack(new ItemStack(Block.chest), 90, 8));
		this.registerRocketBenchRecipe(input2, new PositionedStack(new ItemStack(GCCoreItems.rocketTier1, 1, 1), 139, 92));

		input2 = new HashMap<Integer, PositionedStack>(input1);
		input2.put(15, new PositionedStack(new ItemStack(Block.chest), 116, 8));
		this.registerRocketBenchRecipe(input2, new PositionedStack(new ItemStack(GCCoreItems.rocketTier1, 1, 1), 139, 92));

		input2 = new HashMap<Integer, PositionedStack>(input1);
		input2.put(16, new PositionedStack(new ItemStack(Block.chest), 142, 8));
		this.registerRocketBenchRecipe(input2, new PositionedStack(new ItemStack(GCCoreItems.rocketTier1, 1, 1), 139, 92));

		input2 = new HashMap<Integer, PositionedStack>(input1);
		input2.put(14, new PositionedStack(new ItemStack(Block.chest), 90, 8));
		input2.put(15, new PositionedStack(new ItemStack(Block.chest), 116, 8));
		this.registerRocketBenchRecipe(input2, new PositionedStack(new ItemStack(GCCoreItems.rocketTier1, 1, 2), 139, 92));

		input2 = new HashMap<Integer, PositionedStack>(input1);
		input2.put(15, new PositionedStack(new ItemStack(Block.chest), 116, 8));
		input2.put(16, new PositionedStack(new ItemStack(Block.chest), 142, 8));
		this.registerRocketBenchRecipe(input2, new PositionedStack(new ItemStack(GCCoreItems.rocketTier1, 1, 2), 139, 92));

		input2 = new HashMap<Integer, PositionedStack>(input1);
		input2.put(14, new PositionedStack(new ItemStack(Block.chest), 90, 8));
		input2.put(16, new PositionedStack(new ItemStack(Block.chest), 142, 8));
		this.registerRocketBenchRecipe(input2, new PositionedStack(new ItemStack(GCCoreItems.rocketTier1, 1, 2), 139, 92));

		input2 = new HashMap<Integer, PositionedStack>(input1);
		input2.put(14, new PositionedStack(new ItemStack(Block.chest), 90, 8));
		input2.put(15, new PositionedStack(new ItemStack(Block.chest), 116, 8));
		input2.put(16, new PositionedStack(new ItemStack(Block.chest), 142, 8));
		this.registerRocketBenchRecipe(input2, new PositionedStack(new ItemStack(GCCoreItems.rocketTier1, 1, 3), 139, 92));
	}

	private void addCircuitFabricatorRecipes()
	{
		HashMap<Integer, PositionedStack> input1 = new HashMap<Integer, PositionedStack>();
		input1.put(0, new PositionedStack(new ItemStack(Item.diamond), 10, 22));
		input1.put(1, new PositionedStack(new ItemStack(GCCoreItems.basicItem, 1, 2), 69, 51));
		input1.put(2, new PositionedStack(new ItemStack(GCCoreItems.basicItem, 1, 2), 69, 69));
		input1.put(3, new PositionedStack(new ItemStack(Item.redstone), 117, 51));
		input1.put(4, new PositionedStack(new ItemStack(Block.torchRedstoneActive), 140, 25));
		this.registerCircuitFabricatorRecipe(input1, new PositionedStack(new ItemStack(GCCoreItems.basicItem, 3, 13), 147, 91));

		HashMap<Integer, PositionedStack> input2 = new HashMap<Integer, PositionedStack>(input1);
		input2.put(4, new PositionedStack(new ItemStack(Item.dyePowder, 1, 4), 140, 25));
		this.registerCircuitFabricatorRecipe(input2, new PositionedStack(new ItemStack(GCCoreItems.basicItem, 9, 12), 147, 91));

		input2 = new HashMap<Integer, PositionedStack>(input1);
		input2.put(4, new PositionedStack(new ItemStack(Item.redstoneRepeater), 140, 25));
		this.registerCircuitFabricatorRecipe(input2, new PositionedStack(new ItemStack(GCCoreItems.basicItem, 1, 14), 147, 91));
	}

	private void addIngotCompressorRecipes()
	{
		for (int i = 0; i < CompressorRecipes.getRecipeList().size(); i++)
		{
			HashMap<Integer, PositionedStack> input1 = new HashMap<Integer, PositionedStack>();
			IRecipe rec = CompressorRecipes.getRecipeList().get(i);

			if (rec instanceof ShapedRecipes)
			{
				ShapedRecipes recipe = (ShapedRecipes) rec;

				for (int j = 0; j < recipe.recipeItems.length; j++)
				{
					ItemStack stack = recipe.recipeItems[j];

					input1.put(j, new PositionedStack(stack, 21 + j % 3 * 18, 26 + j / 3 * 18));
				}
			}
			else if (rec instanceof ShapelessRecipes)
			{
				ShapelessRecipes recipe = (ShapelessRecipes) rec;

				for (int j = 0; j < recipe.recipeItems.size(); j++)
				{
					ItemStack stack = (ItemStack) recipe.recipeItems.get(j);

					input1.put(j, new PositionedStack(stack, 21 + j % 3 * 18, 26 + j / 3 * 18));
				}
			}

			this.registerIngotCompressorRecipe(input1, new PositionedStack(rec.getRecipeOutput(), 140, 46));
		}
	}
}
