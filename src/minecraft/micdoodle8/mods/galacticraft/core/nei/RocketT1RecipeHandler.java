package micdoodle8.mods.galacticraft.core.nei;

import java.awt.Rectangle;
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
import cpw.mods.fml.common.FMLLog;

public class RocketT1RecipeHandler extends TemplateRecipeHandler 
{
	int ticksPassed;

	public String getRecipeId()
	{
		return "galacticraft.rocketT1";
	}
    
    @Override
    public int recipiesPerPage()
    {
        return 1;
    }

	public Set<Entry<ArrayList<PositionedStack>, PositionedStack>> getRecipes()
	{
		HashMap<ArrayList<PositionedStack>, PositionedStack> recipes = new HashMap<ArrayList<PositionedStack>, PositionedStack>();

        int changex = -3;
        int changey = 23;

		// Spaceship T1
		ArrayList<PositionedStack> input1 = new ArrayList<PositionedStack>();
		input1.add(new PositionedStack(new ItemStack(GCCoreItems.rocketNoseCone), 48 + changex, -8 + changey));
		input1.add(new PositionedStack(new ItemStack(GCCoreItems.heavyPlating), 39 + changex, -6 + 0 * 18 + 16 + changey));
		input1.add(new PositionedStack(new ItemStack(GCCoreItems.heavyPlating), 39 + changex, -6 + 1 * 18 + 16 + changey));
		input1.add(new PositionedStack(new ItemStack(GCCoreItems.heavyPlating), 39 + changex, -6 + 2 * 18 + 16 + changey));
		input1.add(new PositionedStack(new ItemStack(GCCoreItems.heavyPlating), 39 + changex, -6 + 3 * 18 + 16 + changey));
		input1.add(new PositionedStack(new ItemStack(GCCoreItems.heavyPlating), 57 + changex, -6 + 0 * 18 + 16 + changey));
		input1.add(new PositionedStack(new ItemStack(GCCoreItems.heavyPlating), 57 + changex, -6 + 1 * 18 + 16 + changey));
		input1.add(new PositionedStack(new ItemStack(GCCoreItems.heavyPlating), 57 + changex, -6 + 2 * 18 + 16 + changey));
		input1.add(new PositionedStack(new ItemStack(GCCoreItems.heavyPlating), 57 + changex, -6 + 3 * 18 + 16 + changey));
		input1.add(new PositionedStack(new ItemStack(GCCoreItems.rocketEngine), 48 + changex, 82 + changey));
		input1.add(new PositionedStack(new ItemStack(GCCoreItems.rocketFins), 21 + changex, 64 + changey));
		input1.add(new PositionedStack(new ItemStack(GCCoreItems.rocketFins), 21 + changex, 82 + changey));
		input1.add(new PositionedStack(new ItemStack(GCCoreItems.rocketFins), 75 + changex, 64 + changey));
		input1.add(new PositionedStack(new ItemStack(GCCoreItems.rocketFins), 75 + changex, 82 + changey));
		recipes.put(input1, new PositionedStack(new ItemStack(GCCoreItems.spaceship, 1, 0), 142 + changex, 69 + changey));
		
		// Spaceship T1 with 27 storage space
		ArrayList<PositionedStack> input2 = new ArrayList<PositionedStack>();
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
		guimanager.bindTexture(getGuiTexture());
		guimanager.drawTexturedModalRect(0, 0, 3, 4, 168, 130);
	}

	@Override
	public void drawExtras(GuiContainerManager guimanager, int i)
	{
		float f = ticksPassed >= 20 ? (ticksPassed - 20) % 20 / 20.0F : 0.0F;
		drawProgressBar(guimanager, 63, 34, 176 + 26, 0, 24, 7, f, 0);
		f = ticksPassed <= 20 ? ticksPassed / 20.0F : 1.0F;
		drawProgressBar(guimanager, 149, 12, 176 + 26, 7, 4, 52, f, 3);
	}

	@Override
	public void onUpdate()
	{
		super.onUpdate();
		ticksPassed++;
	}

	@Override
	public void loadTransferRects()
	{
	}

	@Override
	public void loadCraftingRecipes(String outputId, Object... results)
	{
		if(outputId.equals(getRecipeId()))
		{
			for(Map.Entry irecipe : getRecipes())
			{
				arecipes.add(new CachedRocketRecipe(irecipe));
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
		for(Map.Entry irecipe : getRecipes())
		{
			if(NEIServerUtils.areStacksSameTypeCrafting(((PositionedStack)irecipe.getValue()).item, result))
			{
				arecipes.add(new CachedRocketRecipe(irecipe));
			}
		}
	}

	@Override
	public void loadUsageRecipes(ItemStack ingredient)
	{
		for(Map.Entry irecipe : getRecipes())
		{
			for (PositionedStack pstack : (((ArrayList<PositionedStack>)irecipe.getKey())))
			{
				if (NEIServerUtils.areStacksSameTypeCrafting(ingredient, pstack.item))
				{
					arecipes.add(new CachedRocketRecipe(irecipe));
					break;
				}
			}
		}
	}

	public class CachedRocketRecipe extends TemplateRecipeHandler.CachedRecipe
	{
		public ArrayList<PositionedStack> input;
		public PositionedStack output;

		@Override
        public ArrayList<PositionedStack> getIngredients()
        {
            return input;
        }

		@Override
		public PositionedStack getResult()
		{
			return output;
		}

		public CachedRocketRecipe(ArrayList<PositionedStack> pstack1, PositionedStack pstack2)
		{
			super();
			input = pstack1;
			output = pstack2;
		}

		public CachedRocketRecipe(Map.Entry recipe)
		{
			this((ArrayList<PositionedStack>)recipe.getKey(), (PositionedStack)recipe.getValue());
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
