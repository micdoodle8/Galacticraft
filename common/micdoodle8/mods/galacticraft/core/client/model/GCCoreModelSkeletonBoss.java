package micdoodle8.mods.galacticraft.core.client.model;

import micdoodle8.mods.galacticraft.core.entities.GCCoreEntitySkeletonBoss;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.util.MathHelper;

/**
 * GCCoreModelSkeletonBoss.java
 * 
 * This file is part of the Galacticraft project
 * 
 * @author micdoodle8
 * @license Lesser GNU Public License v3 (http://www.gnu.org/licenses/lgpl.html)
 * 
 */
public class GCCoreModelSkeletonBoss extends ModelBase
{
	ModelRenderer UpperHead;
	ModelRenderer Pelvis;
	ModelRenderer Sternum;
	ModelRenderer RightLeg;
	ModelRenderer RightArm;
	ModelRenderer Spine;
	ModelRenderer LeftArm;
	ModelRenderer LeftLeg;
	ModelRenderer LeftFrontBotRib;
	ModelRenderer LeftFrontTopRib;
	ModelRenderer LeftFront2ndRib;
	ModelRenderer LeftFront3rdRib;
	ModelRenderer LeftSideBotRib;
	ModelRenderer LeftSide3rdRib;
	ModelRenderer LeftSide2ndRib;
	ModelRenderer LeftSideTopRib;
	ModelRenderer RightSideTopRib;
	ModelRenderer RightSide2ndRib;
	ModelRenderer RightSide3rdRib;
	ModelRenderer RightSideBotRib;
	ModelRenderer RightFrontBotRib;
	ModelRenderer RightFront3rdRib;
	ModelRenderer RightFront2ndRib;
	ModelRenderer RightFrontTopRib;
	ModelRenderer LeftBackTopRib;
	ModelRenderer LeftBack2ndRib;
	ModelRenderer LeftBack3rdRib;
	ModelRenderer LeftBackBotRib;
	ModelRenderer RightBackBotRib;
	ModelRenderer RightBack3rdRib;
	ModelRenderer RightBack2ndRib;
	ModelRenderer RightBackTopRib;

