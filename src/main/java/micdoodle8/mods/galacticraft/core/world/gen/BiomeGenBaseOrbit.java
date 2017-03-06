package micdoodle8.mods.galacticraft.core.world.gen;

import micdoodle8.mods.galacticraft.core.entities.*;
import micdoodle8.mods.galacticraft.core.util.ConfigManagerCore;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.biome.BiomeGenBase.SpawnListEntry;

public class BiomeGenBaseOrbit extends BiomeGenBase
{
    public static final BiomeGenBase space = new BiomeGenBaseOrbit(ConfigManagerCore.biomeIDbase + 3).setBiomeName("Space");

    @SuppressWarnings("unchecked")
    private BiomeGenBaseOrbit(int var1)
    {
        super(var1);
        this.spawnableMonsterList.clear();
        this.spawnableWaterCreatureList.clear();
        this.spawnableCreatureList.clear();
        this.spawnableCaveCreatureList.clear();
        this.spawnableMonsterList.add(new SpawnListEntry(EntityEvolvedZombie.class, 100, 4, 4));
        this.spawnableMonsterList.add(new SpawnListEntry(EntityEvolvedSpider.class, 100, 4, 4));
        this.spawnableMonsterList.add(new SpawnListEntry(EntityEvolvedSkeleton.class, 100, 4, 4));
        this.spawnableMonsterList.add(new SpawnListEntry(EntityEvolvedCreeper.class, 100, 4, 4));
        this.spawnableMonsterList.add(new SpawnListEntry(EntityEvolvedEnderman.class, 10, 1, 4));
        this.spawnableMonsterList.add(new SpawnListEntry(EntityEvolvedWitch.class, 5, 1, 1));
        this.rainfall = 0F;
    }

    @Override
    public BiomeGenBaseOrbit setColor(int var1)
    {
        return (BiomeGenBaseOrbit) super.setColor(var1);
    }

    @Override
    public float getSpawningChance()
    {
        return 0.01F;
    }
}
