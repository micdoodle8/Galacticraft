package micdoodle8.mods.galacticraft.mars.wgen;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import micdoodle8.mods.galacticraft.core.entities.GCCoreEntityCreeper;
import micdoodle8.mods.galacticraft.core.entities.GCCoreEntitySkeleton;
import micdoodle8.mods.galacticraft.core.entities.GCCoreEntitySpider;
import micdoodle8.mods.galacticraft.core.entities.GCCoreEntityZombie;
import micdoodle8.mods.galacticraft.core.perlin.generator.Gradient;
import micdoodle8.mods.galacticraft.core.wgen.GCCoreCraterSize;
import micdoodle8.mods.galacticraft.mars.blocks.GCMarsBlocks;
import net.minecraft.block.Block;
import net.minecraft.block.BlockSand;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.util.IProgressUpdate;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.biome.SpawnListEntry;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.ChunkProviderGenerate;
import net.minecraft.world.gen.structure.MapGenMineshaft;
import net.minecraft.world.gen.structure.MapGenVillage;

/**
 * Copyright 2012-2013, micdoodle8
 * 
 * All rights reserved.
 * 
 */
public class GCMarsChunkProvider extends ChunkProviderGenerate
{
    final short topBlockID = (short) GCMarsBlocks.marsBlock.blockID;
    final byte topBlockMeta = 5;
    final short fillBlockID = (short) GCMarsBlocks.marsBlock.blockID;
    final byte fillBlockMeta = 6;
    final short lowerBlockID = (short) GCMarsBlocks.marsBlock.blockID;
    final byte lowerBlockMeta = 3;

    private final Random rand;

    private final Gradient noiseGen1;
    private final Gradient noiseGen2;
    private final Gradient noiseGen3;
    private final Gradient noiseGen4;
    private final Gradient noiseGen5;
    private final Gradient noiseGen6;
    private final Gradient noiseGen7;
    public GCMarsBiomeDecorator biomedecoratorplanet = new GCMarsBiomeDecorator(GCMarsBiomeGenBase.marsFlat);

    private final World worldObj;

    private final GCMarsCavern caveGenerator = new GCMarsCavern();
    private final GCMarsCaveGen caveGenerator2 = new GCMarsCaveGen();

    private final MapGenVillage villageGenerator = new MapGenVillage();

    private final MapGenMineshaft mineshaftGenerator = new MapGenMineshaft();

    private BiomeGenBase[] biomesForGeneration =
    { GCMarsBiomeGenBase.marsFlat };

    double[] noise1;
    double[] noise2;
    double[] noise3;
    double[] noise4;
    double[] noise5;
    double[] noise6;
    float[] field_35388_l;
    int[][] field_914_i = new int[32][32];

    private static final double TERRAIN_HEIGHT_MOD = 12;
    private static final double SMALL_FEATURE_HEIGHT_MOD = 26;
    private static final double MOUNTAIN_HEIGHT_MOD = 95;
    private static final double VALLEY_HEIGHT_MOD = 50;
    private static final int CRATER_PROB = 2000;

    // DO NOT CHANGE
    private static final int MID_HEIGHT = 63;
    private static final int CHUNK_SIZE_X = 16;
    private static final int CHUNK_SIZE_Y = 128;
    private static final int CHUNK_SIZE_Z = 16;
    private static final double MAIN_FEATURE_FILTER_MOD = 4;
    private static final double LARGE_FEATURE_FILTER_MOD = 8;
    private static final double SMALL_FEATURE_FILTER_MOD = 8;

    public GCMarsChunkProvider(World par1World, long par2, boolean par4)
    {
        super(par1World, par2, par4);
        this.worldObj = par1World;
        this.rand = new Random(par2);

        this.noiseGen1 = new Gradient(this.rand.nextLong(), 4, 0.25);
        this.noiseGen2 = new Gradient(this.rand.nextLong(), 4, 0.25);
        this.noiseGen3 = new Gradient(this.rand.nextLong(), 4, 0.25);
        this.noiseGen4 = new Gradient(this.rand.nextLong(), 2, 0.25);
        this.noiseGen5 = new Gradient(this.rand.nextLong(), 1, 0.25);
        this.noiseGen6 = new Gradient(this.rand.nextLong(), 1, 0.25);
        this.noiseGen7 = new Gradient(this.rand.nextLong(), 1, 0.25);
    }

