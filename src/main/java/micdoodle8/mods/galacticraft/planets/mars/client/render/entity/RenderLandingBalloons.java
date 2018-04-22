package micdoodle8.mods.galacticraft.planets.mars.client.render.entity;

import com.google.common.collect.ImmutableList;

import micdoodle8.mods.galacticraft.core.util.ClientUtil;
import micdoodle8.mods.galacticraft.planets.GalacticraftPlanets;
import micdoodle8.mods.galacticraft.planets.mars.client.model.ModelBalloonParachute;
import micdoodle8.mods.galacticraft.planets.mars.entities.EntityLandingBalloons;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.culling.ICamera;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class RenderLandingBalloons extends Render<EntityLandingBalloons>
{
    private IBakedModel balloonModel;
    protected ModelBalloonParachute parachuteModel = new ModelBalloonParachute();

    public RenderLandingBalloons(RenderManager manager)
    {
        super(manager);
        this.shadowSize = 1.2F;
    }

    private void updateModels()
    {
        if (this.balloonModel == null)
        {
            try
            {
                this.balloonModel = ClientUtil.modelFromOBJ(new ResourceLocation(GalacticraftPlanets.ASSET_PREFIX, "landing_balloon.obj"), ImmutableList.of("Sphere"));
            }
            catch (Exception e)
            {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    protected ResourceLocation getEntityTexture(EntityLandingBalloons entity)
    {
        return TextureMap.LOCATION_BLOCKS_TEXTURE;
    }

    @Override
    public void doRender(EntityLandingBalloons entity, double x, double y, double z, float entityYaw, float partialTicks)
    {
        float pitch = entity.prevRotationPitch + (entity.rotationPitch - entity.prevRotationPitch) * partialTicks;
        GlStateManager.disableRescaleNormal();
        GlStateManager.pushMatrix();
        GlStateManager.translate((float) x, (float) y + 0.8F, (float) z);
        GlStateManager.rotate(entityYaw, 0.0F, 1.0F, 0.0F);
        GlStateManager.rotate(180.0F, 1.0F, 0.0F, 0.0F);
        GlStateManager.rotate(pitch, 0.0F, 0.0F, 1.0F);

        this.updateModels();
        this.bindEntityTexture(entity);

        if (Minecraft.isAmbientOcclusionEnabled())
        {
            GlStateManager.shadeModel(GL11.GL_SMOOTH);
        }
        else
        {
            GlStateManager.shadeModel(GL11.GL_FLAT);
        }

        GlStateManager.scale(-1.0F, -1.0F, 1.0F);
        GlStateManager.scale(0.5F, 0.5F, 0.5F);
        ClientUtil.drawBakedModel(this.balloonModel);
        GlStateManager.popMatrix();

        if (entity.posY >= 500.0F)
        {
            GlStateManager.pushMatrix();
            GlStateManager.translate((float) x - 1.25F, (float) y - 0.93F, (float) z - 0.3F);
            GlStateManager.scale(2.5F, 3.0F, 2.5F);
            this.parachuteModel.renderAll();
            GlStateManager.popMatrix();
        }
        RenderHelper.enableStandardItemLighting();
    }
    
    @Override
    public boolean shouldRender(EntityLandingBalloons lander, ICamera camera, double camX, double camY, double camZ)
    {
        AxisAlignedBB axisalignedbb = lander.getEntityBoundingBox().grow(2D, 1D, 2D);
        return lander.isInRangeToRender3d(camX, camY, camZ) && camera.isBoundingBoxInFrustum(axisalignedbb);
    }
}
