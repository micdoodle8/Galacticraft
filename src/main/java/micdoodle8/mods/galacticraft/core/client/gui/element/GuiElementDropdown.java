package micdoodle8.mods.galacticraft.core.client.gui.element;

import micdoodle8.mods.galacticraft.core.Constants;
import micdoodle8.mods.galacticraft.core.util.ColorUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

public class GuiElementDropdown extends Button
{
    protected static final ResourceLocation TEXTURE = new ResourceLocation(Constants.MOD_ID_CORE, "textures/gui/gui.png");

    public boolean dropdownClicked;
    public String[] optionStrings;
    public int selectedOption = -1;
    public FontRenderer font;
    private final IDropboxCallback parentGui;

    public GuiElementDropdown(IDropboxCallback parentClass, int x, int y, String... text)
    {
        super(x, y, 15, 15, "", (button) ->
        {
        });
        Minecraft minecraft = Minecraft.getInstance();
        this.parentGui = parentClass;
        this.font = minecraft.fontRenderer;
        this.optionStrings = text;

        int largestString = Integer.MIN_VALUE;

        for (String element : text)
        {
            largestString = Math.max(largestString, this.font.getStringWidth(element));
        }

        this.width = largestString + 8 + 15;
    }

    @Override
    public void renderButton(int par2, int par3, float partial)
    {
        if (this.selectedOption == -1)
        {
            this.selectedOption = this.parentGui.getInitialSelection(this);
        }

        if (this.visible)
        {
//            GL11.glDisable(GL12.GL_RESCALE_NORMAL);
//            RenderHelper.disableStandardItemLighting();
//            GL11.glDisable(GL11.GL_LIGHTING);
//            GL11.glDisable(GL11.GL_DEPTH_TEST);
            GL11.glPushMatrix();

            this.setBlitOffset(300);
            GL11.glTranslatef(0, 0, 500);
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            this.isHovered = par2 >= this.x && par3 >= this.y && par2 < this.x + this.width && par3 < this.y + this.height;
            AbstractGui.fill(this.x, this.y, this.x + this.width - 15, this.y + (this.dropdownClicked ? this.height * this.optionStrings.length : this.height), ColorUtil.to32BitColor(255, 0, 0, 0));
            AbstractGui.fill(this.x + 1, this.y + 1, this.x + this.width - 16, this.y + (this.dropdownClicked ? this.height * this.optionStrings.length : this.height) - 1, ColorUtil.to32BitColor(255, 150, 150, 150));
            AbstractGui.fill(this.x + this.width - 15, this.y, this.x + this.width - 1, this.y + this.height, ColorUtil.to32BitColor(255, 0, 0, 0));
            AbstractGui.fill(this.x + this.width - 15, this.y + 1, this.x + this.width - 2, this.y + this.height - 1, ColorUtil.to32BitColor(255, 150, 150, 150));

            if (this.dropdownClicked && par2 >= this.x && par3 >= this.y && par2 < this.x + this.width && par3 < this.y + this.height * this.optionStrings.length)
            {
                int hoverPos = (par3 - this.y) / this.height;
                AbstractGui.fill(this.x + 1, this.y + this.height * hoverPos + 1, this.x + this.width - 16, this.y + this.height * (hoverPos + 1) - 1, ColorUtil.to32BitColor(255, 175, 175, 175));
            }

            if (this.dropdownClicked)
            {
                for (int i = 0; i < this.optionStrings.length; i++)
                {
                    this.font.drawStringWithShadow(this.optionStrings[i], this.x + this.width / 2 - 7 - this.font.getStringWidth(this.optionStrings[i]) / 2, this.y + (this.height - 6) / 2 + this.height * i, ColorUtil.to32BitColor(255, 255, 255, 255));
                }
            }
            else
            {
                this.font.drawStringWithShadow(this.optionStrings[this.selectedOption], this.x + this.width / 2 - 7 - this.font.getStringWidth(this.optionStrings[this.selectedOption]) / 2, this.y + (this.height - 6) / 2, ColorUtil.to32BitColor(255, 255, 255, 255));
            }

            Minecraft minecraft = Minecraft.getInstance();
            minecraft.textureManager.bindTexture(TEXTURE);
            this.blit(this.x + this.width - 12, this.y + 5, 185, 0, 7, 4);

            GL11.glPopMatrix();
            this.setBlitOffset(0);
//            GL11.glEnable(GL11.GL_LIGHTING);
//            GL11.glEnable(GL11.GL_DEPTH_TEST);
//            RenderHelper.enableStandardItemLighting();
//            GL11.glEnable(GL12.GL_RESCALE_NORMAL);
        }
    }

    @Override
    protected boolean clicked(double p_clicked_1_, double p_clicked_3_)
    {
        if (this.active && this.visible && p_clicked_1_ >= (double) this.x && p_clicked_3_ >= (double) this.y && p_clicked_1_ < (double) (this.x + this.width) && p_clicked_3_ < (double) (this.y + this.height * (dropdownClicked ? this.optionStrings.length : 1.0F)))
        {
            boolean canInteract = this.parentGui.canBeClickedBy(this, Minecraft.getInstance().player);
            if (!canInteract)
            {
                this.parentGui.onIntruderInteraction();
            }
            else
            {
                return true;
            }
        }
        this.dropdownClicked = false;
        return false;
    }

    @Override
    public void onClick(double mouseX, double mouseY)
    {
        if (!dropdownClicked)
        {
            this.dropdownClicked = true;
        }
        else
        {
            int optionClicked = (((int) mouseY) - this.y) / this.height;
            this.selectedOption = optionClicked % this.optionStrings.length;
            this.dropdownClicked = false;
            this.parentGui.onSelectionChanged(this, this.selectedOption);
        }
    }

    public interface IDropboxCallback
    {
        boolean canBeClickedBy(GuiElementDropdown dropdown, PlayerEntity player);

        void onSelectionChanged(GuiElementDropdown dropdown, int selection);

        int getInitialSelection(GuiElementDropdown dropdown);

        void onIntruderInteraction();
    }
}
