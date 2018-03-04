package micdoodle8.mods.galacticraft.planets.asteroids.blocks;

import micdoodle8.mods.galacticraft.api.transmission.tile.IConductor;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.blocks.BlockTileGC;
import micdoodle8.mods.galacticraft.core.blocks.ISortableBlock;
import micdoodle8.mods.galacticraft.core.energy.EnergyUtil;
import micdoodle8.mods.galacticraft.core.energy.tile.EnergyStorageTile;
import micdoodle8.mods.galacticraft.core.items.IShiftDescription;
import micdoodle8.mods.galacticraft.core.util.EnumColor;
import micdoodle8.mods.galacticraft.core.util.EnumSortCategoryBlock;
import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
import micdoodle8.mods.galacticraft.planets.asteroids.tile.TileEntityBeamReceiver;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockBeamReceiver extends BlockTileGC implements IShiftDescription, ISortableBlock
{
    public static final PropertyDirection FACING = PropertyDirection.create("facing");
    protected static final AxisAlignedBB UP_AABB = new AxisAlignedBB(0.3F, 0.3F, 0.3F, 0.7F, 1.0F, 0.7F);
    protected static final AxisAlignedBB DOWN_AABB = new AxisAlignedBB(0.2F, 0.0F, 0.2F, 0.8F, 0.42F, 0.8F);
    protected static final AxisAlignedBB EAST_AABB = new AxisAlignedBB(0.58F, 0.2F, 0.2F, 1.0F, 0.8F, 0.8F);
    protected static final AxisAlignedBB WEST_AABB = new AxisAlignedBB(0.0F, 0.2F, 0.2F, 0.42F, 0.8F, 0.8F);
    protected static final AxisAlignedBB NORTH_AABB = new AxisAlignedBB(0.2F, 0.2F, 0.0F, 0.8F, 0.8F, 0.42F);
    protected static final AxisAlignedBB SOUTH_AABB = new AxisAlignedBB(0.2F, 0.2F, 0.58F, 0.8F, 0.8F, 1.0F);

    public BlockBeamReceiver(String assetName)
    {
        super(Material.IRON);
        this.setUnlocalizedName(assetName);
        this.setSoundType(SoundType.METAL);
    }

    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos)
    {
        switch (state.getValue(FACING))
        {
        case UP:
            return UP_AABB;
        case DOWN:
            return DOWN_AABB;
        case EAST:
            return EAST_AABB;
        case WEST:
            return WEST_AABB;
        case SOUTH:
            return SOUTH_AABB;
        default:
        case NORTH:
            return NORTH_AABB;
        }
    }

    @SideOnly(Side.CLIENT)
    @Override
    public CreativeTabs getCreativeTabToDisplayOn()
    {
        return GalacticraftCore.galacticraftBlocksTab;
    }

    @Override
    public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos)
    {
        int oldMeta = getMetaFromState(worldIn.getBlockState(pos));
        int meta = this.getMetadataFromAngle(worldIn, pos, EnumFacing.getFront(oldMeta).getOpposite());

        if (meta == -1)
        {
            worldIn.destroyBlock(pos, true);
        }
        else if (meta != oldMeta)
        {
            worldIn.setBlockState(pos, getStateFromMeta(meta), 3);
            TileEntity thisTile = worldIn.getTileEntity(pos);
            if (thisTile instanceof TileEntityBeamReceiver)
            {
                TileEntityBeamReceiver thisReceiver = (TileEntityBeamReceiver) thisTile;
                thisReceiver.setFacing(EnumFacing.getFront(meta));
                thisReceiver.invalidateReflector();
                thisReceiver.initiateReflector();
            }
        }

        super.neighborChanged(state, worldIn, pos, blockIn, fromPos);
    }

    @Override
    public void onBlockAdded(World world, BlockPos pos, IBlockState state)
    {
        TileEntity thisTile = world.getTileEntity(pos);
        if (thisTile instanceof TileEntityBeamReceiver)
        {
            ((TileEntityBeamReceiver) thisTile).setFacing(EnumFacing.getFront(getMetaFromState(state)));
        }
    }

