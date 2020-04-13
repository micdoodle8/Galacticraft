package micdoodle8.mods.galacticraft.core.advancement;

import java.lang.reflect.Method;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import micdoodle8.mods.galacticraft.api.prefab.entity.EntitySpaceshipBase;
import micdoodle8.mods.galacticraft.core.advancement.criterion.GenericTrigger;
import micdoodle8.mods.galacticraft.core.entities.EntitySkeletonBoss;
import micdoodle8.mods.galacticraft.core.entities.player.GCPlayerStats;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.advancements.ICriterionInstance;
import net.minecraft.advancements.ICriterionTrigger;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;

public class GCTriggers
{
    public static final GenericTrigger LAUNCH_ROCKET = new GenericTrigger("launch_rocket")
    {
        @Override
        public ICriterionInstance deserializeInstance(JsonObject jsonObject, JsonDeserializationContext jsonDeserializationContext)
        {
            return new Instance(getId())
            {
                @Override
                public boolean test(EntityPlayerMP player)
                {
                    if (player.getRidingEntity() instanceof EntitySpaceshipBase)
                    {
                        if (((EntitySpaceshipBase) player.getRidingEntity()).launchPhase >= EntitySpaceshipBase.EnumLaunchPhase.LAUNCHED.ordinal())
                        {
                            return player.getRidingEntity().getPassengers().size() >= 1 && player.getRidingEntity().getPassengers().get(0) instanceof EntityPlayerMP;
                        }
                    }
                    return false;
                }
            };
        }
    };
    public static final GenericTrigger FIND_MOON_BOSS = new GenericTrigger("boss_moon")
    {
        @Override
        public ICriterionInstance deserializeInstance(JsonObject jsonObject, JsonDeserializationContext jsonDeserializationContext)
        {
            return new Instance(getId())
            {
                @Override
                public boolean test(EntityPlayerMP player)
                {
                    for (Entity entity : player.world.loadedEntityList)
                    {
                        return entity instanceof EntitySkeletonBoss && entity.getDistanceSq(player) < 20 * 20;
                    }
                    return false;
                }
            };
        }
    };
    public static final GenericTrigger CREATE_SPACE_STATION = new GenericTrigger("create_space_station")
    {
        @Override
        public ICriterionInstance deserializeInstance(JsonObject jsonObject, JsonDeserializationContext jsonDeserializationContext)
        {
            return new Instance(getId())
            {
                @Override
                public boolean test(EntityPlayerMP player)
                {
                    return !GCPlayerStats.get(player).getSpaceStationDimensionData().isEmpty();
                }
            };
        }
    };

    public static void registerTriggers()
    {
        Method register = null;
        try {
            Class clazz = CriteriaTriggers.class;
            Method[] mm = clazz.getDeclaredMethods();
            for (Method m : mm)
            {
                Class<?>[] params = m.getParameterTypes();
                if (params != null && params.length == 1 && params[0] == ICriterionTrigger.class)
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
                register.invoke(null, GCTriggers.FIND_MOON_BOSS);
                register.invoke(null, GCTriggers.CREATE_SPACE_STATION);
            } catch (Exception ignore) {}
        }
    }
}