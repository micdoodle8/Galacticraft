package micdoodle8.mods.galacticraft.core.dimension;

import net.minecraft.world.biome.WorldChunkManager;
import net.minecraft.world.chunk.IChunkProvider;
import micdoodle8.mods.galacticraft.api.galaxies.CelestialBody;
import micdoodle8.mods.galacticraft.api.prefab.world.gen.WorldProviderSpace;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.world.gen.ChunkProviderOrbit;
import micdoodle8.mods.galacticraft.core.world.gen.WorldChunkManagerOrbit;

public abstract class WorldProviderSpaceStation extends WorldProviderSpace
{
    private SpinManager spinManager = new SpinManager(this);

    public SpinManager getSpinManager()
    {
        return spinManager;
    }
    
    @Override
    public void setDimension(int var1)
    {
        super.setDimension(var1);
    }

    /**
     * Called only once from WorldProvider.registerWorld()
     * so this provides a handy initialisation method
     */
    @Override
    public void registerWorldChunkManager()
    {
        super.registerWorldChunkManager();
        this.getSpinManager().registerServerSide();
    }
    
    @Override
    public CelestialBody getCelestialBody()
    {
        return GalacticraftCore.satelliteSpaceStation;
    }
    
    @Override
    public Class<? extends IChunkProvider> getChunkProviderClass()
    {
        return ChunkProviderOrbit.class;
    }

    @Override
    public Class<? extends WorldChunkManager> getWorldChunkManagerClass()
    {
        return WorldChunkManagerOrbit.class;
    }
    
    @Override
    public void updateWeather()
    {
        super.updateWeather();
    }
}
