package micdoodle8.mods.galacticraft.core.blocks;

import micdoodle8.mods.galacticraft.core.GCBlocks;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.energy.tile.TileBaseUniversalElectrical;
import micdoodle8.mods.galacticraft.core.items.IShiftDescription;
import micdoodle8.mods.galacticraft.core.tile.TileEntityCircuitFabricator;
import micdoodle8.mods.galacticraft.core.tile.TileEntityCoalGenerator;
import micdoodle8.mods.galacticraft.core.tile.TileEntityElectricIngotCompressor;
import micdoodle8.mods.galacticraft.core.tile.TileEntityOxygenStorageModule;
import micdoodle8.mods.galacticraft.core.util.EnumSortCategoryBlock;
import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
import net.minecraft.block.SoundType;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

import java.util.Random;

public class BlockMachine2 extends BlockTileGC implements IShiftDescription, ISortableBlock
{
    public static final int ELECTRIC_COMPRESSOR_METADATA = 0;
    public static final int CIRCUIT_FABRICATOR_METADATA = 4;
    public static final int OXYGEN_STORAGE_MODULE_METADATA = 8;

    public static final PropertyDirection FACING = PropertyDirection.create("facing", EnumFacing.Plane.HORIZONTAL);
    public static final PropertyEnum TYPE = PropertyEnum.create("type", EnumMachineExtendedType.class);

    public enum EnumMachineExtendedType implements IStringSerializable
    {
        ELECTRIC_COMPRESSOR(0, "electric_compressor"),
        CIRCUIT_FABRICATOR(1, "circuit_fabricator"),
        OXYGEN_STORAGE(2, "oxygen_storage");

        private final int meta;
        private final String name;

        EnumMachineExtendedType(int meta, String name)
        {
            this.meta = meta;
            this.name = name;
        }

        public int getMeta()
        {
            return this.meta;
        }

        public static EnumMachineExtendedType byMetadata(int meta)
        {
            return values()[meta];
        }

        @Override
        public String getName()
        {
            return this.name;
        }
    }

