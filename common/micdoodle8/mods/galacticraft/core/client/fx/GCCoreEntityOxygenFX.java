package micdoodle8.mods.galacticraft.core.client.fx;

import micdoodle8.mods.galacticraft.api.vector.Vector3;
import net.minecraft.client.particle.EntityFX;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.world.World;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

/**
 * GCCoreEntityOxygenFX.java
 * 
 * This file is part of the Galacticraft project
 * 
 * @author micdoodle8
 * @license Lesser GNU Public License v3 (http://www.gnu.org/licenses/lgpl.html)
 * 
 */
@SideOnly(Side.CLIENT)
public class GCCoreEntityOxygenFX extends EntityFX
{
	private final float portalParticleScale;
	private final double portalPosX;
	private final double portalPosY;
	private final double portalPosZ;

	public GCCoreEntityOxygenFX(World par1World, Vector3 position, Vector3 motion, Vector3 color)
	{
		super(par1World, position.x, position.y, position.z, motion.x, motion.y, motion.z);
		this.motionX = motion.x;
		this.motionY = motion.y;
		this.motionZ = motion.z;
		this.portalPosX = this.posX = position.x;
		this.portalPosY = this.posY = position.y;
		this.portalPosZ = this.posZ = position.z;
		this.portalParticleScale = this.particleScale = 0.1F;
		this.particleRed = color.floatX();
		this.particleGreen = color.floatY();
		this.particleBlue = color.floatZ();
		this.particleMaxAge = (int) (Math.random() * 10.0D) + 40;
		this.noClip = true;
		this.setParticleTextureIndex((int) (Math.random() * 8.0D));
	}

	@Override
	public void renderParticle(Tessellator par1Tessellator, float par2, float par3, float par4, float par5, float par6, float par7)
	{
		float var8 = (this.particleAge + par2) / this.particleMaxAge;
		var8 = 1.0F - var8;
		var8 *= var8;
		var8 = 1.0F - var8;
		this.particleScale = this.portalParticleScale * var8;
		super.renderParticle(par1Tessellator, par2, par3, par4, par5, par6, par7);
	}

	@Override
	public int getBrightnessForRender(float par1)
	{
		final int var2 = super.getBrightnessForRender(par1);
		float var3 = (float) this.particleAge / (float) this.particleMaxAge;
		var3 *= var3;
		var3 *= var3;
		final int var4 = var2 & 255;
		int var5 = var2 >> 16 & 255;
		var5 += (int) (var3 * 15.0F * 16.0F);

		if (var5 > 240)
		{
			var5 = 240;
		}

		return var4 | var5 << 16;
	}

	/**
	 * Gets how bright this entity is.
	 */
	@Override
	public float getBrightness(float par1)
	{
		final float var2 = super.getBrightness(par1);
		float var3 = (float) this.particleAge / (float) this.particleMaxAge;
		var3 = var3 * var3 * var3 * var3;
		return var2 * (1.0F - var3) + var3;
	}

	/**
	 * Called to update the entity's position/logic.
	 */
	@Override
	public void onUpdate()
	{
		this.prevPosX = this.posX;
		this.prevPosY = this.posY;
		this.prevPosZ = this.posZ;
		float var1 = (float) this.particleAge / (float) this.particleMaxAge;
		final float var2 = var1;
		var1 = -var1 + var1 * var1 * 2.0F;
		var1 = 1.0F - var1;
		this.posX = this.portalPosX + this.motionX * var1;
		this.posY = this.portalPosY + this.motionY * var1 + (1.0F - var2);
		this.posZ = this.portalPosZ + this.motionZ * var1;

		if (this.particleAge++ >= this.particleMaxAge)
		{
			this.setDead();
		}
	}
}
