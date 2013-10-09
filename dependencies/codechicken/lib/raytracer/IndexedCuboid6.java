package codechicken.lib.raytracer;

import codechicken.lib.vec.Cuboid6;

public class IndexedCuboid6 extends Cuboid6
{
    public Object data;
    
    public IndexedCuboid6(Object data, Cuboid6 cuboid)
    {
        super(cuboid);
        this.data = data;
    }
}