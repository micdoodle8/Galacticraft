package micdoodle8.mods.galacticraft.mars;

import cpw.mods.fml.common.Side;
import cpw.mods.fml.common.asm.SideOnly;
import net.minecraft.src.CreativeTabs;
import net.minecraft.src.EnumRarity;
import net.minecraft.src.EnumToolMaterial;
import net.minecraft.src.ItemHoe;
import net.minecraft.src.ItemStack;

public class GCMarsItemHoe extends ItemHoe
{
	private EnumToolMaterial material;
	
	public GCMarsItemHoe(int par1, EnumToolMaterial par2EnumToolMaterial) 
	{
		super(par1, par2EnumToolMaterial);
		this.material = par2EnumToolMaterial;
		this.setCreativeTab(CreativeTabs.tabTools);
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
		if (this.material == GCMarsItems.TOOLQUANDRIUM)
		{
	        return EnumRarity.epic;
		}

        return EnumRarity.rare;
    }
}
