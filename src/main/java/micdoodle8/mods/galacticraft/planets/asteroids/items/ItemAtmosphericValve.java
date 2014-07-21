package micdoodle8.mods.galacticraft.planets.asteroids.items;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.proxy.ClientProxyCore;
import micdoodle8.mods.galacticraft.planets.asteroids.AsteroidsModule;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.*;

public class ItemAtmosphericValve extends Item
{
	public ItemAtmosphericValve(String assetName)
	{
		super();
		this.setMaxDamage(0);
		this.setUnlocalizedName(assetName);
		this.setMaxStackSize(64);
		this.setTextureName(AsteroidsModule.TEXTURE_PREFIX + assetName);
	}

	@SideOnly(Side.CLIENT)
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

   @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister iconRegister)
    {
        this.itemIcon = iconRegister.registerIcon(this.getIconString());
    }
}