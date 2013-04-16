package micdoodle8.mods.galacticraft.core.client.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.util.MathHelper;

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
		textureWidth = 128;
		textureHeight = 128;
		
		UpperHead = new ModelRenderer(this, 0, 16);
		UpperHead.addBox(-4F, -8F, -6F, 8, 8, 8);
		UpperHead.setRotationPoint(0F, -24F, 6F);
		UpperHead.setTextureSize(64, 32);
		UpperHead.mirror = true;
		setRotation(UpperHead, 0.122173F, 0F, 0F);
		Pelvis = new ModelRenderer(this, 32, 19);
		Pelvis.addBox(-6F, 0F, -3F, 12, 5, 5);
		Pelvis.setRotationPoint(0F, -2F, 5F);
		Pelvis.setTextureSize(64, 32);
		Pelvis.mirror = true;
		setRotation(Pelvis, 0F, 0F, 0F);
		Sternum = new ModelRenderer(this, 0, 0);
		Sternum.addBox(-1.5F, 0F, -1F, 3, 9, 1);
		Sternum.setRotationPoint(0F, -21F, 2F);
		Sternum.setTextureSize(64, 32);
		Sternum.mirror = true;
		setRotation(Sternum, 0F, 0F, 0F);
		RightLeg = new ModelRenderer(this, 56, 33);
		RightLeg.mirror = true;
		RightLeg.addBox(-2F, 0F, -2F, 3, 26, 3);
		RightLeg.setRotationPoint(-5F, 0F, 5F);
		RightLeg.setTextureSize(64, 32);
		RightLeg.mirror = true;
		setRotation(RightLeg, 0F, 0F, 0F);
		RightLeg.mirror = false;
		RightArm = new ModelRenderer(this, 56, 33);
		RightArm.addBox(-2F, -2F, -1.5F, 3, 24, 3);
		RightArm.setRotationPoint(-8F, -20F, 5F);
		RightArm.setTextureSize(64, 32);
		RightArm.mirror = true;
		setRotation(RightArm, 0F, 0F, 0F);
		Spine = new ModelRenderer(this, 32, 33);
		Spine.addBox(-1.5F, 0F, -1F, 3, 22, 2);
		Spine.setRotationPoint(0F, -24F, 6F);
		Spine.setTextureSize(64, 32);
		Spine.mirror = true;
		setRotation(Spine, 0F, 0F, 0F);
		LeftArm = new ModelRenderer(this, 56, 33);
		LeftArm.addBox(-1F, -2F, -1.5F, 3, 24, 3);
		LeftArm.setRotationPoint(8F, -20F, 5F);
		LeftArm.setTextureSize(64, 32);
		LeftArm.mirror = true;
		setRotation(LeftArm, 0F, 0F, 0F);
		LeftLeg = new ModelRenderer(this, 56, 33);
		LeftLeg.addBox(-2F, 0F, -2F, 3, 26, 3);
		LeftLeg.setRotationPoint(6F, 0F, 5F);
		LeftLeg.setTextureSize(64, 32);
		LeftLeg.mirror = true;
		setRotation(LeftLeg, 0F, 0F, 0F);
		LeftFrontBotRib = new ModelRenderer(this, 0, 0);
		LeftFrontBotRib.addBox(-1F, 0F, 0F, 1, 2, 6);
		LeftFrontBotRib.setRotationPoint(7F, -13F, 2F);
		LeftFrontBotRib.setTextureSize(64, 32);
		LeftFrontBotRib.mirror = true;
		setRotation(LeftFrontBotRib, 0F, -1.570796F, 0F);
		LeftFrontTopRib = new ModelRenderer(this, 0, 0);
		LeftFrontTopRib.addBox(-1F, 0F, 0F, 1, 2, 6);
		LeftFrontTopRib.setRotationPoint(7F, -22F, 2F);
		LeftFrontTopRib.setTextureSize(64, 32);
		LeftFrontTopRib.mirror = true;
		setRotation(LeftFrontTopRib, 0F, -1.570796F, 0F);
		LeftFront2ndRib = new ModelRenderer(this, 0, 0);
		LeftFront2ndRib.addBox(-1F, 0F, 0F, 1, 2, 6);
		LeftFront2ndRib.setRotationPoint(7F, -19F, 2F);
		LeftFront2ndRib.setTextureSize(64, 32);
		LeftFront2ndRib.mirror = true;
		setRotation(LeftFront2ndRib, 0F, -1.570796F, 0F);
		LeftFront3rdRib = new ModelRenderer(this, 0, 0);
		LeftFront3rdRib.addBox(-1F, 0F, 0F, 1, 2, 6);
		LeftFront3rdRib.setRotationPoint(7F, -16F, 2F);
		LeftFront3rdRib.setTextureSize(64, 32);
		LeftFront3rdRib.mirror = true;
		setRotation(LeftFront3rdRib, 0F, -1.570796F, 0F);
		LeftSideBotRib = new ModelRenderer(this, 0, 0);
		LeftSideBotRib.addBox(-1F, 0F, -6F, 1, 2, 6);
		LeftSideBotRib.setRotationPoint(7F, -13F, 7F);
		LeftSideBotRib.setTextureSize(64, 32);
		LeftSideBotRib.mirror = true;
		setRotation(LeftSideBotRib, 0F, 0F, 0F);
		LeftSide3rdRib = new ModelRenderer(this, 0, 0);
		LeftSide3rdRib.addBox(-1F, 0F, -6F, 1, 2, 6);
		LeftSide3rdRib.setRotationPoint(7F, -16F, 7F);
		LeftSide3rdRib.setTextureSize(64, 32);
		LeftSide3rdRib.mirror = true;
		setRotation(LeftSide3rdRib, 0F, 0F, 0F);
		LeftSide2ndRib = new ModelRenderer(this, 0, 0);
		LeftSide2ndRib.addBox(-1F, 0F, -6F, 1, 2, 6);
		LeftSide2ndRib.setRotationPoint(7F, -19F, 7F);
		LeftSide2ndRib.setTextureSize(64, 32);
		LeftSide2ndRib.mirror = true;
		setRotation(LeftSide2ndRib, 0F, 0F, 0F);
		LeftSideTopRib = new ModelRenderer(this, 0, 0);
		LeftSideTopRib.addBox(-1F, 0F, -6F, 1, 2, 6);
		LeftSideTopRib.setRotationPoint(7F, -22F, 7F);
		LeftSideTopRib.setTextureSize(64, 32);
		LeftSideTopRib.mirror = true;
		setRotation(LeftSideTopRib, 0F, 0F, 0F);
		RightSideTopRib = new ModelRenderer(this, 0, 0);
		RightSideTopRib.addBox(0F, 0F, -6F, 1, 2, 6);
		RightSideTopRib.setRotationPoint(-7F, -22F, 7F);
		RightSideTopRib.setTextureSize(64, 32);
		RightSideTopRib.mirror = true;
		setRotation(RightSideTopRib, 0F, 0F, 0F);
		RightSide2ndRib = new ModelRenderer(this, 0, 0);
		RightSide2ndRib.addBox(0F, 0F, -6F, 1, 2, 6);
		RightSide2ndRib.setRotationPoint(-7F, -19F, 7F);
		RightSide2ndRib.setTextureSize(64, 32);
		RightSide2ndRib.mirror = true;
		setRotation(RightSide2ndRib, 0F, 0F, 0F);
		RightSide3rdRib = new ModelRenderer(this, 0, 0);
		RightSide3rdRib.addBox(0F, 0F, -6F, 1, 2, 6);
		RightSide3rdRib.setRotationPoint(-7F, -16F, 7F);
		RightSide3rdRib.setTextureSize(64, 32);
		RightSide3rdRib.mirror = true;
		setRotation(RightSide3rdRib, 0F, 0F, 0F);
		RightSideBotRib = new ModelRenderer(this, 0, 0);
		RightSideBotRib.addBox(0F, 0F, -6F, 1, 2, 6);
		RightSideBotRib.setRotationPoint(-7F, -13F, 7F);
		RightSideBotRib.setTextureSize(64, 32);
		RightSideBotRib.mirror = true;
		setRotation(RightSideBotRib, 0F, 0F, 0F);
		RightFrontBotRib = new ModelRenderer(this, 0, 0);
		RightFrontBotRib.addBox(0F, 0F, 0F, 1, 2, 6);
		RightFrontBotRib.setRotationPoint(-7F, -13F, 2F);
		RightFrontBotRib.setTextureSize(64, 32);
		RightFrontBotRib.mirror = true;
		setRotation(RightFrontBotRib, 0F, 1.570796F, 0F);
		RightFront3rdRib = new ModelRenderer(this, 0, 0);
		RightFront3rdRib.addBox(0F, 0F, 0F, 1, 2, 6);
		RightFront3rdRib.setRotationPoint(-7F, -16F, 2F);
		RightFront3rdRib.setTextureSize(64, 32);
		RightFront3rdRib.mirror = true;
		setRotation(RightFront3rdRib, 0F, 1.570796F, 0F);
		RightFront2ndRib = new ModelRenderer(this, 0, 0);
		RightFront2ndRib.addBox(0F, 0F, 0F, 1, 2, 6);
		RightFront2ndRib.setRotationPoint(-7F, -19F, 2F);
		RightFront2ndRib.setTextureSize(64, 32);
		RightFront2ndRib.mirror = true;
		setRotation(RightFront2ndRib, 0F, 1.570796F, 0F);
		RightFrontTopRib = new ModelRenderer(this, 0, 0);
		RightFrontTopRib.addBox(0F, 0F, 0F, 1, 2, 6);
		RightFrontTopRib.setRotationPoint(-7F, -22F, 2F);
		RightFrontTopRib.setTextureSize(64, 32);
		RightFrontTopRib.mirror = true;
		setRotation(RightFrontTopRib, 0F, 1.570796F, 0F);
		LeftBackTopRib = new ModelRenderer(this, 0, 0);
		LeftBackTopRib.addBox(-1F, 0F, 0F, 1, 2, 6);
		LeftBackTopRib.setRotationPoint(7F, -22F, 7F);
		LeftBackTopRib.setTextureSize(64, 32);
		LeftBackTopRib.mirror = true;
		setRotation(LeftBackTopRib, 0F, -1.570796F, 0F);
		LeftBack2ndRib = new ModelRenderer(this, 0, 0);
		LeftBack2ndRib.addBox(-1F, 0F, 0F, 1, 2, 6);
		LeftBack2ndRib.setRotationPoint(7F, -19F, 7F);
		LeftBack2ndRib.setTextureSize(64, 32);
		LeftBack2ndRib.mirror = true;
		setRotation(LeftBack2ndRib, 0F, -1.570796F, 0F);
		LeftBack3rdRib = new ModelRenderer(this, 0, 0);
		LeftBack3rdRib.addBox(-1F, 0F, 0F, 1, 2, 6);
		LeftBack3rdRib.setRotationPoint(7F, -16F, 7F);
		LeftBack3rdRib.setTextureSize(64, 32);
		LeftBack3rdRib.mirror = true;
		setRotation(LeftBack3rdRib, 0F, -1.570796F, 0F);
		LeftBackBotRib = new ModelRenderer(this, 0, 0);
		LeftBackBotRib.addBox(-1F, 0F, 0F, 1, 2, 6);
		LeftBackBotRib.setRotationPoint(7F, -13F, 7F);
		LeftBackBotRib.setTextureSize(64, 32);
		LeftBackBotRib.mirror = true;
		setRotation(LeftBackBotRib, 0F, -1.570796F, 0F);
		RightBackBotRib = new ModelRenderer(this, 0, 0);
		RightBackBotRib.addBox(0F, 0F, 0F, 1, 2, 6);
		RightBackBotRib.setRotationPoint(-7F, -13F, 7F);
		RightBackBotRib.setTextureSize(64, 32);
		RightBackBotRib.mirror = true;
		setRotation(RightBackBotRib, 0F, 1.570796F, 0F);
		RightBack3rdRib = new ModelRenderer(this, 0, 0);
		RightBack3rdRib.addBox(0F, 0F, 0F, 1, 2, 6);
		RightBack3rdRib.setRotationPoint(-7F, -16F, 7F);
		RightBack3rdRib.setTextureSize(64, 32);
		RightBack3rdRib.mirror = true;
		setRotation(RightBack3rdRib, 0F, 1.570796F, 0F);
		RightBack2ndRib = new ModelRenderer(this, 0, 0);
		RightBack2ndRib.addBox(0F, 0F, 0F, 1, 2, 6);
		RightBack2ndRib.setRotationPoint(-7F, -19F, 7F);
		RightBack2ndRib.setTextureSize(64, 32);
		RightBack2ndRib.mirror = true;
		setRotation(RightBack2ndRib, 0F, 1.570796F, 0F);
		RightBackTopRib = new ModelRenderer(this, 0, 0);
		RightBackTopRib.addBox(0F, 0F, 0F, 1, 2, 6);
		RightBackTopRib.setRotationPoint(-7F, -22F, 7F);
		RightBackTopRib.setTextureSize(64, 32);
		RightBackTopRib.mirror = true;
		setRotation(RightBackTopRib, 0F, 1.570796F, 0F);
	}
	  
	@Override
	public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5)
	{
		super.render(entity, f, f1, f2, f3, f4, f5);
		setRotationAngles(f, f1, f2, f3, f4, f5, entity);
		UpperHead.render(f5);
		Pelvis.render(f5);
		Sternum.render(f5);
		RightLeg.render(f5);
		RightArm.render(f5);
		Spine.render(f5);
		LeftArm.render(f5);
		LeftLeg.render(f5);
		LeftFrontBotRib.render(f5);
		LeftFrontTopRib.render(f5);
		LeftFront2ndRib.render(f5);
		LeftFront3rdRib.render(f5);
		LeftSideBotRib.render(f5);
		LeftSide3rdRib.render(f5);
		LeftSide2ndRib.render(f5);
		LeftSideTopRib.render(f5);
		RightSideTopRib.render(f5);
		RightSide2ndRib.render(f5);
		RightSide3rdRib.render(f5);
		RightSideBotRib.render(f5);
		RightFrontBotRib.render(f5);
		RightFront3rdRib.render(f5);
		RightFront2ndRib.render(f5);
		RightFrontTopRib.render(f5);
		LeftBackTopRib.render(f5);
		LeftBack2ndRib.render(f5);
		LeftBack3rdRib.render(f5);
		LeftBackBotRib.render(f5);
		RightBackBotRib.render(f5);
		RightBack3rdRib.render(f5);
		RightBack2ndRib.render(f5);
		RightBackTopRib.render(f5);
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
		super.setRotationAngles(par1, par2, par3, par4, par5, par6, e);
    	this.UpperHead.rotateAngleY = 0;
        this.UpperHead.rotateAngleX = 0;
        this.RightArm.rotateAngleX = MathHelper.cos(par1 * 0.6662F + (float)Math.PI) * 2.0F * par2 * 0.5F;
        this.LeftArm.rotateAngleX = MathHelper.cos(par1 * 0.6662F) * 2.0F * par2 * 0.5F;
        this.RightArm.rotateAngleZ = 0.0F;
        this.LeftArm.rotateAngleZ = 0.0F;
        this.RightLeg.rotateAngleX = MathHelper.cos(par1 * 0.6662F) * 1.4F * par2;
        this.LeftLeg.rotateAngleX = MathHelper.cos(par1 * 0.6662F + (float)Math.PI) * 1.4F * par2;
        this.RightLeg.rotateAngleY = 0.0F;
        this.LeftLeg.rotateAngleY = 0.0F;

        if (this.isRiding)
        {
            this.RightArm.rotateAngleX += -((float)Math.PI / 5F);
            this.LeftArm.rotateAngleX += -((float)Math.PI / 5F);
            this.RightLeg.rotateAngleX = -((float)Math.PI * 2F / 5F);
            this.LeftLeg.rotateAngleX = -((float)Math.PI * 2F / 5F);
            this.RightLeg.rotateAngleY = (float)Math.PI / 10F;
            this.LeftLeg.rotateAngleY = -((float)Math.PI / 10F);
        }

        this.RightArm.rotateAngleY = 0.0F;
        this.LeftArm.rotateAngleY = 0.0F;
        float var7;
        float var8;

        if (this.onGround > -9990.0F)
        {
            var7 = this.onGround;
            this.Spine.rotateAngleY = MathHelper.sin(MathHelper.sqrt_float(var7) * (float)Math.PI * 2.0F) * 0.2F;
            
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
            var8 = MathHelper.sin(var7 * (float)Math.PI);
            final float var9 = MathHelper.sin(this.onGround * (float)Math.PI) * -(this.UpperHead.rotateAngleX - 0.7F) * 0.75F;
            this.RightArm.rotateAngleX = (float)(this.RightArm.rotateAngleX - (var8 * 1.2D + var9));
            this.RightArm.rotateAngleY += this.Spine.rotateAngleY * 2.0F;
            this.RightArm.rotateAngleZ = MathHelper.sin(this.onGround * (float)Math.PI) * -0.4F;
        }

        this.RightArm.rotateAngleZ += MathHelper.cos(par3 * 0.09F) * 0.05F + 0.05F;
        this.LeftArm.rotateAngleZ -= MathHelper.cos(par3 * 0.09F) * 0.05F + 0.05F;
        this.RightArm.rotateAngleX += MathHelper.sin(par3 * 0.067F) * 0.05F;
        this.LeftArm.rotateAngleX -= MathHelper.sin(par3 * 0.067F) * 0.05F;

        MathHelper.sin(this.onGround * (float)Math.PI);
        MathHelper.sin((1.0F - (1.0F - this.onGround) * (1.0F - this.onGround)) * (float)Math.PI);

        var7 = 0.0F;
        var8 = 0.0F;
        this.RightArm.rotateAngleZ = 0.0F;
        this.LeftArm.rotateAngleZ = 0.0F;
        this.RightArm.rotateAngleY = -(0.1F - var7 * 0.6F) + this.UpperHead.rotateAngleY;
        this.LeftArm.rotateAngleY = 0.1F - var7 * 0.6F + this.UpperHead.rotateAngleY + 0.4F;
        this.RightArm.rotateAngleX = -((float)Math.PI / 2F) + this.UpperHead.rotateAngleX;
        this.LeftArm.rotateAngleX = -((float)Math.PI / 2F) + this.UpperHead.rotateAngleX;
        this.RightArm.rotateAngleX -= var7 * 1.2F - var8 * 0.4F;
        this.LeftArm.rotateAngleX -= var7 * 1.2F - var8 * 0.4F;
        this.RightArm.rotateAngleZ += MathHelper.cos(par3 * 0.09F) * 0.05F + 0.05F;
        this.LeftArm.rotateAngleZ -= MathHelper.cos(par3 * 0.09F) * 0.05F + 0.05F;
        this.RightArm.rotateAngleX += MathHelper.sin(par3 * 0.067F) * 0.05F;
        this.LeftArm.rotateAngleX -= MathHelper.sin(par3 * 0.067F) * 0.05F;
            
        final float f6 = MathHelper.sin(this.onGround * (float)Math.PI);
        final float f7 = MathHelper.sin((1.0F - (1.0F - this.onGround) * (1.0F - this.onGround)) * (float)Math.PI);
        this.RightArm.rotateAngleZ = 0.0F;
        this.LeftArm.rotateAngleZ = 0.0F;
        this.RightArm.rotateAngleY = -(0.1F - f6 * 0.6F);
        this.LeftArm.rotateAngleY = 0.1F - f6 * 0.6F;
        this.RightArm.rotateAngleX = -((float)Math.PI / 2F);
        this.LeftArm.rotateAngleX = -((float)Math.PI / 2F);
        this.RightArm.rotateAngleX -= f6 * 1.2F - f7 * 0.4F;
        this.LeftArm.rotateAngleX -= f6 * 1.2F - f7 * 0.4F;
        this.RightArm.rotateAngleZ += MathHelper.cos(par3 * 0.09F) * 0.05F + 0.05F;
        this.LeftArm.rotateAngleZ -= MathHelper.cos(par3 * 0.09F) * 0.05F + 0.05F;
        this.RightArm.rotateAngleX += MathHelper.sin(par3 * 0.067F) * 0.05F;
        this.LeftArm.rotateAngleX -= MathHelper.sin(par3 * 0.067F) * 0.05F;
    }
}