    public void generateTerrain(int chunkX, int chunkZ, short[] idArray, byte[] metaArray)
    {
        this.noiseGen1.frequency = 0.015;
        this.noiseGen2.frequency = 0.01;
        this.noiseGen3.frequency = 0.01;
        this.noiseGen4.frequency = 0.01;
        this.noiseGen5.frequency = 0.01;
        this.noiseGen6.frequency = 0.001;
        this.noiseGen7.frequency = 0.005;

        for (int x = 0; x < GCMarsChunkProvider.CHUNK_SIZE_X; x++)
        {
            for (int z = 0; z < GCMarsChunkProvider.CHUNK_SIZE_Z; z++)
            {
                final double baseHeight = this.noiseGen1.getNoise(chunkX * 16 + x, chunkZ * 16 + z) * GCMarsChunkProvider.TERRAIN_HEIGHT_MOD;
                final double smallHillHeight = this.noiseGen2.getNoise(chunkX * 16 + x, chunkZ * 16 + z) * GCMarsChunkProvider.SMALL_FEATURE_HEIGHT_MOD;
                double mountainHeight = Math.abs(this.noiseGen3.getNoise(chunkX * 16 + x, chunkZ * 16 + z));
                double valleyHeight = Math.abs(this.noiseGen4.getNoise(chunkX * 16 + x, chunkZ * 16 + z));
                final double featureFilter = this.noiseGen5.getNoise(chunkX * 16 + x, chunkZ * 16 + z) * GCMarsChunkProvider.MAIN_FEATURE_FILTER_MOD;
                final double largeFilter = this.noiseGen6.getNoise(chunkX * 16 + x, chunkZ * 16 + z) * GCMarsChunkProvider.LARGE_FEATURE_FILTER_MOD;
                final double smallFilter = this.noiseGen7.getNoise(chunkX * 16 + x, chunkZ * 16 + z) * GCMarsChunkProvider.SMALL_FEATURE_FILTER_MOD - 0.5;
                /*
                 * if(largeFilter < 0.0) { featureDev = valleyHeight; } else
                 * if(largeFilter > 1.0) { featureDev = mountainHeight; } else {
                 * featureDev = valleyHeight + (mountainHeight - valleyHeight) *
                 * (largeFilter); }
                 * 
                 * if(smallFilter < 0.0) { featureDev = smallHillHeight; } else
                 * if(smallFilter < 1.0) { featureDev = smallHillHeight +
                 * (featureDev - smallHillHeight) * (smallFilter); }
                 */

                /*
                 * if(featureFilter < 0.0) { yDev = baseHeight; } else
                 * if(featureFilter > 1.0) { yDev = featureDev; } else { yDev =
                 * baseHeight + (featureDev - baseHeight) * (featureFilter); }
                 */
                mountainHeight = this.lerp(smallHillHeight, mountainHeight * GCMarsChunkProvider.MOUNTAIN_HEIGHT_MOD, this.fade(this.clamp(mountainHeight * 2, 0, 1)));
                valleyHeight = this.lerp(smallHillHeight, valleyHeight * GCMarsChunkProvider.VALLEY_HEIGHT_MOD - GCMarsChunkProvider.VALLEY_HEIGHT_MOD + 9, this.fade(this.clamp((valleyHeight + 2) * 4, 0, 1)));

                double yDev = this.lerp(valleyHeight, mountainHeight, this.fade(largeFilter));
                yDev = this.lerp(smallHillHeight, yDev, smallFilter);
                yDev = this.lerp(baseHeight, yDev, featureFilter);

                for (int y = 0; y < GCMarsChunkProvider.CHUNK_SIZE_Y; y++)
                {
                    if (y < GCMarsChunkProvider.MID_HEIGHT + yDev)
                    {
                        idArray[this.getIndex(x, y, z)] = this.lowerBlockID;
                        metaArray[this.getIndex(x, y, z)] = this.lowerBlockMeta;
                    }
                }
            }
        }
    }

    private double lerp(double d1, double d2, double t)
    {
        if (t < 0.0)
        {
            return d1;
        }
        else if (t > 1.0)
        {
            return d2;
        }
        else
        {
            return d1 + (d2 - d1) * t;
        }
    }

    private double fade(double n)
    {
        return n * n * n * (n * (n * 6 - 15) + 10);
    }

    private double clamp(double x, double min, double max)
    {
        if (x < min)
        {
            return min;
        }
        if (x > max)
        {
            return max;
        }
        return x;
    }

