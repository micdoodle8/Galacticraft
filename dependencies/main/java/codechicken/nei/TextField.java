package codechicken.nei;

import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.ChatAllowedCharacters;
import org.lwjgl.input.Keyboard;

import static codechicken.lib.gui.GuiDraw.*;

public abstract class TextField extends Widget
{
    public TextField(String ident) {
        identifier = ident;
    }

    public int getTextColour() {
        return focused() ? 0xFFE0E0E0 : 0xFF909090;
    }

    public void drawBox() {
        drawRect(x, y, w, h, 0xffA0A0A0);
        drawRect(x + 1, y + 1, w - 2, h - 2, 0xFF000000);
    }

    @Override
    public void draw(int mousex, int mousey) {
        drawBox();

        String drawtext = text;

        int textWidth;
        while((textWidth = getStringWidth(drawtext)) > w - 14)
            drawtext = drawtext.substring(1);

        if (focused() && (cursorCounter / 6) % 2 == 0)
            drawtext = drawtext + '_';

        int textx = centered ? x + (w - textWidth) / 2 : x + 4;
        int texty = y + (h + 1) / 2 - 3;

        drawString(drawtext, textx, texty, getTextColour());
    }

    @Override
    public void onGuiClick(int mousex, int mousey) {
        if (!contains(mousex, mousey))
            setFocus(false);
    }

    @Override
    public boolean handleClick(int mousex, int mousey, int button) {
        if (button == 1)
            setText("");
        setFocus(true);
        return true;
    }

    @Override
    public boolean handleKeyPress(int keyID, char keyChar) {
        if (!focused())
            return false;

        if (keyID == Keyboard.KEY_BACK) {
            if (text.length() > 0) {
                setText(text.substring(0, text.length() - 1));
                backdowntime = System.currentTimeMillis();
            }
        } else if (keyID == Keyboard.KEY_RETURN || keyID == Keyboard.KEY_ESCAPE) {
            setFocus(false);
            onExit();
        } else if (keyChar == 22)//paste
        {
            String pastestring = GuiScreen.getClipboardString();
            if (pastestring == null)
                pastestring = "";

            if (isValid(text + pastestring))
                setText(text + pastestring);
        } else if (isValid(text + keyChar))
            setText(text + keyChar);

        return true;
    }

    public void onExit() {
    }

    public abstract void onTextChange(String oldText);

    public boolean isValid(String string) {
        // Solve the problem that Minecraft can't post Chinese characters
        return ChatAllowedCharacters.isAllowedCharacter(string.charAt(string.length() - 1));
    }

    @Override
    public void update() {
        cursorCounter++;
        if (backdowntime > 0) {
            if (Keyboard.isKeyDown(Keyboard.KEY_BACK) && text.length() > 0) {
                if (System.currentTimeMillis() - backdowntime > 200 / (1 + backs * 0.3F)) {
                    setText(text.substring(0, text.length() - 1));
                    backdowntime = System.currentTimeMillis();
                    backs++;
                }
            } else {
                backdowntime = 0;
                backs = 0;
            }
        }
    }

    public void setText(String s) {
        String oldText = text;
        text = filterText(s);
        onTextChange(oldText);
    }

    public String filterText(String s) {
        return s;
    }

    public void setFocus(boolean focus) {
        if (focus) {
            LayoutManager.setInputFocused(this);
        } else if (focused()) {
            LayoutManager.setInputFocused(null);
        }
    }

    public boolean focused() {
        return LayoutManager.getInputFocused() == this;
    }

    public String text() {
        return text;
    }

    private String text = "";
    public boolean centered;
    public long backdowntime;
    public int backs;
    public String identifier;
    public int cursorCounter;
}
