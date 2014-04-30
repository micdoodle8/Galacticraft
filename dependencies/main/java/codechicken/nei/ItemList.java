package codechicken.nei;

import codechicken.nei.ItemPanel.ItemPanelObject;
import codechicken.nei.api.ItemInfo;
import codechicken.nei.guihook.GuiContainerManager;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ListMultimap;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IIcon;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

public class ItemList
{
    public static List<ItemStack> items = new ArrayList<ItemStack>();
    public static ListMultimap<Item, ItemStack> itemMap = ArrayListMultimap.create();

    private static boolean matching = false;
    private static boolean loading = false;
    private static boolean research = false;
    private static boolean reload = false;
    private static HashSet<Item> erroredItems = new HashSet<Item>();

    private static interface ItemMatcher
    {
        public boolean matches(ItemStack item);
    }
    
    /*private static class SubsetItemMatcher implements ItemMatcher
    {
        final List<SubSetRangeTag> tags;
        
        public SubsetItemMatcher(List<SubSetRangeTag> tags)
        {
            this.tags = tags;
        }
        
        @Override
        public boolean matches(ItemStack item)
        {
            for(SubSetRangeTag tag : tags)
                if(tag.isItemInRange(item.getItem(), item.getItemDamage()))
                    return true;
            return false;
        }
    }*/

    private static class PatternItemMatcher implements ItemMatcher
    {
        Pattern pattern;

        public PatternItemMatcher(Pattern pattern) {
            this.pattern = pattern;
        }

        @Override
        public boolean matches(ItemStack item) {
            return pattern.matcher(EnumChatFormatting.getTextWithoutFormattingCodes(GuiContainerManager.concatenatedDisplayName(item, true).toLowerCase())).find();
        }
    }

    private static class EverythingItemMatcher implements ItemMatcher
    {
        @Override
        public boolean matches(ItemStack item) {
            return true;
        }
    }

    private static class NothingItemMatcher implements ItemMatcher
    {
        @Override
        public boolean matches(ItemStack item) {
            return false;
        }
    }

    public static ItemMatcher getSearchMatcher() {
        String matchstring = NEIClientConfig.getSearchExpression().toLowerCase();
        /*if(matchstring.startsWith("@") && matchstring.length() > 1)
        {
            LinkedList<SubSetRangeTag> tags = new LinkedList<SubSetRangeTag>();
            try
            {
                String pattern = matchstring.substring(1);
                for(SubSetRangeTag tag : DropDownFile.dropDownInstance.allTags())
                    if(CommonUtils.filterText(tag.qualifiedname).toLowerCase().contains(pattern))
                        tags.add(tag);
                if(tags.isEmpty())
                    return new NothingItemMatcher();
                
                return new SubsetItemMatcher(tags);
            }
            catch(PatternSyntaxException e)
            {
                return new NothingItemMatcher();
            }
        }*/

        {
            matchstring = matchstring.replace(".", "");
            matchstring = matchstring.replace("?", ".");
            matchstring = matchstring.replace("*", ".+?");
            Pattern pattern = null;
            try {
                pattern = Pattern.compile(matchstring);
            } catch (PatternSyntaxException ignored) {}
            if (pattern == null || pattern.toString().equals(""))
                return new EverythingItemMatcher();

            return new PatternItemMatcher(pattern);
        }
    }

    public static boolean itemMatchesSearch(ItemStack item) {
        return getSearchMatcher().matches(item);
    }

    public static interface IItemCounter
    {
        public Item getItem();

        public Thread getThread();
    }

    @SuppressWarnings("serial")
    public static class TimeoutException extends RuntimeException
    {
        public final Item itemID;

        public TimeoutException(String msg, Item lastItem) {
            super(msg);
            itemID = lastItem;
        }
    }

    public static class ThreadLoadMonitor extends Thread
    {
        IItemCounter loadingThread;

        public ThreadLoadMonitor(IItemCounter handle) {
            super("NEI Load Monitor");
            loadingThread = handle;
        }