    public void replaceBlocksForBiome(int par1, int par2, short[] arrayOfIDs, byte[] arrayOfMeta, BiomeGenBase[] par4ArrayOfBiomeGenBase)
    {
        final int var5 = 20;
        final double var6 = 0.03125D;
        this.noiseGen4.frequency = var6 * 2;
        for (int var8 = 0; var8 < 16; ++var8)
        {
            for (int var9 = 0; var9 < 16; ++var9)
            {
                final int var12 = (int) (this.noiseGen4.getNoise(par1 * 16 + var8, par2 * 16 + var9) / 3.0D + 3.0D + this.rand.nextDouble() * 0.25D);
                int var13 = -1;
                short var14 = this.topBlockID;
                byte var14m = this.topBlockMeta;
                short var15 = this.fillBlockID;
                byte var15m = this.fillBlockMeta;

                for (int var16 = 127; var16 >= 0; --var16)
                {
                    final int index = this.getIndex(var8, var16, var9);

                    if (var16 <= 0 + this.rand.nextInt(5))
                    {
                        arrayOfIDs[index] = (short) Block.bedrock.blockID;
                    }
                    else
                    {
                        final int var18 = arrayOfIDs[index];

                        if (var18 == 0)
                        {
                            var13 = -1;
                        }
                        else if (var18 == this.lowerBlockID)
                        {
                            arrayOfMeta[index] = this.lowerBlockMeta;

                            if (var13 == -1)
                            {
                                if (var12 <= 0)
                                {
                                    var14 = 0;
                                    var14m = 0;
                                    var15 = this.lowerBlockID;
                                    var15m = this.lowerBlockMeta;
                                }
                                else if (var16 >= var5 - -16 && var16 <= var5 + 1)
                                {
                                    var14 = this.topBlockID;
                                    var14m = this.topBlockMeta;
                                    var14 = this.fillBlockID;
                                    var14m = this.fillBlockMeta;
                                }

                                if (var16 < var5 && var14 == 0)
                                {
                                    var14 = 0;
                                }

                                var13 = var12;

                                if (var16 >= var5 - 1)
                                {
                                    arrayOfIDs[index] = var14;
                                    arrayOfMeta[index] = var14m;
                                }
                                else
                                {
                                    arrayOfIDs[index] = var15;
                                    arrayOfMeta[index] = var15m;
                                }
                            }
                            else if (var13 > 0)
                            {
                                --var13;
                                arrayOfIDs[index] = var15;
                                arrayOfMeta[index] = var15m;
                            }
                        }
                    }
                }
            }
        }
    }

    @Override
    public Chunk provideChunk(int par1, int par2)
    {
        this.rand.setSeed(par1 * 341873128712L + par2 * 132897987541L);
        final short[] ids = new short[32768];
        final byte[] meta = new byte[32768];
        this.generateTerrain(par1, par2, ids, meta);
        this.createCraters(par1, par2, ids, meta);
        this.biomesForGeneration = this.worldObj.getWorldChunkManager().loadBlockGeneratorData(this.biomesForGeneration, par1 * 16, par2 * 16, 16, 16);
        this.replaceBlocksForBiome(par1, par2, ids, meta, this.biomesForGeneration);
        this.caveGenerator.generate(this, this.worldObj, par1, par2, ids, meta);
        this.caveGenerator2.generate(this, this.worldObj, par1, par2, ids, meta);

        final Chunk var4 = new Chunk(this.worldObj, ids, meta, par1, par2);
        final byte[] var5 = var4.getBiomeArray();

        for (int var6 = 0; var6 < var5.length; ++var6)
        {
            var5[var6] = (byte) this.biomesForGeneration[var6].biomeID;
        }

        var4.generateSkylightMap();
        return var4;
    }

    public void createCraters(int chunkX, int chunkZ, short[] chunkArray, byte[] metaArray)
    {
        this.noiseGen5.frequency = 0.015;
        for (int cx = chunkX - 2; cx <= chunkX + 2; cx++)
        {
            for (int cz = chunkZ - 2; cz <= chunkZ + 2; cz++)
            {
                for (int x = 0; x < GCMarsChunkProvider.CHUNK_SIZE_X; x++)
                {
                    for (int z = 0; z < GCMarsChunkProvider.CHUNK_SIZE_Z; z++)
                    {
                        if (Math.abs(this.randFromPoint(cx * 16 + x, (cz * 16 + z) * 1000)) < this.noiseGen5.getNoise(cx * 16 + x, cz * 16 + z) / GCMarsChunkProvider.CRATER_PROB)
                        {
                            final Random random = new Random(cx * 16 + x + (cz * 16 + z) * 5000);
                            final GCCoreCraterSize cSize = GCCoreCraterSize.sizeArray[random.nextInt(GCCoreCraterSize.sizeArray.length)];
                            final int size = random.nextInt(cSize.MAX_SIZE - cSize.MIN_SIZE) + cSize.MIN_SIZE + 15;
                            this.makeCrater(cx * 16 + x, cz * 16 + z, chunkX * 16, chunkZ * 16, size, chunkArray, metaArray);
                        }
                    }
                }
            }
        }
    }

