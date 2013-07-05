package micdoodle8.mods.galacticraft.API.world;

import micdoodle8.mods.galacticraft.API.recipe.SpaceStationRecipe;

public class SpaceStationType
{
    private final int spaceStationID;
    private final String planetToOrbit;
    private final int planetID;
    private final SpaceStationRecipe recipe;

    public SpaceStationType(int spaceStationID, String planetToOrbit, int planetID, SpaceStationRecipe recipe)
    {
        this.spaceStationID = spaceStationID;
        this.planetToOrbit = planetToOrbit;
        this.planetID = planetID;
        this.recipe = recipe;
    }

    public int getSpaceStationID()
    {
        return this.spaceStationID;
    }

    public String getWorldToOrbit()
    {
        return this.planetToOrbit;
    }

    public int getWorldToOrbitID()
    {
        return this.planetID;
    }

    public SpaceStationRecipe getRecipeForSpaceStation()
    {
        return this.recipe;
    }
}
