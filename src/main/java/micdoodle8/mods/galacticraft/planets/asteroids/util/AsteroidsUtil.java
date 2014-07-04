package micdoodle8.mods.galacticraft.planets.asteroids.util;

import cpw.mods.fml.common.registry.EntityRegistry;
import micdoodle8.mods.galacticraft.planets.GalacticraftPlanets;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;

public class AsteroidsUtil
{
	public static void registerAsteroidsNonMobEntity(Class<? extends Entity> var0, String var1, int id, int trackingDistance, int updateFreq, boolean sendVel)
	{
		EntityList.addMapping(var0, var1, id);
		EntityRegistry.registerModEntity(var0, var1, id, GalacticraftPlanets.instance, trackingDistance, updateFreq, sendVel);
	}
}
