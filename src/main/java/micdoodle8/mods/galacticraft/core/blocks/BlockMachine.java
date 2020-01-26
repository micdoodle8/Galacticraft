package micdoodle8.mods.galacticraft.core.blocks;

import micdoodle8.mods.galacticraft.core.GCBlocks;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.tile.TileEntityCoalGenerator;
import micdoodle8.mods.galacticraft.core.tile.TileEntityElectricFurnace;
import micdoodle8.mods.galacticraft.core.tile.TileEntityEnergyStorageModule;
import micdoodle8.mods.galacticraft.core.tile.TileEntityIngotCompressor;
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
import net.minecraft.world.World;

import java.util.Random;

public class BlockMachine extends BlockMachineBase
{
    public static final PropertyEnum<EnumMachineType> TYPE = PropertyEnum.create("type", EnumMachineType.class);

    public enum EnumMachineType implements IStringSerializable
    {
        COAL_GENERATOR(0, "coal_generator", TileEntityCoalGenerator.class, "tile.coal_generator.description", "tile.machine.0"),
        COMPRESSOR(3, "ingot_compressor", TileEntityIngotCompressor.class, "tile.compressor.description", "tile.machine.3"); // 3 for backwards compatibility

        private final int meta;
        private final String name;
        private final Class tile;
        private final String shiftDescriptionKey;
        private final String blockName;

        EnumMachineType(int meta, String name, Class tile, String key, String blockName)
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

        private final static EnumMachineType[] values = values();
        public static EnumMachineType byMeta(int meta)
        {
            switch (meta)
            {
            case 3:
                return COMPRESSOR;
            default:
                return COAL_GENERATOR;
            }
        }
        
        public static EnumMachineType getByMetadata(int meta)
        {
            return byMeta(meta / 4);
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

    public BlockMachine(String assetName)
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
    public void randomDisplayTick(IBlockState stateIn, World worldIn, BlockPos pos, Random rand)
    {
        TileEntity tile = worldIn.getTileEntity(pos);

        if (tile instanceof TileEntityCoalGenerator)
        {
            TileEntityCoalGenerator tileEntity = (TileEntityCoalGenerator) tile;
            if (tileEntity.heatGJperTick > 0)
            {
                float particlePosX = pos.getX() + 0.5F;
                float particlePosY = pos.getY() + 0.0F + rand.nextFloat() * 6.0F / 16.0F;
                float particlePosZ = pos.getZ() + 0.5F;
                float particleSize0 = 0.52F;
                float particleSize1 = rand.nextFloat() * 0.6F - 0.3F;

                switch (stateIn.getValue(FACING))
                {
                case NORTH:
                    worldIn.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, particlePosX + particleSize1, particlePosY, particlePosZ - particleSize0, 0.0D, 0.0D, 0.0D);
                    worldIn.spawnParticle(EnumParticleTypes.FLAME, particlePosX + particleSize1, particlePosY, particlePosZ - particleSize0, 0.0D, 0.0D, 0.0D);
                    break;
                case EAST:
                    worldIn.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, particlePosX + particleSize0, particlePosY, particlePosZ + particleSize1, 0.0D, 0.0D, 0.0D);
                    worldIn.spawnParticle(EnumParticleTypes.FLAME, particlePosX + particleSize0, particlePosY, particlePosZ + particleSize1, 0.0D, 0.0D, 0.0D);
                    break;
                case SOUTH:
                    worldIn.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, particlePosX + particleSize1, particlePosY, particlePosZ + particleSize0, 0.0D, 0.0D, 0.0D);
                    worldIn.spawnParticle(EnumParticleTypes.FLAME, particlePosX + particleSize1, particlePosY, particlePosZ + particleSize0, 0.0D, 0.0D, 0.0D);
                    break;
                case WEST:
                    worldIn.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, particlePosX - particleSize0, particlePosY, particlePosZ + particleSize1, 0.0D, 0.0D, 0.0D);
                    worldIn.spawnParticle(EnumParticleTypes.FLAME, particlePosX - particleSize0, particlePosY, particlePosZ + particleSize1, 0.0D, 0.0D, 0.0D);
                    break;
                }
            }
        }
    }

    /**
     * Called when the block is placed in the world.
     */
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
        int metadata = getMetaFromState(world.getBlockState(pos));
        int change = world.getBlockState(pos).getValue(FACING).rotateY().getHorizontalIndex();

//(Currently neither of these machines is electric, so the following is unnecessary)
//        if (metadata < BlockMachine.COMPRESSOR_METADATA)
//        {
//            TileEntity te = world.getTileEntity(pos);
//            if (te instanceof TileBaseUniversalElectrical)
//            {
//                ((TileBaseUniversalElectrical) te).updateFacing();
//            }
//        }

        world.setBlockState(pos, getStateFromMeta((metadata & BlockMachineBase.METADATA_MASK) + change), 3);
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
            return true;
        }

        return true;
    }

    @Override
    public TileEntity createTileEntity(World world, IBlockState state)
    {
        int meta = getMetaFromState(state) & BlockMachineBase.METADATA_MASK;
        if (meta == 4)
        {
            return new TileEntityEnergyStorageModule();   //Legacy code in case a block in game not yet converted to BlockMachineTiered
        }
        else if (meta == 8)
        {
            return new TileEntityElectricFurnace();   //Legacy code in case a block in game not yet converted to BlockMachineTiered
        }
        EnumMachineType type = EnumMachineType.getByMetadata(meta);
        return type.tileConstructor();
    }

    @Override
    public void getSubBlocks(CreativeTabs tab, NonNullList<ItemStack> list)
    {
        for (EnumMachineType type : EnumMachineType.values)
            list.add(new ItemStack(this, 1, type.getMetadata()));
    }

    @Override
    public int damageDropped(IBlockState state)
    {
        return getMetaFromState(state) & BlockMachineBase.METADATA_MASK;
    }

    @Override
    public String getShiftDescription(int meta)
    {
        EnumMachineType type = EnumMachineType.getByMetadata(meta);
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
        EnumMachineType type = EnumMachineType.getByMetadata(meta);
        return type.getUnlocalizedName();
    }

    @Override
    public IBlockState getStateFromMeta(int meta)
    {
        EnumFacing enumfacing = EnumFacing.getHorizontal(meta % 4);
        EnumMachineType type = EnumMachineType.getByMetadata(meta);
        return this.getDefaultState().withProperty(FACING, enumfacing).withProperty(TYPE, type);
    }

    @Override
    public int getMetaFromState(IBlockState state)
    {
        return (state.getValue(FACING)).getHorizontalIndex() + ((EnumMachineType) state.getValue(TYPE)).getMetadata();
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
