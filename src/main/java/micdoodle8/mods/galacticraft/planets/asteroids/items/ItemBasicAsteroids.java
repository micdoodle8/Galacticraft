//package micdoodle8.mods.galacticraft.planets.asteroids.items;
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
//public class ItemBasicAsteroids extends Item implements ISortableItem
//{
//    public static String[] names = { "ingot_titanium", "engine_t2", "rocket_fins_t2", "shard_iron", "shard_titanium", "reinforced_plate_t3", "compressed_titanium", "thermal_cloth", "beam_core", "dust_titanium" };
////    protected IIcon[] icons = new IIcon[ItemBasicAsteroids.names.length];
//
//    public ItemBasicAsteroids(String name)
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
//    /*@Override
//    @OnlyIn(Dist.CLIENT)
//    public void registerIcons(IIconRegister iconRegister)
//    {
//        int i = 0;
//
//        for (String name : ItemBasicAsteroids.names)
//        {
//            this.icons[i++] = iconRegister.registerIcon(GalacticraftPlanets.TEXTURE_PREFIX + name);
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
//            for (int i = 0; i < ItemBasicAsteroids.names.length; i++)
//            {
//                list.add(new ItemStack(this, 1, i));
//            }
//        }
//    }
//
//    @Override
//    public String getUnlocalizedName(ItemStack par1ItemStack)
//    {
//        if (ItemBasicAsteroids.names.length > par1ItemStack.getDamage())
//        {
//            return "item." + ItemBasicAsteroids.names[par1ItemStack.getDamage()];
//        }
//
//        return "unnamed";
//    }
//
//    @Override
//    @OnlyIn(Dist.CLIENT)
//    public void addInformation(ItemStack par1ItemStack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn)
//    {
//        if (par1ItemStack != null && par1ItemStack.getDamage() == 5)
//        {
//            tooltip.add(GCCoreUtil.translate("item.tier3.desc"));
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
//        case 0:
//            return EnumSortCategoryItem.INGOT;
//        case 5:
//        case 6:
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
//        case 5:
//            return 2F;
//        case 6:
//            return 1F;
//        }
//        return -1F;
//    }
//}
