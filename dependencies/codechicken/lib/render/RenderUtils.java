package codechicken.lib.render;

import static net.minecraftforge.client.IItemRenderer.ItemRenderType.ENTITY;
import static net.minecraftforge.client.IItemRenderer.ItemRendererHelper.BLOCK_3D;

import org.lwjgl.opengl.GL11;

import codechicken.lib.vec.Cuboid6;
import codechicken.lib.vec.Rectangle4i;
import codechicken.lib.vec.Transformation;
import codechicken.lib.vec.Rotation;
import codechicken.lib.vec.Vector3;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.Icon;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraftforge.client.IItemRenderer;
import net.minecraftforge.client.MinecraftForgeClient;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;

public class RenderUtils
{
    static Vector3[] vectors = new Vector3[8];
    static RenderItem uniformRenderItem = new RenderItem()
    {
        public boolean shouldBob()
        {
            return false;
        }
    };
    static EntityItem entityItem;
    
    static
    {
        for(int i = 0; i < vectors.length; i++)
            vectors[i] = new Vector3();
        
        uniformRenderItem.setRenderManager(RenderManager.instance);
        
        entityItem = new EntityItem(null);
        entityItem.hoverStart = 0;
    }
    
    public static void renderFluidQuad(Vector3 point1, Vector3 point2, Vector3 point3, Vector3 point4, Icon icon, double res)
    {
        double u1 = icon.getMinU();
        double du = icon.getMaxU()-icon.getMinU();
        double v2 = icon.getMaxV();
        double dv = icon.getMaxV()-icon.getMinV();
        
        Vector3 wide = vectors[0].set(point4).subtract(point1);
        Vector3 high = vectors[1].set(point1).subtract(point2);
        Tessellator t = Tessellator.instance;
        
        double wlen = wide.mag();
        double hlen = high.mag();
        
        double x = 0;
        while(x < wlen)
        {
            double rx = wlen - x;
            if(rx > res)
                rx = res;

            double y = 0;
            while(y < hlen)
            {
                double ry = hlen-y;
                if(ry > res)
                    ry = res;

                Vector3 dx1 = vectors[2].set(wide).multiply(x/wlen);
                Vector3 dx2 = vectors[3].set(wide).multiply((x+rx)/wlen);    
                Vector3 dy1 = vectors[4].set(high).multiply(y/hlen);    
                Vector3 dy2 = vectors[5].set(high).multiply((y+ry)/hlen);

                t.addVertexWithUV(point2.x+dx1.x+dy2.x, point2.y+dx1.y+dy2.y, point2.z+dx1.z+dy2.z, u1, v2-ry/res*dv);
                t.addVertexWithUV(point2.x+dx1.x+dy1.x, point2.y+dx1.y+dy1.y, point2.z+dx1.z+dy1.z, u1, v2);
                t.addVertexWithUV(point2.x+dx2.x+dy1.x, point2.y+dx2.y+dy1.y, point2.z+dx2.z+dy1.z, u1+rx/res*du, v2);
                t.addVertexWithUV(point2.x+dx2.x+dy2.x, point2.y+dx2.y+dy2.y, point2.z+dx2.z+dy2.z, u1+rx/res*du, v2-ry/res*dv);
                
                y+=ry;
            }
            
            x+=rx;
        }
    }
    
    public static void translateToWorldCoords(Entity entity, float frame)
    {       
        double interpPosX = entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * frame;
        double interpPosY = entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * frame;
        double interpPosZ = entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * frame;
        
        GL11.glTranslated(-interpPosX, -interpPosY, -interpPosZ);
    }
    
    public static void drawCuboidOutline(Cuboid6 c)
    {
        Tessellator var2 = Tessellator.instance;
        var2.startDrawing(3);
        var2.addVertex(c.min.x, c.min.y, c.min.z);
        var2.addVertex(c.max.x, c.min.y, c.min.z);
        var2.addVertex(c.max.x, c.min.y, c.max.z);
        var2.addVertex(c.min.x, c.min.y, c.max.z);
        var2.addVertex(c.min.x, c.min.y, c.min.z);
        var2.draw();
        var2.startDrawing(3);
        var2.addVertex(c.min.x, c.max.y, c.min.z);
        var2.addVertex(c.max.x, c.max.y, c.min.z);
        var2.addVertex(c.max.x, c.max.y, c.max.z);
        var2.addVertex(c.min.x, c.max.y, c.max.z);
        var2.addVertex(c.min.x, c.max.y, c.min.z);
        var2.draw();
        var2.startDrawing(1);
        var2.addVertex(c.min.x, c.min.y, c.min.z);
        var2.addVertex(c.min.x, c.max.y, c.min.z);
        var2.addVertex(c.max.x, c.min.y, c.min.z);
        var2.addVertex(c.max.x, c.max.y, c.min.z);
        var2.addVertex(c.max.x, c.min.y, c.max.z);
        var2.addVertex(c.max.x, c.max.y, c.max.z);
        var2.addVertex(c.min.x, c.min.y, c.max.z);
        var2.addVertex(c.min.x, c.max.y, c.max.z);
        var2.draw();
    }
    
