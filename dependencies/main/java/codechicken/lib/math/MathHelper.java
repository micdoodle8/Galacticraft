package codechicken.lib.math;

public class MathHelper
{
    public static final double phi = 1.618033988749894;
    public static final double pi = Math.PI;
    public static final double todeg = 57.29577951308232;
    public static final double torad = 0.017453292519943;
    public static final double sqrt2 = 1.414213562373095;
    
    public static double[] SIN_TABLE = new double[65536];
    static
    {
        for (int i = 0; i < 65536; ++i)
            SIN_TABLE[i] = Math.sin(i / 65536D * 2 * Math.PI);
        
        SIN_TABLE[0] = 0;
        SIN_TABLE[16384] = 1;
        SIN_TABLE[32768] = 0;
        SIN_TABLE[49152] = 1;
    }
    
    public static double sin(double d)
    {
        return SIN_TABLE[(int)((float)d * 10430.378F) & 65535];
    }
    
    public static double cos(double d)
    {
        return SIN_TABLE[(int)((float)d * 10430.378F + 16384.0F) & 65535];
    }
    
    /**
     * @param a The value
     * @param b The value to approach
     * @param max The maximum step
     * @return the closed value to b no less than max from a
     */
    public static float approachLinear(float a, float b, float max)
    {
        return (a > b) ?
                (a - b < max ? b : a-max) :
                (b - a < max ? b : a+max);
    }
    
    /**
     * @param a The value
     * @param b The value to approach
     * @param max The maximum step
     * @return the closed value to b no less than max from a
     */
    public static double approachLinear(double a, double b, double max)
    {
        return (a > b) ?
                (a - b < max ? b : a-max) :
                (b - a < max ? b : a+max);
    }

    /**
     * @param a The first value
     * @param b The second value
     * @param d The interpolation factor, between 0 and 1
     * @return a+(b-a)*d
     */
    public static float interpolate(float a, float b, float d)
    {
        return a+(b-a)*d;
    }

    /**
     * @param a The first value
     * @param b The second value
     * @param d The interpolation factor, between 0 and 1
     * @return a+(b-a)*d
     */
    public static double interpolate(double a, double b, double d)
    {
        return a+(b-a)*d;
    }
    
    /**
     * @param a The value
     * @param b The value to approach
     * @param ratio The ratio to reduce the difference by
     * @return a+(b-a)*ratio
     */
    public static double approachExp(double a, double b, double ratio)
    {
        return a+(b-a)*ratio;
    }

    /**
     * @param a The value
     * @param b The value to approach
     * @param ratio The ratio to reduce the difference by
     * @param cap The maximum amount to advance by
     * @return a+(b-a)*ratio
     */
    public static double approachExp(double a, double b, double ratio, double cap)
    {
        double d = (b-a)*ratio;
        if(Math.abs(d) > cap)
            d = Math.signum(d)*cap;
        return a+d;
    }

    /**
     * @param a The value
     * @param b The value to approach
     * @param ratio The ratio to reduce the difference by
     * @param c The value to retreat from
     * @param kick The difference when a == c
     * @return
     */
    public static double retreatExp(double a, double b, double c, double ratio, double kick)
    {
        double d = (Math.abs(c-a)+kick)*ratio;
        if(d > Math.abs(b-a))
            return b;
        return a+Math.signum(b-a)*d;
    }

    /**
     * 
     * @param value The value
     * @param min The min value
     * @param max The max value
     * @return The clipped value between min and max
     */
    public static double clip(double value, double min, double max)
    {
        if(value > max)
            value = max;
        if(value < min)
            value = min;
        return value;
    }

    /**
     * @return a <= x <= b
     */
    public static boolean between(double a, double x, double b)
    {
        return a <= x && x <= b;
    }

    public static int approachExpI(int a, int b, double ratio)
    {
        int r = (int)Math.round(approachExp(a, b, ratio));
        return r == a ? b : r;
    }

    public static int retreatExpI(int a, int b, int c, double ratio, int kick)
    {
        int r = (int)Math.round(retreatExp(a, b, c, ratio, kick));
        return r == a ? b : r;
    }
    
    public static int floor_double(double d)
    {
        return net.minecraft.util.MathHelper.floor_double(d);
    }
    
    public static int roundAway(double d)
    {
        return (int) (d < 0 ? Math.floor(d) : Math.ceil(d));
    }
    
    public static int compare(int a, int b)
    {
        return a == b ? 0 : a < b ? -1 : 1;
    }
    
    public static int compare(double a, double b)
    {
        return a == b ? 0 : a < b ? -1 : 1;
    }
}
