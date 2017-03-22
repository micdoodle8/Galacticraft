package micdoodle8.mods.galacticraft.core.dimension;

import micdoodle8.mods.galacticraft.api.prefab.world.gen.WorldProviderSpace;
import micdoodle8.mods.galacticraft.core.world.gen.BiomeProviderOrbit;
import micdoodle8.mods.galacticraft.core.world.gen.ChunkProviderOrbit;
import net.minecraft.world.biome.BiomeProvider;
import net.minecraft.world.chunk.IChunkGenerator;

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
    protected void createBiomeProvider()
    {
        super.createBiomeProvider();
        this.getSpinManager().registerServerSide();
    }

    @Override
    public Class<? extends IChunkGenerator> getChunkProviderClass()
    {
        return ChunkProviderOrbit.class;
    }

    @Override
    public Class<? extends BiomeProvider> getBiomeProviderClass()
    {
        return BiomeProviderOrbit.class;
    }
    
    @Override
    public void updateWeather()
    {
        super.updateWeather();
        spinManager.updateSpin();
    }
}