    public void makeCrater(int craterX, int craterZ, int chunkX, int chunkZ, int size, short[] chunkArray, byte[] metaArray)
    {
        for (int x = 0; x < GCMarsChunkProvider.CHUNK_SIZE_X; x++)
        {
            for (int z = 0; z < GCMarsChunkProvider.CHUNK_SIZE_Z; z++)
            {
                double xDev = craterX - (chunkX + x);
                double zDev = craterZ - (chunkZ + z);
                if (xDev * xDev + zDev * zDev < size * size)
                {
                    xDev /= size;
                    zDev /= size;
                    final double sqrtY = xDev * xDev + zDev * zDev;
                    double yDev = sqrtY * sqrtY * 6;
                    yDev = 5 - yDev;
                    int helper = 0;
                    for (int y = 127; y > 0; y--)
                    {
                        if (chunkArray[this.getIndex(x, y, z)] != 0 && helper <= yDev)
                        {
                            chunkArray[this.getIndex(x, y, z)] = 0;
                            metaArray[this.getIndex(x, y, z)] = 0;
                            helper++;
                        }
                        if (helper > yDev)
                        {
                            break;
                        }
                    }
                }
            }
        }
    }

    private int getIndex(int x, int y, int z)
    {
        return y << 8 | z << 4 | x;
    }

    private double randFromPoint(int x, int z)
    {
        int n;
        n = x + z * 57;
        n = n << 13 ^ n;
        return 1.0 - (n * (n * n * 15731 + 789221) + 1376312589 & 0x7fffffff) / 1073741824.0;
    }

    /*
     * private double[] initializeNoiseField(double[] par1ArrayOfDouble, int
     * par2, int par3, int par4, int par5, int par6, int par7) { if
     * (par1ArrayOfDouble == null) { par1ArrayOfDouble = new double[par5 * par6
     * * par7]; }
     * 
     * if (this.field_35388_l == null) { this.field_35388_l = new float[25];
     * 
     * for (int var8 = -2; var8 <= 2; ++var8) { for (int var9 = -2; var9 <= 2;
     * ++var9) { final float var10 = 10.0F / MathHelper.sqrt_float(var8 * var8 +
     * var9 * var9 + 0.2F); this.field_35388_l[var8 + 2 + (var9 + 2) * 5] =
     * var10; } } }
     * 
     * final double var44 = 684.412D; final double var45 = 684.412D; this.noise5
     * = this.noiseGen5.generateNoiseOctaves(this.noise5, par2, par4, par5,
     * par7, 1.121D, 1.121D, 0.5D); this.noise6 =
     * this.noiseGen6.generateNoiseOctaves(this.noise6, par2, par4, par5, par7,
     * 200.0D, 200.0D, 0.5D); this.noise3 =
     * this.noiseGen3.generateNoiseOctaves(this.noise3, par2, par3, par4, par5,
     * par6, par7, var44 / 80.0D, var45 / 160.0D, var44 / 80.0D); this.noise1 =
     * this.noiseGen1.generateNoiseOctaves(this.noise1, par2, par3, par4, par5,
     * par6, par7, var44, var45, var44); this.noise2 =
     * this.noiseGen2.generateNoiseOctaves(this.noise2, par2, par3, par4, par5,
     * par6, par7, var44, var45, var44); final boolean var43 = false; final
     * boolean var42 = false; int var12 = 0; int var13 = 0;
     * 
     * for (int var14 = 0; var14 < par5; ++var14) { for (int var15 = 0; var15 <
     * par7; ++var15) { float var16 = 0.0F; float var17 = 0.0F; float var18 =
     * 0.0F; final int var19 = 2; final BiomeGenBase var20 =
     * this.biomesForGeneration[var14 + 2 + (var15 + 2) * (par5 + 5)];
     * 
     * for (int var21 = -var19; var21 <= var19; ++var21) { for (int var22 =
     * -var19; var22 <= var19; ++var22) { final BiomeGenBase var23 =
     * this.biomesForGeneration[var14 + var21 + 2 + (var15 + var22 + 2) * (par5
     * + 5)]; float var24 = this.field_35388_l[var21 + 2 + (var22 + 2) * 5] /
     * (var23.minHeight + 2.0F);
     * 
     * if (var23.minHeight > var20.minHeight) { var24 /= 2.0F; }
     * 
     * var16 += var23.maxHeight * var24 + 2; var17 += var23.minHeight * var24 *
     * 0.5 + 2; var18 += var24; } }
     * 
     * var16 /= var18; var17 /= var18; var16 = var16 * 0.9F + 0.1F; var17 =
     * (var17 * 4.0F - 1.0F) / 8.0F; double var47 = this.noise6[var13] /
     * 8000.0D;
     * 
     * if (var47 < 0.0D) { var47 = -var47 * 0.3D; }
     * 
     * var47 = var47 * 3.0D - 2.0D;
     * 
     * if (var47 < 0.0D) { var47 /= 2.0D;
     * 
     * if (var47 < -1.0D) { var47 = -1.0D; }
     * 
     * var47 /= 1.4D; var47 /= 2.0D; } else { if (var47 > 1.0D) { var47 = 1.0D;
     * }
     * 
     * var47 /= 8.0D; }
     * 
     * ++var13;
     * 
     * for (int var46 = 0; var46 < par6; ++var46) { double var48 = var17; final
     * double var26 = var16; var48 += var47 * 0.2D; var48 = var48 * par6 /
     * 16.0D; final double var28 = par6 / 2.0D + var48 * 4.0D; double var30 =
     * 0.0D; double var32 = (var46 - var28) * 12.0D * 128.0D / 128.0D / var26;
     * 
     * if (var32 < 0.0D) { var32 *= 4.0D; }
     * 
     * final double var34 = this.noise1[var12] / 512.0D; final double var36 =
     * this.noise2[var12] / 512.0D; final double var38 = (this.noise3[var12] /
     * 10.0D + 1.0D) / 2.0D;
     * 
     * if (var38 < 0.0D) { var30 = var34; } else if (var38 > 1.0D) { var30 =
     * var36; } else { var30 = var34 + (var36 - var34) * var38; }
     * 
     * var30 -= var32;
     * 
     * if (var46 > par6 - 4) { final double var40 = (var46 - (par6 - 4)) / 3.0F;
     * var30 = var30 * (1.0D - var40) + -10.0D * var40; }
     * 
     * par1ArrayOfDouble[var12] = var30; ++var12; } } }
     * 
     * return par1ArrayOfDouble; }
     */

