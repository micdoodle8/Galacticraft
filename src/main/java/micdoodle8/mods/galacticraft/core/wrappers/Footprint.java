package micdoodle8.mods.galacticraft.core.wrappers;

import micdoodle8.mods.galacticraft.api.vector.Vector3;

public class Footprint
{
	public static final short MAX_AGE = 3200;
	public float rotation;
	public Vector3 position;
	public short age;
	
	public Footprint(Vector3 position, float rotation)
	{
		this(position, rotation, (short) 0);
	}
	
	public Footprint(Vector3 position, float rotation, short age)
	{
		this.position = position;
		this.rotation = rotation;
		this.age = age;
	}
}