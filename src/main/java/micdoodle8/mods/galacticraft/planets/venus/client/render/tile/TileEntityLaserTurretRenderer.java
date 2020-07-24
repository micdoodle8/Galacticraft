//package micdoodle8.mods.galacticraft.planets.venus.client.render.tile;
//
//import com.google.common.collect.ImmutableList;
//import com.mojang.blaze3d.platform.GLX;
//import com.mojang.blaze3d.systems.RenderSystem;
//import micdoodle8.mods.galacticraft.api.vector.Vector3D;
//import micdoodle8.mods.galacticraft.core.util.ClientUtil;
//import micdoodle8.mods.galacticraft.planets.GalacticraftPlanets;
//import micdoodle8.mods.galacticraft.planets.venus.tile.TileEntityLaserTurret;
//import net.minecraft.client.Minecraft;
//import net.minecraft.client.renderer.BufferBuilder;
//import net.minecraft.client.renderer.RenderHelper;
//import net.minecraft.client.renderer.Tessellator;
//import net.minecraft.client.renderer.texture.AtlasTexture;
//import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
//import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
//import net.minecraft.entity.Entity;
//import net.minecraft.util.ResourceLocation;
//import net.minecraftforge.api.distmarker.Dist;
//import net.minecraftforge.api.distmarker.OnlyIn;
//import net.minecraftforge.client.model.ModelLoader;
//import net.minecraftforge.client.model.obj.OBJModel;
//import org.lwjgl.opengl.GL11;
//
//@OnlyIn(Dist.CLIENT)
//public class TileEntityLaserTurretRenderer extends TileEntityRenderer<TileEntityLaserTurret>
//{
//    public static OBJModel.OBJBakedModel laserBase;
//    public static OBJModel.OBJBakedModel laserPhalange;
//    public static OBJModel.OBJBakedModel laserPhalangeAxle;
//    public static OBJModel.OBJBakedModel laserTurrets;
//    public static OBJModel.OBJBakedModel laserTurretsOff;
//    public static OBJModel.OBJBakedModel orb1;
//    public static OBJModel.OBJBakedModel orb2;
//
//    public static void updateModels(ModelLoader modelLoader)
//    {
//        try
//        {
//            laserBase = ClientUtil.modelFromOBJ(modelLoader, new ResourceLocation(GalacticraftPlanets.ASSET_PREFIX, "laser.obj"), ImmutableList.of("baseConnector0", "baseConnector1"));
//            laserPhalange = ClientUtil.modelFromOBJ(modelLoader, new ResourceLocation(GalacticraftPlanets.ASSET_PREFIX, "laser.obj"), ImmutableList.of("phalange"));
//            laserPhalangeAxle = ClientUtil.modelFromOBJ(modelLoader, new ResourceLocation(GalacticraftPlanets.ASSET_PREFIX, "laser.obj"), ImmutableList.of("phalangeAxle"));
//            laserTurrets = ClientUtil.modelFromOBJ(modelLoader, new ResourceLocation(GalacticraftPlanets.ASSET_PREFIX, "laser.obj"), ImmutableList.of("turretLeft", "turretRight"));
//            laserTurretsOff = ClientUtil.modelFromOBJ(modelLoader, new ResourceLocation(GalacticraftPlanets.ASSET_PREFIX, "laser.obj"), ImmutableList.of("turretLeft_off", "turretRight_off"));
//
//            orb1 = ClientUtil.modelFromOBJ(modelLoader, new ResourceLocation(GalacticraftPlanets.ASSET_PREFIX, "orb.obj"), ImmutableList.of("inner_Icosphere"));
//            orb2 = ClientUtil.modelFromOBJ(modelLoader, new ResourceLocation(GalacticraftPlanets.ASSET_PREFIX, "orb.obj"), ImmutableList.of("inner_Icosphere.001"));
//        }
//        catch (Exception e)
//        {
//            throw new RuntimeException(e);
//        }
//    }
//
//    @Override
//    public void render(TileEntityLaserTurret tile, double x, double y, double z, float partialTicks, int destroyStage)
//    {
//        RenderSystem.pushMatrix();
//        RenderSystem.translatef((float) x + 0.5F, (float) y + 1.5F, (float) z + 0.5F);
//        RenderSystem.pushMatrix();
//        RenderHelper.disableStandardItemLighting();
//
//        this.bindTexture(AtlasTexture.LOCATION_BLOCKS_TEXTURE);
//
//        if (Minecraft.isAmbientOcclusionEnabled())
//        {
//            RenderSystem.shadeModel(7425);
//        }
//        else
//        {
//            RenderSystem.shadeModel(7424);
//        }
//
//        RenderSystem.scalef(1 / 16.0F, 1 / 16.0F, 1 / 16.0F);
//
//        RenderSystem.pushMatrix();
//        RenderSystem.scalef(0.9F, 1.0F, 0.9F);
//        ClientUtil.drawBakedModel(laserBase);
//        RenderSystem.popMatrix();
//
//        RenderSystem.rotatef(tile.yaw, 0.0F, 1.0F, 0.0F);
//
//        // Interpolate between yaw and targetYaw
//
//        float partialRot = Math.signum(tile.pitch) * tile.pitch * (tile.pitch / 120.0F);
//        RenderSystem.rotatef(partialRot, 0.0F, 0.0F, -1.0F);
//
//        RenderSystem.pushMatrix();
//        RenderSystem.scalef(1.1F, 1.0F, 1.0F);
//
//        ClientUtil.drawBakedModel(laserPhalange);
//        RenderSystem.popMatrix();
//
//        RenderSystem.rotatef(tile.pitch - partialRot, 0.0F, 0.0F, -1.0F);
//
//        ClientUtil.drawBakedModel(laserPhalangeAxle);
//        ClientUtil.drawBakedModel(tile.active ? laserTurrets : laserTurretsOff);
//
//        RenderSystem.popMatrix();
//        Tessellator tess = Tessellator.getInstance();
//
//        float inv = (float) (Math.pow(tile.chargeLevel / 5.0F + 1.0F, 2.5F) * 1.0F);
//        float invNext = (float) (Math.pow((tile.chargeLevel + 1) / 5.0F + 1.0F, 2.5F) * 1.0F);
//        float rotate = inv + (invNext - inv) * partialTicks;
//
//        float lightMapSaveX = GLX.lastBrightnessX;
//        float lightMapSaveY = GLX.lastBrightnessY;
//        RenderSystem.glMultiTexCoord2f(33985, 240.0F, 240.0F);
//
//        if (tile.chargeLevel > 0)
//        {
//            RenderSystem.pushMatrix();
//            RenderSystem.enableBlend();
//            RenderSystem.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
//            RenderSystem.rotatef(tile.yaw, 0.0F, 1.0F, 0.0F);
//            RenderSystem.rotatef(tile.pitch, 0.0F, 0.0F, -1.0F);
//            RenderSystem.translatef(-0.6F, 0.28F, 0.0F);
//
//            RenderSystem.disableTexture();
//
//            tess.getBuffer().begin(GL11.GL_LINE_LOOP, DefaultVertexFormats.POSITION_COLOR);
//
//            tess.getBuffer().pos(0.09F, 0.0F, 0.275F).color(0.0F, 1.0F, 0.0F, 1.0F).endVertex();
//            tess.getBuffer().pos(0.0F, 0.0F, 0.0F).color(0.0F, 1.0F, 0.0F, 1.0F).endVertex();
//            tess.getBuffer().pos(0.09F, 0.0F, -0.275F).color(0.0F, 1.0F, 0.0F, 1.0F).endVertex();
//
//            tess.draw();
//            RenderSystem.enableTexture();
//
//            float scale = tile.chargeLevel / 600.0F;
//            RenderSystem.scalef(0.01F + scale, 0.01F + scale, 0.01F + scale);
//            RenderSystem.rotatef(rotate, 0.0F, 1.0F, 0.0F);
//            ClientUtil.drawBakedModel(orb1);
//            RenderSystem.rotatef(rotate, 0.0F, 1.0F, 0.0F);
//            ClientUtil.drawBakedModel(orb2);
//            RenderSystem.disableBlend();
//            RenderSystem.popMatrix();
//        }
//
//        if (tile.timeSinceShot > 0 && tile.timeSinceShot < 5)
//        {
//            Entity e = tile.getWorld().getEntityByID(tile.targettedEntity);
//
//            if (e != null)
//            {
//                RenderSystem.disableTexture();
//                RenderSystem.pushMatrix();
//                RenderSystem.enableBlend();
//                RenderSystem.disableCull();
//                RenderSystem.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
//                RenderSystem.rotatef(tile.yaw, 0.0F, 1.0F, 0.0F);
//                RenderSystem.rotatef(tile.pitch, 0.0F, 0.0F, -1.0F);
//                RenderSystem.translatef(-0.6F, 0.28F, 0.0F);
//
//                BufferBuilder bb = tess.getBuffer();
//
//                bb.begin(7, DefaultVertexFormats.POSITION_COLOR);
//
//                Vector3D vec = new Vector3D(e.getPosX(), e.getPosY() + e.getEyeHeight(), e.getPosZ());
//                vec.translate(new Vector3D(-(tile.getPos().getX() + 0.5F), -(tile.getPos().getY() + 1.78F), -(tile.getPos().getZ() + 0.5F)));
//                float dist = (float) vec.getMagnitude() - 0.8F;
//                float shotTimer = (float) (Math.pow((5.0F - tile.timeSinceShot) / 5.0F + 1.0F, 2.5F) * 0.5F);
//                float shotTimerNext = (float) (Math.pow((5.0F - (tile.timeSinceShot + 1)) / 5.0F + 1.0F, 2.5F) * 0.5F);
//                float fade = shotTimer + (shotTimerNext - shotTimer) * partialTicks;
//                float yMin = -0.101F * fade;
//                float yMax = 0.101F * fade;
//                float yMin1 = -0.15F * fade;
//                float yMax1 = 0.15F * fade;
//
//                tess.getBuffer().pos(0.0F, yMax, yMax).color(0.0F, 1.0F, 0.0F, 0.5F).endVertex();
//                tess.getBuffer().pos(0.0F, yMin, yMin).color(0.0F, 1.0F, 0.0F, 0.5F).endVertex();
//                tess.getBuffer().pos(-dist, yMin * 0.4F, yMin * 0.4F).color(0.0F, 1.0F, 0.0F, 0.5F).endVertex();
//                tess.getBuffer().pos(-dist, yMax * 0.4F, yMax * 0.4F).color(0.0F, 1.0F, 0.0F, 0.5F).endVertex();
//
//                tess.getBuffer().pos(0.0F, yMax, yMin).color(0.0F, 1.0F, 0.0F, 0.5F).endVertex();
//                tess.getBuffer().pos(0.0F, yMin, yMax).color(0.0F, 1.0F, 0.0F, 0.5F).endVertex();
//                tess.getBuffer().pos(-dist, yMin * 0.4F, yMax * 0.4F).color(0.0F, 1.0F, 0.0F, 0.5F).endVertex();
//                tess.getBuffer().pos(-dist, yMax * 0.4F, yMin * 0.4F).color(0.0F, 1.0F, 0.0F, 0.5F).endVertex();
//
//                tess.getBuffer().pos(0.0F, yMax1, yMax1).color(0.0F, 1.0F, 0.0F, 0.5F).endVertex();
//                tess.getBuffer().pos(0.0F, yMin1, yMin1).color(0.0F, 1.0F, 0.0F, 0.5F).endVertex();
//                tess.getBuffer().pos(-dist, yMin1 * 0.4F, yMin1 * 0.4F).color(0.0F, 1.0F, 0.0F, 0.5F).endVertex();
//                tess.getBuffer().pos(-dist, yMax1 * 0.4F, yMax1 * 0.4F).color(0.0F, 1.0F, 0.0F, 0.5F).endVertex();
//
//                tess.getBuffer().pos(0.0F, yMax1, yMin1).color(0.0F, 1.0F, 0.0F, 0.5F).endVertex();
//                tess.getBuffer().pos(0.0F, yMin1, yMax1).color(0.0F, 1.0F, 0.0F, 0.5F).endVertex();
//                tess.getBuffer().pos(-dist, yMin1 * 0.4F, yMax1 * 0.4F).color(0.0F, 1.0F, 0.0F, 0.5F).endVertex();
//                tess.getBuffer().pos(-dist, yMax1 * 0.4F, yMin1 * 0.4F).color(0.0F, 1.0F, 0.0F, 0.5F).endVertex();
//
//                tess.draw();
//
//                RenderSystem.enableCull();
//                RenderSystem.enableTexture();
//                RenderSystem.popMatrix();
//            }
//        }
//        RenderSystem.glMultiTexCoord2f(33985, lightMapSaveX, lightMapSaveY);
//        RenderSystem.popMatrix();
//    }
//}
