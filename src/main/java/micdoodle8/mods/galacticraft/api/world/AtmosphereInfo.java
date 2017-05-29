package micdoodle8.mods.galacticraft.api.world;

import java.util.ArrayList;

public class AtmosphereInfo
{
    private final Boolean isBreathable;
    private final boolean hasPrecipitation;
    private final boolean isCorrosive;
    private final float thermalLevel;
    private final float windLevel;
    private final float density;
    public ArrayList<EnumAtmosphericGas> composition = new ArrayList<>();
    
    /**
     * @param breathable - supply null here to have the AtmosphereInfo object test the atmospheric composition for oxygen and CO2, or true or false to override
     * @param precipitation - true for rain etc, false for none
     * @param corrosive - true for atmosphere which can corrode armor
     * @param relativeTemperature - the thermal level relative to Overworld: 0.0F is default
     * @param windLevel - the wind level
     * @param density - the atmospheric density (affects sounds and meteor frequency): 1.0F is like Overworld
     */
    public AtmosphereInfo(Boolean breathable, boolean precipitation, boolean corrosive, float relativeTemperature, float windLevel, float density)
    {    
        this.isBreathable = breathable;
        this.hasPrecipitation = precipitation;
        this.isCorrosive = corrosive;
        this.thermalLevel = relativeTemperature;
        this.windLevel = windLevel;
        this.density = density;
    }
    
    public boolean isBreathable()
    {
        if (this.isBreathable == null)
        {
            return this.isGasPresent(EnumAtmosphericGas.OXYGEN) && !this.isGasPresent(EnumAtmosphericGas.CO2);
        }
        return this.isBreathable;
    }
    
    public boolean hasPrecipitation()
    {
        return this.hasPrecipitation;
    }

    public boolean isCorrosive()
    {
        return this.isCorrosive;
    }

    public float thermalLevel()
    {
        return this.thermalLevel;
    }

    public float windLevel()
    {
        return this.windLevel;
    }

    public float relativeDensity()
    {
        return this.density;
    }

    public boolean isGasPresent(EnumAtmosphericGas gas)
    {
        return this.composition.contains(gas);
    }

    public boolean hasNoGases()
    {
        return this.composition.isEmpty();
    }
}
