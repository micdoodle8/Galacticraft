package micdoodle8.mods.galacticraft.mars.world.gen;

/**
 * GCMarsBiomeGenFlat.java
 * 
 * This file is part of the Galacticraft project
 * 
 * @author micdoodle8
 * @license Lesser GNU Public License v3 (http://www.gnu.org/licenses/lgpl.html)
 * 
 */
public class GCMarsBiomeGenFlat extends GCMarsBiomeGenBase
{
	public GCMarsBiomeGenFlat(int par1)
	{
		super(par1);
		this.setBiomeName("marsFlat");
		this.setColor(16711680);
		this.setHeight(new Height(2.5F, 0.4F));
	}
}