    public static void renderFluidCuboid(Cuboid6 bound, Icon tex, double res)
    {
        renderFluidQuad(//bottom
                new Vector3(bound.min.x, bound.min.y, bound.min.z),
                new Vector3(bound.max.x, bound.min.y, bound.min.z),
                new Vector3(bound.max.x, bound.min.y, bound.max.z),
                new Vector3(bound.min.x, bound.min.y, bound.max.z), 
                tex, res);
        renderFluidQuad(//top
                new Vector3(bound.min.x, bound.max.y, bound.min.z),
                new Vector3(bound.min.x, bound.max.y, bound.max.z),
                new Vector3(bound.max.x, bound.max.y, bound.max.z),
                new Vector3(bound.max.x, bound.max.y, bound.min.z), 
                tex, res);
        renderFluidQuad(//-x
                new Vector3(bound.min.x, bound.max.y, bound.min.z),
                new Vector3(bound.min.x, bound.min.y, bound.min.z),
                new Vector3(bound.min.x, bound.min.y, bound.max.z),
                new Vector3(bound.min.x, bound.max.y, bound.max.z), 
                tex, res);
        renderFluidQuad(//+x
                new Vector3(bound.max.x, bound.max.y, bound.max.z),
                new Vector3(bound.max.x, bound.min.y, bound.max.z),
                new Vector3(bound.max.x, bound.min.y, bound.min.z),
                new Vector3(bound.max.x, bound.max.y, bound.min.z), 
                tex, res);
        renderFluidQuad(//-z
                new Vector3(bound.max.x, bound.max.y, bound.min.z),
                new Vector3(bound.max.x, bound.min.y, bound.min.z),
                new Vector3(bound.min.x, bound.min.y, bound.min.z),
                new Vector3(bound.min.x, bound.max.y, bound.min.z), 
                tex, res);
        renderFluidQuad(//+z
                new Vector3(bound.min.x, bound.max.y, bound.max.z),
                new Vector3(bound.min.x, bound.min.y, bound.max.z),
                new Vector3(bound.max.x, bound.min.y, bound.max.z),
                new Vector3(bound.max.x, bound.max.y, bound.max.z), 
                tex, res);
    }
    
    public static void renderBlockOverlaySide(int x, int y, int z, int side, double tx1, double tx2, double ty1, double ty2)
    {
        double[] points = new double[]{x - 0.009, x + 1.009, y - 0.009, y + 1.009, z - 0.009, z + 1.009};

        Tessellator tessellator = Tessellator.instance;
        switch(side)
        {
            case 0:
                tessellator.addVertexWithUV(points[0], points[2], points[4], tx1, ty1);
                tessellator.addVertexWithUV(points[1], points[2], points[4], tx2, ty1);
                tessellator.addVertexWithUV(points[1], points[2], points[5], tx2, ty2);
                tessellator.addVertexWithUV(points[0], points[2], points[5], tx1, ty2);
            break;
            case 1:
                tessellator.addVertexWithUV(points[1], points[3], points[4], tx2, ty1);
                tessellator.addVertexWithUV(points[0], points[3], points[4], tx1, ty1);
                tessellator.addVertexWithUV(points[0], points[3], points[5], tx1, ty2);
                tessellator.addVertexWithUV(points[1], points[3], points[5], tx2, ty2);
            break;
            case 2:
                tessellator.addVertexWithUV(points[0], points[3], points[4], tx2, ty1);
                tessellator.addVertexWithUV(points[1], points[3], points[4], tx1, ty1);
                tessellator.addVertexWithUV(points[1], points[2], points[4], tx1, ty2);
                tessellator.addVertexWithUV(points[0], points[2], points[4], tx2, ty2);
            break;
            case 3:
                tessellator.addVertexWithUV(points[1], points[3], points[5], tx2, ty1);
                tessellator.addVertexWithUV(points[0], points[3], points[5], tx1, ty1);
                tessellator.addVertexWithUV(points[0], points[2], points[5], tx1, ty2);
                tessellator.addVertexWithUV(points[1], points[2], points[5], tx2, ty2);
            break;
            case 4:
                tessellator.addVertexWithUV(points[0], points[3], points[5], tx2, ty1);
                tessellator.addVertexWithUV(points[0], points[3], points[4], tx1, ty1);
                tessellator.addVertexWithUV(points[0], points[2], points[4], tx1, ty2);
                tessellator.addVertexWithUV(points[0], points[2], points[5], tx2, ty2);
            break;
            case 5:
                tessellator.addVertexWithUV(points[1], points[3], points[4], tx2, ty1);
                tessellator.addVertexWithUV(points[1], points[3], points[5], tx1, ty1);
                tessellator.addVertexWithUV(points[1], points[2], points[5], tx1, ty2);
                tessellator.addVertexWithUV(points[1], points[2], points[4], tx2, ty2);
            break;
        }
    }

