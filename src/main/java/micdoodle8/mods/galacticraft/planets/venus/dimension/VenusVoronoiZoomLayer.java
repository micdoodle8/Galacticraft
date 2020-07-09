package micdoodle8.mods.galacticraft.planets.venus.dimension;

import net.minecraft.world.gen.IExtendedNoiseRandom;
import net.minecraft.world.gen.area.IArea;
import net.minecraft.world.gen.layer.traits.IAreaTransformer1;

public enum VenusVoronoiZoomLayer implements IAreaTransformer1
{
    INSTANCE;

    private static final int ZOOM_BITS = 2;

    @Override
    public int func_215728_a(IExtendedNoiseRandom<?> random, IArea area, int x, int y)
    {
        int absx = x - 2;
        int absy = y - 2;
        int shiftedAbsX = absx >> 2;
        int shiftedAbsY = absy >> 2;
        int backX = shiftedAbsX << 2;
        int backY = shiftedAbsY << 2;
        random.setPosition(backX, backY);
        double offsetX = ((double) random.random(1024) / 1024.0D - 0.5D) * 3.6D;
        double offsetY = ((double) random.random(1024) / 1024.0D - 0.5D) * 3.6D;
        random.setPosition(backX + 4, backY);
        double offsetYY = ((double) random.random(1024) / 1024.0D - 0.5D) * 3.6D + 4.0D;
        double offsetXY = ((double) random.random(1024) / 1024.0D - 0.5D) * 3.6D;
        random.setPosition(backX, backY + 4);
        double offsetYX = ((double) random.random(1024) / 1024.0D - 0.5D) * 3.6D;
        double offsetXX = ((double) random.random(1024) / 1024.0D - 0.5D) * 3.6D + 4.0D;
        random.setPosition(backX + 4, backY + 4);
        double offsetYXY = ((double) random.random(1024) / 1024.0D - 0.5D) * 3.6D + 4.0D;
        double offsetXXY = ((double) random.random(1024) / 1024.0D - 0.5D) * 3.6D + 4.0D;
        int xx = absx & 3;
        int yy = absy & 3;
        // TODO performance improve -> MathHelper
        double a0 = Math.abs(yy - offsetY) + Math.abs(xx - offsetX);
        //double a0 = ((double)yy - offsetY) * ((double)yy - offsetY) + ((double)xx - offsetX) * ((double)xx - offsetX);
        double a1 = Math.abs(yy - offsetXY) + Math.abs(xx - offsetYY);
        //double a1 = ((double)yy - offsetXY) * ((double)yy - offsetXY) + ((double)xx - offsetYY) * ((double)xx - offsetYY);
        double a2 = Math.abs(yy - offsetXX) + Math.abs(xx - offsetYX);
        //double a2 = ((double)yy - offsetXX) * ((double)yy - offsetXX) + ((double)xx - offsetYX) * ((double)xx - offsetYX);
        double a3 = Math.abs(yy - offsetXXY) + Math.abs(xx - offsetYXY);
        //double a3 = ((double)yy - offsetXXY) * ((double)yy - offsetXXY) + ((double)xx - offsetYXY) * ((double)xx - offsetYXY);
        if (a0 < a1 && a0 < a2 && a0 < a3)
        {
            return area.getValue(this.func_215721_a(backX), this.func_215722_b(backY));
        }
        else if (a1 < a0 && a1 < a2 && a1 < a3)
        {
            return area.getValue(this.func_215721_a(backX + 4), this.func_215722_b(backY)) & 255;
        }
        else
        {
            return a2 < a0 && a2 < a1 && a2 < a3 ? area.getValue(this.func_215721_a(backX), this.func_215722_b(backY + 4)) : area.getValue(this.func_215721_a(backX + 4), this.func_215722_b(backY + 4)) & 255;
        }
    }

    @Override
    public int func_215721_a(int x)
    {
        return x >> ZOOM_BITS;
    }

    @Override
    public int func_215722_b(int z)
    {
        return z >> ZOOM_BITS;
    }
}