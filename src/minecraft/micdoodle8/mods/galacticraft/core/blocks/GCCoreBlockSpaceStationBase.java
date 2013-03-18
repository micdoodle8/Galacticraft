package micdoodle8.mods.galacticraft.core.blocks;

import micdoodle8.mods.galacticraft.core.tile.GCCoreTileEntitySpaceStationBase;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class GCCoreBlockSpaceStationBase extends BlockContainer
{
	public GCCoreBlockSpaceStationBase(int par1) 
	{
		super(par1, Material.rock);
	}

	@Override
	public TileEntity createNewTileEntity(World world) 
	{
		return new GCCoreTileEntitySpaceStationBase();
	}
}
