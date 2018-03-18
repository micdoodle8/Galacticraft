package buildcraft.api.facades;

public interface IFacade {
    FacadeType getType();

    IFacadePhasedState[] getPhasedStates();
}
