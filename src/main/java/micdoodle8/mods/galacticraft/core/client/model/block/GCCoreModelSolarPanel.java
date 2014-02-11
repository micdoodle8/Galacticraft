package micdoodle8.mods.galacticraft.core.client.model.block;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

/**
 * GCCoreModelSolarPanel.java
 * 
 * This file is part of the Galacticraft project
 * 
 * @author micdoodle8
 * @license Lesser GNU Public License v3 (http://www.gnu.org/licenses/lgpl.html)
 * 
 */
public class GCCoreModelSolarPanel extends ModelBase
{
	ModelRenderer panelMain;
	ModelRenderer sideHorizontal0;
	ModelRenderer sideVertical0;
	ModelRenderer sideVertical2;
	ModelRenderer sideVertical1;
	ModelRenderer sideHorizontal1;
	ModelRenderer sideHorizontal3;
	ModelRenderer sideHorizontal2;
	ModelRenderer pole;

	public GCCoreModelSolarPanel()
	{
		this(0.0F);
	}

	public GCCoreModelSolarPanel(float var1)
	{
		this.textureWidth = 256;
		this.textureHeight = 128;
		this.panelMain = new ModelRenderer(this, 0, 0);
		this.panelMain.addBox(-23F, -0.5F, -23F, 46, 1, 46);
		this.panelMain.setRotationPoint(0F, 0F, 0F);
		this.panelMain.setTextureSize(256, 128);
		this.panelMain.mirror = true;
		this.setRotation(this.panelMain, 0F, 0F, 0F);
		this.sideHorizontal0 = new ModelRenderer(this, 0, 48);
		this.sideHorizontal0.addBox(-24F, -1.111F, -23F, 1, 1, 46);
		this.sideHorizontal0.setRotationPoint(0F, 0F, 0F);
		this.sideHorizontal0.setTextureSize(256, 128);
		this.sideHorizontal0.mirror = true;
		this.setRotation(this.sideHorizontal0, 0F, 0F, 0F);
		this.sideVertical0 = new ModelRenderer(this, 94, 48);
		this.sideVertical0.addBox(-24F, -1.1F, 23F, 48, 1, 1);
		this.sideVertical0.setRotationPoint(0F, 0F, 0F);
		this.sideVertical0.setTextureSize(256, 128);
		this.sideVertical0.mirror = true;
		this.setRotation(this.sideVertical0, 0F, 0F, 0F);
		this.sideVertical2 = new ModelRenderer(this, 94, 48);
		this.sideVertical2.addBox(-24F, -1.1F, -24F, 48, 1, 1);
		this.sideVertical2.setRotationPoint(0F, 0F, 0F);
		this.sideVertical2.setTextureSize(256, 128);
		this.sideVertical2.mirror = true;
		this.setRotation(this.sideVertical2, 0F, 0F, 0F);
		this.sideVertical1 = new ModelRenderer(this, 94, 48);
		this.sideVertical1.addBox(-24F, -1.1F, -0.5F, 48, 1, 1);
		this.sideVertical1.setRotationPoint(0F, 0F, 0F);
		this.sideVertical1.setTextureSize(256, 128);
		this.sideVertical1.mirror = true;
		this.setRotation(this.sideVertical1, 0F, 0F, 0F);
		this.sideHorizontal1 = new ModelRenderer(this, 0, 48);
		this.sideHorizontal1.addBox(-9F, -1.111F, -23F, 1, 1, 46);
		this.sideHorizontal1.setRotationPoint(0F, 0F, 0F);
		this.sideHorizontal1.setTextureSize(256, 128);
		this.sideHorizontal1.mirror = true;
		this.setRotation(this.sideHorizontal1, 0F, 0F, 0F);
		this.sideHorizontal3 = new ModelRenderer(this, 0, 48);
		this.sideHorizontal3.addBox(23F, -1.111F, -23F, 1, 1, 46);
		this.sideHorizontal3.setRotationPoint(0F, 0F, 0F);
		this.sideHorizontal3.setTextureSize(256, 128);
		this.sideHorizontal3.mirror = true;
		this.setRotation(this.sideHorizontal3, 0F, 0F, 0F);
		this.sideHorizontal2 = new ModelRenderer(this, 0, 48);
		this.sideHorizontal2.addBox(8F, -1.111F, -23F, 1, 1, 46);
		this.sideHorizontal2.setRotationPoint(0F, 0F, 0F);
		this.sideHorizontal2.setTextureSize(256, 128);
		this.sideHorizontal2.mirror = true;
		this.setRotation(this.sideHorizontal2, 0F, 0F, 0F);
		this.pole = new ModelRenderer(this, 94, 50);
		this.pole.addBox(-1.5F, 0.0F, -1.5F, 3, 24, 3);
		this.pole.setRotationPoint(0F, 0F, 0F);
		this.pole.setTextureSize(256, 128);
		this.pole.mirror = true;
		this.setRotation(this.pole, 0F, 0F, 0F);
	}

	private void setRotation(ModelRenderer model, float x, float y, float z)
	{
		model.rotateAngleX = x;
		model.rotateAngleY = y;
		model.rotateAngleZ = z;
	}

	public void renderPanel()
	{
		this.panelMain.render(0.0625F);
		this.sideHorizontal0.render(0.0625F);
		this.sideVertical0.render(0.0625F);
		this.sideVertical2.render(0.0625F);
		this.sideVertical1.render(0.0625F);
		this.sideHorizontal1.render(0.0625F);
		this.sideHorizontal3.render(0.0625F);
		this.sideHorizontal2.render(0.0625F);
	}

	public void renderPole()
	{
		this.pole.render(0.0625F);
	}

	@Override
	public void setRotationAngles(float f, float f1, float f2, float f3, float f4, float f5, Entity e)
	{
		super.setRotationAngles(f, f1, f2, f3, f4, f5, e);
	}
}
