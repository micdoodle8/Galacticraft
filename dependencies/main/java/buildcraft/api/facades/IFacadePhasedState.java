package buildcraft.api.facades;

import javax.annotation.Nullable;

import net.minecraft.item.EnumDyeColor;

public interface IFacadePhasedState {
    IFacadeState getState();

    @Nullable
    EnumDyeColor getActiveColor();
}
