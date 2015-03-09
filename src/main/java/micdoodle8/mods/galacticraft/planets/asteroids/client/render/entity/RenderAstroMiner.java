package micdoodle8.mods.galacticraft.planets.asteroids.client.render.entity;

import micdoodle8.mods.galacticraft.planets.asteroids.entities.EntityAstroMiner;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

public class RenderAstroMiner extends Render
{
    private RenderBlocks blockRenderer = new RenderBlocks();
    private float spin;
    private float lastPartTime;

    @Override
    public void doRender(Entity entity, double x, double y, double z, float f, float partialTickTime)
    {
        EntityAstroMiner asteroid = (EntityAstroMiner) entity;

        GL11.glPushMatrix();

        /*
        GL11.glEnable(GL11.GL_BLEND);
        OpenGlHelper.glBlendFunc(770, 771, 1, 0);
        GL11.glColor4f(0.0F, 0.0F, 0.0F, 0.4F);
        GL11.glLineWidth(2.0F);
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glDepthMask(false);
        RenderGlobal.drawOutlinedBoundingBox(asteroid.boundingBox, 16777215);
        GL11.glDepthMask(true);
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glDisable(GL11.GL_BLEND);
         */
        GL11.glTranslatef((float) x, (float) y + 1, (float) z);
        GL11.glRotatef(asteroid.rotationPitch, 1, 0, 0);
        GL11.glRotatef(asteroid.rotationYaw, 0, 1, 0);
        GL11.glTranslatef(0F, -0.65F, 0F);
        GL11.glScalef(1.3F, 1.3F, 1.3F);

        this.bindEntityTexture(asteroid);
        this.blockRenderer.renderBlockAsItem(Blocks.coal_block, 0, 1.0F);
        GL11.glTranslatef(-0.6F, 0F, 0F);
        float partTime = partialTickTime - lastPartTime;
        lastPartTime = partialTickTime;
        while (partTime < 0) partTime += 1F;
        spin += 2F * partTime;
        if (spin >= 360F) spin-=360F;
        GL11.glRotatef(spin, 1, 0, 0);
        GL11.glScalef(0.5F, 1F, 1F);
        this.blockRenderer.renderBlockAsItem(Blocks.diamond_block, 0, 1.0F);

        GL11.glPopMatrix();
    }

    @Override
    protected ResourceLocation getEntityTexture(Entity entity)
    {
        return TextureMap.locationBlocksTexture;
    }
}
