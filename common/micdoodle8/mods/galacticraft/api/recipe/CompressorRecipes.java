package micdoodle8.mods.galacticraft.api.recipe;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.ShapedRecipes;
import net.minecraft.item.crafting.ShapelessRecipes;
import net.minecraft.world.World;
import net.minecraftforge.oredict.OreDictionary;

public class CompressorRecipes
{
	private static List<IRecipe> recipes = new ArrayList<IRecipe>();

	public static ShapedRecipes addRecipe(ItemStack output, Object... inputList)
	{
		String s = "";
		int i = 0;
		int j = 0;
		int k = 0;

		if (inputList[i] instanceof String[])
		{
			String[] astring = (String[]) inputList[i++];

			for (String s1 : astring)
			{
				++k;
				j = s1.length();
				s = s + s1;
			}
		}
		else
		{
			while (inputList[i] instanceof String)
			{
				String s2 = (String) inputList[i++];
				++k;
				j = s2.length();
				s = s + s2;
			}
		}

		HashMap<Character, ItemStack> hashmap;

		for (hashmap = new HashMap<Character, ItemStack>(); i < inputList.length; i += 2)
		{
			Character character = (Character) inputList[i];
			ItemStack itemstack1 = null;

			if (inputList[i + 1] instanceof Item)
			{
				itemstack1 = new ItemStack((Item) inputList[i + 1]);
			}
			else if (inputList[i + 1] instanceof Block)
			{
				itemstack1 = new ItemStack((Block) inputList[i + 1], 1, 32767);
			}
			else if (inputList[i + 1] instanceof ItemStack)
			{
				itemstack1 = (ItemStack) inputList[i + 1];
			}

			hashmap.put(character, itemstack1);
		}

		ItemStack[] aitemstack = new ItemStack[j * k];

		for (int i1 = 0; i1 < j * k; ++i1)
		{
			char c0 = s.charAt(i1);

			if (hashmap.containsKey(Character.valueOf(c0)))
			{
				aitemstack[i1] = hashmap.get(Character.valueOf(c0)).copy();
			}
			else
			{
				aitemstack[i1] = null;
			}
		}

		ShapedRecipes shapedrecipes = new ShapedRecipes(j, k, aitemstack, output);
		CompressorRecipes.recipes.add(shapedrecipes);
		return shapedrecipes;
	}

	public static void addShapelessRecipe(ItemStack par1ItemStack, Object... par2ArrayOfObj)
	{
		ArrayList<ItemStack> arraylist = new ArrayList<ItemStack>();
		Object[] aobject = par2ArrayOfObj;
		int i = par2ArrayOfObj.length;

		for (int j = 0; j < i; ++j)
		{
			Object object1 = aobject[j];

			if (object1 instanceof ItemStack)
			{
				arraylist.add(((ItemStack) object1).copy());
			}
			else if (object1 instanceof Item)
			{
				arraylist.add(new ItemStack((Item) object1));
			}
			else if (object1 instanceof String)
			{
				ArrayList<ItemStack> list = OreDictionary.getOres((String) object1);

				if (!list.isEmpty())
				{
					arraylist.add(list.get(0));
				}
			}
			else
			{
				if (!(object1 instanceof Block))
				{
					throw new RuntimeException("Invalid shapeless compressor recipe!");
				}

				arraylist.add(new ItemStack((Block) object1));
			}
		}

		CompressorRecipes.recipes.add(new ShapelessRecipes(par1ItemStack, arraylist));
	}

