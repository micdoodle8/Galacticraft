package micdoodle8.mods.galacticraft.planets.asteroids.world.gen;

import micdoodle8.mods.galacticraft.core.entities.EntityEvolvedCreeper;
import micdoodle8.mods.galacticraft.core.entities.EntityEvolvedSkeleton;
import micdoodle8.mods.galacticraft.core.entities.EntityEvolvedSpider;
import micdoodle8.mods.galacticraft.core.entities.EntityEvolvedZombie;
import micdoodle8.mods.galacticraft.core.util.ConfigManagerCore;
import net.minecraft.entity.monster.EntityEnderman;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.BiomeDictionary;

public class BiomeAsteroids extends Biome
{
    public static final Biome asteroid = new BiomeAsteroids(new BiomeProperties("Asteroids").setRainfall(0.0F));

    @SuppressWarnings("unchecked")
    private BiomeAsteroids(BiomeProperties properties)
    {
        super(properties);
        this.spawnableMonsterList.clear();
        this.spawnableWaterCreatureList.clear();
        this.spawnableCreatureList.clear();
        this.spawnableCaveCreatureList.clear();
        spawnableMonsterList.add(new SpawnListEntry(EntityEvolvedZombie.class, 3000, 1, 3));
        spawnableMonsterList.add(new SpawnListEntry(EntityEvolvedSpider.class, 2000, 1, 2));
        spawnableMonsterList.add(new SpawnListEntry(EntityEvolvedSkeleton.class, 1500, 1, 1));
        spawnableMonsterList.add(new SpawnListEntry(EntityEvolvedCreeper.class, 2000, 1, 1));
        if (ConfigManagerCore.challengeMode || ConfigManagerCore.challengeMobDropsAndSpawning)
        {
            spawnableMonsterList.add(new SpawnListEntry(EntityEnderman.class, 250, 1, 1));
        }
        if (!ConfigManagerCore.disableBiomeTypeRegistrations)
        {
            BiomeDictionary.registerBiomeType(this, BiomeDictionary.Type.COLD, BiomeDictionary.Type.DRY, BiomeDictionary.Type.DEAD, BiomeDictionary.Type.SPOOKY);
        }
    }

    @Override
    public float getSpawningChance()
    {
        return 0.01F;
    }
}
