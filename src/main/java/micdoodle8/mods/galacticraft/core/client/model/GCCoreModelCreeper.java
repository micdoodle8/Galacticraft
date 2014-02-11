package micdoodle8.mods.galacticraft.core.client.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.util.MathHelper;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

/**
 * GCCoreModelCreeper.java
 * 
 * This file is part of the Galacticraft project
 * 
 * @author micdoodle8
 * @license Lesser GNU Public License v3 (http://www.gnu.org/licenses/lgpl.html)
 * 
 */
@SideOnly(Side.CLIENT)
public class GCCoreModelCreeper extends ModelBase
{
	ModelRenderer leftOxygenTank;
	ModelRenderer rightOxygenTank;
	ModelRenderer tubeRight2;
	ModelRenderer tubeLeft1;
	ModelRenderer tubeRight3;
	ModelRenderer tubeRight4;
	ModelRenderer tubeRight5;
	ModelRenderer tubeLeft6;
	ModelRenderer tubeRight7;
	ModelRenderer tubeRight1;
	ModelRenderer tubeLeft2;
	ModelRenderer tubeLeft3;
	ModelRenderer tubeLeft4;
	ModelRenderer tubeLeft5;
	ModelRenderer tubeLeft7;
	ModelRenderer tubeRight6;
	ModelRenderer tubeLeft8;
	ModelRenderer oxygenMask;
	public ModelRenderer head;
	public ModelRenderer field_78133_b;
	public ModelRenderer body;
	public ModelRenderer leg1;
	public ModelRenderer leg2;
	public ModelRenderer leg3;
	public ModelRenderer leg4;

	public GCCoreModelCreeper()
	{
		this(0.0F);
	}

