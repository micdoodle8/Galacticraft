package micdoodle8.mods.galacticraft.core.client.gui;

import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.client.gui.page.GCCoreSmallFontRenderer;
import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;
import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.common.FMLLog;

public class GCCoreGuiDropdown extends GuiButton
{
    protected static final ResourceLocation texture = new ResourceLocation(GalacticraftCore.TEXTURE_DOMAIN, "textures/gui/gui.png");
    
    public boolean dropdownClicked;
    public String[] optionStrings;
    public int selectedOption;
    public GCCoreSmallFontRenderer font;
    
    public GCCoreGuiDropdown(int id, int x, int y, String... text)
    {
        super(id, x, y, 13, 13, "");
        Minecraft mc = FMLClientHandler.instance().getClient();
        this.font = new GCCoreSmallFontRenderer(mc.gameSettings, new ResourceLocation("textures/font/ascii.png"), mc.renderEngine, false);
        this.optionStrings = text;
        
        int largestString = Integer.MIN_VALUE;
        
        for (int i = 0; i < text.length; i++)
        {
            largestString = Math.max(largestString, mc.fontRenderer.getStringWidth(text[i]));
        }
        
        this.width = largestString + 4;
        this.selectedOption = 0;
    }

    @Override
    public void drawButton(Minecraft par1Minecraft, int par2, int par3)
    {
        if (this.drawButton)
        {
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            this.field_82253_i = par2 >= this.xPosition && par3 >= this.yPosition && par2 < this.xPosition + this.width && par3 < this.yPosition + this.height;
//            this.drawTexturedModalRect(this.xPosition, this.yPosition, this.isSelected ? 20 : 33, field_82253_i ? 24 : 37, this.width, this.height);
            Gui.drawRect(this.xPosition, this.yPosition, this.xPosition + this.width, this.yPosition + (this.dropdownClicked ? this.height * this.optionStrings.length : this.height), GCCoreUtil.convertTo32BitColor(255, 255, 255, 255));
            Gui.drawRect(this.xPosition + 1, this.yPosition + 1, this.xPosition + this.width - 1, this.yPosition + (this.dropdownClicked ? this.height * this.optionStrings.length : this.height) - 1, GCCoreUtil.convertTo32BitColor(255, 0, 0, 0));
            
            if (this.dropdownClicked && par2 >= this.xPosition && par3 >= this.yPosition && par2 < this.xPosition + this.width && par3 < this.yPosition + this.height * this.optionStrings.length)
            {
                int hoverPos = (par3 - this.yPosition) / this.height;
                Gui.drawRect(this.xPosition + 1, this.yPosition + this.height * hoverPos + 1, this.xPosition + this.width - 1, this.yPosition + this.height * (hoverPos + 1) - 1, GCCoreUtil.convertTo32BitColor(255, 100, 100, 100));
            }
            
            this.mouseDragged(par1Minecraft, par2, par3);
            
            if (this.dropdownClicked)
            {
                for (int i = 0; i < this.optionStrings.length; i++)
                {
//                    this.drawCenteredString(par1Minecraft.fontRenderer, this.optionStrings[i], this.xPosition + this.width / 2, (this.yPosition + (this.height - 8) / 2) + this.height * i, GCCoreUtil.convertTo32BitColor(255, 255, 255, 255));
                    this.font.drawStringWithShadow(this.optionStrings[i], this.xPosition + this.width / 2 - this.font.getStringWidth(this.optionStrings[i]) / 2, (this.yPosition + (this.height - 8) / 2) + this.height * i, GCCoreUtil.convertTo32BitColor(255, 255, 255, 255));
                }
            }
            else
            {
                this.font.drawStringWithShadow(this.optionStrings[this.selectedOption], this.xPosition + this.width / 2 - this.font.getStringWidth(this.optionStrings[this.selectedOption]) / 2, this.yPosition + (this.height - 8) / 2, GCCoreUtil.convertTo32BitColor(255, 255, 255, 255));
            }
        }
    }

    @Override
    public boolean mousePressed(Minecraft par1Minecraft, int par2, int par3)
    {
        if (!this.dropdownClicked)
        {
            if (this.enabled && this.drawButton && par2 >= this.xPosition && par3 >= this.yPosition && par2 < this.xPosition + this.width && par3 < this.yPosition + this.height)
            {
                this.dropdownClicked = true;
                return true;
            }
        }
        else
        {
            if (this.enabled && this.drawButton && par2 >= this.xPosition && par3 >= this.yPosition && par2 < this.xPosition + this.width && par3 < this.yPosition + this.height * this.optionStrings.length)
            {
                int optionClicked = (par3 - this.yPosition) / this.height;
                this.selectedOption = optionClicked % this.optionStrings.length;
                this.dropdownClicked = false;
                return true;
            }
            else
            {
                this.dropdownClicked = false;
            }
        }
        
        return false;
    }
}
