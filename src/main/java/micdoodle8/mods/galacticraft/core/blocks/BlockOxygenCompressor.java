package micdoodle8.mods.galacticraft.core.blocks;

import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.energy.tile.TileBaseUniversalElectrical;
import micdoodle8.mods.galacticraft.core.items.ItemBlockDesc;
import micdoodle8.mods.galacticraft.core.tile.TileEntityOxygenCompressor;
import micdoodle8.mods.galacticraft.core.tile.TileEntityOxygenDecompressor;
import micdoodle8.mods.galacticraft.core.util.EnumSortCategoryBlock;
import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
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

public class BlockOxygenCompressor extends BlockAdvancedTile implements ItemBlockDesc.IBlockShiftDesc, ISortableBlock
{
    public static final int OXYGEN_COMPRESSOR_METADATA = 0;
    public static final int OXYGEN_DECOMPRESSOR_METADATA = 4;

    public static final PropertyDirection FACING = PropertyDirection.create("facing", EnumFacing.Plane.HORIZONTAL);
    public static final PropertyEnum TYPE = PropertyEnum.create("type", EnumCompressorType.class);

    public enum EnumCompressorType implements IStringSerializable
    {
        COMPRESSOR(0, "compressor"),
        DECOMPRESSOR(1, "decompressor");

        private final int meta;
        private final String name;

        private EnumCompressorType(int meta, String name)
        {
            this.meta = meta;
            this.name = name;
        }

        public int getMeta()
        {
            return this.meta;
        }

        public static EnumCompressorType byMetadata(int meta)
        {
            return values()[meta];
        }

        @Override
        public String getName() {
            return this.name;
        }
    }

    /*private IIcon iconMachineSide;
    private IIcon iconCompressor1;
    private IIcon iconCompressor2;
    private IIcon iconDecompressor;
    private IIcon iconOxygenInput;
    private IIcon iconOxygenOutput;
    private IIcon iconInput;*/

    public BlockOxygenCompressor(boolean isActive, String assetName)
    {
        super(Material.ROCK);
        this.setHardness(1.0F);
        this.setSoundType(Block.soundTypeStone);
        //this.setBlockTextureName(GalacticraftCore.TEXTURE_PREFIX + assetName);
        this.setUnlocalizedName(assetName);
    }

    @Override
    public int getRenderType()
    {
        return GalacticraftCore.proxy.getBlockRender(this);
    }

    @Override
    public CreativeTabs getCreativeTabToDisplayOn()
    {
        return GalacticraftCore.galacticraftBlocksTab;
    }

    /*@Override
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister par1IconRegister)
    {
        this.iconMachineSide = par1IconRegister.registerIcon(GalacticraftCore.TEXTURE_PREFIX + "machine_blank");
        this.iconCompressor1 = par1IconRegister.registerIcon(GalacticraftCore.TEXTURE_PREFIX + "machine_compressor_1");
        this.iconCompressor2 = par1IconRegister.registerIcon(GalacticraftCore.TEXTURE_PREFIX + "machine_compressor_2");
        this.iconDecompressor = par1IconRegister.registerIcon(GalacticraftCore.TEXTURE_PREFIX + "machine_decompressor_1");
        this.iconOxygenInput = par1IconRegister.registerIcon(GalacticraftCore.TEXTURE_PREFIX + "machine_oxygen_input");
        this.iconOxygenOutput = par1IconRegister.registerIcon(GalacticraftCore.TEXTURE_PREFIX + "machine_oxygen_output");
        this.iconInput = par1IconRegister.registerIcon(GalacticraftCore.TEXTURE_PREFIX + "machine_input");
    }*/

    @Override
    public boolean onUseWrench(World world, BlockPos pos, EntityPlayer entityPlayer, EnumFacing side, float hitX, float hitY, float hitZ)
    {
        int metadata = getMetaFromState(world.getBlockState(pos));
        int change = world.getBlockState(pos).getValue(FACING).rotateY().getHorizontalIndex();

        world.setBlockState(pos, this.getStateFromMeta(metadata - (metadata % 4) + change), 3);

        TileEntity te = world.getTileEntity(pos);
        if (te instanceof TileBaseUniversalElectrical)
        {
            ((TileBaseUniversalElectrical) te).updateFacing();
        }

        return true;
    }

    @Override
    public boolean onMachineActivated(World world, BlockPos pos, IBlockState state, EntityPlayer entityPlayer, EnumFacing side, float hitX, float hitY, float hitZ)
    {
        entityPlayer.openGui(GalacticraftCore.instance, -1, world, pos.getX(), pos.getY(), pos.getZ());
        return true;
    }

    @Override
    public TileEntity createTileEntity(World world, IBlockState state)
    {
        int metadata = getMetaFromState(state);
        if (metadata >= BlockOxygenCompressor.OXYGEN_DECOMPRESSOR_METADATA)
        {
            return new TileEntityOxygenDecompressor();
        }
        else if (metadata >= BlockOxygenCompressor.OXYGEN_COMPRESSOR_METADATA)
        {
            return new TileEntityOxygenCompressor();
        }
        else
        {
            return null;
        }
    }

