package micdoodle8.mods.galacticraft.planets.asteroids.dimension;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import micdoodle8.mods.galacticraft.api.galaxies.CelestialBody;
import micdoodle8.mods.galacticraft.api.prefab.world.gen.WorldProviderSpace;
import micdoodle8.mods.galacticraft.api.vector.BlockVec3;
import micdoodle8.mods.galacticraft.api.vector.Vector3;
import micdoodle8.mods.galacticraft.api.world.ISolarLevel;
import micdoodle8.mods.galacticraft.core.util.ConfigManagerCore;
import micdoodle8.mods.galacticraft.planets.asteroids.AsteroidsModule;
import micdoodle8.mods.galacticraft.planets.asteroids.entities.EntityAstroMiner;
import micdoodle8.mods.galacticraft.planets.asteroids.world.gen.ChunkProviderAsteroids;
import micdoodle8.mods.galacticraft.planets.asteroids.world.gen.WorldChunkManagerAsteroids;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.world.biome.WorldChunkManager;
import net.minecraft.world.chunk.IChunkProvider;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.TreeMap;

public class WorldProviderAsteroids extends WorldProviderSpace implements ISolarLevel
{
    //Used to list asteroid centres to external code that needs to know them
    private HashSet<BlockVec3> asteroidCentres = new HashSet();
    private boolean dataNotLoaded = true;
    private AsteroidSaveData datafile;
	private double solarMultiplier = -1D;

    //	@Override
//	public void registerWorldChunkManager()
//	{
//		this.worldChunkMgr = new WorldChunkManagerAsteroids(this.worldObj, 0F);
//	}

    @Override
    public CelestialBody getCelestialBody()
    {
        return AsteroidsModule.planetAsteroids;
    }

    @Override
    public Vector3 getFogColor()
    {
        return new Vector3(0, 0, 0);
    }

    @Override
    public Vector3 getSkyColor()
    {
        return new Vector3(0, 0, 0);
    }

    @Override
    public boolean canRainOrSnow()
    {
        return false;
    }

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

    public boolean isDaytime()
    {
        return true;
    }

    @Override
    public Class<? extends IChunkProvider> getChunkProviderClass()
    {
        return ChunkProviderAsteroids.class;
    }

    @Override
    public Class<? extends WorldChunkManager> getWorldChunkManagerClass()
    {
        return WorldChunkManagerAsteroids.class;
    }

    @Override
    public boolean shouldForceRespawn()
    {
        return !ConfigManagerCore.forceOverworldRespawn;
    }

