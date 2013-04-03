package micdoodle8.mods.galacticraft.core.schematic;

import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.item.ItemStack;
import micdoodle8.mods.galacticraft.API.ISchematicPage;
import micdoodle8.mods.galacticraft.core.GCCoreConfigManager;
import micdoodle8.mods.galacticraft.core.client.gui.GCCoreGuiSchematicBuggy;
import micdoodle8.mods.galacticraft.core.inventory.GCCoreContainerBuggyBench;
import micdoodle8.mods.galacticraft.core.items.GCCoreItems;

public class GCCoreSchematicMoonBuggy implements ISchematicPage
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
		return new ItemStack(GCCoreItems.buggy);
	}

	@Override
	public String getTitle()
	{
		return "Moon Buggy";
	}

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
