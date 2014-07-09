package micdoodle8.mods.galacticraft.planets.mars.schematic;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import micdoodle8.mods.galacticraft.api.recipe.ISchematicPage;
import micdoodle8.mods.galacticraft.core.Constants;
import micdoodle8.mods.galacticraft.core.items.GCItems;
import micdoodle8.mods.galacticraft.planets.GalacticraftPlanets;
import micdoodle8.mods.galacticraft.planets.GuiIdsPlanets;
import micdoodle8.mods.galacticraft.planets.mars.ConfigManagerMars;
import micdoodle8.mods.galacticraft.planets.mars.client.gui.GuiSchematicTier2Rocket;
import micdoodle8.mods.galacticraft.planets.mars.inventory.ContainerSchematicTier2Rocket;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.item.ItemStack;

public class SchematicTier2Rocket implements ISchematicPage
{
	@Override
	public int getPageID()
	{
		return ConfigManagerMars.idSchematicRocketT2;
	}

	@Override
	public int getGuiID()
	{
		return GuiIdsPlanets.NASA_WORKBENCH_TIER_2_ROCKET + Constants.MOD_ID_PLANETS.hashCode();
	}

	@Override
	public ItemStack getRequiredItem()
	{
		return new ItemStack(GCItems.schematic, 1, 1);
	}

	@SideOnly(Side.CLIENT)
	@Override
	public GuiScreen getResultScreen(EntityPlayer player, int x, int y, int z)
	{
		return new GuiSchematicTier2Rocket(player.inventory, x, y, z);
	}

	@Override
	public Container getResultContainer(EntityPlayer player, int x, int y, int z)
	{
		return new ContainerSchematicTier2Rocket(player.inventory, x, y, z);
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
