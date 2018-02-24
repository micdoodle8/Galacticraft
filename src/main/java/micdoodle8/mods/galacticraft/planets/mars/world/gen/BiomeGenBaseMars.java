package micdoodle8.mods.galacticraft.planets.mars.world.gen;

import micdoodle8.mods.galacticraft.api.world.BiomeGenBaseGC;
import micdoodle8.mods.galacticraft.core.util.ConfigManagerCore;
import net.minecraft.world.biome.BiomeGenBase;

public class BiomeGenBaseMars extends BiomeGenBaseGC
{
    public static final BiomeGenBase marsFlat = new BiomeGenFlatMars(ConfigManagerCore.biomeIDbase + 1).setBiomeName("Mars Flat");

    BiomeGenBaseMars(int var1)
    {
        super(var1);
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
