package micdoodle8.mods.galacticraft.core.client.gui.element;

import micdoodle8.mods.galacticraft.core.util.ColorUtil;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.input.Mouse;

import java.util.ArrayList;
import java.util.List;

@SideOnly(Side.CLIENT)
public class GuiElementGradientList extends Gui
{
    public static class ListElement
    {
        public String value;
        public int color;

        public ListElement(String value, int color)
        {
            this.value = value;
            this.color = color;
        }
    }

    public static final int BUTTON_HEIGHT = 20;
    public int width;
    public int height;
    public int xPosition;
    public int yPosition;
    public List<ListElement> listContents = new ArrayList<ListElement>();
    public int sliderPos = this.yPosition + 1;
    private int lastMousePosY;
    private boolean sliderGrabbed;
    private boolean sliderEnabled;
    private int selectedIndex = -1;

    public GuiElementGradientList(int xPos, int yPos, int width, int height)
    {
        this.xPosition = xPos;
        this.yPosition = yPos;
        this.width = width;
        this.height = height;
    }

    public void updateListContents(List<ListElement> newContents)
    {
        this.listContents = newContents;
        this.sliderEnabled = this.listContents.size() * GuiElementGradientList.BUTTON_HEIGHT > this.height;
        if (this.selectedIndex >= this.listContents.size())
        {
            this.selectedIndex = -1;
        }
    }

