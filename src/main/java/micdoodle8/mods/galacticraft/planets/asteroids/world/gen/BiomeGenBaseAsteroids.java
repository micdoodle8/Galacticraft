package micdoodle8.mods.galacticraft.planets.asteroids.world.gen;

import micdoodle8.mods.galacticraft.core.entities.*;
import micdoodle8.mods.galacticraft.core.util.ConfigManagerCore;
import net.minecraft.entity.monster.EntityEnderman;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.biome.BiomeGenBase.SpawnListEntry;
import net.minecraftforge.common.BiomeDictionary;

public class BiomeGenBaseAsteroids extends BiomeGenBase
{
    public static final BiomeGenBase asteroid = new BiomeGenBaseAsteroids(ConfigManagerCore.biomeIDbase + 2).setBiomeName("Asteroids");

    @SuppressWarnings("unchecked")
    private BiomeGenBaseAsteroids(int var1)
    {
        super(var1);
        this.spawnableMonsterList.clear();
        this.spawnableWaterCreatureList.clear();
        this.spawnableCreatureList.clear();
        this.spawnableCaveCreatureList.clear();
        spawnableMonsterList.add(new SpawnListEntry(EntityEvolvedZombie.class, 3000, 1, 3));
        spawnableMonsterList.add(new SpawnListEntry(EntityEvolvedSpider.class, 2000, 1, 2));
        spawnableMonsterList.add(new SpawnListEntry(EntityEvolvedSkeleton.class, 1500, 1, 1));
        spawnableMonsterList.add(new SpawnListEntry(EntityEvolvedCreeper.class, 2000, 1, 1));
        spawnableMonsterList.add(new SpawnListEntry(EntityEvolvedWitch.class, 5, 1, 1));
        if (ConfigManagerCore.challengeMode || ConfigManagerCore.challengeMobDropsAndSpawning)
        {
            spawnableMonsterList.add(new SpawnListEntry(EntityEvolvedEnderman.class, 250, 1, 1));
        }
        this.rainfall = 0F;
        if (!ConfigManagerCore.disableBiomeTypeRegistrations)
        {
            BiomeDictionary.registerBiomeType(this, BiomeDictionary.Type.COLD, BiomeDictionary.Type.DRY, BiomeDictionary.Type.DEAD, BiomeDictionary.Type.SPOOKY);
        }
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