    public BlockMachine2(String assetName)
    {
        super(GCBlocks.machine);
        this.setHardness(1.0F);
        this.setSoundType(SoundType.METAL);
        this.setUnlocalizedName(assetName);
    }

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
    public void randomDisplayTick(IBlockState stateIn, World worldIn, BlockPos pos, Random rand)
    {
        TileEntity tile = worldIn.getTileEntity(pos);

        if (tile instanceof TileEntityCoalGenerator)
        {
            TileEntityCoalGenerator tileEntity = (TileEntityCoalGenerator) tile;
            if (tileEntity.heatGJperTick > 0)
            {
                int metadata = getMetaFromState(stateIn);
                float particlePosX = pos.getX() + 0.5F;
                float particlePosY = pos.getY() + 0.0F + rand.nextFloat() * 6.0F / 16.0F;
                float particlePosZ = pos.getZ() + 0.5F;
                float particleSize0 = 0.52F;
                float particleSize1 = rand.nextFloat() * 0.6F - 0.3F;

                if (metadata == 3)
                {
                    worldIn.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, particlePosX - particleSize0, particlePosY, particlePosZ + particleSize1, 0.0D, 0.0D, 0.0D);
                    worldIn.spawnParticle(EnumParticleTypes.FLAME, particlePosX - particleSize0, particlePosY, particlePosZ + particleSize1, 0.0D, 0.0D, 0.0D);
                }
                else if (metadata == 2)
                {
                    worldIn.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, particlePosX + particleSize0, particlePosY, particlePosZ + particleSize1, 0.0D, 0.0D, 0.0D);
                    worldIn.spawnParticle(EnumParticleTypes.FLAME, particlePosX + particleSize0, particlePosY, particlePosZ + particleSize1, 0.0D, 0.0D, 0.0D);
                }
                else if (metadata == 1)
                {
                    worldIn.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, particlePosX + particleSize1, particlePosY, particlePosZ - particleSize0, 0.0D, 0.0D, 0.0D);
                    worldIn.spawnParticle(EnumParticleTypes.FLAME, particlePosX + particleSize1, particlePosY, particlePosZ - particleSize0, 0.0D, 0.0D, 0.0D);
                }
                else if (metadata == 0)
                {
                    worldIn.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, particlePosX + particleSize1, particlePosY, particlePosZ + particleSize0, 0.0D, 0.0D, 0.0D);
                    worldIn.spawnParticle(EnumParticleTypes.FLAME, particlePosX + particleSize1, particlePosY, particlePosZ + particleSize0, 0.0D, 0.0D, 0.0D);
                }
            }
        }
    }

    @Override
    public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack)
    {
        int metadata = getMetaFromState(state);

        final int angle = MathHelper.floor(placer.rotationYaw * 4.0F / 360.0F + 0.5D) & 3;
        int change = EnumFacing.getHorizontal(angle).getOpposite().getHorizontalIndex();

        if (metadata >= BlockMachine2.OXYGEN_STORAGE_MODULE_METADATA)
        {
            worldIn.setBlockState(pos, getStateFromMeta(BlockMachine2.OXYGEN_STORAGE_MODULE_METADATA + change), 3);
        }
        else if (metadata >= BlockMachine2.CIRCUIT_FABRICATOR_METADATA)
        {
            worldIn.setBlockState(pos, getStateFromMeta(BlockMachine2.CIRCUIT_FABRICATOR_METADATA + change), 3);
        }
        else if (metadata >= BlockMachine2.ELECTRIC_COMPRESSOR_METADATA)
        {
            worldIn.setBlockState(pos, getStateFromMeta(BlockMachine2.ELECTRIC_COMPRESSOR_METADATA + change), 3);
        }
    }

    @Override
    public boolean onUseWrench(World world, BlockPos pos, EntityPlayer entityPlayer, EnumHand hand, ItemStack heldItem, EnumFacing side, float hitX, float hitY, float hitZ)
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
    public boolean onMachineActivated(World world, BlockPos pos, IBlockState state, EntityPlayer entityPlayer, EnumHand hand, ItemStack heldItem, EnumFacing side, float hitX, float hitY, float hitZ)
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
        int metadata = getMetaFromState(state);
        if (metadata >= BlockMachine2.OXYGEN_STORAGE_MODULE_METADATA)
        {
            return new TileEntityOxygenStorageModule();
        }
        else if (metadata >= BlockMachine2.CIRCUIT_FABRICATOR_METADATA)
        {
            return new TileEntityCircuitFabricator();
        }
        else if (metadata >= BlockMachine2.ELECTRIC_COMPRESSOR_METADATA)
        {
            return new TileEntityElectricIngotCompressor();
        }
        else
        {
            return null;
        }
    }

    public ItemStack getElectricCompressor()
    {
        return new ItemStack(this, 1, BlockMachine2.ELECTRIC_COMPRESSOR_METADATA);
    }

    public ItemStack getCircuitFabricator()
    {
        return new ItemStack(this, 1, BlockMachine2.CIRCUIT_FABRICATOR_METADATA);
    }

    public ItemStack getOxygenStorageModule()
    {
        return new ItemStack(this, 1, BlockMachine2.OXYGEN_STORAGE_MODULE_METADATA);
    }

    @Override
    public void getSubBlocks(Item itemIn, CreativeTabs tab, NonNullList<ItemStack> list)
    {
        list.add(this.getElectricCompressor());
        list.add(this.getCircuitFabricator());
        list.add(this.getOxygenStorageModule());
    }

    @Override
    public int damageDropped(IBlockState state)
    {
        int metadata = getMetaFromState(state);
        if (metadata >= BlockMachine2.OXYGEN_STORAGE_MODULE_METADATA)
        {
            return BlockMachine2.OXYGEN_STORAGE_MODULE_METADATA;
        }
        else if (metadata >= BlockMachine2.CIRCUIT_FABRICATOR_METADATA)
        {
            return BlockMachine2.CIRCUIT_FABRICATOR_METADATA;
        }
        else if (metadata >= BlockMachine2.ELECTRIC_COMPRESSOR_METADATA)
        {
            return BlockMachine2.ELECTRIC_COMPRESSOR_METADATA;
        }
        else
        {
            return 0;
        }
    }

    @Override
    public String getShiftDescription(int meta)
    {
        switch (meta)
        {
        case CIRCUIT_FABRICATOR_METADATA:
            return GCCoreUtil.translate("tile.circuit_fabricator.description");
        case ELECTRIC_COMPRESSOR_METADATA:
            return GCCoreUtil.translate("tile.compressor_electric.description");
        case OXYGEN_STORAGE_MODULE_METADATA:
            return GCCoreUtil.translate("tile.oxygen_storage_module.description");
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
        EnumMachineExtendedType type = EnumMachineExtendedType.byMetadata((int) Math.floor(meta / 4.0));
        return this.getDefaultState().withProperty(FACING, enumfacing).withProperty(TYPE, type);
    }

    @Override
    public int getMetaFromState(IBlockState state)
    {
        return (state.getValue(FACING)).getHorizontalIndex() + ((EnumMachineExtendedType) state.getValue(TYPE)).getMeta() * 4;
    }

    @Override
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
