package micdoodle8.mods.galacticraft.API;

public interface IGalacticraftSubModClient
{
    public String getDimensionName();

    public String getPlanetSpriteDirectory();

    public IPlanetSlotRenderer getSlotRenderer();

    public IMapPlanet getPlanetForMap();

    public IMapPlanet[] getChildMapPlanets();

    /**
     * @return theme for this planet, can be null if you don't have one. MUST BE
     *         AN OGG FILE
     * 
     * @see ClientProxyMoon.getPathToMusicFile()
     */
    public String getPathToMusicFile();
}