    @Override
    public boolean chunkExists(int par1, int par2)
    {
        return true;
    }

    public void decoratePlanet(World par1World, Random par2Random, int par3, int par4)
    {
        this.biomedecoratorplanet.decorate(par1World, par2Random, par3, par4);
    }

    @Override
    public void populate(IChunkProvider par1IChunkProvider, int par2, int par3)
    {
        BlockSand.fallInstantly = true;
        int var4 = par2 * 16;
        int var5 = par3 * 16;
        this.worldObj.getBiomeGenForCoords(var4 + 16, var5 + 16);
        this.rand.setSeed(this.worldObj.getSeed());
        final long var7 = this.rand.nextLong() / 2L * 2L + 1L;
        final long var9 = this.rand.nextLong() / 2L * 2L + 1L;
        this.rand.setSeed(par2 * var7 + par3 * var9 ^ this.worldObj.getSeed());
        this.decoratePlanet(this.worldObj, this.rand, var4, var5);
        var4 += 8;
        var5 += 8;

        BlockSand.fallInstantly = false;
    }

    @Override
    public boolean saveChunks(boolean par1, IProgressUpdate par2IProgressUpdate)
    {
        return true;
    }

    @Override
    public boolean canSave()
    {
        return true;
    }

    @Override
    public String makeString()
    {
        return "RandomLevelSource";
    }

    @Override
    public List getPossibleCreatures(EnumCreatureType par1EnumCreatureType, int i, int j, int k)
    {
        if (j < 64 && par1EnumCreatureType == EnumCreatureType.monster)
        {
            final List monsters = new ArrayList();
            monsters.add(new SpawnListEntry(GCCoreEntityZombie.class, 14, 2, 3));
            monsters.add(new SpawnListEntry(GCCoreEntitySpider.class, 14, 2, 3));
            monsters.add(new SpawnListEntry(GCCoreEntitySkeleton.class, 14, 2, 3));
            monsters.add(new SpawnListEntry(GCCoreEntityCreeper.class, 14, 2, 3));
            return monsters;
        }
        else
        {
            return null;
        }
    }
}
