package codechicken.nei.api;

import java.util.List;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.item.ItemStack;
import codechicken.nei.VisiblityData;

/**
 * If this is implemented on a gui, it will be automatically registered
 */
public interface INEIGuiHandler
{
    public VisiblityData modifyVisiblity(GuiContainer gui, VisiblityData currentVisibility);
    
    /**
     * NEI will give the specified item to the slot returned if the player's inventory is full.
     * return -1 for no slot.
     */
    public int getItemSpawnSlot(GuiContainer gui, ItemStack item);
    
    /**
     * @return A list of TaggedInventoryAreas that will be used with the savestates.
     */
    public List<TaggedInventoryArea> getInventoryAreas(GuiContainer gui);
    
    /**
     * Handles clicks while an itemstack has been dragged from the item panel. Use this to set configurable slots and the like. 
     * Changes made to the stackSize of the dragged stack will be kept
     * @param gui The current gui instance
     * @param mousex The x position of the mouse
     * @param mousey The y position of the mouse
     * @param draggedStack The stack being dragged from the item panel
     * @param button The button presed
     * @return True if the drag n drop was handled. False to resume processing through other routes. The held stack will be deleted if draggedStack.stackSize == 0
     */
    public boolean handleDragNDrop(GuiContainer gui, int mousex, int mousey, ItemStack draggedStack, int button);
}
