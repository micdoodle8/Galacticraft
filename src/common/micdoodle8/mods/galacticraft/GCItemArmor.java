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
    	if (material == GCItems.QUANDRIUM)
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
		if (this.material == GCItems.QUANDRIUM)
		{
	        return EnumRarity.epic;
		}

        return EnumRarity.rare;
    }
}
