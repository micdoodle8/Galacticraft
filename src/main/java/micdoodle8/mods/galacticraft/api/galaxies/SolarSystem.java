package micdoodle8.mods.galacticraft.api.galaxies;

import micdoodle8.mods.galacticraft.api.vector.Vector3;
import net.minecraft.util.StatCollector;

import java.util.Locale;

public class SolarSystem
{
    protected final String systemName;
    protected String unlocalizedName;
    protected Vector3 mapPosition = null;
    protected Star mainStar = null;
    protected String unlocalizedGalaxyName;

    public SolarSystem(String solarSystem, String parentGalaxy)
    {
        this.systemName = solarSystem.toLowerCase(Locale.ENGLISH);
        this.unlocalizedName = solarSystem;
        this.unlocalizedGalaxyName = parentGalaxy;
    }

    public String getName()
    {
        return this.systemName;
    }

    public final int getID()
    {
        return GalaxyRegistry.getSolarSystemID(this.systemName);
    }

    public String getLocalizedName()
    {
        String s = this.getUnlocalizedName();
        return s == null ? "" : StatCollector.translateToLocal(s);
    }

    public String getUnlocalizedName()
    {
        return "solarsystem." + this.unlocalizedName;
    }

    public Vector3 getMapPosition()
    {
        return this.mapPosition;
    }

    public SolarSystem setMapPosition(Vector3 mapPosition)
    {
    	mapPosition.scale(500D);
        this.mapPosition = mapPosition;
        return this;
    }

    public Star getMainStar()
    {
        return this.mainStar;
    }

    public SolarSystem setMainStar(Star star)
    {
        this.mainStar = star;
        return this;
    }

    public String getLocalizedParentGalaxyName()
    {
        String s = this.getUnlocalizedParentGalaxyName();
        return s == null ? "" : StatCollector.translateToLocal(s);
    }

    public String getUnlocalizedParentGalaxyName()
    {
        return "galaxy." + this.unlocalizedGalaxyName;
    }
}
