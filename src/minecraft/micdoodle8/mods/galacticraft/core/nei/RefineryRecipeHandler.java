package micdoodle8.mods.galacticraft.core.nei;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import micdoodle8.mods.galacticraft.core.items.GCCoreItems;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;

import org.lwjgl.opengl.GL11;

import codechicken.nei.NEIServerUtils;
import codechicken.nei.PositionedStack;
import codechicken.nei.forge.GuiContainerManager;
import codechicken.nei.recipe.TemplateRecipeHandler;

public class RefineryRecipeHandler extends TemplateRecipeHandler
{
	int ticksPassed;

	public String getRecipeId()
	{
		return "galacticraft.refinery";
	}

    @Override
    public int recipiesPerPage()
    {
        return 1;
    }

	public Set<Entry<ArrayList<PositionedStack>, PositionedStack>> getRecipes()
	{
		final HashMap<ArrayList<PositionedStack>, PositionedStack> recipes = new HashMap<ArrayList<PositionedStack>, PositionedStack>();

        final int changex = -3;
        final int changey = 23;

		// Spaceship T1
		final ArrayList<PositionedStack> input1 = new ArrayList<PositionedStack>();
		input1.add(new PositionedStack(new ItemStack(GCCoreItems.rocketNoseCone), 48 + changex, -8 + changey));
		input1.add(new PositionedStack(new ItemStack(GCCoreItems.heavyPlating), 39 + changex, -6 + 0 * 18 + 16 + changey));
		input1.add(new PositionedStack(new ItemStack(GCCoreItems.heavyPlating), 39 + changex, -6 + 1 * 18 + 16 + changey));
		recipes.put(input1, new PositionedStack(new ItemStack(GCCoreItems.spaceship, 1, 0), 142 + changex, 69 + changey));

		// Spaceship T1 with 27 storage space
		final ArrayList<PositionedStack> input2 = new ArrayList<PositionedStack>();
		input2.add(new PositionedStack(new ItemStack(GCCoreItems.rocketNoseCone), 48 + changex, -8 + changey));
		input2.add(new PositionedStack(new ItemStack(GCCoreItems.heavyPlating), 39 + changex, -6 + 0 * 18 + 16 + changey));
		input2.add(new PositionedStack(new ItemStack(GCCoreItems.heavyPlating), 39 + changex, -6 + 1 * 18 + 16 + changey));
		input2.add(new PositionedStack(new ItemStack(GCCoreItems.heavyPlating), 39 + changex, -6 + 2 * 18 + 16 + changey));
		input2.add(new PositionedStack(new ItemStack(GCCoreItems.heavyPlating), 39 + changex, -6 + 3 * 18 + 16 + changey));
		input2.add(new PositionedStack(new ItemStack(GCCoreItems.heavyPlating), 57 + changex, -6 + 0 * 18 + 16 + changey));
		input2.add(new PositionedStack(new ItemStack(GCCoreItems.heavyPlating), 57 + changex, -6 + 1 * 18 + 16 + changey));
		input2.add(new PositionedStack(new ItemStack(GCCoreItems.heavyPlating), 57 + changex, -6 + 2 * 18 + 16 + changey));
		input2.add(new PositionedStack(new ItemStack(GCCoreItems.heavyPlating), 57 + changex, -6 + 3 * 18 + 16 + changey));
		input2.add(new PositionedStack(new ItemStack(GCCoreItems.rocketEngine), 48 + changex, 82 + changey));
		input2.add(new PositionedStack(new ItemStack(GCCoreItems.rocketFins), 21 + changex, 64 + changey));
		input2.add(new PositionedStack(new ItemStack(GCCoreItems.rocketFins), 21 + changex, 82 + changey));
		input2.add(new PositionedStack(new ItemStack(GCCoreItems.rocketFins), 75 + changex, 64 + changey));
		input2.add(new PositionedStack(new ItemStack(GCCoreItems.rocketFins), 75 + changex, 82 + changey));
		input2.add(new PositionedStack(new ItemStack(Block.chest), 93 + 0 * 26 + changex, -15 + changey));
		recipes.put(input2, new PositionedStack(new ItemStack(GCCoreItems.spaceship, 1, 1), 142 + changex, 69 + changey));

		return recipes.entrySet();
	}

	@Override
	public void drawBackground(GuiContainerManager guimanager, int i)
	{
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		guimanager.bindTexture(this.getGuiTexture());
		guimanager.drawTexturedModalRect(0, 0, 3, 4, 168, 130);
	}

	@Override
	public void onUpdate()
	{
		super.onUpdate();
	}

	@Override
	public void loadTransferRects()
	{
	}

	@Override
	public void loadCraftingRecipes(String outputId, Object... results)
	{
		if(outputId.equals(this.getRecipeId()))
		{
			for(final Map.Entry irecipe : this.getRecipes())
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
		for(final Map.Entry irecipe : this.getRecipes())
		{
			if(NEIServerUtils.areStacksSameTypeCrafting(((PositionedStack)irecipe.getValue()).item, result))
			{
				this.arecipes.add(new CachedRefineryRecipe(irecipe));
			}
		}
	}

	@Override
	public void loadUsageRecipes(ItemStack ingredient)
	{
		for(final Map.Entry irecipe : this.getRecipes())
		{
			if (NEIServerUtils.areStacksSameTypeCrafting(ingredient, ((PositionedStack) irecipe.getKey()).item))
			{
				this.arecipes.add(new CachedRefineryRecipe(irecipe));
				break;
			}
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

		public CachedRefineryRecipe(Map.Entry recipe)
		{
			this((PositionedStack)recipe.getKey(), (PositionedStack)recipe.getValue());
		}
	}

	@Override
	public String getRecipeName()
	{
		return "Rocket T1";
	}

	@Override
	public String getGuiTexture()
	{
		return "/micdoodle8/mods/galacticraft/core/client/gui/rocketbench.png";
	}
}
