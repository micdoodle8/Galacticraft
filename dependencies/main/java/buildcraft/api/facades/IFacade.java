package buildcraft.api.facades;

public interface IFacade {
    FacadeType getType();

    boolean isHollow();

    IFacadePhasedState[] getPhasedStates();
}
