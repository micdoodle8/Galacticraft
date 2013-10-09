package codechicken.lib.colour;

public class ColourRGBA extends Colour
{
    public ColourRGBA(int colour)
    {
        super((colour >> 24) & 0xFF, (colour >> 16) & 0xFF, (colour >> 8) & 0xFF, colour & 0xFF);
    }

    public ColourRGBA(double r, double g, double b, double a)
    {
        super((int) (255 * r), (int) (255 * g), (int) (255 * b), (int) (255 * a));
    }

    public ColourRGBA(int r, int g, int b, int a)
    {
        super(r, g, b, a);
    }

    public ColourRGBA(ColourRGBA colour)
    {
        super(colour);
    }

    public int pack()
    {
        return pack(this);
    }

    @Override
    public Colour copy()
    {
        return new ColourRGBA(this);
    }

    public static int pack(Colour colour)
    {
        return (colour.r&0xFF) << 24 | (colour.g&0xFF) << 16 | (colour.b&0xFF) << 8 | (colour.a&0xFF);
    }
}
