package micdoodle8.mods.galacticraft.mars.world.gen;

import micdoodle8.mods.galacticraft.core.entities.EntityEvolvedSpider;
import micdoodle8.mods.galacticraft.core.entities.EntityEvolvedZombie;
import net.minecraft.world.biome.BiomeGenBase;

/**
 * GCMarsBiomeGenBase.java
 * 
 * This file is part of the Galacticraft project
 * 
 * @author micdoodle8
 * @license Lesser GNU Public License v3 (http://www.gnu.org/licenses/lgpl.html)
 * 
 */
public class BiomeGenBaseMars extends BiomeGenBase
{
	public static final BiomeGenBase marsFlat = new BiomeGenBaseMars(103).setBiomeName("marsFlat").setColor(16711680).setHeight(new Height(2.5F, 0.4F));

	@SuppressWarnings("unchecked")
	public BiomeGenBaseMars(int var1)
	{
		super(var1);
		this.spawnableMonsterList.clear();
		this.spawnableWaterCreatureList.clear();
		this.spawnableCreatureList.clear();
		this.spawnableMonsterList.add(new SpawnListEntry(EntityEvolvedZombie.class, 10, 4, 4));
		this.spawnableMonsterList.add(new SpawnListEntry(EntityEvolvedSpider.class, 10, 4, 4));
		this.rainfall = 0F;
	}

	@Override
	public BiomeGenBaseMars setColor(int var1)
	{
		return (BiomeGenBaseMars) super.setColor(var1);
	}

	@Override
	public float getSpawningChance()
	{
		return 0.01F;
	}
}
