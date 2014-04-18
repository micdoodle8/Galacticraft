package micdoodle8.mods.galacticraft.core.schematic;

import micdoodle8.mods.galacticraft.api.recipe.SchematicPage;
import micdoodle8.mods.galacticraft.core.GCCoreConfigManager;
import micdoodle8.mods.galacticraft.core.client.gui.GCCoreGuiSchematicBuggy;
import micdoodle8.mods.galacticraft.core.inventory.GCCoreContainerBuggyBench;
import micdoodle8.mods.galacticraft.core.items.GCCoreItems;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.item.ItemStack;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

/**
 * GCCoreSchematicMoonBuggy.java
 * 
 * This file is part of the Galacticraft project
 * 
 * @author micdoodle8
 * @license Lesser GNU Public License v3 (http://www.gnu.org/licenses/lgpl.html)
 * 
 */
public class GCCoreSchematicMoonBuggy extends SchematicPage
{
	@Override
	public int getPageID()
	{
		return GCCoreConfigManager.idSchematicMoonBuggy;
	}

	@Override
	public int getGuiID()
	{
		return GCCoreConfigManager.idGuiBuggyCraftingBench;
	}

	@Override
	public ItemStack getRequiredItem()
	{
		return new ItemStack(GCCoreItems.schematic, 1, 0);
	}

	@SideOnly(Side.CLIENT)
	@Override
	public GuiScreen getResultScreen(EntityPlayer player, int x, int y, int z)
	{
		return new GCCoreGuiSchematicBuggy(player.inventory);
	}

	@Override
	public Container getResultContainer(EntityPlayer player, int x, int y, int z)
	{
		return new GCCoreContainerBuggyBench(player.inventory, x, y, z);
	}
}
