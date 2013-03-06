package micdoodle8.mods.galacticraft.core.client.model.block;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

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
		
		base = new ModelRenderer(this, 64, 0);
		base.addBox(-8F, -2.5F, -8F, 16, 5, 16);
		base.setRotationPoint(0F, 21.5F, 0F);
		base.setTextureSize(128, 64);
		base.mirror = true;
		setRotation(base, 0F, 0F, 0F);
		fuelTank = new ModelRenderer(this, 48, 0);
		fuelTank.addBox(-13.5F, -3F, -13.5F, 3, 6, 3);
		fuelTank.setRotationPoint(6F, 16F, 6F);
		fuelTank.setTextureSize(128, 64);
		fuelTank.mirror = true;
		setRotation(fuelTank, 0F, 0F, 0F);
		oilTank = new ModelRenderer(this, 32, 0);
		oilTank.addBox(-1.5F, -3F, -1.5F, 3, 6, 3);
		oilTank.setRotationPoint(6F, 16F, 6F);
		oilTank.setTextureSize(128, 64);
		oilTank.mirror = true;
		setRotation(oilTank, 0F, 0F, 0F);
		Shape3 = new ModelRenderer(this, 0, 0);
		Shape3.addBox(-1F, -0.5F, -1F, 2, 1, 2);
		Shape3.setRotationPoint(2F, 9F, -2F);
		Shape3.setTextureSize(128, 64);
		Shape3.mirror = true;
		setRotation(Shape3, 0F, 0F, 0F);
		tube[2] = new ModelRenderer(this, 17, 0);
		tube[2].addBox(-0.5F, -0.5F, -2.5F, 1, 1, 5);
		tube[2].setRotationPoint(-1F, 14F, -4F);
		tube[2].setTextureSize(128, 64);
		tube[2].mirror = true;
		setRotation(tube[2], 0F, 0F, 0F);
		tube[3] = new ModelRenderer(this, 17, 7);
		tube[3].addBox(-2F, -0.5F, -0.5F, 4, 1, 1);
		tube[3].setRotationPoint(-3F, 14F, -6F);
		tube[3].setTextureSize(128, 64);
		tube[3].mirror = true;
		setRotation(tube[3], 0F, 0F, 0F);
		tube[0] = new ModelRenderer(this, 17, 7);
		tube[0].addBox(-2F, -0.5F, -0.5F, 4, 1, 1);
		tube[0].setRotationPoint(3F, 17F, 6F);
		tube[0].setTextureSize(128, 64);
		tube[0].mirror = true;
		setRotation(tube[0], 0F, 0F, 0F);
		tube[1] = new ModelRenderer(this, 17, 0);
		tube[1].addBox(-0.5F, -0.5F, -2.5F, 1, 1, 5);
		tube[1].setRotationPoint(1F, 17F, 4F);
		tube[1].setTextureSize(128, 64);
		tube[1].mirror = true;
		setRotation(tube[1], 0F, 0F, 0F);
		mainBoiler = new ModelRenderer(this, 0, 35);
		mainBoiler.addBox(-4F, -5F, -4F, 8, 10, 8);
		mainBoiler.setRotationPoint(0F, 14F, 0F);
		mainBoiler.setTextureSize(128, 64);
		mainBoiler.mirror = true;
		setRotation(mainBoiler, 0F, 0F, 0F);
		smokeStack[0] = new ModelRenderer(this, 0, 0);
		smokeStack[0].addBox(-1F, -0.5F, -1F, 2, 1, 2);
		smokeStack[0].setRotationPoint(2F, 9F, 2F);
		smokeStack[0].setTextureSize(128, 64);
		smokeStack[0].mirror = true;
		setRotation(smokeStack[0], 0F, 0F, 0F);
		smokeStack[1] = new ModelRenderer(this, 0, 0);
		smokeStack[1].addBox(-1F, -0.5F, -1F, 2, 1, 2);
		smokeStack[1].setRotationPoint(-2F, 9F, 2F);
		smokeStack[1].setTextureSize(128, 64);
		smokeStack[1].mirror = true;
		setRotation(smokeStack[1], 0F, 0F, 0F);
		smokeStack[2] = new ModelRenderer(this, 0, 0);
		smokeStack[2].addBox(-1F, -0.5F, -1F, 2, 1, 2);
		smokeStack[2].setRotationPoint(-2F, 9F, -2F);
		smokeStack[2].setTextureSize(128, 64);
		smokeStack[2].mirror = true;
		setRotation(smokeStack[2], 0F, 0F, 0F);
	}

	public void renderAll()
	{
		base.render(0.0625F);
		Shape3.render(0.0625F);
		mainBoiler.render(0.0625F);
		smokeStack[0].render(0.0625F);
		smokeStack[1].render(0.0625F);
		smokeStack[2].render(0.0625F);
	}
	
	public void renderTank1()
	{
		oilTank.render(0.0625F);
		tube[0].render(0.0625F);
		tube[1].render(0.0625F);
	}
	
	public void renderTank2()
	{
		tube[2].render(0.0625F);
		tube[3].render(0.0625F);
		fuelTank.render(0.0625F);
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
	
	private float toRadians(float f)
	{
		return (float) (f * (Math.PI / 180.0F));
	}
}
