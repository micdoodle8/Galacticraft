package micdoodle8.mods.galacticraft.core.world.gen;

import net.minecraft.block.Blocks;
import net.minecraft.entity.EntityType;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.MobSpawnerTileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.structure.IStructurePieceType;
import net.minecraft.world.gen.feature.structure.StructurePiece;

import java.util.Random;

public abstract class StructureComponentGC extends StructurePiece
{
    protected StructureComponentGC(IStructurePieceType type, int componentType)
    {
        super(type, componentType);
    }

    public StructureComponentGC(IStructurePieceType type, CompoundNBT nbt)
    {
        super(type, nbt);
    }

    public static MutableBoundingBox getComponentToAddBoundingBox(int x, int y, int z, int lengthOffset, int heightOffset, int widthOffset, int length, int height, int width, Direction coordBaseMode)
    {
        if (coordBaseMode != null)
        {
            switch (SwitchEnumFacing.field_176064_a[coordBaseMode.ordinal()])
            {
            case 0:
                return new MutableBoundingBox(x + lengthOffset, y + heightOffset, z + widthOffset, x + length + lengthOffset, y + height + heightOffset, z + width + widthOffset);
            case 1:
                return new MutableBoundingBox(x - width + widthOffset, y + heightOffset, z + lengthOffset, x + widthOffset, y + height + heightOffset, z + length + lengthOffset);
            case 2:
                return new MutableBoundingBox(x - length - lengthOffset, y + heightOffset, z - width - widthOffset, x - lengthOffset, y + height + heightOffset, z - widthOffset);
            case 3:
                return new MutableBoundingBox(x + widthOffset, y + heightOffset, z - length, x + width + widthOffset, y + height + heightOffset, z + lengthOffset);
            }
        }
        return new MutableBoundingBox(x + lengthOffset, y + heightOffset, z + widthOffset, x + length + lengthOffset, y + height + heightOffset, z + width + widthOffset);
    }

    protected void placeSpawnerAtCurrentPosition(World var1, Random var2, int var3, int var4, int var5, EntityType<?> var6, MutableBoundingBox var7)
    {
        final int var8 = this.getXWithOffset(var3, var5);
        final int var9 = this.getYWithOffset(var4);
        final int var10 = this.getZWithOffset(var3, var5);

        BlockPos pos = new BlockPos(var8, var9, var10);
        if (var7.isVecInside(pos) && var1.getBlockState(pos).getBlock() != Blocks.SPAWNER)
        {
            var1.setBlockState(pos, Blocks.SPAWNER.getDefaultState(), 2);
            final MobSpawnerTileEntity var11 = (MobSpawnerTileEntity) var1.getTileEntity(pos);

            if (var11 != null)
            {
                var11.getSpawnerBaseLogic().setEntityType(var6);
            }
        }
    }

    protected int[] offsetTowerCoords(int var1, int var2, int var3, int var4, int var5)
    {
        final int var6 = this.getXWithOffset(var1, var3);
        final int var7 = this.getYWithOffset(var2);
        final int var8 = this.getZWithOffset(var1, var3);
        return var5 == 0 ? new int[]{var6 + 1, var7 - 1, var8 - var4 / 2} : var5 == 1 ? new int[]{var6 + var4 / 2, var7 - 1, var8 + 1} : var5 == 2 ? new int[]{var6 - 1, var7 - 1, var8 + var4 / 2} : var5 == 3 ? new int[]{var6 - var4 / 2, var7 - 1, var8 - 1} : new int[]{var1, var2, var3};
    }

    public int[] getOffsetAsIfRotated(int[] var1, Direction var2)
    {
        final Direction var3 = getCoordBaseMode();
        final int[] var4 = new int[3];
        this.setCoordBaseMode(var2);
        var4[0] = this.getXWithOffset(var1[0], var1[2]);
        var4[1] = this.getYWithOffset(var1[1]);
        var4[2] = this.getZWithOffset(var1[0], var1[2]);
        this.setCoordBaseMode(var3);
        return var4;
    }

    @Override
    protected int getXWithOffset(int var1, int var2)
    {
        switch (getCoordBaseMode().getHorizontalIndex())
        {
        case 0:
            return this.boundingBox.minX + var1;

        case 1:
            return this.boundingBox.maxX - var2;

        case 2:
            return this.boundingBox.maxX - var1;

        case 3:
            return this.boundingBox.minX + var2;

        default:
            return var1;
        }
    }

    @Override
    protected int getZWithOffset(int var1, int var2)
    {
        switch (getCoordBaseMode().getHorizontalIndex())
        {
        case 0:
            return this.boundingBox.minZ + var2;

        case 1:
            return this.boundingBox.minZ + var1;

        case 2:
            return this.boundingBox.maxZ - var2;

        case 3:
            return this.boundingBox.maxZ - var1;

        default:
            return var2;
        }
    }

    @Override
    protected int getYWithOffset(int var1)
    {
        return super.getYWithOffset(var1);
    }

    protected static class SwitchEnumFacing
    {
        protected static int[] field_176064_a = new int[Direction.values().length];

        static
        {
            try
            {
                field_176064_a[Direction.NORTH.ordinal()] = 1;
            }
            catch (NoSuchFieldError var4)
            {
            }

            try
            {
                field_176064_a[Direction.SOUTH.ordinal()] = 2;
            }
            catch (NoSuchFieldError var3)
            {
            }

            try
            {
                field_176064_a[Direction.WEST.ordinal()] = 3;
            }
            catch (NoSuchFieldError var2)
            {
            }

            try
            {
                field_176064_a[Direction.EAST.ordinal()] = 4;
            }
            catch (NoSuchFieldError var1)
            {
            }
        }
    }
}
