package micdoodle8.mods.galacticraft.core.client.render.entities;

import micdoodle8.mods.galacticraft.api.recipe.SchematicRegistry;
import micdoodle8.mods.galacticraft.core.entities.EntityHangingSchematic;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.EntityHanging;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RenderSchematic extends Render<EntityHangingSchematic>
{
    public RenderSchematic(RenderManager manager)
    {
        super(manager);
    }

    @Override
    protected ResourceLocation getEntityTexture(EntityHangingSchematic entity)
    {
        return SchematicRegistry.getSchematicTexture(entity.schematic);
    }

    @Override
    public void doRender(EntityHangingSchematic entity, double x, double y, double z, float entityYaw, float partialTicks)
    {
        GlStateManager.pushMatrix();
        GlStateManager.translate(x, y, z);
        GlStateManager.rotate(180.0F - entityYaw, 0.0F, 1.0F, 0.0F);
        GlStateManager.enableRescaleNormal();
        this.bindEntityTexture(entity);
        float f = 0.0625F;
        GlStateManager.scale(f, f, f);
        this.renderPainting(entity, entity.getWidthPixels(), entity.getHeightPixels());
        GlStateManager.disableRescaleNormal();
        GlStateManager.popMatrix();
        super.doRender(entity, x, y, z, entityYaw, partialTicks);
    }
    
    private void renderPainting(EntityHangingSchematic painting, int width, int height)
    {
        float f = (float)(-width) / 2.0F;
        float f1 = (float)(-height) / 2.0F;
        float f2 = 0.5F;
        float f3 = 0.75F;
        float f4 = 0.8125F;
        float f5 = 0.0F;
        float f6 = 0.0625F;
        float f7 = 0.75F;
        float f8 = 0.8125F;
        float f9 = 0.001953125F;
        float f10 = 0.001953125F;
        float f11 = 0.7519531F;
        float f12 = 0.7519531F;
        float f13 = 0.0F;
        float f14 = 0.0625F;

        for (int i = 0; i < width / 16; ++i)
        {
            for (int j = 0; j < height / 16; ++j)
            {
                double a = f + (float)((i + 1) * 16);
                double b = f + (float)(i * 16);
                double c = f1 + (float)((j + 1) * 16);
                double d = f1 + (float)(j * 16);
                this.setLightmap(painting, (a + b) / 2.0F, (c + d) / 2.0F);
                float f19 = (float)(width - i * 16) / 32.0F;
                float f20 = (float)(width - (i + 1) * 16) / 32.0F;
                float f21 = (float)(height - j * 16) / 32.0F;
                float f22 = (float)(height - (j + 1) * 16) / 32.0F;
                Tessellator tessellator = Tessellator.getInstance();
                BufferBuilder worldrenderer = tessellator.getBuffer();
                worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX_NORMAL);
                worldrenderer.pos(a, d, (-f2)).tex(f20, f21).normal(0.0F, 0.0F, -1.0F).endVertex();
                worldrenderer.pos(b, d, (-f2)).tex(f19, f21).normal(0.0F, 0.0F, -1.0F).endVertex();
                worldrenderer.pos(b, c, (-f2)).tex(f19, f22).normal(0.0F, 0.0F, -1.0F).endVertex();
                worldrenderer.pos(a, c, (-f2)).tex(f20, f22).normal(0.0F, 0.0F, -1.0F).endVertex();
                worldrenderer.pos(a, c, f2).tex(f3, f5).normal(0.0F, 0.0F, 1.0F).endVertex();
                worldrenderer.pos(b, c, f2).tex(f4, f5).normal(0.0F, 0.0F, 1.0F).endVertex();
                worldrenderer.pos(b, d, f2).tex(f4, f6).normal(0.0F, 0.0F, 1.0F).endVertex();
                worldrenderer.pos(a, d, f2).tex(f3, f6).normal(0.0F, 0.0F, 1.0F).endVertex();
                worldrenderer.pos(a, c, (-f2)).tex(f7, f9).normal(0.0F, 1.0F, 0.0F).endVertex();
                worldrenderer.pos(b, c, (-f2)).tex(f8, f9).normal(0.0F, 1.0F, 0.0F).endVertex();
                worldrenderer.pos(b, c, f2).tex(f8, f10).normal(0.0F, 1.0F, 0.0F).endVertex();
                worldrenderer.pos(a, c, f2).tex(f7, f10).normal(0.0F, 1.0F, 0.0F).endVertex();
                worldrenderer.pos(a, d, f2).tex(f7, f9).normal(0.0F, -1.0F, 0.0F).endVertex();
                worldrenderer.pos(b, d, f2).tex(f8, f9).normal(0.0F, -1.0F, 0.0F).endVertex();
                worldrenderer.pos(b, d, (-f2)).tex(f8, f10).normal(0.0F, -1.0F, 0.0F).endVertex();
                worldrenderer.pos(a, d, (-f2)).tex(f7, f10).normal(0.0F, -1.0F, 0.0F).endVertex();
                worldrenderer.pos(a, c, f2).tex(f12, f13).normal(-1.0F, 0.0F, 0.0F).endVertex();
                worldrenderer.pos(a, d, f2).tex(f12, f14).normal(-1.0F, 0.0F, 0.0F).endVertex();
                worldrenderer.pos(a, d, (-f2)).tex(f11, f14).normal(-1.0F, 0.0F, 0.0F).endVertex();
                worldrenderer.pos(a, c, (-f2)).tex(f11, f13).normal(-1.0F, 0.0F, 0.0F).endVertex();
                worldrenderer.pos(b, c, (-f2)).tex(f12, f13).normal(1.0F, 0.0F, 0.0F).endVertex();
                worldrenderer.pos(b, d, (-f2)).tex(f12, f14).normal(1.0F, 0.0F, 0.0F).endVertex();
                worldrenderer.pos(b, d, f2).tex(f11, f14).normal(1.0F, 0.0F, 0.0F).endVertex();
                worldrenderer.pos(b, c, f2).tex(f11, f13).normal(1.0F, 0.0F, 0.0F).endVertex();
                tessellator.draw();
            }
        }
    }

    private void setLightmap(EntityHanging painting, double p_77008_2_, double p_77008_3_)
    {
        int i = MathHelper.floor(painting.posX);
        int j = MathHelper.floor(painting.posY + (double)(p_77008_3_ / 16.0F));
        int k = MathHelper.floor(painting.posZ);
        EnumFacing enumfacing = painting.facingDirection;

        if (enumfacing == EnumFacing.NORTH)
        {
            i = MathHelper.floor(painting.posX + (double)(p_77008_2_ / 16.0F));
        }

        if (enumfacing == EnumFacing.WEST)
        {
            k = MathHelper.floor(painting.posZ - (double)(p_77008_2_ / 16.0F));
        }

        if (enumfacing == EnumFacing.SOUTH)
        {
            i = MathHelper.floor(painting.posX - (double)(p_77008_2_ / 16.0F));
        }

        if (enumfacing == EnumFacing.EAST)
        {
            k = MathHelper.floor(painting.posZ + (double)(p_77008_2_ / 16.0F));
        }

        int l = this.renderManager.world.getCombinedLight(new BlockPos(i, j, k), 0);
        int i1 = l % 65536;
        int j1 = l / 65536;
        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, (float)i1, (float)j1);
        GlStateManager.color(1.0F, 1.0F, 1.0F);
    }
}
