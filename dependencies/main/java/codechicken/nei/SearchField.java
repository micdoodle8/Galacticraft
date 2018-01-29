package codechicken.nei;

import codechicken.lib.item.filtering.IItemFilter;
import codechicken.lib.item.filtering.IItemFilterProvider;
import codechicken.nei.ItemList.AnyMultiItemFilter;
import codechicken.nei.ItemList.EverythingItemFilter;
import codechicken.nei.ItemList.PatternItemFilter;
import codechicken.nei.api.API;
import codechicken.nei.config.KeyBindings;
import net.minecraft.util.text.TextFormatting;

import java.util.LinkedList;
import java.util.List;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import static codechicken.lib.gui.GuiDraw.drawGradientRect;
import static codechicken.lib.gui.GuiDraw.drawRect;
import static codechicken.nei.NEIClientConfig.world;

public class SearchField extends TextField implements IItemFilterProvider {
    /**
     * Interface for returning a custom filter based on search field text
     */
    public interface ISearchProvider {
        /**
         * @return false if this filter should only be used if no other non-default filters match the search string
         */
        boolean isPrimary();

        /**
         * @return An item filter for items matching SearchTex null to ignore this provider
         */
        IItemFilter getFilter(String searchText);
    }

    private static class DefaultSearchProvider implements ISearchProvider {
        @Override
        public boolean isPrimary() {
            return false;
        }

        @Override
        public IItemFilter getFilter(String searchText) {
            Pattern pattern = getPattern(searchText);
            return pattern == null ? null : new PatternItemFilter(pattern);
        }
    }

    public static List<ISearchProvider> searchProviders = new LinkedList<ISearchProvider>();

    long lastclicktime;

    public SearchField(String ident) {
        super(ident);
        API.addItemFilter(this);
        API.addSearchProvider(new DefaultSearchProvider());
    }

    public static boolean searchInventories() {
        return world.nbt.getBoolean("searchinventories");
    }

    @Override
    public int getTextColour() {
//        if (JEIIntegrationManager.itemPannelOwner == EnumItemBrowser.JEI ? JEIIntegrationManager.getFilteredItems().size() == 0 : ItemPanel.items.size() == 0) {
//            return Color.red.getRGB();
//        }
        return super.getTextColour();
    }

    @Override
    public void drawBox() {
        if (searchInventories()) {
            drawGradientRect(x, y, w, h, 0xFFFFFF00, 0xFFC0B000);
        } else {
            drawRect(x, y, w, h, 0xffA0A0A0);
        }
        drawRect(x + 1, y + 1, w - 2, h - 2, 0xFF000000);
    }

    @Override
    public boolean handleClick(int mousex, int mousey, int button) {
        if (button == 0) {
            if (focused() && (System.currentTimeMillis() - lastclicktime < 500)) {//double click
                NEIClientConfig.world.nbt.setBoolean("searchinventories", !searchInventories());
                NEIClientConfig.world.saveNBT();
            } else {
                lastclicktime = System.currentTimeMillis();
            }
        }
        return super.handleClick(mousex, mousey, button);
    }

    @Override
    public void onTextChange(String oldText) {
        NEIClientConfig.setSearchExpression(text());
        ItemList.updateFilter.restart();
    }

    @Override
    public void lastKeyTyped(int keyID, char keyChar) {
        if (KeyBindings.get("nei.options.keys.gui.search").isActiveAndMatches(keyID)) {
            setFocus(true);
        }
    }

    @Override
    public String filterText(String s) {
        return TextFormatting.getTextWithoutFormattingCodes(s);
    }

    public static Pattern getPattern(String search) {
        switch (NEIClientConfig.getIntSetting("inventory.searchmode")) {
        case 0://plain
            search = "\\Q" + search + "\\E";
            break;
        case 1:
            search = search.replace(".", "").replace("?", ".").replace("*", ".+?");
            break;
        }

        Pattern pattern = null;
        try {
            pattern = Pattern.compile(search);
        } catch (PatternSyntaxException ignored) {
        }
        return pattern == null || pattern.toString().length() == 0 ? null : pattern;
    }

    @Override
    public IItemFilter getFilter() {
        String s_filter = text().toLowerCase();

        List<IItemFilter> primary = new LinkedList<IItemFilter>();
        List<IItemFilter> secondary = new LinkedList<IItemFilter>();
        for (ISearchProvider p : searchProviders) {
            IItemFilter filter = p.getFilter(s_filter);
            if (filter != null) {
                (p.isPrimary() ? primary : secondary).add(filter);
            }
        }

        if (!primary.isEmpty()) {
            return new AnyMultiItemFilter(primary);
        }
        if (!secondary.isEmpty()) {
            return new AnyMultiItemFilter(secondary);
        }
        return new EverythingItemFilter();
    }
}
