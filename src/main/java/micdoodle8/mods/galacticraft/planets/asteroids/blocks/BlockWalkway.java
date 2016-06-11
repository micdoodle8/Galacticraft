package micdoodle8.mods.galacticraft.planets.asteroids.blocks;

import micdoodle8.mods.galacticraft.api.transmission.NetworkType;
import micdoodle8.mods.galacticraft.api.vector.Vector3;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.blocks.BlockTransmitter;
import micdoodle8.mods.galacticraft.core.energy.EnergyUtil;
import micdoodle8.mods.galacticraft.core.items.ItemBlockDesc;
import micdoodle8.mods.galacticraft.core.tile.TileEntityAluminumWire;
import micdoodle8.mods.galacticraft.core.tile.TileEntityOxygenPipe;
import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
import micdoodle8.mods.galacticraft.core.util.OxygenUtil;
import micdoodle8.mods.galacticraft.planets.GalacticraftPlanets;
import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

public class BlockWalkway extends BlockTransmitter implements ITileEntityProvider, ItemBlockDesc.IBlockShiftDesc
{
    protected BlockWalkway(String assetName)
    {
        super(Material.iron);
        this.setHardness(1.0F);
//        this.setBlockTextureName(GalacticraftPlanets.TEXTURE_PREFIX + "walkway");
        this.setUnlocalizedName(assetName);
        this.setStepSound(Block.soundTypeMetal);
        this.isBlockContainer = true;
        this.minVector = new Vector3(0.0, 0.32, 0.0);
        this.maxVector = new Vector3(1.0, 1.0, 1.0);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public boolean shouldSideBeRendered(IBlockAccess worldIn, BlockPos pos, EnumFacing side)
    {
        return true;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public CreativeTabs getCreativeTabToDisplayOn()
    {
        return GalacticraftCore.galacticraftBlocksTab;
    }

    @Override
    public boolean isOpaqueCube()
    {
        return false;
    }

    @Override
    public boolean isFullCube()
    {
        return false;
    }

    @Override
    public IBlockState onBlockPlaced(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer)
    {
        return getStateFromMeta(this.getWalkwayOrientation(worldIn, pos));
    }

    @Override
    public void onNeighborBlockChange(World worldIn, BlockPos pos, IBlockState state, Block neighborBlock)
    {
        worldIn.setBlockState(pos, getStateFromMeta(this.getWalkwayOrientation(worldIn, pos)), 3);

        if (this.getNetworkType() != null)
        {
            super.onNeighborBlockChange(worldIn, pos, state, neighborBlock);
        }
    }

    public int getWalkwayOrientation(World worldIn, BlockPos pos)
    {
        int connectedNorth = isNormalCube(worldIn.getBlockState(pos.north()).getBlock()) || worldIn.getBlockState(pos.north()).getBlock() instanceof BlockWalkway ? 1 : 0;
        int connectedEast = isNormalCube(worldIn.getBlockState(pos.east()).getBlock()) || worldIn.getBlockState(pos.east()).getBlock() instanceof BlockWalkway ? 2 : 0;
        int connectedSouth = isNormalCube(worldIn.getBlockState(pos.south()).getBlock()) || worldIn.getBlockState(pos.south()).getBlock() instanceof BlockWalkway ? 4 : 0;
        int connectedWest = isNormalCube(worldIn.getBlockState(pos.west()).getBlock()) || worldIn.getBlockState(pos.west()).getBlock() instanceof BlockWalkway ? 8 : 0;

        return connectedNorth | connectedEast | connectedSouth | connectedWest;
    }

    public boolean isNormalCube(Block block)
    {
        return block.getMaterial().blocksMovement() && block.isFullCube();
    }

    @Override
    public TileEntity createNewTileEntity(World worldIn, int metadata)
    {
        if (this == AsteroidBlocks.blockWalkwayOxygenPipe)
        {
        	return new TileEntityOxygenPipe(); 
        }

        if (this == AsteroidBlocks.blockWalkwayWire)
        {
        	return new TileEntityAluminumWire(2); 
        }

        return null;
    }

    @Override
    public NetworkType getNetworkType()
    {
        if (this == AsteroidBlocks.blockWalkwayOxygenPipe)
        {
            return NetworkType.OXYGEN;
        }

        if (this == AsteroidBlocks.blockWalkwayWire)
        {
            return NetworkType.POWER;
        }

        return null;
    }

    @Override
    public void setBlockBoundsBasedOnState(IBlockAccess worldIn, BlockPos pos)
    {
        TileEntity tileEntity = worldIn.getTileEntity(pos);
        TileEntity[] connectable = new TileEntity[6];

        if (tileEntity != null)
        {
            if (this.getNetworkType() != null)
            {
                switch (this.getNetworkType())
                {
                case OXYGEN:
                    connectable = OxygenUtil.getAdjacentOxygenConnections(tileEntity);
                    break;
                case POWER:
                    connectable = EnergyUtil.getAdjacentPowerConnections(tileEntity);
                    break;
                default:
                    break;
                }
            }

            float minX = 0.0F;
            float minY = 0.32F;
            float minZ = 0.0F;
            float maxX = 1.0F;
            float maxY = 1.0F;
            float maxZ = 1.0F;

            if (connectable[0] != null)
            {
                minY = 0.0F;
            }

            this.setBlockBounds(minX, minY, minZ, maxX, maxY, maxZ);
        }
    }

    private void addCollisionBox(World worldIn, BlockPos pos, AxisAlignedBB aabb, List list)
    {
        AxisAlignedBB mask1 = this.getCollisionBoundingBox(worldIn, pos, worldIn.getBlockState(pos));

        if (mask1 != null && aabb.intersectsWith(mask1))
        {
            list.add(mask1);
        }
    }

    @SuppressWarnings("rawtypes")
    @Override
    public void addCollisionBoxesToList(World worldIn, BlockPos pos, IBlockState state, AxisAlignedBB mask, List list, Entity collidingEntity)
    {
        TileEntity tileEntity = worldIn.getTileEntity(pos);
        TileEntity[] connectable = new TileEntity[6];

        if (this.getNetworkType() != null)
        {
            switch (this.getNetworkType())
            {
            case OXYGEN:
                connectable = OxygenUtil.getAdjacentOxygenConnections(tileEntity);
                break;
            case POWER:
                connectable = EnergyUtil.getAdjacentPowerConnections(tileEntity);
                break;
            default:
                break;
            }
        }

        this.setBlockBounds((float) this.minVector.x, (float) this.minVector.y, (float) this.minVector.z, (float) this.maxVector.x, (float) this.maxVector.y, (float) this.maxVector.z);
        this.addCollisionBox(worldIn, pos, mask, list);

        this.setBlockBounds(0.0F, 0.9F, 0.0F, 1.0F, 1.0F, 1.0F);
        this.addCollisionBox(worldIn, pos, mask, list);

        if (connectable[4] != null)
        {
            this.setBlockBounds(0, (float) this.minVector.y, (float) this.minVector.z, (float) this.maxVector.x, (float) this.maxVector.y, (float) this.maxVector.z);
            this.addCollisionBox(worldIn, pos, mask, list);
        }

        if (connectable[5] != null)
        {
            this.setBlockBounds((float) this.minVector.x, (float) this.minVector.y, (float) this.minVector.z, 1, (float) this.maxVector.y, (float) this.maxVector.z);
            this.addCollisionBox(worldIn, pos, mask, list);
        }

        if (connectable[0] != null)
        {
            this.setBlockBounds((float) this.minVector.x, 0, (float) this.minVector.z, (float) this.maxVector.x, (float) this.maxVector.y, (float) this.maxVector.z);
            this.addCollisionBox(worldIn, pos, mask, list);
        }

        if (connectable[1] != null)
        {
            this.setBlockBounds((float) this.minVector.x, (float) this.minVector.y, (float) this.minVector.z, (float) this.maxVector.x, 1, (float) this.maxVector.z);
            this.addCollisionBox(worldIn, pos, mask, list);
        }

        if (connectable[2] != null)
        {
            this.setBlockBounds((float) this.minVector.x, (float) this.minVector.y, 0, (float) this.maxVector.x, (float) this.maxVector.y, (float) this.maxVector.z);
            this.addCollisionBox(worldIn, pos, mask, list);
        }

        if (connectable[3] != null)
        {
            this.setBlockBounds((float) this.minVector.x, (float) this.minVector.y, (float) this.minVector.z, (float) this.maxVector.x, (float) this.maxVector.y, 1);
            this.addCollisionBox(worldIn, pos, mask, list);
        }

        this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
    }

    @Override
    public String getShiftDescription(int meta)
    {
        if (this == AsteroidBlocks.blockWalkway)
        {
            return GCCoreUtil.translate("tile.walkway.description");
        }
        else if (this == AsteroidBlocks.blockWalkwayWire)
        {
            return GCCoreUtil.translate("tile.walkwayAluminumWire.description");
        }
        else if (this == AsteroidBlocks.blockWalkwayOxygenPipe)
        {
            return GCCoreUtil.translate("tile.walkwayOxygenPipe.description");
        }

        return "";
    }

    @Override
    public boolean showDescription(int meta)
    {
        return true;
    }
}
