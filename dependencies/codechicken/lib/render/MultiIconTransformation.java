package codechicken.lib.render;

import net.minecraft.util.Icon;

/**
 * Icon index is specified as (int)u>>1
 */
public class MultiIconTransformation implements IUVTransformation
{
    public Icon[] icons;
    
    public MultiIconTransformation(Icon... icons)
    {
        this.icons = icons;
    }
    
    @Override
    public void transform(UV texcoord)
    {
        int i = (int)texcoord.u>>1;
        Icon icon = icons[i%icons.length];
        texcoord.u = icon.getInterpolatedU(texcoord.u%2*16);
        texcoord.v = icon.getInterpolatedV(texcoord.v%2*16);
    }
    
    public static CCModel setIconIndex(CCModel m, int index)
    {
        return setIconIndex(m, 0, m.verts.length, index);
    }
    
    public static CCModel setIconIndex(CCModel m, int start, int end, int index)
    {
        for(int k = start; k < end; k++)
        {
            UV uv = m.verts[k].uv;
            uv.u = uv.u%2+index*2;
            uv.v %= 2;
        }
        return m;
    }
}
