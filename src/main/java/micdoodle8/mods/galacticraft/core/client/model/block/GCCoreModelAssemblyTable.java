package micdoodle8.mods.galacticraft.core.client.model.block;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

/**
 * GCCoreModelAssemblyTable.java
 * 
 * This file is part of the Galacticraft project
 * 
 * @author micdoodle8
 * @license Lesser GNU Public License v3 (http://www.gnu.org/licenses/lgpl.html)
 * 
 */
public class GCCoreModelAssemblyTable extends ModelBase
{
	ModelRenderer base1a;
	ModelRenderer baseAux1;
	ModelRenderer base1b;
	ModelRenderer base2a;
	ModelRenderer base2b;
	ModelRenderer baseAux3;
	ModelRenderer baseAux2;
	ModelRenderer clawL1;
	ModelRenderer clawL2;
	ModelRenderer clawR1;
	ModelRenderer clawR2;
	ModelRenderer clawRPR;
	ModelRenderer clawRPL;
	ModelRenderer armB1;
	ModelRenderer manipulatorRotationPointB;
	ModelRenderer baseRotationPointB;
	ModelRenderer armRotationPointB;
	ModelRenderer armB2;
	ModelRenderer armR1;
	ModelRenderer armR2;
	ModelRenderer baseRotationPointR;
	ModelRenderer armRotationPointR;
	ModelRenderer weldHead;
	ModelRenderer weldBit;
	ModelRenderer screenRotationPointF;
	ModelRenderer armF1;
	ModelRenderer baseRotationPointF;
	ModelRenderer Screen;
	ModelRenderer armRotationPointL;
	ModelRenderer armL2;
	ModelRenderer baseRotationPointL;
	ModelRenderer armL1;
	ModelRenderer sensorDish;
	ModelRenderer sensor;

	public GCCoreModelAssemblyTable()
	{
		this(0.0F);
	}

