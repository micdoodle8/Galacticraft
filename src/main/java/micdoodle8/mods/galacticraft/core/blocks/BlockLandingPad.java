package micdoodle8.mods.galacticraft.core.blocks;

import micdoodle8.mods.galacticraft.api.block.IPartialSealableBlock;
import micdoodle8.mods.galacticraft.core.GCBlocks;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.items.IShiftDescription;
import micdoodle8.mods.galacticraft.core.tile.TileEntityBuggyFuelerSingle;
import micdoodle8.mods.galacticraft.core.tile.TileEntityLandingPadSingle;
import micdoodle8.mods.galacticraft.core.util.EnumSortCategoryBlock;
import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.IStringSerializable;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

public class BlockLandingPad extends BlockAdvancedTile implements IPartialSealableBlock, IShiftDescription, ISortableBlock
{
    public static final PropertyEnum<EnumLandingPadType> PAD_TYPE = PropertyEnum.create("type", EnumLandingPadType.class);

    public enum EnumLandingPadType implements IStringSerializable
    {
        ROCKET_PAD(0, "rocket"),
        BUGGY_PAD(1, "buggy");

        private final int meta;
        private final String name;

        EnumLandingPadType(int meta, String name)
        {
            this.meta = meta;
            this.name = name;
        }

        public int getMeta()
        {
            return this.meta;
        }

        public static EnumLandingPadType byMetadata(int meta)
        {
            return values()[meta];
        }

        @Override
        public String getName()
        {
            return this.name;
        }
    }

    public BlockLandingPad(String assetName)
    {
        super(Material.iron);
        this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 3 / 16.0F, 1.0F);
        this.setHardness(1.0F);
        this.setResistance(10.0F);
        this.setStepSound(Block.soundTypeMetal);
        this.setUnlocalizedName(assetName);
    }

    @Override
    public CreativeTabs getCreativeTabToDisplayOn()
    {
        return GalacticraftCore.galacticraftBlocksTab;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void getSubBlocks(Item par1, CreativeTabs par2CreativeTabs, List<ItemStack> par3List)
    {
        for (int i = 0; i < 2; i++)
        {
            par3List.add(new ItemStack(par1, 1, i));
        }
    }

    private boolean checkAxis(World worldIn, BlockPos pos, Block block, EnumFacing facing)
    {
        int sameCount = 0;
        for (int i = 1; i <= 3; i++)
        {
            if (worldIn.getBlockState(pos.offset(facing, i)).getBlock() == block)
            {
                sameCount++;
            }
        }

        return sameCount < 3;
    }

    @Override
    public boolean canPlaceBlockOnSide(World worldIn, BlockPos pos, EnumFacing side)
    {
        final Block id = GCBlocks.landingPad;

        if (!checkAxis(worldIn, pos, id, EnumFacing.EAST) ||
                !checkAxis(worldIn, pos, id, EnumFacing.WEST) ||
                !checkAxis(worldIn, pos, id, EnumFacing.NORTH) ||
                !checkAxis(worldIn, pos, id, EnumFacing.SOUTH))
        {
            return false;
        }

        if (worldIn.getBlockState(pos.offset(EnumFacing.DOWN)).getBlock() == GCBlocks.landingPad && side == EnumFacing.UP)
        {
            return false;
        }
        else
        {
            return this.canPlaceBlockAt(worldIn, pos);
        }
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
    public TileEntity createNewTileEntity(World world, int meta)
    {
        switch (meta)
        {
        case 0:
            return new TileEntityLandingPadSingle();
        case 1:
            return new TileEntityBuggyFuelerSingle();
        default:
            return null;
        }
    }

    @Override
    public boolean isSealed(World worldIn, BlockPos pos, EnumFacing direction)
    {
        return direction == EnumFacing.UP;
    }

    @Override
    public int damageDropped(IBlockState state)
    {
        return getMetaFromState(state);
    }

    @Override
    public String getShiftDescription(int meta)
    {
        if (meta == 0)
        {
            return GCCoreUtil.translate(this.getUnlocalizedName() + ".description");
        }
        return GCCoreUtil.translate("tile.buggy_pad.description");
    }

    @Override
    public boolean showDescription(int meta)
    {
        return true;
    }

    @Override
    public IBlockState getStateFromMeta(int meta)
    {
        return this.getDefaultState().withProperty(PAD_TYPE, EnumLandingPadType.byMetadata(meta));
    }

    @Override
    public int getMetaFromState(IBlockState state)
    {
        return ((EnumLandingPadType) state.getValue(PAD_TYPE)).getMeta();
    }

    @Override
    protected BlockState createBlockState()
    {
        return new BlockState(this, PAD_TYPE);
    }

    @Override
    public EnumSortCategoryBlock getCategory(int meta)
    {
        return EnumSortCategoryBlock.PAD;
    }
}
