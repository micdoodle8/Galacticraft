package codechicken.core.gui;

import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.ChatAllowedCharacters;

import org.lwjgl.input.Keyboard;

public class GuiCCTextField extends GuiWidget
{
    private String text;
    private boolean isFocused;
    private boolean isEnabled;
    
    public int maxStringLength;
    public int cursorCounter;
    public String actionCommand;
    
    private String allowedcharacters;

    public GuiCCTextField(int x, int y, int width, int height, String text)
    {
        super(x, y, width, height);
        isFocused = false;
        isEnabled = true;
        this.text = text;

        allowedcharacters = ChatAllowedCharacters.allowedCharacters;
    }

    public GuiCCTextField setActionCommand(String s)
    {
        actionCommand = s;
        return this;
    }

    public void setText(String s)
    {
        if(s.equals(text))
            return;

        String oldText = text;
        text = s;
        onTextChanged(oldText);
    }

    public void onTextChanged(String oldText)
    {
    }

    public final String getText()
    {
        return text;
    }
    
    public final boolean isEnabled()
    {
        return isEnabled;
    }

    public void setEnabled(boolean b)
    {
        isEnabled = b;
        if(!isEnabled && isFocused)
            setFocused(false);
    }
    
    public final boolean isFocused()
    {
        return isFocused;
    }

    @Override
    public void update()
    {
        cursorCounter++;
    }

    @Override
    public void keyTyped(char c, int keycode)
    {
        if(!isEnabled || !isFocused)
            return;

        /*if(c == '\t')//tab
        {
            parentGuiScreen.selectNextField();
        }*/
        if(c == '\026')//paste
        {
            String s = GuiScreen.getClipboardString();
            if(s == null || s.equals(""))
                return;

            for(int i = 0; i < s.length(); i++)
            {
                if(text.length() == maxStringLength)
                    return;

                char tc = s.charAt(i);
                if(canAddChar(tc))
                    setText(text + tc);
            }
        }
        if(keycode == Keyboard.KEY_RETURN)
        {
            setFocused(false);
            sendAction(actionCommand, getText());
        }

        if(keycode == Keyboard.KEY_BACK && text.length() > 0)
            setText(text.substring(0, text.length() - 1));

        if((text.length() < maxStringLength || maxStringLength == 0) && canAddChar(c))
            setText(text + c);
    }

    public boolean canAddChar(char c)
    {
        return allowedcharacters.indexOf(c) >= 0;
    }

    @Override
    public void mouseClicked(int x, int y, int button)
    {
        if(isEnabled && pointInside(x, y))
        {
            setFocused(true);
            if(button == 1)
                setText("");
        }
        else
            setFocused(false);
    }

    public void setFocused(boolean focus)
    {
        if(focus == isFocused)
            return;
        isFocused = focus;
        onFocusChanged();
    }

    public void onFocusChanged()
    {
        if(isFocused)
            cursorCounter = 0;
    }
    
    @Override
    public void draw(int i, int j, float f)
    {
        drawBackground();
        drawText();
    }

    public void drawBackground()
    {
        drawRect(x - 1, y - 1, x + width + 1, y + height + 1, 0xffa0a0a0);
        drawRect(x, y, x + width, y + height, 0xff000000);
    }
    
    public String getDrawText()
    {
        String s = getText();
        if(isEnabled && isFocused && (cursorCounter / 6) % 2 == 0)
            s+="_";
        return s;
    }
    
    public void drawText()
    {
        drawString(fontRenderer, getDrawText(), x + 4, y + height/2 - 4, getTextColour());
    }

    public int getTextColour()
    {
        return isEnabled ? 0xe0e0e0 : 0x707070;
    }

    public GuiCCTextField setMaxStringLength(int i)
    {
        maxStringLength = i;
        return this;
    }

    public GuiCCTextField setAllowedCharacters(String s)
    {
        if(s == null)
            s = ChatAllowedCharacters.allowedCharacters;
        else
            allowedcharacters = s;
        return this;
    }
}
