package micdoodle8.mods.galacticraft.core.client.gui.element;

import cpw.mods.fml.client.FMLClientHandler;
import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ChatAllowedCharacters;
import org.lwjgl.input.Keyboard;

public class GuiElementTextBox extends GuiButton
{
    public String text;
    public boolean numericOnly;
    public boolean centered;
    private int maxLength;

    public long timeBackspacePressed;
    public int cursorPulse;
    public int backspacePressed;
    public boolean isTextFocused = false;
    public int incorrectUseTimer;

    private ITextBoxCallback parentGui;

    private Minecraft mc = FMLClientHandler.instance().getClient();

    public GuiElementTextBox(int id, ITextBoxCallback parentGui, int x, int y, int width, int height, String initialText, boolean numericOnly, int maxLength, boolean centered)
    {
        super(id, x, y, width, height, initialText);
        this.parentGui = parentGui;
        this.numericOnly = numericOnly;
        this.maxLength = maxLength;
        this.centered = centered;
    }

    /**
     * Call this from the parent GUI class in keyTyped.
     */
    public boolean keyTyped(char keyChar, int keyID)
    {
        if (this.isTextFocused)
        {
            if (keyID == Keyboard.KEY_BACK)
            {
                if (this.text.length() > 0)
                {
                    if (this.parentGui.canPlayerEdit(this, this.mc.thePlayer))
                    {
                        String toBeParsed = this.text.substring(0, this.text.length() - 1);

                        if (this.isValid(toBeParsed))
                        {
                            this.text = toBeParsed;
                            this.timeBackspacePressed = System.currentTimeMillis();
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
            else if (keyChar == 22)
            {
                String pastestring = GuiScreen.getClipboardString();

                if (pastestring == null)
                {
                    pastestring = "";
                }

                if (this.isValid(this.text + pastestring))
                {
                    if (this.parentGui.canPlayerEdit(this, this.mc.thePlayer))
                    {
                        this.text = this.text + pastestring;
                        this.text = this.text.substring(0, Math.min(String.valueOf(this.text).length(), this.maxLength));
                    }
                    else
                    {
                        this.incorrectUseTimer = 10;
                        this.parentGui.onIntruderInteraction(this);
                    }
                }
            }
            else if (this.isValid(this.text + keyChar))
            {
                if (this.parentGui.canPlayerEdit(this, this.mc.thePlayer))
                {
                    this.text = this.text + keyChar;
                    this.text = this.text.substring(0, Math.min(this.text.length(), this.maxLength));
                }
                else
                {
                    this.incorrectUseTimer = 10;
                    this.parentGui.onIntruderInteraction(this);
                }
            }

            this.parentGui.onTextChanged(this, this.text);
            return true;
        }

        return false;
    }

    @Override
    public void drawButton(Minecraft par1Minecraft, int par2, int par3)
    {
        if (this.text == null)
        {
            this.text = this.parentGui.getInitialText(this);
            this.parentGui.onTextChanged(this, this.text);
        }

        if (this.visible)
        {
            Gui.drawRect(this.xPosition, this.yPosition, this.xPosition + this.width, this.yPosition + this.height, GCCoreUtil.to32BitColor(140, 140, 140, 140));
            Gui.drawRect(this.xPosition + 1, this.yPosition + 1, this.xPosition + this.width - 1, this.yPosition + this.height - 1, GCCoreUtil.to32BitColor(255, 0, 0, 0));

            this.cursorPulse++;

            if (this.timeBackspacePressed > 0)
            {
                if (Keyboard.isKeyDown(Keyboard.KEY_BACK) && this.text.length() > 0)
                {
                    if (System.currentTimeMillis() - this.timeBackspacePressed > 200 / (1 + this.backspacePressed * 0.3F) && this.parentGui.canPlayerEdit(this, this.mc.thePlayer))
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

                        this.timeBackspacePressed = System.currentTimeMillis();
                        this.backspacePressed++;
                    }
                    else if (!this.parentGui.canPlayerEdit(this, this.mc.thePlayer))
                    {
                        this.incorrectUseTimer = 10;
                        this.parentGui.onIntruderInteraction(this);
                    }
                }
                else
                {
                    this.timeBackspacePressed = 0;
                    this.backspacePressed = 0;
                }
            }

            if (this.incorrectUseTimer > 0)
            {
                this.incorrectUseTimer--;
            }

            int xPos = this.xPosition + 4;

            if (this.centered)
            {
                xPos = this.xPosition + this.width / 2 - this.mc.fontRenderer.getStringWidth(this.text) / 2;
            }

            this.drawString(this.mc.fontRenderer, this.text + (this.cursorPulse / 24 % 2 == 0 && this.isTextFocused ? "_" : ""), xPos, this.yPosition + this.height / 2 - 4, this.incorrectUseTimer > 0 ? GCCoreUtil.to32BitColor(255, 255, 20, 20) : this.parentGui.getTextColor(this));
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
            if (string.length() > 0 && ChatAllowedCharacters.isAllowedCharacter(string.charAt(string.length() - 1)))
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

            return ChatAllowedCharacters.isAllowedCharacter(string.charAt(string.length() - 1));
        }
    }

    @Override
    public boolean mousePressed(Minecraft par1Minecraft, int par2, int par3)
    {
        if (super.mousePressed(par1Minecraft, par2, par3))
        {
            Gui.drawRect(this.xPosition, this.yPosition, this.xPosition + this.width, this.yPosition + this.height, 0xffA0A0A0);
            this.isTextFocused = true;
            this.text = this.parentGui.getInitialText(this);
            this.parentGui.onTextChanged(this, this.text);
            return true;
        }
        else
        {
            this.isTextFocused = false;
            return false;
        }
    }

    public int getMaxLength() {
        return maxLength;
    }

    public static interface ITextBoxCallback
    {
        public boolean canPlayerEdit(GuiElementTextBox textBox, EntityPlayer player);

        public void onTextChanged(GuiElementTextBox textBox, String newText);

        public String getInitialText(GuiElementTextBox textBox);

        public int getTextColor(GuiElementTextBox textBox);

        public void onIntruderInteraction(GuiElementTextBox textBox);
    }
}
