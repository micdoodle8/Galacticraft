package micdoodle8.mods.galacticraft.core.util;

import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.registry.Registry;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.Comparator;

public class ItemGroupGC extends ItemGroup
{
    private ItemStack itemForTab;
    private Comparator<ItemStack> tabSorter;

    public ItemGroupGC(int index, String label, ItemStack itemForTab, Comparator<ItemStack> tabSorter)
    {
        super(index, label);
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
