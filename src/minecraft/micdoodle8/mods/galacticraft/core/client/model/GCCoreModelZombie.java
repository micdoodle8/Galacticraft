package micdoodle8.mods.galacticraft.core.client.model;

import net.minecraft.src.Entity;
import net.minecraft.src.MathHelper;
import net.minecraft.src.ModelBase;
import net.minecraft.src.ModelRenderer;

/**
 * Copyright 2012, micdoodle8
 * 
 *  All rights reserved.
 *
 */
public class GCCoreModelZombie extends ModelBase
{
	ModelRenderer leftOxygenTank;
	ModelRenderer rightOxygenTank;
	ModelRenderer tubeRight2;
	ModelRenderer tubeLeft1;
	ModelRenderer tubeRight3;
	ModelRenderer tubeRight4;
	ModelRenderer tubeRight5;
	ModelRenderer tubeLeft6;
	ModelRenderer tubeRight7;
	ModelRenderer tubeRight1;
	ModelRenderer tubeLeft2;
	ModelRenderer tubeLeft3;
	ModelRenderer tubeLeft4;
	ModelRenderer tubeLeft5;
	ModelRenderer tubeLeft7;
	ModelRenderer tubeRight6;
	ModelRenderer tubeLeft8;
	ModelRenderer oxygenMask;
    ModelRenderer bipedHead;
    ModelRenderer bipedHeadwear;
    ModelRenderer bipedBody;
    ModelRenderer bipedRightArm;
    ModelRenderer bipedLeftArm;
    ModelRenderer bipedRightLeg;
    ModelRenderer bipedLeftLeg;
    ModelRenderer bipedEars;
    ModelRenderer bipedCloak;
	
