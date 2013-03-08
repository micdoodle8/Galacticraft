package micdoodle8.mods.galacticraft.mars.items;

import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import net.minecraft.item.EnumArmorMaterial;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.IArmorTextureProvider;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class GCMarsItemArmor extends ItemArmor implements IArmorTextureProvider
{
	public boolean attachedMask;
	private final EnumArmorMaterial material;

	public GCMarsItemArmor(int par1, EnumArmorMaterial par2EnumArmorMaterial, int par3, int par4, boolean breathable)
	{
		super(par1, par2EnumArmorMaterial, par3, par4);
		this.material = par2EnumArmorMaterial;
		this.setCreativeTab(GalacticraftCore.galacticraftTab);
		this.attachedMask = breathable;
	}

    @Override
	public String getArmorTextureFile(ItemStack itemstack)
    {
    	if (this.material == GCMarsItems.QUANDRIUMARMOR)
    	{
    		if (itemstack.getItem().itemID == GCMarsItems.quandriumHelmet.itemID)
    		{
    			return "/micdoodle8/mods/galacticraft/mars/client/armor/quandrium_1.png";
    		}
    		else if (itemstack.getItem().itemID == GCMarsItems.quandriumChestplate.itemID || itemstack.getItem().itemID == GCMarsItems.quandriumBoots.itemID)
    		{
    			return "/micdoodle8/mods/galacticraft/mars/client/armor/quandrium_2.png";
    		}
    		else if (itemstack.getItem().itemID == GCMarsItems.quandriumLeggings.itemID)
    		{
    			return "/micdoodle8/mods/galacticraft/mars/client/armor/quandrium_3.png";
    		}
//    		else if (itemstack.getItem().itemID == GCMarsItems.quandriumHelmetBreathable.itemID)
//    		{
//    			return "/micdoodle8/mods/galacticraft/mars/client/armor/quandriumox_1.png";
//    		}
    	}
    	else if (this.material == GCMarsItems.ARMORDESH)
    	{
    		if (itemstack.getItem().itemID == GCMarsItems.deshHelmet.itemID)
    		{
    			return "/micdoodle8/mods/galacticraft/mars/client/armor/desh_1.png";
    		}
    		else if (itemstack.getItem().itemID == GCMarsItems.deshChestplate.itemID || itemstack.getItem().itemID == GCMarsItems.deshBoots.itemID)
    		{
    			return "/micdoodle8/mods/galacticraft/mars/client/armor/desh_2.png";
    		}
    		else if (itemstack.getItem().itemID == GCMarsItems.deshLeggings.itemID)
    		{
    			return "/micdoodle8/mods/galacticraft/mars/client/armor/desh_3.png";
    		}
//    		else if (itemstack.getItem().itemID == GCMarsItems.deshHelmetBreathable.itemID)
//    		{
//    			return "/micdoodle8/mods/galacticraft/mars/client/armor/deshox_1.png";
//    		}
    	}
    	else if (this.material == GCMarsItems.ARMORHEAVY)
    	{
    		if (itemstack.getItem().itemID == GCMarsItems.heavyBoots.itemID)
    		{
    			return "/micdoodle8/mods/galacticraft/mars/client/armor/heavy_1.png";
    		}
    	}

    	return null;
    }

	@Override
	public String getTextureFile()
	{
		return "/micdoodle8/mods/galacticraft/mars/client/items/mars.png";
	}

	@Override
    @SideOnly(Side.CLIENT)
    public EnumRarity getRarity(ItemStack par1ItemStack)
    {
		if (this.material == GCMarsItems.QUANDRIUMARMOR)
		{
	        return EnumRarity.epic;
		}

        return EnumRarity.rare;
    }
}
