package codechicken.lib.lighting;

import net.minecraft.client.renderer.Tessellator;
import codechicken.lib.render.CCModel;
import codechicken.lib.render.CCRenderState;
import codechicken.lib.render.IVertexModifier;
import codechicken.lib.render.UV;
import codechicken.lib.vec.Rotation;
import codechicken.lib.vec.Vector3;

public class LightModel implements IVertexModifier
{
    public static class Light
    {
        public Vector3 ambient = new Vector3();
        public Vector3 diffuse = new Vector3();
        public Vector3 position;
        
        public Light(Vector3 pos)
        {
            position = pos.copy().normalize();
        }
        
        public Light setDiffuse(Vector3 vec)
        {
            diffuse.set(vec);
            return this;
        }
        
        public Light setAmbient(Vector3 vec)
        {
            ambient.set(vec);
            return this;
        }
    }
    
    public static LightModel standardLightModel;
    static
    {
        standardLightModel = new LightModel()
                .setAmbient(new Vector3(0.4, 0.4, 0.4))
                .addLight(new Light(new Vector3(0.2, 1, -0.7))
                    .setDiffuse(new Vector3(0.6, 0.6, 0.6)))
                .addLight(new Light(new Vector3(-0.2, 1, 0.7))
                    .setDiffuse(new Vector3(0.6, 0.6, 0.6)));
    }
    
    private Vector3 ambient = new Vector3();
    private Light[] lights = new Light[8];
    private int lightCount;
    
    public LightModel addLight(Light light)
    {
        lights[lightCount++] = light;
        return this;
    }
    
    public LightModel setAmbient(Vector3 vec)
    {
        ambient.set(vec);
        return this;
    }

    /**
     * @param colour The pre-lighting vertex colour. RGBA format
     * @param normal The normal at the vertex
     * @return The lighting applied colour
     */
    public int apply(int colour, Vector3 normal)
    {
        Vector3 n_colour = ambient.copy();
        for(int l = 0; l < lightCount; l++)
        {
            Light light = lights[l];
            double n_l = light.position.dotProduct(normal);
            double f = n_l > 0 ? 1 : 0;
            n_colour.x += light.ambient.x + f*light.diffuse.x*n_l;
            n_colour.y += light.ambient.y + f*light.diffuse.y*n_l;
            n_colour.z += light.ambient.z + f*light.diffuse.z*n_l;
        }

        if(n_colour.x > 1)
            n_colour.x = 1;
        if(n_colour.y > 1)
            n_colour.y = 1;
        if(n_colour.z > 1)
            n_colour.z = 1;
        
        n_colour.multiply((colour >>> 24)/255D, (colour >> 16 & 0xFF)/255D, (colour >> 8 & 0xFF)/255D);
        return (int)(n_colour.x*255)<<24 | (int)(n_colour.y*255)<<16 | (int)(n_colour.z*255)<<8 | colour&0xFF;
    }
    
    @Override
    public void applyModifiers(CCModel m, Tessellator tess, Vector3 vec, UV uv, Vector3 normal, int i)
    {
        CCRenderState.setColour(apply((m == null || m.colours == null) ? -1 : m.colours[i], normal));
    }
    
    @Override
    public boolean needsNormals()
    {
        return true;
    }
    
    public PlanarLightModel reducePlanar()
    {
        int[] colours = new int[6];
        for(int i = 0; i < 6; i++)
            colours[i] = apply(-1, Rotation.axes[i]);
        return new PlanarLightModel(colours);
    }
}
