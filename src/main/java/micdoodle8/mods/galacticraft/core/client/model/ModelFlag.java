//package micdoodle8.mods.galacticraft.core.client.model;
//
//import micdoodle8.mods.galacticraft.api.vector.Vector3;
//import micdoodle8.mods.galacticraft.api.world.IGalacticraftDimension;
//import micdoodle8.mods.galacticraft.core.entities.EntityFlag;
//import net.minecraft.client.renderer.model.ModelRenderer;
//import net.minecraft.client.renderer.Tessellator;
//import net.minecraft.client.renderer.BufferBuilder;
//import net.minecraft.client.renderer.model.Model;
//import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
//import org.lwjgl.opengl.GL11;
//
//public class ModelFlag extends Model
//{
//    ModelRenderer base;
//    ModelRenderer pole;
//
//    public ModelFlag()
//    {
//        this.textureWidth = 128;
//        this.textureHeight = 64;
//        this.base = new ModelRenderer(this, 4, 0);
//        this.base.addBox(-1.5F, 0F, -1.5F, 3, 1, 3);
//        this.base.setRotationPoint(0F, 23F, 0F);
//        this.base.setTextureSize(128, 64);
//        this.base.mirror = true;
//        this.setRotation(this.base, 0F, 0F, 0F);
//        this.pole = new ModelRenderer(this, 0, 0);
//        this.pole.addBox(-0.5F, -40F, -0.5F, 1, 40, 1);
//        this.pole.setRotationPoint(0F, 23F, 0F);
//        this.pole.setTextureSize(128, 64);
//        this.pole.mirror = true;
//        this.setRotation(this.pole, 0F, 0F, 0F);
//    }
//
//    public void render(EntityFlag flag, float scale)
//    {
//        this.renderPole(scale);
//        this.renderFlag(flag, flag.ticksExisted);
//    }
//
//    public void renderPole(float scale)
//    {
//        this.base.render(scale);
//        this.pole.render(scale);
//    }
//
//    public void renderFlag(EntityFlag entity, float ticks)
//    {
//        if (entity.flagData != null)
//        {
//            RenderSystem.pushMatrix();
//
//            RenderSystem.scalef(0.5F, 0.5F, 0.5F);
//            RenderSystem.translatef(0.0F, -1.1F, 0.0F);
//
//            RenderSystem.disableTexture();
//            RenderSystem.disable(GL11.GL_CULL_FACE);
//
//            float windLevel = 1.0F;
//
//            if (entity.world.getDimension() instanceof IGalacticraftDimension)
//            {
//                windLevel = ((IGalacticraftDimension) entity.world.getDimension()).getWindLevel();
//            }
//
//            for (int i = 0; i < entity.flagData.getWidth(); i++)
//            {
//                for (int j = 0; j < entity.flagData.getHeight(); j++)
//                {
//                    RenderSystem.pushMatrix();
//                    RenderSystem.translatef(0, -1.0F, 0);
//                    float offset = 0.0F;
//                    float offsetAhead = 0.0F;
//
//                    if (windLevel > 0)
//                    {
//                        offset = (float) (Math.sin(ticks / 2.0F + i * 50 + 3) / 25.0F) * i / 30.0F;
//                        offsetAhead = (float) (Math.sin(ticks / 2.0F + (i + 1) * 50 + 3) / 25.0F) * (i + 1) / 30.0F;
//                        offset *= windLevel;
//                        offsetAhead *= windLevel;
//                    }
//
//                    Vector3 col = entity.flagData.getColorAt(i, j);
//                    RenderSystem.color3f(col.floatX(), col.floatY(), col.floatZ());
//
//                    Tessellator tess = Tessellator.getInstance();
//                    BufferBuilder worldRenderer = tess.getBuffer();
//                    worldRenderer.begin(GL11.GL_TRIANGLES, DefaultVertexFormats.POSITION);
//
//                    worldRenderer.pos(i / 24.0F + 0.0 / 24.0F, j / 24.0F + 0.0 / 24.0F + offset, offset).endVertex();
//                    worldRenderer.pos(i / 24.0F + 0.0 / 24.0F, j / 24.0F + 1.0 / 24.0F + offset, offset).endVertex();
//                    worldRenderer.pos(i / 24.0F + 1.0 / 24.0F, j / 24.0F + 1.0 / 24.0F + offsetAhead, offsetAhead).endVertex();
//
//                    worldRenderer.pos(i / 24.0F + 0.0 / 24.0F, j / 24.0F + 0.0 / 24.0F + offset, offset).endVertex();
//                    worldRenderer.pos(i / 24.0F + 1.0 / 24.0F, j / 24.0F + 1.0 / 24.0F + offsetAhead, offsetAhead).endVertex();
//                    worldRenderer.pos(i / 24.0F + 1.0 / 24.0F, j / 24.0F + 0.0 / 24.0F + offsetAhead, offsetAhead).endVertex();
//
//                    tess.draw();
//
//                    RenderSystem.color3f(1, 1, 1);
//                    RenderSystem.popMatrix();
//                }
//            }
//
//            RenderSystem.enableTexture();
//            RenderSystem.enable(GL11.GL_CULL_FACE);
//
//            RenderSystem.popMatrix();
//        }
//    }
//
//    private void setRotation(ModelRenderer model, float x, float y, float z)
//    {
//        model.rotateAngleX = x;
//        model.rotateAngleY = y;
//        model.rotateAngleZ = z;
//    }
//}
