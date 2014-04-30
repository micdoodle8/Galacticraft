package codechicken.lib.render;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.item.ItemStack;
import org.lwjgl.opengl.GL11;

public class FontUtils
{
    public static FontRenderer fontRenderer = Minecraft.getMinecraft().fontRenderer;
    
    public static void drawCenteredString(String s, int xCenter, int y, int colour)
    {
        fontRenderer.drawString(s, xCenter-fontRenderer.getStringWidth(s)/2, y, colour);
    }
    
    public static void drawRightString(String s, int xRight, int y, int colour)
    {
        fontRenderer.drawString(s, xRight-fontRenderer.getStringWidth(s), y, colour);
    }
    
    public static final String[] prefixes = new String[]{"K", "M", "G"};
    public static void drawItemQuantity(int x, int y, ItemStack item, String quantity, int mode)
    {
        if(item == null || (quantity == null && item.stackSize <= 1))
            return;
        
        if(quantity == null)
        {
            switch(mode)
            {
                case 2:
                    int q = item.stackSize;
                    String postfix = "";
                    for(int p = 0; p < 3 && q > 1000; p++)
                    {
                        q/=1000;
                        postfix = prefixes[p];
                    }
                    quantity = Integer.toString(q)+postfix;
                case 1:
                    quantity = "";
                    if(item.stackSize/64 > 0)
                        quantity+=item.stackSize/64 + "s";
                    if(item.stackSize%64 > 0)
                        quantity+=item.stackSize%64;
                break;
                default:
                    quantity = Integer.toString(item.stackSize);
                break;
            }
        }
        
        double scale = quantity.length() > 2 ? 0.5 : 1;
        double sheight = 8*scale;
        double swidth = fontRenderer.getStringWidth(quantity)*scale;

        GL11.glDisable(GL11.GL_LIGHTING);
        GL11.glDisable(GL11.GL_DEPTH_TEST);
        GL11.glPushMatrix();
            GL11.glTranslated(x+16-swidth, y+16-sheight, 0);
            GL11.glScaled(scale, scale, 1);
            fontRenderer.drawStringWithShadow(quantity, 0, 0, 0xFFFFFF);
        GL11.glPopMatrix();
        GL11.glEnable(GL11.GL_LIGHTING);
        GL11.glEnable(GL11.GL_DEPTH_TEST);
    }
}
