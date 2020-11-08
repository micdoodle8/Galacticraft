package micdoodle8.mods.galacticraft.api.galaxies;

import com.google.common.collect.Maps;
import micdoodle8.mods.galacticraft.api.world.*;
import net.minecraft.block.Block;
import net.minecraft.entity.EntityClassification;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.LanguageMap;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.Biome.SpawnListEntry;
import net.minecraft.world.dimension.Dimension;
import net.minecraft.world.dimension.DimensionType;
import net.minecraftforge.registries.IForgeRegistry;
import org.apache.commons.lang3.builder.EqualsBuilder;

import javax.annotation.Nullable;
import java.util.*;

public abstract class CelestialBody implements Comparable<CelestialBody>
{
    protected final String bodyName;
    protected String unlocalizedName;

    protected float relativeSize = 1.0F;
    protected ScalableDistance relativeDistanceFromCenter = new ScalableDistance(1.0F, 1.0F);
    protected float relativeOrbitTime = 1.0F;
    protected float phaseShift = 0.0F;
    protected DimensionType dimensionType = null;
    protected Class<? extends Dimension> providerClass;
    protected String dimensionSuffix;
    protected boolean autoRegisterDimension = false;
    protected boolean isReachable = false;
    protected boolean forceStaticLoad = true;
    protected int tierRequired = 0;

    public AtmosphereInfo atmosphere = new AtmosphereInfo(false, false, false, 0.0F, 0.0F, 1.0F);
    protected LinkedList<Biome> biomeInfo;
    public LinkedList<Biome> biomesToGenerate;
    public BiomeGC[] biomesToAdapt;
    protected Map<SpawnListEntry, EntityClassification> mobInfo;

    protected ResourceLocation celestialBodyIcon;

    protected float ringColorR = 1.0F;
    protected float ringColorG = 1.0F;
    protected float ringColorB = 1.0F;

    protected ArrayList<String> checklistKeys = new ArrayList<>();

    private List<Block> surfaceBlocks = null;

    public CelestialBody(String bodyName)
    {
        this.bodyName = bodyName.toLowerCase(Locale.ENGLISH);
        this.unlocalizedName = bodyName;
    }

    public abstract int getID();

    public abstract String getUnlocalizedNamePrefix();

    public String getName()
    {
        return this.bodyName;
    }

    public String getUnlocalizedName()
    {
        return this.getUnlocalizedNamePrefix() + "." + this.unlocalizedName;
    }

    public String getLocalizedName()
    {
        String s = this.getUnlocalizedName();
        s = s == null ? "" : LanguageMap.getInstance().translateKey(s);
        int comment = s.indexOf('#');
        return (comment > 0) ? s.substring(0, comment).trim() : s;
    }

    /**
     * Used for rendering planet's location on the map.
     * <p/>
     * Value of 2.0F would result in the planet being rendered twice as large as
     * earth.
     *
     * @return Size of the planet/moon relative to earth.
     */
    public float getRelativeSize()
    {
        return this.relativeSize;
    }

    /**
     * Used for rendering planet's location on the map.
     * <p/>
     * Value of 2.0F would result in an ellipse with twice the radius of the
     * overworld.
     *
     * @return Distance from the center of the map relative to earth.
     */
    public ScalableDistance getRelativeDistanceFromCenter()
    {
        return this.relativeDistanceFromCenter;
    }

    /**
     * Used for rendering planet's location on the map.
     * <p/>
     * Value of 1π would result in the planet being rendered directly
     * accross from the original position
     * <p/>
     * Value of 2π is a full rotation and therefore would be rendered at
     * the same spot as the original position
     *
     * @return Phase shift of planet for planet's revolution around the sun.
     */
    public float getPhaseShift()
    {
        return this.phaseShift;
    }

    /**
     * Multiplier for length of time relative to earth that this planet takes to
     * orbit fully.
     * <p/>
     * Value of 2.0F would result in the planet rotating twice as slow (and
     * therefore take twice as long) as the earth takes to revolve around the
     * sun.
     *
     * @return Multiple value for planet's revolution around the sun.
     */
    public float getRelativeOrbitTime()
    {
        return this.relativeOrbitTime;
    }

    public int getTierRequirement()
    {
        return this.tierRequired;
    }

    public CelestialBody setTierRequired(int tierRequired)
    {
        this.tierRequired = tierRequired;
        return this;
    }

    public CelestialBody setRelativeSize(float relativeSize)
    {
        this.relativeSize = relativeSize;
        return this;
    }

    public CelestialBody setRelativeDistanceFromCenter(ScalableDistance relativeDistanceFromCenter)
    {
        this.relativeDistanceFromCenter = relativeDistanceFromCenter;
        return this;
    }

    public CelestialBody setPhaseShift(float phaseShift)
    {
        this.phaseShift = phaseShift;
        return this;
    }

    public CelestialBody setRelativeOrbitTime(float relativeOrbitTime)
    {
        this.relativeOrbitTime = relativeOrbitTime;
        return this;
    }

    public CelestialBody setAtmosphere(AtmosphereInfo atmos)
    {
        this.atmosphere = atmos;
        return this;
    }

    public CelestialBody setDimensionInfo(DimensionType dimType, Class<? extends Dimension> providerClass)
    {
        return this.setDimensionInfo(dimType, providerClass, true);
    }

