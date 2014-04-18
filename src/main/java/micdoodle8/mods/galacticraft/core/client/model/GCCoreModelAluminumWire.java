package micdoodle8.mods.galacticraft.core.client.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

/**
 * GCCoreModelAluminumWire.java
 * 
 * This file is part of the Galacticraft project
 * 
 * @author micdoodle8
 * @license Lesser GNU Public License v3 (http://www.gnu.org/licenses/lgpl.html)
 * 
 */
@SideOnly(Side.CLIENT)
public class GCCoreModelAluminumWire extends ModelBase
{
	// fields
	ModelRenderer Middle;
	ModelRenderer Right;
	ModelRenderer Left;
	ModelRenderer Back;
	ModelRenderer Front;
	ModelRenderer Top;
	ModelRenderer Bottom;

	public GCCoreModelAluminumWire()
	{
		this.textureWidth = 64;
		this.textureHeight = 32;
		this.Middle = new ModelRenderer(this, 0, 0);
		this.Middle.addBox(-1F, -1F, -1F, 4, 4, 4);
		this.Middle.setRotationPoint(-1F, 15F, -1F);
		this.Middle.setTextureSize(64, 32);
		this.Middle.mirror = true;
		this.setRotation(this.Middle, 0F, 0F, 0F);
		this.Right = new ModelRenderer(this, 21, 0);
		this.Right.addBox(0F, 0F, 0F, 6, 4, 4);
		this.Right.setRotationPoint(2F, 14F, -2F);
		this.Right.setTextureSize(64, 32);
		this.Right.mirror = true;
		this.setRotation(this.Right, 0F, 0F, 0F);
		this.Left = new ModelRenderer(this, 21, 0);
		this.Left.addBox(0F, 0F, 0F, 6, 4, 4);
		this.Left.setRotationPoint(-8F, 14F, -2F);
		this.Left.setTextureSize(64, 32);
		this.Left.mirror = true;
		this.setRotation(this.Left, 0F, 0F, 0F);
		this.Back = new ModelRenderer(this, 0, 11);
		this.Back.addBox(0F, 0F, 0F, 4, 4, 6);
		this.Back.setRotationPoint(-2F, 14F, 2F);
		this.Back.setTextureSize(64, 32);
		this.Back.mirror = true;
		this.setRotation(this.Back, 0F, 0F, 0F);
		this.Front = new ModelRenderer(this, 0, 11);
		this.Front.addBox(0F, 0F, 0F, 4, 4, 6);
		this.Front.setRotationPoint(-2F, 14F, -8F);
		this.Front.setTextureSize(64, 32);
		this.Front.mirror = true;
		this.setRotation(this.Front, 0F, 0F, 0F);
		this.Top = new ModelRenderer(this, 21, 11);
		this.Top.addBox(0F, 0F, 0F, 4, 6, 4);
		this.Top.setRotationPoint(-2F, 8F, -2F);
		this.Top.setTextureSize(64, 32);
		this.Top.mirror = true;
		this.setRotation(this.Top, 0F, 0F, 0F);
		this.Bottom = new ModelRenderer(this, 21, 11);
		this.Bottom.addBox(0F, 0F, 0F, 4, 6, 4);
		this.Bottom.setRotationPoint(-2F, 18F, -2F);
		this.Bottom.setTextureSize(64, 32);
		this.Bottom.mirror = true;
		this.setRotation(this.Bottom, 0F, 0F, 0F);
	}

	@Override
	public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5)
	{
		super.render(entity, f, f1, f2, f3, f4, f5);
		this.setRotationAngles(f, f1, f2, f3, f4, f5, entity);
		this.renderMiddle();
		this.renderBottom();
		this.renderTop();
		this.renderLeft();
		this.renderRight();
		this.renderBack();
		this.renderFront();
	}

	public void renderMiddle()
	{
		this.Middle.render(0.0625F);
	}

	public void renderBottom()
	{
		this.Bottom.render(0.0625F);
	}

	public void renderTop()
	{
		this.Top.render(0.0625F);
	}

	public void renderLeft()
	{
		this.Left.render(0.0625F);
	}

	public void renderRight()
	{
		this.Right.render(0.0625F);
	}

	public void renderBack()
	{
		this.Back.render(0.0625F);
	}

	public void renderFront()
	{
		// this.Front.render(0.0625F);
	}

	private void setRotation(ModelRenderer model, float x, float y, float z)
	{
		model.rotateAngleX = x;
		model.rotateAngleY = y;
		model.rotateAngleZ = z;
	}

	@Override
	public void setRotationAngles(float x, float y, float z, float f3, float f4, float f5, Entity entity)
	{
		super.setRotationAngles(x, y, z, f3, f4, f5, entity);
	}
}