	public static ItemStack findMatchingRecipe(IInventory inventory, World par2World)
	{
		int i = 0;
		ItemStack itemstack = null;
		ItemStack itemstack1 = null;
		int j;

		for (j = 0; j < inventory.getSizeInventory(); ++j)
		{
			ItemStack itemstack2 = inventory.getStackInSlot(j);

			if (itemstack2 != null)
			{
				if (i == 0)
				{
					itemstack = itemstack2;
				}

				if (i == 1)
				{
					itemstack1 = itemstack2;
				}

				++i;
			}
		}

		if (i == 2 && itemstack.itemID == itemstack1.itemID && itemstack.stackSize == 1 && itemstack1.stackSize == 1 && Item.itemsList[itemstack.itemID].isRepairable())
		{
			Item item = Item.itemsList[itemstack.itemID];
			int k = item.getMaxDamage() - itemstack.getItemDamageForDisplay();
			int l = item.getMaxDamage() - itemstack1.getItemDamageForDisplay();
			int i1 = k + l + item.getMaxDamage() * 5 / 100;
			int j1 = item.getMaxDamage() - i1;

			if (j1 < 0)
			{
				j1 = 0;
			}

			return new ItemStack(itemstack.itemID, 1, j1);
		}
		else
		{
			for (j = 0; j < CompressorRecipes.recipes.size(); ++j)
			{
				IRecipe irecipe = CompressorRecipes.recipes.get(j);

				if (irecipe instanceof ShapedRecipes && CompressorRecipes.matches((ShapedRecipes) irecipe, inventory, par2World))
				{
					return irecipe.getRecipeOutput().copy();
				}
				else if (irecipe instanceof ShapelessRecipes && CompressorRecipes.matchesShapeless((ShapelessRecipes) irecipe, inventory, par2World))
				{
					return irecipe.getRecipeOutput().copy();
				}
			}

			return null;
		}
	}

	private static boolean matches(ShapedRecipes recipe, IInventory inventory, World par2World)
	{
		for (int i = 0; i <= 3 - recipe.recipeWidth; ++i)
		{
			for (int j = 0; j <= 3 - recipe.recipeHeight; ++j)
			{
				if (CompressorRecipes.checkMatch(recipe, inventory, i, j, true))
				{
					return true;
				}

				if (CompressorRecipes.checkMatch(recipe, inventory, i, j, false))
				{
					return true;
				}
			}
		}

		return false;
	}

	private static boolean checkMatch(ShapedRecipes recipe, IInventory inventory, int par2, int par3, boolean par4)
	{
		for (int k = 0; k < 3; ++k)
		{
			for (int l = 0; l < 3; ++l)
			{
				int i1 = k - par2;
				int j1 = l - par3;
				ItemStack itemstack = null;

				if (i1 >= 0 && j1 >= 0 && i1 < recipe.recipeWidth && j1 < recipe.recipeHeight)
				{
					if (par4)
					{
						itemstack = recipe.recipeItems[recipe.recipeWidth - i1 - 1 + j1 * recipe.recipeWidth];
					}
					else
					{
						itemstack = recipe.recipeItems[i1 + j1 * recipe.recipeWidth];
					}
				}

				ItemStack itemstack1 = null;

				if (k >= 0 && l < 3)
				{
					int k2 = k + l * 3;
					itemstack1 = inventory.getStackInSlot(k2);
				}

				if (itemstack1 != null || itemstack != null)
				{
					if (itemstack1 == null && itemstack != null || itemstack1 != null && itemstack == null)
					{
						return false;
					}

					if (itemstack.itemID != itemstack1.itemID)
					{
						return false;
					}

					if (itemstack.getItemDamage() != 32767 && itemstack.getItemDamage() != itemstack1.getItemDamage())
					{
						return false;
					}
				}
			}
		}

		return true;
	}

	private static boolean matchesShapeless(ShapelessRecipes recipe, IInventory inventory, World par2World)
	{
		@SuppressWarnings("unchecked")
		ArrayList<ItemStack> arraylist = new ArrayList<ItemStack>(recipe.recipeItems);

		for (int i = 0; i < 3; ++i)
		{
			for (int j = 0; j < 3; ++j)
			{
				ItemStack itemstack = null;

				if (j >= 0 && i < 3)
				{
					int k2 = j + i * 3;
					itemstack = inventory.getStackInSlot(k2);
				}

				if (itemstack != null)
				{
					boolean flag = false;
					Iterator<ItemStack> iterator = arraylist.iterator();

					while (iterator.hasNext())
					{
						ItemStack itemstack1 = iterator.next();

						if (itemstack.itemID == itemstack1.itemID && (itemstack1.getItemDamage() == 32767 || itemstack.getItemDamage() == itemstack1.getItemDamage()))
						{
							flag = true;
							arraylist.remove(itemstack1);
							break;
						}
					}

					if (!flag)
					{
						return false;
					}
				}
			}
		}

		return arraylist.isEmpty();
	}

	public static List<IRecipe> getRecipeList()
	{
		return CompressorRecipes.recipes;
	}
}
