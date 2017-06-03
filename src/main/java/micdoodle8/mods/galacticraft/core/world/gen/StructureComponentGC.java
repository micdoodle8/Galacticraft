package micdoodle8.mods.galacticraft.core.world.gen;

import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntityMobSpawner;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.StructureBoundingBox;
import net.minecraft.world.gen.structure.StructureComponent;

import java.util.Random;

public abstract class StructureComponentGC extends StructureComponent
{
    public StructureComponentGC(int var1)
    {
        super(var1);
    }

    public static StructureBoundingBox getComponentToAddBoundingBox(int x, int y, int z, int lengthOffset, int heightOffset, int widthOffset, int length, int height, int width, EnumFacing coordBaseMode)
    {
        if (coordBaseMode != null)
        {
            switch (SwitchEnumFacing.field_176064_a[coordBaseMode.ordinal()])
            {
            case 0:
                return new StructureBoundingBox(x + lengthOffset, y + heightOffset, z + widthOffset, x + length + lengthOffset, y + height + heightOffset, z + width + widthOffset);
            case 1:
                return new StructureBoundingBox(x - width + widthOffset, y + heightOffset, z + lengthOffset, x + widthOffset, y + height + heightOffset, z + length + lengthOffset);
            case 2:
                return new StructureBoundingBox(x - length - lengthOffset, y + heightOffset, z - width - widthOffset, x - lengthOffset, y + height + heightOffset, z - widthOffset);
            case 3:
                return new StructureBoundingBox(x + widthOffset, y + heightOffset, z - length, x + width + widthOffset, y + height + heightOffset, z + lengthOffset);
            }
        }
        return new StructureBoundingBox(x + lengthOffset, y + heightOffset, z + widthOffset, x + length + lengthOffset, y + height + heightOffset, z + width + widthOffset);
    }

    protected void placeSpawnerAtCurrentPosition(World var1, Random var2, int var3, int var4, int var5, ResourceLocation var6, StructureBoundingBox var7)
    {
        final int var8 = this.getXWithOffset(var3, var5);
        final int var9 = this.getYWithOffset(var4);
        final int var10 = this.getZWithOffset(var3, var5);

        BlockPos pos = new BlockPos(var8, var9, var10);
        if (var7.isVecInside(pos) && var1.getBlockState(pos).getBlock() != Blocks.MOB_SPAWNER)
        {
            var1.setBlockState(pos, Blocks.MOB_SPAWNER.getDefaultState(), 2);
            final TileEntityMobSpawner var11 = (TileEntityMobSpawner) var1.getTileEntity(pos);

            if (var11 != null)
            {
                var11.getSpawnerBaseLogic().setEntityId(var6);
            }
        }
    }

    protected int[] offsetTowerCoords(int var1, int var2, int var3, int var4, int var5)
    {
        final int var6 = this.getXWithOffset(var1, var3);
        final int var7 = this.getYWithOffset(var2);
        final int var8 = this.getZWithOffset(var1, var3);
        return var5 == 0 ? new int[] { var6 + 1, var7 - 1, var8 - var4 / 2 } : var5 == 1 ? new int[] { var6 + var4 / 2, var7 - 1, var8 + 1 } : var5 == 2 ? new int[] { var6 - 1, var7 - 1, var8 + var4 / 2 } : var5 == 3 ? new int[] { var6 - var4 / 2, var7 - 1, var8 - 1 } : new int[] { var1, var2, var3 };
    }

    public int[] getOffsetAsIfRotated(int[] var1, EnumFacing var2)
    {
        final EnumFacing var3 = getCoordBaseMode();
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
        protected static int[] field_176064_a = new int[EnumFacing.VALUES.length];

        static
        {
            try
            {
                field_176064_a[EnumFacing.NORTH.ordinal()] = 1;
            }
            catch (NoSuchFieldError var4)
            {
            }

            try
            {
                field_176064_a[EnumFacing.SOUTH.ordinal()] = 2;
            }
            catch (NoSuchFieldError var3)
            {
            }

            try
            {
                field_176064_a[EnumFacing.WEST.ordinal()] = 3;
            }
            catch (NoSuchFieldError var2)
            {
            }

            try
            {
                field_176064_a[EnumFacing.EAST.ordinal()] = 4;
            }
            catch (NoSuchFieldError var1)
            {
            }
        }
    }
}
