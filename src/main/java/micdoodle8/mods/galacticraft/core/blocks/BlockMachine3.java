package micdoodle8.mods.galacticraft.core.blocks;

import micdoodle8.mods.galacticraft.core.GCBlocks;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.energy.tile.TileBaseUniversalElectrical;
import micdoodle8.mods.galacticraft.core.items.IShiftDescription;
import micdoodle8.mods.galacticraft.core.tile.TileEntityPainter;
import micdoodle8.mods.galacticraft.core.util.EnumSortCategoryBlock;
import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
import net.minecraft.block.Block;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.world.World;

import java.util.List;
import java.util.Random;

/**
 * A block for several types of Galacticraft machine
 * with a base building purpose - e.g. Painter
 *
 */
public class BlockMachine3 extends BlockTileGC implements IShiftDescription, ISortableBlock
{
    public static final int PAINTER_METADATA = 0;

    public static final int METADATA_MASK = 0x0c; //Used to select the machine type from metadata

    public static final PropertyDirection FACING = PropertyDirection.create("facing", EnumFacing.Plane.HORIZONTAL);
    public static final PropertyEnum<EnumMachineBuildingType> TYPE = PropertyEnum.create("type", EnumMachineBuildingType.class);

    public enum EnumMachineBuildingType implements IStringSerializable
    {
        PAINTER(0, "painter");

        private final int meta;
        private final String name;

        EnumMachineBuildingType(int meta, String name)
        {
            this.meta = meta;
            this.name = name;
        }

        public int getMeta()
        {
            return this.meta;
        }

        public static EnumMachineBuildingType byMetadata(int meta)
        {
            return values()[meta];
        }

        @Override
        public String getName()
        {
            return this.name;
        }
    }

    public BlockMachine3(String assetName)
    {
        super(GCBlocks.machine);
        this.setHardness(1.0F);
        this.setStepSound(Block.soundTypeMetal);
        this.setUnlocalizedName(assetName);
    }

    @Override
    public CreativeTabs getCreativeTabToDisplayOn()
    {
        return GalacticraftCore.galacticraftBlocksTab;
    }

    @Override
    public boolean isOpaqueCube()
    {
        return true;
    }

    @Override
    public boolean isFullCube()
    {
        return true;
    }

    @Override
    public void randomDisplayTick(World worldIn, BlockPos pos, IBlockState state, Random rand)
    {
    }

    @Override
    public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack)
    {
        int metadata = getMetaFromState(state);

        final int angle = MathHelper.floor_double(placer.rotationYaw * 4.0F / 360.0F + 0.5D) & 3;
        int change = EnumFacing.getHorizontal(angle).getOpposite().getHorizontalIndex();

        worldIn.setBlockState(pos, getStateFromMeta((metadata & METADATA_MASK) + change), 3);
    }


    @Override
    public boolean onUseWrench(World world, BlockPos pos, EntityPlayer entityPlayer, EnumFacing side, float hitX, float hitY, float hitZ)
    {
        IBlockState state = world.getBlockState(pos);
        TileBaseUniversalElectrical.onUseWrenchBlock(state, world, pos, state.getValue(FACING));
        return true;
    }

    @Override
    public boolean onMachineActivated(World world, BlockPos pos, IBlockState state, EntityPlayer entityPlayer, EnumFacing side, float hitX, float hitY, float hitZ)
    {
        if (!world.isRemote)
        {
            entityPlayer.openGui(GalacticraftCore.instance, -1, world, pos.getX(), pos.getY(), pos.getZ());
            return true;
        }

        return true;
    }

    @Override
    public TileEntity createTileEntity(World world, IBlockState state)
    {
        int metadata = getMetaFromState(state) & METADATA_MASK;
        if (metadata == BlockMachine3.PAINTER_METADATA)
        {
            return new TileEntityPainter();
        }
        return null;
    }

    public ItemStack getPainter()
    {
        return new ItemStack(this, 1, BlockMachine3.PAINTER_METADATA);
    }

    @Override
    public void getSubBlocks(Item par1, CreativeTabs par2CreativeTabs, List<ItemStack> par3List)
    {
        par3List.add(this.getPainter());
    }

    @Override
    public int damageDropped(IBlockState state)
    {
        return getMetaFromState(state) & METADATA_MASK;
    }

    @Override
    public ItemStack getPickBlock(MovingObjectPosition target, World world, BlockPos pos, EntityPlayer player)
    {
        int metadata = this.getDamageValue(world, pos);

        return new ItemStack(this, 1, metadata);
    }

    @Override
    public String getShiftDescription(int meta)
    {
        switch (meta)
        {
        case PAINTER_METADATA:
            return GCCoreUtil.translate("tile.painter.description");
        }
        return "";
    }

    @Override
    public boolean showDescription(int meta)
    {
        return true;
    }

    @Override
    public IBlockState getStateFromMeta(int meta)
    {
        EnumFacing enumfacing = EnumFacing.getHorizontal(meta % 4);
        EnumMachineBuildingType type = EnumMachineBuildingType.byMetadata(meta / 4);
        return this.getDefaultState().withProperty(FACING, enumfacing).withProperty(TYPE, type);
    }

    @Override
    public int getMetaFromState(IBlockState state)
    {
        return (state.getValue(FACING)).getHorizontalIndex() + ((EnumMachineBuildingType) state.getValue(TYPE)).getMeta() * 4;
    }

    @Override
    protected BlockState createBlockState()
    {
        return new BlockState(this, FACING, TYPE);
    }

    @Override
    public EnumSortCategoryBlock getCategory(int meta)
    {
        return EnumSortCategoryBlock.MACHINE;
    }
}
