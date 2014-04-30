package codechicken.nei;

import net.minecraft.util.EnumChatFormatting;

import static codechicken.lib.gui.GuiDraw.drawGradientRect;
import static codechicken.lib.gui.GuiDraw.drawRect;
import static codechicken.nei.NEIClientConfig.world;

public class SearchField extends TextField
{
    public SearchField(String ident) {
        super(ident);
    }

    public static boolean searchInventories() {
        return world.nbt.getBoolean("searchinventories");
    }

    @Override
    public void drawBox() {
        if (searchInventories())
            drawGradientRect(x, y, width, height, 0xFFFFFF00, 0xFFC0B000);
        else
            drawRect(x, y, width, height, 0xffA0A0A0);
        drawRect(x + 1, y + 1, width - 2, height - 2, 0xFF000000);
    }

    @Override
    public boolean handleClick(int mousex, int mousey, int button) {
        if (button == 0) {
            if (focused() && (System.currentTimeMillis() - lastclicktime < 500))//double click
            {
                world.nbt.setBoolean("searchinventories", !searchInventories());
                world.saveNBT();
            }
            lastclicktime = System.currentTimeMillis();
        }
        return super.handleClick(mousex, mousey, button);
    }

    @Override
    public void onTextChange(String oldText) {
        NEIClientConfig.setSearchExpression(text());
        ItemList.updateSearch();
    }

    @Override
    public void lastKeyTyped(int keyID, char keyChar) {
        if (keyID == NEIClientConfig.getKeyBinding("gui.search"))
            setFocus(true);
    }

    @Override
    public String filterText(String s) {
        return EnumChatFormatting.getTextWithoutFormattingCodes(s);
    }

    long lastclicktime;
}
