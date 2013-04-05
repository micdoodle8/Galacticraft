package micdoodle8.mods.galacticraft.API;

import java.util.HashSet;

import net.minecraft.entity.Entity;

public interface ISpaceship
{
	public Entity[] getSpaceshipParts();

	public HashSet<Integer> getPossiblePlanets();

	public int getPreLaunchWait();
}