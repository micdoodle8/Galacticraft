package buildcraft.api.tiles;

import javax.annotation.Nonnull;

import net.minecraftforge.common.capabilities.Capability;

import buildcraft.api.core.CapabilitiesHelper;

public class TilesAPI {
    @Nonnull
    public static final Capability<IControllable> CAP_CONTROLLABLE;

    @Nonnull
    public static final Capability<IHasWork> CAP_HAS_WORK;

    @Nonnull
    public static final Capability<IHeatable> CAP_HEATABLE;

    @Nonnull
    public static final Capability<ITileAreaProvider> CAP_TILE_AREA_PROVIDER;

    static {
        CAP_CONTROLLABLE = CapabilitiesHelper.registerCapability(IControllable.class);
        CAP_HAS_WORK = CapabilitiesHelper.registerCapability(IHasWork.class);
        CAP_HEATABLE = CapabilitiesHelper.registerCapability(IHeatable.class);
        CAP_TILE_AREA_PROVIDER = CapabilitiesHelper.registerCapability(ITileAreaProvider.class);
    }
}