    public static boolean shouldRenderFluid(FluidStack stack)
    {
        return stack.amount > 0 && stack.getFluid() != null;
    }
    
    /**
     * @param stack The fluid stack to render
     * @return The icon of the fluid
     */
    public static Icon prepareFluidRender(FluidStack stack, int alpha)
    {
        GL11.glDisable(GL11.GL_LIGHTING);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        
        Fluid fluid = stack.getFluid();
        CCRenderState.setColour(fluid.getColor(stack)<<8|alpha);        
        TextureUtils.bindAtlas(fluid.getSpriteNumber());
        return fluid.getIcon(stack);
    }
    
    /**
     * Re-enables lighting and disables blending.
     */
    public static void postFluidRender()
    {
        GL11.glEnable(GL11.GL_LIGHTING);
        GL11.glDisable(GL11.GL_BLEND);
    }
    
    public static double fluidDensityToAlpha(double density)
    {
        return Math.pow(density, 0.4);
    }

    /**
     * Renders a fluid within a bounding box. 
     * If the fluid is a liquid it will render as a normal tank with height equal to density/bound.height.
     * If the fluid is a gas, it will render the full box with an alpha equal to density.
     * Warning, bound will be mutated if the fluid is a liquid
     * @param stack The fluid to render.
     * @param bound The box within which the fluid is contained.
     * @param density The volume of fluid / the capacity of the tank. For gases this determines the alpha, for liquids this determines the height.
     * @param res The resolution to render at.
     */
    public static void renderFluidCuboid(FluidStack stack, Cuboid6 bound, double density, double res)
    {
        if(!shouldRenderFluid(stack))
            return;
        
        int alpha = 255;
        if(stack.getFluid().isGaseous())
            alpha = (int) (fluidDensityToAlpha(density)*255);
        else
            bound.max.y = bound.min.y+(bound.max.y-bound.min.y)*density;
        
        Icon tex = prepareFluidRender(stack, alpha);
        CCRenderState.startDrawing(7);
        renderFluidCuboid(bound, tex, res);
        CCRenderState.draw();
        postFluidRender();
    }
    
    public static void renderFluidGauge(FluidStack stack, Rectangle4i rect, double density, double res)
    {
        if(!shouldRenderFluid(stack))
            return;
        
        int alpha = 255;
        if(stack.getFluid().isGaseous())
            alpha = (int) (fluidDensityToAlpha(density)*255);
        else
        {
            int height = (int) (rect.h*density);
            rect.y +=rect.h-height;
            rect.h = height;
        }
        
        Icon tex = prepareFluidRender(stack, alpha);
        CCRenderState.startDrawing(7);
        renderFluidQuad(
                new Vector3(rect.x, rect.y, 0),
                new Vector3(rect.x, rect.y+rect.h, 0),
                new Vector3(rect.x+rect.w, rect.y+rect.h, 0),
                new Vector3(rect.x+rect.w, rect.y, 0), tex, res);
        CCRenderState.draw();
        postFluidRender();
    }
    
    public static void renderItemUniform(ItemStack item)
    {
        IItemRenderer customRenderer = MinecraftForgeClient.getItemRenderer(item, ENTITY);
        boolean is3D = customRenderer != null && customRenderer.shouldUseRenderHelper(ENTITY, item, BLOCK_3D);

        boolean larger = false;
        if (item.getItem() instanceof ItemBlock && RenderBlocks.renderItemIn3d(Block.blocksList[item.itemID].getRenderType()))
        {
            int renderType = Block.blocksList[item.itemID].getRenderType();
            larger = !(renderType == 1 || renderType == 19 || renderType == 12 || renderType == 2);
        }
        else if(is3D)
        {
            larger = true;
        }
        
        double d = 2;
        double d1 = 1/d;
        if(larger)
            GL11.glScaled(d, d, d);

        GL11.glColor4f(1, 1, 1, 1);
        
        entityItem.setEntityItemStack(item);
        uniformRenderItem.doRenderItem(entityItem, 0, larger ? 0.09 : 0.06, 0, 0, 0);
        
        if(larger)
            GL11.glScaled(d1, d1, d1);
    }
    
