package micdoodle8.mods.galacticraft.core.client.render.entities;

import micdoodle8.mods.galacticraft.core.entities.GCCoreEntityAstroOrb;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.entity.Entity;
import net.minecraft.util.MathHelper;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class GCCoreRenderAstroOrb extends Render
{
    public GCCoreRenderAstroOrb()
    {
        this.shadowSize = 0.15F;
        this.shadowOpaque = 0.75F;
    }

    public void renderTheXPOrb(GCCoreEntityAstroOrb par1EntityXPOrb, double par2, double par4, double par6, float par8, float par9)
    {
        GL11.glPushMatrix();
        GL11.glTranslatef((float)par2, (float)par4, (float)par6);
        int var10 = par1EntityXPOrb.getTextureByXP();
        this.loadTexture("/item/xporb.png");
        Tessellator var11 = Tessellator.instance;
        float var12 = (var10 % 4 * 16 + 0) / 64.0F;
        float var13 = (var10 % 4 * 16 + 16) / 64.0F;
        float var14 = (var10 / 4 * 16 + 0) / 64.0F;
        float var15 = (var10 / 4 * 16 + 16) / 64.0F;
        float var16 = 1.0F;
        float var17 = 0.5F;
        float var18 = 0.25F;
        int var19 = par1EntityXPOrb.getBrightnessForRender(par9);
        int var20 = var19 % 65536;
        int var21 = var19 / 65536;
        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, var20 / 1.0F, var21 / 1.0F);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        float var26 = 255.0F;
        float var27 = (par1EntityXPOrb.xpColor + par9) / 2.0F;
        var21 = (int)((MathHelper.sin(var27 + 0.0F) + 1.0F) * 0.0F * var26);
        int var22 = (int)var26;
        int var23 = (int)((MathHelper.sin(var27 + 4.1887903F) + 1.0F) * 5F * var26);
        int var24 = var21 / 2 << 16 | var22 / 2 << 8 | 255;
        GL11.glRotatef(180.0F - this.renderManager.playerViewY, 0.0F, 1.0F, 0.0F);
        GL11.glRotatef(-this.renderManager.playerViewX, 1.0F, 0.0F, 0.0F);
        float var25 = 0.2F;
        GL11.glScalef(var25, var25, var25);
        var11.startDrawingQuads();
        var11.setColorRGBA_I(var24, 128);
        var11.setNormal(0.0F, 1.0F, 0.0F);
        var11.addVertexWithUV((0.0F - var17), (0.0F - var18), 0.0D, var12, var15);
        var11.addVertexWithUV((var16 - var17), (0.0F - var18), 0.0D, var13, var15);
        var11.addVertexWithUV((var16 - var17), (1.0F - var18), 0.0D, var13, var14);
        var11.addVertexWithUV((0.0F - var17), (1.0F - var18), 0.0D, var12, var14);
        var11.draw();
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glDisable(GL12.GL_RESCALE_NORMAL);
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
        this.renderTheXPOrb((GCCoreEntityAstroOrb)par1Entity, par2, par4, par6, par8, par9);
    }
}
