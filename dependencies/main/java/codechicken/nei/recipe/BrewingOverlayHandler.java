package codechicken.nei.recipe;

import codechicken.nei.PositionedStack;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.inventory.Slot;

import java.util.List;

public class BrewingOverlayHandler extends DefaultOverlayHandler
{
    @Override
    public Slot[][] mapIngredSlots(GuiContainer gui, List<PositionedStack> ingredients)
    {
        Slot[][] map = super.mapIngredSlots(gui, ingredients);
        Slot[] potSlots = new Slot[3];
        for(int i = 0; i < 3; i++)
            potSlots[i] = (Slot) gui.inventorySlots.inventorySlots.get(i);
        map[1] = potSlots;
        return map;
    }
}