    public CelestialBody setDimensionInfo(DimensionType type, Class<? extends Dimension> providerClass, boolean autoRegister)
    {
        this.dimensionType = type;
        this.providerClass = providerClass;
        this.autoRegisterDimension = autoRegister;
        this.isReachable = true;
        return this;
    }

    public boolean shouldAutoRegister()
    {
        return this.autoRegisterDimension;
    }

    @Nullable
    public DimensionType getDimensionType()
    {
        return this.dimensionType;
    }

    public Class<? extends Dimension> getWorldProvider()
    {
        return this.providerClass;
    }

    public boolean getReachable()
    {
        return this.isReachable;
    }

    /*
     * Use this to list the atmospheric gases on the celestial body, starting with the most abundant
     * Do not include trace gases (anything less than 0.25%)
     * (Do not use for stars!)
     */
    public CelestialBody atmosphereComponent(EnumAtmosphericGas gas)
    {
        this.atmosphere.composition.add(gas);
        return this;
    }

    public CelestialBody setRingColorRGB(float ringColorR, float ringColorG, float ringColorB)
    {
        this.ringColorR = ringColorR;
        this.ringColorG = ringColorG;
        this.ringColorB = ringColorB;
        return this;
    }

    public float getRingColorR()
    {
        return this.ringColorR;
    }

    public float getRingColorG()
    {
        return this.ringColorG;
    }

    public float getRingColorB()
    {
        return this.ringColorB;
    }

    public ResourceLocation getBodyIcon()
    {
        return this.celestialBodyIcon;
    }

    public CelestialBody setBodyIcon(ResourceLocation planetIcon)
    {
        this.celestialBodyIcon = planetIcon;
        return this;
    }

    public boolean getForceStaticLoad()
    {
        return this.forceStaticLoad;
    }

    public CelestialBody setForceStaticLoad(boolean force)
    {
        this.forceStaticLoad = force;
        return this;
    }

    public void addChecklistKeys(String... keys)
    {
        this.checklistKeys.addAll(Arrays.asList(keys));
    }

    public List<String> getChecklistKeys()
    {
        return this.checklistKeys;
    }

    @Override
    public int hashCode()
    {
        return this.getUnlocalizedName().hashCode();
    }

    @Override
    public boolean equals(Object other)
    {
        if (other instanceof CelestialBody)
        {
            return new EqualsBuilder().append(this.getUnlocalizedName(), ((CelestialBody) other).getUnlocalizedName()).isEquals();
        }

        return false;
    }

    @Override
    public int compareTo(CelestialBody other)
    {
        ScalableDistance thisDistance = this.getRelativeDistanceFromCenter();
        ScalableDistance otherDistance = other.getRelativeDistanceFromCenter();
        return otherDistance.unScaledDistance < thisDistance.unScaledDistance ? 1 : (otherDistance.unScaledDistance > thisDistance.unScaledDistance ? -1 : 0);
    }

    public static class ScalableDistance
    {
        public final float unScaledDistance;
        public final float scaledDistance;

        public ScalableDistance(float unScaledDistance, float scaledDistance)
        {
            this.unScaledDistance = unScaledDistance;
            this.scaledDistance = scaledDistance;
        }
    }

    public void setUnreachable()
    {
        this.isReachable = false;
    }

    public String getDimensionSuffix()
    {
        return dimensionSuffix;
    }

    public void setDimensionSuffix(String dimensionSuffix)
    {
        this.dimensionSuffix = dimensionSuffix;
    }

    public void setBiomeInfo(IForgeRegistry<Biome> registry, Biome... biomes)
    {
        this.biomeInfo = new LinkedList<Biome>();
        this.biomesToGenerate = new LinkedList<Biome>();
        LinkedList<BiomeGC> adaptiveBiomes = new LinkedList<>();
        int index = 0;
        for (Biome b : biomes)
        {
            this.biomeInfo.add(b);
//            if (b instanceof BiomeGC && ((BiomeGC) b).isAdaptiveBiome)
//            {
////                this.biomesToGenerate.add(BiomeAdaptive.register(registry, index++, (BiomeGC) b));
//                adaptiveBiomes.add((BiomeGC) b);
//            }
//            else
            {
                this.biomesToGenerate.add(b);
            }
        }
        this.biomesToAdapt = adaptiveBiomes.toArray(new BiomeGC[adaptiveBiomes.size()]);
    }

    public List<Biome> getBiomes()
    {
        return this.biomeInfo;
    }

    public void addMobInfo(SpawnListEntry entry, EntityClassification classification)
    {
        if (this.mobInfo == null)
        {
            this.mobInfo = Maps.newHashMap();
        }
        this.mobInfo.put(entry, classification);
    }

    public void initialiseMobSpawns()
    {
        if (this.biomeInfo != null && this.mobInfo != null)
        {
            for (Biome biome : this.biomeInfo)
            {
                if (biome instanceof IMobSpawnBiome)
                {
                    ((IMobSpawnBiome) biome).initialiseMobLists(this.mobInfo);
                }
            }
        }
    }

    public List<Block> getSurfaceBlocks()
    {
        return surfaceBlocks;
    }

    public void setSurfaceBlocks(List<Block> surfaceBlocks)
    {
        this.surfaceBlocks = surfaceBlocks;
    }
}
