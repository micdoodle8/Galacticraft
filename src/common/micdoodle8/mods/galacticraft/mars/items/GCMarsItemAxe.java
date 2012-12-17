package micdoodle8.mods.galacticraft.mars.items;

import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import net.minecraft.src.EnumRarity;
import net.minecraft.src.EnumToolMaterial;
import net.minecraft.src.ItemAxe;
import net.minecraft.src.ItemStack;
import cpw.mods.fml.common.Side;
import cpw.mods.fml.common.asm.SideOnly;

public class GCMarsItemAxe extends ItemAxe
{
	private final EnumToolMaterial material;
	
	public GCMarsItemAxe(int par1, EnumToolMaterial par2EnumToolMaterial) 
	{
		super(par1, par2EnumToolMaterial);
		this.material = par2EnumToolMaterial;
		this.setCreativeTab(GalacticraftCore.galacticraftTab);
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
