package micdoodle8.mods.galacticraft.core.world.gen;


/**
 * GCMoonBiomeGenFlat.java
 * 
 * This file is part of the Galacticraft project
 * 
 * @author micdoodle8
 * @license Lesser GNU Public License v3 (http://www.gnu.org/licenses/lgpl.html)
 * 
 */
public class BiomeGenFlatMoon extends BiomeGenBaseMoon
{
	public BiomeGenFlatMoon(int par1)
	{
		super(par1);
		this.setBiomeName("moonFlat");
		this.setColor(11111111);
		this.setHeight(new Height(1.5F, 0.4F));
	}
}
