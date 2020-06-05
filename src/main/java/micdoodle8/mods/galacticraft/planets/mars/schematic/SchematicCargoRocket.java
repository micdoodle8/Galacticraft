package micdoodle8.mods.galacticraft.planets.mars.schematic;

import micdoodle8.mods.galacticraft.api.recipe.ISchematicPage;
import micdoodle8.mods.galacticraft.core.Constants;
import micdoodle8.mods.galacticraft.planets.GuiIdsPlanets;
import micdoodle8.mods.galacticraft.planets.mars.ConfigManagerMars;
import micdoodle8.mods.galacticraft.planets.mars.client.gui.GuiSchematicCargoRocket;
import micdoodle8.mods.galacticraft.planets.mars.inventory.ContainerSchematicCargoRocket;
import micdoodle8.mods.galacticraft.planets.mars.items.MarsItems;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.container.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class SchematicCargoRocket implements ISchematicPage
{
    @Override
    public int getPageID()
    {
        return ConfigManagerMars.idSchematicCargoRocket;
    }

    @Override
    public int getGuiID()
    {
        return GuiIdsPlanets.NASA_WORKBENCH_CARGO_ROCKET + Constants.MOD_ID_PLANETS.hashCode();
    }

    @Override
    public ItemStack getRequiredItem()
    {
        return new ItemStack(MarsItems.schematic, 1, 1);
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public Screen getResultScreen(PlayerEntity player, BlockPos pos)
    {
        return new GuiSchematicCargoRocket(player.inventory, pos);
    }

    @Override
    public Container getResultContainer(PlayerEntity player, BlockPos pos)
    {
        return new ContainerSchematicCargoRocket(player.inventory, pos);
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
