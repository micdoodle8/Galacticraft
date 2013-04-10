package micdoodle8.mods.galacticraft.API;

import net.minecraft.world.WorldProvider;

public class SpaceStationType 
{
	private int spaceStationID;
	private String planetToOrbit;
	private int planetID;
	private SpaceStationRecipe recipe;
	
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
