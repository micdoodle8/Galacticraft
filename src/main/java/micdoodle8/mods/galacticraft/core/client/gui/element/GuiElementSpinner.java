package micdoodle8.mods.galacticraft.core.client.gui.element;

import com.mojang.blaze3d.systems.RenderSystem;
import micdoodle8.mods.galacticraft.core.Constants;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ResourceLocation;

public class GuiElementSpinner extends Button
{
    protected static final ResourceLocation texture = new ResourceLocation(Constants.MOD_ID_CORE, "textures/gui/gui.png");
    private final ISpinnerCallback parentGui;
    private final int textColor;
    private boolean hoverUpper;
    private boolean hoverLower;
    public Integer value;

    public GuiElementSpinner(ISpinnerCallback parentGui, int x, int y)
    {
        this(parentGui, x, y, 4210752);
    }

    public GuiElementSpinner(ISpinnerCallback parentGui, int x, int y, int textColor)
    {
        super(x, y, 31, 20, "", (button) ->
        {
        });
        this.parentGui = parentGui;
        this.textColor = textColor;
    }

    @Override
    public void renderButton(int mouseX, int mouseY, float partial)
    {
        if (this.value == null)
        {
            this.value = this.parentGui.getInitialValue(this);
        }

        if (this.visible)
        {
            Minecraft minecraft = Minecraft.getInstance();
            minecraft.getTextureManager().bindTexture(GuiElementSpinner.texture);
            RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
            this.isHovered = mouseX >= this.x && mouseY >= this.y && mouseX < this.x + this.width && mouseY < this.y + this.height;
            this.hoverUpper = mouseX >= this.x + 20 && mouseX < this.x + width && mouseY > this.y && mouseY < this.y + this.height / 2;
            this.hoverLower = mouseX >= this.x + 20 && mouseX < this.x + width && mouseY > this.y + this.height / 2 && mouseY < this.y + this.height;
            this.blit(this.x, this.y, 214, 0, 20, this.height);
            this.blit(this.x + 20, this.y, this.hoverUpper ? 245 : 234, 0, 11, 10);
            this.blit(this.x + 20, this.y + 10, this.hoverLower ? 245 : 234, 10, 11, 10);
//            this.mouseDragged(minecraft, mX, mY);
            minecraft.fontRenderer.drawString(this.value.toString(), this.x + 11 - (minecraft.fontRenderer.getStringWidth(this.value.toString()) >> 1), this.y + (this.height - 6) / 2, this.textColor);
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
    protected boolean clicked(double mouseX, double mouseY)
    {
        Minecraft minecraft = Minecraft.getInstance();
        if (this.active && this.visible)
        {
            if (mouseX >= this.x + 20 && mouseX < this.x + width && mouseY > this.y && mouseY < this.y + this.height / 2)
            {
                if (this.parentGui.canPlayerEdit(this, minecraft.player))
                {
                    increase();
                    return true;
                }
            }

            if (mouseX >= this.x + 20 && mouseX < this.x + width && mouseY > this.y + this.height / 2 && mouseY < this.y + this.height)
            {
                if (this.parentGui.canPlayerEdit(this, minecraft.player))
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

        boolean canPlayerEdit(GuiElementSpinner spinner, PlayerEntity player);

        int getInitialValue(GuiElementSpinner spinner);

        void onIntruderInteraction();
    }
}
