package micdoodle8.mods.galacticraft.core.nei;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

import codechicken.core.gui.GuiDraw;
import codechicken.nei.NEIServerUtils;
import codechicken.nei.PositionedStack;
import codechicken.nei.recipe.TemplateRecipeHandler;

/**
 * ElectricIngotCompressorRecipeHandler.java
 * 
 * This file is part of the Galacticraft project
 * 
 * @author micdoodle8
 * @license Lesser GNU Public License v3 (http://www.gnu.org/licenses/lgpl.html)
 * 
 */
public class ElectricIngotCompressorRecipeHandler extends TemplateRecipeHandler
{
	private static final ResourceLocation ingotCompressorTexture = new ResourceLocation(GalacticraftCore.ASSET_DOMAIN, "textures/gui/electric_IngotCompressor.png");
	public static int ticksPassed;

	public String getRecipeId()
	{
		return "galacticraft.electricingotcompressor";
	}

	@Override
	public int recipiesPerPage()
	{
		return 1;
	}

	public Set<Entry<ArrayList<PositionedStack>, PositionedStack>> getRecipes()
	{
		HashMap<ArrayList<PositionedStack>, PositionedStack> recipes = new HashMap<ArrayList<PositionedStack>, PositionedStack>();

		for (Entry<HashMap<Integer, PositionedStack>, PositionedStack> stack : NEIGalacticraftConfig.getIngotCompressorRecipes())
		{
			ArrayList<PositionedStack> inputStacks = new ArrayList<PositionedStack>();

			for (Map.Entry<Integer, PositionedStack> input : stack.getKey().entrySet())
			{
				inputStacks.add(input.getValue());
			}

			recipes.put(inputStacks, stack.getValue());
		}

		return recipes.entrySet();
	}

	@Override
	public void drawBackground(int i)
	{
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		GuiDraw.changeTexture(ElectricIngotCompressorRecipeHandler.ingotCompressorTexture);
		GuiDraw.drawTexturedModalRect(20, 25, 18, 17, 137, 54);

		if (ElectricIngotCompressorRecipeHandler.ticksPassed % 70 > 26)
		{
			GuiDraw.drawTexturedModalRect(103, 38, 176, 0, 17, 13);
		}

		GuiDraw.drawTexturedModalRect(79, 46, 176, 13, Math.min(ElectricIngotCompressorRecipeHandler.ticksPassed % 70, 53), 17);
	}

	@Override
	public void onUpdate()
	{
		ElectricIngotCompressorRecipeHandler.ticksPassed += 1;
		super.onUpdate();
	}

	@Override
	public void loadTransferRects()
	{
	}

	@Override
	public void loadCraftingRecipes(String outputId, Object... results)
	{
		if (outputId.equals(this.getRecipeId()))
		{
			for (final Map.Entry<ArrayList<PositionedStack>, PositionedStack> irecipe : this.getRecipes())
			{
				this.arecipes.add(new ElectricCompressorRecipe(irecipe));
			}
		}
		else
		{
			super.loadCraftingRecipes(outputId, results);
		}
	}

	@Override
	public void loadCraftingRecipes(ItemStack result)
	{
		for (final Map.Entry<ArrayList<PositionedStack>, PositionedStack> irecipe : this.getRecipes())
		{
			if (NEIServerUtils.areStacksSameTypeCrafting(irecipe.getValue().item, result))
			{
				this.arecipes.add(new ElectricCompressorRecipe(irecipe));
			}
		}
	}

	@Override
	public void loadUsageRecipes(ItemStack ingredient)
	{
		for (final Map.Entry<ArrayList<PositionedStack>, PositionedStack> irecipe : this.getRecipes())
		{
			for (final PositionedStack pstack : irecipe.getKey())
			{
				if (NEIServerUtils.areStacksSameTypeCrafting(ingredient, pstack.item))
				{
					this.arecipes.add(new ElectricCompressorRecipe(irecipe));
					break;
				}
			}
		}
	}

	@Override
	public ArrayList<PositionedStack> getIngredientStacks(int recipe)
	{
		return (ArrayList<PositionedStack>) this.arecipes.get(recipe).getIngredients();
	}

	@Override
	public PositionedStack getResultStack(int recipe)
	{
		if (ElectricIngotCompressorRecipeHandler.ticksPassed % 70 >= 53)
		{
			return this.arecipes.get(recipe).getResult();
		}

		return null;
	}

	public class ElectricCompressorRecipe extends TemplateRecipeHandler.CachedRecipe
	{
		public ArrayList<PositionedStack> input;
		public PositionedStack output;

		@Override
		public ArrayList<PositionedStack> getIngredients()
		{
			return this.input;
		}

		@Override
		public PositionedStack getResult()
		{
			return this.output;
		}

		public ElectricCompressorRecipe(ArrayList<PositionedStack> pstack1, PositionedStack pstack2)
		{
			super();

			ArrayList<PositionedStack> ingred = new ArrayList<PositionedStack>();

			for (PositionedStack stack : pstack1)
			{
				PositionedStack stack2 = stack.copy();
				stack2.item.stackSize *= 2;
				ingred.add(stack2);
			}

			this.input = ingred;
			pstack2.rely -= 8;
			this.output = pstack2;
		}

		public ElectricCompressorRecipe(Map.Entry<ArrayList<PositionedStack>, PositionedStack> recipe)
		{
			this(new ArrayList<PositionedStack>(recipe.getKey()), recipe.getValue().copy());
		}

		@Override
		public PositionedStack getOtherStack()
		{
			if (ElectricIngotCompressorRecipeHandler.ticksPassed % 70 >= 53)
			{
				PositionedStack outputCopy = this.output.copy();
				outputCopy.rely += 18;
				return outputCopy;
			}

			return null;
		}
	}

	@Override
	public String getRecipeName()
	{
		return "El. Ingot Compressor";
	}

	@Override
	public String getGuiTexture()
	{
		return "/assets/galacticraftcore/textures/gui/electric_IngotCompressor.png";
	}
}