	public GCCoreModelZombie()
	{
		textureWidth = 128;
		textureHeight = 64;
		int par1 = 0;
		int par2 = 0;
		leftOxygenTank = new ModelRenderer(this, 56, 20);
		leftOxygenTank.addBox(-1.5F, 0F, -1.5F, 3, 7, 3);
		leftOxygenTank.setRotationPoint(2F, 2F, 3.8F);
		leftOxygenTank.setTextureSize(128, 64);
		leftOxygenTank.mirror = true;
		setRotation(leftOxygenTank, 0F, 0F, 0F);
		rightOxygenTank = new ModelRenderer(this, 56, 20);
		rightOxygenTank.addBox(-1.5F, 0F, -1.5F, 3, 7, 3);
		rightOxygenTank.setRotationPoint(-2F, 2F, 3.8F);
		rightOxygenTank.setTextureSize(128, 64);
		rightOxygenTank.mirror = true;
		setRotation(rightOxygenTank, 0F, 0F, 0F);
		tubeRight2 = new ModelRenderer(this, 56, 30);
		tubeRight2.addBox(-0.5F, -0.5F, -0.5F, 1, 1, 1);
		tubeRight2.setRotationPoint(-2F, 2F, 6.8F);
		tubeRight2.setTextureSize(128, 64);
		tubeRight2.mirror = true;
		setRotation(tubeRight2, 0F, 0F, 0F);
		tubeLeft1 = new ModelRenderer(this, 56, 30);
		tubeLeft1.addBox(-0.5F, -0.5F, -0.5F, 1, 1, 1);
		tubeLeft1.setRotationPoint(2F, 3F, 5.8F);
		tubeLeft1.setTextureSize(128, 64);
		tubeLeft1.mirror = true;
		setRotation(tubeLeft1, 0F, 0F, 0F);
		tubeRight3 = new ModelRenderer(this, 56, 30);
		tubeRight3.addBox(-0.5F, -0.5F, -0.5F, 1, 1, 1);
		tubeRight3.setRotationPoint(-2F, 1F, 6.8F);
		tubeRight3.setTextureSize(128, 64);
		tubeRight3.mirror = true;
		setRotation(tubeRight3, 0F, 0F, 0F);
		tubeRight4 = new ModelRenderer(this, 56, 30);
		tubeRight4.addBox(-0.5F, -0.5F, -0.5F, 1, 1, 1);
		tubeRight4.setRotationPoint(-2F, 0F, 6.8F);
		tubeRight4.setTextureSize(128, 64);
		tubeRight4.mirror = true;
		setRotation(tubeRight4, 0F, 0F, 0F);
		tubeRight5 = new ModelRenderer(this, 56, 30);
		tubeRight5.addBox(-0.5F, -0.5F, -0.5F, 1, 1, 1);
		tubeRight5.setRotationPoint(-2F, -1F, 6.8F);
		tubeRight5.setTextureSize(128, 64);
		tubeRight5.mirror = true;
		setRotation(tubeRight5, 0F, 0F, 0F);
		tubeLeft6 = new ModelRenderer(this, 56, 30);
		tubeLeft6.addBox(-0.5F, -0.5F, -0.5F, 1, 1, 1);
		tubeLeft6.setRotationPoint(2F, -2F, 5.8F);
		tubeLeft6.setTextureSize(128, 64);
		tubeLeft6.mirror = true;
		setRotation(tubeLeft6, 0F, 0F, 0F);
		tubeRight7 = new ModelRenderer(this, 56, 30);
		tubeRight7.addBox(-0.5F, -0.5F, -0.5F, 1, 1, 1);
		tubeRight7.setRotationPoint(-2F, -3F, 4.8F);
		tubeRight7.setTextureSize(128, 64);
		tubeRight7.mirror = true;
		setRotation(tubeRight7, 0F, 0F, 0F);
		tubeRight1 = new ModelRenderer(this, 56, 30);
		tubeRight1.addBox(-0.5F, -0.5F, -0.5F, 1, 1, 1);
		tubeRight1.setRotationPoint(-2F, 3F, 5.8F);
		tubeRight1.setTextureSize(128, 64);
		tubeRight1.mirror = true;
		setRotation(tubeRight1, 0F, 0F, 0F);
		tubeLeft2 = new ModelRenderer(this, 56, 30);
		tubeLeft2.addBox(-0.5F, -0.5F, -0.5F, 1, 1, 1);
		tubeLeft2.setRotationPoint(2F, 2F, 6.8F);
		tubeLeft2.setTextureSize(128, 64);
		tubeLeft2.mirror = true;
		setRotation(tubeLeft2, 0F, 0F, 0F);
		tubeLeft3 = new ModelRenderer(this, 56, 30);
		tubeLeft3.addBox(-0.5F, -0.5F, -0.5F, 1, 1, 1);
		tubeLeft3.setRotationPoint(2F, 1F, 6.8F);
		tubeLeft3.setTextureSize(128, 64);
		tubeLeft3.mirror = true;
		setRotation(tubeLeft3, 0F, 0F, 0F);
		tubeLeft4 = new ModelRenderer(this, 56, 30);
		tubeLeft4.addBox(-0.5F, -0.5F, -0.5F, 1, 1, 1);
		tubeLeft4.setRotationPoint(2F, 0F, 6.8F);
		tubeLeft4.setTextureSize(128, 64);
		tubeLeft4.mirror = true;
		setRotation(tubeLeft4, 0F, 0F, 0F);
		tubeLeft5 = new ModelRenderer(this, 56, 30);
		tubeLeft5.addBox(-0.5F, -0.5F, -0.5F, 1, 1, 1);
		tubeLeft5.setRotationPoint(2F, -1F, 6.8F);
		tubeLeft5.setTextureSize(128, 64);
		tubeLeft5.mirror = true;
		setRotation(tubeLeft5, 0F, 0F, 0F);
		tubeLeft7 = new ModelRenderer(this, 56, 30);
		tubeLeft7.addBox(-0.5F, -0.5F, -0.5F, 1, 1, 1);
		tubeLeft7.setRotationPoint(2F, -3F, 4.8F);
		tubeLeft7.setTextureSize(128, 64);
		tubeLeft7.mirror = true;
		setRotation(tubeLeft7, 0F, 0F, 0F);
		tubeRight6 = new ModelRenderer(this, 56, 30);
		tubeRight6.addBox(-0.5F, -0.5F, -0.5F, 1, 1, 1);
		tubeRight6.setRotationPoint(-2F, -2F, 5.8F);
		tubeRight6.setTextureSize(128, 64);
		tubeRight6.mirror = true;
		setRotation(tubeRight6, 0F, 0F, 0F);
		tubeLeft8 = new ModelRenderer(this, 56, 30);
		tubeLeft8.addBox(0F, 0F, 0F, 1, 1, 1);
		tubeLeft8.setRotationPoint(0F, -5F, 0F);
		tubeLeft8.setTextureSize(128, 64);
		tubeLeft8.mirror = true;
		setRotation(tubeLeft8, 0F, 0F, 0F);
		oxygenMask = new ModelRenderer(this, 56, 0);
		oxygenMask.addBox(-5F, -9F, -5F, 10, 10, 10);
		oxygenMask.setRotationPoint(0F, 0F, 0F);
		oxygenMask.setTextureSize(128, 64);
		oxygenMask.mirror = true;
		setRotation(oxygenMask, 0F, 0F, 0F);
        this.bipedCloak = new ModelRenderer(this, 0, 0);
        this.bipedCloak.addBox(-5.0F, 0.0F, -1.0F, 10, 16, 1, par1);
        bipedCloak.setTextureSize(128, 64);
        this.bipedEars = new ModelRenderer(this, 24, 0);
        this.bipedEars.addBox(-3.0F, -6.0F, -1.0F, 6, 6, 1, par1);
        bipedEars.setTextureSize(128, 64);
        this.bipedHead = new ModelRenderer(this, 0, 0);
        this.bipedHead.addBox(-4.0F, -8.0F, -4.0F, 8, 8, 8, par1);
        this.bipedHead.setRotationPoint(0.0F, 0.0F + par2, 0.0F);
        bipedHead.setTextureSize(128, 64);
        this.bipedHeadwear = new ModelRenderer(this, 32, 0);
        this.bipedHeadwear.addBox(-4.0F, -8.0F, -4.0F, 8, 8, 8, par1 + 0.5F);
        this.bipedHeadwear.setRotationPoint(0.0F, 0.0F + par2, 0.0F);
        bipedHeadwear.setTextureSize(128, 64);
        this.bipedBody = new ModelRenderer(this, 16, 16);
        this.bipedBody.addBox(-4.0F, 0.0F, -2.0F, 8, 12, 4, par1);
        this.bipedBody.setRotationPoint(0.0F, 0.0F + par2, 0.0F);
        bipedBody.setTextureSize(128, 64);
        this.bipedRightArm = new ModelRenderer(this, 40, 16);
        this.bipedRightArm.addBox(-3.0F, -2.0F, -2.0F, 4, 12, 4, par1);
        this.bipedRightArm.setRotationPoint(-5.0F, 2.0F + par2, 0.0F);
        bipedRightArm.setTextureSize(128, 64);
        this.bipedLeftArm = new ModelRenderer(this, 40, 16);
        this.bipedLeftArm.mirror = true;
        this.bipedLeftArm.addBox(-1.0F, -2.0F, -2.0F, 4, 12, 4, par1);
        this.bipedLeftArm.setRotationPoint(5.0F, 2.0F + par2, 0.0F);
        bipedLeftArm.setTextureSize(128, 64);
        this.bipedRightLeg = new ModelRenderer(this, 0, 16);
        this.bipedRightLeg.addBox(-2.0F, 0.0F, -2.0F, 4, 12, 4, par1);
        this.bipedRightLeg.setRotationPoint(-2.0F, 12.0F + par2, 0.0F);
        bipedRightLeg.setTextureSize(128, 64);
        this.bipedLeftLeg = new ModelRenderer(this, 0, 16);
        this.bipedLeftLeg.mirror = true;
        this.bipedLeftLeg.addBox(-2.0F, 0.0F, -2.0F, 4, 12, 4, par1);
        this.bipedLeftLeg.setRotationPoint(2.0F, 12.0F + par2, 0.0F);
        bipedLeftLeg.setTextureSize(128, 64);
	}

