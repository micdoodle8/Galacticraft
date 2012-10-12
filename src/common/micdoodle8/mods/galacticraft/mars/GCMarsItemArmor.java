package micdoodle8.mods.galacticraft.mars;

import cpw.mods.fml.common.Side;
import cpw.mods.fml.common.asm.SideOnly;
import micdoodle8.mods.galacticraft.core.GCItemBreathableHelmet;
import net.minecraft.src.CreativeTabs;
import net.minecraft.src.EnumArmorMaterial;
import net.minecraft.src.EnumRarity;
import net.minecraft.src.ItemStack;
import net.minecraftforge.common.IArmorTextureProvider;

public class GCMarsItemArmor extends GCItemBreathableHelmet implements IArmorTextureProvider
{
	public boolean attachedMask;
	private EnumArmorMaterial material;
	
	public GCMarsItemArmor(int par1, EnumArmorMaterial par2EnumArmorMaterial, int par3, int par4, boolean breathable) 
	{
		super(par1, par2EnumArmorMaterial, par3, par4);
		this.material = par2EnumArmorMaterial;
		this.setCreativeTab(CreativeTabs.tabCombat);
		this.attachedMask = breathable;
	}

    public String getArmorTextureFile(ItemStack itemstack)
    {
    	if (material == GCMarsItems.QUANDRIUMARMOR)
    	{
    		if (itemstack.getItem().shiftedIndex == GCMarsItems.quandriumHelmet.shiftedIndex)
    		{
    			return "/micdoodle8/mods/galacticraft/mars/client/armor/quandrium_1.png";
    		}
    		else if (itemstack.getItem().shiftedIndex == GCMarsItems.quandriumChestplate.shiftedIndex || itemstack.getItem().shiftedIndex == GCMarsItems.quandriumBoots.shiftedIndex)
    		{
    			return "/micdoodle8/mods/galacticraft/mars/client/armor/quandrium_2.png";
    		}
    		else if (itemstack.getItem().shiftedIndex == GCMarsItems.quandriumLeggings.shiftedIndex)
    		{
    			return "/micdoodle8/mods/galacticraft/mars/client/armor/quandrium_3.png";
    		}
    		else if (itemstack.getItem().shiftedIndex == GCMarsItems.quandriumHelmetBreathable.shiftedIndex)
    		{
    			return "/micdoodle8/mods/galacticraft/mars/client/armor/quandriumox_1.png";
    		}
    	}
    	else if (material == GCMarsItems.ARMORDESH)
    	{
    		if (itemstack.getItem().shiftedIndex == GCMarsItems.deshHelmet.shiftedIndex)
    		{
    			return "/micdoodle8/mods/galacticraft/mars/client/armor/desh_1.png";
    		}
    		else if (itemstack.getItem().shiftedIndex == GCMarsItems.deshChestplate.shiftedIndex || itemstack.getItem().shiftedIndex == GCMarsItems.deshBoots.shiftedIndex)
    		{
    			return "/micdoodle8/mods/galacticraft/mars/client/armor/desh_2.png";
    		}
    		else if (itemstack.getItem().shiftedIndex == GCMarsItems.deshLeggings.shiftedIndex)
    		{
    			return "/micdoodle8/mods/galacticraft/mars/client/armor/desh_3.png";
    		}
    		else if (itemstack.getItem().shiftedIndex == GCMarsItems.deshHelmetBreathable.shiftedIndex)
    		{
    			return "/micdoodle8/mods/galacticraft/mars/client/armor/deshox_1.png";
    		}
    	}
    	else if (material == GCMarsItems.TITANIUMARMOR)
    	{
    		if (itemstack.getItem().shiftedIndex == GCMarsItems.titaniumHelmet.shiftedIndex)
    		{
    			return "/micdoodle8/mods/galacticraft/mars/client/armor/titanium_1.png";
    		}
    		else if (itemstack.getItem().shiftedIndex == GCMarsItems.titaniumChestplate.shiftedIndex || itemstack.getItem().shiftedIndex == GCMarsItems.titaniumBoots.shiftedIndex)
    		{
    			return "/micdoodle8/mods/galacticraft/mars/client/armor/titanium_2.png";
    		}
    		else if (itemstack.getItem().shiftedIndex == GCMarsItems.titaniumLeggings.shiftedIndex)
    		{
    			return "/micdoodle8/mods/galacticraft/mars/client/armor/titanium_3.png";
    		}
    		else if (itemstack.getItem().shiftedIndex == GCMarsItems.titaniumHelmetBreathable.shiftedIndex)
    		{
    			return "/micdoodle8/mods/galacticraft/mars/client/armor/titaniumox_1.png";
    		}
    	}
    	else if (material == GCMarsItems.ARMORHEAVY)
    	{
    		if (itemstack.getItem().shiftedIndex == GCMarsItems.heavyBoots.shiftedIndex)
    		{
    			return "/micdoodle8/mods/galacticraft/mars/client/armor/heavy_1.png";
    		}
    	}

    	return null;
    }
	
	public String getTextureFile()
	{
		return "/micdoodle8/mods/galacticraft/mars/client/items/core.png";
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
