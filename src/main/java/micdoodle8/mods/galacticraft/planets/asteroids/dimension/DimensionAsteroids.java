package micdoodle8.mods.galacticraft.planets.asteroids.dimension;

import micdoodle8.mods.galacticraft.api.galaxies.CelestialBody;
import micdoodle8.mods.galacticraft.api.prefab.world.gen.DimensionSpace;
import micdoodle8.mods.galacticraft.api.vector.BlockVec3;
import micdoodle8.mods.galacticraft.api.vector.Vector3;
import micdoodle8.mods.galacticraft.api.world.ISolarLevel;
import micdoodle8.mods.galacticraft.core.event.EventHandlerGC;
import micdoodle8.mods.galacticraft.core.util.ConfigManagerCore;
import micdoodle8.mods.galacticraft.core.util.GCLog;
import micdoodle8.mods.galacticraft.core.world.gen.BiomeMoon;
import micdoodle8.mods.galacticraft.core.world.gen.dungeon.RoomTreasure;
import micdoodle8.mods.galacticraft.planets.asteroids.AsteroidsModule;
import micdoodle8.mods.galacticraft.planets.asteroids.entities.EntityAstroMiner;
import micdoodle8.mods.galacticraft.planets.asteroids.world.gen.AsteroidChunkGenerator;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SharedConstants;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.provider.BiomeProviderType;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.Heightmap;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.TreeMap;

public class DimensionAsteroids extends DimensionSpace implements ISolarLevel
{
    //Used to list asteroid centres to external code that needs to know them
    private final HashSet<AsteroidData> asteroids = new HashSet<>();
    private boolean dataNotLoaded = true;
    private AsteroidSaveData datafile;
    private double solarMultiplier = -1D;

    public DimensionAsteroids(World worldIn, DimensionType typeIn)
    {
        super(worldIn, typeIn, 0.0F);
        this.nether = true;
    }

    //	@Override
//	public void registerWorldChunkManager()
//	{
//		this.worldChunkMgr = new WorldChunkManagerAsteroids(this.world, 0F);
//	}

    @Override
    public ChunkGenerator createChunkGenerator()
    {
        AsteroidGenSettings settings = new AsteroidGenSettings();
        return new AsteroidChunkGenerator(this.world, BiomeProviderType.FIXED.create(BiomeProviderType.FIXED.createSettings(world.getWorldInfo()).setBiome(BiomeMoon.moonBiome)), settings);
    }

    @Override
    public CelestialBody getCelestialBody()
    {
        return AsteroidsModule.planetAsteroids;
    }

    @Override
    public Vec3d getFogColor(float celestialAngle, float partialTicks)
    {
        return new Vec3d(0, 0, 0);
    }

//    @Override
//    public Vector3 getFogColor()
//    {
//        return new Vector3(0, 0, 0);
//    }
//
//    @Override
//    public Vector3 getSkyColor()
//    {
//        return new Vector3(0, 0, 0);
//    }

    @Override
    public boolean hasSunset()
    {
        return false;
    }

    @Override
    public long getDayLength()
    {
        return 0;
    }

    @Override
    public boolean isDaytime()
    {
        return true;
    }

//    @Override
//    public Class<? extends ChunkGenerator> getChunkProviderClass()
//    {
//        return ChunkProviderAsteroids.class;
//    }

    @Override
    public float calculateCelestialAngle(long par1, float par3)
    {
        return 0.25F;
    }

//    @Override
//    @OnlyIn(Dist.CLIENT)
//    public float getStarBrightness(float par1)
//    {
//        return 1.0F;
//    }
//
//	@Override
//	public IChunkProvider createChunkGenerator()
//	{
//		return new ChunkProviderAsteroids(this.world, this.world.getSeed(), this.world.getWorldInfo().isMapFeaturesEnabled());
//	}

//    @Override
//    public double getHorizon()
//    {
//        return 44.0D;
//    }

    @Override
    public int getSeaLevel()
    {
        return 96;
    }

