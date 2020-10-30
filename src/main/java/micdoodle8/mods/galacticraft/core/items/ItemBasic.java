//package micdoodle8.mods.galacticraft.core.items;
//
//import micdoodle8.mods.galacticraft.core.GalacticraftCore;
//import micdoodle8.mods.galacticraft.core.entities.player.GCPlayerStats;
//import micdoodle8.mods.galacticraft.core.proxy.ClientProxyCore;
//import micdoodle8.mods.galacticraft.core.util.EnumColor;
//import micdoodle8.mods.galacticraft.core.util.EnumSortCategoryItem;
//import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
//import net.minecraft.client.util.ITooltipFlag;
//import net.minecraft.item.ItemGroup;
//import net.minecraft.entity.Entity;
//import net.minecraft.entity.player.PlayerEntity;
//import net.minecraft.entity.player.ServerPlayerEntity;
//import net.minecraft.item.Rarity;
//import net.minecraft.item.Item;
//import net.minecraft.item.ItemStack;
//import net.minecraft.nbt.CompoundNBT;
//import net.minecraft.util.*;
//import net.minecraft.util.text.StringTextComponent;
//import net.minecraft.world.World;
//import net.minecraftforge.api.distmarker.Dist;
//import net.minecraftforge.api.distmarker.OnlyIn;
//import net.minecraftforge.fml.LogicalSide;
//import net.minecraftforge.fml.relauncher.SideOnly;
//
//import java.util.List;
//
//import javax.annotation.Nullable;
//
//public class ItemBasic extends Item implements ISortableItem
//{
//    public static final String[] names = { "solar_module_0",
//    "solar_module_1", "raw_silicon", "ingot_copper", "ingot_tin",
//    "ingot_aluminum", "compressed_copper", "compressed_tin",
//    "compressed_aluminum", "compressed_steel", "compressed_bronze",
//    "compressed_iron", "wafer_solar", "wafer_basic", "wafer_advanced",
//    "dehydrated_apple", "dehydrated_carrot", "dehydrated_melon",
//    "dehydrated_potato", "frequency_module", "ambient_thermal_controller" };
//    public static final int WAFER_BASIC = 13;
//    public static final int WAFER_ADVANCED = 14;
//
////    protected IIcon[] icons = new IIcon[ItemBasic.names.length];
//
//    public ItemBasic(Item.Properties properties)
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
//    @Override
//    public String getUnlocalizedName(ItemStack itemStack)
//    {
//        if (itemStack.getDamage() > 14 && itemStack.getDamage() < 19)
//        {
//            return this.getUnlocalizedName() + ".canned_food";
//        }
//
//        return this.getUnlocalizedName() + "." + ItemBasic.names[itemStack.getDamage()];
//    }
//
//    @Override
//    public void getSubItems(ItemGroup tab, NonNullList<ItemStack> list)
//    {
//        if (tab == GalacticraftCore.galacticraftItemsTab || tab == ItemGroup.SEARCH)
//        {
//            for (int i = 0; i < 15; i++)
//            {
//                list.add(new ItemStack(this, 1, i));
//            }
//            for (int i = 19; i < ItemBasic.names.length; i++)
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
//    @OnlyIn(Dist.CLIENT)
//    public void addInformation(ItemStack par1ItemStack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn)
//    {
//        if (par1ItemStack.getDamage() > 14 && par1ItemStack.getDamage() < 19)
//        {
//            tooltip.add(EnumColor.BRIGHT_GREEN + GCCoreUtil.translate(this.getUnlocalizedName() + "." + ItemBasic.names[par1ItemStack.getDamage()] + ""));
//        }
//        else if (par1ItemStack.getDamage() == 19)
//        {
//            tooltip.add(EnumColor.AQUA + GCCoreUtil.translate("gui.frequency_module.desc.0"));
//            tooltip.add(EnumColor.AQUA + GCCoreUtil.translate("gui.frequency_module.desc.1"));
//        }
//    }
//
//    @Override
//    public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, Hand hand)
//    {
//        ItemStack itemStackIn = playerIn.getHeldItem(hand);
//        if (itemStackIn.getDamage() == 19)
//        {
//            if (playerIn instanceof ServerPlayerEntity)
//            {
//                GCPlayerStats stats = GCPlayerStats.get(playerIn);
//                ItemStack gear = stats.getExtendedInventory().getStackInSlot(5);
//
//                if (gear.isEmpty() && itemStackIn.getTag() == null)
//                {
//                    stats.getExtendedInventory().setInventorySlotContents(5, itemStackIn.copy());
//                    itemStackIn = ItemStack.EMPTY;
//                }
//            }
//        }
//
//        return new ActionResult<>(ActionResultType.FAIL, itemStackIn);
//    }
//
//    @Override
//    public boolean onLeftClickEntity(ItemStack itemStack, PlayerEntity player, Entity entity)
//    {
//        if (itemStack.getDamage() != 19)
//        {
//            return false;
//        }
//
//        //Frequency module
//        if (!player.world.isRemote && entity != null && !(entity instanceof PlayerEntity))
//        {
//            if (itemStack.getTag() == null)
//            {
//                itemStack.setTag(new CompoundNBT());
//            }
//
//            itemStack.getTag().setLong("linkedUUIDMost", entity.getUniqueID().getMostSignificantBits());
//            itemStack.getTag().setLong("linkedUUIDLeast", entity.getUniqueID().getLeastSignificantBits());
//
//            player.sendMessage(new StringTextComponent(GCCoreUtil.translate("gui.tracking.message")));
//            return true;
//        }
//        return false;
//    }
//
//    @Override
//    public EnumSortCategoryItem getCategory()
//    {
//        switch (meta)
//        {
//        case 3:
//        case 4:
//        case 5:
//            return EnumSortCategoryItem.INGOT;
//        case 6:
//        case 7:
//        case 8:
//        case 9:
//        case 10:
//        case 11:
//            return EnumSortCategoryItem.PLATE;
//        case 19:
//            return EnumSortCategoryItem.GEAR;
//        }
//        return EnumSortCategoryItem.GENERAL;
//    }
//
//    @Override
//    public float getSmeltingExperience(ItemStack item)
//    {
//        switch (item.getDamage())
//        {
//        case 6:
//        case 7:
//        case 8:
//        case 9:
//        case 10:
//        case 11:
//            return 1F;
//        }
//        return -1F;
//    }
//}
