package codechicken.lib.lighting;

import codechicken.lib.render.CCModel;
import codechicken.lib.render.CCRenderState;
import codechicken.lib.render.IVertexModifier;
import codechicken.lib.render.UV;
import codechicken.lib.vec.BlockCoord;
import codechicken.lib.vec.Vector3;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.world.IBlockAccess;

/**
 * Note that when using the class as a vertex transformer, the vertices are assumed to be within the BB (x, y, z) -> (x+1, y+1, z+1)
 */
public class LightMatrix implements IVertexModifier
{
    public int computed = 0;
    public float[][] ao = new float[13][4];
    public int[][] brightness = new int[13][4];
    public BlockCoord pos = new BlockCoord();
    
    private float[] aSamples = new float[27];
    private int[] bSamples = new int[27];
    private Vector3 v_temp = new Vector3();
    
    /**
     * The 9 positions in the sample array for each side, sides >= 6 are centered on sample 13 (the block itself)
     */
    public static final int[][] ssamplem = new int[][]{
        { 0, 1, 2, 3, 4, 5, 6, 7, 8},
        {18,19,20,21,22,23,24,25,26},
        { 0, 9,18, 1,10,19, 2,11,20},
        { 6,15,24, 7,16,25, 8,17,26},
        { 0, 3, 6, 9,12,15,18,21,24},
        { 2, 5, 8,11,14,17,20,23,26},
        { 9,10,11,12,13,14,15,16,17},
        { 9,10,11,12,13,14,15,16,17},
        { 3,12,21, 4,13,22, 5,14,23},
        { 3,12,21, 4,13,22, 5,14,23},
        { 1, 4, 7,10,13,16,19,22,25},
        { 1, 4, 7,10,13,16,19,22,25},
        {13,13,13,13,13,13,13,13,13}};
    public static final int[][] qsamplem = new int[][]{//the positions in the side sample array for each corner
        {0,1,3,4},
        {5,1,2,4},
        {6,7,3,4},
        {5,7,8,4}};
    public static final float[] sideao = new float[]{
        0.5F, 1F, 0.8F, 0.8F, 0.6F, 0.6F,
        0.5F, 1F, 0.8F, 0.8F, 0.6F, 0.6F,
        1F};
    
    /*static
    {
        int[][] os = new int[][]{
                {0,-1,0},
                {0, 1,0},
                {0,0,-1},
                {0,0, 1},
                {-1,0,0},
                { 1,0,0}};
        
        for(int s = 0; s < 12; s++)
        {
            int[] d0 = s < 6 ? new int[]{os[s][0]+1, os[s][1]+1, os[s][2]+1} : new int[]{1, 1, 1};
            int[] d1 = os[((s&0xE)+3)%6];
            int[] d2 = os[((s&0xE)+5)%6];
            for(int a = -1; a <= 1; a++)
                for(int b = -1; b <= 1; b++)
                    ssamplem[s][(a+1)*3+b+1] = (d0[1]+d1[1]*a+d2[1]*b)*9+(d0[2]+d1[2]*a+d2[2]*b)*3+(d0[0]+d1[0]*a+d2[0]*b);
        }
        System.out.println(Arrays.deepToString(ssamplem));
    }*/
    
    public void computeAt(IBlockAccess a, int x, int y, int z)
    {
        pos.set(x, y, z);
        computed = 0;
        //inc x, inc z, inc y
        sample( 0, aSamples, bSamples, a, x-1, y-1, z-1);
        sample( 1, aSamples, bSamples, a, x  , y-1, z-1);
        sample( 2, aSamples, bSamples, a, x+1, y-1, z-1);
        sample( 3, aSamples, bSamples, a, x-1, y-1, z  );
        sample( 4, aSamples, bSamples, a, x  , y-1, z  );
        sample( 5, aSamples, bSamples, a, x+1, y-1, z  );
        sample( 6, aSamples, bSamples, a, x-1, y-1, z+1);
        sample( 7, aSamples, bSamples, a, x  , y-1, z+1);
        sample( 8, aSamples, bSamples, a, x+1, y-1, z+1);
        sample( 9, aSamples, bSamples, a, x-1, y  , z-1);
        sample(10, aSamples, bSamples, a, x  , y  , z-1);
        sample(11, aSamples, bSamples, a, x+1, y  , z-1);
        sample(12, aSamples, bSamples, a, x-1, y  , z  );
        sample(13, aSamples, bSamples, a, x  , y  , z  );
        sample(14, aSamples, bSamples, a, x+1, y  , z  );
        sample(15, aSamples, bSamples, a, x-1, y  , z+1);
        sample(16, aSamples, bSamples, a, x  , y  , z+1);
        sample(17, aSamples, bSamples, a, x+1, y  , z+1);
        sample(18, aSamples, bSamples, a, x-1, y+1, z-1);
        sample(19, aSamples, bSamples, a, x  , y+1, z-1);
        sample(20, aSamples, bSamples, a, x+1, y+1, z-1);
        sample(21, aSamples, bSamples, a, x-1, y+1, z  );
        sample(22, aSamples, bSamples, a, x  , y+1, z  );
        sample(23, aSamples, bSamples, a, x+1, y+1, z  );
        sample(24, aSamples, bSamples, a, x-1, y+1, z+1);
        sample(25, aSamples, bSamples, a, x  , y+1, z+1);
        sample(26, aSamples, bSamples, a, x+1, y+1, z+1);
    }
    
