package micdoodle8.mods.galacticraft.planets.mars.recipe;

import micdoodle8.mods.galacticraft.core.GCItems;
import micdoodle8.mods.galacticraft.planets.mars.blocks.MarsBlocks;
import micdoodle8.mods.galacticraft.planets.mars.items.MarsItems;
import micdoodle8.mods.galacticraft.planets.mars.util.MarsUtil;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraftforge.oredict.OreDictionary;

import java.util.HashMap;

public class RecipeManagerMars
{
	public static void loadCompatibilityRecipes()
    {
    }

    public static void addUniversalRecipes()
    {
        OreDictionary.registerOre("ingotDesh", new ItemStack(MarsItems.marsItemBasic, 1, 2));
        OreDictionary.registerOre("compressedDesh", new ItemStack(MarsItems.marsItemBasic, 1, 5));

        // Smelting
        FurnaceRecipes.instance().addSmeltingRecipe(new ItemStack(MarsItems.marsItemBasic, 1, 0), new ItemStack(MarsItems.marsItemBasic, 1, 2), 0.2F);
        FurnaceRecipes.instance().addSmeltingRecipe(new ItemStack(MarsBlocks.marsBlock, 1, 4), new ItemStack(MarsBlocks.marsBlock, 1, 9), 0.0F);
        FurnaceRecipes.instance().addSmeltingRecipe(new ItemStack(MarsBlocks.marsBlock, 1, 0), new ItemStack(GCItems.basicItem, 1, 3), 1.0F);
        FurnaceRecipes.instance().addSmeltingRecipe(new ItemStack(MarsBlocks.marsBlock, 1, 1), new ItemStack(GCItems.basicItem, 1, 4), 1.0F);
        FurnaceRecipes.instance().addSmeltingRecipe(new ItemStack(MarsBlocks.marsBlock, 1, 2), new ItemStack(MarsItems.marsItemBasic, 1, 2), 0.2F);
        FurnaceRecipes.instance().addSmeltingRecipe(new ItemStack(MarsBlocks.marsBlock, 1, 3), new ItemStack(Items.IRON_INGOT), 0.2F);

        // Schematic
        HashMap<Integer, ItemStack> input = new HashMap<Integer, ItemStack>();
        input.put(1, new ItemStack(GCItems.partNoseCone));
        input.put(2, new ItemStack(MarsItems.marsItemBasic, 1, 3));
        input.put(3, new ItemStack(MarsItems.marsItemBasic, 1, 3));
        input.put(4, new ItemStack(MarsItems.marsItemBasic, 1, 3));
        input.put(5, new ItemStack(MarsItems.marsItemBasic, 1, 3));
        input.put(6, new ItemStack(MarsItems.marsItemBasic, 1, 3));
        input.put(7, new ItemStack(MarsItems.marsItemBasic, 1, 3));
        input.put(8, new ItemStack(MarsItems.marsItemBasic, 1, 3));
        input.put(9, new ItemStack(MarsItems.marsItemBasic, 1, 3));
        input.put(10, new ItemStack(MarsItems.marsItemBasic, 1, 3));
        input.put(11, new ItemStack(MarsItems.marsItemBasic, 1, 3));
        input.put(12, new ItemStack(GCItems.rocketEngine, 1, 1));
        input.put(13, new ItemStack(GCItems.partFins));
        input.put(14, new ItemStack(GCItems.partFins));
        input.put(15, new ItemStack(GCItems.rocketEngine));
        input.put(16, new ItemStack(GCItems.rocketEngine, 1, 1));
        input.put(17, new ItemStack(GCItems.partFins));
        input.put(18, new ItemStack(GCItems.partFins));
        input.put(19, ItemStack.EMPTY);
        input.put(20, ItemStack.EMPTY);
        input.put(21, ItemStack.EMPTY);
        MarsUtil.addRocketBenchT2Recipe(new ItemStack(MarsItems.rocketMars, 1, 0), input);

        HashMap<Integer, ItemStack> input2 = new HashMap<Integer, ItemStack>(input);
        input2.put(19, new ItemStack(Blocks.CHEST));
        input2.put(20, ItemStack.EMPTY);
        input2.put(21, ItemStack.EMPTY);
        MarsUtil.addRocketBenchT2Recipe(new ItemStack(MarsItems.rocketMars, 1, 1), input2);

        input2 = new HashMap<Integer, ItemStack>(input);
        input2.put(19, ItemStack.EMPTY);
        input2.put(20, new ItemStack(Blocks.CHEST));
        input2.put(21, ItemStack.EMPTY);
        MarsUtil.addRocketBenchT2Recipe(new ItemStack(MarsItems.rocketMars, 1, 1), input2);

        input2 = new HashMap<Integer, ItemStack>(input);
        input2.put(19, ItemStack.EMPTY);
        input2.put(20, ItemStack.EMPTY);
        input2.put(21, new ItemStack(Blocks.CHEST));
        MarsUtil.addRocketBenchT2Recipe(new ItemStack(MarsItems.rocketMars, 1, 1), input2);

        input2 = new HashMap<Integer, ItemStack>(input);
        input2.put(19, new ItemStack(Blocks.CHEST));
        input2.put(20, new ItemStack(Blocks.CHEST));
        input2.put(21, ItemStack.EMPTY);
        MarsUtil.addRocketBenchT2Recipe(new ItemStack(MarsItems.rocketMars, 1, 2), input2);

        input2 = new HashMap<Integer, ItemStack>(input);
        input2.put(19, new ItemStack(Blocks.CHEST));
        input2.put(20, ItemStack.EMPTY);
        input2.put(21, new ItemStack(Blocks.CHEST));
        MarsUtil.addRocketBenchT2Recipe(new ItemStack(MarsItems.rocketMars, 1, 2), input2);

        input2 = new HashMap<Integer, ItemStack>(input);
        input2.put(19, ItemStack.EMPTY);
        input2.put(20, new ItemStack(Blocks.CHEST));
        input2.put(21, new ItemStack(Blocks.CHEST));
        MarsUtil.addRocketBenchT2Recipe(new ItemStack(MarsItems.rocketMars, 1, 2), input2);

        input2 = new HashMap<Integer, ItemStack>(input);
        input2.put(19, new ItemStack(Blocks.CHEST));
        input2.put(20, new ItemStack(Blocks.CHEST));
        input2.put(21, new ItemStack(Blocks.CHEST));
        MarsUtil.addRocketBenchT2Recipe(new ItemStack(MarsItems.rocketMars, 1, 3), input2);

        //

        input = new HashMap<Integer, ItemStack>();
        input.put(1, new ItemStack(GCItems.partNoseCone));
        input.put(2, new ItemStack(GCItems.basicItem, 1, 14));
        input.put(3, new ItemStack(MarsItems.marsItemBasic, 1, 3));
        input.put(4, new ItemStack(MarsItems.marsItemBasic, 1, 3));
        input.put(5, new ItemStack(MarsItems.marsItemBasic, 1, 3));
        input.put(6, new ItemStack(MarsItems.marsItemBasic, 1, 3));
        input.put(7, new ItemStack(MarsItems.marsItemBasic, 1, 3));
        input.put(8, new ItemStack(MarsItems.marsItemBasic, 1, 3));
        input.put(9, new ItemStack(GCItems.partFins));
        input.put(10, new ItemStack(GCItems.partFins));
        input.put(11, new ItemStack(GCItems.rocketEngine));
        input.put(12, new ItemStack(GCItems.partFins));
        input.put(13, new ItemStack(GCItems.partFins));

        input2 = new HashMap<Integer, ItemStack>(input);
        input2.put(14, new ItemStack(Blocks.CHEST));
        input2.put(15, ItemStack.EMPTY);
        input2.put(16, ItemStack.EMPTY);
        MarsUtil.adCargoRocketRecipe(new ItemStack(MarsItems.rocketMars, 1, 11), input2);

        input2 = new HashMap<Integer, ItemStack>(input);
        input2.put(14, ItemStack.EMPTY);
        input2.put(15, new ItemStack(Blocks.CHEST));
        input2.put(16, ItemStack.EMPTY);
        MarsUtil.adCargoRocketRecipe(new ItemStack(MarsItems.rocketMars, 1, 11), input2);

        input2 = new HashMap<Integer, ItemStack>(input);
        input2.put(14, ItemStack.EMPTY);
        input2.put(15, ItemStack.EMPTY);
        input2.put(16, new ItemStack(Blocks.CHEST));
        MarsUtil.adCargoRocketRecipe(new ItemStack(MarsItems.rocketMars, 1, 11), input2);

        input2 = new HashMap<Integer, ItemStack>(input);
        input2.put(14, new ItemStack(Blocks.CHEST));
        input2.put(15, new ItemStack(Blocks.CHEST));
        input2.put(16, ItemStack.EMPTY);
        MarsUtil.adCargoRocketRecipe(new ItemStack(MarsItems.rocketMars, 1, 12), input2);

        input2 = new HashMap<Integer, ItemStack>(input);
        input2.put(14, new ItemStack(Blocks.CHEST));
        input2.put(15, ItemStack.EMPTY);
        input2.put(16, new ItemStack(Blocks.CHEST));
        MarsUtil.adCargoRocketRecipe(new ItemStack(MarsItems.rocketMars, 1, 12), input2);

        input2 = new HashMap<Integer, ItemStack>(input);
        input2.put(14, ItemStack.EMPTY);
        input2.put(15, new ItemStack(Blocks.CHEST));
        input2.put(16, new ItemStack(Blocks.CHEST));
        MarsUtil.adCargoRocketRecipe(new ItemStack(MarsItems.rocketMars, 1, 12), input2);

        input2 = new HashMap<Integer, ItemStack>(input);
        input2.put(14, new ItemStack(Blocks.CHEST));
        input2.put(15, new ItemStack(Blocks.CHEST));
        input2.put(16, new ItemStack(Blocks.CHEST));
        MarsUtil.adCargoRocketRecipe(new ItemStack(MarsItems.rocketMars, 1, 13), input2);
    }
}
