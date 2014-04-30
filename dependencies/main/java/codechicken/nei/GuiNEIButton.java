package codechicken.nei;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

public class GuiNEIButton extends GuiButton
{
    protected static ResourceLocation guiTex = new ResourceLocation("textures/gui/widgets.png");
    
    public GuiNEIButton(int i, int j, int k, int l, int i1, String s)
    {
        super(i, j, k, l, i1, s);
    }
    
    public void drawButton(Minecraft minecraft, int i, int j)
    {
        if(!visible)
            return;

        FontRenderer fontrenderer = minecraft.fontRenderer;
        minecraft.renderEngine.bindTexture(guiTex);
        GL11.glColor4f(1, 1, 1, 1);
        boolean flag = i >= xPosition && j >= yPosition && i < xPosition + width && j < yPosition + height;
        int k = getHoverState(flag);
        drawTexturedModalRect(xPosition, yPosition, 0, 46 + k * 20, width / 2, height / 2);//top left
        drawTexturedModalRect(xPosition + width / 2, yPosition, 200 - width / 2, 46 + k * 20, width / 2, height / 2);//top right
        drawTexturedModalRect(xPosition, yPosition + height / 2, 0, 46 + k * 20 + 20 - height / 2, width / 2, height / 2);//bottom left
        drawTexturedModalRect(xPosition + width / 2, yPosition + height / 2, 200 - width / 2, 46 + k * 20 + 20 - height / 2, width / 2, height / 2);//bottom right
        mouseDragged(minecraft, i, j);
        
        if(!enabled)
            drawCenteredString(fontrenderer, displayString, xPosition + width / 2, yPosition + (height - 8) / 2, 0xffa0a0a0);
        else if(flag)
            drawCenteredString(fontrenderer, displayString, xPosition + width / 2, yPosition + (height - 8) / 2, 0xffffa0);
        else
            drawCenteredString(fontrenderer, displayString, xPosition + width / 2, yPosition + (height - 8) / 2, 0xe0e0e0);
    }
}
