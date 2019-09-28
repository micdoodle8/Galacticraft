package micdoodle8.mods.galacticraft.planets.venus.client.render.tile;

import org.lwjgl.opengl.GL11;

import com.google.common.base.Function;
import com.google.common.collect.ImmutableList;

import micdoodle8.mods.galacticraft.api.vector.Vector3;
import micdoodle8.mods.galacticraft.core.client.model.OBJLoaderGC;
import micdoodle8.mods.galacticraft.core.util.ClientUtil;
import micdoodle8.mods.galacticraft.planets.GalacticraftPlanets;
import micdoodle8.mods.galacticraft.planets.venus.tile.TileEntityLaserTurret;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.IModel;
import net.minecraftforge.client.model.obj.OBJModel;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class TileEntityLaserTurretRenderer extends TileEntitySpecialRenderer<TileEntityLaserTurret>
{
    public static OBJModel.OBJBakedModel laserBase;
    public static OBJModel.OBJBakedModel laserPhalange;
    public static OBJModel.OBJBakedModel laserPhalangeAxle;
    public static OBJModel.OBJBakedModel laserTurrets;
    public static OBJModel.OBJBakedModel laserTurretsOff;
    public static OBJModel.OBJBakedModel orb1;
    public static OBJModel.OBJBakedModel orb2;

    public IBakedModel updateModels()
    {
        if (laserBase == null)
        {
            try
            {
                IModel model0 = OBJLoaderGC.instance.loadModel(new ResourceLocation(GalacticraftPlanets.ASSET_PREFIX, "laser.obj"));
                IModel model1 = OBJLoaderGC.instance.loadModel(new ResourceLocation(GalacticraftPlanets.ASSET_PREFIX, "orb.obj"));
                Function<ResourceLocation, TextureAtlasSprite> spriteFunction = location -> Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite(location.toString());

                laserBase = (OBJModel.OBJBakedModel) model0.bake(new OBJModel.OBJState(ImmutableList.of("baseConnector0", "baseConnector1"), false), DefaultVertexFormats.ITEM, spriteFunction);
                laserPhalange = (OBJModel.OBJBakedModel) model0.bake(new OBJModel.OBJState(ImmutableList.of("phalange"), false), DefaultVertexFormats.ITEM, spriteFunction);
                laserPhalangeAxle = (OBJModel.OBJBakedModel) model0.bake(new OBJModel.OBJState(ImmutableList.of("phalangeAxle"), false), DefaultVertexFormats.ITEM, spriteFunction);
                laserTurrets = (OBJModel.OBJBakedModel) model0.bake(new OBJModel.OBJState(ImmutableList.of("turretLeft", "turretRight"), false), DefaultVertexFormats.ITEM, spriteFunction);
                laserTurretsOff = (OBJModel.OBJBakedModel) model0.bake(new OBJModel.OBJState(ImmutableList.of("turretLeft_off", "turretRight_off"), false), DefaultVertexFormats.ITEM, spriteFunction);
                orb1 = (OBJModel.OBJBakedModel) model1.bake(new OBJModel.OBJState(ImmutableList.of("inner_Icosphere"), false), DefaultVertexFormats.ITEM, spriteFunction);
                orb2 = (OBJModel.OBJBakedModel) model1.bake(new OBJModel.OBJState(ImmutableList.of("outer_Icosphere.001"), false), DefaultVertexFormats.ITEM, spriteFunction);
            }
            catch (Exception e)
            {
                throw new RuntimeException(e);
            }
        }
        return laserBase;
    }

    @Override
    public void render(TileEntityLaserTurret tile, double x, double y, double z, float partialTicks, int destroyStage, float alpha)
    {
        GlStateManager.pushMatrix();
        GlStateManager.translate((float) x + 0.5F, (float) y + 1.5F, (float) z + 0.5F);
        GlStateManager.pushMatrix();
        RenderHelper.disableStandardItemLighting();

        this.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);

        if (Minecraft.isAmbientOcclusionEnabled())
        {
            GlStateManager.shadeModel(GL11.GL_SMOOTH);
        }
        else
        {
            GlStateManager.shadeModel(GL11.GL_FLAT);
        }

        this.updateModels();

        GlStateManager.scale(1 / 16.0F, 1 / 16.0F, 1 / 16.0F);

        GlStateManager.pushMatrix();
        GlStateManager.scale(0.9F, 1.0F, 0.9F);
        ClientUtil.drawBakedModel(laserBase);
        GlStateManager.popMatrix();

        GlStateManager.rotate(tile.yaw, 0.0F, 1.0F, 0.0F);

        // Interpolate between yaw and targetYaw

        float partialRot = Math.signum(tile.pitch) * tile.pitch * (tile.pitch / 120.0F);
        GlStateManager.rotate(partialRot, 0.0F, 0.0F, -1.0F);

        GlStateManager.pushMatrix();
        GlStateManager.scale(1.1F, 1.0F, 1.0F);

        ClientUtil.drawBakedModel(laserPhalange);
        GlStateManager.popMatrix();

        GlStateManager.rotate(tile.pitch - partialRot, 0.0F, 0.0F, -1.0F);

        ClientUtil.drawBakedModel(laserPhalangeAxle);
        ClientUtil.drawBakedModel(tile.active ? laserTurrets : laserTurretsOff);

        GlStateManager.popMatrix();
        Tessellator tess = Tessellator.getInstance();

        float inv = (float) (Math.pow(tile.chargeLevel / 5.0F + 1.0F, 2.5F) * 1.0F);
        float invNext = (float) (Math.pow((tile.chargeLevel + 1) / 5.0F + 1.0F, 2.5F) * 1.0F);
        float rotate = inv + (invNext - inv) * partialTicks;

        float lightMapSaveX = OpenGlHelper.lastBrightnessX;
        float lightMapSaveY = OpenGlHelper.lastBrightnessY;
        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 240F, 240F);

        if (tile.chargeLevel > 0)
        {
            GlStateManager.pushMatrix();
            GlStateManager.enableBlend();
            GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
            GlStateManager.rotate(tile.yaw, 0.0F, 1.0F, 0.0F);
            GlStateManager.rotate(tile.pitch, 0.0F, 0.0F, -1.0F);
            GlStateManager.translate(-0.6F, 0.28F, 0.0F);

            GlStateManager.disableTexture2D();

            tess.getBuffer().begin(GL11.GL_LINE_LOOP, DefaultVertexFormats.POSITION_COLOR);

            tess.getBuffer().pos(0.09F, 0.0F, 0.275F).color(0.0F, 1.0F, 0.0F, 1.0F).endVertex();
            tess.getBuffer().pos(0.0F, 0.0F, 0.0F).color(0.0F, 1.0F, 0.0F, 1.0F).endVertex();
            tess.getBuffer().pos(0.09F, 0.0F, -0.275F).color(0.0F, 1.0F, 0.0F, 1.0F).endVertex();

            tess.draw();
            GlStateManager.enableTexture2D();

            float scale = tile.chargeLevel / 600.0F;
            GlStateManager.scale(0.01F + scale, 0.01F + scale, 0.01F + scale);
            GlStateManager.rotate(rotate, 0.0F, 1.0F, 0.0F);
            ClientUtil.drawBakedModel(orb1);
            GlStateManager.rotate(rotate, 0.0F, 1.0F, 0.0F);
            ClientUtil.drawBakedModel(orb2);
            GlStateManager.disableBlend();
            GlStateManager.popMatrix();
        }

        if (tile.timeSinceShot > 0 && tile.timeSinceShot < 5)
        {
            Entity e = tile.getWorld().getEntityByID(tile.targettedEntity);

            if (e != null)
            {
                GlStateManager.disableTexture2D();
                GlStateManager.pushMatrix();
                GlStateManager.enableBlend();
                GlStateManager.disableCull();
                GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
                GlStateManager.rotate(tile.yaw, 0.0F, 1.0F, 0.0F);
                GlStateManager.rotate(tile.pitch, 0.0F, 0.0F, -1.0F);
                GlStateManager.translate(-0.6F, 0.28F, 0.0F);

                BufferBuilder bb = tess.getBuffer();

                bb.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_COLOR);

                Vector3 vec = new Vector3(e.posX, e.posY + e.getEyeHeight(), e.posZ);
                vec.translate(new Vector3(-(tile.getPos().getX() + 0.5F), -(tile.getPos().getY() + 1.78F), -(tile.getPos().getZ() + 0.5F)));
                float dist = (float) vec.getMagnitude() - 0.8F;
                float shotTimer = (float) (Math.pow((5.0F - tile.timeSinceShot) / 5.0F + 1.0F, 2.5F) * 0.5F);
                float shotTimerNext = (float) (Math.pow((5.0F - (tile.timeSinceShot + 1)) / 5.0F + 1.0F, 2.5F) * 0.5F);
                float fade = shotTimer + (shotTimerNext - shotTimer) * partialTicks;
                float yMin = -0.101F * fade;
                float yMax = 0.101F * fade;
                float yMin1 = -0.15F * fade;
                float yMax1 = 0.15F * fade;

                tess.getBuffer().pos(0.0F, yMax, yMax).color(0.0F, 1.0F, 0.0F, 0.5F).endVertex();
                tess.getBuffer().pos(0.0F, yMin, yMin).color(0.0F, 1.0F, 0.0F, 0.5F).endVertex();
                tess.getBuffer().pos(-dist, yMin * 0.4F, yMin * 0.4F).color(0.0F, 1.0F, 0.0F, 0.5F).endVertex();
                tess.getBuffer().pos(-dist, yMax * 0.4F, yMax * 0.4F).color(0.0F, 1.0F, 0.0F, 0.5F).endVertex();

                tess.getBuffer().pos(0.0F, yMax, yMin).color(0.0F, 1.0F, 0.0F, 0.5F).endVertex();
                tess.getBuffer().pos(0.0F, yMin, yMax).color(0.0F, 1.0F, 0.0F, 0.5F).endVertex();
                tess.getBuffer().pos(-dist, yMin * 0.4F, yMax * 0.4F).color(0.0F, 1.0F, 0.0F, 0.5F).endVertex();
                tess.getBuffer().pos(-dist, yMax * 0.4F, yMin * 0.4F).color(0.0F, 1.0F, 0.0F, 0.5F).endVertex();

                tess.getBuffer().pos(0.0F, yMax1, yMax1).color(0.0F, 1.0F, 0.0F, 0.5F).endVertex();
                tess.getBuffer().pos(0.0F, yMin1, yMin1).color(0.0F, 1.0F, 0.0F, 0.5F).endVertex();
                tess.getBuffer().pos(-dist, yMin1 * 0.4F, yMin1 * 0.4F).color(0.0F, 1.0F, 0.0F, 0.5F).endVertex();
                tess.getBuffer().pos(-dist, yMax1 * 0.4F, yMax1 * 0.4F).color(0.0F, 1.0F, 0.0F, 0.5F).endVertex();

                tess.getBuffer().pos(0.0F, yMax1, yMin1).color(0.0F, 1.0F, 0.0F, 0.5F).endVertex();
                tess.getBuffer().pos(0.0F, yMin1, yMax1).color(0.0F, 1.0F, 0.0F, 0.5F).endVertex();
                tess.getBuffer().pos(-dist, yMin1 * 0.4F, yMax1 * 0.4F).color(0.0F, 1.0F, 0.0F, 0.5F).endVertex();
                tess.getBuffer().pos(-dist, yMax1 * 0.4F, yMin1 * 0.4F).color(0.0F, 1.0F, 0.0F, 0.5F).endVertex();

                tess.draw();

                GlStateManager.enableCull();
                GlStateManager.enableTexture2D();
                GlStateManager.popMatrix();
            }
        }
        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, lightMapSaveX, lightMapSaveY);
        GlStateManager.popMatrix();
    }
}
