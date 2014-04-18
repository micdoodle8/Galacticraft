package micdoodle8.mods.galacticraft.api.recipe;

import java.util.ArrayList;

import micdoodle8.mods.galacticraft.api.recipe.SchematicEvent.FlipPage;
import micdoodle8.mods.galacticraft.api.recipe.SchematicEvent.Unlock;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class SchematicRegistry
{
	public static ArrayList<ISchematicPage> schematicRecipes = new ArrayList<ISchematicPage>();

	/**
	 * Register a new schematic page
	 */
	public static void registerSchematicRecipe(ISchematicPage page)
	{
		if (!SchematicRegistry.schematicRecipes.contains(page))
		{
			SchematicRegistry.schematicRecipes.add(page);
		}
	}

	/**
	 * Finds the recipe for the given itemstack
	 * 
	 * @param stack
	 *            the itemstack to be searched with
	 * @return the recipe that requires the provided itemstack
	 */
	public static ISchematicPage getMatchingRecipeForItemStack(ItemStack stack)
	{
		for (final ISchematicPage schematic : SchematicRegistry.schematicRecipes)
		{
			final ItemStack requiredItem = schematic.getRequiredItem();

			if (requiredItem != null && stack != null && requiredItem.isItemEqual(stack))
			{
				return schematic;
			}
		}

		return null;
	}

	/**
	 * Finds the recipe for the given recipe ID
	 * 
	 * @param id
	 *            the ID to be searched with
	 * @return the recipe that has and ID equal to the one provided
	 */
	public static ISchematicPage getMatchingRecipeForID(int id)
	{
		for (final ISchematicPage schematic : SchematicRegistry.schematicRecipes)
		{
			if (schematic.getPageID() == id)
			{
				return schematic;
			}
		}

		return null;
	}

	/**
	 * Called when a player unlocks a page. Used internally.
	 * 
	 * @param player
	 *            the player that unlocked the schematic
	 * @param page
	 *            the schematic page to be unlocked
	 */
	public static void addUnlockedPage(EntityPlayerMP player, ISchematicPage page)
	{
		// Used internally to add page to player's list of unlocked schematics.
		// No need to subscribe to this event
		MinecraftForge.EVENT_BUS.post(new Unlock(player, page));
	}

	/**
	 * Called when a player unlocks a page. Used internally.
	 * 
	 * @param player
	 *            the player that unlocked the schematic
	 * @param stack
	 *            the itemstack the player has provided
	 * @return the schematic page that was unlocked
	 */
	public static ISchematicPage unlockNewPage(EntityPlayerMP player, ItemStack stack)
	{
		if (stack != null)
		{
			final ISchematicPage schematic = SchematicRegistry.getMatchingRecipeForItemStack(stack);

			if (schematic != null)
			{
				SchematicRegistry.addUnlockedPage(player, schematic);

				return schematic;
			}
		}

		return null;
	}

	/**
	 * Finds the correct schematic when player presses NEXT
	 * 
	 * @param currentIndex
	 *            the current index of unlocked schematics the player is viewing
	 * @return the schematic page that will be shown when the player clicks NEXT
	 */
	@SideOnly(Side.CLIENT)
	public static void flipToNextPage(int currentIndex)
	{
		FMLClientHandler.instance().getClient().currentScreen = null;

		// Used internally inside Galacticraft to flip to the next page. No need
		// to subscribe to this event.
		MinecraftForge.EVENT_BUS.post(new FlipPage(null, currentIndex, 1));
	}

	/**
	 * Finds the correct schematic when player presses BACK
	 * 
	 * @param currentIndex
	 *            the current index of unlocked schematics the player is viewing
	 * @return the schematic page that will be shown when the player clicks BACK
	 */
	@SideOnly(Side.CLIENT)
	public static void flipToLastPage(int currentIndex)
	{
		FMLClientHandler.instance().getClient().currentScreen = null;

		// Used internally inside Galacticraft to flip to the last page. No need
		// to subscribe to this event.
		MinecraftForge.EVENT_BUS.post(new FlipPage(null, currentIndex, -1));
	}
}
