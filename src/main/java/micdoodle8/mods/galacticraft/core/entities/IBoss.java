package micdoodle8.mods.galacticraft.core.entities;

import micdoodle8.mods.galacticraft.api.vector.Vector3;
import micdoodle8.mods.galacticraft.core.tile.TileEntityDungeonSpawner;

public interface IBoss
{
    public void setRoom(Vector3 roomCoords, Vector3 roomSize);

    public void onBossSpawned(TileEntityDungeonSpawner spawner);
}
