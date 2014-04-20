package micdoodle8.mods.galacticraft.planets.mars.world.gen;

/**
 * GCMarsBiomeGenFlat.java
 * 
 * This file is part of the Galacticraft project
 * 
 * @author micdoodle8
 * @license Lesser GNU Public License v3 (http://www.gnu.org/licenses/lgpl.html)
 * 
 */
public class BiomeGenFlagMars extends BiomeGenBaseMars
{
	public BiomeGenFlagMars(int par1)
	{
		super(par1);
		this.setBiomeName("marsFlat");
		this.setColor(16711680);
		this.setHeight(new Height(2.5F, 0.4F));
	}
}
