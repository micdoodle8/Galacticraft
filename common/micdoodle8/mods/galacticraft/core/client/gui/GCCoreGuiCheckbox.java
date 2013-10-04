package micdoodle8.mods.galacticraft.core.client.gui;

import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

public class GCCoreGuiCheckbox extends GuiButton
{
    protected static final ResourceLocation texture = new ResourceLocation(GalacticraftCore.TEXTURE_DOMAIN, "textures/gui/gui.png");
    
    public boolean isSelected;
    
    public GCCoreGuiCheckbox(int id, int x, int y, boolean selected, String text)
    {
        super(id, x, y, 13, 13, text);
        this.isSelected = selected;
    }

    @Override
    public void drawButton(Minecraft par1Minecraft, int par2, int par3)
    {
        if (this.drawButton)
        {
            par1Minecraft.getTextureManager().bindTexture(texture);
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            this.field_82253_i = par2 >= this.xPosition && par3 >= this.yPosition && par2 < this.xPosition + this.width && par3 < this.yPosition + this.height;
            this.drawTexturedModalRect(this.xPosition, this.yPosition, this.isSelected ? 20 : 33, field_82253_i ? 24 : 37, this.width, this.height);
            this.mouseDragged(par1Minecraft, par2, par3);
            par1Minecraft.fontRenderer.drawString(this.displayString, this.xPosition + this.width + 3, this.yPosition + (this.height - 6) / 2, 4210752, false);
        }
    }

    @Override
    public boolean mousePressed(Minecraft par1Minecraft, int par2, int par3)
    {
        if (this.enabled && this.drawButton && par2 >= this.xPosition && par3 >= this.yPosition && par2 < this.xPosition + this.width && par3 < this.yPosition + this.height)
        {
            this.isSelected = !this.isSelected;
            return true;
        }
        
        return false;
    }
}
