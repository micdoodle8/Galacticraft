package micdoodle8.mods.galacticraft.core.client.gui.container;

import micdoodle8.mods.galacticraft.core.client.gui.element.GuiElementInfoRegion;
import micdoodle8.mods.galacticraft.core.util.CompatibilityManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.BeaconContainer;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;

import java.util.ArrayList;
import java.util.List;

public abstract class GuiContainerGC<T extends Container> extends ContainerScreen<T>
{
    public List<GuiElementInfoRegion> infoRegions = new ArrayList<>();

    public GuiContainerGC(T container, PlayerInventory playerInventory, ITextComponent title)
    {
        super(container, playerInventory, title);
    }

    @Override
    public void render(int mouseX, int mouseY, float partialTicks)
    {
        this.renderBackground();
        super.render(mouseX, mouseY, partialTicks);
        this.renderHoveredToolTip(mouseX, mouseY);

        for (int k = 0; k < this.infoRegions.size(); ++k)
        {
            GuiElementInfoRegion guibutton = this.infoRegions.get(k);
            guibutton.drawRegion(mouseX, mouseY);
        }
    }


    @Override
    public void init(Minecraft mc, int width, int height)
    {
        this.infoRegions.clear();
        super.init(mc, width, height);
    }

    public int getTooltipOffset(int par1, int par2)
    {
        for (int i1 = 0; i1 < this.container.inventorySlots.size(); ++i1)
        {
            Slot slot = this.container.inventorySlots.get(i1);

            if (slot.isEnabled() && this.isPointInRegion(slot.xPos, slot.yPos, 16, 16, par1, par2))
            {
                ItemStack itemStack = slot.getStack();

                if (!itemStack.isEmpty())
                {
                    List list = itemStack.getTooltip(this.minecraft.player, this.minecraft.gameSettings.advancedItemTooltips ? ITooltipFlag.TooltipFlags.ADVANCED : ITooltipFlag.TooltipFlags.NORMAL);
                    int size = list.size();

                    if (CompatibilityManager.isWailaLoaded())
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
