package micdoodle8.mods.galacticraft.core.world.gen.dungeon;

import micdoodle8.mods.galacticraft.api.world.IGalacticraftWorldProvider;
import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.MapGenStructure;
import net.minecraft.world.gen.structure.MapGenStructureIO;
import net.minecraft.world.gen.structure.StructureComponent;
import net.minecraft.world.gen.structure.StructureStart;

import java.util.List;
import java.util.Random;

public class MapGenDungeon extends MapGenStructure
{
    private static boolean initialized;
    private DungeonConfiguration configuration;

    static
    {
        try
        {
            MapGenDungeon.initiateStructures();
        }
        catch (Throwable e)
        {

        }
    }

    public MapGenDungeon(DungeonConfiguration configuration)
    {
        this.configuration = configuration;
    }

    public static void initiateStructures() throws Throwable
    {
        if (!MapGenDungeon.initialized)
        {
            MapGenStructureIO.registerStructure(MapGenDungeon.Start.class, "MoonDungeon");
            MapGenStructureIO.registerStructureComponent(DungeonStart.class, "MoonDungeonStart");
            MapGenStructureIO.registerStructureComponent(Corridor.class, "MoonDungeonCorridor");
            MapGenStructureIO.registerStructureComponent(RoomEmpty.class, "MoonDungeonEmptyRoom");
            MapGenStructureIO.registerStructureComponent(RoomBoss.class, "MoonDungeonBossRoom");
            MapGenStructureIO.registerStructureComponent(RoomTreasure.class, "MoonDungeonTreasureRoom");
            MapGenStructureIO.registerStructureComponent(RoomSpawner.class, "MoonDungeonSpawnerRoom");
            MapGenStructureIO.registerStructureComponent(RoomChest.class, "MoonDungeonChestRoom");
        }

        MapGenDungeon.initialized = true;
    }

    @Override
    public String getStructureName()
    {
        return "GC_Dungeon";
    }

    @Override
    protected boolean canSpawnStructureAtCoords(int chunkX, int chunkZ)
    {
        long dungeonPos = getDungeonPosForCoords(this.worldObj, chunkX, chunkZ, ((IGalacticraftWorldProvider) this.worldObj.provider).getDungeonSpacing());
        int i = (int) (dungeonPos >> 32);
        int j = (int) dungeonPos;  //Java automatically gives the 32 least significant bits
        return i == chunkX && j == chunkZ;
    }
    
    public static long getDungeonPosForCoords(World world, int chunkX, int chunkZ, int spacing)
    {
        final int numChunks = spacing / 16;
        if (chunkX < 0)
        {
            chunkX -= numChunks - 1;
        }

        if (chunkZ < 0)
        {
            chunkZ -= numChunks - 1;
        }

        int k = chunkX / numChunks;
        int l = chunkZ / numChunks;
        long seed = (long)k * 341873128712L + (long)l * 132897987541L + world.getWorldInfo().getSeed() + (long)(10387340 + world.provider.getDimensionId());
        Random random = new Random();
        random.setSeed(seed);
        k = k * numChunks + random.nextInt(numChunks);
        l = l * numChunks + random.nextInt(numChunks);
        return (((long) k) << 32) + l;
    }
    
    /**
     * This returns an angle between 0 and 360 degrees.  0 degrees means due North from the current (x, z) position
     * Only provides meaningful results in worlds with dungeon generation using this class!
     */
    public static float directionToNearestDungeon(World world, double xpos, double zpos)
    {
        int spacing = ((IGalacticraftWorldProvider) world.provider).getDungeonSpacing();
        if (spacing == 0) return 0F;
        int x = MathHelper.floor_double(xpos);
        int z = MathHelper.floor_double(zpos);
        int quadrantX = x % spacing;
        int quadrantZ = z % spacing;
        int searchOffsetX = quadrantX / (spacing / 2);  //0 or 1
        int searchOffsetZ = quadrantZ / (spacing / 2);  //0 or 1
        double nearestX = 0;
        double nearestZ = 0;
        double nearestDistance = Double.MAX_VALUE;
        for (int cx = searchOffsetX - 1; cx < searchOffsetX + 1; cx++)
        {
            for (int cz = searchOffsetZ - 1; cz < searchOffsetZ + 1; cz++)
            {
                long dungeonPos = getDungeonPosForCoords(world, (x + cx * spacing) / 16, (z + cz * spacing) / 16, spacing);
                int i = 2 + (((int) (dungeonPos >> 32)) << 4);
                int j = 2 + (((int) dungeonPos) << 4);  //Java automatically gives the 32 least significant bits
                double oX = i - xpos;
                double oZ = j - zpos;
                double distanceSq = oX * oX + oZ * oZ;
                if (distanceSq < nearestDistance)
                {
                    nearestDistance = distanceSq;
                    nearestX = oX;
                    nearestZ = oZ;
                }
            }
        }

        return GCCoreUtil.getAngleForRelativePosition(nearestX, nearestZ);
    }

    @Override
    protected StructureStart getStructureStart(int chunkX, int chunkZ)
    {
        return new MapGenDungeon.Start(this.worldObj, this.rand, chunkX, chunkZ, this.configuration);
    }

    public static class Start extends StructureStart
    {
        private DungeonConfiguration configuration;

        public Start()
        {
        }

        public Start(World worldIn, Random rand, int chunkX, int chunkZ, DungeonConfiguration configuration)
        {
            super(chunkX, chunkZ);
            this.configuration = configuration;
            DungeonStart startPiece = new DungeonStart(worldIn, configuration, rand, (chunkX << 4) + 2, (chunkZ << 4) + 2);
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
