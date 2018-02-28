package micdoodle8.mods.galacticraft.core.items;

import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.proxy.ClientProxyCore;
import micdoodle8.mods.galacticraft.core.util.EnumSortCategoryItem;
import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

public class ItemMoon extends ItemDesc implements ISortableItem
{
    public static String[] names = { "meteoric_iron_ingot", "compressed_meteoric_iron", "lunar_sapphire" };
//    protected IIcon[] icons = new IIcon[ItemMoon.names.length];

    public ItemMoon(String str)
    {
        super();
        this.setMaxDamage(0);
        this.setHasSubtypes(true);
        this.setUnlocalizedName(str);
    }

    /*@Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister iconRegister)
    {
        int i = 0;

        for (String name : ItemMoon.names)
        {
            this.icons[i++] = iconRegister.registerIcon("galacticraftmoon:" + name);
        }
    }

    @Override
    public IIcon getIconFromDamage(int damage)
    {
        if (this.icons.length > damage)
        {
            return this.icons[damage];
        }

        return super.getIconFromDamage(damage);
    }*/

    @Override
    public void getSubItems(Item par1, CreativeTabs par2CreativeTabs, List<ItemStack> par3List)
    {
        for (int i = 0; i < ItemMoon.names.length; i++)
        {
            par3List.add(new ItemStack(par1, 1, i));
        }
    }

    @Override
    public String getUnlocalizedName(ItemStack par1ItemStack)
    {
        if (names.length > par1ItemStack.getItemDamage())
        {
            return "item." + ItemMoon.names[par1ItemStack.getItemDamage()];
        }

        return "unnamed";
    }

    @Override
    public int getMetadata(int par1)
    {
        return par1;
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
    public EnumSortCategoryItem getCategory(int meta)
    {
        switch (meta)
        {
        case 0:
            return EnumSortCategoryItem.INGOT;
        case 2:
            return EnumSortCategoryItem.GENERAL;
        default:
            return EnumSortCategoryItem.PLATE;
        }
    }

    @Override
    public String getShiftDescription(int meta)
    {
        if (meta == 2)
        {
            return GCCoreUtil.translate("item.lunar_sapphire.description");
        }

        return "";
    }

    @Override
    public boolean showDescription(int meta)
    {
        return meta == 2;
    }

    @Override
    public float getSmeltingExperience(ItemStack item)
    {
        switch (item.getItemDamage())
        {
        case 1:
            return 1F;
        }
        return -1F;
    }
}
