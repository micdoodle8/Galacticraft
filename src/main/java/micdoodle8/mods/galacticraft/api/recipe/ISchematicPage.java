package micdoodle8.mods.galacticraft.api.recipe;

import net.minecraft.client.gui.IHasContainer;
import net.minecraft.client.gui.ScreenManager;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.SimpleNamedContainerProvider;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

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
     * The item required to unlock this schematic. The item class must implement
     * ISchematicItem, since it goes in the NASA Workbench unlock slot.
     */
    ItemStack getRequiredItem();

    /**
     * The resulting client-LogicalSide GUI for this page
     *
     * @param player The player opening this GUI
     * @param pos    Coordinates of the NASA Workbench
     * @return the GUI to be opened with this schematic
     */
    @OnlyIn(Dist.CLIENT)
    <M extends Container, U extends Screen & IHasContainer<M>> ScreenManager.IScreenFactory<M, U> getResultScreen(PlayerEntity player, BlockPos pos);

    /**
     * The resulting container for this page
     *
     * @param player The player opening this GUI
     * @return the container to be opened with this schematic
     */
    SimpleNamedContainerProvider getContainerProvider(PlayerEntity player);
}
