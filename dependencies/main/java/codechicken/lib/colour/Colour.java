package codechicken.lib.colour;

import codechicken.lib.config.ConfigTag.IConfigType;
import codechicken.lib.math.MathHelper;
import codechicken.lib.util.Copyable;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class Colour implements Copyable<Colour> {
    public static IConfigType<Colour> configRGB = new IConfigType<Colour>() {
        @Override
        public String configValue(Colour entry) {
            String s = Long.toString(((long) entry.rgb()) << 32 >>> 32, 16);
            while (s.length() < 6) {
                s = "0" + s;
            }
            return "0x" + s.toUpperCase();
        }

        private final Pattern patternRGB = Pattern.compile("(\\d+),(\\d+),(\\d+)");

        @Override
        public Colour valueOf(String text) throws Exception {
            Matcher matcherRGB = patternRGB.matcher(text.replaceAll("\\s", ""));
            if (matcherRGB.matches()) {
                return new ColourRGBA(Integer.parseInt(matcherRGB.group(1)), Integer.parseInt(matcherRGB.group(2)), Integer.parseInt(matcherRGB.group(3)), 0xFF);
            }

            int hex = (int) Long.parseLong(text.replace("0x", ""), 16);
            return new ColourRGBA(hex << 8 | 0xFF);
        }
    };

    public byte r;
    public byte g;
    public byte b;
    public byte a;

    public Colour(int r, int g, int b, int a) {
        this.r = (byte) r;
        this.g = (byte) g;
        this.b = (byte) b;
        this.a = (byte) a;
    }

    public Colour(Colour colour) {
        r = colour.r;
        g = colour.g;
        b = colour.b;
        a = colour.a;
    }

    @SideOnly(Side.CLIENT)
    public void glColour() {
        GlStateManager.color((r & 0xFF) / 255F, (g & 0xFF) / 255F, (b & 0xFF) / 255F, (a & 0xFF) / 255F);
    }

    @SideOnly(Side.CLIENT)
    public void glColour(int a) {
        GlStateManager.color((r & 0xFF) / 255F, (g & 0xFF) / 255F, (b & 0xFF) / 255F, a / 255F);
    }

    public abstract int pack();

    @Override
    public String toString() {
        return getClass().getSimpleName() + "[0x" + Integer.toHexString(pack()).toUpperCase() + "]";
    }

    public Colour add(Colour colour2) {
        a += colour2.a;
        r += colour2.r;
        g += colour2.g;
        b += colour2.b;
        return this;
    }

    public Colour sub(Colour colour2) {
        int ia = (a & 0xFF) - (colour2.a & 0xFF);
        int ir = (r & 0xFF) - (colour2.r & 0xFF);
        int ig = (g & 0xFF) - (colour2.g & 0xFF);
        int ib = (b & 0xFF) - (colour2.b & 0xFF);
        a = (byte) (ia < 0 ? 0 : ia);
        r = (byte) (ir < 0 ? 0 : ir);
        g = (byte) (ig < 0 ? 0 : ig);
        b = (byte) (ib < 0 ? 0 : ib);
        return this;
    }

    public Colour invert() {
        a = (byte) (0xFF - (a & 0xFF));
        r = (byte) (0xFF - (r & 0xFF));
        g = (byte) (0xFF - (g & 0xFF));
        b = (byte) (0xFF - (b & 0xFF));
        return this;
    }

    public Colour multiply(Colour colour2) {
        a = (byte) ((a & 0xFF) * ((colour2.a & 0xFF) / 255D));
        r = (byte) ((r & 0xFF) * ((colour2.r & 0xFF) / 255D));
        g = (byte) ((g & 0xFF) * ((colour2.g & 0xFF) / 255D));
        b = (byte) ((b & 0xFF) * ((colour2.b & 0xFF) / 255D));
        return this;
    }

    public Colour scale(double d) {
        a = (byte) ((a & 0xFF) * d);
        r = (byte) ((r & 0xFF) * d);
        g = (byte) ((g & 0xFF) * d);
        b = (byte) ((b & 0xFF) * d);
        return this;
    }

    public Colour interpolate(Colour colour2, double d) {
        return this.add(colour2.copy().sub(this).scale(d));
    }

    public Colour multiplyC(double d) {
        r = (byte) MathHelper.clip((r & 0xFF) * d, 0, 255);
        g = (byte) MathHelper.clip((g & 0xFF) * d, 0, 255);
        b = (byte) MathHelper.clip((b & 0xFF) * d, 0, 255);

        return this;
    }

    public abstract Colour copy();

    public int rgb() {
        return (r & 0xFF) << 16 | (g & 0xFF) << 8 | (b & 0xFF);
    }

    public int argb() {
        return (a & 0xFF) << 24 | (r & 0xFF) << 16 | (g & 0xFF) << 8 | (b & 0xFF);
    }

    public int rgba() {
        return (r & 0xFF) << 24 | (g & 0xFF) << 16 | (b & 0xFF) << 8 | (a & 0xFF);
    }

    public Colour set(Colour colour) {
        r = colour.r;
        g = colour.g;
        b = colour.b;
        a = colour.a;
        return this;
    }

    public boolean equals(Colour colour) {
        return colour != null && rgba() == colour.rgba();
    }
}
