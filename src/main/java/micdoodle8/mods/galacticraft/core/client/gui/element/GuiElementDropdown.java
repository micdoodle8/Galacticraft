package micdoodle8.mods.galacticraft.core.client.gui.element;

import cpw.mods.fml.client.FMLClientHandler;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.client.gui.screen.SmallFontRenderer;
import micdoodle8.mods.galacticraft.core.util.ColorUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

public class GuiElementDropdown extends GuiButton
{
    protected static final ResourceLocation texture = new ResourceLocation(GalacticraftCore.ASSET_PREFIX, "textures/gui/gui.png");

    public boolean dropdownClicked;
    public String[] optionStrings;
    public int selectedOption = -1;
    public SmallFontRenderer font;
    private IDropboxCallback parentClass;

    public GuiElementDropdown(int id, IDropboxCallback parentClass, int x, int y, String... text)
    {
        super(id, x, y, 13, 13, "");
        Minecraft mc = FMLClientHandler.instance().getClient();
        this.parentClass = parentClass;
        this.font = new SmallFontRenderer(mc.gameSettings, new ResourceLocation("textures/font/ascii.png"), mc.renderEngine, false);
        this.optionStrings = text;

        int largestString = Integer.MIN_VALUE;

        for (String element : text)
        {
            largestString = Math.max(largestString, this.font.getStringWidth(element));
        }

        this.width = largestString + 8;
    }

    @Override
    public void drawButton(Minecraft par1Minecraft, int par2, int par3)
    {
        if (this.selectedOption == -1)
        {
            this.selectedOption = this.parentClass.getInitialSelection(this);
        }

        if (this.visible)
        {
//            GL11.glDisable(GL12.GL_RESCALE_NORMAL);
//            RenderHelper.disableStandardItemLighting();
//            GL11.glDisable(GL11.GL_LIGHTING);
//            GL11.glDisable(GL11.GL_DEPTH_TEST);
            GL11.glPushMatrix();

            this.zLevel = 300.0F;
            GL11.glTranslatef(0, 0, 500);
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            this.field_146123_n = par2 >= this.xPosition && par3 >= this.yPosition && par2 < this.xPosition + this.width && par3 < this.yPosition + this.height;
            Gui.drawRect(this.xPosition, this.yPosition, this.xPosition + this.width, this.yPosition + (this.dropdownClicked ? this.height * this.optionStrings.length : this.height), ColorUtil.to32BitColor(255, 200, 200, 200));
            Gui.drawRect(this.xPosition + 1, this.yPosition + 1, this.xPosition + this.width - 1, this.yPosition + (this.dropdownClicked ? this.height * this.optionStrings.length : this.height) - 1, ColorUtil.to32BitColor(255, 0, 0, 0));

            if (this.dropdownClicked && par2 >= this.xPosition && par3 >= this.yPosition && par2 < this.xPosition + this.width && par3 < this.yPosition + this.height * this.optionStrings.length)
            {
                int hoverPos = (par3 - this.yPosition) / this.height;
                Gui.drawRect(this.xPosition + 1, this.yPosition + this.height * hoverPos + 1, this.xPosition + this.width - 1, this.yPosition + this.height * (hoverPos + 1) - 1, ColorUtil.to32BitColor(255, 100, 100, 100));
            }

            this.mouseDragged(par1Minecraft, par2, par3);

            if (this.dropdownClicked)
            {
                for (int i = 0; i < this.optionStrings.length; i++)
                {
                    this.font.drawStringWithShadow(this.optionStrings[i], this.xPosition + this.width / 2 - this.font.getStringWidth(this.optionStrings[i]) / 2, this.yPosition + (this.height - 8) / 2 + this.height * i, ColorUtil.to32BitColor(255, 255, 255, 255));
                }
            }
            else
            {
                this.font.drawStringWithShadow(this.optionStrings[this.selectedOption], this.xPosition + this.width / 2 - this.font.getStringWidth(this.optionStrings[this.selectedOption]) / 2, this.yPosition + (this.height - 8) / 2, ColorUtil.to32BitColor(255, 255, 255, 255));
            }

            GL11.glPopMatrix();
            this.zLevel = 0.0F;
//            GL11.glEnable(GL11.GL_LIGHTING);
//            GL11.glEnable(GL11.GL_DEPTH_TEST);
//            RenderHelper.enableStandardItemLighting();
//            GL11.glEnable(GL12.GL_RESCALE_NORMAL);
        }
    }

    @Override
    public boolean mousePressed(Minecraft par1Minecraft, int par2, int par3)
    {
        if (!this.dropdownClicked)
        {
            if (this.enabled && this.visible && par2 >= this.xPosition && par3 >= this.yPosition && par2 < this.xPosition + this.width && par3 < this.yPosition + this.height)
            {
                if (this.parentClass.canBeClickedBy(this, par1Minecraft.thePlayer))
                {
                    this.dropdownClicked = true;
                    return true;
                }
                else
                {
                    this.parentClass.onIntruderInteraction();
                }
            }
        }
        else
        {
            if (this.enabled && this.visible && par2 >= this.xPosition && par3 >= this.yPosition && par2 < this.xPosition + this.width && par3 < this.yPosition + this.height * this.optionStrings.length)
            {
                if (this.parentClass.canBeClickedBy(this, par1Minecraft.thePlayer))
                {
                    int optionClicked = (par3 - this.yPosition) / this.height;
                    this.selectedOption = optionClicked % this.optionStrings.length;
                    this.dropdownClicked = false;
                    this.parentClass.onSelectionChanged(this, this.selectedOption);
                    return true;
                }
                else
                {
                    this.parentClass.onIntruderInteraction();
                }
            }
            else
            {
                this.dropdownClicked = false;
            }
        }

        return false;
    }

    public static interface IDropboxCallback
    {
        public boolean canBeClickedBy(GuiElementDropdown dropdown, EntityPlayer player);

        public void onSelectionChanged(GuiElementDropdown dropdown, int selection);

        public int getInitialSelection(GuiElementDropdown dropdown);

        public void onIntruderInteraction();
    }
}
