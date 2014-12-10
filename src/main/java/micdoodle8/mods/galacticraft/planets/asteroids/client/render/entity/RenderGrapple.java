package micdoodle8.mods.galacticraft.planets.asteroids.client.render.entity;

import micdoodle8.mods.galacticraft.planets.asteroids.client.render.item.ItemRendererGrappleHook;
import micdoodle8.mods.galacticraft.planets.asteroids.entities.EntityGrapple;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Vec3;
import org.lwjgl.opengl.GL11;

public class RenderGrapple extends Render
{
    public void doRender(EntityGrapple grapple, double x, double y, double z, float par8, float partialTicks)
    {
        GL11.glPushMatrix();

        Vec3 vec3 = Vec3.createVectorHelper(0.0D, -0.2D, 0.0D);
        EntityPlayer shootingEntity = grapple.getShootingEntity();

        if (shootingEntity != null && grapple.getPullingEntity())
        {
            double d3 = shootingEntity.prevPosX + (shootingEntity.posX - shootingEntity.prevPosX) * partialTicks + vec3.xCoord;
            double d4 = shootingEntity.prevPosY + (shootingEntity.posY - shootingEntity.prevPosY) * partialTicks + vec3.yCoord;
            double d5 = shootingEntity.prevPosZ + (shootingEntity.posZ - shootingEntity.prevPosZ) * partialTicks + vec3.zCoord;

            Tessellator tessellator = Tessellator.instance;
            GL11.glDisable(GL11.GL_TEXTURE_2D);
            GL11.glDisable(GL11.GL_LIGHTING);
            tessellator.startDrawing(GL11.GL_LINE_STRIP);
            tessellator.setColorOpaque_F(203.0F / 255.0F, 203.0F / 255.0F, 192.0F / 255.0F);
            byte b2 = 16;

            double d14 = grapple.prevPosX + (grapple.posX - grapple.prevPosX) * partialTicks;
            double d8 = grapple.prevPosY + (grapple.posY - grapple.prevPosY) * partialTicks + 0.25D;
            double d10 = grapple.prevPosZ + (grapple.posZ - grapple.prevPosZ) * partialTicks;
            double d11 = (float) (d3 - d14);
            double d12 = (float) (d4 - d8);
            double d13 = (float) (d5 - d10);
            tessellator.addTranslation(0, -0.2F, 0);

            for (int i = 0; i <= b2; ++i)
            {
                float f12 = (float) i / (float) b2;
                tessellator.addVertex(x + d11 * f12, y + d12 * (f12 * f12 + f12) * 0.5D + 0.15D, z + d13 * f12);
            }

            tessellator.draw();
            tessellator.setTranslation(0, 0, 0);
            GL11.glEnable(GL11.GL_LIGHTING);
            GL11.glEnable(GL11.GL_TEXTURE_2D);
        }

        GL11.glTranslatef((float) x, (float) y, (float) z);
        GL11.glRotatef(grapple.prevRotationYaw + (grapple.rotationYaw - grapple.prevRotationYaw) * partialTicks - 90.0F, 0.0F, 1.0F, 0.0F);
        GL11.glRotatef(grapple.prevRotationPitch + (grapple.rotationPitch - grapple.prevRotationPitch) * partialTicks - 180, 0.0F, 0.0F, 1.0F);
        GL11.glRotatef(grapple.prevRotationRoll + (grapple.rotationRoll - grapple.prevRotationRoll) * partialTicks, 1.0F, 0.0F, 0.0F);
        this.bindEntityTexture(grapple);
        ItemRendererGrappleHook.modelGrapple.renderAll();

        GL11.glPopMatrix();
    }

    protected ResourceLocation getEntityTexture(EntityGrapple grapple)
    {
        return ItemRendererGrappleHook.grappleTexture;
    }

    @Override
    protected ResourceLocation getEntityTexture(Entity entity)
    {
        return this.getEntityTexture((EntityGrapple) entity);
    }

    @Override
    public void doRender(Entity entity, double x, double y, double z, float par8, float partialTicks)
    {
        this.doRender((EntityGrapple) entity, x, y, z, par8, partialTicks);
    }
}
