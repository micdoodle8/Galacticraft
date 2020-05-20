package micdoodle8.mods.galacticraft.planets.venus.world.gen.layer;

import net.minecraft.world.gen.layer.Layer;
import net.minecraft.world.gen.layer.VoroniZoomLayer;
import net.minecraft.world.gen.layer.ZoomLayer;

public abstract class GenLayerVenus extends Layer
{
    public GenLayerVenus(long l)
    {
        super(l);
    }

    public static Layer[] createWorld(long l)
    {
        Layer biomes = new GenLayerVenusBiomes(l);
        biomes = new ZoomLayer(1000L, biomes);
//        biomes = new GenLayerVenusSurround(500L, biomes);
        biomes = new ZoomLayer(1001L, biomes);
        biomes = new ZoomLayer(1002L, biomes);
        biomes = new ZoomLayer(1003L, biomes);
        Layer genLayerVeronoiZoom = new VoroniZoomLayer(10L, biomes);
        biomes.initWorldGenSeed(l);
        genLayerVeronoiZoom.initWorldGenSeed(l);

        return new Layer[] { biomes, genLayerVeronoiZoom };
    }
}
