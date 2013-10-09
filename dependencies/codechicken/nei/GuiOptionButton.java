package codechicken.nei;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiSmallButton;

public class GuiOptionButton extends GuiSmallButton
{
    public GuiOptionButton(int i, int j, int k, String s)
    {
        super(i, j, k, s);
    }
    
    @Override
    public boolean mousePressed(Minecraft minecraft, int i, int j)
    {
        return drawButton && i >= xPosition && j >= yPosition && i < xPosition + width && j < yPosition + height;
    }
}
