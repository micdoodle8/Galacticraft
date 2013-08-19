package micdoodle8.mods.galacticraft.core.entities;

import micdoodle8.mods.galacticraft.core.tile.GCCoreTileEntityDungeonSpawner;
import universalelectricity.core.vector.Vector3;

public interface IBoss
{
    public void setRoom(Vector3 roomCoords, Vector3 roomSize);

    public void onBossSpawned(GCCoreTileEntityDungeonSpawner spawner);
}
