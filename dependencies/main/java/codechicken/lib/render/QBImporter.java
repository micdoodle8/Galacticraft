package codechicken.lib.render;

import codechicken.lib.render.uv.UV;
import codechicken.lib.render.uv.UVScale;
import codechicken.lib.vec.*;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.util.IIcon;
import net.minecraft.util.ResourceLocation;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.*;

public class QBImporter
{
    private static class ImagePackNode
    {
        Rectangle4i rect;
        ImagePackNode child1;
        ImagePackNode child2;
        QBImage packed;

        public ImagePackNode(int x, int y, int w, int h) {
            rect = new Rectangle4i(x, y, w, h);
        }

        public boolean pack(QBImage img) {
            if(child1 != null)
                return child1.pack(img) || child2.pack(img);

            if(packed != null)
                return false;

            int fit = getFit(img.width(), img.height());
            if(fit == 0)
                return false;

            if((fit & 2) != 0) {//exact fit
                packed = img;
                img.packSlot = rect;
                img.packT = new ImageTransform((fit&1)<<2);
                return true;
            }

            int w = (fit & 1) == 0 ? img.width() : img.height();
            int h = (fit & 1) == 0 ? img.height() : img.width();

            if(rect.w - w > rect.h - h) {//create split with biggest leftover space
                child1 = new ImagePackNode(rect.x, rect.y, w, rect.h);
                child2 = new ImagePackNode(rect.x+w, rect.y, rect.w-w, rect.h);
            } else {
                child1 = new ImagePackNode(rect.x, rect.y, rect.w, h);
                child2 = new ImagePackNode(rect.x, rect.y+h, rect.w, rect.h-h);
            }
            return child1.pack(img);
        }

        private int getFit(int w, int h) {
            if(w == rect.w && h == rect.h)
                return 2;
            if(w == rect.h && h == rect.w)
                return 3;
            if(rect.w >= w && rect.h >= h)
                return 4;
            if(rect.w >= h && rect.h >= w)
                return 5;

            return 0;
        }

        private static void nextSize(Rectangle4i rect, boolean square) {
            if(square) {
                rect.w <<= 1;
                rect.h <<= 1;
            }
            else {
                if(rect.w == rect.h)
                    rect.w *= 2;
                else
                    rect.h *= 2;
            }
        }

        public static ImagePackNode pack(List<QBImage> images, boolean square) {
            Collections.sort(images);

            int area = 0;
            for(QBImage img : images)
                area+=img.area();

            ImagePackNode node = new ImagePackNode(0, 0, 2, 2);
            while(node.rect.area() < area)
                nextSize(node.rect, square);

            while(true) {
                boolean packed = true;
                for(QBImage img : images)
                    if(!node.pack(img)) {
                        packed = false;
                        break;
                    }

                if(packed)
                    return node;

                node.child1 = node.child2 = null;
                nextSize(node.rect, square);
            }
        }

        public BufferedImage toImage() {
            BufferedImage img = new BufferedImage(rect.w, rect.h, BufferedImage.TYPE_INT_ARGB);
            write(img);
            return img;
        }

        private void write(BufferedImage img) {
            if(child1 != null) {
                child1.write(img);
                child2.write(img);
            }
            else if(packed != null) {
                ImageTransform t = packed.packT;
                for(int u = 0; u < rect.w; u++)
                    for(int v = 0; v < rect.h; v++) {
                        int rgba = t.access(packed, u, v);
                        img.setRGB(u+rect.x, v+rect.y, rgba>>>8 | rgba<<24);
                    }
            }
        }
    }

    private static class ImageTransform
    {
        int transform;

        public ImageTransform(int i) {
            transform = i;
        }

        public ImageTransform() {
            this(0);
        }

        public boolean transpose() {
            return (transform&4) != 0;
        }

        public boolean flipU() {
            return (transform&1) != 0;
        }

        public boolean flipV() {
            return (transform&2) != 0;
        }

