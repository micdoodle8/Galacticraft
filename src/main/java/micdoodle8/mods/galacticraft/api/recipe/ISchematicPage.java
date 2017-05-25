package micdoodle8.mods.galacticraft.api.recipe;

import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * Schematic page to be added to NASA Workbench
 */
public interface ISchematicPage extends Comparable<ISchematicPage>
{
    /**
     * Get the page ID. Make it configurable since it has to be unique between
     * other Galacticraft addons. Determines order of schematics.
     */
    int getPageID();

    /**
     * The GUI ID of this page. Used like any other GUI IDs to determine which
     * container and GUI to open. Again, must be unique between mods so make it
     * configurable.
     */
    int getGuiID();

    /**
     * The item required to unlock this schematic. The item class must implement
     * ISchematicItem, since it goes in the NASA Workbench unlock slot.
     */
    ItemStack getRequiredItem();

    /**
     * The resulting client-side GUI for this page
     *
     * @param player The player opening this GUI
     * @param pos    Coordinates of the NASA Workbench
     * @return the GUI to be opened with this schematic
     */
    @SideOnly(Side.CLIENT)
    GuiScreen getResultScreen(EntityPlayer player, BlockPos pos);

    /**
     * The resulting container for this page
     *
     * @param player The player opening this GUI
     * @param pos    Coordinates of the NASA Workbench
     * @return the container to be opened with this schematic
     */
    Container getResultContainer(EntityPlayer player, BlockPos pos);
}
