package micdoodle8.mods.galacticraft.API;

import java.util.Random;
import net.minecraft.item.ItemStack;

public interface IDungeonBoss
{
    /**
     * Returning a float that is not -1 here will spawn given experience with
     * fancy effect
     * 
     * @return experience to spawn on death (-1 for none)
     */
    public float getExperienceToSpawn();

    /**
     * once a player is within this distance of the boss spawner tile, the boss
     * will spawned
     * 
     * NOT IMPLEMENTED YET
     * 
     * @return distance to closest player before spawning
     */
    public double getDistanceToSpawn();

    /**
     * Called when the boss is spawned
     * 
     * @param spawner
     *            the spawner tile that this boss was spawned from
     */
    public void onBossSpawned(IDungeonBossSpawner spawner);

    /**
     * Get's the guaranteed loot
     * 
     * @param loop
     *            the loop count for filling this chest
     * @param rand
     *            the random object for variation
     * @return a mep entry with the integer and the amount
     */
    public ItemStack getGuaranteedLoot(int loop, Random rand);

    /**
     * Get the loot with a chance
     * 
     * @param rand
     *            the random object for variation
     * @return a mep entry with the integer and the amount
     */
    public ItemStack getLoot(Random rand);
}