        public int access(QBImage img, int u, int v) {
            if(transpose()) {
                int tmp = u; u = v; v = tmp;
            }
            if(flipU())
                u = img.width()-1-u;
            if(flipV())
                v = img.height()-1-v;

            return img.data[u][v];
        }

        public UV transform(UV uv) {
            if(transpose()) {
                double tmp = uv.u; uv.u = uv.v; uv.v = tmp;
            }
            if(flipU())
                uv.u = 1-uv.u;
            if(flipV())
                uv.v = 1-uv.v;

            return uv;
        }
    }

    public static class QBImage implements Comparable<QBImage>
    {
        int[][] data;
        ImageTransform packT;
        Rectangle4i packSlot;

        public int width() {
            return data.length;
        }

        public int height() {
            return data[0].length;
        }

        public int area() {
            return width()*height();
        }

        @Override
        public int compareTo(QBImage o) {
            int a = area(); int b = o.area();
            return a > b ? -1 : a == b ? 0 : 1;
        }

        public ImageTransform transformTo(QBImage img) {
            if(width() == img.width() && height() == img.height())
                for(int i = 0; i < 4; i++) {
                    ImageTransform t = new ImageTransform(i);
                    if(equals(img, t))
                        return t;
                }
            if(width() == img.height() && height() == img.width())
                for(int i = 4; i < 8; i++) {
                    ImageTransform t = new ImageTransform(i);
                    if(equals(img, t))
                        return t;
                }
            return null;
        }

        public boolean equals(QBImage img, ImageTransform t) {
            for(int u = 0; u < img.width(); u++)
                for(int v = 0; v < img.height(); v++)
                    if(t.access(this, u, v) != img.data[u][v])
                        return false;

            return true;
        }

        public void transform(UV uv) {
            packT.transform(uv);
            uv.u*=packSlot.w;
            uv.v*=packSlot.h;
            uv.u+=packSlot.x;
            uv.v+=packSlot.y;
        }
    }

    private static final int[][] vertOrder = new int[][] {//clockwise because MC is left handed
            {3, 0},
            {1, 0},
            {1, 2},
            {3, 2}};

    public static class QBQuad
    {
        public Vertex5[] verts = new Vertex5[4];
        public QBImage image = new QBImage();
        public ImageTransform t = new ImageTransform();
        public int side;

        public QBQuad(int side) {
            this.side = side;
        }

        public void applyImageT() {
            for(Vertex5 vert : verts) {
                t.transform(vert.uv);
                image.transform(vert.uv);
            }
        }


        public static QBQuad restore(Rectangle4i flat, int side, double d, QBImage img) {
            QBQuad quad = new QBQuad(side);
            quad.image = img;

            Transformation t = new Scale(-1, 1, -1).with(Rotation.sideOrientation(side, 0)).with(new Translation(new Vector3().setSide(side, d)));
            quad.verts[0] = new Vertex5(flat.x, 0, flat.y, 0, 0);
            quad.verts[1] = new Vertex5(flat.x+flat.w, 0, flat.y, 1, 0);
            quad.verts[2] = new Vertex5(flat.x+flat.w, 0, flat.y+flat.h, 1, 1);
            quad.verts[3] = new Vertex5(flat.x, 0, flat.y+flat.h, 0, 1);
            for(Vertex5 vert : quad.verts)
                vert.apply(t);

            return quad;
        }

        public Rectangle4i flatten() {
            Transformation t = Rotation.sideOrientation(side, 0).inverse().with(new Scale(-1, 0, -1));
            Vector3 vmin = verts[0].vec.copy().apply(t);
            Vector3 vmax = verts[2].vec.copy().apply(t);
            return new Rectangle4i((int)vmin.x, (int)vmin.z, (int)(vmax.x-vmin.x), (int)(vmax.z-vmin.z));
        }
    }