	public GCCoreModelSkeletonBoss()
	{
		this.textureWidth = 128;
		this.textureHeight = 128;

		this.UpperHead = new ModelRenderer(this, 0, 16);
		this.UpperHead.addBox(-4F, -8F, -6F, 8, 8, 8);
		this.UpperHead.setRotationPoint(0F, -24F, 6F);
		this.UpperHead.setTextureSize(64, 32);
		this.UpperHead.mirror = true;
		this.setRotation(this.UpperHead, 0.122173F, 0F, 0F);
		this.Pelvis = new ModelRenderer(this, 32, 19);
		this.Pelvis.addBox(-6F, 0F, -3F, 12, 5, 5);
		this.Pelvis.setRotationPoint(0F, -2F, 5F);
		this.Pelvis.setTextureSize(64, 32);
		this.Pelvis.mirror = true;
		this.setRotation(this.Pelvis, 0F, 0F, 0F);
		this.Sternum = new ModelRenderer(this, 0, 0);
		this.Sternum.addBox(-1.5F, 0F, -1F, 3, 9, 1);
		this.Sternum.setRotationPoint(0F, -21F, 2F);
		this.Sternum.setTextureSize(64, 32);
		this.Sternum.mirror = true;
		this.setRotation(this.Sternum, 0F, 0F, 0F);
		this.RightLeg = new ModelRenderer(this, 56, 33);
		this.RightLeg.mirror = true;
		this.RightLeg.addBox(-2F, 0F, -2F, 3, 26, 3);
		this.RightLeg.setRotationPoint(-5F, 0F, 5F);
		this.RightLeg.setTextureSize(64, 32);
		this.RightLeg.mirror = true;
		this.setRotation(this.RightLeg, 0F, 0F, 0F);
		this.RightLeg.mirror = false;
		this.RightArm = new ModelRenderer(this, 56, 33);
		this.RightArm.addBox(-2F, -2F, -1.5F, 3, 24, 3);
		this.RightArm.setRotationPoint(-8F, -20F, 5F);
		this.RightArm.setTextureSize(64, 32);
		this.RightArm.mirror = true;
		this.setRotation(this.RightArm, 0F, 0F, 0F);
		this.Spine = new ModelRenderer(this, 32, 33);
		this.Spine.addBox(-1.5F, 0F, -1F, 3, 22, 2);
		this.Spine.setRotationPoint(0F, -24F, 6F);
		this.Spine.setTextureSize(64, 32);
		this.Spine.mirror = true;
		this.setRotation(this.Spine, 0F, 0F, 0F);
		this.LeftArm = new ModelRenderer(this, 56, 33);
		this.LeftArm.addBox(-1F, -2F, -1.5F, 3, 24, 3);
		this.LeftArm.setRotationPoint(8F, -20F, 5F);
		this.LeftArm.setTextureSize(64, 32);
		this.LeftArm.mirror = true;
		this.setRotation(this.LeftArm, 0F, 0F, 0F);
		this.LeftLeg = new ModelRenderer(this, 56, 33);
		this.LeftLeg.addBox(-2F, 0F, -2F, 3, 26, 3);
		this.LeftLeg.setRotationPoint(6F, 0F, 5F);
		this.LeftLeg.setTextureSize(64, 32);
		this.LeftLeg.mirror = true;
		this.setRotation(this.LeftLeg, 0F, 0F, 0F);
		this.LeftFrontBotRib = new ModelRenderer(this, 0, 0);
		this.LeftFrontBotRib.addBox(-1F, 0F, 0F, 1, 2, 6);
		this.LeftFrontBotRib.setRotationPoint(7F, -13F, 2F);
		this.LeftFrontBotRib.setTextureSize(64, 32);
		this.LeftFrontBotRib.mirror = true;
		this.setRotation(this.LeftFrontBotRib, 0F, -1.570796F, 0F);
		this.LeftFrontTopRib = new ModelRenderer(this, 0, 0);
		this.LeftFrontTopRib.addBox(-1F, 0F, 0F, 1, 2, 6);
		this.LeftFrontTopRib.setRotationPoint(7F, -22F, 2F);
		this.LeftFrontTopRib.setTextureSize(64, 32);
		this.LeftFrontTopRib.mirror = true;
		this.setRotation(this.LeftFrontTopRib, 0F, -1.570796F, 0F);
		this.LeftFront2ndRib = new ModelRenderer(this, 0, 0);
		this.LeftFront2ndRib.addBox(-1F, 0F, 0F, 1, 2, 6);
		this.LeftFront2ndRib.setRotationPoint(7F, -19F, 2F);
		this.LeftFront2ndRib.setTextureSize(64, 32);
		this.LeftFront2ndRib.mirror = true;
		this.setRotation(this.LeftFront2ndRib, 0F, -1.570796F, 0F);
		this.LeftFront3rdRib = new ModelRenderer(this, 0, 0);
		this.LeftFront3rdRib.addBox(-1F, 0F, 0F, 1, 2, 6);
		this.LeftFront3rdRib.setRotationPoint(7F, -16F, 2F);
		this.LeftFront3rdRib.setTextureSize(64, 32);
		this.LeftFront3rdRib.mirror = true;
		this.setRotation(this.LeftFront3rdRib, 0F, -1.570796F, 0F);
		this.LeftSideBotRib = new ModelRenderer(this, 0, 0);
		this.LeftSideBotRib.addBox(-1F, 0F, -6F, 1, 2, 6);
		this.LeftSideBotRib.setRotationPoint(7F, -13F, 7F);
		this.LeftSideBotRib.setTextureSize(64, 32);
		this.LeftSideBotRib.mirror = true;
		this.setRotation(this.LeftSideBotRib, 0F, 0F, 0F);
		this.LeftSide3rdRib = new ModelRenderer(this, 0, 0);
		this.LeftSide3rdRib.addBox(-1F, 0F, -6F, 1, 2, 6);
		this.LeftSide3rdRib.setRotationPoint(7F, -16F, 7F);
		this.LeftSide3rdRib.setTextureSize(64, 32);
		this.LeftSide3rdRib.mirror = true;
		this.setRotation(this.LeftSide3rdRib, 0F, 0F, 0F);
		this.LeftSide2ndRib = new ModelRenderer(this, 0, 0);
		this.LeftSide2ndRib.addBox(-1F, 0F, -6F, 1, 2, 6);
		this.LeftSide2ndRib.setRotationPoint(7F, -19F, 7F);
		this.LeftSide2ndRib.setTextureSize(64, 32);
		this.LeftSide2ndRib.mirror = true;
		this.setRotation(this.LeftSide2ndRib, 0F, 0F, 0F);
		this.LeftSideTopRib = new ModelRenderer(this, 0, 0);
		this.LeftSideTopRib.addBox(-1F, 0F, -6F, 1, 2, 6);
		this.LeftSideTopRib.setRotationPoint(7F, -22F, 7F);
		this.LeftSideTopRib.setTextureSize(64, 32);
		this.LeftSideTopRib.mirror = true;
		this.setRotation(this.LeftSideTopRib, 0F, 0F, 0F);
		this.RightSideTopRib = new ModelRenderer(this, 0, 0);
		this.RightSideTopRib.addBox(0F, 0F, -6F, 1, 2, 6);
		this.RightSideTopRib.setRotationPoint(-7F, -22F, 7F);
		this.RightSideTopRib.setTextureSize(64, 32);
		this.RightSideTopRib.mirror = true;
		this.setRotation(this.RightSideTopRib, 0F, 0F, 0F);
		this.RightSide2ndRib = new ModelRenderer(this, 0, 0);
		this.RightSide2ndRib.addBox(0F, 0F, -6F, 1, 2, 6);
		this.RightSide2ndRib.setRotationPoint(-7F, -19F, 7F);
		this.RightSide2ndRib.setTextureSize(64, 32);
		this.RightSide2ndRib.mirror = true;
		this.setRotation(this.RightSide2ndRib, 0F, 0F, 0F);
		this.RightSide3rdRib = new ModelRenderer(this, 0, 0);
		this.RightSide3rdRib.addBox(0F, 0F, -6F, 1, 2, 6);
		this.RightSide3rdRib.setRotationPoint(-7F, -16F, 7F);
		this.RightSide3rdRib.setTextureSize(64, 32);
		this.RightSide3rdRib.mirror = true;
		this.setRotation(this.RightSide3rdRib, 0F, 0F, 0F);
		this.RightSideBotRib = new ModelRenderer(this, 0, 0);
		this.RightSideBotRib.addBox(0F, 0F, -6F, 1, 2, 6);
		this.RightSideBotRib.setRotationPoint(-7F, -13F, 7F);
		this.RightSideBotRib.setTextureSize(64, 32);
		this.RightSideBotRib.mirror = true;
		this.setRotation(this.RightSideBotRib, 0F, 0F, 0F);
		this.RightFrontBotRib = new ModelRenderer(this, 0, 0);
		this.RightFrontBotRib.addBox(0F, 0F, 0F, 1, 2, 6);
		this.RightFrontBotRib.setRotationPoint(-7F, -13F, 2F);
		this.RightFrontBotRib.setTextureSize(64, 32);
		this.RightFrontBotRib.mirror = true;
		this.setRotation(this.RightFrontBotRib, 0F, 1.570796F, 0F);
		this.RightFront3rdRib = new ModelRenderer(this, 0, 0);
		this.RightFront3rdRib.addBox(0F, 0F, 0F, 1, 2, 6);
		this.RightFront3rdRib.setRotationPoint(-7F, -16F, 2F);
		this.RightFront3rdRib.setTextureSize(64, 32);
		this.RightFront3rdRib.mirror = true;
		this.setRotation(this.RightFront3rdRib, 0F, 1.570796F, 0F);
		this.RightFront2ndRib = new ModelRenderer(this, 0, 0);
		this.RightFront2ndRib.addBox(0F, 0F, 0F, 1, 2, 6);
		this.RightFront2ndRib.setRotationPoint(-7F, -19F, 2F);
		this.RightFront2ndRib.setTextureSize(64, 32);
		this.RightFront2ndRib.mirror = true;
		this.setRotation(this.RightFront2ndRib, 0F, 1.570796F, 0F);
		this.RightFrontTopRib = new ModelRenderer(this, 0, 0);
		this.RightFrontTopRib.addBox(0F, 0F, 0F, 1, 2, 6);
		this.RightFrontTopRib.setRotationPoint(-7F, -22F, 2F);
		this.RightFrontTopRib.setTextureSize(64, 32);
		this.RightFrontTopRib.mirror = true;
		this.setRotation(this.RightFrontTopRib, 0F, 1.570796F, 0F);
		this.LeftBackTopRib = new ModelRenderer(this, 0, 0);
		this.LeftBackTopRib.addBox(-1F, 0F, 0F, 1, 2, 6);
		this.LeftBackTopRib.setRotationPoint(7F, -22F, 7F);
		this.LeftBackTopRib.setTextureSize(64, 32);
		this.LeftBackTopRib.mirror = true;
		this.setRotation(this.LeftBackTopRib, 0F, -1.570796F, 0F);
		this.LeftBack2ndRib = new ModelRenderer(this, 0, 0);
		this.LeftBack2ndRib.addBox(-1F, 0F, 0F, 1, 2, 6);
		this.LeftBack2ndRib.setRotationPoint(7F, -19F, 7F);
		this.LeftBack2ndRib.setTextureSize(64, 32);
		this.LeftBack2ndRib.mirror = true;
		this.setRotation(this.LeftBack2ndRib, 0F, -1.570796F, 0F);
		this.LeftBack3rdRib = new ModelRenderer(this, 0, 0);
		this.LeftBack3rdRib.addBox(-1F, 0F, 0F, 1, 2, 6);
		this.LeftBack3rdRib.setRotationPoint(7F, -16F, 7F);
		this.LeftBack3rdRib.setTextureSize(64, 32);
		this.LeftBack3rdRib.mirror = true;
		this.setRotation(this.LeftBack3rdRib, 0F, -1.570796F, 0F);
		this.LeftBackBotRib = new ModelRenderer(this, 0, 0);
		this.LeftBackBotRib.addBox(-1F, 0F, 0F, 1, 2, 6);
		this.LeftBackBotRib.setRotationPoint(7F, -13F, 7F);
		this.LeftBackBotRib.setTextureSize(64, 32);
		this.LeftBackBotRib.mirror = true;
		this.setRotation(this.LeftBackBotRib, 0F, -1.570796F, 0F);
		this.RightBackBotRib = new ModelRenderer(this, 0, 0);
		this.RightBackBotRib.addBox(0F, 0F, 0F, 1, 2, 6);
		this.RightBackBotRib.setRotationPoint(-7F, -13F, 7F);
		this.RightBackBotRib.setTextureSize(64, 32);
		this.RightBackBotRib.mirror = true;
		this.setRotation(this.RightBackBotRib, 0F, 1.570796F, 0F);
		this.RightBack3rdRib = new ModelRenderer(this, 0, 0);
		this.RightBack3rdRib.addBox(0F, 0F, 0F, 1, 2, 6);
		this.RightBack3rdRib.setRotationPoint(-7F, -16F, 7F);
		this.RightBack3rdRib.setTextureSize(64, 32);
		this.RightBack3rdRib.mirror = true;
		this.setRotation(this.RightBack3rdRib, 0F, 1.570796F, 0F);
		this.RightBack2ndRib = new ModelRenderer(this, 0, 0);
		this.RightBack2ndRib.addBox(0F, 0F, 0F, 1, 2, 6);
		this.RightBack2ndRib.setRotationPoint(-7F, -19F, 7F);
		this.RightBack2ndRib.setTextureSize(64, 32);
		this.RightBack2ndRib.mirror = true;
		this.setRotation(this.RightBack2ndRib, 0F, 1.570796F, 0F);
		this.RightBackTopRib = new ModelRenderer(this, 0, 0);
		this.RightBackTopRib.addBox(0F, 0F, 0F, 1, 2, 6);
		this.RightBackTopRib.setRotationPoint(-7F, -22F, 7F);
		this.RightBackTopRib.setTextureSize(64, 32);
		this.RightBackTopRib.mirror = true;
		this.setRotation(this.RightBackTopRib, 0F, 1.570796F, 0F);
	}

