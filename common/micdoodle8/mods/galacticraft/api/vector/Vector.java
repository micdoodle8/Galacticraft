package micdoodle8.mods.galacticraft.api.vector;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.world.IBlockAccess;
import cpw.mods.fml.common.FMLLog;

public abstract class Vector implements Cloneable
{
    public abstract List<Double> getCoords();

    public abstract void setCoords(List<Double> coords) throws IndexOutOfBoundsException;

    public abstract Vector add(Vector vec);

    public void writeToNBT(NBTTagList list)
    {
        for (Double coord : this.getCoords())
        {
            NBTTagCompound nbt = new NBTTagCompound();
            nbt.setDouble("Coordinate", coord);
            list.appendTag(nbt);
        }
    }

    public static Vector readFromNBT(NBTTagList list)
    {
        Vector vec;

        switch (list.tagCount())
        {
        case 2:
            vec = new Vector2();
            break;
        case 3:
            vec = new Vector3();
            break;
        default:
            FMLLog.severe("FAILED TO LOAD VECTOR FROM NBT");
            return null;
        }

        List<Double> coordList = new ArrayList<Double>();

        for (int i = 0; i < list.tagCount(); ++i)
        {
            NBTTagCompound nbt = (NBTTagCompound) list.tagAt(i);
            coordList.add(nbt.getDouble("Coordinate"));
        }

        try
        {
            vec.setCoords(coordList);
        }
        catch (IndexOutOfBoundsException e)
        {
            FMLLog.severe("FAILED TO LOAD VECTOR FROM NBT");
            return null;
        }

        return vec;
    }

    public static class Vector3 extends Vector
    {
        public double x;
        public double y;
        public double z;

        public Vector3()
        {

        }

        public Vector3(double x, double y, double z)
        {
            this.x = x;
            this.y = y;
            this.z = z;
        }

        @Override
        public List<Double> getCoords()
        {
            return Arrays.asList(new Double[] { this.x, this.y, this.z });
        }

        public int getIntX()
        {
            return (int) Math.floor(this.x);
        }

        public int getIntY()
        {
            return (int) Math.floor(this.y);
        }

        public int getIntZ()
        {
            return (int) Math.floor(this.z);
        }

        @Override
        public void setCoords(List<Double> coords) throws IndexOutOfBoundsException
        {
            this.x = coords.get(0);
            this.y = coords.get(1);
            this.z = coords.get(2);
        }

        @Override
        public Vector3 clone()
        {
            return new Vector3(this.x, this.y, this.z);
        }

        @Override
        public Vector add(Vector vec)
        {
            if (vec instanceof Vector3)
            {
                this.x += ((Vector3) vec).x;
                this.y += ((Vector3) vec).y;
                this.z += ((Vector3) vec).z;
            }
            else
            {
                FMLLog.severe("CANNOT ADD VECTOR2 to VECTOR3");
            }

            return this;
        }

        public int getBlockID(IBlockAccess blockAccess)
        {
            return blockAccess.getBlockId(this.getIntX(), this.getIntY(), this.getIntZ());
        }

        public Object toUEVector3()
        {
            try
            {
                Class<?> clazz = Class.forName("universalelectricity.core.vector.Vector3");
                Constructor<?> c = clazz.getConstructor(Double.class, Double.class, Double.class);
                return c.newInstance(this.x, this.y, this.z);
            }
            catch (Exception e)
            {
                e.printStackTrace();
                return null;
            }
        }
    }

    public static class Vector2 extends Vector
    {
        public double x;
        public double y;

        public Vector2()
        {

        }

        public Vector2(double x, double y)
        {
            this.x = x;
            this.y = y;
        }

        @Override
        public List<Double> getCoords()
        {
            return Arrays.asList(new Double[] { this.x, this.y });
        }

        @Override
        public void setCoords(List<Double> coords) throws IndexOutOfBoundsException
        {
            this.x = coords.get(0);
            this.y = coords.get(1);
        }

        @Override
        public Vector2 clone()
        {
            return new Vector2(this.x, this.y);
        }

        @Override
        public Vector add(Vector vec)
        {
            if (vec instanceof Vector2)
            {
                this.x += ((Vector3) vec).x;
                this.y += ((Vector3) vec).y;
            }
            else
            {
                FMLLog.severe("CANNOT ADD VECTOR3 to VECTOR2");
            }

            return this;
        }
    }
}
