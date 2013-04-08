package micdoodle8.mods.galacticraft.core.client.fx;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.particle.EntityFX;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.world.World;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

/**
 * Copyright 2012-2013, micdoodle8
 *
 *  All rights reserved.
 *
 */
@SideOnly(Side.CLIENT)
public class GCCoreEntityLaunchSmokeFX extends EntityFX
{
    float smokeParticleScale;

    public GCCoreEntityLaunchSmokeFX(World par1World, double par2, double par4, double par6, double par8, double par10, double par12, float par14, boolean b)
    {
        super(par1World, par2, par4, par6, 0.0D, 0.0D, 0.0D);
        this.motionX *= 0.10000000149011612D;
        this.motionY *= 0.10000000149011612D;
        this.motionZ *= 0.10000000149011612D;
        this.setSize(0.2F, 0.2F);
        this.motionX += par8;
        this.motionY += par10;
        this.motionZ += par12;
        this.particleAlpha = 1.0F;
        this.particleRed = this.particleGreen = this.particleBlue = (float)(Math.random() * 0.30000001192092896D) + 0.6F;
        this.particleScale *= 0.75F;
        this.particleScale *= par14 * 3;
        this.smokeParticleScale = this.particleScale;
        if (b)
        {
            this.particleMaxAge = (int)50.0D;
            this.particleMaxAge = (int)(this.particleMaxAge * par14);
        }
        else
        {
            this.particleMaxAge = (int)2.0D;
            this.particleMaxAge = 1;
        }
        this.noClip = false;
    }

    @Override
	public void renderParticle(Tessellator par1Tessellator, float par2, float par3, float par4, float par5, float par6, float par7)
    {
    	GL11.glPushMatrix();
    	GL11.glDepthMask(false);
    	GL11.glDisable(GL11.GL_DEPTH_TEST);
        float var8 = (this.particleAge + par2) / this.particleMaxAge * 32.0F;

        if (var8 < 0.0F)
        {
            var8 = 0.0F;
        }

        if (var8 > 1.0F)
        {
            var8 = 1.0F;
        }

        this.particleScale = this.smokeParticleScale * var8;
        float f6 = (float)this.particleTextureIndexX / 16.0F;
        float f7 = f6 + 0.0624375F;
        float f8 = (float)this.particleTextureIndexY / 16.0F;
        float f9 = f8 + 0.0624375F;
        float f10 = 0.1F * this.particleScale;

        if (this.particleTextureIndex != null)
        {
            f6 = this.particleTextureIndex.getMinU();
            f7 = this.particleTextureIndex.getMaxU();
            f8 = this.particleTextureIndex.getMinV();
            f9 = this.particleTextureIndex.getMaxV();
        }

        float f11 = (float)(this.prevPosX + (this.posX - this.prevPosX) * (double)par2 - interpPosX);
        float f12 = (float)(this.prevPosY + (this.posY - this.prevPosY) * (double)par2 - interpPosY);
        float f13 = (float)(this.prevPosZ + (this.posZ - this.prevPosZ) * (double)par2 - interpPosZ);
        float f14 = 1.0F;
        par1Tessellator.setColorRGBA_F(this.particleRed * f14, this.particleGreen * f14, this.particleBlue * f14, this.particleAlpha);
        par1Tessellator.addVertexWithUV((double)(f11 - par3 * f10 - par6 * f10), (double)(f12 - par4 * f10), (double)(f13 - par5 * f10 - par7 * f10), (double)f7, (double)f9);
        par1Tessellator.addVertexWithUV((double)(f11 - par3 * f10 + par6 * f10), (double)(f12 + par4 * f10), (double)(f13 - par5 * f10 + par7 * f10), (double)f7, (double)f8);
        par1Tessellator.addVertexWithUV((double)(f11 + par3 * f10 + par6 * f10), (double)(f12 + par4 * f10), (double)(f13 + par5 * f10 + par7 * f10), (double)f6, (double)f8);
        par1Tessellator.addVertexWithUV((double)(f11 + par3 * f10 - par6 * f10), (double)(f12 - par4 * f10), (double)(f13 + par5 * f10 - par7 * f10), (double)f6, (double)f9);

    	GL11.glEnable(GL11.GL_DEPTH_TEST);
    	GL11.glDepthMask(true);
        GL11.glPopMatrix();
    }

    @Override
	public void onUpdate()
    {
        this.prevPosX = this.posX;
        this.prevPosY = this.posY;
        this.prevPosZ = this.posZ;

        if (this.particleAge++ >= this.particleMaxAge)
        {
            this.setDead();
        }

        this.setParticleTextureIndex(7 - this.particleAge * 8 / this.particleMaxAge);
        this.motionY += 0.001D;
        this.moveEntity(this.motionX, this.motionY, this.motionZ);

        if (this.posY == this.prevPosY)
        {
            this.motionX *= 1.1D;
            this.motionZ *= 1.1D;
        }

        this.motionX *= 0.9599999785423279D;
        this.motionY *= 0.9599999785423279D;
        this.motionZ *= 0.9599999785423279D;

        if (this.onGround)
        {
        	this.motionX = (this.rand.nextFloat() * 2.0F * this.rand.nextInt(2) * 2 - 1) / 4.0;
        	this.motionZ = (this.rand.nextFloat() * 2.0F * this.rand.nextInt(2) * 2 - 1) / 4.0;

        	this.motionX *= 0.699999988079071D;
            this.motionZ *= 0.699999988079071D;
        }
    }
}
