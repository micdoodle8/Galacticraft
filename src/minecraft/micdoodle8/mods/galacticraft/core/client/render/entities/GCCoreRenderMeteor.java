package micdoodle8.mods.galacticraft.core.client.render.entities;

import micdoodle8.mods.galacticraft.core.client.model.GCCoreModelMeteor;
import micdoodle8.mods.galacticraft.core.entities.GCCoreEntityMeteor;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.entity.Entity;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class GCCoreRenderMeteor extends Render
{
    private final RenderBlocks renderBlocks = new RenderBlocks();
    private final GCCoreModelMeteor modelMeteor;

    public GCCoreRenderMeteor()
    {
        this.shadowSize = 1F;
        this.modelMeteor = new GCCoreModelMeteor();
    }

    public void doRenderMeteor(GCCoreEntityMeteor par1EntityFallingSand, double par2, double par4, double par6, float par8, float par9)
    {
        GL11.glPushMatrix();
        GL11.glTranslatef((float)par2, (float)par4, (float)par6);
        GL11.glRotatef(par8, 0.0F, 1.0F, 0.0F);
        GL11.glRotatef(par8, 1.0F, 0.0F, 0.0F);
        final float f = par1EntityFallingSand.getSize();
        GL11.glScalef(f / 2, f / 2, f / 2);
        this.loadTexture("/micdoodle8/mods/galacticraft/core/client/entities/meteor.png");
        this.modelMeteor.render(par1EntityFallingSand, 0.0F, 0.0F, -0.5F, 0.0F, 0.0F, 0.1F);
        GL11.glPopMatrix();
    }

    /**
     * Actually renders the given argument. This is a synthetic bridge method, always casting down its argument and then
     * handing it off to a worker function which does the actual work. In all probabilty, the class Render is generic
     * (Render<T extends Entity) and this method has signature public void doRender(T entity, double d, double d1,
     * double d2, float f, float f1). But JAD is pre 1.5 so doesn't do that.
     */
    @Override
	public void doRender(Entity par1Entity, double par2, double par4, double par6, float par8, float par9)
    {
        this.doRenderMeteor((GCCoreEntityMeteor)par1Entity, par2, par4, par6, par8, par9);
    }
}
