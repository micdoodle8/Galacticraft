package micdoodle8.mods.galacticraft.moon.world.gen;

/**
 * GCMoonBiomeGenFlat.java
 * 
 * This file is part of the Galacticraft project
 * 
 * @author micdoodle8
 * @license Lesser GNU Public License v3 (http://www.gnu.org/licenses/lgpl.html)
 * 
 */
public class GCMoonBiomeGenFlat extends GCMoonBiomeGenBase
{
	public GCMoonBiomeGenFlat(int par1)
	{
		super(par1);
		this.setBiomeName("moonFlat");
		this.setColor(11111111);
		this.minHeight = 1.5F;
		this.maxHeight = 0.4F;
	}
}
