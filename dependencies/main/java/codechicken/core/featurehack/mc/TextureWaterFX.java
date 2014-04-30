package codechicken.core.featurehack.mc;

import codechicken.lib.colour.ColourRGBA;
import codechicken.lib.render.TextureFX;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class TextureWaterFX extends TextureFX
{
    /** red RGB value for water texture */
    protected float[] red = new float[256];

    /** green RGB value for water texture */
    protected float[] green = new float[256];

    /** blue RGB value for water texture */
    protected float[] blue = new float[256];

    /** alpha RGB value for water texture */
    protected float[] alpha = new float[256];
    public TextureWaterFX()
    {
        super(16, "water_still_fx");
        setup();
    }

    @Override
    public void setup()
    {
        super.setup();
        red = new float[tileSizeSquare];
        green = new float[tileSizeSquare];
        blue = new float[tileSizeSquare];
        alpha = new float[tileSizeSquare];
    }

    public void onTick()
    {
        int var1;
        int var2;
        float var3;
        int var5;
        int var6;

        for (var1 = 0; var1 < tileSizeBase; ++var1)
        {
            for (var2 = 0; var2 < tileSizeBase; ++var2)
            {
                var3 = 0.0F;

                for (int var4 = var1 - 1; var4 <= var1 + 1; ++var4)
                {
                    var5 = var4 & tileSizeMask;
                    var6 = var2 & tileSizeMask;
                    var3 += this.red[var5 + var6 * tileSizeBase];
                }

                this.green[var1 + var2 * tileSizeBase] = var3 / 3.3F + this.blue[var1 + var2 * tileSizeBase] * 0.8F;
            }
        }

        for (var1 = 0; var1 < tileSizeBase; ++var1)
        {
            for (var2 = 0; var2 < tileSizeBase; ++var2)
            {
                this.blue[var1 + var2 * tileSizeBase] += this.alpha[var1 + var2 * tileSizeBase] * 0.05F;

                if (this.blue[var1 + var2 * tileSizeBase] < 0.0F)
                {
                    this.blue[var1 + var2 * tileSizeBase] = 0.0F;
                }

                this.alpha[var1 + var2 * tileSizeBase] -= 0.1F;

                if (Math.random() < 0.05D)
                {
                    this.alpha[var1 + var2 * tileSizeBase] = 0.5F;
                }
            }
        }

        float[] var12 = this.green;
        this.green = this.red;
        this.red = var12;

        for (var2 = 0; var2 < tileSizeSquare; ++var2)
        {
            var3 = this.red[var2];

            if (var3 > 1.0F)
            {
                var3 = 1.0F;
            }

            if (var3 < 0.0F)
            {
                var3 = 0.0F;
            }

            float var13 = var3 * var3;
            var5 = (int)(32.0F + var13 * 32.0F);
            var6 = (int)(50.0F + var13 * 64.0F);
            int var7 = 255;
            int var8 = (int)(146.0F + var13 * 50.0F);

            if (this.anaglyphEnabled)
            {
                int var9 = (var5 * 30 + var6 * 59 + var7 * 11) / 100;
                int var10 = (var5 * 30 + var6 * 70) / 100;
                int var11 = (var5 * 30 + var7 * 70) / 100;
                var5 = var9;
                var6 = var10;
                var7 = var11;
            }

            imageData[var2] = new ColourRGBA(var5, var6, var7, var8).argb();
        }
    }
}
