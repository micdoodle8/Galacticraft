package micdoodle8.mods.galacticraft.api.world;

import micdoodle8.mods.galacticraft.api.recipe.SpaceStationRecipe;
import net.minecraft.world.dimension.DimensionType;

/**
 * Currently only used internally, not guaranteed to work.
 */
public class SpaceStationType
{
    //    private final int spaceStationID;
    private final DimensionType planetID;
    private SpaceStationRecipe recipe;

    /**
     * The planet ID is the home planet.  Only one SpaceStationType is allowed per home planet.
     * The dimension number for the planet ID should match what it will be on the CLIENT
     * even if the server has assigned different dimension IDs.  For example, for the Overworld
     * the planet ID should be 0 (it should be 0, even if the server has set ConfigManagerCore.idDimensionOverworld.get()
     * to a different value than 0).
     */
    public SpaceStationType(/*int spaceStationID, */DimensionType planetID, SpaceStationRecipe recipe)
    {
//        this.spaceStationID = spaceStationID;
        this.planetID = planetID;
        this.recipe = recipe;
    }

    /**
     * Dimension ID of the space station
     */
//    public int getSpaceStationID()
//    {
//        return this.spaceStationID;
//    }

    /**
     * Dimension ID of the planet this space station is orbiting
     */
    public DimensionType getWorldToOrbitID()
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

    public void setRecipeForSpaceStation(SpaceStationRecipe newRecipe)
    {
        this.recipe = newRecipe;
    }
}
