package micdoodle8.mods.galacticraft.mars.client.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.util.MathHelper;

/**
 * GCMarsModelSlimeling.java
 * 
 * This file is part of the Galacticraft project
 * 
 * @author micdoodle8
 * @license Lesser GNU Public License v3 (http://www.gnu.org/licenses/lgpl.html)
 * 
 */
public class GCMarsModelSlimeling extends ModelBase
{
	ModelRenderer tail3;
	ModelRenderer tail2;
	ModelRenderer tail1;
	ModelRenderer bodyMain;
	ModelRenderer neck;
	ModelRenderer head;

	public GCMarsModelSlimeling(float scale)
	{
		this.textureWidth = 256;
		this.textureHeight = 128;

		this.head = new ModelRenderer(this, 196, 25);
		this.head.addBox(-4.5F, -15.7F, 0.5F, 9, 9, 9);
		this.head.setRotationPoint(0F, 0F, 0F);
		this.head.setTextureSize(256, 128);
		this.head.mirror = true;
		this.setRotation(this.head, 0F, 0F, 0F);
		this.tail3 = new ModelRenderer(this, 0, 25);
		this.tail3.addBox(-3.5F, 1F, -17F, 7, 5, 7);
		this.tail3.setRotationPoint(0F, 0F, 0F);
		this.tail3.setTextureSize(256, 128);
		this.tail3.mirror = true;
		this.setRotation(this.tail3, 0F, 0F, 0F);
		this.tail2 = new ModelRenderer(this, 28, 25);
		this.tail2.addBox(-4.5F, -1F, -15F, 9, 7, 9);
		this.tail2.setRotationPoint(0F, 0F, 0F);
		this.tail2.setTextureSize(256, 128);
		this.tail2.mirror = true;
		this.setRotation(this.tail2, 0F, 0F, 0F);
		this.tail1 = new ModelRenderer(this, 64, 25);
		this.tail1.addBox(-5.5F, -3F, -11F, 11, 9, 10);
		this.tail1.setRotationPoint(0F, 0F, 0F);
		this.tail1.setTextureSize(256, 128);
		this.tail1.mirror = true;
		this.setRotation(this.tail1, 0F, 0F, 0F);
		this.bodyMain = new ModelRenderer(this, 106, 25);
		this.bodyMain.addBox(-6F, -6F, -6F, 12, 12, 12);
		this.bodyMain.setRotationPoint(0F, 0F, 0F);
		this.bodyMain.setTextureSize(256, 128);
		this.bodyMain.mirror = true;
		this.setRotation(this.bodyMain, 0F, 0F, 0F);
		this.neck = new ModelRenderer(this, 154, 25);
		this.neck.addBox(-5.5F, -10.5F, -3F, 11, 11, 10);
		this.neck.setRotationPoint(0F, 0F, 0F);
		this.neck.setTextureSize(256, 128);
		this.neck.mirror = true;
		this.setRotation(this.neck, 0F, 0F, 0F);

		if (scale > 0)
		{
			this.head = new ModelRenderer(this, 156, 0);
			this.head.addBox(-3.5F, -14.7F, 1.5F, 7, 7, 7);
			this.head.setRotationPoint(0F, 0F, 0F);
			this.head.setTextureSize(256, 128);
			this.head.mirror = true;
			this.setRotation(this.head, 0F, 0F, 0F);
			this.neck = new ModelRenderer(this, 122, 0);
			this.neck.addBox(-4.5F, -9.5F, -2F, 9, 9, 8);
			this.neck.setRotationPoint(0F, 0F, 0F);
			this.neck.setTextureSize(256, 128);
			this.neck.mirror = true;
			this.setRotation(this.neck, 0F, 0F, 0F);
			this.bodyMain = new ModelRenderer(this, 82, 0);
			this.bodyMain.addBox(-5F, -5F, -5F, 10, 10, 10);
			this.bodyMain.setRotationPoint(0F, 0F, 0F);
			this.bodyMain.setTextureSize(256, 128);
			this.bodyMain.mirror = true;
			this.setRotation(this.bodyMain, 0F, 0F, 0F);
			this.tail1 = new ModelRenderer(this, 48, 0);
			this.tail1.addBox(-4.5F, -2F, -10F, 9, 7, 8);
			this.tail1.setRotationPoint(0F, 0F, 0F);
			this.tail1.setTextureSize(256, 128);
			this.tail1.mirror = true;
			this.setRotation(this.tail1, 0F, 0F, 0F);
			this.tail2 = new ModelRenderer(this, 20, 0);
			this.tail2.addBox(-3.5F, 0F, -14F, 7, 5, 7);
			this.tail2.setRotationPoint(0F, 0F, 0F);
			this.tail2.setTextureSize(256, 128);
			this.tail2.mirror = true;
			this.setRotation(this.tail2, 0F, 0F, 0F);
			this.tail3 = new ModelRenderer(this, 0, 0);
			this.tail3.addBox(-2.5F, 2F, -16F, 5, 3, 5);
			this.tail3.setRotationPoint(0F, 0F, 0F);
			this.tail3.setTextureSize(256, 128);
			this.tail3.mirror = true;
			this.setRotation(this.tail3, 0F, 0F, 0F);
		}

		this.bodyMain.addChild(this.tail1);
		this.neck.addChild(this.head);
		this.tail1.addChild(this.tail2);
		this.tail2.addChild(this.tail3);
		this.bodyMain.addChild(this.neck);
	}

	@Override
	public void render(Entity entity, float par2, float par3, float par4, float par5, float par6, float par7)
	{
		super.render(entity, par2, par3, par4, par5, par6, par7);
		this.setRotationAngles(par2, par3, par4, par5, par6, par7, entity);
		this.bodyMain.render(par7);
	}

	private void setRotation(ModelRenderer model, float x, float y, float z)
	{
		model.rotateAngleX = x;
		model.rotateAngleY = y;
		model.rotateAngleZ = z;
	}

	@Override
	public void setRotationAngles(float par1, float par2, float par3, float par4, float par5, float par6, Entity par7Entity)
	{
		this.tail1.rotateAngleY = MathHelper.cos(par1 * 0.6662F) * 0.2F * par2;
		this.tail2.rotateAngleY = MathHelper.cos(par1 * 0.6662F) * 0.2F * par2;
		this.tail3.rotateAngleY = MathHelper.cos(par1 * 0.6662F) * 0.2F * par2;
		this.tail1.offsetZ = MathHelper.cos(0.5F * par1 * 0.6662F) * 0.2F * par2;
		this.tail2.offsetZ = MathHelper.cos(0.5F * par1 * 0.6662F) * 0.2F * par2;
		this.tail3.offsetZ = MathHelper.cos(0.5F * par1 * 0.6662F) * 0.2F * par2;
		this.neck.offsetZ = -MathHelper.cos(0.5F * par1 * 0.6662F) * 0.1F * par2;
		this.head.offsetZ = -MathHelper.cos(0.5F * par1 * 0.6662F) * 0.1F * par2;
	}
}
