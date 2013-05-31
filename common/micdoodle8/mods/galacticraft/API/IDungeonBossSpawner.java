package micdoodle8.mods.galacticraft.API;

/**
 * Implement into tile entities that will have bosses spawn from them
 */
public interface IDungeonBossSpawner
{
    public void setBossDefeated(boolean defeated);

    public boolean getBossDefeated();

    public void setBossSpawned(boolean spawned);

    public boolean getBossSpawned();

    public void setBoss(IDungeonBoss boss);

    public IDungeonBoss getBoss();
}
