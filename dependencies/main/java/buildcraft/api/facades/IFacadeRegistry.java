package buildcraft.api.facades;

import java.util.Collection;

import javax.annotation.Nullable;

import net.minecraft.item.EnumDyeColor;

public interface IFacadeRegistry {

    Collection<? extends IFacadeState> getValidFacades();

    IFacadePhasedState createPhasedState(IFacadeState state, boolean isHollow, @Nullable EnumDyeColor activeColor);

    IFacade createPhasedFacade(IFacadePhasedState[] states);

    default IFacade createBasicFacade(IFacadeState state, boolean isHollow) {
        return createPhasedFacade(new IFacadePhasedState[] { createPhasedState(state, isHollow, null) });
    }
}
