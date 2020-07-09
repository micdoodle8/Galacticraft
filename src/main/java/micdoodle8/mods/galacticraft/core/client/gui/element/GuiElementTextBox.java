package micdoodle8.mods.galacticraft.core.client.gui.element;

import micdoodle8.mods.galacticraft.core.util.ColorUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.Widget;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.SharedConstants;
import org.lwjgl.glfw.GLFW;

public class GuiElementTextBox extends Widget
{
    public String text;
    public boolean numericOnly;
    public boolean centered;
    private final int maxLength;

    public long backspacePressedMillis;
    public int cursorPulse;
    public int backspacePressedTicks;
    public boolean isTextFocused = false;
    public int incorrectUseTimer;
    public boolean resetOnClick = true;

    private final ITextBoxCallback parentGui;

    private final Minecraft minecraft = Minecraft.getInstance();

    public GuiElementTextBox(ITextBoxCallback parentGui, int x, int y, int width, int height, String initialText, boolean numericOnly, int maxLength, boolean centered)
    {
        super(x, y, width, height, initialText);
        this.parentGui = parentGui;
        this.numericOnly = numericOnly;
        this.maxLength = maxLength;
        this.centered = centered;
    }

    @Override
    public boolean charTyped(char character, int modifiers)
    {
        if (SharedConstants.isAllowedCharacter(character))
        {
            if (this.active)
            {
                if (this.parentGui.canPlayerEdit(this, this.minecraft.player))
                {
                    this.text = this.text + character;
                    this.text = this.text.substring(0, Math.min(this.text.length(), this.maxLength));
                }
                else
                {
                    this.incorrectUseTimer = 10;
                    this.parentGui.onIntruderInteraction(this);
                }
            }

            return true;
        }
        else
        {
            return false;
        }
    }

    @Override
    public boolean keyPressed(int key, int scanCode, int modifiers)
    {
        if (this.isTextFocused)
        {
            String prevText = this.text;
            if (key == GLFW.GLFW_KEY_BACKSPACE)
            {
                backspaceHeld = true;
                if (this.text.length() > 0)
                {
                    if (this.parentGui.canPlayerEdit(this, this.minecraft.player))
                    {
                        String toBeParsed = this.text.substring(0, this.text.length() - 1);

                        if (this.isValid(toBeParsed))
                        {
                            this.text = toBeParsed;
                            this.backspacePressedMillis = System.currentTimeMillis();
                        }
                        else
                        {
                            this.text = "";
                        }
                    }
                    else
                    {
                        this.incorrectUseTimer = 10;
                        this.parentGui.onIntruderInteraction(this);
                    }
                }
            }
            else if (Screen.isPaste(key))
            {
                String pastestring = Minecraft.getInstance().keyboardListener.getClipboardString();

                if (this.isValid(this.text + pastestring))
                {
                    if (this.parentGui.canPlayerEdit(this, this.minecraft.player))
                    {
                        this.text = this.text + pastestring;
                        this.text = this.text.substring(0, Math.min(this.text.length(), this.maxLength));
                    }
                    else
                    {
                        this.incorrectUseTimer = 10;
                        this.parentGui.onIntruderInteraction(this);
                    }
                }
            }

            if (!prevText.equals(text))
            {
                this.parentGui.onTextChanged(this, this.text);
            }
            return true;
        }

        return false;
    }

    private boolean backspaceHeld = false;

    @Override
    public boolean keyReleased(int key, int scanCode, int modifiers)
    {
        if (key == GLFW.GLFW_KEY_BACKSPACE)
        {
            backspaceHeld = false;
            return true;
        }

        return super.keyReleased(key, scanCode, modifiers);
    }

