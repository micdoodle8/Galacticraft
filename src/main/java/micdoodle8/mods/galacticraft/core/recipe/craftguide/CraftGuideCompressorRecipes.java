package micdoodle8.mods.galacticraft.core.recipe.craftguide;

import micdoodle8.mods.galacticraft.api.recipe.CompressorRecipes;
import micdoodle8.mods.galacticraft.core.blocks.BlockMachine;
import micdoodle8.mods.galacticraft.core.blocks.GCBlocks;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.ShapedRecipes;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.ShapelessOreRecipe;
import uristqwerty.CraftGuide.api.*;

import java.util.ArrayList;

public class CraftGuideCompressorRecipes implements RecipeProvider
{
	private final Slot[] slots = new Slot[11];

	public CraftGuideCompressorRecipes()
	{
		for (int i = 0; i < 3; i++)
			for (int j = 0; j < 3; j++)
				slots[i + j * 3] = new ItemSlot(i * 18 + 3, j * 18 + 3, 16, 16).drawOwnBackground();
		slots[9] = new ItemSlot(59, 21, 16, 16, true).setSlotType(SlotType.OUTPUT_SLOT).drawOwnBackground();
		slots[10] = new ItemSlot(59, 3, 16, 16).setSlotType(SlotType.MACHINE_SLOT);
	}

	@Override
	public void generateRecipes(RecipeGenerator generator)
	{
		 ItemStack machine = new ItemStack(GCBlocks.machineBase, 1, BlockMachine.COMPRESSOR_METADATA);
		 RecipeTemplate template = generator.createRecipeTemplate(slots, machine);

		 for (int i = 0; i < CompressorRecipes.getRecipeList().size(); i++)
		 {
			 Object[] array = new Object[11];
			 IRecipe rec = CompressorRecipes.getRecipeList().get(i);

			 if (rec instanceof ShapedRecipes)
			 {
				 ShapedRecipes recipe = (ShapedRecipes) rec;

				 for (int j = 0; j < recipe.recipeItems.length; j++)
				 {
					 ItemStack stack = recipe.recipeItems[j];

					 array[j] = stack.copy();
				 }
				 array[9] = recipe.getRecipeOutput().copy();
			 }
			 else if (rec instanceof ShapelessOreRecipe)
			 {
				 ShapelessOreRecipe recipe = (ShapelessOreRecipe) rec;

				 for (int j = 0; j < recipe.getInput().size(); j++)
				 {
					 Object obj = recipe.getInput().get(j);

					 if (obj instanceof ItemStack) array[j] = ((ItemStack)obj).copy();
					 else if (obj instanceof String) array[j] = OreDictionary.getOres((String)obj).clone();
					 else if (obj instanceof ArrayList) array[j] = ((ArrayList)obj).clone();
				 }
				 array[9] = recipe.getRecipeOutput().copy();
			 }

			 array[10] = machine;
			 generator.addRecipe(template, array);
		 }
	}
}
