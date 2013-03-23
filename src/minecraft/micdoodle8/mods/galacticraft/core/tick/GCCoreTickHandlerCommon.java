package micdoodle8.mods.galacticraft.core.tick;

import java.util.EnumSet;

import micdoodle8.mods.galacticraft.API.IInterplanetaryObject;
import micdoodle8.mods.galacticraft.API.IOrbitDimension;
import micdoodle8.mods.galacticraft.core.dimension.GCCoreEnumTeleportType;
import micdoodle8.mods.galacticraft.core.util.WorldUtil;
import net.minecraft.entity.Entity;
import net.minecraft.world.WorldServer;
import cpw.mods.fml.common.ITickHandler;
import cpw.mods.fml.common.TickType;

public class GCCoreTickHandlerCommon implements ITickHandler
{
	public static int chatCooldown;
	
	@Override
	public void tickStart(EnumSet<TickType> type, Object... tickData)
	{
		if (type.equals(EnumSet.of(TickType.SERVER)))
        {
//			GalacticraftCore.tick++;

			if (chatCooldown > 0)
			{
				chatCooldown--;
			}
        }
		else if (type.equals(EnumSet.of(TickType.WORLD)))
		{
			WorldServer world = (WorldServer) tickData[0];
			Object[] entityList = world.loadedEntityList.toArray();
			for (Object o : entityList)
			{
				if (o instanceof Entity && o instanceof IInterplanetaryObject)
				{
					Entity e = (Entity) o;
					IInterplanetaryObject iiobject = (IInterplanetaryObject) e;
					
					if (e.posY >= iiobject.getYCoordToTeleportFrom() && e.dimension != iiobject.getDimensionForTeleport())
					{
						int dim = iiobject.getDimensionForTeleport();
						WorldUtil.transferEntityToDimension(e, dim, world, dim == 0 ? GCCoreEnumTeleportType.TOOVERWORLD : GCCoreEnumTeleportType.TOPLANET);
					}
				}
				
				if (o instanceof Entity && ((Entity) o).worldObj.provider instanceof IOrbitDimension)
				{
					Entity e = (Entity) o;
					IOrbitDimension dimension = (IOrbitDimension)((Entity)o).worldObj.provider;
					
					if (e.posY <= dimension.getYCoordToTeleport())
					{
	    	    		Integer dim = WorldUtil.getProviderForName(dimension.getPlanetToOrbit()).dimensionId;
	    	    		
    	    			WorldUtil.transferEntityToDimension(e, dim, world, GCCoreEnumTeleportType.TOOVERWORLD);
					}
				}
			}
		}
	}

	@Override
	public void tickEnd(EnumSet<TickType> type, Object... tickData) { }

	@Override
	public EnumSet<TickType> ticks()
	{
		return EnumSet.of(TickType.SERVER, TickType.WORLD);
	}

	@Override
	public String getLabel()
	{
		return "Galacticraft Core Common";
	}
}
