package micdoodle8.mods.galacticraft.core.items;

import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import net.minecraft.src.EnumRarity;
import net.minecraft.src.EnumToolMaterial;
import net.minecraft.src.ItemStack;
import net.minecraft.src.ItemSword;
import cpw.mods.fml.common.Side;
import cpw.mods.fml.common.asm.SideOnly;

public class GCCoreItemSword extends ItemSword
{
	private final EnumToolMaterial material;
	
	public GCCoreItemSword(int par1, EnumToolMaterial par2EnumToolMaterial) 
	{
		super(par1, par2EnumToolMaterial);
		this.material = par2EnumToolMaterial;
		this.setCreativeTab(GalacticraftCore.galacticraftTab);
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
