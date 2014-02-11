package micdoodle8.mods.galacticraft.core.client.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.util.MathHelper;

/**
 * GCCoreModelWorm.java
 * 
 * This file is part of the Galacticraft project
 * 
 * @author micdoodle8
 * @license Lesser GNU Public License v3 (http://www.gnu.org/licenses/lgpl.html)
 * 
 */
public class GCCoreModelWorm extends ModelBase
{
	ModelRenderer[] body = new ModelRenderer[9];
	ModelRenderer head;
	ModelRenderer[][] pincers = new ModelRenderer[4][4];

	public GCCoreModelWorm(float f)
	{
		this.textureWidth = 256;
		this.textureHeight = 128;

		this.body[6] = new ModelRenderer(this, 152, 28);
		this.body[6].addBox(-5F, -5F, -122F, 10, 10, 16);
		this.body[6].setRotationPoint(0F, 16F, -66F);
		this.body[6].setTextureSize(256, 128);
		this.body[6].mirror = true;
		this.setRotation(this.body[6], 3.141593F, 0F, -0.6981317F);
		this.body[5] = new ModelRenderer(this, 152, 0);
		this.body[5].addBox(-6F, -6F, -106F, 12, 12, 16);
		this.body[5].setRotationPoint(0F, 16F, -66F);
		this.body[5].setTextureSize(256, 128);
		this.body[5].mirror = true;
		this.setRotation(this.body[5], 3.141593F, 0F, -0.3490659F);
		this.body[4] = new ModelRenderer(this, 88, 96);
		this.body[4].addBox(-8F, -8F, -90F, 16, 16, 16);
		this.body[4].setRotationPoint(0F, 16F, -66F);
		this.body[4].setTextureSize(256, 128);
		this.body[4].mirror = true;
		this.setRotation(this.body[4], 3.141593F, 0F, 0F);
		this.body[2] = new ModelRenderer(this, 88, 32);
		this.body[2].addBox(-8F, -8F, -58F, 16, 16, 16);
		this.body[2].setRotationPoint(0F, 16F, -66F);
		this.body[2].setTextureSize(256, 128);
		this.body[2].mirror = true;
		this.setRotation(this.body[2], 3.141593F, 0F, 0.6981317F);
		this.body[1] = new ModelRenderer(this, 88, 0);
		this.body[1].addBox(-8F, -8F, -42F, 16, 16, 16);
		this.body[1].setRotationPoint(0F, 16F, -66F);
		this.body[1].setTextureSize(256, 128);
		this.body[1].mirror = true;
		this.setRotation(this.body[1], 3.141593F, 0F, 1.047198F);
		this.body[0] = new ModelRenderer(this, 0, 0);
		this.body[0].addBox(-8F, -8F, -26F, 16, 16, 16);
		this.body[0].setRotationPoint(0F, 16F, -66F);
		this.body[0].setTextureSize(256, 128);
		this.body[0].mirror = true;
		this.setRotation(this.body[0], 3.141593F, 0F, 1.396263F);
		this.head = new ModelRenderer(this, 0, 32);
		this.head.addBox(-8F, -8F, -10F, 16, 16, 16);
		this.head.setRotationPoint(0F, 16F, -66F);
		this.head.setTextureSize(256, 128);
		this.head.mirror = true;
		this.setRotation(this.head, 3.141593F, 0F, 0F);
		this.pincers[0][0] = new ModelRenderer(this, 64, 24);
		this.pincers[0][0].addBox(-0.5F, -15F, 9F, 1, 1, 3);
		this.pincers[0][0].setRotationPoint(0F, 16F, -65F);
		this.pincers[0][0].setTextureSize(256, 128);
		this.pincers[0][0].mirror = true;
		this.setRotation(this.pincers[0][0], 3.141593F, 0F, 0F);
		this.pincers[3][3] = new ModelRenderer(this, 64, 0);
		this.pincers[3][3].addBox(-1F, -11F, -4F, 2, 3, 10);
		this.pincers[3][3].setRotationPoint(0F, 16F, -65F);
		this.pincers[3][3].setTextureSize(256, 128);
		this.pincers[3][3].mirror = true;
		this.setRotation(this.pincers[3][3], 3.141593F, 0F, 1.570796F);
		this.pincers[2][3] = new ModelRenderer(this, 64, 13);
		this.pincers[2][3].addBox(-0.5F, -14F, 4F, 1, 3, 3);
		this.pincers[2][3].setRotationPoint(0F, 16F, -65F);
		this.pincers[2][3].setTextureSize(256, 128);
		this.pincers[2][3].mirror = true;
		this.setRotation(this.pincers[2][3], 3.141593F, 0F, 1.570796F);
		this.pincers[1][3] = new ModelRenderer(this, 64, 19);
		this.pincers[1][3].addBox(-0.5F, -15F, 6F, 1, 2, 3);
		this.pincers[1][3].setRotationPoint(0F, 16F, -65F);
		this.pincers[1][3].setTextureSize(256, 128);
		this.pincers[1][3].mirror = true;
		this.setRotation(this.pincers[1][3], 3.141593F, 0F, 1.570796F);
		this.pincers[3][1] = new ModelRenderer(this, 64, 0);
		this.pincers[3][1].addBox(-1F, -11F, -4F, 2, 3, 10);
		this.pincers[3][1].setRotationPoint(0F, 16F, -65F);
		this.pincers[3][1].setTextureSize(256, 128);
		this.pincers[3][1].mirror = true;
		this.setRotation(this.pincers[3][1], 3.141593F, 0F, 4.712389F);
		this.pincers[2][1] = new ModelRenderer(this, 64, 13);
		this.pincers[2][1].addBox(-0.5F, -14F, 4F, 1, 3, 3);
		this.pincers[2][1].setRotationPoint(0F, 16F, -65F);
		this.pincers[2][1].setTextureSize(256, 128);
		this.pincers[2][1].mirror = true;
		this.setRotation(this.pincers[2][1], 3.141593F, 0F, 4.712389F);
		this.pincers[1][1] = new ModelRenderer(this, 64, 19);
		this.pincers[1][1].addBox(-0.5F, -15F, 6F, 1, 2, 3);
		this.pincers[1][1].setRotationPoint(0F, 16F, -65F);
		this.pincers[1][1].setTextureSize(256, 128);
		this.pincers[1][1].mirror = true;
		this.setRotation(this.pincers[1][1], 3.141593F, 0F, 4.712389F);
		this.pincers[0][1] = new ModelRenderer(this, 64, 24);
		this.pincers[0][1].addBox(-0.5F, -15F, 9F, 1, 1, 3);
		this.pincers[0][1].setRotationPoint(0F, 16F, -65F);
		this.pincers[0][1].setTextureSize(256, 128);
		this.pincers[0][1].mirror = true;
		this.setRotation(this.pincers[0][1], 3.141593F, 0F, 4.712389F);
		this.pincers[3][2] = new ModelRenderer(this, 64, 0);
		this.pincers[3][2].addBox(-1F, -11F, -4F, 2, 3, 10);
		this.pincers[3][2].setRotationPoint(0F, 16F, -65F);
		this.pincers[3][2].setTextureSize(256, 128);
		this.pincers[3][2].mirror = true;
		this.setRotation(this.pincers[3][2], 3.141593F, 0F, 3.141593F);
		this.pincers[2][2] = new ModelRenderer(this, 64, 13);
		this.pincers[2][2].addBox(-0.5F, -14F, 4F, 1, 3, 3);
		this.pincers[2][2].setRotationPoint(0F, 16F, -65F);
		this.pincers[2][2].setTextureSize(256, 128);
		this.pincers[2][2].mirror = true;
		this.setRotation(this.pincers[2][2], 3.141593F, 0F, 3.141593F);
		this.pincers[1][2] = new ModelRenderer(this, 64, 19);
		this.pincers[1][2].addBox(-0.5F, -15F, 6F, 1, 2, 3);
		this.pincers[1][2].setRotationPoint(0F, 16F, -65F);
		this.pincers[1][2].setTextureSize(256, 128);
		this.pincers[1][2].mirror = true;
		this.setRotation(this.pincers[1][2], 3.141593F, 0F, 3.141593F);
		this.pincers[0][2] = new ModelRenderer(this, 64, 24);
		this.pincers[0][2].addBox(-0.5F, -15F, 9F, 1, 1, 3);
		this.pincers[0][2].setRotationPoint(0F, 16F, -65F);
		this.pincers[0][2].setTextureSize(256, 128);
		this.pincers[0][2].mirror = true;
		this.setRotation(this.pincers[0][2], 3.141593F, 0F, 3.141593F);
		this.pincers[3][0] = new ModelRenderer(this, 64, 0);
		this.pincers[3][0].addBox(-1F, -11F, -4F, 2, 3, 10);
		this.pincers[3][0].setRotationPoint(0F, 16F, -65F);
		this.pincers[3][0].setTextureSize(256, 128);
		this.pincers[3][0].mirror = true;
		this.setRotation(this.pincers[3][0], 3.141593F, 0F, 0F);
		this.pincers[2][0] = new ModelRenderer(this, 64, 13);
		this.pincers[2][0].addBox(-0.5F, -14F, 4F, 1, 3, 3);
		this.pincers[2][0].setRotationPoint(0F, 16F, -65F);
		this.pincers[2][0].setTextureSize(256, 128);
		this.pincers[2][0].mirror = true;
		this.setRotation(this.pincers[2][0], 3.141593F, 0F, 0F);
		this.pincers[1][0] = new ModelRenderer(this, 64, 19);
		this.pincers[1][0].addBox(-0.5F, -15F, 6F, 1, 2, 3);
		this.pincers[1][0].setRotationPoint(0F, 16F, -65F);
		this.pincers[1][0].setTextureSize(256, 128);
		this.pincers[1][0].mirror = true;
		this.setRotation(this.pincers[1][0], 3.141593F, 0F, 0F);
		this.pincers[0][3] = new ModelRenderer(this, 64, 24);
		this.pincers[0][3].addBox(-0.5F, -15F, 9F, 1, 1, 3);
		this.pincers[0][3].setRotationPoint(0F, 16F, -65F);
		this.pincers[0][3].setTextureSize(256, 128);
		this.pincers[0][3].mirror = true;
		this.setRotation(this.pincers[0][3], 3.141593F, 0F, 1.570796F);
		this.body[3] = new ModelRenderer(this, 88, 64);
		this.body[3].addBox(-8F, -8F, -74F, 16, 16, 16);
		this.body[3].setRotationPoint(0F, 16F, -66F);
		this.body[3].setTextureSize(256, 128);
		this.body[3].mirror = true;
		this.setRotation(this.body[3], 3.141593F, 0F, 0.3490659F);
		this.body[7] = new ModelRenderer(this, 152, 54);
		this.body[7].addBox(-4F, -4F, -154F, 6, 6, 16);
		this.body[7].setRotationPoint(0F, 16F, -66F);
		this.body[7].setTextureSize(256, 128);
		this.body[7].mirror = true;
		this.setRotation(this.body[7], 3.141593F, 0F, -1.396263F);
		this.body[8] = new ModelRenderer(this, 152, 78);
		this.body[8].addBox(-4F, -4F, -138F, 8, 8, 16);
		this.body[8].setRotationPoint(0F, 16F, -66F);
		this.body[8].setTextureSize(256, 128);
		this.body[8].mirror = true;
		this.setRotation(this.body[8], 3.141593F, 0F, -1.047198F);

		// body[8] = new ModelRenderer(this, 88, 78);
		// body[8].addBox(-3F, -3F, -9F, 6, 6, 16, f);
		// body[8].setRotationPoint(0F, 16F, -79F);
		// body[8].setTextureSize(256, 128);
		// body[8].mirror = true;
		// setRotation(body[8], 0F, 0F, 0F);
		// body[6] = new ModelRenderer(this, 88, 28);
		// body[6].addBox(-5F, -5F, -9F, 10, 10, 16, f);
		// body[6].setRotationPoint(0F, 16F, -47F);
		// body[6].setTextureSize(256, 128);
		// body[6].mirror = true;
		// setRotation(body[6], 0F, 0F, -0.6981317F);
		// body[5] = new ModelRenderer(this, 88, 0);
		// body[5].addBox(-6F, -6F, -9F, 12, 12, 16, f);
		// body[5].setRotationPoint(0F, 16F, -31F);
		// body[5].setTextureSize(256, 128);
		// body[5].mirror = true;
		// setRotation(body[5], 0F, 0F, -0.3490659F);
		// body[4] = new ModelRenderer(this, 0, 0);
		// body[4].addBox(-8F, -8F, -9F, 16, 16, 16, f);
		// body[4].setRotationPoint(0F, 16F, -15F);
		// body[4].setTextureSize(256, 128);
		// body[4].mirror = true;
		// setRotation(body[4], 0F, 0F, 0F);
		// body[2] = new ModelRenderer(this, 0, 0);
		// body[2].addBox(-8F, -8F, -9F, 16, 16, 16, f);
		// body[2].setRotationPoint(0F, 16F, 17F);
		// body[2].setTextureSize(256, 128);
		// body[2].mirror = true;
		// setRotation(body[2], 0F, 0F, 0.6981317F);
		// body[1] = new ModelRenderer(this, 0, 0);
		// body[1].addBox(-8F, -8F, -9F, 16, 16, 16, f);
		// body[1].setRotationPoint(0F, 16F, 33F);
		// body[1].setTextureSize(256, 128);
		// body[1].mirror = true;
		// setRotation(body[1], 0F, 0F, 1.047198F);
		// body[0] = new ModelRenderer(this, 0, 0);
		// body[0].addBox(-8F, -8F, -9F, 16, 16, 16, f);
		// body[0].setRotationPoint(0F, 16F, 49F);
		// body[0].setTextureSize(256, 128);
		// body[0].mirror = true;
		// setRotation(body[0], 0F, 0F, 1.396263F);
		// head = new ModelRenderer(this, 0, 32);
		// head.addBox(-8F, -8F, -9F, 16, 16, 16, f);
		// head.setRotationPoint(0F, 16F, 65F);
		// head.setTextureSize(256, 128);
		// head.mirror = true;
		// setRotation(head, 0F, 0F, 0F);
		// pincer1a = new ModelRenderer(this, 64, 24);
		// pincer1a.addBox(-0.5F, -15F, 9F, 1, 1, 3, f);
		// pincer1a.setRotationPoint(0F, 16F, 65F);
		// pincer1a.setTextureSize(256, 128);
		// pincer1a.mirror = true;
		// setRotation(pincer1a, 0F, 0F, 0F);
		// pincer4d = new ModelRenderer(this, 64, 0);
		// pincer4d.addBox(-1F, -11F, -4F, 2, 3, 10, f);
		// pincer4d.setRotationPoint(0F, 16F, 65F);
		// pincer4d.setTextureSize(256, 128);
		// pincer4d.mirror = true;
		// setRotation(pincer4d, 0F, 0F, 1.570796F);
		// pincer3d = new ModelRenderer(this, 64, 13);
		// pincer3d.addBox(-0.5F, -14F, 4F, 1, 3, 3, f);
		// pincer3d.setRotationPoint(0F, 16F, 65F);
		// pincer3d.setTextureSize(256, 128);
		// pincer3d.mirror = true;
		// setRotation(pincer3d, 0F, 0F, 1.570796F);
		// pincer2d = new ModelRenderer(this, 64, 19);
		// pincer2d.addBox(-0.5F, -15F, 6F, 1, 2, 3, f);
		// pincer2d.setRotationPoint(0F, 16F, 65F);
		// pincer2d.setTextureSize(256, 128);
		// pincer2d.mirror = true;
		// setRotation(pincer2d, 0F, 0F, 1.570796F);
		// pincer4b = new ModelRenderer(this, 64, 0);
		// pincer4b.addBox(-1F, -11F, -4F, 2, 3, 10, f);
		// pincer4b.setRotationPoint(0F, 16F, 65F);
		// pincer4b.setTextureSize(256, 128);
		// pincer4b.mirror = true;
		// setRotation(pincer4b, 0F, 0F, 4.712389F);
		// pincer3b = new ModelRenderer(this, 64, 13);
		// pincer3b.addBox(-0.5F, -14F, 4F, 1, 3, 3, f);
		// pincer3b.setRotationPoint(0F, 16F, 65F);
		// pincer3b.setTextureSize(256, 128);
		// pincer3b.mirror = true;
		// setRotation(pincer3b, 0F, 0F, 4.712389F);
		// pincer2b = new ModelRenderer(this, 64, 19);
		// pincer2b.addBox(-0.5F, -15F, 6F, 1, 2, 3, f);
		// pincer2b.setRotationPoint(0F, 16F, 65F);
		// pincer2b.setTextureSize(256, 128);
		// pincer2b.mirror = true;
		// setRotation(pincer2b, 0F, 0F, 4.712389F);
		// pincer1b = new ModelRenderer(this, 64, 24);
		// pincer1b.addBox(-0.5F, -15F, 9F, 1, 1, 3, f);
		// pincer1b.setRotationPoint(0F, 16F, 65F);
		// pincer1b.setTextureSize(256, 128);
		// pincer1b.mirror = true;
		// setRotation(pincer1b, 0F, 0F, 4.712389F);
		// pincer4c = new ModelRenderer(this, 64, 0);
		// pincer4c.addBox(-1F, -11F, -4F, 2, 3, 10, f);
		// pincer4c.setRotationPoint(0F, 16F, 65F);
		// pincer4c.setTextureSize(256, 128);
		// pincer4c.mirror = true;
		// setRotation(pincer4c, 0F, 0F, 3.141593F);
		// pincer3c = new ModelRenderer(this, 64, 13);
		// pincer3c.addBox(-0.5F, -14F, 4F, 1, 3, 3, f);
		// pincer3c.setRotationPoint(0F, 16F, 65F);
		// pincer3c.setTextureSize(256, 128);
		// pincer3c.mirror = true;
		// setRotation(pincer3c, 0F, 0F, 3.141593F);
		// pincer2c = new ModelRenderer(this, 64, 19);
		// pincer2c.addBox(-0.5F, -15F, 6F, 1, 2, 3, f);
		// pincer2c.setRotationPoint(0F, 16F, 65F);
		// pincer2c.setTextureSize(256, 128);
		// pincer2c.mirror = true;
		// setRotation(pincer2c, 0F, 0F, 3.141593F);
		// pincer1c = new ModelRenderer(this, 64, 24);
		// pincer1c.addBox(-0.5F, -15F, 9F, 1, 1, 3, f);
		// pincer1c.setRotationPoint(0F, 16F, 65F);
		// pincer1c.setTextureSize(256, 128);
		// pincer1c.mirror = true;
		// setRotation(pincer1c, 0F, 0F, 3.141593F);
		// pincer4a = new ModelRenderer(this, 64, 0);
		// pincer4a.addBox(-1F, -11F, -4F, 2, 3, 10, f);
		// pincer4a.setRotationPoint(0F, 16F, 65F);
		// pincer4a.setTextureSize(256, 128);
		// pincer4a.mirror = true;
		// setRotation(pincer4a, 0F, 0F, 0F);
		// pincer3a = new ModelRenderer(this, 64, 13);
		// pincer3a.addBox(-0.5F, -14F, 4F, 1, 3, 3, f);
		// pincer3a.setRotationPoint(0F, 16F, 65F);
		// pincer3a.setTextureSize(256, 128);
		// pincer3a.mirror = true;
		// setRotation(pincer3a, 0F, 0F, 0F);
		// pincer2a = new ModelRenderer(this, 64, 19);
		// pincer2a.addBox(-0.5F, -15F, 6F, 1, 2, 3, f);
		// pincer2a.setRotationPoint(0F, 16F, 65F);
		// pincer2a.setTextureSize(256, 128);
		// pincer2a.mirror = true;
		// setRotation(pincer2a, 0F, 0F, 0F);
		// pincer1d = new ModelRenderer(this, 64, 24);
		// pincer1d.addBox(-0.5F, -15F, 9F, 1, 1, 3, f);
		// pincer1d.setRotationPoint(0F, 16F, 65F);
		// pincer1d.setTextureSize(256, 128);
		// pincer1d.mirror = true;
		// setRotation(pincer1d, 0F, 0F, 1.570796F);
		// body[3] = new ModelRenderer(this, 0, 0);
		// body[3].addBox(-8F, -8F, -9F, 16, 16, 16, f);
		// body[3].setRotationPoint(0F, 16F, 1F);
		// body[3].setTextureSize(256, 128);
		// body[3].mirror = true;
		// setRotation(body[3], 0F, 0F, 0.3490659F);
		// body[7] = new ModelRenderer(this, 88, 54);
		// body[7].addBox(-4F, -4F, -9F, 8, 8, 16, f);
		// body[7].setRotationPoint(0F, 16F, -63F);
		// body[7].setTextureSize(256, 128);
		// body[7].mirror = true;
		// setRotation(body[7], 0F, 0F, -1.047198F);
	}

