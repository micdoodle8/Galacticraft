package micdoodle8.mods.galacticraft.core.client.gui.page;

import micdoodle8.mods.galacticraft.core.client.gui.GCCoreGuiManual;
import org.w3c.dom.Element;

public abstract class GCCoreBookPage
{
    protected GCCoreGuiManual manual;
    protected int side;

    public void init(GCCoreGuiManual manual, int side)
    {
        this.manual = manual;
        this.side = side;
    }

    public abstract void readPageFromXML(Element element);

    public void renderBackgroundLayer(int localwidth, int localheight)
    {
    }

    public abstract void renderContentLayer(int localwidth, int localheight);
}
