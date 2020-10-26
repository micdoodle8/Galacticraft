package micdoodle8.mods.galacticraft.core.util;

import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.registry.Registry;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.Collections;
import java.util.Comparator;

public class CreativeTabGC extends ItemGroup
{
    private ItemStack itemForTab;
    private Comparator<ItemStack> tabSorter;

    public CreativeTabGC(String name, ItemStack itemForTab, Comparator<ItemStack> tabSorter)
    {
        super(name);
        this.itemForTab = itemForTab;
        this.tabSorter = tabSorter;
    }

    @Override
    public ItemStack createIcon()
    {
        return this.itemForTab;
    }

    public void setItemForTab(ItemStack itemForTab)
    {
        this.itemForTab = itemForTab;
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public String getTranslationKey()
    {
        return "item_group." + this.getTabLabel();
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void fill(NonNullList<ItemStack> items)
    {
        for (Item item : Registry.ITEM)
        {
            item.fillItemGroup(this, items);
        }

        if (tabSorter != null)
        {
            try
            {
                Collections.sort(items, tabSorter);
            }
            catch (Exception e)
            {

            }
        }
    }

//    @Override
//    public void displayAllRelevantItems(NonNullList<ItemStack> list)
//    {
//        super.displayAllRelevantItems(list);
//        if (this.tabSorter != null)
//        {
//            try {
//                Collections.sort(list, tabSorter);
//            } catch (Exception e)
//            {
//                e.printStackTrace();
//            }
//        }
//    } TODO Sorting

    public void setTabSorter(Comparator<ItemStack> tabSorter)
    {
        this.tabSorter = tabSorter;
    }
}
