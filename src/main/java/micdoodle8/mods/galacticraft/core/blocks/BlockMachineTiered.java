package micdoodle8.mods.galacticraft.core.blocks;

import micdoodle8.mods.galacticraft.core.GCBlocks;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.energy.tile.TileBaseUniversalElectrical;
import micdoodle8.mods.galacticraft.core.items.IShiftDescription;
import micdoodle8.mods.galacticraft.core.tile.IMachineSides;
import micdoodle8.mods.galacticraft.core.tile.IMachineSidesProperties;
import micdoodle8.mods.galacticraft.core.tile.TileEntityElectricFurnace;
import micdoodle8.mods.galacticraft.core.tile.TileEntityEnergyStorageModule;
import micdoodle8.mods.galacticraft.core.util.EnumSortCategoryBlock;
import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
import net.minecraft.block.SoundType;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockMachineTiered extends BlockTileGC implements IShiftDescription, ISortableBlock
{
    public static final int STORAGE_MODULE_METADATA = 0;
    public static final int ELECTRIC_FURNACE_METADATA = 4;
    public static IMachineSidesProperties MACHINESIDES_RENDERTYPE = IMachineSidesProperties.TWOFACES_HORIZ;
    
    public static final PropertyDirection FACING = PropertyDirection.create("facing", EnumFacing.Plane.HORIZONTAL);
    public static final PropertyEnum TYPE = PropertyEnum.create("type", EnumTieredMachineType.class);
    public static final PropertyInteger FILL_VALUE = PropertyInteger.create("fill_value", 0, 33);
    public static final PropertyEnum SIDES = MACHINESIDES_RENDERTYPE.asProperty;

    public enum EnumTieredMachineType implements IStringSerializable
    {
        STORAGE_MODULE(0, "energy_storage"),
        ELECTRIC_FURNACE(1, "electric_furnace"),
        STORAGE_CLUSTER(2, "cluster_storage"),
        ARC_FURNACE(3, "arc_furnace");

        private final int meta;
        private final String name;

        EnumTieredMachineType(int meta, String name)
        {
            this.meta = meta;
            this.name = name;
        }

        public int getMeta()
        {
            return this.meta;
        }

        public static EnumTieredMachineType byMetadata(int meta)
        {
            return values()[meta];
        }

        @Override
        public String getName()
        {
            return this.name;
        }
    }

    public BlockMachineTiered(String assetName)
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
    public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack)
    {
        int metadata = getMetaFromState(state);

        final int angle = MathHelper.floor(placer.rotationYaw * 4.0F / 360.0F + 0.5D) & 3;
        int change = EnumFacing.getHorizontal(angle).getOpposite().getHorizontalIndex();

        if (metadata >= BlockMachineTiered.ELECTRIC_FURNACE_METADATA + 8)
        {
            worldIn.setBlockState(pos, getStateFromMeta(BlockMachineTiered.ELECTRIC_FURNACE_METADATA + 8 + change), 3);
        }
        else if (metadata >= BlockMachineTiered.STORAGE_MODULE_METADATA + 8)
        {
            worldIn.setBlockState(pos, getStateFromMeta(BlockMachineTiered.STORAGE_MODULE_METADATA + 8 + change), 3);
        }
        else if (metadata >= BlockMachineTiered.ELECTRIC_FURNACE_METADATA)
        {
            worldIn.setBlockState(pos, getStateFromMeta(BlockMachineTiered.ELECTRIC_FURNACE_METADATA + change), 3);
        }
        else if (metadata >= BlockMachineTiered.STORAGE_MODULE_METADATA)
        {
            worldIn.setBlockState(pos, getStateFromMeta(BlockMachineTiered.STORAGE_MODULE_METADATA + change), 3);
        }
    }

    @Override
    public boolean onUseWrench(World world, BlockPos pos, EntityPlayer entityPlayer, EnumHand hand, ItemStack heldItem, EnumFacing side, float hitX, float hitY, float hitZ)
    {
        IBlockState state = world.getBlockState(pos);
        TileBaseUniversalElectrical.onUseWrenchBlock(state, world, pos, state.getValue(FACING));
        return true;
    }

    /**
     * Called when the block is right clicked by the player
     */
    @Override
    public boolean onMachineActivated(World world, BlockPos pos, IBlockState state, EntityPlayer entityPlayer, EnumHand hand, ItemStack heldItem, EnumFacing side, float hitX, float hitY, float hitZ)
    {
        if (!world.isRemote)
        {
            entityPlayer.openGui(GalacticraftCore.instance, -1, world, pos.getX(), pos.getY(), pos.getZ());
        }

        return true;
    }

    @Override
    public TileEntity createNewTileEntity(World world, int metadata)
    {
        int tier = metadata / 8 + 1;

        TileEntity tile;
        if ((metadata & 4) == BlockMachineTiered.ELECTRIC_FURNACE_METADATA)
        {
            tile = new TileEntityElectricFurnace(tier);
        }
        else
        {
            tile = new TileEntityEnergyStorageModule(tier);
        }
        
        tile.setWorld(world);
        return tile;
    }

    public ItemStack getEnergyStorageModule()
    {
        return new ItemStack(this, 1, BlockMachineTiered.STORAGE_MODULE_METADATA);
    }

    public ItemStack getEnergyStorageCluster()
    {
        return new ItemStack(this, 1, 8 + BlockMachineTiered.STORAGE_MODULE_METADATA);
    }

    public ItemStack getElectricFurnace()
    {
        return new ItemStack(this, 1, BlockMachineTiered.ELECTRIC_FURNACE_METADATA);
    }

    public ItemStack getElectricArcFurnace()
    {
        return new ItemStack(this, 1, 8 + BlockMachineTiered.ELECTRIC_FURNACE_METADATA);
    }

    @Override
    public void getSubBlocks(CreativeTabs tab, NonNullList<ItemStack> list)
    {
        list.add(this.getEnergyStorageModule());
        list.add(this.getElectricFurnace());
        if (GalacticraftCore.isPlanetsLoaded)
        {
            list.add(this.getEnergyStorageCluster());
            list.add(this.getElectricArcFurnace());
        }
    }

    @Override
    public int damageDropped(IBlockState state)
    {
        return getMetaFromState(state) & 12;
    }

    @Override
    public String getShiftDescription(int meta)
    {
        int tier = (meta >= 8 ? 2 : 1);
        switch (meta & 4)
        {
        case ELECTRIC_FURNACE_METADATA:
            return GCCoreUtil.translate("tile.electric_furnace_tier" + tier + ".description");
        case STORAGE_MODULE_METADATA:
            return GCCoreUtil.translate("tile.energy_storage_module_tier" + tier + ".description");
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
        EnumTieredMachineType type = EnumTieredMachineType.byMetadata((int) Math.floor(meta / 4.0));
        return this.getDefaultState().withProperty(FACING, enumfacing).withProperty(TYPE, type);
    }

    @Override
    public int getMetaFromState(IBlockState state)
    {
        return ((EnumFacing) state.getValue(FACING)).getHorizontalIndex() + ((EnumTieredMachineType) state.getValue(TYPE)).getMeta() * 4;
    }

    @Override
    protected BlockStateContainer createBlockState()
    {
        return new BlockStateContainer(this, FACING, TYPE, FILL_VALUE, SIDES);
    }

    @Override
    public IBlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos)
    {
        TileEntity tile = worldIn.getTileEntity(pos);
        state = IMachineSides.addPropertyForTile(state, tile, MACHINESIDES_RENDERTYPE, SIDES);

        if (!(tile instanceof TileEntityEnergyStorageModule))
        {
            return state.withProperty(FILL_VALUE, 0);
        }
        int energyLevel = ((TileEntityEnergyStorageModule) tile).scaledEnergyLevel;
        if (state.getValue(TYPE) == EnumTieredMachineType.STORAGE_CLUSTER) energyLevel += 17;
        return state.withProperty(FILL_VALUE, energyLevel);
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
