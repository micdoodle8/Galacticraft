package micdoodle8.mods.galacticraft.core.world.gen;

import micdoodle8.mods.galacticraft.api.world.BiomeGC;
import micdoodle8.mods.galacticraft.core.GCBlocks;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.feature.IFeatureConfig;
import net.minecraft.world.gen.surfacebuilders.SurfaceBuilder;
import net.minecraft.world.gen.surfacebuilders.SurfaceBuilderConfig;

public class BiomeMoonHills extends BiomeMoon
{
    public static final BiomeMoonHills moonBiomeHills = new BiomeMoonHills();
    //    public static final Biome moonFlat = new BiomeFlatMoon(new BiomeProperties("Moon").setBaseHeight(1.5F).setHeightVariation(0.4F).setRainfall(0.0F));

    BiomeMoonHills()
    {
        super((new Biome.Builder()).surfaceBuilder(SurfaceBuilder.DEFAULT, new SurfaceBuilderConfig(GCBlocks.moonTurf.getDefaultState(), GCBlocks.moonDirt.getDefaultState(), GCBlocks.moonDirt.getDefaultState())).precipitation(Biome.RainType.NONE).category(Category.NONE).depth(1.1F).scale(0.0F).temperature(0.0F).downfall(0.0F).waterColor(4159204).waterFogColor(329011).parent(null), true);
        addDefaultFeatures();
    }

    @Override
    public float getSpawningChance()
    {
        return 0.1F;
    }
}
