package micdoodle8.mods.galacticraft.core.blocks;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import micdoodle8.mods.galacticraft.core.tile.GCCoreTileEntitySpaceStationBase;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class GCCoreBlockSpaceStationBase extends BlockContainer
{
	public GCCoreBlockSpaceStationBase(int par1) 
	{
		super(par1, Material.rock);
	}

    @Override
	@SideOnly(Side.CLIENT)
    public void func_94332_a(IconRegister par1IconRegister)
    {
    	this.field_94336_cN = par1IconRegister.func_94245_a("galacticraftcore:deco_aluminium_3");
    }

	@Override
	public TileEntity createNewTileEntity(World world) 
	{
		return new GCCoreTileEntitySpaceStationBase();
	}
}
