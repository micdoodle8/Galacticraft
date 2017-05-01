package micdoodle8.mods.galacticraft.core.util;

import micdoodle8.mods.galacticraft.api.vector.Vector3;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.entities.player.GCPlayerStats;
import micdoodle8.mods.galacticraft.core.network.PacketSimple;
import micdoodle8.mods.galacticraft.core.network.PacketSimple.EnumSimplePacket;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.BlockPos;
import net.minecraftforge.fml.common.network.NetworkRegistry.TargetPoint;

public class ColorUtil
{
    //Credit to Mark Ransom @ StackOverflow for the colorwheel idea

    static Vector3 red = new Vector3(255, 0, 0);
    static Vector3 orange = new Vector3(255, 160, 0);
    static Vector3 yellow = new Vector3(255, 255, 0);
    static Vector3 green = new Vector3(0, 255, 0);
    static Vector3 cyan = new Vector3(0, 255, 255);
    static Vector3 blue = new Vector3(0, 0, 255);
    static Vector3 magenta = new Vector3(255, 0, 255);
    static Vector3 white = new Vector3(255, 255, 255);
    static Vector3 black = new Vector3(0, 0, 0);
    static Vector3 mud = new Vector3(94, 81, 74);

    static double[] colorwheelAngles = { -110D, -30D, 0D, 60D, 120D, 180D, 215D, 250D, 330D, 360D, 420D, 480D };
    static Vector3[] colorwheelColors = { blue, magenta, red, orange, yellow, green, cyan, blue, magenta, red, orange, yellow };

    private static Vector3 hue_to_rgb(double deg)
    {
        deg = deg % 360;

        double previous_angle = colorwheelAngles[1];

        for (int i = 2; i < colorwheelAngles.length - 2; i++)
        {
            Double angle = colorwheelAngles[i];
            if (deg <= angle)
            {
                return interpolateInArray(colorwheelColors, i, (angle - deg) / (angle - previous_angle));
            }
            previous_angle = angle;
        }

        return null;
    }

    private static double rgb_to_hue(Vector3 input)
    {
        double maxCol = Math.max(Math.max(input.x, input.y), input.z);
        if (maxCol <= 0)
        {
            return 0;
        }
        Vector3 rgb = input.scale(255D / maxCol);

        double mindist = 1024;
        int mini = 0;

        for (int i = 2; i < colorwheelAngles.length - 2; i++)
        {
            Vector3 color = colorwheelColors[i];
            double separation = color.distance(rgb);
            if (separation < mindist)
            {
                mindist = separation;
                mini = i;
            }
        }

        double separation1;
        double separation2;
        separation1 = colorwheelColors[mini - 1].distance(rgb);
        separation2 = colorwheelColors[mini + 1].distance(rgb);

        double hue;
        if (separation1 < separation2)
        {
            double separationtot = colorwheelColors[mini - 1].distance(colorwheelColors[mini]);
            hue = interpolateInArray(colorwheelAngles, mini, mindist / separationtot);
            if (hue < 0)
            {
                hue += 360D;
            }
        }
        else
        {
            double separationtot = colorwheelColors[mini + 1].distance(colorwheelColors[mini]);
            hue = interpolateInArray(colorwheelAngles, mini + 1, separation2 / separationtot);
            if (hue > 360D)
            {
                hue -= 360D;
            }
        }

        return hue;
    }

    private static double cubicInterpolate(double y0, double y1, double y2, double y3, double mu)
    {
        double a0, a1, a2, a3, mu2;

        mu2 = mu * mu;
        a3 = y3 - y2 - y0 + y1;
        a2 = y0 - y1 - a3;
        a1 = y2 - y0;
        a0 = y1;

        return (a3 * mu * mu2 + a2 * mu2 + a1 * mu + a0);
    }

    private static Vector3 interpolateInArray(Vector3[] array, int i, double mu)
    {
        Vector3 point0 = array[i + 1];
        Vector3 point1 = array[i];
        Vector3 point2 = array[i - 1];
        Vector3 point3 = array[i - 2];

        double x = cubicInterpolate(point0.x, point1.x, point2.x, point3.x, mu);
        double y = cubicInterpolate(point0.y, point1.y, point2.y, point3.y, mu);
        double z = cubicInterpolate(point0.z, point1.z, point2.z, point3.z, mu);

        return new Vector3(x, y, z);
    }

    private static double interpolateInArray(double[] array, int i, double mu)
    {
        return cubicInterpolate(array[i + 1], array[i], array[i - 1], array[i - 2], mu);
    }

    public static Vector3 addColorsRealistically(Vector3 color1, Vector3 color2)
    {
        double hue1 = ColorUtil.rgb_to_hue(color1);
        double hue2 = ColorUtil.rgb_to_hue(color2);
        if (hue1 - hue2 > 180D)
        {
            hue2 += 360D;
        }
        if (hue2 - hue1 > 180D)
        {
            hue1 += 360D;
        }
        double hueresult = (hue1 + hue2) / 2;
        if (hueresult > 360D)
        {
            hueresult -= 360D;
        }
        //TODO: if hue1 and hue2 differ by close to 180degrees, add in some 'mud' color
        //TODO: add greyscale here

        return ColorUtil.hue_to_rgb(hueresult).scale(1 / 255D);
    }

