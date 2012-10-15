package micdoodle8.mods.galacticraft.mars;

import cpw.mods.fml.common.Side;
import cpw.mods.fml.common.asm.SideOnly;
import net.minecraft.src.CreativeTabs;
import net.minecraft.src.EnumRarity;
import net.minecraft.src.EnumToolMaterial;
import net.minecraft.src.ItemAxe;
import net.minecraft.src.ItemStack;

public class GCMarsItemAxe extends ItemAxe
{
	private EnumToolMaterial material;
	
	public GCMarsItemAxe(int par1, EnumToolMaterial par2EnumToolMaterial) 
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
