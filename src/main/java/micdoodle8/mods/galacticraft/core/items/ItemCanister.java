//package micdoodle8.mods.galacticraft.core.items;
//
//import micdoodle8.mods.galacticraft.core.GalacticraftCore;
//import micdoodle8.mods.galacticraft.core.proxy.ClientProxyCore;
//import micdoodle8.mods.galacticraft.core.util.EnumSortCategoryItem;
//import net.minecraft.item.ItemGroup;
//import net.minecraft.item.Rarity;
//import net.minecraft.item.Item;
//import net.minecraft.item.ItemStack;
//import net.minecraft.util.NonNullList;
//import net.minecraftforge.fml.LogicalSide;
//import net.minecraftforge.fml.relauncher.SideOnly;
//
//public class ItemCanister extends Item implements ISortableItem
//{
//    public static final String[] names = { "tin", // 0
//            "copper" }; // 1
//
////    protected IIcon[] icons;
//
//    public ItemCanister(Item.Properties properties)
//    {
//        super(properties);
//        this.setMaxDamage(0);
//        this.setHasSubtypes(true);
//        this.setUnlocalizedName(assetName);
//        //this.setTextureName(Constants.TEXTURE_PREFIX + assetName);
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
//    /*@Override
//    @OnlyIn(Dist.CLIENT)
//    public void registerIcons(IIconRegister iconRegister)
//    {
//        int i = 0;
//        this.icons = new IIcon[ItemCanister.names.length];
//
//        for (final String name : ItemCanister.names)
//        {
//            this.icons[i++] = iconRegister.registerIcon(this.getIconString() + "." + name);
//        }
//    }*/
//
//    @Override
//    public String getUnlocalizedName(ItemStack itemStack)
//    {
//        return this.getUnlocalizedName() + "." + ItemCanister.names[itemStack.getDamage()];
//    }
//
//    /*@Override
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
//            for (int i = 0; i < 2; i++)
//            {
//                list.add(new ItemStack(this, 1, i));
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
//        return EnumSortCategoryItem.CANISTER;
//    }
//}
