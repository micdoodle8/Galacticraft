package micdoodle8.mods.galacticraft.mars.client.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.util.MathHelper;

/**
 * GCMarsModelSludgeling.java
 * 
 * This file is part of the Galacticraft project
 * 
 * @author micdoodle8
 * @license Lesser GNU Public License v3 (http://www.gnu.org/licenses/lgpl.html)
 * 
 */
public class GCMarsModelSludgeling extends ModelBase
{
	ModelRenderer tail4;
	ModelRenderer body;
	ModelRenderer tail1;
	ModelRenderer tail2;
	ModelRenderer tail3;

	public GCMarsModelSludgeling()
	{
		this.textureWidth = 64;
		this.textureHeight = 32;

		this.tail4 = new ModelRenderer(this, 0, 0);
		this.tail4.addBox(-0.5F, 0.3F, 4.5F, 1, 1, 1);
		this.tail4.setRotationPoint(0F, 23.5F, -2F);
		this.tail4.setTextureSize(64, 32);
		this.tail4.mirror = true;
		this.setRotation(this.tail4, 0F, 0F, 0F);
		this.body = new ModelRenderer(this, 4, 0);
		this.body.addBox(-1F, -0.5F, -1.5F, 2, 1, 3);
		this.body.setRotationPoint(0F, 23.5F, -2F);
		this.body.setTextureSize(64, 32);
		this.body.mirror = true;
		this.setRotation(this.body, 0F, 0F, 0F);
		this.tail1 = new ModelRenderer(this, 0, 0);
		this.tail1.addBox(-0.5F, -0.3F, 1.5F, 1, 1, 1);
		this.tail1.setRotationPoint(0F, 23.5F, -2F);
		this.tail1.setTextureSize(64, 32);
		this.tail1.mirror = true;
		this.setRotation(this.tail1, 0F, 0F, 0F);
		this.tail2 = new ModelRenderer(this, 0, 0);
		this.tail2.addBox(-0.5F, -0.1F, 2.5F, 1, 1, 1);
		this.tail2.setRotationPoint(0F, 23.5F, -2F);
		this.tail2.setTextureSize(64, 32);
		this.tail2.mirror = true;
		this.setRotation(this.tail2, 0F, 0F, 0F);
		this.tail3 = new ModelRenderer(this, 0, 0);
		this.tail3.addBox(-0.5F, 0.1F, 3.5F, 1, 1, 1);
		this.tail3.setRotationPoint(0F, 23.5F, -2F);
		this.tail3.setTextureSize(64, 32);
		this.tail3.mirror = true;
		this.setRotation(this.tail3, 0F, 0F, 0F);
	}

	@Override
	public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5)
	{
		super.render(entity, f, f1, f2, f3, f4, f5);
		this.setRotationAngles(f, f1, f2, f3, f4, f5, entity);
		this.body.render(f5);
		this.tail1.render(f5);
		this.tail2.render(f5);
		this.tail3.render(f5);
		this.tail4.render(f5);
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
		this.tail1.rotateAngleY = MathHelper.cos(f2 * 0.3F + 0 * 0.15F * (float) Math.PI) * (float) Math.PI * 0.025F * (1 + Math.abs(0 - 2));
		this.tail2.rotateAngleY = MathHelper.cos(f2 * 0.3F + 1 * 0.15F * (float) Math.PI) * (float) Math.PI * 0.025F * (1 + Math.abs(1 - 2));
		this.tail3.rotateAngleY = MathHelper.cos(f2 * 0.3F + 2 * 0.15F * (float) Math.PI) * (float) Math.PI * 0.025F * (1 + Math.abs(1 - 2));
		this.tail4.rotateAngleY = MathHelper.cos(f2 * 0.3F + 3 * 0.15F * (float) Math.PI) * (float) Math.PI * 0.025F * (1 + Math.abs(1 - 2));
	}
}
