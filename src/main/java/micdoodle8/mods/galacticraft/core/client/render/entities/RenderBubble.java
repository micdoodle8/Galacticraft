//package micdoodle8.mods.galacticraft.core.client.render.entities;
//
//import cpw.mods.fml.relauncher.Side;
//import cpw.mods.fml.relauncher.SideOnly;
//import micdoodle8.mods.galacticraft.core.GalacticraftCore;
//import micdoodle8.mods.galacticraft.core.client.model.ModelBubble;
//import micdoodle8.mods.galacticraft.core.entities.IBubble;
//import net.minecraft.client.renderer.entity.Render;
//import net.minecraft.entity.Entity;
//import net.minecraft.util.ResourceLocation;
//import org.lwjgl.opengl.GL11;
//import org.lwjgl.opengl.GL12;
//
//@SideOnly(Side.CLIENT)
//public class RenderBubble extends Render
//{
//    private static final ResourceLocation oxygenBubbleTexture = new ResourceLocation(GalacticraftCore.ASSET_PREFIX, "textures/model/bubble.png");
//
//    private final ModelBubble oxygenBubbleModel = new ModelBubble();
//
//    private final float colorRed;
//    private final float colorGreen;
//    private final float colorBlue;
//
//    public RenderBubble(float red, float green, float blue)
//    {
//        this.colorRed = red;
//        this.colorGreen = green;
//        this.colorBlue = blue;
//    }
//
//    @Override
//    protected ResourceLocation getEntityTexture(Entity par1Entity)
//    {
//        return RenderBubble.oxygenBubbleTexture;
//    }
//
//    @Override
//    public void doRender(Entity entity, double d0, double d1, double d2, float f, float f1)
//    {
//        if (((IBubble) entity).shouldRender())
//        {
//            GL11.glPushMatrix();
//            GL11.glEnable(GL12.GL_RESCALE_NORMAL);
//            GL11.glTranslatef((float) d0, (float) d1, (float) d2);
//
//            this.bindEntityTexture(entity);
//
//            GL11.glEnable(GL11.GL_BLEND);
//
//            GL11.glAlphaFunc(GL11.GL_GREATER, 0.1F);
//            GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
//            GL11.glColor4f(this.colorRed, this.colorGreen, this.colorBlue, 1.0F);
//            GL11.glMatrixMode(GL11.GL_TEXTURE);
//            GL11.glLoadIdentity();
//            GL11.glMatrixMode(GL11.GL_MODELVIEW);
//            GL11.glDepthMask(false);
//            GL11.glScalef(((IBubble) entity).getSize(), ((IBubble) entity).getSize(), ((IBubble) entity).getSize());
//
//            this.oxygenBubbleModel.render(entity, (float) d0, (float) d1, (float) d2, 0, 0, 1.0F);
//
//            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
//            GL11.glMatrixMode(GL11.GL_TEXTURE);
//            GL11.glDepthMask(true);
//            GL11.glLoadIdentity();
//            GL11.glMatrixMode(GL11.GL_MODELVIEW);
//            GL11.glEnable(GL11.GL_LIGHTING);
//            GL11.glDisable(GL11.GL_BLEND);
//            GL11.glDepthFunc(GL11.GL_LEQUAL);
//
//            GL11.glDisable(GL12.GL_RESCALE_NORMAL);
//            GL11.glPopMatrix();
//            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
//        }
//    }
//}
