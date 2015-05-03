package micdoodle8.mods.galacticraft.core.items;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import micdoodle8.mods.galacticraft.api.item.IItemOxygenSupply;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.proxy.ClientProxyCore;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class ItemCanisterOxygenInfinite extends Item implements IItemOxygenSupply
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
    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister iconRegister)
    {
        this.itemIcon = iconRegister.registerIcon(GalacticraftCore.TEXTURE_PREFIX + "oxygenCanisterInfinite");
    }

    @Override
    public CreativeTabs getCreativeTab()
    {
        return GalacticraftCore.galacticraftItemsTab;
    }

    @Override
    public ItemStack getContainerItem(ItemStack itemstack)
    {
        return itemstack;
    }

	@Override
	public float discharge(ItemStack itemStack, float amount)
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
}
