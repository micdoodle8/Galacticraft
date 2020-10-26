//package micdoodle8.mods.galacticraft.planets.venus.items;
//
//import micdoodle8.mods.galacticraft.core.GalacticraftCore;
//import micdoodle8.mods.galacticraft.core.entities.player.GCPlayerStats;
//import micdoodle8.mods.galacticraft.core.items.ISortableItem;
//import micdoodle8.mods.galacticraft.core.items.ItemDesc;
//import micdoodle8.mods.galacticraft.core.proxy.ClientProxyCore;
//import micdoodle8.mods.galacticraft.core.util.EnumSortCategoryItem;
//import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
//import net.minecraft.entity.player.PlayerEntity;
//import net.minecraft.item.ItemGroup;
//import net.minecraft.entity.player.ServerPlayerEntity;
//import net.minecraft.item.Rarity;
//import net.minecraft.item.ItemStack;
//import net.minecraft.util.*;
//import net.minecraft.util.ActionResultType;
//import net.minecraft.util.Hand;
//import net.minecraft.world.World;
//
//public class ItemBasicVenus extends ItemDesc implements ISortableItem
//{
//    public static String[] names = { "shield_controller", "ingot_lead", "radioisotope_core", "thermal_fabric", "solar_dust", "solar_module_2", "thin_solar_wafer" };
//
//    public ItemBasicVenus(String name)
//    {
//        super();
//        this.setMaxDamage(0);
//        this.setHasSubtypes(true);
//        this.setUnlocalizedName(name);
//    }
//
////    @OnlyIn(Dist.CLIENT)
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
//            for (int i = 0; i < ItemBasicVenus.names.length; i++)
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
//            return "item." + ItemBasicVenus.names[par1ItemStack.getDamage()];
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
//    @Override
//    public EnumSortCategoryItem getCategory()
//    {
//        switch (meta)
//        {
//        case 0:
//            return EnumSortCategoryItem.GEAR;
//        case 1:
//            return EnumSortCategoryItem.INGOT;
//        default:
//            return EnumSortCategoryItem.GENERAL;
//        }
//    }
//
//    @Override
//    public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity player, Hand hand)
//    {
//        ItemStack itemStack = player.getHeldItem(hand);
//
//        if (player instanceof ServerPlayerEntity && itemStack.getDamage() == 0)
//        {
//            GCPlayerStats stats = GCPlayerStats.get(player);
//            ItemStack gear = stats.getExtendedInventory().getStackInSlot(10);
//
//            if (gear.isEmpty())
//            {
//                stats.getExtendedInventory().setInventorySlotContents(10, itemStack.copy());
//                itemStack.setCount(0);
//            }
//        }
//        return new ActionResult<>(ActionResultType.SUCCESS, itemStack);
//    }
//
//    @Override
//    public String getShiftDescription(ItemStack stack)
//    {
//        if (meta == 0)
//        {
//            return GCCoreUtil.translate("item.shield_controller.description");
//        }
//
//        return "";
//    }
//
//    @Override
//    public boolean showDescription(ItemStack stack)
//    {
//        return meta == 0;
//    }
//}