    public int[] brightness(int side)
    {
        sideSample(side);
        return brightness[side];
    }
    
    public float[] ao(int side)
    {
        sideSample(side);
        return ao[side];
    }
    
    public void sideSample(int side)
    {
        if((computed&1<<side) == 0)
        {
            int[] ssample = ssamplem[side];
            for(int q = 0; q < 4; q++)
            {
                int[] qsample = qsamplem[q];
                if(Minecraft.isAmbientOcclusionEnabled())
                    interp(side, q, ssample[qsample[0]], ssample[qsample[1]], ssample[qsample[2]], ssample[qsample[3]]);
                else
                    interp(side, q, ssample[4], ssample[4], ssample[4], ssample[4]);
            }
            computed|=1<<side;
        }
    }

    private void sample(int i, float[] aSamples, int[] bSamples, IBlockAccess a, int x, int y, int z)
    {
        int bid = a.getBlockId(x, y, z);
        Block b = Block.blocksList[bid];
        if(b == null)
        {
            bSamples[i] = a.getLightBrightnessForSkyBlocks(x, y, z, 0);
            aSamples[i] = 1;
        }
        else
        {
            bSamples[i] = a.getLightBrightnessForSkyBlocks(x, y, z, b.getLightValue(a, x, y, z));
            aSamples[i] = b.getAmbientOcclusionLightValue(a, x, y, z);
        }
    }
    
    private void interp(int s, int q, int a, int b, int c, int d)
    {
        ao[s][q] = interpAO(aSamples[a], aSamples[b], aSamples[c], aSamples[d])*sideao[s];
        brightness[s][q] = interpBrightness(bSamples[a], bSamples[b], bSamples[c], bSamples[d]);
    }
    
    public static float interpAO(float a, float b, float c, float d)
    {
        return (a+b+c+d)/4F;
    }
    
    public static int interpBrightness(int a, int b, int c, int d)
    {
        if(a == 0)
            a = d;
        if(b == 0)
            b = d;
        if(c == 0)
            c = d;
        return (a+b+c+d)>>2 & 0xFF00FF;
    }

    public void setColour(Tessellator tess, LC lc, int c)
    {
        float[] a = ao(lc.side);
        float f = (a[0]*lc.fa + a[1]*lc.fb + a[2]*lc.fc + a[3]*lc.fd);
        CCRenderState.vertexColour((int)((c>>>24)*f), (int)((c>>16&0xFF)*f), (int)((c>>8&0xFF)*f), (c&0xFF));
    }

    public void setBrightness(Tessellator tess, LC lc)
    {
        int[] b = brightness(lc.side);
        tess.setBrightness((int)(b[0]*lc.fa + b[1]*lc.fb + b[2]*lc.fc+b[3]*lc.fd) & 0xFF00FF);
    }
    
    @Override
    public void applyModifiers(CCModel m, Tessellator tess, Vector3 vec, UV uv, Vector3 normal, int i)
    {
        LC lc;
        if(m instanceof CCRBModel)
            lc = ((CCRBModel)m).lightCoefficents[i];
        else
            lc = LC.compute(v_temp.set(vec).add(-pos.x, -pos.y, -pos.z), normal);
        
        setColour(tess, lc, (m == null || m.colours == null) ? -1 : m.colours[i]);
        setBrightness(tess, lc);
    }
    
    @Override
    public boolean needsNormals()
    {
        return true;
    }
}