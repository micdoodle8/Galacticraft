//package micdoodle8.mods.galacticraft.core.items;
//
//import micdoodle8.mods.galacticraft.core.GalacticraftCore;
//import micdoodle8.mods.galacticraft.core.proxy.ClientProxyCore;
//import micdoodle8.mods.galacticraft.core.util.EnumSortCategoryItem;
//import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
//import net.minecraft.item.ItemGroup;
//import net.minecraft.item.Rarity;
//import net.minecraft.item.ItemStack;
//import net.minecraft.util.NonNullList;
//import net.minecraftforge.fml.LogicalSide;
//import net.minecraftforge.fml.relauncher.SideOnly;
//
//public class ItemMoon extends ItemDesc implements ISortableItem
//{
//    public static String[] names = { "meteoric_iron_ingot", "compressed_meteoric_iron", "lunar_sapphire" };
////    protected IIcon[] icons = new IIcon[ItemMoon.names.length];
//
//    public ItemMoon(String str)
//    {
//        super();
//        this.setMaxDamage(0);
//        this.setHasSubtypes(true);
//        this.setUnlocalizedName(str);
//    }
//
//    /*@Override
//    @OnlyIn(Dist.CLIENT)
//    public void registerIcons(IIconRegister iconRegister)
//    {
//        int i = 0;
//
//        for (String name : ItemMoon.names)
//        {
//            this.icons[i++] = iconRegister.registerIcon("galacticraftmoon:" + name);
//        }
//    }
//
//    @Override
//    public IIcon getIconFromDamage(int damage)
//    {
//        if (this.icons.length > damage)
//        {
//            return this.icons[damage];
//        }
//
//        return super.getIconFromDamage(damage);
//    }*/
//
//    @Override
//    public void getSubItems(ItemGroup tab, NonNullList<ItemStack> list)
//    {
//        if (tab == GalacticraftCore.galacticraftItemsTab || tab == ItemGroup.SEARCH)
//        {
//            for (int i = 0; i < ItemMoon.names.length; i++)
//            {
//                list.add(new ItemStack(this, 1, i));
//            }
//        }
//    }
//
//    @Override
//    public String getUnlocalizedName(ItemStack par1ItemStack)
//    {
//        if (names.length > par1ItemStack.getDamage())
//        {
//            return "item." + ItemMoon.names[par1ItemStack.getDamage()];
//        }
//
//        return "unnamed";
//    }
//
//    @Override
//    public int getMetadata(int par1)
//    {
//        return par1;
//    }
//
////    @Override
////    public ItemGroup getCreativeTab()
////    {
////        return GalacticraftCore.galacticraftItemsTab;
////    }
//
//    @Override
//    @OnlyIn(Dist.CLIENT)
//    public Rarity getRarity(ItemStack par1ItemStack)
//    {
//        return ClientProxyCore.galacticraftItem;
//    }
//
//    @Override
//    public EnumSortCategoryItem getCategory()
//    {
//        switch (meta)
//        {
//        case 0:
//            return EnumSortCategoryItem.INGOT;
//        case 2:
//            return EnumSortCategoryItem.GENERAL;
//        default:
//            return EnumSortCategoryItem.PLATE;
//        }
//    }
//
//    @Override
//    public String getShiftDescription(ItemStack stack)
//    {
//        if (meta == 2)
//        {
//            return GCCoreUtil.translate("item.lunar_sapphire.description");
//        }
//
//        return "";
//    }
//
//    @Override
//    public boolean showDescription(ItemStack stack)
//    {
//        return meta == 2;
//    }
//
//    @Override
//    public float getSmeltingExperience(ItemStack item)
//    {
//        switch (item.getDamage())
//        {
//        case 1:
//            return 1F;
//        }
//        return -1F;
//    }
//}
