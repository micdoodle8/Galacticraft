package micdoodle8.mods.galacticraft.API.tile;

import universalelectricity.core.vector.Vector3;

public interface IColorable
{
    public void setColor(byte col);

    public byte getColor();

    public void onAdjacentColorChanged(Vector3 thisVec, Vector3 updatedVec);
}