    public void draw(int mousePosX, int mousePosY)
    {
        if (this.sliderEnabled)
        {
            if (this.sliderGrabbed || mousePosX >= this.xPosition + this.width - 9 && mousePosX < this.xPosition + this.width && mousePosY >= this.yPosition && mousePosY < this.yPosition + this.height)
            {
                if (Mouse.isButtonDown(0))
                {
                    this.sliderGrabbed = true;

                    if (this.lastMousePosY > 0)
                    {
                        if (mousePosY >= this.sliderPos && mousePosY < this.sliderPos + 15)
                        {
                            int deltaY = this.lastMousePosY - this.sliderPos;
                            this.sliderPos = mousePosY - deltaY;
                        }
                        else
                        {
                            this.sliderPos = mousePosY - 7;
                        }
                    }

                    this.lastMousePosY = mousePosY;
                }
                else
                {
                    this.sliderGrabbed = false;
                }
            }
            else
            {
                this.lastMousePosY = 0;
            }
        }

        if (Mouse.isButtonDown(0))
        {
            if (mousePosX >= this.xPosition && mousePosX < this.xPosition + this.width - 10 && mousePosY >= this.yPosition && mousePosY < this.yPosition + this.height)
            {
                int clickPosY = mousePosY - this.yPosition + (int) Math.floor((this.listContents.size() * GuiElementGradientList.BUTTON_HEIGHT - this.height) * this.getSliderPercentage());
                this.selectedIndex = clickPosY / GuiElementGradientList.BUTTON_HEIGHT;

                if (this.selectedIndex < 0 || this.selectedIndex >= this.listContents.size())
                {
                    this.selectedIndex = -1;
                }
            }
        }

        this.sliderPos = Math.min(Math.max(this.yPosition, this.sliderPos), this.yPosition + this.height - 15);

        this.drawGradientRect(this.xPosition, this.yPosition, this.xPosition + this.width - 10, this.yPosition + this.height, ColorUtil.to32BitColor(255, 30, 30, 30), ColorUtil.to32BitColor(255, 30, 30, 30));
        this.drawGradientRect(this.xPosition + this.width - 9, this.yPosition, this.xPosition + this.width, this.yPosition + this.height, ColorUtil.to32BitColor(255, 50, 50, 50), ColorUtil.to32BitColor(255, 50, 50, 50));
        int sliderColor = this.sliderEnabled ? ColorUtil.to32BitColor(255, 90, 90, 90) : ColorUtil.to32BitColor(255, 40, 40, 40);
        this.drawGradientRect(this.xPosition + this.width - 9, this.sliderPos, this.xPosition + this.width, this.sliderPos + 15, sliderColor, sliderColor);

        Gui.drawRect(this.xPosition + this.width - 1, this.yPosition, this.xPosition + this.width, this.yPosition + this.height, ColorUtil.to32BitColor(255, 0, 0, 0));
        Gui.drawRect(this.xPosition + this.width - 10, this.yPosition, this.xPosition + this.width - 9, this.yPosition + this.height, ColorUtil.to32BitColor(255, 0, 0, 0));
        Gui.drawRect(this.xPosition, this.yPosition, this.xPosition + 1, this.yPosition + this.height, ColorUtil.to32BitColor(255, 0, 0, 0));
        Gui.drawRect(this.xPosition, this.yPosition, this.xPosition + this.width, this.yPosition + 1, ColorUtil.to32BitColor(255, 0, 0, 0));
        Gui.drawRect(this.xPosition, this.yPosition + this.height - 1, this.xPosition + this.width, this.yPosition + this.height, ColorUtil.to32BitColor(255, 0, 0, 0));

        sliderColor = this.sliderEnabled ? ColorUtil.to32BitColor(255, 120, 120, 120) : ColorUtil.to32BitColor(255, 60, 60, 60);
        Gui.drawRect(this.xPosition + this.width - 9, this.sliderPos + 1, this.xPosition + this.width - 8, this.sliderPos + 14, sliderColor);
        Gui.drawRect(this.xPosition + this.width - 2, this.sliderPos + 1, this.xPosition + this.width - 1, this.sliderPos + 14, sliderColor);
        Gui.drawRect(this.xPosition + this.width - 9, this.sliderPos, this.xPosition + this.width - 1, this.sliderPos + 1, sliderColor);
        Gui.drawRect(this.xPosition + this.width - 9, this.sliderPos + 15, this.xPosition + this.width - 1, this.sliderPos + 14, sliderColor);

        int currentDrawHeight = this.yPosition + 1 - (int) Math.floor((this.listContents.size() * GuiElementGradientList.BUTTON_HEIGHT - this.height) * this.getSliderPercentage());
        FontRenderer fontRenderer = FMLClientHandler.instance().getClient().fontRendererObj;

        for (int i = 0; i < this.listContents.size(); i++)
        {
            ListElement displayButton = this.listContents.get(i);

            if (displayButton != null && displayButton.value != null && !displayButton.value.isEmpty())
            {
                int yCoord0 = currentDrawHeight;
                int yCoord1 = currentDrawHeight + GuiElementGradientList.BUTTON_HEIGHT - 1;

                if (yCoord1 > this.yPosition && yCoord0 < this.yPosition + this.height)
                {
                    yCoord0 = Math.max(this.yPosition + 1, yCoord0);
                    yCoord1 = Math.min(this.yPosition + this.height - 1, yCoord1);

                    int color = i == this.selectedIndex ? ColorUtil.to32BitColor(255, 35, 35, 35) : ColorUtil.to32BitColor(255, 25, 25, 25);

                    Gui.drawRect(this.xPosition + 1, yCoord0, this.xPosition + this.width - 10, yCoord1, color);

                    if (currentDrawHeight + GuiElementGradientList.BUTTON_HEIGHT / 2 - fontRenderer.FONT_HEIGHT / 2 > this.yPosition && currentDrawHeight + GuiElementGradientList.BUTTON_HEIGHT / 2 + fontRenderer.FONT_HEIGHT / 2 < this.yPosition + this.height)
                    {
                        fontRenderer.drawString(displayButton.value, this.xPosition + (this.width - 10) / 2 - fontRenderer.getStringWidth(displayButton.value) / 2, currentDrawHeight + GuiElementGradientList.BUTTON_HEIGHT / 2 - fontRenderer.FONT_HEIGHT / 2, displayButton.color);
                    }

                }

                currentDrawHeight += GuiElementGradientList.BUTTON_HEIGHT;
            }
        }
    }

    public void update()
    {
    }

    private float getSliderPercentage()
    {
        if (!this.sliderEnabled)
        {
            return 0.0F;
        }

        return (this.sliderPos - this.yPosition) / (float) (this.height - 15);
    }

    public ListElement getSelectedElement()
    {
        if (this.selectedIndex == -1)
        {
            return null;
        }

        return this.listContents.get(this.selectedIndex);
    }
}
