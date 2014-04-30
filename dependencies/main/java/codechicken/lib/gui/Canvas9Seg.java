package codechicken.lib.gui;

import codechicken.lib.render.CCRenderState;
import codechicken.lib.render.TextureDataHolder;
import codechicken.lib.render.TextureUtils;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.ResourceLocation;

public class Canvas9Seg
{
    public final ResourceLocation tex;
    public float[] seg_u = new float[4];
    public float[] seg_v = new float[4];
    public int[] seg_w = new int[3];
    public int[] seg_h = new int[3];

    public Canvas9Seg(ResourceLocation tex) {
        this.tex = tex;
        load();
    }

    private int[] readMarkers(TextureDataHolder data, int stride, int size) {
        int[] markers = new int[4];

        int marker = 1;
        int prev_col = data.data[0];
        for(int i = 1; i < size; i++) {
            if(data.data[i*stride] != prev_col) {
                markers[marker] = i;
                prev_col = data.data[i*stride];
                if(++marker == 4)
                    break;
            }
        }

        markers[0] += 1;
        markers[3] -= 1;
        return markers;
    }

    private void parseMarkers(TextureDataHolder data, int stride, int size, int[] sizes, float[] texcoords) {
        int[] markers = readMarkers(data, stride, size);
        for(int i = 0; i < 4; i++) {
            texcoords[i] = markers[i]/(float)size;
            if(i > 0)
                sizes[i-1] = markers[i]-markers[i-1];
        }
    }

    private void load() {
        TextureDataHolder data = TextureUtils.loadTexture(tex);
        parseMarkers(data, 1, data.width, seg_w, seg_u);
        parseMarkers(data, data.width, data.height, seg_h, seg_v);
    }

    private void drawSeg(int[] sw, int[] sh, int seg) {
        Tessellator t = Tessellator.instance;
        int u = seg%3; int v = seg/3;
        t.addVertexWithUV(sw[u], sh[v], 0, seg_u[u], seg_v[v]);
        t.addVertexWithUV(sw[u], sh[v+1], 0, seg_u[u], seg_v[v+1]);
        t.addVertexWithUV(sw[u+1], sh[v+1], 0, seg_u[u+1], seg_v[v+1]);
        t.addVertexWithUV(sw[u+1], sh[v], 0, seg_u[u+1], seg_v[v]);
    }

    public void draw(int x, int y, int w, int h) {
        CCRenderState.changeTexture(tex);
        CCRenderState.reset();
        CCRenderState.startDrawing();

        int[] sw = new int[]{x, x+seg_w[0], x+w-seg_w[2], x+w};
        int[] sh = new int[]{y, y+seg_h[0], y+h-seg_h[2], y+h};

        for(int seg = 0; seg < 9; seg++)
            drawSeg(sw, sh, seg);

        CCRenderState.draw();
    }
}