	@Override
	public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5)
	{
		super.render(entity, f, f1, f2, f3, f4, f5);
		setRotationAngles(f, f1, f2, f3, f4, f5);
		leftOxygenTank.render(f5);
		rightOxygenTank.render(f5);
		tubeRight2.render(f5);
		tubeLeft1.render(f5);
		tubeRight3.render(f5);
		tubeRight4.render(f5);
		tubeRight5.render(f5);
		tubeLeft6.render(f5);
		tubeRight7.render(f5);
		tubeRight1.render(f5);
		tubeLeft2.render(f5);
		tubeLeft3.render(f5);
		tubeLeft4.render(f5);
		tubeLeft5.render(f5);
		tubeLeft7.render(f5);
		tubeRight6.render(f5);
		tubeLeft8.render(f5);
		oxygenMask.render(f5);
        this.bipedHead.render(f5);
        this.bipedBody.render(f5);
        this.bipedRightArm.render(f5);
        this.bipedLeftArm.render(f5);
        this.bipedRightLeg.render(f5);
        this.bipedLeftLeg.render(f5);
        this.bipedHeadwear.render(f5);
	}
	
	private void setRotation(ModelRenderer model, float x, float y, float z)
	{
		model.rotateAngleX = x;
		model.rotateAngleY = y;
		model.rotateAngleZ = z;
	}

    public void setRotationAngles(float par1, float par2, float par3, float par4, float par5, float par6)
    {
    	this.bipedHead.rotateAngleY = par4 / (180F / (float)Math.PI);
        this.bipedHead.rotateAngleX = par5 / (180F / (float)Math.PI);
    	this.oxygenMask.rotateAngleY = par4 / (180F / (float)Math.PI);
        this.oxygenMask.rotateAngleX = par5 / (180F / (float)Math.PI);
        this.bipedHeadwear.rotateAngleY = this.bipedHead.rotateAngleY;
        this.bipedHeadwear.rotateAngleX = this.bipedHead.rotateAngleX;
        this.bipedRightArm.rotateAngleX = MathHelper.cos(par1 * 0.6662F + (float)Math.PI) * 2.0F * par2 * 0.5F;
        this.bipedLeftArm.rotateAngleX = MathHelper.cos(par1 * 0.6662F) * 2.0F * par2 * 0.5F;
        this.bipedRightArm.rotateAngleZ = 0.0F;
        this.bipedLeftArm.rotateAngleZ = 0.0F;
        this.bipedRightLeg.rotateAngleX = MathHelper.cos(par1 * 0.6662F) * 1.4F * par2;
        this.bipedLeftLeg.rotateAngleX = MathHelper.cos(par1 * 0.6662F + (float)Math.PI) * 1.4F * par2;
        this.bipedRightLeg.rotateAngleY = 0.0F;
        this.bipedLeftLeg.rotateAngleY = 0.0F;

        if (this.isRiding)
        {
            this.bipedRightArm.rotateAngleX += -((float)Math.PI / 5F);
            this.bipedLeftArm.rotateAngleX += -((float)Math.PI / 5F);
            this.bipedRightLeg.rotateAngleX = -((float)Math.PI * 2F / 5F);
            this.bipedLeftLeg.rotateAngleX = -((float)Math.PI * 2F / 5F);
            this.bipedRightLeg.rotateAngleY = ((float)Math.PI / 10F);
            this.bipedLeftLeg.rotateAngleY = -((float)Math.PI / 10F);
        }

        this.bipedRightArm.rotateAngleY = 0.0F;
        this.bipedLeftArm.rotateAngleY = 0.0F;
        float var7;
        float var8;

        if (this.onGround > -9990.0F)
        {
            var7 = this.onGround;
            this.bipedBody.rotateAngleY = MathHelper.sin(MathHelper.sqrt_float(var7) * (float)Math.PI * 2.0F) * 0.2F;
            this.bipedRightArm.rotationPointZ = MathHelper.sin(this.bipedBody.rotateAngleY) * 5.0F;
            this.bipedRightArm.rotationPointX = -MathHelper.cos(this.bipedBody.rotateAngleY) * 5.0F;
            this.bipedLeftArm.rotationPointZ = -MathHelper.sin(this.bipedBody.rotateAngleY) * 5.0F;
            this.bipedLeftArm.rotationPointX = MathHelper.cos(this.bipedBody.rotateAngleY) * 5.0F;
            this.bipedRightArm.rotateAngleY += this.bipedBody.rotateAngleY;
            this.bipedLeftArm.rotateAngleY += this.bipedBody.rotateAngleY;
            this.bipedLeftArm.rotateAngleX += this.bipedBody.rotateAngleY;
            var7 = 1.0F - this.onGround;
            var7 *= var7;
            var7 *= var7;
            var7 = 1.0F - var7;
            var8 = MathHelper.sin(var7 * (float)Math.PI);
            float var9 = MathHelper.sin(this.onGround * (float)Math.PI) * -(this.bipedHead.rotateAngleX - 0.7F) * 0.75F;
            this.bipedRightArm.rotateAngleX = (float)(this.bipedRightArm.rotateAngleX - (var8 * 1.2D + var9));
            this.bipedRightArm.rotateAngleY += this.bipedBody.rotateAngleY * 2.0F;
            this.bipedRightArm.rotateAngleZ = MathHelper.sin(this.onGround * (float)Math.PI) * -0.4F;
        }

        this.bipedBody.rotateAngleX = 0.0F;
        this.bipedRightLeg.rotationPointZ = 0.0F;
        this.bipedLeftLeg.rotationPointZ = 0.0F;
        this.bipedRightLeg.rotationPointY = 12.0F;
        this.bipedLeftLeg.rotationPointY = 12.0F;
        this.bipedHead.rotationPointY = 0.0F;

        this.bipedRightArm.rotateAngleZ += MathHelper.cos(par3 * 0.09F) * 0.05F + 0.05F;
        this.bipedLeftArm.rotateAngleZ -= MathHelper.cos(par3 * 0.09F) * 0.05F + 0.05F;
        this.bipedRightArm.rotateAngleX += MathHelper.sin(par3 * 0.067F) * 0.05F;
        this.bipedLeftArm.rotateAngleX -= MathHelper.sin(par3 * 0.067F) * 0.05F;

        float var7a = MathHelper.sin(this.onGround * (float)Math.PI);
        float var8a = MathHelper.sin((1.0F - (1.0F - this.onGround) * (1.0F - this.onGround)) * (float)Math.PI);
        
        this.bipedRightArm.rotateAngleZ = 0.0F;
        this.bipedLeftArm.rotateAngleZ = 0.0F;
        this.bipedRightArm.rotateAngleY = -(0.1F - var7a * 0.6F);
        this.bipedLeftArm.rotateAngleY = 0.1F - var7a * 0.6F;
        this.bipedRightArm.rotateAngleX = -((float)Math.PI / 2F);
        this.bipedLeftArm.rotateAngleX = -((float)Math.PI / 2F);
        this.bipedRightArm.rotateAngleX -= var7a * 1.2F - var8a * 0.4F;
        this.bipedLeftArm.rotateAngleX -= var7a * 1.2F - var8a * 0.4F;
        this.bipedRightArm.rotateAngleZ += MathHelper.cos(par3 * 0.09F) * 0.05F + 0.05F;
        this.bipedLeftArm.rotateAngleZ -= MathHelper.cos(par3 * 0.09F) * 0.05F + 0.05F;
        this.bipedRightArm.rotateAngleX += MathHelper.sin(par3 * 0.067F) * 0.05F;
        this.bipedLeftArm.rotateAngleX -= MathHelper.sin(par3 * 0.067F) * 0.05F;
    }
}
