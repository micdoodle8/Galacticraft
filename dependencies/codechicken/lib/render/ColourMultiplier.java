package codechicken.lib.render;

import net.minecraft.client.renderer.Tessellator;
import codechicken.lib.colour.Colour;
import codechicken.lib.colour.ColourRGBA;
import codechicken.lib.vec.Vector3;

public class ColourMultiplier implements IVertexModifier
{
    public Colour colour;
    
    public ColourMultiplier(Colour colour)
    {
        this.colour = colour;
    }
    
    public ColourMultiplier(int colour)
    {
        this(new ColourRGBA(colour));
    }
    
    @Override
    public void applyModifiers(CCModel m, Tessellator tess, Vector3 vec, UV uv, Vector3 normal, int i)
    {
        if(CCRenderState.useModelColours() && m != null && m.colours != null)
            CCRenderState.vertexColour(new ColourRGBA(m.colours[i]).multiply(colour).rgba());
    }

    @Override
    public boolean needsNormals()
    {
        return false;
    }
}
