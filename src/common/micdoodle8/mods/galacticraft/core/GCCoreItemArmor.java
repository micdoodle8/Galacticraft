package micdoodle8.mods.galacticraft.core;

import cpw.mods.fml.common.Side;
import cpw.mods.fml.common.asm.SideOnly;
import micdoodle8.mods.galacticraft.core.GCItemBreathableHelmet;
import net.minecraft.src.CreativeTabs;
import net.minecraft.src.EnumArmorMaterial;
import net.minecraft.src.EnumRarity;
import net.minecraft.src.ItemStack;
import net.minecraftforge.common.IArmorTextureProvider;

public class GCCoreItemArmor extends GCItemBreathableHelmet implements IArmorTextureProvider
{
	public boolean attachedMask;
	private EnumArmorMaterial material;
	
	public GCCoreItemArmor(int par1, EnumArmorMaterial par2EnumArmorMaterial, int par3, int par4, boolean breathable) 
	{
		super(par1, par2EnumArmorMaterial, par3, par4);
		this.material = par2EnumArmorMaterial;
		this.setCreativeTab(CreativeTabs.tabCombat);
		this.attachedMask = breathable;
	}

    public String getArmorTextureFile(ItemStack itemstack)
    {
    	if (material == GCCoreItems.TITANIUMARMOR)
    	{
    		if (itemstack.getItem().shiftedIndex == GCCoreItems.titaniumHelmet.shiftedIndex)
    		{
    			return "/micdoodle8/mods/galacticraft/core/client/armor/titanium_1.png";
    		}
    		else if (itemstack.getItem().shiftedIndex == GCCoreItems.titaniumChestplate.shiftedIndex || itemstack.getItem().shiftedIndex == GCCoreItems.titaniumBoots.shiftedIndex)
    		{
    			return "/micdoodle8/mods/galacticraft/core/client/armor/titanium_2.png";
    		}
    		else if (itemstack.getItem().shiftedIndex == GCCoreItems.titaniumLeggings.shiftedIndex)
    		{
    			return "/micdoodle8/mods/galacticraft/core/client/armor/titanium_3.png";
    		}
    		else if (itemstack.getItem().shiftedIndex == GCCoreItems.titaniumHelmetBreathable.shiftedIndex)
    		{
    			return "/micdoodle8/mods/galacticraft/core/client/armor/titaniumox_1.png";
    		}
    	}

    	return null;
    }
	
	public String getTextureFile()
	{
		return "/micdoodle8/mods/galacticraft/core/client/items/core.png";
	}

	@Override
    @SideOnly(Side.CLIENT)
    public EnumRarity getRarity(ItemStack par1ItemStack)
    {
        return EnumRarity.rare;
    }
}
