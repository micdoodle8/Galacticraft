package codechicken.nei;

import codechicken.nei.widget.Button;

public abstract class ButtonCycled extends Button {
    @Override
    public Image getRenderIcon() {
        return icons[index];
    }

    public int index;
    public Image[] icons;
}
