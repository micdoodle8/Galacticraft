package micdoodle8.mods.galacticraft.core.schematic;

import micdoodle8.mods.galacticraft.api.recipe.SchematicPage;
import micdoodle8.mods.galacticraft.core.client.gui.container.GuiSchematicRocketT1;
import micdoodle8.mods.galacticraft.core.inventory.ContainerRocketBench;
import micdoodle8.mods.galacticraft.core.util.GCConfigManager;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.item.ItemStack;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

/**
 * GCCoreSchematicRocketT1.java
 * 
 * This file is part of the Galacticraft project
 * 
 * @author micdoodle8
 * @license Lesser GNU Public License v3 (http://www.gnu.org/licenses/lgpl.html)
 * 
 */
public class SchematicPageTier1Rocket extends SchematicPage
{
	@Override
	public int getPageID()
	{
		return GCConfigManager.idSchematicRocketT1;
	}

	@Override
	public int getGuiID()
	{
		return GCConfigManager.idGuiRocketCraftingBench;
	}

	@Override
	public ItemStack getRequiredItem()
	{
		return null;
	}

	@SideOnly(Side.CLIENT)
	@Override
	public GuiScreen getResultScreen(EntityPlayer player, int x, int y, int z)
	{
		return new GuiSchematicRocketT1(player.inventory, x, y, z);
	}

	@Override
	public Container getResultContainer(EntityPlayer player, int x, int y, int z)
	{
		return new ContainerRocketBench(player.inventory, x, y, z);
	}
}
