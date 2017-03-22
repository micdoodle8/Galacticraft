package micdoodle8.mods.galacticraft.core.entities;

public interface ITumblable
{
    public void setTumbling(float value);
    
    public float getTumbleAngle(float partial);

    public float getTumbleAxisX();

    public float getTumbleAxisZ();
}
