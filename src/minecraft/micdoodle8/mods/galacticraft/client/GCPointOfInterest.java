package micdoodle8.mods.galacticraft.client;

public class GCPointOfInterest 
{
    public int x;
    public int y;
    public int z;
    public boolean enable;
    public float red;
    public float green;
    public float blue;
    public int type;

    public GCPointOfInterest(int var2, int var3, int var4, boolean var5, float var6, float var7, float var8)
    {
        this.x = var2;
        this.y = var3;
        this.z = var4;
        this.enable = var5;
        this.red = var6;
        this.green = var7;
        this.blue = var8;
    }

    public GCPointOfInterest(int var2, int var3, int var4, boolean var5, float var6, float var7, float var8, int var9)
    {
        this.x = var2;
        this.y = var3;
        this.z = var4;
        this.enable = var5;
        this.red = var6;
        this.green = var7;
        this.blue = var8;
        this.type = Math.max(0, var9 <= 1 ? var9 : 0);
    }

    public GCPointOfInterest(GCPointOfInterest var1)
    {
        this.set(var1);
    }

    public void set(GCPointOfInterest var1)
    {
        this.x = var1.x;
        this.y = var1.y;
        this.z = var1.z;
        this.enable = var1.enable;
        this.red = var1.red;
        this.green = var1.green;
        this.blue = var1.blue;
        this.type = Math.max(0, var1.type <= 1 ? var1.type : 0);
    }

    static GCPointOfInterest load(String var0)
    {
        try
        {
            String[] var1 = var0.split(":");
            int var3 = Integer.parseInt(var1[1]);
            int var4 = Integer.parseInt(var1[2]);
            int var5 = Integer.parseInt(var1[3]);
            boolean var6 = Boolean.parseBoolean(var1[4]);
            int var7 = Integer.parseInt(var1[5], 16);
            float var8 = (float)(var7 >> 16 & 255) / 255.0F;
            float var9 = (float)(var7 >> 8 & 255) / 255.0F;
            float var10 = (float)(var7 >> 0 & 255) / 255.0F;
            int var11 = var1.length >= 7 ? Integer.parseInt(var1[6]) : 0;
            return new GCPointOfInterest(var3, var4, var5, var6, var8, var9, var10, var11);
        }
        catch (RuntimeException var12)
        {
            var12.printStackTrace();
            return null;
        }
    }
}
