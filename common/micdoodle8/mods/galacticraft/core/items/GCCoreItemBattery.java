package micdoodle8.mods.galacticraft.core.items;

import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.client.ClientProxyCore;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemStack;
import universalelectricity.core.item.ItemElectric;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class GCCoreItemBattery extends ItemElectric
{
	public GCCoreItemBattery(int id)
	{
		super(id);
	}

    @Override
    public CreativeTabs getCreativeTab()
    {
        return GalacticraftCore.galacticraftTab;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public EnumRarity getRarity(ItemStack par1ItemStack)
    {
        return ClientProxyCore.galacticraftItem;
    }

	@SideOnly(Side.CLIENT)
	@Override
	public void registerIcons(IconRegister iconRegister)
	{
		this.itemIcon = iconRegister.registerIcon(GalacticraftCore.TEXTURE_PREFIX + this.getUnlocalizedName().replace("item.", ""));
	}

	@Override
	public float getMaxElectricityStored(ItemStack itemStack)
	{
		return 5000;
	}

	@Override
	public float getVoltage(ItemStack itemStack)
	{
		return 120;
	}
}
