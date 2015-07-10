package micdoodle8.mods.galacticraft.core.client.render.entities;

import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.client.model.ModelLander;
import micdoodle8.mods.galacticraft.core.entities.EntityLander;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class RenderLander extends Render
{
    private static final ResourceLocation landerTexture = new ResourceLocation(GalacticraftCore.ASSET_PREFIX, "textures/model/lander.png");

    protected ModelLander landerModel;

    public RenderLander()
    {
        super(FMLClientHandler.instance().getClient().getRenderManager());
        this.shadowSize = 2F;
        this.landerModel = new ModelLander();
    }

    protected ResourceLocation func_110779_a(EntityLander par1EntityArrow)
    {
        return RenderLander.landerTexture;
    }

    @Override
    protected ResourceLocation getEntityTexture(Entity par1Entity)
    {
        return this.func_110779_a((EntityLander) par1Entity);
    }

    public void renderLander(EntityLander entity, double par2, double par4, double par6, float par8, float par9)
    {
        GL11.glPushMatrix();
        final float var24 = entity.prevRotationPitch + (entity.rotationPitch - entity.prevRotationPitch) * par9;
        GL11.glTranslatef((float) par2, (float) par4 + 1.45F, (float) par6);
        GL11.glRotatef(180.0F - par8, 0.0F, 1.0F, 0.0F);
        GL11.glRotatef(-var24, 0.0F, 0.0F, 1.0F);

        float f6 = entity.timeSinceHit - par9;
        float f7 = entity.currentDamage - par9;

        if (f7 < 0.0F)
        {
            f7 = 0.0F;
        }

        if (f6 > 0.0F)
        {
            GL11.glRotatef((float) Math.sin(f6) * 0.2F * f6 * f7 / 25.0F, 1.0F, 0.0F, 0.0F);
        }

        this.bindEntityTexture(entity);
        GL11.glScalef(-1.0F, -1.0F, 1.0F);
        this.landerModel.render(entity, 0.0F, 0.0F, -0.1F, 0.0F, 0.0F, 0.0625F);
        GL11.glPopMatrix();
    }

    @Override
    public void doRender(Entity par1Entity, double par2, double par4, double par6, float par8, float par9)
    {
        this.renderLander((EntityLander) par1Entity, par2, par4, par6, par8, par9);
    }
}
