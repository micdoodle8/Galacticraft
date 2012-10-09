package micdoodle8.mods.galacticraft;

import cpw.mods.fml.common.Side;
import cpw.mods.fml.common.asm.SideOnly;
import net.minecraft.src.CreativeTabs;
import net.minecraft.src.EnumArmorMaterial;
import net.minecraft.src.EnumRarity;
import net.minecraft.src.ItemStack;
import net.minecraftforge.common.IArmorTextureProvider;

public class GCItemArmor extends GCItemBreathableHelmet implements IArmorTextureProvider
{
	public boolean attachedMask;
	private EnumArmorMaterial material;
	
	public GCItemArmor(int par1, EnumArmorMaterial par2EnumArmorMaterial, int par3, int par4, boolean breathable) 
	{
		super(par1, par2EnumArmorMaterial, par3, par4);
		this.material = par2EnumArmorMaterial;
		this.setCreativeTab(CreativeTabs.tabCombat);
		this.attachedMask = breathable;
	}

    public String getArmorTextureFile(ItemStack itemstack)
    {
    	if (material == GCItems.QUANDRIUMARMOR)
    	{
    		if (itemstack.getItem().shiftedIndex == GCItems.quandriumHelmet.shiftedIndex)
    		{
    			return "/micdoodle8/mods/galacticraft/client/armor/quandrium_1.png";
    		}
    		else if (itemstack.getItem().shiftedIndex == GCItems.quandriumChestplate.shiftedIndex || itemstack.getItem().shiftedIndex == GCItems.quandriumBoots.shiftedIndex)
    		{
    			return "/micdoodle8/mods/galacticraft/client/armor/quandrium_2.png";
    		}
    		else if (itemstack.getItem().shiftedIndex == GCItems.quandriumLeggings.shiftedIndex)
    		{
    			return "/micdoodle8/mods/galacticraft/client/armor/quandrium_3.png";
    		}
    		else if (itemstack.getItem().shiftedIndex == GCItems.quandriumHelmetBreathable.shiftedIndex)
    		{
    			return "/micdoodle8/mods/galacticraft/client/armor/quandriumox_1.png";
    		}
    	}
    	else if (material == GCItems.ARMORDESH)
    	{
    		if (itemstack.getItem().shiftedIndex == GCItems.deshHelmet.shiftedIndex)
    		{
    			return "/micdoodle8/mods/galacticraft/client/armor/desh_1.png";
    		}
    		else if (itemstack.getItem().shiftedIndex == GCItems.deshChestplate.shiftedIndex || itemstack.getItem().shiftedIndex == GCItems.deshBoots.shiftedIndex)
    		{
    			return "/micdoodle8/mods/galacticraft/client/armor/desh_2.png";
    		}
    		else if (itemstack.getItem().shiftedIndex == GCItems.deshLeggings.shiftedIndex)
    		{
    			return "/micdoodle8/mods/galacticraft/client/armor/desh_3.png";
    		}
    		else if (itemstack.getItem().shiftedIndex == GCItems.deshHelmetBreathable.shiftedIndex)
    		{
    			return "/micdoodle8/mods/galacticraft/client/armor/deshox_1.png";
    		}
    	}
    	else if (material == GCItems.TITANIUMARMOR)
    	{
    		if (itemstack.getItem().shiftedIndex == GCItems.titaniumHelmet.shiftedIndex)
    		{
    			return "/micdoodle8/mods/galacticraft/client/armor/titanium_1.png";
    		}
    		else if (itemstack.getItem().shiftedIndex == GCItems.titaniumChestplate.shiftedIndex || itemstack.getItem().shiftedIndex == GCItems.titaniumBoots.shiftedIndex)
    		{
    			return "/micdoodle8/mods/galacticraft/client/armor/titanium_2.png";
    		}
    		else if (itemstack.getItem().shiftedIndex == GCItems.titaniumLeggings.shiftedIndex)
    		{
    			return "/micdoodle8/mods/galacticraft/client/armor/titanium_3.png";
    		}
    		else if (itemstack.getItem().shiftedIndex == GCItems.titaniumHelmetBreathable.shiftedIndex)
    		{
    			return "/micdoodle8/mods/galacticraft/client/armor/titaniumox_1.png";
    		}
    	}
    	else if (material == GCItems.ARMORHEAVY)
    	{
    		if (itemstack.getItem().shiftedIndex == GCItems.heavyBoots.shiftedIndex)
    		{
    			return "/micdoodle8/mods/galacticraft/client/armor/heavy_1.png";
    		}
    	}

    	return null;
    }
	
	public String getTextureFile()
	{
		return "/micdoodle8/mods/galacticraft/client/items/core.png";
	}

	@Override
    @SideOnly(Side.CLIENT)
    public EnumRarity getRarity(ItemStack par1ItemStack)
    {
		if (this.material == GCItems.QUANDRIUMARMOR)
		{
	        return EnumRarity.epic;
		}

        return EnumRarity.rare;
    }
}
