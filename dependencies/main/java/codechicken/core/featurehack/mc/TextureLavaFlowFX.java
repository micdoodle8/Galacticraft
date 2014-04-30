package codechicken.core.featurehack.mc;

import codechicken.lib.colour.ColourRGBA;
import codechicken.lib.render.TextureFX;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.util.MathHelper;

@SideOnly(Side.CLIENT)
public class TextureLavaFlowFX extends TextureFX
{
    protected float[] field_76871_g = new float[256];
    protected float[] field_76874_h = new float[256];
    protected float[] field_76875_i = new float[256];
    protected float[] field_76872_j = new float[256];
    int field_76873_k = 0;

    //shadow
    public int tileSizeBase = 16;
    public int tileSizeSquare = 256;
    public int tileSizeMask = 15;
    public int tileSizeSquareMask = 255;

    public TextureLavaFlowFX()
    {
        super(32, "lava_flow_fx");
    }

    @Override
    public void setup()
    {
        super.setup();
        field_76871_g = new float[tileSizeSquare];
        field_76874_h = new float[tileSizeSquare];
        field_76875_i = new float[tileSizeSquare];
        field_76872_j = new float[tileSizeSquare];
        field_76873_k = 0;
    }

    public void onTick()
    {
        ++this.field_76873_k;
        int var2;
        float var3;
        int var5;
        int var6;
        int var7;
        int var8;
        int var9;

        for (int var1 = 0; var1 < tileSizeBase; ++var1)
        {
            for (var2 = 0; var2 < tileSizeBase; ++var2)
            {
                var3 = 0.0F;
                int var4 = (int)(MathHelper.sin(var2 * (float)Math.PI * 2.0F / 16.0F) * 1.2F);
                var5 = (int)(MathHelper.sin(var1 * (float)Math.PI * 2.0F / 16.0F) * 1.2F);

                for (var6 = var1 - 1; var6 <= var1 + 1; ++var6)
                {
                    for (var7 = var2 - 1; var7 <= var2 + 1; ++var7)
                    {
                        var8 = var6 + var4 & tileSizeMask;
                        var9 = var7 + var5 & tileSizeMask;
                        var3 += this.field_76871_g[var8 + var9 * tileSizeBase];
                    }
                }

                this.field_76874_h[var1 + var2 * tileSizeBase] = var3 / 10.0F + (this.field_76875_i[(var1 + 0 & tileSizeMask) + (var2 + 0 & tileSizeMask) * tileSizeBase] + this.field_76875_i[(var1 + 1 & tileSizeMask) + (var2 + 0 & tileSizeMask) * tileSizeBase] + this.field_76875_i[(var1 + 1 & tileSizeMask) + (var2 + 1 & tileSizeMask) * tileSizeBase] + this.field_76875_i[(var1 + 0 & tileSizeMask) + (var2 + 1 & tileSizeMask) * tileSizeBase]) / 4.0F * 0.8F;
                this.field_76875_i[var1 + var2 * tileSizeBase] += this.field_76872_j[var1 + var2 * tileSizeBase] * 0.01F;

                if (this.field_76875_i[var1 + var2 * tileSizeBase] < 0.0F)
                {
                    this.field_76875_i[var1 + var2 * tileSizeBase] = 0.0F;
                }

                this.field_76872_j[var1 + var2 * tileSizeBase] -= 0.06F;

                if (Math.random() < 0.005D)
                {
                    this.field_76872_j[var1 + var2 * tileSizeBase] = 1.5F;
                }
            }
        }

        float[] var11 = this.field_76874_h;
        this.field_76874_h = this.field_76871_g;
        this.field_76871_g = var11;

        for (var2 = 0; var2 < tileSizeSquare; ++var2)
        {
            var3 = this.field_76871_g[(var2 - this.field_76873_k / 3 * tileSizeBase) & tileSizeSquareMask] * 2.0F;

            if (var3 > 1.0F)
            {
                var3 = 1.0F;
            }

            if (var3 < 0.0F)
            {
                var3 = 0.0F;
            }

            var5 = (int)(var3 * 100.0F + 155.0F);
            var6 = (int)(var3 * var3 * 255.0F);
            var7 = (int)(var3 * var3 * var3 * var3 * 128.0F);

            if (this.anaglyphEnabled)
            {
                var8 = (var5 * 30 + var6 * 59 + var7 * 11) / 100;
                var9 = (var5 * 30 + var6 * 70) / 100;
                int var10 = (var5 * 30 + var7 * 70) / 100;
                var5 = var8;
                var6 = var9;
                var7 = var10;
            }

            int px = var2&tileSizeMask;
            int py = var2/tileSizeBase;
            writeColour(px, py, var5, var6, var7, -1);
            writeColour(px+16, py, var5, var6, var7, -1);
            writeColour(px, py+16, var5, var6, var7, -1);
            writeColour(px+16, py+16, var5, var6, var7, -1);
        }
    }

    private void writeColour(int px, int py, int var5, int var6, int var7, int var8)
    {
        imageData[py*32+px] = new ColourRGBA(var5, var6, var7, var8).argb();
    }
}
