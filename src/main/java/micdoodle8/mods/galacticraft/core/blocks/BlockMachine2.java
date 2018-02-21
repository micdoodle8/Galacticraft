package micdoodle8.mods.galacticraft.core.blocks;

import micdoodle8.mods.galacticraft.core.GCBlocks;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.energy.tile.TileBaseUniversalElectrical;
import micdoodle8.mods.galacticraft.core.items.IShiftDescription;
import micdoodle8.mods.galacticraft.core.tile.IMachineSides;
import micdoodle8.mods.galacticraft.core.tile.IMachineSidesProperties;
import micdoodle8.mods.galacticraft.core.tile.TileEntityCircuitFabricator;
import micdoodle8.mods.galacticraft.core.tile.TileEntityDeconstructor;
import micdoodle8.mods.galacticraft.core.tile.TileEntityElectricIngotCompressor;
import micdoodle8.mods.galacticraft.core.tile.TileEntityOxygenStorageModule;
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
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import java.util.List;
import java.util.Random;

public class BlockMachine2 extends BlockTileGC implements IShiftDescription, ISortableBlock
{
    public static final int ELECTRIC_COMPRESSOR_METADATA = 0;
    public static final int CIRCUIT_FABRICATOR_METADATA = 4;
    public static final int OXYGEN_STORAGE_MODULE_METADATA = 8;
    public static final int DECONSTRUCTOR_METADATA = 12;

    public static final PropertyDirection FACING = PropertyDirection.create("facing", EnumFacing.Plane.HORIZONTAL);
    public static final PropertyEnum<EnumMachineExtendedType> TYPE = PropertyEnum.create("type", EnumMachineExtendedType.class);
    public static IMachineSidesProperties MACHINESIDES_RENDERTYPE = IMachineSidesProperties.TWOFACES_HORIZ;
    public static final PropertyEnum SIDES = MACHINESIDES_RENDERTYPE.asProperty;

    public enum EnumMachineExtendedType implements IStringSerializable
    {
        ELECTRIC_COMPRESSOR(0, "electric_compressor"),
        CIRCUIT_FABRICATOR(1, "circuit_fabricator"),
        OXYGEN_STORAGE(2, "oxygen_storage"),
        DECONSTRUCTOR(3, "deconstructor");

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
        TileEntity tile = worldIn.getTileEntity(pos);

    }

    @Override
    public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack)
    {
        int metadata = getMetaFromState(state);

        final int angle = MathHelper.floor_double(placer.rotationYaw * 4.0F / 360.0F + 0.5D) & 3;
        int change = EnumFacing.getHorizontal(angle).getOpposite().getHorizontalIndex();
        worldIn.setBlockState(pos, getStateFromMeta((metadata & 0x0c) + change), 3);
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
        int metadata = getMetaFromState(state);
        if (metadata >= BlockMachine2.DECONSTRUCTOR_METADATA)
        {
            return new TileEntityDeconstructor();
        }
        else if (metadata >= BlockMachine2.OXYGEN_STORAGE_MODULE_METADATA)
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

    public ItemStack getDeconstructor()
    {
        return new ItemStack(this, 1, BlockMachine2.DECONSTRUCTOR_METADATA);
    }

    @Override
    public void getSubBlocks(Item par1, CreativeTabs par2CreativeTabs, List<ItemStack> par3List)
    {
        par3List.add(this.getElectricCompressor());
        par3List.add(this.getCircuitFabricator());
        par3List.add(this.getOxygenStorageModule());
        par3List.add(this.getDeconstructor());
    }

    @Override
    public int damageDropped(IBlockState state)
    {
        int metadata = getMetaFromState(state);
        return metadata & 0x0c;
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
        case CIRCUIT_FABRICATOR_METADATA:
            return GCCoreUtil.translate("tile.circuit_fabricator.description");
        case ELECTRIC_COMPRESSOR_METADATA:
            return GCCoreUtil.translate("tile.compressor_electric.description");
        case OXYGEN_STORAGE_MODULE_METADATA:
            return GCCoreUtil.translate("tile.oxygen_storage_module.description");
        case DECONSTRUCTOR_METADATA:
            return GCCoreUtil.translate("tile.deconstructor.description");
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
        EnumMachineExtendedType type = EnumMachineExtendedType.byMetadata(meta / 4);
        return this.getDefaultState().withProperty(FACING, enumfacing).withProperty(TYPE, type);
    }

    @Override
    public int getMetaFromState(IBlockState state)
    {
        return (state.getValue(FACING)).getHorizontalIndex() + ((EnumMachineExtendedType) state.getValue(TYPE)).getMeta() * 4;
    }

    @Override
    protected BlockState createBlockState()
    {
        return new BlockState(this, FACING, TYPE, SIDES);
    }

    @Override
    public IBlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos)
    {
        TileEntity tile = worldIn.getTileEntity(pos);
        return IMachineSides.addPropertyForTile(state, tile, MACHINESIDES_RENDERTYPE, SIDES);
    }

    @Override
    public EnumSortCategoryBlock getCategory(int meta)
    {
        return EnumSortCategoryBlock.MACHINE;
    }
    
    @Override
    public boolean onSneakUseWrench(World world, BlockPos pos, EntityPlayer entityPlayer, EnumFacing side, float hitX, float hitY, float hitZ)
    {
        TileEntity tile = world.getTileEntity(pos);
        if (tile instanceof IMachineSides)
        {
            ((IMachineSides)tile).nextSideConfiguration(tile);
            return true;
        }
        return false;
    }
}