	@Override
	public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5)
	{
		super.render(entity, f, f1, f2, f3, f4, f5);
		this.setRotationAngles(f, f1, f2, f3, f4, f5, entity);
		this.UpperHead.render(f5);
		this.Pelvis.render(f5);
		this.Sternum.render(f5);
		this.RightLeg.render(f5);
		this.RightArm.render(f5);
		this.Spine.render(f5);
		this.LeftArm.render(f5);
		this.LeftLeg.render(f5);
		this.LeftFrontBotRib.render(f5);
		this.LeftFrontTopRib.render(f5);
		this.LeftFront2ndRib.render(f5);
		this.LeftFront3rdRib.render(f5);
		this.LeftSideBotRib.render(f5);
		this.LeftSide3rdRib.render(f5);
		this.LeftSide2ndRib.render(f5);
		this.LeftSideTopRib.render(f5);
		this.RightSideTopRib.render(f5);
		this.RightSide2ndRib.render(f5);
		this.RightSide3rdRib.render(f5);
		this.RightSideBotRib.render(f5);
		this.RightFrontBotRib.render(f5);
		this.RightFront3rdRib.render(f5);
		this.RightFront2ndRib.render(f5);
		this.RightFrontTopRib.render(f5);
		this.LeftBackTopRib.render(f5);
		this.LeftBack2ndRib.render(f5);
		this.LeftBack3rdRib.render(f5);
		this.LeftBackBotRib.render(f5);
		this.RightBackBotRib.render(f5);
		this.RightBack3rdRib.render(f5);
		this.RightBack2ndRib.render(f5);
		this.RightBackTopRib.render(f5);
	}

	private void setRotation(ModelRenderer model, float x, float y, float z)
	{
		model.rotateAngleX = x;
		model.rotateAngleY = y;
		model.rotateAngleZ = z;
	}

	@Override
	public void setRotationAngles(float par1, float par2, float par3, float par4, float par5, float par6, Entity e)
	{
		final GCCoreEntitySkeletonBoss boss = (GCCoreEntitySkeletonBoss) e;
		super.setRotationAngles(par1, par2, par3, par4, par5, par6, e);
		this.UpperHead.rotateAngleY = 0;
		this.UpperHead.rotateAngleX = 0;
		this.RightArm.rotateAngleX = MathHelper.cos(par1 * 0.6662F + (float) Math.PI) * 2.0F * par2 * 0.5F;
		this.LeftArm.rotateAngleX = MathHelper.cos(par1 * 0.6662F) * 2.0F * par2 * 0.5F;
		this.RightArm.rotateAngleZ = 0.0F;
		this.LeftArm.rotateAngleZ = 0.0F;
		this.RightLeg.rotateAngleX = MathHelper.cos(par1 * 0.6662F) * 1.4F * par2;
		this.LeftLeg.rotateAngleX = MathHelper.cos(par1 * 0.6662F + (float) Math.PI) * 1.4F * par2;
		this.RightLeg.rotateAngleY = 0.0F;
		this.LeftLeg.rotateAngleY = 0.0F;

		if (this.isRiding)
		{
			this.RightArm.rotateAngleX += -((float) Math.PI / 5F);
			this.LeftArm.rotateAngleX += -((float) Math.PI / 5F);
			this.RightLeg.rotateAngleX = -((float) Math.PI * 2F / 5F);
			this.LeftLeg.rotateAngleX = -((float) Math.PI * 2F / 5F);
			this.RightLeg.rotateAngleY = (float) Math.PI / 10F;
			this.LeftLeg.rotateAngleY = -((float) Math.PI / 10F);
		}

		this.RightArm.rotateAngleY = 0.0F;
		this.LeftArm.rotateAngleY = 0.0F;
		float var7;
		float var8;

		if (this.onGround > -9990.0F)
		{
			var7 = this.onGround;
			this.Spine.rotateAngleY = MathHelper.sin(MathHelper.sqrt_float(var7) * (float) Math.PI * 2.0F) * 0.2F;

			this.RightArm.rotationPointZ = MathHelper.sin(this.Spine.rotateAngleY) * 5.0F;
			this.RightArm.rotationPointX = -MathHelper.cos(this.Spine.rotateAngleY) * 5.0F;
			this.LeftArm.rotationPointZ = -MathHelper.sin(this.Spine.rotateAngleY) * 5.0F;
			this.LeftArm.rotationPointX = MathHelper.cos(this.Spine.rotateAngleY) * 5.0F;
			this.RightArm.rotateAngleY += this.Spine.rotateAngleY;
			this.LeftArm.rotateAngleY += this.Spine.rotateAngleY;
			this.LeftArm.rotateAngleX += this.Spine.rotateAngleY;
			var7 = 1.0F - this.onGround;
			var7 *= var7;
			var7 *= var7;
			var7 = 1.0F - var7;
			var8 = MathHelper.sin(var7 * (float) Math.PI);
			final float var9 = MathHelper.sin(this.onGround * (float) Math.PI) * -(this.UpperHead.rotateAngleX - 0.7F) * 0.75F;
			this.RightArm.rotateAngleX = (float) (this.RightArm.rotateAngleX - (var8 * 1.2D + var9));
			this.RightArm.rotateAngleY += this.Spine.rotateAngleY * 2.0F;
			this.RightArm.rotateAngleZ = MathHelper.sin(this.onGround * (float) Math.PI) * -0.4F;
		}

		this.RightArm.rotateAngleZ += MathHelper.cos(par3 * 0.09F) * 0.05F + 0.05F;
		this.LeftArm.rotateAngleZ -= MathHelper.cos(par3 * 0.09F) * 0.05F + 0.05F;
		this.RightArm.rotateAngleX += MathHelper.sin(par3 * 0.067F) * 0.05F;
		this.LeftArm.rotateAngleX -= MathHelper.sin(par3 * 0.067F) * 0.05F;

		MathHelper.sin(this.onGround * (float) Math.PI);
		MathHelper.sin((1.0F - (1.0F - this.onGround) * (1.0F - this.onGround)) * (float) Math.PI);

		var7 = 0.0F;
		var8 = 0.0F;
		this.RightArm.rotateAngleZ = 0.0F;
		this.LeftArm.rotateAngleZ = 0.0F;
		this.RightArm.rotateAngleY = -(0.1F - var7 * 0.6F) + this.UpperHead.rotateAngleY;
		this.LeftArm.rotateAngleY = 0.1F - var7 * 0.6F + this.UpperHead.rotateAngleY + 0.4F;
		this.RightArm.rotateAngleX = -((float) Math.PI / 2F) + this.UpperHead.rotateAngleX;
		this.LeftArm.rotateAngleX = -((float) Math.PI / 2F) + this.UpperHead.rotateAngleX;
		this.RightArm.rotateAngleX -= var7 * 1.2F - var8 * 0.4F;
		this.LeftArm.rotateAngleX -= var7 * 1.2F - var8 * 0.4F;
		this.RightArm.rotateAngleZ += MathHelper.cos(par3 * 0.09F) * 0.05F + 0.05F;
		this.LeftArm.rotateAngleZ -= MathHelper.cos(par3 * 0.09F) * 0.05F + 0.05F;
		this.RightArm.rotateAngleX += MathHelper.sin(par3 * 0.067F) * 0.05F;
		this.LeftArm.rotateAngleX -= MathHelper.sin(par3 * 0.067F) * 0.05F;

		final float f6 = MathHelper.sin(this.onGround * (float) Math.PI);
		final float f7 = MathHelper.sin((1.0F - (1.0F - this.onGround) * (1.0F - this.onGround)) * (float) Math.PI);
		this.RightArm.rotateAngleZ = 0.0F;
		this.LeftArm.rotateAngleZ = 0.0F;
		this.RightArm.rotateAngleY = -(0.1F - f6 * 0.6F);
		this.LeftArm.rotateAngleY = 0.1F - f6 * 0.6F;
		this.RightArm.rotateAngleX = -((float) Math.PI / 2F);
		this.LeftArm.rotateAngleX = -((float) Math.PI / 2F);
		this.RightArm.rotateAngleX -= f6 * 1.2F - f7 * 0.4F;
		this.LeftArm.rotateAngleX -= f6 * 1.2F - f7 * 0.4F;
		this.RightArm.rotateAngleZ += MathHelper.cos(par3 * 0.09F) * 0.05F + 0.05F;
		this.LeftArm.rotateAngleZ -= MathHelper.cos(par3 * 0.09F) * 0.05F + 0.05F;
		this.RightArm.rotateAngleX += MathHelper.sin(par3 * 0.067F) * 0.05F;
		this.LeftArm.rotateAngleX -= MathHelper.sin(par3 * 0.067F) * 0.05F;

		if (boss.throwTimer + boss.postThrowDelay > 0)
		{
			this.RightArm.rotateAngleX -= MathHelper.cos((boss.throwTimer + boss.postThrowDelay) * 0.05F) * 1.2F + 0.05F;
			this.LeftArm.rotateAngleX -= MathHelper.cos((boss.throwTimer + boss.postThrowDelay) * 0.05F) * 1.2F + 0.05F;
		}
	}
}
