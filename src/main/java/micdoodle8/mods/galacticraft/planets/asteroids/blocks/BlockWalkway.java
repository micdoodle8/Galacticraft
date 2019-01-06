package micdoodle8.mods.galacticraft.planets.asteroids.blocks;

import micdoodle8.mods.galacticraft.api.transmission.NetworkType;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.blocks.BlockTransmitter;
import micdoodle8.mods.galacticraft.core.blocks.ISortableBlock;
import micdoodle8.mods.galacticraft.core.energy.EnergyUtil;
import micdoodle8.mods.galacticraft.core.items.IShiftDescription;
import micdoodle8.mods.galacticraft.core.tile.TileEntityAluminumWire;
import micdoodle8.mods.galacticraft.core.tile.TileEntityFluidPipe;
import micdoodle8.mods.galacticraft.core.tile.TileEntityNull;
import micdoodle8.mods.galacticraft.core.util.EnumSortCategoryBlock;
import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
import micdoodle8.mods.galacticraft.core.util.OxygenUtil;
import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockWalkway extends BlockTransmitter implements ITileEntityProvider, IShiftDescription, ISortableBlock
{
    public static final PropertyEnum<EnumWalkwayType> WALKWAY_TYPE = PropertyEnum.create("type", EnumWalkwayType.class);
//    private Vector3 minVector = new Vector3(0.0, 0.32, 0.0);
//    private Vector3 maxVector = new Vector3(1.0, 1.0, 1.0);
    protected static final AxisAlignedBB AABB_UNCONNECTED = new AxisAlignedBB(0.0, 0.32, 0.0, 1.0, 1.0, 1.0);
    protected static final AxisAlignedBB AABB_CONNECTED_DOWN = new AxisAlignedBB(0.0, 0.0, 0.0, 1.0, 1.0, 1.0);

    public enum EnumWalkwayType implements IStringSerializable
    {
        WALKWAY(0, "walkway"),
        WALKWAY_WIRE(1, "walkway_wire"),
        WALKWAY_PIPE(2, "walkway_pipe");

        private final int meta;
        private final String name;

        private EnumWalkwayType(int meta, String name)
        {
            this.meta = meta;
            this.name = name;
        }

        public int getMeta()
        {
            return this.meta;
        }

        private final static EnumWalkwayType[] values = values();
        public static EnumWalkwayType byMetadata(int meta)
        {
            return values[meta % values.length];
        }

        @Override
        public String getName()
        {
            return this.name;
        }
    }

    protected BlockWalkway(String assetName)
    {
        super(Material.IRON);
        this.setHardness(1.0F);
        this.setUnlocalizedName(assetName);
        this.setSoundType(SoundType.METAL);
        this.hasTileEntity = true;
        this.setDefaultState(this.blockState.getBaseState().withProperty(WALKWAY_TYPE, EnumWalkwayType.WALKWAY));
    }

    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos)
    {
        state = this.getActualState(state, source, pos);

        if (state.getValue(DOWN))
        {
            return AABB_CONNECTED_DOWN;
        }

        return AABB_UNCONNECTED;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public boolean shouldSideBeRendered(IBlockState blockState, IBlockAccess blockAccess, BlockPos pos, EnumFacing side)
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
    public boolean isOpaqueCube(IBlockState state)
    {
        return false;
    }

    @Override
    public boolean isFullCube(IBlockState state)
    {
        return false;
    }

    @Override
    public BlockFaceShape getBlockFaceShape(IBlockAccess worldIn, IBlockState state, BlockPos pos, EnumFacing face)
    {
        return BlockFaceShape.UNDEFINED;
    }

    @Override
    public boolean canPlaceTorchOnTop(IBlockState state, IBlockAccess world, BlockPos pos)
    {
        return true;
    }
    
    @Override
    public boolean isNormalCube(IBlockState state)
    {
        return state.getMaterial().blocksMovement() && state.getBlock().isFullCube(state);
    }

    @Override
    public TileEntity createNewTileEntity(World worldIn, int metadata)
    {
        if (metadata == EnumWalkwayType.WALKWAY_PIPE.getMeta())
        {
            return new TileEntityFluidPipe();
        }

        if (metadata == EnumWalkwayType.WALKWAY_WIRE.getMeta())
        {
            return new TileEntityAluminumWire(2);
        }

        return new TileEntityNull();
    }

    @Override
    public NetworkType getNetworkType(IBlockState state)
    {
        if (state.getValue(WALKWAY_TYPE) == EnumWalkwayType.WALKWAY_PIPE)
        {
            return NetworkType.FLUID;
        }

        if (state.getValue(WALKWAY_TYPE) == EnumWalkwayType.WALKWAY_PIPE)
        {
            return NetworkType.POWER;
        }

        return null;
    }

//    @Override
//    public void setBlockBoundsBasedOnState(IBlockAccess worldIn, BlockPos pos)
//    {
//        TileEntity tileEntity = worldIn.getTileEntity(pos);
//        TileEntity[] connectable = new TileEntity[6];
//
//        if (tileEntity != null)
//        {
//            IBlockState state = worldIn.getBlockState(pos);
//            if (this.getNetworkType(state) != null)
//            {
//                switch (this.getNetworkType(state))
//                {
//                case FLUID:
//                    connectable = OxygenUtil.getAdjacentFluidConnections(tileEntity);
//                    break;
//                case POWER:
//                    connectable = EnergyUtil.getAdjacentPowerConnections(tileEntity);
//                    break;
//                default:
//                    break;
//                }
//            }
//
//            float minX = 0.0F;
//            float minY = 0.32F;
//            float minZ = 0.0F;
//            float maxX = 1.0F;
//            float maxY = 1.0F;
//            float maxZ = 1.0F;
//
//            if (connectable[0] != null)
//            {
//                minY = 0.0F;
//            }
//
//            this.setBlockBounds(minX, minY, minZ, maxX, maxY, maxZ);
//        }
//    }

//    private void addCollisionBox(World worldIn, BlockPos pos, AxisAlignedBB aabb, List list)
//    {
//        AxisAlignedBB mask1 = this.getCollisionBoundingBox(worldIn, pos, worldIn.getBlockState(pos));
//
//        if (mask1 != null && aabb.intersectsWith(mask1))
//        {
//            list.add(mask1);
//        }
//    }

//    @Override
//    public void addCollisionBoxesToList(World worldIn, BlockPos pos, IBlockState state, AxisAlignedBB mask, List<AxisAlignedBB> list, Entity collidingEntity)
//    {
//        TileEntity tileEntity = worldIn.getTileEntity(pos);
//        TileEntity[] connectable = new TileEntity[6];
//
//        if (this.getNetworkType(state) != null)
//        {
//            switch (this.getNetworkType(state))
//            {
//            case FLUID:
//                connectable = OxygenUtil.getAdjacentFluidConnections(tileEntity);
//                break;
//            case POWER:
//                connectable = EnergyUtil.getAdjacentPowerConnections(tileEntity);
//                break;
//            default:
//                break;
//            }
//        }
//
//        this.setBlockBounds((float) this.minVector.x, (float) this.minVector.y, (float) this.minVector.z, (float) this.maxVector.x, (float) this.maxVector.y, (float) this.maxVector.z);
//        this.addCollisionBox(worldIn, pos, mask, list);
//
//        this.setBlockBounds(0.0F, 0.9F, 0.0F, 1.0F, 1.0F, 1.0F);
//        this.addCollisionBox(worldIn, pos, mask, list);
//
//        if (connectable[4] != null)
//        {
//            this.setBlockBounds(0, (float) this.minVector.y, (float) this.minVector.z, (float) this.maxVector.x, (float) this.maxVector.y, (float) this.maxVector.z);
//            this.addCollisionBox(worldIn, pos, mask, list);
//        }
//
//        if (connectable[5] != null)
//        {
//            this.setBlockBounds((float) this.minVector.x, (float) this.minVector.y, (float) this.minVector.z, 1, (float) this.maxVector.y, (float) this.maxVector.z);
//            this.addCollisionBox(worldIn, pos, mask, list);
//        }
//
//        if (connectable[0] != null)
//        {
//            this.setBlockBounds((float) this.minVector.x, 0, (float) this.minVector.z, (float) this.maxVector.x, (float) this.maxVector.y, (float) this.maxVector.z);
//            this.addCollisionBox(worldIn, pos, mask, list);
//        }
//
//        if (connectable[1] != null)
//        {
//            this.setBlockBounds((float) this.minVector.x, (float) this.minVector.y, (float) this.minVector.z, (float) this.maxVector.x, 1, (float) this.maxVector.z);
//            this.addCollisionBox(worldIn, pos, mask, list);
//        }
//
//        if (connectable[2] != null)
//        {
//            this.setBlockBounds((float) this.minVector.x, (float) this.minVector.y, 0, (float) this.maxVector.x, (float) this.maxVector.y, (float) this.maxVector.z);
//            this.addCollisionBox(worldIn, pos, mask, list);
//        }
//
//        if (connectable[3] != null)
//        {
//            this.setBlockBounds((float) this.minVector.x, (float) this.minVector.y, (float) this.minVector.z, (float) this.maxVector.x, (float) this.maxVector.y, 1);
//            this.addCollisionBox(worldIn, pos, mask, list);
//        }
//
//        this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
//    }

    @Override
    public String getShiftDescription(int meta)
    {
        if (meta == EnumWalkwayType.WALKWAY.getMeta())
        {
            return GCCoreUtil.translate("tile.walkway.walkway.description");
        }
        else if (meta == EnumWalkwayType.WALKWAY_WIRE.getMeta())
        {
            return GCCoreUtil.translate("tile.walkway.walkway_wire.description");
        }
        else if (meta == EnumWalkwayType.WALKWAY_PIPE.getMeta())
        {
            return GCCoreUtil.translate("tile.walkway.walkway_pipe.description");
        }

        return "";
    }

    @Override
    public boolean showDescription(int meta)
    {
        return true;
    }

    @Override
    protected BlockStateContainer createBlockState()
    {
        return new BlockStateContainer(this, WALKWAY_TYPE, NORTH, EAST, SOUTH, WEST, DOWN);
    }

    @Override
    public IBlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos)
    {
        Object[] connectable = new Object[EnumFacing.VALUES.length];

        TileEntity tileEntity = null;
        
        if (state.getValue(WALKWAY_TYPE) != EnumWalkwayType.WALKWAY)
        {
            tileEntity = worldIn.getTileEntity(pos);
        }
        for (EnumFacing direction : EnumFacing.VALUES)
        {
            if (direction == EnumFacing.UP || (direction == EnumFacing.DOWN && tileEntity == null))
            {
                continue;
            }

            if (state.getValue(WALKWAY_TYPE) == EnumWalkwayType.WALKWAY)
            {
                BlockPos neighbour = pos.offset(direction);
                Block block = worldIn.getBlockState(neighbour).getBlock();

                if (block == this || block.isSideSolid(worldIn.getBlockState(neighbour), worldIn, neighbour, direction.getOpposite()))
                {
                    connectable[direction.ordinal()] = block;
                }
            }
            else if (tileEntity !=null && state.getValue(WALKWAY_TYPE) == EnumWalkwayType.WALKWAY_PIPE)
            {
                connectable = OxygenUtil.getAdjacentFluidConnections(tileEntity);
            }
            else if (tileEntity !=null && state.getValue(WALKWAY_TYPE) == EnumWalkwayType.WALKWAY_WIRE)
            {
                connectable = EnergyUtil.getAdjacentPowerConnections(tileEntity);
            }
        }

        return state.withProperty(NORTH, Boolean.valueOf(connectable[EnumFacing.NORTH.ordinal()] != null))
                .withProperty(EAST, Boolean.valueOf(connectable[EnumFacing.EAST.ordinal()] != null))
                .withProperty(SOUTH, Boolean.valueOf(connectable[EnumFacing.SOUTH.ordinal()] != null))
                .withProperty(WEST, Boolean.valueOf(connectable[EnumFacing.WEST.ordinal()] != null))
                .withProperty(DOWN, Boolean.valueOf(connectable[EnumFacing.DOWN.ordinal()] != null));
    }

    @Override
    public IBlockState getStateFromMeta(int meta)
    {
        return this.getDefaultState().withProperty(WALKWAY_TYPE, EnumWalkwayType.byMetadata(meta));
    }

    @Override
    public int getMetaFromState(IBlockState state)
    {
        return ((EnumWalkwayType) state.getValue(WALKWAY_TYPE)).getMeta();
    }

    @Override
    public void getSubBlocks(CreativeTabs tab, NonNullList<ItemStack> list)
    {
        list.add(new ItemStack(this, 1, 0));
        list.add(new ItemStack(this, 1, 1));
        list.add(new ItemStack(this, 1, 2));
    }

    @Override
    public int damageDropped(IBlockState state)
    {
        return this.getMetaFromState(state);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public BlockRenderLayer getBlockLayer()
    {
        return BlockRenderLayer.CUTOUT;
    }

    @Override
    public EnumSortCategoryBlock getCategory(int meta)
    {
        return EnumSortCategoryBlock.GENERAL;
    }
}
