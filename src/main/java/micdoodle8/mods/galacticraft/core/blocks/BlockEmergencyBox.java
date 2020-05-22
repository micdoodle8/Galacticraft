package micdoodle8.mods.galacticraft.core.blocks;

import micdoodle8.mods.galacticraft.api.block.IPartialSealableBlock;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.items.IShiftDescription;
import micdoodle8.mods.galacticraft.core.tile.TileEntityEmergencyBox;
import micdoodle8.mods.galacticraft.core.util.EnumSortCategoryBlock;
import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.item.ItemGroup;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.block.BlockRenderType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.NonNullList;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockEmergencyBox extends BlockAdvancedTile implements IShiftDescription, ISortableBlock, IPartialSealableBlock
{
    public static final PropertyBool KIT = PropertyBool.create("kit");
    
    public BlockEmergencyBox(Properties builder)
    {
        super(Material.IRON);
        this.setHardness(4.0F);
        this.setResistance(70F);
        this.setSoundType(SoundType.METAL);
        this.setLightLevel(1.0F);
        this.setUnlocalizedName(assetName);
        this.setDefaultState(stateContainer.getBaseState().with(KIT, false));
    }

    @Override
    protected BlockStateContainer createBlockState()
    {
        return new BlockStateContainer(this, KIT);
    }

    @Override
    public BlockState getStateFromMeta(int meta)
    {
        return this.getDefaultState().withProperty(KIT, meta % 2 == 1);
    }

    @Override
    public int getMetaFromState(BlockState state)
    {
        return (state.getValue(KIT) ? 1 : 0);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void getSubBlocks(ItemGroup tab, NonNullList<ItemStack> list)
    {
        list.add(new ItemStack(this, 1, 0));
        list.add(new ItemStack(this, 1, 1));
    }

    @Override
    public int damageDropped(BlockState state)
    {
        return getMetaFromState(state);
    }

    @Override
    public ItemStack getPickBlock(BlockState state, RayTraceResult target, World world, BlockPos pos, PlayerEntity player)
    {
        return new ItemStack(this, 1, this.getMetaFromState(state));
    }

    @Override
    @SideOnly(Side.CLIENT)
    public BlockRenderLayer getBlockLayer()
    {
        return BlockRenderLayer.CUTOUT;
    }

    @Override
    public BlockRenderType getRenderType(BlockState state)
    {
        return BlockRenderType.MODEL;
    }

    @Override
    public boolean isOpaqueCube(BlockState state)
    {
        return false;
    }

    @Override
    public boolean isFullCube(BlockState state)
    {
        return false;
    }

    @Override
    public BlockFaceShape getBlockFaceShape(IBlockAccess worldIn, BlockState state, BlockPos pos, Direction face)
    {
        return (face == Direction.UP || face == Direction.DOWN) ? BlockFaceShape.SOLID : BlockFaceShape.UNDEFINED;
    }

    @Override
    public TileEntity createNewTileEntity(World world, int meta)
    {
        return new TileEntityEmergencyBox();
    }

    @Override
    public ItemGroup getCreativeTabToDisplayOn()
    {
        return GalacticraftCore.galacticraftBlocksTab;
    }

    @Override
    public boolean onMachineActivated(World world, BlockPos pos, BlockState state, PlayerEntity player, Hand hand, ItemStack heldItem, Direction side, float hitX, float hitY, float hitZ)
    {
        TileEntity tile = world.getTileEntity(pos);
        if (tile instanceof TileEntityEmergencyBox)
        {
            if (!world.isRemote)
            {
                ((TileEntityEmergencyBox)tile).click(player, side, state.getValue(KIT));
            }
            return true;
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
    public EnumSortCategoryBlock getCategory(int meta)
    {
        return EnumSortCategoryBlock.GENERAL;
    }

    @Override
    public boolean isSealed(World world, BlockPos pos, Direction direction)
    {
        return direction.ordinal() < 2;
    }
    
    @Override
    public boolean isSideSolid(BlockState state, IBlockAccess world, BlockPos pos, Direction direction)
    {
        return direction.ordinal() < 2;
    }
}
