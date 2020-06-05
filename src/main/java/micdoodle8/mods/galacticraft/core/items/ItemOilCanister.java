//package micdoodle8.mods.galacticraft.core.items;
//
//import micdoodle8.mods.galacticraft.core.GalacticraftCore;
//import micdoodle8.mods.galacticraft.core.util.ConfigManagerCore;
//import micdoodle8.mods.galacticraft.core.util.EnumSortCategoryItem;
//import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
//import net.minecraft.client.util.ITooltipFlag;
//import net.minecraft.item.Item;
//import net.minecraft.item.ItemGroup;
//import net.minecraft.entity.Entity;
//import net.minecraft.item.ItemStack;
//import net.minecraft.util.NonNullList;
//import net.minecraft.util.text.ITextComponent;
//import net.minecraft.util.text.StringTextComponent;
//import net.minecraft.world.World;
//import net.minecraftforge.api.distmarker.Dist;
//import net.minecraftforge.api.distmarker.OnlyIn;
//import net.minecraftforge.fml.relauncher.Side;
//import net.minecraftforge.fml.relauncher.SideOnly;
//
//import java.util.List;
//
//import javax.annotation.Nullable;
//
//public class ItemOilCanister extends ItemCanisterGeneric implements ISortableItem
//{
////    protected IIcon[] icons = new IIcon[7];
//
//    public ItemOilCanister(Item.Properties builder)
//    {
//        super(builder);
//        this.setAllowedFluid(ConfigManagerCore.useOldOilFluidID ? "oilgc" : "oil");
////        this.setContainerItem(this);
//        //this.setTextureName(Constants.TEXTURE_PREFIX + assetName);
//    }
//
//    /*@Override
//    @OnlyIn(Dist.CLIENT)
//    public void registerIcons(IIconRegister iconRegister)
//    {
//        for (int i = 0; i < this.icons.length; i++)
//        {
//            this.icons[i] = iconRegister.registerIcon(this.getIconString() + "_" + i);
//        }
//    }*/
//
//    @Override
//    public String getUnlocalizedName(ItemStack itemStack)
//    {
//        if (itemStack.getMaxDamage() - itemStack.getDamage() == 0)
//        {
//            return "item.empty_liquid_canister";
//        }
//
//        if (itemStack.getDamage() == 1)
//        {
//            return "item.oil_canister";
//        }
//
//        return "item.oil_canister_partial";
//    }
//
//    /*@Override
//    public IIcon getIconFromDamage(int par1)
//    {
//        final int damage = 6 * par1 / this.getMaxDamage();
//
//        if (this.icons.length > damage)
//        {
//            return this.icons[this.icons.length - damage - 1];
//        }
//
//        return super.getIconFromDamage(damage);
//    }*/
//
//    @Override
//    public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn)
//    {
//        if (stack.getMaxDamage() - stack.getDamage() > 0)
//        {
//            tooltip.add(new StringTextComponent(GCCoreUtil.translate("gui.message.oil.name") + ": " + (stack.getMaxDamage() - stack.getDamage())));
//        }
//    }
//
////    @Override
////    @OnlyIn(Dist.CLIENT)
////    public void getSubItems(ItemGroup tab, NonNullList<ItemStack> list)
////    {
//////        if (tab == GalacticraftCore.galacticraftItemsTab || tab == ItemGroup.SEARCH)
//////        {
//////            list.add(new ItemStack(this, 1, this.getMaxDamage()));
//////            list.add(new ItemStack(this, 1, 1));
//////        }
////    }
//
//    @Override
//    public void onUpdate(ItemStack par1ItemStack, World par2World, Entity par3Entity, int par4, boolean par5)
//    {
//        if (ItemCanisterGeneric.EMPTY == par1ItemStack.getItemDamage())
//        {
//            par1ItemStack.setTag(null);
//        }
//        else if (par1ItemStack.getItemDamage() <= 0)
//        {
//            par1ItemStack.setItemDamage(1);
//        }
//    }
//
//    @Override
//    public EnumSortCategoryItem getCategory(int meta)
//    {
//        return EnumSortCategoryItem.CANISTER;
//    }
//}
