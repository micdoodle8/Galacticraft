package micdoodle8.mods.galacticraft.core.advancement;

import java.lang.reflect.Method;

import micdoodle8.mods.galacticraft.core.advancement.criterion.GenericTrigger;
import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.advancements.ICriterionTrigger;

public class GCTriggers
{
    public static final GenericTrigger LAUNCH_ROCKET = new GenericTrigger("launch_rocket");
    public static final GenericTrigger KILL_MOON_BOSS = new GenericTrigger("boss_moon");
    public static final GenericTrigger CREATE_SPACE_STATION = new GenericTrigger("create_space_station");

    public static void registerTriggers()
    {
        Method register = null;
        try {
            Class clazz = CriteriaTriggers.class;
            Method[] mm = clazz.getDeclaredMethods();
            for (Method m : mm)
            {
                Class<?>[] params = m.getParameterTypes();
                if (params != null && params[0] == ICriterionTrigger.class)
                {
                    register = m;
                    break;
                }
            }
        } catch (Exception e) { e.printStackTrace(); }
        
        if (register != null)
        {
            try {
                register.invoke(null, GCTriggers.LAUNCH_ROCKET);
                register.invoke(null, GCTriggers.KILL_MOON_BOSS);
                register.invoke(null, GCTriggers.CREATE_SPACE_STATION);
            } catch (Exception ignore) {}
        }
    }
}