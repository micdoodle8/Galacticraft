package micdoodle8.mods.galacticraft.planets.asteroids.schematic;

import micdoodle8.mods.galacticraft.api.recipe.ISchematicPage;
import micdoodle8.mods.galacticraft.core.Constants;
import micdoodle8.mods.galacticraft.planets.GuiIdsPlanets;
import micdoodle8.mods.galacticraft.planets.asteroids.ConfigManagerAsteroids;
import micdoodle8.mods.galacticraft.planets.asteroids.client.gui.GuiSchematicTier3Rocket;
import micdoodle8.mods.galacticraft.planets.asteroids.inventory.ContainerSchematicTier3Rocket;
import micdoodle8.mods.galacticraft.planets.mars.items.MarsItems;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class SchematicTier3Rocket implements ISchematicPage
{
    @Override
    public int getPageID()
    {
        return ConfigManagerAsteroids.idSchematicRocketT3;
    }

    @Override
    public int getGuiID()
    {
        return GuiIdsPlanets.NASA_WORKBENCH_TIER_3_ROCKET + Constants.MOD_ID_PLANETS.hashCode();
    }

    @Override
    public ItemStack getRequiredItem()
    {
        return new ItemStack(MarsItems.schematic, 1, 0);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public GuiScreen getResultScreen(EntityPlayer player, BlockPos pos)
    {
        return new GuiSchematicTier3Rocket(player.inventory, pos);
    }

    @Override
    public Container getResultContainer(EntityPlayer player, BlockPos pos)
    {
        return new ContainerSchematicTier3Rocket(player.inventory, pos);
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
