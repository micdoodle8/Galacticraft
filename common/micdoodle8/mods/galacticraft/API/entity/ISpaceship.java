package micdoodle8.mods.galacticraft.api.entity;

import java.util.HashSet;

public interface ISpaceship
{
    public HashSet<Integer> getPossiblePlanets();

    public int getPreLaunchWait();
}
