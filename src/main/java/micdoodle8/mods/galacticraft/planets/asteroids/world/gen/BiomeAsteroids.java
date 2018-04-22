package micdoodle8.mods.galacticraft.planets.asteroids.world.gen;

import java.util.LinkedList;

import micdoodle8.mods.galacticraft.api.world.BiomeGenBaseGC;
import micdoodle8.mods.galacticraft.core.entities.*;
import micdoodle8.mods.galacticraft.core.util.ConfigManagerCore;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.BiomeDictionary;

public class BiomeAsteroids extends BiomeGenBaseGC
{
    public static final Biome asteroid = new BiomeAsteroids(new BiomeProperties("Asteroids").setRainfall(0.0F));

    private BiomeAsteroids(BiomeProperties properties)
    {
        super(properties, true);
        this.spawnableMonsterList.clear();
        this.spawnableWaterCreatureList.clear();
        this.spawnableCreatureList.clear();
        this.spawnableCaveCreatureList.clear();
        this.resetMonsterListByMode(ConfigManagerCore.challengeMobDropsAndSpawning);
    }

    @Override
    public void registerTypes(Biome b)
    {
        //Currently unused for Asteroids due to adaptive biomes system
        BiomeDictionary.addTypes(b, BiomeDictionary.Type.COLD, BiomeDictionary.Type.DRY, BiomeDictionary.Type.DEAD, BiomeDictionary.Type.SPOOKY);
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
    public void initialiseMobLists(LinkedList<SpawnListEntry> mobInfo)
    {
    }

    @Override
    public float getSpawningChance()
    {
        return 0.01F;
    }
}
