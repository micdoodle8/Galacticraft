package micdoodle8.mods.galacticraft.core.client.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.util.MathHelper;

/**
 * GCCoreModelSpider.java
 * 
 * This file is part of the Galacticraft project
 * 
 * @author micdoodle8
 * @license Lesser GNU Public License v3 (http://www.gnu.org/licenses/lgpl.html)
 * 
 */
public class GCCoreModelSpider extends ModelBase
{
	ModelRenderer body;
	ModelRenderer rearEnd;
	ModelRenderer leg8;
	ModelRenderer leg6;
	ModelRenderer leg4;
	ModelRenderer leg2;
	ModelRenderer leg7;
	ModelRenderer leg5;
	ModelRenderer leg3;
	ModelRenderer leg1;
	ModelRenderer head;

	ModelRenderer oxygenMask;
	ModelRenderer tank1;
	ModelRenderer tank2;
	ModelRenderer tube1;
	ModelRenderer tube2;
	ModelRenderer tube3;
	ModelRenderer tube4;
	ModelRenderer tube5;
	ModelRenderer tube6;
	ModelRenderer tube7;
	ModelRenderer tube8;
	ModelRenderer tube9;
	ModelRenderer tube10;
	ModelRenderer tube11;
	ModelRenderer tube12;
	ModelRenderer tube13;
	ModelRenderer tube15;
	ModelRenderer tube14;
	ModelRenderer tube16;
	ModelRenderer tube17;
	ModelRenderer tube18;

	public GCCoreModelSpider()
	{
		this(0.0F);
	}

