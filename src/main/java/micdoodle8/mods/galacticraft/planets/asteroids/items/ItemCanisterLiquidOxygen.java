package micdoodle8.mods.galacticraft.planets.asteroids.items;

import micdoodle8.mods.galacticraft.api.item.IItemOxygenSupply;
import micdoodle8.mods.galacticraft.core.Constants;
import micdoodle8.mods.galacticraft.core.items.ISortable;
import micdoodle8.mods.galacticraft.core.items.ItemCanisterGeneric;
import micdoodle8.mods.galacticraft.core.util.EnumSortCategory;
import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.LogicalSide;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.List;

public class ItemCanisterLiquidOxygen extends ItemCanisterGeneric implements IItemOxygenSupply, ISortable
{
    //    protected IIcon[] icons = new IIcon[7];
    private static final HashMap<ItemStack, Integer> craftingvalues = new HashMap<>();

    public ItemCanisterLiquidOxygen(Item.Properties builder)
    {
        super(builder);
//        this.setAllowedFluid("liquidoxygen");
        //this.setTextureName(GalacticraftPlanets.TEXTURE_PREFIX + assetName);
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
//        if (ItemCanisterGeneric.EMPTY - itemStack.getDamage() == 0)
//        {
//            return "item.empty_gas_canister";
//        }
//
//        if (itemStack.getDamage() == 1)
//        {
//            return "item.canister.lox.full";
//        }
//
//        return "item.canister.lox.partial";
//    }

    /*@Override
    public IIcon getIconFromDamage(int par1)
    {
        final int damage = 6 * par1 / ItemCanisterGeneric.EMPTY;

        if (this.icons.length > damage)
        {
            return this.icons[this.icons.length - damage - 1];
        }

        return super.getIconFromDamage(damage);
    }*/

    @Override
    @OnlyIn(Dist.CLIENT)
    public void addInformation(ItemStack par1ItemStack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn)
    {
        if (ItemCanisterGeneric.EMPTY_CAPACITY - par1ItemStack.getDamage() > 0)
        {
            tooltip.add(new StringTextComponent(GCCoreUtil.translate("item.canister.lox") + ": " + (ItemCanisterGeneric.EMPTY_CAPACITY - par1ItemStack.getDamage())));
        }
    }

    public static void saveDamage(ItemStack itemstack, int damage)
    {
        ItemCanisterLiquidOxygen.craftingvalues.put(itemstack, Integer.valueOf(damage));
    }

    @Override
    public ItemStack getContainerItem(ItemStack itemstack)
    {
        Integer saved = ItemCanisterLiquidOxygen.craftingvalues.get(itemstack);
        if (saved != null)
        {
            if (saved < ItemCanisterGeneric.EMPTY_CAPACITY)
            {
                ItemCanisterLiquidOxygen.craftingvalues.remove(itemstack);
                itemstack.setDamage(saved);
                return itemstack.copy();
            }
            ItemStack stack = new ItemStack(this.getContainerItem(), 1);
            stack.setDamage(ItemCanisterGeneric.EMPTY_CAPACITY);
            return stack;
        }
        if (GCCoreUtil.getEffectiveSide() == LogicalSide.CLIENT)
        {
            return itemstack.copy();
        }
        return super.getContainerItem(itemstack);
    }

    @Override
    public int discharge(ItemStack itemStack, int amount)
    {
        int damage = itemStack.getDamage();
        int used = Math.min((int) (amount * Constants.LOX_GAS_RATIO), ItemCanisterGeneric.EMPTY_CAPACITY - damage);
        this.setNewDamage(itemStack, damage + used);
        return (int) Math.floor(used / Constants.LOX_GAS_RATIO);
    }

    @Override
    public int getOxygenStored(ItemStack par1ItemStack)
    {
        return ItemCanisterGeneric.EMPTY_CAPACITY - par1ItemStack.getDamage();
    }

    @Override
    public EnumSortCategory getCategory()
    {
        return EnumSortCategory.CANISTER;
    }
}
