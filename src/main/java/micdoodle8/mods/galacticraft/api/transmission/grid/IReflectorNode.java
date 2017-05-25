package micdoodle8.mods.galacticraft.api.transmission.grid;

import micdoodle8.mods.galacticraft.api.vector.Vector3;

public interface IReflectorNode
{
    Vector3 getInputPoint();

    Vector3 getOutputPoint(boolean offset);
}
