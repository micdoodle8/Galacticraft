//package micdoodle8.mods.galacticraft.core.items;
//
//import micdoodle8.mods.galacticraft.core.GalacticraftCore;
//import micdoodle8.mods.galacticraft.core.proxy.ClientProxyCore;
//import micdoodle8.mods.galacticraft.core.util.CompatibilityManager;
//import micdoodle8.mods.galacticraft.core.util.EnumSortCategoryItem;
//import net.minecraft.item.ItemGroup;
//import net.minecraft.item.Rarity;
//import net.minecraft.item.Item;
//import net.minecraft.item.ItemStack;
//import net.minecraft.util.NonNullList;
//import net.minecraftforge.fml.LogicalSide;
//import net.minecraftforge.fml.relauncher.SideOnly;
//
//public class ItemIC2Compat extends Item implements ISortableItem
//{
//    public static final String[] types = { "dust", "ore_purified", "ore_crushed", "dust_small" };
//    public static final String[] names = { "alu", "titanium" };
//
//    public ItemIC2Compat(Item.Properties builder)
//    {
//        super();
//        this.setMaxDamage(0);
//        this.setHasSubtypes(CompatibilityManager.isIc2Loaded());
//        this.setUnlocalizedName(assetName);
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
//    public String getUnlocalizedName(ItemStack itemStack)
//    {
//        int meta = itemStack.getDamage();
//        if (!CompatibilityManager.isIc2Loaded()) meta = 0;
//        return this.getUnlocalizedName() + "." + ItemIC2Compat.types[meta % 4] + "_" + ItemIC2Compat.names[meta / 4];
//    }
//
//    @Override
//    public void getSubItems(ItemGroup tab, NonNullList<ItemStack> par3List)
//    {
//        if (tab == GalacticraftCore.galacticraftItemsTab || tab == ItemGroup.SEARCH)
//        {
//            par3List.add(new ItemStack(this, 1, 0));
//            if (CompatibilityManager.isIc2Loaded())
//            {
//                par3List.add(new ItemStack(this, 1, 1));
//                par3List.add(new ItemStack(this, 1, 2));
//                par3List.add(new ItemStack(this, 1, 7));
//            }
//        }
//    }
//
//    @Override
//    public int getMetadata(int par1)
//    {
//        return par1;
//    }
//
//    @Override
//    public EnumSortCategoryItem getCategory()
//    {
//        return EnumSortCategoryItem.GENERAL;
//    }
//}
