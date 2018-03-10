package micdoodle8.mods.galacticraft.core.client.render.entities;

import micdoodle8.mods.galacticraft.core.Constants;
import micdoodle8.mods.galacticraft.core.client.model.ModelLander;
import micdoodle8.mods.galacticraft.core.entities.EntityLander;
import net.minecraft.client.renderer.culling.ICamera;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class RenderLander extends Render<EntityLander>
{
    private static final ResourceLocation landerTexture = new ResourceLocation(Constants.ASSET_PREFIX, "textures/model/lander.png");

    protected ModelLander landerModel;

    public RenderLander(RenderManager manager)
    {
        super(manager);
        this.shadowSize = 2F;
        this.landerModel = new ModelLander();
    }

    @Override
    protected ResourceLocation getEntityTexture(EntityLander par1Entity)
    {
        return RenderLander.landerTexture;
    }

    @Override
    public void doRender(EntityLander lander, double par2, double par4, double par6, float par8, float par9)
    {
        GL11.glPushMatrix();
        final float var24 = lander.prevRotationPitch + (lander.rotationPitch - lander.prevRotationPitch) * par9;
        GL11.glTranslatef((float) par2, (float) par4 + 1.55F, (float) par6);
        GL11.glRotatef(180.0F - par8, 0.0F, 1.0F, 0.0F);
        GL11.glRotatef(-var24, 0.0F, 0.0F, 1.0F);

        float f6 = lander.timeSinceHit - par9;
        float f7 = lander.currentDamage - par9;

        if (f7 < 0.0F)
        {
            f7 = 0.0F;
        }

        if (f6 > 0.0F)
        {
            GL11.glRotatef((float) Math.sin(f6) * 0.2F * f6 * f7 / 25.0F, 1.0F, 0.0F, 0.0F);
        }

        this.bindEntityTexture(lander);
        GL11.glScalef(-1.0F, -1.0F, 1.0F);
        this.landerModel.render(lander, 0.0F, 0.0F, -0.1F, 0.0F, 0.0F, 0.0625F);
        GL11.glPopMatrix();
    }
    
    @Override
    public boolean shouldRender(EntityLander lander, ICamera camera, double camX, double camY, double camZ)
    {
        AxisAlignedBB axisalignedbb = lander.getEntityBoundingBox().expand(2D, 1D, 2D);
        return lander.isInRangeToRender3d(camX, camY, camZ) && camera.isBoundingBoxInFrustum(axisalignedbb);
    }
}
