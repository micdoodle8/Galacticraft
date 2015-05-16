package micdoodle8.mods.galacticraft.core.oxygen;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import micdoodle8.mods.galacticraft.api.vector.BlockVec3;

public class DataOxygen
{
    private final static Map<Integer, Map<BlockVec3, Integer>> airBlocks = new ConcurrentHashMap();

    public static Map<BlockVec3, Integer> getAirData(int dim)
    {
    	Map<BlockVec3, Integer> existing = airBlocks.get(dim);
    	if (existing == null)
    	{
    		existing = new ConcurrentHashMap<BlockVec3, Integer>();
    		airBlocks.put(dim, existing);
    	}
    	return existing;
    }
//
//    @Override
//    public int hashCode()
//    {
//        return ((this.y * 379 + this.x) * 373 + this.z) * 7;
//    }
//
//    @Override
//    public boolean equals(Object o)
//    {
//        if (o instanceof DataOxygen)
//        {
//        	DataOxygen vector = (DataOxygen) o;
//            return this.x == vector.x && this.y == vector.y && this.z == vector.z;
//        }
//
//        return false;
//    }
}
