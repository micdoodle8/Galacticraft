package micdoodle8.mods.galacticraft.core.items;

import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import net.minecraft.item.EnumArmorMaterial;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.IArmorTextureProvider;
import cpw.mods.fml.relauncher.SideOnly;
import cpw.mods.fml.relauncher.Side;

public class GCCoreItemArmor extends GCCoreItemBreathableHelmet implements IArmorTextureProvider
{
	public boolean attachedMask;
	private final EnumArmorMaterial material;
	
	public GCCoreItemArmor(int par1, EnumArmorMaterial par2EnumArmorMaterial, int par3, int par4, boolean breathable) 
	{
		super(par1, par2EnumArmorMaterial, par3, par4);
		this.material = par2EnumArmorMaterial;
		this.setCreativeTab(GalacticraftCore.galacticraftTab);
		this.attachedMask = breathable;
	}

	@Override
    public String getArmorTextureFile(ItemStack itemstack)
    {
    	if (this.material == GCCoreItems.TITANIUMARMOR)
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

	@Override
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