    private static Vertex5[] face = new Vertex5[]{new Vertex5(), new Vertex5(), new Vertex5(), new Vertex5()};
    public static void renderBlock(Cuboid6 c, int sideMask, IFaceRenderer r)
    {
        double x1 = c.min.x;
        double x2 = c.max.x;
        double y1 = c.min.y;
        double y2 = c.max.y;
        double z1 = c.min.z;
        double z2 = c.max.z;
        double u1 = 0;
        double u2 = 0;
        double v1 = 0;
        double v2 = 0;
        
        if((sideMask&1) == 0)
        {
            u1 = x1; v1 = z1;
            u2 = x2; v2 = z2;
            face[0].set(x1, y1, z2, u1, v2);
            face[1].set(x1, y1, z1, u1, v1);
            face[2].set(x2, y1, z1, u2, v1);
            face[3].set(x2, y1, z2, u2, v2);
            r.renderFace(face, c.min.y > 0 ? 6 : 0);
        }
        
        if((sideMask&2) == 0)
        {
            u1 = x1+2; v1 = z1;
            u2 = x2+2; v2 = z2;
            face[0].set(x2, y2, z2, u2, v2);
            face[1].set(x2, y2, z1, u2, v1);
            face[2].set(x1, y2, z1, u1, v1);
            face[3].set(x1, y2, z2, u1, v2);
            r.renderFace(face, c.max.y < 1 ? 7 : 1);
        }
        
        if((sideMask&4) == 0)
        {
            u1 = 1-x1+4; v1 = 1-y2;
            u2 = 1-x2+4; v2 = 1-y1;
            face[0].set(x1, y1, z1, u1, v2);
            face[1].set(x1, y2, z1, u1, v1);
            face[2].set(x2, y2, z1, u2, v1);
            face[3].set(x2, y1, z1, u2, v2);
            r.renderFace(face, c.min.z > 0 ? 8 : 2);
        }
    
        if((sideMask&8) == 0)
        {
            u1 = x1+6; v1 = 1-y2;
            u2 = x2+6; v2 = 1-y1;
            face[0].set(x2, y1, z2, u2, v2);
            face[1].set(x2, y2, z2, u2, v1);
            face[2].set(x1, y2, z2, u1, v1);
            face[3].set(x1, y1, z2, u1, v2);
            r.renderFace(face, c.max.z < 1 ? 9 : 3);
        }
    
        if((sideMask&0x10) == 0)
        {
            u1 = z1+8; v1 = 1-y2;
            u2 = z2+8; v2 = 1-y1;
            face[0].set(x1, y1, z2, u2, v2);
            face[1].set(x1, y2, z2, u2, v1);
            face[2].set(x1, y2, z1, u1, v1);
            face[3].set(x1, y1, z1, u1, v2);
            r.renderFace(face, c.min.x > 0 ? 10 : 4);
        }
    
        if((sideMask&0x20) == 0)
        {
            u1 = 1-z1+10; v1 = 1-y2;
            u2 = 1-z2+10; v2 = 1-y1;
            face[0].set(x2, y1, z1, u1, v2);
            face[1].set(x2, y2, z1, u1, v1);
            face[2].set(x2, y2, z2, u2, v1);
            face[3].set(x2, y1, z2, u2, v2);
            r.renderFace(face, c.max.x < 1 ? 11 : 5);
        }
    }

    public static void renderBlock(Cuboid6 bounds, int sideMask, final Transformation t, final IUVTransformation u, final IVertexModifier m)
    {
        renderBlock(bounds, sideMask, new IFaceRenderer(){
            boolean drawNormal = CCRenderState.useNormals();
            boolean computeNormal = drawNormal || m != null && m.needsNormals();
            Vector3 normal = new Vector3();
            Vertex5 vert;
            Vector3 vec = new Vector3();
            UV uv = new UV();
            
            @Override
            public void renderFace(Vertex5[] face, int side)
            {
                Tessellator tess = Tessellator.instance;
                for(int i = 0; i < face.length; i++)
                {
                    if(computeNormal)
                    {
                        if(t != null)
                            t.applyN(normal.set(Rotation.axes[side%6]));
                        else
                            normal = Rotation.axes[side%6];
                        
                        if(drawNormal)
                            tess.setNormal((float)normal.x, (float)normal.y, (float)normal.z);
                    }
                    
                    vert = face[i];
                    if(t != null)
                        t.apply(vec.set(vert.vec));
                    else
                        vec = vert.vec;
                    
                    if(u != null)
                        u.transform(uv.set(vert.uv));
                    else
                        uv = vert.uv;
                    
                    if(m != null)
                        m.applyModifiers(null, tess, vec, uv, normal, i);
                    
                    tess.addVertexWithUV(vec.x, vec.y, vec.z, uv.u, uv.v);
                }
            }
        });
    }
}
