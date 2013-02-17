package micdoodle8.mods.galacticraft.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import micdoodle8.mods.galacticraft.API.IMapPlanet;

public class DupKeyHashMap extends HashMap<IMapPlanet, List<IMapPlanet>>
{
	public void put(IMapPlanet key, IMapPlanet planet)
	{
		List<IMapPlanet> current = this.get(key);
		
		if (current == null)
		{
			current = new ArrayList<IMapPlanet>();
			super.put(key, current);
		}
		
		current.add(planet);
	}
}
