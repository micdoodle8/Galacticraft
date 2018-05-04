package micdoodle8.mods.galacticraft.core.world.gen;

import micdoodle8.mods.galacticraft.api.prefab.world.gen.MapGenBaseMeta;
import micdoodle8.mods.galacticraft.api.world.ChunkProviderBase;
import micdoodle8.mods.galacticraft.core.GCBlocks;
import micdoodle8.mods.galacticraft.core.blocks.BlockBasicMoon;
import micdoodle8.mods.galacticraft.core.perlin.NoiseModule;
import micdoodle8.mods.galacticraft.core.perlin.generator.Gradient;
import micdoodle8.mods.galacticraft.core.util.ConfigManagerCore;
import micdoodle8.mods.galacticraft.core.world.gen.dungeon.DungeonConfiguration;
import micdoodle8.mods.galacticraft.core.world.gen.dungeon.MapGenDungeon;
import micdoodle8.mods.galacticraft.core.world.gen.dungeon.RoomBoss;
import micdoodle8.mods.galacticraft.core.world.gen.dungeon.RoomTreasure;
import net.minecraft.block.BlockFalling;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.world.ChunkCoordIntPair;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.ChunkPrimer;
import net.minecraft.world.chunk.IChunkProvider;

import java.util.List;
import java.util.Random;

public class ChunkProviderMoon extends ChunkProviderBase
{
    public static final IBlockState BLOCK_TOP = GCBlocks.blockMoon.getDefaultState().withProperty(BlockBasicMoon.BASIC_TYPE_MOON, BlockBasicMoon.EnumBlockBasicMoon.MOON_TURF);
    public static final IBlockState BLOCK_FILL = GCBlocks.blockMoon.getDefaultState().withProperty(BlockBasicMoon.BASIC_TYPE_MOON, BlockBasicMoon.EnumBlockBasicMoon.MOON_DIRT);
    public static final IBlockState BLOCK_LOWER = GCBlocks.blockMoon.getDefaultState().withProperty(BlockBasicMoon.BASIC_TYPE_MOON, BlockBasicMoon.EnumBlockBasicMoon.MOON_STONE);

    private final Random rand;

    private final NoiseModule noiseGen1;
    private final NoiseModule noiseGen2;
    private final NoiseModule noiseGen3;
    private final NoiseModule noiseGen4;

    private final World worldObj;
    private final MapGenVillageMoon villageGenerator = new MapGenVillageMoon();

    private final MapGenDungeon dungeonGeneratorMoon = new MapGenDungeon(new DungeonConfiguration(GCBlocks.blockMoon.getDefaultState().withProperty(BlockBasicMoon.BASIC_TYPE_MOON, BlockBasicMoon.EnumBlockBasicMoon.MOON_DUNGEON_BRICK), 25, 8, 16, 5, 6, RoomBoss.class, RoomTreasure.class));

    private BiomeGenBase[] biomesForGeneration = { BiomeGenBaseMoon.moonFlat };

    private final MapGenBaseMeta caveGenerator = new MapGenCavesMoon();

    private static final int CRATER_PROB = 300;

    // DO NOT CHANGE
    private static final int MID_HEIGHT = 63;
    private static final int CHUNK_SIZE_X = 16;
    private static final int CHUNK_SIZE_Y = 128;
    private static final int CHUNK_SIZE_Z = 16;

    public ChunkProviderMoon(World par1World, long par2, boolean par4)
    {
        this.worldObj = par1World;
        this.rand = new Random(par2);
        this.noiseGen1 = new Gradient(this.rand.nextLong(), 4, 0.25F);
        this.noiseGen2 = new Gradient(this.rand.nextLong(), 4, 0.25F);
        this.noiseGen3 = new Gradient(this.rand.nextLong(), 1, 0.25F);
        this.noiseGen4 = new Gradient(this.rand.nextLong(), 1, 0.25F);
    }

