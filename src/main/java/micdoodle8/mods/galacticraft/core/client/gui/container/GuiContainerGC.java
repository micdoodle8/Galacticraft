package micdoodle8.mods.galacticraft.core.client.gui.container;

import cpw.mods.fml.common.Loader;
import micdoodle8.mods.galacticraft.core.client.gui.element.GuiElementInfoRegion;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public abstract class GuiContainerGC extends GuiContainer
{
    public List<GuiElementInfoRegion> infoRegions = new ArrayList<GuiElementInfoRegion>();

    public GuiContainerGC(Container container)
    {
        super(container);
    }

    @Override
    public void drawScreen(int par1, int par2, float par3)
    {
        super.drawScreen(par1, par2, par3);

        for (int k = 0; k < this.infoRegions.size(); ++k)
        {
            GuiElementInfoRegion guibutton = this.infoRegions.get(k);
            guibutton.drawRegion(par1, par2);
        }
    }

    @Override
    public void setWorldAndResolution(Minecraft par1Minecraft, int par2, int par3)
    {
        this.infoRegions.clear();
        super.setWorldAndResolution(par1Minecraft, par2, par3);
    }

    public int getTooltipOffset(int par1, int par2)
    {
        for (int i1 = 0; i1 < this.inventorySlots.inventorySlots.size(); ++i1)
        {
            Slot slot = (Slot) this.inventorySlots.inventorySlots.get(i1);

            if (slot.func_111238_b() && this.func_146978_c(slot.xDisplayPosition, slot.yDisplayPosition, 16, 16, par1, par2))
            {
                ItemStack itemStack = slot.getStack();

                if (itemStack != null)
                {
                    List list = itemStack.getTooltip(this.mc.thePlayer, this.mc.gameSettings.advancedItemTooltips);
                    int size = list.size();

                    if (Loader.isModLoaded("Waila"))
                    {
                        size++;
                    }

                    return size * 10 + 10;
                }
            }
        }

        return 0;
    }
}
