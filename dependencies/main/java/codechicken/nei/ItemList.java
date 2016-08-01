package codechicken.nei;

import codechicken.nei.ThreadOperationTimer.TimeoutException;
import codechicken.nei.api.ItemFilter;
import codechicken.nei.api.ItemFilter.ItemFilterProvider;
import codechicken.nei.api.ItemInfo;
import codechicken.nei.guihook.GuiContainerManager;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ListMultimap;
import net.minecraft.client.resources.model.IBakedModel;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Pattern;

public class ItemList {
    /**
     * Fields are replaced atomically and contents never modified.
     */
    public static volatile List<ItemStack> items = new ArrayList<ItemStack>();
    /**
     * Fields are replaced atomically and contents never modified.
     */
    public static volatile ListMultimap<Item, ItemStack> itemMap = ArrayListMultimap.create();
    /**
     * Updates to this should be synchronised on this
     */
    public static final List<ItemFilterProvider> itemFilterers = new LinkedList<ItemFilterProvider>();
    public static final List<ItemsLoadedCallback> loadCallbacks = new LinkedList<ItemsLoadedCallback>();

    private static HashSet<Item> erroredItems = new HashSet<Item>();
    private static HashSet<String> stackTraces = new HashSet<String>();

    public static class EverythingItemFilter implements ItemFilter {
        @Override
        public boolean matches(ItemStack item) {
            return true;
        }
    }

    public static class NothingItemFilter implements ItemFilter {
        @Override
        public boolean matches(ItemStack item) {
            return false;
        }
    }

    public static class PatternItemFilter implements ItemFilter {
        public Pattern pattern;

        public PatternItemFilter(Pattern pattern) {
            this.pattern = pattern;
        }

        @Override
        public boolean matches(ItemStack item) {
            return pattern.matcher(ItemInfo.getSearchName(item)).find();
        }
    }

    public static class AllMultiItemFilter implements ItemFilter {
        public List<ItemFilter> filters = new LinkedList<ItemFilter>();

        public AllMultiItemFilter(List<ItemFilter> filters) {
            this.filters = filters;
        }

        public AllMultiItemFilter() {
            this(new LinkedList<ItemFilter>());
        }

        @Override
        public boolean matches(ItemStack item) {
            for (ItemFilter filter : filters) {
                try {
                    if (!filter.matches(item)) {
                        return false;
                    }
                } catch (Exception e) {
                    NEIClientConfig.logger.error("Exception filtering " + item + " with " + filter, e);
                }
            }

            return true;
        }
    }

    public static class AnyMultiItemFilter implements ItemFilter {
        public List<ItemFilter> filters = new LinkedList<ItemFilter>();

        public AnyMultiItemFilter(List<ItemFilter> filters) {
            this.filters = filters;
        }

        public AnyMultiItemFilter() {
            this(new LinkedList<ItemFilter>());
        }

        @Override
        public boolean matches(ItemStack item) {
            for (ItemFilter filter : filters) {
                try {
                    if (filter.matches(item)) {
                        return true;
                    }
                } catch (Exception e) {
                    NEIClientConfig.logger.error("Exception filtering " + item + " with " + filter, e);
                }
            }

            return false;
        }
    }

    public interface ItemsLoadedCallback {
        void itemsLoaded();
    }

    public static boolean itemMatchesAll(ItemStack item, List<ItemFilter> filters) {
        for (ItemFilter filter : filters) {
            try {
                if (!filter.matches(item)) {
                    return false;
                }
            } catch (Exception e) {
                NEIClientConfig.logger.error("Exception filtering " + item + " with " + filter, e);
            }
        }

        return true;
    }

    /**
     * @deprecated use getItemListFilter().matches(item)
     */
    @Deprecated
    public static boolean itemMatches(ItemStack item) {
        return getItemListFilter().matches(item);
    }

    public static ItemFilter getItemListFilter() {
        return new AllMultiItemFilter(getItemFilters());
    }

    public static List<ItemFilter> getItemFilters() {
        LinkedList<ItemFilter> filters = new LinkedList<ItemFilter>();
        synchronized (itemFilterers) {
            for (ItemFilterProvider p : itemFilterers) {
                filters.add(p.getFilter());
            }
        }
        return filters;
    }

    public static final RestartableTask loadItems = new RestartableTask("NEI Item Loading") {
        private void damageSearch(Item item, List<ItemStack> permutations) {
            HashSet<String> damageIconSet = new HashSet<String>();
            for (int damage = 0; damage < 16; damage++) {
                try {
                    ItemStack stack = new ItemStack(item, 1, damage);
                    IBakedModel model = GuiContainerManager.drawItems.getItemModelMesher().getItemModel(stack);
                    String name = GuiContainerManager.concatenatedDisplayName(stack, false);
                    String s = name + "@" + (model == null ? 0 : model.hashCode());
                    if (!damageIconSet.contains(s)) {
                        damageIconSet.add(s);
                        permutations.add(stack);
                    }
                } catch (TimeoutException t) {
                    throw t;
                } catch (Throwable t) {
                    NEIServerUtils.logOnce(t, stackTraces, "Ommiting " + item + ":" + damage + " " + item.getClass().getSimpleName(), item.toString());
                }
            }
        }

        @Override
        public void execute() {
            ThreadOperationTimer timer = getTimer(500);

            LinkedList<ItemStack> items = new LinkedList<ItemStack>();
            LinkedList<ItemStack> permutations = new LinkedList<ItemStack>();
            ListMultimap<Item, ItemStack> itemMap = ArrayListMultimap.create();

            timer.setLimit(500);
            for (Item item : Item.itemRegistry) {
                if (interrupted()) {
                    return;
                }

                if (item == null || erroredItems.contains(item)) {
                    continue;
                }

                try {
                    timer.reset(item);

                    permutations.clear();
                    permutations.addAll(ItemInfo.itemOverrides.get(item));

                    if (permutations.isEmpty()) {
                        item.getSubItems(item, null, permutations);
                    }

                    if (permutations.isEmpty()) {
                        damageSearch(item, permutations);
                    }

                    permutations.addAll(ItemInfo.itemVariants.get(item));

                    timer.reset();
                    items.addAll(permutations);
                    itemMap.putAll(item, permutations);
                } catch (Throwable t) {
                    NEIServerConfig.logger.error("Removing item: " + item + " from list.", t);
                    erroredItems.add(item);
                }
            }

            if (interrupted()) {
                return;
            }
            ItemList.items = items;
            ItemList.itemMap = itemMap;
            for (ItemsLoadedCallback callback : loadCallbacks) {
                callback.itemsLoaded();
            }

            updateFilter.restart();
        }
    };

    public static final RestartableTask updateFilter = new RestartableTask("NEI Item Filtering") {
        @Override
        public void execute() {
            ArrayList<ItemStack> filtered = new ArrayList<ItemStack>();
            ItemFilter filter = getItemListFilter();
            for (ItemStack item : items) {
                if (interrupted()) {
                    return;
                }

                if (filter.matches(item)) {
                    filtered.add(item);
                }
            }

            if (interrupted()) {
                return;
            }
            ItemSorter.sort(filtered);
            if (interrupted()) {
                return;
            }
            ItemPanel.updateItemList(filtered);
        }
    };
}
