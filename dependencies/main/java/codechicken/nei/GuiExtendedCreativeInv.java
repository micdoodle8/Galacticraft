package codechicken.nei;

import codechicken.lib.texture.TextureUtils;
import codechicken.nei.api.INEIGuiHandler;
import codechicken.nei.api.TaggedInventoryArea;
import codechicken.nei.util.NEIServerUtils;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.inventory.Container;
import net.minecraft.item.ItemStack;

import java.util.Arrays;
import java.util.List;

public class GuiExtendedCreativeInv extends GuiContainer implements INEIGuiHandler {
    public GuiExtendedCreativeInv(Container par1Container) {
        super(par1Container);
        ySize = 198;
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float var1, int var2, int var3) {
        GlStateManager.color(1, 1, 1, 1);
        TextureUtils.changeTexture("nei:textures/gui/inv.png");

        int x = guiLeft;
        int y = guiTop - 4;

        drawTexturedModalRect(x - 23, y, 0, 0, 199, 204);
    }

    @Override
    public VisibilityData modifyVisiblity(GuiContainer gui, VisibilityData currentVisibility) {
        return currentVisibility;
    }

    @Override
    public Iterable<Integer> getItemSpawnSlots(GuiContainer gui, ItemStack item) {
        return NEIServerUtils.getRange(0, 54);
    }

    @Override
    public List<TaggedInventoryArea> getInventoryAreas(GuiContainer gui) {
        return Arrays.asList(new TaggedInventoryArea("ExtendedCreativeInv", 0, 54, inventorySlots));
    }

    @Override
    public boolean handleDragNDrop(GuiContainer gui, int mousex, int mousey, ItemStack draggedStack, int button) {
        return false;
    }

    @Override
    public boolean hideItemPanelSlot(GuiContainer gui, int x, int y, int w, int h) {
        return false;
    }
}
