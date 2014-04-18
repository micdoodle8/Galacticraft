package micdoodle8.mods.galacticraft.core.client.model;

import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.model.ModelVillager;
import net.minecraft.entity.Entity;

/**
 * GCCoreModelVillager.java
 * 
 * This file is part of the Galacticraft project
 * 
 * @author micdoodle8
 * @license Lesser GNU Public License v3 (http://www.gnu.org/licenses/lgpl.html)
 * 
 */
public class GCCoreModelVillager extends ModelVillager
{
	public ModelRenderer brain;

	public GCCoreModelVillager(float par1)
	{
		this(par1, 0.0F, 64, 64);
	}

	public GCCoreModelVillager(float par1, float par2, int par3, int par4)
	{
		super(par1, par2, 0, 0);
		this.villagerHead = new ModelRenderer(this).setTextureSize(par3, par4);
		this.villagerHead.setRotationPoint(0.0F, 0.0F + par2, 0.0F);
		this.villagerHead.setTextureOffset(0, 0).addBox(-4.0F, -10.0F, -4.0F, 8, 10, 8, par1 + 0.001F);
		this.villagerNose = new ModelRenderer(this).setTextureSize(par3, par4);
		this.villagerNose.setRotationPoint(0.0F, par2 - 2.0F, 0.0F);
		this.villagerNose.setTextureOffset(24, 0).addBox(-1.0F, -1.0F, -6.0F, 2, 4, 2, par1 + 0.002F);
		this.villagerHead.addChild(this.villagerNose);
		this.villagerBody = new ModelRenderer(this).setTextureSize(par3, par4);
		this.villagerBody.setRotationPoint(0.0F, 0.0F + par2, 0.0F);
		this.villagerBody.setTextureOffset(16, 20).addBox(-4.0F, 0.0F, -3.0F, 8, 12, 6, par1 + 0.003F);
		this.villagerBody.setTextureOffset(0, 38).addBox(-4.0F, 0.0F, -3.0F, 8, 18, 6, par1 + 0.5F + 0.004F);
		this.villagerArms = new ModelRenderer(this).setTextureSize(par3, par4);
		this.villagerArms.setRotationPoint(0.0F, 0.0F + par2 + 2.0F, 0.0F);
		this.villagerArms.setTextureOffset(44, 22).addBox(-8.0F, -2.0F, -2.0F, 4, 8, 4, par1 + 0.005F);
		this.villagerArms.setTextureOffset(44, 22).addBox(4.0F, -2.0F, -2.0F, 4, 8, 4, par1 + 0.0001F);
		this.villagerArms.setTextureOffset(40, 38).addBox(-4.0F, 2.0F, -2.0F, 8, 4, 4, par1 + 0.0004F);
		this.rightVillagerLeg = new ModelRenderer(this, 0, 22).setTextureSize(par3, par4);
		this.rightVillagerLeg.setRotationPoint(-2.0F, 12.0F + par2, 0.0F);
		this.rightVillagerLeg.addBox(-2.0F, 0.0F, -2.0F, 4, 12, 4, par1 + 0.0006F);
		this.leftVillagerLeg = new ModelRenderer(this, 0, 22).setTextureSize(par3, par4);
		this.leftVillagerLeg.mirror = true;
		this.leftVillagerLeg.setRotationPoint(2.0F, 12.0F + par2, 0.0F);
		this.leftVillagerLeg.addBox(-2.0F, 0.0F, -2.0F, 4, 12, 4, par1 + 0.0002F);
		this.brain = new ModelRenderer(this).setTextureSize(par3, par4);
		this.brain.setRotationPoint(0.0F, 0.0F + par2, 0.0F);
		this.brain.setTextureOffset(32, 0).addBox(-4.0F, -16.0F, -4.0F, 8, 8, 8, par1 + 0.5F);
	}

	@Override
	public void render(Entity par1Entity, float par2, float par3, float par4, float par5, float par6, float par7)
	{
		super.render(par1Entity, par2, par3, par4, par5, par6, par7);

		this.brain.render(par7);
	}

	@Override
	public void setRotationAngles(float par1, float par2, float par3, float par4, float par5, float par6, Entity par7Entity)
	{
		super.setRotationAngles(par1, par2, par3, par4, par5, par6, par7Entity);

		this.brain.rotateAngleX = this.villagerHead.rotateAngleX;
		this.brain.rotateAngleY = this.villagerHead.rotateAngleY;
		this.brain.rotateAngleZ = this.villagerHead.rotateAngleZ;
	}
}
