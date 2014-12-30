package micdoodle8.mods.galacticraft.core.world.gen;

import micdoodle8.mods.galacticraft.core.util.ConfigManagerCore;
import net.minecraft.world.biome.BiomeGenBase;

public class BiomeGenBaseMoon extends BiomeGenBase
{
    public static final BiomeGenBase moonFlat = new BiomeGenFlatMoon(ConfigManagerCore.biomeIDbase).setBiomeName("moon");

    BiomeGenBaseMoon(int var1)
    {
        super(var1);
        this.spawnableMonsterList.clear();
        this.spawnableWaterCreatureList.clear();
        this.spawnableCreatureList.clear();
        this.rainfall = 0F;
    }

    @Override
    public BiomeGenBaseMoon setColor(int var1)
    {
        return (BiomeGenBaseMoon) super.setColor(var1);
    }

    @Override
    public float getSpawningChance()
    {
        return 0.1F;
    }
}
