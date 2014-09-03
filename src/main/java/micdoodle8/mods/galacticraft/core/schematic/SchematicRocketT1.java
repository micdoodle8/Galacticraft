package micdoodle8.mods.galacticraft.core.schematic;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import micdoodle8.mods.galacticraft.api.recipe.SchematicPage;
import micdoodle8.mods.galacticraft.core.client.gui.GuiIdsCore;
import micdoodle8.mods.galacticraft.core.client.gui.container.GuiSchematicTier1Rocket;
import micdoodle8.mods.galacticraft.core.inventory.ContainerSchematicTier1Rocket;
import micdoodle8.mods.galacticraft.core.util.ConfigManagerCore;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.item.ItemStack;

public class SchematicRocketT1 extends SchematicPage
{
    @Override
    public int getPageID()
    {
        return ConfigManagerCore.idSchematicRocketT1;
    }

    @Override
    public int getGuiID()
    {
        return GuiIdsCore.NASA_WORKBENCH_ROCKET;
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
        return new GuiSchematicTier1Rocket(player.inventory, x, y, z);
    }

    @Override
    public Container getResultContainer(EntityPlayer player, int x, int y, int z)
    {
        return new ContainerSchematicTier1Rocket(player.inventory, x, y, z);
    }
}
