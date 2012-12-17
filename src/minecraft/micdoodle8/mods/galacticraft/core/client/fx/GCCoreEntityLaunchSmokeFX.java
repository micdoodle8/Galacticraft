package micdoodle8.mods.galacticraft.core.client.fx;

import net.minecraft.src.EntityFX;
import net.minecraft.src.Tessellator;
import net.minecraft.src.World;
import cpw.mods.fml.common.Side;
import cpw.mods.fml.common.asm.SideOnly;

/**
 * Copyright 2012, micdoodle8
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
        this.motionX += par8;
        this.motionY += par10;
        this.motionZ += par12;
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
            this.particleMaxAge = (int)10.0D;
            this.particleMaxAge = (int)(this.particleMaxAge * par14);
        }
        this.noClip = false;
    }

    @Override
	public void renderParticle(Tessellator par1Tessellator, float par2, float par3, float par4, float par5, float par6, float par7)
    {
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
        super.renderParticle(par1Tessellator, par2, par3, par4, par5, par6, par7);
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
