package codechicken.nei.config;

import codechicken.lib.vec.Rectangle4i;
import codechicken.nei.TextField;
import codechicken.nei.config.GuiOptionList.OptionScrollSlot;

import java.util.List;

import static codechicken.lib.gui.GuiDraw.drawString;
import static codechicken.lib.gui.GuiDraw.getStringWidth;

public class OptionTextField extends Option
{
    private boolean focused = false;
    private TextField textField = new TextField("")
    {
        @Override
        public boolean isValid(String string) {
            return super.isValid(string) && isValidInput(string);
        }

        @Override
        public void onTextChange(String oldText) {
            if(focused() && isValidValue(text()))
                getTag().setValue(text());
        }

        @Override
        public boolean focused() {
            return focused;
        }

        @Override
        public void setFocus(boolean focus) {
            if (focus && defaulting())
                return;

            if (!focus && !isValidValue(text()))
                setText(getTag().getValue());

            focused = focus;
        }
    };

    public OptionTextField(String name) {
        super(name);
        textField.h = 20;
    }

    @Override
    public void onAdded(OptionScrollSlot slot) {
        super.onAdded(slot);
    }

    @Override
    public void update() {
        textField.update();
        if(!textField.focused())
            textField.setText(renderTag().getValue());
    }

    public String getPrefix() {
        return translateN(name);
    }

    @Override
    public void draw(int mousex, int mousey, float frame) {
        drawString(getPrefix(), 10, 6, -1);

        textField.w = slot.slotWidth() - getStringWidth(getPrefix()) - 16;
        textField.x = slot.slotWidth() - textField.w;
        textField.draw(mousex, mousey);
    }

    @Override
    public void keyTyped(char c, int keycode) {
        textField.handleKeyPress(keycode, c);
    }

    @Override
    public void mouseClicked(int mousex, int mousey, int button) {
        if (textField.contains(mousex, mousey))
            textField.handleClick(mousex, mousey, button);
    }

    @Override
    public void onMouseClicked(int mousex, int mousey, int button) {
        textField.onGuiClick(mousex, mousey);
    }

    @Override
    public List<String> handleTooltip(int mousex, int mousey, List<String> currenttip) {
        if (new Rectangle4i(10, 0, textField.x - 10, 20).contains(mousex, mousey)) {
            String tip = translateN(name + ".tip");
            if (!tip.equals(name + ".tip"))
                currenttip.add(tip);
        }
        return currenttip;
    }

    public boolean isValidInput(String s) {
        return true;
    }

    public boolean isValidValue(String s) {
        return true;
    }
}
