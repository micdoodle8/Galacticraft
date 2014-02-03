package micdoodle8.mods.galacticraft.core.nei;

import java.util.ArrayList;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.items.GCCoreItems;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

import codechicken.core.gui.GuiDraw;
import codechicken.nei.NEIServerUtils;
import codechicken.nei.PositionedStack;
import codechicken.nei.recipe.TemplateRecipeHandler;

/**
 * RefineryRecipeHandler.java
 * 
 * This file is part of the Galacticraft project
 * 
 * @author micdoodle8
 * @license Lesser GNU Public License v3 (http://www.gnu.org/licenses/lgpl.html)
 * 
 */
public class RefineryRecipeHandler extends TemplateRecipeHandler
{
	private static final ResourceLocation refineryGuiTexture = new ResourceLocation(GalacticraftCore.ASSET_DOMAIN, "textures/gui/refinery.png");
	int ticksPassed;

	public String getRecipeId()
	{
		return "galacticraft.refinery";
	}

	@Override
	public int recipiesPerPage()
	{
		return 2;
	}

	public Set<Entry<PositionedStack, PositionedStack>> getRecipes()
	{
		return NEIGalacticraftConfig.getRefineryRecipes();
	}

	@Override
	public void drawBackground(int i)
	{
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		GuiDraw.changeTexture(RefineryRecipeHandler.refineryGuiTexture);
		GuiDraw.drawTexturedModalRect(-2, 0, 3, 4, 168, 64);
		if (this.ticksPassed % 144 < 124 && this.ticksPassed % 144 > 10)
		{
			GuiDraw.drawTexturedModalRect(2, 42, 176, 6, 16, 20);
		}
		else if (this.ticksPassed % 144 < 134)
		{
			GuiDraw.drawTexturedModalRect(148, 42, 176 + 16, 6, 16, 20);
		}
		GuiDraw.drawTexturedModalRect(21, 21, 0, 186, this.ticksPassed % 144, 20);
	}

	@Override
	public void onUpdate()
	{
		this.ticksPassed += 2;
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
			for (final Map.Entry<PositionedStack, PositionedStack> irecipe : this.getRecipes())
			{
				this.arecipes.add(new CachedRefineryRecipe(irecipe));
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
		for (final Map.Entry<PositionedStack, PositionedStack> irecipe : this.getRecipes())
		{
			if (NEIServerUtils.areStacksSameTypeCrafting(irecipe.getValue().item, result))
			{
				this.arecipes.add(new CachedRefineryRecipe(irecipe));
			}
		}
	}

	@Override
	public void loadUsageRecipes(ItemStack ingredient)
	{
		for (final Map.Entry<PositionedStack, PositionedStack> irecipe : this.getRecipes())
		{
			if (NEIServerUtils.areStacksSameTypeCrafting(ingredient, irecipe.getKey().item))
			{
				this.arecipes.add(new CachedRefineryRecipe(irecipe));
				break;
			}
		}
	}

	@Override
	public ArrayList<PositionedStack> getIngredientStacks(int recipe)
	{
		if (this.ticksPassed % 144 > 10)
		{
			ArrayList<PositionedStack> stacks = new ArrayList<PositionedStack>();
			stacks.add(new PositionedStack(new ItemStack(GCCoreItems.oilCanister, 1, GCCoreItems.oilCanister.getMaxDamage()), this.arecipes.get(recipe).getIngredients().get(0).relx, this.arecipes.get(recipe).getIngredients().get(0).rely));
			return stacks;
		}
		else
		{
			return (ArrayList<PositionedStack>) this.arecipes.get(recipe).getIngredients();
		}
	}

	@Override
	public PositionedStack getResultStack(int recipe)
	{
		if (this.ticksPassed % 144 < 134)
		{
			return new PositionedStack(new ItemStack(GCCoreItems.oilCanister, 1, GCCoreItems.oilCanister.getMaxDamage()), this.arecipes.get(recipe).getResult().relx, this.arecipes.get(recipe).getResult().rely);
		}
		else
		{
			return this.arecipes.get(recipe).getResult();
		}
	}

	public class CachedRefineryRecipe extends TemplateRecipeHandler.CachedRecipe
	{
		public PositionedStack input;
		public PositionedStack output;

		@Override
		public PositionedStack getIngredient()
		{
			return this.input;
		}

		@Override
		public PositionedStack getResult()
		{
			return this.output;
		}

		public CachedRefineryRecipe(PositionedStack pstack1, PositionedStack pstack2)
		{
			super();
			this.input = pstack1;
			this.output = pstack2;
		}

		public CachedRefineryRecipe(Map.Entry<PositionedStack, PositionedStack> recipe)
		{
			this(recipe.getKey(), recipe.getValue());
		}
	}

	@Override
	public String getRecipeName()
	{
		return "Refinery";
	}

	@Override
	public String getGuiTexture()
	{
		return "/mods/galacticraftcore/textures/gui/refinery.png";
	}
}
