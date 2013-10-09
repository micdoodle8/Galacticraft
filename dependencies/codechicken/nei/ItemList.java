package codechicken.nei;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Icon;

import codechicken.core.CommonUtils;
import codechicken.core.inventory.ItemKey;
import codechicken.nei.ItemPanel.ItemPanelObject;
import codechicken.nei.api.ItemInfo;
import codechicken.nei.forge.GuiContainerManager;

public class ItemList
{
    public static ArrayList<ItemStack> items = new ArrayList<ItemStack>();
    @SuppressWarnings("unchecked")
    public static List<ItemStack>[] itemMap = new List[Item.itemsList.length];
    
    private static boolean matching = false;
    private static boolean loading = false;
    private static boolean research = false;
    private static boolean reload = false;
    private static HashSet<Integer> erroredIDs = new HashSet<Integer>();
    
    private static interface ItemMatcher
    {
        public boolean matches(ItemStack item);
    }
    
    private static class SubsetItemMatcher implements ItemMatcher
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
                if(tag.isItemInRange(item.itemID, item.getItemDamage()))
                    return true;
            return false;
        }
    }
    
    private static class PatternItemMatcher implements ItemMatcher
    {
        Pattern pattern;
        
        public PatternItemMatcher(Pattern pattern)
        {
            this.pattern = pattern;
        }

        @Override
        public boolean matches(ItemStack item)
        {
            return pattern.matcher(CommonUtils.filterText(GuiContainerManager.concatenatedDisplayName(item, true).toLowerCase())).find();
        }
    }
    
    private static class EverythingItemMatcher implements ItemMatcher
    {
        @Override
        public boolean matches(ItemStack item)
        {
            return true;
        }
    }
    
    private static class NothingItemMatcher implements ItemMatcher
    {
        @Override
        public boolean matches(ItemStack item)
        {
            return false;
        }
    }
    
    public static ItemMatcher getSearchMatcher()
    {
        String matchstring = NEIClientConfig.getSearchExpression().toLowerCase();
        if(matchstring.startsWith("@") && matchstring.length() > 1)
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
        }
        
        {
            matchstring = matchstring.replace(".", "");
            matchstring = matchstring.replace("?", ".");
            matchstring = matchstring.replace("*", ".+?");
            Pattern pattern;
            try
            {
                pattern = Pattern.compile(matchstring);
            }
            catch(PatternSyntaxException e)
            {
                return new EverythingItemMatcher();
            }
            if(pattern == null || pattern.toString().equals(""))
                return new EverythingItemMatcher();
            
            return new PatternItemMatcher(pattern);
        }
    }
    
    public static boolean itemMatchesSearch(ItemStack item)
    {
        return getSearchMatcher().matches(item);
    }
    
    public static interface IItemCounter
    {
        public int getItem();
        public Thread getThread();
    }

    @SuppressWarnings("serial")
    public static class TimeoutException extends RuntimeException
    {
        public final int itemID;
        
        public TimeoutException(String msg, int lastItem)
        {
            super(msg);
            itemID = lastItem;
        }
    }

    public static class ThreadLoadMonitor extends Thread
    {
        IItemCounter loadingThread;
        
        public ThreadLoadMonitor(IItemCounter handle)
        {
            super("NEI Load Monitor");
            loadingThread = handle;
        }
        
        @SuppressWarnings("deprecation")
        @Override
        public void run()
        {
            int lastItem = 0;
            long lastTime = System.currentTimeMillis();
            while(loadingThread.getThread().isAlive())
            {
                if(lastItem != loadingThread.getItem())
                {
                    lastTime = System.currentTimeMillis();
                    lastItem = loadingThread.getItem();
                }
                else if(System.currentTimeMillis() - lastTime > 2000 && lastItem >= 0)
                {
                    loadingThread.getThread().stop(new TimeoutException("Took to long to advance item", lastItem));
                }
                try
                {
                    Thread.sleep(2000);
                }
                catch(InterruptedException ie)
                {}
            }
        }
    }

    public static class ThreadMatchSearch extends Thread implements IItemCounter
    {
        private int itemID;
        
        public ThreadMatchSearch()
        {
            super("NEI Item Searching Thread");
            matching = true;
        }
        
        @Override
        public int getItem()
        {
            return itemID;
        }
        
        @Override
        public Thread getThread()
        {
            return this;
        }
        
        @Override
        public void run()
        {
            //new ThreadLoadMonitor(this).start();
            startMatch:
            while(matching)
            {
                try
                {
                    ArrayList<ItemPanelObject> visibleitems = new ArrayList<ItemPanelObject>();
                    ItemMatcher matcher = getSearchMatcher();
                    for(ItemStack item : items)
                    {
                        itemID = item.itemID;
                        if(research)
                        {
                            research = false;
                            continue startMatch;
                        }
                        
                        if(item.hasTagCompound() ? NEIClientConfig.vishash.isItemHidden(item.itemID, item.stackTagCompound) : NEIClientConfig.vishash.isItemHidden(item.itemID, item.getItemDamage()))
                            continue;
                        
                        if(!NEIClientConfig.canGetItem(new ItemKey(item)))
                            continue;
                        
                        if(matcher.matches(item))
                            visibleitems.add(new ItemPanelStack(item));
                    }
                    ItemPanel.visibleitems = visibleitems;
                }
                catch(TimeoutException e)
                {
                    System.err.println("Removing itemID: "+e.itemID+" from list.");
                    e.printStackTrace();
                    erroredIDs.add(e.itemID);
                    loadItems();
                }
                matching = false;
            }
        }
    }

    private static HashSet<String> stackTraces = new HashSet<String>();
    public static class ThreadLoadItems extends Thread implements IItemCounter
    {
        private int itemID = 0;
        
        public ThreadLoadItems()
        {
            super("NEI Item Loading Thread");
            loading = true;
        }
        
        @Override
        public int getItem()
        {
            return itemID;
        }
        
        @Override
        public Thread getThread()
        {
            return this;
        }
        
        @Override
        public void run()
        {
            new ThreadLoadMonitor(this).start();
            startSearch:
            while(loading)
            {
                try
                {
                    ArrayList<ItemStack> items = new ArrayList<ItemStack>();
                    ArrayList<ItemStack> sublist = new ArrayList<ItemStack>();
                    
                    for(itemID = 0; itemID < Item.itemsList.length; itemID++)
                    {
                        if(reload)
                        {
                            reload = false;
                            continue startSearch;
                        }
                        
                        Item item = Item.itemsList[itemID];
                        if(item == null || ItemInfo.isHidden(item.itemID))
                            continue;
                        
                        List<ItemStack> permutations = new ArrayList<ItemStack>();
                        
                        sublist.clear();
                        ArrayList<int[]> damageranges;
                        try
                        {
                            item.getSubItems(itemID, null, sublist);
                            damageranges = ItemInfo.getItemDamageVariants(item.itemID);
                        }
                        catch(Exception e)
                        {
                            System.err.println("Removing itemID: "+itemID+" from list.");
                            e.printStackTrace();
                            erroredIDs.add(itemID);
                            continue;
                        }
                        if(sublist.size() > 0)
                        {
                            ArrayList<Integer> discreteDamages = new ArrayList<Integer>();
                            for(ItemStack stack : sublist)
                            {
                                if(stack.hasTagCompound())
                                {
                                    stack = stack.copy();
                                    permutations.add(stack);
                                }
                                else
                                {
                                    discreteDamages.add(stack.getItemDamage());
                                }
                            }
                            
                            if(damageranges == ItemInfo.defaultDamageRange)
                                damageranges = NEIClientUtils.concatIntegersToRanges(discreteDamages);
                            else
                                damageranges = NEIClientUtils.addIntegersToRanges(damageranges, discreteDamages);
                        }
                        
                        boolean skipDamage0 = false;
                        ArrayList<ItemStack> datalist = ItemInfo.getItemCompounds(itemID);
                        if(datalist != null && datalist.size() > 0 && NEIClientConfig.canPerformAction("itemnbt"))
                        {
                            skipDamage0 = true;
                            
                            for(ItemStack stack : datalist)
                            {
                                stack = stack.copy();
                                permutations.add(stack);                        
                            }
                        }
                        
                        HashSet<String> damageIconSet = new HashSet<String>();
                        for(int[] damagerange : damageranges)
                        {
                            for(int damage = damagerange[0]; damage <= damagerange[1]; damage++)
                            {
                                ItemStack itemstack = new ItemStack(item, 1, damage);
                                try
                                {
                                    Icon icon = item.getIconIndex(itemstack);
                                    String name = GuiContainerManager.concatenatedDisplayName(itemstack, false);                                
                                    String s = name+"@"+(icon == null ? 0 : icon.hashCode());
                                    if(!damageIconSet.contains(s))
                                    {
                                        damageIconSet.add(s);
                                        if(damage == 0 && skipDamage0)
                                            continue;
                                        
                                        permutations.add(itemstack);
                                    }
                                        
                                }
                                catch(Throwable t)
                                {
                                    StringWriter sw = new StringWriter();
                                    t.printStackTrace(new PrintWriter(sw));
                                    String stackTrace = itemstack+sw.toString();
                                    if(!stackTraces.contains(stackTrace))
                                    {
                                        System.err.println("NEI: Omitting #"+itemID+":"+damage+" "+item.getClass().getSimpleName());
                                        t.printStackTrace();
                                        stackTraces.add(stackTrace);
                                    }
                                }
                            }
                        }
                        
                        items.addAll(permutations);
                        itemMap[itemID] = permutations;
                    }

                    itemID = -1;
                    
                    DropDownFile dropDownInstance = DropDownFile.dropDownInstance;
                    dropDownInstance.resetHashes();
                    for(ItemStack stack : items)
                    {
                        if(reload)
                        {
                            reload = false;
                            continue startSearch;
                        }
                        dropDownInstance.addItemIfInRange(stack.itemID, stack.getItemDamage(), stack.stackTagCompound);
                    }
                    dropDownInstance.updateState();
                    ItemList.items = items;
                    if(reload)
                    {
                        reload = false;
                        continue startSearch;
                    }
                    loading = false;
                }
                catch(TimeoutException e)
                {
                    System.err.println("Removing itemID: "+e.itemID+" from list.");
                    e.printStackTrace();
                    erroredIDs.add(e.itemID);
                }
            }
            updateSearch();
        }
    }
    
    public static boolean isMatching()
    {
        return matching;
    }
    
    public static void updateSearch()
    {
        if(matching)
            research = true;
        else
            new ThreadMatchSearch().start();
    }
    
    public static void loadItems()
    {
        if(loading)
            reload = true;
        else
            new ThreadLoadItems().start();
    }
}
