package micdoodle8.mods.galacticraft.planets.asteroids.world.gen;

import micdoodle8.mods.galacticraft.core.entities.EntityEvolvedSpider;
import micdoodle8.mods.galacticraft.core.entities.EntityEvolvedZombie;
import micdoodle8.mods.galacticraft.core.util.ConfigManagerCore;
import net.minecraft.world.biome.BiomeGenBase;

public class BiomeGenBaseAsteroids extends BiomeGenBase
{
    public static final BiomeGenBase asteroid = new BiomeGenBaseAsteroids(ConfigManagerCore.biomeIDbase + 2).setBiomeName("asteroids");

    @SuppressWarnings("unchecked")
    private BiomeGenBaseAsteroids(int var1)
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
    public BiomeGenBaseAsteroids setColor(int var1)
    {
        return (BiomeGenBaseAsteroids) super.setColor(var1);
    }

    @Override
    public float getSpawningChance()
    {
        return 0.01F;
    }
}
