package micdoodle8.mods.galacticraft.mars.schematic;

import micdoodle8.mods.galacticraft.api.recipe.ISchematicPage;
import micdoodle8.mods.galacticraft.mars.GCMarsConfigManager;
import micdoodle8.mods.galacticraft.mars.client.gui.GCMarsGuiSchematicCargoRocketBench;
import micdoodle8.mods.galacticraft.mars.inventory.GCMarsContainerCargoRocketBench;
import micdoodle8.mods.galacticraft.mars.items.GCMarsItems;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.item.ItemStack;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

/**
 * GCMarsSchematicCargoRocket.java
 * 
 * This file is part of the Galacticraft project
 * 
 * @author micdoodle8
 * @license Lesser GNU Public License v3 (http://www.gnu.org/licenses/lgpl.html)
 * 
 */
public class GCMarsSchematicCargoRocket implements ISchematicPage
{
	@Override
	public int getPageID()
	{
		return GCMarsConfigManager.idSchematicCargoRocket;
	}

	@Override
	public int getGuiID()
	{
		return GCMarsConfigManager.idGuiCargoRocketCraftingBench;
	}

	@Override
	public ItemStack getRequiredItem()
	{
		return new ItemStack(GCMarsItems.schematic.itemID, 1, 1);
	}

	@SideOnly(Side.CLIENT)
	@Override
	public GuiScreen getResultScreen(EntityPlayer player, int x, int y, int z)
	{
		return new GCMarsGuiSchematicCargoRocketBench(player.inventory, x, y, z);
	}

	@Override
	public Container getResultContainer(EntityPlayer player, int x, int y, int z)
	{
		return new GCMarsContainerCargoRocketBench(player.inventory, x, y, z);
	}

	@Override
	public int compareTo(ISchematicPage o)
	{
		if (this.getPageID() > o.getPageID())
		{
			return 1;
		}
		else
		{
			return -1;
		}
	}
}
