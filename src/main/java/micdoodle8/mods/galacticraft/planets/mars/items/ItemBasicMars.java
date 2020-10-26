//package micdoodle8.mods.galacticraft.planets.mars.items;
//
//import micdoodle8.mods.galacticraft.core.GalacticraftCore;
//import micdoodle8.mods.galacticraft.core.items.ISortableItem;
//import micdoodle8.mods.galacticraft.core.proxy.ClientProxyCore;
//import micdoodle8.mods.galacticraft.core.util.EnumSortCategoryItem;
//import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
//import net.minecraft.client.util.ITooltipFlag;
//import net.minecraft.item.ItemGroup;
//import net.minecraft.item.Rarity;
//import net.minecraft.item.Item;
//import net.minecraft.item.ItemStack;
//import net.minecraft.util.NonNullList;
//import net.minecraft.world.World;
//import net.minecraftforge.fml.LogicalSide;
//import net.minecraftforge.fml.relauncher.SideOnly;
//
//import java.util.List;
//
//import javax.annotation.Nullable;
//
//public class ItemBasicMars extends Item implements ISortableItem
//{
//    public static String[] names = { "raw_desh", "desh_stick", "ingot_desh", "reinforced_plate_t2", "slimeling_cargo", "compressed_desh", "fluid_manip" };
//
//    public ItemBasicMars(String name)
//    {
//        super();
//        this.setMaxDamage(0);
//        this.setHasSubtypes(true);
//        this.setUnlocalizedName(name);
//    }
//
//    @OnlyIn(Dist.CLIENT)
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
//    public void getSubItems(ItemGroup tab, NonNullList<ItemStack> list)
//    {
//        if (tab == GalacticraftCore.galacticraftItemsTab || tab == ItemGroup.SEARCH)
//        {
//            for (int i = 0; i < ItemBasicMars.names.length; i++)
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
//            return "item." + ItemBasicMars.names[par1ItemStack.getDamage()];
//        }
//
//        return "unnamed";
//    }
//
//    @Override
//    @OnlyIn(Dist.CLIENT)
//    public void addInformation(ItemStack par1ItemStack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn)
//    {
//        if (par1ItemStack != null && par1ItemStack.getDamage() == 3)
//        {
//            tooltip.add(GCCoreUtil.translate("item.tier2.desc"));
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
//        switch (meta)
//        {
//        case 2:
//            return EnumSortCategoryItem.INGOT;
//        case 3:
//        case 5:
//            return EnumSortCategoryItem.PLATE;
//        }
//        return EnumSortCategoryItem.GENERAL;
//    }
//
//    @Override
//    public float getSmeltingExperience(ItemStack item)
//    {
//        switch (item.getDamage())
//        {
//        case 3:
//            return 1.5F;
//        case 5:
//            return 1F;
//        }
//        return -1F;
//    }
//}
