package micdoodle8.mods.galacticraft.core.util;

import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Collections;
import java.util.Comparator;

public class CreativeTabGC extends ItemGroup
{
    private ItemStack itemForTab;
    private Comparator<ItemStack> tabSorter;

    public CreativeTabGC(int par1, String par2Str, ItemStack itemForTab, Comparator<ItemStack> tabSorter)
    {
        super(par1, par2Str);
        this.itemForTab = itemForTab;
        this.tabSorter = tabSorter;
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public ItemStack getTabIconItem()
    {
        return this.itemForTab;
    }

    public void setItemForTab(ItemStack itemForTab)
    {
        this.itemForTab = itemForTab;
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public String getTranslatedTabLabel()
    {
        return "item_group." + this.getTabLabel();
    }

    @Override
    public void displayAllRelevantItems(NonNullList<ItemStack> list)
    {
        super.displayAllRelevantItems(list);
        if (this.tabSorter != null)
        {
            try {
                Collections.sort(list, tabSorter);
            } catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    }

    public void setTabSorter(Comparator<ItemStack> tabSorter)
    {
        this.tabSorter = tabSorter;
    }
}
