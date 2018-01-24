package micdoodle8.mods.galacticraft.core.client.gui.element;

import micdoodle8.mods.galacticraft.core.client.gui.container.GuiContainerGC;
import micdoodle8.mods.galacticraft.core.util.ColorUtil;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import java.util.Iterator;
import java.util.List;

@SideOnly(Side.CLIENT)
public class GuiElementInfoRegion extends Gui
{
    protected int width;
    protected int height;
    public int xPosition;
    public int yPosition;
    public boolean enabled;
    public boolean drawRegion;
    public boolean withinRegion;
    public List<String> tooltipStrings;
    public int parentWidth;
    public int parentHeight;
    public GuiContainerGC parentGui;

    public GuiElementInfoRegion(int xPos, int yPos, int width, int height, List<String> tooltipStrings, int parentWidth, int parentHeight, GuiContainerGC parentGui)
    {
        this.width = 200;
        this.height = 20;
        this.enabled = true;
        this.xPosition = xPos;
        this.yPosition = yPos;
        this.width = width;
        this.height = height;
        this.tooltipStrings = tooltipStrings;
        this.parentWidth = parentWidth;
        this.parentHeight = parentHeight;
        this.parentGui = parentGui;
    }

    protected int getHoverState(boolean par1)
    {
        byte b0 = 1;

        if (!this.enabled)
        {
            b0 = 0;
        }
        else if (par1)
        {
            b0 = 2;
        }

        return b0;
    }

    public void drawRegion(int par2, int par3)
    {
        GL11.glDisable(GL12.GL_RESCALE_NORMAL);
        RenderHelper.disableStandardItemLighting();
        GL11.glDisable(GL11.GL_LIGHTING);
        GL11.glDisable(GL11.GL_DEPTH_TEST);

        this.withinRegion = par2 >= this.xPosition && par3 >= this.yPosition && par2 < this.xPosition + this.width && par3 < this.yPosition + this.height;

        if (this.drawRegion)
        {
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            int k = this.getHoverState(this.withinRegion);
            Gui.drawRect(this.xPosition, this.yPosition, this.xPosition + this.width, this.yPosition + this.height, ColorUtil.to32BitColor(100 * k, 255, 0, 0));
        }

        if (this.tooltipStrings != null && !this.tooltipStrings.isEmpty() && this.withinRegion)
        {
            int k = 0;
            Iterator<String> iterator = this.tooltipStrings.iterator();

            while (iterator.hasNext())
            {
                String s = iterator.next();
                int l = FMLClientHandler.instance().getClient().fontRenderer.getStringWidth(s);

                if (l > k)
                {
                    k = l;
                }
            }

            int i1 = par2 + 12;
            int j1 = par3 - 12;
            int k1 = 8;

            if (this.tooltipStrings.size() > 1)
            {
                k1 += (this.tooltipStrings.size() - 1) * 10;
            }

            if (i1 + k > this.parentWidth)
            {
                i1 -= 28 + k;
            }

            if (this.parentGui.getTooltipOffset(par2, par3) > 0)
            {
                j1 -= k1 + 9;
            }

            this.zLevel = 300.0F;
//            GuiElementInfoRegion.itemRenderer.zLevel = 300.0F;
            int l1 = -267386864;
            this.drawGradientRect(i1 - 3, j1 - 4, i1 + k + 3, j1 - 3, l1, l1);
            this.drawGradientRect(i1 - 3, j1 + k1 + 3, i1 + k + 3, j1 + k1 + 4, l1, l1);
            this.drawGradientRect(i1 - 3, j1 - 3, i1 + k + 3, j1 + k1 + 3, l1, l1);
            this.drawGradientRect(i1 - 4, j1 - 3, i1 - 3, j1 + k1 + 3, l1, l1);
            this.drawGradientRect(i1 + k + 3, j1 - 3, i1 + k + 4, j1 + k1 + 3, l1, l1);
            int i2 = 1347420415;
            int j2 = (i2 & 16711422) >> 1 | i2 & -16777216;
            this.drawGradientRect(i1 - 3, j1 - 3 + 1, i1 - 3 + 1, j1 + k1 + 3 - 1, i2, j2);
            this.drawGradientRect(i1 + k + 2, j1 - 3 + 1, i1 + k + 3, j1 + k1 + 3 - 1, i2, j2);
            this.drawGradientRect(i1 - 3, j1 - 3, i1 + k + 3, j1 - 3 + 1, i2, i2);
            this.drawGradientRect(i1 - 3, j1 + k1 + 2, i1 + k + 3, j1 + k1 + 3, j2, j2);

            for (int k2 = 0; k2 < this.tooltipStrings.size(); ++k2)
            {
                String s1 = this.tooltipStrings.get(k2);
                FMLClientHandler.instance().getClient().fontRenderer.drawStringWithShadow(s1, i1, j1, -1);

                j1 += 10;
            }

            this.zLevel = 0.0F;
//            GuiElementInfoRegion.itemRenderer.zLevel = 0.0F;
        }

        GL11.glEnable(GL11.GL_LIGHTING);
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        RenderHelper.enableStandardItemLighting();
        GL11.glEnable(GL12.GL_RESCALE_NORMAL);
    }
}
