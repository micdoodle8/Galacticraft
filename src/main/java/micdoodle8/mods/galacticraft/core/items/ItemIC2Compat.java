package micdoodle8.mods.galacticraft.core.items;

import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.proxy.ClientProxyCore;
import micdoodle8.mods.galacticraft.core.util.CompatibilityManager;
import micdoodle8.mods.galacticraft.core.util.EnumSortCategoryItem;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

public class ItemIC2Compat extends Item implements ISortableItem
{
    public static final String[] types = { "dust", "ore_purified", "ore_crushed", "dust_small" };
    public static final String[] names = { "alu", "titanium" };

    public ItemIC2Compat(String assetName)
    {
        super();
        this.setMaxDamage(0);
        this.setHasSubtypes(CompatibilityManager.isIc2Loaded());
        this.setUnlocalizedName(assetName);
    }

    @Override
    public CreativeTabs getCreativeTab()
    {
        return GalacticraftCore.galacticraftItemsTab;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public EnumRarity getRarity(ItemStack par1ItemStack)
    {
        return ClientProxyCore.galacticraftItem;
    }

    @Override
    public String getUnlocalizedName(ItemStack itemStack)
    {
        int meta = itemStack.getItemDamage();
        if (!CompatibilityManager.isIc2Loaded()) meta = 0;
        return this.getUnlocalizedName() + "." + ItemIC2Compat.types[meta % 4] + "_" + ItemIC2Compat.names[meta / 4];
    }

    @Override
    public void getSubItems(Item par1, CreativeTabs par2CreativeTabs, List<ItemStack> par3List)
    {
        par3List.add(new ItemStack(par1, 1, 0));
        if (CompatibilityManager.isIc2Loaded())
        {
            par3List.add(new ItemStack(par1, 1, 1));
            par3List.add(new ItemStack(par1, 1, 2));
            par3List.add(new ItemStack(par1, 1, 7));
        }
    }

    @Override
    public int getMetadata(int par1)
    {
        return par1;
    }

    @Override
    public EnumSortCategoryItem getCategory(int meta)
    {
        return EnumSortCategoryItem.GENERAL;
    }
}
