package micdoodle8.mods.galacticraft.core.blocks;

import micdoodle8.mods.galacticraft.core.GCCoreConfigManager;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class GCCoreBlockAdvancedCraftingTable extends Block
{
	public GCCoreBlockAdvancedCraftingTable(int par1)
	{
		super(par1, Material.wood);
		this.blockIndexInTexture = 19;
        this.setCreativeTab(GalacticraftCore.galacticraftTab);
        this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.1F, 1.0F);
	}
	
	@Override
    public int getRenderType()
    {
        return GalacticraftCore.proxy.getGCCraftingTableRenderID();
    }

	@Override
	public int getBlockTextureFromSide(int par1)
    {
        return par1 == 1 ? this.blockIndexInTexture + 1 : par1 == 0 ? this.blockIndexInTexture - 1 : this.blockIndexInTexture;
    }
	
	@Override
	public boolean onBlockActivated(World par1World, int par2, int par3, int par4, EntityPlayer par5EntityPlayer, int par6, float par7, float par8, float par9)
    {
		par5EntityPlayer.openGui(GalacticraftCore.instance, GCCoreConfigManager.idGuiRocketCraftingBench, par1World, par2, par3, par4);
		return true;
    }

	@Override
    public void setBlockBoundsBasedOnState(IBlockAccess par1IBlockAccess, int par2, int par3, int par4)
    {
    	this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.1F, 1.0F);
    }

	@Override
	public String getTextureFile()
	{
		return "/micdoodle8/mods/galacticraft/core/client/blocks/core.png";
	}
}
