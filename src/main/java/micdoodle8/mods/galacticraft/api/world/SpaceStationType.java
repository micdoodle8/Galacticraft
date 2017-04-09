package micdoodle8.mods.galacticraft.api.world;

import micdoodle8.mods.galacticraft.api.recipe.SpaceStationRecipe;

/**
 * Currently only used internally, not guaranteed to work.
 */
public class SpaceStationType
{
    private final int spaceStationID;
    private final int planetID;
    private final SpaceStationRecipe recipe;

    /**
     * The planet ID is the home planet.  Only one SpaceStationType is allowed per home planet.
     * The dimension number for the planet ID should match what it will be on the CLIENT
     * even if the server has assigned different dimension IDs.  For example, for the Overworld
     * the planet ID should be 0 (it should be 0, even if the server has set ConfigManagerCore.idDimensionOverworld
     * to a different value than 0).
     */
    public SpaceStationType(int spaceStationID, int planetID, SpaceStationRecipe recipe)
    {
        this.spaceStationID = spaceStationID;
        this.planetID = planetID;
        this.recipe = recipe;
    }

    /**
     * Dimension ID of the space station
     */
    public int getSpaceStationID()
    {
        return this.spaceStationID;
    }

    /**
     * Dimension ID of the planet this space station is orbiting
     */
    public int getWorldToOrbitID()
    {
        return this.planetID;
    }

    /**
     * The recipe to create this space station
     */
    public SpaceStationRecipe getRecipeForSpaceStation()
    {
        return this.recipe;
    }
}
