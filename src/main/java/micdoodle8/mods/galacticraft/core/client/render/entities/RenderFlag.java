package micdoodle8.mods.galacticraft.core.client.render.entities;

import micdoodle8.mods.galacticraft.core.Constants;
import micdoodle8.mods.galacticraft.core.client.model.ModelFlag;
import micdoodle8.mods.galacticraft.core.entities.EntityFlag;
import net.minecraft.client.renderer.culling.ICamera;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import org.lwjgl.opengl.GL11;

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
    public void doRender(EntityFlag flag, double par2, double par4, double par6, float par8, float par9)
    {
        GL11.glPushMatrix();
        long var10 = flag.getEntityId() * 493286711L;
        var10 = var10 * var10 * 4392167121L + var10 * 98761L;
        final float var12 = (((var10 >> 16 & 7L) + 0.5F) / 8.0F - 0.5F) * 0.004F;
        final float var13 = (((var10 >> 20 & 7L) + 0.5F) / 8.0F - 0.5F) * 0.004F;
        final float var14 = (((var10 >> 24 & 7L) + 0.5F) / 8.0F - 0.5F) * 0.004F;
        GL11.glTranslatef(var12, var13 + 1.5F, var14);

        GL11.glTranslatef((float) par2, (float) par4, (float) par6);
        GL11.glRotatef(180.0F - flag.getFacingAngle(), 0.0F, 1.0F, 0.0F);
        this.bindEntityTexture(flag);
        GL11.glScalef(-1.0F, -1.0F, 1.0F);
        this.modelFlag.render(flag, 0.0F, 0.0F, -0.1F, 0.0F, 0.0F, 0.0625F);
        GL11.glPopMatrix();
    }
    
    @Override
    public boolean shouldRender(EntityFlag lander, ICamera camera, double camX, double camY, double camZ)
    {
        AxisAlignedBB axisalignedbb = lander.getEntityBoundingBox().expand(1D, 2D, 1D);
        return lander.isInRangeToRender3d(camX, camY, camZ) && camera.isBoundingBoxInFrustum(axisalignedbb);
    }
}