	@Override
	public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5)
	{
		super.render(entity, f, f1, f2, f3, f4, f5);
		this.setRotationAngles(f, f1, f2, f3, f4, f5, entity);

		this.updateBodyRotation(entity, f2);

		for (final ModelRenderer element : this.body)
		{
			element.render(f5);
		}

		for (int i = 0; i < 4; i++)
		{
			for (int j = 0; j < 4; j++)
			{
				this.pincers[i][j].render(f5);
			}
		}

		this.head.render(f5);
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
	}

	private void updateBodyRotation(Entity e, float f)
	{
		final float var8 = 0.03F * (e.entityId % 10) + 0.05F;

		for (int i = 0; i < this.body.length; i++)
		{
			if (i != this.body.length - 1)
			{
				// body[i].rotateAngleY = MathHelper.cos(f * 0.9F + (float)i *
				// 0.15F * (float)Math.PI) * (float)Math.PI * 0.005F * (float)(1
				// + Math.abs(i - 2));
				this.body[i].rotationPointX = MathHelper.sin(f * 0.9F + i * 0.15F * (float) Math.PI) * (float) Math.PI * 1F * Math.abs(i - 2);
			}
			else
			{
				// body[i].rotateAngleY = MathHelper.cos(f * 0.9F + (float)i *
				// 0.15F * (float)Math.PI) * (float)Math.PI * 0.005F * (float)(1
				// + Math.abs(i - 2));
				this.body[i].rotationPointX = MathHelper.sin(f * 0.9F + i * 0.15F * (float) Math.PI) * (float) Math.PI * 0.7F * Math.abs(i - 2);
			}
			this.body[i].rotateAngleZ = e.ticksExisted * var8 - (float) (Math.PI / 15) * i;
		}

		this.head.rotateAngleZ = e.ticksExisted * var8 + (float) (Math.PI / 8) * 1;

		for (int i = 0; i < 4; i++)
		{
			for (int j = 0; j < 4; j++)
			{
				this.pincers[i][j].rotateAngleZ = e.ticksExisted * var8 + (float) (Math.PI / 2) * j;
			}
		}

		for (int i = 0; i < 4; i++)
		{
			this.pincers[i][0].rotateAngleX = MathHelper.sin(e.ticksExisted * var8) * 4.5F * (float) Math.PI / 180.0F - (float) Math.PI;
			this.pincers[i][1].rotateAngleX = MathHelper.sin(e.ticksExisted * var8) * 4.5F * (float) Math.PI / 180.0F - (float) Math.PI;
			this.pincers[i][2].rotateAngleX = MathHelper.sin(e.ticksExisted * var8) * 4.5F * (float) Math.PI / 180.0F - (float) Math.PI;
			this.pincers[i][3].rotateAngleX = MathHelper.sin(e.ticksExisted * var8) * 4.5F * (float) Math.PI / 180.0F - (float) Math.PI;
		}
	}
}
