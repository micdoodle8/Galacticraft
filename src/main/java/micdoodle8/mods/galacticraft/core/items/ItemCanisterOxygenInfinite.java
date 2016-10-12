package micdoodle8.mods.galacticraft.core.items;

import micdoodle8.mods.galacticraft.core.util.EnumColor;
import micdoodle8.mods.galacticraft.core.util.EnumSortCategoryItem;
import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import micdoodle8.mods.galacticraft.api.item.IItemOxygenSupply;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.proxy.ClientProxyCore;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import java.util.List;

public class ItemCanisterOxygenInfinite extends Item implements IItemOxygenSupply, ISortableItem
{
    public ItemCanisterOxygenInfinite(String assetName)
    {
        super();
        this.setMaxDamage(ItemCanisterGeneric.EMPTY);
        this.setMaxStackSize(1);
        this.setNoRepair();
        this.setUnlocalizedName(assetName);
        this.setContainerItem(GCItems.oilCanister);
    }

    @Override
    public boolean hasEffect(ItemStack stack)
    {
        return true;
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Override
    public void addInformation(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, List<String> tooltip, boolean par4)
    {
        tooltip.add(EnumColor.DARK_GREEN + GCCoreUtil.translate("gui.infinite_item.desc"));
        tooltip.add(EnumColor.RED + GCCoreUtil.translate("gui.creative_only.desc"));
    }

    /*@Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister iconRegister)
    {
        this.itemIcon = iconRegister.registerIcon(GalacticraftCore.TEXTURE_PREFIX + "oxygenCanisterInfinite");
    }*/

    @Override
    public CreativeTabs getCreativeTab()
    {
        return GalacticraftCore.galacticraftItemsTab;
    }

    @Override
    public ItemStack getContainerItem(ItemStack itemstack)
    {
        if (super.getContainerItem(itemstack) == null)
        	return null;
    	return itemstack;
    }

	@Override
	public int discharge(ItemStack itemStack, int amount)
	{
		return amount;
	}

	@Override
	public int getOxygenStored(ItemStack par1ItemStack)
	{
		return par1ItemStack.getMaxDamage();
	}
	
	@Override
    	@SideOnly(Side.CLIENT)
    	public EnumRarity getRarity(ItemStack par1ItemStack)
    	{
        	return ClientProxyCore.galacticraftItem;
    	}

    @Override
    public EnumSortCategoryItem getCategory(int meta)
    {
        return EnumSortCategoryItem.GEAR;
    }
}
