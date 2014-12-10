package codechicken.nei;

import codechicken.nei.ItemList.EverythingItemFilter;
import codechicken.nei.ItemList.PatternItemFilter;
import codechicken.nei.api.API;
import codechicken.nei.api.ItemFilter;
import codechicken.nei.api.ItemFilter.ItemFilterProvider;
import net.minecraft.util.EnumChatFormatting;

import java.util.LinkedList;
import java.util.List;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import static codechicken.lib.gui.GuiDraw.drawGradientRect;
import static codechicken.lib.gui.GuiDraw.drawRect;
import static codechicken.nei.NEIClientConfig.world;

public class SearchField extends TextField implements ItemFilterProvider
{
    /**
     * Interface for returning a custom filter based on search field text
     */
    public static interface ISearchProvider
    {
        /**
         * Return null to ignore this provider and use the normal searching protocol
         */
        public ItemFilter getFilter(String searchText);
    }
    public static List<ISearchProvider> searchProviders = new LinkedList<ISearchProvider>();

    long lastclicktime;

    public SearchField(String ident) {
        super(ident);
        API.addItemFilter(this);
    }

    public static boolean searchInventories() {
        return world.nbt.getBoolean("searchinventories");
    }

    @Override
    public void drawBox() {
        if (searchInventories())
            drawGradientRect(x, y, w, h, 0xFFFFFF00, 0xFFC0B000);
        else
            drawRect(x, y, w, h, 0xffA0A0A0);
        drawRect(x + 1, y + 1, w - 2, h - 2, 0xFF000000);
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
        ItemList.updateFilter();
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

    @Override
    public ItemFilter getFilter() {
        String s_filter = text().toLowerCase();

        for(ISearchProvider p : searchProviders) {
            ItemFilter filter = p.getFilter(s_filter);
            if(filter != null)
                return filter;
        }

        switch(NEIClientConfig.getIntSetting("inventory.searchmode")) {
            case 0://plain
                s_filter = "\\Q"+s_filter+"\\E";
                break;
            case 1:
                s_filter = s_filter
                        .replace(".", "")
                        .replace("?", ".")
                        .replace("*", ".+?");
                break;
        }

        Pattern pattern = null;
        try {
            pattern = Pattern.compile(s_filter);
        } catch (PatternSyntaxException ignored) {}
        if (pattern == null || pattern.toString().equals(""))
            return new EverythingItemFilter();

        return new PatternItemFilter(pattern);
    }
}
