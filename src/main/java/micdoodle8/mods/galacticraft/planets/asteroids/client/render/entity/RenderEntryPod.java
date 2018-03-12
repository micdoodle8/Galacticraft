package micdoodle8.mods.galacticraft.planets.asteroids.client.render.entity;

import com.google.common.collect.ImmutableList;

import micdoodle8.mods.galacticraft.core.util.ClientUtil;
import micdoodle8.mods.galacticraft.planets.GalacticraftPlanets;
import micdoodle8.mods.galacticraft.planets.asteroids.entities.EntityEntryPod;
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

import org.lwjgl.opengl.GL11;

public class RenderEntryPod extends Render<EntityEntryPod>
{
    private IBakedModel modelEntryPod;

    public RenderEntryPod(RenderManager manager)
    {
        super(manager);
    }

    private void updateModels()
    {
        if (this.modelEntryPod == null)
        {
            try
            {
                this.modelEntryPod = ClientUtil.modelFromOBJ(new ResourceLocation(GalacticraftPlanets.ASSET_PREFIX, "pod.obj"), ImmutableList.of("PodBody"));
            }
            catch (Exception e)
            {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public void doRender(EntityEntryPod entity, double x, double y, double z, float entityYaw, float partialTicks)
    {
        float pitch = entity.prevRotationPitch + (entity.rotationPitch - entity.prevRotationPitch) * partialTicks;
        float yaw = entity.prevRotationYaw + (entity.rotationYaw - entity.prevRotationYaw) * partialTicks;
        GlStateManager.disableRescaleNormal();
        GlStateManager.pushMatrix();
        GlStateManager.translate((float) x, (float) y, (float) z);
        GlStateManager.rotate(180.0F - entityYaw, 0.0F, 1.0F, 0.0F);
        GlStateManager.rotate(180.0F - pitch, 0.0F, 0.0F, 1.0F);
        GlStateManager.rotate(-yaw, 0.0F, 1.0F, 0.0F);

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
        GlStateManager.scale(0.65F, 0.6F, 0.65F);
        ClientUtil.drawBakedModel(this.modelEntryPod);
        GlStateManager.popMatrix();
        RenderHelper.enableStandardItemLighting();
    }

    @Override
    protected ResourceLocation getEntityTexture(EntityEntryPod entity)
    {
        return TextureMap.LOCATION_BLOCKS_TEXTURE;
    }
    
    @Override
    public boolean shouldRender(EntityEntryPod lander, ICamera camera, double camX, double camY, double camZ)
    {
        AxisAlignedBB axisalignedbb = lander.getEntityBoundingBox().grow(1D, 2D, 1D);
        return lander.isInRangeToRender3d(camX, camY, camZ) && camera.isBoundingBoxInFrustum(axisalignedbb);
    }
}
