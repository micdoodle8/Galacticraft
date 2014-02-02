package micdoodle8.mods.galacticraft.core.client.fx;

import java.util.List;

import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.network.GCCorePacketHandlerServer.EnumPacketServer;
import micdoodle8.mods.galacticraft.core.util.PacketUtil;
import net.minecraft.client.particle.EntityFX;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.world.World;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.common.network.PacketDispatcher;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

/**
 * GCCoreEntityLanderFlameFX.java
 * 
 * This file is part of the Galacticraft project
 * 
 * @author micdoodle8
 * @license Lesser GNU Public License v3 (http://www.gnu.org/licenses/lgpl.html)
 * 
 */
@SideOnly(Side.CLIENT)
public class GCCoreEntityLanderFlameFX extends EntityFX
{
	float smokeParticleScale;

	public GCCoreEntityLanderFlameFX(World world, double x, double y, double z, double mX, double mY, double mZ)
	{
		this(world, x, y, z, mX, mY, mZ, 1.0F);
	}

	public GCCoreEntityLanderFlameFX(World par1World, double par2, double par4, double par6, double par8, double par10, double par12, float scale)
	{
		super(par1World, par2, par4, par6, par8, par10, par12);
		this.motionX *= 0.10000000149011612D;
		this.motionZ *= 0.10000000149011612D;
		this.motionX += par8;
		this.motionY = par10;
		this.motionZ += par12;
		this.particleRed = 200F / 255F;
		this.particleGreen = 200F / 255F;
		this.particleBlue = 200F / 255F + this.rand.nextFloat() / 3;
		this.particleScale *= 8F * scale;
		this.smokeParticleScale = this.particleScale;
		this.particleMaxAge = (int) 5.0D;
		this.noClip = false;
	}

	@Override
	public void renderParticle(Tessellator par1Tessellator, float par2, float par3, float par4, float par5, float par6, float par7)
	{
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
		super.renderParticle(par1Tessellator, par2, par3, par4, par5, par6, par7);
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL11.glDepthMask(true);
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

		this.particleGreen -= 0.09F;
		this.particleRed -= 0.09F;

		if (this.posY == this.prevPosY)
		{
			this.motionX *= 1.1D;
			this.motionZ *= 1.1D;
		}

		this.particleScale *= 0.9599999785423279D;

		this.motionX *= 0.9599999785423279D;
		this.motionY *= 0.9599999785423279D;
		this.motionZ *= 0.9599999785423279D;

		final List<?> var3 = this.worldObj.getEntitiesWithinAABBExcludingEntity(this, this.boundingBox.expand(1.0D, 0.5D, 1.0D));

		if (var3 != null)
		{
			for (int var4 = 0; var4 < var3.size(); ++var4)
			{
				final Entity var5 = (Entity) var3.get(var4);

				if (var5 instanceof EntityLiving)
				{
					if (!var5.isDead && !var5.isBurning() && !var5.equals(FMLClientHandler.instance().getClient().thePlayer))
					{
						var5.setFire(3);
						final Object[] toSend = { var5.entityId };
						PacketDispatcher.sendPacketToServer(PacketUtil.createPacket(GalacticraftCore.CHANNEL, EnumPacketServer.SET_ENTITY_FIRE, toSend));
					}
				}
			}
		}
	}

	@Override
	public int getBrightnessForRender(float par1)
	{
		return 15728880;
	}

	@Override
	public float getBrightness(float par1)
	{
		return 1.0F;
	}
}
