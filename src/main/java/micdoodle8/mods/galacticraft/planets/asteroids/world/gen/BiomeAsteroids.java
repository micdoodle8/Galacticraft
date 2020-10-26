package micdoodle8.mods.galacticraft.planets.asteroids.world.gen;

import micdoodle8.mods.galacticraft.api.world.BiomeGC;
import micdoodle8.mods.galacticraft.core.entities.GCEntities;
import micdoodle8.mods.galacticraft.core.util.ConfigManagerCore;
import net.minecraft.entity.EntityClassification;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.surfacebuilders.SurfaceBuilder;
import net.minecraftforge.common.BiomeDictionary;

import java.util.LinkedList;

import static net.minecraft.world.gen.surfacebuilders.SurfaceBuilder.STONE_STONE_GRAVEL_CONFIG;

public class BiomeAsteroids extends BiomeGC
{
    public static final BiomeAsteroids asteroid = new BiomeAsteroids();

    private BiomeAsteroids()
    {
        super((new Biome.Builder()).surfaceBuilder(SurfaceBuilder.NOPE, STONE_STONE_GRAVEL_CONFIG).precipitation(Biome.RainType.NONE).category(Category.NONE).depth(1.5F).scale(0.4F).temperature(0.0F).downfall(0.0F).waterColor(4159204).waterFogColor(329011).parent(null), true);
        this.getSpawns(EntityClassification.MONSTER).clear();
        this.getSpawns(EntityClassification.WATER_CREATURE).clear();
        this.getSpawns(EntityClassification.CREATURE).clear();
        this.getSpawns(EntityClassification.AMBIENT).clear();
        this.getSpawns(EntityClassification.MISC).clear();
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
        this.addSpawn(EntityClassification.MONSTER, new SpawnListEntry(GCEntities.EVOLVED_ZOMBIE, 3000, 1, 3));
        this.addSpawn(EntityClassification.MONSTER, new SpawnListEntry(GCEntities.EVOLVED_SPIDER, 2000, 1, 2));
        this.addSpawn(EntityClassification.MONSTER, new SpawnListEntry(GCEntities.EVOLVED_SKELETON, 1500, 1, 1));
        this.addSpawn(EntityClassification.MONSTER, new SpawnListEntry(GCEntities.EVOLVED_CREEPER, 2000, 1, 1));
        if (challengeMode)
        {
            this.addSpawn(EntityClassification.MONSTER, new SpawnListEntry(GCEntities.EVOLVED_ENDERMAN, 250, 1, 1));
        }
    }

    @Override
    public float getSpawningChance()
    {
        return 0.01F;
    }
}