    public static int addColorsRealistically(int color1, int color2)
    {
        Vector3 result = addColorsRealistically(toVec3(color1), toVec3(color2));
        return fromVec3(result);
    }

    public static int addColorsBasic(int color1, int color2)
    {
        int g = color1 >> 8;
        int r = g >> 8;
        g &= 255;
        int b = color1 & 255;
        int gg = color2 >> 8;
        int rr = gg >> 8;
        gg &= 255;
        int bb = color2 & 255;
        
        r = (r + rr) / 2;
        g = (g + gg) / 2;
        b = (b + bb) / 2;

        r = r << 16;
        g = g << 8;
        return r | g | b;
    }

    
    public static Vector3 toVec3(int col)
    {
        int gg = col >> 8;
        int rr = gg >> 8;
        gg &= 255;
        int bb = col & 255;
        return new Vector3(rr / 255F, gg / 255F, bb / 255F);
    }

    public static int fromVec3(Vector3 result)
    {
        int r = (int) (result.x * 255D + 0.499D);
        int g = (int) (result.y * 255D + 0.499D);
        int b = (int) (result.z * 255D + 0.499D);
        r = r << 16;
        g = g << 8;
        return r | g | b;
    }

    public static int to32BitColor(int a, int r, int g, int b)
    {
        a = a << 24;
        r = r << 16;
        g = g << 8;

        return a | r | g | b;
    }

    public static int to32BitColorB(byte r, byte g, byte b)
    {
        int rr = (r & 255) << 16;
        int gg = (g & 255) << 8;

        return rr | gg | (b & 255);
    }

    public static int lighten(int col, float factor)
    {
        int gg = col >> 8;
        int rr = gg >> 8;
        gg &= 255;
        int bb = col & 255;
        rr *= (1F + factor);
        gg *= (1F + factor);
        bb *= (1F + factor);
        if (rr > 255)
        {
            rr = 255;
        }
        if (gg > 255)
        {
            gg = 255;
        }
        if (bb > 255)
        {
            bb = 255;
        }
        return rr << 16 | gg << 8 | bb;
    }

    /**
     * Lighten to the specified intensity
     * @param col
     * @return
     */
    public static int lightenFully(int col, int intensity)
    {
        if (intensity == 0)
            return 0;
        int gg = col >> 8;
        int rr = gg >> 8;
        gg &= 255;
        int bb = col & 255;
        rr = 255 - rr;
        gg = 255 - gg;
        bb = 255 - bb;
        double greyInvert = Math.min(rr, Math.min(gg, bb));
        double delta = Math.max(rr, Math.max(gg, bb));
        delta = (delta - greyInvert) / delta;
        if (greyInvert >= intensity)
        {
            return col;
        }
        double factor = (intensity - greyInvert) / intensity / Math.pow(delta, 0.6);
        rr -= 24;  //this -24 and math.pow(delta 0.6) found empirically, there's no science here!
        gg -= 24;
        bb -= 24;
        rr *= factor;
        gg *= factor;
        bb *= factor;
        if (rr > 255)
        {
            rr = 255;
        }
        if (gg > 255)
        {
            gg = 255;
        }
        if (bb > 255)
        {
            bb = 255;
        }
        rr = 255 - rr;
        gg = 255 - gg;
        bb = 255 - bb;
        if (rr > 255)
        {
            rr = 255;
        }
        if (gg > 255)
        {
            gg = 255;
        }
        if (bb > 255)
        {
            bb = 255;
        }
        return rr << 16 | gg << 8 | bb;
    }

    public static int toGreyscale(int col)
    {
        int gg = col >> 8;
        int grey = gg >> 8;
        grey += gg & 255;
        grey += col & 255;
        grey /= 3;
        return grey << 16 | grey << 8 | (grey & 255);
    }

    public static void sendUpdatedColorsToPlayer(GCPlayerStats stats)
    {
        int dimID = stats.getPlayer().get().dimension;
        GalacticraftCore.packetPipeline.sendTo(new PacketSimple(EnumSimplePacket.C_RECOLOR_ALL_GLASS, dimID, new Object[] { Integer.valueOf(stats.getGlassColor1()), Integer.valueOf(stats.getGlassColor2()), Integer.valueOf(stats.getGlassColor3()) }), stats.getPlayer().get());
    }
    
    public static void updateColorsForArea(int dimID, BlockPos pos, int range, int color1, int color2, int color3)
    {
        GalacticraftCore.packetPipeline.sendToAllAround(new PacketSimple(EnumSimplePacket.C_RECOLOR_ALL_GLASS, dimID, new Object[] { color1, color2, color3 }), new TargetPoint(dimID, pos.getX(), pos.getY(), pos.getZ(), range));
    }

    public static void setGLColor(int col)
    {
        int gg = col >> 8;
        int rr = gg >> 8;
        gg &= 255;
        int bb = col & 255;
        GlStateManager.color(rr / 255F, gg / 255F, bb / 255F, 1.0F);
    }
}
