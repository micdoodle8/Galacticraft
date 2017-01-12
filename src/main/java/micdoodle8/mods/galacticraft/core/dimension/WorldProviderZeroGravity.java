package micdoodle8.mods.galacticraft.core.dimension;

import micdoodle8.mods.galacticraft.api.prefab.world.gen.WorldProviderSpace;

public abstract class WorldProviderZeroGravity extends WorldProviderSpace
{
    private SpinManager spinManager = new SpinManager(this);

    public SpinManager getSpinManager()
    {
        return spinManager;
    }
}
