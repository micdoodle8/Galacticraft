package micdoodle8.mods.galacticraft.core.blocks;

import net.minecraft.world.World;
import universalelectricity.core.vector.Vector3;
import universalelectricity.prefab.multiblock.BlockMulti;
import universalelectricity.prefab.multiblock.TileEntityMulti;

public class GCCoreBlockMulti extends BlockMulti
{
	public GCCoreBlockMulti(int id) 
	{
		super(id);
	}

	@Override
	public void makeFakeBlock(World worldObj, Vector3 position, Vector3 mainBlock)
	{
		this.makeFakeBlock(worldObj, position, mainBlock, 0);
	}

	public void makeFakeBlock(World worldObj, Vector3 position, Vector3 mainBlock, int meta)
	{
		worldObj.setBlockAndMetadataWithNotify(position.intX(), position.intY(), position.intZ(), this.blockID, meta, 2);
		((TileEntityMulti) worldObj.getBlockTileEntity(position.intX(), position.intY(), position.intZ())).setMainBlock(mainBlock);
	}
	
    public float getBlockHardness(World par1World, int par2, int par3, int par4)
    {
        return par1World.getBlockMetadata(par2, par3, par4) == 0 ? this.blockHardness : -1.0F;
    }
}
