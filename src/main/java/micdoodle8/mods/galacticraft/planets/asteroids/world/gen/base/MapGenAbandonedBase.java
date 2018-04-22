package micdoodle8.mods.galacticraft.planets.asteroids.world.gen.base;

import net.minecraft.util.math.BlockPos;
import java.util.List;
import java.util.Random;

import micdoodle8.mods.galacticraft.api.vector.BlockVec3;
import micdoodle8.mods.galacticraft.api.world.IGalacticraftWorldProvider;
import micdoodle8.mods.galacticraft.core.world.gen.dungeon.MapGenDungeon;
import micdoodle8.mods.galacticraft.planets.asteroids.dimension.WorldProviderAsteroids;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.chunk.ChunkPrimer;
import net.minecraft.world.gen.structure.MapGenStructure;
import net.minecraft.world.gen.structure.MapGenStructureIO;
import net.minecraft.world.gen.structure.StructureComponent;
import net.minecraft.world.gen.structure.StructureStart;

public class MapGenAbandonedBase extends MapGenStructure
{
    private static boolean initialized;

    static
    {
        try
        {
            MapGenAbandonedBase.initiateStructures();
        }
        catch (Throwable e)
        {

        }
    }

    public MapGenAbandonedBase()
    {
    }

    public static void initiateStructures() throws Throwable
    {
        if (!MapGenAbandonedBase.initialized)
        {
              MapGenStructureIO.registerStructure(MapGenAbandonedBase.Start.class, "AbandonedBase");
              MapGenStructureIO.registerStructureComponent(BaseStart.class, "AbandonedBaseStart");
              MapGenStructureIO.registerStructureComponent(BaseRoom.class, "AbandonedBaseRoom");
              MapGenStructureIO.registerStructureComponent(BaseDeck.class, "AbandonedBaseDeck");
              MapGenStructureIO.registerStructureComponent(BasePlate.class, "AbandonedBasePlate");
              MapGenStructureIO.registerStructureComponent(BaseHangar.class, "AbandonedBaseHangar");

              //Currently in fact unused, but just in case...
              MapGenStructureIO.registerStructureComponent(BaseLinking.class, "AbandonedBaseCorridor");
        }

        MapGenAbandonedBase.initialized = true;
    }

    @Override
    public BlockPos getNearestStructurePos(World worldIn, BlockPos pos, boolean p_180706_3_)
    {
        return null;
    }

    @Override
    public String getStructureName()
    {
        return "GC_AbandonedBase";
    }
   
    @Override
    protected boolean canSpawnStructureAtCoords(int chunkX, int chunkZ)
    {
        long dungeonPos = MapGenDungeon.getDungeonPosForCoords(this.world, chunkX, chunkZ, ((IGalacticraftWorldProvider) this.world.provider).getDungeonSpacing());
        int i = (int) (dungeonPos >> 32);
        int j = (int) dungeonPos;
        return i == chunkX && j == chunkZ;
    }

    @Override
    public void generate(World worldIn, int x, int z, ChunkPrimer chunkPrimerIn)
    {
        int i = this.range;
        this.world = worldIn;
        this.rand.setSeed(worldIn.getSeed());
        long j = this.rand.nextLong();
        long k = this.rand.nextLong();

        for (int l = x - i; l <= x + i; ++l)
        {
            for (int i1 = z - i; i1 <= z + i; ++i1)
            {
                long j1 = (long)l * j;
                long k1 = (long)i1 * k;
                this.rand.setSeed(j1 ^ k1 ^ worldIn.getSeed());
                this.recursiveGenerate(worldIn, l, i1, x, z, chunkPrimerIn);
            }
        }
    }
    
    public void reset()
    {
        this.structureMap.clear();
    }

    @Override
    protected StructureStart getStructureStart(int chunkX, int chunkZ)
    {
        BlockVec3 asteroid = ((WorldProviderAsteroids) this.world.provider).getClosestAsteroidXZ((chunkX << 4) + 8, 0, (chunkZ << 4) + 8, false);
        if (asteroid == null)
        {
            return new MapGenAbandonedBase.Start(this.world, this.rand, (chunkX << 4) + 8, (chunkZ << 4) + 8, 15, new BaseConfiguration(148, this.rand));
        }
        return new MapGenAbandonedBase.Start(this.world, this.rand, asteroid.x, asteroid.z, asteroid.sideDoneBits - 5, new BaseConfiguration(asteroid.y - 10, this.rand));
    }

    public static class Start extends StructureStart
    {
        private BaseConfiguration configuration;

        public Start()
        {
        }

        public Start(World worldIn, Random rand, int posX, int posZ, int size, BaseConfiguration configuration)
        {
            super(posX >> 4, posZ >> 4);
            this.configuration = configuration;
            if (size < 1) size = 1;
            size = size * (int) MathHelper.sqrt(size) / 4;
            if (configuration.isHangarDeck()) size -= 6;
            int xoffset = 0;
            int zoffset = 0;
            EnumFacing direction = EnumFacing.Plane.HORIZONTAL.random(rand);
            switch (direction)
            {
            case NORTH:
                zoffset = -size;
                break;
            case SOUTH:
                zoffset = size;
                break;
            case WEST:
                xoffset = -size;
                break;
            case EAST:
                xoffset = size;
                break;
            }
            BaseStart startPiece = new BaseStart(configuration, rand, posX + xoffset, posZ + zoffset, direction);
            startPiece.buildComponent(startPiece, this.components, rand);
            List<StructureComponent> list = startPiece.attachedComponents;

            while (!list.isEmpty())
            {
                int i = rand.nextInt(list.size());
                StructureComponent structurecomponent = list.remove(i);
                structurecomponent.buildComponent(startPiece, this.components, rand);
            }

            this.updateBoundingBox();
        }
    }
 }