	public GCCoreModelSpider(float par1)
	{
		this.textureWidth = 128;
		this.textureHeight = 64;

		this.body = new ModelRenderer(this, 0, 0);
		this.body.addBox(-3F, -3F, -3F, 6, 6, 6, par1);
		this.body.setRotationPoint(0F, 15F, 0F);
		this.body.setTextureSize(128, 64);
		this.body.mirror = true;
		this.setRotation(this.body, 0F, 0F, 0F);
		this.rearEnd = new ModelRenderer(this, 0, 12);
		this.rearEnd.addBox(-5F, -4F, -6F, 10, 8, 12, par1);
		this.rearEnd.setRotationPoint(0F, 15F, 9F);
		this.rearEnd.setTextureSize(128, 64);
		this.rearEnd.mirror = true;
		this.setRotation(this.rearEnd, 0F, 0F, 0F);
		this.leg8 = new ModelRenderer(this, 18, 0);
		this.leg8.addBox(-1F, -1F, -1F, 16, 2, 2, par1);
		this.leg8.setRotationPoint(4F, 15F, -1F);
		this.leg8.setTextureSize(128, 64);
		this.leg8.mirror = true;
		this.setRotation(this.leg8, 0F, 0.5759587F, 0.1919862F);
		this.leg6 = new ModelRenderer(this, 18, 0);
		this.leg6.addBox(-1F, -1F, -1F, 16, 2, 2, par1);
		this.leg6.setRotationPoint(4F, 15F, 0F);
		this.leg6.setTextureSize(128, 64);
		this.leg6.mirror = true;
		this.setRotation(this.leg6, 0F, 0.2792527F, 0.1919862F);
		this.leg4 = new ModelRenderer(this, 18, 0);
		this.leg4.addBox(-1F, -1F, -1F, 16, 2, 2, par1);
		this.leg4.setRotationPoint(4F, 15F, 1F);
		this.leg4.setTextureSize(128, 64);
		this.leg4.mirror = true;
		this.setRotation(this.leg4, 0F, -0.2792527F, 0.1919862F);
		this.leg2 = new ModelRenderer(this, 18, 0);
		this.leg2.addBox(-1F, -1F, -1F, 16, 2, 2, par1);
		this.leg2.setRotationPoint(4F, 15F, 2F);
		this.leg2.setTextureSize(128, 64);
		this.leg2.mirror = true;
		this.setRotation(this.leg2, 0F, -0.5759587F, 0.1919862F);
		this.leg7 = new ModelRenderer(this, 18, 0);
		this.leg7.addBox(-15F, -1F, -1F, 16, 2, 2, par1);
		this.leg7.setRotationPoint(-4F, 15F, -1F);
		this.leg7.setTextureSize(128, 64);
		this.leg7.mirror = true;
		this.setRotation(this.leg7, 0F, -0.5759587F, -0.1919862F);
		this.leg5 = new ModelRenderer(this, 18, 0);
		this.leg5.addBox(-15F, -1F, -1F, 16, 2, 2, par1);
		this.leg5.setRotationPoint(-4F, 15F, 0F);
		this.leg5.setTextureSize(128, 64);
		this.leg5.mirror = true;
		this.setRotation(this.leg5, 0F, -0.2792527F, -0.1919862F);
		this.leg3 = new ModelRenderer(this, 18, 0);
		this.leg3.addBox(-15F, -1F, -1F, 16, 2, 2, par1);
		this.leg3.setRotationPoint(-4F, 15F, 1F);
		this.leg3.setTextureSize(128, 64);
		this.leg3.mirror = true;
		this.setRotation(this.leg3, 0F, 0.2792527F, -0.1919862F);
		this.leg1 = new ModelRenderer(this, 18, 0);
		this.leg1.addBox(-15F, -1F, -1F, 16, 2, 2, par1);
		this.leg1.setRotationPoint(-4F, 15F, 2F);
		this.leg1.setTextureSize(128, 64);
		this.leg1.mirror = true;
		this.setRotation(this.leg1, 0F, 0.5759587F, -0.1919862F);
		this.head = new ModelRenderer(this, 32, 4);
		this.head.addBox(-4F, -4F, -8F, 8, 8, 8, par1);
		this.head.setRotationPoint(0F, 15F, -3F);
		this.head.setTextureSize(128, 64);
		this.head.mirror = true;
		this.setRotation(this.head, 0F, 0F, 0F);

		this.oxygenMask = new ModelRenderer(this, 0, 32);
		this.oxygenMask.addBox(-5F, -5F, -9F, 10, 10, 10, par1);
		this.oxygenMask.setRotationPoint(0F, 15F, -3F);
		this.oxygenMask.setTextureSize(128, 64);
		this.oxygenMask.mirror = true;
		this.setRotation(this.oxygenMask, 0F, 0F, 0F);
		this.tank1 = new ModelRenderer(this, 40, 34);
		this.tank1.addBox(1F, -3F, 0F, 3, 3, 7, par1);
		this.tank1.setRotationPoint(0F, 11F, 4F);
		this.tank1.setTextureSize(128, 64);
		this.tank1.mirror = true;
		this.setRotation(this.tank1, 0F, 0F, 0F);
		this.tank2 = new ModelRenderer(this, 40, 34);
		this.tank2.addBox(-4F, -3F, 0F, 3, 3, 7, par1);
		this.tank2.setRotationPoint(0F, 11F, 4F);
		this.tank2.setTextureSize(128, 64);
		this.tank2.mirror = true;
		this.setRotation(this.tank2, 0F, 0F, 0F);
		this.tube1 = new ModelRenderer(this, 40, 32);
		this.tube1.addBox(2F, 0F, -6.5F, 1, 1, 1, par1);
		this.tube1.setRotationPoint(0F, 11F, 4F);
		this.tube1.setTextureSize(128, 64);
		this.tube1.mirror = true;
		this.setRotation(this.tube1, 0F, 0F, 0F);
		this.tube2 = new ModelRenderer(this, 40, 32);
		this.tube2.addBox(2F, -1F, -5.5F, 1, 1, 1, par1);
		this.tube2.setRotationPoint(0F, 11F, 4F);
		this.tube2.setTextureSize(128, 64);
		this.tube2.mirror = true;
		this.setRotation(this.tube2, 0F, 0F, 0F);
		this.tube3 = new ModelRenderer(this, 40, 32);
		this.tube3.addBox(2F, -1F, -4.5F, 1, 1, 1, par1);
		this.tube3.setRotationPoint(0F, 11F, 4F);
		this.tube3.setTextureSize(128, 64);
		this.tube3.mirror = true;
		this.setRotation(this.tube3, 0F, 0F, 0F);
		this.tube4 = new ModelRenderer(this, 40, 32);
		this.tube4.addBox(2F, -2F, -3.5F, 1, 1, 1, par1);
		this.tube4.setRotationPoint(0F, 11F, 4F);
		this.tube4.setTextureSize(128, 64);
		this.tube4.mirror = true;
		this.setRotation(this.tube4, 0F, 0F, 0F);
		this.tube5 = new ModelRenderer(this, 40, 32);
		this.tube5.addBox(2F, -3F, -2.5F, 1, 1, 1, par1);
		this.tube5.setRotationPoint(0F, 11F, 4F);
		this.tube5.setTextureSize(128, 64);
		this.tube5.mirror = true;
		this.setRotation(this.tube5, 0F, 0F, 0F);
		this.tube6 = new ModelRenderer(this, 40, 32);
		this.tube6.addBox(2F, -4F, -2.5F, 1, 1, 1, par1);
		this.tube6.setRotationPoint(0F, 11F, 4F);
		this.tube6.setTextureSize(128, 64);
		this.tube6.mirror = true;
		this.setRotation(this.tube6, 0F, 0F, 0F);
		this.tube7 = new ModelRenderer(this, 40, 32);
		this.tube7.addBox(2F, -5F, -1.5F, 1, 1, 1, par1);
		this.tube7.setRotationPoint(0F, 11F, 4F);
		this.tube7.setTextureSize(128, 64);
		this.tube7.mirror = true;
		this.setRotation(this.tube7, 0F, 0F, 0F);
		this.tube8 = new ModelRenderer(this, 40, 32);
		this.tube8.addBox(2F, -5F, -0.5F, 1, 1, 1, par1);
		this.tube8.setRotationPoint(0F, 11F, 4F);
		this.tube8.setTextureSize(128, 64);
		this.tube8.mirror = true;
		this.setRotation(this.tube8, 0F, 0F, 0F);
		this.tube9 = new ModelRenderer(this, 40, 32);
		this.tube9.addBox(2F, -4F, 0.5F, 1, 1, 1, par1);
		this.tube9.setRotationPoint(0F, 11F, 4F);
		this.tube9.setTextureSize(128, 64);
		this.tube9.mirror = true;
		this.setRotation(this.tube9, 0F, 0F, 0F);
		this.tube10 = new ModelRenderer(this, 40, 32);
		this.tube10.addBox(-3F, 0F, -6.5F, 1, 1, 1, par1);
		this.tube10.setRotationPoint(0F, 11F, 4F);
		this.tube10.setTextureSize(128, 64);
		this.tube10.mirror = true;
		this.setRotation(this.tube10, 0F, 0F, 0F);
		this.tube11 = new ModelRenderer(this, 40, 32);
		this.tube11.addBox(-3F, -1F, -5.5F, 1, 1, 1, par1);
		this.tube11.setRotationPoint(0F, 11F, 4F);
		this.tube11.setTextureSize(128, 64);
		this.tube11.mirror = true;
		this.setRotation(this.tube11, 0F, 0F, 0F);
		this.tube12 = new ModelRenderer(this, 40, 32);
		this.tube12.addBox(-3F, -1F, -4.5F, 1, 1, 1, par1);
		this.tube12.setRotationPoint(0F, 11F, 4F);
		this.tube12.setTextureSize(128, 64);
		this.tube12.mirror = true;
		this.setRotation(this.tube12, 0F, 0F, 0F);
		this.tube13 = new ModelRenderer(this, 40, 32);
		this.tube13.addBox(-3F, -2F, -3.5F, 1, 1, 1, par1);
		this.tube13.setRotationPoint(0F, 11F, 4F);
		this.tube13.setTextureSize(128, 64);
		this.tube13.mirror = true;
		this.setRotation(this.tube13, 0F, 0F, 0F);
		this.tube15 = new ModelRenderer(this, 40, 32);
		this.tube15.addBox(-3F, -4F, -2.5F, 1, 1, 1, par1);
		this.tube15.setRotationPoint(0F, 11F, 4F);
		this.tube15.setTextureSize(128, 64);
		this.tube15.mirror = true;
		this.setRotation(this.tube15, 0F, 0F, 0F);
		this.tube14 = new ModelRenderer(this, 40, 32);
		this.tube14.addBox(-3F, -3F, -2.5F, 1, 1, 1, par1);
		this.tube14.setRotationPoint(0F, 11F, 4F);
		this.tube14.setTextureSize(128, 64);
		this.tube14.mirror = true;
		this.setRotation(this.tube14, 0F, 0F, 0F);
		this.tube16 = new ModelRenderer(this, 40, 32);
		this.tube16.addBox(-3F, -5F, -1.5F, 1, 1, 1, par1);
		this.tube16.setRotationPoint(0F, 11F, 4F);
		this.tube16.setTextureSize(128, 64);
		this.tube16.mirror = true;
		this.setRotation(this.tube16, 0F, 0F, 0F);
		this.tube17 = new ModelRenderer(this, 40, 32);
		this.tube17.addBox(-3F, -5F, -0.5F, 1, 1, 1, par1);
		this.tube17.setRotationPoint(0F, 11F, 4F);
		this.tube17.setTextureSize(128, 64);
		this.tube17.mirror = true;
		this.setRotation(this.tube17, 0F, 0F, 0F);
		this.tube18 = new ModelRenderer(this, 40, 32);
		this.tube18.addBox(-3F, -4F, 0.5F, 1, 1, 1, par1);
		this.tube18.setRotationPoint(0F, 11F, 4F);
		this.tube18.setTextureSize(128, 64);
		this.tube18.mirror = true;
		this.setRotation(this.tube18, 0F, 0F, 0F);
	}

