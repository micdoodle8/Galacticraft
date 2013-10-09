package codechicken.nei;

import java.util.Comparator;

public class WidgetZOrder
    implements Comparator<Widget>
{
    boolean topfirst;
    public WidgetZOrder(boolean topfirst)
    {
        this.topfirst = topfirst;
    }

    public int compare(Widget w1, Widget w2)
    {
        return w1.z != w2.z ? ((topfirst ? w1.z > w2.z : w1.z < w2.z) ? 1 : -1) : 1;
    }
}
