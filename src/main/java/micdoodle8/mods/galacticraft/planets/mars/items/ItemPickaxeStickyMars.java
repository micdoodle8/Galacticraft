//package micdoodle8.mods.galacticraft.planets.mars.items;
//
//import micdoodle8.mods.galacticraft.core.GalacticraftCore;
//import micdoodle8.mods.galacticraft.core.items.ISortableItem;
//import micdoodle8.mods.galacticraft.core.proxy.ClientProxyCore;
//import micdoodle8.mods.galacticraft.core.util.EnumSortCategoryItem;
//import net.minecraft.item.ItemGroup;
//import net.minecraft.item.Rarity;
//import net.minecraft.item.PickaxeItem;
//import net.minecraft.item.ItemStack;
//import net.minecraft.util.NonNullList;
//import net.minecraftforge.fml.LogicalSide;
//import net.minecraftforge.fml.relauncher.SideOnly;
//
//public class ItemPickaxeStickyMars extends PickaxeItem implements ISortableItem
//{
//    public ItemPickaxeStickyMars(ToolMaterial par2EnumToolMaterial)
//    {
//        super(par2EnumToolMaterial);
//    }
//
//    @Override
//    @OnlyIn(Dist.CLIENT)
//    public Rarity getRarity(ItemStack par1ItemStack)
//    {
//        return ClientProxyCore.galacticraftItem;
//    }
//
//    @OnlyIn(Dist.CLIENT)
////    @Override
////    public ItemGroup getCreativeTab()
////    {
////        return GalacticraftCore.galacticraftItemsTab;
////    }
//
//    /*@Override
//    @OnlyIn(Dist.CLIENT)
//    public void registerIcons(IIconRegister par1IconRegister)
//    {
//        this.itemIcon = par1IconRegister.registerIcon("galacticraftmars:deshPick_slime");
//    }*/
//
//    @Override
//    public void getSubItems(ItemGroup tab, NonNullList<ItemStack> list)
//    {
//        if (tab == GalacticraftCore.galacticraftItemsTab || tab == ItemGroup.SEARCH)
//        {
//            list.add(new ItemStack(this, 1, 0));
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
//        return EnumSortCategoryItem.TOOLS;
//    }
//}