	@Override
	public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5)
	{
		super.render(entity, f, f1, f2, f3, f4, f5);
		this.setRotationAngles(f, f1, f2, f3, f4, f5);
		this.body.render(f5);
		this.rearEnd.render(f5);
		this.leg8.render(f5);
		this.leg6.render(f5);
		this.leg4.render(f5);
		this.leg2.render(f5);
		this.leg7.render(f5);
		this.leg5.render(f5);
		this.leg3.render(f5);
		this.leg1.render(f5);
		this.head.render(f5);
		this.oxygenMask.render(f5);
		this.tank1.render(f5);
		this.tank2.render(f5);
		this.tube1.render(f5);
		this.tube2.render(f5);
		this.tube3.render(f5);
		this.tube4.render(f5);
		this.tube5.render(f5);
		this.tube6.render(f5);
		this.tube7.render(f5);
		this.tube8.render(f5);
		this.tube9.render(f5);
		this.tube10.render(f5);
		this.tube11.render(f5);
		this.tube12.render(f5);
		this.tube13.render(f5);
		this.tube15.render(f5);
		this.tube14.render(f5);
		this.tube16.render(f5);
		this.tube17.render(f5);
		this.tube18.render(f5);
	}

	private void setRotation(ModelRenderer model, float x, float y, float z)
	{
		model.rotateAngleX = x;
		model.rotateAngleY = y;
		model.rotateAngleZ = z;
	}

	public void setRotationAngles(float f, float f1, float f2, float f3, float f4, float f5)
	{
		this.head.rotateAngleY = f3 / (180F / (float) Math.PI);
		this.head.rotateAngleX = f4 / (180F / (float) Math.PI);
		this.oxygenMask.rotateAngleY = f3 / (180F / (float) Math.PI);
		this.oxygenMask.rotateAngleX = f4 / (180F / (float) Math.PI);
		final float var7 = (float) Math.PI / 4F;
		this.leg1.rotateAngleZ = -var7;
		this.leg2.rotateAngleZ = var7;
		this.leg3.rotateAngleZ = -var7 * 0.74F;
		this.leg4.rotateAngleZ = var7 * 0.74F;
		this.leg5.rotateAngleZ = -var7 * 0.74F;
		this.leg6.rotateAngleZ = var7 * 0.74F;
		this.leg7.rotateAngleZ = -var7;
		this.leg8.rotateAngleZ = var7;
		final float var8 = -0.0F;
		final float var9 = 0.3926991F;
		this.leg1.rotateAngleY = var9 * 2.0F + var8;
		this.leg2.rotateAngleY = -var9 * 2.0F - var8;
		this.leg3.rotateAngleY = var9 * 1.0F + var8;
		this.leg4.rotateAngleY = -var9 * 1.0F - var8;
		this.leg5.rotateAngleY = -var9 * 1.0F + var8;
		this.leg6.rotateAngleY = var9 * 1.0F - var8;
		this.leg7.rotateAngleY = -var9 * 2.0F + var8;
		this.leg8.rotateAngleY = var9 * 2.0F - var8;
		final float var10 = -(MathHelper.cos(f * 0.6662F * 2.0F + 0.0F) * 0.4F) * f1;
		final float var11 = -(MathHelper.cos(f * 0.6662F * 2.0F + (float) Math.PI) * 0.4F) * f1;
		final float var12 = -(MathHelper.cos(f * 0.6662F * 2.0F + (float) Math.PI / 2F) * 0.4F) * f1;
		final float var13 = -(MathHelper.cos(f * 0.6662F * 2.0F + (float) Math.PI * 3F / 2F) * 0.4F) * f1;
		final float var14 = Math.abs(MathHelper.sin(f * 0.6662F + 0.0F) * 0.4F) * f1;
		final float var15 = Math.abs(MathHelper.sin(f * 0.6662F + (float) Math.PI) * 0.4F) * f1;
		final float var16 = Math.abs(MathHelper.sin(f * 0.6662F + (float) Math.PI / 2F) * 0.4F) * f1;
		final float var17 = Math.abs(MathHelper.sin(f * 0.6662F + (float) Math.PI * 3F / 2F) * 0.4F) * f1;
		this.leg1.rotateAngleY += var10;
		this.leg2.rotateAngleY += -var10;
		this.leg3.rotateAngleY += var11;
		this.leg4.rotateAngleY += -var11;
		this.leg5.rotateAngleY += var12;
		this.leg6.rotateAngleY += -var12;
		this.leg7.rotateAngleY += var13;
		this.leg8.rotateAngleY += -var13;
		this.leg1.rotateAngleZ += var14;
		this.leg2.rotateAngleZ += -var14;
		this.leg3.rotateAngleZ += var15;
		this.leg4.rotateAngleZ += -var15;
		this.leg5.rotateAngleZ += var16;
		this.leg6.rotateAngleZ += -var16;
		this.leg7.rotateAngleZ += var17;
		this.leg8.rotateAngleZ += -var17;
	}
}