    public static class QBCuboid
    {
        public QBMatrix mat;
        public CuboidCoord c;
        public int sides;

        public QBCuboid(QBMatrix mat, CuboidCoord c) {
            this.mat = mat;
            this.c = c;
            sides = 0;
        }

        public static boolean intersects(QBCuboid a, QBCuboid b) {
            CuboidCoord c = a.c;
            CuboidCoord d = b.c;
            return c.min.x <= d.max.x &&
                    d.min.x <= c.max.x &&
                    c.min.y <= d.max.y &&
                    d.min.y <= c.max.y &&
                    c.min.z <= d.max.z &&
                    d.min.z <= c.max.z;
        }

        public static void clip(QBCuboid a, QBCuboid b) {
            if(intersects(a, b)) {
                a.clip(b);
                b.clip(a);
            }
        }

        public void clip(QBCuboid o) {
            CuboidCoord d = o.c;
            for(int a = 0; a < 6; a+=2)
            {
                int a1 = (a+2)%6;
                int a2 = (a+4)%6;
                if(c.getSide(a1+1) <= d.getSide(a1+1) &&
                    c.getSide(a1) >= d.getSide(a1) &&
                    c.getSide(a2+1) <= d.getSide(a2+1) &&
                    c.getSide(a2) >= d.getSide(a2)) {

                    if(c.getSide(a) <= d.getSide(a+1) && c.getSide(a) >= d.getSide(a)) {
                        c.setSide(a, d.getSide(a + 1) + 1);
                        sides|=1<<a;
                    }
                    if(c.getSide(a+1) >= d.getSide(a) && c.getSide(a+1) <= d.getSide(a+1)) {
                        c.setSide(a + 1, d.getSide(a) - 1);
                        sides|=2<<a;
                    }
                }
            }
        }

        public void extractQuads(List<QBQuad> quads) {
            Cuboid6 box = c.bounds();
            for(int s = 0; s < 6; s++)
                if((sides & 1<<s) == 0)
                    quads.add(extractQuad(s, box));
        }

        private QBQuad extractQuad(int side, Cuboid6 box) {
            double[] da = new double[3];
            da[side>>1] = box.getSide(side);

            QBQuad quad = new QBQuad(side);
            for(int i = 0; i < 4; i++) {
                int rU = vertOrder[i][0];
                int rV = vertOrder[i][1];
                int sideU = Rotation.rotateSide(side, rU);
                int sideV = Rotation.rotateSide(side, rV);
                da[sideU>>1] = box.getSide(sideU);
                da[sideV>>1] = box.getSide(sideV);
                quad.verts[i] = new Vertex5(Vector3.fromAxes(da), (3-rU)/2, rV/2);
            }

            int sideU = Rotation.rotateSide(side, 1);
            int sideV = Rotation.rotateSide(side, 2);
            quad.image.data = new int[c.size(sideU)][c.size(sideV)];
            QBImage image = quad.image;

            int[] ia = new int[3];
            ia[side>>1] = c.getSide(side); ia[sideU>>1] = c.getSide(sideU^1); ia[sideV>>1] = c.getSide(sideV^1);
            BlockCoord b = BlockCoord.fromAxes(ia);
            BlockCoord bU = BlockCoord.sideOffsets[sideU];
            BlockCoord bV = BlockCoord.sideOffsets[sideV];
            for(int u = 0; u < image.width(); u++)
                for(int v = 0; v < image.height(); v++)
                    image.data[u][v] = mat.matrix[b.x+bU.x*u+bV.x*v][b.y+bU.y*u+bV.y*v][b.z+bU.z*u+bV.z*v];

            return quad;
        }
    }

    public static class QBMatrix
    {
        public String name;
        public BlockCoord pos;
        public BlockCoord size;
        public int[][][] matrix;

