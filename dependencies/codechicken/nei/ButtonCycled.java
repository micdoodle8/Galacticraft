package codechicken.nei;

public abstract class ButtonCycled extends Button
{    
    @Override
    public Image getRenderIcon()
    {
        return icons[index];
    }

    public int index;
    public Image[] icons;
}