    @Override
    public void render(int mouseX, int mouseY, float partial)
    {
        if (this.text == null)
        {
            this.text = this.parentGui.getInitialText(this);
            this.parentGui.onTextChanged(this, this.text);
        }

        if (this.visible)
        {
            AbstractGui.fill(this.x, this.y, this.x + this.width, this.y + this.height, ColorUtil.to32BitColor(140, 140, 140, 140));
            AbstractGui.fill(this.x + 1, this.y + 1, this.x + this.width - 1, this.y + this.height - 1, ColorUtil.to32BitColor(255, 0, 0, 0));

            this.cursorPulse++;

            if (this.backspacePressedMillis > 0)
            {
                if (backspaceHeld && this.text.length() > 0)
                {
                    if (this.text.length() > 0)
                    {
                        if (System.currentTimeMillis() - this.backspacePressedMillis > 200 / (1 + this.backspacePressedTicks * 0.3F) && this.parentGui.canPlayerEdit(this, this.minecraft.player))
                        {
                            String toBeParsed = this.text.substring(0, this.text.length() - 1);

                            if (this.isValid(toBeParsed))
                            {
                                this.text = toBeParsed;
                                this.parentGui.onTextChanged(this, this.text);
                            }
                            else
                            {
                                this.text = "";
                            }

                            this.backspacePressedMillis = System.currentTimeMillis();
                            this.backspacePressedTicks++;
                        }
                        else if (!this.parentGui.canPlayerEdit(this, this.minecraft.player))
                        {
                            this.incorrectUseTimer = 10;
                            this.parentGui.onIntruderInteraction(this);
                        }
                    }
                }
                else
                {
                    this.backspacePressedMillis = 0;
                    this.backspacePressedTicks = 0;
                }
            }

            if (this.incorrectUseTimer > 0)
            {
                this.incorrectUseTimer--;
            }

            int xPos = this.x + 4;

            if (this.centered)
            {
                xPos = this.x + this.width / 2 - this.minecraft.fontRenderer.getStringWidth(this.text) / 2;
            }

            this.drawString(this.minecraft.fontRenderer, this.text + (this.cursorPulse / 24 % 2 == 0 && this.isTextFocused ? "_" : ""), xPos, this.y + this.height / 2 - 4, this.incorrectUseTimer > 0 ? ColorUtil.to32BitColor(255, 255, 20, 20) : this.parentGui.getTextColor(this));
        }
    }

    public int getIntegerValue()
    {
        try
        {
            return Integer.parseInt(this.text.equals("") ? "0" : this.text);
        }
        catch (Exception e)
        {
            return -1;
        }
    }

    public boolean isValid(String string)
    {
        if (this.numericOnly)
        {
            if (string.length() > 0 && SharedConstants.isAllowedCharacter(string.charAt(string.length() - 1)))
            {
                try
                {
                    Integer.parseInt(string);
                    return true;
                }
                catch (Exception e)
                {
                    return false;
                }
            }
            else
            {
                return false;
            }
        }
        else
        {
            if (string.length() <= 0)
            {
                return false;
            }

            return SharedConstants.isAllowedCharacter(string.charAt(string.length() - 1));
        }
    }

    @Override
    protected boolean clicked(double mouseX, double mouseY)
    {
        if (super.clicked(mouseX, mouseY))
        {
            AbstractGui.fill(this.x, this.y, this.x + this.width, this.y + this.height, 0xffA0A0A0);
            this.isTextFocused = true;
            if (resetOnClick)
            {
                this.text = this.parentGui.getInitialText(this);
            }
            this.parentGui.onTextChanged(this, this.text);
            return true;
        }
        else
        {
            this.isTextFocused = false;
            return false;
        }
    }

    public int getMaxLength()
    {
        return maxLength;
    }

    public interface ITextBoxCallback
    {
        boolean canPlayerEdit(GuiElementTextBox textBox, PlayerEntity player);

        void onTextChanged(GuiElementTextBox textBox, String newText);

        String getInitialText(GuiElementTextBox textBox);

        int getTextColor(GuiElementTextBox textBox);

        void onIntruderInteraction(GuiElementTextBox textBox);
    }
}
