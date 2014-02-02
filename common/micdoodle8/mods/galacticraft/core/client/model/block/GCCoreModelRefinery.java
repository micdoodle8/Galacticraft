package micdoodle8.mods.galacticraft.core.client.model.block;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

/**
 * GCCoreModelRefinery.java
 * 
 * This file is part of the Galacticraft project
 * 
 * @author micdoodle8
 * @license Lesser GNU Public License v3 (http://www.gnu.org/licenses/lgpl.html)
 * 
 */
public class GCCoreModelRefinery extends ModelBase
{
	ModelRenderer base;
	ModelRenderer fuelTank;
	ModelRenderer oilTank;
	ModelRenderer Shape3;
	ModelRenderer[] tube = new ModelRenderer[4];
	ModelRenderer mainBoiler;
	ModelRenderer[] smokeStack = new ModelRenderer[4];

	public GCCoreModelRefinery()
	{
		this.textureWidth = 128;
		this.textureHeight = 64;

		this.base = new ModelRenderer(this, 64, 0);
		this.base.addBox(-8F, -2.5F, -8F, 16, 5, 16);
		this.base.setRotationPoint(0F, 21.5F, 0F);
		this.base.setTextureSize(128, 64);
		this.base.mirror = true;
		this.setRotation(this.base, 0F, 0F, 0F);
		this.fuelTank = new ModelRenderer(this, 48, 0);
		this.fuelTank.addBox(-13.5F, -3F, -13.5F, 3, 6, 3);
		this.fuelTank.setRotationPoint(6F, 16F, 6F);
		this.fuelTank.setTextureSize(128, 64);
		this.fuelTank.mirror = true;
		this.setRotation(this.fuelTank, 0F, 0F, 0F);
		this.oilTank = new ModelRenderer(this, 32, 0);
		this.oilTank.addBox(-1.5F, -3F, -1.5F, 3, 6, 3);
		this.oilTank.setRotationPoint(6F, 16F, 6F);
		this.oilTank.setTextureSize(128, 64);
		this.oilTank.mirror = true;
		this.setRotation(this.oilTank, 0F, 0F, 0F);
		this.Shape3 = new ModelRenderer(this, 0, 0);
		this.Shape3.addBox(-1F, -0.5F, -1F, 2, 1, 2);
		this.Shape3.setRotationPoint(2F, 9F, -2F);
		this.Shape3.setTextureSize(128, 64);
		this.Shape3.mirror = true;
		this.setRotation(this.Shape3, 0F, 0F, 0F);
		this.tube[2] = new ModelRenderer(this, 17, 0);
		this.tube[2].addBox(-0.5F, -0.5F, -2.5F, 1, 1, 5);
		this.tube[2].setRotationPoint(-1F, 14F, -4F);
		this.tube[2].setTextureSize(128, 64);
		this.tube[2].mirror = true;
		this.setRotation(this.tube[2], 0F, 0F, 0F);
		this.tube[3] = new ModelRenderer(this, 17, 7);
		this.tube[3].addBox(-2F, -0.5F, -0.5F, 4, 1, 1);
		this.tube[3].setRotationPoint(-3F, 14F, -6F);
		this.tube[3].setTextureSize(128, 64);
		this.tube[3].mirror = true;
		this.setRotation(this.tube[3], 0F, 0F, 0F);
		this.tube[0] = new ModelRenderer(this, 17, 7);
		this.tube[0].addBox(-2F, -0.5F, -0.5F, 4, 1, 1);
		this.tube[0].setRotationPoint(3F, 17F, 6F);
		this.tube[0].setTextureSize(128, 64);
		this.tube[0].mirror = true;
		this.setRotation(this.tube[0], 0F, 0F, 0F);
		this.tube[1] = new ModelRenderer(this, 17, 0);
		this.tube[1].addBox(-0.5F, -0.5F, -2.5F, 1, 1, 5);
		this.tube[1].setRotationPoint(1F, 17F, 4F);
		this.tube[1].setTextureSize(128, 64);
		this.tube[1].mirror = true;
		this.setRotation(this.tube[1], 0F, 0F, 0F);
		this.mainBoiler = new ModelRenderer(this, 0, 35);
		this.mainBoiler.addBox(-4F, -5F, -4F, 8, 10, 8);
		this.mainBoiler.setRotationPoint(0F, 14F, 0F);
		this.mainBoiler.setTextureSize(128, 64);
		this.mainBoiler.mirror = true;
		this.setRotation(this.mainBoiler, 0F, 0F, 0F);
		this.smokeStack[0] = new ModelRenderer(this, 0, 0);
		this.smokeStack[0].addBox(-1F, -0.5F, -1F, 2, 1, 2);
		this.smokeStack[0].setRotationPoint(2F, 9F, 2F);
		this.smokeStack[0].setTextureSize(128, 64);
		this.smokeStack[0].mirror = true;
		this.setRotation(this.smokeStack[0], 0F, 0F, 0F);
		this.smokeStack[1] = new ModelRenderer(this, 0, 0);
		this.smokeStack[1].addBox(-1F, -0.5F, -1F, 2, 1, 2);
		this.smokeStack[1].setRotationPoint(-2F, 9F, 2F);
		this.smokeStack[1].setTextureSize(128, 64);
		this.smokeStack[1].mirror = true;
		this.setRotation(this.smokeStack[1], 0F, 0F, 0F);
		this.smokeStack[2] = new ModelRenderer(this, 0, 0);
		this.smokeStack[2].addBox(-1F, -0.5F, -1F, 2, 1, 2);
		this.smokeStack[2].setRotationPoint(-2F, 9F, -2F);
		this.smokeStack[2].setTextureSize(128, 64);
		this.smokeStack[2].mirror = true;
		this.setRotation(this.smokeStack[2], 0F, 0F, 0F);
	}

	public void renderAll()
	{
		this.base.render(0.0625F);
		this.Shape3.render(0.0625F);
		this.mainBoiler.render(0.0625F);
		this.smokeStack[0].render(0.0625F);
		this.smokeStack[1].render(0.0625F);
		this.smokeStack[2].render(0.0625F);
	}

	public void renderTank1()
	{
		this.oilTank.render(0.0625F);
		this.tube[0].render(0.0625F);
		this.tube[1].render(0.0625F);
	}

	public void renderTank2()
	{
		this.tube[2].render(0.0625F);
		this.tube[3].render(0.0625F);
		this.fuelTank.render(0.0625F);
	}

	private void setRotation(ModelRenderer model, float x, float y, float z)
	{
		model.rotateAngleX = x;
		model.rotateAngleY = y;
		model.rotateAngleZ = z;
	}

	@Override
	public void setRotationAngles(float f, float f1, float f2, float f3, float f4, float f5, Entity e)
	{
		super.setRotationAngles(f, f1, f2, f3, f4, f5, e);
	}
}