    @Override
    @Nullable
    public BlockPos findSpawn(ChunkPos chunkPosIn, boolean checkValid)
    {
        for (int i = chunkPosIn.getXStart(); i <= chunkPosIn.getXEnd(); ++i)
        {
            for (int j = chunkPosIn.getZStart(); j <= chunkPosIn.getZEnd(); ++j)
            {
                BlockPos blockpos = this.findSpawn(i, j, checkValid);
                if (blockpos != null)
                {
                    return blockpos;
                }
            }
        }

        return null;
    }

    @Override
    @Nullable
    public BlockPos findSpawn(int posX, int posZ, boolean checkValid)
    {
        BlockPos.Mutable blockpos$mutableblockpos = new BlockPos.Mutable(posX, 0, posZ);
        Biome biome = this.world.getBiome(blockpos$mutableblockpos);
        BlockState blockstate = biome.getSurfaceBuilderConfig().getTop();
        if (checkValid && !blockstate.getBlock().isIn(BlockTags.VALID_SPAWN))
        {
            return null;
        }
        else
        {
            Chunk chunk = this.world.getChunk(posX >> 4, posZ >> 4);
            int i = chunk.getTopBlockY(Heightmap.Type.MOTION_BLOCKING, posX & 15, posZ & 15);
            if (i < 0)
            {
                return null;
            }
            else if (chunk.getTopBlockY(Heightmap.Type.WORLD_SURFACE, posX & 15, posZ & 15) > chunk.getTopBlockY(Heightmap.Type.OCEAN_FLOOR, posX & 15, posZ & 15))
            {
                return null;
            }
            else
            {
                for (int j = i + 1; j >= 0; --j)
                {
                    blockpos$mutableblockpos.setPos(posX, j, posZ);
                    BlockState blockstate1 = this.world.getBlockState(blockpos$mutableblockpos);
                    if (!blockstate1.getFluidState().isEmpty())
                    {
                        break;
                    }

                    if (blockstate1.equals(blockstate))
                    {
                        return blockpos$mutableblockpos.up().toImmutable();
                    }
                }

                return null;
            }
        }
    }

    @Override
    public boolean doesXZShowFog(int x, int z)
    {
        return false;
    }

//    @Override
//    public int getAverageGroundLevel()
//    {
//        return 96;
//    }
//
//    @Override
//    public boolean canCoordinateBeSpawn(int var1, int var2)
//    {
//        return true;
//    }

    //Overriding so that beds do not explode on Asteroids
    @Override
    public boolean canRespawnHere()
    {
        if (EventHandlerGC.bedActivated)
        {
            EventHandlerGC.bedActivated = false;
            return true;
        }
        return false;
    }

    @Override
    public float getGravity()
    {
        return 0.072F;
    }

    @Override
    public double getMeteorFrequency()
    {
        return 10.0D;
    }

    @Override
    public double getFuelUsageMultiplier()
    {
        return 0.9D;
    }

    @Override
    public boolean canSpaceshipTierPass(int tier)
    {
        return tier >= 3;
    }

    @Override
    public float getFallDamageModifier()
    {
        return 0.1F;
    }

    public void addAsteroid(int x, int y, int z, int size, int core)
    {
        AsteroidData coords = new AsteroidData(x, y, z, size, core);
        if (!this.asteroids.contains(coords))
        {
            if (this.dataNotLoaded)
            {
                this.loadAsteroidSavedData();
            }
            if (!this.asteroids.contains(coords))
            {
                this.addToNBT(this.datafile.datacompound, coords);
                this.asteroids.add(coords);
            }
        }
    }

    public void removeAsteroid(int x, int y, int z)
    {
        AsteroidData coords = new AsteroidData(x, y, z);
        if (this.asteroids.contains(coords))
        {
            this.asteroids.remove(coords);

            if (this.dataNotLoaded)
            {
                this.loadAsteroidSavedData();
            }
            this.writeToNBT(this.datafile.datacompound);
        }
    }

