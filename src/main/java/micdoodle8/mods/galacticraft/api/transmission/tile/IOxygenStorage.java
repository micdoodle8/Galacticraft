package micdoodle8.mods.galacticraft.api.transmission.tile;

/**
 * This interface is to be applied to all TileEntities which have Oxygen stores
 */
public interface IOxygenStorage
{
    /**
     * Sets the amount of oxygen this unit has stored.
     */
    public void setOxygenStored(int oxygen);

    /**
     * * @return Get the amount of oxygen currently stored in the block.
     */
    public int getOxygenStored();

    /**
     * @return Get the max amount of oxygen that can be stored in the block.
     */
    public int getMaxOxygenStored();
}
