package micdoodle8.mods.galacticraft.client;

import net.minecraft.src.Entity;
import net.minecraft.src.MathHelper;
import net.minecraft.src.ModelBase;
import net.minecraft.src.ModelRenderer;

public class GCModelSludgeling extends ModelBase
{
	ModelRenderer tail4;
	ModelRenderer body;
	ModelRenderer tail1;
	ModelRenderer tail2;
	ModelRenderer tail3;

	public GCModelSludgeling()
	{
		textureWidth = 64;
		textureHeight = 32;

		tail4 = new ModelRenderer(this, 0, 0);
		tail4.addBox(-0.5F, 0.3F, 4.5F, 1, 1, 1);
		tail4.setRotationPoint(0F, 23.5F, -2F);
		tail4.setTextureSize(64, 32);
		tail4.mirror = true;
		setRotation(tail4, 0F, 0F, 0F);
		body = new ModelRenderer(this, 4, 0);
		body.addBox(-1F, -0.5F, -1.5F, 2, 1, 3);
		body.setRotationPoint(0F, 23.5F, -2F);
		body.setTextureSize(64, 32);
		body.mirror = true;
		setRotation(body, 0F, 0F, 0F);
		tail1 = new ModelRenderer(this, 0, 0);
		tail1.addBox(-0.5F, -0.3F, 1.5F, 1, 1, 1);
		tail1.setRotationPoint(0F, 23.5F, -2F);
		tail1.setTextureSize(64, 32);
		tail1.mirror = true;
		setRotation(tail1, 0F, 0F, 0F);
		tail2 = new ModelRenderer(this, 0, 0);
		tail2.addBox(-0.5F, -0.1F, 2.5F, 1, 1, 1);
		tail2.setRotationPoint(0F, 23.5F, -2F);
		tail2.setTextureSize(64, 32);
		tail2.mirror = true;
		setRotation(tail2, 0F, 0F, 0F);
		tail3 = new ModelRenderer(this, 0, 0);
		tail3.addBox(-0.5F, 0.1F, 3.5F, 1, 1, 1);
		tail3.setRotationPoint(0F, 23.5F, -2F);
		tail3.setTextureSize(64, 32);
		tail3.mirror = true;
		setRotation(tail3, 0F, 0F, 0F);
	}

	public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5)
	{
		super.render(entity, f, f1, f2, f3, f4, f5);
		setRotationAngles(f, f1, f2, f3, f4, f5);
		body.render(f5);
		tail1.render(f5);
		tail2.render(f5);
		tail3.render(f5);
		tail4.render(f5);
	}

	private void setRotation(ModelRenderer model, float x, float y, float z)
	{
		model.rotateAngleX = x;
		model.rotateAngleY = y;
		model.rotateAngleZ = z;
	}

	public void setRotationAngles(float f, float f1, float f2, float f3, float f4, float f5)
	{
        this.tail1.rotateAngleY = MathHelper.cos(f2 * 0.3F + (float)0 * 0.15F * (float)Math.PI) * (float)Math.PI * 0.025F * (float)(1 + Math.abs(0 - 2));
        this.tail2.rotateAngleY = MathHelper.cos(f2 * 0.3F + (float)1 * 0.15F * (float)Math.PI) * (float)Math.PI * 0.025F * (float)(1 + Math.abs(1 - 2));
        this.tail3.rotateAngleY = MathHelper.cos(f2 * 0.3F + (float)2 * 0.15F * (float)Math.PI) * (float)Math.PI * 0.025F * (float)(1 + Math.abs(1 - 2));
        this.tail4.rotateAngleY = MathHelper.cos(f2 * 0.3F + (float)3 * 0.15F * (float)Math.PI) * (float)Math.PI * 0.025F * (float)(1 + Math.abs(1 - 2));
		super.setRotationAngles(f, f1, f2, f3, f4, f5);	
	}
}
