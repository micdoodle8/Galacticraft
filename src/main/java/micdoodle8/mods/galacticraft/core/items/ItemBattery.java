package micdoodle8.mods.galacticraft.core.items;

import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.energy.item.ItemElectricBase;
import micdoodle8.mods.galacticraft.core.proxy.ClientProxyCore;
import micdoodle8.mods.galacticraft.core.util.EnumSortCategoryItem;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemBattery extends ItemElectricBase implements ISortableItem
{
    public ItemBattery(String assetName)
    {
        super();
        this.setMaxStackSize(4);
        this.setUnlocalizedName(assetName);
        //this.setTextureName(Constants.TEXTURE_PREFIX + assetName);
    }

    @Override
    public CreativeTabs getCreativeTab()
    {
        return GalacticraftCore.galacticraftItemsTab;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public EnumRarity getRarity(ItemStack par1ItemStack)
    {
        return ClientProxyCore.galacticraftItem;
    }

    @Override
    public float getMaxElectricityStored(ItemStack itemStack)
    {
        return 15000;
    }

    @Override
    public EnumSortCategoryItem getCategory(int meta)
    {
        return EnumSortCategoryItem.GENERAL;
    }

    @Override
    public int getItemStackLimit(ItemStack stack)
    {
        if (stack.getItemDamage() < 100 || stack.hasTagCompound() && stack.getTagCompound().hasKey("electricity"))
        {
            return 1;
        }
        return this.getItemStackLimit();
    }
}
