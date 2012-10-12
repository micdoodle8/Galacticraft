package micdoodle8.mods.galacticraft.mars.client;

import net.minecraft.src.Entity;
import net.minecraft.src.MathHelper;
import net.minecraft.src.ModelBase;
import net.minecraft.src.ModelRenderer;
import cpw.mods.fml.common.Side;
import cpw.mods.fml.common.asm.SideOnly;

/**
 * Copyright 2012, micdoodle8
 * 
 *  All rights reserved.
 *
 */
@SideOnly(Side.CLIENT)
public class GCModelCreeper extends ModelBase
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

    public GCModelCreeper()
    {
        this(0.0F);
    }

    public GCModelCreeper(float par1)
    {
    	textureWidth = 128;
		textureHeight = 64;
		leftOxygenTank = new ModelRenderer(this, 40, 20);
		leftOxygenTank.addBox(-1.5F, 0F, -1.5F, 3, 7, 3);
		leftOxygenTank.setRotationPoint(2F, 5F, 3.8F);
		leftOxygenTank.setTextureSize(128, 64);
		leftOxygenTank.mirror = true;
		setRotation(leftOxygenTank, 0F, 0F, 0F);
		rightOxygenTank = new ModelRenderer(this, 40, 20);
		rightOxygenTank.addBox(-1.5F, 0F, -1.5F, 3, 7, 3);
		rightOxygenTank.setRotationPoint(-2F, 5F, 3.8F);
		rightOxygenTank.setTextureSize(128, 64);
		rightOxygenTank.mirror = true;
		setRotation(rightOxygenTank, 0F, 0F, 0F);
		tubeRight2 = new ModelRenderer(this, 40, 30);
		tubeRight2.addBox(-0.5F, -0.5F, -0.5F, 1, 1, 1);
		tubeRight2.setRotationPoint(-2F, 5F, 6.8F);
		tubeRight2.setTextureSize(128, 64);
		tubeRight2.mirror = true;
		setRotation(tubeRight2, 0F, 0F, 0F);
		tubeLeft1 = new ModelRenderer(this, 40, 30);
		tubeLeft1.addBox(-0.5F, -0.5F, -0.5F, 1, 1, 1);
		tubeLeft1.setRotationPoint(2F, 6F, 5.8F);
		tubeLeft1.setTextureSize(128, 64);
		tubeLeft1.mirror = true;
		setRotation(tubeLeft1, 0F, 0F, 0F);
		tubeRight3 = new ModelRenderer(this, 40, 30);
		tubeRight3.addBox(-0.5F, -0.5F, -0.5F, 1, 1, 1);
		tubeRight3.setRotationPoint(-2F, 4F, 6.8F);
		tubeRight3.setTextureSize(128, 64);
		tubeRight3.mirror = true;
		setRotation(tubeRight3, 0F, 0F, 0F);
		tubeRight4 = new ModelRenderer(this, 40, 30);
		tubeRight4.addBox(-0.5F, -0.5F, -0.5F, 1, 1, 1);
		tubeRight4.setRotationPoint(-2F, 3F, 6.8F);
		tubeRight4.setTextureSize(128, 64);
		tubeRight4.mirror = true;
		setRotation(tubeRight4, 0F, 0F, 0F);
		tubeRight5 = new ModelRenderer(this, 40, 30);
		tubeRight5.addBox(-0.5F, -0.5F, -0.5F, 1, 1, 1);
		tubeRight5.setRotationPoint(-2F, 2F, 6.8F);
		tubeRight5.setTextureSize(128, 64);
		tubeRight5.mirror = true;
		setRotation(tubeRight5, 0F, 0F, 0F);
		tubeLeft6 = new ModelRenderer(this, 40, 30);
		tubeLeft6.addBox(-0.5F, -0.5F, -0.5F, 1, 1, 1);
		tubeLeft6.setRotationPoint(2F, 1F, 5.8F);
		tubeLeft6.setTextureSize(128, 64);
		tubeLeft6.mirror = true;
		setRotation(tubeLeft6, 0F, 0F, 0F);
		tubeRight7 = new ModelRenderer(this, 40, 30);
		tubeRight7.addBox(-0.5F, -0.5F, -0.5F, 1, 1, 1);
		tubeRight7.setRotationPoint(-2F, 0F, 4.8F);
		tubeRight7.setTextureSize(128, 64);
		tubeRight7.mirror = true;
		setRotation(tubeRight7, 0F, 0F, 0F);
		tubeRight1 = new ModelRenderer(this, 40, 30);
		tubeRight1.addBox(-0.5F, -0.5F, -0.5F, 1, 1, 1);
		tubeRight1.setRotationPoint(-2F, 6F, 5.8F);
		tubeRight1.setTextureSize(128, 64);
		tubeRight1.mirror = true;
		setRotation(tubeRight1, 0F, 0F, 0F);
		tubeLeft2 = new ModelRenderer(this, 40, 30);
		tubeLeft2.addBox(-0.5F, -0.5F, -0.5F, 1, 1, 1);
		tubeLeft2.setRotationPoint(2F, 5F, 6.8F);
		tubeLeft2.setTextureSize(128, 64);
		tubeLeft2.mirror = true;
		setRotation(tubeLeft2, 0F, 0F, 0F);
		tubeLeft3 = new ModelRenderer(this, 40, 30);
		tubeLeft3.addBox(-0.5F, -0.5F, -0.5F, 1, 1, 1);
		tubeLeft3.setRotationPoint(2F, 4F, 6.8F);
		tubeLeft3.setTextureSize(128, 64);
		tubeLeft3.mirror = true;
		setRotation(tubeLeft3, 0F, 0F, 0F);
		tubeLeft4 = new ModelRenderer(this, 40, 30);
		tubeLeft4.addBox(-0.5F, -0.5F, -0.5F, 1, 1, 1);
		tubeLeft4.setRotationPoint(2F, 3F, 6.8F);
		tubeLeft4.setTextureSize(128, 64);
		tubeLeft4.mirror = true;
		setRotation(tubeLeft4, 0F, 0F, 0F);
		tubeLeft5 = new ModelRenderer(this, 40, 30);
		tubeLeft5.addBox(-0.5F, -0.5F, -0.5F, 1, 1, 1);
		tubeLeft5.setRotationPoint(2F, 2F, 6.8F);
		tubeLeft5.setTextureSize(128, 64);
		tubeLeft5.mirror = true;
		setRotation(tubeLeft5, 0F, 0F, 0F);
		tubeLeft7 = new ModelRenderer(this, 40, 30);
		tubeLeft7.addBox(-0.5F, -0.5F, -0.5F, 1, 1, 1);
		tubeLeft7.setRotationPoint(2F, 0F, 4.8F);
		tubeLeft7.setTextureSize(128, 64);
		tubeLeft7.mirror = true;
		setRotation(tubeLeft7, 0F, 0F, 0F);
		tubeRight6 = new ModelRenderer(this, 40, 30);
		tubeRight6.addBox(-0.5F, -0.5F, -0.5F, 1, 1, 1);
		tubeRight6.setRotationPoint(-2F, 1F, 5.8F);
		tubeRight6.setTextureSize(128, 64);
		tubeRight6.mirror = true;
		setRotation(tubeRight6, 0F, 0F, 0F);
		tubeLeft8 = new ModelRenderer(this, 40, 30);
		tubeLeft8.addBox(0F, 0F, 0F, 1, 1, 1);
		tubeLeft8.setRotationPoint(0F, -2F, 0F);
		tubeLeft8.setTextureSize(128, 64);
		tubeLeft8.mirror = true;
		setRotation(tubeLeft8, 0F, 0F, 0F);
		oxygenMask = new ModelRenderer(this, 40, 0);
		oxygenMask.addBox(-5F, -9F, -5F, 10, 10, 10);
		oxygenMask.setRotationPoint(0F, 4F, 0F);
		oxygenMask.setTextureSize(128, 64);
		oxygenMask.mirror = true;
		setRotation(oxygenMask, 0F, 0F, 0F);

        byte var2 = 4;
        this.head = new ModelRenderer(this, 0, 0);
        this.head.addBox(-4.0F, -8.0F, -4.0F, 8, 8, 8, par1);
        this.head.setRotationPoint(0.0F, (float)var2, 0.0F);
        head.setTextureSize(128, 64);
        this.field_78133_b = new ModelRenderer(this, 32, 0);
        this.field_78133_b.addBox(-4.0F, -8.0F, -4.0F, 8, 8, 8, par1 + 0.5F);
        this.field_78133_b.setRotationPoint(0.0F, (float)var2, 0.0F);
        field_78133_b.setTextureSize(128, 64);
        this.body = new ModelRenderer(this, 16, 16);
        this.body.addBox(-4.0F, 0.0F, -2.0F, 8, 12, 4, par1);
        this.body.setRotationPoint(0.0F, (float)var2, 0.0F);
        body.setTextureSize(128, 64);
        this.leg1 = new ModelRenderer(this, 0, 16);
        this.leg1.addBox(-2.0F, 0.0F, -2.0F, 4, 6, 4, par1);
        this.leg1.setRotationPoint(-2.0F, (float)(12 + var2), 4.0F);
        leg1.setTextureSize(128, 64);
        this.leg2 = new ModelRenderer(this, 0, 16);
        this.leg2.addBox(-2.0F, 0.0F, -2.0F, 4, 6, 4, par1);
        this.leg2.setRotationPoint(2.0F, (float)(12 + var2), 4.0F);
        leg2.setTextureSize(128, 64);
        this.leg3 = new ModelRenderer(this, 0, 16);
        this.leg3.addBox(-2.0F, 0.0F, -2.0F, 4, 6, 4, par1);
        this.leg3.setRotationPoint(-2.0F, (float)(12 + var2), -4.0F);
        leg3.setTextureSize(128, 64);
        this.leg4 = new ModelRenderer(this, 0, 16);
        this.leg4.addBox(-2.0F, 0.0F, -2.0F, 4, 6, 4, par1);
        this.leg4.setRotationPoint(2.0F, (float)(12 + var2), -4.0F);
        leg4.setTextureSize(128, 64);
    }
	
	private void setRotation(ModelRenderer model, float x, float y, float z)
	{
		model.rotateAngleX = x;
		model.rotateAngleY = y;
		model.rotateAngleZ = z;
	}

    public void render(Entity par1Entity, float par2, float par3, float par4, float par5, float par6, float par7)
    {
        this.setRotationAngles(par2, par3, par4, par5, par6, par7);
		leftOxygenTank.render(par7);
		rightOxygenTank.render(par7);
		tubeRight2.render(par7);
		tubeLeft1.render(par7);
		tubeRight3.render(par7);
		tubeRight4.render(par7);
		tubeRight5.render(par7);
		tubeLeft6.render(par7);
		tubeRight7.render(par7);
		tubeRight1.render(par7);
		tubeLeft2.render(par7);
		tubeLeft3.render(par7);
		tubeLeft4.render(par7);
		tubeLeft5.render(par7);
		tubeLeft7.render(par7);
		tubeRight6.render(par7);
		tubeLeft8.render(par7);
		oxygenMask.render(par7);
        this.head.render(par7);
        this.body.render(par7);
        this.leg1.render(par7);
        this.leg2.render(par7);
        this.leg3.render(par7);
        this.leg4.render(par7);
    }

    public void setRotationAngles(float par1, float par2, float par3, float par4, float par5, float par6)
    {
        this.oxygenMask.rotateAngleY = par4 / (180F / (float)Math.PI);
        this.oxygenMask.rotateAngleX = par5 / (180F / (float)Math.PI);
        this.head.rotateAngleY = par4 / (180F / (float)Math.PI);
        this.head.rotateAngleX = par5 / (180F / (float)Math.PI);
        this.leg1.rotateAngleX = MathHelper.cos(par1 * 0.6662F) * 1.4F * par2;
        this.leg2.rotateAngleX = MathHelper.cos(par1 * 0.6662F + (float)Math.PI) * 1.4F * par2;
        this.leg3.rotateAngleX = MathHelper.cos(par1 * 0.6662F + (float)Math.PI) * 1.4F * par2;
        this.leg4.rotateAngleX = MathHelper.cos(par1 * 0.6662F) * 1.4F * par2;
    }
}
