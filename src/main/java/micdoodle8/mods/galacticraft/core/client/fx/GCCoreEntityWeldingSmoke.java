package micdoodle8.mods.galacticraft.core.client.fx;

import net.minecraft.client.particle.EntityFX;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.world.World;

/**
 * GCCoreEntityWeldingSmoke.java
 * 
 * This file is part of the Galacticraft project
 * 
 * @author micdoodle8
 * @license Lesser GNU Public License v3 (http://www.gnu.org/licenses/lgpl.html)
 * 
 */
public class GCCoreEntityWeldingSmoke extends EntityFX
{
	float smokeParticleScale;

	public GCCoreEntityWeldingSmoke(World par1World, double par2, double par4, double par6, double par8, double par10, double par12, float par14)
	{
		super(par1World, par2, par4, par6, 0.0D, 0.0D, 0.0D);
		this.motionX *= 0.10000000149011612D;
		this.motionY *= 0.10000000149011612D;
		this.motionZ *= 0.10000000149011612D;
		this.motionX += par8;
		this.motionY += par10;
		this.motionZ += par12;
		this.particleRed = 255F / 255F;
		this.particleGreen = 255F / 255F;
		this.particleBlue = 0F / 255F + this.rand.nextFloat() / 6;
		this.particleScale *= 0.15F;
		this.particleScale *= par14 * 3;
		this.smokeParticleScale = this.particleScale;
		this.particleMaxAge = (int) 50.0D;
		this.particleMaxAge = (int) (this.particleMaxAge * par14);
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
		this.moveEntity(this.motionX, this.motionY, this.motionZ);

		if (this.posY == this.prevPosY)
		{
			this.motionX *= 1.1D;
			this.motionZ *= 1.1D;
		}

		this.setParticleTextureIndex(160 + 7 - this.particleAge * 8 / this.particleMaxAge);
		this.motionY -= 0.01D;
		this.moveEntity(this.motionX, this.motionY, this.motionZ);
		this.motionX *= 0.9100000262260437D;
		this.motionY *= 0.9100000262260437D;
		this.motionZ *= 0.9100000262260437D;
	}
}
