package micdoodle8.mods.galacticraft.core.advancement;

import micdoodle8.mods.galacticraft.core.advancement.criterion.GenericTrigger;
import net.minecraft.advancements.CriteriaTriggers;

public class GCTriggers
{
    public static final GenericTrigger LAUNCH_ROCKET = new GenericTrigger("launch_rocket");
    public static final GenericTrigger KILL_MOON_BOSS = new GenericTrigger("boss_moon");
    public static final GenericTrigger CREATE_SPACE_STATION = new GenericTrigger("create_space_station");

    public static void registerTriggers() {
        CriteriaTriggers.register(GCTriggers.LAUNCH_ROCKET);
        CriteriaTriggers.register(GCTriggers.KILL_MOON_BOSS);
        CriteriaTriggers.register(GCTriggers.CREATE_SPACE_STATION);
    }
}