package codechicken.nei;

import net.minecraft.util.ChatAllowedCharacters;
import net.minecraft.client.gui.GuiScreen;

import org.lwjgl.input.Keyboard;

import static codechicken.core.gui.GuiDraw.*;
import static codechicken.nei.NEIClientUtils.translate;

public abstract class SaveLoadButton extends Button
{
    public SaveLoadButton(String s)
    {
        super(s);
    }

    @Override
    public boolean handleClick(int mousex, int mousey, int button)
    {
        if(button == 1)
        {
            label = "";
            onTextChange();
            focused = true;
            NEIClientUtils.mc().sndManager.playSoundFX("random.click", 1.0F, 1.0F);
            return true;
        }
        return super.handleClick(mousex, mousey, button);
    }

    public abstract void onTextChange();

    @Override
    public void onGuiClick(int i, int j)
    {
        if(!contains(i, j))
            focused = false;
    }

    @Override
    public boolean handleKeyPress(int keyID, char keyChar)
    {
        if(!focused)
            return false;
        
        if(keyID == Keyboard.KEY_BACK)
        {
            if(label.length() > 0)
            {
                label = label.substring(0, label.length() - 1);
                onTextChange();
                backdowntime = System.currentTimeMillis();
            }
        }
        else if(keyID == Keyboard.KEY_RETURN)
        {
            focused = false;
        }        
        else if(keyChar == 22)//paste
        {
            String pastestring = GuiScreen.getClipboardString();
            if(pastestring == null) 
            {
                pastestring = "";
            }
            
            label = label + pastestring;
            onTextChange();
        }
        else if(ChatAllowedCharacters.allowedCharacters.indexOf(keyChar) >= 0) 
        {
            label = label + keyChar;
            onTextChange();
        }
        return true;
    }

    @Override
    public void update()
    {
        cursorCounter++;
        if(backdowntime > 0)
        {
            if(Keyboard.isKeyDown(Keyboard.KEY_BACK) && label.length() > 5)
            {
                if(System.currentTimeMillis() - backdowntime > 200 / (1+backs * 0.3F))
                {
                    label = label.substring(0, label.length() - 1);
                    onTextChange();
                    backdowntime = System.currentTimeMillis();
                    backs++;
                }
            }
            else
            {
                backdowntime = 0;
                backs = 0;
            }
        }
    }
    
    @Override
    public void draw(int mousex, int mousey)
    {    
        super.draw(mousex, mousey);
        if(focused && (cursorCounter / 6) % 2 == 0)
            drawString("_", x + (width + getStringWidth(getRenderLabel())) / 2, y + (height - 8) / 2, 0xFFFFFFFF);
    }
    
    @Override
    public String getRenderLabel()
    {
        return translate(saved ? "load" : "save") + " " + label;
    }
    
    public boolean saved;
    public long backdowntime;
    public int backs;
    public int cursorCounter;
    public boolean focused;
}