	public GCCoreModelCreeper(float par1)
	{
		this.textureWidth = 128;
		this.textureHeight = 64;
		this.leftOxygenTank = new ModelRenderer(this, 40, 20);
		this.leftOxygenTank.addBox(-1.5F, 0F, -1.5F, 3, 7, 3, par1);
		this.leftOxygenTank.setRotationPoint(2F, 5F, 3.8F);
		this.leftOxygenTank.setTextureSize(128, 64);
		this.leftOxygenTank.mirror = true;
		this.setRotation(this.leftOxygenTank, 0F, 0F, 0F);
		this.rightOxygenTank = new ModelRenderer(this, 40, 20);
		this.rightOxygenTank.addBox(-1.5F, 0F, -1.5F, 3, 7, 3, par1);
		this.rightOxygenTank.setRotationPoint(-2F, 5F, 3.8F);
		this.rightOxygenTank.setTextureSize(128, 64);
		this.rightOxygenTank.mirror = true;
		this.setRotation(this.rightOxygenTank, 0F, 0F, 0F);
		this.tubeRight2 = new ModelRenderer(this, 40, 30);
		this.tubeRight2.addBox(-0.5F, -0.5F, -0.5F, 1, 1, 1, par1);
		this.tubeRight2.setRotationPoint(-2F, 5F, 6.8F);
		this.tubeRight2.setTextureSize(128, 64);
		this.tubeRight2.mirror = true;
		this.setRotation(this.tubeRight2, 0F, 0F, 0F);
		this.tubeLeft1 = new ModelRenderer(this, 40, 30);
		this.tubeLeft1.addBox(-0.5F, -0.5F, -0.5F, 1, 1, 1, par1);
		this.tubeLeft1.setRotationPoint(2F, 6F, 5.8F);
		this.tubeLeft1.setTextureSize(128, 64);
		this.tubeLeft1.mirror = true;
		this.setRotation(this.tubeLeft1, 0F, 0F, 0F);
		this.tubeRight3 = new ModelRenderer(this, 40, 30);
		this.tubeRight3.addBox(-0.5F, -0.5F, -0.5F, 1, 1, 1, par1);
		this.tubeRight3.setRotationPoint(-2F, 4F, 6.8F);
		this.tubeRight3.setTextureSize(128, 64);
		this.tubeRight3.mirror = true;
		this.setRotation(this.tubeRight3, 0F, 0F, 0F);
		this.tubeRight4 = new ModelRenderer(this, 40, 30);
		this.tubeRight4.addBox(-0.5F, -0.5F, -0.5F, 1, 1, 1, par1);
		this.tubeRight4.setRotationPoint(-2F, 3F, 6.8F);
		this.tubeRight4.setTextureSize(128, 64);
		this.tubeRight4.mirror = true;
		this.setRotation(this.tubeRight4, 0F, 0F, 0F);
		this.tubeRight5 = new ModelRenderer(this, 40, 30);
		this.tubeRight5.addBox(-0.5F, -0.5F, -0.5F, 1, 1, 1, par1);
		this.tubeRight5.setRotationPoint(-2F, 2F, 6.8F);
		this.tubeRight5.setTextureSize(128, 64);
		this.tubeRight5.mirror = true;
		this.setRotation(this.tubeRight5, 0F, 0F, 0F);
		this.tubeLeft6 = new ModelRenderer(this, 40, 30);
		this.tubeLeft6.addBox(-0.5F, -0.5F, -0.5F, 1, 1, 1, par1);
		this.tubeLeft6.setRotationPoint(2F, 1F, 5.8F);
		this.tubeLeft6.setTextureSize(128, 64);
		this.tubeLeft6.mirror = true;
		this.setRotation(this.tubeLeft6, 0F, 0F, 0F);
		this.tubeRight7 = new ModelRenderer(this, 40, 30);
		this.tubeRight7.addBox(-0.5F, -0.5F, -0.5F, 1, 1, 1, par1);
		this.tubeRight7.setRotationPoint(-2F, 0F, 4.8F);
		this.tubeRight7.setTextureSize(128, 64);
		this.tubeRight7.mirror = true;
		this.setRotation(this.tubeRight7, 0F, 0F, 0F);
		this.tubeRight1 = new ModelRenderer(this, 40, 30);
		this.tubeRight1.addBox(-0.5F, -0.5F, -0.5F, 1, 1, 1, par1);
		this.tubeRight1.setRotationPoint(-2F, 6F, 5.8F);
		this.tubeRight1.setTextureSize(128, 64);
		this.tubeRight1.mirror = true;
		this.setRotation(this.tubeRight1, 0F, 0F, 0F);
		this.tubeLeft2 = new ModelRenderer(this, 40, 30);
		this.tubeLeft2.addBox(-0.5F, -0.5F, -0.5F, 1, 1, 1, par1);
		this.tubeLeft2.setRotationPoint(2F, 5F, 6.8F);
		this.tubeLeft2.setTextureSize(128, 64);
		this.tubeLeft2.mirror = true;
		this.setRotation(this.tubeLeft2, 0F, 0F, 0F);
		this.tubeLeft3 = new ModelRenderer(this, 40, 30);
		this.tubeLeft3.addBox(-0.5F, -0.5F, -0.5F, 1, 1, 1, par1);
		this.tubeLeft3.setRotationPoint(2F, 4F, 6.8F);
		this.tubeLeft3.setTextureSize(128, 64);
		this.tubeLeft3.mirror = true;
		this.setRotation(this.tubeLeft3, 0F, 0F, 0F);
		this.tubeLeft4 = new ModelRenderer(this, 40, 30);
		this.tubeLeft4.addBox(-0.5F, -0.5F, -0.5F, 1, 1, 1, par1);
		this.tubeLeft4.setRotationPoint(2F, 3F, 6.8F);
		this.tubeLeft4.setTextureSize(128, 64);
		this.tubeLeft4.mirror = true;
		this.setRotation(this.tubeLeft4, 0F, 0F, 0F);
		this.tubeLeft5 = new ModelRenderer(this, 40, 30);
		this.tubeLeft5.addBox(-0.5F, -0.5F, -0.5F, 1, 1, 1, par1);
		this.tubeLeft5.setRotationPoint(2F, 2F, 6.8F);
		this.tubeLeft5.setTextureSize(128, 64);
		this.tubeLeft5.mirror = true;
		this.setRotation(this.tubeLeft5, 0F, 0F, 0F);
		this.tubeLeft7 = new ModelRenderer(this, 40, 30);
		this.tubeLeft7.addBox(-0.5F, -0.5F, -0.5F, 1, 1, 1, par1);
		this.tubeLeft7.setRotationPoint(2F, 0F, 4.8F);
		this.tubeLeft7.setTextureSize(128, 64);
		this.tubeLeft7.mirror = true;
		this.setRotation(this.tubeLeft7, 0F, 0F, 0F);
		this.tubeRight6 = new ModelRenderer(this, 40, 30);
		this.tubeRight6.addBox(-0.5F, -0.5F, -0.5F, 1, 1, 1, par1);
		this.tubeRight6.setRotationPoint(-2F, 1F, 5.8F);
		this.tubeRight6.setTextureSize(128, 64);
		this.tubeRight6.mirror = true;
		this.setRotation(this.tubeRight6, 0F, 0F, 0F);
		this.tubeLeft8 = new ModelRenderer(this, 40, 30);
		this.tubeLeft8.addBox(0F, 0F, 0F, 1, 1, 1, par1);
		this.tubeLeft8.setRotationPoint(0F, -2F, 0F);
		this.tubeLeft8.setTextureSize(128, 64);
		this.tubeLeft8.mirror = true;
		this.setRotation(this.tubeLeft8, 0F, 0F, 0F);
		this.oxygenMask = new ModelRenderer(this, 40, 0);
		this.oxygenMask.addBox(-5F, -9F, -5F, 10, 10, 10, par1);
		this.oxygenMask.setRotationPoint(0F, 4F, 0F);
		this.oxygenMask.setTextureSize(128, 64);
		this.oxygenMask.mirror = true;
		this.setRotation(this.oxygenMask, 0F, 0F, 0F);

		final byte var2 = 4;
		this.head = new ModelRenderer(this, 0, 0);
		this.head.addBox(-4.0F, -8.0F, -4.0F, 8, 8, 8, par1);
		this.head.setRotationPoint(0.0F, var2, 0.0F);
		this.head.setTextureSize(128, 64);
		this.field_78133_b = new ModelRenderer(this, 32, 0);
		this.field_78133_b.addBox(-4.0F, -8.0F, -4.0F, 8, 8, 8, par1 + 0.5F);
		this.field_78133_b.setRotationPoint(0.0F, var2, 0.0F);
		this.field_78133_b.setTextureSize(128, 64);
		this.body = new ModelRenderer(this, 16, 16);
		this.body.addBox(-4.0F, 0.0F, -2.0F, 8, 12, 4, par1);
		this.body.setRotationPoint(0.0F, var2, 0.0F);
		this.body.setTextureSize(128, 64);
		this.leg1 = new ModelRenderer(this, 0, 16);
		this.leg1.addBox(-2.0F, 0.0F, -2.0F, 4, 6, 4, par1);
		this.leg1.setRotationPoint(-2.0F, 12 + var2, 4.0F);
		this.leg1.setTextureSize(128, 64);
		this.leg2 = new ModelRenderer(this, 0, 16);
		this.leg2.addBox(-2.0F, 0.0F, -2.0F, 4, 6, 4, par1);
		this.leg2.setRotationPoint(2.0F, 12 + var2, 4.0F);
		this.leg2.setTextureSize(128, 64);
		this.leg3 = new ModelRenderer(this, 0, 16);
		this.leg3.addBox(-2.0F, 0.0F, -2.0F, 4, 6, 4, par1);
		this.leg3.setRotationPoint(-2.0F, 12 + var2, -4.0F);
		this.leg3.setTextureSize(128, 64);
		this.leg4 = new ModelRenderer(this, 0, 16);
		this.leg4.addBox(-2.0F, 0.0F, -2.0F, 4, 6, 4, par1);
		this.leg4.setRotationPoint(2.0F, 12 + var2, -4.0F);
		this.leg4.setTextureSize(128, 64);
	}

