package micdoodle8.mods.galacticraft.core.schematic;

import micdoodle8.mods.galacticraft.API.ISchematicPage;
import micdoodle8.mods.galacticraft.core.GCCoreConfigManager;
import micdoodle8.mods.galacticraft.core.client.gui.GCCoreGuiSchematicRocketT1;
import micdoodle8.mods.galacticraft.core.inventory.GCCoreContainerRocketBench;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.item.ItemStack;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class GCCoreSchematicRocketT1 implements ISchematicPage
{
	@Override
	public int getPageID()
	{
		return GCCoreConfigManager.idSchematicRocketT1;
	}

	@Override
	public int getGuiID()
	{
		return GCCoreConfigManager.idGuiRocketCraftingBench;
	}

	@Override
	public ItemStack getRequiredItem()
	{
		return null;
	}

	@SideOnly(Side.CLIENT)
	@Override
	public String getTitle()
	{
		return "T1 Rocket";
	}

	@SideOnly(Side.CLIENT)
	@Override
	public GuiScreen getResultScreen(EntityPlayer player, int x, int y, int z)
	{
		return new GCCoreGuiSchematicRocketT1(player.inventory, x, y, z);
	}

	@Override
	public Container getResultContainer(EntityPlayer player, int x, int y, int z)
	{
		return new GCCoreContainerRocketBench(player.inventory, x, y, z);
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
