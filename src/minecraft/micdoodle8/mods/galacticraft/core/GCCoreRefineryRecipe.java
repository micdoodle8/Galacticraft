//package micdoodle8.mods.galacticraft.core;
//
//import java.util.SortedSet;
//import java.util.TreeSet;
//
//import net.minecraftforge.liquids.LiquidStack;
//import cpw.mods.fml.common.FMLLog;
//
//public class GCCoreRefineryRecipe implements Comparable<GCCoreRefineryRecipe> 
//{
//	private static SortedSet<GCCoreRefineryRecipe> recipes = new TreeSet<GCCoreRefineryRecipe>();
//
//	public final LiquidStack ingredient;
//	public final LiquidStack result;
//	
//	public GCCoreRefineryRecipe(LiquidStack ingredient, LiquidStack result) 
//	{
//		this.ingredient = ingredient;
//		this.result = result;
//	}
//
//	public static void registerRefineryRecipe(GCCoreRefineryRecipe recipe) 
//	{
//		if (!recipes.contains(recipe))
//		{
//			recipes.add(recipe);
//		}
//	}
//
//	public static GCCoreRefineryRecipe findRefineryRecipe(LiquidStack liquid1) 
//	{
//		for (GCCoreRefineryRecipe recipe : recipes)
//		{
//			FMLLog.info("" + recipe.ingredient.itemID + " " + liquid1.itemID);
//			if (recipe.matches(liquid1))
//			{
//				return recipe;
//			}
//		}
//
//		return null;
//	}
//
//	public boolean matches(LiquidStack liquid1) 
//	{
//		if (liquid1 == null)
//		{
//			return false;
//		}
//
//		if (ingredient != null) 
//		{
//			return ingredient.isLiquidEqual(liquid1);
//		}
//
//		return false;
//	}
//
//	@Override
//	public int compareTo(GCCoreRefineryRecipe other) {
//
//		if (other == null)
//		{
//			return -1;
//		}
//		else if (ingredient == null) 
//		{
//			if (other.ingredient == null)
//			{
//				return 0;
//			}
//			else
//			{
//				return 1;
//			}
//		} 
//		else if (other.ingredient == null)
//		{
//			return -1;
//		}
//		else if (ingredient.itemID != other.ingredient.itemID)
//		{
//			return ingredient.itemID - other.ingredient.itemID;
//		}
//		else if (ingredient.itemMeta != other.ingredient.itemMeta)
//		{
//			return ingredient.itemMeta - other.ingredient.itemMeta;
//		}
//
//		return 0;
//	}
//
//	@Override
//	public boolean equals(Object obj) 
//	{
//		if (obj != null && obj instanceof GCCoreRefineryRecipe)
//		{
//			return this.compareTo((GCCoreRefineryRecipe) obj) == 0;
//		}
//			
//		return false;
//	}
//
//	@Override
//	public int hashCode() 
//	{
//		if (ingredient == null)
//		{
//			return 0;
//		}
//		
//		return ingredient.itemID ^ ingredient.itemMeta;
//	}
//}