    public void setBlocksInChunk(int chunkX, int chunkZ, ChunkPrimer primer)
    {
        this.noiseGen1.setFrequency(0.0125F);
        this.noiseGen2.setFrequency(0.015F);
        this.noiseGen3.setFrequency(0.01F);
        this.noiseGen4.setFrequency(0.02F);

        for (int x = 0; x < ChunkProviderMoon.CHUNK_SIZE_X; x++)
        {
            for (int z = 0; z < ChunkProviderMoon.CHUNK_SIZE_Z; z++)
            {
                final double d = this.noiseGen1.getNoise(x + chunkX * 16, z + chunkZ * 16) * 8;
                final double d2 = this.noiseGen2.getNoise(x + chunkX * 16, z + chunkZ * 16) * 24;
                double d3 = this.noiseGen3.getNoise(x + chunkX * 16, z + chunkZ * 16) - 0.1;
                d3 *= 4;

                double yDev;

                if (d3 < 0.0D)
                {
                    yDev = d;
                }
                else if (d3 > 1.0D)
                {
                    yDev = d2;
                }
                else
                {
                    yDev = d + (d2 - d) * d3;
                }

                for (int y = 0; y < ChunkProviderMoon.CHUNK_SIZE_Y; y++)
                {
                    if (y < ChunkProviderMoon.MID_HEIGHT + yDev)
                    {
                        primer.setBlockState(getIndex(x, y, z), BLOCK_LOWER);
                    }
                }
            }
        }
    }

    public void replaceBlocksForBiome(int par1, int par2, ChunkPrimer primer, BiomeGenBase[] par4ArrayOfBiomeGenBase)
    {
        final int var5 = 20;
        for (int var8 = 0; var8 < 16; ++var8)
        {
            for (int var9 = 0; var9 < 16; ++var9)
            {
                final int var12 = (int) (this.noiseGen4.getNoise(var8 + par1 * 16, var9 * par2 * 16) / 3.0D + 3.0D + this.rand.nextDouble() * 0.25D);
                int var13 = -1;
                IBlockState state0 = BLOCK_TOP;
                IBlockState state1 = BLOCK_FILL;

                for (int var16 = 127; var16 >= 0; --var16)
                {
                    final int index = this.getIndex(var8, var16, var9);

                    if (var16 <= this.rand.nextInt(5))
                    {
                        primer.setBlockState(index, Blocks.bedrock.getDefaultState());
                    }
                    else
                    {
                        IBlockState var18 = primer.getBlockState(index);
                        if (Blocks.air == var18)
                        {
                            var13 = -1;
                        }
                        else if (var18 == BLOCK_LOWER)
                        {
                            if (var13 == -1)
                            {
                                if (var12 <= 0)
                                {
                                    state0 = Blocks.air.getDefaultState();
                                    state1 = BLOCK_LOWER;
                                }
                                else if (var16 >= var5 - -16 && var16 <= var5 + 1)
                                {
                                    state0 = BLOCK_FILL;
                                }

                                var13 = var12;

                                if (var16 >= var5 - 1)
                                {
                                    primer.setBlockState(index, state0);
                                }
                                else if (var16 < var5 - 1 && var16 >= var5 - 2)
                                {
                                    primer.setBlockState(index, state1);
                                }
                            }
                            else if (var13 > 0)
                            {
                                --var13;
                                primer.setBlockState(index, state1);
                            }
                        }
                    }
                }
            }
        }
    }

    @Override
    public Chunk provideChunk(int x, int z)
    {
        this.rand.setSeed((long) x * 341873128712L + (long) z * 132897987541L);
        ChunkPrimer chunkprimer = new ChunkPrimer();
        this.setBlocksInChunk(x, z, chunkprimer);
        this.createCraters(x, z, chunkprimer);
        this.replaceBlocksForBiome(x, z, chunkprimer, null);

        this.caveGenerator.generate(this, this.worldObj, x, z, chunkprimer);

        this.dungeonGeneratorMoon.generate(this, this.worldObj, x, z, chunkprimer);
        this.villageGenerator.generate(this, this.worldObj, x, z, chunkprimer);

        Chunk chunk = new Chunk(this.worldObj, chunkprimer, x, z);
        byte[] abyte = chunk.getBiomeArray();
        byte id = (byte) BiomeGenBaseMoon.moonFlat.biomeID;
        for (int i = 0; i < abyte.length; ++i)
        {
            abyte[i] = id;
        }

        chunk.generateSkylightMap();
        return chunk;
    }