//    @Override
//    public void setBlockBoundsBasedOnState(IBlockAccess world, BlockPos pos)
//    {
//        int meta = getMetaFromState(world.getBlockState(pos));
//
//        if (meta != -1)
//        {
//            EnumFacing dir = EnumFacing.getFront(meta);
//
//            switch (dir)
//            {
//            case UP:
//                this.setBlockBounds(0.3F, 0.3F, 0.3F, 0.7F, 1.0F, 0.7F);
//                break;
//            case DOWN:
//                this.setBlockBounds(0.2F, 0.0F, 0.2F, 0.8F, 0.42F, 0.8F);
//                break;
//            case EAST:
//                this.setBlockBounds(0.58F, 0.2F, 0.2F, 1.0F, 0.8F, 0.8F);
//                break;
//            case WEST:
//                this.setBlockBounds(0.0F, 0.2F, 0.2F, 0.42F, 0.8F, 0.8F);
//                break;
//            case NORTH:
//                this.setBlockBounds(0.2F, 0.2F, 0.0F, 0.8F, 0.8F, 0.42F);
//                break;
//            case SOUTH:
//                this.setBlockBounds(0.2F, 0.2F, 0.58F, 0.8F, 0.8F, 1.0F);
//                break;
//            default:
//                break;
//            }
//        }
//    }
//
//    @Override
//    public void addCollisionBoxesToList(World worldIn, BlockPos pos, IBlockState state, AxisAlignedBB mask, List<AxisAlignedBB> list, Entity collidingEntity)
//    {
//        this.setBlockBoundsBasedOnState(worldIn, pos);
//        super.addCollisionBoxesToList(worldIn, pos, state, mask, list, collidingEntity);
//    }

    private int getMetadataFromAngle(World world, BlockPos pos, EnumFacing side)
    {
        EnumFacing direction = side.getOpposite();

        TileEntity tileAt = world.getTileEntity(pos.add(direction.getFrontOffsetX(), direction.getFrontOffsetY(), direction.getFrontOffsetZ()));

        if (tileAt instanceof EnergyStorageTile)
        {
            if (((EnergyStorageTile) tileAt).getModeFromDirection(direction.getOpposite()) != null)
            {
                return direction.ordinal();
            }
            else
            {
                return -1;
            }
        }

        if (EnergyUtil.otherModCanReceive(tileAt, direction.getOpposite()))
        {
            return direction.ordinal();
        }

        for (EnumFacing adjacentDir : EnumFacing.VALUES)
        {
            if (adjacentDir == direction)
            {
                continue;
            }
            tileAt = world.getTileEntity(pos.add(adjacentDir.getFrontOffsetX(), adjacentDir.getFrontOffsetY(), adjacentDir.getFrontOffsetZ()));

            if (tileAt instanceof IConductor)
            {
                continue;
            }

            if (tileAt instanceof EnergyStorageTile && ((EnergyStorageTile) tileAt).getModeFromDirection(adjacentDir.getOpposite()) != null)
            {
                return adjacentDir.ordinal();
            }

            if (EnergyUtil.otherModCanReceive(tileAt, adjacentDir.getOpposite()))
            {
                return adjacentDir.ordinal();
            }
        }

        return -1;
    }

    @Override
    public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer, EnumHand hand)
    {
        return getStateFromMeta(this.getMetadataFromAngle(world, pos, facing));
    }

    @Override
    public boolean canPlaceBlockOnSide(World worldIn, BlockPos pos, EnumFacing side)
    {
        if (this.getMetadataFromAngle(worldIn, pos, side) != -1)
        {
            return true;
        }

        if (worldIn.isRemote)
        {
            this.sendIncorrectSideMessage();
        }

        return false;
    }

    @SideOnly(Side.CLIENT)
    private void sendIncorrectSideMessage()
    {
        FMLClientHandler.instance().getClient().player.sendMessage(new TextComponentString(EnumColor.RED + GCCoreUtil.translate("gui.receiver.cannot_attach")));
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
    public EnumBlockRenderType getRenderType(IBlockState state)
    {
        return EnumBlockRenderType.INVISIBLE;
    }

    @Override
    public int damageDropped(IBlockState metadata)
    {
        return 0;
    }

    @Override
    public TileEntity createTileEntity(World world, IBlockState metadata)
    {
        return new TileEntityBeamReceiver();
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void getSubBlocks(CreativeTabs tab, NonNullList<ItemStack> list)
    {
        list.add(new ItemStack(this, 1, 0));
    }

    @Override
    public boolean onMachineActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, ItemStack heldItem, EnumFacing side, float hitX, float hitY, float hitZ)
    {
        TileEntity tile = worldIn.getTileEntity(pos);

        if (tile instanceof TileEntityBeamReceiver)
        {
            return ((TileEntityBeamReceiver) tile).onMachineActivated(worldIn, pos, state, playerIn, hand, heldItem, side, hitX, hitY, hitZ);
        }

        return false;
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

    @Override
    public IBlockState getStateFromMeta(int meta)
    {
        EnumFacing enumfacing = EnumFacing.getFront(meta);
        return this.getDefaultState().withProperty(FACING, enumfacing);
    }

    @Override
    public int getMetaFromState(IBlockState state)
    {
        return (state.getValue(FACING)).getIndex();
    }

    @Override
    protected BlockStateContainer createBlockState()
    {
        return new BlockStateContainer(this, FACING);
    }

    @Override
    public EnumSortCategoryBlock getCategory(int meta)
    {
        return EnumSortCategoryBlock.MACHINE;
    }
}
