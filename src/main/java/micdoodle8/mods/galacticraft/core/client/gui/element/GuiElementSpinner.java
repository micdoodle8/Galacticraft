package micdoodle8.mods.galacticraft.core.client.gui.element;

import micdoodle8.mods.galacticraft.core.Constants;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

public class GuiElementSpinner extends GuiButton
{
    protected static final ResourceLocation texture = new ResourceLocation(Constants.ASSET_PREFIX, "textures/gui/gui.png");
    private ISpinnerCallback parentGui;
    private int textColor;
    private boolean hoverUpper;
    private boolean hoverLower;
    public Integer value;

    public GuiElementSpinner(int id, ISpinnerCallback parentGui, int x, int y)
    {
        this(id, parentGui, x, y, 4210752);
    }

    public GuiElementSpinner(int id, ISpinnerCallback parentGui, int x, int y, int textColor)
    {
        super(id, x, y, 31, 20, "");
        this.parentGui = parentGui;
        this.textColor = textColor;
    }

    @Override
    public void drawButton(Minecraft mc, int mX, int mY, float partial)
    {
        if (this.value == null)
        {
            this.value = this.parentGui.getInitialValue(this);
        }

        if (this.visible)
        {
            mc.getTextureManager().bindTexture(GuiElementSpinner.texture);
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            this.hovered = mX >= this.x && mY >= this.y && mX < this.x + this.width && mY < this.y + this.height;
            this.hoverUpper = mX >= this.x + 20 && mX < this.x + width && mY > this.y && mY < this.y + this.height / 2;
            this.hoverLower = mX >= this.x + 20 && mX < this.x + width && mY > this.y + this.height / 2 && mY < this.y + this.height;
            this.drawTexturedModalRect(this.x, this.y, 214, 0, 20, this.height);
            this.drawTexturedModalRect(this.x + 20, this.y, this.hoverUpper ? 245 : 234, 0, 11, 10);
            this.drawTexturedModalRect(this.x + 20, this.y + 10, this.hoverLower ? 245 : 234, 10, 11, 10);
//            this.mouseDragged(mc, mX, mY);
            mc.fontRenderer.drawString(this.value.toString(), this.x + 11 - (mc.fontRenderer.getStringWidth(this.value.toString()) >> 1), this.y + (this.height - 6) / 2, this.textColor, false);
        }
    }

    public void increase()
    {
        this.parentGui.onSelectionChanged(this, this.value + 1);
        this.value++;
    }

    public void decrease()
    {
        this.parentGui.onSelectionChanged(this, this.value - 1);
        this.value--;
    }

    @Override
    public boolean mousePressed(Minecraft mc, int mX, int mY)
    {
        if (this.enabled && this.visible)
        {
            if (mX >= this.x + 20 && mX < this.x + width && mY > this.y && mY < this.y + this.height / 2)
            {
                if (this.parentGui.canPlayerEdit(this, mc.player))
                {
                    increase();
                    return true;
                }
            }

            if (mX >= this.x + 20 && mX < this.x + width && mY > this.y + this.height / 2 && mY < this.y + this.height)
            {
                if (this.parentGui.canPlayerEdit(this, mc.player))
                {
                    decrease();
                    return true;
                }
            }
        }

        return false;
    }

    public interface ISpinnerCallback
    {
        void onSelectionChanged(GuiElementSpinner spinner, int newVal);

        boolean canPlayerEdit(GuiElementSpinner spinner, EntityPlayer player);

        int getInitialValue(GuiElementSpinner spinner);

        void onIntruderInteraction();
    }
}
