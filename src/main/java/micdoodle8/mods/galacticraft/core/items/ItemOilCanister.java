package micdoodle8.mods.galacticraft.core.items;

import micdoodle8.mods.galacticraft.core.Constants;
import micdoodle8.mods.galacticraft.core.GCItems;
import micdoodle8.mods.galacticraft.core.util.EnumSortCategory;
import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

public class ItemOilCanister extends ItemCanisterGeneric implements ISortable
{
//    protected IIcon[] icons = new IIcon[7];

    public ItemOilCanister(Item.Properties builder)
    {
        super(builder);
//        this.setAllowedFluid(ConfigManagerCore.useOldOilFluidID.get() ? "oilgc" : "oil");
        this.setAllowedFluid(new ResourceLocation(Constants.MOD_ID_CORE, "oil")); // TODO Other oil support
//        this.setContainerItem(this);
        //this.setTextureName(Constants.TEXTURE_PREFIX + assetName);
    }

    /*@Override
    @OnlyIn(Dist.CLIENT)
    public void registerIcons(IIconRegister iconRegister)
    {
        for (int i = 0; i < this.icons.length; i++)
        {
            this.icons[i] = iconRegister.registerIcon(this.getIconString() + "_" + i);
        }
    }*/

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

    /*@Override
    public IIcon getIconFromDamage(int par1)
    {
        final int damage = 6 * par1 / this.getMaxDamage();

        if (this.icons.length > damage)
        {
            return this.icons[this.icons.length - damage - 1];
        }

        return super.getIconFromDamage(damage);
    }*/

    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn)
    {
        if (stack.getMaxDamage() - stack.getDamage() > 0)
        {
            tooltip.add(new StringTextComponent(GCCoreUtil.translate("gui.message.oil.name") + ": " + (stack.getMaxDamage() - stack.getDamage())));
        }
    }

    @Override
    public void fillItemGroup(ItemGroup group, NonNullList<ItemStack> items)
    {
        if (this.isInGroup(group))
        {
            items.add(createEmptyCanister(1));
            items.add(new ItemStack(this));
        }
    }


//    @Override
//    @OnlyIn(Dist.CLIENT)
//    public void getSubItems(ItemGroup tab, NonNullList<ItemStack> list)
//    {
////        if (tab == GalacticraftCore.galacticraftItemsTab || tab == ItemGroup.SEARCH)
////        {
////            list.add(new ItemStack(this, 1, this.getMaxDamage()));
////            list.add(new ItemStack(this, 1, 1));
////        }
//    }


    @Override
    public void inventoryTick(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected)
    {
        if (ItemCanisterGeneric.EMPTY_CAPACITY == stack.getDamage())
        {
//            stack.setTag(null);
        }
        else if (stack.getDamage() <= 0)
        {
            stack.setDamage(1);
        }
    }

    @Override
    public EnumSortCategory getCategory()
    {
        return EnumSortCategory.CANISTER;
    }

    public static ItemStack createEmptyCanister(int count)
    {
        ItemStack stack = new ItemStack(GCItems.oilCanister, count);
        stack.setDamage(ItemCanisterGeneric.EMPTY_CAPACITY);
        return stack;
    }
}
