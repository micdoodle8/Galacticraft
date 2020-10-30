package micdoodle8.mods.galacticraft.planets.venus.client.render.tile;

import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.platform.GLX;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import micdoodle8.mods.galacticraft.api.vector.Vector3D;
import micdoodle8.mods.galacticraft.core.Constants;
import micdoodle8.mods.galacticraft.core.client.obj.GCModelCache;
import micdoodle8.mods.galacticraft.core.util.ClientUtil;
import micdoodle8.mods.galacticraft.planets.GalacticraftPlanets;
import micdoodle8.mods.galacticraft.planets.venus.tile.TileEntityLaserTurret;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.client.model.obj.OBJModel;
import org.lwjgl.opengl.GL11;

@OnlyIn(Dist.CLIENT)
public class TileEntityLaserTurretRenderer extends TileEntityRenderer<TileEntityLaserTurret>
{
    public static IBakedModel laserBase;
    public static IBakedModel laserPhalange;
    public static IBakedModel laserPhalangeAxle;
    public static IBakedModel laserTurrets;
    public static IBakedModel laserTurretsOff;
    public static IBakedModel orb1;
    public static IBakedModel orb2;

    public TileEntityLaserTurretRenderer(TileEntityRendererDispatcher rendererDispatcherIn)
    {
        super(rendererDispatcherIn);
        GCModelCache.INSTANCE.reloadCallback(this::updateModels);
    }

    private void updateModels()
    {
        laserBase = GCModelCache.INSTANCE.getModel(new ResourceLocation(GalacticraftPlanets.ASSET_PREFIX, "models/laser.obj"), ImmutableList.of("baseConnector0", "baseConnector1"));
        laserPhalange = GCModelCache.INSTANCE.getModel(new ResourceLocation(GalacticraftPlanets.ASSET_PREFIX, "models/laser.obj"), ImmutableList.of("phalange"));
        laserPhalangeAxle = GCModelCache.INSTANCE.getModel(new ResourceLocation(GalacticraftPlanets.ASSET_PREFIX, "models/laser.obj"), ImmutableList.of("phalangeAxle"));
        laserTurrets = GCModelCache.INSTANCE.getModel(new ResourceLocation(GalacticraftPlanets.ASSET_PREFIX, "models/laser.obj"), ImmutableList.of("turretLeft", "turretRight"));
        laserTurretsOff = GCModelCache.INSTANCE.getModel(new ResourceLocation(GalacticraftPlanets.ASSET_PREFIX, "models/laser.obj"), ImmutableList.of("turretLeft_off", "turretRight_off"));
        orb1 = GCModelCache.INSTANCE.getModel(new ResourceLocation(GalacticraftPlanets.ASSET_PREFIX, "models/orb.obj"), ImmutableList.of("inner_Icosphere"));
        orb2 = GCModelCache.INSTANCE.getModel(new ResourceLocation(GalacticraftPlanets.ASSET_PREFIX, "models/orb.obj"), ImmutableList.of("inner_Icosphere.001"));
    }

