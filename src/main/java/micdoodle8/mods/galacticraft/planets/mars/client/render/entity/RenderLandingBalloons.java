package micdoodle8.mods.galacticraft.planets.mars.client.render.entity;

import com.google.common.collect.ImmutableList;

import micdoodle8.mods.galacticraft.core.util.ClientUtil;
import micdoodle8.mods.galacticraft.planets.GalacticraftPlanets;
import micdoodle8.mods.galacticraft.planets.mars.client.model.ModelBalloonParachute;
import micdoodle8.mods.galacticraft.planets.mars.entities.EntityLandingBalloons;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.culling.ICamera;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.IFlexibleBakedModel;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class RenderLandingBalloons extends Render<EntityLandingBalloons>
{
    private IFlexibleBakedModel balloonModel;
    protected ModelBalloonParachute parachuteModel = new ModelBalloonParachute();

    public RenderLandingBalloons(RenderManager manager)
    {
        super(manager);
        this.shadowSize = 1.2F;
    }

    private void updateModels()
    {
        if (balloonModel == null)
        {
            try
            {
                balloonModel = ClientUtil.modelFromOBJ(new ResourceLocation(GalacticraftPlanets.ASSET_PREFIX, "landing_balloon.obj"), ImmutableList.of("Sphere"));
            }
            catch (Exception e)
            {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    protected ResourceLocation getEntityTexture(EntityLandingBalloons par1Entity)
    {
        return new ResourceLocation("missing");
    }

    @Override
    public void doRender(EntityLandingBalloons entity, double par2, double par4, double par6, float par8, float par9)
    {
        GL11.glPushMatrix();
        final float var24 = entity.prevRotationPitch + (entity.rotationPitch - entity.prevRotationPitch) * par9;
        GL11.glTranslatef((float) par2, (float) par4 + 0.8F, (float) par6);
        GL11.glRotatef(par8, 0.0F, 1.0F, 0.0F);
        GL11.glRotatef(180.0F, 1.0F, 0.0F, 0.0F);
        GL11.glRotatef(var24, 0.0F, 0.0F, 1.0F);

        this.updateModels();

        this.bindTexture(TextureMap.locationBlocksTexture);

        if (Minecraft.isAmbientOcclusionEnabled())
        {
            GlStateManager.shadeModel(GL11.GL_SMOOTH);
        }
        else
        {
            GlStateManager.shadeModel(GL11.GL_FLAT);
        }

        GL11.glScalef(-1.0F, -1.0F, 1.0F);
        GL11.glScalef(0.5F, 0.5F, 0.5F);
        ClientUtil.drawBakedModel(this.balloonModel);
        GL11.glPopMatrix();

        if (entity.posY >= 500.0F)
        {
            GL11.glPushMatrix();

            GL11.glTranslatef((float) par2 - 1.25F, (float) par4 - 0.93F, (float) par6 - 0.3F);
            GL11.glScalef(2.5F, 3.0F, 2.5F);
            this.parachuteModel.renderAll();

            GL11.glPopMatrix();
        }
    }
    
    @Override
    public boolean shouldRender(EntityLandingBalloons lander, ICamera camera, double camX, double camY, double camZ)
    {
        AxisAlignedBB axisalignedbb = lander.getEntityBoundingBox().expand(2D, 1D, 2D);
        return lander.isInRangeToRender3d(camX, camY, camZ) && camera.isBoundingBoxInFrustum(axisalignedbb);
    }
}
