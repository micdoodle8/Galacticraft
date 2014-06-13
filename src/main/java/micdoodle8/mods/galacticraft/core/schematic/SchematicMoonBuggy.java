package micdoodle8.mods.galacticraft.core.schematic;

import micdoodle8.mods.galacticraft.api.recipe.SchematicPage;
import micdoodle8.mods.galacticraft.core.client.gui.GuiIdsCore;
import micdoodle8.mods.galacticraft.core.client.gui.container.GuiSchematicBuggy;
import micdoodle8.mods.galacticraft.core.inventory.ContainerBuggyBench;
import micdoodle8.mods.galacticraft.core.items.GCItems;
import micdoodle8.mods.galacticraft.core.util.ConfigManagerCore;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.item.ItemStack;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;



public class SchematicMoonBuggy extends SchematicPage
{
	@Override
	public int getPageID()
	{
		return ConfigManagerCore.idSchematicMoonBuggy;
	}

	@Override
	public int getGuiID()
	{
		return GuiIdsCore.NASA_WORKBENCH_BUGGY;
	}

	@Override
	public ItemStack getRequiredItem()
	{
		return new ItemStack(GCItems.schematic, 1, 0);
	}

	@SideOnly(Side.CLIENT)
	@Override
	public GuiScreen getResultScreen(EntityPlayer player, int x, int y, int z)
	{
		return new GuiSchematicBuggy(player.inventory);
	}

	@Override
	public Container getResultContainer(EntityPlayer player, int x, int y, int z)
	{
		return new ContainerBuggyBench(player.inventory, x, y, z);
	}
}
