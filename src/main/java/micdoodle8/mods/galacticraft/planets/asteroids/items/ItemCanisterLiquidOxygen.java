package micdoodle8.mods.galacticraft.planets.asteroids.items;

import micdoodle8.mods.galacticraft.api.item.IItemOxygenSupply;
import micdoodle8.mods.galacticraft.core.Constants;
import micdoodle8.mods.galacticraft.core.items.ISortableItem;
import micdoodle8.mods.galacticraft.core.items.ItemCanisterGeneric;
import micdoodle8.mods.galacticraft.core.util.EnumSortCategoryItem;
import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.HashMap;
import java.util.List;

public class ItemCanisterLiquidOxygen extends ItemCanisterGeneric implements IItemOxygenSupply, ISortableItem
{
    //    protected IIcon[] icons = new IIcon[7];
    private static HashMap<ItemStack, Integer> craftingvalues = new HashMap<>();

    public ItemCanisterLiquidOxygen(String assetName)
    {
        super(assetName);
        this.setAllowedFluid("liquidoxygen");
        //this.setTextureName(GalacticraftPlanets.TEXTURE_PREFIX + assetName);
    }

    /*@Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister iconRegister)
    {
        for (int i = 0; i < this.icons.length; i++)
        {
            this.icons[i] = iconRegister.registerIcon(this.getIconString() + "_" + i);
        }
    }*/

    @Override
    public String getUnlocalizedName(ItemStack itemStack)
    {
        if (ItemCanisterGeneric.EMPTY - itemStack.getItemDamage() == 0)
        {
            return "item.empty_gas_canister";
        }

        if (itemStack.getItemDamage() == 1)
        {
            return "item.canister.lox.full";
        }

        return "item.canister.lox.partial";
    }

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
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, List<String> tooltip, boolean par4)
    {
        if (ItemCanisterGeneric.EMPTY - par1ItemStack.getItemDamage() > 0)
        {
            tooltip.add(GCCoreUtil.translate("item.canister.lox.name") + ": " + (ItemCanisterGeneric.EMPTY - par1ItemStack.getItemDamage()));
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
            if (saved < ItemCanisterGeneric.EMPTY)
            {
                ItemCanisterLiquidOxygen.craftingvalues.remove(itemstack);
                itemstack.setItemDamage(saved);
                return itemstack;
            }
            return new ItemStack(this.getContainerItem(), 1, ItemCanisterGeneric.EMPTY);
        }
        return super.getContainerItem(itemstack);
    }

    @Override
    public int discharge(ItemStack itemStack, int amount)
    {
        int damage = itemStack.getItemDamage();
        int used = Math.min((int) (amount * Constants.LOX_GAS_RATIO), ItemCanisterGeneric.EMPTY - damage);
        this.setNewDamage(itemStack, damage + used);
        return (int) Math.floor(used / Constants.LOX_GAS_RATIO);
    }

    @Override
    public int getOxygenStored(ItemStack par1ItemStack)
    {
        return ItemCanisterGeneric.EMPTY - par1ItemStack.getItemDamage();
    }

    @Override
    public EnumSortCategoryItem getCategory(int meta)
    {
        return EnumSortCategoryItem.CANISTER;
    }
}
