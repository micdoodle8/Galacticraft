package micdoodle8.mods.galacticraft.core.client.model;

import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.model.ModelVillager;
import net.minecraft.entity.Entity;

public class GCCoreModelVillager extends ModelVillager
{
    public ModelRenderer brain;

    public GCCoreModelVillager(float par1)
    {
        this(par1, 0.0F, 64, 64);
    }

    public GCCoreModelVillager(float par1, float par2, int par3, int par4)
    {
		super(par1);

        this.brain = (new ModelRenderer(this)).setTextureSize(par3, par4);
        this.brain.setRotationPoint(0.0F, 0.0F + par2, 0.0F);
        this.brain.setTextureOffset(32, 0).addBox(-4.0F, -16.0F, -4.0F, 8, 8, 8, 0.5F);
//		this.villagerHead.addChild(brain);
	}
    
    public void render(Entity par1Entity, float par2, float par3, float par4, float par5, float par6, float par7)
    {
    	super.render(par1Entity, par2, par3, par4, par5, par6, par7);
    	
    	this.brain.render(par7);
    }

    public void setRotationAngles(float par1, float par2, float par3, float par4, float par5, float par6, Entity par7Entity)
    {
    	super.setRotationAngles(par1, par2, par3, par4, par5, par6, par7Entity);
    	
    	this.brain.rotateAngleX = this.villagerHead.rotateAngleX;
    	this.brain.rotateAngleY = this.villagerHead.rotateAngleY;
    	this.brain.rotateAngleZ = this.villagerHead.rotateAngleZ;
    }
}
