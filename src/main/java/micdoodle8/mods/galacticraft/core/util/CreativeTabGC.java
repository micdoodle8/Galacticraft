package micdoodle8.mods.galacticraft.core.util;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class CreativeTabGC extends CreativeTabs
{
    private Item itemForTab;
    private int metaForTab;
    private Comparator<ItemStack> tabSorter;

    public CreativeTabGC(int par1, String par2Str, Item itemForTab, int metaForTab, Comparator<ItemStack> tabSorter)
    {
        super(par1, par2Str);
        this.itemForTab = itemForTab;
        this.metaForTab = metaForTab;
        this.tabSorter = tabSorter;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public String getTranslatedTabLabel()
    {
        return "item_group." + this.getTabLabel();
    }

    @Override
    @SideOnly(Side.CLIENT)
    public Item getTabIconItem()
    {
        return this.itemForTab;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public int getIconItemDamage()
    {
        return this.metaForTab;
    }

    public void setItemForTab(Item itemForTab)
    {
        this.itemForTab = itemForTab;
    }

    public void setMetaForTab(int metaForTab)
    {
        this.metaForTab = metaForTab;
    }

    @Override
    public void displayAllRelevantItems(List<ItemStack> list)
    {
        super.displayAllRelevantItems(list);
        if (this.tabSorter != null)
        {
            Collections.sort(list, tabSorter);
        }
    }

    public void setTabSorter(Comparator<ItemStack> tabSorter)
    {
        this.tabSorter = tabSorter;
    }
}
