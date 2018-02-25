package micdoodle8.mods.galacticraft.planets.asteroids.blocks;

import micdoodle8.mods.galacticraft.api.transmission.NetworkType;
import micdoodle8.mods.galacticraft.api.vector.Vector3;
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
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

public class BlockWalkway extends BlockTransmitter implements ITileEntityProvider, IShiftDescription, ISortableBlock
{
    public static final PropertyEnum<EnumWalkwayType> WALKWAY_TYPE = PropertyEnum.create("type", EnumWalkwayType.class);
    private Vector3 minVector = new Vector3(0.0, 0.32, 0.0);
    private Vector3 maxVector = new Vector3(1.0, 1.0, 1.0);

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

        public static EnumWalkwayType byMetadata(int meta)
        {
            return values()[meta];
        }

        @Override
        public String getName()
        {
            return this.name;
        }
    }

    protected BlockWalkway(String assetName)
    {
        super(Material.iron);
        this.setHardness(1.0F);
        this.setUnlocalizedName(assetName);
        this.setStepSound(Block.soundTypeMetal);
        this.isBlockContainer = true;
        this.setDefaultState(this.blockState.getBaseState().withProperty(WALKWAY_TYPE, EnumWalkwayType.WALKWAY));
    }

    @Override
    public Vector3 getMinVector(IBlockState state)
    {
        return this.minVector;
    }

    @Override
    public Vector3 getMaxVector(IBlockState state)
    {
        return this.maxVector;
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

    public boolean isNormalCube(Block block)
    {
        return block.getMaterial().blocksMovement() && block.isFullCube();
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

    @Override
    public void setBlockBoundsBasedOnState(IBlockAccess worldIn, BlockPos pos)
    {
        TileEntity tileEntity = worldIn.getTileEntity(pos);
        TileEntity[] connectable = new TileEntity[6];
        IBlockState state = worldIn.getBlockState(pos);

        if (tileEntity != null && state.getBlock() instanceof BlockWalkway)
        {
            if (this.getNetworkType(state) != null)
            {
                switch (this.getNetworkType(state))
                {
                case FLUID:
                    connectable = OxygenUtil.getAdjacentFluidConnections(tileEntity);
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

    private void addCollisionBox(World worldIn, BlockPos pos, AxisAlignedBB aabb, List<AxisAlignedBB> list)
    {
        AxisAlignedBB mask1 = this.getCollisionBoundingBox(worldIn, pos, worldIn.getBlockState(pos));

        if (mask1 != null && aabb.intersectsWith(mask1))
        {
            list.add(mask1);
        }
    }

    @Override
    public void addCollisionBoxesToList(World worldIn, BlockPos pos, IBlockState state, AxisAlignedBB mask, List<AxisAlignedBB> list, Entity collidingEntity)
    {
        TileEntity tileEntity = worldIn.getTileEntity(pos);
        TileEntity[] connectable = new TileEntity[6];

        if (this.getNetworkType(state) != null)
        {
            switch (this.getNetworkType(state))
            {
            case FLUID:
                connectable = OxygenUtil.getAdjacentFluidConnections(tileEntity);
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
    protected BlockState createBlockState()
    {
        return new BlockState(this, WALKWAY_TYPE, NORTH, EAST, SOUTH, WEST, DOWN);
    }

    @Override
    public IBlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos)
    {
        Object[] connectable = new Object[EnumFacing.VALUES.length];

        for (EnumFacing direction : EnumFacing.VALUES)
        {
            if (direction == EnumFacing.UP || (direction == EnumFacing.DOWN && state.getValue(WALKWAY_TYPE) == EnumWalkwayType.WALKWAY))
            {
                continue;
            }

            if (state.getValue(WALKWAY_TYPE) == EnumWalkwayType.WALKWAY)
            {
                BlockPos neighbour = pos.offset(direction);
                Block block = worldIn.getBlockState(neighbour).getBlock();

                if (block == this || block.isSideSolid(worldIn, neighbour, direction.getOpposite()))
                {
                    connectable[direction.ordinal()] = block;
                }
            }
            else if (state.getValue(WALKWAY_TYPE) == EnumWalkwayType.WALKWAY_PIPE)
            {
                TileEntity tileEntity = worldIn.getTileEntity(pos);
                connectable = OxygenUtil.getAdjacentFluidConnections(tileEntity);
            }
            else if (state.getValue(WALKWAY_TYPE) == EnumWalkwayType.WALKWAY_WIRE)
            {
                TileEntity tileEntity = worldIn.getTileEntity(pos);
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
    public void getSubBlocks(Item itemIn, CreativeTabs tab, List<ItemStack> list)
    {
        list.add(new ItemStack(itemIn, 1, 0));
        list.add(new ItemStack(itemIn, 1, 1));
        list.add(new ItemStack(itemIn, 1, 2));
    }

    @Override
    public int damageDropped(IBlockState state)
    {
        return this.getMetaFromState(state);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public EnumWorldBlockLayer getBlockLayer()
    {
        return EnumWorldBlockLayer.CUTOUT;
    }

    @Override
    public EnumSortCategoryBlock getCategory(int meta)
    {
        return EnumSortCategoryBlock.GENERAL;
    }
}
