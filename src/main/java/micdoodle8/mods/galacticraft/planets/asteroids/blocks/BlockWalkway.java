package micdoodle8.mods.galacticraft.planets.asteroids.blocks;

import micdoodle8.mods.galacticraft.planets.GalacticraftPlanets;
import micdoodle8.mods.galacticraft.planets.asteroids.AsteroidsModule;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.world.World;

public class BlockWalkway extends Block
{
	protected BlockWalkway(String assetName)
	{
		super(Material.iron);
		this.setHardness(1.0F);
		this.setBlockTextureName(AsteroidsModule.TEXTURE_DOMAIN + assetName);
		this.setBlockName(assetName);
	}

	@Override
	public CreativeTabs getCreativeTabToDisplayOn()
	{
		return AsteroidsModule.asteroidsTab;
	}

	@Override
	public int getRenderType()
	{
		return GalacticraftPlanets.getBlockRenderID(this);
	}

	@Override
	public boolean isOpaqueCube()
	{
		return false;
	}

	@Override
	public boolean renderAsNormalBlock()
	{
		return false;
	}
	
    public int onBlockPlaced(World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ, int meta)
    {
        return getWalkwayOrientation(world, x, y, z);
    }
    
    public void onNeighborBlockChange(World world, int x, int y, int z, Block blockChanged) 
    {
    	world.setBlock(x, y, z, this, this.getWalkwayOrientation(world, x, y, z), 3);
    }
    
    public int getWalkwayOrientation(World world, int x, int y, int z)
    {
    	int connectedNorth = world.getBlock(x, y, z - 1).isAir(world, x, y, z - 1) ? 0 : 1;
    	int connectedEast = world.getBlock(x + 1, y, z).isAir(world, x + 1, y, z) ? 0 : 2;
    	int connectedSouth = world.getBlock(x, y, z + 1).isAir(world, x, y, z + 1) ? 0 : 4;
    	int connectedWest = world.getBlock(x - 1, y, z).isAir(world, x - 1, y, z) ? 0 : 8;
    	
    	int combined = connectedNorth | connectedEast | connectedSouth | connectedWest;
    	return combined == 15 ? combined : combined;
    }
}
