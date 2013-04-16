package micdoodle8.mods.galacticraft.API;

import net.minecraft.world.World;
import universalelectricity.core.vector.Vector3;

/**
 *  Implement into tile entities that will have bosses spawn from them
 */
public interface IDungeonBossSpawner 
{
//	/**
//	 * @return true if boss can spawn here
//	 */
//	public boolean canSpawn();
//	
//	/**
//	 * @return position to spawn in from (not relative to tile, this is the actual coordinate)
//	 */
//	public Vector3 getBossSpawnPosition();
//	
//	/**
//	 * Gets the boss to spawn
//	 * 
//	 * @param world the world to be spawned into
//	 * @return dungeon boss that will be spawned here
//	 */
//	public IDungeonBoss getBossForThisTile();
	
	public void setBossDefeated(boolean defeated);
	
	public boolean getBossDefeated();
}
