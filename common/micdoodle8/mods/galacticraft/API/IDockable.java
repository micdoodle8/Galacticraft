package micdoodle8.mods.galacticraft.API;

public interface IDockable extends IFuelable, ICargoEntity
{
    public void setPad(IFuelDock pad);

    public IFuelDock getLandingPad();

    public void onPadDestroyed();

    public boolean isDockValid(IFuelDock dock);
}