    /*@Override
    public IIcon getIcon(int side, int metadata)
    {
        if (side == 0 || side == 1)
        {
            return this.iconMachineSide;
        }

        if (metadata >= BlockOxygenCompressor.OXYGEN_DECOMPRESSOR_METADATA)
        {
            metadata -= BlockOxygenCompressor.OXYGEN_DECOMPRESSOR_METADATA;

            if (side == metadata + 2)
            {
                return this.iconInput;
            }
            else if (side == ForgeDirection.getOrientation(metadata + 2).getOpposite().ordinal())
            {
                return this.iconOxygenOutput;
            }
            else if (metadata == 0 && side == 5 || metadata == 3 && side == 3 || metadata == 1 && side == 4 || metadata == 2 && side == 2)
            {
                return this.iconCompressor2;
            }
            else
            {
                return this.iconDecompressor;
            }
        }
        else if (metadata >= BlockOxygenCompressor.OXYGEN_COMPRESSOR_METADATA)
        {
            metadata -= BlockOxygenCompressor.OXYGEN_COMPRESSOR_METADATA;

            if (side == metadata + 2)
            {
                return this.iconInput;
            }
            else if (side == ForgeDirection.getOrientation(metadata + 2).getOpposite().ordinal())
            {
                return this.iconOxygenInput;
            }
            else if (metadata == 0 && side == 5 || metadata == 3 && side == 3 || metadata == 1 && side == 4 || metadata == 2 && side == 2)
            {
                return this.iconCompressor2;
            }
            else
            {
                return this.iconCompressor1;
            }
        }
        else
        {
            return this.iconMachineSide;
        }
    }*/

    @Override
    public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack)
    {
        final int angle = (int)Math.floor(placer.rotationYaw * 4.0F / 360.0F + 0.5D) & 3;
        int change = EnumFacing.getHorizontal(angle).getOpposite().getHorizontalIndex();

        if (stack.getItemDamage() >= BlockOxygenCompressor.OXYGEN_DECOMPRESSOR_METADATA)
        {
            change += BlockOxygenCompressor.OXYGEN_DECOMPRESSOR_METADATA;
        }
        else if (stack.getItemDamage() >= BlockOxygenCompressor.OXYGEN_COMPRESSOR_METADATA)
        {
            change += BlockOxygenCompressor.OXYGEN_COMPRESSOR_METADATA;
        }

        worldIn.setBlockState(pos, getStateFromMeta(change), 3);
    }

    @Override
    public void getSubBlocks(Item par1, CreativeTabs par2CreativeTabs, List<ItemStack> par3List)
    {
        par3List.add(new ItemStack(this, 1, BlockOxygenCompressor.OXYGEN_COMPRESSOR_METADATA));
        par3List.add(new ItemStack(this, 1, BlockOxygenCompressor.OXYGEN_DECOMPRESSOR_METADATA));
    }

    @Override
    public int damageDropped(IBlockState state)
    {
        int metadata = getMetaFromState(state);
        if (metadata >= BlockOxygenCompressor.OXYGEN_DECOMPRESSOR_METADATA)
        {
            return BlockOxygenCompressor.OXYGEN_DECOMPRESSOR_METADATA;
        }
        else if (metadata >= BlockOxygenCompressor.OXYGEN_COMPRESSOR_METADATA)
        {
            return BlockOxygenCompressor.OXYGEN_COMPRESSOR_METADATA;
        }
        else
        {
            return 0;
        }
    }

    @Override
    public ItemStack getPickBlock(IBlockState state, RayTraceResult target, World world, BlockPos pos, EntityPlayer player)
    {
        int metadata = this.getDamageValue(world, pos);
        return new ItemStack(this, 1, metadata);
    }

    @Override
    public String getShiftDescription(int meta)
    {
        switch (meta)
        {
        case OXYGEN_COMPRESSOR_METADATA:
            return GCCoreUtil.translate("tile.oxygen_compressor.description");
        case OXYGEN_DECOMPRESSOR_METADATA:
            return GCCoreUtil.translate("tile.oxygen_decompressor.description");
        }
        return "";
    }

    @Override
    public boolean showDescription(int meta)
    {
        return true;
    }

    public IBlockState getStateFromMeta(int meta)
    {
        EnumFacing enumfacing = EnumFacing.getHorizontal(meta % 4);
        EnumCompressorType type = EnumCompressorType.byMetadata((int)Math.floor(meta / 4.0));
        return this.getDefaultState().withProperty(FACING, enumfacing).withProperty(TYPE, type);
    }

    public int getMetaFromState(IBlockState state)
    {
        return ((EnumFacing)state.getValue(FACING)).getHorizontalIndex() + ((EnumCompressorType)state.getValue(TYPE)).getMeta() * 4;
    }

    protected BlockStateContainer createBlockState()
    {
        return new BlockStateContainer(this, FACING, TYPE);
    }

    @Override
    public EnumSortCategoryBlock getCategory(int meta)
    {
        return EnumSortCategoryBlock.MACHINE;
    }
}