        public void readMatrix(DataInputStream din, boolean compressed) throws IOException {
            if(compressed) {
                int z = 0;
                while (z < size.z)
                {
                    int index = 0;

                    while (true)
                    {
                        int data = din.readInt();

                        if (data == NEXTSLICEFLAG)
                            break;

                        if (data == CODEFLAG) {
                            int count = readTni(din);
                            data = din.readInt();

                            for(int j = 0; j < count; j++, index++)
                                matrix[index % size.x][index / size.x][z] = data;
                        }
                        else {
                            matrix[index % size.x][index / size.x][z] = data;
                            index++;
                        }
                    }
                    z++;
                }
            } else {
                for(int z = 0; z < size.z; z++)
                    for(int y = 0; y < size.y; y++)
                        for(int x = 0; x < size.x; x++)
                            matrix[x][y][z] = din.readInt();
            }
        }

        public void convertBGRAtoRGBA() {
            for(int z = 0; z < size.z; z++)
                for(int y = 0; y < size.y; y++)
                    for(int x = 0; x < size.x; x++) {
                        int i = matrix[x][y][z];
                        matrix[x][y][z] = Integer.reverseBytes(i>>>8)|i&0xFF;
                    }
        }

        private boolean voxelFull(boolean[][][] solid, CuboidCoord c) {
            for(BlockCoord b : c)
                if(matrix[b.x][b.y][b.z] == 0)
                    return false;
            for(BlockCoord b : c)
                solid[b.x][b.y][b.z] = false;
            return true;
        }

        private QBCuboid expand(boolean[][][] solid, BlockCoord b) {
            CuboidCoord c = new CuboidCoord(b);
            solid[b.x][b.y][b.z] = false;

            for(int s = 0; s < 6; s++) {
                CuboidCoord slice = c.copy();
                slice.expand(s^1, -(slice.size(s)-1));
                slice.expand(s, 1);

                while(slice.getSide(s) >= 0 && slice.getSide(s) < size.getSide(s)) {
                    if(!voxelFull(solid, slice))
                        break;
                    slice.expand(s ^ 1, -1);
                    slice.expand(s, 1);
                    c.expand(s, 1);
                }
            }
            return new QBCuboid(this, c);
        }

        public List<QBCuboid> rectangulate() {
            List<QBCuboid> list = new ArrayList<QBCuboid>();
            boolean[][][] solid = new boolean[size.x][size.y][size.z];
            for(int z = 0; z < size.z; z++)
                for(int y = 0; y < size.y; y++)
                    for(int x = 0; x < size.x; x++)
                        solid[x][y][z] = matrix[x][y][z] != 0;

            for(int x = 0; x < size.x; x++)
                for(int z = 0; z < size.z; z++)
                    for(int y = 0; y < size.y; y++)
                        if(solid[x][y][z])
                            list.add(expand(solid, new BlockCoord(x, y, z)));

            for(int i = 0; i < list.size(); i++)
                for(int j = i+1; j < list.size(); j++)
                    QBCuboid.clip(list.get(i), list.get(j));

            return list;
        }

        public List<QBQuad> extractQuads(boolean texturePlanes) {
            List<QBQuad> quads = new LinkedList<QBQuad>();
            for(QBCuboid c : rectangulate())
                c.extractQuads(quads);

            if(texturePlanes)
                optimisePlanes(quads);

            return quads;
        }

        private void optimisePlanes(List<QBQuad> quads) {
            Multimap<Integer, QBQuad> map = HashMultimap.create();
            for(QBQuad quad : quads)
                map.put(quad.side | ((int)quad.verts[0].vec.getSide(quad.side))<<3, quad);

            quads.clear();
            for(Integer key : map.keySet()) {
                Collection<QBQuad> plane = map.get(key);
                if(plane.size() == 1) {
                    quads.add(plane.iterator().next());
                    continue;
                }

                int side = key&7;
                Rectangle4i rect = null;
                for(QBQuad q : plane)
                    if(rect == null)
                        rect = q.flatten();
                    else
                        rect.include(q.flatten());

                QBImage img = new QBImage();
                img.data = new int[rect.w][rect.h];
                for(QBQuad q : plane) {
                    QBImage from = q.image;
                    Rectangle4i r = q.flatten();
                    int du = r.x-rect.x;
                    int dv = r.y-rect.y;
                    for(int u = 0; u < from.width(); u++)
                        for(int v = 0; v < from.height(); v++)
                            img.data[du+u][dv+v] = from.data[u][v];
                }

                quads.add(QBQuad.restore(rect, side, key>>3, img));
            }
        }

