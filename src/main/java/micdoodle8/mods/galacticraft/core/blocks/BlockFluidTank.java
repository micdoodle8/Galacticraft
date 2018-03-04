package micdoodle8.mods.galacticraft.core.blocks;

import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.items.IShiftDescription;
import micdoodle8.mods.galacticraft.core.tile.TileEntityFluidTank;
import micdoodle8.mods.galacticraft.core.util.EnumSortCategoryBlock;
import micdoodle8.mods.galacticraft.core.util.FluidUtil;
import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidActionResult;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockFluidTank extends Block implements IShiftDescription, ISortableBlock, ITileEntityProvider
{
    public static final PropertyBool UP = PropertyBool.create("up");
    public static final PropertyBool DOWN = PropertyBool.create("down");
    private static final AxisAlignedBB BOUNDS = new AxisAlignedBB(0.05F, 0.0F, 0.05F, 0.95F, 1.0F, 0.95F);

    public BlockFluidTank(String assetName)
    {
        super(Material.GLASS);
        this.setHardness(3.0F);
        this.setResistance(8.0F);
        this.setSoundType(SoundType.GLASS);
        this.setUnlocalizedName(assetName);
    }

    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos)
    {
        return BOUNDS;
    }

    @Override
    public void breakBlock(World worldIn, BlockPos pos, IBlockState state)
    {
        TileEntity tile = worldIn.getTileEntity(pos);
        if (tile instanceof TileEntityFluidTank)
        {
            TileEntityFluidTank tank = (TileEntityFluidTank) tile;
            tank.onBreak();
        }
        super.breakBlock(worldIn, pos, state);
    }

    @Override
    public CreativeTabs getCreativeTabToDisplayOn()
    {
        return GalacticraftCore.galacticraftBlocksTab;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public BlockRenderLayer getBlockLayer()
    {
        return BlockRenderLayer.CUTOUT;
    }

    @Override
    public boolean isFullCube(IBlockState state)
    {
        return false;
    }

    @Override
    public boolean isOpaqueCube(IBlockState state)
    {
        return false;
    }

    @Override
    public BlockFaceShape getBlockFaceShape(IBlockAccess worldIn, IBlockState state, BlockPos pos, EnumFacing face)
    {
        return BlockFaceShape.UNDEFINED;
    }

    @Override
    public EnumSortCategoryBlock getCategory(int meta)
    {
        return EnumSortCategoryBlock.GENERAL;
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
    protected BlockStateContainer createBlockState()
    {
        return new BlockStateContainer(this, UP, DOWN);
    }

    @Override
    public IBlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos)
    {
        IBlockState stateAbove = worldIn.getBlockState(pos.up());
        IBlockState stateBelow = worldIn.getBlockState(pos.down());
        return state.withProperty(UP, stateAbove.getBlock() == this).withProperty(DOWN, stateBelow.getBlock() == this);
    }

    @Override
    public IBlockState getStateFromMeta(int meta)
    {
        return this.getDefaultState();
    }

    @Override
    public int getMetaFromState(IBlockState state)
    {
        return 0;
    }

    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta)
    {
        return new TileEntityFluidTank();
    }

//    @Override
//    public void setBlockBoundsBasedOnState(IBlockAccess worldIn, BlockPos pos)
//    {
//        this.setBlockBounds((float) BOUNDS.minX, (float) BOUNDS.minY, (float) BOUNDS.minZ, (float) BOUNDS.maxX, (float) BOUNDS.maxY, (float) BOUNDS.maxZ);
//    }
//
//    @Override
//    public void addCollisionBoxesToList(World worldIn, BlockPos pos, IBlockState state, AxisAlignedBB mask, List<AxisAlignedBB> list, Entity collidingEntity)
//    {
//        this.setBlockBoundsBasedOnState(worldIn, pos);
//        super.addCollisionBoxesToList(worldIn, pos, state, mask, list, collidingEntity);
//    }
//
//    @Override
//    public AxisAlignedBB getCollisionBoundingBox(IBlockState blockState, IBlockAccess worldIn, BlockPos pos)
//    {
//        this.setBlockBoundsBasedOnState(worldIn, pos);
//        return super.getCollisionBoundingBox(worldIn, pos, state);
//    }
//
//    @Override
//    public AxisAlignedBB getSelectedBoundingBox(IBlockState state, World worldIn, BlockPos pos)
//    {
//        this.setBlockBoundsBasedOnState(worldIn, pos);
//        return super.getSelectedBoundingBox(worldIn, pos);
//    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ)
    {
        if (super.onBlockActivated(worldIn, pos, state, playerIn, hand, side, hitX, hitY, hitZ))
        {
            return true;
        }

        if (hand == EnumHand.OFF_HAND)
        {
        	return false;
        }

        ItemStack current = playerIn.inventory.getCurrentItem();
        int slot = playerIn.inventory.currentItem;

        if (!current.isEmpty())
        {
            TileEntity tile = worldIn.getTileEntity(pos);

            if (tile instanceof TileEntityFluidTank)
            {
                TileEntityFluidTank tank = (TileEntityFluidTank) tile;

                FluidActionResult forgeResult = FluidUtil.interactWithFluidHandler(current, tank.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, null), playerIn);
                if (forgeResult.isSuccess())
                {
                	playerIn.inventory.setInventorySlotContents(slot, forgeResult.result);
            		if (playerIn.inventoryContainer != null)
            		{
            			playerIn.inventoryContainer.detectAndSendChanges();
            		}
            		return true;
                }

                return false;
            }
        }

        return false;
    }

    @Override
    public int getLightValue(IBlockState state, IBlockAccess world, BlockPos pos)
    {
        TileEntity tile = world.getTileEntity(pos);

        if (tile instanceof TileEntityFluidTank)
        {
            TileEntityFluidTank tank = (TileEntityFluidTank) tile;
            return tank.fluidTank.getFluid() == null || tank.fluidTank.getFluid().amount == 0 ? 0 : tank.fluidTank.getFluid().getFluid().getLuminosity(tank.fluidTank.getFluid());
        }

        return 0;
    }
}