	private void setRotation(ModelRenderer model, float x, float y, float z)
	{
		model.rotateAngleX = x;
		model.rotateAngleY = y;
		model.rotateAngleZ = z;
	}

	@Override
	public void render(Entity par1Entity, float par2, float par3, float par4, float par5, float par6, float par7)
	{
		this.setRotationAngles(par2, par3, par4, par5, par6, par7);
		this.leftOxygenTank.render(par7);
		this.rightOxygenTank.render(par7);
		this.tubeRight2.render(par7);
		this.tubeLeft1.render(par7);
		this.tubeRight3.render(par7);
		this.tubeRight4.render(par7);
		this.tubeRight5.render(par7);
		this.tubeLeft6.render(par7);
		this.tubeRight7.render(par7);
		this.tubeRight1.render(par7);
		this.tubeLeft2.render(par7);
		this.tubeLeft3.render(par7);
		this.tubeLeft4.render(par7);
		this.tubeLeft5.render(par7);
		this.tubeLeft7.render(par7);
		this.tubeRight6.render(par7);
		this.tubeLeft8.render(par7);
		this.oxygenMask.render(par7);
		this.head.render(par7);
		this.body.render(par7);
		this.leg1.render(par7);
		this.leg2.render(par7);
		this.leg3.render(par7);
		this.leg4.render(par7);
	}

	public void setRotationAngles(float par1, float par2, float par3, float par4, float par5, float par6)
	{
		this.oxygenMask.rotateAngleY = par4 / (180F / (float) Math.PI);
		this.oxygenMask.rotateAngleX = par5 / (180F / (float) Math.PI);
		this.head.rotateAngleY = par4 / (180F / (float) Math.PI);
		this.head.rotateAngleX = par5 / (180F / (float) Math.PI);
		this.leg1.rotateAngleX = MathHelper.cos(par1 * 0.6662F) * 1.4F * par2;
		this.leg2.rotateAngleX = MathHelper.cos(par1 * 0.6662F + (float) Math.PI) * 1.4F * par2;
		this.leg3.rotateAngleX = MathHelper.cos(par1 * 0.6662F + (float) Math.PI) * 1.4F * par2;
		this.leg4.rotateAngleX = MathHelper.cos(par1 * 0.6662F) * 1.4F * par2;
	}
}