        public CCModel buildModel(List<QBQuad> quads, BufferedImage img, boolean scaleMC) {
            CCModel m = CCModel.quadModel(quads.size()*4);
            int i = 0;
            for(QBQuad quad : quads) {
                quad.applyImageT();
                m.verts[i++] = quad.verts[0];
                m.verts[i++] = quad.verts[1];
                m.verts[i++] = quad.verts[2];
                m.verts[i++] = quad.verts[3];
            }
            m.apply(new UVScale(1D/img.getWidth(), 1D/img.getHeight()));
            m.apply(new Translation(pos.x, pos.y, pos.z));
            if(scaleMC)
                m.apply(new Scale(1/16D));
            m.computeNormals();
            return m;
        }

        private static void addImages(List<QBQuad> quads, List<QBImage> images) {
            for(QBQuad q : quads) {
                QBImage img = q.image;
                boolean matched = false;
                for(QBImage img2 : images) {
                    ImageTransform t = img.transformTo(img2);
                    if(t != null) {
                        q.t = t;
                        q.image = img2;
                        matched = true;
                        break;
                    }
                }
                if(!matched)
                    images.add(img);
            }
        }
    }

    public static final int TEXTUREPLANES = 1;
    public static final int SQUARETEXTURE = 2;
    public static final int MERGETEXTURES = 4;
    public static final int SCALEMC = 8;
    public static class QBModel
    {
        public QBMatrix[] matrices;
        public boolean rightHanded;

        public RasterisedModel toRasterisedModel(int flags) {
            List<QBImage> qbImages = new ArrayList<QBImage>();
            List<List<QBQuad>> modelQuads = new ArrayList<List<QBQuad>>();
            List<BufferedImage> images = new ArrayList<BufferedImage>();

            boolean texturePlanes = (flags & TEXTUREPLANES) != 0;
            boolean squareTextures = (flags & SQUARETEXTURE) != 0;
            boolean mergeTextures = (flags & MERGETEXTURES) != 0;
            boolean scaleMC = (flags & SCALEMC) != 0;

            for(QBMatrix mat : matrices) {
                List<QBQuad> quads = mat.extractQuads(texturePlanes);
                modelQuads.add(quads);
                QBMatrix.addImages(quads, qbImages);
                if(!mergeTextures) {
                    images.add(ImagePackNode.pack(qbImages, squareTextures).toImage());
                    qbImages.clear();
                }
            }

            if(mergeTextures)
                images.add(ImagePackNode.pack(qbImages, squareTextures).toImage());

            RasterisedModel m = new RasterisedModel(images);
            for(int i = 0; i < matrices.length; i++) {
                QBMatrix mat = matrices[i];
                BufferedImage img = images.get(mergeTextures ? 0 : i);
                m.add(mat.name, mat.buildModel(modelQuads.get(i), img, scaleMC));
            }
            return m;
        }
    }

    public static class RasterisedModel
    {
        private class Holder {
            CCModel m;
            int img;

            public Holder(CCModel m, int img) {
                this.m = m;
                this.img = img;
            }
        }

        private Map<String, Holder> map = new HashMap<String, Holder>();
        private List<BufferedImage> images;
        private String[] icons;

        public RasterisedModel(List<BufferedImage> images) {
            this.images = images;
            icons = new String[images.size()];
        }