    private void loadAsteroidSavedData()
    {
        try
        {
            this.datafile = new AsteroidSaveData(AsteroidSaveData.saveDataID);
            this.datafile.read(((ServerWorld) this.world).getSavedData().load(AsteroidSaveData.saveDataID, SharedConstants.getVersion().getWorldVersion()));
        }
        catch (IOException e)
        {
            this.datafile = null;
            e.printStackTrace();
        }

        if (this.datafile == null)
        {
            this.datafile = new AsteroidSaveData(AsteroidSaveData.saveDataID);
            ((ServerWorld) this.world).getSavedData().set(this.datafile);
            this.writeToNBT(this.datafile.datacompound);
        }
        else
        {
            this.readFromNBT(this.datafile.datacompound);
        }

        this.dataNotLoaded = false;
    }

    private void readFromNBT(CompoundNBT nbt)
    {
        ListNBT coordList = nbt.getList("coords", 10);
        if (coordList.size() > 0)
        {
            for (int j = 0; j < coordList.size(); j++)
            {
                CompoundNBT tag1 = coordList.getCompound(j);

                if (tag1 != null)
                {
                    this.asteroids.add(AsteroidData.readFromNBT(tag1));
                }
            }
        }
    }

    private void writeToNBT(CompoundNBT nbt)
    {
        ListNBT coordList = new ListNBT();
        for (AsteroidData coords : this.asteroids)
        {
            CompoundNBT tag = new CompoundNBT();
            coords.writeToNBT(tag);
            coordList.add(tag);
        }
        nbt.put("coords", coordList);
        this.datafile.markDirty();
    }

    private void addToNBT(CompoundNBT nbt, AsteroidData coords)
    {
        ListNBT coordList = nbt.getList("coords", 10);
        CompoundNBT tag = new CompoundNBT();
        coords.writeToNBT(tag);
        coordList.add(tag);
        nbt.put("coords", coordList);
        this.datafile.markDirty();
    }


    public boolean checkHasAsteroids()
    {
        if (this.dataNotLoaded)
        {
            this.loadAsteroidSavedData();
        }

        return this.asteroids.size() != 0;
    }

    public BlockVec3 getClosestAsteroidXZ(int x, int y, int z, boolean mark)
    {
        if (!this.checkHasAsteroids())
        {
            return null;
        }

        BlockVec3 result = null;
        AsteroidData resultRoid = null;
        int lowestDistance = Integer.MAX_VALUE;

        for (AsteroidData test : this.asteroids)
        {
            if (mark && (test.sizeAndLandedFlag & 128) > 0)
            {
                continue;
            }

            int dx = x - test.centre.x;
            int dz = z - test.centre.z;
            int a = dx * dx + dz * dz;
            if (a < lowestDistance)
            {
                lowestDistance = a;
                result = test.centre;
                resultRoid = test;
            }
        }

        if (result == null)
        {
            return null;
        }

        if (mark)
        {
            resultRoid.sizeAndLandedFlag |= 128;
            this.writeToNBT(this.datafile.datacompound);
        }
        result = result.clone();
        result.sideDoneBits = resultRoid.sizeAndLandedFlag & 127;
        return result;
    }

    public ArrayList<BlockVec3> getClosestAsteroidsXZ(int x, int y, int z, int facing, int count)
    {
        if (!this.checkHasAsteroids())
        {
            return null;
        }

        TreeMap<Integer, BlockVec3> targets = new TreeMap<>();

        for (AsteroidData roid : this.asteroids)
        {
            BlockVec3 test = roid.centre;
            switch (facing)
            {
            case 2:
                if (z - 16 < test.z)
                {
                    continue;
                }
                break;
            case 3:
                if (z + 16 > test.z)
                {
                    continue;
                }
                break;
            case 4:
                if (x - 16 < test.x)
                {
                    continue;
                }
                break;
            case 5:
                if (x + 16 > test.x)
                {
                    continue;
                }
                break;
            }
            int dx = x - test.x;
            int dz = z - test.z;
            int a = dx * dx + dz * dz;
            if (a < 262144)
            {
                targets.put(a, test);
            }
        }

        int max = Math.max(count, targets.size());
        if (max <= 0)
        {
            return null;
        }

        ArrayList<BlockVec3> returnValues = new ArrayList<>();
        int i = 0;
        int offset = EntityAstroMiner.MINE_LENGTH_AST / 2;
        for (BlockVec3 target : targets.values())
        {
            BlockVec3 coords = target.clone();
            GCLog.debug("Found nearby asteroid at " + target.toString());
            switch (facing)
            {
            case 2:
                coords.z += offset;
                break;
            case 3:
                coords.z -= offset;
                break;
            case 4:
                coords.x += offset;
                break;
            case 5:
                coords.x -= offset;
                break;
            }
            returnValues.add(coords);
            if (++i >= count)
            {
                break;
            }
        }

        return returnValues;
    }

