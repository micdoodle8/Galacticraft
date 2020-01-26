package micdoodle8.mods.galacticraft.core.blocks;

import micdoodle8.mods.galacticraft.core.GCBlocks;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.energy.tile.TileBaseUniversalElectrical;
import micdoodle8.mods.galacticraft.core.tile.IMachineSides;
import micdoodle8.mods.galacticraft.core.tile.IMachineSidesProperties;
import micdoodle8.mods.galacticraft.core.tile.TileEntityCircuitFabricator;
import micdoodle8.mods.galacticraft.core.tile.TileEntityDeconstructor;
import micdoodle8.mods.galacticraft.core.tile.TileEntityElectricIngotCompressor;
import micdoodle8.mods.galacticraft.core.tile.TileEntityOxygenStorageModule;
import micdoodle8.mods.galacticraft.core.util.EnumSortCategoryBlock;
import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
import net.minecraft.block.SoundType;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import java.util.Random;

public class BlockMachine2 extends BlockMachineBase
{
    public static final PropertyEnum<EnumMachineExtendedType> TYPE = PropertyEnum.create("type", EnumMachineExtendedType.class);
    public static IMachineSidesProperties MACHINESIDES_RENDERTYPE = IMachineSidesProperties.TWOFACES_HORIZ;
    public static final PropertyEnum SIDES = MACHINESIDES_RENDERTYPE.asProperty;

    public enum EnumMachineExtendedType implements IStringSerializable
    {
        ELECTRIC_COMPRESSOR(0, "electric_compressor", TileEntityElectricIngotCompressor.class, "tile.compressor_electric.description", "tile.machine2.4"),
        CIRCUIT_FABRICATOR(1, "circuit_fabricator", TileEntityCircuitFabricator.class, "tile.circuit_fabricator.description", "tile.machine2.5"),
        OXYGEN_STORAGE(2, "oxygen_storage", TileEntityOxygenStorageModule.class, "tile.oxygen_storage_module.description", "tile.machine2.6"),
        DECONSTRUCTOR(3, "deconstructor", TileEntityDeconstructor.class, "tile.deconstructor.description", "tile.machine2.10");
        
        private final int meta;
        private final String name;
        private final Class tile;
        private final String shiftDescriptionKey;
        private final String blockName;

        EnumMachineExtendedType(int meta, String name, Class tile, String key, String blockName)
        {
            this.meta = meta;
            this.name = name;
            this.tile = tile;
            this.shiftDescriptionKey = key;
            this.blockName = blockName;
        }

        public int getMetadata()
        {
            return this.meta * 4;
        }

        private final static EnumMachineExtendedType[] values = values();
        public static EnumMachineExtendedType byMeta(int meta)
        {
            return values[meta % values.length];
        }
        
        public static EnumMachineExtendedType getByMetadata(int metadata)
        {
            return byMeta(metadata / 4);
        }

        @Override
        public String getName()
        {
            return this.name;
        }
        
        public TileEntity tileConstructor()
        {
            try
            {
                return (TileEntity) this.tile.newInstance();
            } catch (InstantiationException | IllegalAccessException ex)
            {
                return null;
            }
        }

        public String getShiftDescription()
        {
            return GCCoreUtil.translate(this.shiftDescriptionKey);
        }

        public String getUnlocalizedName()
        {
            return this.blockName;
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
        return true;
    }

    @Override
    public boolean isFullCube(IBlockState state)
    {
        return true;
    }

    @Override
    public void randomDisplayTick(IBlockState stateIn, World worldIn, BlockPos pos, Random rand)
    {
//        TileEntity tile = worldIn.getTileEntity(pos);

    }

    @Override
    public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack)
    {
        int metadata = getMetaFromState(state);

        final int angle = MathHelper.floor(placer.rotationYaw * 4.0F / 360.0F + 0.5D) & 3;
        int change = EnumFacing.getHorizontal(angle).getOpposite().getHorizontalIndex();
        worldIn.setBlockState(pos, getStateFromMeta((metadata & BlockMachineBase.METADATA_MASK) + change), 3);
    }

    @Override
    public boolean onUseWrench(World world, BlockPos pos, EntityPlayer entityPlayer, EnumHand hand, ItemStack heldItem, EnumFacing side, float hitX, float hitY, float hitZ)
    {
        IBlockState state = world.getBlockState(pos);
        TileBaseUniversalElectrical.onUseWrenchBlock(state, world, pos, state.getValue(FACING));
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
        int meta = getMetaFromState(state);
        EnumMachineExtendedType type = EnumMachineExtendedType.getByMetadata(meta);
        return type.tileConstructor();
    }

    @Override
    public void getSubBlocks(CreativeTabs tab, NonNullList<ItemStack> list)
    {
        for (EnumMachineExtendedType type : EnumMachineExtendedType.values)
            list.add(new ItemStack(this, 1, type.getMetadata()));
    }

    @Override
    public int damageDropped(IBlockState state)
    {
        int metadata = getMetaFromState(state);
        return metadata & BlockMachineBase.METADATA_MASK;
    }

    @Override
    public String getShiftDescription(int meta)
    {
        EnumMachineExtendedType type = EnumMachineExtendedType.getByMetadata(meta);
        return type.getShiftDescription();
    }

    @Override
    public boolean showDescription(int meta)
    {
        return true;
    }

    @Override
    public String getUnlocalizedName(int meta)
    {
        EnumMachineExtendedType type = EnumMachineExtendedType.getByMetadata(meta);
        return type.getUnlocalizedName();
    }

    @Override
    public IBlockState getStateFromMeta(int meta)
    {
        EnumFacing enumfacing = EnumFacing.getHorizontal(meta % 4);
        EnumMachineExtendedType type = EnumMachineExtendedType.getByMetadata(meta);
        return this.getDefaultState().withProperty(FACING, enumfacing).withProperty(TYPE, type);
    }

    @Override
    public int getMetaFromState(IBlockState state)
    {
        return (state.getValue(FACING)).getHorizontalIndex() + ((EnumMachineExtendedType) state.getValue(TYPE)).getMetadata();
    }

    @Override
    protected BlockStateContainer createBlockState()
    {
        return new BlockStateContainer(this, FACING, TYPE, SIDES);
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
    public boolean onSneakUseWrench(World world, BlockPos pos, EntityPlayer entityPlayer, EnumHand hand, ItemStack heldItem, EnumFacing side, float hitX, float hitY, float hitZ)
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
