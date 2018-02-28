package micdoodle8.mods.galacticraft.planets.asteroids.world.gen;

import micdoodle8.mods.galacticraft.core.entities.*;
import micdoodle8.mods.galacticraft.core.util.ConfigManagerCore;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraftforge.common.BiomeDictionary;

public class BiomeGenBaseAsteroids extends BiomeGenBase
{
    public static final BiomeGenBase asteroid = new BiomeGenBaseAsteroids(ConfigManagerCore.biomeIDbase + 2).setBiomeName("Asteroids");

    private BiomeGenBaseAsteroids(int var1)
    {
        super(var1);
        this.spawnableWaterCreatureList.clear();
        this.spawnableCreatureList.clear();
        this.spawnableCaveCreatureList.clear();
        this.resetMonsterListByMode(ConfigManagerCore.challengeMobDropsAndSpawning);
        this.rainfall = 0F;
        if (!ConfigManagerCore.disableBiomeTypeRegistrations)
        {
            BiomeDictionary.registerBiomeType(this, BiomeDictionary.Type.COLD, BiomeDictionary.Type.DRY, BiomeDictionary.Type.DEAD, BiomeDictionary.Type.SPOOKY);
        }
    }

    public void resetMonsterListByMode(boolean challengeMode)
    {
        this.spawnableMonsterList.clear();
        this.spawnableMonsterList.add(new SpawnListEntry(EntityEvolvedZombie.class, 3000, 1, 3));
        this.spawnableMonsterList.add(new SpawnListEntry(EntityEvolvedSpider.class, 2000, 1, 2));
        this.spawnableMonsterList.add(new SpawnListEntry(EntityEvolvedSkeleton.class, 1500, 1, 1));
        this.spawnableMonsterList.add(new SpawnListEntry(EntityEvolvedCreeper.class, 2000, 1, 1));
        if (challengeMode) this.spawnableMonsterList.add(new SpawnListEntry(EntityEvolvedEnderman.class, 250, 1, 1));
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
