package micdoodle8.mods.galacticraft.core.client.render.entities;

import micdoodle8.mods.galacticraft.core.Constants;
import micdoodle8.mods.galacticraft.core.client.model.ModelFlag;
import micdoodle8.mods.galacticraft.core.entities.EntityFlag;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.culling.ICamera;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RenderFlag extends Render<EntityFlag>
{
    public static ResourceLocation flagTexture = new ResourceLocation(Constants.ASSET_PREFIX, "textures/model/flag.png");

    protected ModelFlag modelFlag;

    public RenderFlag(RenderManager manager)
    {
        super(manager);
        this.shadowSize = 1F;
        this.modelFlag = new ModelFlag();
    }

    @Override
    protected ResourceLocation getEntityTexture(EntityFlag entity)
    {
        return RenderFlag.flagTexture;
    }

    @Override
    public void doRender(EntityFlag entity, double x, double y, double z, float entityYaw, float partialTicks)
    {
        GlStateManager.disableRescaleNormal();
        GlStateManager.pushMatrix();
        long seed = entity.getEntityId() * 493286711L;
        seed = seed * seed * 4392167121L + seed * 98761L;
        float seedX = (((seed >> 16 & 7L) + 0.5F) / 8.0F - 0.5F) * 0.004F;
        float seedY = (((seed >> 20 & 7L) + 0.5F) / 8.0F - 0.5F) * 0.004F;
        float seedZ = (((seed >> 24 & 7L) + 0.5F) / 8.0F - 0.5F) * 0.004F;
        GlStateManager.translate(seedX, seedY + 1.5F, seedZ);
        GlStateManager.translate((float) x, (float) y, (float) z);
        GlStateManager.rotate(180.0F - entity.getFacingAngle(), 0.0F, 1.0F, 0.0F);
        this.bindEntityTexture(entity);
        GlStateManager.scale(-1.0F, -1.0F, 1.0F);
        this.modelFlag.render(entity, 0.0F, 0.0F, -0.1F, 0.0F, 0.0F, 0.0625F);
        GlStateManager.popMatrix();
    }
    
    @Override
    public boolean shouldRender(EntityFlag lander, ICamera camera, double camX, double camY, double camZ)
    {
        AxisAlignedBB axisalignedbb = lander.getEntityBoundingBox().grow(1D, 2D, 1D);
        return lander.isInRangeToRender3d(camX, camY, camZ) && camera.isBoundingBoxInFrustum(axisalignedbb);
    }
}
