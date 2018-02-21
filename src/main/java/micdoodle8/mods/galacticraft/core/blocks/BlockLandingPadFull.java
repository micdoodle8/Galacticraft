package micdoodle8.mods.galacticraft.core.blocks;

import micdoodle8.mods.galacticraft.api.block.IPartialSealableBlock;
import micdoodle8.mods.galacticraft.core.GCBlocks;
import micdoodle8.mods.galacticraft.core.tile.IMultiBlock;
import micdoodle8.mods.galacticraft.core.tile.TileEntityBuggyFueler;
import micdoodle8.mods.galacticraft.core.tile.TileEntityLandingPad;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Random;

public class BlockLandingPadFull extends BlockAdvancedTile implements IPartialSealableBlock
{
    public static final PropertyEnum<EnumLandingPadFullType> PAD_TYPE = PropertyEnum.create("type", EnumLandingPadFullType.class);

    public enum EnumLandingPadFullType implements IStringSerializable
    {
        ROCKET_PAD(0, "rocket"),
        BUGGY_PAD(1, "buggy");

        private final int meta;
        private final String name;

        EnumLandingPadFullType(int meta, String name)
        {
            this.meta = meta;
            this.name = name;
        }

        public int getMeta()
        {
            return this.meta;
        }

        public static EnumLandingPadFullType byMetadata(int meta)
        {
            return values()[meta];
        }

        @Override
        public String getName()
        {
            return this.name;
        }
    }

    public BlockLandingPadFull(String assetName)
    {
        super(Material.rock);
        this.setHardness(1.0F);
        this.setResistance(10.0F);
        this.setStepSound(Block.soundTypeMetal);
        this.setUnlocalizedName(assetName);
        this.maxY = 0.25F;
    }

    @Override
    public int damageDropped(IBlockState state)
    {
        return getMetaFromState(state);
    }

    @Override
    public int quantityDropped(Random par1Random)
    {
        return 9;
    }

    @Override
    public void breakBlock(World worldIn, BlockPos pos, IBlockState state)
    {
        final TileEntity var9 = worldIn.getTileEntity(pos);

        if (var9 instanceof IMultiBlock)
        {
            ((IMultiBlock) var9).onDestroy(var9);
        }

        super.breakBlock(worldIn, pos, state);
    }

    @Override
    public Item getItemDropped(IBlockState state, Random rand, int fortune)
    {
        return Item.getItemFromBlock(GCBlocks.landingPad);
    }

    @Override
    public AxisAlignedBB getCollisionBoundingBox(World worldIn, BlockPos pos, IBlockState state)
    {
        switch (getMetaFromState(state))
        {
        case 0:
            return AxisAlignedBB.fromBounds(pos.getX() + this.minX, pos.getY() + this.minY, pos.getZ() + this.minZ,
                    pos.getX() + this.maxX, pos.getY() + this.maxY, pos.getZ() + this.maxZ);
//        case 2:
//            return AxisAlignedBB.fromBounds(pos.getX() + this.minX, pos.getY() + this.minY, pos.getZ() + this.minZ,
//                    pos.getX() + this.maxX, pos.getY() + this.maxY, pos.getZ() + this.maxZ);
        default:
            return AxisAlignedBB.fromBounds(pos.getX() + 0.0D, pos.getY() + 0.0D, pos.getZ() + 0.0D,
                    pos.getX() + 1.0D, pos.getY() + 0.2D, pos.getZ() + 1.0D);
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public AxisAlignedBB getSelectedBoundingBox(World worldIn, BlockPos pos)
    {
        switch (getMetaFromState(worldIn.getBlockState(pos)))
        {
        case 0:
            return AxisAlignedBB.fromBounds(pos.getX() + this.minX, pos.getY() + this.minY, pos.getZ() + this.minZ,
                    pos.getX() + this.maxX, pos.getY() + this.maxY, pos.getZ() + this.maxZ);
//        case 2:
//            return AxisAlignedBB.fromBounds(pos.getX() + this.minX, pos.getY() + this.minY, pos.getZ() + this.minZ,
//                    pos.getX() + this.maxX, pos.getY() + this.maxY, pos.getZ() + this.maxZ);
        default:
            return AxisAlignedBB.fromBounds(pos.getX() + 0.0D, pos.getY() + 0.0D, pos.getZ() + 0.0D,
                    pos.getX() + 1.0D, pos.getY() + 0.2D, pos.getZ() + 1.0D);
        }
    }

    @Override
    public boolean canPlaceBlockAt(World worldIn, BlockPos pos)
    {
        for (int x2 = -1; x2 < 2; ++x2)
        {
            for (int z2 = -1; z2 < 2; ++z2)
            {
                if (!super.canPlaceBlockAt(worldIn, new BlockPos(pos.getX() + x2, pos.getY(), pos.getZ() + z2)))
                {
                    return false;
                }
            }

        }

        return true;
    }

    @Override
    public boolean hasTileEntity(IBlockState state)
    {
        return true;
    }

    @Override
    public TileEntity createTileEntity(World world, IBlockState state)
    {
        switch (getMetaFromState(state))
        {
        case 0:
            return new TileEntityLandingPad();
        case 1:
            return new TileEntityBuggyFueler();
        // case 2:
        // return new GCCoreTileEntityCargoPad();
        default:
            return null;
        }
    }

    @Override
    public void onNeighborBlockChange(World worldIn, BlockPos pos, IBlockState state, Block neighborBlock)
    {
        worldIn.markBlockForUpdate(pos);
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

    @SideOnly(Side.CLIENT)
    @Override
    public boolean shouldSideBeRendered(IBlockAccess worldIn, BlockPos pos, EnumFacing side)
    {
        return true;
    }

    @Override
    public boolean isSealed(World worldIn, BlockPos pos, EnumFacing direction)
    {
        return direction == EnumFacing.UP;
    }

    @Override
    public ItemStack getPickBlock(MovingObjectPosition target, World world, BlockPos pos, EntityPlayer player)
    {
        int metadata = getMetaFromState(world.getBlockState(pos));
        return new ItemStack(Item.getItemFromBlock(GCBlocks.landingPad), 1, metadata);
    }

    @Override
    public IBlockState getStateFromMeta(int meta)
    {
        return this.getDefaultState().withProperty(PAD_TYPE, EnumLandingPadFullType.byMetadata(meta));
    }

    @Override
    public int getMetaFromState(IBlockState state)
    {
        return ((EnumLandingPadFullType) state.getValue(PAD_TYPE)).getMeta();
    }

    @Override
    protected BlockState createBlockState()
    {
        return new BlockState(this, PAD_TYPE);
    }
}
