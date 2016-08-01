package codechicken.nei.api;

import codechicken.nei.PositionedStack;
import codechicken.nei.guihook.GuiContainerManager;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.inventory.Slot;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;
import java.util.List;

public class DefaultOverlayRenderer implements IRecipeOverlayRenderer {
    IStackPositioner positioner;
    ArrayList<PositionedStack> ingreds;

    public DefaultOverlayRenderer(List<PositionedStack> ai, IStackPositioner positioner) {
        positioner = this.positioner = positioner;
        ingreds = new ArrayList<PositionedStack>();
        for (PositionedStack stack : ai) {
            ingreds.add(stack.copy());
        }
        ingreds = positioner.positionStacks(ingreds);
    }

    @Override
    public void renderOverlay(GuiContainerManager gui, Slot slot) {
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE);

        GuiContainerManager.setItemRenderColour(0xA0A0A0B0);
        for (PositionedStack stack : ingreds) {
            if (stack.relx == slot.xDisplayPosition && stack.rely == slot.yDisplayPosition) {
                GuiContainerManager.drawItem(stack.relx, stack.rely, stack.item);
            }
        }
        GuiContainerManager.setItemRenderColour(-1);

        GlStateManager.disableBlend();
        GlStateManager.enableLighting();
    }
}