	public GCCoreModelAssemblyTable(float var1)
	{
		this.textureWidth = 256;
		this.textureHeight = 128;
		this.base1a = new ModelRenderer(this, 113, 24);
		this.base1a.addBox(-2.5F, -2F, -7.5F, 5, 2, 15);
		this.base1a.setRotationPoint(0F, 24F, 0F);
		this.base1a.setTextureSize(256, 128);
		this.base1a.mirror = true;
		this.setRotation(this.base1a, 0F, 1.570796F, 0F);
		this.baseAux1 = new ModelRenderer(this, 35, 40);
		this.baseAux1.addBox(-3.5F, 0F, -3.5F, 7, 1, 7);
		this.baseAux1.setRotationPoint(0F, 20F, 0F);
		this.baseAux1.setTextureSize(256, 128);
		this.baseAux1.mirror = true;
		this.setRotation(this.baseAux1, 0F, 0.7853982F, 0F);
		this.base1b = new ModelRenderer(this, 113, 24);
		this.base1b.addBox(-2.5F, -2F, -7.5F, 5, 2, 15);
		this.base1b.setRotationPoint(0F, 24F, 0F);
		this.base1b.setTextureSize(256, 128);
		this.base1b.mirror = true;
		this.setRotation(this.base1b, 0F, 0F, 0F);
		this.base2a = new ModelRenderer(this, 65, 0);
		this.base2a.addBox(-3.5F, -2F, -7F, 7, 5, 14);
		this.base2a.setRotationPoint(0F, 23F, 0F);
		this.base2a.setTextureSize(256, 128);
		this.base2a.mirror = true;
		this.setRotation(this.base2a, 0F, -0.7853982F, 0F);
		this.base2b = new ModelRenderer(this, 65, 0);
		this.base2b.addBox(-3.5F, -2F, -7F, 7, 3, 14);
		this.base2b.setRotationPoint(0F, 23F, 0F);
		this.base2b.setTextureSize(256, 128);
		this.base2b.mirror = true;
		this.setRotation(this.base2b, 0F, 0.7853982F, 0F);
		this.baseAux3 = new ModelRenderer(this, 50, 62);
		this.baseAux3.addBox(-8.5F, 0F, -0.5F, 17, 4, 1);
		this.baseAux3.setRotationPoint(0F, 20.5F, 0F);
		this.baseAux3.setTextureSize(256, 128);
		this.baseAux3.mirror = true;
		this.setRotation(this.baseAux3, 0F, -0.7853982F, 0F);
		this.baseAux2 = new ModelRenderer(this, 50, 62);
		this.baseAux2.addBox(-8.5F, 0F, -0.5F, 17, 4, 1);
		this.baseAux2.setRotationPoint(0F, 20.5F, 0F);
		this.baseAux2.setTextureSize(256, 128);
		this.baseAux2.mirror = true;
		this.setRotation(this.baseAux2, 0F, 0.7853982F, 0F);
		this.clawL1 = new ModelRenderer(this, 7, 57);
		this.clawL1.addBox(-0.5F, -1F, -3F, 1, 2, 3);
		this.clawL1.setRotationPoint(-1F, -0.2F, 4F);
		this.clawL1.setTextureSize(256, 128);
		this.clawL1.mirror = true;
		this.setRotation(this.clawL1, 0F, 1.003826F, 0F);
		this.clawL2 = new ModelRenderer(this, 7, 57);
		this.clawL2.addBox(-2.7F, -1F, -5F, 1, 2, 3);
		this.clawL2.setRotationPoint(-1F, -0.2F, 4F);
		this.clawL2.setTextureSize(256, 128);
		this.clawL2.mirror = true;
		this.setRotation(this.clawL2, 0F, 0.1698892F, 0F);
		this.clawR1 = new ModelRenderer(this, 7, 57);
		this.clawR1.addBox(-0.5F, -1F, -3F, 1, 2, 3);
		this.clawR1.setRotationPoint(1F, -0.2F, 4F);
		this.clawR1.setTextureSize(256, 128);
		this.clawR1.mirror = true;
		this.setRotation(this.clawR1, 0F, -1.041005F, 0F);
		this.clawR2 = new ModelRenderer(this, 7, 57);
		this.clawR2.addBox(1.7F, -1F, -5F, 1, 2, 3);
		this.clawR2.setRotationPoint(1F, -0.2F, 4F);
		this.clawR2.setTextureSize(256, 128);
		this.clawR2.mirror = true;
		this.setRotation(this.clawR2, 0F, -0.1896157F, 0F);
		this.clawRPR = new ModelRenderer(this, 0, 45);
		this.clawRPR.addBox(-2.2F, -1.5F, -3F, 1, 3, 1);
		this.clawRPR.setRotationPoint(1F, -0.2F, 4F);
		this.clawRPR.setTextureSize(256, 128);
		this.clawRPR.mirror = true;
		this.setRotation(this.clawRPR, 0F, -1.63514F, 0F);
		this.clawRPL = new ModelRenderer(this, 0, 45);
		this.clawRPL.addBox(-2.2F, -1.5F, -3F, 1, 3, 1);
		this.clawRPL.setRotationPoint(-1F, -0.2F, 4F);
		this.clawRPL.setTextureSize(256, 128);
		this.clawRPL.mirror = true;
		this.setRotation(this.clawRPL, 0F, 0.4096913F, 0F);
		this.armB1 = new ModelRenderer(this, 46, 0);
		this.armB1.addBox(-1F, -12F, -1F, 2, 12, 2);
		this.armB1.setRotationPoint(0F, 22F, 8F);
		this.armB1.setTextureSize(256, 128);
		this.armB1.mirror = true;
		this.setRotation(this.armB1, -0.3005282F, 0F, 0F);
		this.manipulatorRotationPointB = new ModelRenderer(this, 0, 69);
		this.manipulatorRotationPointB.addBox(-1.5F, -1.5F, -1.5F, 3, 3, 3);
		this.manipulatorRotationPointB.setRotationPoint(0F, 0F, 4.5F);
		this.manipulatorRotationPointB.setTextureSize(256, 128);
		this.manipulatorRotationPointB.mirror = true;
		this.setRotation(this.manipulatorRotationPointB, 0F, 1.570796F, 0F);
		this.baseRotationPointB = new ModelRenderer(this, 0, 69);
		this.baseRotationPointB.addBox(-1.5F, -1.2F, -1.5F, 3, 3, 3);
		this.baseRotationPointB.setRotationPoint(0F, 22F, 7.5F);
		this.baseRotationPointB.setTextureSize(256, 128);
		this.baseRotationPointB.mirror = true;
		this.setRotation(this.baseRotationPointB, this.toRadians(0), this.toRadians(90), this.toRadians(15));
		this.armRotationPointB = new ModelRenderer(this, 0, 69);
		this.armRotationPointB.addBox(-1.5F, -1.5F, -1.5F, 3, 3, 3);
		this.armRotationPointB.setRotationPoint(0F, 10F, 11.5F);
		this.armRotationPointB.setTextureSize(256, 128);
		this.armRotationPointB.mirror = true;
		this.setRotation(this.armRotationPointB, this.toRadians(90), this.toRadians(0), this.toRadians(-90));
		this.armB2 = new ModelRenderer(this, 46, 0);
		this.armB2.addBox(-1F, -12F, -1F, 2, 12, 2);
		this.armB2.setRotationPoint(0F, 9F, 11F);
		this.armB2.setTextureSize(256, 128);
		this.armB2.mirror = true;
		this.setRotation(this.armB2, 0.6289468F, 0F, 0F);
		this.armR1 = new ModelRenderer(this, 46, 0);
		this.armR1.addBox(-1F, -12F, -1F, 2, 12, 2);
		this.armR1.setRotationPoint(-8F, 22F, 0F);
		this.armR1.setTextureSize(256, 128);
		this.armR1.mirror = true;
		this.setRotation(this.armR1, 0.6351428F, 1.570796F, 0F);
		this.armR2 = new ModelRenderer(this, 55, 0);
		this.armR2.addBox(-1F, -8F, -1F, 2, 8, 2);
		this.armR2.setRotationPoint(-15F, 11F, 0F);
		this.armR2.setTextureSize(256, 128);
		this.armR2.mirror = true;
		this.setRotation(this.armR2, -0.9635439F, 1.570796F, 0F);
		this.baseRotationPointR = new ModelRenderer(this, 0, 69);
		this.baseRotationPointR.addBox(-1.5F, -1.5F, -1.5F, 3, 3, 3);
		this.baseRotationPointR.setRotationPoint(-7.9F, 22F, 0F);
		this.baseRotationPointR.setTextureSize(256, 128);
		this.baseRotationPointR.mirror = true;
		this.setRotation(this.baseRotationPointR, 0F, 0F, 0.5235988F);
		this.armRotationPointR = new ModelRenderer(this, 0, 69);
		this.armRotationPointR.addBox(-1.5F, -1.5F, -1.5F, 3, 3, 3);
		this.armRotationPointR.setRotationPoint(-15.76667F, 11.5F, 0F);
		this.armRotationPointR.setTextureSize(256, 128);
		this.armRotationPointR.mirror = true;
		this.setRotation(this.armRotationPointR, 0F, 0F, -0.1745329F);
		this.weldHead = new ModelRenderer(this, 17, 0);
		this.weldHead.addBox(-2F, -1.5F, -1.5F, 5, 3, 3);
		this.weldHead.setRotationPoint(-9F, 6F, 0F);
		this.weldHead.setTextureSize(256, 128);
		this.weldHead.mirror = true;
		this.setRotation(this.weldHead, 0F, 0F, 0.5948578F);
		this.weldBit = new ModelRenderer(this, 0, 0);
		this.weldBit.addBox(0F, -0.5F, -0.5F, 7, 1, 1);
		this.weldBit.setRotationPoint(-9F, 6F, 0F);
		this.weldBit.setTextureSize(256, 128);
		this.weldBit.mirror = true;
		this.setRotation(this.weldBit, 0F, 0F, 0.5948606F);
		this.screenRotationPointF = new ModelRenderer(this, 0, 77);
		this.screenRotationPointF.addBox(-1.5F, -1.5F, -1.5F, 3, 2, 3);
		this.screenRotationPointF.setRotationPoint(0F, 14F, -9F);
		this.screenRotationPointF.setTextureSize(256, 128);
		this.screenRotationPointF.mirror = true;
		this.setRotation(this.screenRotationPointF, this.toRadians(30), this.toRadians(0), this.toRadians(0));
		this.armF1 = new ModelRenderer(this, 55, 0);
		this.armF1.addBox(-1F, -8F, -1F, 2, 8, 2);
		this.armF1.setRotationPoint(0F, 21F, -7.5F);
		this.armF1.setTextureSize(256, 128);
		this.armF1.mirror = true;
		this.setRotation(this.armF1, 0.2602503F, 0F, 0F);
		this.baseRotationPointF = new ModelRenderer(this, 0, 69);
		this.baseRotationPointF.addBox(-1.5F, -1.5F, -1.5F, 3, 3, 3);
		this.baseRotationPointF.setRotationPoint(0F, 21.6F, -6.8F);
		this.baseRotationPointF.setTextureSize(256, 128);
		this.baseRotationPointF.mirror = true;
		this.setRotation(this.baseRotationPointF, this.toRadians(30F), this.toRadians(0), this.toRadians(0));
		this.Screen = new ModelRenderer(this, 10, 45);
		this.Screen.addBox(-3.5F, -2.5F, -1F, 7, 5, 1);
		this.Screen.setRotationPoint(0F, 13F, -10F);
		this.Screen.setTextureSize(256, 128);
		this.Screen.mirror = true;
		this.setRotation(this.Screen, -1.047198F, 0F, 0F);
		this.armRotationPointL = new ModelRenderer(this, 0, 69);
		this.armRotationPointL.addBox(-1.5F, -1.5F, -1.5F, 3, 3, 3);
		this.armRotationPointL.setRotationPoint(13.8F, 18.4F, 0F);
		this.armRotationPointL.setTextureSize(256, 128);
		this.armRotationPointL.mirror = true;
		this.setRotation(this.armRotationPointL, 0F, -3.141593F, 0.5235988F);
		this.armL2 = new ModelRenderer(this, 55, 0);
		this.armL2.addBox(-1F, -8F, -1F, 2, 8, 2);
		this.armL2.setRotationPoint(13.7F, 18F, 0F);
		this.armL2.setTextureSize(256, 128);
		this.armL2.mirror = true;
		this.setRotation(this.armL2, 0.6351428F, 1.570796F, 0F);
		this.baseRotationPointL = new ModelRenderer(this, 0, 69);
		this.baseRotationPointL.addBox(-1.5F, -1.5F, -1.5F, 3, 3, 3);
		this.baseRotationPointL.setRotationPoint(7.5F, 22F, 0F);
		this.baseRotationPointL.setTextureSize(256, 128);
		this.baseRotationPointL.mirror = true;
		this.setRotation(this.baseRotationPointL, 0F, -3.141593F, 0.5235988F);
		this.armL1 = new ModelRenderer(this, 55, 0);
		this.armL1.addBox(-1F, -8F, -1F, 2, 8, 2);
		this.armL1.setRotationPoint(8F, 22F, 0F);
		this.armL1.setTextureSize(256, 128);
		this.armL1.mirror = true;
		this.setRotation(this.armL1, -1.037895F, 1.570796F, 0F);
		this.sensorDish = new ModelRenderer(this, 68, 41);
		this.sensorDish.addBox(-1F, -2F, -2F, 1, 4, 4);
		this.sensorDish.setRotationPoint(6F, 12F, 0F);
		this.sensorDish.setTextureSize(256, 128);
		this.sensorDish.mirror = true;
		this.setRotation(this.sensorDish, 0F, 0F, -0.3005282F);
		this.sensor = new ModelRenderer(this, 60, 54);
		this.sensor.addBox(-3F, -2F, -1F, 5, 2, 2);
		this.sensor.setRotationPoint(9F, 12F, 0F);
		this.sensor.setTextureSize(256, 128);
		this.sensor.mirror = true;
		this.setRotation(this.sensor, 0F, 0F, -0.3005353F);
	}

