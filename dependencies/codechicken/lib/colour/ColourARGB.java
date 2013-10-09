package codechicken.lib.colour;

public class ColourARGB extends Colour
{
    public ColourARGB(int colour)
    {
        super((colour >> 16) & 0xFF, (colour >> 8) & 0xFF, colour & 0xFF, (colour >> 24) & 0xFF);
    }
    
    public ColourARGB(int a, int r, int g, int b)
    {
        super(r, g, b, a);
    }

    public ColourARGB(ColourARGB colour)
    {
        super(colour);
    }

    public ColourARGB copy()
    {
        return new ColourARGB(this);
    }

    public int pack()
    {
        return pack(this);
    }

    public static int pack(Colour colour)
    {
        return (colour.a&0xFF) << 24 | (colour.r&0xFF) << 16 | (colour.g&0xFF) << 8 | (colour.b&0xFF);
    }
}