    @Override
    public void render(TileEntityLaserTurret tile, float partialTicks, MatrixStack matStack, IRenderTypeBuffer bufferIn, int combinedLightIn, int combinedOverlayIn)
    {
        matStack.push();
//        GlStateManager.pushMatrix();
        matStack.translate(0.5F, 1.5F, 0.5F);
//        GlStateManager.translatef((float) x + 0.5F, (float) y + 1.5F, (float) z + 0.5F);
        matStack.push();
//        GlStateManager.pushMatrix();
        RenderHelper.disableStandardItemLighting();

        combinedLightIn = WorldRenderer.getCombinedLight(tile.getWorld(), tile.getPos().up());

//        this.bindTexture(AtlasTexture.LOCATION_BLOCKS_TEXTURE);

        if (Minecraft.isAmbientOcclusionEnabled())
        {
            GlStateManager.shadeModel(GL11.GL_SMOOTH);
        }
        else
        {
            GlStateManager.shadeModel(GL11.GL_FLAT);
        }

        matStack.scale(1 / 16.0F, 1 / 16.0F, 1 / 16.0F);
//        GlStateManager.scalef(1 / 16.0F, 1 / 16.0F, 1 / 16.0F);

//        GlStateManager.pushMatrix();
        matStack.push();
//        GlStateManager.scalef(0.9F, 1.0F, 0.9F);
        matStack.scale(0.9F, 1.0F, 0.9F);
        ClientUtil.drawBakedModel(laserBase, bufferIn, matStack, combinedLightIn);
//        GlStateManager.popMatrix();
        matStack.pop();

//        GlStateManager.rotatef(tile.yaw, 0.0F, 1.0F, 0.0F);
        matStack.rotate(new Quaternion(new Vector3f(0.0F, 1.0F, 0.0F), tile.yaw, true));

        // Interpolate between yaw and targetYaw

        float partialRot = Math.signum(tile.pitch) * tile.pitch * (tile.pitch / 120.0F);
//        GlStateManager.rotatef(partialRot, 0.0F, 0.0F, -1.0F);
        matStack.rotate(new Quaternion(new Vector3f(0.0F, 0.0F, -1.0F), partialRot, true));

//        GlStateManager.pushMatrix();
        matStack.push();
//        GlStateManager.scalef(1.1F, 1.0F, 1.0F);
        matStack.scale(1.1F, 1.0F, 1.0F);

        ClientUtil.drawBakedModel(laserPhalange, bufferIn, matStack, combinedLightIn);
//        GlStateManager.popMatrix();
        matStack.pop();

//        GlStateManager.rotatef(tile.pitch - partialRot, 0.0F, 0.0F, -1.0F);
        matStack.rotate(new Quaternion(new Vector3f(0.0F, 0.0F, -1.0F), tile.pitch - partialRot, true));

        ClientUtil.drawBakedModel(laserPhalangeAxle, bufferIn, matStack, combinedLightIn);
        ClientUtil.drawBakedModel(tile.active ? laserTurrets : laserTurretsOff, bufferIn, matStack, combinedLightIn);

//        GlStateManager.popMatrix();
        matStack.pop();
        Tessellator tess = Tessellator.getInstance();

        float inv = (float) (Math.pow(tile.chargeLevel / 5.0F + 1.0F, 2.5F) * 1.0F);
        float invNext = (float) (Math.pow((tile.chargeLevel + 1) / 5.0F + 1.0F, 2.5F) * 1.0F);
        float rotate = inv + (invNext - inv) * partialTicks;

//        float lightMapSaveX = GLX.lastBrightnessX;
//        float lightMapSaveY = GLX.lastBrightnessY;
//        GLX.glMultiTexCoord2f(GLX.GL_TEXTURE1, 240.0F, 240.0F);

        if (tile.chargeLevel > 0)
        {
//            GlStateManager.pushMatrix();
            matStack.push();
            GlStateManager.enableBlend();
            RenderSystem.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
//            GlStateManager.rotatef(tile.yaw, 0.0F, 1.0F, 0.0F);
            matStack.rotate(new Quaternion(new Vector3f(0.0F, 1.0F, 0.0F), tile.yaw, true));
//            GlStateManager.rotatef(tile.pitch, 0.0F, 0.0F, -1.0F);
            matStack.rotate(new Quaternion(new Vector3f(0.0F, 0.0F, -1.0F), tile.pitch, true));
//            GlStateManager.translatef(-0.6F, 0.28F, 0.0F);
            matStack.translate(-0.6F, 0.28F, 0.0F);

            GlStateManager.disableTexture();

            tess.getBuffer().begin(GL11.GL_LINE_LOOP, DefaultVertexFormats.POSITION_COLOR);

            tess.getBuffer().pos(0.09F, 0.0F, 0.275F).color(0.0F, 1.0F, 0.0F, 1.0F).endVertex();
            tess.getBuffer().pos(0.0F, 0.0F, 0.0F).color(0.0F, 1.0F, 0.0F, 1.0F).endVertex();
            tess.getBuffer().pos(0.09F, 0.0F, -0.275F).color(0.0F, 1.0F, 0.0F, 1.0F).endVertex();

            tess.draw();
            GlStateManager.enableTexture();

            float scale = tile.chargeLevel / 600.0F;
//            GlStateManager.scalef(0.01F + scale, 0.01F + scale, 0.01F + scale);
            matStack.scale(0.01F + scale, 0.01F + scale, 0.01F + scale);
//            GlStateManager.rotatef(rotate, 0.0F, 1.0F, 0.0F);
            matStack.rotate(new Quaternion(new Vector3f(0.0F, 1.0F, 0.0F), rotate, true));
            ClientUtil.drawBakedModel(orb1, bufferIn, matStack, Constants.PACKED_LIGHT_FULL_BRIGHT);
//            GlStateManager.rotatef(rotate, 0.0F, 1.0F, 0.0F);
            matStack.rotate(new Quaternion(new Vector3f(0.0F, 1.0F, 0.0F), rotate, true));
            ClientUtil.drawBakedModel(orb2, bufferIn, matStack, Constants.PACKED_LIGHT_FULL_BRIGHT);
            GlStateManager.disableBlend();
//            GlStateManager.popMatrix();
            matStack.pop();
        }

        if (tile.timeSinceShot > 0 && tile.timeSinceShot < 5)
        {
            Entity e = tile.getWorld().getEntityByID(tile.targettedEntity);

            if (e != null)
            {
                GlStateManager.disableTexture();
//                GlStateManager.pushMatrix();
                matStack.push();
                GlStateManager.enableBlend();
                GlStateManager.disableCull();
                RenderSystem.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
//                GlStateManager.rotatef(tile.yaw, 0.0F, 1.0F, 0.0F);
                matStack.rotate(new Quaternion(new Vector3f(0.0F, 1.0F, 0.0F), tile.yaw, true));
//                GlStateManager.rotatef(tile.pitch, 0.0F, 0.0F, -1.0F);
                matStack.rotate(new Quaternion(new Vector3f(0.0F, 0.0F, -1.0F), tile.pitch, true));
//                GlStateManager.translatef(-0.6F, 0.28F, 0.0F);
                matStack.translate(-0.6F, 0.28F, 0.0F);

                BufferBuilder bb = tess.getBuffer();

                bb.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_COLOR);

                Vector3D vec = new Vector3D(e.getPosX(), e.getPosY() + e.getEyeHeight(), e.getPosZ());
                vec.translate(new Vector3D(-(tile.getPos().getX() + 0.5F), -(tile.getPos().getY() + 1.78F), -(tile.getPos().getZ() + 0.5F)));
                float dist = (float) vec.getMagnitude() - 0.8F;
                float shotTimer = (float) (Math.pow((5.0F - tile.timeSinceShot) / 5.0F + 1.0F, 2.5F) * 0.5F);
                float shotTimerNext = (float) (Math.pow((5.0F - (tile.timeSinceShot + 1)) / 5.0F + 1.0F, 2.5F) * 0.5F);
                float fade = shotTimer + (shotTimerNext - shotTimer) * partialTicks;
                float yMin = -0.101F * fade;
                float yMax = 0.101F * fade;
                float yMin1 = -0.15F * fade;
                float yMax1 = 0.15F * fade;

                tess.getBuffer().pos(0.0F, yMax, yMax).color(0.0F, 1.0F, 0.0F, 0.5F).lightmap(Constants.PACKED_LIGHT_FULL_BRIGHT).endVertex();
                tess.getBuffer().pos(0.0F, yMin, yMin).color(0.0F, 1.0F, 0.0F, 0.5F).lightmap(Constants.PACKED_LIGHT_FULL_BRIGHT).endVertex();
                tess.getBuffer().pos(-dist, yMin * 0.4F, yMin * 0.4F).color(0.0F, 1.0F, 0.0F, 0.5F).lightmap(Constants.PACKED_LIGHT_FULL_BRIGHT).endVertex();
                tess.getBuffer().pos(-dist, yMax * 0.4F, yMax * 0.4F).color(0.0F, 1.0F, 0.0F, 0.5F).lightmap(Constants.PACKED_LIGHT_FULL_BRIGHT).endVertex();

                tess.getBuffer().pos(0.0F, yMax, yMin).color(0.0F, 1.0F, 0.0F, 0.5F).lightmap(Constants.PACKED_LIGHT_FULL_BRIGHT).endVertex();
                tess.getBuffer().pos(0.0F, yMin, yMax).color(0.0F, 1.0F, 0.0F, 0.5F).lightmap(Constants.PACKED_LIGHT_FULL_BRIGHT).endVertex();
                tess.getBuffer().pos(-dist, yMin * 0.4F, yMax * 0.4F).color(0.0F, 1.0F, 0.0F, 0.5F).lightmap(Constants.PACKED_LIGHT_FULL_BRIGHT).endVertex();
                tess.getBuffer().pos(-dist, yMax * 0.4F, yMin * 0.4F).color(0.0F, 1.0F, 0.0F, 0.5F).lightmap(Constants.PACKED_LIGHT_FULL_BRIGHT).endVertex();

                tess.getBuffer().pos(0.0F, yMax1, yMax1).color(0.0F, 1.0F, 0.0F, 0.5F).lightmap(Constants.PACKED_LIGHT_FULL_BRIGHT).endVertex();
                tess.getBuffer().pos(0.0F, yMin1, yMin1).color(0.0F, 1.0F, 0.0F, 0.5F).lightmap(Constants.PACKED_LIGHT_FULL_BRIGHT).endVertex();
                tess.getBuffer().pos(-dist, yMin1 * 0.4F, yMin1 * 0.4F).color(0.0F, 1.0F, 0.0F, 0.5F).lightmap(Constants.PACKED_LIGHT_FULL_BRIGHT).endVertex();
                tess.getBuffer().pos(-dist, yMax1 * 0.4F, yMax1 * 0.4F).color(0.0F, 1.0F, 0.0F, 0.5F).lightmap(Constants.PACKED_LIGHT_FULL_BRIGHT).endVertex();

                tess.getBuffer().pos(0.0F, yMax1, yMin1).color(0.0F, 1.0F, 0.0F, 0.5F).lightmap(Constants.PACKED_LIGHT_FULL_BRIGHT).endVertex();
                tess.getBuffer().pos(0.0F, yMin1, yMax1).color(0.0F, 1.0F, 0.0F, 0.5F).lightmap(Constants.PACKED_LIGHT_FULL_BRIGHT).endVertex();
                tess.getBuffer().pos(-dist, yMin1 * 0.4F, yMax1 * 0.4F).color(0.0F, 1.0F, 0.0F, 0.5F).lightmap(Constants.PACKED_LIGHT_FULL_BRIGHT).endVertex();
                tess.getBuffer().pos(-dist, yMax1 * 0.4F, yMin1 * 0.4F).color(0.0F, 1.0F, 0.0F, 0.5F).lightmap(Constants.PACKED_LIGHT_FULL_BRIGHT).endVertex();

                tess.draw();

                GlStateManager.enableCull();
                GlStateManager.enableTexture();
                matStack.pop();
//                GlStateManager.popMatrix();
            }
        }
//        GLX.glMultiTexCoord2f(GLX.GL_TEXTURE1, lightMapSaveX, lightMapSaveY);
//        GlStateManager.popMatrix();
        matStack.pop();
    }
}