	private void setRotation(ModelRenderer model, float x, float y, float z)
	{
		model.rotateAngleX = x;
		model.rotateAngleY = y;
		model.rotateAngleZ = z;
	}

	public void renderAll()
	{
		this.base1a.render(0.0625F);
		this.baseAux1.render(0.0625F);
		this.base1b.render(0.0625F);
		this.base2a.render(0.0625F);
		this.base2b.render(0.0625F);
		this.baseAux3.render(0.0625F);
		this.baseAux2.render(0.0625F);
		this.clawL1.render(0.0625F);
		this.clawL2.render(0.0625F);
		this.clawR1.render(0.0625F);
		this.clawR2.render(0.0625F);
		this.clawRPR.render(0.0625F);
		this.clawRPL.render(0.0625F);
		this.armB1.render(0.0625F);
		this.manipulatorRotationPointB.render(0.0625F);
		this.baseRotationPointB.render(0.0625F);
		this.armRotationPointB.render(0.0625F);
		this.armB2.render(0.0625F);
		this.armR1.render(0.0625F);
		this.armR2.render(0.0625F);
		this.baseRotationPointR.render(0.0625F);
		this.armRotationPointR.render(0.0625F);
		this.weldHead.render(0.0625F);
		this.weldBit.render(0.0625F);
		this.screenRotationPointF.render(0.0625F);
		this.armF1.render(0.0625F);
		this.baseRotationPointF.render(0.0625F);
		this.Screen.render(0.0625F);
		this.armRotationPointL.render(0.0625F);
		this.armL2.render(0.0625F);
		this.baseRotationPointL.render(0.0625F);
		this.armL1.render(0.0625F);
		this.sensorDish.render(0.0625F);
		this.sensor.render(0.0625F);
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
