package micdoodle8.mods.galacticraft.mars.client.fx;

import micdoodle8.mods.galacticraft.mars.blocks.GCMarsBlocks;
import net.minecraft.block.BlockFluid;
import net.minecraft.block.material.Material;
import net.minecraft.client.particle.EntityFX;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

/**
 * GCMarsEntityDropParticleFX.java
 * 
 * This file is part of the Galacticraft project
 * 
 * @author micdoodle8
 * @license Lesser GNU Public License v3 (http://www.gnu.org/licenses/lgpl.html)
 * 
 */
@SideOnly(Side.CLIENT)
public class GCMarsEntityDropParticleFX extends EntityFX
{
	/** the material type for dropped items/blocks */
	private final Material materialType;

	/** The height of the current bob */
	private int bobTimer;

	public GCMarsEntityDropParticleFX(World par1World, double par2, double par4, double par6, Material par8Material)
	{
		super(par1World, par2, par4, par6, 0.0D, 0.0D, 0.0D);
		this.motionX = this.motionY = this.motionZ = 0.0D;

		if (par8Material == GCMarsBlocks.bacterialSludge)
		{
			this.particleRed = 0.0F;
			this.particleGreen = 0.0F;
			this.particleBlue = 0.0F;
		}
		else
		{
			this.particleRed = 1.0F;
			this.particleGreen = 0.0F;
			this.particleBlue = 0.0F;
		}

		this.setParticleTextureIndex(113);
		this.setSize(0.01F, 0.01F);
		this.particleGravity = 0.06F;
		this.materialType = par8Material;
		this.bobTimer = 40;
		this.particleMaxAge = (int) (64.0D / (Math.random() * 0.8D + 0.2D));
		this.motionX = this.motionY = this.motionZ = 0.0D;
	}

	@Override
	public int getBrightnessForRender(float par1)
	{
		return /*
				 * this.materialType == Material.water ?
				 * super.getBrightnessForRender(par1) :
				 */257;
	}

	/**
	 * Gets how bright this entity is.
	 */
	@Override
	public float getBrightness(float par1)
	{
		return /*
				 * this.materialType == Material.water ?
				 * super.getBrightness(par1) :
				 */1.0F;
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

		if (this.materialType == GCMarsBlocks.bacterialSludge)
		{
			this.particleRed = 0.1F;
			this.particleGreen = 0.1F;
			this.particleBlue = 0.1F;
		}
		else
		{
			this.particleRed = 1.0F;
			this.particleGreen = 16.0F / (40 - this.bobTimer + 16);
			this.particleBlue = 4.0F / (40 - this.bobTimer + 8);
		}

		this.motionY -= this.particleGravity;

		if (this.bobTimer-- > 0)
		{
			this.motionX *= 0.02D;
			this.motionY *= 0.02D;
			this.motionZ *= 0.02D;
			this.setParticleTextureIndex(113);
		}
		else
		{
			this.setParticleTextureIndex(112);
		}

		this.moveEntity(this.motionX, this.motionY, this.motionZ);
		this.motionX *= 0.9800000190734863D;
		this.motionY *= 0.9800000190734863D;
		this.motionZ *= 0.9800000190734863D;

		if (this.particleMaxAge-- <= 0)
		{
			this.setDead();
		}

		if (this.onGround)
		{
			if (this.materialType == Material.water)
			{
				this.setDead();
				this.worldObj.spawnParticle("splash", this.posX, this.posY, this.posZ, 0.0D, 0.0D, 0.0D);
			}
			else
			{
				this.setParticleTextureIndex(114);
			}

			this.motionX *= 0.699999988079071D;
			this.motionZ *= 0.699999988079071D;
		}

		final Material var1 = this.worldObj.getBlockMaterial(MathHelper.floor_double(this.posX), MathHelper.floor_double(this.posY), MathHelper.floor_double(this.posZ));

		if (var1.isLiquid() || var1.isSolid())
		{
			final double var2 = MathHelper.floor_double(this.posY) + 1 - BlockFluid.getFluidHeightPercent(this.worldObj.getBlockMetadata(MathHelper.floor_double(this.posX), MathHelper.floor_double(this.posY), MathHelper.floor_double(this.posZ)));

			if (this.posY < var2)
			{
				this.setDead();
			}
		}
	}
}