    @Override
    public float calculateCelestialAngle(long par1, float par3)
    {
        return 0.25F;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public float getStarBrightness(float par1)
    {
        return 1.0F;
    }

//	@Override
//	public IChunkProvider createChunkGenerator()
//	{
//		return new ChunkProviderAsteroids(this.worldObj, this.worldObj.getSeed(), this.worldObj.getWorldInfo().isMapFeaturesEnabled());
//	}

    @Override
    public double getHorizon()
    {
        return 44.0D;
    }

    @Override
    public int getAverageGroundLevel()
    {
        return 44;
    }

    @Override
    public boolean canCoordinateBeSpawn(int var1, int var2)
    {
        return true;
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

    @Override
    public float getSoundVolReductionAmount()
    {
        return 10.0F;
    }

    @Override
    public boolean hasBreathableAtmosphere()
    {
        return false;
    }

    @Override
    public float getThermalLevelModifier()
    {
        return -1.5F;
    }

    public void addAsteroid(int x, int y, int z)
    {
        BlockVec3 coords = new BlockVec3(x, y, z);
        if (!this.asteroidCentres.contains(coords))
        {
            if (this.dataNotLoaded)
            {
                this.loadAsteroidSavedData();
            }
            if (!this.asteroidCentres.contains(coords))
            {
                this.addToNBT(this.datafile.datacompound, coords);
                this.asteroidCentres.add(coords);
            }
        }
    }

    public void removeAsteroid(int x, int y, int z)
    {
        BlockVec3 coords = new BlockVec3(x, y, z);
        if (this.asteroidCentres.contains(coords))
        {
            this.asteroidCentres.remove(coords);

            if (this.dataNotLoaded)
            {
                this.loadAsteroidSavedData();
            }
            this.writeToNBT(this.datafile.datacompound);
        }
    }

    private void loadAsteroidSavedData()
    {
        this.datafile = (AsteroidSaveData) this.worldObj.loadItemData(AsteroidSaveData.class, AsteroidSaveData.saveDataID);

        if (this.datafile == null)
        {
            this.datafile = new AsteroidSaveData("");
            this.worldObj.setItemData(AsteroidSaveData.saveDataID, this.datafile);
            this.writeToNBT(this.datafile.datacompound);
        }
        else
        {
            this.readFromNBT(this.datafile.datacompound);
        }

        this.dataNotLoaded = false;
    }

    private void readFromNBT(NBTTagCompound nbt)
    {
        NBTTagList coordList = nbt.getTagList("coords", 10);
        if (coordList.tagCount() > 0)
        {
            for (int j = 0; j < coordList.tagCount(); j++)
            {
                NBTTagCompound tag1 = coordList.getCompoundTagAt(j);

                if (tag1 != null)
                {
                    this.asteroidCentres.add(BlockVec3.readFromNBT(tag1));
                }
            }
        }
    }

    private void writeToNBT(NBTTagCompound nbt)
    {
        NBTTagList coordList = new NBTTagList();
        for (BlockVec3 coords : this.asteroidCentres)
        {
            NBTTagCompound tag = new NBTTagCompound();
            coords.writeToNBT(tag);
            coordList.appendTag(tag);
        }
        nbt.setTag("coords", coordList);
        this.datafile.markDirty();
    }

    private void addToNBT(NBTTagCompound nbt, BlockVec3 coords)
    {
        NBTTagList coordList = nbt.getTagList("coords", 10);
        NBTTagCompound tag = new NBTTagCompound();
        coords.writeToNBT(tag);
        coordList.appendTag(tag);
        nbt.setTag("coords", coordList);
        this.datafile.markDirty();
    }

    public BlockVec3 getClosestAsteroidXZ(int x, int y, int z)
    {
        if (this.dataNotLoaded)
        {
            this.loadAsteroidSavedData();
        }

        if (this.asteroidCentres.size() == 0)
        {
            return null;
        }

        BlockVec3 result = null;
        int lowestDistance = Integer.MAX_VALUE;

        for (BlockVec3 test : this.asteroidCentres)
        {
            int dx = x - test.x;
            int dz = z - test.z;
            int a = dx * dx + dz * dz;
            if (a < lowestDistance)
            {
                lowestDistance = a;
                result = test;
            }
        }

        return result.clone();
    }

    public ArrayList<BlockVec3> getClosestAsteroidsXZ(int x, int y, int z, int facing, int count)
    {
        if (this.dataNotLoaded)
        {
            this.loadAsteroidSavedData();
        }

        if (this.asteroidCentres.size() == 0)
        {
            return null;
        }

        TreeMap<Integer, BlockVec3> targets = new TreeMap();
        
        for (BlockVec3 test : this.asteroidCentres)
        {
            switch (facing)
            {
            case 2:
            	if (z - 16 < test.z)
            		continue;
            	break;
            case 3:
            	if (z + 16 > test.z)
            		continue;
            	break;
            case 4:
            	if (x - 16 < test.x)
            		continue;
            	break;
            case 5:
            	if (x + 16 > test.x)
            		continue;
            	break;
            }
        	int dx = x - test.x;
            int dz = z - test.z;
            int a = dx * dx + dz * dz;
            if (a < 262144) targets.put(a, test);
        }

        int max = Math.max(count,  targets.size());
        if (max <= 0) return null;
        
        ArrayList<BlockVec3> returnValues = new ArrayList();
        int i = 0;
        int offset = EntityAstroMiner.MINE_LENGTH_AST / 2;
        for (BlockVec3 target : targets.values())
        {
        	BlockVec3 coords = target.clone();
        	System.out.println("Found nearby asteroid at "+ target.toString());
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
        	if (++i >= count) break; 
        }
        
        return returnValues;
    }

    
    @Override
    public float getWindLevel()
    {
        return 0.05F;
    }
    
    @Override
    public int getActualHeight()
    {
        return 256;
    }

    @Override
    public void registerWorldChunkManager()
    {
        super.registerWorldChunkManager();
        this.hasNoSky = true;
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
}
