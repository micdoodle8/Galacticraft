package micdoodle8.mods.galacticraft.core.wrappers;

import micdoodle8.mods.galacticraft.api.vector.Vector3;

public class Footprint
{
    public static final short MAX_AGE = 3200;
    public final int dimension;
    public final float rotation;
    public final Vector3 position;
    public short age;
    public final String owner;
    public int lightmapVal;

    public Footprint(int dimension, Vector3 position, float rotation, String ownerUUID, int lightmapVal)
    {
        this(dimension, position, rotation, (short) 0, ownerUUID, lightmapVal);
    }

    public Footprint(int dimension, Vector3 position, float rotation, short age, String ownerUUID, int lightmapVal)
    {
        this.dimension = dimension;
        this.position = position;
        this.rotation = rotation;
        this.age = age;
        this.owner = ownerUUID;
        this.lightmapVal = lightmapVal;
    }
}