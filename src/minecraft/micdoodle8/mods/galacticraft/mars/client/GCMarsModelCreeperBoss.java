package micdoodle8.mods.galacticraft.mars.client;

import micdoodle8.mods.galacticraft.mars.GCMarsEntityCreeperBoss;
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
public class GCMarsModelCreeperBoss extends ModelBase 
{
	ModelRenderer headMain;
	ModelRenderer bodyMain;
	ModelRenderer rightLegFront;
	ModelRenderer leftLegFront;
	ModelRenderer rightLeg;
	ModelRenderer leftLeg;
	ModelRenderer oxygenTank;
	ModelRenderer headLeft;
	ModelRenderer headRight;
	ModelRenderer neckRight;
	ModelRenderer neckLeft;

	public GCMarsModelCreeperBoss() 
	{
		textureWidth = 128;
		textureHeight = 64;
		neckRight = new ModelRenderer(this, 16, 20);
		neckRight.mirror = true;
		neckRight.addBox(-2.5F, -9F, -1.5F, 5, 9, 3);
		neckRight.setRotationPoint(-3F, 10F, 0F);
		neckRight.setTextureSize(128, 64);
		neckRight.mirror = true;
		setRotation(neckRight, 0F, 0F, -1.169371F);
		neckRight.mirror = false;
		neckLeft = new ModelRenderer(this, 16, 20);
		neckLeft.addBox(-2.5F, -9F, -1.5F, 5, 9, 3);
		neckLeft.setRotationPoint(3F, 10F, 0F);
		neckLeft.setTextureSize(128, 64);
		neckLeft.mirror = true;
		setRotation(neckLeft, 0F, 0F, 1.169371F);
		headMain = new ModelRenderer(this, 0, 0);
		headMain.addBox(-4F, -8F, -4F, 8, 8, 8);
		headMain.setRotationPoint(0F, 6F, 0F);
		headMain.setTextureSize(128, 64);
		headMain.mirror = true;
		setRotation(headMain, 0F, 0F, 0F);
		bodyMain = new ModelRenderer(this, 16, 16);
		bodyMain.addBox(-4F, 0F, -2F, 8, 12, 4);
		bodyMain.setRotationPoint(0F, 6F, 0F);
		bodyMain.setTextureSize(128, 64);
		bodyMain.mirror = true;
		setRotation(bodyMain, 0F, 0F, 0F);
		rightLegFront = new ModelRenderer(this, 0, 16);
		rightLegFront.addBox(-2F, 0F, -2F, 4, 6, 4);
		rightLegFront.setRotationPoint(-2F, 18F, -4F);
		rightLegFront.setTextureSize(128, 64);
		rightLegFront.mirror = true;
		setRotation(rightLegFront, 0F, 0F, 0F);
		leftLegFront = new ModelRenderer(this, 0, 16);
		leftLegFront.addBox(-2F, 0F, -2F, 4, 6, 4);
		leftLegFront.setRotationPoint(2F, 18F, -4F);
		leftLegFront.setTextureSize(128, 64);
		leftLegFront.mirror = true;
		setRotation(leftLegFront, 0F, 0F, 0F);
		rightLeg = new ModelRenderer(this, 0, 16);
		rightLeg.addBox(0F, 0F, -2F, 4, 6, 4);
		rightLeg.setRotationPoint(-4F, 18F, 4F);
		rightLeg.setTextureSize(128, 64);
		rightLeg.mirror = true;
		setRotation(rightLeg, 0F, 0F, 0F);
		leftLeg = new ModelRenderer(this, 0, 16);
		leftLeg.addBox(-2F, 0F, -2F, 4, 6, 4);
		leftLeg.setRotationPoint(2F, 18F, 4F);
		leftLeg.setTextureSize(128, 64);
		leftLeg.mirror = true;
		setRotation(leftLeg, 0F, 0F, 0F);
		oxygenTank = new ModelRenderer(this, 40, 0);
		oxygenTank.addBox(-5F, -9F, -5F, 10, 10, 10);
		oxygenTank.setRotationPoint(0F, 6F, 0F);
		oxygenTank.setTextureSize(128, 64);
		oxygenTank.mirror = true;
		setRotation(oxygenTank, 0F, 0F, 0F);
		headLeft = new ModelRenderer(this, 0, 0);
		headLeft.addBox(1F, -9F, -4F, 8, 8, 8);
		headLeft.setRotationPoint(3F, 6F, 0.1F);
		headLeft.setTextureSize(128, 64);
		headLeft.mirror = true;
		setRotation(headLeft, 0F, 0F, 0.7853982F);
		headRight = new ModelRenderer(this, 0, 0);
		headRight.addBox(-9F, -9F, -4F, 8, 8, 8);
		headRight.setRotationPoint(-3F, 6F, -0.1F);
		headRight.setTextureSize(128, 64);
		headRight.mirror = true;
		setRotation(headRight, 0F, 0F, -0.7853982F);

	}

	@Override
	public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) 
	{
		super.render(entity, f, f1, f2, f3, f4, f5);
		
		GCMarsEntityCreeperBoss creeper = (GCMarsEntityCreeperBoss) entity;
		
		if (creeper.headsRemaining == 3)
		{
			headLeft.render(f5);
			neckLeft.render(f5);
		}
		
		if (creeper.headsRemaining >= 2)
		{
			headRight.render(f5);
			neckRight.render(f5);
		}

		this.setRotationAngles(f, f1, f2, f3, f4, f5, entity);
		headMain.render(f5);
		bodyMain.render(f5);
		rightLegFront.render(f5);
		leftLegFront.render(f5);
		rightLeg.render(f5);
		leftLeg.render(f5);
		oxygenTank.render(f5);
	}

	private void setRotation(ModelRenderer model, float x, float y, float z) 
	{
		model.rotateAngleX = x;
		model.rotateAngleY = y;
		model.rotateAngleZ = z;
	}

	@Override
	public void setRotationAngles(float f, float f1, float f2, float f3, float f4, float f5, Entity entity) 
	{
		super.setRotationAngles(f, f1, f2, f3, f4, f5, entity);
		this.headMain.rotateAngleY = f3 / (180F / (float) Math.PI);
		this.headMain.rotateAngleX = f4 / (180F / (float) Math.PI);
		this.oxygenTank.rotateAngleY = f3 / (180F / (float) Math.PI);
		this.oxygenTank.rotateAngleX = f4 / (180F / (float) Math.PI);
		this.rightLegFront.rotateAngleX = MathHelper.cos(f * 0.6662F) * 1.4F * f1;
		this.leftLegFront.rotateAngleX = MathHelper.cos(f * 0.6662F + (float) Math.PI) * 2F * f1;
		this.leftLeg.rotateAngleX = MathHelper.cos(f * 0.6662F + (float) Math.PI) * 2F * f1;
		this.rightLeg.rotateAngleX = MathHelper.cos(f * 0.6662F) * 2F * f1;
	}
}
