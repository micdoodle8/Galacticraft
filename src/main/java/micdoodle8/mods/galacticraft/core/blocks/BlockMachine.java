package micdoodle8.mods.galacticraft.core.blocks;

import micdoodle8.mods.galacticraft.core.tile.TileEntityCoalGenerator;
import micdoodle8.mods.galacticraft.core.tile.TileEntityIngotCompressor;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Random;

public class BlockMachine extends BlockMachineBase
{
    public static final PropertyEnum<EnumMachineType> TYPE = PropertyEnum.create("type", EnumMachineType.class);

    public enum EnumMachineType implements EnumMachineBase, IStringSerializable
    {
        COAL_GENERATOR(0, "coal_generator", TileEntityCoalGenerator::new, "tile.coal_generator.description", "tile.machine.0"),
        COMPRESSOR(12, "ingot_compressor", TileEntityIngotCompressor::new, "tile.compressor.description", "tile.machine.3"); // 3 for backwards compatibility

        private final int meta;
        private final String name;
        private final TileConstructor tile;
        private final String shiftDescriptionKey;
        private final String blockName;

        EnumMachineType(int meta, String name, TileConstructor tile, String key, String blockName)
        {
            this.meta = meta;
            this.name = name;
            this.tile = tile;
            this.shiftDescriptionKey = key;
            this.blockName = blockName;
        }

        @Override
        public int getMetadata()
        {
            return this.meta;
        }

        private final static EnumMachineType[] values = values();
        @Override
        public EnumMachineType fromMetadata(int meta)
        {
            switch (meta / 4)
            {
            case 3:
                return COMPRESSOR;
            default:
                return COAL_GENERATOR;
            }
        }
        
        @Override
        public String getName()
        {
            return this.name;
        }
        
        @Override
        public TileEntity tileConstructor()
        {
            return this.tile.create();
        }

        @FunctionalInterface
        private static interface TileConstructor
        {
              TileEntity create();
        }

        @Override
        public String getShiftDescriptionKey()
        {
            return this.shiftDescriptionKey;
        }

        @Override
        public String getUnlocalizedName()
        {
            return this.blockName;
        }
    }

    public BlockMachine(String assetName)
    {
        super(assetName);
    }

    @Override
    protected void initialiseTypes()
    {
        this.types = EnumMachineType.values;
        this.typeBase = EnumMachineType.values[0];
    }

    @SideOnly(Side.CLIENT)
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

    @Override
    public IBlockState getStateFromMeta(int meta)
    {
        EnumFacing enumfacing = EnumFacing.getHorizontal(meta % 4);
        EnumMachineType type = (EnumMachineType) typeBase.fromMetadata(meta);
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
}
