package micdoodle8.mods.galacticraft.moon.client;

import net.minecraft.util.MathHelper;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class GCMoonColorizerGrass
{
    /** Color buffer for grass */
    private static int[] grassBuffer = new int[65536];

    public static void setGrassBiomeColorizer(int[] par0ArrayOfInteger)
    {
        GCMoonColorizerGrass.grassBuffer = par0ArrayOfInteger;
    }

    /**
     * Gets grass color from temperature and humidity. Args: temperature,
     * humidity
     */
    public static int getGrassColor(double par0, double par2)
    {
        par2 *= par0;
        int var4 = (int) (65536 - par0);
        int var5 = (int) (65536 - par2);
        var4 = MathHelper.clamp_int(var4, 0, 65536);
        var5 = MathHelper.clamp_int(var5, 0, 65536);
        return GCMoonColorizerGrass.grassBuffer[var4];
    }
}