    @Override
    public int getActualHeight()
    {
        return 256;
    }

    @Override
    public double getSolarEnergyMultiplier()
    {
        if (this.solarMultiplier < 0D)
        {
            double s = this.getSolarSize();
            this.solarMultiplier = s * s * s * ConfigManagerCore.spaceStationEnergyScalar;
        }
        return this.solarMultiplier;
    }

    private static class AsteroidData
    {
        protected BlockVec3 centre;
        protected int sizeAndLandedFlag = 15;
        protected int coreAndSpawnedFlag = -2;

        public AsteroidData(int x, int y, int z)
        {
            this.centre = new BlockVec3(x, y, z);
        }

        public AsteroidData(int x, int y, int z, int size, int core)
        {
            this.centre = new BlockVec3(x, y, z);
            this.sizeAndLandedFlag = size;
            this.coreAndSpawnedFlag = core;
        }

        public AsteroidData(BlockVec3 bv)
        {
            this.centre = bv;
        }

        @Override
        public int hashCode()
        {
            if (this.centre != null)
            {
                return this.centre.hashCode();
            }
            else
            {
                return 0;
            }
        }

        @Override
        public boolean equals(Object o)
        {
            if (o instanceof AsteroidData)
            {
                BlockVec3 vector = ((AsteroidData) o).centre;
                return this.centre.x == vector.x && this.centre.y == vector.y && this.centre.z == vector.z;
            }

            if (o instanceof BlockVec3)
            {
                BlockVec3 vector = (BlockVec3) o;
                return this.centre.x == vector.x && this.centre.y == vector.y && this.centre.z == vector.z;
            }

            return false;
        }

        public CompoundNBT writeToNBT(CompoundNBT tag)
        {
            tag.putInt("x", this.centre.x);
            tag.putInt("y", this.centre.y);
            tag.putInt("z", this.centre.z);
            tag.putInt("coreAndFlag", this.coreAndSpawnedFlag);
            tag.putInt("sizeAndFlag", this.sizeAndLandedFlag);
            return tag;
        }

        public static AsteroidData readFromNBT(CompoundNBT tag)
        {
            BlockVec3 tempVector = new BlockVec3();
            tempVector.x = tag.getInt("x");
            tempVector.y = tag.getInt("y");
            tempVector.z = tag.getInt("z");

            AsteroidData roid = new AsteroidData(tempVector);
            if (tag.contains("coreAndFlag"))
            {
                roid.coreAndSpawnedFlag = tag.getInt("coreAndFlag");
            }
            if (tag.contains("sizeAndFlag"))
            {
                roid.sizeAndLandedFlag = tag.getInt("sizeAndFlag");
            }

            return roid;
        }
    }

    @Override
    public int getDungeonSpacing()
    {
        return 576;
        //Used for generating Abandoned Base 
    }

//    @Override
//    public DimensionType getDimensionType()
//    {
//        return GCPlanetDimensions.ASTEROIDS;
//    }

    @Override
    public float getArrowGravity()
    {
        return 0.002F;
    }

    @Override
    public ResourceLocation getDungeonChestType()
    {
        return RoomTreasure.MOONCHEST;
    }

    @Override
    public boolean hasSkyLight()
    {
        return false;
    }

    @Override
    public List<Block> getSurfaceBlocks()
    {
        return null;
    }
}
