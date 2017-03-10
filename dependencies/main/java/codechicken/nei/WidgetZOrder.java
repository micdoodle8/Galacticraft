package codechicken.nei;

import codechicken.nei.widget.Widget;

import java.util.Comparator;

public class WidgetZOrder implements Comparator<Widget> {
    boolean topFirst;

    public WidgetZOrder(boolean topFirst) {
        this.topFirst = topFirst;
    }

    public int compare(Widget w1, Widget w2) {
        return w1.z != w2.z ? ((topFirst ? w1.z > w2.z : w1.z < w2.z) ? 1 : -1) : 1;
    }
}
