package codechicken.lib.render;

import codechicken.lib.lighting.LC;
import codechicken.lib.vec.Cuboid6;

public class BlockRenderer
{
    public static class BlockFace implements CCRenderState.IVertexSource
    {
        public Vertex5[] verts = new Vertex5[]{new Vertex5(), new Vertex5(), new Vertex5(), new Vertex5()};
        public LC[] lightCoords = new LC[]{new LC(), new LC(), new LC(), new LC()};
        public boolean lcComputed = false;
        public int side;

        @Override
        public Vertex5[] getVertices() {
            return verts;
        }

        @Override
        public <T> T getAttributes(CCRenderState.VertexAttribute<T> attr) {
            return attr == CCRenderState.lightCoordAttrib && lcComputed ? (T)lightCoords : null;
        }

        @Override
        public boolean hasAttribute(CCRenderState.VertexAttribute<?> attr) {
            return attr == CCRenderState.sideAttrib || attr == CCRenderState.lightCoordAttrib && lcComputed;
        }

        @Override
        public void prepareVertex() {
            CCRenderState.side = side;
        }

        public BlockFace computeLightCoords() {
            if(!lcComputed) {
                for (int i = 0; i < 4; i++)
                    lightCoords[i].compute(verts[i].vec, side);
                lcComputed = true;
            }
            return this;
        }

        public BlockFace loadCuboidFace(Cuboid6 c, int side) {
            double x1 = c.min.x;
            double x2 = c.max.x;
            double y1 = c.min.y;
            double y2 = c.max.y;
            double z1 = c.min.z;
            double z2 = c.max.z;
            double u1; double u2; double v1; double v2;
            this.side = side;
            lcComputed = false;

            switch(side) {
                case 0:
                    u1 = x1; v1 = z1;
                    u2 = x2; v2 = z2;
                    verts[0].set(x1, y1, z2, u1, v2, 0);
                    verts[1].set(x1, y1, z1, u1, v1, 0);
                    verts[2].set(x2, y1, z1, u2, v1, 0);
                    verts[3].set(x2, y1, z2, u2, v2, 0);
                    break;
                case 1:
                    u1 = x1; v1 = z1;
                    u2 = x2; v2 = z2;
                    verts[0].set(x2, y2, z2, u2, v2, 1);
                    verts[1].set(x2, y2, z1, u2, v1, 1);
                    verts[2].set(x1, y2, z1, u1, v1, 1);
                    verts[3].set(x1, y2, z2, u1, v2, 1);
                    break;
                case 2:
                    u1 = 1-x1; v1 = 1-y2;
                    u2 = 1-x2; v2 = 1-y1;
                    verts[0].set(x1, y1, z1, u1, v2, 2);
                    verts[1].set(x1, y2, z1, u1, v1, 2);
                    verts[2].set(x2, y2, z1, u2, v1, 2);
                    verts[3].set(x2, y1, z1, u2, v2, 2);
                    break;
                case 3:
                    u1 = x1; v1 = 1-y2;
                    u2 = x2; v2 = 1-y1;
                    verts[0].set(x2, y1, z2, u2, v2, 3);
                    verts[1].set(x2, y2, z2, u2, v1, 3);
                    verts[2].set(x1, y2, z2, u1, v1, 3);
                    verts[3].set(x1, y1, z2, u1, v2, 3);
                    break;
                case 4:
                    u1 = z1; v1 = 1-y2;
                    u2 = z2; v2 = 1-y1;
                    verts[0].set(x1, y1, z2, u2, v2, 4);
                    verts[1].set(x1, y2, z2, u2, v1, 4);
                    verts[2].set(x1, y2, z1, u1, v1, 4);
                    verts[3].set(x1, y1, z1, u1, v2, 4);
                    break;
                case 5:
                    u1 = 1-z1; v1 = 1-y2;
                    u2 = 1-z2; v2 = 1-y1;
                    verts[0].set(x2, y1, z1, u1, v2, 5);
                    verts[1].set(x2, y2, z1, u1, v1, 5);
                    verts[2].set(x2, y2, z2, u2, v1, 5);
                    verts[3].set(x2, y1, z2, u2, v2, 5);
            }
            return this;
        }
    }

    private static BlockFace face = new BlockFace();

    /**
     * Renders faces of a cuboid with texture coordinates mapped to match a standard minecraft block
     * @param bounds The bounding cuboid to render
     * @param sideMask A mask of faces not to render
     */
    public static void renderCuboid(Cuboid6 bounds, int sideMask) {
        if(sideMask == 0x3F)
            return;

        CCRenderState.setModel(face);
        for(int s = 0; s < 6; s++)
            if((sideMask & 1<<s) == 0) {
                face.loadCuboidFace(bounds, s);
                CCRenderState.render();
            }
    }
}
