package micdoodle8.mods.galacticraft.core.client.gui.page;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class GCCoreTextPage extends GCCoreBookPage
{
    String text;

    @Override
    public void readPageFromXML(Element element)
    {
        NodeList nodes = element.getElementsByTagName("text");
        if (nodes != null)
        {
            this.text = nodes.item(0).getTextContent();
        }
    }

    @Override
    public void renderContentLayer(int localWidth, int localHeight)
    {
        this.manual.font.drawSplitString(this.text, localWidth, localHeight, 178, 0);
    }
}
