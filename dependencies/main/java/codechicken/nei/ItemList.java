package codechicken.nei;

import codechicken.nei.ThreadOperationTimer.TimeoutException;
import codechicken.nei.api.ItemFilter;
import codechicken.nei.api.ItemFilter.ItemFilterProvider;
import codechicken.nei.api.ItemInfo;
import codechicken.nei.guihook.GuiContainerManager;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ListMultimap;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Pattern;

public class ItemList
{
    /**
     * Fields are replaced atomically and contents never modified.
     */
    public static List<ItemStack> items = new ArrayList<ItemStack>();
    /**
     * Fields are replaced atomically and contents never modified.
     */
    public static ListMultimap<Item, ItemStack> itemMap = ArrayListMultimap.create();
    /**
     * Updates to this should be synchronised on this
     */
    public static final List<ItemFilterProvider> itemFilterers = new LinkedList<ItemFilterProvider>();
    public static final List<ItemsLoadedCallback> loadCallbacks = new LinkedList<ItemsLoadedCallback>();

    private static boolean loading = false;
    private static boolean reload = false;

    private static boolean filtering = false;
    private static boolean refilter = false;

    private static HashSet<Item> erroredItems = new HashSet<Item>();
    private static HashSet<String> stackTraces = new HashSet<String>();

    public static class EverythingItemFilter implements ItemFilter
    {
        @Override
        public boolean matches(ItemStack item) {
            return true;
        }
    }

    public static class NothingItemFilter implements ItemFilter
    {
        @Override
        public boolean matches(ItemStack item) {
            return false;
        }
    }

    public static class PatternItemFilter implements ItemFilter
    {
        Pattern pattern;

        public PatternItemFilter(Pattern pattern) {
            this.pattern = pattern;
        }

        @Override
        public boolean matches(ItemStack item) {
            return pattern.matcher(ItemInfo.getSearchName(item)).find();
        }
    }

    public static interface ItemsLoadedCallback
    {
        public void itemsLoaded();
    }

    public static boolean itemMatches(ItemStack item, List<ItemFilter> filters) {
        for(ItemFilter filter : filters)
            if(!filter.matches(item))
                return false;

        return true;
    }

    public static boolean itemMatches(ItemStack item) {
        return itemMatches(item, getItemFilters());
    }

    public static List<ItemFilter> getItemFilters() {
        LinkedList<ItemFilter> filters = new LinkedList<ItemFilter>();
        synchronized (itemFilterers) {
            for(ItemFilterProvider p : itemFilterers)
                filters.add(p.getFilter());
        }
        return filters;
    }

    private static class ThreadLoadItems extends Thread
    {
        public ThreadLoadItems() {
            super("NEI Item Loading");
        }

        private void damageSearch(Item item, List<ItemStack> permutations) {
            HashSet<String> damageIconSet = new HashSet<String>();
            for (int damage = 0; damage < 16; damage++)
                try {
                    ItemStack itemstack = new ItemStack(item, 1, damage);
                    IIcon icon = item.getIconIndex(itemstack);
                    String name = GuiContainerManager.concatenatedDisplayName(itemstack, false);
                    String s = name + "@" + (icon == null ? 0 : icon.hashCode());
                    if (!damageIconSet.contains(s)) {
                        damageIconSet.add(s);
                        permutations.add(itemstack);
                    }
                }
                catch(TimeoutException t) {
                    throw t;
                }
                catch(Throwable t) {
                    NEIServerUtils.logOnce(t, stackTraces, "Ommiting "+item+":"+damage+" "+item.getClass().getSimpleName(), item.toString());
                }
        }

        @Override
        public void run() {
            ThreadOperationTimer timer = ThreadOperationTimer.start(this, 500);
            restart:
            do {
                reload = false;
                LinkedList<ItemStack> items = new LinkedList<ItemStack>();
                LinkedList<ItemStack> permutations = new LinkedList<ItemStack>();
                ListMultimap<Item, ItemStack> itemMap = ArrayListMultimap.create();

                timer.setLimit(500);
                for (Item item : (Iterable<Item>) Item.itemRegistry) {
                    if (reload)
                        continue restart;

                    if (item == null || erroredItems.contains(item))
                        continue;

                    try {
                        timer.reset(item);

                        permutations.clear();
                        permutations.addAll(ItemInfo.getItemOverrides(item));
                        if (permutations.isEmpty())
                            item.getSubItems(item, null, permutations);

                        if (permutations.isEmpty())
                            damageSearch(item, permutations);

                        timer.reset();

                        items.addAll(permutations);
                        itemMap.putAll(item, permutations);
                    } catch (Throwable t) {
                        NEIServerConfig.logger.error("Removing item: " + item + " from list.", t);
                        erroredItems.add(item);
                    }
                }

                ItemList.items = items;
                ItemList.itemMap = itemMap;
                for(ItemsLoadedCallback callback : loadCallbacks)
                    callback.itemsLoaded();

                updateFilter();
            }
            while (reload);
            loading = false;
        }
    }

    public static class ThreadFilterItems extends Thread {
        public ThreadFilterItems() {
            super("NEI Item Filtering");
        }

        @Override
        public void run() {
            restart:
            do {
                refilter = false;
                ArrayList<ItemStack> filtered = new ArrayList<ItemStack>();
                List<ItemFilter> filters = getItemFilters();
                for(ItemStack item : items) {
                    if(refilter)
                        continue restart;

                    if(itemMatches(item, filters))
                        filtered.add(item);
                }

                ItemSorter.sort(filtered);
                ItemPanel.updateItemList(filtered);
            }
            while(refilter);
            filtering = false;
        }
    }

    public static void updateFilter() {
        if (filtering)
            refilter = true;
        else {
            filtering = true;
            new ThreadFilterItems().start();
        }
    }

    public static void loadItems() {
        if (loading)
            reload = true;
        else {
            loading = true;
            new ThreadLoadItems().start();
        }
    }
}
