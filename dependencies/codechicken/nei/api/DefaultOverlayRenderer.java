package codechicken.nei.api;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.inventory.Slot;

import org.lwjgl.opengl.GL11;

import codechicken.nei.PositionedStack;
import codechicken.nei.forge.GuiContainerManager;

public class DefaultOverlayRenderer implements IRecipeOverlayRenderer
{    
    public DefaultOverlayRenderer(List<PositionedStack> ai, IStackPositioner positioner)
    {
        positioner = this.positioner = positioner;
        ingreds = new ArrayList<PositionedStack>();
        for(PositionedStack stack : ai)
            ingreds.add(stack.copy());
        ingreds = positioner.positionStacks(ingreds);
    }
    
    @Override
    public void renderOverlay(GuiContainerManager gui, Slot slot)
    {
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(770, 1);
        GL11.glColor4d(0.6, 0.6, 0.6, 0.7);
        
        GuiContainerManager.setColouredItemRender(true);
        for(PositionedStack stack : ingreds)
        {
            if(stack.relx == slot.xDisplayPosition && stack.rely == slot.yDisplayPosition)
                GuiContainerManager.drawItem(stack.relx, stack.rely, stack.item);
        }
        GuiContainerManager.setColouredItemRender(false);
        
        GL11.glColor4f(1, 1, 1, 1);
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glEnable(GL11.GL_LIGHTING);
    }
    
    IStackPositioner positioner;    
    ArrayList<PositionedStack> ingreds;
}
