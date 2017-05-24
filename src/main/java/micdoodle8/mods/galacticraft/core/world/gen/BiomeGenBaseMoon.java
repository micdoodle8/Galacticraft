package micdoodle8.mods.galacticraft.core.world.gen;

import micdoodle8.mods.galacticraft.api.world.BiomeGenBaseGC;
import micdoodle8.mods.galacticraft.core.util.ConfigManagerCore;
import net.minecraft.world.biome.BiomeDecorator;
import net.minecraft.world.biome.BiomeGenBase;

public class BiomeGenBaseMoon extends BiomeGenBaseGC
{
    public static final BiomeGenBase moonFlat = new BiomeGenFlatMoon(ConfigManagerCore.biomeIDbase).setBiomeName("Moon");

    BiomeGenBaseMoon(int var1)
    {
        super(var1);
        this.rainfall = 0F;
    }
    
    @Override
    public BiomeDecorator createBiomeDecorator()
    {
        return getModdedBiomeDecorator(new BiomeDecoratorMoon());
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
