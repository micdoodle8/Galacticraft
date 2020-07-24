package micdoodle8.mods.galacticraft.core.advancement;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import micdoodle8.mods.galacticraft.api.prefab.entity.EntitySpaceshipBase;
import micdoodle8.mods.galacticraft.core.advancement.criterion.GenericTrigger;
import micdoodle8.mods.galacticraft.core.entities.EntitySkeletonBoss;
import micdoodle8.mods.galacticraft.core.entities.player.GCPlayerStats;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.advancements.ICriterionInstance;
import net.minecraft.advancements.ICriterionTrigger;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.world.server.ServerWorld;

import java.lang.reflect.Method;

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
                public boolean test(ServerPlayerEntity player)
                {
                    if (player.getRidingEntity() instanceof EntitySpaceshipBase)
                    {
                        if (((EntitySpaceshipBase) player.getRidingEntity()).launchPhase >= EntitySpaceshipBase.EnumLaunchPhase.LAUNCHED.ordinal())
                        {
                            return player.getRidingEntity().getPassengers().size() >= 1 && player.getRidingEntity().getPassengers().get(0) instanceof ServerPlayerEntity;
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
                public boolean test(ServerPlayerEntity player)
                {
                    return ((ServerWorld) player.world).getEntities().filter((entity -> entity instanceof EntitySkeletonBoss && entity.getDistanceSq(player) < 400)).count() >= 1;
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
                public boolean test(ServerPlayerEntity player)
                {
                    return !GCPlayerStats.get(player).getSpaceStationDimensionData().isEmpty();
                }
            };
        }
    };

    public static void registerTriggers()
    {
        Method register = null;
        try
        {
            Class clazz = CriteriaTriggers.class;
            Method[] mm = clazz.getDeclaredMethods();
            for (Method m : mm)
            {
                Class<?>[] params = m.getParameterTypes();
                if (params.length == 1 && params[0] == ICriterionTrigger.class)
                {
                    register = m;
                    break;
                }
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        if (register != null)
        {
            try
            {
                register.invoke(null, GCTriggers.LAUNCH_ROCKET);
                register.invoke(null, GCTriggers.FIND_MOON_BOSS);
                register.invoke(null, GCTriggers.CREATE_SPACE_STATION);
            }
            catch (Exception ignore)
            {
            }
        }
    }
}