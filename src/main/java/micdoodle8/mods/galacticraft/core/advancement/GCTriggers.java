package micdoodle8.mods.galacticraft.core.advancement;

import micdoodle8.mods.galacticraft.core.advancement.criterion.GenericTrigger;
import net.minecraft.advancements.CriteriaTriggers;

public class GCTriggers
{
    public static final GenericTrigger LAUNCH_ROCKET = new GenericTrigger("launch_rocket");
    public static final GenericTrigger LOW_GRAVITY_JUMP = new GenericTrigger("low_gravity_jump");
    public static final GenericTrigger BOSS_MOON = new GenericTrigger("boss_moon");

    public static void registerTriggers() {
        CriteriaTriggers.register(GCTriggers.LAUNCH_ROCKET);
        CriteriaTriggers.register(GCTriggers.LOW_GRAVITY_JUMP);
        CriteriaTriggers.register(GCTriggers.BOSS_MOON);
    }
}