package micdoodle8.mods.galacticraft.core.perlin;

/**
 * NoiseModule.java
 * 
 * This file is part of the Galacticraft project
 * 
 * @author fishtaco567
 * @license Lesser GNU Public License v3 (http://www.gnu.org/licenses/lgpl.html)
 * 
 */
public abstract class NoiseModule
{
	public double frequencyX = 1;
	public double frequencyY = 1;
	public double frequencyZ = 1;
	public double amplitude = 1;

	public abstract double getNoise(double i);

	public abstract double getNoise(double i, double j);

	public abstract double getNoise(double i, double j, double k);
	
	public void setFrequency(double frequency) {
		this.frequencyX = frequency;
		this.frequencyY = frequency;
		this.frequencyZ = frequency;
	}

}
