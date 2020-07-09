package micdoodle8.mods.galacticraft.core.entities;

public interface ITumblable
{
    void setTumbling(float value);

    float getTumbleAngle(float partial);

    float getTumbleAxisX();

    float getTumbleAxisZ();
}
