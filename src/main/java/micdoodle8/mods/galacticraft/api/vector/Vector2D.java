package micdoodle8.mods.galacticraft.api.vector;

/**
 * Vector2 Class is used for defining objects in a 2D space.
 *
 * @author Calclavia
 */

public class Vector2D implements Cloneable
{
    public double x;
    public double y;

    public Vector2D()
    {
        this(0, 0);
    }

    public Vector2D(double x, double y)
    {
        this.x = x;
        this.y = y;
    }

    /**
     * Returns the integer floor value.
     *
     * @return
     */
    public int intX()
    {
        return (int) Math.floor(this.x);
    }

    public int intY()
    {
        return (int) Math.floor(this.y);
    }

    /**
     * Makes a new copy of this Vector. Prevents variable referencing problems.
     */
    @Override
    public final Vector2D clone()
    {
        return new Vector2D(this.x, this.y);
    }

    public static double distance(Vector2D point1, Vector2D point2)
    {
        double xDifference = point1.x - point2.x;
        double yDiference = point1.y - point2.y;
        return Math.sqrt(xDifference * xDifference + yDiference * yDiference);
    }

    public static double slope(Vector2D point1, Vector2D point2)
    {
        double xDifference = point1.x - point2.x;
        double yDiference = point1.y - point2.y;
        return yDiference / xDifference;
    }

    public double distanceTo(Vector2D target)
    {
        double xDifference = this.x - target.x;
        double yDifference = this.y - target.y;
        return Math.sqrt(xDifference * xDifference + yDifference * yDifference);
    }

    public Vector2D add(Vector2D par1)
    {
        this.x += par1.x;
        this.y += par1.y;
        return this;
    }

    public Vector2D add(double par1)
    {
        this.x += par1;
        this.y += par1;
        return this;
    }

    public Vector2D invert()
    {
        this.multiply(-1);
        return this;
    }

    public Vector2D multiply(double amount)
    {
        this.x *= amount;
        this.y *= amount;
        return this;
    }

    public Vector2D round()
    {
        return new Vector2D(Math.round(this.x), Math.round(this.y));
    }

    public Vector2D ceil()
    {
        return new Vector2D(Math.ceil(this.x), Math.ceil(this.y));
    }

    public Vector2D floor()
    {
        return new Vector2D(Math.floor(this.x), Math.floor(this.y));
    }

    @Override
    public int hashCode()
    {
        return ("X:" + this.x + "Y:" + this.y).hashCode();
    }

    @Override
    public boolean equals(Object o)
    {
        if (o instanceof Vector2D)
        {
            Vector2D vector = (Vector2D) o;
            return this.x == vector.x && this.y == vector.y;
        }

        return false;
    }

    @Override
    public String toString()
    {
        return "Vector2D [" + this.x + "," + this.y + "]";
    }
}