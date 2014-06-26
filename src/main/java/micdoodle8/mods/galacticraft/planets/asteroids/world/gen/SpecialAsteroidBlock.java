package micdoodle8.mods.galacticraft.planets.asteroids.world.gen;

import net.minecraft.block.Block;

public class SpecialAsteroidBlock
{

	public Block block;
	public byte meta;
	public int probability;
	public double thickness; //Arbitrary scale from 0 to 1;

	public SpecialAsteroidBlock(Block block, byte meta, int probability, double thickness)
	{
		this.block = block;
		this.meta = meta;
		this.probability = probability;
		this.thickness = thickness;
	}

}