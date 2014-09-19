package micdoodle8.mods.galacticraft.core.blocks;

import micdoodle8.mods.galacticraft.api.vector.BlockVec3;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.items.ItemBlockDesc;
import micdoodle8.mods.galacticraft.core.tile.TileEntityArclamp;
import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.IIcon;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

public class BlockBrightLamp extends BlockAdvanced implements ItemBlockDesc.IBlockShiftDesc
{
    public static IIcon icon;

    //Metadata: bits 0-2 are the side of the base plate using standard side convention (0-5)

    protected BlockBrightLamp(String assetName)
    {
        super(Material.glass);
        this.setHardness(0.1F);
        this.setStepSound(Block.soundTypeWood);
        this.setBlockTextureName("stone");
        this.setBlockName(assetName);
        this.setLightLevel(1.0F);
    }

    @Override
    public AxisAlignedBB getCollisionBoundingBoxFromPool(World par1World, int x, int y, int z)
    {
        double boundsMin = 0.2D;
        double boundsMax = 0.8D;
        return AxisAlignedBB.getBoundingBox(x + boundsMin, y + boundsMin, z + boundsMin, x + boundsMax, y + boundsMax, z + boundsMax);
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

    @Override
    public int getRenderType()
    {
        return GalacticraftCore.proxy.getBlockRender(this);
    }

    @Override
    public boolean canPlaceBlockAt(World par1World, int x, int y, int z)
    {
        BlockVec3 thisvec = new BlockVec3(x, y, z);
        for (int i = 0; i < 6; i++)
        {
            if (thisvec.blockOnSideHasSolidFace(par1World, i))
            {
                return true;
            }
        }
        return false;
    }

    @Override
    public int onBlockPlaced(World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ, int metaOld)
    {
        BlockVec3 thisvec = new BlockVec3(x, y, z);

        if (thisvec.blockOnSideHasSolidFace(world, side ^ 1))
        {
            return side ^ 1;
        }

        return metaOld;
    }

    /**
     * Lets the block know when one of its neighbor changes. Doesn't know which
     * neighbor changed (coordinates passed are their own) Args: x, y, z,
     * neighbor blockID
     */
    @Override
    public void onNeighborBlockChange(World par1World, int x, int y, int z, Block par5)
    {
        final int side = par1World.getBlockMetadata(x, y, z);

        BlockVec3 thisvec = new BlockVec3(x, y, z);

        if (thisvec.blockOnSideHasSolidFace(par1World, side))
        {
            return;
        }

        this.dropBlockAsItem(par1World, x, y, z, 0, 0);
        par1World.setBlock(x, y, z, Blocks.air);
    }

    /**
     * Ray traces through the blocks collision from start vector to end vector
     * returning a ray trace hit. Args: world, x, y, z, startVec, endVec
     */
    @Override
    public MovingObjectPosition collisionRayTrace(World par1World, int x, int y, int z, Vec3 par5Vec3, Vec3 par6Vec3)
    {
        final int var7 = par1World.getBlockMetadata(x, y, z);
        float var8 = 0.3F;

        if (var7 == 4)
        {
            this.setBlockBounds(0.0F, 0.2F, 0.5F - var8, var8 * 2.0F, 0.8F, 0.5F + var8);
        }
        else if (var7 == 5)
        {
            this.setBlockBounds(1.0F - var8 * 2.0F, 0.2F, 0.5F - var8, 1.0F, 0.8F, 0.5F + var8);
        }
        else if (var7 == 2)
        {
            this.setBlockBounds(0.5F - var8, 0.2F, 0.0F, 0.5F + var8, 0.8F, var8 * 2.0F);
        }
        else if (var7 == 3)
        {
            this.setBlockBounds(0.5F - var8, 0.2F, 1.0F - var8 * 2.0F, 0.5F + var8, 0.8F, 1.0F);
        }
        else if (var7 == 0)
        {
            this.setBlockBounds(0.5F - var8, 0.0F, 0.5F - var8, 0.5F + var8, 0.6F, 0.5F + var8);
        }
        else
        {
            this.setBlockBounds(0.5F - var8, 0.4F, 0.5F - var8, 0.5F + var8, 1.0F, 0.5F + var8);
        }

        return super.collisionRayTrace(par1World, x, y, z, par5Vec3, par6Vec3);
    }

    @Override
    public boolean onUseWrench(World world, int x, int y, int z, EntityPlayer entityPlayer, int side, float hitX, float hitY, float hitZ)
    {
        if (!world.isRemote)
        {
	    	TileEntity tile = world.getTileEntity(x, y, z);
	        if (tile instanceof TileEntityArclamp)
	        {
	            ((TileEntityArclamp) tile).facingChanged();
	        }
        }
        return true;
    }

    @Override
    public TileEntity createNewTileEntity(World world, int meta)
    {
        return new TileEntityArclamp();
    }

    @Override
    public CreativeTabs getCreativeTabToDisplayOn()
    {
        return GalacticraftCore.galacticraftBlocksTab;
    }

    @Override
    public String getShiftDescription(int meta)
    {
        return GCCoreUtil.translate(this.getUnlocalizedName() + ".description");
    }

    @Override
    public boolean showDescription(int meta)
    {
        return true;
    }
}