        public void add(String name, CCModel m) {
            map.put(name, new Holder(m, Math.min(map.size(), images.size()-1)));
        }

        public CCModel getModel(String key) {
            return map.get(key).m;
        }

        public IIcon getIcon(String key, IIconRegister r, String iconName) {
            int img = map.get(key).img;
            if(icons[img] != null && !iconName.equals(icons[img]))
                throw new IllegalArgumentException("Attempted to get a previously registered icon by a different name: "+icons[img]+", "+iconName);
            if(icons[img] != null)
                return r.registerIcon(iconName);

            icons[img] = iconName;
            return TextureUtils.getTextureSpecial(r, iconName).addTexture(new TextureDataHolder(images.get(img)));
        }

        private void exportImg(BufferedImage img, File imgFile) throws IOException {
            if(!imgFile.exists())
                imgFile.createNewFile();
            ImageIO.write(img, "PNG", imgFile);
        }

        public void export(File objFile, File imgDir) {
            try {
                if(!objFile.exists())
                    objFile.createNewFile();
                if(!imgDir.exists())
                    imgDir.mkdirs();

                Map<String, CCModel> modelMap = new HashMap<String, CCModel>();
                for(Map.Entry<String, Holder> e : map.entrySet())
                    modelMap.put(e.getKey(), e.getValue().m);

                PrintWriter p = new PrintWriter(objFile);
                CCModel.exportObj(modelMap, p);
                p.close();

                if(images.size() < map.size())
                    exportImg(images.get(0), new File(imgDir, objFile.getName().replaceAll("(.+)\\..+", "$1.png")));
                else
                    for(Map.Entry<String, Holder> e : map.entrySet())
                        exportImg(images.get(e.getValue().img), new File(imgDir, e.getKey()+".png"));

            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private static String readAsciiString(DataInputStream din) throws IOException {
        byte[] bytes = new byte[din.readByte()&0xFF];
        din.readFully(bytes);
        return new String(bytes, "US-ASCII");
    }

    private static int readTni(DataInputStream din) throws IOException {
        return Integer.reverseBytes(din.readInt());
    }

    private static final int CODEFLAG = Integer.reverseBytes(2);
    private static final int NEXTSLICEFLAG = Integer.reverseBytes(6);
    public static QBModel loadQB(InputStream input) throws IOException {
        DataInputStream din = new DataInputStream(input);

        QBModel m = new QBModel();
        int version = din.readInt();
        int colorFormat = din.readInt();
        m.rightHanded = din.readInt() != 0;
        boolean compressed = din.readInt() != 0;
        boolean visEncoded = din.readInt() != 0;

        if(visEncoded)
            throw new IllegalArgumentException("Encoded Visiblity States not supported");

        m.matrices = new QBMatrix[readTni(din)];
        for(int i = 0; i < m.matrices.length; i++) {
            QBMatrix mat = new QBMatrix();
            m.matrices[i] = mat;
            mat.name = readAsciiString(din);
            mat.size = new BlockCoord(readTni(din), readTni(din), readTni(din));
            mat.pos = new BlockCoord(readTni(din), readTni(din), readTni(din));
            mat.matrix = new int[mat.size.x][mat.size.y][mat.size.z];
            mat.readMatrix(din, compressed);
            if(colorFormat == 1)
                mat.convertBGRAtoRGBA();
        }

        return m;
    }

    public static QBModel loadQB(ResourceLocation res) {
        try {
            return loadQB(Minecraft.getMinecraft().getResourceManager().getResource(res).getInputStream());
        } catch(Exception e) {
            throw new RuntimeException("failed to load model: "+res, e);
        }
    }

    public static QBModel loadQB(File file) {
        try {
            FileInputStream fin = new FileInputStream(file);
            try {
                return loadQB(fin);
            } finally {
                fin.close();
            }
        } catch(Exception e) {
            throw new RuntimeException("failed to load model: "+file.getPath(), e);
        }
    }
}