    private void createCraters(int chunkX, int chunkZ, ChunkPrimer primer)
    {
        for (int cx = chunkX - 2; cx <= chunkX + 2; cx++)
        {
            for (int cz = chunkZ - 2; cz <= chunkZ + 2; cz++)
            {
                for (int x = 0; x < ChunkProviderMoon.CHUNK_SIZE_X; x++)
                {
                    for (int z = 0; z < ChunkProviderMoon.CHUNK_SIZE_Z; z++)
                    {
                        if (Math.abs(this.randFromPoint(cx * 16 + x, (cz * 16 + z) * 1000)) < this.noiseGen4.getNoise(x * ChunkProviderMoon.CHUNK_SIZE_X + x, cz * ChunkProviderMoon.CHUNK_SIZE_Z + z) / ChunkProviderMoon.CRATER_PROB)
                        {
                            final Random random = new Random(cx * 16 + x + (cz * 16 + z) * 5000);
                            final EnumCraterSize cSize = EnumCraterSize.sizeArray[random.nextInt(EnumCraterSize.sizeArray.length)];
                            final int size = random.nextInt(cSize.MAX_SIZE - cSize.MIN_SIZE) + cSize.MIN_SIZE;
                            this.makeCrater(cx * 16 + x, cz * 16 + z, chunkX * 16, chunkZ * 16, size, primer);
                        }
                    }
                }
            }
        }
    }

    private void makeCrater(int craterX, int craterZ, int chunkX, int chunkZ, int size, ChunkPrimer primer)
    {
        for (int x = 0; x < ChunkProviderMoon.CHUNK_SIZE_X; x++)
        {
            for (int z = 0; z < ChunkProviderMoon.CHUNK_SIZE_Z; z++)
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
                        if (Blocks.air != primer.getBlockState(this.getIndex(x, y, z)).getBlock() && helper <= yDev)
                        {
                            primer.setBlockState(this.getIndex(x, y, z), Blocks.air.getDefaultState());
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
        return (x * 16 + z) * 256 + y;
    }

    private double randFromPoint(int x, int z)
    {
        int n;
        n = x + z * 57;
        n = n << 13 ^ n;
        return 1.0 - (n * (n * n * 15731 + 789221) + 1376312589 & 0x7fffffff) / 1073741824.0;
    }

    @Override
    public void populate(IChunkProvider chunkProvider, int x, int z)
    {
        BlockFalling.fallInstantly = true;
        int i = x * 16;
        int j = z * 16;
        BlockPos blockpos = new BlockPos(i, 0, j);
        BiomeGenBase biomegenbase = this.worldObj.getBiomeGenForCoords(blockpos.add(16, 0, 16));
        this.rand.setSeed(this.worldObj.getSeed());
        long k = this.rand.nextLong() / 2L * 2L + 1L;
        long l = this.rand.nextLong() / 2L * 2L + 1L;
        this.rand.setSeed((long) x * k + (long) z * l ^ this.worldObj.getSeed());

        if (!ConfigManagerCore.disableMoonVillageGen)
        {
            this.villageGenerator.generateStructure(this.worldObj, this.rand, new ChunkCoordIntPair(x, z));
        }

        this.dungeonGeneratorMoon.generateStructure(this.worldObj, this.rand, new ChunkCoordIntPair(x, z));

        biomegenbase.decorate(this.worldObj, this.rand, new BlockPos(i, 0, j));
        BlockFalling.fallInstantly = false;
    }

    @Override
    public String makeString()
    {
        return "MoonLevelSource";
    }

    @Override
    public List<BiomeGenBase.SpawnListEntry> getPossibleCreatures(EnumCreatureType creatureType, BlockPos pos)
    {
        if (creatureType == EnumCreatureType.MONSTER)
        {
            return BiomeGenBaseMoon.moonFlat.getSpawnableList(creatureType);
        }
        else
        {
            return null;
        }
    }

    @Override
    public void recreateStructures(Chunk chunk, int x, int z)
    {
        if (!ConfigManagerCore.disableMoonVillageGen)
        {
            this.villageGenerator.generate(this, this.worldObj, x, z, null);
        }

        this.dungeonGeneratorMoon.generate(this, this.worldObj, x, z, null);
    }
}