        @SuppressWarnings("deprecation")
        @Override
        public void run() {
            if(true) return;
            Item lastItem = null;
            long lastTime = System.currentTimeMillis();
            while (loadingThread.getThread().isAlive()) {
                if (lastItem != loadingThread.getItem()) {
                    lastTime = System.currentTimeMillis();
                    lastItem = loadingThread.getItem();
                } else if (System.currentTimeMillis() - lastTime > 2000 && lastItem != null) {
                    loadingThread.getThread().stop(new TimeoutException("Took to long to advance item", lastItem));
                }
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException ie) {}
            }
        }
    }

    public static class ThreadMatchSearch extends Thread implements IItemCounter
    {
        private Item item;

        public ThreadMatchSearch() {
            super("NEI Item Searching Thread");
            matching = true;
        }

        @Override
        public Item getItem() {
            return item;
        }

        @Override
        public Thread getThread() {
            return this;
        }

        @Override
        public void run() {
            //new ThreadLoadMonitor(this).start();
            try {
                startMatch:
                while (matching) {
                    try {
                        ArrayList<ItemPanelObject> visibleitems = new ArrayList<ItemPanelObject>();
                        ItemMatcher matcher = getSearchMatcher();
                        for (ItemStack stack : items) {
                            item = stack.getItem();
                            if (research) {
                                research = false;
                                continue startMatch;
                            }
                        
                        /*if(stack.hasTagCompound() ? NEIClientConfig.vishash.isItemHidden(item, stack.stackTagCompound) : NEIClientConfig.vishash.isItemHidden(item, stack.getItemDamage()))
                            continue;*/

                            if (NEIClientConfig.bannedBlocks.contains(stack))
                                continue;

                            if (matcher.matches(stack))
                                visibleitems.add(new ItemPanelStack(stack));
                        }
                        ItemPanel.visibleitems = visibleitems;
                    } catch (TimeoutException e) {
                        System.err.println("Removing itemID: " + e.itemID + " from list.");
                        e.printStackTrace();
                        erroredItems.add(e.itemID);
                        loadItems();
                    }
                    matching = false;
                }
            }
            finally {
                matching = false;
            }
        }
    }

    private static HashSet<String> stackTraces = new HashSet<String>();

    public static class ThreadLoadItems extends Thread implements IItemCounter
    {
        private Item item;

        public ThreadLoadItems() {
            super("NEI Item Loading Thread");
            loading = true;
        }

        @Override
        public Item getItem() {
            return item;
        }

        @Override
        public Thread getThread() {
            return this;
        }

        @Override
        public void run() {
            new ThreadLoadMonitor(this).start();
            startSearch:
            while (loading) {
                try {
                    ArrayList<ItemStack> items = new ArrayList<ItemStack>();
                    ArrayList<ItemStack> permutations = new ArrayList<ItemStack>();
                    ListMultimap<Item, ItemStack> itemMap = ArrayListMultimap.create();

                    for (Item item : (Iterable<Item>) Item.itemRegistry) {
                        this.item = item;
                        if (reload) {
                            reload = false;
                            continue startSearch;
                        }

                        if (item == null || ItemInfo.isHidden(item))
                            continue;

                        permutations.clear();
                        permutations.addAll(ItemInfo.getItemOverrides(item));
                        if(permutations.isEmpty()) {
                            try {
                                item.getSubItems(item, null, permutations);
                            } catch (Exception e) {
                                System.err.println("Removing itemID: " + item + " from list.");
                                e.printStackTrace();
                                erroredItems.add(item);
                                continue;
                            }
                        }
                        if(permutations.isEmpty()) {
                            HashSet<String> damageIconSet = new HashSet<String>();
                            for (int damage = 0; damage < 16; damage++) {
                                ItemStack itemstack = new ItemStack(item, 1, damage);
                                try {
                                    IIcon icon = item.getIconIndex(itemstack);
                                    String name = GuiContainerManager.concatenatedDisplayName(itemstack, false);
                                    String s = name + "@" + (icon == null ? 0 : icon.hashCode());
                                    if (!damageIconSet.contains(s)) {
                                        damageIconSet.add(s);
                                        permutations.add(itemstack);
                                    }

                                } catch (Throwable t) {
                                    StringWriter sw = new StringWriter();
                                    t.printStackTrace(new PrintWriter(sw));
                                    String stackTrace = itemstack + sw.toString();
                                    if (!stackTraces.contains(stackTrace)) {
                                        System.err.println("NEI: Omitting #" + this.item + ":" + damage + " " + item.getClass().getSimpleName());
                                        t.printStackTrace();
                                        stackTraces.add(stackTrace);
                                    }
                                }
                            }
                        }

                        for(Iterator<ItemStack> it = permutations.iterator(); it.hasNext(); )
                            if(ItemInfo.isHidden(it.next()))
                                it.remove();

                        items.addAll(permutations);
                        itemMap.putAll(item, permutations);
                    }

                    item = null;
                    
                    /*DropDownFile dropDownInstance = DropDownFile.dropDownInstance;
                    dropDownInstance.resetHashes();
                    for(ItemStack stack : items)
                    {
                        if(reload)
                        {
                            reload = false;
                            continue startSearch;
                        }
                        dropDownInstance.addItemIfInRange(stack.getItem(), stack.getItemDamage(), stack.stackTagCompound);
                    }
                    dropDownInstance.updateState();*/
                    ItemList.items = items;
                    ItemList.itemMap = itemMap;
                    if (reload) {
                        reload = false;
                        continue;
                    }
                    loading = false;
                } catch (TimeoutException e) {
                    System.err.println("Removing itemID: " + e.itemID + " from list.");
                    e.printStackTrace();
                    erroredItems.add(e.itemID);
                }
            }
            updateSearch();
        }
    }

    public static boolean isMatching() {
        return matching;
    }

    public static void updateSearch() {
        if (matching)
            research = true;
        else
            new ThreadMatchSearch().start();
    }

    public static void loadItems() {
        if (loading)
            reload = true;
        else
            new ThreadLoadItems().start();
    }
}
