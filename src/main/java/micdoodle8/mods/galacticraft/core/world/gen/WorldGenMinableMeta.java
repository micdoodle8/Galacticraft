package micdoodle8.mods.galacticraft.core.world.gen;

import net.minecraft.block.Block;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenMinable;

import java.util.Random;

public class WorldGenMinableMeta extends WorldGenMinable
{
    private final Block minableBlockId;

    private final int numberOfBlocks;

    private final int metadata;

    private boolean usingMetadata = false;

    private final Block fillerID;

    private final int fillerMetadata;

    public WorldGenMinableMeta(Block par1, int par2, int par3, boolean par4, Block id, int meta)
    {
        super(par1, par3, par2, id);
        this.minableBlockId = par1;
        this.numberOfBlocks = par2;
        this.metadata = par3;
        this.usingMetadata = par4;
        this.fillerID = id;
        this.fillerMetadata = meta;
    }

    @Override
    public boolean generate(World par1World, Random par2Random, int px, int py, int pz)
    {
        final float f = par2Random.nextFloat() * (float) Math.PI;
        final float sinf = MathHelper.sin(f) * this.numberOfBlocks / 8.0F;
        final float cosf = MathHelper.cos(f) * this.numberOfBlocks / 8.0F;
        final float x1 = px + 8 + sinf;
        final float x2 = -2F * sinf;
        final float z1 = pz + 8 + cosf;
        final float z2 = -2F * cosf;
        final float y1 = py + par2Random.nextInt(3) - 2;
        final float y2 = py + par2Random.nextInt(3) - 2 - y1;

        for (int l = 0; l <= this.numberOfBlocks; ++l)
        {
            final float progress = (float) l / this.numberOfBlocks;
            final float cx = x1 + x2 * progress;
            final float cy = y1 + y2 * progress;
            final float cz = z1 + z2 * progress;
            final float size = ((MathHelper.sin((float) Math.PI * progress) + 1.0F) * par2Random.nextFloat() * this.numberOfBlocks / 16.0F + 1.0F) / 2.0F;
            final int xMin = MathHelper.floor_float(cx - size);
            final int yMin = MathHelper.floor_float(cy - size);
            final int zMin = MathHelper.floor_float(cz - size);
            final int xMax = MathHelper.floor_float(cx + size);
            final int yMax = MathHelper.floor_float(cy + size);
            final int zMax = MathHelper.floor_float(cz + size);

            for (int ix = xMin; ix <= xMax; ++ix)
            {
                float dx = (ix + 0.5F - cx) / size;
                dx *= dx;

                if (dx < 1.0F)
                {
                    for (int iy = yMin; iy <= yMax; ++iy)
                    {
                        float dy = (iy + 0.5F - cy) / size;
                        dy *= dy;

                        if (dx + dy < 1.0F)
                        {
                            for (int iz = zMin; iz <= zMax; ++iz)
                            {
                                float dz = (iz + 0.5F - cz) / size;
                                dz *= dz;

                                if (dx + dy + dz < 1.0F && par1World.getBlock(ix, iy, iz) == this.fillerID && par1World.getBlockMetadata(ix, iy, iz) == this.fillerMetadata)
                                {
                                    if (!this.usingMetadata)
                                    {
                                        par1World.setBlock(ix, iy, iz, this.minableBlockId, 0, 3);
                                    }
                                    else
                                    {
                                        par1World.setBlock(ix, iy, iz, this.minableBlockId, this.metadata, 3);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        return true;
    }
}
