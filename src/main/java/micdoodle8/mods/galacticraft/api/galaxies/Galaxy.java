package micdoodle8.mods.galacticraft.api.galaxies;

import java.util.Locale;

import micdoodle8.mods.galacticraft.api.vector.Vector3;
import net.minecraft.util.StatCollector;

public class Galaxy 
{
    protected final String galaxyName;
    protected String unlocalizedName;
    protected Vector3 mapPosition = null;

    public Galaxy(String galaxyName)
    {
        this.galaxyName = galaxyName.toLowerCase(Locale.ENGLISH);
        this.unlocalizedName = galaxyName;
    }
    
    public String getName()
    {
    	return this.galaxyName;
    }

    public final int getID()
    {
        return GalaxyRegistry.getGalaxyID(this.galaxyName);
    }
    
    public String getLocalizedName()
    {
        String s = this.getUnlocalizedName();
        return s == null ? "" : StatCollector.translateToLocal(s);
    }
    
    public String getUnlocalizedName()
    {
        return "galaxy." + this.unlocalizedName;
    }
    
    public Vector3 getMapPosition()
    {
    	return this.mapPosition;
    }
    
    public Galaxy setMapPosition(Vector3 mapPosition)
    {
    	this.mapPosition = mapPosition;
    	return this;
    }
}
