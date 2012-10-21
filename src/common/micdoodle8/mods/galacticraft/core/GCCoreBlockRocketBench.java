package micdoodle8.mods.galacticraft.core;

import java.util.ArrayList;
import net.minecraft.src.*;

import cpw.mods.fml.common.ICraftingHandler;

public class GCCoreBlockRocketBench extends Block
{
	public GCCoreBlockRocketBench(int par1)
	{
		super(par1, Material.wood);
		this.blockIndexInTexture = 19;
        this.setCreativeTab(CreativeTabs.tabDecorations);
	}

	@Override
	public int getBlockTextureFromSide(int par1)
    {
        return par1 == 1 ? this.blockIndexInTexture + 1 : (par1 == 0 ? this.blockIndexInTexture - 1 : this.blockIndexInTexture);
    }
	
	@Override
	public boolean onBlockActivated(World par1World, int par2, int par3, int par4, EntityPlayer par5EntityPlayer, int par6, float par7, float par8, float par9)
    {
		par5EntityPlayer.openGui(GalacticraftCore.instance, GCCoreConfigManager.idGuiRocketCraftingBench, par1World, par2, par3, par4);
		return true;
    }

	@Override
	public String getTextureFile() 
	{
		return "/micdoodle8/mods/galacticraft/core/client/blocks/core.png";
	}
}
